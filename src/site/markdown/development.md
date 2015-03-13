## Development

### Building

IPF currently requires Java SE 7 for both compile time and runtime, but also builds and runs with Java SE 8.

It builds using Maven 3. As of version 3, IPF is available over [Maven Central], so no custom repositories need to
be added to the `settings.xml` configuration file.

Before building, adjust `MAVEN_OPTS` to assign Maven more heap space.
``XX:MaxPermSize`` can be omitted for buildung with Java 8.

```
    set MAVEN_OPTS=-Xmx512m -XX:MaxPermSize=128m
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


[Maven Central]: http://search.maven.org