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

echo "Clean up temp files and making package"
make all

echo "Starting Java API"
echo "log file: " ${LOG_FILE}
rm -f ${LOG_FILE}
touch ${LOG_FILE}

# redirect stdout and stderr to ${LOG_FILE}
/usr/java/jdk1.8.0_131/bin/java -jar target/mgd_java_api-swarm.jar -Papp.properties -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true &> ${LOG_FILE} &

# takes about 20 secs to finish, then you know it is done
sleep 20

