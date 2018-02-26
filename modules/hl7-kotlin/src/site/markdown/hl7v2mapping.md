## Mapping HL7 type values

The [Mapping Service] is part of the IPF Core features. After all, although often used in HL7 processing, code system mapping
is not a feature that is inherently exclusive for HL7.

What remains specific to IPF's HL7 v2 support, however, is that the mapping extensions can be applied directly on all [HAPI] message types.


### Example

Given the following mapping example (in Groovy):

```groovy

    mappings = {
        encounterType(['2.16.840.1.113883.12.4','2.16.840.1.113883.5.4'],
             E : 'EMER',
             I : 'IMP',
             O : 'AMB'
        )

        vip(['2.16.840.1.113883.12.99','2.16.840.1.113883.5.1075'],
             Y      : 'VIP',
             (ELSE) : { it }
        )

        messageType(
             'ADT^A01' : 'PRPA_IN402001',
             (ELSE) : { throw new HL7Exception("Invalid message type", 207) }
        )
    }

```

The mapping functions can be directly applied on composite or primitive field objects:

```kotlin

    // Mapping primitives
    assertEquals("I", msg["PV1"][2].value)
    assertEquals("IMP", msg["PV1"][2].map("encounterType")

    // To map a Composite field, you can write
    assertEquals("PRPA_IN402001", msg["MSH"][9].map("messageType")

```

[HAPI]: https://hapifhir.github.io/hapi-hl7v2/
[Mapping Service]: ../ipf-commons-map/index.html