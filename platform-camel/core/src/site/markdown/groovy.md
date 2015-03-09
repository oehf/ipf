## Groovy Camel extensions

In previous IPF versions, Camel DSL elements have been extended to accept Groovy closures as arguments.
As of IPF 3, these extensions are provided directly by the [camel-groovy] DSL of Apache Camel.
The corresponding IPF extensions have been deprecated and moved to the `ipf-platform-camel-core-legacy` module.

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


[camel-groovy]: http://camel.apache.org/groovy-dsl.html
