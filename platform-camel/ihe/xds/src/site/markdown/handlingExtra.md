## Extra XDS data

### Handling extra metadata elements in Submissions

Section 4.1.14 of the ITI TF 8.0 Vol. 3 stipulates the possibility to transport non-standard metadata along with Document Entries,
Submission Sets, Folders, and Associations. The XDS Registry actor must be able to handle such information — either by storing it
and returning when queried via `ITI-18`, or by ignoring it and including the warning "XdsExtraMetadataNotSaved" into a `ITI-42` response.

If extra metadata is provided in a request, the Camel message header
`org.openehealth.ipf.commons.ihe.xds.core.XdsJaxbDataBinding#SUBMISSION_SET_HAS_EXTRA_METADATA` will be set to `Boolean.TRUE`.

In addition to the Camel message header mentioned above, extra XDS metadata is mapped to the field `extraMetadata`
in the corresponding classes of the simplified data model (`Association`, `DocumentEntry`, `Folder`, `SubmissionSet`).
The field is defined as a map from ebXML slot names (`String`) to lists of ebXML slot values (`String`).

According to the IHE ITI Technical Framework, extra slot names must start with `urn:`, but not with `urn:ihe:`
— this condition is checked by IPF, and all slots with wrong names will be silently ignored.

#### Example

Here is an example of how to set extra metadata using Groovy.

```groovy

    RegisterDocumentSet request = ...

    request.documentEntries[0].extraMetadata = [
       'urn:oehf:name' : ['Open eHealth Foundation'],
       'urn:oehf:web'  : ['www.openehealth.org']]

    request.submissionSet.extraMetadata = [
       'urn:oehf:framework'        : ['IPF'],
       'urn:oehf:frameworkVersion' : ['2.5.2']]

    request.associations[1].extraMetadata = [
       'urn:acme:extraSlot1' : ['value1', 'value2', 'value3'],
       'urn:acme:extraSlot2' : ['123', '456']]

    request.folders[0].extraMetadata = [
       'urn:xyz:values'  : ['i', 'ii', 'iii'],
       'urn:acme:values' : ['vii', 'viii']]

```

### Handling extra query parameters

The simplified XDS data model supports additional (user-defined) parameter slots — they are mapped to the field
`extraParameters` of the `StoredQuery`class. This field is a map from slot names (`String`) to instances of `QueryList`.

In that way, AND/OR semantics of parameter values can be expressed by the means of multiple slots with the same name,
with multiple values in each (see IHE IT Infrastructure Technical Framework, Volume 2a , section 3.18.4.1.2.3.5).

According to the ebXML Specification, parameter names shall start with the dollar character `$`.
Moreover, they shall not collide with names of standard parameters defined by IHE.

#### Example

Here is an example of how to set extra query parameters using Java.

```java

    GetDocumentsQuery query = ...;

    QueryList<String> extraParams1 = new QueryList<String>();
    extraParams1.getOuterList().add(Arrays.asList("para-11", "para-12"));
    extraParams1.getOuterList().add(Arrays.asList("para-21", "para-22", "para-23"));

    QueryList<String> extraParams2 = new QueryList<String>();
    extraParams2.getOuterList().add(Arrays.asList("dia-31", "dia-32", "dia-33"));
    extraParams2.getOuterList().add(Arrays.asList("dia-41"));

    query.getExtraParameters().put("$Perimeter", extraParams1);
    query.getExtraParameters().put("$Diameter", extraParams2);

```