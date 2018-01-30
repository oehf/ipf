
## `mhd-iti68` component

The mhd-iti68 component provides interfaces for actors of the *Retrieve Document* IHE transaction (ITI-68),
which is described in the [MHD Supplement](https://ihe.net/uploadedFiles/Documents/ITI/IHE_ITI_Suppl_MHD.pdf).

### Actors

The transaction defines the following actors:

![ITI-68 actors](images/iti68.png)

Producer side corresponds to the *Document Consumer* actor.
Consumer side corresponds to the *Document Responder* actor.

### Dependencies

In a Maven-based environment, the following dependency must be registered in `pom.xml`:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.platform-camel</groupId>
        <artifactId>ipf-platform-camel-ihe-fhir-stu3-mhd</artifactId>
        <version>${ipf-version}</version>
    </dependency>
```

### Endpoint URI Format

#### Producer

There is no producer created by this endpoint. The component can only be used on the server-side.

#### Consumer

The endpoint URI format of `mhd-iti68` component consumers is:

```
mhd-iti68:serviceName[?parameters]
```

The resulting URL of the exposed FHIR REST Service endpoint depends on the configuration of the [deployment container].

For example, when a Tomcat container on the host `eHealth.server.org` is configured in the following way:

```
port = 8888
contextPath = /IHE
servletPath = /binary/*
```

then the mhd-iti68 consumer will be available for external clients under the URL
`http://eHealth.server.org:8888/IHE/binary`

Additional URI parameters are optional and control special features as described in the corresponding section below.

SSL support for IPF IHE consumers side must be configured in their [deployment container].
See e.g. SSL How-To for [Tomcat 8](https://tomcat.apache.org/tomcat-8.5-doc/ssl-howto.html).

### Example

This is an example on how to use the component on the consumer side:

```java
    from("mhd-iti68:mhdservice?audit=true")
      .process(myProcessor)
      // process the incoming request and create a response
```

### Basic Common Component Features

* [ATNA auditing]

The ITI-68 endpoint forwards a preliminary [Iti68AuditDataset](../apidocs/org/openehealth/ipf/commons/ihe/fhir/iti68/Iti68AuditDataset.html) 
instance in a Camel message header named `AuditDataset`. The Camel consumer route has the possibility adding 
the unique document ID, and, if applicable in scenarios involving XDS, an optional patient ID, repository ID 
and home community ID in order to populate this AuditDataset with more information. This is because the 
document retrieval URL is completely unspecified with regard to this information.


### Remarks for this component

Although this component is part of a FHIR-specific module, it just responds to a plain servlet
request, which can be about any URL that is mapped on the Camel servlet (see [deployment container]).
The successful response does consist of a binary data stream, representing the document content.


[ATNA auditing]: ../ipf-platform-camel-ihe/atna.html
[Security]: ../ipf-platform-camel-ihe-fhir-core/security.html

[deployment container]: ../ipf-platform-camel-ihe-fhir-core/deployment.html

