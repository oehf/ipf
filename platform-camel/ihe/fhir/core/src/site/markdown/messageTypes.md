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
| [ITI-65]         | `Bundle` containing `DocumentManifest`, `DocumentReference` and `Binary` resources  | n/a
| [ITI-66] 	       | n/a                                                     | Query Parameters
| [ITI-67]         | n/a                                                     | Query Parameters
| [ITI-68] 	       | n/a                                                     | n/a
| [ITI-78]         | n/a                                                     | Query Parameters
| [ITI-81] 	       | n/a                                                     | Query Parameters
| [ITI-83] 	       | n/a                                                     | Query Parameters

### Producer-side responses

Producer-side responses are automatically unmarshalled, i.e. the incoming message stream returned by the server 
is transformed into a HAPI FHIR resource. When unmarshalling fails, an exception will be thrown.

| Transaction       | Response Message Type (`org.hl7.fhir.instance.model...`) 
|-------------------|---------------------------------------------------------
| [ITI-65]          | `Bundle` 
| [ITI-66] (search) | `Bundle` containing `DocumentManifest` resources
| [ITI-66] (get)    | `DocumentManifest` resource
| [ITI-67] (search) | `Bundle` containing `DocumentReference` resources
| [ITI-67] (get)    | `DocumentReference` resource
| [ITI-68] 	        | binary content (usually via an `InputStream`)
| [ITI-78] (search) | `Bundle` containing matching `Patient` resources 
| [ITI-78] (get)    | `Patient` resource
| [ITI-81] (get)    | `AuditEvent` resource
| [ITI-83]          | `Parameters` containing matching identifiers


### Consumer-side responses

Consumer-side responses are accepted in a number of data types, depending on the transaction, corresponding with
the producer-side responses (see above).

Additionally, exceptions are translated into a corresponding HTTP status code and `OperationOutcome` content.
Please refer to the [HAPI FHIR documentation](http://hapifhir.io/doc_rest_server.html#ExceptionError_Handling)
for details.


### Producer-side requests

Data types for the *request* message of the supported transactions on producer (i.e. client) side are listed in the table below:

| Transaction       | Request Message Type  |
|-------------------|-------------------------------------------------------- | 
| [ITI-65]          | `Bundle` 
| [ITI-66] (search) | `ca.uhn.fhir.rest.gclient.ICriterion` or URL string
| [ITI-66] (get)    | String with the DocumentManifest resource identifier
| [ITI-67] (search) | `ca.uhn.fhir.rest.gclient.ICriterion` or URL string
| [ITI-67] (get)    | String with the DocumentReference resource identifier
| [ITI-68] 	        | URL string
| [ITI-78] (search) | `ca.uhn.fhir.rest.gclient.ICriterion` or URL string 
| [ITI-78] (get)    | String with the Patient resource identifier
| [ITI-81] (search) | `ca.uhn.fhir.rest.gclient.ICriterion` or URL string
| [ITI-83]          | `org.hl7.fhir.instance.model.Parameters` 

The URL string may be complete (e.g. http://example.com/base/Patient?name=foo) in which case the client's base URL will be ignored. 
Or it can be relative (e.g. Patient?family=smith) in which case the client's base URL will be used.

[ITI-65]: ../ipf-platform-camel-ihe-fhir-mhd/iti65.html
[ITI-66]: ../ipf-platform-camel-ihe-fhir-mhd/iti66.html
[ITI-67]: ../ipf-platform-camel-ihe-fhir-mhd/iti67.html
[ITI-68]: ../ipf-platform-camel-ihe-fhir-mhd/iti68.html
[ITI-81]: ../ipf-platform-camel-ihe-fhir-atna/iti81.html
[ITI-78]: ../ipf-platform-camel-ihe-fhir-pixpdq/iti78.html
[ITI-83]: ../ipf-platform-camel-ihe-fhir-pixpdq/iti83.html