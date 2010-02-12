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
package org.openehealth.ipf.commons.ihe.pixpdqv3.iti47;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Action;


@WebService(targetNamespace = "urn:ihe:iti:pdqv3:2007", name = "PDSupplier_PortType")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Iti47PortType {

    @WebResult(name = "PRPA_IN201306UV02", targetNamespace = "urn:hl7-org:v3", partName = "Body")
    @Action(input = "urn:hl7-org:v3:PRPA_IN201305UV02", output = "urn:hl7-org:v3:PRPA_IN201306UV02")
    @WebMethod(operationName = "PDSupplier_PRPA_IN201305UV02")
    public /*PRPAIN201306UV02*/ String pdSupplierPRPAIN201305UV02(
        @WebParam(partName = "Body", name = "PRPA_IN201305UV02", targetNamespace = "urn:hl7-org:v3")
        //PRPAIN201305UV02 body
        String body
    );

    @WebResult(name = "PRPA_IN201306UV02", targetNamespace = "urn:hl7-org:v3", partName = "Body")
    @Action(input = "urn:hl7-org:v3:QUQI_IN000003UV01_Continue", output = "urn:hl7-org:v3:PRPA_IN201306UV02")
    @WebMethod(operationName = "PDSupplier_QUQI_IN000003UV01_Continue")
    public /*PRPAIN201306UV02*/ String pdSupplierQUQIIN000003UV01Continue(
        @WebParam(partName = "Body", name = "QUQI_IN000003UV01", targetNamespace = "urn:hl7-org:v3")
        //QUQIIN000003UV01Type body
        String body
    );

    @WebResult(name = "MCCI_IN000002UV01", targetNamespace = "urn:hl7-org:v3", partName = "Body")
    @Action(input = "urn:hl7-org:v3:QUQI_IN000003UV01_Cancel", output = "urn:hl7-org:v3:MCCI_IN000002UV01")
    @WebMethod(operationName = "PDSupplier_QUQI_IN000003UV01_Cancel")
    public /*MCCIIN000002UV01*/ String pdSupplierQUQIIN000003UV01Cancel(
        @WebParam(partName = "Body", name = "QUQI_IN000003UV01_Cancel", targetNamespace = "urn:hl7-org:v3")
        //QUQIIN000003UV01Type body
        String body
    );
}
