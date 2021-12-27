

[https://kyungsnim.net/176](https://kyungsnim.net/176).

```
pnat@pnatbox:/var/log$ groups
pnat adm dialout cdrom sudo dip plugdev lxd
```

```sh
sudo adduser 계정
sudo passwd 계정
> 패스워드 입력

sudo usermod -aG sudo 계정
sudo usermod -aG dialout 계정

su - 계정
```


sudo vi /etc/sudoers
sudo vi /etc/passwd
sudo vi /etc/group


```
sudo userdel -r 계정명
sudo rm -rf /home/계정명
```


