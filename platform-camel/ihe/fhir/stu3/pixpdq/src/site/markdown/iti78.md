
## `pdqm-iti78` component

The pdqm-iti78 component provides interfaces for actors of the *Patient Demographics Query for Mobile* IHE transaction (ITI-78),
which is described in the [Patient Demographics Query for Mobile (PDQm) Supplement](https://www.ihe.net/uploadedFiles/Documents/ITI/IHE_ITI_Suppl_PDQm.pdf).

Note that this implementation is a *projection* of what PDQm could look like once it has been ported to FHIR DSTU2.
### Actors

The transaction defines the following actors:

![ITI-78 actors](images/iti78.png)

Producer side corresponds to the *Patient Demographics Consumer* actor.
Consumer side corresponds to the *Patient Demographics Supplier* actor.

### Dependencies

In a Maven-based environment, the following dependency must be registered in `pom.xml`:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.platform-camel</groupId>
        <artifactId>ipf-platform-camel-ihe-fhir-dstu2-pixpdq</artifactId>
        <version>${ipf-version}</version>
    </dependency>
```

### Endpoint URI Format

#### Producer

The endpoint URI format of `pdqm-iti78` component producers is:

```
pdqm-iti78://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `pdqm-iti78` component consumers is:

```
pdqm-iti78:serviceName[?parameters]
```

The resulting URL of the exposed FHIR REST Service endpoint depends on the configuration of the [deployment container].

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /fhir/*
```

then the pdqm-iti78 consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/fhir/Patient`

Additional URI parameters are optional and control special features as described in the corresponding section below.


### Example

This is an example on how to use the component on the consumer side:

```java
    from("pdqm-iti78:pdqmservice?audit=true")
      .process(myProcessor)
      // process the incoming request and create a response
```

### Translation into PDQ

IPF comes with [translators](translation.html) to translate ITI-78 requests into ITI-21 requests and vice versa for the responses.

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

