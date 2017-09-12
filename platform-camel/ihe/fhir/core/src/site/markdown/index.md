## FHIR

FHIR® – Fast Healthcare Interoperability Resources (hl7.org/fhir) – is a next generation standards framework created by HL7,
combining the best features of HL7v2, HL7v3, and CDA while leveraging the latest web standards and applying a tight focus on implementability.

IHE has had a set of profiles based on FHIR, but they are were on earlier versions of HL7 FHIR. Now that [DSTU2](https://hl7.org/fhir/DSTU2/index.html) 
and [STU3](https://hl7.org/fhir/index.html) are formally published,
IHE updated their profiles to these versions. IPF adds support for a subset of them by providing Camel components (hiding the 
 implementation details on transport level) and translators between the FHIR and HL7 v2 message models.

While the FHIR transactions in IHE ITI revision 13 (2016/2017) was based on FHIR DSTU2,
the transactions have been migrated to STU3 for IHE ITI revision 14 (2017/2018).

Supported transactions for DSTU2 are:

* [ITI-65](../ipf-platform-camel-ihe-fhir-dstu2-mhd/iti65.html) Provide Document Bundle
* [ITI-66](../ipf-platform-camel-ihe-fhir-dstu2-mhd/iti66.html) Find Document Manifests
* [ITI-67](../ipf-platform-camel-ihe-fhir-dstu2-mhd/iti67.html) Find Document References
* [ITI-68](../ipf-platform-camel-ihe-fhir-dstu2-mhd/iti68.html) Retrieve Document
* [ITI-78](../ipf-platform-camel-ihe-fhir-dstu2-pixpdq/iti78.html) Patient Demographics Query for Mobile
* [ITI-81](../ipf-platform-camel-ihe-fhir-dstu2-atna/iti81.html) Retrieve ATNA Audit Event
* [ITI-83](../ipf-platform-camel-ihe-fhir-dstu2-pixpdq/iti83.html) Patient Identifier Cross-reference for Mobile

Note that DSTU2 transactions are deprecated and support for them will be removed in one of the upcoming IPF versions.
Bug fixes and changes will be done for STU3 only. 

Currently supported transactions for STU3 are:

* [ITI-65](../ipf-platform-camel-ihe-fhir-stu3-mhd/iti65.html) Provide Document Bundle
* [ITI-66](../ipf-platform-camel-ihe-fhir-stu3-mhd/iti66.html) Find Document Manifests
* [ITI-67](../ipf-platform-camel-ihe-fhir-stu3-mhd/iti67.html) Find Document References
* [ITI-68](../ipf-platform-camel-ihe-fhir-stu3-mhd/iti68.html) Retrieve Document
* [ITI-78](../ipf-platform-camel-ihe-fhir-stu3-pixpdq/iti78.html) Patient Demographics Query for Mobile
* [ITI-81](../ipf-platform-camel-ihe-fhir-stu3-atna/iti81.html) Retrieve ATNA Audit Event
* [ITI-83](../ipf-platform-camel-ihe-fhir-stu3-pixpdq/iti83.html) Patient Identifier Cross-reference for Mobile