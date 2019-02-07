#!/bin/bash

#
# start the API in the background
# send standard out to the ${LOG_FILE}
# the stop script will archive the log file
#

if [ "${MGICONFIG}" = "" ]
then
    MGICONFIG=/usr/local/mgi/live/mgiconfig
    export MGICONFIG
fi

source ${MGICONFIG}/master.config.sh

. ./Configuration

echo "Clean up temp files"
make clean

echo "Starting Java API"
rm -f ${LOG_FILE}
touch ${LOG_FILE}
ls -l ${LOG_FILE}

# redirect stdout and stderr to ${LOG_FILE}
/usr/java/jdk1.8.0_131/bin/java -jar target/mgd_java_api-swarm.jar -Papp.properties -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true &> ${LOG_FILE} &

