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
reg.exe add "HKEY_CLASSES_ROOT\rcsviewer" /v "URL protocol" /d "" /f
reg.exe add "HKEY_CLASSES_ROOT\rcsviewer\shell\open\command" /v "" /d "C:\TWISE\RemoteViewer_v1.4\RemoteViewer_v1.4.exe %%1" /f
echo .
pause
