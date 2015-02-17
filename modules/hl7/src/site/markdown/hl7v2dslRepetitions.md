## Repetitions

### Accessing Repetitions

Groups, segments and fields may be repeatable. Parentheses like with regular method calls are used in order to obtain a
certain element of a repeating structure.

```groovy
// Returns the first PATIENT group inside the second PATIENT_RESULT group
def group = message.PATIENT_RESULT(1).PATIENT(0)

// Returns the second NK1 segment
def nk1   = group.NK1(1)

// Access first NK1-5 field (phone)
def phone = nk1[5](0)                          
```

Note that, unlike field numbers, repetitions are **zero-based**.

In order to get a repeating structure, simply omit the index so that it looks like a method call without parameters:

```groovy
// Returns a java.util.Iterable containing first PATIENT groups
def group = message.PATIENT_RESULT()

// Returns a java.util.Iterable containing all phone numbers of the first contact
def phone = group.NK1(0)[5]()                          
```

### Counting Repetitions

The number of repetitions are counted by simply using the `count()` method;

// Use the DSL count method for counting the field repetitions                        
int groups = message.count('PATIENT_RESULT')

// or use the HAPI structure method for counting the structure repetitions
// int groups = message.PATIENT_RESULTReps

// Use the DSL count method for counting the field repetitions
int phones = group.NK1(0).count(5)
```

