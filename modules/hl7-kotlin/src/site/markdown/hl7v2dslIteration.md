## Iterative functions

As HL7 messages are compound structures, it should be possible to traverse them. Thus, the HL7 DSL offers the `asIterable` function
for HL7 messages and groups in order to become iterable, so that e.g. all [Iterable extension functions] become applicable.
 
Due to their nested structures, iteration is implemented as a depth first traversal over all non-empty substructures, i.e. non-empty groups and segments.


```kotlin

    // Count the number of substructures
    var numberOfStructures = 0
    message.asIterable().forEach { numberOfStructures++ }

    // Check if there are any groups
    val hasGroups = message.asIterable().any { it is Group }

    // A list of the names of all substructures
    val allStructureNames = message.asIterable().map { it.name }

    // For loop
    numberOfStructures = 0
    for (structure in message) {
        numberOfStructures++
    }

    // Find the first nested OBX segment
    val obx = msg1.asIterable().find { it.name == "OBX" }

    // Find all nested OBX segments
    val obxList = msg1.asIterable().filter { it.name == "OBX" }

```

The `find`/`filter` methods are particularly useful together with [smart navigation][hl7v2dslSmart] use cases:

* accessing data in a deeply nested message structure that is not visible in the pipe-encoded representation.
* uniformly accessing corresponding fields in messages with different structure
* messages that have a group structure in a newer HL7 version while having a flat structure in previous versions.


[hl7v2dslSmart]: hl7v2dslSmartNavigation.html
[Iterable extension functions]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html