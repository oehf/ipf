## Development

### Building

As of version 3.2, IPF requires Java 8 for both compile time and runtime.

IPF builds using Maven 3.3.9. IPF is available over [Maven Central], so no custom repositories need to
be added to the `settings.xml` configuration file.

Before building, adjust `MAVEN_OPTS` to assign Maven more heap space.

```
    set MAVEN_OPTS=-Xmx1024m
    mvn clean install
```

### Sources

IPF uses [git](https://git-scm.com/) for source code management. The IPF git repository is located at
[https://github.com/oehf/ipf](https://github.com/oehf/ipf).

Additionally, there are the following support projects:

* `ipf-gazelle`, which provides conformance profiles for HL7v2 based IHE transactions.
It may be released independently and is located at [https://github.com/oehf/ipf-gazelle](https://github.com/oehf/ipf-gazelle).
* `ipf-oht-atna`, which provides infrastructure for IHE audit trails and node authentication via TLS.
It may be released independently and is located at [https://github.com/oehf/ipf-oht-atna](https://github.com/oehf/ipf-oht-atna).

### Continuous Integration

IPF is built on [Travis](https://travis-ci.org/oehf). Snapshot artifacts are uploaded to the 
[Sonatype](https://oss.sonatype.org/content/repositories/snapshots/org/openehealth/ipf/) snapshot repository.

### Issue Tracking

Issue tracking is done in github. For current issues check [https://github.com/oehf/ipf/issues](https://github.com/oehf/ipf/issues).

### IDE

IPF depends on Maven, [Groovy](https://www.groovy-lang.org/) and [Lombok](https://projectlombok.org/).
Depending on the choice of your IDE, you may need to install corresponding plugins.

### Module Dependencies

The following figure gives an overview of the IPF modules, their dependencies and their names.
The modules of the IHE subpackages are shown in the figure below.

The module names match the jar file names in the Maven repository.

![Dependencies](images/dependencies.png)

The next figure shows the IPF eHealth modules. The Camel-independent modules are contained in the `ipf.commons.ihe` package,
Camel-specific eHealth integration modules are contained in the `ipf.platform-camel.ihe` package.
These modules can be used to implement actor interfaces of standard IHE and Continua transactions, as well for implementation
of project-specific ones. For list of currently supported IHE transactions visit the [eHealth support pages].

![IHE Dependencies](images/dependencies-ihe.png)

[Maven Central]: https://search.maven.org
[eHealth support pages]: ../platform-camel-ihe/index.html