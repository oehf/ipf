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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.iti56;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.cxf.interceptor.Fault;
import org.openehealth.ipf.commons.ihe.hl7v3.iti56.Iti56PortType;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.AbstractWebService;

/**
 * Service implementation for the IHE ITI-56 transaction (XCPD).
 * @author Dmytro Rud
 */
@Slf4j
public class Iti56Service extends AbstractWebService implements Iti56PortType {

    @Override
    public String locatePatients(String request) {
        Exchange result = process(request);
        if (result.getException() != null) {
            log.debug("ITI-56 service failed", result.getException());
            throw new Fault(result.getException());
        }
        
        return Exchanges.resultMessage(result).getBody(String.class);
    }

}
