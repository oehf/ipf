## IPF 6.0 Migration Guide

IPF 6.0 comes with some changes that must be considered when upgrading from IPF 5.3.


### Jakarta EE 11 and Spring Boot 4.1

IPF 6.0 moves to the Jakarta EE 11 stack. Java 17 remains the minimum (the build is also tested
against 21 and 25), but the surrounding libraries move on:

| | IPF 5.3.0 | IPF 6.0.0 |
|---|-----------|-----------|
| Spring Boot | 3.5 | 4.1 |
| Apache Camel | 4.18 | 4.22 |
| Apache CXF | 4.1 | 4.2 |
| Jackson | 2.22 | 3.1 |
| Kotlin | 2.2 | 2.4 |
| HAPI FHIR | 8.10 | 8.12 |

HAPI HL7v2 (2.6) and Groovy (5.0) are unchanged. Consequences for applications:

* **Jackson 3.** The databind API moved from `com.fasterxml.jackson.*` to `tools.jackson.*`.
  Do not raise `jackson-version` past the version Spring Boot manages — Jackson 3.2.x rejects the
  type-id/bean-property name collision that the XDS metadata model relies on, and the build will
  fail. `httpclient5-version` and `tomcat-version` are likewise kept in sync with Spring Boot's
  managed versions, while `kotlin-version` deliberately overrides Spring Boot's so that the Kotlin
  compiler and the stdlib stay on the same release. Each of them carries an explanatory comment in
  the pom.
* **Generic type parameters were removed from a large number of public types**, because they
  carried no information that callers could use. Applications that implement or extend these SPIs
  have to drop the type arguments — a source-only change, no behaviour is affected. Among the
  types concerned are `AuditStrategy`, `AuditStrategySupport`, `AuditInterceptor`,
  `DispatchAuditStrategy`, `NoAuditStrategy`, `NakFactory`, `AsynchronyCorrelator`,
  `ClientRequestFactory`, `FhirAuditStrategy`, `AbstractFhirAuditStrategy`, `FhirEndpoint`,
  `FhirEndpointConfiguration`, `FhirInteractionId`, `FhirTransactionConfiguration`,
  `FhirTransactionOptionsProvider`, `FhirComponentWithOptions`, `HL7v2Endpoint`,
  `Hl7v2InteractionId`, `Hl7v2TransactionConfiguration`, `Hl7v2ConfigurationHolder`,
  `Hl7v3Component`, `Hl7v3Endpoint`, `Hl7v3InteractionId`, `MllpComponent`, `MllpEndpoint`,
  `AbstractWsEndpoint` and `HpdEndpoint`:

        // before
        public class MyRequestFactory
                implements ClientRequestFactory<IClientExecutable<IOperationUntypedWithInput<Bundle>, ?>> {
            public IClientExecutable<IOperationUntypedWithInput<Bundle>, ?> getClientExecutable(...)

        // after
        public class MyRequestFactory implements ClientRequestFactory {
            public IOperationUntypedWithInput<Bundle> getClientExecutable(...)

  `ClientRequestFactory#getClientExecutable` now declares the concrete HAPI client type instead of
  a wildcard `IClientExecutable`, so implementations can return their builder directly.
* **ATNA audit records changed** where the latest DICOM CPs required it. Tests that assert on the
  exact content of generated audit messages need to be re-baselined.


### Removal of FHIR STU3 support

Support for FHIR STU3 (DSTU3) has been removed. The modules `ipf-commons-ihe-fhir-stu3-*`,
`ipf-platform-camel-ihe-fhir-stu3-*` and `ipf-fhir-stu3-spring-boot-starter` no longer exist; use
their FHIR R4 counterparts instead. The default `FhirContext` of the Camel FHIR data formats is
now R4 instead of DSTU3.


### MHD 4.2.4

MHD support was updated to version 4.2.4, remaining backwards compatible with 4.2.3.

* **`MhdVersion` is now an enum.** It used to be a marker interface with a `supportsVersion()`
  method and a nested `enum Version { v320, v423 }`; it is now simply
  `enum MhdVersion { v320, v423, v424 }`. The marker interfaces `Mhd423`, `Pdqm320` and `Pixm310`
  were removed along with it — a model class does not have a single version, since the resources it
  builds conform to more than one MHD version at once.
* **Profile validation against MHD 3.2.0 is no longer possible.** The v320 implementation guide
  resources are no longer shipped, and `MhdValidator.packagePathFor(MhdVersion.v320)` raises an
  `IllegalArgumentException`. The `v320` constant remains for the compatibility resource providers,
  and `Iti66Options.COMPATIBILITY` still serves both `DocumentManifest` and `List` resources.
* **entryUUID and uniqueId are distinguished by `Identifier.type`** rather than `Identifier.use`
  (CP-ITI-1328-01), expressed by the new `MhdIdentifierType` enum. For compatibility with MHD
  4.2.3 the model classes keep setting the old `Identifier.use` — `official` for the entryUUID,
  `usual` for the uniqueId — so code reading the `use` continues to work. New code should match on
  the type instead:

        MhdIdentifierType.ENTRY_UUID.find(documentReference.getIdentifier())
            .map(Identifier::getValue)
            .ifPresent(entryUuid -> ...);

* **The Target Communities Option (CP-ITI-1326-02) is supported.** The `DocumentReference` and
  `List` model classes carry a `homeCommunityId` extension, and [ITI-66] and [ITI-67] accept a
  `targetCommunityIdList` search parameter.
* Canonical references that pin a version, e.g. `.../Encounter|4.0.1`, are resolved again during
  profile validation.


### Hardened XML processing

XML processing was hardened against XXE and SSRF, which restricts what the parsers will fetch.

* The new `org.openehealth.ipf.commons.xml.XmlSecurity` applies the countermeasures to the JAXP and
  Saxon components that see untrusted input: `XsdValidator`, `XsltTransmogrifier`,
  `SchematronTransmogrifier`, `XqjTransmogrifier` and `CDAR2Parser`. External entities and DTDs are
  no longer resolved. Schema *compilation* is restricted to the `file` and `jar` protocols, so an
  `xs:import` or `xs:include` may still reach a sibling file or a resource inside a jar but not the
  network; instance validation and XSLT, Schematron and XQuery processing permit no external access
  at all. Anything that relied on resolving external entities, on a remote `xs:import`, or on
  `document()` over http has to provide those resources locally.
* The deprecated classes `org.openehealth.ipf.commons.core.ContentMap` and
  `org.openehealth.ipf.commons.core.DomBuildersPool` were removed. The pool lives on as
  `org.openehealth.ipf.commons.xml.DomBuildersPool` in `ipf-commons-xml`. `ipf-commons-core` no
  longer depends on `jaxb-runtime`, not even optionally.
* The object pools of `DomBuildersPool` and `JaxWsClientFactory` now evict idle entries, using the
  new `org.openehealth.ipf.commons.core.pool.PoolEvictor`. No configuration is required.
* `XmlYielder` moved from `ipf-commons-xml` to `ipf-commons-ihe-hl7v3`, package
  `org.openehealth.ipf.commons.ihe.hl7v3`, its only user. `ipf-commons-xml` no longer requires
  Groovy at all, so modules that relied on its transitive `groovy-xml` dependency must declare that
  themselves.


### Removal of the Configurer SPI

The `Configurer` SPI only ever expressed "collect all beans of type `T` and push them into some
holder", which core Spring expresses as `beanFactory.getBeanProvider(T.class).orderedStream()`.
The holders and registrars now collect their own contributions, so the SPI and the configurers
built on it are deprecated and will be removed in the next major release:

* `org.openehealth.ipf.commons.core.config.Configurer`
* `org.openehealth.ipf.commons.core.config.OrderedConfigurer`
* `org.openehealth.ipf.commons.spring.core.config.SpringConfigurationPostProcessor`
* `org.openehealth.ipf.commons.spring.map.config.CustomMappingsConfigurer`
* `org.openehealth.ipf.modules.hl7.config.CustomModelClassFactoryConfigurer` (Java and Kotlin)
* `org.openehealth.ipf.commons.core.extend.config.DynamicExtensionConfigurer`

Everything listed above keeps working in IPF 6.0, and the old and the new mechanism may be active
at the same time without any contribution being applied twice. **Spring Boot applications need no
changes at all**, apart from the two exceptions described at the end of this section.

The contribution beans themselves — `CustomMappings`, `CustomModelClasses`, `DynamicExtension`
and `CustomRouteBuilder` — are untouched.


#### Delete the post processor and drop the configurers

    <!-- before -->
    <bean id="mappingService" class="...spring.map.SpringBidiMappingService"/>
    <bean id="c1" class="...spring.map.config.CustomMappingsConfigurer">
        <property name="mappingService" ref="mappingService"/>
    </bean>
    <bean id="c2" class="...modules.hl7.config.CustomModelClassFactoryConfigurer">
        <property name="customModelClassFactory" ref="customModelClassFactory"/>
    </bean>
    <bean id="c3" class="...core.extend.config.DynamicExtensionConfigurer"/>
    <bean id="postProcessor" class="...spring.core.config.SpringConfigurationPostProcessor"/>

    <!-- after -->
    <bean id="mappingService" class="...spring.map.SpringBidiMappingService"/>
    <bean class="...modules.hl7.config.CustomModelClassesRegistrar">
        <property name="customModelClassFactory" ref="customModelClassFactory"/>
    </bean>
    <bean class="...spring.core.extend.SpringDynamicExtensionRegistrar"/>

| Old | New |
|-----|-----|
| `CustomMappingsConfigurer` | nothing — `SpringBidiMappingService` collects the `MappingResourceHolder` beans itself |
| `CustomModelClassFactoryConfigurer` | `org.openehealth.ipf.modules.hl7.config.CustomModelClassesRegistrar` |
| Kotlin `CustomModelClassFactoryConfigurer` | `org.openehealth.ipf.modules.hl7.kotlin.config.CustomModelClassesRegistrar` |
| `DynamicExtensionConfigurer` | `org.openehealth.ipf.commons.spring.core.extend.SpringDynamicExtensionRegistrar` |
| `CustomRouteBuilderConfigurer` | the same class — keep the bean, it collects for itself now |
| `SpringConfigurationPostProcessor` | delete it, but read the next section first |

A `CustomModelClassesRegistrar` without a `customModelClassFactory` property configures *every*
`CustomModelClassFactory` bean of the application context.


#### Declare `<ipf:globalContext/>` if you do not already have it

`SpringConfigurationPostProcessor` created a `SpringRegistry` as a side effect, and thereby
initialized the `ContextFacade` that the stateful Groovy and Kotlin extensions look beans up in.
Dropping the post processor without declaring the registry explicitly results in
`IllegalStateException: Registry instance has not been set` at the first extension call:

    <beans xmlns:ipf="http://openehealth.org/schema/ipf-commons-core"
           xsi:schemaLocation="...
    http://openehealth.org/schema/ipf-commons-core
    http://openehealth.org/schema/ipf-commons-core.xsd">

        <ipf:globalContext/>

This is the change most likely to be overlooked.


#### Ordering moves onto the contribution

`setOrder(...)` on a configurer applied a single order value to everything that configurer
collected. Use Spring's `@Order` / `Ordered` on the contributing bean instead, which is more
fine grained. `@Order` on a `@Bean` factory method is honoured, as is `@Order` on the
contribution class or an `Ordered` implementation:

    @Bean
    @Order(10)
    CustomMappings baseMappings() { ... }

Contributions of ancestor application contexts are collected as well. Without an explicit
`@Order` the contributions of the local context come first; use `@Order` if that sequence
matters.


#### Behavioural changes

* **Contributions are collected earlier.** Collecting now happens while the singletons are being
  initialized (`SmartInitializingSingleton`), and for route builders when Spring starts its
  `Lifecycle` beans — no longer on `ContextRefreshedEvent`. Mapping definitions and custom HL7v2
  model classes are therefore in place before the Camel routes are built and started. Workarounds
  for the previous window, in which a `MappingService` was still empty during bean initialization,
  are no longer needed.
* Groovy DSL extensions are still registered before the route builders build their routes. This
  is now guaranteed by the Spring lifecycle rather than by bean registration order.
* `CustomRouteBuilderConfigurer` looks up the `CamelContext` itself if the application context
  contains exactly one, so its `camelContext` property has become optional.


#### Two exceptions for Spring Boot applications

* If the application declares its own `CustomModelClassFactory` bean, it must be constructed with
  a **mutable** map, e.g. `new HashMap<>(Map.of(...))` rather than `Map.of(...)`. HAPI adds the
  contributed package definitions straight into that map, so an immutable one lets the context
  fail to start with an `UnsupportedOperationException`.
* The `CustomMappingsConfigurer` and `SpringConfigurationPostProcessor` beans of
  `IpfAutoConfiguration` are gone. Inject the `SpringBidiMappingService` instead.

Two related defects were fixed along the way. Spring Boot applications can now contribute custom
HL7v2 model classes at all — the HL7v2 starter registers a `CustomModelClassesRegistrar`, whereas
previously only `CustomMappings` were collected. And the auto-configured PIX/PDQ model class
packages of the HL7v2 starter now resolve, because they were assembled without the separating dot
and the default `HapiContext` silently fell back to the plain HAPI model classes. Note that
`QBP_Q21` exists in both the PIX and the PDQ package and that HAPI keeps the last match rather
than the first, so the PIX variant wins for HL7 v2.5. This concerns only the general purpose
`HapiContext` bean; the IHE transaction endpoints are unaffected, as each of them carries its own
single-profile model class factory.
