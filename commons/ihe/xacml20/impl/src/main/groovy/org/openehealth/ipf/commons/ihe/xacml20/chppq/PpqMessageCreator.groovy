package org.openehealth.ipf.commons.ihe.xacml20.chppq

import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils
import org.openehealth.ipf.commons.ihe.xacml20.herasaf.functions.IiEqualFunction
import org.openehealth.ipf.commons.ihe.xacml20.herasaf.types.CvDataTypeAttribute
import org.openehealth.ipf.commons.ihe.xacml20.herasaf.types.IiDataTypeAttribute
import org.openehealth.ipf.commons.ihe.xacml20.model.AccessLevel
import org.openehealth.ipf.commons.ihe.xacml20.model.CE
import org.openehealth.ipf.commons.ihe.xacml20.model.ConfidentialityCode
import org.openehealth.ipf.commons.ihe.xacml20.model.NameQualifier
import org.openehealth.ipf.commons.ihe.xacml20.model.PurposeOfUse
import org.openehealth.ipf.commons.ihe.xacml20.model.SubjectRole
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.AddPolicyRequest
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.AssertionBasedRequestType
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.DeletePolicyRequest
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.UpdatePolicyRequest
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.XACMLPolicySetIdReferenceStatementType
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.CV
import org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.II
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.AssertionType
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.NameIDType
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.StatementAbstractType
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.assertion.XACMLPolicyStatementType
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLPolicyQueryType
import org.herasaf.xacml.core.combiningAlgorithm.policy.impl.PolicyDenyOverridesAlgorithm
import org.herasaf.xacml.core.context.impl.*
import org.herasaf.xacml.core.dataTypeAttribute.DataTypeAttribute
import org.herasaf.xacml.core.dataTypeAttribute.impl.AnyURIDataTypeAttribute
import org.herasaf.xacml.core.dataTypeAttribute.impl.DateDataTypeAttribute
import org.herasaf.xacml.core.dataTypeAttribute.impl.StringDataTypeAttribute
import org.herasaf.xacml.core.function.impl.equalityPredicates.AnyURIEqualFunction
import org.herasaf.xacml.core.function.impl.equalityPredicates.StringEqualFunction
import org.herasaf.xacml.core.function.impl.nonNumericComparisonFunctions.DateGreaterThanOrEqualFunction
import org.herasaf.xacml.core.policy.Evaluatable
import org.herasaf.xacml.core.policy.impl.EnvironmentAttributeDesignatorType
import org.herasaf.xacml.core.policy.impl.EnvironmentMatchType
import org.herasaf.xacml.core.policy.impl.EnvironmentsType
import org.herasaf.xacml.core.policy.impl.EvaluatableIDImpl
import org.herasaf.xacml.core.policy.impl.IdReferenceType
import org.herasaf.xacml.core.policy.impl.PolicySetType
import org.herasaf.xacml.core.policy.impl.ResourceAttributeDesignatorType
import org.herasaf.xacml.core.policy.impl.ResourceMatchType
import org.herasaf.xacml.core.policy.impl.ResourcesType
import org.herasaf.xacml.core.policy.impl.SubjectAttributeDesignatorType
import org.herasaf.xacml.core.policy.impl.SubjectMatchType
import org.herasaf.xacml.core.policy.impl.SubjectsType
import org.herasaf.xacml.core.policy.impl.TargetType
import org.joda.time.DateTime
import org.openehealth.ipf.commons.xml.XmlUtils

import javax.xml.bind.JAXBElement
import javax.xml.datatype.DatatypeFactory
import java.text.SimpleDateFormat

import static org.openehealth.ipf.commons.ihe.xacml20.model.PpqConstants.*


/**
 * @author Dmytro Rud
 */
class PpqMessageCreator {

    private static final String VERSION = '2.0'

    static final org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.ObjectFactory HL7V3_FACTORY =
            new org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.ObjectFactory()

    static final org.herasaf.xacml.core.context.impl.ObjectFactory XACML_CONTEXT_FACTORY =
            new org.herasaf.xacml.core.context.impl.ObjectFactory()

    static final org.herasaf.xacml.core.policy.impl.ObjectFactory XACML_POLICY_FACTORY =
            new org.herasaf.xacml.core.policy.impl.ObjectFactory()

    static final DatatypeFactory XML_FACTORY = DatatypeFactory.newInstance()

    static String randomXacmlId() {
        return '_' + UUID.randomUUID().toString()
    }

    private static XACMLPolicyQueryType createPpqQuery(List<JAXBElement<?>> contents) {
        return new XACMLPolicyQueryType(
                ID: randomXacmlId(),
                version: VERSION,
                issueInstant: XML_FACTORY.newXMLGregorianCalendar(new GregorianCalendar()),
                requestOrPolicySetIdReferenceOrPolicyIdReference: contents)
    }

    /**
     * Creates a PPQ Query by EPR-SPID
     */
    static XACMLPolicyQueryType createQueryByPatientId(String eprSpid) {
        return createQueryByPatientId(eprSpid, CodingSystemIds.SWISS_PATIENT_ID)
    }

    /**
     * Creates a PPQ Query by Patient ID and Assigning Authority
     */
    static XACMLPolicyQueryType createQueryByPatientId(String patientId, String assigningAuthorityOid) {
        return createPpqQuery([
                XACML_CONTEXT_FACTORY.createRequest(new RequestType(
                        subjects: [new SubjectType()],
                        resources: [
                                new ResourceType(
                                        attributes: [
                                                new AttributeType(
                                                        attributeId: Xacml20Utils.ATTRIBUTE_TYPE_PATIENT_ID,
                                                        dataType: new IiDataTypeAttribute(),
                                                        attributeValues: [
                                                                new AttributeValueType(
                                                                        content: [
                                                                                HL7V3_FACTORY.createInstanceIdentifier(new II(
                                                                                        root: assigningAuthorityOid,
                                                                                        extension: patientId,
                                                                                )),
                                                                        ],
                                                                ),
                                                        ],
                                                ),
                                        ],
                                ),
                        ],
                        action: new ActionType(),
                        environment: new EnvironmentType(),
                )),
        ])
    }

    /**
     * Creates a PPQ Query by Policy ID (works also for policy set IDs)
     */
    static XACMLPolicyQueryType createQueryByPolicyId(String policyId) {
        return createPpqQuery([
                XACML_POLICY_FACTORY.createPolicyIdReference(new IdReferenceType(
                        value: policyId
                ))
        ])
    }

    static AttributeType createAttribute(String id, DataTypeAttribute dataType, Object value) {
        return new AttributeType(
                attributeId: id,
                dataType: dataType,
                attributeValues: [new AttributeValueType(content: [value])]
        )
    }

    static String toCv(CE ce) {
        def cv = new CV(
                code:           ce.code,
                codeSystem:     ce.codeSystem,
                codeSystemName: ce.codeSystemName,
                displayName:    ce.displayName,
        )
        def cvJaxbElement = HL7V3_FACTORY.createCodedValue(cv)
        return XmlUtils.renderJaxb(Xacml20Utils.JAXB_CONTEXT, cvJaxbElement, false)
    }

    static String toIi(String eprSpid) {
        def ii = new II(extension: eprSpid, root: CodingSystemIds.SWISS_PATIENT_ID)
        def iiJaxbElement = HL7V3_FACTORY.createInstanceIdentifier(ii)
        return XmlUtils.renderJaxb(Xacml20Utils.JAXB_CONTEXT, iiJaxbElement, false)
    }

    static RequestType createAdrRequestBasis(String homeCommunityId, String eprSpid, String gln, String organizationOid) {
        return new RequestType(
                subjects: [
                        new SubjectType(
                                attributes: [
                                        createAttribute(AttributeIds.XACML_1_0_SUBJECT_ID, new StringDataTypeAttribute(), gln),
                                        createAttribute(AttributeIds.XACML_1_0_SUBJECT_ID_QUALIFIER, new StringDataTypeAttribute(), NameQualifier.PROFESSIONAL.qualifier),
                                        createAttribute(AttributeIds.XCA_2010_HOME_COMMUNITY_ID, new AnyURIDataTypeAttribute(), homeCommunityId),
                                        createAttribute(AttributeIds.XACML_2_0_SUBJECT_ROLE, new CvDataTypeAttribute(), toCv(SubjectRole.PROFESSIONAL.code)),
                                        createAttribute(AttributeIds.XSPA_1_0_SUBJECT_ORGANIZATION_ID, new AnyURIDataTypeAttribute(), organizationOid),
                                        createAttribute(AttributeIds.XSPA_1_0_SUBJECT_PURPOSE_OF_USE, new CvDataTypeAttribute(), toCv(PurposeOfUse.NORMAL.code)),
                                ],
                        ),
                ],
                resources: [
                        new ResourceType(
                                attributes: [
                                        createAttribute(AttributeIds.EHEALTH_SUISSSE_2015_EPR_SPID, new IiDataTypeAttribute(), toIi(eprSpid)),
                                ]
                        ),
                ],
                action: new ActionType(),
                environment: new EnvironmentType(),
        )
    }

    /**
     * Creates an ADR request to check rights to perform PPQ AddPolicy operation (i.e. to delegate access rights defined by a patient).
     *
     * @param homeCommunityId   ID of the current community.
     * @param eprSpid           EPR-SPID of the patient whose policies are to be delegated.
     * @param gln               GLN of the practitioner whose rights to delegate policies shall be checked.
     * @param organizationOid   OID of organization where the practitioner is a member.
     * @param accessLevel       Access level to delegate.
     * @return                  ADR request.
     */
    static RequestType createDelegatePoliciesAdrRequest(String homeCommunityId, String eprSpid, String gln, String organizationOid, AccessLevel accessLevel) {
        RequestType request = createAdrRequestBasis(homeCommunityId, eprSpid, gln, organizationOid)
        request.resources[0].attributes << createAttribute(AttributeIds.EHEALTH_SUISSSE_2015_REFERENCED_POLICY_SET, new AnyURIDataTypeAttribute(), accessLevel.urn)
        request.action.attributes << createAttribute(AttributeIds.XACML_1_0_ACTION_ID, new AnyURIDataTypeAttribute(), ActionIds.PPQ_ADD_POLICY)
        return request
    }

    /**
     * Creates an ADR request to check rights to perform XDS Registry Stored Query (ITI-18).
     *
     * @param homeCommunityId       ID of the current community.
     * @param eprSpid               EPR-SPID of the patient whose documents are to be queried.
     * @param gln                   GLN of the practitioner whose rights to query documents shall be checked.
     * @param organizationOid       OID of the organization where the practitioner is a member.
     * @param confidentialityCode   Document confidentiality code.
     * @return                      ADR request.
     */
    static RequestType createIti18AdrRequest(String homeCommunityId, String eprSpid, String gln, String organizationOid, ConfidentialityCode confidentialityCode) {
        RequestType request = createAdrRequestBasis(homeCommunityId, eprSpid, gln, organizationOid)
        request.resources[0].attributes << createAttribute(AttributeIds.XDS_2007_CONFIDENTIALITY_CODE, new CvDataTypeAttribute(), toCv(confidentialityCode.code))
        request.action.attributes << createAttribute(AttributeIds.XACML_1_0_ACTION_ID, new AnyURIDataTypeAttribute(), ActionIds.XDS_STORED_QUERY)
        return request
    }

    static PolicySetType createPolicySet3132Basis(String eprSpid, AccessLevel accessLevel, Date endDate) {
        JAXBElement patientId = HL7V3_FACTORY.createInstanceIdentifier(new II(root: CodingSystemIds.SWISS_PATIENT_ID, extension: eprSpid))

        PolicySetType policySet = new PolicySetType(
                policySetId: new EvaluatableIDImpl(randomXacmlId()),
                policyCombiningAlg: new PolicyDenyOverridesAlgorithm(),
                target: new TargetType(
                        subjects: new SubjectsType(
                                subjects: [
                                        new org.herasaf.xacml.core.policy.impl.SubjectType(),
                                ],
                        ),
                        resources: new ResourcesType(
                                resources: [
                                        new org.herasaf.xacml.core.policy.impl.ResourceType(
                                                resourceMatches: [
                                                        new ResourceMatchType(
                                                                matchFunction: new IiEqualFunction(),
                                                                attributeValue: new org.herasaf.xacml.core.policy.impl.AttributeValueType(
                                                                        content: [patientId],
                                                                        dataType: new IiDataTypeAttribute(),
                                                                ),
                                                                resourceAttributeDesignator: new ResourceAttributeDesignatorType(
                                                                        attributeId: AttributeIds.EHEALTH_SUISSSE_2015_EPR_SPID,
                                                                        dataType: new IiDataTypeAttribute(),
                                                                ),
                                                        ),
                                                ]
                                        ),
                                ],
                        ),
                ),
                additionalInformation: [XACML_POLICY_FACTORY.createPolicySetIdReference(new IdReferenceType(value: accessLevel.urn))]
        )

        if (endDate) {
            policySet.target.environments = new EnvironmentsType(
                    environments: [
                            new org.herasaf.xacml.core.policy.impl.EnvironmentType(
                                    environmentMatches: [
                                            new EnvironmentMatchType(
                                                    matchFunction: new DateGreaterThanOrEqualFunction(),
                                                    attributeValue: new org.herasaf.xacml.core.policy.impl.AttributeValueType(
                                                            content: [new SimpleDateFormat("yyyy-MM-dd").format(endDate)],
                                                            dataType: new DateDataTypeAttribute(),
                                                    ),
                                                    environmentAttributeDesignator: new EnvironmentAttributeDesignatorType(
                                                            attributeId: AttributeIds.XACML_1_0_CURRENT_DATE,
                                                            dataType: new DateDataTypeAttribute(),
                                                    ),
                                            ),
                                    ],
                            ),
                    ],
            )
        }

        return policySet
    }

    /**
     * Creates a policy set on the of the policy template #31 (access delegation to an single practitioner).
     *
     * @param eprSpid       EPR-SPID of the patient whose policies are to be delegated.
     * @param gln           GLN of the practitioner to whom the rights shall be delegated.
     * @param accessLevel   Access level which shall be delegated.
     * @param endDate       Optional end date of the validity of the delegated policies.
     * @return              Policy set.
     */
    static PolicySetType createPolicySet31(String eprSpid, String gln, AccessLevel accessLevel, Date endDate) {
        PolicySetType policySetType = createPolicySet3132Basis(eprSpid, accessLevel, endDate)
        policySetType.target.subjects.subjects[0].subjectMatches += [
                new SubjectMatchType(
                        matchFunction: new StringEqualFunction(),
                        attributeValue: new org.herasaf.xacml.core.policy.impl.AttributeValueType(
                                content: [gln],
                                dataType: new StringDataTypeAttribute(),
                        ),
                        subjectAttributeDesignator: new SubjectAttributeDesignatorType(
                                attributeId: AttributeIds.XACML_1_0_SUBJECT_ID,
                                dataType: new StringDataTypeAttribute(),
                        ),
                ),
                new SubjectMatchType(
                        matchFunction: new StringEqualFunction(),
                        attributeValue: new org.herasaf.xacml.core.policy.impl.AttributeValueType(
                                content: ['urn:gs1:gln'],
                                dataType: new StringDataTypeAttribute(),
                        ),
                        subjectAttributeDesignator: new SubjectAttributeDesignatorType(
                                attributeId: AttributeIds.XACML_1_0_SUBJECT_ID_QUALIFIER,
                                dataType: new StringDataTypeAttribute(),
                        ),
                ),
        ]
        return policySetType
    }

    /**
     * Creates a policy set on the of the policy template #32 (access delegation to an organization/a group of practitioners).
     *
     * @param eprSpid       EPR-SPID of the patient whose policies are to be delegated.
     * @param oid           OID of the organization to which the rights shall be delegated.
     * @param accessLevel   Access level which shall be delegated.
     * @param endDate       Required end date of the validity of the delegated policies.
     * @return              Policy set.
     */
    static PolicySetType createPolicySet32(String eprSpid, String oid, AccessLevel accessLevel, Date endDate) {
        PolicySetType policySetType = createPolicySet3132Basis(eprSpid, accessLevel, endDate)
        policySetType.target.subjects.subjects[0].subjectMatches += [
                new SubjectMatchType(
                        matchFunction: new AnyURIEqualFunction(),
                        attributeValue: new org.herasaf.xacml.core.policy.impl.AttributeValueType(
                                content: [oid],
                                dataType: new AnyURIDataTypeAttribute(),
                        ),
                        subjectAttributeDesignator: new SubjectAttributeDesignatorType(
                                attributeId: AttributeIds.XSPA_1_0_SUBJECT_ORGANIZATION_ID,
                                dataType: new AnyURIDataTypeAttribute(),
                        ),
                ),
        ]
        return policySetType
    }

    private static createRequestAssertion(String homeCommunityId, List<StatementAbstractType> statements) {
        return new AssertionType(
                ID: randomXacmlId(),
                version: VERSION,
                issueInstant: XML_FACTORY.newXMLGregorianCalendar(new GregorianCalendar()),
                issuer: new NameIDType(
                        nameQualifier: NAME_QUALIFIER_EHEALTH_SUISSSE_COMMUNITY_INDEX,
                        value: homeCommunityId,
                ),
                statementOrAuthnStatementOrAuthzDecisionStatement: statements,
        )
    }

    /**
     * Creates a PPQ Add Policy request.
     * @param homeCommunityId   ID of the current community.
     * @param evaluatables      Policies and/or policy sets which are to be added.
     * @return                  PPQ Add Policy request.
     */
    static AddPolicyRequest createAddPoliciesRequest(String homeCommunityId, Collection<? extends Evaluatable> evaluatables) {
        return new AddPolicyRequest(assertion: createRequestAssertion(homeCommunityId, [new XACMLPolicyStatementType(policyOrPolicySet: evaluatables)]))
    }

    /**
     * Creates a PPQ Delete Policy request.
     * @param homeCommunityId   ID of the current community.
     * @param evaluatables      IDs of policies and/or policy sets which are to be deleted.
     * @return                  PPQ Delete Policy request.
     */
    static DeletePolicyRequest createDeletePoliciesRequest(String homeCommunityId, List<String> policyIds) {
        List<StatementAbstractType> policyStatements = policyIds.collect { policyId ->
            new XACMLPolicySetIdReferenceStatementType(policySetIdReference: [
                    new IdReferenceType(value: policyId)
            ])
        }

        return new DeletePolicyRequest(assertion: createRequestAssertion(homeCommunityId, policyStatements))
    }

    private static <T extends AssertionBasedRequestType> T createPpqAddOrUpdatePolicyRequest(String elementName, String homeCommunityId, String contents) {
        String requestString = """
        <epr:${elementName} xmlns:epr="urn:e-health-suisse:2015:policy-administration" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
            <saml:Assertion xmlns:saml="urn:oasis:names:tc:SAML:2.0:assertion" ID="${randomXacmlId()}" Version="${VERSION}" IssueInstant="${DateTime.now().toString()}">
                <saml:Issuer NameQualifier="${NAME_QUALIFIER_EHEALTH_SUISSSE_COMMUNITY_INDEX}">${homeCommunityId}</saml:Issuer>
                <saml:Statement xsi:type="xacml-saml:XACMLPolicyStatementType" xmlns:xacml-saml="urn:oasis:names:tc:xacml:2.0:profile:saml2.0:v2:schema:assertion">
                    ${contents}
                </saml:Statement>
            </saml:Assertion>
        </epr:${elementName}>
        """

        Object object = Xacml20Utils.JAXB_CONTEXT.createUnmarshaller().unmarshal(XmlUtils.source(requestString))
        if (object instanceof JAXBElement) {
            object = ((JAXBElement) object).value
        }
        return (T) object
    }

    /**
     * Creates a PPQ Add Policy request.
     * @param homeCommunityId   ID of the current community.
     * @param contents          Policies and/or policy sets as concatenated XML documents.
     * @return                  PPQ Add Policy request.
     */
    static AddPolicyRequest createAddPoliciesRequest(String homeCommunityId, String contents) throws Exception {
        return createPpqAddOrUpdatePolicyRequest('AddPolicyRequest', homeCommunityId, contents)
    }

    /**
     * Creates a PPQ Update Policy request.
     * @param homeCommunityId   ID of the current community.
     * @param contents          Policies and/or policy sets as concatenated XML documents.
     * @return                  PPQ Update Policy request.
     */
    static UpdatePolicyRequest createUpdatePoliciesRequest(String homeCommunityId, String contents) throws Exception {
        return createPpqAddOrUpdatePolicyRequest('UpdatePolicyRequest', homeCommunityId, contents)
    }
}
