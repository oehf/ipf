
## Deployment of FHIR-based IPF IHE consumer endpoints

Every project that exposes consumer endpoints of FHIR-based IHE components needs to configure a web application
container for them. Currently the following containers have been tested:

* Standalone Apache Tomcat
* Embedded in Spring Boot

Neccessary configuration steps for all these variants will be described in corresponding sections below.

### Standalone Apache Tomcat

To make the IPF application deployable in the Apache Tomcat servlet container, a deployment descriptor web.xml
must include the FHIR servlet and servlet-mapping. With recent Servlet specifications, you can also put
a corresponding `web-fragments.xml` file into the classpath.

Here is an example:

```

    <?xml version="1.0"?>
    <!DOCTYPE web-app PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 3.0//EN">

    <web-app>
        <display-name>Test IPF IHE Web-App</display-name>

        <!-- Servlet for FHIR  -->
        <servlet>
            <servlet-name>FhirServlet</servlet-name>
            <servlet-class>org.openehealth.ipf.commons.ihe.fhir.CamelFhirServlet</servlet-class>
            <init-param>
                <param-name>logging</param-name>
                <param-value>true</param-value>
            </init-param>
            <init-param>
                <param-name>highlight</param-name>
                <param-value>true</param-value>
            </init-param>
        </servlet>
    
        <!-- Camel Endpoint Resource Mapping, should be FhirServlet -->
        <servlet-mapping>
            <servlet-name>FhirServlet</servlet-name>
            <url-pattern>/fhir/*</url-pattern>
        </servlet-mapping>
    
    </web-app>

```


The following servlet init parameters are supported:

| Parameter name       | Type       | Default value | Short description                                                                    |
|:---------------------|:-----------|:--------------|:-------------------------------------------------------------------------------------|
| `logging`            | Boolean    | false         | writes incoming requests into the log
| `highlight`          | Boolean    | false         | whether responses to requests from browsers are (syntax-)highlighted
| `pretty`             | Boolean    | false         | whether responses are indented
| `pagingProviderSize` | Integer    | 50            | amount of [paging requests] being maintained concurrently
| `defaultPageSize`    | Integer    | 20            | default page size of returned resources
| `maximumPageSize`    | Integer    | 100           | maximum page size of returned resources


A special case is the [ITI-68](../ipf-platform-camel-ihe-fhir-mhd/iti68.html) transaction. This is no FHIR
transaction as such but just a HTTP(S) download. Therefore, instead of being routed over the `FhirServlet`
this transaction is served by a `CamelServlet` as provided by Camel's [Servlet component](http://camel.apache.org/servlet.html):

```

<servlet>
   <servlet-name>CamelServlet</servlet-name>
   <display-name>Camel Http Transport Servlet</display-name>
   <servlet-class>org.apache.camel.component.servlet.CamelHttpTransportServlet</servlet-class>
   <load-on-startup>1</load-on-startup>
 </servlet>

<servlet-mapping>
    <servlet-name>CamelServlet</servlet-name>
    <url-pattern>/binary/*</url-pattern>
</servlet-mapping>

```

The servlet definition above would match the following consumer endpoint:

```
   from("mhd-iti68://binary[?options])
```



### Embedded in Spring Boot

Container deployments embedded in [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-embedded-servlet-containers.html)
can be easily achieved by depending on [ipf-fhir-spring-boot-starter](../ipf-fhir-spring-boot-starter/index.html).
This starter module along with `camel-servlet-starter` sets up the necessary servlets and the servlet init parameters are mapped to
application properties.

Note that Spring Boot supports Tomcat, Jetty and Undertow as servlet implementations.


[paging requests]: cachingAndPaging.html