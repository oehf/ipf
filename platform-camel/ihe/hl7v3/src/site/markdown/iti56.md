
## `xcpd-iti56` component

The xcpd-iti56 component provides interfaces for actors of the *Cross-Gateway Patient Location Query* IHE transaction (ITI-56),
which is described in the [IHE IT Infrastructure Technical Framework, Volume 2b , Section 3.56](http://ihe.net/uploadedFiles/Documents/ITI/IHE_ITI_TF_Vol2b.pdf).

### Actors

The transaction defines the following actors:

![ITI-56 actors](images/iti56.png)

Producer side corresponds to the *Patient Demographics Consumer* actor.
Consumer side corresponds to the *Patient Demographics Supplier* actor.

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

The endpoint URI format of `xcpd-iti56` component producers is:

```
xcpd-iti56://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `xcpd-iti56` component consumers is:

```
xcpd-iti56:serviceName[?parameters]
```

The resulting URL of the exposed IHE Web Service endpoint depends on both the configuration of the [deployment container]
and the serviceName parameter provided in the Camel endpoint URI.

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /xcpd/*
```

and serviceName equals to `iti56Service`, then the xcpd-iti56 consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/xcpd/iti56Service`

Additional URI parameters are optional and control special features as described in the corresponding section below.


### Example

This is an example on how to use the component on the consumer side:

```java
from("xcpd-iti56:iti56Service?audit=true")
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
* [Asynchronous Web Service exchange option]


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
[Asynchronous Web Service exchange option]: ../ws/async.md




