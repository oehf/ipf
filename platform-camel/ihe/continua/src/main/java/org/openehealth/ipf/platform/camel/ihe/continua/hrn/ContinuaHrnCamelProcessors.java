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
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLProvideAndRegisterDocumentSetRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.ProvideAndRegisterDocumentSetRequestValidator;
import org.openehealth.ipf.commons.xml.SchematronProfile;
import org.openehealth.ipf.commons.xml.SchematronValidator;
import org.openehealth.ipf.commons.xml.XsdValidator;
import org.openehealth.ipf.modules.cda.CDAR2Constants;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.util.Collections;

import static org.openehealth.ipf.commons.ihe.xds.core.validate.Actor.REPOSITORY;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.IheProfile.ContinuaHRN;

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
            // ebXML validation
            EbXMLProvideAndRegisterDocumentSetRequest30 message =
                new EbXMLProvideAndRegisterDocumentSetRequest30(exchange.getIn().getBody(ProvideAndRegisterDocumentSetRequestType.class));
            ValidationProfile profile = new ValidationProfile(false, ContinuaHRN, REPOSITORY);
            new ProvideAndRegisterDocumentSetRequestValidator().validate(message, profile);

            // transform ebXML into simplified model, extract embedded documents, check document count
            ProvideAndRegisterDocumentSet request = exchange.getIn().getBody(ProvideAndRegisterDocumentSet.class);
            exchange.getIn().setBody(request);

            if (request.getDocuments().size() != 1) {
                throw new ValidationException("exactly one document must be provided in the HRN request");
            }

            // Document content type enrichment: create byte array and String
            Document document = request.getDocuments().get(0);
            byte[] bytes = document.getContent(byte[].class);

            // perform PHMR-specific XML Schema and Schematron validations
            new XsdValidator().validate(getSource(bytes), CDAR2Constants.CDAR2_SCHEMA);
            new SchematronValidator().validate(getSource(bytes),
                    new SchematronProfile(CDAR2Constants.CDA_PHMR_SCHEMATRON_RULES,
                            Collections.<String, Object> singletonMap("phase", "errors")));
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

}
