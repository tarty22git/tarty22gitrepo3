#!/bin/bash
echo "======================================================================="
echo "Stopping International Payment Platform on Linux..."
echo "======================================================================="

PID=$(lsof -t -i:8080)

if [ -z "$PID" ]; then
    echo "No running application found on port 8080."
else
    kill -9 $PID
    echo "Application process $PID stopped successfully."
fi
