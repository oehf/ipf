/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.continua.hrn;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.ihe.core.IpfInteractionId;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLProvideAndRegisterDocumentSetRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.ProvideAndRegisterDocumentSetRequestValidator;
import org.openehealth.ipf.commons.xml.CombinedXmlValidationProfile;
import org.openehealth.ipf.commons.xml.CombinedXmlValidator;
import org.openehealth.ipf.modules.cda.CDAR2Constants;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.Map;

import static org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter.validationEnabled;

/**
 * Validating and transformation processors for the Continua HRN transaction.
 * 
 * @author Dmytro Rud
 * @author Stefan Ivanov
 */
abstract public class ContinuaHrnCamelProcessors {

    private static final Processor HRN_REQUEST_TRANSFORMER_AND_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            boolean validationEnabled = validationEnabled(exchange);

            // ebXML validation
            if (validationEnabled) {
                EbXMLProvideAndRegisterDocumentSetRequest30 message =
                    new EbXMLProvideAndRegisterDocumentSetRequest30(exchange.getIn().getBody(ProvideAndRegisterDocumentSetRequestType.class));
                ValidationProfile profile = new ValidationProfile(IpfInteractionId.Continua_HRN);
                new ProvideAndRegisterDocumentSetRequestValidator().validate(message, profile);
            }

            // transform ebXML into simplified model, extract embedded documents, check document count
            ProvideAndRegisterDocumentSet request = exchange.getIn().getBody(ProvideAndRegisterDocumentSet.class);
            exchange.getIn().setBody(request);

            if (validationEnabled) {
                if (request.getDocuments().size() != 1) {
                    throw new ValidationException("exactly one document must be provided in the HRN request");
                }
            }

            // Document content type enrichment: create byte array and String
            Document document = request.getDocuments().get(0);
            byte[] documentBytes = document.getContent(byte[].class);
            String documentString = document.getContent(String.class);

            // PHMR-specific validation
            if (validationEnabled) {
                new CombinedXmlValidator().validate(documentString, new PhmrValidationProfile());
            }
        }
    };


    /**
     * Converts the given byte array to a Source object.
     */
    private static Source getSource(byte[] bytes) {
        return new StreamSource(new ByteArrayInputStream(bytes));
    }


    /**
     * Returns a transformation & validation processor for Continua HRN request messages.
     * The HRN request contained in the exchange will be translated into IPF simplified
     * XDS message model and enriched with CDA document content.
     */
    public static Processor continuaHrnRequestTransformerAndValidator() {
        return HRN_REQUEST_TRANSFORMER_AND_VALIDATOR;
    }
    
    /**
     * Returns a validating processor for Continua HRN response messages.
     * Actually there is no differences to ITI-41 response message validation.
     */
    public static Processor continuaHrnResponseValidator() {
        return XdsCamelValidators.iti41ResponseValidator();
    }



    private static class PhmrValidationProfile implements CombinedXmlValidationProfile {
        @Override
        public boolean isValidRootElement(String rootElementName) {
            return "ClinicalDocument".equals(rootElementName);
        }

        @Override
        public String getXsdPath(String rootElementName) {
            return CDAR2Constants.CDAR2_SCHEMA;
        }

        @Override
        public String getSchematronPath(String rootElementName) {
            return CDAR2Constants.CDA_PHMR_SCHEMATRON_RULES;
        }

        @Override
        public Map<String, Object> getCustomSchematronParameters() {
            return Collections.<String, Object>singletonMap("phase", "errors");
        }
    }
}
