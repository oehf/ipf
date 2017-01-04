
## Deployment of Web Service-based IPF IHE consumer endpoints

Every project that exposes consumer endpoints of Web Service-based IPF IHE components needs to configure a web application
container for them. Currently the following containers have been tested:

* Standalone Apache Tomcat
* Embedded in Spring Boot

Neccessary configuration steps for all these variants will be described in corresponding sections below.

### Standalone Apache Tomcat

To make the IPF application deployable in the Apache Tomcat servlet container, a deployment descriptor web.xml
must contain a reference to the application's Spring context descriptor and include the CXF servlet.
Here is an example:

```

    <?xml version="1.0"?>
    <!DOCTYPE web-app PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 3.0//EN">

    <web-app>
      <display-name>Test IPF IHE Web-App</display-name>
      <context-param>
        <!-- configures the classpath of the Spring application context -->
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:path/to/your/context.xml</param-value>
      </context-param>

      <listener>
        <listener-class>
          org.springframework.web.context.ContextLoaderListener
        </listener-class>
      </listener>
      <servlet>
        <servlet-name>CXFServlet</servlet-name>
        <servlet-class>
            org.apache.cxf.transport.servlet.CXFServlet
        </servlet-class>
      </servlet>
      <servlet-mapping>
        <!-- configures the address of the servlet path under which our web services are published -->
        <servlet-name>CXFServlet</servlet-name>
        <url-pattern>/services/*</url-pattern>
      </servlet-mapping>
    </web-app>

```


### Embedded in Spring Boot

Container deployments embedded in [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-embedded-servlet-containers.html)
can be easily achieved by depending on [ipf-xds-spring-boot-starter](../ipf-xds-spring-boot-starter/index.html).
This starter module sets up the necessary servlets and the servlet init parameters are mapped to application properties.

Note that Spring Boot supports Tomcat, Jetty and Undertow as servlet implementations.
