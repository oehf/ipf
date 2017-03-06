## IHE Client Example

The `IHEWebServiceClient` is a simple example of how IHE producer endpoints can be hidden behind a service facade.
The service methods are not exposing any part of the Apache Camel machinery that IPF builds up to implement IHE
producers:

```java
    ...

    // ===================
    // XDS-related queries
    // ===================

    public QueryResponse iti18StoredQuery(StoredQuery query, String host, int port, String pathAndParameters) throws Exception {
        QueryRegistry storedQuery = new QueryRegistry(query);
        String endpoint = String.format("xds-iti18://%s:%d/%s", host, port, pathAndParameters);
        return send(endpoint, storedQuery, QueryResponse.class);
    }

    // Use this for RAD-68, too
    public Response iti41ProvideAndRegister(ProvideAndRegisterDocumentSet documentSet, String host, int port, String pathAndParameters) throws Exception {
        String endpoint = String.format("xds-iti41://%s:%d/%s", host, port, pathAndParameters);
        return send(endpoint, documentSet, Response.class);
    }

    public RetrievedDocumentSet iti43RetrieveDocumentSet(RetrieveDocumentSet retrieve, String host, int port, String pathAndParameters) throws Exception {
        String endpoint = String.format("xds-iti43://%s:%d/%s", host, port, pathAndParameters);
        return send(endpoint, retrieve, RetrievedDocumentSet.class);
    }

    ...

```

Internally, a Camel `ProducerTemplate` is used to send the objects to the desired producer endpoints. It is not even necessary
to predefine the endpoints in the Camel context in advance - they will be created on the fly. The only required piece of
infrastructure is a plain `CamelContext`:

```xml
    <camel:camelContext id="clientContext">
        <camel:jmxAgent id="jmxAgent" usePlatformMBeanServer="true" disabled="false"/>
    </camel:camelContext>
```

The other context files of the example deal with setting up the ATNA details, optional interceptors and
[CXF conduit](https://cxf.apache.org/docs/client-http-transport-including-ssl-support.html) information to attach security
aspects to the web service requests.


The source code for this example is located in the [ipf-tutorials-iheclient](https://github.com/oehf/ipf/tree/master/tutorials/ihrclient) module.