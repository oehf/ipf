## FHIR

FHIR® – Fast Healthcare Interoperability Resources (hl7.org/fhir) – is a next generation standards framework created by HL7,
combining the best features of HL7v2, HL7v3, and CDA while leveraging the latest web standards and applying a tight focus on implementability.

IHE has had a set of profiles based on FHIR, but they are were on earlier versions of HL7 FHIR. Now that [DSTU2](http://hl7.org/fhir/index.html) is formally published, 
IHE updated their profiles to this version. IPF adds support for a subset of them by providing Camel components (hiding the 
 implementation details on transport level) and translators between the FHIR and HL7 v2 message models.

Current supported transactions are:

* [ITI-65] Provide Document Bundle
* [ITI-66] Find Document Manifests
* [ITI-67] Find Document References
* [ITI-68] Retrieve Document
* [ITI-78] Patient Demographics Query for Mobile
* [ITI-81] Retrieve ATNA Audit Event
* [ITI-83] Patient Identifier Cross-reference for Mobile

[ITI-65]: ../ipf-platform-camel-ihe-fhir-mhd/iti65.html
[ITI-66]: ../ipf-platform-camel-ihe-fhir-mhd/iti66.html
[ITI-67]: ../ipf-platform-camel-ihe-fhir-mhd/iti67.html
[ITI-68]: ../ipf-platform-camel-ihe-fhir-mhd/iti68.html
[ITI-78]: ../ipf-platform-camel-ihe-fhir-pixpdq/iti78.html
[ITI-81]: ../ipf-platform-camel-ihe-fhir-atna/iti81.html
[ITI-83]: ../ipf-platform-camel-ihe-fhir-pixpdq/iti83.html