
## File-based Logging of Web Service Message Payload

The general functionality of [file-based payload logging] is provided for Web Service endpoints
in form of two CXF interceptors located within the package `oorg.openehealth.ipf.commons.ihe.ws.cxf.payload`,
which have to be deployed on the endpoints:

* `.InPayloadLoggerInterceptor` for inbound messages
* `.OutPayloadLoggerInterceptor` for outbound message

In addition to the base expression placeholders, the following one is defined:

| Value Name          | Value Type | Description
|:--------------------|:-----------|:----------------------------------------------------------------
| `partialResponse`   | boolean    | `true` when the currently handled message represents a WS-Addressing asynchronous acknowledgement (HTTP code 202). This gives a means to distinguish between such acknowledgement messages and "normal" messages which have the same sequence IDs

    
### Example

This is a Spring configuration fragment that defines a set of parameterized interceptor beans:

```xml
    <bean id="logFileNamePrefix" class="java.lang.String">
        <constructor-arg value="#{systemProperties['IPF_LOG_DIR']}/[processId]/[date('yyyyMMdd-HH00')]/[sequenceId]" />
    </bean>

    <bean id="serverInLogger" class="org.openehealth.ipf.commons.ihe.ws.cxf.payload.InPayloadLoggerInterceptor">
        <constructor-arg value="#{@logFileNamePrefix}-server-in.txt" />
    </bean>

    <bean id="serverOutLogger" class="org.openehealth.ipf.commons.ihe.ws.cxf.payload.OutPayloadLoggerInterceptor">
        <constructor-arg value="#{@logFileNamePrefix}-server-out[partialResponse ? '-partial' : ''].txt" />
    </bean>
```

In this example, a common prefix for log file names is defined. Referencing the JVM property IPF_LOG_DIR gives an 
additional possibility for path customization by the user. 
The serverOutLogger interceptor is configured to distinguish between "normal" outbound messages and WS-Addressing
asynchronous acknowledgements — the latter will be stored in files with a special suffix "-partial".

The endpoint URI which uses these interceptors can look like:

```java
    from("xca-iti39:iti39service" +
         "?inInterceptors=#serverInLogger" +
         "&inFaultInterceptors=#serverInLogger" +
         "&outInterceptors=#serverOutLogger" +
         "&outFaultInterceptors=#serverOutLogger")
```

### Custom file name expression resolvers

The logger beans in the example above implicitly use the Spring Expression language, which requires you to add the following
runtime dependency to your `pom.xml` file:

```xml
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-expression</artifactId>
        <version>${spring.version}</version>
        <scope>runtime</scope>
    </dependency>
```

You can also provide your own implementation of `org.openehealth.ipf.commons.ihe.core.payload.ExpressionResolver` and
pass an instance with the constructors of the payload loggers instead.


[interceptors]: customInterceptors.html
[file-based payload logging]: ../ipf-platform-camel-ihe/commonPayloadLogging.html