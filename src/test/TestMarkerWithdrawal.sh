#!/bin/sh

if [ "${MGICONFIG}" = "" ]
then
    MGICONFIG=/usr/local/mgi/live/mgiconfig
    export MGICONFIG
fi

echo $MGICONFIG

CONFIG_MASTER=${MGICONFIG}/master.config.sh
export CONFIG_MASTER
. ${CONFIG_MASTER}

CLASSPATH=${MGD_JAVA_API}/target/classes:${MGD_JAVA_API}/target/test-classes:${MGD_JAVA_API}/src/lib/core.jar:${COMMON_CLASSPATH}
export CLASSPATH
echo $CLASSPATH

${JAVA} -classpath ${CLASSPATH} -DCONFIG=${CONFIG_MASTER} org.jax.mgi.TestMarkerWithdrawal

