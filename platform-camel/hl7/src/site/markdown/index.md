## Camel DSL Extensions for HL7v2

Working with HL7 messages is explained in the [HL7 Messaging] section.

However, [HAPI] HL7 [`Message`](http://hl7api.sourceforge.net/base/apidocs/ca/uhn/hl7v2/model/Message.html) objects are
also transferred through Camel routes, and at times it is convenient
to have access to these APIs directly in Camel's routing DSL, e.g. to generate an acknowledge that shall be returned to
the client.


### Dependencies

In case you use the IHE MLLP components of IPF, all the required HL7-related libraries are already transitively included.
Otherwise, the following dependency must be registered in `pom.xml`:

```xml

    <!-- IPF HL7 extensions and DSL -->
    <dependency>
        <groupId>org.openehealth.ipf.platform-camel</groupId>
        <artifactId>ipf-platform-camel-hl7</artifactId>
        <version>${ipf-version}</version>
    </dependency>

    <!-- For each HL7 version being used, add structure library as well, e.g. v2.5 -->
    <dependency>
        <groupId>ca.uhn.hapi</groupId>
        <artifactId>hapi-structures-v25</artifactId>
        <version>${hapi-version}</version>
    </dependency>

```


### Camel HL7 support

The [Camel HL7 component][camel-hl7] already provides a convenient set of features, e.g. to marshal and unmarshal
messages, validate them or create acknowledgement responses. Please refer to the [documentation][camel-hl7] for details.


### IPF Extensions to Camel HL7 support

IPF adds a few more features to the [Camel HL7 component][camel-hl7], which are described below.


#### Validation

HL7 v2 messages can be validated in routes with the `verify().hl7()` extension. This differs from the `HL7.messageConforms()`
predicate of Camel in one important aspect:

The Camel predicate can be used for filters or validators, however, by design it just returns `true` or `false`, and the
resulting [`PredicateValidationException`](http://camel.apache.org/maven/current/camel-core/apidocs/org/apache/camel/processor/validation/PredicateValidationException.html)
gives no details whatsoever about the details, i.e. *why* the HL7 validation as
failed and the location of the failure in the message.

In contrast, the IPF validator throws a [`ValidationException`](../apidocs/org/openehealth/ipf/commons/core/modules/api/ValidationException.html) containing all the details
about the validation failure that was provided by the [HAPI] validator classes.

```groovy

    from(...)
      .unmarshal().hl7()
      .verify().hl7()
      ...

```

The code above uses the [HAPI] [`ValidationContext`](http://hl7api.sourceforge.net/base/apidocs/ca/uhn/hl7v2/validation/ValidationContext.html)
associated with the [`HapiContext`](http://hl7api.sourceforge.net/base/apidocs/ca/uhn/hl7v2/HapiContext.html) under which the message was
parsed. A different validation context can be used by specifying the `.profile(Expression)` or `.staticProfile(Object)` modifiers:

```groovy

    from(...)
      .unmarshal().hl7()
      .verify().hl7().profile { exchange ->
         // Calculate `ValidationContext`, `ValidationRuleBuilder` or `HapiContext`
      ...

    from(...)
      .unmarshal().hl7()
      // context can be a `ValidationContext`, `ValidationRuleBuilder` or `HapiContext`
      .verify().hl7().staticProfile(context)

      ...

```

When using plain Java routes, the same behavior can be obtained by using the corresponding Camel processors:

```java

    import org.openehealth.ipf.platform.camel.hl7.HL7v2;

    from(...)
      .unmarshal().hl7()
      .process(HL7v2.validatingProcessor())

    from(...)
      .unmarshal().hl7()
      .process(HL7v2.validatingProcessor(myOtherHapiContext))

```


#### Expressions

By means of the helper class [`org.openehealth.ipf.platform.camel.hl7.HL7v2`](../apidocs/org/openehealth/ipf/platform/camel/hl7/HL7v2.html)
IPF also provides a number of expressions that can be used to extract values from HL7 messages in
Camel exchanges are translate them into a different message. These expression do **not** require the
Groovy programming language.

* `get(String terserSpec)` : returns the value from the field specified with the HAPI [`Terser`](http://hl7api.sourceforge.net/base/apidocs/ca/uhn/hl7v2/util/Terser.html) spec
* `set(String terserSpec, Expression value)` : sets the value of the field specified with the HAPI [`Terser`](http://hl7api.sourceforge.net/base/apidocs/ca/uhn/hl7v2/util/Terser.html) spec
* `ack(...)` : various methods to create an acknowledgement message
* `response(String eventType, String triggerEvent)` : create a response message of the given event type


#### JMS Serialization

Although HAPI messages as such are serializable, their associated
[`Parser`](http://hl7api.sourceforge.net/base/apidocs/ca/uhn/hl7v2/parser/Parser.html) and
[`HapiContext`](http://hl7api.sourceforge.net/base/apidocs/ca/uhn/hl7v2/HapiContext.html) objects are
transient and get lost when a message is routes through a JMS message queue.

The `org.openehealth.ipf.platform.camel.hl7.converter.HL7MessageConverter` can be used to transparently
convert the HAPI message into a text when writing into the JMS queue and back into a HAPI message object
when reading from the JMS queue (you need to provide a dedicated `HapiContext` for the latter operation).

You need to add dependencies to Spring and the JMS API in the `pom.xml` Maven project descriptor, e.g.:

```xml

    <dependency>
        <groupId>org.apache.geronimo.specs</groupId>
        <artifactId>geronimo-jms_1.1_spec</artifactId>
        <version>1.1.1</version>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-jms</artifactId>
        <version>4.1.4</version>
    </dependency>

```



[HAPI]: http://hl7api.sourceforge.net
[camel-hl7]: http://camel.apache.org/hl7.html