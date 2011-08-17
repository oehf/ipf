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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti55;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Exception;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3NakFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.iti55.Iti55PortType;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

/**
 * Service implementation for the IHE ITI-55 transaction (XCPD).
 * @author Dmytro Rud
 */
public class Iti55Service extends DefaultItiWebService implements Iti55PortType {

    @Override
    public String respondingGatewayPRPAIN201305UV02(String request) {
        try {
            Exchange result = process(request, null, ExchangePattern.InOut);
            if(result.getException() != null) {
                throw result.getException();
            }
            return Exchanges.resultMessage(result).getBody(String.class);
        } catch (Exception e) {
            Hl7v3Exception hl7v3Exception = new Hl7v3Exception(e, null);
            hl7v3Exception.setDetectedIssueManagementCode("InternalError");
            hl7v3Exception.setDetectedIssueManagementCodeSystem("1.3.6.1.4.1.19376.1.2.27.3");
            return Hl7v3NakFactory.createNak(request, hl7v3Exception, "PRPA_IN201306UV02", true);
        }
    }

}
