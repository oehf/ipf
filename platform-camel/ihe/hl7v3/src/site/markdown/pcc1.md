
## `qed-pcc1` component

The qed-pcc1 component provides interfaces for actors of the *Query for Existing Data* IHE transaction (PCC-1),
which is described in the [IHE QED Supplement, Section 3.1](http://www.ihe.net/Technical_Framework/upload/IHE_PCC_Query_for_Existing_Data_QED_Supplement_TI_2008-08-22.pdf).

### Actors

The transaction defines the following actors:

![PCC-1 actors](images/pcc1.png)

Producer side corresponds to the *Clinical Data Consumer* actor.
Consumer side corresponds to the *Clinical Data Source* actor.

### Dependencies

In a Maven-based environment, the following dependency should be registered in `pom.xml`:

```xml
<dependency>
    <groupId>org.openehealth.ipf.platform-camel</groupId>
    <artifactId>ipf-platform-camel-ihe-hl7v3</artifactId>
    <version>${ipf-version}</version>
</dependency>
```

### Endpoint URI Format

#### Producer

The endpoint URI format of `qed-pcc1` component producers is:

```
qed-pcc1://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `qed-pcc1` component consumers is:

```
qed-pcc1:serviceName[?parameters]
```

The resulting URL of the exposed IHE Web Service endpoint depends on both the configuration of the [deployment container]
and the serviceName parameter provided in the Camel endpoint URI.

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /qed/*
```

and serviceName equals to `pcc1Service`, then the qed-pcc1 consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/qed/pcc1Service`

Additional URI parameters are optional and control special features as described in the corresponding section below.


### Example

This is an example on how to use the component on the consumer side:

```java
from("qed-pcc1:pcc1Service?audit=true")
  .process(myProcessor)
  // process the incoming request and create a response
```

### Remarks for this component

The original IHE specification does not define ATNA audit records for QED. Therefore IPF uses own definitions,
which are described in the attached [document](docs/QED-ATNA-Structures.docx).


### Basic Common Component Features

* [ATNA auditing]
* [Message validation]

### Basic Web Service Component Features

* [Secure transport]
* [File-Based payload logging]

### Basic HL7v3 Component Features

* [Message types]

### Advanced Web Service Component Features

* [Handling Protocol Handlers]
* [Deploying custom CXF interceptors]
* [Handling automatically rejected messages]
* [Using CXF features]


[ATNA auditing]: ../atna.html
[Message validation]: ../messageValidation.html

[deployment container]: ../ws/deployment.html
[Secure Transport]: ../ws/secureTransport.html
[File-Based payload logging]: ../ws/payloadLogging.html

[Message types]: messageTypes.html

[Handling Protocol Handlers]: ../ws/protocolHeaders.html
[Deploying custom CXF interceptors]: ../ws/customInterceptors.html
[Handling automatically rejected messages]: ../handlingRejected.html
[Using CXF features]: ../ws/cxfFeatures.html




