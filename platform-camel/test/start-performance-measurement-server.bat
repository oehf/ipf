@echo off

rem --------------------------------------------------------
rem  Startup Performance Management server (must be single node only)
rem --------------------------------------------------------
"%JAVA_HOME%\bin\java.exe" %JAVA_OPTS% -cp conf;lib\* org.openehealth.ipf.platform.camel.test.performance.server.PerformanceMeasurementServer
