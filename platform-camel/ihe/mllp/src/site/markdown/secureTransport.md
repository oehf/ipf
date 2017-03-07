
## MLLP transport-level encryption

TLS-related aspects of MLLP-based transactions are controlled by the following URI parameters:

### Parameters

| Parameter name          | Type       | Default value   | Short description                                                                    |
|:------------------------|:-----------|:----------------|:-------------------------------------------------------------------------------------|
| `secure`                | boolean    | false           | enables transport-level encryption for the given endpoint
| `sslContext`            | String     | n/a             | name of a user-defined SSL context, if any, with leading '#'.
| `sslContextParameters`  | [SSLContextParameters] | n/a | enables transport-level encryption and determines the SSL parameters that shall be applied to the endpoint 
| `sslProtocols`          | String     | as defined in SSLContext | comma-separated list of SSL protocols that should be enforced by the given endpoint
| `sslCiphers`            | String     | as defined in SSLContext | comma-separated list of SSL cipher suites that should be enforced by the given endpoint 
| `clientAuth`            | one of `NONE`, `WANT`, `MUST` | as defined in SSLContext | whether client authentication for mutual TLS is required (MUST), requested (WANT) or not requested (NONE) on the given endpoint

If `secure` is set to true but neither `sslContext` nor `sslContextParameters` are provided,the Camel registry is looked up for 
a unique `sslContextParameters` bean instance to be used. If none is found, a default SSL Context (optionally controlled by the system environment) 
is instantiated. If more than one is found, an exception is thrown.
`clientAuth`, `sslProtocols` and `sslCiphers` override the corresponding settings in `sslContext` or `sslContextParameters`

[SslContextParameters] can be configured as shown in the example below. In this case, the MLLP producer URI requires 
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


[SSLContextParameters]: https://camel.apache.org/camel-configuration-utilities.html