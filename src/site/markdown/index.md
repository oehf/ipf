
## About

The Open eHealth Integration Platform (IPF) provides interfaces for health-care related integration solutions.
An prominent example of an healthcare-related use case of IPF is the implementation of interfaces for transactions specified
in Integrating the Healthcare Enterprise ([IHE][ihe]) profiles.

IPF can be easily embedded into any Java application and additionally supports deployments inside OSGi environments.

IPF is built upon and extends the [Apache Camel](http://camel.apache.org) routing and mediation engine. It has an application programming layer
based on the [Groovy](http://www.groovy-lang.org) programming language and comes with comprehensive support for message processing and connecting
systems in the eHealth domain. IPF provides domain-specific languages (DSLs) for implementing
[Enterprise Integration Patterns](http://www.enterpriseintegrationpatterns.com/)
in general-purpose as well as healthcare-specific integration solutions.


## Use it

IPF uses [Maven](http://maven.apache.org) as build tool.
Depending on your project needs you might want to define dependencies to various IPF artifacts. For example,
the following statements will include all dependencies needed to work with IPF interfaces for MLLP-based
HL7v2 [IHE][ihe] transactions:


```xml
<dependency>
  <groupId>org.openehealth.ipf.platform-camel</groupId>
  <artifactId>ipf-platform-camel-ihe-mllp</artifactId>
  <version>3.0</version>
</dependency>
```

Now you can expose or consume IHE-compliant transaction endpoints.


## Features

The following table summarizes the IPF features related to the eHealth domain:

| Feature                                         | Description
|-------------------------------------------------|-----------------------------------------------
| [Support for eHealth integration profiles]      | A set of components for creating actor interfaces as specified in IHE and Continua integration profiles. IPF currently supports creation of actor interfaces for the IHE profiles XDS.a, XDS.b, PIX, PDQ, PIXv3, PDQv3, QED, XCPD, XCA, XCA-I, XCF, XPID, PCD, as well as for Continua profiles HRN and WAN.
| [HL7 Message processing]                        | Basis for HL7 message processing is the HL7v2 DSL. These provides the basis for implementing [HL7 Message processing Camel routes].
| [HL7 Message translation]                       | Translation utilities for translating between HL7v3 and HL7v2 messages for correspdoning IHE transactions
| [CDA Support]                                   | Wrapping a number of CDA-related libraries, providing the basis for implementing [CDA processing Camel routes].


Other IPF features provide part of the underlying foundation or supporting functionality:

| Feature                                         | Description
|-------------------------------------------------|-----------------------------------------------
| [Core Features]                                 | Domain-neutral message processors and DSL extensions usable for general-purpose message processing.
| [Code System Mapping]                           | A simplistic mechanism for mapping code values between code systems
| [Dynamic Feature Registration]                  | Aids in building up modular integration solutions where each module contributes routes, services etc. to the overall application


IPF is prepared to run in OSGi environments as well:

| Feature                                         | Description
|-------------------------------------------------|-----------------------------------------------
| [OSGi Support]                                  | Enables the deployment of IPF modules (bundles) to OSGi platforms. IPF service bundles register platform services at the OSGi service registry for consumption by IPF applications



## Tutorials and Examples

| Tutorial                                        | Description
|-------------------------------------------------|-----------------------------------------------
| [HL7 Support tutorial]                          | How to integration HL7 message processing into Camel
| [XDS tutorial]                                  | A Groovy based implementation of an XDS repository
| [Dynamic extension tutorial]                    | How to have IPF-based application modules contributing to an application
| [IHE Client Example]                            | Some examples how to use IPF producer endpoints
| [OSGi tutorial]                                 | OSGi Tutorial

## Non-functional aspects

| Feature                                         | Description
|-------------------------------------------------|-----------------------------------------------
| [Recoverability]                                | Recoverability means that a system can recover from crashes or service failures without losing messages or data
| [Performance] Monitoring                        | Monitor performance and throughput of routes


## Migration

IPF 3.x is not fully backwards-compatible with IPF 2.x. Read the [Migration Instructions] for how to migrate a
IPF 2.x-based integration solution.


## Development

[Contribute][development] by reporting issues, suggesting new features, or forking the
Git repository on [GitHub][ipf-github] and provide some good pull requests!


### License

IPF code is Open Source and licensed under [Apache license][apache-license].


[apache-license]: http://www.apache.org/licenses/LICENSE-2.0
[development]: development.html
[ipf-github]: http://github.com/oehf/ipf
[ihe]: http://www.ihe.net
[Support for eHealth integration profiles]: ipf-platform-camel-ihe/index.html
[HL7 Message processing]: ipf-modules-hl7/index.html
[HL7 Message processing Camel routes]: ipf-platform-camel-hl7/index.html
[HL7 Message translation]: ipf-commons-ihe-hl7v3/index.html
[CDA Support]: ipf-modules-cda/index.html
[CDA processing Camel routes]: ipf-platform-camel-cda/index.html
[Core Features]: ipf-platform-camel-core/index.html
[Code System Mapping]: ipf-commons-map/index.html
[Dynamic Feature Registration]: dynamic.html
[OSGi Support]: osgi/index.html
[Migration Instructions]: migration.html
[Recoverability]: recoverability.html
[Performance]: performance.html
[HL7 Support tutorial]: ipf-tutorials-hl7/index.html
[XDS tutorial]: ipf-tutorials-xds/index.html
[Dynamic extension tutorial]: ipf-tutorials-config/index.html
[IHE Client Example]: ipf-tutorials-iheclient/index.html