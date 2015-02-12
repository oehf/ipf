
## About

The Open eHealth Integration Platform (IPF) provides interfaces for health-care related integration solutions.
An prominent example of an healthcare-related use case of IPF is the implementation of interfaces for transactions specified
in Integrating the Healthcare Enterprise ([IHE][ihe]) profiles.

IPF can be easily embedded into any Java application and additionally supports deployments inside OSGi environments.

IPF is built upon and extends the [Apache Camel](http://camel.apache.org) routing and mediation engine. It has an application programming layer
based on the [Groovy](http://groovy.codehaus.org) programming language and comes with comprehensive support for message processing and connecting
systems in the eHealth domain. IPF provides domain-specific languages (DSLs) for implementing
[Enterprise Integration Patterns](http://www.enterpriseintegrationpatterns.com/)
in general-purpose as well as healthcare-specific integration solutions.



## Use it

IPF uses [Maven](http://maven.apache.org) as build tool.
Depending on your project needs you might want to define dependencies to various IPF artifacts. For example,
the following statements will include all dependencies needed to work with IPF interfaces for MLLP-based
HL7v2 transactions:


```xml
<dependency>
  <groupId>org.openehealth.ipf.platform-camel</groupId>
  <artifactId>ipf-platform-camel-ihe-mllp</artifactId>
  <version>3.0</version>
</dependency>
```

Now you can expose or consume IHE-compliant transaction endpoints.


## Contribute

[Contribute][contribute] by reporting issues, suggesting new features, or forking the
Git repository on [GitHub][ipf-github] and provide some good pull requests!


### License

IPF code is Open Source and licensed under [Apache license][apache-license].


[apache-license]: http://www.apache.org/licenses/LICENSE-2.0
[contribute]: contribute.html
[ipf-github]: http://github.com/oehf/ipf
[ihe]: http://www.ihe.net