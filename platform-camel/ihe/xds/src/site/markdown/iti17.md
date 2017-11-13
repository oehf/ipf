
## `xds-iti17` component

The xds-iti17 component provides interfaces for actors of the XDS.a *Retrieve Document* IHE transaction (ITI-17),
which is described in the [IHE IT Infrastructure Technical Framework, Volume 2a , Section 3.17](https://ihe.net/uploadedFiles/Documents/ITI/IHE_ITI_TF_Vol2a.pdf).

As opposed to the other transactions from the XDS profile, ITI-17 uses "pure" HTTP GET communication instead of Web Services.

### Actors

The transaction defines the following actors:

![ITI-17 actors](images/iti17.png)

Producer side corresponds to the *Document Consumer* actor.
Consumer side corresponds to the *Document Repository* actor.

### Dependencies

In a Maven-based environment, the following dependency must be registered in `pom.xml`:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.platform-camel</groupId>
        <artifactId>ipf-platform-camel-ihe-xds</artifactId>
        <version>${ipf-version}</version>
    </dependency>
```

### Endpoint URI Format

#### Producer

The endpoint URI format of `xds-iti17` component producers is:

```
xds-iti17://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `xds-iti17` component consumers is:

```
xds-iti17:serviceName[?parameters]
```

Same as for Web Service-based transactions, a deployment container is required. The IPF provides a special servlet class
for ITI-17 consumers. The sample web application descriptor below shows how to use this servlet along with the one for Web Services:

```xml

    <?xml version="1.0"?>
    <!DOCTYPE web-app PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
         ">

    <web-app>
      <display-name>Test IPF IHE Web-App</display-name>
      <context-param>
        <!-- configures the classpath of the Spring application context -->
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:example/context.xml</param-value>
      </context-param>

      <listener>
        <listener-class>
          org.springframework.web.context.ContextLoaderListener
        </listener-class>
      </listener>

      <servlet>
        <!-- Servlet used for all CXF web services -->
        <servlet-name>CXFServlet</servlet-name>
        <servlet-class>
            org.apache.cxf.transport.servlet.CXFServlet
        </servlet-class>
      </servlet>

      <servlet>
        <!-- Servlet used only for ITI-17 -->
        <servlet-name>Iti17Servlet</servlet-name>
        <servlet-class>
            org.openehealth.ipf.platform.camel.ihe.xds.iti17.servlet.Iti17Servlet
        </servlet-class>
      </servlet>

      <servlet-mapping>
        <!-- configures the address of the servlet path under which our web services are published -->
        <servlet-name>CXFServlet</servlet-name>
        <url-pattern>/services/*</url-pattern>
      </servlet-mapping>

      <servlet-mapping>
        <!-- configures the address of the servlet path under which the ITI-17 transaction is published -->
        <servlet-name>Iti17Servlet</servlet-name>
        <url-pattern>/iti17/*</url-pattern>
      </servlet-mapping>
    </web-app>

```

With this configuration, the ITI-17 endpoint will be accessible under the URL `http://host:port/webcontext/iti17/myIti17Service`.
Note that the servlet path (`iti17`) is different to that of other transactions (services) that uses the CXFServlet.

Additional URI parameters are optional and control special features as described in the corresponding section below.


### Message Types

The request message is supposed to be a `String` containing additional path elements of the document to be retrieved and
will be appended to the endpoint URI. Its exact structure depends on the implementation of the Repository actor.
It can be a path, a query, or a combination of both:

| Document URL                                                           | ITI-17 input message body
|------------------------------------------------------------------------|----------------------------
| `http://host:port/webcontext/iti17/myIti17Service/my/path/to/document` | `/my/path/to/document`
| `http://host:port/webcontext/iti17/myIti17Service/docId=123`           | `?docId=123`
| `http://host:port/webcontext/iti17/myIti17Service/my/path?docId=321`   | `/my/path?docId=321`

Successful response messages represent `InputStream` instances with the contents of the requested document.
Streaming capabilities are provided out-of-box


### Example

This is an example on how to use the component on the consumer side:

```java
    from("xds-iti17:iti17Service?audit=true")
      .process(myProcessor)
      // process the incoming request and create a response
```


### Basic Common Component Features

* [ATNA auditing]
* [Secure transport]


[ATNA auditing]: ../ipf-platform-camel-ihe/atna.html
[Secure Transport]: ../ipf-platform-camel-ihe-ws/secureTransport.html

