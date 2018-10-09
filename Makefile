#
# default Makefile params
#

# any changes to 'all' also need to be made to Jenkins/mgd_java_api-dev configuration
all:
	mvn clean package -DskipTests

clean:
	find /tmp/wildfly* -type f -exec rm -rf {} \;

run:
	java -jar target/mgd_java_api-swarm.jar -Papp.properties

debug:
	java -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000 -jar target/mgd_java_api-swarm.jar -Papp.properties

#test:
#	mvn test -DCONFIG=${MGICONFIG}/master.config.sh

#
# bheidev01.jax.org : older server
# need to specifically call java 8 and run targets
#
runnet:
	/usr/java/jdk1.8.0_131/bin/java -jar target/mgd_java_api-swarm.jar -Papp.properties -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true

