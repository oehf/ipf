
## Deployment of FHIR-based IPF IHE consumer endpoints

Every project that exposes consumer endpoints of FHIR-based IHE components needs to configure a web application
container for them. Currently the following containers have been tested:

* Standalone Apache Tomcat
* Embedded Apache Tomcat
* Jetty

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


The following servlet parameters are supported:

| Parameter name       | Type       | Default value | Short description                                                                    |
|:---------------------|:-----------|:--------------|:-------------------------------------------------------------------------------------|
| `logging`            | Boolean    | false         | writes incoming requests into the log
| `highlight`          | Boolean    | false         | whether responses to requests from browsers are (syntax-)highlighted
| `pretty`             | Boolean    | false         | whether responses are indented
| `pagingProviderSize` | Integer    | 50            | amount of [paging requests] being maintained concurrently
| `defaultPageSize`    | Integer    | 20            | default page size of returned resources
| `maximumPageSize`    | Integer    | 100           | maximum page size of returned resources


### Embedded Apache Tomcat

TODO

### Jetty

TODO


[paging requests]: cachingAndPaging.html