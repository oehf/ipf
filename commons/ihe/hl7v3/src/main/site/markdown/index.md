## Translation between HL7 v2 and HL7 v3 message models

IPF provides utilities for translation between HL7v2 and HL7v3, thus giving the possibility to implement HL7v3-based [IHE] transactions
on top ot their HL7 v2 counterparts and to avoid redundancy in that way.

Currently supported transaction pairs are

* PIX Feed ([ITI-8]/[ITI-44])
* PIX Query ([ITI-9]/[ITI-45])
* PIX Update Notification ([ITI-10]/[ITI-46])
* PDQ ([ITI-21]/[ITI-47])


### Dependencies

In a Maven-based environment, the following dependency should be registered in `pom.xml`:

```xml
<dependency>
    <groupId>org.openehealth.ipf.platform-camel</groupId>
    <artifactId>ipf-platform-camel-ihe-hl7v3</artifactId>
    <version>${ipf-version}</version>
</dependency>
```

This depends transitively on the required module:

```xml
<dependency>
    <groupId>org.openehealth.ipf.commons</groupId>
    <artifactId>ipf-commons-ihe-hl7v3</artifactId>
    <version>${ipf-version}</version>
</dependency>
```


### Configuring the Mapping Service

For translation of PIX Feed and PDQ messages, the IPF [Mapping Service] must be activated and configured to use the mapping
provided by the IPF (which can be accessed as a classpath resource). Here is a snippet of Spring-based configuration:

```
<bean id="mappingService" class="org.openehealth.ipf.commons.map.BidiMappingService">
    <property name="mappingScripts">
        <list>
            <value>classpath:META-INF/map/hl7-v2-v3-translation.map</value>
            <!-- other custom mappings -->
            <value>...</value>
        </list>
    </property>
</bean>
```

### Translators

The package `org.openehealth.ipf.commons.ihe.hl7v3.translation` contains the set of translators that is able to
translate between corresponding IHE transactions.

From a *Patient identity Cross Reference Manager* 's perspective, there are **inbound** translators:

| HL7v3-Transaction      | HL7v3-to-HL7v2 request             | HL7v2-Transaction   | HL7v2-to-HL7v3 response
| -----------------------|------------------------------------|---------------------|----------------------------------
| PIX Feed V3 [ITI-44]   | `PixFeedRequest3to2Translator`     | PIX Feed [ITI-8]    | `PixFeedAck2to3Translator`
| PIX Query v3 [ITI-45]  | `PixQueryRequest3to2Translator`    | PIX Query [ITI-9]   | `PixQueryResponse2to3Translator`
| PDQ V3 [ITI-47]        | `PdqRequest3to2Translator`         | PDQ [ITI-21]        | `PdqResponse2to3Translator`

... and **outbound** translators:

| HL7v2-Transaction      | HL7v2-to-HL7v3 request                | HL7v3-Transaction     | HL7v3-to-HL7v2 response
| -----------------------|---------------------------------------|-----------------------|--------------------------
| PIX Feed [ITI-8]       | `PixFeedRequest2to3Translator`        | PIX Feed V3 [ITI-44]  | `PixAck3to2Translator`
| PIX Update [ITI-10]    | `PixUpdateNotification2to3Translator` | PIX Update V3 [ITI-46]| `PixAck3to2Translator`

Note that currently only the [ITI-8]/[ITI-44] pair is provided for both directions.


Each translator has a set of configurable properties. Their descriptions can be currently taken from javadoc of the
corresponding classes. There are reasonable default values, therefore the explicite configuration (see <property> items
in the example below) can be omitted in many cases.

```xml

<!-- Example for PIX Feed -->

<bean name="pixFeedRequestTranslator"
      class="org.openehealth.ipf.commons.ihe.hl7v3.translation.PixFeedRequest3to2Translator">
    <property name="useSenderDeviceName" value="true" />
    <property name="useReceiverDeviceName" value="true" />
    <property name="copyEmailAs" value="PID-13-1" />
    <property name="copyAccountNumberAs" value="PID-18" />
    <property name="accountNumberRoot" value="1.2.3" />
    <property name="copyNationalIdentifierAs" value="PID-19" />
    <property name="nationalIdentifierRoot" value="2.16.840.1.113883.4.1" />
    <property name="birthNameCopyTo" value="PID-5" />
    <property name="useOtherIds" value="true" />
</bean>

<bean name="pixFeedAckTranslator"
      class="org.openehealth.ipf.commons.ihe.hl7v3.translation.PixFeedAck2to3Translator">
    <property name="ackCodeFirstCharacter" value="C" />
</bean>

```

### Using the translators

A translator instance can be used two ways:

* directly from a Java or Groovy application (not discussed here)
* from a Camel route using ´.process()`

The module `ipf-platform-camel-ihe-hl7v3`, being the basis for the PIXv3 and PDQv3 transactions' implementation,
provides processors that can be used to embed HL7 translation functionality into a Camel route.

There are two processor implementations, each taking a `translator` instance as parameter for the desired translation:

* `PixPdqV3CamelTranslators.translatorHL7v3toHL7v2(translator)`
* `PixPdqV3CamelTranslators.translatorHL7v2toHL7v3(translator)`

Before translating from HL7v3 to HL7v2, the original request is saved internally, because some parts of it will have to
be yielded unmodified into the HL7 v3 response message. When the translation of a response message was not preceded by
the translation of the corresponding request message and therefore the request could not been saved automagically by means
of IPF's internal machinery, the user has to provide the request manually (as a String containing XML document or a Message
instance depending on the transaction under consideration) in the property `HL7V3_ORIGINAL_REQUEST_PROPERTY` of the Camel exchange.


### Example

Here is a sample Camel route that impements bridges PIX Feed v3 requests (ITI-44) to an HL7 v2-based Patient Identifier
Cross-Reference Manager (ITI-8), and does the same in reverse direction for acknowledgements.


```groovy

import org.openehealth.ipf.commons.ihe.hl7v3.translation.*;
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelTranslators.*;

public class BridgeRouteBuilder extends SpringRouteBuilder {

    private PixFeedRequest2to3Translator pixFeedRequestTranslator;
    private PixFeedAck2to3Translator pixFeedAckTranslator;
    private String pixManagerUri;

    ...

    // Probably the shortest possible HL7v3 PIX Manager implementation ;-)
    public void configure() throws Exception {
        from("pixv3-iti44:iti44service")
            .onException(Exception.class)
            .maximumRedeliveries(0)
            // some reasonable exception handling
            .end()
        .process(translatorHL7v3toHL7v2(pixFeedRequestTranslator))
        .to("pix-iti8://${pixManagerUri}")
        .process(translatorHL7v2toHL7v3(pixFeedAckTranslator));
    }
}


```
[ITI-8]: ../../../platform-camel/ihe/mllp/iti8.html
[ITI-9]: ../../../platform-camel/ihe/mllp/iti9.html
[ITI-10]: ../../../platform-camel/ihe/mllp/iti10.html
[ITI-14]: ../../../platform-camel/ihe/xds/iti14.html
[ITI-21]: ../../../platform-camel/ihe/mllp/iti21.html

[ITI-44]: ../../../platform-camel/ihe/hl7v3/iti44.html
[ITI-45]: ../../../platform-camel/ihe/hl7v3/iti45.html
[ITI-46]: ../../../platform-camel/ihe/hl7v3/iti46.html
[ITI-47]: ../../../platform-camel/ihe/hl7v3/iti47.html

[Mapping Service]: ../../map/index.html

[IHE]: http://www.ihe.net