
## `pix-iti8` / `xds-iti8` component

The pix-iti8/xds-iti8 component provides interfaces for actors of the *Patient Identity Feed* IHE transaction (ITI-8),
which is described in the [IHE IT Infrastructure Technical Framework, Volume 2a , Section 3.8](https://ihe.net/uploadedFiles/Documents/ITI/IHE_ITI_TF_Vol2a.pdf).
This transaction relates to both PIX and XDS IHE profiles, therefore the same component is available under two different names.

### Actors

The transaction defines the following actors:

![ITI-8 actors](images/iti8.png)

Producer side corresponds to the *Patient Identity Source* actor.
Consumer side corresponds to both *Patient Identifier Cross-reference Manager* and *XDS Document Registry* actors.

### Dependencies

In a Maven-based environment, the following dependency must be registered in `pom.xml`:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.platform-camel</groupId>
        <artifactId>ipf-platform-camel-ihe-mllp</artifactId>
        <version>${ipf-version}</version>
    </dependency>
```

### Endpoint URI Format

The endpoint URI format of the `pix-iti8`/`xds-iti8` component is identical for producers and consumers:

```
pix-iti8://hostname:port[?parameters]
xds-iti8://hostname:port[?parameters]
```

where *hostname* is either an IP address or a domain name, and *port* is a number. For the consumer side, the host name
`0.0.0.0` allows the access from any remote host.
These two obligatory URI parts represent the address of the MLLP endpoint which is to be served by the given consumer or
accessed by the given producer. URI parameters controlling the transaction features are described below.


### HL7v2 Codec

All HL7v2-based transactions are realized using the [camel-mina2](https://camel.apache.org/mina2.html) and [camel-hl7](https://camel.apache.org/hl7.html)
components and requires that an [HL7v2 Codec](codec.html) is available in the Camel registry.

### Example

This is an example on how to use the component on the consumer side:

```java
    from("pix-iti8://0.0.0.0:8777?audit=true&secure=true")
      .process(myProcessor)
      // process the incoming request and create a response
```

### Basic Common Component Features

* [ATNA auditing]
* [Message validation]

### Basic MLLP Component Features

* [Message types and exception handling]
* [Secure transport]
* [File-Based payload logging]

### Advanced MLLP Component Features

* [Interceptor chain configuration]
* [Segment fragmentation]
* [Unsolicited request message fragmentation]


### Remarks for this component

* ITI-8 is the only HL7v2-based transaction that uses HL7 v2.3.1, while most others use HL7 v2.5


[ATNA auditing]: ../ipf-platform-camel-ihe/atna.html
[Message validation]: ../ipf-platform-camel-ihe/messageValidation.html
[HL7v2 Codec]: codec.html
[Message types and exception handling]: messageTypes.html
[Secure transport]: secureTransport.html
[File-Based payload logging]: payloadLogging.html
[Interceptor chain configuration]: interceptorChain.html
[Segment fragmentation]: segmentFragmentation.html
[Unsolicited request message fragmentation]: unsolicitedFragmentation.html

