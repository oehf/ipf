
## `pixm-iti83` component

The pixm-iti83 component provides interfaces for actors of the *Patient Identifier Cross-reference for Mobile* IHE transaction (ITI-83),
which is described in the [Patient Identifier Cross-reference for Mobile (PIXm) Supplement](http://www.ihe.net/uploadedFiles/Documents/ITI/IHE_ITI_Suppl_PIXm.pdf).

### Actors

The transaction defines the following actors:

![ITI-83 actors](images/iti83.png)

Producer side corresponds to the *Patient Identifier Cross-reference Consumer* actor.
Consumer side corresponds to the *Patient Identifier Cross-reference Manager* actor.

### Dependencies

In a Maven-based environment, the following dependency must be registered in `pom.xml`:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.platform-camel</groupId>
        <artifactId>ipf-platform-camel-ihe-fhir-pixpdq</artifactId>
        <version>${ipf-version}</version>
    </dependency>
```

### Endpoint URI Format

#### Producer

The endpoint URI format of `pixm-iti83` component producers is:

```
pixm-iti83://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `pixm-iti83` component consumers is:

```
pixm-iti83:serviceName[?parameters]
```

The resulting URL of the exposed FHIR REST Service endpoint depends on the configuration of the [deployment container].

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /fhir/*
```

then the pixm-iti83 consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/fhir/Patient`

Additional URI parameters are optional and control special features as described in the corresponding section below.



### Example

This is an example on how to use the component on the consumer side:

```java
    from("pixm-iti83:pixmservice?audit=true")
      .process(myProcessor)
      // process the incoming request and create a response
```

### Translation into PIX Query

IPF comes with [translators](translation.html) to translate ITI-83 requests into ITI-9 requests and vice versa for the responses.

### Basic Common Component Features

* [ATNA auditing]

### Basic FHIR Component Features

* [Message types and exception handling]
* [Security]

### Connection-related FHIR Component Features

* [Connection parameters]

[ATNA auditing]: ../ipf-platform-camel-ihe/atna.html
[Message types and exception handling]: ../ipf-platform-camel-ihe-fhir-core/messageTypes.html
[Security]: ../ipf-platform-camel-ihe-fhir-core/security.html
[Connection parameters]: ../ipf-platform-camel-ihe-fhir-core/connection.html

[deployment container]: ../ipf-platform-camel-ihe-fhir-core/deployment.html

