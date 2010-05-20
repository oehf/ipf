#!/bin/sh

EXTENSION=$1

JAVA_OPTS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"

BASEDIR=`dirname $0`
TUTORIAL_CLASSPATH=$BASEDIR/lib/*:$BASEDIR/conf/.

exec $JAVA_HOME/bin/java $JAVA_OPTS -cp $TUTORIAL_CLASSPATH org.openehealth.ipf.tutorials.config.base.Base $EXTENSION