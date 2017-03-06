
## `xds-iti14` component

The xds-iti14 component provides interfaces for actors of the *Register Document Set* IHE transaction (ITI-14),
which is described in the [IHE IT Infrastructure Technical Framework, Volume 2a , Section 3.14](https://ihe.net/uploadedFiles/Documents/ITI/IHE_ITI_TF_Vol2a.pdf).

### Actors

The transaction defines the following actors:

![ITI-14 actors](images/iti14.png)

Producer side corresponds to the *Document Repository* actor.
Consumer side corresponds to the *Document Registry* actor.

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

The endpoint URI format of `xds-iti14` component producers is:

```
xds-iti14://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `xds-iti14` component consumers is:

```
xds-iti14:serviceName[?parameters]
```

The resulting URL of the exposed IHE Web Service endpoint depends on both the configuration of the [deployment container]
and the serviceName parameter provided in the Camel endpoint URI.

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /xds/*
```

and serviceName equals to `iti14Service`, then the xds-iti14 consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/xds/iti14Service`

Additional URI parameters are optional and control special features as described in the corresponding section below.


### Example

This is an example on how to use the component on the consumer side:

```java
    from("xds-iti14:iti14Service?audit=true")
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

* [Handling Protocol Headers]
* [Deploying custom CXF interceptors]
* [Handling automatically rejected messages]
* [Using CXF features]

### Advanced XDS Component Features

* [Handling extra query parameters and extra metadata elements]


[ATNA auditing]: ../ipf-platform-camel-ihe/atna.html
[Message validation]: ../ipf-platform-camel-ihe/messageValidation.html

[deployment container]: ../ipf-platform-camel-ihe-ws/deployment.html
[Secure Transport]: ../ipf-platform-camel-ihe-ws/secureTransport.html
[File-Based payload logging]: ../ipf-platform-camel-ihe-ws/payloadLogging.html

[Message types]: messageTypes.html
[Handling extra query parameters and extra metadata elements]: handlingExtra.html

[Handling Protocol Headers]: ../ipf-platform-camel-ihe-ws/protocolHeaders.html
[Deploying custom CXF interceptors]: ../ipf-platform-camel-ihe-ws/customInterceptors.html
[Handling automatically rejected messages]: ../ipf-platform-camel-ihe-ws/handlingRejected.html
[Using CXF features]: ../ipf-platform-camel-ihe-ws/cxfFeatures.html




