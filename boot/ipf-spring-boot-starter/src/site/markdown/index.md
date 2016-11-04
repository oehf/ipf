## Spring Boot support

[Spring Boot] is a framework to create stand-alone, production-grade Spring based applications
very easily thanks to its convention over configuration mechanism that allows Spring Boot to pre-configure 
applications in an opinionated way whenever possible.
All of this auto-configuration is driven by the concept of _starters_ and _conditional annotations_ that Spring Boot provides. 

A starter is basically a dependency descriptor that you can use in your application in order add all the related dependencies 
involved in a technology with a minimal specification. Additionally a starter typically contain auto-configuration classes that 
instantiate a default set of Spring beans that implement the functionality of the starter module depending on corresponding configuration properties
found e.g. on the classpath. All beans can be overridden in case the default is not sufficient.
 
IPF provide Spring Boot starter modules for bootstrapping basic IPF infrastructure such as mapping as well as for
HL7- or IHE-related components. The IPF starter modules also depend on [Camel Spring Boot], which sets up a configurable Camel infrastructure for your project.
Dependencies on the IPF starter modules are established through regular Maven dependencies, e.g. for the IHE XDS starter:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.boot</groupId>
        <artifactId>ipf-xds-spring-boot-starter</artifactId>
    </dependency>
```

Next to `ipf-spring-boot-starter`, there available starter modules are:

| Starter module                                                               | Purpose |
|------------------------------------------------------------------------------|------------------------------
| [ipf-atna-spring-boot-starter](../ipf-atna-spring-boot-starter/index.html)   | set up ATNA infrastructure
| [ipf-hl7-spring-boot-starter](../ipf-hl7-spring-boot-starter/index.html)     | for HL7v2/MLLP-based IHE transactions
| [ipf-hl7v3-spring-boot-starter](../ipf-hl7v3-spring-boot-starter/index.html) | for HL7v3/SOAP-based IHE transactions
| [ipf-xds-spring-boot-starter](../ipf-xds-spring-boot-starter/index.html)     | for XDS/SOAP-based IHE transactions
| [ipf-fhir-spring-boot-starter](../ipf-fhir-spring-boot-starter/index.html)   | for FHIR/REST-based IHE transactions

All other IPF starter modules transitively depend on `ipf-spring-boot-starter`, so there is no need to explicitly
depend on this module.

`ipf-spring-boot-starter` auto-configures `org.openehealth.ipf.commons.map.SpringBidiMappingService` and provides
Spring beans for picking up any `org.openehealth.ipf.commons.map.config.CustomMappings`. See [here](../dynamic.html) for details.
If further sets up a `org.openehealth.ipf.commons.core.config.SpringRegistry`.

In addition, if the properties `server.ssl.enabled` and `ipf.commons.reuse-ssl-config` are set to  `true`, a bean
of type `org.apache.camel.util.jsse.SslContextParameters` named `bootSslContextParameters` is provided so you can
reuse the Spring Boot security configuration for [FHIR](../ipf-platform-camel-ihe-fhir-core/security.html), 
[MLLP](../ipf-platform-camel-ihe-mllp/secureTransport.html) and [Web Service](../ipf-platform-camel-ihe-ws/secureTransport.html) IHE transaction endpoints supported by IPF.

`ipf-spring-boot-starter` provides the following application properties:

| Property (`ipf.commons.`)  | Default        | Description                                         |
|----------------------------|----------------|-----------------------------------------------------|
| `reuse-ssl-config`         | false          | Whether to set up a `bootSslContextParameters` bean derived from Spring Boot SSL settings

[Spring Boot]: http://projects.spring.io/spring-boot/
[Camel Spring Boot]: http://camel.apache.org/spring-boot.html