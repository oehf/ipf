## Accessing groups and segments using the HL7v2 Domain Specific Language

Groups and segments can be accessed by name like in a map.

```kotlin
    import ca.uhn.hl7v2.model.Message

    val message: Message = ...

    // Accessing a segment
    val msh   = message["MSH"]

    // Accessing a group
    val group = message["PATIENT_RESULT"]

    // Accessing a segment nested within groups
    val pid   = message["PATIENT_RESULT"]["PATIENT"]["PID"]
```

In case the structure is [repeating][hl7v2dslRepetitions], this simple syntax only returns the first repetition



[hl7v2dslRepetitions]: hl7v2dslRepetitions.html