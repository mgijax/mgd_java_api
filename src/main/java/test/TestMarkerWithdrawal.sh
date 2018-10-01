#!/bin/sh

if [ "${MGICONFIG}" = "" ]
then
    MGICONFIG=/usr/local/mgi/live/mgiconfig
    export MGICONFIG
fi

CONFIG_MASTER=${MGICONFIG}/master.config.sh
export CONFIG_MASTER
. ${CONFIG_MASTER}
echo $MGICONFIG

CLASSPATH=/home/lec/mgi/mgd_java_api/target/classes:/home/lec/mgi/mgd_java_api/src/lib/core.jar:${COMMON_CLASSPATH}
export CLASSPATH
echo $CLASSPATH

${JAVA} -classpath ${CLASSPATH} -DCONFIG=${CONFIG_MASTER} test.TestMarkerWithdrawal

