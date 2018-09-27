#!/bin/sh

if [ "${MGICONFIG}" = "" ]
then
    MGICONFIG=/usr/local/mgi/live/mgiconfig
    export MGICONFIG
fi

CONFIG_MASTER=${MGICONFIG}/master.config.sh
export CONFIG_MASTER
. ${CONFIG_MASTER}

JAVA_LIB=${MGI_JAVALIB}/core.jar
export JAVA_LIB

CLASSPATH=${JAVA_LIB}:${COMMON_CLASSPATH}
export CLASSPATH

${JAVA} -classpath ${CLASSPATH} -DCONFIG=${CONFIG_MASTER} TestMarkerWithdrawal

