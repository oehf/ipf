package org.openehealth.ipf.commons.ihe.svs.iti48;

import org.openehealth.ipf.commons.ihe.svs.core.requests.RetrieveValueSetRequest;
import org.openehealth.ipf.commons.ihe.svs.core.responses.RetrieveValueSetResponse;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Action;

/**
 * Provides the ITI-48 web-service interface (SOAP).
 */
@WebService(targetNamespace = "urn:ihe:iti:svs:2008", name = "ValueSetRepository_PortType", portName = "ValueSetRepository_Port_Soap12")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface Iti48PortType {

    /**
     * Performs a stored query according to the ITI-48 specification.
     *
     * @param body The query request.
     * @return the query response.
     */
    @WebResult(name = "RetrieveValueSetResponse",
            targetNamespace = "urn:ihe:iti:svs:2008",
            partName = "body")
    @Action(input = "urn:ihe:iti:svs:2008:RetrieveValueSet",
            output = "urn:ihe:iti:svs:2008:RetrieveValueSetResponse")
    @WebMethod(operationName = "ValueSetRepository_RetrieveValueSet")
    RetrieveValueSetResponse valueSetRepositoryRetrieveValueSet(
            @WebParam(partName = "body",
                    name = "RetrieveValueSetRequest",
                    targetNamespace = "urn:ihe:iti:svs:2008")
                    final RetrieveValueSetRequest body
    );
}
