
## `pcd-pcd01` component

The pcd-pcd01 component provides interfaces for actors of the *Communicate Patient Care Device Data* IHE transaction (PCD-01),
which is described in the [IHE Patient Care Device Technical Framework, Volume 2, Section 3](http://www.ihe.net/Technical_Framework/upload/IHE_PCD_TF_Vol2.pdf).

### Actors

The transaction defines the following actors:

![PCD-01 actors](images/pcd01.png)

Producer side corresponds to the *Patient Care Device Consumer* actor.
Consumer side corresponds to the *Patient Care Device Supplier* actor.

### Dependencies

In a Maven-based environment, the following dependency must be registered in `pom.xml`:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.platform-camel</groupId>
        <artifactId>ipf-platform-camel-ihe-hl7v2ws</artifactId>
        <version>${ipf-version}</version>
    </dependency>
```

### Endpoint URI Format

#### Producer

The endpoint URI format of `pcd-pcd01` component producers is:

```
pcd-pcd01://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `pcd-pcd01` component consumers is:

```
pcd-pcd01:serviceName[?parameters]
```

The resulting URL of the exposed IHE Web Service endpoint depends on both the configuration of the [deployment container]
and the serviceName parameter provided in the Camel endpoint URI.

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /xds/*
```

and serviceName equals to `iti42Service`, then the pcd-pcd01 consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/xds/iti42Service`

Additional URI parameters are optional and control special features as described in the corresponding section below.


### Example

This is an example on how to use the component on the consumer side:

```java
    from("pcd-pcd01:iti42Service?audit=true")
      .process(myProcessor)
      // process the incoming request and create a response
```


### Basic Common Component Features

* [ATNA auditing]
* [Message validation]

### Basic Web Service Component Features

* [Secure transport]
* [File-Based payload logging]

### Basic HL7 Component Features

* [Message types and exception handling]

### Advanced Web Service Component Features

* [Handling Protocol Handlers]
* [Deploying custom CXF interceptors]
* [Handling automatically rejected messages]
* [Using CXF features]



[ATNA auditing]: ../ipf-platform-camel-ihe/atna.html
[Message validation]: ../ipf-platform-camel-ihe/messageValidation.html

[deployment container]: ../ipf-platform-camel-ihe-ws/deployment.html
[Secure Transport]: ../ipf-platform-camel-ihe-ws/secureTransport.html
[File-Based payload logging]: ../ipf-platform-camel-ihe-ws/payloadLogging.html

[Message types and exception handling]: ../ipf-platform-camel-ihe-mllp/messageTypes.html

[Handling Protocol Handlers]: ../ipf-platform-camel-ihe-ws/protocolHeaders.html
[Deploying custom CXF interceptors]: ../ipf-platform-camel-ihe-ws/customInterceptors.html
[Handling automatically rejected messages]: ../ipf-platform-camel-ihe-ws/handlingRejected.html
[Using CXF features]: ../ipf-platform-camel-ihe-ws/cxfFeatures.html




