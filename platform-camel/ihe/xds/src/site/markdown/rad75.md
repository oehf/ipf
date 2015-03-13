
## `xcai-rad75` component

The xcai-rad75 component provides interfaces for actors of the *Retrieve Imaging Document Set* IHE transaction (RAD-75),
which is described in the [IHE Radiology (RAD) Technical Framework, Vol. 3, Section 4.75.](http://ihe.net/uploadedFiles/Documents/Radiology/IHE_RAD_TF_Vol3.pdf).

### Actors

The transaction defines the following actors:

![RAD-75 actors](images/rad75.png)

Producer side corresponds to the *Imaging Document Consumer* actor.
Consumer side corresponds to the *Imaging Document Source* actor.

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

The endpoint URI format of `xcai-rad75` component producers is:

```
xcai-rad75://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `xcai-rad75` component consumers is:

```
xds-iti75:serviceName[?parameters]
```

The resulting URL of the exposed IHE Web Service endpoint depends on both the configuration of the [deployment container]
and the serviceName parameter provided in the Camel endpoint URI.

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /xcai/*
```

and serviceName equals to `rad75Service`, then the xcai-rad75 consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/xcai/rad75Service`

Additional URI parameters are optional and control special features as described in the corresponding section below.


### Example

This is an example on how to use the component on the consumer side:

```java
    from("xcai-rad75:rad75Service?audit=true")
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
* [Asynchronous Web Service exchange option]

### Advanced XDS Component Features

* [Handling extra query parameters and extra metadata elements]


[ATNA auditing]: ../ipf-platform-camel-ihe/atna.html
[Message validation]: ../ipf-platform-camel-ihe/messageValidation.html

[deployment container]: ../ipf-platform-camel-ihe-ws/deployment.html
[Secure Transport]: ../ipf-platform-camel-ihe-ws/secureTransport.html
[File-Based payload logging]: ../ipf-platform-camel-ihe-ws/payloadLogging.html

[Message types]: messageTypes.html
[Handling extra query parameters and extra metadata elements]: handlingExtra.html

[Handling Protocol Handlers]: ../ipf-platform-camel-ihe-ws/protocolHeaders.html
[Deploying custom CXF interceptors]: ../ipf-platform-camel-ihe-ws/customInterceptors.html
[Handling automatically rejected messages]: ../ipf-platform-camel-ihe-ws/handlingRejected.html
[Using CXF features]: ../ipf-platform-camel-ihe-ws/cxfFeatures.html
[Asynchronous Web Service exchange option]: ../ipf-platform-camel-ihe-ws/async.md



