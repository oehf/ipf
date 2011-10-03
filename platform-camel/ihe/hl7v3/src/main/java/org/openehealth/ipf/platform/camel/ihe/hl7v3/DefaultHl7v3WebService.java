/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3;

import groovy.util.slurpersupport.GPathResult;
import org.apache.camel.Exchange;
import org.apache.commons.lang3.Validate;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3NakFactory;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ServiceInfo;
import org.openehealth.ipf.commons.xml.XmlUtils;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

/**
 * Generic Web Service implementation for HL7 v3-based transactions.
 * @author Dmytro Rud
 */
public class DefaultHl7v3WebService extends DefaultItiWebService {

    private final Hl7v3ServiceInfo serviceInfo;

    public DefaultHl7v3WebService(Hl7v3ServiceInfo serviceInfo) {
        Validate.notNull(serviceInfo);
        this.serviceInfo = serviceInfo;
    }
    
    /**
     * The proper message processing method.
     * @param request
     *      XML payload of the HL7 v3 request message.
     * @return
     *      XML payload of the HL7 v3 response message or an automatically generated NAK.
     */
    protected Object doProcess(Object request) {
        Exchange result = process(request);
        if(result.getException() != null) {
            return Hl7v3NakFactory.createNak(XmlUtils.toString(request, null), result.getException(),
                    serviceInfo.getNakRootElementName(),
                    serviceInfo.isNakNeedControlActProcess());
        }
        return prepareBody(result);
    }

    /**
     * Creates a transaction-specific NAK message.
     */
    protected String createNak(String requestString, Throwable t) {
        return Hl7v3NakFactory.createNak(requestString, t,
                serviceInfo.getNakRootElementName(),
                serviceInfo.isNakNeedControlActProcess());
    }

    /**
     * Creates a transaction-specific NAK message.
     */
    protected String createNak(GPathResult request, Throwable t) {
        return Hl7v3NakFactory.createNak(request, t,
                serviceInfo.getNakRootElementName(),
                serviceInfo.isNakNeedControlActProcess());
    }

    public Hl7v3ServiceInfo getServiceInfo() {
        return serviceInfo;
    }
}
