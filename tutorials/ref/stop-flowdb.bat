@echo off

rem --------------------------------------------------------
rem  Shutdown flow manager database
rem --------------------------------------------------------
"%JAVA_HOME%\bin\java.exe" %JAVA_OPTS% -cp lib\derby-${derby-version}.jar;lib\derbynet-${derby-version}.jar org.apache.derby.drda.NetworkServerControl shutdown