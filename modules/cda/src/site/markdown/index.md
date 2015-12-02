## Generic CDA support

CDA support is assembled from a variety of sources:

* the [MDHT] project. The MDHT libraries are redistributed as part of IPF's CDA support.
* Tooling to validate CDA documents in their "raw" XML form are provided natively by IPF

[MDHT support] of IPF
There are [CDA-specific] and [MDHT-specific] extensions to Camel provided by the respective IPF modules.


### Parsing of generic CDA documents

As CDA documents are plain XML, a native Groovy XML parser (e.g. XMLSlurper) can be used to parse it into a hierarchy of
`Node` objects.

```groovy
 InputStream is = getClass().getResourceAsStream("/SampleCDADocument.xml")
 def clinicalDocument = new XMLSlurper().parse(is)
```

### Extracting information from parser CDA documents

Here is a code example of how data can be extracted from a parser document:

```groovy
    ...
    def components = clinicalDocument.component.structuredBody.component

    // Simple navigation
    assertEquals('en-US', clinicalDocument.languageCode.@code.text())
    assertEquals('KP00017', clinicalDocument.author[0].assignedAuthor.id[0].@extension.text())

    // Avoid NullPointerException by with safe dereferencing using the ?. operator
    assertEquals('KP00017', clinicalDocument?.author[0].assignedAuthor.id[0].@extension.text())
    def clinicalDocument2 = null
    assertNull(clinicalDocument2?.languageCode?.@code?.text())


    // Use any(Closure) to check if the predicate is true at least once
    assertTrue(components.any { it.section.code.@code == '10164-2' })

    // Use every(Closure) to check if the predicate is always true
    assertTrue(components.every { it.section.code.@codeSystem == '2.16.840.1.113883.6.1' })

    // Use find(Closure) to return the first value matching the closure condition
    assertEquals('History of Present Illness',
            components.find { it.section.code.@code == '10164-2' }.section.title.text())

    // Use findAll to return all values matching the closure condition
    assertEquals(1, components.findAll { it.section.code.@code == '10164-2' }.size())

    // Use findIndexOf to return the index of the first item that matches
    // the condition specified in the closure.
    assertEquals(1, components.findIndexOf { it.section.code.@code == '10153-2' })

    // Use collect to iterate through an object transforming each value into a
    // new value using the closure as a transformer, returning a list of transformed values.
    assertEquals([
            'History of Present Illness',
            'Past Medical History',
            'Medications',
            'Allergies and Adverse Reactions',
            'Family history',
            'Social History',
            'Physical Examination',
            'Labs',
            'In-office Procedures',
            'Assessment',
            'Plan'],
            components.collect { it.section.title.text() })

    // The spread operator parent*.action is equivalent to
    // parent.collect{ child -> child?.action }
    assertEquals([
            'History of Present Illness',
            'Past Medical History',
            'Medications',
            'Allergies and Adverse Reactions',
            'Family history',
            'Social History',
            'Physical Examination',
            'Labs',
            'In-office Procedures',
            'Assessment',
            'Plan'],
            components.section.title*.text())

    // Use depthFirst (or '**') to search for elements anywhere in
    // the structure
    def drugCodes = clinicalDocument.depthFirst().findAll
      { it.name() == "manufacturedLabeledDrug" }.code*.@code

    assertEquals([
            '66493003',
            '91143003',
            '10312003',
            '376209006',
            '10312003',
            '331646005' ],
            drugCodes*.text())

    // Use of helper functions to encapsulate commonly used GPath expressions
    def drugCodes2 = findAllElements(clinicalDocument, "manufacturedLabeledDrug").code*.@code
    assertEquals(drugCodes, drugCodes2)
  }

  private Collection findAllElements(GPathResult result, String name) {
    return result.depthFirst().findAll { it.name() == name }
  }
```

### Validating CDA documents

CDA document instances in their XML representation can be validated using the W3C XML Schema and Schematron validators.
The class `org.openehealth.ipf.modules.cda.CDAR2Constants` provides constants for the location of schema and schematron resources.

Include the required library in the `pom.xml` file:

```xml

    <dependency>
       <groupId>org.openehealth.ipf.modules</groupId>
       <artifactId>ipf-modules-cda-core</artifactId>
       <version>${ipf-version}</version>
    </dependency>

```

Then choose a XML schema to validate against:

```groovy
    import org.openehealth.ipf.modules.cda.CDAR2Constants
    import org.openehealth.ipf.commons.xml.XsdValidator
    ...
    def validator = new XsdValidator()
    validator.validate(xmldoc, CDAR2Constants.HITSP_32_2_5_SCHEMA)
    ...
```

Correspondingly, a more thorough schematron validation looks like this:

```groovy
    import org.openehealth.ipf.modules.cda.CDAR2Constants
    import org.openehealth.ipf.commons.xml.XsdValidator
    ...
    def validator = new SchematronValidator()
    def params = [phase : "errors"];
    validator.validate(xmldoc, new SchematronProfile(CDAR2Constants.HITSP_32_2_5_SCHEMATRON_RULES, params)
```

[MDHT]: https://www.projects.openhealthtools.org/sf/projects/mdht/
[MDHT support]: ../ipf-modules-cda-mdht/index.html
[CDA-specific]: ../ipf-platform-camel-cda/index.html
[MDHT-specific]: ../ipf-platform-camel-mdht/index.html