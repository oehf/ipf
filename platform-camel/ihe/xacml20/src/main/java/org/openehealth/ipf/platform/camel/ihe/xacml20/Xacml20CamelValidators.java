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
package org.openehealth.ipf.platform.camel.ihe.xacml20;

import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20MessageValidator;
import org.openehealth.ipf.commons.ihe.xacml20.stub.ehealthswiss.EprPolicyRepositoryResponse;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLAuthzDecisionQueryType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.protocol.XACMLPolicyQueryType;

import static org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter.validationEnabled;

/**
 * @since 3.5.1
 * @author Dmytro Rud
 */
public class Xacml20CamelValidators {

    private static final Processor CH_PPQ_1_REQUEST_VALIDATOR = exchange -> {
        if (validationEnabled(exchange)) {
            Xacml20MessageValidator.validateChPpq1Request(exchange.getIn().getMandatoryBody());
        }
    };

    private static final Processor CH_PPQ_1_RESPONSE_VALIDATOR = exchange -> {
        if (validationEnabled(exchange)) {
            Xacml20MessageValidator.validateChPpq1Response(exchange.getIn().getMandatoryBody(EprPolicyRepositoryResponse.class));
        }
    };

    private static final Processor CH_PPQ_2_REQUEST_VALIDATOR = exchange -> {
        if (validationEnabled(exchange)) {
            Xacml20MessageValidator.validateChPpq2Request(exchange.getIn().getMandatoryBody(XACMLPolicyQueryType.class));
        }
    };

    private static final Processor ITI_79_REQUEST_VALIDATOR = exchange -> {
        if (validationEnabled(exchange)) {
            Xacml20MessageValidator.validateIti79Request(exchange.getIn().getMandatoryBody(XACMLAuthzDecisionQueryType.class));
        }
    };

    private static final Processor CH_ADR_REQUEST_VALIDATOR = exchange -> {
        if (validationEnabled(exchange)) {
            Xacml20MessageValidator.validateChAdrRequest(exchange.getIn().getMandatoryBody(XACMLAuthzDecisionQueryType.class));
        }
    };

    private static final Processor QUERY_RESPONSE_VALIDATOR = exchange -> {
        if (validationEnabled(exchange)) {
            Xacml20MessageValidator.validateQueryResponse(exchange.getIn().getMandatoryBody(ResponseType.class));
        }
    };

    public static Processor chPpq1RequestValidator() {
        return CH_PPQ_1_REQUEST_VALIDATOR;
    }

    public static Processor chPpq1ResponseValidator() {
        return CH_PPQ_1_RESPONSE_VALIDATOR;
    }

    public static Processor chPpq2RequestValidator() {
        return CH_PPQ_2_REQUEST_VALIDATOR;
    }

    public static Processor chPpq2ResponseValidator() {
        return QUERY_RESPONSE_VALIDATOR;
    }

    public static Processor iti79RequestValidator() {
        return ITI_79_REQUEST_VALIDATOR;
    }

    public static Processor iti79ResponseValidator() {
        return QUERY_RESPONSE_VALIDATOR;
    }

    public static Processor chAdrRequestValidator() {
        return CH_ADR_REQUEST_VALIDATOR;
    }

    public static Processor chAdrResponseValidator() {
        return QUERY_RESPONSE_VALIDATOR;
    }

}
