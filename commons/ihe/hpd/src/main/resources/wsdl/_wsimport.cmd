set COMMAND=C:\dev\apache-cxf-3.1.10\bin\wsdl2java.bat
set JAVA_TOOL_OPTIONS=-Duser.language=en

rem rmdir /q /s org
call %COMMAND% -p org.openehealth.ipf.commons.ihe.hpd.stub.iti59 iti58.wsdl
