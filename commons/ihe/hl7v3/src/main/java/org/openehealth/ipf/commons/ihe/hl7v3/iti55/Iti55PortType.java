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
package org.openehealth.ipf.commons.ihe.hl7v3.iti55;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.xml.ws.Action;
 
/**
 * SEI for XCPD ITI-55 -- Cross Gateway Patient Discovery.
 * @author Dmytro Rud
 */
@WebService(targetNamespace = "urn:ihe:iti:xcpd:2009", name = "RespondingGateway_PortType", portName = "RespondingGateway_Port_Soap12")
@SOAPBinding(style = Style.DOCUMENT, parameterStyle = ParameterStyle.BARE)
public interface Iti55PortType {
    public static final String REGULAR_REQUEST_INPUT_ACTION =
            "urn:hl7-org:v3:PRPA_IN201305UV02:CrossGatewayPatientDiscovery";
    public static final String REGULAR_REQUEST_OUTPUT_ACTION =
            "urn:hl7-org:v3:PRPA_IN201306UV02:CrossGatewayPatientDiscovery";
    public static final String DEFERRED_REQUEST_INPUT_ACTION =
            "urn:hl7-org:v3:PRPA_IN201305UV02:Deferred:CrossGatewayPatientDiscovery";
    public static final String DEFERRED_REQUEST_OUTPUT_ACTION =
            "urn:hl7-org:v3:MCCI_IN000002UV01";

    @Action(input = REGULAR_REQUEST_INPUT_ACTION, output = REGULAR_REQUEST_OUTPUT_ACTION)
    @WebMethod(operationName = "RespondingGateway_PRPA_IN201305UV02")
    String discoverPatients(
        @WebParam(partName = "Body", targetNamespace = "urn:ihe:iti:xcpd:2009")
        String request
    );

    @Action(input = DEFERRED_REQUEST_INPUT_ACTION, output = DEFERRED_REQUEST_OUTPUT_ACTION)
    @WebMethod(operationName = "RespondingGateway_Deferred_PRPA_IN201305UV02")
    String discoverPatientsDeferred(
        @WebParam(partName = "Body", targetNamespace = "urn:ihe:iti:xcpd:2009")
        String request
    );
}
