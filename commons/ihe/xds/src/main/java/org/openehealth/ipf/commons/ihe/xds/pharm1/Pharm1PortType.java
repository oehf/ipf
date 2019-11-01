package org.openehealth.ipf.commons.ihe.xds.pharm1;

import org.apache.cxf.annotations.DataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.XdsJaxbDataBinding;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ObjectFactory;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;

@WebService(
    targetNamespace = "urn:ihe:iti:xds-b:2007",
    name = "CommunityPharmacyManager_PortType",
    portName = "CommunityPharmacyManager_Port_Soap12"
)
@XmlSeeAlso({ObjectFactory.class, org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.ObjectFactory.class, org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.ObjectFactory.class, org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.ObjectFactory.class})
@SOAPBinding(
    parameterStyle = SOAPBinding.ParameterStyle.BARE
)
@DataBinding(XdsJaxbDataBinding.class)
public interface Pharm1PortType {
    @WebResult(
        name = "AdhocQueryResponse",
        targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0",
        partName = "body"
    )
    @Action(
        input = "urn:ihe:pharm:cmpd:2010:QueryPharmacyDocuments",
        output = "urn:ihe:pharm:cmpd:2010:QueryPharmacyDocumentsResponse"
    )
    @WebMethod(
        operationName = "CommunityPharmacyManager_QueryPharmacyDocuments"
    )
    AdhocQueryResponse communityPharmacyManagerQueryPharmacyDocuments(@WebParam(partName = "body", name = "AdhocQueryRequest", targetNamespace = "urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0") AdhocQueryRequest var1);
}
