## IPF 3.5 Migration Guide

IPF 3.5 comes with some changes that must be considered when upgrading from IPF 3.4.


### ATNA Audit changes

ATNA auditing was reimplemented, which makes the need of the OpenHealthTools libraries obsolete.

For migration, replace your legacy ATNA configuration. For details on how to configure ATNA auditing now, 
please check [here](../ipf-platform-camel-ihe/atna.html).

If you only require one `AuditContext` for all transactions, you can keep the legacy `audit=<boolean>`
parameter in your endpoint URIs. It is recommended, however, to switch to the new `auditContext=#ref`
parameter, where `ref` is the ID of a `AuditContext` bean.

The parameters for executing RFC 5425-compliant auditing over TLS are now exclusively derived from
the standard [JSSE parameters](https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#InstallationAndCustomization).

If you derived your own ATNA auditing from the OpenHealthTools libraries, you might want to migrate to the new API
in order to avoid redundant configuration. Please check [here](../ipf-commons-audit/index.html) for details on
the new API and configuration.
If you choose to migrate your own ATNA auditing at a later point in time, you can continue to use the legacy
library by explicitly depending on `org.openehealth.ipf.oht.atna:ipf-oht-atna:3.6.0`. 

### Package Changes

In order to avoid split packages (violating the module concept as of Java 9), a couple of package names
were changed to avoid that different IPF modules expose classes under the same package. Most likely,
the following changes will affect library users:

* `ipf-commons-spring` module: classes were moved from `org.openehealth.ipf.commons.*` to `org.openehealth.ipf.commons.spring.*`
* `ipf-commons-ihe-fhir-dstu2-core` module: classes were moved from `org.openehealth.ipf.commons.ihe.fhir.*` to `org.openehealth.ipf.commons.ihe.fhir.support.*`
* `ipf-commons-ihe-fhir-stu3-core` module: classes were moved from `org.openehealth.ipf.commons.ihe.fhir.*` to `org.openehealth.ipf.commons.ihe.fhir.support.*`


### Removal of deprecated functionality in IPF 3.5

The following deprecated classes will be eventually removed in IPF 3.5

* all deprecated classes in `ipf-modules-hl7`, particularly the old validation rule builders