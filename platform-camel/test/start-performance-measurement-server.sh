# Copyright 2009 the original author or authors.
# 
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#     
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


# --------------------------------------------------------
#  Derive the HTTP port from the first argument (or use default)
# --------------------------------------------------------
HTTP_PORT=$1
if ["$HTTP_PORT" = ""]
then
    HTTP_PORT=9191
fi


# --------------------------------------------------------
#  Derive whether to override the measurement history date from the 
#  second argument. If no override is used, the nodes of the cluster 
#  being tested for performance must have the same system times. 
#  The two valid values are true and false
# --------------------------------------------------------
OVERRIDE_MEASUREMENT_HISTORY_DATE=$2
if ["$OVERRIDE_MEASUREMENT_HISTORY_DATE" = ""]
then
    OVERRIDE_MEASUREMENT_HISTORY_DATE=true
fi
    
# --------------------------------------------------------
#  Derive JMX port from the third argument (or use default)
# --------------------------------------------------------
JMX_PORT=$3
if ["$JMX_PORT" = ""]
then
    JMX_PORT=9999
fi

PERFORMANCE_MEASUREMENT_SERVER_OPTS=-Dpms.http.port=$HTTP_PORT -Dpms.override.measurement.history.reference.date=$OVERRIDE_MEASUREMENT_HISTORY_DATE

# --------------------------------------------------------
#  JMX and general Java system properties
# --------------------------------------------------------
JAVA_OPTS=-Xmx256m -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=$JMX_PORT -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false

# --------------------------------------------------------
#  Startup Performance Management server (must be single node only)
# --------------------------------------------------------
"$JAVA_HOME/bin/java" $PERFORMANCE_MEASUREMENT_SERVER_OPTS $JAVA_OPTS -cp "../lib/*" org.openehealth.ipf.platform.camel.test.performance.server.PerformanceMeasurementServer
