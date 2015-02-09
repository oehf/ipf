
## HL7 v2 interactive response continuation in MLLP-based IPF IHE components

This feature corresponds to Section 5.6.3 (Interactive continuation of response messages) of the HL7 v2.5 specification,
is supported on both producer and consumer sides, and controlled by the following endpoint URI parameters:

### Parameters

| Parameter name                      | Type       | Default value | Short description                                                                    |
|:------------------------------------|:-----------|:--------------|:-------------------------------------------------------------------------------------|
| `supportInteractiveContinuation`    | boolean    | false         | whether interactive message continuation should be supported by the given endpoint
| `interactiveContinuationDefaultThreshold` | int  | -1            | default threshold (maximal count of data records per message) for interactive message continuation. Relevant on consumer side only, and only when the field `RCP-2-1` of the request message does not contain a parseable integer value. Values smaller than 1 lead to no continuation
| `interactiveContinuationStorage`    | String     | n/a           | Spring bean name of a storage for interactive continuation chains (relevant on consumer side only)
| `autoCancel`                        | boolean    | false         | whether a "continuation cancel" message should be automatically sent to the server when the producer receives last data fragment. Relevant on producer side only

As stated in the above table, support for interactive continuation can be enabled for an endpoint by setting its URL parameter
`supportInteractiveContinuation` to `true`.

### Behavior

Interactive continuation is like [unsolicited request message fragmentation], but relates to response messages instead of request messages, and
uses data records count instead of segments count as the message splitting criterion (HL7 specification declares some other
counts — e.g. those of lines, characters, or pages — to be usable as splitting criteria as well, but they are not supported by the IPF).

What a "data record" does actually mean thereby, is transaction-dependent — for example, in PDQ (ITI-21), each data record
corresponds to a `QUERY_RESPONSE` group which consists of segments `PID`, `PD1`, `NK1`, and `QRI`
(data record definitions for ITI-21 and ITI-22 are available in the IPF out-of-the-box).


#### Consumer

When interactive continuation is enabled, a consumer will apply transaction-specific rules to split the messages into fragments,
using threshold value from the field `RCP-2-1`, provided that `RCP-2-2` is equal to "RD".
Each fragment can be requested more than once, in arbitrary order.

If the mentioned threshold field is not filled in the expected way, the value of the URL parameter
`interactiveContinuationDefaultThreshold` will be used. When this parameter is not configured as well or its value is
less than 1, no message splitting will be performed.

Interaction steps performed by the consumer are shown on the diagram below:

![Consumer Interactive Continuation](images/conti-consumer-ic.png)

Fragments are stored internally, whereby the user must provide a storage via the `interactiveContinuationStorage`
URI parameter of the consumer endpoint. This bean must implement the interface
`org.openehealth.ipf.platform.camel.ihe.mllp.core.InteractiveContinuationStorage`. An Ehcache-based implementation is provided by the IPF.

Here is an example of how to configure the Spring context descriptor, supposed that "interactiveContinuationCache" is defined in Ehcache configuration file:

```xml
<bean id="ehcacheManager" class="net.sf.ehcache.CacheManager" factory-method="create" />

<bean id="myICStorage"
      class="org.openehealth.ipf.platform.camel.ihe.mllp.core.EhcacheInteractiveConfigurationStorage">
    <constructor-arg>
        <bean factory-bean="ehcacheManager" factory-method="getCache">
            <constructor-arg value="interactiveContinuationCache" />
        </bean>
    </constructor-arg>
</bean>
```

The consumer endpoint URI can then contain parameters `"&supportInteractiveContinuation=true&interactiveContinuationStorage=#myICStorage"`.

Obsolete fragment chains can be removed from the storage either by means of a corresponding cancel message `QCN^J01`
sent by the client (as usual, such messages are automatically served by the IPF) or by proprietary mechanisms of the storage, if available.

#### Producer

When an MLLP-based IPF IHE producer with enabled interactive continuation support recognizes that the response message it just received
is actually the first fragment of an interactive chain, it automatically adds its segment DSC to the initial request message and sends
the latter again, as prescribed by the HL7 specification, requesting the next fragment in that way.

This step will be repeated for all subsequent fragments until the last fragment of the chain has arrived.
After that, the producer joins all collected data records together (using the same transaction-specific rules as the consumer used)
and delivers the cumulative response to the caller.

Optionally — i.e. when the endpoint URI parameter `autoCancel` is set to true — a "continuation cancel" message will be sent
in order to tell the data provider that the it can safely release its resources. The diagram below shows these interaction steps:

![Producer Interactive Continuation](images/conti-producer-ic.png)

All unexpected fragments will be passed through to the route without changes.
This rule applies to cancel messages which relate to non-existent interactive chains (represented by their query tags) as well.


### Combining unsolicited request fragmentation with interactive response continuation

Simultaneous activation of unsolicited message fragmentation and interactive message continuation can be problematic,
because each of them makes use of the same fields in the segment `DSC`. For example, when a request message was sent using
unsolicited fragmentation, and the response represents an interactive continuation fragment, it will be impossible to send
the request for the second response fragment using unsolicited fragmentation, because `DSC-1` will contain a non-empty value.




[unsolicited request message fragmentation]: unsolicitedFragmentation.html