
## About

The Open eHealth Integration Platform (IPF) provides interfaces for health-care related integration solutions.
An prominent example of an healthcare-related use case of IPF is the implementation of interfaces for transactions specified
in Integrating the Healthcare Enterprise ([IHE][ihe]) profiles.

IPF can be easily embedded into any Java application and additionally supports deployments inside OSGi environments.

IPF is built upon and extends the [Apache Camel](https://camel.apache.org) routing and mediation engine. It has an application programming layer
based on the [Groovy](https://www.groovy-lang.org) programming language and comes with comprehensive support for message processing and connecting
systems in the eHealth domain. IPF provides domain-specific languages (DSLs) for implementing
[Enterprise Integration Patterns](https://www.enterpriseintegrationpatterns.com/)
in general-purpose as well as healthcare-specific integration solutions.

## What's new

See [the list of fixed Github issues](https://github.com/oehf/ipf/milestone/11?closed=1) for an overview.
We also have some more detailed [Release Notes](changes-report.html).

## Usage

IPF uses [Maven](https://maven.apache.org) as build tool.
Depending on your project needs you might want to define dependencies to various IPF artifacts. For example,
the following statements will include all dependencies needed to work with IPF interfaces for MLLP-based
HL7v2 [IHE][ihe] transactions:


```xml
<dependency>
  <groupId>org.openehealth.ipf.platform-camel</groupId>
  <artifactId>ipf-platform-camel-ihe-mllp</artifactId>
  <version>3.5.0</version>
</dependency>
```

Even better, you can import the IPF bom in the dependency management section. Then you don't have to provide
version numbers for the major dependencies anymore.

```xml
<dependencyManagement>
    <dependency>
        <groupId>org.openehealth.ipf</groupId>
        <artifactId>ipf-dependencies</artifactId>
        <version>3.5.0</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
    ...
<dependencyManagement>


<dependencies>
    <dependency>
      <groupId>org.openehealth.ipf.platform-camel</groupId>
      <artifactId>ipf-platform-camel-ihe-mllp</artifactId>
    </dependency>
    ...
</dependencies>
```

Now you can expose or consume IHE-compliant MLLP-based transaction endpoints, e.g. receiving and validating PIX Feed requests:

```java

    import static org.openehealth.ipf.platform.camel.ihe.mllp.PixPdqCamelValidators.*;
    import org.apache.camel.builder.RouteBuilder;

    from("pix-iti8://0.0.0.0:8777?audit=true&secure=true")
      .process(itiValidator())  // validate incoming request
      .process(myProcessor);    // process the incoming request and create a response

```

## Features

The following table summarizes the IPF features related to the eHealth domain:

| Feature                                         | Description
|-------------------------------------------------|-----------------------------------------------
| [Support for eHealth integration profiles]      | A set of components for creating actor interfaces as specified in IHE and Continua integration profiles. IPF currently supports creation of actor interfaces for the IHE profiles XDS.a, XDS.b, PIX, PDQ, PIXv3, PDQv3, PIXm, PDQm, MHD, QED, XCPD, XCA, XCA-I, XCF, XPID, PCD, as well as for Continua profiles HRN and WAN.
| [HL7 Message processing]                        | Basis for HL7 message processing is the HL7v2 DSL. These provides the basis for implementing [HL7 Message processing Camel routes].
| [HL7 Message translation]                       | Translation utilities for translating between HL7v3 and HL7v2 messages for corresponding IHE transactions
| [CDA Support]                                   | Wrapping a number of CDA-related libraries, providing the basis for implementing [CDA processing Camel routes].
| [FHIR Support]                                  | FHIR® – Fast Healthcare Interoperability Resources (hl7.org/fhir) – is a next generation standards framework created by HL7 leveraging the latest web standards and applying a tight focus on implementability. 
| [DICOM Audit Support]                           | Support for constructing, serializing and sending DICOM audit messages  


Other IPF features provide part of the underlying foundation or supporting functionality:

| Feature                                         | Description
|-------------------------------------------------|-----------------------------------------------
| [Core Features]                                 | Domain-neutral message processors and DSL extensions usable for general-purpose message processing.
| [Code System Mapping]                           | A simplistic mechanism for mapping code values between code systems
| [Dynamic Feature Registration]                  | Aids in building up modular integration solutions where each module contributes routes, services etc. to the overall application


IPF comes with some [Spring Boot Starters](ipf-spring-boot-starter/index.html) that support running eHealth applications
in the Spring Boot runtime environment.


## Removed functionality

* As XDS.a transactions have been retired by IHE, all functionality related with ITI-14, ITI-15, ITI-16 and ITI-17
have been removed. This includes the ebXML/ebRS 2.x model classes.

 
## Added modules

IPF 3.4 added the modules listed below:

 * `ipf-commons-audit`


## Tutorials and Examples

| Tutorial                                        | Description
|-------------------------------------------------|-----------------------------------------------
| [HL7 Support tutorial]                          | How to integration HL7 message processing into Camel
| [XDS tutorial]                                  | A Groovy based implementation of an XDS repository
| [Dynamic extension tutorial]                    | How to have IPF-based application modules contributing to an application
| [IHE Client Example]                            | Some examples how to use IPF producer endpoints

## Non-functional aspects

| Feature                                         | Description
|-------------------------------------------------|-----------------------------------------------
| [Recoverability]                                | Recoverability means that a system can recover from crashes or service failures without losing messages or data
| [Performance Monitoring]                        | Monitor performance and throughput of routes


## Update Instructions

If you are using previous versions of IPF and want to update:

* IPF 3.5.x comes with some changes that must be considered when upgrading from IPF 3.4.x. Read the [3.5 Update Instructions] for how to update from IPF 3.4.x
* IPF 3.4.x comes with some changes that must be considered when upgrading from IPF 3.2.x or IPF 3.3.x to IPF 3.4.x Read the [3.4 Update Instructions] for how to update from IPF 3.1.x
* IPF 3.2.x comes with some changes that must be considered when upgrading from IPF 3.1.x to IPF 3.2.x Read the [3.2 Update Instructions] for how to update from IPF 3.1.x
* IPF 3.1.x introduces a few minor incompatibilities compared to IPF 3.0.x due to having less mandatory dependencies on the Spring framework. Read the [3.1 Update Instructions] for how to update from IPF 3.0.x
* IPF 3.0.x is not backwards-compatible with IPF 2.x. Read the [Migration Instructions] for how to migrate a IPF 2.x-based integration solution.


## Development

[Contribute][development] by reporting issues, suggesting new features, or forking the
Git repository on [GitHub][ipf-github] and provide some good pull requests!


## License

IPF code is Open Source and licensed under [Apache license][apache-license].


[apache-license]: https://www.apache.org/licenses/LICENSE-2.0
[development]: development.html
[ipf-github]: https://github.com/oehf/ipf
[ihe]: https://www.ihe.net
[Support for eHealth integration profiles]: ipf-platform-camel-ihe/index.html
[HL7 Message processing]: ipf-modules-hl7/index.html
[HL7 Message processing Camel routes]: ipf-platform-camel-hl7/index.html
[HL7 Message translation]: ipf-commons-ihe-hl7v3/index.html
[DICOM Audit Support]: ipf-commons-atna/index.html
[FHIR support]: ipf-platform-camel-ihe-fhir-core/index.html
[CDA Support]: ipf-modules-cda/index.html
[CDA processing Camel routes]: ipf-platform-camel-cda/index.html
[Core Features]: ipf-platform-camel-core/index.html
[Code System Mapping]: ipf-commons-map/index.html
[Dynamic Feature Registration]: dynamic.html
[Migration Instructions]: migration.html
[3.1 Update Instructions]: migration-3.1.html
[3.2 Update Instructions]: migration-3.2.html
[3.4 Update Instructions]: migration-3.4.html
[3.5 Update Instructions]: migration-3.5.html
[Recoverability]: recoverability.html
[Performance Monitoring]: performance.html
[HL7 Support tutorial]: ipf-tutorials-hl7/index.html
[XDS tutorial]: ipf-tutorials-xds/index.html
[Dynamic extension tutorial]: ipf-tutorials-config/index.html
[IHE Client Example]: ipf-tutorials-iheclient/index.html