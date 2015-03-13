## MDHT Support

The `ipf-modules-cda-mdht` module wraps the [MDHT] libraries from OpenHealthTools and provides Parser, Renderer,
and Validator implementations. These implementations do not require Groovy to be used.

### Dependencies

Add the following depenedncy to the `pom.xml` file:

```xml
    <dependency>
       <groupId>org.openehealth.ipf.modules</groupId>
       <artifactId>ipf-modules-cda-mdht</artifactId>
       <version>${ipf-version}</version>
    </dependency>

```

### Camel integration

[MDHT-specific] Camel support is available in the [ipf-platform-camel-mdht][MDHT-specific] module.

### Examples

Here is an example how to parse and render a CCD document:

```java
   CDAR2Parser parser = new CDAR2Parser();
   CDAR2Parser renderer = new CDAR2Renderer();

   CDAR2Utils.initCCD();
   InputStream is = getClass().getResourceAsStream("/SampleCCDDocument.xml");
   ClinicalDocument clinicalDocument = parser.parse(is);
   String result = renderer.render(clinicalDocument, (Object[]) null);
```

Here is an example how to validate a CCD document:

```java
   CDAR2Validator validator = new CDAR2Validator();

   CDAR2Utils.initCCD();
   InputStream is = getClass().getResourceAsStream("/SampleCCDDocument.xml");
   ClinicalDocument clinicalDocument = parser.parse(is);
   validator.validate(clinicalDocument, null);
```

[MDHT]: https://www.projects.openhealthtools.org/sf/projects/mdht/
[MDHT-specific]: ../ipf-platform-camel-mdht/index.html