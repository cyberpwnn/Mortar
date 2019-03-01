@Echo off
echo Apply Script: COPY x2
echo F|xcopy /y /s /f /q "%1" "%2"
echo F|xcopy /y /s /f /q "%1" "%3"
echo Writing Version

echo %4 > "C:\Users\cyberpwn\Documents\development\workspace\Mortar\version.txt"  