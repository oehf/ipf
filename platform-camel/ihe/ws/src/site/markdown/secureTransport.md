
## Web Service transport-level encryption



### Consumer deployment

SSL support for IPF IHE consumers side must be configured in their [deployment container](deployment.html).
Examples are deployment in [Tomcat 7](http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html) or
[Tomcat 8](http://tomcat.apache.org/tomcat-8.0-doc/ssl-howto.html).

There are no additional endpoint URL parameters.


### Producer deployment

TLS-related aspects of Web Service-based transactions are controlled by the following URI parameters:

| Parameter name   | Type       | Default value | Short description
|:-----------------|:-----------|:--------------|:---------------------------------------------
| `secure`         | boolean    | false         | whether transport-level encryption shall be applied by the given endpoint

In addition, [CXF](http://cxf.apache.org)'s HTTP client must be configured accordingly.
This is done within the String context as detailed in the
[CXF documentation](http://cxf.apache.org/docs/client-http-transport-including-ssl-support.html).

Example:

```xml

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:camel="http://camel.apache.org/schema/spring"
       xmlns:http="http://cxf.apache.org/transports/http/configuration"
       xmlns:sec="http://cxf.apache.org/configuration/security"
       xsi:schemaLocation="
http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans.xsd
http://camel.apache.org/schema/spring
http://camel.apache.org/schema/spring/camel-spring.xsd
http://cxf.apache.org/transports/http/configuration
http://cxf.apache.org/schemas/configuration/http-conf.xsd
http://cxf.apache.org/configuration/security
http://cxf.apache.org/schemas/configuration/security.xsd
">

    <import resource="classpath:META-INF/cxf/cxf.xml" />
    <import resource="classpath:META-INF/cxf/cxf-extension-jaxws.xml" />
    <import resource="classpath:META-INF/cxf/cxf-servlet.xml" />

    <http:conduit name="*.http-conduit">
        <!-- Configuration of timeouts -->
        <http:client ConnectionTimeout="0" ReceiveTimeout="0"/>

        <!-- TLS configuration -->
        <http:tlsClientParameters disableCNCheck="true">
            <sec:keyManagers keyPassword="changeit">
                <sec:keyStore type="JKS" password="changeit" file="keystore" />
            </sec:keyManagers>
            <sec:trustManagers>
                <sec:keyStore type="JKS" password="changeit" file="keystore" />
            </sec:trustManagers>
            <sec:cipherSuitesFilter>
                <!-- these filters ensure that a ciphersuite with export-suitable or
                     null encryption is used, but exclude anonymous Diffie-Hellman
                     key change as this is vulnerable to man-in-the-middle attacks -->
                <sec:include>.*_EXPORT_.*</sec:include>
                <sec:include>.*_EXPORT1024_.*</sec:include>
                <sec:include>.*_WITH_DES_.*</sec:include>
                <sec:include>.*_WITH_NULL_.*</sec:include>
                <sec:exclude>.*_DH_anon_.*</sec:exclude>
            </sec:cipherSuitesFilter>
        </http:tlsClientParameters>

        <!-- Optional HTTP auth configuration -->
        <http:authorization>
            <sec:UserName>userName</sec:UserName>
            <sec:Password>*****</sec:Password>
        </http:authorization>
    </http:conduit>
    ...

```