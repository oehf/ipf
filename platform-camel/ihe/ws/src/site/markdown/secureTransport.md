
## Web Service basic authentication options

Client-side endpoints (i.e. producers) can be configured with Basic Authentication credentials

| Parameter name   | Type       | Default value | Short description                                                                    |
|:-----------------|:-----------|:--------------|:-------------------------------------------------------------------------------------|
| `username`       | String     | n/a           | username for basic authentication
| `password`       | String     | n/a           | password for basic authentication

## Web Service transport-level encryption

### Consumer

SSL support for IPF IHE consumers side must be configured in their [deployment container](deployment.html).
See e.g. SSL How-To for [Tomcat 7](http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html) or [Tomcat 8](http://tomcat.apache.org/tomcat-8.0-doc/ssl-howto.html).

### Producer

TLS-related aspects of Web Service-based transactions are controlled by the following URI parameters:

| Parameter name         | Type                   | Default value  | Short description
|:-----------------------|:-----------------------|:-------|:---------------------------------------------
| `secure`               | boolean                | false  | enabled transport-level encryption for the given endpoint
| `sslContextParameters` | [SslContextParameters] | n/a    | enables transport-level encryption and determines the SSL parameters that shall be applied to the endpoint
| `hostnameVerifier`     | [HostnameVerifier]     | n/a    | strategy for host name verification

If `secure` is set to true but no `sslContextParameters` are provided, the Camel registry is looked up for 
a unique `sslContextParameters` bean instance to be used. If none is found, a default SSL Context (optionally controlled by the system environment) 
is instantiated. If more than one is found, an exception is thrown.

[SslContextParameters] can be configured as shown in the example below. In this case, the WS producer URI requires 
the parameter `sslContextParameters=#myContext`.

```xml
     <beans xmlns="http://www.springframework.org/schema/beans"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns:camel="http://camel.apache.org/schema/spring"
            xsi:schemaLocation="
     http://www.springframework.org/schema/beans
     http://www.springframework.org/schema/beans/spring-beans.xsd
     http://camel.apache.org/schema/spring
     http://camel.apache.org/schema/spring/camel-spring.xsd">
     
     ...
     
    <camel:sslContextParameters id="myContext">
        <camel:keyManagers keyPassword="changeit">
            <camel:keyStore type="JKS" password="changeit" resource="client.jks"/>
        </camel:keyManagers>
        <camel:trustManagers>
            <camel:keyStore type="JKS" password="changeit" resource="client.jks"/>
        </camel:trustManagers>
        <camel:clientParameters>
            <camel:cipherSuitesFilter>
                <camel:include>.*_EXPORT_.*</camel:include>
                <camel:include>.*_EXPORT1024_.*</camel:include>
                <camel:include>.*_WITH_DES_.*</camel:include>
                <camel:include>.*_WITH_NULL_.*</camel:include>
                <camel:exclude>.*_DH_anon_.*</camel:exclude>
            </camel:cipherSuitesFilter>
        </camel:clientParameters>
    </camel:sslContextParameters>    
     
 ```

If [sslContextParameters][SslContextParameters] are not provided, [CXF](http://cxf.apache.org)'s HTTP client must be configured accordingly.
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
    http://cxf.apache.org/schemas/configuration/security.xsd">

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

[SSLContextParameters]: http://camel.apache.org/camel-configuration-utilities.html
[HostnameVerifier]: http://docs.oracle.com/javase/8/docs/api/javax/net/ssl/HostnameVerifier.html