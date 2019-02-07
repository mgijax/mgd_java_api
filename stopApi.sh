#!/bin/bash

#
# stop the API
# archive the ${LOG_FILE}
#

. ./Configuration

echo "Stopping Java API"

# only kill process started by user running this command
PID=`pgrep -u ${USER} -f "target/mgd_java_api-swarm.jar"`
printf "Killing process with pid=$PID\n"
kill -HUP $PID

# some time for log to flush before archiving
sleep 5

echo "Timestamp and save log file"
timestamp=`date '+%Y%m%d.%H%M'`
cp -r ${LOG_FILE} ${LOG_FILE}.${timestamp}

echo "Remove old archived log files : older than 360 days"
find ${LOG_DIR}/* -type f -mtime +360 -exec rm -rf {} \;
