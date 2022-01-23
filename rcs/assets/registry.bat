@echo off
echo .
echo .
echo .
echo ######################################################################
echo Created by Windowexe.com , WindowexeRegToBat
echo ######################################################################
echo .
echo .
echo .
reg.exe add "HKEY_CLASSES_ROOT\mymstsc" /v "URL Protocol" /d "" /f
reg.exe add "HKEY_CLASSES_ROOT\mymstsc\shell\open\command" /v "" /d "C:\TWISE\rcs\mstsc.bat %%1" /f
echo .
pause
