# LINUX 각 설정, 명령어

Ubuntu version : 18.04.5 LTS (GNU/Linux 4.15.0-163-generic x86_64)

## shutdown or reboot 

```sh
sudo shutdown -h now

sudo reboot
```
           
## linux serial port 확인

```sh
dmesg | grep tty

```

## serial port 속성 확인

```sh
sudo stty –F /dev/ttyS0
```

## serial port 속도 변경

```sh
sudo stty 115200 < /dev/ttyS0
```

# 개인 PC에서 (VirtualBOX) TEST용, 가상 serial port 생성 및 활용

socat util 사용.

```sh
apt-get install socat
```

socat 가상 serial port 생성은 아래와 같은 명령어를 실행한다.

반드시 virtual box에서 실행한 ubuntu 터미널에서 실행 해야 한다.

```sh
sudo ln -s /dev/pts/1 /dev/ttyUSB01

sudo ln -s /dev/pts/2 /dev/ttyUSB02

socat -d -d pty,raw,echo=0 pty,raw,echo=0
```

실행 결과가 아래와 같이 나왔다면,

```sh
2021/12/20 11:37:45 socat[14159] N PTY is /dev/pts/1
2021/12/20 11:37:45 socat[14159] N PTY is /dev/pts/2
2021/12/20 11:37:45 socat[14159] N starting data transfer loop with FDs [5,5] and [7,7]
```

아래 설명한 가상 serial message를 보낼때, `/dev/pts/2` 로 보내야 정상 동작 한다.

```sh
echo -e -n "\x06\x03\x31" > /dev/pts/2
```


java application은 아래 port를 사용한다.

etos driver는 /dev/ttyUSB01
virtual eq 는 /dev/ttyUSB02

## etos driver 실행 환경 설정 및 활용 명령어

[주의] demo 환경에서는 수동으로 virtual serial port는 생성해서 테스트를 하므로,

`sudo systemctl enable EAP01.service` 명령어를 사용하지 않는다.

수동으로 start / stop 하여 테스트 한다.


etos driver는 ubuntu에 service로 등록 하여 사용한다.

etos driver는 port별로 각각 process 방식으로 운영한다.

따라서, HW 제원을 확인하여 N개의 service를 등록 해야 한다.

/etc/systemd/system 하위에 EAP${number}.service 형태로 등록한다.

ex) EAP01.service, EAP02.service

또한, 향후 etos 상태정보 관리를 위해 h2database(server mode)를 사용한다.

h2database 또한 service 등록 해야 한다.

service 등록 명령어는 아래와 같다.

```sh
sudo systemctl daemon-reload
sudo systemctl enable EAP01.service
sudo systemctl start EAP01.service
```

service alive 확인은 아래 명령어로 확인

```sh
service EAP01 status
```

추후 아래와 같은 명령어를 가지고 상태 모니터링이 가능하다.

```sh
systemctl list-units --type=service --all --state=running | grep EAP | awk '{print $0}'

systemctl list-units --type=service --all --state=running | grep EAP | awk '{print $5}'
```


service를 중단 하기 위해서는 아래와 같은 명령어를 사용한다.

절대로 `kill -9 ${pid}` or `kill -KILL ${pid}` 는 사용하지 않는다.

```sh
service EAP01 stop
```

```sh
ps -eo pid,lstart,cmd | grep app.name=EAP | grep -v "grep" | awk '{print $1}'

kill -TERM ${pid}

or

kill -15 ${pid}
```

## h2database service 파일

```sh
[Unit]
Description=h2database service
After=network.target

[Service]
Type=simple
User=${user name}
ExecStart=/home/${user name}/h2/bin/h2.sh

[Install]
WantedBy=multi-user.target
```

h2database는 `https://www.h2database.com/html/main.html` 에서 다운 받을 수 있다. version은 `Version 2.0.202 (2021-11-25)` 이다.

아래는 h2.sh

web 접속 url은 `http://192.168.246.4:8082/` 이다.

```sh
#!/bin/sh
dir=$(dirname "$0")
/usr/lib/jvm/openjdk-8u282-b08/bin/java -cp "$dir/h2-2.0.202.jar:$H2DRIVERS:$CLASSPATH" org.h2.tools.Console -webAllowOthers -tcpAllowOthers "$@"
```

```sh

# ACK
echo -e -n "\x06\x30\x31" > /dev/pts/2

echo -e -n "\x06\x30\x31" > /dev/ttyS0

# NAK
echo -e -n "\x15\x30\x31\x01\x01" > /dev/pts/2

echo -e -n "\x15\x30\x31\x01\x01" > /dev/ttyS0

# Cassette id report
echo -e -n "\x02\x38\x31\x30\x31\x34\x43\x34\x41\x33\x33\x33\x31\x34\x31\x34\x31\x35\x30\x35\x32\x35\x37\x33\x30\x34\x31\x33\x31\x03\x42\x33" > /dev/pts/2



echo -e -n "\x02\x38\x31\x30\x31\x34\x43\x34\x41\x33\x33\x33\x31\x34\x31\x34\x31\x35\x30\x35\x32\x35\x37\x33\x30\x34\x31\x33\x31\x03\x42\x33" > /dev/ttyS0


```


```
~/etos
  /config
    application.properties
    etos.smd
    logback.xml
    spring-appcontext.xml
    spring-default-appcontext.xml
    spring-rf-appcontext.xml
  etos.jar
  /log
  start.sh

  	
```
