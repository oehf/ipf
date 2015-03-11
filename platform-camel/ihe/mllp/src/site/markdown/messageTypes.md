
## Handling of data types and exceptions in MLLP-based IPF IHE components

The IHE Camel components provided by IPF accept and produced a fixed set of message types. These types differ
depending on whether an endpoint acts as producer or consumer.

### Consumer-side requests

Consumer-side requests are automatically unmarshalled, i.e. the incoming message stream sent by the client 
is transformed into a HAPI Message object. When unmarshalling fails, an HL7 NAK response will be 
automatically generated and passed back to the sender.

### Producer-side responses

Producer-side responses are automatically unmarshalled, i.e. the incoming message stream returned by the server 
is transformed into a HAPI Message object. When unmarshalling fails, an exception will be thrown.

### Consumer-side responses

Consumer-side responses are accepted in a number of data types that will be converted into a HL7 message stream
to be returned to the client:

* HAPI `ca.uhn.hl7v2.model.Message`
* `String`
* `byte[]`
* `java.nio.ByteBuffer`
* `java.io.InputStream`
* `java.io.File`
* `org.apache.camel.component.file.GenericFile<File>`
* `org.apache.camel.WrappedFile<File>`

In addition, the message body can contain an `Exception` instance, which will be transformed into a NAK response. 
Any exceptions thrown in the route that are not handled otherwise will lead to NAK responses as well.
When neither the data type of the response message is supported nor an exception has been thrown in the route, the message header `org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpComponent.ACK_TYPE_CODE_HEADER` will be taken into consideration. 

When the value of this header belongs to the enumeration type `ca.uhn.hl7v2.AcknowledgmentCode`, an acknowledgement will be
automatically generated and sent back to the client — a positive one for `AcknowledgmentCode.AA`,
a negative one (NAK) for `AcknowledgmentCode.AE` and `AcknowledgmentCode.AR`.

When even this header is not set or when its value is not of desired type, the consumer route fails.

### Producer-side requests

Producer-side requests are accepted in a number of data types that will be converted into a HL7 message stream
to be sent to the server:

* `ca.uhn.hl7v2.model.Message` (HAPI)
* `String`
* `byte[]`
* `java.nio.ByteBuffer`
* `java.io.InputStream`
* `java.io.File`
* `org.apache.camel.component.file.GenericFile<File>`
* `org.apache.camel.WrappedFile<File>`

