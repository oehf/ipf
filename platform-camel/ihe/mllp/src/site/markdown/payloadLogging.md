
## File-based Logging of MLLP Message Payload

The general functionality of [file-based payload logging] is provided for MLLP endpoints 
in form of a set of [interceptors] located within the package `org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept`,
which have to be deployed on the endpoints:

* `...consumer.ConsumerInPayloadLoggerInterceptor` for inbound messages on consumer side,
* `...consumer.ConsumerOutPayloadLoggerInterceptor` for outbound messages on consumer side,
* `...producer.ProducerInPayloadLoggerInterceptor` for inbound messages on producer side,
* `...producer.ProducerOutPayloadLoggerInterceptor` for outbound messages on producer side,

    
### Example

This is a Spring configuration fragment that defines a set of parameterized interceptor beans:

```xml
<bean id="logFileNamePrefix" class="java.lang.String">
    <constructor-arg value="#{systemProperties['IPF_LOG_DIR']}/[processId]/[date('yyyyMMdd-HH00')]/[sequenceId]" />
</bean>
 
<bean id="serverInLogger" scope="prototype"
      class="org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerInPayloadLoggerInterceptor">
    <constructor-arg value="#{@logFileNamePrefix}-server-in.txt" />
    <property name="locallyEnabled" value="true" />
</bean>
 
<bean id="serverOutLogger" scope="prototype"
      class="org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer.ConsumerOutPayloadLoggerInterceptor">
    <constructor-arg value="#{@logFileNamePrefix}-server-out.txt" />
</bean>
 
<bean id="clientInLogger" scope="prototype"
      class="org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerInPayloadLoggerInterceptor">
    <constructor-arg value="#{@logFileNamePrefix}-client-in.txt" />
</bean>
 
<bean id="clientOutLogger" scope="prototype"
      class="org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer.ProducerOutPayloadLoggerInterceptor">
    <constructor-arg value="#{@logFileNamePrefix}-client-out.txt" />
</bean>
```

In this example, a common prefix for log file names is defined. Referencing the JVM property IPF_LOG_DIR gives an 
additional possibility for path customization by the user. 
The consumer endpoint URI which uses these interceptors may look like:

```java
from("pdq-iti21://localhost:12354&interceptors=#serverInLogger,#serverOutLogger")
    .....
```


[interceptors]: interceptorChain.html
[file-based payload logging]: ../commonPayloadLogging.html