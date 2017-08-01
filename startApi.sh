#!/bin/bash

# setup for scrumdogdev server, called via Jenkins
/usr/java/jdk1.8.0_131/bin/java -jar target/mgd_java_api-swarm.jar -Papp.properties &
