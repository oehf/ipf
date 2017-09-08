## IPF 3.4 Migration Guide

IPF 3.2 comes with some changes that must be considered when upgrading from IPF 3.2 or IPF 3.3 to IPF 3.4.


### FHIR-based IHE transaction modules changes

The module ihe-modules-fhir has been removed. It only contributed 3rd party dependencies.

The following deprecated classes have been removed:

* `org.openehealth.ipf.commons.ihe.fhir.CamelFhirServlet`
* `org.openehealth.ipf.commons.ihe.fhir.AuditRecordTranslator`

The following backwards-incompatible changes were done:

* `org.openehealth.ipf.commons.ihe.fhir.CamelFhirServlet` needs to be initialized with a fhirVersion ('DSTU2_HL7ORG' or 'DSTU3')
* `org.openehealth.ipf.commons.ihe.fhir.FhirSearchParameters` defined a new method `getIncludeSpec()` in order to store `_include` and `_revinclude` response modifiers

While adding FHIR-based IHE transactions for the STU3 version of FHIR, it became necessary to split up modules into 
ones that are independent of a specific FHIR version and ones that depend on a specific FHIR version. Most generic
functionality was already version-independent, but IHE transaction functionality obviously requires the appropriate
FHIR resource classes.

Adjust your dependencies to include specifically either the `-dstu2-` or `-stu3-` modules, e.g. 
instead of `ipf-platform-ihe-fhir-pixpdq` use either `ipf-platform-ihe-fhir-dstu2-pixpdq` or `ipf-platform-ihe-fhir-stu3-pixpdq`.

Note that all DSTU2 version code is effectively deprecated, i.e. no bugs or changes will be applied anymore, and related
modules will be removed in one of the upcoming releases.


### IHE Profile Updates

IPF 3.4 is compatible with IHE ITI Revision 14 (published on July 21, 2017), including changes from the following Change Proposals:
