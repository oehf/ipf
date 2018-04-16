package org.openehealth.ipf.platform.camel.ihe.xacml20;

import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20MessageValidator;

import static org.openehealth.ipf.platform.camel.core.adapter.ValidatorAdapter.validationEnabled;

/**
 * @author Dmytro Rud
 */
public class Xacml20CamelValidators {

    private static final Processor CH_PPQ_REQUEST_VALIDATOR = exchange -> {
        if (validationEnabled(exchange)) {
            new Xacml20MessageValidator().validateRequest(exchange.getIn().getBody());
        }
    };

    private static final Processor CH_PPQ_RESPONSE_VALIDATOR = exchange -> {
        if (validationEnabled(exchange)) {
            new Xacml20MessageValidator().validateResponse(exchange.getIn().getBody());
        }
    };

    public static Processor chPpqRequestValidator() {
        return CH_PPQ_REQUEST_VALIDATOR;
    }

    public static Processor chPpqResponseValidator() {
        return CH_PPQ_RESPONSE_VALIDATOR;
    }

}
