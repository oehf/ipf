## Creating new instances of HL7 messages, structures, and types

### New messages

You can create a new message from scratch by specifying event type, trigger event and version. Its message header fields are
populated with the event type, trigger event, version, the current time as message date, and the common separators.

```groovy

    import ca.uhn.hl7v2.model.Message

    // creates a ca.uhn.hl7v2.model.v25.message.ADT_A01 object
    def msg = Message.ADT_A01(hapiContext, '2.5')

    // also creates a ca.uhn.hl7v2.model.v25.message.ADT_A01 object, because a
    // ADT^A04 uses the ADT_A01 message structure
    msg = Message.ADT_A04(hapiContext, '2.5')

```

While [HAPI] already allows to create acknowledgements on [`Message`](https://hapifhir.github.io/hapi-hl7v2//base/apidocs/ca/uhn/hl7v2/model/Message.html) objects,
IPF additionally provides a more generic `respond` method, e.g. for responding to queries.

The response

* is in the same HL7 version as the original message
* refers to the message metadata of the original message (e.g. swapped sender and receiver fields)
* contains the current timestamp as message date
* has a populated MSA segment


### New segments

Just as creating a message, you can also create a segment by calling its respective name as static method on the
[`ca.uhn.hl7v2.model.Structure`](https://hapifhir.github.io/hapi-hl7v2//base/apidocs/ca/uhn/hl7v2/model/Message.html) interface.
You need to pass the enclosing `Message` object as argument, which determines the HL7 version to be used.

```groovy

    import ca.uhn.hl7v2.model.Segment

    // creates a ca.uhn.hl7v2.mode.v25.segment.OBX object
    def obx = Segment.OBX(msg)

```

### New Types

Just as creating a message or segment, you can also create a field by calling its respective name as static method on the
[`ca.uhn.hl7v2.model.Composite`](https://hapifhir.github.io/hapi-hl7v2//base/apidocs/ca/uhn/hl7v2/model/Composite.html) or
[`ca.uhn.hl7v2.model.Primitive`](https://hapifhir.github.io/hapi-hl7v2//base/apidocs/ca/uhn/hl7v2/model/Primitive.html) interface.

You need to pass the enclosing `Message` object as argument, which determines the HL7 version to be used.

Composites may be initialized with a map containing the component values.
Primitives may be initialized with a literal string value.

```groovy

    import ca.uhn.hl7v2.model.Composite
    import ca.uhn.hl7v2.model.Primitive

    // Static method extension to the HAPI Composite class
    def ce = Composite.CE(msg, [identifier:'T57000', text:'GALLBLADDER', nameOfCodingSystem:'SNM'])

    // Static method extension to the HAPI Primitive class
    def st = Primitive.ST(msg, 'value')

```


[HAPI]: https://hapifhir.github.io/hapi-hl7v2/