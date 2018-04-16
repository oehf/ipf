package org.openehealth.ipf.commons.ihe.xacml20;

import org.openehealth.ipf.commons.ihe.xacml20.herasaf.Hl7v3DataTypesInitializer;
import org.openehealth.ipf.commons.ihe.xacml20.herasaf.Hl7v3FunctionsInitializer;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.AddPolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.AssertionBasedRequestType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.UpdatePolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.AssertionType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.StatementAbstractType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.StatusCodeType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.StatusType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.assertion.XACMLPolicyStatementType;
import org.herasaf.xacml.core.policy.Evaluatable;
import org.herasaf.xacml.core.simplePDP.initializers.InitializerExecutor;
import org.herasaf.xacml.core.simplePDP.initializers.api.Initializer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Dmytro Rud
 */
public class Xacml20Utils {

    public static final String PPQ_STATUS_SUCCESS = "urn:e-health-suisse:2015:response-status:success";
    public static final String PPQ_STATUS_FAILURE = "urn:e-health-suisse:2015:response-status:failure";

    public static final String ATTRIBUTE_TYPE_PATIENT_ID = "urn:e-health-suisse:2015:epr-spid";
    public static final String ELEMENT_NAME_PATIENT_ID = "InstanceIdentifier";

    public static final JAXBContext JAXB_CONTEXT;
    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(
                    org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.ObjectFactory.class,
                    org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.ObjectFactory.class,
                    org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ObjectFactory.class,
                    org.herasaf.xacml.core.policy.impl.ObjectFactory.class,
                    org.herasaf.xacml.core.context.impl.ObjectFactory.class,
                    org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.ObjectFactory.class,
                    org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.assertion.ObjectFactory.class,
                    org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3.ObjectFactory.class,
                    org.apache.xml.security.binding.xmlenc.ObjectFactory.class,
                    org.apache.xml.security.binding.xmldsig.ObjectFactory.class);
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * Configures the HERAS-AF framework to use HL7v3 data types and functions
     * and provides the possibility to run user-defined custom initialization routines.
     * <p/>
     * This function is idempotent, it is safe to run it multiple times.
     *
     * @param customInitializers additional optional user-defined custom initializers.
     */
    public static void initializeHerasaf(Initializer... customInitializers) {
        Set<Initializer> initializers = new HashSet<>(InitializerExecutor.getDefaultInitializers());
        initializers.add(new Hl7v3DataTypesInitializer());
        initializers.add(new Hl7v3FunctionsInitializer());
        initializers.addAll(Arrays.asList(customInitializers));
        InitializerExecutor.setInitalizers(initializers);
        InitializerExecutor.runInitializers();
    }

    public static ResponseType createXacmlQueryResponse(String status) {
        StatusCodeType statusCodeType = new StatusCodeType();
        statusCodeType.setValue(status);
        StatusType statusType = new StatusType();
        statusType.setStatusCode(statusCodeType);
        ResponseType responseType = new ResponseType();
        responseType.setStatus(statusType);
        return responseType;
    }

    /**
     * Creates a stream of all policies and policy sets contained in the given PPQ response object.
     *
     * @param response PPQ response.
     * @return resulting stream, may be ampty but never <code>null</code>.
     */
    public static Stream<Evaluatable> toStream(ResponseType response) {
        Stream<Evaluatable> result = Stream.of();
        if (response != null) {
            for (Object object : response.getAssertionOrEncryptedAssertion()) {
                if (object instanceof AssertionType) {
                    AssertionType assertion = (AssertionType) object;
                    for (StatementAbstractType statement : assertion.getStatementOrAuthnStatementOrAuthzDecisionStatement()) {
                        if (statement instanceof XACMLPolicyStatementType) {
                            XACMLPolicyStatementType policyStatement = (XACMLPolicyStatementType) statement;
                            result = Stream.concat(result, policyStatement.getPolicyOrPolicySet().stream().map(x -> (Evaluatable) x));
                        }
                    }
                }
            }
        }
        return result;
    }

    private static Stream<Evaluatable> toStream(AssertionBasedRequestType request) {
        Stream<Evaluatable> result = Stream.of();
        if (request != null) {
            for (StatementAbstractType abstractStatement : request.getAssertion().getStatementOrAuthnStatementOrAuthzDecisionStatement()) {
                if (abstractStatement instanceof XACMLPolicyStatementType) {
                    XACMLPolicyStatementType policyStatement = (XACMLPolicyStatementType) abstractStatement;
                    result = Stream.concat(result, policyStatement.getPolicyOrPolicySet().stream().map(x -> (Evaluatable) x));
                }
            }
        }
        return result;
    }

    /**
     * Creates a stream of all policies and policy sets contained in the given PPQ Add Policy request.
     *
     * @param request PPQ Add Policy request.
     * @return resulting stream, may be ampty but never <code>null</code>.
     */
    public static Stream<Evaluatable> toStream(AddPolicyRequest request) {
        return toStream((AssertionBasedRequestType) request);
    }

    /**
     * Creates a stream of all policies and policy sets contained in the given PPQ Update Policy request.
     *
     * @param request PPQ Update Policy request.
     * @return resulting stream, may be ampty but never <code>null</code>.
     */
    public static Stream<Evaluatable> toStream(UpdatePolicyRequest request) {
        return toStream((AssertionBasedRequestType) request);
    }

}
