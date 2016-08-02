## IPF 3.2 Migration Guide

IPF 3.2 comes with some changes that must be considered when upgrading from IPF 3.1 to IPF 3.2.

### Dependency POM

IPF declares a dependency POM that manages the versions of the major required 3rd party libraries as well as of all IPF modules.
In order to align with these versions and minimize conflicts, import the following dependency: 

  
```xml
    <dependencyManagement>
        <dependencies>
        ...
            <dependency>
                <groupId>org.openehealth.ipf</groupId>
                <artifactId>ipf-dependencies</artifactId>
                <version>${ipf.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        ...
        </dependencies>
    </dependencyManagement>
```    


### EhCache
 
EhCache is now an optional dependency. The EhCache implementations `org.openehealth.ipf.platform.camel.ihe.hl7v3.EhcacheHl7v3ContinuationStorage`,
`org.openehealth.ipf.platform.camel.ihe.mllp.core.EhcacheInteractiveContinuationStorage`,
`org.openehealth.ipf.platform.camel.ihe.mllp.core.EhcacheUnsolicitedFragmentationStorage` and 
`org.openehealth.ipf.commons.ihe.ws.correlation.EhcacheAsynchonyCorrelator` can only be used if you add a Maven dependency to EhCache to your project.


### XUA Token Parsing

The dependencies for parsing the IHE XUA SAML Token from web service requests (for the sake of ATNA auditing) have been isolated in the new
new ```ipf-commons-ihe-xua``` module. When you expect XUA tokens, you need to add the following Maven dependency:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.commons</groupId>
        <artifactId>ipf-commons-ihe-xua</artifactId>
    </dependency>
```

