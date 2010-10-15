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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3;

import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3NakFactory;
import org.openehealth.ipf.platform.camel.core.util.Exchanges;
import org.openehealth.ipf.platform.camel.ihe.ws.DefaultItiWebService;

/**
 * Generic Web Service implementation for HL7 v3-based transactions.
 * @author Dmytro Rud
 */
public class DefaultHl7v3WebService extends DefaultItiWebService {

    private final String nakRootElementName;
    private final boolean nakNeedControlActProcess;
    
    /**
     * @param nakRootElementName
     *      root element name of NAK messages. 
     * @param nakNeedControlActProcess
     *      whether the <tt>controlActProcess</tt> element 
     *      should be generated in NAK messages.
     */
    public DefaultHl7v3WebService(String nakRootElementName, boolean nakNeedControlActProcess) {
        this.nakRootElementName = nakRootElementName;
        this.nakNeedControlActProcess = nakNeedControlActProcess;
    }
    
    /**
     * The proper message processing method.
     * @param request
     *      XML payload of the HL7 v3 request message.
     * @return
     *      XML payload of the HL7 v3 response message or an automatically generated NAK.
     */
    protected String doProcess(String request) {
        Exchange result = process(request);
        if(result.getException() != null) {
            return Hl7v3NakFactory.createNak(request, result.getException(), 
                    this.nakRootElementName, this.nakNeedControlActProcess);
        }
        return Exchanges.resultMessage(result).getBody(String.class);
    }


    public String getNakRootElementName() {
        return nakRootElementName;
    }

    public boolean isNakNeedControlActProcess() {
        return nakNeedControlActProcess;
    }

}
