## Mapping HL7 type values

The [Mapping Service] is part of the IPF Core features. After all, although often used in HL7 processing, code system mapping
is not a feature that is inherently exclusive for HL7.

What remains specific to IPF's HL7 v2 support, however, is that the mapping extensions can be applied directly on all [HAPI] message types.


### Example

Given the following mapping example:

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
             'ADT^A01' : 'PRPA_IN402001'
             (ELSE) : { throw new HL7Exception("Invalid message type", 207) }
        )
    }

```

The mapping functions can be directly applied on composite or primitive field objects:

```groovy

    // Mapping primitives
    assert msg.PV1.patientClass.value == 'I'
    assert msg.PV1.patientClass.map('encounterType') == 'IMP'
    assert msg.PV1.patientClass.mapEncounterType() == 'IMP'

    // Together with the HL7 v2 DSL, you can also write
    assert msg.PV1[2].mapEncounterType() == 'IMP'

    // To map a Composite field, you can write
    assert msg.MSH.messageType.mapMessageType() == 'PRPA_IN402001'
    assert msg.MSH[9].mapMessageType() == 'PRPA_IN402001'

```

[HAPI]: https://hapifhir.github.io/hapi-hl7v2/
[Mapping Service]: ../ipf-commons-map/index.html