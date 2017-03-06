
## `xds-iti43` component

The xds-iti43 component provides interfaces for actors of the *Retrieve Document Set.b* IHE transaction (ITI-43),
which is described in the [IHE IT Infrastructure Technical Framework, Volume 2b , Section 3.43]((https://ihe.net/uploadedFiles/Documents/ITI/IHE_ITI_TF_Vol2b.pdf).

This component also provides support for the On-Demand XDS Documents option described in the corresponding
[IHE ITI Supplement](http://www.ihe.net/Technical_Framework/upload/IHE_ITI_Suppl_On_Demand_Documents_Rev1-2_TI_2011-08-19.pdf).

### Actors

The transaction defines the following actors:

![ITI-43 actors](images/iti43.png)

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

The endpoint URI format of `xds-iti43` component producers is:

```
xds-iti43://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `xds-iti43` component consumers is:

```
xds-iti43:serviceName[?parameters]
```

The resulting URL of the exposed IHE Web Service endpoint depends on both the configuration of the [deployment container]
and the serviceName parameter provided in the Camel endpoint URI.

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /xds/*
```

and serviceName equals to `iti43Service`, then the xds-iti43 consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/xds/iti43Service`

Additional URI parameters are optional and control special features as described in the corresponding section below.


### Example

This is an example on how to use the component on the consumer side:

```java
    from("xds-iti43:iti43Service?audit=true")
      .process(myProcessor)
      // process the incoming request and create a response
```


### Remarks for this component

This transaction sends document content as part of its request or response messages. In practice such messages can become
quite large. To allow for memory-efficient streaming of the document content, the aforementioned components rely on
[Apache CXF](https://cxf.apache.org/) support for binary data.

CXF streams the content on disk and then provides a `DataHandler` to access the file.
Furthermore, CXF offers some [environment properties](https://cxf.apache.org/docs/security.html#Security-Largedatastreamcaching)
which can be used to configure this content caching.


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




