
## FHIR basic authentication options

Client-side FHIR endpoints (i.e. producers) can be configured with Basic Authentication credentials

| Parameter name   | Type       | Default value | Short description                                                                    |
|:-----------------|:-----------|:--------------|:-------------------------------------------------------------------------------------|
| `username`       | String     | n/a           | username for basic authentication
| `password`       | String     | n/a           | password for basic authentication


## FHIR transport-level encryption

### Consumer

SSL support for IPF IHE consumers side must be configured in their [deployment container](deployment.html).
See e.g. SSL How-To for [Tomcat 7](http://tomcat.apache.org/tomcat-7.0-doc/ssl-howto.html) or [Tomcat 8](http://tomcat.apache.org/tomcat-8.0-doc/ssl-howto.html).

### Producer

TLS-related aspects for Client-side FHIR endpoints (i.e. producers) are controlled by the following URI parameters:

| Parameter name         | Type                     | Default value | Short description                                                                    |
|:-----------------------|:-------------------------|:--------------|:-------------------------------------------------------------------------------------|
| `secure`               | boolean                  | false         | enables transport-level encryption for the given endpoint
| `sslContextParameters` | [SslContextParameters]   | n/a           | enables transport-level encryption and determines the SSL parameters that shall be applied to the endpoint
| `hostnameVerifier`     | [HostnameVerifier]       | n/a           | strategy for host name verification

If `secure` is set to true but no `sslContextParameters` are provided, the Camel registry is looked up for 
a unique `sslContextParameters` bean instance to be used. If none is found, a default SSL Context (optionally controlled by the system environment) 
is instantiated. If more than one is found, an exception is thrown.


[SslContextParameters] can be configured as shown in the example below. In this case, the FHIR producer URI requires 
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


[SSLContextParameters]: http://camel.apache.org/camel-configuration-utilities.html
[HostnameVerifier]: http://docs.oracle.com/javase/8/docs/api/javax/net/ssl/HostnameVerifier.html
