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

import static org.openehealth.ipf.commons.ihe.xds.core.validate.Actor.REPOSITORY;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.IheProfile.XDS_B;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLProvideAndRegisterDocumentSetRequest30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLRegistryResponse30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.ProvideAndRegisterDocumentSetRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.responses.RegistryResponseValidator;

/**
 * Validating processors for ebXML 3.0-based Continua HRN transacion.
 * 
 * @author Dmytro Rud
 * @author Stefan Ivanov
 */
abstract public class ContinuaHrnCamelValidators {
    
    private static final Processor HRN_REQUEST_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            EbXMLProvideAndRegisterDocumentSetRequest30 message = 
                new EbXMLProvideAndRegisterDocumentSetRequest30(exchange.getIn().getBody(ProvideAndRegisterDocumentSetRequestType.class));            
            ValidationProfile profile = new ValidationProfile(false, XDS_B, REPOSITORY);
            new ProvideAndRegisterDocumentSetRequestValidator().validate(message, profile);
        }
    };        
   
    private static final Processor HRN_RESPONSE_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            EbXMLRegistryResponse30 message = 
                new EbXMLRegistryResponse30(exchange.getIn().getBody(RegistryResponseType.class));            
            ValidationProfile profile = new ValidationProfile(false, XDS_B, REPOSITORY);
            new RegistryResponseValidator().validate(message, profile);
        }
    };
    
    /**
     * Returns a validating processor for Continua HRN request messages.
     */
    public static Processor continuaHrnRequestValidator() {
        return HRN_REQUEST_VALIDATOR;
    }
    
    /**
     * Returns a validating processor for Continua HRN response messages.
     */
    public static Processor continuaHrnResponseValidator() {
        return HRN_RESPONSE_VALIDATOR;
    }

}
