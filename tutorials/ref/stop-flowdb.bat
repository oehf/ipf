@echo off

rem --------------------------------------------------------
rem  Shutdown flow manager database
rem --------------------------------------------------------
"%JAVA_HOME%\bin\java.exe" %JAVA_OPTS% -cp lib\derby-10.4.1.3.jar;lib\derbynet-10.4.1.3.jar org.apache.derby.drda.NetworkServerControl shutdown