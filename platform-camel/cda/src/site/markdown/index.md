## Camel DSL Extensions for generic CDA

Working with generic CDAs is explained in the [Generic CDA support] section.

However, Groovy's XML `Node` objects are also transferred through Camel routes, and at times it is convenient
to have access to these APIs directly in Camel's routing DSL, e.g. to generate an acknowledge that shall be returned to
the client.


### Dependencies

The following dependency must be registered in `pom.xml`:

```xml
    <!-- IPF CDA extensions and DSL -->

    <dependency>
        <groupId>org.apache-camel</groupId>
        <artifactId>camel-groovy</artifactId>
        <version>${camel-version}</version>
    </dependency>
    <dependency>
        <groupId>org.openehealth.ipf.platform-camel</groupId>
        <artifactId>ipf-platform-camel-cda</artifactId>
        <version>${ipf-version}</version>
    </dependency>
```


### IPF Extensions for Generic CDA support

#### Data Format

CDA documents can be parsed and rendered using the DSL extensions provided by the [camel-groovy] module. See below for
an example.

#### Validation

CDA documents can be validated in routes with the `verify().xsd()...` and `verify().schematron()...` extensions.

| XML schema validation profile | Schematron validation profile | Description
|-------------------------------|-------------------------------|-----------------------
| `cdar2()`                     | n/a                           | plain CDA schema
| `ccda_schema()`               | `ccda()`                      | Consolidated CDA
| `hitspc32_schema()`           | `hitspc32()`                  | HITSP C32
| n/a                           | `ccd()`                       | CCD

The Camel predicate can be used for filters or validators, however, by design it just returns `true` or `false`, and the
resulting `PredicateValidationException` gives no details whatsoever about the details, i.e. *why* the MDHT validation has
failed and the location of the failure in the document.

In contrast, the IPF validator throws a [`ValidationException`](../apidocs/org/openehealth/ipf/commons/core/modules/api/ValidationException.html)
containing all the details about the validation failure that was provided by the [Generic CDA support] validator classes.

```groovy

    from(...)
      .unmarshal().gpath(true)
      // Validate against the schema
      .verify().xsd().ccda_schema()
      // Validate more closely using Schematron rules
      .verify().schematron().ccda()
      ...

```

[Generic CDA support]: ../ipf-modules-cda-core/index.html
[camel-groovy]: https://camel.apache.org/groovy-dsl.html