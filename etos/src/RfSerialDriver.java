package com.skhynixsystemic.etos.rf;

import java.nio.ByteBuffer;

import org.springframework.beans.factory.annotation.Autowired;

import kr.co.twise.etos.protocol.serial.AbstractSerialDriver;
import kr.co.twise.etos.protocol.serial.Secs1Message;
import kr.co.twise.etos.protocol.serial.SerialException;
import kr.co.twise.etos.protocol.serial.TimeoutException;
import kr.co.twise.etos.utils.HexaUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * SystemIC에 적용된 RF Reader기는 두가지 모드가 있다.
 * VBM-FT(VerBose Mode), CTM-FT (ConTinuout Mode)
 * 
 * VBM-FT 모드 : 
 *   RF-READ RCMD가 내려가야 CID-Report가 올라온다.
 *   설비시나리오는, Port에서 LoadComplete를 보고 하면, EAP가 RF-READ RCMD를 보낸다.
 *   
 * 
 * CTM-FT 모드 :
 *   Foup이 RF tagging 되면, CID-Report가 올라온다.
 *   당 설비는 Dummy port이므로, LoadComplete 보고를 할 수 없다. 따라서 RF tagging 되면, CID-Report를 하게된다.
 *   
 * EAP 시나리오는 S1F1 > RF Mode change RCMD > 각 모드별 동작 으로 되어있다.
 * 
 * VBM-FT,  CTM-FT 는 각각 정의된 serial message를 두번 내린 후, 최종 063031 을 받으면 s2f42, s6f11 host 보고
 * 
 * VBM-FT 모드에서 RF_READ를 보내야, 실제 설비에서 rf를 읽고 그 결과를 보고 한다.
 */
@Slf4j
public class RfSerialDriver extends AbstractSerialDriver {
	
	private long systemByte = -1;
	private RCMD currentRCMD = null;
	private ModeSendState currentSendState = ModeSendState.NONE;
	
	@Autowired RFSerialPacketResolver rfSerialPacketResolver;
	@Autowired RFSecsMessageResolver rfSecsMessageResolver;
	
	public RfSerialDriver() {
		super.checksumHelper = new RfSecs1Checksum();
	}

	@Override
	public void run() {

		while (isRun()) {
			try {
				final Byte oneByte = readByte(serialInfo.getT1Timeout());

				if (oneByte == null) {
					// HSMS 에서 온 것 체크
					doSend();
				}

				
				if (oneByte != null) {
//        	log.info("ETOS < {} :: {}", serialInfo.getPort(), HexaUtils.byteArray2HexString(oneByte));
        	
        	// ack
        	if ( oneByte == 0x06 ) {
        		resolveAck();
        	}
        	// nak
        	else if (oneByte == 0x15) {
        		resolveNak();
        	}
        	// cid report fix
        	else if ( oneByte == 0x02 ) {
        		doRecvCassetteIdReport();
        	}
        	else {
        		throw new SerialException("Unknown received data. " + HexaUtils.byte2HexString(oneByte));
        	}
        }
			} catch (Exception e) {
 				log.error(e.getMessage(), e);
			}
		}
	}

	private void resolveAck() {
		
		readData(2);
		
		if (currentRCMD == RCMD.RF_READ) {
			// s2f42 보고 ~~~
			rfSecsMessageResolver.sendRcmdReply(systemByte, true);
			
			// 2021 01 06 추가
			sleep(100);
			rfSecsMessageResolver.sendCidReadReport(READ_PORT, "");
			
		}
		else if ( currentRCMD == RCMD.VBM_FT ||  currentRCMD == RCMD.CTM_FT) {
			doRecvModeChangeReport();
		}
	}
	
	private void resolveNak() {
	  
	  readData(4);
	  
	  if (currentRCMD == null) {
	    return;
	  }
		
		
		rfSecsMessageResolver.sendRcmdReply(systemByte, false);
		
		systemByte = -1;
		currentRCMD = null;
		currentSendState = ModeSendState.NONE;
	}

	private void doSend() {

		final Secs1Message secs1Message = queue4eap2machine.peek();
		
		if (secs1Message == null) {
			return;
		}
		

		if (!"S2F41".equals(secs1Message.SxFx())) {
			log.error("this msg is not RCMD. so {} is not support.", secs1Message.SxFx());
			// queue에서 삭제 해 준다.
			queue4eap2machine.poll();
			return;
		}
		
		// 
		systemByte = secs1Message.systemByte();
		
		try {
			// smd파일에 정의가 안되서 exception 발생
			// smd파일에 잘못 작성 되어서 exception 발생
			currentRCMD = RCMD.of(secs1Message.trxSecsIdItemValue(1));
		} catch (Exception e) {
			log.error("Illegal rcmd value. {}", e.getMessage(), e);
			throw new SerialException(e);
		} finally {
			// queue에서 삭제 해 준다.
			queue4eap2machine.poll();
		}
		
		try {
			if ( currentRCMD == RCMD.RF_READ ) {
				etos2RF_READ_Command(secs1Message);
			}
			else {
				
				try {
					sendSerialData( rfSerialPacketResolver.resolveCurrentModeChangeRCMD(currentRCMD, currentSendState) );
					this.currentSendState = ModeSendState.FIRST_STATE;
				} catch (Exception e) {
					// S2F42 nak를 보낸다.
					this.currentSendState = ModeSendState.NONE;
					rfSecsMessageResolver.sendRcmdReply(systemByte, false);
					throw new SerialException(e);
				}
			}
		} catch (Exception e) {
			throw new SerialException(e);
		} finally {
			// queue에서 삭제 해 준다.
			queue4eap2machine.poll();
		}
	}
	
	// 2021 01 06 추가
	String READ_PORT = "";
	
	private void etos2RF_READ_Command(Secs1Message secs1Message) {

		// READ command가 오면 무조건 NONE 상태로 바꾼다.
		this.currentSendState = ModeSendState.NONE;
		
		try {
			// smd파일에 정의가 안되서 exception 발생
			// smd파일에 잘못 작성 되어서 exception 발생
			
			//
			READ_PORT = secs1Message.trxSecsIdItemValue(5);
			byte[] serialMessage = rfSerialPacketResolver.resolve_RF_READ_RCMD( READ_PORT, checksumHelper );
			sendSerialData(serialMessage);
			
		} catch (Exception e) {
			
			// s2f42 보고 ~~~
			rfSecsMessageResolver.sendRcmdReply(systemByte, false);
			throw new SerialException(e);
		}
	}
	
	/**
	 * 모드 변경은 메시지 2개를 쏘아야 하고, ... 각각의 응답을 받아야 한다.
	 * 
	 * @throws SerialException
	 */
	private void doRecvModeChangeReport() throws SerialException {

		// **************************
		// 아래 로직은 VBM_FT or CTM_FT 일 경우에만 해당 
		
		if (this.currentSendState == ModeSendState.FIRST_STATE) {
			byte[] secondMessage = rfSerialPacketResolver.resolveCurrentModeChangeRCMD(currentRCMD, currentSendState);
			
			try {
				sendSerialData(secondMessage);
				this.currentSendState = ModeSendState.SECOND_STATE;
			} catch (Exception e) {
				this.currentSendState = ModeSendState.NONE;
				
				// s2f42 보고 ~~~
				rfSecsMessageResolver.sendRcmdReply(systemByte, false);
				throw new SerialException(e);
			}
			
		} else if (this.currentSendState == ModeSendState.SECOND_STATE) {
			this.currentSendState = ModeSendState.NONE;
			rfSecsMessageResolver.sendModeChangeReport(systemByte, currentRCMD);
		}
	}
	
	private void doRecvCassetteIdReport() throws SerialException {
		//
		final byte[] totalDatas = doReadSerialCassetteReportPacket();
		log.info("ETOS < {} :: {}", serialInfo.getPort(), HexaUtils.byteArray2HexString(totalDatas));
		
		if ( checksumHelper.validate(totalDatas) ) {
			final String port = rfSerialPacketResolver.extractPort(totalDatas);
			final String cid = rfSerialPacketResolver.extractCasstteId(totalDatas);
			
			// 2021 01 06 추가
			rfSecsMessageResolver.sendRcmdReply(systemByte, true);
			sleep(100);
			
			rfSecsMessageResolver.sendCidReadReport(port, cid);
			
			// 2021 01 06 추가
			READ_PORT = "";
			currentRCMD = null;
			currentSendState = ModeSendState.NONE;
		}
		else {
			throw new SerialException("Checksum exception");
		}
	}
	
	byte[] doReadSerialCassetteReportPacket() {
		
		// 패킷 구조
			// 0x02 로 시작되며 이것은 이미 읽음.
			// channel 2 byte :: normal foup(30 31) / reticle foup(38 31)
			// port = 2 byte
		final byte[] channel = readData(2);
		final byte[] port = readData(2);

		// 고정 값으로 예상되지만, 0x03 을 찾아야 한다.
		final ByteBuffer buffer = ByteBuffer.allocate(200);
		buffer.put((byte) 0x02); // header 고정값
		buffer.put(channel);
		buffer.put(port);
		
		while (true) {
			Byte oneByte = readByte(serialInfo.getT1Timeout());
			if (oneByte == null) {
				throw new TimeoutException("T1 timeout");
			} else {
				// ETX 이후 체크 썸 2 바이트를 마저 읽어야 함.
				if (oneByte == 0x03) { // 통신 프로토콜 값으로 정의 된 사항임
					
					buffer.put(oneByte);
					
					byte[] checksum = readData(2);
					if (checksum != null && checksum.length == 2) {
						buffer.put(checksum);
						break;
					}
				} else {
					buffer.put(oneByte);
				}
			}
		}
		buffer.flip();
		//
		final byte[] totalDatas = new byte[buffer.remaining()];
		buffer.get(totalDatas);
		buffer.clear();
		return totalDatas;
	}
}
