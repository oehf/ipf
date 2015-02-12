
## Adding CXF features to IPF Web Service endpoints

IPF supports [CXF Features](http://cxf.apache.org/docs/features.html) on consumer and producer endpoints of
Web Service-based IPF eHealth components.

The corresponding endpoint URI should be extended with the following parameter:

| Parameter name   | Description
|:-----------------|:---------------------------------------------
| `features`       | List of Spring bean names referencing instances of the type [AbstractFeature](http://cxf.apache.org/javadoc/latest/org/apache/cxf/feature/AbstractFeature.html).

