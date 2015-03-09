## Functional extensions to HAPI

While the [HL7v2 DSL] has its focus on providing a domain-specific syntax to navigate in HL7 messages and changing fields
within messages, the functional extensions retrofit a couple of convenient functions on top of HAPI.
By means of Groovy metaprogramming, however, it looks like these extensions are part of the HAPI API, i.e. you can call
the methods on both the raw HAPI objects.

### Custom model class factories

In order to instantiate concrete implementations of Message, Group, Segment etc, the [HAPI] Parsers use a `ModelClassFactory`
member object that looks up classes for these model components. The default implementation provides access to model components
as specified in the HL7 specs.

In real world HL7 projects you frequently need to deal with non-standard HL7 "dialects" which are not covered by the specification
and causes the parser to generate "generic" model classes when used out-of-the-box.

Although [HAPI] already provides a `CustomModelClassFactory` class to address this issue, IPF brings in some additional
flexibility, e.g. by compiling a [Groovy]-based `CustomModelClassFactory` at runtime.

Details are described [here][hl7v2cmcf].


### Creating new instances of messages, structures, and types

You can create a new message from scratch by specifying event type, trigger event and version.
Just as creating a message, you can also create a segment, passing the enclosing `Message` object as argument,
which determines the HL7 version to be used.
Finally, just as creating a message or segment, you can also create a composite or primitive field, passing the enclosing `Message`
object as argument.

Details are described [here][hl7v2creating].


### Mapping HL7 type values

The [Mapping Service] is part of the IPF Core features. After all, although often used in HL7 processing, code system mapping
is not a feature that is inherently exclusive for HL7.

What remains specific to IPF's HL7 v2 support, however, is that the mapping extensions can be applied directly on all [HAPI] types.

Details are described [here][hl7v2mapping].


[HAPI]: http://hl7api.sourceforge.net
[Groovy]: http://groovy.codehaus.org
[Groovy extension module]: http://groovy.codehaus.org/Creating+an+extension+module
[HL7v2 DSL]: hl7v2dsl.html
[hl7v2cmcf]: hl7v2cmcf.html
[hl7v2creating]: hl7v2creating.html
[hl7v2mapping]: hl7v2mapping.html
[Mapping Service]: ../ipf-commons-map/index.html