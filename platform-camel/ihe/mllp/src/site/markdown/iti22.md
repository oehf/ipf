
## `pdq-iti22` component

The pdq-iti22 component provides interfaces for actors of the *Patient Demographics and Visit Query* IHE transaction (ITI-22),
which is described in the [IHE IT Infrastructure Technical Framework, Volume 2a , Section 3.22](http://ihe.net/uploadedFiles/Documents/ITI/IHE_ITI_TF_Vol2a.pdf).

### Actors

The transaction defines the following actors:

![ITI-22 actors](images/iti22.png)

Producer side corresponds to the *Patient Demographics Consumer* actor.
Consumer side corresponds to the *Patient Demographics Supplier* actor.

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

The endpoint URI format of the `pdq-iti22` component is identical for producers and consumers:

```
pdq-iti22://hostname:port[?parameters]
```

where *hostname* is either an IP address or a domain name, and *port* is a number. For the consumer side, the host name
`0.0.0.0` allows the access from any remote host.
These two obligatory URI parts represent the address of the MLLP endpoint which is to be served by the given consumer or
accessed by the given producer. URI parameters controlling the transaction features are described below.


### HL7v2 Codec

All HL7v2-based transactions are realized using the [camel-mina2](http://camel.apache.org/mina2.html) and [camel-hl7](http://camel.apache.org/hl7.html)
components and requires that an [HL7v2 Codec](codec.html) is available in the Camel registry.

### Example

This is an example on how to use the component on the consumer side:

```java
    from("pdq-iti22://0.0.0.0:8777?audit=true&secure=true")
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

### Transaction-Specific Component Feature

* [Interactive response message continuation]



[ATNA auditing]: ../ipf-platform-camel-ihe/atna.html
[Message validation]: ../ipf-platform-camel-ihe/messageValidation.html
[HL7v2 Codec]: codec.html
[Message types and exception handling]: messageTypes.html
[Secure transport]: secureTransport.html
[File-Based payload logging]: payloadLogging.html
[Interceptor chain configuration]: interceptorChain.html
[Segment fragmentation]: segmentFragmentation.html
[Unsolicited request message fragmentation]: unsolicitedFragmentation.html
[Interactive response message continuation]: interactiveContinuation.html
