# 작업절차


find / -name 찾는파일명

vi -b 파일명

:%s/^M//g

^M : ctl + v + m


## 1. time zone 등록

tzselect

timedatectl list-timezones

sudo timedatectl set-timezone Asia/Seoul


## 2. ip 할당

/etc/netplan 에 있는 yaml 파일

```yaml
network:
  ethernets:
    enp3s0:
      addresses: [192.168.0.105/24]
      gateway4: 192.168.0.1
      optional: true
      nameservers:
        addresses: [8.8.8.8,8.8.4.4]
  version: 2
```


```yaml
# This is the network config written by 'subiquity'
network:
  ethernets:
    enp3s0:
      addresses:
      - 192.168.0.105/24
      gateway4: 192.168.0.1
      optional: true
      nameservers:
        addresses:
        - 8.8.8.8
    enp4s0:
      addresses:
      - 111.111.111.111/24
      gateway4: 111.111.111.1
      optional: true
      nameservers:
        addresses:
        - 8.8.8.8
  version: 2
```


ip 수정 후 아래 명령어 실행

```sh
sudo netplan apply
```




## 3. service 등록


getty@tty1.service 를 운영해야 box에서 직접 terminal 접근이 가능


/lib/systemd/system/getty@.service 파일 내용중 아래와 같이 수정하여, 자동 로그인 되도록 변경


```bin
ExecStart=-/sbin/agetty --autologin pnat --noclear %I $TERM
#ExecStart=-/sbin/agetty -o '-p -- \\u' --noclear %I $TERM
```


아래 service stop and disabled

근데 이거 상황에 따라 필요할 수도...

```
systemctl list-unit-files --all --state=disabled

sudo systemctl status usbConsole.service
sudo systemctl stop usbConsole.service
sudo systemctl disable usbConsole.service
```


cloud-init 삭제

```sh
echo 'datasource_list: [ None ]' | sudo -s tee /etc/cloud/cloud.cfg.d/90_dpkg.cfg 
sudo apt-get purge cloud-init
sudo rm -rf /etc/cloud/
sudo rm -rf /var/lib/cloud/
```



add_service.sh 을 실행한다.

```
systemctl list-units --type=service --all
sudo systemctl disable XXX.serivce
sudo systemctl daemon-reload
sudo systemctl reset-failed
```


sudo systemctl enable EQP_01.service
sudo systemctl enable EQP_02.service
sudo systemctl enable EQP_03.service
sudo systemctl enable EQP_04.service
sudo systemctl enable EQP_05.service
sudo systemctl enable EQP_06.service
sudo systemctl enable EQP_07.service
sudo systemctl enable EQP_08.service

sudo systemctl daemon-reload

sudo systemctl start EQP_01.service
sudo systemctl start EQP_02.service
sudo systemctl start EQP_03.service
sudo systemctl start EQP_04.service
sudo systemctl start EQP_05.service
sudo systemctl start EQP_06.service
sudo systemctl start EQP_07.service
sudo systemctl start EQP_08.service


systemctl list-units --type=service --all --state=running | grep EQP_ | awk '{print $5}'


ps -eo pid,lstart,cmd | grep app.name=EQP_0 | grep -v "grep" | awk '{print $1}'







