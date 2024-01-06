#!/bin/bash

#
# start the API in the background
# send standard out to the ${LOG_FILE}
# the stop script will archive the log file
#
# you will see this in Jenkins:  don't remove it!
#
# BUILD_ID=dontKillMe
#

if [ -f ./Configuration ]
then
    . ./Configuration
else
    echo "Configuration file is missing in mgd_java_api"
    exit 1
fi

if [ "$1" == "dev" ] ; then
    echo "Starting Java API in dev mode"
    mvn compile quarkus:dev
else
    echo "Starting Java API"
    echo "log file: " ${LOG_FILE}
    rm -f ${LOG_FILE}
    touch ${LOG_FILE}

    # redirect stdout and stderr to ${LOG_FILE}
    ${JAVA} -jar target/mgd_java_api-runner.jar &> ${LOG_FILE} &
    echo $! > ${MGI_LIVE}/mgd_java_api.pid
    sleep 5
fi

