#!/bin/bash
echo "======================================================================="
echo "Starting International Payment Platform on Linux..."
echo "======================================================================="

JAR_PATH="payment-api/target/payment-api-1.0.0-SNAPSHOT.jar"

if [ ! -f "$JAR_PATH" ]; then
    echo "Executable JAR not found. Building project first..."
    ./build.sh
fi

java -jar "$JAR_PATH"
