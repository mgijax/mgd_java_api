#
# default Makefile params
#

all:
	mvn clean package -DskipTests

clean:
	find /tmp/wildfly* -type f -exec rm -rf {} \;

run:
	java -jar target/mgd_java_api-swarm.jar -Papp.properties

debug:
	java -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000 -jar target/mgd_java_api-swarm.jar -Papp.properties 

#using src/test/TestMarkerWithdrawal.sh for now
#test:
#	mvn test
# mvn test -Dswarm.ds.server=mgi-testdb4 -Dswarm.ds.database=lec -Dswarm.ds.username=mgd_dbo -Dswarm.ds.passwordfile=/home/lec/mgi/dbutils/pgdbutilities/.pgpass_1 -Dswarm.markerWithdrawal=/home/lec/mgi/dbutils/pgdbutilities/bin/ei/markerWithdrawal.csh

#
# bheidev01.jax.org : older server
# need to specifically call java 8 and run targets
#
runnet:
	/usr/java/jdk1.8.0_131/bin/java -jar target/mgd_java_api-swarm.jar -Papp.properties -Djava.net.preferIPv4Stack=true -Djava.net.preferIPv4Addresses=true

