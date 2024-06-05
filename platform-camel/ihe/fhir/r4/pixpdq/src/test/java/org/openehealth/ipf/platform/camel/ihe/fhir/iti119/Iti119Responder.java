package org.openehealth.ipf.platform.camel.ihe.fhir.iti119;

import org.apache.camel.Exchange;
import org.apache.camel.support.ExpressionAdapter;
import org.hl7.fhir.r4.model.Parameters;

public class Iti119Responder extends ExpressionAdapter {

    private final ResponseCase responseCase;

    public Iti119Responder(ResponseCase responseCase) {
        this.responseCase = responseCase;
    }

    @Override
    public Object evaluate(Exchange exchange) {
        Parameters request = exchange.getIn().getBody(Parameters.class);
        return responseCase.populateResponse(request);
    }
}
