#!/bin/bash

PID=`pgrep -f "target/mgd_java_api-swarm.jar"`

#PID=pgrep -f 'target/mgd_java_api-swarm.jar'
printf "Killing process with pid=$PID\n"
kill -9 $PID
