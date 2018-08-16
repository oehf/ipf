#!/bin/bash

java -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -cp 'lib/*' org.openehealth.ipf.tutorials.xds.Server $1