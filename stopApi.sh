#!/bin/bash

#
# stop the API
# archive the ${LOG_FILE}
#

if [ -f ./Configuration ]
then
    . ./Configuration
else
    echo "Configuration file is missing in mgd_java_api"
    exit 1
fi

echo "Stopping Java API"

if [ -f ${MGI_LIVE}/mgd_java_api.pid ]
then
        PID=`cat ${MGI_LIVE}/mgd_java_api.pid`
        KILLPID=`pgrep -u ${USER} -f "target/mgd_java_api-swarm.jar" | grep ${PID}`
        if [ ! -z ${KILLPID} ]
        then
                printf "Killing process with pid=${KILLPID}\n"
                kill -HUP ${KILLPID}
        fi
        rm -rf ${MGI_LIVE}/mgd_java_api.pid
fi

# some time for log to flush before archiving
sleep 5

echo "Timestamp and save log file"
timestamp=`date '+%Y%m%d.%H%M'`
cp -r ${LOG_FILE} ${LOG_FILE}.${timestamp}

echo "Remove old archived log files : older than 360 days"
find ${LOG_DIR}/* -type f -mtime +360 -exec rm -rf {} \;
