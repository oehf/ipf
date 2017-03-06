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
    <artifactId>ipf-platform-camel-ihe-fhir-pixpdq</artifactId>
    <version>${ipf-version}</version>
</dependency>
```

This depends transitively on the required module:

```xml
<dependency>
    <groupId>org.openehealth.ipf.commons</groupId>
    <artifactId>ipf-commons-ihe-fhir-pixpdq</artifactId>
    <version>${ipf-version}</version>
</dependency>
```


### Configuring the URI Mapper

For translation of FHIR messages, an instance of `org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper` is required
in order to map FHIR URIs into OIDs and vice versa. IPF provides an implementation (`org.openehealth.ipf.commons.ihe.fhir.translation.NamingSystemUriMapper`)
that uses an instance of `org.openehealth.ipf.commons.ihe.fhir.NamingSystemService` under the hood. 

The default implementation is `org.openehealth.ipf.commons.ihe.fhir.DefaultNamingSystemServiceImpl`, which expects a Bundle of FHIR [NamingSystem resources].
In addition, for code system mapping, a [Mapping Service] bean must be available.
Here is a snippet of the required Spring-based configuration:

```xml

    <bean id="fhirContext" class="ca.uhn.fhir.context.FhirContext" factory-method="forDstu2Hl7Org"/>

    <bean id="mappingService" class="org.openehealth.ipf.commons.map.SpringBidiMappingService">
        <property name="mappingResources">
            <list>
                 <value>classpath:META-INF/map/fhir-hl7v2-translation.map</value>
            </list>
        </property>
    </bean>

    <!-- Use NamingSystemService for the URI Mapper -->

    <bean id="namingSystemService" class="org.openehealth.ipf.commons.ihe.fhir.DefaultNamingSystemServiceImpl">
        <constructor-arg ref="fhirContext"/>
        <property name="namingSystemsFromXml" value="classpath:identifiers.xml"/>
    </bean>

    <bean id="uriMapper" class="org.openehealth.ipf.commons.ihe.fhir.translation.NamingSystemUriMapper">
        <constructor-arg ref="namingSystemService"/>
        <constructor-arg value="identifiers"/>
    </bean>
    
```

Note that Spring Boot applications can depend on `ipf-fhir-spring-boot-starter`, which already auto-configures these beans for you. 

An example for a bundle of [NamingSystem resources] (referenced to be contained in the `identifiers.xml` file in the example above)
looks like this:

```xml

<Bundle xmlns="http://hl7.org/fhir" >
    <id value="identifiers"/>
    <type value="collection"/>

    <entry>
        <resource>
            <NamingSystem>
                <id value="fhir1"/>
                <name value="FHIR1 Patient Identifier Namespace"/>
                <status value="active"/>
                <kind value="identifier"/>
                <date value="2015-07-31"/>
                <type>
                    <coding>
                        <system value="http://hl7.org/fhir/identifier-type"/>
                        <code value="PI"/>
                    </coding>
                </type>
                <uniqueId>
                    <type value="oid"/>
                    <value value="1.2.3.4"/>
                </uniqueId>
                <uniqueId>
                    <type value="other"/>
                    <value value="fhir1"/>
                </uniqueId>
                <uniqueId>
                    <type value="uri"/>
                    <value value="http://org.openehealth/ipf/commons/ihe/fhir/1"/>
                    <preferred value="true"/>
                </uniqueId>
            </NamingSystem>
        </resource>
    </entry>

    <entry>
        <resource>
            <NamingSystem>
                <id value="fhir2"/>
                <name value="FHIR2 Patient Identifier Namespace"/>
                <status value="active"/>
                <kind value="identifier"/>
                <date value="2015-07-31"/>
                <type>
                    <coding>
                        <system value="http://hl7.org/fhir/identifier-type"/>
                        <code value="PI"/>
                    </coding>
                </type>
                <uniqueId>
                    <type value="oid"/>
                    <value value="1.2.3.4.5.6"/>
                </uniqueId>
                <uniqueId>
                    <type value="other"/>
                    <value value="fhir2"/>
                </uniqueId>
                <uniqueId>
                    <type value="uri"/>
                    <value value="http://org.openehealth/ipf/commons/ihe/fhir/2"/>
                    <preferred value="true"/>
                </uniqueId>
            </NamingSystem>
        </resource>
    </entry>

</Bundle>

```

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

<bean name="pdqmResponseTranslator"
      class="org.openehealth.ipf.commons.ihe.fhir.translation.iti78.PdqResponseToPdqmResponseTranslator">
    <property name="uriMapper" ref="uriMapper" />
</bean>

```

### Using the translators

A translator instance can be used two ways:

* directly from a Java or Groovy application (not discussed here)
* from a Camel route using ´.process()`

The module `ipf-platform-camel-ihe-fhir-pixpdq`, being the basis for the PIXm/PDQm FHIR transactions' implementation,
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
[NamingSystem resources]: https://www.hl7.org/fhir/namingsystem.html

[IHE]: https://www.ihe.net