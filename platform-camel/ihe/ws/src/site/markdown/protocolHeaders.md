
## Working with HTTP and SOAP headers in Web Service-based IPF eHealth components

IPF provides the possibility to access protocol (HTTP and SOAP) headers of incoming Web Service messages and
to insert additional protocol headers into outgoing messages.

### Accessing protocol headers of incoming messages

From within the route, headers of the current incoming Web Service message can be accessed as Camel message headers:

| Protocol  | Camel header name                           | Type
|:----------|:--------------------------------------------|:---------------------------------------------
| HTTP      | `AbstractWsEndpoint#INCOMING_HTTP_HEADERS`  | `Map<String, String>` with keys corresponding to header names
| SOAP      | `AbstractWsEndpoint#INCOMING_SOAP_HEADERS`  | `Map<QName, org.apache.cxf.headers.Header>` with keys corresponding to header names


### Setting additional protocol headers of outgoing messages

To add custom HTTP and/or SOAP headers to an outgoing message, a corresponding data structure
must be made available through corresponding Camel message header:

| Protocol  | Camel header name                           | Type
|:----------|:--------------------------------------------|:---------------------------------------------
| HTTP      | `AbstractWsEndpoint#OUTGOING_HTTP_HEADERS`  | `Map<String, String>` with keys corresponding to header names
| SOAP      | `AbstractWsEndpoint#OUTGOING_SOAP_HEADERS`  | `Collection<org.apache.cxf.headers.Header>` or `Map<QName, org.apache.cxf.headers.Header>` (keys will be ignored)


### Example of configuring a simple custom SOAP header with plain text content

```groovy

import static AbstractWsEndpoint.*

.process {
    def headerName = new QName('urn:ihe:iti:ihe:2007', 'CustomXdsHeader')
    def header = new Header(headerName, "simple contents", new JAXBDataBinding(String.class))
    if (! it.in.headers.containsKey(OUTGOING_SOAP_HEADERS)) {
        it.in.headers[OUTGOING_SOAP_HEADERS] = []
    }
    it.in.headers[OUTGOING_SOAP_HEADERS] << header
}

```

### Example of configuring custom HTTP headers

```groovy

import static AbstractWsEndpoint.*

.process {
   def myRequestHeader = it.in.headers[INCOMING_HTTP_HEADERS]['MyRequestHeader']
   if (! it.in.headers.containsKey(OUTGOING_HTTP_HEADERS)) {
       it.in.headers[OUTGOING_HTTP_HEADERS] = [:]
   }
   it.in.headers[OUTGOING_HTTP_HEADERS]['SAMLToken'] = '...'
   it.in.headers[OUTGOING_HTTP_HEADERS]['MyResponseHeader'] = 'Re: ' + myRequestHeader
}

```
