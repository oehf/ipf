## Iterative functions

As HL7 messages are compound structures, it should be possible to traverse them. Thus, the HL7 DSL implements iterators for
HL7 messages and groups. Due to their nested structures, iteration is implemented as a depth first traversal over all
non-empty substructures, i.e. non-empty groups and segments.

```groovy
    import ca.uhn.hl7v2.model.Message

    boolean hasGroups = message.any { it instanceof Group }
    def allStructureNames = message*.name
```

An `iterator()` method is defined for the Group and Message classes. This method is seldomly used directly, however, a lot of Groovy's iterative 
functions rely on the existence of such an `iterator`. As a consequence, you can e.g. use the following [Groovy] functions on HL7 messages and groups:

* each
* eachWithIndex
* every
* any
* collect
* find
* findAll
* split
* for statement
* the spread operator

### Examples

```groovy

    // Count the number of substructures
    int numberOfStructures = 0
    msg1.each { numberOfStructures++ }
    println "The message has $numberOfStructures substructures"

    // Check if there are any groups
    boolean hasGroups = msg1.any { it instanceof Group }

    // A list of the names of all substructures
    def names = msg1*.name

    // For loop
    for (structure in msg1) {
      // do something with structure
    }

    // Find the first nested OBX segment
    def obx = msg1.find { it.name == 'OBX' }
    obx = msg1.findOBX() // shortcut notation

    // Find all nested OBX segments
    def obxList = msg1.findAll { it.name == 'OBX' }
    obxList = msg1.findAllOBX() // shortcut notation

```

The find/findAll methods are particularly useful together with [smart navigation][hl7v2dslSmart] use cases:

* accessing data in a deeply nested message structure that is not visible in the pipe-encoded representation.
* uniformly accessing corresponding fields in messages with different structure
* messages that have a group structure in a newer HL7 version while having a flat structure in previous versions.

```groovy
    def patientName = msg.PATIENT_RESULT(0).PATIENT.PID[5][1].value
    patientName = msg.findPID()[5][1].value  // equivalent, shorter, and group-structure-agnostic
```

[Groovy]: http://groovy.codehaus.org
[hl7v2dslSmart]: hl7v2dslSmartNavigation.html