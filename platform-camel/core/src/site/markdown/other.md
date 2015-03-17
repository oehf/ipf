## Other Camel DSL extensions

Some other DSL extensions, mostly for convenience.

### Error Handler

A convenience DSL extension provided by IPF is the `unhandled` DSL element. It replaces the verbose
`errorHandler(noErrorHandler())` statement in route definitions i.e. it drops the error handler from a route.

```groovy
    from('direct:input')
        .unhandled()
        // further processing here ...
        .to('mock:output')
```

### Exception message and object

The class [`org.apache.camel.builder.ExpressionClause`](http://camel.apache.org/maven/current/camel-core/apidocs/org/apache/camel/builder/ExpressionClause.html)
has been extended with expressions to access the exception object
or the exception message of an exchange. The corresponding DSL extensions are `exceptionMessage` and `exceptionObject`.

```groovy
    from('direct:input1')
        .onException(ValidationException)
            .transform().exceptionMessage()
            .handled(true)
            .to('mock:error')
            .end()
        // ... ValidationException thrown here ...
        .to('mock:output')

    from('direct:input2')
        .onException(ValidationException)
            .setHeader('foo').exceptionObject()
            .to('mock:error')
            .end()
        // ... ValidationException thrown here ...
        .to('mock:output')

```

### Direct and Mock

These extensions are just to minimize code for commonly used Camel endpoints. The routes in the example above can also
 be written like this:

```groovy
    direct('input1')
        .onException(ValidationException)
            .transform().exceptionMessage()
            .handled(true)
            .mock('error')
            .end()
     // ... ValidationException thrown here ...
     .to('mock:output')
``
