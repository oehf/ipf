rmdir /q/s target
mkdir target
set JAVA_TOOL_OPTIONS=-Duser.language=en
C:\dev\apache-cxf-3.2.2\bin\wsdl2java -validate -d target -b ch-ppq.xjb -p org.openehealth.ipf.commons.ihe.xacml20.chppq ch-ppq.wsdl
