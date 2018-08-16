## HL7 Messaging

HL7's Version 2.x messaging standard is the workhorse of electronic data exchange in the clinical domain and arguably the most widely i
mplemented standard for healthcare in the world.

The HL7 Standard covers messages that exchange information in the general areas of Patient Demographics, Patient Charges and Accounting, Clinical Observations, Medical Records Document Management, and many more.

HL7 Version 2.8.1 represents HL7's latest development efforts to the line of Version 2 Standards that date back to 1989, and the underlying HAPI HL7 library supports all of them.


### Features

The HL7 v2 support of IPF does not reinvent the wheel. It leverages [HAPI], one of the most proven HL7 v2 Java libraries. It provides, however, features on top of HAPI that adds a lot of convenience compared to the original API, and retrofits some missing items.

| Feature                   | Functionality                           
|:--------------------------|:----------------------------------------
| [HL7v2 DSL]               | A domain specific language based on the Groovy programming language for manipulating HL7 messages. HL7 message processing in IPF applications becomes almost trivial. 
| [HL7v2 API extensions]    | For more flexibility in defining valid sets of HL7 structures, mapping values between code systems, creating messages, etc.
| [HL7v2 Validation extensions] | Additional validation rules for HL7 messages

**Note**:
In the meanwhile, many HL7v2 features of IPF 2.x have been moved to either the [camel-hl7] component or the [HAPI] library itself.
This particularly applies to HL7 validation, acknowledgement generation, and integration into the Camel routing engine.
Please check the respective documentation of these projects for details.


### Dependencies

In a Maven-based environment, the following dependency must be registered in `pom.xml`:

```xml

    <!-- IPF HL7 extensions and DSL -->
    <dependency>
        <groupId>org.openehealth.ipf.modules</groupId>
        <artifactId>ipf-modules-hl7</artifactId>
        <version>${ipf-version}</version>
    </dependency>

    <!-- For each HL7 version being used, add structure library, e.g. v2.5 -->
    <dependency>
        <groupId>ca.uhn.hapi</groupId>
        <artifactId>hapi-structures-v25</artifactId>
        <version>${hapi-version}</version>
    </dependency>

```

In case you use the IHE MLLP components of IPF, the required HL7 libraries are transitively included.


[HAPI]: https://hapifhir.github.io/hapi-hl7v2/
[HL7v2 DSL]: hl7v2dsl.html
[HL7v2 API extensions]: hl7v2extensions.html
[HL7v2 Validation extensions]: hl7v2validation.html
[camel-hl7]: https://camel.apache.org/hl7.html