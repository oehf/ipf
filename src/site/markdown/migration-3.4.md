## IPF 3.4 Migration Guide

IPF 3.4 comes with some changes that must be considered when upgrading from IPF 3.2 or IPF 3.3 to IPF 3.4.


### FHIR-based IHE transaction modules changes

The module ihe-modules-fhir has been removed. There was no code, the module only contributed 3rd party dependencies.

The following classes were deprecated:

* `org.openehealth.ipf.platform.camel.ihe.fhir.translation.FhirCamelTranslators` : use `org.openehealth.ipf.platform.camel.ihe.fhir.core.FhirCamelTranslators`
* `org.openehealth.ipf.commons.ihe.fhir.translation.TranslatorFhirToHL7v2` : use `org.openehealth.ipf.commons.ihe.fhir.translation.FhirTranslator<Message>`
* `org.openehealth.ipf.commons.ihe.fhir.translation.TranslatorHL7v2ToFhir` : use `org.openehealth.ipf.commons.ihe.fhir.translation.ToFhirTranslator<Message>`


The following deprecated classes have been removed:

* `org.openehealth.ipf.commons.ihe.fhir.CamelFhirServlet`
* `org.openehealth.ipf.commons.ihe.fhir.AuditRecordTranslator`
* `org.openehealth.ipf.platform.camel.ihe.fhir.iti78.Iti78Configuration`
* `org.openehealth.ipf.platform.camel.ihe.fhir.iti83.Iti83Configuration`

The following backwards-incompatible changes were done:

* `org.openehealth.ipf.commons.ihe.fhir.CamelFhirServlet` now requires to be initialized with a fhirVersion parameter ('DSTU2_HL7ORG' or 'DSTU3')
* `org.openehealth.ipf.commons.ihe.fhir.FhirSearchParameters` defines a new method `getIncludeSpec()` in order to store `_include` and `_revinclude` response modifiers


Due to adding FHIR-based IHE transactions for the STU3 version of FHIR, modules were split up into 
ones that are independent of a specific FHIR version and ones that depend on a specific FHIR version. Most generic
functionality was already version-independent, but IHE transaction functionality obviously requires the appropriate
FHIR resource classes.

Adjust your dependencies to include specifically either the `-dstu2-` or `-stu3-` modules, e.g. 
instead of `ipf-platform-ihe-fhir-pixpdq` use either `ipf-platform-ihe-fhir-dstu2-pixpdq` or `ipf-platform-ihe-fhir-stu3-pixpdq`.

Note that all DSTU2 code is effectively deprecated, i.e. no bugs or changes will be applied anymore, and related
modules will be removed in one of the upcoming releases. STU3 modules are actively supported, until one day IHE moves to STU4, etc.


### IHE Profile Updates

IPF 3.4 is compatible with IHE ITI Revision 14 (published on July 21, 2017), including changes from the following Change Proposals:
