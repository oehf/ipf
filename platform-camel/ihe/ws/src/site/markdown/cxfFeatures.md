
## Adding CXF features to IPF Web Service endpoints

IPF supports [CXF Features](http://cxf.apache.org/docs/features.html) on consumer and producer endpoints of
Web Service-based IPF eHealth components.

The corresponding endpoint URI should be extended with the following parameter:

| Parameter name   | Description
|:-----------------|:---------------------------------------------
| `features`       | List of Spring bean names referencing instances of the type [AbstractFeature](http://cxf.apache.org/javadoc/latest/org/apache/cxf/feature/AbstractFeature.html).

### Example

Given the following feature bean:

```xml

    <bean id="gzipFeature" class="org.apache.cxf.transport.common.gzip.GZIPFeature">
        ...
    </bean>
```

The feature can now be referenced in a Web Service-based IHE endpoint:

```java
    ...
    .to("pdqv3-iti47://www.honestpdsupplier.org/pdqv3?features=#gzipFeature");
```