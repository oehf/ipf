
## `qedm-pcc44` component

The qedm-pcc44 component provides interfaces for actors of the *Query for Existing Data Mobile* IHE transaction (PCC-44),
which is described in the [QEDm Supplement](http://ihe.net/uploadedFiles/Documents/PCC/IHE_PCC_Suppl_QEDm.pdf).

### Actors

The transaction defines the following actors:

![PCC-44 actors](images/pcc44.png)

Producer side corresponds to the *Document Consumer* actor.
Consumer side corresponds to the *Document Responder* actor.

### Dependencies

In a Maven-based environment, the following dependency must be registered in `pom.xml`:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.platform-camel</groupId>
        <artifactId>ipf-platform-camel-ihe-fhir-stu3-qedm</artifactId>
        <version>${ipf-version}</version>
    </dependency>
```

### Endpoint URI Format

#### Producer

The endpoint URI format of `qedm-pcc44` component producers is:

```
qedm-pcc44://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `qedm-pcc44` component consumers is:

```
qedm-pcc44:serviceName[?parameters]
```

The resulting URL of the exposed FHIR REST Service endpoint depends on the configuration of the [deployment container].

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /fhir/*
```

then the qedm-pcc44 consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/fhir/<resource type>`

Additional URI parameters are optional and control special features as described in the corresponding section below.


### Transaction Options

This transaction defines the following options; at least one of them must be chosen to be supported:
 
 * OBSERVATIONS
 * ALLERGIES
 * CONDITIONS
 * DIAGNOSTIC_REPORTS
 * MEDICATIONS
 * IMMUNIZATIONS
 * PROCEDURES
 * ENCOUNTERS
 * ALL (default)

*Note*: the PROVENANCE option is not yet supported

The options are represented as enumeration in the class [Pcc44Options](../apidocs/org/openehealth/ipf/platform/camel/ihe/fhir/pcc44/Pcc44Options.html).

Support for one or more options is configured using the optional `options` parameter, followed by a comma-separated list of
options to be supported. If `options` is provided, the default option (see above) is used.
The endpoint will reject resource types that are outside the scope of the configured option.

### Example

This is an example on how to use the component on the consumer side:

```java
    from("qedm-pcc44:qedmservice?audit=true&options=OBSERVATIONS")
      .process(myProcessor)
      // process the incoming request and create a response
```


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

