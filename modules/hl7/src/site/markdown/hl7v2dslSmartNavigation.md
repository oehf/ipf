## Smart navigation

Accessing HL7 messages usually requires knowledge about the specified message structure,
which is often not visible by looking at the printed message.
To make things worse, the internal structure changes between HL7 versions. In more recent versions, primitive types are
sometimes replaced with composite types having the so far used primitive as first component.
This appears to be backwards compatible on printed messages, but requires different DSL expressions when obtaining field values.

Smart navigation resolves these problems by assuming reasonable defaults when repetitions or component operators are omitted

### Omitting repetition operator

If a repetition operator () is omitted, the first repetition of a group, segment or field is assumed

```groovy
    assert message.PATIENT_RESULT.PATIENT == message.PATIENT_RESULT(0).PATIENT(0)

    assert group.NK1(0)[5](0)[1].value == group.NK1[5](0)[1].value
    assert group.NK1(0)[5](0)[1].value == group.NK1[5][1].value
```

### Omitting component index

If a component index is omitted, the first component or subcomponent of a composite is assumed.

```groovy
    def group = message.PATIENT_RESULT.PATIENT
    assert group.NK1(0)[2][1][1].value == group.NK1(0)[2].value
```

### Combining smart navigation with finders

Both repetition on component index can be omitted.

```groovy
    def group = message.PATIENT_RESULT.PATIENT
    assert group.NK1(0)[5](0)[1].value2 == group.NK1[5].value2
```

But even so, it is still required to specify the full path to the `NK1` segment.

Here, the [iterative functions][hl7v2dslIteration] come to rescue. They e.g. allow to find
the first structure with a given name within the message:

```groovy
    def phone = message.PATIENT_RESULT(0).PATIENT(0).NK1(0)[5](0)[1].value
    phone = message.findNK1()[5].value    // equivalent
```


[hl7v2dslIteration]: hl7v2dslIteration.html
