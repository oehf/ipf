
## Interceptors on MLLP endpoints

MLLP components are built on top of [camel-mina2](http://camel.apache.org/mina2.html), and their additional eHealth-specific functionality is implemented 
in form of interceptors. Each MLLP interceptor is a [Camel processor](http://camel.apache.org/processor.html) that wraps another processor. 
The latter can either be one of

* another MLLP interceptor 
* standard camel-mina2 producer 
* consumer instance

Each interceptor is responsible for calling the wrapped processor by itself — there is no chain execution entity. 
Both incoming and outgoing messages can be served by the same interceptor instance by executing some functionality before 
and/or after the execution of the wrapped processor. Each interceptor has an ID used to configure chains; 
IDs of interceptors provided by IPF correspond to their full class names.

Interceptor chains are constructed on the basis of the following sources:

* initial default consumer or producer chain as pre-configured in IPF
* additional interceptor instances provided by MLLP components via `getAdditionalConsumerInterceptors()` and `getAdditionalProducerInterceptors()`
* user-defined interceptor instances referenced in the endpoint URI parameter *interceptors* as a comma-separated list of Spring bean names
* interceptor instances produced by user-defined factories referenced in the endpoint URI parameter *interceptorFactories* as a comma-separated list of Spring bean names

Each interceptor may define IDs of interceptors it should be placed before and after in the chain. 
When no such ID lists are provided, the interceptor instance is placed at the end of the chain. 
When it is not possible to appropriately consider the "before/after" configuration of an interceptor, chain creation fails.

Standard chains and interceptor IDs of producer and consumer sides will be described in the sections below. 
Note that each chain "starts" at the network endpoint and "ends" at the Camel route — this is important to know when configuring 
custom interceptors to be placed "before" or "after" standard ones.

### Standard Interceptor Chains

This section describes standard MLLP interceptors of IPF. 
Their classes belong to the following packages, depending on the served endpoint type (producer/consumer) and usability in non-MLLP context:

* `org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.producer`
* `org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.producer`
* `org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.consumer`
* `org.openehealth.ipf.platform.camel.ihe.mllp.core.intercept.consumer`

All class names obey the pattern Producer`<Function>`Interceptor and Consumer`<Function>`Interceptor`

For the sake of shortness, in the table below, only variable parts of package (`hl7v2/mllp.core`) and class names (placeholder `<Function>`) will be given. 
Interceptor IDs correspond to full class names if not otherwise stated.

#### Standard Producer-side Chain

The standard producer-side chain is shown in the figure below:

![Standard Producer-side Chain](images/mllp-producer-interceptors.png)

| Package name part | `<Function>` (class name part) | Optionality  | Processing outgoing requests | Processing incoming responses
|:----------------- |:-------------------------------|:-------------|:---------------------------- |:-----------------------------
| `mllp.core` | `StringProcessing` | always | performs [segment fragmentation], when necessary  | converts message received from the network to String and performs [segment defragmentation], when necessary
| `mllp.core` | `RequestFragmenter` | only when [unsolicited request fragmentation] is enabled  | when corresponding conditions are met, plays out the scenario described in Section 2.10.2.2 of the HL7 v.2.5 specification  | n/a
| `hl7v2` | `Marshal` | only when [interactive response continuation] is disabled  | converts message to String  | converts message into ca.uhn.hl7v2.model.Message
| `mllp.core`  | `MarshalAndInteractive ResponseReceiver` | only when [interactive response continuation] is enabled  | converts message to String  | when corresponding conditions are met, plays out the scenario described in Section 5.6.3 of the HL7 v.2.5 specification, then converts the aggregated message to ca.uhn.hl7v2.model.Message
| `hl7v2`  | `ResponseAcceptance`  | always  | n/a | checks whether the message is of acceptable type and version
| `mllp.core` | `Audit` | only when [ATNA auditing] is enabled | creates and pre-fills an ATNA audit record  | enriches the ATNA audit record and sends it out
| `hl7v2` | `RequestAcceptance` | always  | checks whether the message is of acceptable type and version  | n/a
| `hl7v2` | `Adapting` | always  | converts message received from the Camel route to a ca.uhn.hl7v2.model.Message | n/a

#### Standard Consumer-side Chain

The standard producer-side chain is shown in the figure below:

![Standard Consumer-side Chain](images/mllp-consumer-interceptors.png)

| Package name part | `<Function>` (class name part) | Optionality | Processing incoming requests | Processing outgoing responses
|:----------------- |:-------------------------------|:------------|:-----------------------------|:-----------------------------
| `mllp.core` | `StringProcessing` | always | converts message received from the network to String and performs [segment defragmentation], when necessary | performs [segment fragmentation], when necessary |
| `mllp.core` | `RequestDefragmenter` | only when [unsolicited request fragmentation] is enabled | when corresponding conditions are met, plays out the scenario described in Section 2.10.2.2 of the HL7 v.2.5 specification | n/a |
| `hl7v2` | `Marshal` | always | converts message to `ca.uhn.hl7v2.model.Message` | converts message to String |
| `hl7v2` | `RequestAcceptance` | always | checks whether the message is of acceptable type and version | n/a |
| `mllp.core` | `InteractiveResponseSender` | only when [interactive response continuation] is enabled | n/a | when corresponding conditions are met, plays out the scenario described in Section 5.6.3 of the HL7 v.2.5 specification |
| `mllp.core` | `Audit` | only when [ATNA auditing] is enabled | creates and pre-fills an ATNA audit record | enriches the ATNA audit record and sends it out |
| `hl7v2` | `ResponseAcceptance` | always | n/a | checks whether the message is of acceptable type and version |
| `hl7v2` | `Adapting` | always | n/a | converts message received from the Camel route to `ca.uhn.hl7v2.model.Message` |
| `mllp.core` | `AuthenticationFailure` | only when [ATNA auditing] is enabled | n/a | checks whether a `MllpAuthenticationFailure` has been thrown in the route; when yes, sends out a corresponding ATNA audit record |


### Configuring Custom Interceptors

There is the possibility to enrich standard chains with custom (user-defined, project-specific) interceptors. 
Such interceptors shall be instantiated as Spring beans with scope="prototype", and their bean IDs should be then 
listed in the endpoint URI parameter *interceptors*, e.g.:

```java
from("pdq-iti21://0.0.0.0:18214?interceptors=#myInterceptor,#authenticationInterceptor")
   ....
```

If using Spring beans with `scope="prototype"` is not possible (e.g. because the beans are wrapped into a Proxy), 
you can also provide a singleton bean instance of `org.openehealth.ipf.platform.camel.ihe.hl7v2.intercept.Hl7v2InterceptorFactory`:

```java
from("pdq-iti21://0.0.0.0:18214?interceptorFactories=#myInterceptorFactory,#authenticationInterceptorFactory")
   ....
```

Each `Hl7v2InterceptorFactory` must implement the `getNewInstance()` method so that for each call a new instance 
of the interceptor is returned. For convenience, there is an abstract class `Hl7v2InterceptorFactorySupport` 
that takes the interceptor class as constructor parameter and instantiates interceptor instances by calling `Class#newInstance()`.

In order to be placed appropriately in the chain, custom interceptors should define lists of IDs of interceptors they should be placed "before" and "after".
Example:

```java
public class MyInterceptor extends AbstractHl7v2Interceptor {
    public MyInterceptor() {
        addAfter(ConsumerStringProcessingInterceptor.class.getName(),
                 ConsumerRequestDefragmenterInterceptor.class.getName());
        addBefore(ConsumerMarshalInterceptor.class.getName());
    }
    .....
}
```

A list may contain multiple IDs when some of corresponding interceptors are optional. In general, the following rules are 
applied when inserting a custom interceptor "X" into the given chain (see class `ChainUtils` for implementation details):

1. If the chain already contains an interceptor with the same ID as "X", "X" will be ignored.
2. All elements of the X's "before" and "after" lists, which reference neither interceptors already present in the chain nor the ones waiting for inserting, are ignored.
3. If the "after" list is not empty, but "before" is empty, "X" will be inserted after the "bottommost" interceptor mentioned there.
4. If the "before" list is not empty, "X" will be inserted before the "uppermost" interceptor mentioned there.
5. If neither "before" nor "after" are defined, "X" will be inserted at the end of the chain (thus providing compatible behavior with older IPF versions).
6. If there are any conflicts, e.g. when the "bottommost" interceptor from the X's "after" list is placed below the "uppermost" one from the "before" list, or when circular dependencies are discovered, the chain construction fails.




[unsolicited request fragmentation]: unsolicitedFragmentation.html
[interactive response continuation]: interactiveContinuation.html
[segment fragmentation]: segmentFragmentation.html
[segment defragmentation]: segmentFragmentation.html
[ATNA auditing]: ../ipf-platform-camel-ihe/atna.html