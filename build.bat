@echo off
echo =======================================================================
echo Building International Payment Platform (com.cth.job)
echo =======================================================================

mvn clean package -DskipTests

if %ERRORLEVEL% EQU 0 (
    echo =======================================================================
    echo BUILD SUCCESSFUL! Executable JAR created in payment-api\target\
    echo =======================================================================
) else (
    echo =======================================================================
    echo BUILD FAILED!
    echo =======================================================================
)
