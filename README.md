# Open eHealth Integration Platform (IPF)

[![Build](https://github.com/oehf/ipf/actions/workflows/build.yml/badge.svg)](https://github.com/oehf/ipf/actions/workflows/build.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.openehealth.ipf/ipf-dependencies)](https://central.sonatype.com/namespace/org.openehealth.ipf)
[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](LICENSE.txt)

IPF is an extension of the [Apache Camel](https://camel.apache.org/) routing and mediation engine that provides
ready-to-use building blocks for integrating information systems in healthcare. It implements a broad range of
[IHE](https://profiles.ihe.net/) transactions as Camel components, and offers domain-specific languages for creating,
parsing, validating, navigating and transforming HL7v2, HL7v3/CDA and FHIR messages.

IPF is open source under the [Apache License 2.0](LICENSE.txt).

## Features

- **IHE transactions as Camel endpoints** – act as client (producer) or server (consumer) of an IHE actor simply by
  using an endpoint URI such as `xds-iti18://...` or `pdqm-iti78:...`. IPF takes care of the transport (SOAP/MTOM,
  MLLP, REST/FHIR), message model, validation and ATNA auditing.
- **ATNA auditing** – automatic audit record generation for all supported transactions, serialized as DICOM/RFC 3881
  or FHIR `AuditEvent`, sent via UDP, TLS or FHIR REST.
- **HL7v2 DSL** – Groovy and Kotlin DSLs on top of [HAPI HL7v2](https://hapifhir.github.io/hapi-hl7v2/) for
  navigating and modifying messages, plus mapping services for code translations.
- **HL7v3 / CDA support** – parsing, rendering, validation and builders for CDA documents and HL7v3 messages.
- **FHIR** – integration of [HAPI FHIR](https://hapifhir.io/) (R4) for the IHE mobile
  profiles (MHD, PIXm, PDQm, …).
- **Spring Boot starters** – auto-configuration for all major feature areas.
- **Validation** – schema, schematron and profile-based validation of messages at the route level.

### Supported IHE profiles (selection)

| Area                         | Profiles / transactions                                                                                          |
|------------------------------|------------------------------------------------------------------------------------------------------------------|
| Document sharing (SOAP)      | XDS.b (ITI-18, 41, 42, 43, 57, 61, 62 …), XDS-I.b (RAD-69), XCA (ITI-38, 39), XCA-I (RAD-75), XCDR, XCF, RMU, RMD |
| Document sharing (FHIR)      | MHD (ITI-65, 66, 67, 68, 105)                                                                                    |
| Patient identity (HL7v2)     | PIX (ITI-8, 9, 10), PDQ (ITI-21, 22), PAM (ITI-30, 31)                                                           |
| Patient identity (HL7v3)     | PIXv3 (ITI-44, 45, 46), PDQv3 (ITI-47), XCPD (ITI-55, 56)                                                        |
| Patient identity (FHIR)      | PIXm (ITI-83, 104), PDQm (ITI-78, 119)                                                                           |
| Audit                        | ATNA (incl. RESTful ATNA ITI-81), Basic Audit Log Patterns                                                       |
| Other                        | HPD (ITI-58, 59), SVS (ITI-48), XPID (ITI-64), SeR (ITI-79), XACML 2.0 / PPQ, QED/QEDm, PCD-01, CMPD (PHARM-1, 5) |
| Swiss national extensions    | CH:PPQ, CH:ADR, CH:CIQ, CH:PIDD, CH:CIDD, eCH-0213/0214                                                          |

See the [documentation](https://oehf.github.io/ipf-docs) for the complete list and the endpoint URI
parameters of each transaction.

## Getting started

IPF artifacts are published to Maven Central under the group ID `org.openehealth.ipf`. Import the IPF BOM to get
consistent versions of IPF and all its third-party dependencies:

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.openehealth.ipf</groupId>
            <artifactId>ipf-dependencies</artifactId>
            <version>${ipf-version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

Then add the Spring Boot starter for the feature area you need, e.g.:

```xml
<dependency>
    <groupId>org.openehealth.ipf.boot</groupId>
    <artifactId>ipf-xds-spring-boot-starter</artifactId>
</dependency>
```

A minimal route acting as an XDS Document Registry for Registry Stored Query (ITI-18):

```java
from("xds-iti18:xds-iti18")
    .process(iti18RequestValidator())
    .process(exchange -> {
        // query your registry here and set the QueryResponse as body
    })
    .process(iti18ResponseValidator());
```

Runnable examples for HL7v2, XDS, FHIR and IHE clients can be found in the [`tutorials`](tutorials) directory.

### Version compatibility

| IPF   | Java | Apache Camel | Spring Boot | HAPI FHIR |
|-------|------|--------------|-------------|-----------|
| 6.x   | 17+  | 4.x          | 4.x         | 8.x       |
| 5.x   | 17+  | 4.x          | 3.x         | 7.x       |
| 4.x   | 11+  | 3.x          | 2.x         | 5.x / 6.x |

Release notes and migration hints are available in the [changes report](https://oehf.github.io/ipf-docs/docs/changes/)
and the [migration guide](https://oehf.github.io/ipf-docs/docs/migration/).

## Project structure

| Directory        | Contents                                                                                     |
|------------------|----------------------------------------------------------------------------------------------|
| `dependencies`   | Bill of materials (BOM) defining all IPF and third-party dependency versions                 |
| `commons`        | Camel-independent libraries: IHE message models, validation, ATNA auditing, mapping, XML    |
| `modules`        | Standalone DSLs usable without Camel: HL7v2 (Groovy and Kotlin), CDA                         |
| `platform-camel` | Camel components and DSL extensions wrapping the `commons` libraries                         |
| `boot`           | Spring Boot starters and auto-configuration                                                  |
| `tutorials`      | Runnable example applications                                                                |

## Building from source

Requirements: JDK 17 or later and Maven 3.9+. The build is continuously tested on Java 17, 21 and 25.

```shell
git clone https://github.com/oehf/ipf.git
cd ipf
mvn clean install                      # full build including tests
mvn clean install -DskipTests          # quick build
mvn install -pl platform-camel/ihe/xds -am   # build a single module and its dependencies
```

## Documentation

- [IPF documentation](https://oehf.github.io/ipf-docs) – reference guide, IHE transaction pages, tutorials
- [Javadoc / Groovydoc](https://javadoc.io/doc/org.openehealth.ipf) on javadoc.io
- [Legacy documentation](http://oehf.github.io/ipf) for IPF 3.x and earlier

## Contributing and support

- Report bugs and request features via [GitHub Issues](https://github.com/oehf/ipf/issues).
- Contributions are welcome as pull requests against `master`. Please reference the related issue, follow the
  existing code style, add tests, and add an entry to [`src/site/changes.xml`](src/site/changes.xml) for
  user-visible changes.
- Questions can also be asked on the [ipf-user](https://groups.google.com/group/ipf-user) mailing list.

## License

IPF is licensed under the [Apache License, Version 2.0](LICENSE.txt). See [NOTICE.txt](NOTICE.txt) for third-party
attributions.
