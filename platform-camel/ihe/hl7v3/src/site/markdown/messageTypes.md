## Supported data types in HL7v3-based IPF IHE components

Currently, HL7v3-based components do not use any special data model. Web Service metadata (WSDL documens) reflects
this peculiarity by declaring all message parts to be `xsd:anyType`.

What the components expect to obtain from a Camel route (e.g. outgoing requests on producer side and outgoing responses
on consumer side) is an XML String containing an HL7 v3 message, or something that can be transformed into such String
by the means of [Camel type converters](http://camel.apache.org/type-converter.html) — e.g. byte array,
input stream, stream reader, DOM document, XSLT source, etc.

What the components deliver to a Camel route, is always an XML String.

Camel's Groovy [DSL extensions for XML processing](http://camel.apache.org/groovy-dsl.html) can be efficiently used to
handle and prepare request and response messages.