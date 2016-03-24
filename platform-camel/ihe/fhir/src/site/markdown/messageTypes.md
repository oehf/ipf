## Handling of data types and exceptions in IPF FHIR Components

IPF FHIR IHE endpoints expect and provide certain conent in the Camel message body and headers.
These types differ depending on whether an endpoint acts as producer or consumer.

### Consumer-side requests

Consumer-side requests are automatically unmarshalled, i.e. the incoming message stream sent by the client 
is transformed into a HAPI FHIR resource object (for write operations) or into message header parameters
 (for search operations).
If unmarshalling fails, an FHIR response automatically generated and passed back to the sender.

| Transaction      | Request Message Type (`org.hl7.fhir.instance.model...`) | Request Message Headers
|------------------|-------------------------------------------------------- | --------------------------
| [ITI-78]         | n/a                                                     | Query Parameters
| [ITI-83] 	       | n/a                                                     | Query Parameters


### Producer-side responses

Producer-side responses are automatically unmarshalled, i.e. the incoming message stream returned by the server 
is transformed into a HAPI FHIR resource. When unmarshalling fails, an exception will be thrown.

| Transaction       | Response Message Type (`org.hl7.fhir.instance.model...`) 
|-------------------|---------------------------------------------------------
| [ITI-78] (search) | `Bundle` containing matching `Patient` resources 
| [ITI-78] (get)    | `Patient` resource
| [ITI-83]          | `Parameters` containing matching identifiers     

### Consumer-side responses

Consumer-side responses are accepted in a number of data types, depending on the transaction, corresponding with
the producer-side responses (see above).

Additionally, exceptions are translated into a corresponding HTTP status code and `OperationOutcome` content.
Please refer to the [HAPI FHIR documentation](http://jamesagnew.github.io/hapi-fhir/doc_rest_server.html#ExceptionError_Handling)
for details.


### Producer-side requests

Data types for the *request* message of the supported transactions on producer (i.e. client) side are listed in the table below:

| Transaction       | Request Message Type  |
|-------------------|-------------------------------------------------------- | 
| [ITI-78] (search) | `ca.uhn.fhir.rest.gclient.ICriterion` or URL string     | 
| [ITI-78] (get)    | String with the Patient resource identifier             | 
| [ITI-83]          | `org.hl7.fhir.instance.model.Parameters`                | 

The URL string may be complete (e.g. http://example.com/base/Patient?name=foo) in which case the client's base URL will be ignored. 
Or it can be relative (e.g. Patient?family=smith) in which case the client's base URL will be used.


[ITI-78]: iti78.html
[ITI-83]: iti83.html