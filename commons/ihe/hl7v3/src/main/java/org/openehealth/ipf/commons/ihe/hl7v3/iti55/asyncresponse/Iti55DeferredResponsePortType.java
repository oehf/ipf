/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3.iti55.asyncresponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.Action;

/**
 * SEI for the ITI-55 XCPD Initiating Gateway actor: receiver of Deferred responses.
 * @author Dmytro Rud
 */
@WebService(targetNamespace = "urn:ihe:iti:xcpd:2009", name = "InitiatingGatewayDeferredResponse_PortType")
@SOAPBinding(style = Style.DOCUMENT, parameterStyle = ParameterStyle.BARE)
public interface Iti55DeferredResponsePortType {
    public static final String DEFERRED_RESPONSE_INPUT_ACTION =
            "urn:hl7-org:v3:PRPA_IN201306UV02:Deferred:CrossGatewayPatientDiscovery";
    public static final String DEFERRED_RESPONSE_OUTPUT_ACTION =
            "urn:hl7-org:v3:MCCI_IN000002UV01";

    @Action(input = DEFERRED_RESPONSE_INPUT_ACTION, output = DEFERRED_RESPONSE_OUTPUT_ACTION)
    @WebMethod(operationName = "InitiatingGateway_Deferred_PRPA_IN201306UV02")
    Object receiveDeferredResponse(
            @WebParam(partName = "Body", targetNamespace = "urn:ihe:iti:xcpd:2009")
            Object response
    );
}
