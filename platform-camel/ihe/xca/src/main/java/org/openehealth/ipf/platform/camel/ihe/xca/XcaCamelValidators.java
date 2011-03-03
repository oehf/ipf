/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xca;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.*;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationProfile;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.AdhocQueryRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.requests.RetrieveDocumentSetRequestValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.responses.QueryResponseValidator;
import org.openehealth.ipf.commons.ihe.xds.core.validate.responses.RetrieveDocumentSetResponseValidator;

import static org.openehealth.ipf.commons.ihe.xds.core.validate.Actor.REGISTRY;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.Actor.REPOSITORY;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.IheProfile.XCA;

/**
 * Validating processors for IPF XCA components.
 * @author Dmytro Rud
 */
public class XcaCamelValidators {

    private static final Processor ITI_38_REQUEST_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            EbXMLAdhocQueryRequest30 message =
                new EbXMLAdhocQueryRequest30(exchange.getIn().getBody(AdhocQueryRequest.class));
            ValidationProfile profile = new ValidationProfile(true, XCA, REGISTRY);
            new AdhocQueryRequestValidator().validate(message, profile);
        }
    };

    private static final Processor ITI_38_RESPONSE_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            EbXMLQueryResponse30 message =
                new EbXMLQueryResponse30(exchange.getIn().getBody(AdhocQueryResponse.class));
            ValidationProfile profile = new ValidationProfile(true, XCA, REGISTRY);
            new QueryResponseValidator().validate(message, profile);
        }
    };

    private static final Processor ITI_39_REQUEST_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            EbXMLRetrieveDocumentSetRequest30 message =
                new EbXMLRetrieveDocumentSetRequest30(exchange.getIn().getBody(RetrieveDocumentSetRequestType.class));
            ValidationProfile profile = new ValidationProfile(false, XCA, REPOSITORY);
            new RetrieveDocumentSetRequestValidator().validate(message, profile);
        }
    };

    private static final Processor ITI_39_RESPONSE_VALIDATOR = new Processor() {
        @Override
        public void process(Exchange exchange) throws Exception {
            EbXMLRetrieveDocumentSetResponse30 message =
                new EbXMLRetrieveDocumentSetResponse30(exchange.getIn().getBody(RetrieveDocumentSetResponseType.class));
            ValidationProfile profile = new ValidationProfile(false, XCA, REPOSITORY);
            new RetrieveDocumentSetResponseValidator().validate(message, profile);
        }
    };


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
}
