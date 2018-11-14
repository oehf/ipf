/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xacml20;

import org.apache.commons.lang3.ArrayUtils;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.*;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLPolicyQueryType;
import org.openehealth.ipf.commons.xml.XsdValidator;

import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import java.util.HashMap;
import java.util.Map;

/**
 * @since 3.5.1
 * @author Dmytro Rud
 */
public class Xacml20MessageValidator {

    private static final XsdValidator XSD_VALIDATOR = new XsdValidator();

    private static final Map<Class<?>, String> REQUEST_SCHEMAS;
    static {
        REQUEST_SCHEMAS = new HashMap<>();
        REQUEST_SCHEMAS.put(XACMLPolicyQueryType.class, "schema/xacml-2.0-profile-saml2.0-v2-schema-protocol-wd-14.xsd");
        REQUEST_SCHEMAS.put(AddPolicyRequest.class,     "schema/epr-policy-administration-combined-schema-1.3-local.xsd");
        REQUEST_SCHEMAS.put(UpdatePolicyRequest.class,  "schema/epr-policy-administration-combined-schema-1.3-local.xsd");
        REQUEST_SCHEMAS.put(DeletePolicyRequest.class,  "schema/epr-policy-administration-combined-schema-1.3-local.xsd");
    }

    private static final Map<Class<?>, String> RESPONSE_SCHEMAS;
    static {
        RESPONSE_SCHEMAS = new HashMap<>();
        RESPONSE_SCHEMAS.put(ResponseType.class, "schema/PolicyQueryResponse.xsd");
        RESPONSE_SCHEMAS.put(EprPolicyRepositoryResponse.class, "schema/epr-policy-administration-combined-schema-1.3-local.xsd");
    }

    private static void validateMessage(Object message, Map<Class<?>, String> schemaNames, Class... allowedClasses) {
        if (message == null) {
            throw new ValidationException("Message cannot be <null>");
        }
        if (!ArrayUtils.contains(allowedClasses, message.getClass())) {
            throw new ValidationException("Unsupported message type " + message.getClass());
        }
        try {
            Source source = new JAXBSource(Xacml20Utils.JAXB_CONTEXT, message);
            XSD_VALIDATOR.validate(source, schemaNames.get(message.getClass()));
        } catch (JAXBException e) {
            throw new ValidationException(e);
        }
    }

    public static void validateChPpq1Request(Object request, Class... allowedClasses) {
        validateMessage(request, REQUEST_SCHEMAS, AddPolicyRequest.class, UpdatePolicyRequest.class, DeletePolicyRequest.class);
    }

    public static void validateChPpq2Request(Object request, Class... allowedClasses) {
        validateMessage(request, REQUEST_SCHEMAS, XACMLPolicyQueryType.class);
    }

    public static void validateChPpq1Response(Object request, Class... allowedClasses) {
        validateMessage(request, RESPONSE_SCHEMAS, EprPolicyRepositoryResponse.class);
    }

    public static void validateChPpq2Response(Object request, Class... allowedClasses) {
        validateMessage(request, RESPONSE_SCHEMAS, ResponseType.class);
    }

}
