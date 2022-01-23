@echo off




set s=%1
set s=%s:~10%
rem set s=%s:~7%


set data=%s%

echo DATA : %data%


set IP=
set WIDTH=
set HEIGHT=


rem SET data=192.168.0.40,1200,800

(FOR /f "tokens=1,2,3 delims=/" %%a IN ("%data%") do ( 
	set IP=%%a
	set WIDTH=%%b
	set HEIGHT=%%c
))

echo IP: %IP%
echo WIDTH: %WIDTH%
echo HEIGHT: %HEIGHT%


start "" "C:\Windows\system32\mstsc.exe" /v: %IP% /w: %WIDTH% /h: %HEIGHT%

rem call "C:\Windows\system32\mstsc.exe" /v: %IP% /w: %WIDTH% /h: %HEIGHT%

goto end

:end
exit
