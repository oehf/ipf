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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.modules.cda.CDAR2Validator;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;

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
            // XDS validation
            XdsCamelValidators.iti41RequestValidator().process(exchange);

            // HRN-specific validation
            ProvideAndRegisterDocumentSet request = exchange.getIn().getBody(ProvideAndRegisterDocumentSet.class);
            exchange.getIn().setBody(request);

            if (request.getDocuments().size() != 1) {
                throw new ValidationException("exactly one document must be provided in the HRN request");
            }

            // Content type enrichment: create byte array and MDHT CDA pojo
            Document document = request.getDocuments().get(0);
            document.getContent(byte[].class);
            ClinicalDocument ccd = document.getContent(ClinicalDocument.class);
            if (ccd == null) {
                throw new ValidationException("cannot convert document content to CCD");
            }

            new CDAR2Validator().validate(ccd, null);
        }
    };        
   

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
