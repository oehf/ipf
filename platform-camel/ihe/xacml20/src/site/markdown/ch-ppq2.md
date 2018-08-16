## `ch-ppq2` component

The `ch-ppq2` component provides interfaces for actors of the *Privacy Policy Retrieve* transaction (PPQ-2),
which is described in the 
[Swiss national extensions to the IHE Technical Framework](https://www.e-health-suisse.ch/gemeinschaften-umsetzung/umsetzung/programmierhilfen.html)
(revision from March 2018, section 3.4 in "Ergänzung 2 zu Anhang 5 der EPDV-EDI").

### Actors

The transaction defines the following actors:

![PPQ-2 actors](images/ch-ppq2.png)

Producer side corresponds to the *Policy Consumer* actor.
Consumer side corresponds to the *Policy Repository* actor.

### Dependencies

In a Maven-based environment, the following dependency must be registered in `pom.xml`:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.platform-camel</groupId>
        <artifactId>ipf-platform-camel-ihe-xacml20</artifactId>
        <version>${ipf-version}</version>
    </dependency>
```

### Endpoint URI Format

#### Producer

The endpoint URI format of `ch-ppq2` component producers is:

```
ch-ppq2://hostname:port/path/to/service[?parameters]
```

where *hostname* is either an IP address or a domain name, *port* is a port number, and *path/to/service*
represents additional path elements of the remote service.
URI parameters are optional and control special features as described in the corresponding section below.

#### Consumer

The endpoint URI format of `ch-ppq2` component consumers is:

```
ch-ppq2:serviceName[?parameters]
```

The resulting URL of the exposed IHE Web Service endpoint depends on both the configuration of the [deployment container]
and the serviceName parameter provided in the Camel endpoint URI.

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /policy/*
```

and serviceName equals to `chPpq2Service`, then the`ch-ppq2` consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/policy/chPpq2Service`

Additional URI parameters are optional and control special features as described in the corresponding section below.

### Data Types

Messages exchanged in the `ch-ppq2` transaction are defined in XACML 2.0 standard:
* Request message -- [`XACMLPolicyQueryType`](../apidocs/org/openehealth/ipf/commons/ihe/xacml20/stub/xacml20/saml/protocol/XACMLPolicyQueryType.html) or 
* Response message -- [`ResponseType`](../apidocs/org/openehealth/ipf/commons/ihe/xacml20/stub/saml20/protocol/ResponseType.html)


### Basic Common Component Features

* [ATNA auditing]
* [Message validation]

### Basic Web Service Component Features

* [Secure transport]
* [File-Based payload logging]

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

[Handling Protocol Headers]: ../ipf-platform-camel-ihe-ws/protocolHeaders.html
[Deploying custom CXF interceptors]: ../ipf-platform-camel-ihe-ws/customInterceptors.html
[Handling automatically rejected messages]: ../ipf-platform-camel-ihe-ws/handlingRejected.html
[Using CXF features]: ../ipf-platform-camel-ihe-ws/cxfFeatures.html




