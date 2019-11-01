package org.openehealth.ipf.platform.camel.ihe.xds.pharm1;

import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.pharm1.Pharm1PortType;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsAdhocQueryService;

public class Pharm1Service extends XdsAdhocQueryService implements Pharm1PortType {
    public Pharm1Service() {
        super((String)null);
    }

    // TODO
    public AdhocQueryResponse communityPharmacyManagerQueryPharmacyDocuments(AdhocQueryRequest body) {
        return this.processRequest(body);
    }
}
