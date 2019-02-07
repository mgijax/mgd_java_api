#!/bin/bash

#
# stop the API
# archive the ${LOG_FILE}
#

if [ "${MGICONFIG}" = "" ]
then
    MGICONFIG=/usr/local/mgi/live/mgiconfig
    export MGICONFIG
fi

source ${MGICONFIG}/master.config.sh

. ./Configuration

echo "Stopping Java API"

PID=`pgrep -f "target/mgd_java_api-swarm.jar"`
printf "Killing process with pid=$PID\n"
kill -HUP $PID

#echo "Save log file: ${LOG_FILE}.old"
#cp -r ${LOG_FILE} ${LOG_FILE}.old

