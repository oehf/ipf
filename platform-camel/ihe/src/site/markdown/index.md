
## IPF components for eHealth integration profiles

IPF provides support for several eHealth profiles, primarily from the [IHE] ITI domain.
The basic idea is to offer a [Camel component](http://camel.apache.org/components.html) for each profile transaction.
These components ensure that the technical requirements of the profile are met by applications built on top of the
IPF eHealth Integration Profiles support.

### Context

Core concepts of IHE profiles are:

| Concept          | IHE Definition (Technical Framework, Volume 1) | Description  | Example
|------------------|------------------------------------------------|--------------|--------
| Actor            | Actors are information systems or components of information systems that produce, manage, or act on categories of information required by operational activities in the enterprise. | An application role in a distributed system | Patient Identity Cross-Reference Manager aka PIX Manager.
| Transaction      | Transactions are interactions between actors that communicate the required information through standards-based messages. 	                                                         | A message exchange between actors           | Patient Identity Deed between the Patient Identity Source and the PIX Manager
| Profile          | Each integration profile is a representation of a real-world capability that is supported by a set of actors that interact through transactions.                                    | A set of actors and transactions            | PIX profile

IPF is a development framework with special support for the implementation of IHE concepts (i.e. profiles).
This is illustrated with an abstract IHE profile the figure below.

The profile defines three actors and two transactions. Transaction 1 is between actor 1 and actor 2 whereas transaction 2 is between actor 1 and actor 3.

![Abstract IHE Profile](images/ihe-1.JPG)

With IPF, these IHE concepts are mapped to [Camel components](http://camel.apache.org/component.html), partly by reusing
or extended existing Camel components or by providing [custom components](http://camel.apache.org/writing-components.html).

| Concept          | Mapping to the IPF/Camel programming model
|------------------|----------------------------------------------------------------------
| Actor            | Producer or Consumer of a [Camel endpoint](http://camel.apache.org/endpoint.html)
| Transaction      | Camel component, e.g. `pix-iti8`
| Profile          | Group of Camel components, e.g. `pix`, `xds`


IHE Profiles are grouped by their underlying technical foundation, particularly by their *message format* and
*transport protocol* into IPF modules that can be included as dependencies in the Maven project descriptor:

| Module                           | Provided IHE transactions
|----------------------------------|-----------------------------------------
| ipf-platform-camel-ihe-mllp      | [ITI-8], [ITI-9], [ITI-10], [ITI-21], [ITI-22], [ITI-30], [ITI-31], [ITI-46]
| ipf-platform-camel-ihe-xds       | [ITI-14], [ITI-15], [ITI-16], [ITI-17], [ITI-18], [ITI-38], [ITI-39], [ITI-41], [ITI-42], [ITI-43], [ITI-51], [ITI-57], [ITI-61], [ITI-62], [ITI-63], [RAD-69], [RAD-75]
| ipf-platform-camel-ihe-hl7v3     | [ITI-44], [ITI-45], [ITI-46], [ITI-47], [ITI-55], [ITI-56], [PCC-1]
| ipf-platform-camel-ihe-hl7v2ws   | [PCD-01]

### Example

An implementation of a *Patient Identity Cross-Reference Manager* actor for the *Patient Identity Feed* (PIX ITI-8) transaction
shall be created. PIX ITI-8 is a HL7v2-based transaction using MLLP as transport protocol.
Receiving actors are implemented as a [Camel consumer](http://camel.apache.org/maven/current/camel-core/apidocs/org/apache/camel/Consumer.html).

Thus, the following dependency must be registered in `pom.xml`:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.platform-camel</groupId>
        <artifactId>ipf-platform-camel-ihe-mllp</artifactId>
        <version>${ipf-version}</version>
    </dependency>
```

The basic pattern for consumers is to specify the component name in the URI parameter of a `from`-clause at the beginning of a Camel route:

```java
    // IHEConsumer.java
    from("pix-iti8://0.0.0.0:8777?parameters....")
      .process(...)
      // process the incoming HL7v2 request and create a response
```

While stepping through the Camel route, a proper response (or an Exception) must be generated that is sent back to the caller.

On the other side of the transaction, sending actors (for ITI-8 the *Patient Identity Source*) are implemented as a
[Camel producer](http://camel.apache.org/maven/current/camel-core/apidocs/org/apache/camel/Producer.html).
The basic pattern for producers is to specify the component name in the URI parameter of a to-clause at the end of a Camel route:

```java
    // IHEProducer.java
    from(...)
      .process(...)
      // create a ITI-8 request (i.e. HL7v2 message)
      .to("pix-iti8://mpiserver.com:8888?parameters....")
      // optionally process the response
```


### Transaction Parameters

The parameters usually depend on the IHE transaction; some parameters are valid for groups of transactions,
a few are even valid for all transactions (e.g. turning ATNA auditing on or off). Details are given in the respective pages
describing each component in detail

The most IPF eHealth components are named according to the profile and the transaction they implements
(transaction IDs and profiles relate to IHE, when not stated otherwise).
A special case is the MLLP dispatcher component which allows to accept requests for multiple MLLP-based transactions through a single TCP port.


----

#### Transactions

*  [ITI-8] Patient Identity Feed
*  [ITI-9] PIX Query
* [ITI-10] PIX Update Notification
* [ITI-14] Register Document Set
* [ITI-15] Provide & Register Document Set
* [ITI-16] Query Registry
* [ITI-17] Retrieve Document
* [ITI-18] Registry Stored Query
* [ITI-19] Authenticate Node
* [ITI-20] Record Audit Event
* [ITI-21] Patient Demographics Query
* [ITI-22] Patient Demographics and Visit Query
* [ITI-30] Patient Identity Management
* [ITI-31] Patient Encounter Management
* [ITI-38] Cross-Gateway Query
* [ITI-39] Cross-Gateway Retrieve
* [ITI-41] Provide & Register Document Set
* [ITI-42] Register Document Set
* [ITI-43] Retrieve Document Set
* [ITI-44] Patient Identity Feed v3
* [ITI-45] PIX Query v3
* [ITI-46] PIX Update Notification v3
* [ITI-47] Patient Demographics Query (PDQ) v3
* [ITI-51] Multi-Patient Stored Query
* [ITI-55] Cross-Gateway Patient Discovery
* [ITI-56] Cross-Gateway Patient Location Query
* [ITI-57] Update Document Set
* [ITI-61] Register On-Demand Document Entry
* [ITI-62] Delete Document Set
* [ITI-63] Cross-Gateway Fetch
* [ITI-64] Notify XAD-PID Link Change
* [RAD-69] Retrieve Imaging Document Set
* [RAD-75] Cross-Gateway Retrieve Imaging Document Set
*  [PCC-1] Query for Existing Data
* [PCD-01] Communicate Patient Care Device Data
* [All] MLLP-based Dispatcher
* [Custom] MLLP-based Custom Transactions

---

### Supported Transactions

The table below references all supported eHealth transactions. Click on the link in the first column for details about
required dependencies, usage and parameters.


| Transaction  | Profile       | Description                          | IPF Component           | Transport     | Message Format
|:------------ |:------------- |:-------------------------------------|:----------------------- |:------------- |:--------------
| [ITI-8]      | PIX, XDS.a+b  | Patient Identity Feed                | `pix-iti8`, `xds-iti8`  | MLLP(S)       | HL7 v2.3.1
| [ITI-9]      | PIX           | PIX Query                            | `pix-iti9`              | MLLP(S)       | HL7 v2.5
| [ITI-10]     | PIX           | PIX Update Notfication               | `pix-iti10`             | MLLP(S)       | HL7 v2.5
| [ITI-14]     | XDS.a         | Register Document Set                | `xds-iti14`             | SOAP/HTTP(S)  | ebXML
| [ITI-15]     | XDS.a         | Provide & Register Document Set      | `xds-iti15`             | SOAP/HTTP(S)  | ebXML
| [ITI-16]     | XDS.a         | Query Registry                       | `xds-iti16`             | SOAP/HTTP(S)  | ebXML
| [ITI-17]     | XDS.a         | Retrieve Document                    | `xds-iti17`             | HTTP(S)       | HTTP
| [ITI-18]     | XDS.a+b       | Registry Stored Query                | `xds-iti18`             | SOAP/HTTP(S)  | ebXML
| ITI-19       | ATNA          | Authenticate Node                    | n/a                     | n/a           | n/a
| ITI-20       | ATNA          | Record Audit Event                   | n/a                     | Syslog (+TLS) | RFC-3881
| [ITI-21]     | PDQ           | Patient Demographics Query           | `pdq-iti21`             | MLLP(S)       | HL7 v2.5
| [ITI-22]     | PDQ           | Patient Demographics and Visit Query | `pdq-iti22`             | MLLP(S)       | HL7 v2.5
| [ITI-30]     | PAM           | Patient Identity Management          | `pam-iti30`             | MLLP(S)       | HL7 v2.5
| [ITI-31]     | PAM           | Patient Encounter Management         | `pam-iti31`             | MLLP(S)       | HL7 v2.5
| [ITI-38]     | XCA           | Cross-Gateway Query                  | `xca-iti38`             | SOAP/HTTP(S)  | ebXML
| [ITI-39]     | XCA           | Cross-Gateway Retrieve               | `xca-iti39`             | SOAP/HTTP(S)  | ebXML
| [ITI-41], Continua HRN | XDS.b, Continua | Provide & Register Document Set | `xds-iti41`      | SOAP/HTTP(S)  | ebXML
| [ITI-42]     | XDS.b         | Register Document Set                | `xds-iti42`             | SOAP/HTTP(S)  | ebXML
| [ITI-43]     | XDS.b         | Retrieve Document Set                | `xds-iti43`             | SOAP/HTTP(S)  | ebXML
| [ITI-44]     | PIXv3, XDS.b  | Patient Identity Feed v3             | `pixv3-iti44`,`xds-iti44` | SOAP/HTTP(S)| HL7v3
| [ITI-45]     | PIXv3         | PIX Query v3                         | `pixv3-iti45`           | SOAP/HTTP(S)  | HL7v3
| [ITI-46]     | PIXv3         | PIX Update Notification v3           | `pixv3-iti46`           | SOAP/HTTP(S)  | HL7v3
| [ITI-47]     | PDQv3         | Patient Demographics Query (PDQ) v3  | `pdqv3-iti47`           | SOAP/HTTP(S)  | HL7v3
| [ITI-51]     | XDS.b         | Multi-Patient Stored Query           | `xds-iti51`             | SOAP/HTTP(S)  | ebXML
| [ITI-55]     | XCPD          | Cross-Gateway Patient Discovery      | `xcpd-iti55`            | SOAP/HTTP(S)  | HL7v3
| [ITI-56]     | XCPD          | Cross-Gateway Patient Location Query | `xcpd-iti56`            | SOAP/HTTP(S)  | HL7v3
| [ITI-57]     | XDS.b         | Update Document Set                  | `xds-iti57`             | SOAP/HTTP(S)  | ebXML
| [ITI-61]     | XDS.b         | Register On-Demand Document Entry    | `xds-iti61`             | SOAP/HTTP(S)  | ebXML
| [ITI-62]     | XDS.b         | Delete Document Set                  | `xds-iti62`             | SOAP/HTTP(S)  | ebXML
| [ITI-63]     | XCF           | Cross-Gateway Fetch                  | `xcf-iti63`             | SOAP/HTTP(S)  | ebXML
| [ITI-64]     | XPID          | Notify XAD-PID Link Change           | `xpid-iti64`            | MLLP(S)       | HL7 v2.5
| [RAD-69]     | XDS-I.b, XCA-I.b | Retrieve Imaging Document Set     | `xdsi-rad69`            | SOAP/HTTP(S)  | ebXML
| [RAD-75]     | XCA-I.b       | Cross-Gateway Retrieve Imaging Document Set | `xcai-rad75`     | SOAP/HTTP(S)  | ebXML
| [PCC-1]      | QED           | Query for Existing Data (QED)        | `qed-pcc1`              | SOAP/HTTP(S)  | HL7v3
| [PCD-01], Continua WAN | PCD, Continua | Communicate Patient Care Device (PCD) Data | `pcd-pcd01` | SOAP/HTTP(S) | HL7v2
| [All] MLLP-based | n/a         | Accept requests for multiple MLLP-based transactions through a single TCP port | `mllp-dispatch` | MLLP(S) | HL7 v2 |
| [Custom] MLLP-based | n/a      | Accept requests for custom MLLP-based transactions | `mllp` | MLLP(S) | HL7v2 |

[ITI-8]: ../ipf-platform-camel-ihe-mllp/iti8.html
[ITI-9]: ../ipf-platform-camel-ihe-mllp/iti9.html
[ITI-10]: ../ipf-platform-camel-ihe-mllp/iti10.html
[ITI-14]: ../ipf-platform-camel-ihe-xds/iti14.html
[ITI-15]: ../ipf-platform-camel-ihe-xds/iti15.html
[ITI-16]: ../ipf-platform-camel-ihe-xds/iti16.html
[ITI-17]: ../ipf-platform-camel-ihe-xds/iti17.html
[ITI-18]: ../ipf-platform-camel-ihe-xds/iti18.html
[ITI-21]: ../ipf-platform-camel-ihe-mllp/iti21.html
[ITI-22]: ../ipf-platform-camel-ihe-mllp/iti22.html
[ITI-30]: ../ipf-platform-camel-ihe-mllp/iti30.html
[ITI-31]: ../ipf-platform-camel-ihe-mllp/iti31.html
[ITI-38]: ../ipf-platform-camel-ihe-xds/iti38.html
[ITI-39]: ../ipf-platform-camel-ihe-xds/iti39.html
[ITI-41]: ../ipf-platform-camel-ihe-xds/iti41.html
[ITI-42]: ../ipf-platform-camel-ihe-xds/iti42.html
[ITI-43]: ../ipf-platform-camel-ihe-xds/iti43.html
[ITI-44]: ../ipf-platform-camel-ihe-hl7v3/iti44.html
[ITI-45]: ../ipf-platform-camel-ihe-hl7v3/iti45.html
[ITI-46]: ../ipf-platform-camel-ihe-hl7v3/iti46.html
[ITI-47]: ../ipf-platform-camel-ihe-hl7v3/iti47.html
[ITI-51]: ../ipf-platform-camel-ihe-xds/iti51.html
[ITI-55]: ../ipf-platform-camel-ihe-hl7v3/iti55.html
[ITI-56]: ../ipf-platform-camel-ihe-hl7v3/iti56.html
[ITI-57]: ../ipf-platform-camel-ihe-xds/iti57.html
[ITI-61]: ../ipf-platform-camel-ihe-xds/iti61.html
[ITI-62]: ../ipf-platform-camel-ihe-xds/iti62.html
[ITI-63]: ../ipf-platform-camel-ihe-xds/iti63.html
[ITI-64]: ../ipf-platform-camel-ihe-mllp/iti64.html
[RAD-69]: ../ipf-platform-camel-ihe-xds/rad69.html
[RAD-75]: ../ipf-platform-camel-ihe-xds/rad75.html
[PCC-1]: ../ipf-platform-camel-ihe-hl7v3/pcc1.html
[PCD-01]: ../ipf-platform-camel-ihe-hl7v2ws/pcd01.html
[All]: ../ipf-platform-camel-ihe-mllp/mllpDispatch.html
[Custom]: ../ipf-platform-camel-ihe-mllp/mllpCustom.html
[IHE]: http://www.ihe.net