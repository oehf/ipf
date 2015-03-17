## Development

### Building

IPF currently requires Java SE 7 for both compile time and runtime, but also builds and runs with Java SE 8.

It builds using Maven 3. As of version 3, IPF is available over [Maven Central], so no custom repositories need to
be added to the `settings.xml` configuration file.

Before building, adjust `MAVEN_OPTS` to assign Maven more heap space.
``XX:MaxPermSize`` can be omitted for buildung with Java 8.

```
    set MAVEN_OPTS=-Xmx1024m -XX:MaxPermSize=128m
    mvn clean install
```

### Sources

IPF uses [git](http://git-scm.com/) for source code management. The IPF git repository is located at
[https://github.com/oehf/ipf](https://github.com/oehf/ipf).

Additionally, there is the `ipf-gazelle` project, which provides conformance profiles for HL7v2 based IHE transactions.
It may be released independently and is located at [https://github.com/oehf/ipf-gazelle](https://github.com/oehf/ipf-gazelle).


### Issue Tracking

Issue tracking is done in github. For current issues check [https://github.com/oehf/ipf/issues](https://github.com/oehf/ipf/issues).

### IDE

IPF depends on Maven, Groovy and Lombok.
Dependening on the choice of your IDE, you may need to install the corresponding plugins.


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

[Maven Central]: http://search.maven.org
[eHealth support pages]: ../platform-camel-ihe/index.html