/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.*;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.AdhocQueryRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.ProvideAndRegisterDocumentSetRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.RetrieveDocumentSetRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.SubmitObjectsRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.responses.QueryResponseValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.responses.RegistryResponseValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.responses.RetrieveDocumentSetResponseValidator;

import static org.openehealth.ipf.commons.ihe.core.IpfInteractionId.*;
import static org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter.validationEnabled;

/**
 * Validating processors for ebXML 3.0-based and (by inheritance)
 * ebXML 2.1-based IPF XDS components.
 *  
 * @author Dmytro Rud
 */
abstract public class XdsCamelValidators extends XdsACamelValidators {
    
    private static final Processor ITI_18_REQUEST_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            if (! validationEnabled(exchange)) {
                return;
            }
            EbXMLAdhocQueryRequest30 message =
                new EbXMLAdhocQueryRequest30(exchange.getIn().getBody(AdhocQueryRequest.class));            
            ValidationProfile profile = new ValidationProfile(ITI_18);
            new AdhocQueryRequestValidator().validate(message, profile);
        }
    };        
   
    private static final Processor ITI_18_RESPONSE_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            if (! validationEnabled(exchange)) {
                return;
            }
            EbXMLQueryResponse30 message =
                new EbXMLQueryResponse30(exchange.getIn().getBody(AdhocQueryResponse.class));
            ValidationProfile profile = new ValidationProfile(ITI_18);
            new QueryResponseValidator().validate(message, profile);
        }
    };    
    
    private static final Processor ITI_38_REQUEST_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            if (! validationEnabled(exchange)) {
                return;
            }
            EbXMLAdhocQueryRequest30 message =
                new EbXMLAdhocQueryRequest30(exchange.getIn().getBody(AdhocQueryRequest.class));
            ValidationProfile profile = new ValidationProfile(ITI_38);
            new AdhocQueryRequestValidator().validate(message, profile);
        }
    };

    private static final Processor ITI_38_RESPONSE_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            if (! validationEnabled(exchange)) {
                return;
            }
            EbXMLQueryResponse30 message =
                new EbXMLQueryResponse30(exchange.getIn().getBody(AdhocQueryResponse.class));
            ValidationProfile profile = new ValidationProfile(ITI_38);
            new QueryResponseValidator().validate(message, profile);
        }
    };

    private static final Processor ITI_39_REQUEST_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            if (! validationEnabled(exchange)) {
                return;
            }
            EbXMLRetrieveDocumentSetRequest30 message =
                new EbXMLRetrieveDocumentSetRequest30(exchange.getIn().getBody(RetrieveDocumentSetRequestType.class));
            ValidationProfile profile = new ValidationProfile(ITI_39);
            new RetrieveDocumentSetRequestValidator().validate(message, profile);
        }
    };

    private static final Processor ITI_39_RESPONSE_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            if (! validationEnabled(exchange)) {
                return;
            }
            EbXMLRetrieveDocumentSetResponse30 message =
                new EbXMLRetrieveDocumentSetResponse30(exchange.getIn().getBody(RetrieveDocumentSetResponseType.class));
            ValidationProfile profile = new ValidationProfile(ITI_39);
            new RetrieveDocumentSetResponseValidator().validate(message, profile);
        }
    };

    private static final Processor ITI_41_REQUEST_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            if (! validationEnabled(exchange)) {
                return;
            }
            EbXMLProvideAndRegisterDocumentSetRequest30 message =
                new EbXMLProvideAndRegisterDocumentSetRequest30(exchange.getIn().getBody(ProvideAndRegisterDocumentSetRequestType.class));            
            ValidationProfile profile = new ValidationProfile(ITI_41);
            new ProvideAndRegisterDocumentSetRequestValidator().validate(message, profile);
        }
    };        
   
    private static final Processor ITI_41_RESPONSE_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            if (! validationEnabled(exchange)) {
                return;
            }
            EbXMLRegistryResponse30 message =
                new EbXMLRegistryResponse30(exchange.getIn().getBody(RegistryResponseType.class));            
            ValidationProfile profile = new ValidationProfile(ITI_41);
            new RegistryResponseValidator().validate(message, profile);
        }
    };    

    private static final Processor ITI_42_REQUEST_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            if (! validationEnabled(exchange)) {
                return;
            }
            EbXMLSubmitObjectsRequest30 message =
                new EbXMLSubmitObjectsRequest30(exchange.getIn().getBody(SubmitObjectsRequest.class));
            ValidationProfile profile = new ValidationProfile(ITI_42);
            new SubmitObjectsRequestValidator().validate(message, profile);
        }
    };
   
    private static final Processor ITI_42_RESPONSE_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            if (! validationEnabled(exchange)) {
                return;
            }
            EbXMLRegistryResponse30 message = new EbXMLRegistryResponse30(exchange.getIn().getBody(RegistryResponseType.class));
            ValidationProfile profile = new ValidationProfile(ITI_42);
            new RegistryResponseValidator().validate(message, profile);
        }
    };
    
    private static final Processor ITI_43_REQUEST_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            if (! validationEnabled(exchange)) {
                return;
            }
            EbXMLRetrieveDocumentSetRequest30 message =
                new EbXMLRetrieveDocumentSetRequest30(exchange.getIn().getBody(RetrieveDocumentSetRequestType.class));           
            ValidationProfile profile = new ValidationProfile(ITI_43);
            new RetrieveDocumentSetRequestValidator().validate(message, profile);
        }
    };       
   
    private static final Processor ITI_43_RESPONSE_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            if (! validationEnabled(exchange)) {
                return;
            }
            EbXMLRetrieveDocumentSetResponse30 message =
                new EbXMLRetrieveDocumentSetResponse30(exchange.getIn().getBody(RetrieveDocumentSetResponseType.class));            
            ValidationProfile profile = new ValidationProfile(ITI_43);
            new RetrieveDocumentSetResponseValidator().validate(message, profile);
        }
    };    

    private static final Processor ITI_61_REQUEST_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            if (! validationEnabled(exchange)) {
                return;
            }
            EbXMLSubmitObjectsRequest30 message =
                new EbXMLSubmitObjectsRequest30(exchange.getIn().getBody(SubmitObjectsRequest.class));
            ValidationProfile profile = new ValidationProfile(ITI_61);
            new SubmitObjectsRequestValidator().validate(message, profile);
        }
    };

    private static final Processor ITI_61_RESPONSE_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            if (! validationEnabled(exchange)) {
                return;
            }
            EbXMLRegistryResponse30 message = new EbXMLRegistryResponse30(exchange.getIn().getBody(RegistryResponseType.class));
            ValidationProfile profile = new ValidationProfile(ITI_61);
            new RegistryResponseValidator().validate(message, profile);
        }
    };

    private static final Processor ITI_63_REQUEST_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            if (! validationEnabled(exchange)) {
                return;
            }
            EbXMLAdhocQueryRequest30 message =
                new EbXMLAdhocQueryRequest30(exchange.getIn().getBody(AdhocQueryRequest.class));
            ValidationProfile profile = new ValidationProfile(ITI_63);
            new AdhocQueryRequestValidator().validate(message, profile);
        }
    };

    private static final Processor ITI_63_RESPONSE_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            if (! validationEnabled(exchange)) {
                return;
            }
            EbXMLQueryResponse30 message =
                new EbXMLQueryResponse30(exchange.getIn().getBody(AdhocQueryResponse.class));
            ValidationProfile profile = new ValidationProfile(ITI_63);
            new QueryResponseValidator().validate(message, profile);
        }
    };



    /**
     * Returns a validating processor for ITI-18 request messages.
     */
    public static Processor iti18RequestValidator() {
        return ITI_18_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-18 response messages.
     */
    public static Processor iti18ResponseValidator() {
        return ITI_18_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-38 request messages.
     */
    public static Processor iti38RequestValidator() {
        return ITI_38_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-38 response messages.
     */
    public static Processor iti38ResponseValidator() {
        return ITI_38_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-39 request messages.
     */
    public static Processor iti39RequestValidator() {
        return ITI_39_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-39 response messages.
     */
    public static Processor iti39ResponseValidator() {
        return ITI_39_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-41 request messages.
     */
    public static Processor iti41RequestValidator() {
        return ITI_41_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-41 response messages.
     */
    public static Processor iti41ResponseValidator() {
        return ITI_41_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-42 request messages.
     */
    public static Processor iti42RequestValidator() {
        return ITI_42_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-42 response messages.
     */
    public static Processor iti42ResponseValidator() {
        return ITI_42_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-43 request messages.
     */
    public static Processor iti43RequestValidator() {
        return ITI_43_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-43 response messages.
     */
    public static Processor iti43ResponseValidator() {
        return ITI_43_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-61 request messages.
     */
    public static Processor iti61RequestValidator() {
        return ITI_61_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-61 response messages.
     */
    public static Processor iti61ResponseValidator() {
        return ITI_61_RESPONSE_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-63 request messages.
     */
    public static Processor iti63RequestValidator() {
        return ITI_63_REQUEST_VALIDATOR;
    }

    /**
     * Returns a validating processor for ITI-63 response messages.
     */
    public static Processor iti63ResponseValidator() {
        return ITI_63_RESPONSE_VALIDATOR;
    }

}
