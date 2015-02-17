## Manipulating HL7 messages

Message manipulation is as straightforward as read access. You navigate to a segment or field and assign it a new object or `String` value.

### Manipulating segments

Segments can be replaced. Currently only segments can be assigned, assignment to groups isn't supported yet.

```groovy
// copy over EVN segment from msg2 to msg1

// assignment operator
msg1.EVN = msg2.EVN

// recommended equivalent
msg1.EVN.from(msg2.EVN) 
```

The `from()` method is the preferred over the assignment operator ('=') for two reasons:

1. Segments can be copied with the assignment operator only if the assignment operator follows a property read-access 
operation (via `.property`). If you make an assignment directly to a segment variable, object references are assigned instead.
2. When a segment is obtained from a repetition using using the () operator, it cannot be assigned directly because this will violate 
Groovy/Java syntax. In this case, the `from` method is mandatory.

```groovy
group.NK1(0) = 'abc'         // syntax error!
msg1.NK1(0) = mySegment      // syntax error!
msg1.NK1(0).from(mySegment)  // works!
```

### Manipulating fields

To change a field value, navigate to the field (either by name or index) and either assign it a string value or another field. 
Again, fields may also be assigned using the `from()` method.

```groovy
def nk1      = message.PATIENT_RESULT(0).PATIENT.NK1(0)
def otherNk1 = message.PATIENT_RESULT(0).PATIENT.NK1(0)

// assignment operator
nk1[4]       = otherNk1[4]       // copy address
nk1[4][4]    = otherNk1[4][4]    // copy state or province only

// recommended equivalent
nk1[4][4].from(otherNk1[4][4])

// set state or province to a string value
nk1[4][4]    = 'NY'              
```

Again, the `from()` method is the preferred over the assignment operator ('=') for two reasons:

1. Composites can be copied with the assignment operator only if the assignment operator follows a subscript 
operation (via `[index]`). If you make an assignment directly to a composite variable, object references are assigned instead.
2. When a composite is obtained from a repetition using using the () operator, it cannot be assigned directly because this will violate 
Groovy/Java syntax. In this case, the `from` method is mandatory.

```groovy
field(0) = 'abc'               // syntax error!
field(0) = other               // syntax error!
field(0).from(other)           // works for primitives and composites
field(0).value = 'abc'         // works for primitives only
```

### Adding repetitions

There are two ways to add a repeating element: explicitly and implicitly.

Explicitly calling `nrp()` (for "new repetition") adds an element and returns it to the caller. 
The argument is of type `String` for repeating structures or `int` for repeating fields:

```groovy

// Adding a PATIENT group
def newPatientGroup = message.PATIENT_RESULT.nrp('PATIENT')

// Adding a NK1 segment
def newContactSegment = message.PATIENT_RESULT.PATIENT.nrp('NK1')

// adding a field (NK1-4)
def newAddress = message.PATIENT_RESULT.PATIENT.NK1.nrp(4)
```

Implicitly, an element is also added if you access a repetition that does not exist yet:

```groovy
def message       = new ORU_R01()
def patientResult = message.PATIENT_RESULT(0)          // add a PATIENT_RESULT group
def order         = patientResult.ORDER_OBSERVATION(0) // add a ORDER_OBSERVATION group
def observation   = order.OBSERVATION(0)               // add a OBSERVATION group
def obx5          = observation.OBX[5](0)              // add a OBX-5 field
```

Together with the [Smart Navigation][hl7v2dslSmart] feature, it is particularly convenient that accessing a repeated element 
without index does a default to its first repetition. Hence, the code above can be condensed to:

```groovy
def message = new ORU_R01()
def obx5    = message.PATIENT_RESULT.ORDER_OBSERVATION.OBSERVATION.OBX[5]
```

### Handling content types of OBX segments

The type of data contained in the fifth field of each `OBX` segment is variable and determined by the second field. 
In other words, when `OBX-2` equals to `"CE"`, `OBX-5` contains repeating instances of type `CE`.

To set data type in a newly created OBX segment or to change the type in an existing segment, 
the following approach using the `setObx5Type()` method can be used:

```groovy

// set type 'CE' for repetitions of OBX-5 and ensure that 
// at least 2 such repetitions are available
def obx = message.PATIENT_RESULT.ORDER_OBSERVATION.OBSERVATION.OBX
obx.setObx5Type('CE', 2)   
 
obx[5](0)[1] = 'T57000'
obx[5](0)[2] = 'GALLBLADDER'
obx[5](0)[3] = 'SNM'
```


[hl7v2dslSmart]: hl7v2dslSmart.html
