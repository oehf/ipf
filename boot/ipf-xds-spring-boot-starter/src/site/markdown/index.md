## Spring Boot XDS support

`ipf-xds-spring-boot-starter` sets up the infrastructure for XDS-based IHE transactions
 
The dependency on the IPF [Spring Boot] IHE XDS starter module is:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.boot</groupId>
        <artifactId>ipf-xds-spring-boot-starter</artifactId>
    </dependency>
```


`ipf-xds-spring-boot-starter` autoconfigures ATNA auditor beans for all XDS-based IHE transactions.

Furthermore, if a single `org.springframework.cache.CacheManager` bean is available and the application
property `ipf.xds.caching` is set to true, the following caching beans are set up:

* `cachingAsynchronyCorrelator` for [Asynchronous Web Service exchange option](../ipf-platform-camel-ihe-ws/async.html)

The actual [cache implementation](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-caching.html) 
being used is the one that Spring Boot finds on the classpath.

`ipf-xds-spring-boot-starter` provides the following application properties:

| Property (`ipf.xds.`)     | Default                | Description                                         |
|----------------------------|-----------------------|-----------------------------------------------------|
| `caching`                  | false                 | Whether to set up a cache for Asynchronous Web Service exchange


This starter module also transitively depends on [cxf-spring-boot-starter-jaxws](http://cxf.apache.org/docs/springboot.html) that sets up the CXF
web service stack including the Camel CXF servlet, so you don't have to care about this anymore.

`cxf-spring-boot-starter-jaxws` provides the following application properties:

| Property (`cxf.`)          | Default                | Description                                         |
|----------------------------|------------------------|-----------------------------------------------------|
| `path`                     | /services              | Path that serves as the base URI for the services
| `servlet.init`             | empty map              | optional servlet init parameters
| `servlet.load-on-startup`  | -1                     | startup order


[Spring Boot]: http://projects.spring.io/spring-boot/
