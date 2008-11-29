@echo off

rem --------------------------------------------------------
rem  Start JConsole with flow manager jar containing DTOs
rem --------------------------------------------------------
jconsole -J-Djava.class.path="%JAVA_HOME%/lib/jconsole.jar";"%JAVA_HOME%/lib/tools.jar";lib/flow-1.5-SNAPSHOT.jar
