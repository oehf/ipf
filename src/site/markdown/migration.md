## IPF 3 Migration Guide

When updating from IPF 2.x to IPF 3, you will notice quite a few things that work slightly different than before.
Apart from upgrading the Java environment and a number of third-party libraries (including Camel), the most prominent change
is in the Groovy-based HL7v2 Domain specific language.

Instead of being forced to use wrappers like `MessageAdapter`, the DSL now works directly on the core [HAPI] model classes.
Consequently, all HL7v2-based IHE components and the HL7v2/v3 translator classes also produce or consume `Message` objects
instead of `MessageAdapter` objects.

The sections below list all backward-incompatible changes.


### Environment

* IPF 3 requires at least a Java 7 runtime environment and also runs with Java 8


### Removed Modules

The following IPF modules have been removed.

* ipf-modules-cda-oht
* ipf-commons-test
* ipf-platform-camel-test


### Dependencies

Due to moving the release artifacts to Maven Central, Maven group IDs of some third-party libraries
have been changed. As these dependencies are all transitive, there should be no migration required:

* MDHT: org.openhealthtools.mdht -> org.openehealth.ipf.oht.mdht
* Eclipse OCL: org.eclipse -> org.openehealth.ipf.eclipse.ocl
* Eclipse EMF: org.eclipse -> org.eclipse.birt.runtime
* LPG: net.sourceforge.lpg -> lpg.runtime

IPF heavily depends on Apache Camel, but excludes the following transitive dependencies in favor
of explicit dependencies. In case your `pom.xml` depends directly on Camel libraries as well, make
sure to exclude the following dependencies to avoid packaging and version conflicts:

* Exclude `org.codehaus.groovy:groovy-all` from dependencies to `camel-groovy`
* Exclude `com.sun.xml.bind:jaxb-impl` and `com.sun.xml.bind:jaxb-core` in favor of `org.glassfish.jaxb:jaxb-runtime`

The latter comes from a packaging reorganization of the JAXB reference implementation in their 2.2.11 release, which
requires the additional `jaxb-core` dependency. `jaxb-impl` is marked as "old", however, so IPF uses the recommended
`jaxb-runtime` instead.

### HL7v2 DSL API

With IPF 3, the `ipf-modules-hl7dsl` module and all contained classes have been **deprecated**.
Instead of using the adapter classes around the regular [HAPI] model classes
(e.g. `org.openehealth.ipf.modules.hl7dsl.MessageAdapter` around `ca.uhn.hl7v2.model.Message` ), the DSL can now be
applied directly on the HAPI classes. This has been achieved by including the DSL into the Groovy
[extension module](https://www.groovy-lang.org/metaprogramming.html#_extension_modules) `ipf-modules-hl7`.

Due to this change, the HL7v2 DSL has undergone some minor changes:

| Aspect  | Example | IPF 2.x behavior  | IPF 3.x behavior
|---------|---------|-------------------|-----------------
| Illegal DSL usage | | throws `org.openehealth.ipf.modules.hl7dsl.AdapterException` | throws `org.openehealth.ipf.modules.hl7.dsl.HL7DslException`
| Accessing a repeatable structure | `msg.PATIENT_RESULT` | returns a `Closure` object | returns the first repetition of the HL7 structure
| Executing a parameter-less call on a repeatable structure |  `msg.PATIENT_RESULT()` | returns a `List` of the structures | returns an `Closure` that is `Iterable` over the structures. Note that for counting the repetitions, you can use a dedicated HAPI method (e.g. `msg.PATIENT_RESULTReps`), because `size()` is not allowed for Iterables.
| Executing a parameter-less call on the result of an indexed access on a repeatable structure | `msg['PATIENT_RESULT']()` | equivalent with `msg.PATIENT_RESULT()` | throws `org.openehealth.ipf.modules.hl7.dsl.HL7DslException`
| Accessing the first component of a primitive type | `msg.PID[1][1]` | throws `org.openehealth.ipf.modules.hl7dsl.AdapterException` | returns the primitive itself
| Obtaining the 'value' of a segment | `msg.PID.value` | returns the value of the first field of the segment | throws `org.openehealth.ipf.modules.hl7.dsl.HL7DslException`
| Obtaining the value of a field | | msg.PID[1].value | returns the value of the first primitive, treating the literal "" value as a empty string. Use `.originalValue` to return the value literally. Use `.value2` to treat the literal "" value as a empty string

For migration, replace all type references like `MessageAdapter`, `SegmentAdapter` etc. by their wrapped HAPI model classes
`ca.uhn.hl7v2.model.Message`, `ca.uhn.hl7v2.model.Segment` etc. Obviously, all access to the properties `MessageAdapter.target`
and `MessageAdapter.hapiMessage` must be removed as well.


### HL7 Module API

The underlying [HAPI] library has been upgraded to version 2.2.

* `org.openehealth.ipf.modules.hl7.parser.PipeParser` has been removed. Use a standard `ca.uhn.hl7v2.parser.PipeParser` instead. You may need to adapt its Validation Context to relax default validation, e.g. using `ca.uhn.hl7v2.validation.impl.ValidationContextFactory#noValidation()`.
* `org.openehealth.ipf.modules.hl7.HL7v2Exception` is an unchecked runtime exception wrapper for `ca.uhn.hl7v2.HL7Exception`, so its constructor now only accepts an instance of `ca.uhn.hl7v2.HL7Exception` to be wrapped. This nested exception is now also used as cause.
* A large number of classes has been deprecated in favor of functionality that is provided by the current version of the [HAPI] library out of the box. This particularly applies to custom HL7 exceptions and everything in the packages
** `org.openehealth.ipf.modules.hl7.validation`
** `org.openehealth.ipf.modules.hl7.validation.builder`
** `org.openehealth.ipf.modules.hl7.validation.support`.

Check the [HAPI documentation](https://hapifhir.github.io/hapi-hl7v2/devbyexample.html) for examples how message validation has been implemented there.


### HL7 V2/V3 Translator API

The interfaces `Hl7TranslatorV3toV2` and `Hl7TranslatorV2toV3` in the package `org.openehealth.ipf.commons.ihe.hl7v3.translation`
and all of their implementations now translate to or from `ca.uhn.hl7v2.model.Message` instead of the now legacy `MessageAdapter`.


### MLLP Components API

* The `HL7v2TransactionConfiguration` constructor now takes an array of `ca.uhn.hl7v2.Version` as first parameter instead of a single string. This allows to create MLLP components that accept more than one HL7 version using a more typesafe way. Normally, MLLP-based IHE transaction users should not be affected by this internal change.
* There are two new components using the URI schemes 'mllp' and 'mllp-dispatch'. The name of any existing components with the same name must be modified.
* The header with the name `org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpComponent.ACK_TYPE_CODE_HEADER` is now expected to contain a value of type `ca.uhn.hl7v2.AcknowledgementCode` instead of `org.openehealth.ipf.modules.hl7.AckTypeCode`. If you did manually assign values to this header, you need adapt to the new type.

### IHE Components API

* The `allowIncompleteAudit` parameter has been removed. The endpoints now act like it had been set to "true". Please remove the parameter from all endpoint URIs.
* PIX Feed v2 (ITI-8) transaction messages now have IPF-specific message classes to accomodate with the [Gazelle] conformance profiles. In case you have dedicated type casts in your PIX Feed processors to model classes like `ca.uhn.hl7v2.model.v231.message.ADT_A01`, you must change them to `org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v231.message.ADT_A01`.
* In the simplified XDS data model, the type of timestamp fields  was changed from String to `org.joda.time.DateTime`. Setters of those fields still accept String arguments for backward compatibility, but getters return DateTime in UTC.
** `org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry#creationTime`
** `org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry#serviceStartTime`
** `org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry#serviceStopTime`
** `org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder#lastUpdateTime`
** `org.openehealth.ipf.commons.ihe.xds.core.metadata.PatientInfo#dateOfBirth`
** `org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet#submissionTime`
** `org.openehealth.ipf.commons.ihe.xds.core.metadata.TimeRange#from`
** `org.openehealth.ipf.commons.ihe.xds.core.metadata.TimeRange#to`

### XML Processing

* Calling Java methods from XSLT templates is no longer possible. A workaround is to execute these methods from Java/Groovy before the XSLT is called, and propagate their results to XSLT via template parameters.
* The constant `org.openehealth.ipf.commons.xml.ParametersHelper#RESOURCE_LOCATION` was moved to the class `org.openehealth.ipf.commons.xml.AbstractCachingXmlProcessor`.

### Camel DSL Extensions

The module `ipf-platform-camel-core` had some obsolete extensions and also conflicts when used together with a current Camel version.
In order to resolve this issue with a certain degree of backwards compatibility, the module was reorganized:

* All Camel DSL extensions involving Groovy closure support that is available over the `camel-groovy` component have been moved to `ipf-platform-camel-core-legacy`
* The `validate` Camel DSL extension that conflicts with Camel's `validate` EIP method has been renamed to `verify`. The `validate` extension can still be found in `ipf-platform-camel-core-legacy`.
* The `ipf().split` extension has been moved to `ipf-platform-camel-flow`. It's only relevant when used with the flow manager, otherwise the standard Camel splitter works good enough.
* Some already deprecated classes of the legacy Groovy metaclass machinery (like the old `DefaultModelExtender`) have been removed
* No other IPF module depends on `ipf-platform-camel-core-legacy`, so you don't get accidental transitive dependencies

For migration, you have to include the `org.apache.camel:camel-groovy` dependency into your pom:

```xml
    <dependency>
        <groupId>org.apache.camel</groupId>
        <artifactId>camel-groovy</artifactId>
        <version>${camel-version}</version>
        <!-- Exclude Groovy bundle in favor of a explicit groovy dependency -->
        <!-- in order to avoid version conflicts -->
        <exclusions>
            <exclusion>
                <groupId>org.codehaus.groovy</groupId>
                <artifactId>groovy-all</artifactId>
            </exclusion>
            <exclusion>
                <groupId>com.sun.xml.bind</groupId>
                <artifactId>jaxb-impl</artifactId>
            </exclusion>
            <exclusion>
                <groupId>com.sun.xml.bind</groupId>
                <artifactId>jaxb-core</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
```

In case your routes depend on deprecated extensions and/or including `camel-groovy` is not possible, you can also add a
dependency on `ipf-platform-camel-core-legacy`. This module, however, will be removed in one of the next versions,
so we strongly recommend to migrate the respective parts of your routes.


### IHE Runtime

* Validators for HL7v2-based transactions are now based on Conformance Profiles downloaded from [Gazelle]. In general, validation has become much stricter and conforms closely with the IHE specification, so you may expect validation exceptions for test messages.
* All HL7v2-based IHE consumers and producers do not create and accept the deprecated `MessageAdapter` objects but plain [HAPI] `Message` objects.



[HAPI]: https://hapifhir.github.io/hapi-hl7v2/
[Gazelle]: http://gazelle.ihe.net/