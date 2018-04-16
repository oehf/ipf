
package org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.EncryptedElementType;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Extensions_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "Extensions");
    private final static QName _Status_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "Status");
    private final static QName _StatusCode_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "StatusCode");
    private final static QName _StatusMessage_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "StatusMessage");
    private final static QName _StatusDetail_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "StatusDetail");
    private final static QName _AssertionIDRequest_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "AssertionIDRequest");
    private final static QName _SubjectQuery_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "SubjectQuery");
    private final static QName _AuthnQuery_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "AuthnQuery");
    private final static QName _RequestedAuthnContext_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "RequestedAuthnContext");
    private final static QName _AttributeQuery_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "AttributeQuery");
    private final static QName _AuthzDecisionQuery_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "AuthzDecisionQuery");
    private final static QName _AuthnRequest_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "AuthnRequest");
    private final static QName _NameIDPolicy_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "NameIDPolicy");
    private final static QName _Scoping_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "Scoping");
    private final static QName _RequesterID_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "RequesterID");
    private final static QName _IDPList_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "IDPList");
    private final static QName _IDPEntry_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "IDPEntry");
    private final static QName _GetComplete_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "GetComplete");
    private final static QName _Response_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "Response");
    private final static QName _ArtifactResolve_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "ArtifactResolve");
    private final static QName _Artifact_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "Artifact");
    private final static QName _ArtifactResponse_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "ArtifactResponse");
    private final static QName _ManageNameIDRequest_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "ManageNameIDRequest");
    private final static QName _NewID_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "NewID");
    private final static QName _NewEncryptedID_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "NewEncryptedID");
    private final static QName _Terminate_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "Terminate");
    private final static QName _ManageNameIDResponse_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "ManageNameIDResponse");
    private final static QName _LogoutRequest_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "LogoutRequest");
    private final static QName _SessionIndex_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "SessionIndex");
    private final static QName _LogoutResponse_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "LogoutResponse");
    private final static QName _NameIDMappingRequest_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "NameIDMappingRequest");
    private final static QName _NameIDMappingResponse_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:protocol", "NameIDMappingResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ExtensionsType }
     * 
     */
    public ExtensionsType createExtensionsType() {
        return new ExtensionsType();
    }

    /**
     * Create an instance of {@link StatusType }
     * 
     */
    public StatusType createStatusType() {
        return new StatusType();
    }

    /**
     * Create an instance of {@link StatusCodeType }
     * 
     */
    public StatusCodeType createStatusCodeType() {
        return new StatusCodeType();
    }

    /**
     * Create an instance of {@link StatusDetailType }
     * 
     */
    public StatusDetailType createStatusDetailType() {
        return new StatusDetailType();
    }

    /**
     * Create an instance of {@link AssertionIDRequestType }
     * 
     */
    public AssertionIDRequestType createAssertionIDRequestType() {
        return new AssertionIDRequestType();
    }

    /**
     * Create an instance of {@link AuthnQueryType }
     * 
     */
    public AuthnQueryType createAuthnQueryType() {
        return new AuthnQueryType();
    }

    /**
     * Create an instance of {@link RequestedAuthnContextType }
     * 
     */
    public RequestedAuthnContextType createRequestedAuthnContextType() {
        return new RequestedAuthnContextType();
    }

    /**
     * Create an instance of {@link AttributeQueryType }
     * 
     */
    public AttributeQueryType createAttributeQueryType() {
        return new AttributeQueryType();
    }

    /**
     * Create an instance of {@link AuthzDecisionQueryType }
     * 
     */
    public AuthzDecisionQueryType createAuthzDecisionQueryType() {
        return new AuthzDecisionQueryType();
    }

    /**
     * Create an instance of {@link AuthnRequestType }
     * 
     */
    public AuthnRequestType createAuthnRequestType() {
        return new AuthnRequestType();
    }

    /**
     * Create an instance of {@link NameIDPolicyType }
     * 
     */
    public NameIDPolicyType createNameIDPolicyType() {
        return new NameIDPolicyType();
    }

    /**
     * Create an instance of {@link ScopingType }
     * 
     */
    public ScopingType createScopingType() {
        return new ScopingType();
    }

    /**
     * Create an instance of {@link IDPListType }
     * 
     */
    public IDPListType createIDPListType() {
        return new IDPListType();
    }

    /**
     * Create an instance of {@link IDPEntryType }
     * 
     */
    public IDPEntryType createIDPEntryType() {
        return new IDPEntryType();
    }

    /**
     * Create an instance of {@link ResponseType }
     * 
     */
    public ResponseType createResponseType() {
        return new ResponseType();
    }

    /**
     * Create an instance of {@link ArtifactResolveType }
     * 
     */
    public ArtifactResolveType createArtifactResolveType() {
        return new ArtifactResolveType();
    }

    /**
     * Create an instance of {@link ArtifactResponseType }
     * 
     */
    public ArtifactResponseType createArtifactResponseType() {
        return new ArtifactResponseType();
    }

    /**
     * Create an instance of {@link ManageNameIDRequestType }
     * 
     */
    public ManageNameIDRequestType createManageNameIDRequestType() {
        return new ManageNameIDRequestType();
    }

    /**
     * Create an instance of {@link TerminateType }
     * 
     */
    public TerminateType createTerminateType() {
        return new TerminateType();
    }

    /**
     * Create an instance of {@link StatusResponseType }
     * 
     */
    public StatusResponseType createStatusResponseType() {
        return new StatusResponseType();
    }

    /**
     * Create an instance of {@link LogoutRequestType }
     * 
     */
    public LogoutRequestType createLogoutRequestType() {
        return new LogoutRequestType();
    }

    /**
     * Create an instance of {@link NameIDMappingRequestType }
     * 
     */
    public NameIDMappingRequestType createNameIDMappingRequestType() {
        return new NameIDMappingRequestType();
    }

    /**
     * Create an instance of {@link NameIDMappingResponseType }
     * 
     */
    public NameIDMappingResponseType createNameIDMappingResponseType() {
        return new NameIDMappingResponseType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExtensionsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "Extensions")
    public JAXBElement<ExtensionsType> createExtensions(ExtensionsType value) {
        return new JAXBElement<ExtensionsType>(_Extensions_QNAME, ExtensionsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StatusType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "Status")
    public JAXBElement<StatusType> createStatus(StatusType value) {
        return new JAXBElement<StatusType>(_Status_QNAME, StatusType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StatusCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "StatusCode")
    public JAXBElement<StatusCodeType> createStatusCode(StatusCodeType value) {
        return new JAXBElement<StatusCodeType>(_StatusCode_QNAME, StatusCodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "StatusMessage")
    public JAXBElement<String> createStatusMessage(String value) {
        return new JAXBElement<String>(_StatusMessage_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StatusDetailType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "StatusDetail")
    public JAXBElement<StatusDetailType> createStatusDetail(StatusDetailType value) {
        return new JAXBElement<StatusDetailType>(_StatusDetail_QNAME, StatusDetailType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssertionIDRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "AssertionIDRequest")
    public JAXBElement<AssertionIDRequestType> createAssertionIDRequest(AssertionIDRequestType value) {
        return new JAXBElement<AssertionIDRequestType>(_AssertionIDRequest_QNAME, AssertionIDRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubjectQueryAbstractType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "SubjectQuery")
    public JAXBElement<SubjectQueryAbstractType> createSubjectQuery(SubjectQueryAbstractType value) {
        return new JAXBElement<SubjectQueryAbstractType>(_SubjectQuery_QNAME, SubjectQueryAbstractType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthnQueryType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "AuthnQuery")
    public JAXBElement<AuthnQueryType> createAuthnQuery(AuthnQueryType value) {
        return new JAXBElement<AuthnQueryType>(_AuthnQuery_QNAME, AuthnQueryType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RequestedAuthnContextType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "RequestedAuthnContext")
    public JAXBElement<RequestedAuthnContextType> createRequestedAuthnContext(RequestedAuthnContextType value) {
        return new JAXBElement<RequestedAuthnContextType>(_RequestedAuthnContext_QNAME, RequestedAuthnContextType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeQueryType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "AttributeQuery")
    public JAXBElement<AttributeQueryType> createAttributeQuery(AttributeQueryType value) {
        return new JAXBElement<AttributeQueryType>(_AttributeQuery_QNAME, AttributeQueryType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthzDecisionQueryType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "AuthzDecisionQuery")
    public JAXBElement<AuthzDecisionQueryType> createAuthzDecisionQuery(AuthzDecisionQueryType value) {
        return new JAXBElement<AuthzDecisionQueryType>(_AuthzDecisionQuery_QNAME, AuthzDecisionQueryType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthnRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "AuthnRequest")
    public JAXBElement<AuthnRequestType> createAuthnRequest(AuthnRequestType value) {
        return new JAXBElement<AuthnRequestType>(_AuthnRequest_QNAME, AuthnRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NameIDPolicyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "NameIDPolicy")
    public JAXBElement<NameIDPolicyType> createNameIDPolicy(NameIDPolicyType value) {
        return new JAXBElement<NameIDPolicyType>(_NameIDPolicy_QNAME, NameIDPolicyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ScopingType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "Scoping")
    public JAXBElement<ScopingType> createScoping(ScopingType value) {
        return new JAXBElement<ScopingType>(_Scoping_QNAME, ScopingType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "RequesterID")
    public JAXBElement<String> createRequesterID(String value) {
        return new JAXBElement<String>(_RequesterID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IDPListType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "IDPList")
    public JAXBElement<IDPListType> createIDPList(IDPListType value) {
        return new JAXBElement<IDPListType>(_IDPList_QNAME, IDPListType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link IDPEntryType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "IDPEntry")
    public JAXBElement<IDPEntryType> createIDPEntry(IDPEntryType value) {
        return new JAXBElement<IDPEntryType>(_IDPEntry_QNAME, IDPEntryType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "GetComplete")
    public JAXBElement<String> createGetComplete(String value) {
        return new JAXBElement<String>(_GetComplete_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "Response")
    public JAXBElement<ResponseType> createResponse(ResponseType value) {
        return new JAXBElement<ResponseType>(_Response_QNAME, ResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArtifactResolveType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "ArtifactResolve")
    public JAXBElement<ArtifactResolveType> createArtifactResolve(ArtifactResolveType value) {
        return new JAXBElement<ArtifactResolveType>(_ArtifactResolve_QNAME, ArtifactResolveType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "Artifact")
    public JAXBElement<String> createArtifact(String value) {
        return new JAXBElement<String>(_Artifact_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArtifactResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "ArtifactResponse")
    public JAXBElement<ArtifactResponseType> createArtifactResponse(ArtifactResponseType value) {
        return new JAXBElement<ArtifactResponseType>(_ArtifactResponse_QNAME, ArtifactResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ManageNameIDRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "ManageNameIDRequest")
    public JAXBElement<ManageNameIDRequestType> createManageNameIDRequest(ManageNameIDRequestType value) {
        return new JAXBElement<ManageNameIDRequestType>(_ManageNameIDRequest_QNAME, ManageNameIDRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "NewID")
    public JAXBElement<String> createNewID(String value) {
        return new JAXBElement<String>(_NewID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EncryptedElementType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "NewEncryptedID")
    public JAXBElement<EncryptedElementType> createNewEncryptedID(EncryptedElementType value) {
        return new JAXBElement<EncryptedElementType>(_NewEncryptedID_QNAME, EncryptedElementType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TerminateType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "Terminate")
    public JAXBElement<TerminateType> createTerminate(TerminateType value) {
        return new JAXBElement<TerminateType>(_Terminate_QNAME, TerminateType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StatusResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "ManageNameIDResponse")
    public JAXBElement<StatusResponseType> createManageNameIDResponse(StatusResponseType value) {
        return new JAXBElement<StatusResponseType>(_ManageNameIDResponse_QNAME, StatusResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LogoutRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "LogoutRequest")
    public JAXBElement<LogoutRequestType> createLogoutRequest(LogoutRequestType value) {
        return new JAXBElement<LogoutRequestType>(_LogoutRequest_QNAME, LogoutRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "SessionIndex")
    public JAXBElement<String> createSessionIndex(String value) {
        return new JAXBElement<String>(_SessionIndex_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StatusResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "LogoutResponse")
    public JAXBElement<StatusResponseType> createLogoutResponse(StatusResponseType value) {
        return new JAXBElement<StatusResponseType>(_LogoutResponse_QNAME, StatusResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NameIDMappingRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "NameIDMappingRequest")
    public JAXBElement<NameIDMappingRequestType> createNameIDMappingRequest(NameIDMappingRequestType value) {
        return new JAXBElement<NameIDMappingRequestType>(_NameIDMappingRequest_QNAME, NameIDMappingRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NameIDMappingResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:protocol", name = "NameIDMappingResponse")
    public JAXBElement<NameIDMappingResponseType> createNameIDMappingResponse(NameIDMappingResponseType value) {
        return new JAXBElement<NameIDMappingResponseType>(_NameIDMappingResponse_QNAME, NameIDMappingResponseType.class, null, value);
    }

}
