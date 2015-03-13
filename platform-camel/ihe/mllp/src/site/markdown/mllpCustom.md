
## `mllp` component

The mllp component allows for custom MLLP-based interfaces to benefit from all features that are available to
MLLP-based IHE transactions, e.g. message fragmentation, ATNA auditing, message dispatching etc.
This is particularly valuable if you already use IHE transaction components but you need additional interfaces for
which there is no defined IHE profile.

### Actors

Both consumer and producer sides are supported. There is no mapping to a particular actor.

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

The endpoint URI format of the `mllp` component is identical for producers and consumers:

```
mllp://hostname:port[?parameters]
```

where *hostname* is either an IP address or a domain name, and *port* is a number. For the consumer side, the host name
`0.0.0.0` allows the access from any remote host.
These two obligatory URI parts represent the address of the MLLP endpoint which is to be served by the given consumer or
accessed by the given producer.

Additionally, the URI parameter *hl7TransactionConfig* is mandatory and references a bean of type
`org.openehealth.ipf.platform.camel.ihe.hl7v2.Hl7v2TransactionConfiguration` that defines the contract of the transaction.

The parameters *clientAuditStrategy* and *serverAuditStrategy* are mandatory unless [ATNA auditing] is disabled in the
endpoints. The strategy parameters must reference an instance of type `org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpAuditStrategy`.


### HL7v2 Codec

All HL7v2-based transactions are realized using the [camel-mina2](http://camel.apache.org/mina2.html) and [camel-hl7](http://camel.apache.org/hl7.html)
components and requires that an [HL7v2 Codec](codec.html) is available in the Camel registry.

### Example

This is an example on how to use the component on the consumer side:

```java
    from("mllp://0.0.0.0:8777?hl7TransactionConfig=#config&clientAuditStrategy=#clientStrategy&serverAuditStrategy=#serverStrategy")
      .process(myProcessor)
      // process the incoming request and create a response
```

### Basic Common Component Features

* [ATNA auditing]

### Basic MLLP Component Features

* [Message types and exception handling]
* [Secure transport]
* [File-Based payload logging]

### Advanced MLLP Component Features

* [Interceptor chain configuration]
* [Segment fragmentation]
* [Unsolicited request message fragmentation]


[ATNA auditing]: ../ipf-platform-camel-ihe/atna.html
[Message validation]: ../ipf-platform-camel-ihe/messageValidation.html
[HL7v2 Codec]: codec.html
[Message types and exception handling]: messageTypes.html
[Secure transport]: secureTransport.html
[File-Based payload logging]: payloadLogging.html
[Interceptor chain configuration]: interceptorChain.html
[Segment fragmentation]: segmentFragmentation.html
[Unsolicited request message fragmentation]: unsolicitedFragmentation.html

