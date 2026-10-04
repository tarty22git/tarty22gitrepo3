@echo off
echo =======================================================================
echo Starting International Payment Platform on Windows...
echo =======================================================================

if not exist "payment-api\target\payment-api-1.0.0-SNAPSHOT.jar" (
    echo Executable JAR not found. Building project first...
    call build.bat
)

java -jar payment-api\target\payment-api-1.0.0-SNAPSHOT.jar
