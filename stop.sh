#!/bin/sh

# 특수문자는 escape 처리
PROCESS_NAME="java -jar build\/libs\/kyu-0.0.1-SNAPSHOT.jar"

PID=`ps aux |awk "/$PROCESS_NAME/"'{print $2}'`

if [ -z "$PID" ]; then
  exit 0
fi

echo "PID=$PID"

kill $PID