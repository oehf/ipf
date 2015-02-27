
## `xds-iti42` component

The xds-iti42 component provides interfaces for actors of the *Register Document Set.b* IHE transaction (ITI-42),
which is described in the [IHE IT Infrastructure Technical Framework, Volume 2b , Section 3.42](http://ihe.net/uploadedFiles/Documents/ITI/IHE_ITI_TF_Vol2b.pdf).

### Actors

The transaction defines the following actors:

![ITI-42 actors](images/iti42.png)

Producer side corresponds to the *Document Repository* actor.
Consumer side corresponds to the *Document Registry* actor.

### Dependencies

In a Maven-based environment, the following dependency should be registered in `pom.xml`:

```xml
<dependency>
    <groupId>org.openehealth.ipf.platform-camel</groupId>
    <artifactId>ipf-platform-camel-ihe-xds</artifactId>
    <version>${ipf-version}</version>
</dependency>
```

### Endpoint URI Format

#### Producer

The endpoint URI format of `xds-iti42` component producers is:

```
xds-iti42://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `xds-iti42` component consumers is:

```
xds-iti42:serviceName[?parameters]
```

The resulting URL of the exposed IHE Web Service endpoint depends on both the configuration of the [deployment container]
and the serviceName parameter provided in the Camel endpoint URI.

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /xds/*
```

and serviceName equals to `iti42Service`, then the xds-iti42 consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/xds/iti42Service`

Additional URI parameters are optional and control special features as described in the corresponding section below.


### Example

This is an example on how to use the component on the consumer side:

```java
from("xds-iti42:iti42Service?audit=true")
  .process(myProcessor)
  // process the incoming request and create a response
```


### Basic Common Component Features

* [ATNA auditing]
* [Message validation]

### Basic Web Service Component Features

* [Secure transport]
* [File-Based payload logging]

### Basic XDS Component Features

* [Message types]

### Advanced Web Service Component Features

* [Handling Protocol Handlers]
* [Deploying custom CXF interceptors]
* [Handling automatically rejected messages]
* [Using CXF features]

### Advanced XDS Component Features

* [Handling extra query parameters and extra metadata elements]


[ATNA auditing]: ../atna.html
[Message validation]: ../messageValidation.html

[deployment container]: ../ws/deployment.html
[Secure Transport]: ../ws/secureTransport.html
[File-Based payload logging]: ../ws/payloadLogging.html

[Message types]: messageTypes.html
[Handling extra query parameters and extra metadata elements]: handlingExtra.html

[Handling Protocol Handlers]: ../ws/protocolHeaders.html
[Deploying custom CXF interceptors]: ../ws/customInterceptors.html
[Handling automatically rejected messages]: ../handlingRejected.html
[Using CXF features]: ../ws/cxfFeatures.html




