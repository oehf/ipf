## Support for the "Asynchronous Web Service Exchange" option in IPF eHealth Components

The *Asynchronous Web Service Exchange* option is provided for cross-community IHE integration profiles.
It consists in the usage of the `ReplyTo` SOAP header defined in the [WS-Addressing](https://www.w3.org/Submission/ws-addressing/) specification.

When a request message specifies this header and it contains an URI not equal to the predefined "default" and "none" values,
the service will send the response to this URL instead of returning the response to the original requestor.

The IPF provides support for this option. User intervention is necessary on the client side only
(i.e. for sending requests and receiving asynchronous responses), because the whole server-side magic is provided out-of-box
by the [Apache CXF Framework](https://cxf.apache.org/).

To make an asynchronous call, three additional steps must be performed:

* arrange a message correlator;
* arrange a special receiver endpoint for asynchronous responses;
* store URI of this endpoint in the corresponding header of outgoing request messages.

These steps are described in corresponding sections below.


### Message correlator

The purpose of the message correlator is to provide a mechanism for the attribution of asynchronous responses to the
corresponding requests (which is necessary in particular for ATNA auditing). Moreover, it provides the possibility
to associate a user-defined key to a group of asynchronous requests in order to efficiently aggregate responses.

The user has to define an instance of a class which implements the
[`org.openehealth.ipf.commons.ihe.ws.correlation.AsynchronyCorrelator`](../apidocs/org/openehealth/ipf/commons/ihe/ws/correlation/AsynchronyCorrelator.html) interface.
An [Ehcache](http://ehcache.org/)-based correlator implementation is provided by the IPF out-of-box, so in the simplest
case the instantiation will look like (supposed that `wsCorrelationCache` is defined in Ehcache descriptor):

```xml

    <bean id="ehcacheManager" class="net.sf.ehcache.CacheManager" factory-method="create" />

    <bean id="correlator"
          class="org.openehealth.ipf.commons.ihe.ws.correlation.EhcacheAsynchronyCorrelator">
        <constructor-arg>
            <bean factory-bean="ehcacheManager" factory-method="getCache">
                <constructor-arg value="wsCorrelationCache" />
            </bean>
        </constructor-arg>
    </bean>


```

When a remote service fails to send a response, orphan data items remain in the correlator.
The Ehcache-based implementation is able to handle this issue, user-defined impementations should take care of it by theirselves.


### Receiver Endpoint

A special endpoint should be arranged **on the client side** for serving asynchronous response messages.
Component part of the endpoint URI corresponds to the transaction, with the suffix "-async-response", e.g.
for an XCPD Initiating Gateway (ITI-55 client):

```java

    from("xcpd-iti55-async-response:iti55service-response?correlator=#correlator")
        .process(iti55ResponseValidator())
        ...

```

The mandatory paramater `correlator` must contain a reference to the correlator bean as described above.

Per default, all messages arrived on this endpoint will be processed using `InOnly` exchange pattern.


### Sending asynchronous requests

To send a request in asynchronous mode, the client has to put the URL of the response receiver endpoint into the Camel message header
`AbstractWsEndpoint#WSA_REPLYTO_HEADER_NAME`. Note that this URL must be a plain HTTP(s) one, not a IPF endpoint URI.
Moreover, the correlator bean should be referenced in the producer endpoint URI.:

```java

    from("direct:foobar")
        .setHeader(AbstractWsEndpoint.WSA_REPLYTO_HEADER_NAME,
                   constant("http://localhost:8889/iti55service-response"))
        .to("xcpd-iti55://somehost.uri/XCPDRespondingGateway?correlator=#correlator")

```

In this case, the `.to("xcpd...")` statement will return immediately (using the `InOnly` exchange pattern), and the response
will be retrieved asynchronously over the separate receiver endpoint.

In the same way, a user-defined correlation key can be set via the request message header
`AbstractWsEndpoint#CORRELATION_KEY_HEADER_NAME`. This header will be automatically restored in the asynchronous response
message, when the latter arrives.

