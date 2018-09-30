
## `ch-ciq` component

The `ch-ciq` component provides interfaces for actors of the *Community Information Query* transaction (CH:CIQ),
which is part of the Swiss profile [CH:CPI ("Community Portal Index")](https://www.e-health-suisse.ch/fileadmin/user_upload/Dokumente/2018/E/180712_Entwurf_EPDV-EDI_E2A5_CPI_V0.94_e.pdf).

### Actors

The transaction defines the following actors:

![CH-CIQ actors](images/ch-ciq.png)

Producer side corresponds to the *CPI Consumer* actor.
Consumer side corresponds to the *CPI Provider* actor (a Swiss governmental service).

### Dependencies

In a Maven-based environment, the following dependency must be registered in `pom.xml`:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.platform-camel</groupId>
        <artifactId>ipf-platform-camel-ihe-hpd</artifactId>
        <version>${ipf-version}</version>
    </dependency>
```

### Endpoint URI Format

#### Producer

The endpoint URI format of `ch-ciq` component producers is:

```
ch-ciq://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `ch-ciq` component consumers is:

```
ch-ciq:serviceName[?parameters]
```

The resulting URL of the exposed IHE Web Service endpoint depends on both the configuration of the [deployment container]
and the serviceName parameter provided in the Camel endpoint URI.

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /hpd/*
```

and serviceName equals to `chCiqService`, then the ch-pidd consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/hpd/chCiqService`

Additional URI parameters are optional and control special features as described in the corresponding section below.

### Data Types

The CH:CIQ component produces and consumes objects based on the [DSMLv2](https://www.oasis-open.org/standards#dsmlv2) data model:

* Request message -- [`BatchRequest`](../apidocs/org/openehealth/ipf/commons/ihe/hpd/stub/dsmlv2/BatchRequest.html)
* Response message -- [`BatchResponse`](../apidocs/org/openehealth/ipf/commons/ihe/hpd/stub/dsmlv2/BatchResponse.html)

### Example

This is an example on how to use the component on the consumer side:

```java
    from("ch-ciq:chCiqService")
      .process(myProcessor)
      // process the incoming request and create a response
```


### Basic Common Component Features

* ATNA Auditing is not defined for CH:CIQ
* [Message validation]

### Basic Web Service Component Features

* [Secure transport]
* [File-Based payload logging]

### Advanced Web Service Component Features

* [Handling Protocol Headers]
* [Deploying custom CXF interceptors]
* [Handling automatically rejected messages]
* [Using CXF features]



[Message validation]: ../ipf-platform-camel-ihe/messageValidation.html

[deployment container]: ../ipf-platform-camel-ihe-ws/deployment.html
[Secure Transport]: ../ipf-platform-camel-ihe-ws/secureTransport.html
[File-Based payload logging]: ../ipf-platform-camel-ihe-ws/payloadLogging.html

[Handling Protocol Headers]: ../ipf-platform-camel-ihe-ws/protocolHeaders.html
[Deploying custom CXF interceptors]: ../ipf-platform-camel-ihe-ws/customInterceptors.html
[Handling automatically rejected messages]: ../ipf-platform-camel-ihe-ws/handlingRejected.html
[Using CXF features]: ../ipf-platform-camel-ihe-ws/cxfFeatures.html




