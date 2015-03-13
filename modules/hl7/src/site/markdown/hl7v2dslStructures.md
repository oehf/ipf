## Accessing groups and segments using the HL7v2 Domain Specific Language

Groups and segments can be accessed by name like an object property.

```groovy
    import ca.uhn.hl7v2.model.Message

    Message message = ...

    // Accessing a segment
    def msh   = message.MSH

    // Accessing a group
    def group = message.PATIENT_RESULT

    // Accessing a segment nested within groups
    def pid   = message.PATIENT_RESULT.PATIENT.PID
```

In case the structure is [repeating][hl7v2dslRepetitions], this simple syntax only returns the first repetition


### Usage of anonymous types

Groovy doesn't require to specify the exact type of a variable, instead the `def` keyword can be used. For HL7 v2 processing, this might be a very convenient feature 
that saves many explicit type checks and type casts. However, auto-completion support in the IDE is restricted and, due to the lack of explicit type information,
later refactoring steps can be problematic.

[hl7v2dslRepetitions]: hl7v2dslRepetitions.html