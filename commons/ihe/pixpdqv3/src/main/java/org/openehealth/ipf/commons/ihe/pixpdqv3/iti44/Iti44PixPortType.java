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
package org.openehealth.ipf.commons.ihe.pixpdqv3.iti44;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Action;

/**
 * ITI-44 port type for PIX.
 */
@WebService(targetNamespace = "urn:ihe:iti:pixv3:2007", name = "PIXManager_PortType")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Iti44PixPortType {

    @WebResult(name = "MCCI_IN000002UV01", targetNamespace = "urn:hl7-org:v3", partName = "body")
    @Action(input = "urn:hl7-org:v3:PRPA_IN201301UV02", output = "urn:hl7-org:v3:MCCI_IN000002UV01")
    @WebMethod(operationName = "PIXManager_PRPA_IN201301UV02")
    public String pixManagerPRPAIN201301UV02(
        @WebParam(partName = "body", name = "PRPA_IN201301UV02", targetNamespace = "urn:hl7-org:v3")
        String body
    );

    @WebResult(name = "MCCI_IN000002UV01", targetNamespace = "urn:hl7-org:v3", partName = "body")
    @Action(input = "urn:hl7-org:v3:PRPA_IN201302UV02", output = "urn:hl7-org:v3:MCCI_IN000002UV01")
    @WebMethod(operationName = "PIXManager_PRPA_IN201302UV02")
    public String pixManagerPRPAIN201302UV02(
        @WebParam(partName = "body", name = "PRPA_IN201302UV02", targetNamespace = "urn:hl7-org:v3")
        String body
    );

    @WebResult(name = "MCCI_IN000002UV01", targetNamespace = "urn:hl7-org:v3", partName = "body")
    @Action(input = "urn:hl7-org:v3:PRPA_IN201304UV02", output = "urn:hl7-org:v3:MCCI_IN000002UV01")
    @WebMethod(operationName = "PIXManager_PRPA_IN201304UV02")
    public String pixManagerPRPAIN201304UV02(
        @WebParam(partName = "body", name = "PRPA_IN201304UV02", targetNamespace = "urn:hl7-org:v3")
        String body
    );
}
