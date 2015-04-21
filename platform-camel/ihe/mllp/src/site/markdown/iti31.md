
## `pam-iti31` component ![v3.1](../images/3.1.png)

The pam-iti31 component provides interfaces for actors of the *Patient Encounter Management* IHE transaction (ITI-31),
which is described in the [IHE IT Infrastructure Technical Framework, Volume 2b , Section 3.31](http://ihe.net/uploadedFiles/Documents/ITI/IHE_ITI_TF_Vol2b.pdf).

### Actors

The transaction defines the following actors:

![ITI-31 actors](images/iti31.png)

Producer side corresponds to the *Patient Encounter Supplier* actor.
Consumer side corresponds to the *Patient Encounter Consumer* actor.

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

The endpoint URI format of the `pam-iti31` component is identical for producers and consumers:

```
pam-iti31://hostname:port[?parameters]
```

where *hostname* is either an IP address or a domain name, and *port* is a number. For the consumer side, the host name
`0.0.0.0` allows the access from any remote host.
These two obligatory URI parts represent the address of the MLLP endpoint which is to be served by the given consumer or
accessed by the given producer. URI parameters controlling the transaction features are described below.


### HL7v2 Codec

All HL7v2-based transactions are realized using the [camel-mina2](http://camel.apache.org/mina2.html) and [camel-hl7](http://camel.apache.org/hl7.html)
components and requires that an [HL7v2 Codec](codec.html) is available in the Camel registry.

### Transaction Options

This transaction defines the following options; at least one of them must be chosen to be supported:
 
* `BASIC` (default)
* `INPATIENT_OUTPATIENT_ENCOUNTER_MANAGEMENT`
* `PENDING_EVENT_MANAGEMENT`
* `ADVANCED_ENCOUNTER_MANAGEMENT`
* `TEMPORARY_PATIENT_TRANSFERS_TRACKING`
* `HISTORIC_MOVEMENT_MANAGEMENT`
* `MAINTAIN_DEMOGRAPHICS`
            
The options are represented as enumeration in the class [Iti31Options](../apidocs/org/openehealth/ipf/platform/camel/ihe/mllp/iti31/Iti31Options.html).

Support for one or more options is configured using the optional `options` parameter, followed by a comma-separated list of
options to be supported. If `options` is provided, the default option (see above) is used.
The endpoint will reject event types that are outside the scope of the configured option.

### Example

This is an example on how to use the component on the consumer side:

```java
    from("pam-iti31://0.0.0.0:8777?options=BASIC,INPATIENT_OUTPATIENT_ENCOUNTER_MANAGEMENT")
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




[ATNA auditing]: ../ipf-platform-camel-ihe/atna.html
[Message validation]: ../ipf-platform-camel-ihe/messageValidation.html
[HL7v2 Codec]: codec.html
[Message types and exception handling]: messageTypes.html
[Secure transport]: secureTransport.html
[File-Based payload logging]: payloadLogging.html
[Interceptor chain configuration]: interceptorChain.html
[Segment fragmentation]: segmentFragmentation.html
[Unsolicited request message fragmentation]: unsolicitedFragmentation.html
