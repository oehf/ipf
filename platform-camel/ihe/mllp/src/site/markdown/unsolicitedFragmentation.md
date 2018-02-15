
## HL7 v2 unsolicited request message fragmentation in MLLP-based IPF IHE components

This feature corresponds to Section 2.10.2.2 (Segment fragmentation/continuation using the DSC segment) 
of the HL7 v2.5 specification, is supported on both producer and consumer sides, and controlled by the 
following endpoint URI parameters:

### Parameters

| Parameter name                      | Type       | Default value | Short description                                                                    |
|:------------------------------------|:-----------|:--------------|:-------------------------------------------------------------------------------------|
| `supportUnsolicitedFragmentation`   | boolean    | false         | whether unsolicited message fragmentation should be supported by the given endpoint
| `unsolicitedFragmentationThreshold` | int        | -1            | threshold (maximal count of characters  per segment) for segment fragmentation.  Values smaller than 5 lead to no segment fragmentation.
| `unsolicitedFragmentationStorage`   | String     | n/a           | Spring bean name of a storage for fragment accumulators (relevant on consumer side only)  

As stated in the table above, support for unsolicited request message fragmentation can be activated by setting the URL parameter of the corresponding 
endpoint `supportUnsolicitedFragmentation` to `true`.

### Behavior

On *producer* side, this will lead to automatic fragmentation of outgoing request messages based on the value of the URL parameter 
`unsolicitedFragmentationThreshold`, which denotes the maximal count of segments allowed in an outgoing message. 
This value should be greater than 2 in order to include at least one segment in addition to MSH and DSC. 
The producer will perform all necessary actions of the corresponding message exchange pattern to transport these fragments to the receiver, 
as depicted on the following interaction diagram:

![Producer Unsolicited Fragmentation](images/conti-producer-uf.png)

An MLLP-based IPF IHE consumer with enabled segment fragmentation support is able to automatically collect pieces of fragmented requests 
and put them into the route as a single cumulative request message:

![Consumer Unsolicited Fragmentation](images/conti-consumer-uf.png)

The `MSH` segment of the resulting request message is contained in the very first fragment, therefore the corresponding response 
will contain its message control ID from field `MSH-10` in its field `MSA-2`.

Consumers accumulate received fragments in special storages. The user should provide storage beans via consumer endpoint URIs, 
whereby the beans should correspond to the interface
[`org.openehealth.ipf.commons.ihe.hl7v2.storage.UnsolicitedFragmentationStorage`](../apidocs/org/openehealth/ipf/commons/ihe/hl7v2/storage/UnsolicitedFragmentationStorage.html).
An Ehcache-based implementation is provided by IPF. 

Here's an example of how to configure it in the Spring context descriptor, supposed that "unsolicitedFragmentationCache" 
is defined in Ehcache configuration file:

```xml
    <bean id="ehcacheManager" class="net.sf.ehcache.CacheManager" factory-method="create" />

    <bean id="myUFStorage"
          class="org.openehealth.ipf.commons.ihe.hl7v2.storage.EhcacheUnsolicitedFragmentationStorage">
        <constructor-arg index="0">
            <bean factory-bean="ehcacheManager" factory-method="getCache">
                <constructor-arg value="unsolicitedFragmentationCache" />
            </bean>
        </constructor-arg>
    </bean>
```

The consumer endpoint URI can then contain the parameters `"&supportUnsolicitedFragmentation=true&unsolicitedFragmentationStorage=#myUFStorage"`.
On both producer and consumer side, all unsolicited fragments or fragment requests (messages with filled `MSH-14` and `DSC-1`, respectively), 
which cannot be attributed to any active conversation, will be passed through unchanged to the route. Unsolicited fragmentation is not possible 
when the request message to be fragmented does already contain non-empty values in the fields `MSH-14` and/or `DSC-1`.