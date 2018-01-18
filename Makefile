#
# default Makefile params
#

all:
        mvn clean package

run:
        java -jar target/mgd_java_api-swarm.jar -Papp.properties

#
# bheidev01.jax.org : older server
# need to specifically call java 8 and run targets
#
runnet:
        /usr/java/jdk1.8.0_131/bin/java -jar target/mgd_java_api-swarm.jar -Papp.properties -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true


