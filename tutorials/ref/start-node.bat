@echo off

rem --------------------------------------------------------
rem  Derive node name from first argument (or use default)
rem --------------------------------------------------------
set NODE=%1
if "%NODE%" == "" set NODE=node1

rem --------------------------------------------------------
rem  JMX and cluster node system properties
rem --------------------------------------------------------
set JAVA_OPTS=-Dcluster.node.dir=%NODE% -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false

rem --------------------------------------------------------
rem  Startup tutorial server (cluster node)
rem --------------------------------------------------------
"%JAVA_HOME%\bin\java.exe" %JAVA_OPTS% -cp conf;lib\* org.openehealth.ipf.tutorials.ref.TutorialServer
