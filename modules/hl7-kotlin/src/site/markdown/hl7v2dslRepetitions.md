## Repetitions

### Accessing Repetitions

Groups, segments and fields may be repeatable. Parentheses like with regular method calls are used in order to obtain a
certain element of a repeating structure. Note that, unlike field numbers, repetitions are **zero-based**.

```kotlin
    // Returns the first PATIENT group inside the second PATIENT_RESULT group
    val group = message["PATIENT_RESULT"](1)["PATIENT"](0)

    // Returns the second NK1 segment
    val nk1   = group["NK1"](1)

    // Access first NK1-5 field (phone)
    val phone = nk1[5](0)
```

An alternative - slightly more concise - syntax uses the repetition as second parameter inside the brackets:

```kotlin
    // Returns the first PATIENT group inside the second PATIENT_RESULT group
    val group = message["PATIENT_RESULT", 1]["PATIENT", 0]

    // Returns the second NK1 segment
    val nk1   = group["NK1", 1]

    // Access first NK1-5 field (phone)
    val phone = nk1[5, 0]
```


In order to get a list of structure repetitions, simply omit the index so that it looks like a method call without parameters:

```kotlin
    // Returns an Array containing first PATIENT groups
    val groups = message["PATIENT_RESULT"]()

    // Returns an Array containing all phone numbers of the first contact
    val phones = group.["NK1"](0)[5]()
```

### Counting Repetitions

The number of repetitions are counted by simply using the `count()` method:

```kotlin
    // Use the DSL count method for counting the structure repetitions
    val groups = message.count("PATIENT_RESULT")

    // Use the DSL count method for counting the field repetitions
    val phones = group["NK1", 0].count(5)
```

