
package org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion package. 
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

    private final static QName _BaseID_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "BaseID");
    private final static QName _NameID_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "NameID");
    private final static QName _EncryptedID_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "EncryptedID");
    private final static QName _Issuer_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Issuer");
    private final static QName _AssertionIDRef_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "AssertionIDRef");
    private final static QName _AssertionURIRef_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "AssertionURIRef");
    private final static QName _Assertion_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion");
    private final static QName _Subject_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Subject");
    private final static QName _SubjectConfirmation_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "SubjectConfirmation");
    private final static QName _SubjectConfirmationData_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "SubjectConfirmationData");
    private final static QName _Conditions_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Conditions");
    private final static QName _Condition_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Condition");
    private final static QName _AudienceRestriction_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "AudienceRestriction");
    private final static QName _Audience_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Audience");
    private final static QName _OneTimeUse_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "OneTimeUse");
    private final static QName _ProxyRestriction_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "ProxyRestriction");
    private final static QName _Advice_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Advice");
    private final static QName _EncryptedAssertion_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "EncryptedAssertion");
    private final static QName _Statement_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Statement");
    private final static QName _AuthnStatement_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "AuthnStatement");
    private final static QName _SubjectLocality_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "SubjectLocality");
    private final static QName _AuthnContext_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "AuthnContext");
    private final static QName _AuthnContextClassRef_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "AuthnContextClassRef");
    private final static QName _AuthnContextDeclRef_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "AuthnContextDeclRef");
    private final static QName _AuthnContextDecl_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "AuthnContextDecl");
    private final static QName _AuthenticatingAuthority_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "AuthenticatingAuthority");
    private final static QName _AuthzDecisionStatement_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "AuthzDecisionStatement");
    private final static QName _Action_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Action");
    private final static QName _Evidence_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Evidence");
    private final static QName _AttributeStatement_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "AttributeStatement");
    private final static QName _Attribute_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Attribute");
    private final static QName _AttributeValue_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "AttributeValue");
    private final static QName _EncryptedAttribute_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "EncryptedAttribute");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link EncryptedElementType }
     * 
     */
    public EncryptedElementType createEncryptedElementType() {
        return new EncryptedElementType();
    }

    /**
     * Create an instance of {@link NameIDType }
     * 
     */
    public NameIDType createNameIDType() {
        return new NameIDType();
    }

    /**
     * Create an instance of {@link AssertionType }
     * 
     */
    public AssertionType createAssertionType() {
        return new AssertionType();
    }

    /**
     * Create an instance of {@link SubjectType }
     * 
     */
    public SubjectType createSubjectType() {
        return new SubjectType();
    }

    /**
     * Create an instance of {@link SubjectConfirmationType }
     * 
     */
    public SubjectConfirmationType createSubjectConfirmationType() {
        return new SubjectConfirmationType();
    }

    /**
     * Create an instance of {@link SubjectConfirmationDataType }
     * 
     */
    public SubjectConfirmationDataType createSubjectConfirmationDataType() {
        return new SubjectConfirmationDataType();
    }

    /**
     * Create an instance of {@link ConditionsType }
     * 
     */
    public ConditionsType createConditionsType() {
        return new ConditionsType();
    }

    /**
     * Create an instance of {@link AudienceRestrictionType }
     * 
     */
    public AudienceRestrictionType createAudienceRestrictionType() {
        return new AudienceRestrictionType();
    }

    /**
     * Create an instance of {@link OneTimeUseType }
     * 
     */
    public OneTimeUseType createOneTimeUseType() {
        return new OneTimeUseType();
    }

    /**
     * Create an instance of {@link ProxyRestrictionType }
     * 
     */
    public ProxyRestrictionType createProxyRestrictionType() {
        return new ProxyRestrictionType();
    }

    /**
     * Create an instance of {@link AdviceType }
     * 
     */
    public AdviceType createAdviceType() {
        return new AdviceType();
    }

    /**
     * Create an instance of {@link AuthnStatementType }
     * 
     */
    public AuthnStatementType createAuthnStatementType() {
        return new AuthnStatementType();
    }

    /**
     * Create an instance of {@link SubjectLocalityType }
     * 
     */
    public SubjectLocalityType createSubjectLocalityType() {
        return new SubjectLocalityType();
    }

    /**
     * Create an instance of {@link AuthnContextType }
     * 
     */
    public AuthnContextType createAuthnContextType() {
        return new AuthnContextType();
    }

    /**
     * Create an instance of {@link AuthzDecisionStatementType }
     * 
     */
    public AuthzDecisionStatementType createAuthzDecisionStatementType() {
        return new AuthzDecisionStatementType();
    }

    /**
     * Create an instance of {@link ActionType }
     * 
     */
    public ActionType createActionType() {
        return new ActionType();
    }

    /**
     * Create an instance of {@link EvidenceType }
     * 
     */
    public EvidenceType createEvidenceType() {
        return new EvidenceType();
    }

    /**
     * Create an instance of {@link AttributeStatementType }
     * 
     */
    public AttributeStatementType createAttributeStatementType() {
        return new AttributeStatementType();
    }

    /**
     * Create an instance of {@link AttributeType }
     * 
     */
    public AttributeType createAttributeType() {
        return new AttributeType();
    }

    /**
     * Create an instance of {@link KeyInfoConfirmationDataType }
     * 
     */
    public KeyInfoConfirmationDataType createKeyInfoConfirmationDataType() {
        return new KeyInfoConfirmationDataType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BaseIDAbstractType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "BaseID")
    public JAXBElement<BaseIDAbstractType> createBaseID(BaseIDAbstractType value) {
        return new JAXBElement<BaseIDAbstractType>(_BaseID_QNAME, BaseIDAbstractType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NameIDType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "NameID")
    public JAXBElement<NameIDType> createNameID(NameIDType value) {
        return new JAXBElement<NameIDType>(_NameID_QNAME, NameIDType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EncryptedElementType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "EncryptedID")
    public JAXBElement<EncryptedElementType> createEncryptedID(EncryptedElementType value) {
        return new JAXBElement<EncryptedElementType>(_EncryptedID_QNAME, EncryptedElementType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NameIDType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "Issuer")
    public JAXBElement<NameIDType> createIssuer(NameIDType value) {
        return new JAXBElement<NameIDType>(_Issuer_QNAME, NameIDType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "AssertionIDRef")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createAssertionIDRef(String value) {
        return new JAXBElement<String>(_AssertionIDRef_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "AssertionURIRef")
    public JAXBElement<String> createAssertionURIRef(String value) {
        return new JAXBElement<String>(_AssertionURIRef_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssertionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "Assertion")
    public JAXBElement<AssertionType> createAssertion(AssertionType value) {
        return new JAXBElement<AssertionType>(_Assertion_QNAME, AssertionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubjectType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "Subject")
    public JAXBElement<SubjectType> createSubject(SubjectType value) {
        return new JAXBElement<SubjectType>(_Subject_QNAME, SubjectType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubjectConfirmationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "SubjectConfirmation")
    public JAXBElement<SubjectConfirmationType> createSubjectConfirmation(SubjectConfirmationType value) {
        return new JAXBElement<SubjectConfirmationType>(_SubjectConfirmation_QNAME, SubjectConfirmationType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubjectConfirmationDataType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "SubjectConfirmationData")
    public JAXBElement<SubjectConfirmationDataType> createSubjectConfirmationData(SubjectConfirmationDataType value) {
        return new JAXBElement<SubjectConfirmationDataType>(_SubjectConfirmationData_QNAME, SubjectConfirmationDataType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConditionsType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "Conditions")
    public JAXBElement<ConditionsType> createConditions(ConditionsType value) {
        return new JAXBElement<ConditionsType>(_Conditions_QNAME, ConditionsType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConditionAbstractType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "Condition")
    public JAXBElement<ConditionAbstractType> createCondition(ConditionAbstractType value) {
        return new JAXBElement<ConditionAbstractType>(_Condition_QNAME, ConditionAbstractType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AudienceRestrictionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "AudienceRestriction")
    public JAXBElement<AudienceRestrictionType> createAudienceRestriction(AudienceRestrictionType value) {
        return new JAXBElement<AudienceRestrictionType>(_AudienceRestriction_QNAME, AudienceRestrictionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "Audience")
    public JAXBElement<String> createAudience(String value) {
        return new JAXBElement<String>(_Audience_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OneTimeUseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "OneTimeUse")
    public JAXBElement<OneTimeUseType> createOneTimeUse(OneTimeUseType value) {
        return new JAXBElement<OneTimeUseType>(_OneTimeUse_QNAME, OneTimeUseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ProxyRestrictionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "ProxyRestriction")
    public JAXBElement<ProxyRestrictionType> createProxyRestriction(ProxyRestrictionType value) {
        return new JAXBElement<ProxyRestrictionType>(_ProxyRestriction_QNAME, ProxyRestrictionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdviceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "Advice")
    public JAXBElement<AdviceType> createAdvice(AdviceType value) {
        return new JAXBElement<AdviceType>(_Advice_QNAME, AdviceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EncryptedElementType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "EncryptedAssertion")
    public JAXBElement<EncryptedElementType> createEncryptedAssertion(EncryptedElementType value) {
        return new JAXBElement<EncryptedElementType>(_EncryptedAssertion_QNAME, EncryptedElementType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StatementAbstractType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "Statement")
    public JAXBElement<StatementAbstractType> createStatement(StatementAbstractType value) {
        return new JAXBElement<StatementAbstractType>(_Statement_QNAME, StatementAbstractType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthnStatementType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "AuthnStatement")
    public JAXBElement<AuthnStatementType> createAuthnStatement(AuthnStatementType value) {
        return new JAXBElement<AuthnStatementType>(_AuthnStatement_QNAME, AuthnStatementType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SubjectLocalityType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "SubjectLocality")
    public JAXBElement<SubjectLocalityType> createSubjectLocality(SubjectLocalityType value) {
        return new JAXBElement<SubjectLocalityType>(_SubjectLocality_QNAME, SubjectLocalityType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthnContextType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "AuthnContext")
    public JAXBElement<AuthnContextType> createAuthnContext(AuthnContextType value) {
        return new JAXBElement<AuthnContextType>(_AuthnContext_QNAME, AuthnContextType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "AuthnContextClassRef")
    public JAXBElement<String> createAuthnContextClassRef(String value) {
        return new JAXBElement<String>(_AuthnContextClassRef_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "AuthnContextDeclRef")
    public JAXBElement<String> createAuthnContextDeclRef(String value) {
        return new JAXBElement<String>(_AuthnContextDeclRef_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "AuthnContextDecl")
    public JAXBElement<Object> createAuthnContextDecl(Object value) {
        return new JAXBElement<Object>(_AuthnContextDecl_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "AuthenticatingAuthority")
    public JAXBElement<String> createAuthenticatingAuthority(String value) {
        return new JAXBElement<String>(_AuthenticatingAuthority_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AuthzDecisionStatementType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "AuthzDecisionStatement")
    public JAXBElement<AuthzDecisionStatementType> createAuthzDecisionStatement(AuthzDecisionStatementType value) {
        return new JAXBElement<AuthzDecisionStatementType>(_AuthzDecisionStatement_QNAME, AuthzDecisionStatementType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ActionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "Action")
    public JAXBElement<ActionType> createAction(ActionType value) {
        return new JAXBElement<ActionType>(_Action_QNAME, ActionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EvidenceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "Evidence")
    public JAXBElement<EvidenceType> createEvidence(EvidenceType value) {
        return new JAXBElement<EvidenceType>(_Evidence_QNAME, EvidenceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeStatementType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "AttributeStatement")
    public JAXBElement<AttributeStatementType> createAttributeStatement(AttributeStatementType value) {
        return new JAXBElement<AttributeStatementType>(_AttributeStatement_QNAME, AttributeStatementType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "Attribute")
    public JAXBElement<AttributeType> createAttribute(AttributeType value) {
        return new JAXBElement<AttributeType>(_Attribute_QNAME, AttributeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "AttributeValue")
    public JAXBElement<Object> createAttributeValue(Object value) {
        return new JAXBElement<Object>(_AttributeValue_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EncryptedElementType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "EncryptedAttribute")
    public JAXBElement<EncryptedElementType> createEncryptedAttribute(EncryptedElementType value) {
        return new JAXBElement<EncryptedElementType>(_EncryptedAttribute_QNAME, EncryptedElementType.class, null, value);
    }

}
