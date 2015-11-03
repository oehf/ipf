## IPF 3.1 Migration Guide

IPF 3.1 comes with a few backward-incompatible changes, mostly due to reducing mandatory dependencies to
the Spring framework.


### New module

IPF 3.1 encapsulates most Spring dependencies in the new ```ipf-commons-spring``` module.
When you use IPF with Spring, the required Maven dependency is:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.commons</groupId>
        <artifactId>ipf-commons-spring</artifactId>
        <version>${ipf.version}</version>
    </dependency>
```

The only other modules that directly depend on Spring are ```ipf-commons-flow``` and ```ipf-platform-camel-flow```

### Mapping Service

The `org.openehealth.ipf.commons.map.BidiMappingService` cannot be configured with Spring's `Resource`
objects anymore. With Spring, use `org.openehealth.ipf.commons.map.SpringBidiMappingService` instead. the
new class is located in the `ipf-commons-spring` module.
See [Mapping Service] for details.

### MLLP custom interceptors

The MLLP-based IHE endpoints offer the possibility to add custom interceptors. The *interceptors* parameter
has been removed due to its dependency on the Spring framework. as of IPF 3.1, use the *interceptorFactories*
parameter instead. See [Custom Interceptors] for details.

### Logging Interceptors

The interceptors logging the payload of IHE MLLP or Web Service transactions use the Spring Expression language
for determining the target file name they write into. The dependency to this library has been made optional.
See [MLLP Payload Logging] and [WS Payload Logging] for details.

### XDS

`org.openehealth.ipf.commons.ihe.xds.core.metadata.Document` offered type conversion by directly depending on 
Spring's `ConversionService`. 
The new interface `org.openehealth.ipf.commons.core.config.TypeConverter` lets you now choose the
type converter implementation. 
The only implementation provided by IPF is `org.openehealth.ipf.commons.core.config.SpringTypeConverter`, located
in the new `ipf-commons-spring` module, which realizes the previous behavior.


[Mapping Service]: ../ipf-commons-map/index.html
[Custom Interceptors]: ../ipf-platform-camel-ihe-mllp/interceptorChain.html
[MLLP Payload Logging]: ../ipf-platform-camel-ihe-mllp/payloadLogging.html
[WS Payload Logging]: ../ipf-platform-camel-ihe-ws/payloadLogging.html