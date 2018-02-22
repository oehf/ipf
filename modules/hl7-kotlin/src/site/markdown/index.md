## HL7 Messaging

HL7's Version 2.x messaging standard is the workhorse of electronic data exchange in the clinical domain and arguably the most widely i
mplemented standard for healthcare in the world.

The HL7 Standard covers messages that exchange information in the general areas of Patient Demographics, Patient Charges and Accounting, Clinical Observations, Medical Records Document Management, and many more.

HL7 Version 2.8.1 represents HL7's latest development efforts to the line of Version 2 Standards that date back to 1989, and the underlying HAPI HL7 library supports all of them.


### Features

The HL7 v2 support of IPF does not reinvent the wheel. It leverages [HAPI], one of the most proven HL7 v2 Java libraries. It provides, however, features on top of HAPI that adds a lot of convenience compared to the original API, and retrofits some missing items.

| Feature                   | Functionality                           
|:--------------------------|:----------------------------------------
| [HL7v2 DSL]               | A domain specific language based on the Kotlin programming language for manipulating HL7 messages. HL7 message processing in IPF applications becomes almost trivial. 
| [HL7v2 API extensions]    | For more flexibility in defining valid sets of HL7 structures, mapping values between code systems, creating messages, etc.
| [HL7v2 Validation extensions] | Additional validation rules for HL7 messages


### Dependencies

In a Maven-based environment, the following dependency must be registered in `pom.xml`:

```xml

    <!-- IPF HL7 extensions and DSL -->
    <dependency>
        <groupId>org.openehealth.ipf.modules</groupId>
        <artifactId>ipf-modules-hl7-kotlin</artifactId>
        <version>${ipf-version}</version>
    </dependency>

    <!-- For each HL7 version being used, add structure library, e.g. v2.5 -->
    <dependency>
        <groupId>ca.uhn.hapi</groupId>
        <artifactId>hapi-structures-v25</artifactId>
        <version>${hapi-version}</version>
    </dependency>

```

### Differences to the Groovy-based HL7 DSL

The Kotlin-based DSL is very similar as the Groovy-based HL7 DSL as implemented 
in the `ipf-modules-hl7` module. There is, however, one major difference: while Groovy
fundamentally is a dynamically typed language, Kotlin is statically typed. 

This naturally excludes some constructs found in the Groovy DSL implementation (like
implementing custom strategies when missing methods are encountered). On the other hand,
certain programming mistakes are caught already at compile time.

Other features like operator overloading, extension functions and lambdas/closures are present
both in Kotlin and in Groovy, which makes the overall appearance of the DSL suprisingly similar.
The table below lists the most important differences

| Construct             | Groovy HL7 DSL          | Kotlin HL7 DSL             |
|:----------------------|:------------------------|:---------------------------|
| Accessing structures  | `def msh = msg.MSH`     | `val msh = msg["MSH"]`     | 
| Accessing repetitions | `def nk1 = msg.NK1(1)`  | `val nk1 = msg["NK1"](0)` or `val nk1 = msg["NK1", 0]` |

[HAPI]: https://hapifhir.github.io/hapi-hl7v2/
[HL7v2 DSL]: hl7v2dsl.html
[HL7v2 API extensions]: hl7v2extensions.html
[HL7v2 Validation extensions]: hl7v2validation.html
[camel-hl7]: https://camel.apache.org/hl7.html