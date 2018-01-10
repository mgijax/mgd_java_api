all:
	mvn clean package

run:
	java -jar target/mgd_java_api-swarm.jar -Papp.properties
