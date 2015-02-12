
## Message Validation in IPF eHealth Components

Message validation in IPF eHealth components are implemented by means of validating [Camel processors](http://camel.apache.org/processor.html), 
available for incoming and outgoing messages of all implemented transactions. 

Per convention, each of these validating processors throws an `org.openehealth.ipf.commons.core.modules.api.ValidationException` when the validation fails. 
This exception contains detailed description of detected validation problem(s).

Instances of validating processors, which can be directly used in a route by means of the [.process()](http://camel.apache.org/routes.html#Routes-Usingacustomprocessor) 
DSL element, can be obtained from the corresponding factories, depending on the eHealth integration profile:

| Integration profile                 | Transaction numbers/IDs              | Validating processors factory                                              |
|:------------------------------------|:-------------------------------------|:---------------------------------------------------------------------------|
| IHE XDS.a, XDS.b, XCA               | ITI-14, 15, 16, 18, 38, 39, 41, 42, 43 (no validation for ITI-17)     | `org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators`
| IHE PIX / PDQ                       | ITI-8, 9, 10, 21, 22                 | `org.openehealth.ipf.platform.camel.ihe.mllp.PixPdqCamelValidators`
| IHE PIXv3, PDQv3, XCPD, QED         | ITI-44, 45, 46, 47, 55, 56, PCC-1    | `org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators`
| IHE PCD, Continua WAN               | PCD-01, WAN                          | `org.openehealth.ipf.platform.camel.ihe.hl7v2ws.Hl7v2WsCamelValidators`
| Continua HRN                        | HRN                                  | `org.openehealth.ipf.platform.camel.ihe.continua.hrn.ContinuaHrnCamelProcessors`

Factory methods of these classes (excluding Continua HRN) obey the following naming convention: 

```java
public static Processor iti<transaction number><direction>Validator();
public static Processor pcc<transaction number><direction>Validator();   // for QED PCC-1 transaction
public static Processor pcd<transaction number><direction>Validator();   // for PCD-01 transaction
public static Processor continuaWan<direction>Validator();   // for Continua WAN
```

where

* `<transaction>` number is the number/id of IHE transaction from the profile supported by the given factory
* `<direction>` is either "Request" or "Response"

### Example

An example of using validating processors is given below:

```java
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.*;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.apache.camel.spring.SpringRouteBuilder;
 
public class MyRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("pdqv3-iti47:iti47Service")
            .onException(ValidationException.class)
                .maximumRedeliveries(0)
                ...                               // handle validation failure appropriately
                .end()
            .process(iti47RequestValidator())     // validate received PDQ v3 request message
            ...                                   // prepare response
            .process(iti47ResponseValidator());   // validate prepared PDQ v3 response message
    }
}
```


### Self-detecting message validation in MLLP consumer routes

The consumer side of IPF MLLP transaction endpoints receiving HL7v2 messages from clients is aware of the
IHE transaction context. The consumer endpoint can therefore initialize an [HapiContext](http://hl7api.sourceforge.net/base/apidocs/ca/uhn/hl7v2/HapiContext.html) 
that is passed into the route by the [HL7 Codec](codec.html). This context also contains the appropriate 
[ValidationContext](http://hl7api.sourceforge.net/base/apidocs/ca/uhn/hl7v2/validation/ValidationContext.html) that is initialized with the HL7 conformance
profile matching the transaction.
As a result, no explicit validation processor must be provided anymore:

```java
import static org.openehealth.ipf.platform.camel.ihe.mllp.PixPdqCamelValidators;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.apache.camel.spring.SpringRouteBuilder;
 
public class MyRouteBuilder extends SpringRouteBuilder {
    @Override
    public void configure() throws Exception {
        from("pix-iti8:0.0.0.0:3700")
            .onException(ValidationException.class)
                .maximumRedeliveries(0)
                ...                               // handle validation failure appropriately
                .end()
            .process(itiValidator())              // validate received PIX Feed request message
            ...                                   // prepare response
            .process(itiValidator());             // validate prepared PIX Feed acknowledgement
    }
}
```
