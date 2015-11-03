
## File-based Logging of MLLP Message Payload

The general functionality of [file-based payload logging] is provided for MLLP endpoints 
in form of a set of [interceptors] located within the package `org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept`,
which have to be deployed on the endpoints:

* `...consumer.ConsumerInPayloadLoggerInterceptor` for inbound messages on consumer side
* `...consumer.ConsumerOutPayloadLoggerInterceptor` for outbound messages on consumer side
* `...producer.ProducerInPayloadLoggerInterceptor` for inbound messages on producer side
* `...producer.ProducerOutPayloadLoggerInterceptor` for outbound messages on producer side

These interceptors come with their respective `Hl7v2InterceptorFactory` as inner classes, see the examples below.
    
### Example

This is a Spring configuration fragment that defines a set of parameterized interceptor beans:

```xml
    <!-- Uses Spring Expression Language -->
    <bean id="logFileNamePrefix" class="java.lang.String">
        <constructor-arg value="#{systemProperties['IPF_LOG_DIR']}/[processId]/[date('yyyyMMdd-HH00')]/[sequenceId]" />
    </bean>

    <bean id="serverInLogger" 
          class="org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerInPayloadLoggerInterceptor$Factory">
        <constructor-arg value="#{@logFileNamePrefix}-server-in.txt" />
        <property name="locallyEnabled" value="true" />
    </bean>

    <bean id="serverOutLogger" 
          class="org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerOutPayloadLoggerInterceptor$Factory">
        <constructor-arg value="#{@logFileNamePrefix}-server-out.txt" />
    </bean>

    <bean id="clientInLogger" 
          class="org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerInPayloadLoggerInterceptor$Factory">
        <constructor-arg value="#{@logFileNamePrefix}-client-in.txt" />
    </bean>

    <bean id="clientOutLogger"
          class="org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerOutPayloadLoggerInterceptor$Factory">
        <constructor-arg value="#{@logFileNamePrefix}-client-out.txt" />
    </bean>
```

In this example, a common prefix for log file names is defined. Referencing the JVM property IPF_LOG_DIR gives an 
additional possibility for path customization by the user. 
The consumer endpoint URI which uses these interceptors may look like:

```java
    from("pdq-iti21://localhost:12354&interceptorFactories=#serverInLogger,#serverOutLogger")
        .....
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


[interceptors]: interceptorChain.html
[file-based payload logging]: ../ipf-platform-camel-ihe/commonPayloadLogging.html