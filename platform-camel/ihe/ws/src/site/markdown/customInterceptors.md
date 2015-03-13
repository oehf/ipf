
## Deploying custom CXF interceptors in Web Service-based IHE components

IPF provides the possibility to deploy user-defined custom [CXF interceptors] when initializing Web Service-based IHE endpoints.

Simply add comma-separated lists of references to Interceptor beans to the following endpoint parameters.

### Parameters

| Parameter name          | Description
|:------------------------|:---------------------------------------------
| `inInterceptors`        | Interceptors for incoming SOAP messages
| `inFaultInterceptors`   | Interceptors for incoming fault SOAP messages
| `outInterceptors`       | Interceptors for outgoing SOAP messages
| `outFaultInterceptors`  | Interceptors for outgoing fault SOAP messages


### Example

Given the following interceptor beans:

```xml

    <bean id="securityInterceptor" class="mypackage.CerberosInterceptor">
        ...
    </bean>

    <bean id="accountingInterceptor" class="theirpackage.ScroogeInterceptor">
        ...
    </bean>

    <bean id="renderingInterceptor" class="ourpackage.PicassoInterceptor">
        ...
    </bean>

```

The interceptors can now be referenced in a Web Service-based IHE endpoint:

```java
    ...
    .to("pdqv3-iti47://www.honestpdsupplier.org/pdqv3" +
        "?inInterceptors=#accountingInterceptor, #securityInterceptor" +
        "&outInterceptors=#renderingInterceptor, #securityInterceptor");
```

[CXF interceptors]: http://cxf.apache.org/docs/interceptors.html