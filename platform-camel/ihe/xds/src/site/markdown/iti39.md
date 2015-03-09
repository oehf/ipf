
## `xca-iti39` component

The xca-iti39 component provides interfaces for actors of the *Cross-Gateway Retrieve* IHE transaction (ITI-39),
which is described in the [IHE IT Infrastructure Technical Framework, Volume 2b , Section 3.39](http://ihe.net/uploadedFiles/Documents/ITI/IHE_ITI_TF_Vol2b.pdf).

### Actors

The transaction defines the following actors:

![ITI-39 actors](images/iti39.png)

Producer side corresponds to the *Initiating Gateway* actor.
Consumer side corresponds to the *Responding Gateway* actor.

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

The endpoint URI format of `xca-iti39` component producers is:

```
xca-iti39://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `xca-iti39` component consumers is:

```
xca-iti39:serviceName=?homeCommunityId=<homeCommunityId>[&parameters]
```

The resulting URL of the exposed IHE Web Service endpoint depends on both the configuration of the [deployment container]
and the serviceName parameter provided in the Camel endpoint URI.

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /xca/*
```

and serviceName equals to `iti39Service`, then the xca-iti39 consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/xca/iti39Service`

Additional URI parameters are optional and control special features as described in the corresponding section below.


### Example

This is an example on how to use the component on the consumer side:

```java
from("xca-iti39:iti39Service?homeCommunityId=1.2.3.4&audit=true")
  .process(myProcessor)
  // process the incoming request and create a response
```

### Remarks for this component

This transaction sends document content as part of its request or response messages. In practice such messages can become
quite large. To allow for memory-efficient streaming of the document content, the aforementioned components rely on
[Apache CXF](http://cxf.apache.org/) support for binary data.

CXF streams the content on disk and then provides a `DataHandler` to access the file.
Furthermore, CXF offers some [environment properties](http://cxf.apache.org/docs/security.html#Security-Largedatastreamcaching)
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

