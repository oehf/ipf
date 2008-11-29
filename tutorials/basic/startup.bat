@echo off

rem --------------------------------------------------------
rem  JMX settings
rem --------------------------------------------------------
set JAVA_OPTS=-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false

rem --------------------------------------------------------
rem  Start route
rem --------------------------------------------------------
"%JAVA_HOME%\bin\java.exe" %JAVA_OPTS% -cp conf;lib\* org.openehealth.ipf.tutorials.basic.SampleServer
