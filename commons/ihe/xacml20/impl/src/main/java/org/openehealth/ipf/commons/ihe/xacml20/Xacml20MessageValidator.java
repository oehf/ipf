/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xacml20;

import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.AddPolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.DeletePolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.UpdatePolicyRequest;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLPolicyQueryType;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss12.EpdPolicyRepositoryResponse;
import org.openehealth.ipf.commons.xml.XsdValidator;

import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmytro Rud
 */
public class Xacml20MessageValidator {

    private static final XsdValidator XSD_VALIDATOR = new XsdValidator();

    private static final Map<Class<?>, String> REQUEST_SCHEMAS;
    static {
        REQUEST_SCHEMAS = new HashMap<>();
        REQUEST_SCHEMAS.put(XACMLPolicyQueryType.class, "schema/xacml-2.0-profile-saml2.0-v2-schema-protocol-wd-14.xsd");
        REQUEST_SCHEMAS.put(AddPolicyRequest.class,     "schema/epd-policy-administration-combined-schema-1.2-local.xsd");
        REQUEST_SCHEMAS.put(UpdatePolicyRequest.class,  "schema/epd-policy-administration-combined-schema-1.2-local.xsd");
        REQUEST_SCHEMAS.put(DeletePolicyRequest.class,  "schema/epd-policy-administration-combined-schema-1.2-local.xsd");
    }

    private static final Map<Class<?>, String> RESPONSE_SCHEMAS;
    static {
        RESPONSE_SCHEMAS = new HashMap<>();
        RESPONSE_SCHEMAS.put(ResponseType.class, "schema/PolicyQueryResponse.xsd");
        RESPONSE_SCHEMAS.put(EpdPolicyRepositoryResponse.class, "schema/epd-policy-administration-combined-schema-1.2-local.xsd");
    }

    private static void validateMessage(Object message, String messageName, Map<Class<?>, String> schemaNames) {
        if (message == null) {
            throw new ValidationException(messageName + " cannot be <null>");
        }
        String schemaName = schemaNames.get(message.getClass());
        if (schemaName == null) {
            throw new ValidationException("Unsupported " + messageName + " type " + message.getClass().getName());
        }
        try {
            Source source = new JAXBSource(Xacml20Utils.JAXB_CONTEXT, message);
            XSD_VALIDATOR.validate(source, schemaName);
        } catch (JAXBException e) {
            throw new ValidationException(e);
        }
    }

    public void validateRequest(Object request) {
        validateMessage(request, "request", REQUEST_SCHEMAS);
    }

    public void validateResponse(Object response) {
        validateMessage(response, "response", RESPONSE_SCHEMAS);
    }

}
