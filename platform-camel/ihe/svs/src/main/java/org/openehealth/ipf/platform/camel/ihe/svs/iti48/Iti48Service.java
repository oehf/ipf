package org.openehealth.ipf.platform.camel.ihe.svs.iti48;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.svs.core.requests.RetrieveValueSetRequest;
import org.openehealth.ipf.commons.ihe.svs.core.responses.RetrieveValueSetResponse;
import org.openehealth.ipf.commons.ihe.svs.iti48.Iti48PortType;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;

/**
 * Service implementation for the IHE ITI-48 transaction (Retrieve Value Set).
 * <p>
 * This implementation delegates to a Camel consumer by creating an exchange.
 *
 * @author Quentin Ligier
 */
@Slf4j
public class Iti48Service extends AbstractWebService implements Iti48PortType {

    @Override
    public RetrieveValueSetResponse valueSetRepositoryRetrieveValueSet(final RetrieveValueSetRequest body) {
        final Exchange result = this.process(body);
        var exception = Exchanges.extractException(result);
        if (exception != null) {
            log.debug("Iti-48 service failed", exception);
            // TODO: SOAP faults
            return new RetrieveValueSetResponse();
        }
        return result.getMessage().getBody(RetrieveValueSetResponse.class);
    }
}
