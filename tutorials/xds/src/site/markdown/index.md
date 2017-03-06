## XDS demo repository

This tutorial is a guide to the XDS demo repository, a simplified implementation of an XDS registry and repository
to store documents, folders, submission sets and associations. The demo is useful for everyone who wants to use the IPF XDS components.

It shows how to:

* Use the XDS.b components to offer a registry and a repository service
* Transform and process registration, retrieval and query requests
* Configure and use ATNA logging
* Enable secure transport using HTTPS

The demo repository is non-persistent. A restart will therefore always start out with a blank respository/registry.

### Overview

The XDS demo repository is implemented in Groovy. Most of the code deals with the query functionality.
This is used for the ITI-18 transaction (registry stored query) and for the other transactions to perform checks
of the input data. Within the project you can find the following source files, tests and configuration files:

| Source file              | Responsibility
|--------------------------|----------------------------
| DataStore                | The actual storage of documents, document entries, folders, submission sets and associations. The store is non-persistent at the moment to keep things simple. Allows adding, retrieving and querying
| Comparators              | Basic comparison methods used by the query logic
| ContentUtils             | Helpers to calculate content related data, e.g. hash codes and size
| Iti18RouteBuilder        | Route for the stored query transaction
| Iti4142RouteBuilder      | Routes for the register document set transactions
| Iti43RouteBuilder        | Route for the retrieve document set transaction
| QueryMatcher             | Matching code for various stored query types used by ITI-18
| RegRepModelExtension     | The DSL extension for the routes
| SearchDefinition         | The DSL element for creating a search query in a route
| SearchProcessor          | The processor that performs search queries using the data store
| SearchResult             | An enum that represents the type of results from a search query
| Server                   | The main entry point of the demo repository that starts the server

| Test file                | Responsibility
|--------------------------|----------------------------
| TestRepositoryAndRegistry| Basic tests that send individual requests and check their results
| TestThreading            | A multi-threading test to show the thread-safety of the repository and of the XDS components
| ContentUtils             | Base class for tasks used in the multi-threading test

| Config file                   | Responsibility
|-------------------------------|----------------------------
| context.xml                   | Spring application context containing beans for Camel and IPF configuration as well as the data store
| log4j.xml, logging.properties | logging configuration


The repository can be started within your IDE or from command line using the `startup.bat` after building an assembly.

For this guide it is assumed that you have installed the Groovy Eclipse plugin as described in our development setup.
To start the server within Eclipse, right click on Server.groovy and choose Run as/Groovy.

The repository can be configured to use HTTPS by specifying the command line argument `secure`.


### IPF XDS related code snippets

The main purpose of the demo repository is to demonstrate the features that the IPF XDS Camel components offer.
This section takes a closer look at such code pieces.

#### Basic Configuration

Configuration of an XDS application is pretty similar to the standard configuration of an IPF application.
The main difference is the configuration of CXF. Because the application runs within a Tomcat environment,
CXF must not start its own Jetty instance. Here is the context.xml:

```xml

    <beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns:camel="http://camel.apache.org/schema/spring"
        xmlns:ipf="http://openehealth.org/schema/ipf-commons-core"
        xsi:schemaLocation="
    http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd
    http://camel.apache.org/schema/spring
    http://camel.apache.org/schema/spring/camel-spring.xsd
    http://openehealth.org/schema/ipf-commons-core
    http://openehealth.org/schema/ipf-commons-core.xsd">

        <!-- The following imports are required to configure CXF. cxf-servlet
             is imported to configure CXF to run with servlet support. This
             allows us to use Tomcat with the CXFServlet instead of using CXF
             with a standalone Jetty server. -->
        <import resource="classpath:META-INF/cxf/cxf.xml" />
        <import resource="classpath:META-INF/cxf/cxf-servlet.xml" />

        <!-- Camel context and producer -->
        <camel:camelContext id="camelContext">
            <camel:jmxAgent id="agent" disabled="true" />
            <camel:routeBuilder ref="iti4142RouteBuilder"/>
            <camel:routeBuilder ref="iti43RouteBuilder"/>
            <camel:routeBuilder ref="iti18RouteBuilder"/>
        </camel:camelContext>

        <ipf:globalContext id="globalContext"/>

        <!-- Our route builders for the ITI transactions -->
        <bean id="iti4142RouteBuilder"
            class="org.openehealth.ipf.tutorials.xds.Iti4142RouteBuilder">
        </bean>

        <bean id="iti43RouteBuilder"
            class="org.openehealth.ipf.tutorials.xds.Iti43RouteBuilder">
        </bean>

        <bean id="iti18RouteBuilder"
            class="org.openehealth.ipf.tutorials.xds.Iti18RouteBuilder">
        </bean>

        <!-- The store that contains all the in-memory documents and their meta data -->
        <bean id="dataStore" class="org.openehealth.ipf.tutorials.xds.DataStore" />

    </beans>

```

#### Exposing the XDS endpoints

To allow clients to communicate with the registry/repository, a few routes are defined that automatically expose the
SOAP-based endpoints. The SOAP related details that are required by the IHE profile are completely invisible.
A `from(...)` is all it takes. Take a look at the following snippets:

```groovy

    // Iti18RouteBuilder.groovy

    public void configure() throws Exception {
            ...
            // Entry point for Stored Query
            from('xds-iti18:xds-iti18')
               ...

```

```groovy

    // Iti42RouteBuilder.groovy

    public void configure() throws Exception {
            ...
            // Entry point for Provide and Register Document Set
            from('xds-iti41:xds-iti41')
            ...
            // Entry point for Register Document Set
            from('xds-iti42:xds-iti42')
            ...

```

```groovy

    // Iti43RouteBuilder.groovy

    public void configure() throws Exception {
            ...
            // Entry point for Retrieve Document Set
            from('xds-iti43:xds-iti43')
            ...

```

#### Validating incoming requests

Once the endpoints have been exposed, clients can send in requests. These requests might or might not conform to the IHE
specification. It is usually a good idea to validate incoming requests before processing them.

The XDS components offer a simple validation. This is not meant to be complete, e.g. it cannot validate that a patient ID
is actually known to the registry, but it performs a variety of checks that will simplify our route implementations.
All routes of the demo repository perform this basic validation step right after logging the incoming request, e.g. in ITI-43:

```groovy

import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.*

...

    // Iti43RouteBuilder.groovy
    ...
            // Entry point for Retrieve Document Set
            from('xds-iti43:xds-iti43')
                .log(log) { 'received iti43: ' + it.in.getBody(RetrieveDocumentSet.class) }
                .process(iti43RequestValidator())
```

A validation failure will throw an `XDSMetaDataException`. It is not necessary to put any `onException` handling in the
route for this exception. The XDS components convert validation failures into an equivalent XDS response with the correct error code.
Therefore, this exception can be thrown anywhere else in the routing.

E.g. the demo repository contains code to check for a specific patient ID used by the XDSToolKit. Because the demo does
not track patients yet, it simply throws an exception if this specific patient ID is found in a request:

```groovy

    // Iti4142RouteBuilder.groovy
    ...
        from('direct:checkPatientIds')
                .choice().when { it.in.body.req.submissionSet.patientId.id == '1111111' }
                    .fail(UNKNOWN_PATIENT_ID)
                ...

```

A failure is reported by throwing an `XDSMetaDataException` via the `fail` DSL extension that is implemented in
`RegRepModelExtension.groovy`. The code snippet below shows that this is simply a shortcut to throw the exception:

```groovy

    // RegRepModelExtension.groovy
    ...

        static ProcessorDefinition fail(ProcessorDefinition self, message) {
            self.process { throw new XDSMetaDataException(message) }
        }

```

If any other exception is thrown in the route, the XDS components will report a general error in the failure response
(either `XDSRepositoryError` or `XDSRegistryError`). Of course you can use standard exception handling from Camel to handle such cases.


### Using the meta data classes

Once validated, the route starts processing the incoming message. The format of the data structure that is received in the message body
is very important. By default these are instances of the raw ebXML classes. While these might be interesting for some use cases,
it is often better to use classes that are closer to the XDS meta classes defined by the IHE specification.

These meta classes serve two purposes:

* they ensure conformance with the XDS specification
* they are much easier to use than the more generic ebXML classes

All route builders of the demo repository convert the ebXML bodies to the meta classes after validation. There are different ways to do this.
One way is to simply use `convertBodyTo` which results in the body to be converted from the ebXML class to the meta data class.
This is done in the ITI-43 route:

```groovy

    // Entry point for Retrieve Document Set
            from('xds-iti43:xds-iti43')
                .log(log) { 'received iti43: ' + it.in.getBody(RetrieveDocumentSet.class) }
                // Validate and convert the request
                .validate().iti43Request()
                .convertBodyTo(RetrieveDocumentSet.class)

```

Another way is to retrieve the meta class instance via `getBody`. E.g. in the ITI-41 route builder, the input body is transformed into a map
that contains the actual request object. This allows access to the request at any stage in the routing. To create the map, a transform processing is used:

```groovy

    from('xds-iti41:xds-iti41')
                .log(log) { 'received iti41: ' + it.in.getBody(ProvideAndRegisterDocumentSet.class) }
                // Validate and convert the request
                .validate().iti41Request()
                .transform {
                    [ 'req': it.in.getBody(ProvideAndRegisterDocumentSet.class), 'uuidMap': [:] ]
                }
```

In contrast to `convertBodyTo`, getBody does not replace the body of the message. Check out the log step at the beginning of the route.
It uses `getBody` to retrieve the meta class. The good thing about these classes is that they have meaningful equals, hashCode and toString implementations.

The logging step converts the ebXML class on-the-fly and uses its `toString` method to get a nice textual representation.
If `convertBodyTo` had been used, the validation step would fail, because it expects an ebXML class in the message body.

Lets look at some typical use cases that require access to the meta classes.


#### Evaluating the query type

The next code snippet shows the dispatching of an ITI-18 message based on the stored query type.

This uses [content based routing](https://camel.apache.org/content-based-router.html) via `choice()` to call sub routes
that perform the corresponding query logic. The `queryType` method is a simple shortcut to get the query type property from the request message.

If a non-supported query type is found an exception is thrown using the `fail` processor.
All query types that are defined by the IHE specification are listed in the enum `org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query.QueryType`.

```groovy

    // Iti18RouteBuilder

    public void configure() throws Exception {
            ...

            from('xds-iti18:xds-iti18')
                ...
                // Dispatch to the correct query implementation
                .choice()
                    .when { queryType(it) == FIND_DOCUMENTS }.to('direct:findDocs')
                    .when { queryType(it) == FIND_SUBMISSION_SETS }.to('direct:findSets')
                    .when { queryType(it) == FIND_FOLDERS }.to('direct:findFolders')
                    .when { queryType(it) == GET_SUBMISSION_SET_AND_CONTENTS }.to('direct:getSetAndContents')
                    .when { queryType(it) == GET_DOCUMENTS }.to('direct:getDocs')
                    .when { queryType(it) == GET_FOLDER_AND_CONTENTS }.to('direct:getFolderAndContents')
                    .when { queryType(it) == GET_FOLDERS }.to('direct:getFolders')
                    .when { queryType(it) == GET_SUBMISSION_SETS }.to('direct:getSets')
                    .when { queryType(it) == GET_ASSOCIATIONS }.to('direct:getAssocs')
                    .when { queryType(it) == GET_DOCUMENTS_AND_ASSOCIATIONS }.to('direct:getDocsAndAssocs')
                    .when { queryType(it) == GET_FOLDERS_FOR_DOCUMENT }.to('direct:getFoldersForDoc')
                    .when { queryType(it) == GET_RELATED_DOCUMENTS }.to('direct:getRelatedDocs')
                    .otherwise().fail(ErrorCode.UNKNOWN_STORED_QUERY)
                .end()

             ...
        }

        def queryType(exchange) { exchange.in.body.req.query.type }

```

#### Splitting for individual entry processing

Many XDS transactions work with sets of entries, e.g. upload and download are using multiple documents instead of just one.
Using a splitter you can break down the request message into its individual entries and process them individually.

In the demo repository this is done in many cases. The next snippet of the ITI-43 route shows how to retrieve a document
set by retrieving each document from the store one at a time. Using `split`, the actual splitting of the message is done
by taking the list of documents contained in the meta class. The splitter aggregates a result list using the retrieved documents.
This list is put into the message body after the splitting functionality has finished processing (indicated by end()).

The entries of the list are the result of the processing of retrieve, which is a custom DSL element that calls `DataStore.get()`
to get the contents of the document.

Finally, the message is transformed, putting the aggregated list into the meta class for the response:

```groovy

    // Iti43RouteBuilder.groovy
    ...
            // Entry point for Retrieve Document Set
            from('xds-iti43:xds-iti43')
                .log(log) { 'received iti43: ' + it.in.getBody(RetrieveDocumentSet.class) }
                // Validate and convert the request
                .process(iti43RequestValidator())
                .convertBodyTo(RetrieveDocumentSet.class)

                // Retrieve each requested document and aggregate them in a list
                .split { it.in.body.documents }
                    .aggregationStrategy(new RetrievedDocumentAggregator())
                    .retrieve()
                    .end()
                // Create success response
                .transform {
                    new RetrievedDocumentSet(Status.SUCCESS, it.in.body)
                }
    ...

        private class RetrievedDocumentAggregator extends AbstractListAggregationStrategy<RetrievedDocument> {

            @Override
            RetrievedDocument getValue(Exchange exchange) {
                exchange.in.getBody(RetrievedDocument.class)
            }
        }

```

### Secure Transport

Using HTTPS instead of HTTP requires very little work. In fact, for a registry/repository it does not require anything
related to IPF. Simply configure Tomcat to use secure transport for the web services.

With the embedded Tomcat class of the XDS test package, this is only a few lines of code:

```groovy

    // Server.groovy
    ...
        servletServer.secure = args.length == 1 && args[0].equals('secure')
            servletServer.keystoreFile = 'keystore'
            servletServer.keystorePass = 'changeit'
            servletServer.truststoreFile = 'keystore'
            servletServer.truststorePass = 'changeit'
```

### ATNA Auditing

By default auditing is turned on by all endpoints.
The configuration of the syslog server that receives auditing messages can be found in Server.groovy:

```groovy

    // Server.groovy
    ...
        AuditorModuleContext.context.config.auditRepositoryHost = 'localhost'
        AuditorModuleContext.context.config.auditRepositoryPort = SYSLOG_PORT
    ...
```

Auditing messages will always be send. Because they are send unreliably via the UDP protocol (this is the default),
the XDS components "do not care" if there is actually a syslog server running at the specified host and port.
If you want to see the audit messages that the demo repository logs, you can install a syslog server at localhost using
the standard syslog port 514 or you can change the settings `Server.groovy` to match your setup.

If you want to disable auditing you can do so by changing the endpoint configurations, e.g. for ITI-18:

```groovy

    from('xds-iti18:xds-iti18?audit=false')
    ...
```

### Summary

In this tutorial, we went through some relevant code snippets of the demo XDS registry and repository.

The source code for this tutorial is located in the [ipf-tutorials-xds](https://github.com/oehf/ipf/tree/master/tutorials/xds) module.