#!/bin/sh
wget https://raw.githubusercontent.com/oehf/ipf-labs/master/maven/settings.xml --no-check-certificate
mvn clean deploy -DskipTests=true -q --settings settings.xml
