## Mapping Service

The [`org.openehealth.ipf.commons.map.MappingService`](../apidocs/org/openehealth/ipf/commons/map/MappingService.html)
interface deals with the requirement that message processing often
involves mapping between code systems, i.e. from one set of codes into a corresponding set of codes.
For example, HL7 version 2 to HL7 version 3 use different code systems for most coded values like message type, gender,
clinical encounter type, marital status codes, address and telecommunication use codes, just to mention a few.

MappingService implementations provide the mapping logic, which can be a simple `java.util.Map`, but can also be a facade for a
remote terminology service.

The `ipf-commons-map` component extends the `java.lang.String` and `java.util.Collection` classes with methods targeted at mapping.

The ipf-commons-map library provides the MappingService implementation
([`org.openehealth.ipf.commons.map.BidiMappingService`](../apidocs/org/openehealth/ipf/commons/map/BidiMappingService.html)), which implements

*  bidirectional mapping
*  mapping of arbitrary objects
*  definitions of mappings using external Groovy Scripts

Additionally there is [`org.openehealth.ipf.commons.map.SpringBidiMappingService`](../apidocs/org/openehealth/ipf/commons/map/SpringBidiMappingService.html)
that adds the possibility to configure mapping scripts as Spring `Resource`s.

You are free to implement and use your own service as long as it implements the
[`org.openehealth.ipf.commons.map.MappingService`](../apidocs/org/openehealth/ipf/commons/map/MappingService.html) interface.


### Dependencies

In a Maven-based environment, the following dependency must be registered in `pom.xml`:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.commons</groupId>
        <artifactId>ipf-commons-map</artifactId>
        <version>${ipf-version}</version>
    </dependency>
```

When using Spring, you should also depend on:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.commons</groupId>
        <artifactId>ipf-commons-spring</artifactId>
        <version>${ipf-version}</version>
    </dependency>
```

### Configuring the Mapping Service

Here is how to configure IPF's [`SpringBidiMappingService`](../apidocs/org/openehealth/ipf/commons/map/SpringBidiMappingService.html) using Spring:

```xml

    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xmlns:ipf="http://openehealth.org/schema/ipf-commons-core"
           xsi:schemaLocation="
    http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://openehealth.org/schema/ipf-commons-core
    http://openehealth.org/schema/ipf-commons-core.xsd">

    <!-- Register Spring as global registry -->
    <ipf:globalContext id="globalContext"/>

    ...
    <!-- Groovy class that provides the operations on the mappings -->
    <bean id="myMappingService" class="org.openehealth.ipf.commons.map.SpringBidiMappingService">
       <property name="mappingScripts">
           <list>
              <value>classpath:example.groovy"</value>
              <!-- could add more mapping files -->
           </list>
       </property>
    </bean>
    ...

```

Mapping scripts can also be *dynamically* appended to a global mapping service instance.
See [Custom Mappings](customMappings.md) for details.


### Configuring the mappings

A mapping example (referenced above as `example.groovy`) is displayed below.
The example maps a couple of codes from HL7-related code systems.

```groovy

mappings = {
   encounterType(['2.16.840.1.113883.12.4','2.16.840.1.113883.5.4'],
      E : 'EMER',
      I : 'IMP',
      O : 'AMB'
)

   vip(['2.16.840.1.113883.12.99','2.16.840.1.113883.5.1075'],
      Y : 'VIP',
      (ELSE) : { it }
)

   messageType(
      'ADT^A01' : 'PRPA_IN402001'
     (ELSE) : { throw new HL7Exception("Invalid message type", 207) }
   )
}

```

This defines three mappings (`encounterType`, `vip`, and `messageType`), having an optional definition for
ISO Object Identifiers (OIDs) to identify key and value code systems.

The encounterType mapping has three entries, while the vip and messageType mappings have only one.


### Using the Mapping Service

The Mapping Service can be used directly. By using the Groovy metaclass extension, however, the `map` and
`mapReverse` methods can be directly applied to strings and collections:

```groovy

// using the service bean reference
def x = mappingService.get('encounterType', 'E')

// more concise: using the dynamic map method on a string instance
def y = 'E'.map('encounterType')

// even more concise
def z = 'E'.mapEncounterType()

// x == y == z == 'EMER'

```

The `ELSE` entry is called on `MappingService#get` request with unknown keys. `ELSE` can be

* a Closure, which takes the key as parameter and is then executed
* any other Object o, which will return o.toString().

In the mapping example above,

* for the `vip` mapping the key is returned, so that mappingService.get('vip', 'X') == 'X'
* for the `messageType` mapping, an Exception is thrown.


The Mapping Service also allows mapping in the backward direction.
In case that a mapping definition maps more than one key to the same value (e.g. A->C and B->C),
the backward mapping only contains the last entry, i.e. C->B.

```groovy

// using the service bean reference
def x = mappingService.getKey('vip', 'VIP')

// more concise: using the dynamic map method on a string instance
def y = 'VIP'.mapReverse('vip')

// even more concise
def z = 'VIP'.mapReverseVip()

// x == y == z == 'Y'
```

`ELSE` is also allowed in mapping in the backward direction:

```groovy
mappings = {
  reverseMapping(
    key            : 'value',
    (ELSE)         : 'unknownKey',
    'unknownValue' : (ELSE)
  )

  reverseMappingWithClosures(
    key       : 'value',
    (ELSE)    : 'unknownKey',
    // backwards default mapping to an existing key without conflict:
    { 'key' } : (ELSE)
  )
}
```

