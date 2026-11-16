@echo off
setlocal
set GRADLE_VERSION=8.13
if "%GRADLE_USER_HOME%"=="" set "GRADLE_USER_HOME=%USERPROFILE%\.gradle"
set "CACHE_DIR=%GRADLE_USER_HOME%\lume-bootstrap\gradle-%GRADLE_VERSION%"
set "GRADLE_HOME=%CACHE_DIR%\gradle-%GRADLE_VERSION%"
set "GRADLE_BIN=%GRADLE_HOME%\bin\gradle.bat"
set "ARCHIVE=%CACHE_DIR%\gradle-%GRADLE_VERSION%-bin.zip"
if not exist "%GRADLE_BIN%" (
  if not exist "%CACHE_DIR%" mkdir "%CACHE_DIR%"
  if not exist "%ARCHIVE%" (
    powershell -NoProfile -ExecutionPolicy Bypass -Command "$ProgressPreference='SilentlyContinue'; Invoke-WebRequest 'https://services.gradle.org/distributions/gradle-%GRADLE_VERSION%-bin.zip' -OutFile '%ARCHIVE%'"
    if errorlevel 1 exit /b 1
  )
  powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -LiteralPath '%ARCHIVE%' -DestinationPath '%CACHE_DIR%' -Force"
  if errorlevel 1 exit /b 1
  del "%ARCHIVE%"
)
call "%GRADLE_BIN%" -p "%~dp0" %*
exit /b %ERRORLEVEL%
