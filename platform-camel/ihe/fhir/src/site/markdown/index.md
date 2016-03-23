## FHIR

FHIR® – Fast Healthcare Interoperability Resources (hl7.org/fhir) – is a next generation standards framework created by HL7,
combining the best features of HL7v2, HL7v3, and CDA while leveraging the latest web standards and applying a tight focus on implementability.

IHE has had a set of profiles based on FHIR, but they are were on earlier versions of HL7 FHIR. Now that [DSTU2](http://hl7.org/fhir/index.html) is formally published, 
IHE updated their profiles to this version. IPF adds support for a subset of them by providing Camel components (hiding the 
 implementation details on transport level) and translators between the FHIR and HL7 v2 message models.

## Translation between FHIR and HL7 v2 message models

IPF provides utilities for translation between FHIR and HL7v2, thus giving the possibility to implement FHIR-based [IHE] transactions
on top ot their HL7 v2 counterparts and to avoid redundancy in that way.

Currently supported transaction pairs are

* PIX Query ([ITI-9]/[ITI-83])
* PDQ       ([ITI-21]/[ITI-78])

### Dependencies

In a Maven-based environment, the following dependencies should be registered in `pom.xml`:

```xml
<dependency>
    <groupId>org.openehealth.ipf.commons</groupId>
    <artifactId>ipf-commons-spring</artifactId>
    <version>${ipf-version}</version>
</dependency>
<dependency>
    <groupId>org.openehealth.ipf.platform-camel</groupId>
    <artifactId>ipf-platform-camel-ihe-fhir</artifactId>
    <version>${ipf-version}</version>
</dependency>
```

This depends transitively on the required module:

```xml
<dependency>
    <groupId>org.openehealth.ipf.commons</groupId>
    <artifactId>ipf-commons-ihe-fhir</artifactId>
    <version>${ipf-version}</version>
</dependency>
```


### Configuring the URI Mapper

For translation of FHIR messages, an instance of `org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper` is required
in order to map FHIR URIs into OIDs and vice versa. IPF provides an implementation (`org.openehealth.ipf.commons.ihe.fhir.translation.DefaultUriMapper`)
that uses IPF's [Mapping Service] under the hood. The mapping service must be activated and configured with a mapping
file (which can be accessed as a classpath resource). Here is a snippet of Spring-based configuration:

```
    <bean id="mappingService" class="org.openehealth.ipf.commons.map.SpringBidiMappingService">
        <property name="mappingResources">
            <list>
                <value>classpath:mapping.map</value>
            </list>
        </property>
    </bean>

    <bean id="uriMapper" class="org.openehealth.ipf.commons.ihe.fhir.translation.DefaultUriMapper">
        <constructor-arg index="0" ref="mappingService"/>
        <constructor-arg index="1" value="uriToOid"/>
        <constructor-arg index="2" value="uriToNamespace"/>
    </bean>
    
```

The mapping file translates URIs into OIDs and assigning authority namespaces. 

* If a URI of the form `urn:oid:1.2.3.4` is provided, the `DefaultUriMapper` will extract the OID from the URN.
* 

An example for the mapping file is:

```
    mappings = {
    
            uriToOid (
                    'http://org.openehealth/ipf/commons/ihe/fhir/1' : '1.2.3.4',
                    'http://org.openehealth/ipf/commons/ihe/fhir/2' : '1.2.3.4.5.6'
            )

            uriToNamespace (
                    'http://org.openehealth/ipf/commons/ihe/fhir/1' : 'fhir1',
                    'http://org.openehealth/ipf/commons/ihe/fhir/2' : 'fhir2'
            )
		    
    }
```

Note that the mapping key `uriToOid` must correspond to the second parameter instantiating the `DefaultUriMapper`
instance and the mapping key `uriToNamespace` must correspond to the third parameter.

Of course you are free to include your own implementations of `UriMapper` and/or `MappingService`.


### Translators

The package `org.openehealth.ipf.commons.ihe.fhir.translation...` contains the set of translators that is able to
translate between corresponding IHE transactions.

From a *Patient identity Cross Reference Manager* 's perspective, there are **inbound** translators:

| FHIR transaction       | FHIR-to-HL7v2 request                   | HL7v2-Transaction   | HL7v2-to-FHIR response
| -----------------------|-----------------------------------------|---------------------|----------------------------------
| PDQm [ITI-78]          | `iti78.PdqmRequestToPdqQueryTranslator` | PDQ       [ITI-21]  | `iti78.PdqResponseToPdqmResponseTranslator`
| PIXm [ITI-83]          | `iti83.PixmRequestToPixQueryTranslator` | PIX Query [ITI-9]   | `iti83.PixQueryResponseToPixmResponseTranslator`



Each translator has a set of configurable properties. Their descriptions can be taken from javadoc of the
corresponding classes. Below there's an example of a Spring application context defining translator beans: 

```xml

<!-- Example for PIXm Query -->

<bean name="pixmRequestTranslator"
      class="org.openehealth.ipf.commons.ihe.fhir.translation.iti83.PixmRequestToPixQueryTranslator">
    <property name="uriMapper" ref="uriMapper" />
</bean>

<bean name="pixmResposneTranslator"
      class="org.openehealth.ipf.commons.ihe.fhir.translation.iti83.PixQueryResponseToPixmResponseTranslator">
    <property name="uriMapper" ref="uriMapper" />
</bean>

<!-- Example for PDQm -->

<bean name="pdqmRequestTranslator"
      class="org.openehealth.ipf.commons.ihe.fhir.translation.iti78.PdqmRequestToPdqQueryTranslator">
    <property name="uriMapper" ref="uriMapper" />
</bean>

<bean name="pdqmResposneTranslator"
      class="org.openehealth.ipf.commons.ihe.fhir.translation.iti78.PdqResponseToPdqmResponseTranslator">
    <property name="uriMapper" ref="uriMapper" />
</bean>

```

### Using the translators

A translator instance can be used two ways:

* directly from a Java or Groovy application (not discussed here)
* from a Camel route using ´.process()`

The module `ipf-platform-camel-ihe-fhir`, being the basis for the FHIR transactions' implementation,
provides processors that can be used to embed HL7 translation functionality into a Camel route.

There are two processor implementations, each taking a `translator` instance as parameter for the desired translation:

* `FhirCamelTranslators.translatorFhirToHL7v2(translator)`
* `FhirCamelTranslators.translatorHL7v2ToFhir(translator)`


### Example

Here is a sample Camel route that bridges PIXm requests (ITI-83) to an HL7 v2-based Patient Identifier
Cross-Reference Manager (ITI-9), and does the same in reverse direction for responses.


```java

import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.fhir.translation.TranslatorFhirToHL7v2;
import org.openehealth.ipf.commons.ihe.fhir.translation.TranslatorHL7v2ToFhir;

import static org.openehealth.ipf.platform.camel.ihe.fhir.translation.FhirCamelTranslators.translatorFhirToHL7v2;
import static org.openehealth.ipf.platform.camel.ihe.fhir.translation.FhirCamelTranslators.translatorHL7v2ToFhir;

public class Iti83TestRouteBuilder extends RouteBuilder {

    private final TranslatorFhirToHL7v2 requestTranslator;
    private final TranslatorHL7v2ToFhir responseTranslator;

    public Iti83TestRouteBuilder(TranslatorFhirToHL7v2 requestTranslator, 
                                 TranslatorHL7v2ToFhir responseTranslator) {
        super();
        this.requestTranslator = requestTranslator;
        this.responseTranslator = responseTranslator;
    }

    @Override
    public void configure() throws Exception {
        from("pixm-iti83:translation?audit=true")
                // Translate into ITI-9
                .process(translatorFhirToHL7v2(requestTranslator))
                        // Create some static response
                .to("pix-iti9://${pixManagerUri}")
                        // Translate back into FHIR
                .process(translatorHL7v2ToFhir(responseTranslator));
    }
}

```

[ITI-9]: ../ipf-platform-camel-ihe-mllp/iti9.html
[ITI-21]: ../ipf-platform-camel-ihe-mllp/iti21.html
[ITI-78]: iti78.html
[ITI-83]: iti83.html

[Mapping Service]: ../ipf-commons-map/index.html

[IHE]: http://www.ihe.net