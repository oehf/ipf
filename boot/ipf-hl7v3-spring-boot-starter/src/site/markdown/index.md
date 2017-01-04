## Spring Boot HL7v3 support

`ipf-hl7v3-spring-boot-starter` sets up the infrastructure for HL7v3-based IHE transactions
 
The dependency on the IPF [Spring Boot] IHE HL7v3 starter module is:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.boot</groupId>
        <artifactId>ipf-hl7v3-spring-boot-starter</artifactId>
    </dependency>
```


`ipf-hl7v3-spring-boot-starter` auto-configures ATNA auditor beans for all HL7v3-based IHE transactions. 

This starter module also transitively depends on `cxf-spring-boot-starter-jaxws`(http://cxf.apache.org/docs/springboot.html) that sets up the CXF
web service stack, so you don't have to care about this anymore.

Furthermore, if a single `org.springframework.cache.CacheManager` bean is available and the application
property `ipf.hl7v3.caching` is set to true, the following caching storage beans are set up:

* `cachingAsynchronyCorrelator` for interactive continuation

The actual [cache implementation](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-caching.html) 
being used is the one that Spring Boot finds on the classpath.

`ipf-hl7v3-spring-boot-starter` provides the following application properties:

| Property (`ipf.hl7v3.`)     | Default        | Description                                         |
|----------------------------|-----------------|-----------------------------------------------------|
| `caching`                  | false           | Whether to set up a cache for paging requests

[Spring Boot]: http://projects.spring.io/spring-boot/
