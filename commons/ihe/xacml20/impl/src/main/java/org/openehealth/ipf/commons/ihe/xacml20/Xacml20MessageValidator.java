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

import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.ihe.xacml20.chppq1.ChPpq1RequestValidationProfile;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.EprPolicyRepositoryResponse;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLPolicyQueryType;
import org.openehealth.ipf.commons.xml.CombinedXmlValidator;
import org.openehealth.ipf.commons.xml.XmlUtils;
import org.openehealth.ipf.commons.xml.XsdValidator;

import javax.xml.bind.JAXBException;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Source;
import java.util.Map;

/**
 * @since 3.5.1
 * @author Dmytro Rud
 */
public class Xacml20MessageValidator {

    private static final XsdValidator XSD_VALIDATOR = new XsdValidator();
    private static final CombinedXmlValidator COMBINED_XML_VALIDATOR = new CombinedXmlValidator();

    private static final Map<Class<?>, String> XML_SCHEMAS = Map.of(
            XACMLPolicyQueryType.class, "schema/xacml-2.0-profile-saml2.0-v2-schema-protocol-wd-14.xsd",
            ResponseType.class, "schema/PolicyQueryResponse.xsd",
            EprPolicyRepositoryResponse.class, "schema/epr-policy-administration-combined-schema-1.3-local.xsd");

    private static void validateMessage(Object message) {
        try {
            Source source = new JAXBSource(Xacml20Utils.JAXB_CONTEXT, message);
            XSD_VALIDATOR.validate(source, XML_SCHEMAS.get(message.getClass()));
        } catch (JAXBException e) {
            throw new ValidationException(e);
        }
    }

    public static void validateChPpq1Request(Object request) {
        String s = XmlUtils.renderJaxb(Xacml20Utils.JAXB_CONTEXT, request, false);
        COMBINED_XML_VALIDATOR.validate(s, new ChPpq1RequestValidationProfile());
    }

    public static void validateChPpq2Request(XACMLPolicyQueryType message) {
        validateMessage(message);
    }

    public static void validateChPpq1Response(EprPolicyRepositoryResponse message) {
        validateMessage(message);
    }

    public static void validateChPpq2Response(ResponseType message) {
        validateMessage(message);
    }

}
