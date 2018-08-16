## Accessing fields using the HL7v2 Domain Specific Language

### Navigation to fields

Obtaining fields is similar to obtaining groups and segments except that fields are often referred to by number instead
of by name. Fields are accessed like an array field. Components in a composite field are accessed like a two-dimensional array.
Subcomponents are accessed like a three-dimensional array.

```kotlin
// MSH-3 = sending application composite field
val composite = message["MSH"][3]

// MSH-3-2 = universal ID primitive field
val primitive = message["MSH"][3][2]  
```

It is also possible to navigate by specifying the field *names* instead of the position number, but in Kotlin
this requires the message to know the *exact type at compile time* :

```kotlin
val message: ADT_A01 = loadHl7(context, "/msg-01.hl7")
val primitive = message.msh.sendingApplication.universalIDType
```

However, that along with the change of internal message structures, individual field names change between HL7 versions although 
they refer to the same position of the field in a segment. If you don't know the version of the HL7 message in advance, better 
use the more concise index notation:

```kotlin
// only works for HL7 v2.2 and 2.3 messages
val messageType1 = message.msh.messageType.messageType

// only works for HL7 v2.4\+ messages
val messageType2 = message.msh.messageType.messageCode

// works for all HL7 versions
val messageType3 = message["MSH"][9][1]                       
```

In case the field is [repeating][hl7v2dslRepetitions], this simple syntax only returns the first repetition

### Field values

Corresponding to [HAPI], variables render to their string encoding by implementing an appropriate `encode()` method (just like complete messages).
String values from primitive fields are obtained by `value()`

```kotlin

// Returns ADT^A01
val hl7Event = message["MSH"][9].encode()

// Returns ADT
val messageType = message["MSH"][9][1].value

// Also returns ADT
val messageType = message["MSH"][9].value

// Returns ADT_A01 if MSH[9][3] is empty
val messageType = message["MSH"][9][3].getValueOr("ADT_A01")  // or valueOr("ADT_A01")

// Returns the value of a subcomponent
val street = message["PATIENT_RESULT"]["PATIENT"]["PID"][11][1][1].value
```

### Destructuring

Kotlin lets you destructure composite instances into components:

```kotlin

val(event, trigger, structure) = message["MSH"][9]
println(event.value) // prints ADT

```

### Null values

The HL7 DSL treats explicit HL7 null values (two double quotes "", cf. HL7 2.5, Final, Section 2.5.3) in a special way.

* `value()` and `encode()` returns the double quotes
* `value2()` convert double quotes into an empty string
* `isNullValue()` returns true, if the original value of the field is ""


[HAPI]: https://hapifhir.github.io/hapi-hl7v2/
[hl7v2dslRepetitions]: hl7v2dslRepetitions.html