@echo off

rem --------------------------------------------------------
rem  Flow manager database and JMX settings 
rem --------------------------------------------------------
set JAVA_OPTS=-Dderby.drda.maxThreads=50 -Dcom.sun.management.jmxremote.port=1800 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false

rem --------------------------------------------------------
rem  Startup flow manager database
rem --------------------------------------------------------
"%JAVA_HOME%\bin\java.exe" %JAVA_OPTS% -cp lib\derby-10.4.1.3.jar;lib\derbynet-10.4.1.3.jar org.apache.derby.drda.NetworkServerControl start -noSecurityManager