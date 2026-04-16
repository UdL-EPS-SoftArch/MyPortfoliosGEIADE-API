@echo off
setlocal

set "BASE_DIR=%~dp0"
set "WRAPPER_PROPS=%BASE_DIR%.mvn\wrapper\maven-wrapper.properties"
set "DIST_URL=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.11/apache-maven-3.9.11-bin.zip"

if exist "%WRAPPER_PROPS%" (
    for /f "usebackq tokens=1,* delims==" %%A in ("%WRAPPER_PROPS%") do (
        if /i "%%A"=="distributionUrl" set "DIST_URL=%%B"
    )
)

for %%F in ("%DIST_URL%") do set "ARCHIVE_NAME=%%~nxF"
set "MAVEN_VERSION=%ARCHIVE_NAME:-bin.zip=%"
set "MAVEN_VERSION=%MAVEN_VERSION:apache-maven-=%"
set "INSTALL_ROOT=%USERPROFILE%\.m2\wrapper\dists"
set "ARCHIVE_PATH=%INSTALL_ROOT%\%ARCHIVE_NAME%"
set "MAVEN_HOME=%INSTALL_ROOT%\apache-maven-%MAVEN_VERSION%"
set "MVN_CMD=%MAVEN_HOME%\bin\mvn.cmd"

if not exist "%MVN_CMD%" (
    if not exist "%INSTALL_ROOT%" mkdir "%INSTALL_ROOT%"
    echo Downloading Maven %MAVEN_VERSION%...
    powershell -NoProfile -ExecutionPolicy Bypass -Command ^
        "$ProgressPreference='SilentlyContinue';" ^
        "Invoke-WebRequest -Uri '%DIST_URL%' -OutFile '%ARCHIVE_PATH%';" ^
        "Expand-Archive -LiteralPath '%ARCHIVE_PATH%' -DestinationPath '%INSTALL_ROOT%' -Force"
    if errorlevel 1 (
        echo Failed to download Maven from %DIST_URL%
        exit /b 1
    )
)

call "%MVN_CMD%" %*
exit /b %ERRORLEVEL%
