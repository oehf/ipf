## Groovy Camel extensions

In previous IPF versions, Camel DSL elements have been extended to accept Groovy closures as arguments.
As of IPF 3, these extensions are provided directly by the [camel-groovy] DSL of Apache Camel.
The corresponding IPF extensions have been deprecated and moved to the `ipf-platform-camel-core-legacy` module.

To include [camel-groovy], add the following dependency to the `pom.xml` file:

```xml
    <dependency>
        <groupId>org.apache.camel</groupId>
        <artifactId>camel-groovy</artifactId>
        <version>${camel-version}</version>
        <!-- Exclude Groovy bundle in favor of a explicit groovy dependency -->
        <!-- in order to avoid version conflicts -->
        <exclusions>
            <exclusion>
                <groupId>org.codehaus.groovy</groupId>
                <artifactId>groovy-all</artifactId>
            </exclusion>
            <exclusion>
                <groupId>com.sun.xml.bind</groupId>
                <artifactId>jaxb-impl</artifactId>
            </exclusion>
            <exclusion>
                <groupId>com.sun.xml.bind</groupId>
                <artifactId>jaxb-core</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
```

Now it is possible to use [Groovy closures](https://www.groovy-lang.org/closures.html) as argument to Camel processors,
filters, transformers, etc.

Some examples:

```groovy

    // Processor closure
    from('direct:input1')
        .process {exchange ->
            exchange.in.body = exchange.in.body.reverse()
        }
        .to('mock:output')

    // Filter closure
    from('direct:input2')
        .filter {exchange ->
            exchange.in.body == 'blah'
        }
        .to('mock:output')

    // Transform closure
    from('direct:input3')
        .transform {exchange ->
            exchange.in.body.reverse()
        }
```

[camel-groovy]: https://camel.apache.org/groovy-dsl.html
