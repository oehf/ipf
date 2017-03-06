## Performance Monitoring

Apache Camel provides performance monitoring out-of-the-box, by their generic [JMX support](https://camel.apache.org/camel-jmx.html)
and by a dedicated [camel-metrics](https://camel.apache.org/metrics-component.html) component.

The following example briefly describes how to set up performance monitoring for a IPF consumer route.

Simply add a [Route Policy](https://camel.apache.org/routepolicy.html) bean for each route which exposes route utilization statistics.
You can also define a `MetricsRoutePolicyFactory` for monitoring *all* routes.

```xml
    ...
    <!-- use camel-metrics route policy to gather metrics -->
    <bean id="metricsRoutePolicy" class="org.apache.camel.component.metrics.routepolicy.MetricsRoutePolicy">
        <!-- fine grained monitoring -->
        <property name="useJmx" value="true"/>
        <property name="jmxDomain" value="org.openehealth.ipf.metrics"/>
    </bean>
    ...
```

Refer to the route policy in all route you wish to monitor:

```java

    // Now this route is performance monitored
    from("pix-iti8:0.0.0.0:3700?secure=true")
        .routePolicyRef("metricsRoutePolicy")
        ...
```
