
## `pdqv3-iti47` component

The pdqv3-iti47 component provides interfaces for actors of the *Patient Demographics Query v3* IHE transaction (ITI-47),
which is described in the [IHE IT Infrastructure Technical Framework, Volume 2b , Section 3.47](http://ihe.net/uploadedFiles/Documents/ITI/IHE_ITI_TF_Vol2b.pdf).

### Actors

The transaction defines the following actors:

![ITI-47 actors](images/iti47.png)

Producer side corresponds to the *Patient Demographics Consumer* actor.
Consumer side corresponds to the *Patient Demographics Supplier* actor.

### Dependencies

In a Maven-based environment, the following dependency must be registered in `pom.xml`:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.platform-camel</groupId>
        <artifactId>ipf-platform-camel-ihe-hl7v3</artifactId>
        <version>${ipf-version}</version>
    </dependency>
```

### Endpoint URI Format

#### Producer

The endpoint URI format of `pdqv3-iti47` component producers is:

```
pdqv3-iti47://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `pdqv3-iti47` component consumers is:

```
pdqv3-iti47:serviceName[?parameters]
```

The resulting URL of the exposed IHE Web Service endpoint depends on both the configuration of the [deployment container]
and the serviceName parameter provided in the Camel endpoint URI.

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /pdqv3/*
```

and serviceName equals to `iti47Service`, then the pdqv3-iti47 consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/pdqv3/iti47Service`

Additional URI parameters are optional and control special features as described in the corresponding section below.


### Example

This is an example on how to use the component on the consumer side:

```java
    from("pdqv3-iti47:iti47Service?audit=true")
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

* [Handling Protocol Headers]
* [Deploying custom CXF interceptors]
* [Handling automatically rejected messages]
* [Using CXF features]



[ATNA auditing]: ../ipf-platform-camel-ihe/atna.html
[Message validation]: ../ipf-platform-camel-ihe/messageValidation.html

[deployment container]: ../ipf-platform-camel-ihe-ws/deployment.html
[Secure Transport]: ../ipf-platform-camel-ihe-ws/secureTransport.html
[File-Based payload logging]: ../ipf-platform-camel-ihe-ws/payloadLogging.html

[Message types]: messageTypes.html

[Handling Protocol Headers]: ../ipf-platform-camel-ihe-ws/protocolHeaders.html
[Deploying custom CXF interceptors]: ../ipf-platform-camel-ihe-ws/customInterceptors.html
[Handling automatically rejected messages]: ../ipf-platform-camel-ihe-ws/handlingRejected.html
[Using CXF features]: ../ipf-platform-camel-ihe-ws/cxfFeatures.html




