@echo off

rem Copyright 2009 the original author or authors.
rem 
rem Licensed under the Apache License, Version 2.0 (the "License");
rem you may not use this file except in compliance with the License.
rem You may obtain a copy of the License at
rem     http://www.apache.org/licenses/LICENSE-2.0
rem     
rem Unless required by applicable law or agreed to in writing, software
rem distributed under the License is distributed on an "AS IS" BASIS,
rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
rem See the License for the specific language governing permissions and
rem limitations under the License.


rem --------------------------------------------------------
rem  Derive the HTTP port from the first argument (or use default)
rem --------------------------------------------------------
set HTTP_PORT=%1
    IF "%HTTP_PORT%" == "" SET HTTP_PORT=9191

rem --------------------------------------------------------
rem  Derive whether to override the measurement history date from the 
rem  second argument. If no override is used, the nodes of the cluster 
rem  being tested for performance must have the same system times. 
rem  The two valid values are true and false
rem --------------------------------------------------------
set OVERRIDE_MEASUREMENT_HISTORY_DATE=%2
    IF "%OVERRIDE_MEASUREMENT_HISTORY_DATE%" == "" SET OVERRIDE_MEASUREMENT_HISTORY_DATE=true
    
rem --------------------------------------------------------
rem  Derive JMX port from the third argument (or use default)
rem --------------------------------------------------------
set JMX_PORT=%3
    IF "%JMX_PORT%" == "" SET JMX_PORT=9999

set PERFORMANCE_MEASUREMENT_SERVER_OPTS=-Dpms.http.port=%HTTP_PORT% -Dpms.override.measurement.history.reference.date=%OVERRIDE_MEASUREMENT_HISTORY_DATE%

rem --------------------------------------------------------
rem  JMX and general Java system properties
rem --------------------------------------------------------
set JAVA_OPTS=-Xmx256m -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=%JMX_PORT% -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false

rem --------------------------------------------------------
rem  Startup Performance Management server (must be single node only)
rem --------------------------------------------------------
"%JAVA_HOME%\bin\java.exe" %JAVA_OPTS% %PERFORMANCE_MEASUREMENT_SERVER_OPTS% -cp "..\lib\*" org.openehealth.ipf.platform.camel.test.performance.server.PerformanceMeasurementServer
