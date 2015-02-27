
## `pixv3-iti44 / xds-iti44` component

The pixv3-iti44 / xds-iti44 component provides interfaces for actors of the *Patient Identity Feed v3* IHE transaction (ITI-44),
which is described in the [IHE IT Infrastructure Technical Framework, Volume 2b , Section 3.44](http://ihe.net/uploadedFiles/Documents/ITI/IHE_ITI_TF_Vol2b.pdf).

This transaction is defined in both PIXv3 and XDS IHE profiles, therefore there are two Camel components.
Their only differences relate to naming conventions and namespaces in WSDL documents.

### Actors

The transaction defines the following actors:

![ITI-44 actors](images/iti44.png)

Producer side corresponds to the *Patient Identity Source* actor.
Consumer side corresponds to the *Patient Identifier Cross-reference Manager* and *XDS Document Registry* actors.

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

The endpoint URI format of `pixv3-iti44 / xds-iti44` component producers is:

```
pixv3-iti44://hostname:port/path/to/service[?parameters]
xds-iti44://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `pixv3-iti44 / xds-iti44` component consumers is:

```
pixv3-iti44:serviceName[?parameters]
xds-iti44:serviceName[?parameters]
```

The resulting URL of the exposed IHE Web Service endpoint depends on both the configuration of the [deployment container]
and the serviceName parameter provided in the Camel endpoint URI.

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /pixv3/*
```

and serviceName equals to `iti44Service`, then the pixv3-iti44 consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/pixv3/iti44Service`

Additional URI parameters are optional and control special features as described in the corresponding section below.


### Example

This is an example on how to use the component on the consumer side:

```java
from("pixv3-iti44:iti44Service?audit=true")
  .process(myProcessor)
  // process the incoming request and create a response
```


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




