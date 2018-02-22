## DICOM audit support

The `ipf-commons-audit` module contains a reimplementation and improvement of the ATNA functionality 
contained in the `ipf-oht-atna-*` modules that originate from the Eclipse OpenHealthTools project.
This includes building [DICOM]/[ATNA]-compliant Audit Records as well as queueing and sending them to
an Audit Repository.

The most important improvements are:

* type-safe Audit Event builders, that enforce setting mandatory members and avoid setting
coded values into the wrong places (e.g. accidentally setting an 
`ParticipantObjectTypeCodeRole` as `ParticipantObjectTypeCode`).
* possibility to select an Audit Message XML schema that belongs to a specific [DICOM] version
* possibility to plug-in your own exception handler in case the Audit Repository is not reachable
* configuration is done via an `AuditContext` bean, which is not a static global setting. You
can use as many `AuditContext` as you wish.


### Constructing an Audit Message

In order to create and submit an Audit Message, perform the following steps

1. Construct your Audit Message by using one of the builder classes contained in the
`org.openehealth.ipf.commons.audit.event` package. Note that the IHE 
modules (such as `ipf-commons-ihe-core` or `ipf-commons-ihe-xds`) inherit from some of
these builders to create IHE-style ATNA-compliant Audit messages more easily.
2. Call the `getMessage` or `getMessages` method to obtain the `AuditMessage` from the
builder.
3. Submit the Audit Message to the configured destination by calling 
`AuditContext#audit(AuditMessage...)` method.

Every Audit Message and Audit Message builder also has a `validate` method that check basic
restrictions defined by DICOM or IHE.

For more details on the API, please study the [javadocs](../apidocs/org/openehealth/ipf/commons/audit/package-frame.html).


### Configuration

The `AuditContext` interface (and its `DefaultAuditContext` implementation) is the only place to configure
static details for auditing, e.g. whether auditing is activated, the location of the Audit Repository and 
the transmission protocol. It also allows to setup strategies for serialization, whether to send synchronously or 
asynchronously, and how errors are handled.

The default setup is to send Audit Messages via UDP to localhost:514, and handle errors just by logging them.
For production usage, it is usually required to configure a TLS connection to a remote Audit Repository and
some decent strategy for handling failed connections to the Audit Repository.

 
[DICOM]: https://dicom.nema.org/medical/dicom/current/output/html/part15.html#sect_A.5
[ATNA]: http://ihe.net/uploadedFiles/Documents/ITI/IHE_ITI_TF_Vol2a.pdf