## HL7v2 Message Validation


### Overview

HL7v2 is a complex flat-file structure that, despite being considered a data standard, is also highly flexible.
It is often expected of an HL7 integration engine that non-standard compliant data be accepted and processed without
notification to the receiving system of non-compliance.

However, to achieve real interoperability, the HL7 standard should be constrained to reduce the degree of freedom,
e.g. how to use certain fields or whether to populate optional fields or not. This happens either based on a written
specification or in addition as machine-readble conformance profile.

In order to check whether HL7 messages actually conform to the defined constraints, message validation is essential.

**Note**: the [HAPI] library already offers support for validating HL7 messages by definition of rules that check against constraints
on type level, message level, and encoded message level. Recent versions of [HAPI] provide much easier ways of
specifying validation rules than before, so IPF 3 has removed or deprecated a lot of its custom validation rule builders.
For details refer e.g. to the [HAPI Examples].


IPF 3 continues to support the definition of custom validation rules that exploiting features of the Kotlin language already
being used in other parts of IPF.


### Closure rules

You can use rules to program you own custom constraints on one or more trigger events.
All there is to do is to write a `checkIf` closure that returns an array of
[`ValidationException`](https://hapifhir.github.io/hapi-hl7v2//base/apidocs/ca/uhn/hl7v2/validation/ValidationException.html) objects.
If the array is empty, validation is considered passed.

#### Example

The following example defines a few closure-based rules for primitive types, pasred and encoded
messages.

```kotlin

    import ca.uhn.hl7v2.validation.builder.support.NoValidationBuilder

    class ClosureRulesBuilder : NoValidationBuilder {

        @Override
        protected void configure() {
            super.configure()

           forVersion().asOf(Version.V23)
              .primitive("ID", "IS").checkIf { String value ->
                  value.size() < 200 ? passed() : failed("too long!")
              }
              .message('ADT', '*').checkIf { Message msg ->
                  // validate the message
              }
        }
    }

```



[HAPI]: https://hapifhir.github.io/hapi-hl7v2/
[HAPI Validation Examples]: https://hapifhir.github.io/hapi-hl7v2/devbyexample.html
[Mapping Service]: ../ipf-commons-map/index.html