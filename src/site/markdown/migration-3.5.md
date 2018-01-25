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
 