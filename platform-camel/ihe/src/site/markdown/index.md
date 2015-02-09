
## IPF components for eHealth integration profiles

IPF provides support for several eHealth profiles, primarily from the [IHE](http://www.ihe.net) ITI domain.
The basic idea is to offer a [Camel component](http://camel.apache.org/components.html) for each profile transaction.
These components ensure that the technical requirements of the profile are met by applications built on top of the
IPF eHealth Integration Profiles support.


### Example

Receiving actors are implemented as a [Camel consumer](http://camel.apache.org/maven/current/camel-core/apidocs/org/apache/camel/Consumer.html).
The basic pattern is to specify the component name in the URI parameter of a `from`-clause at the beginning of a Camel route:

```java
// IHEConsumer.java
from("pix-iti8://0.0.0.0:8777?parameters....")
  .process(...)
  // process the incoming request and create a response
```

While stepping through the route, a proper response (or an Exception) must be generated that is sent back to the caller.

Sending actors are implemented as a [Camel producer](http://camel.apache.org/maven/current/camel-core/apidocs/org/apache/camel/Producer.html).
The basic pattern is to specify the component name in the URI parameter of a to-clause at the end of a Camel route:

```java
// IHEProducer.java
from(...)
  .process(...)
  // create a request
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
| [ITI-19]     | ATNA          | Authenticate Node                    | n/a                     | n/a           | n/a
| [ITI-20]     | ATNA          | Record Audit Event                   | n/a                     | Syslog (+TLS) | RFC-3881
| [ITI-21]     | PDQ           | Patient Demographics Query           | `pdq-iti21`             | MLLP(S)       | HL7 v2.5
| [ITI-22]     | PDQ           | Patient Demographics and Visit Query | `pdq-iti22`             | MLLP(S)       | HL7 v2.5
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

[ITI-8]: mllp/iti8.html
[ITI-9]: mllp/iti9.html
[ITI-10]: mllp/iti10.html
[ITI-14]: http://www.google.de
[ITI-15]: http://www.google.de
[ITI-16]: http://www.google.de
[ITI-17]: http://www.google.de
[ITI-18]: http://www.google.de
[ITI-19]: http://www.google.de
[ITI-20]: http://www.google.de
[ITI-21]: http://www.google.de
[ITI-22]: http://www.google.de
[ITI-38]: http://www.google.de
[ITI-39]: http://www.google.de
[ITI-41]: http://www.google.de
[ITI-42]: http://www.google.de
[ITI-43]: http://www.google.de
[ITI-44]: http://www.google.de
[ITI-45]: http://www.google.de
[ITI-46]: http://www.google.de
[ITI-47]: http://www.google.de
[ITI-51]: http://www.google.de
[ITI-55]: http://www.google.de
[ITI-56]: http://www.google.de
[ITI-57]: http://www.google.de
[ITI-61]: http://www.google.de
[ITI-62]: http://www.google.de
[ITI-63]: http://www.google.de
[ITI-64]: http://www.google.de
[RAD-69]: http://www.google.de
[RAD-75]: http://www.google.de
[PCC-1]: http://www.google.de
[PCD-01]: http://www.google.de
[All]: http://www.google.de
[Custom]: http://www.google.de