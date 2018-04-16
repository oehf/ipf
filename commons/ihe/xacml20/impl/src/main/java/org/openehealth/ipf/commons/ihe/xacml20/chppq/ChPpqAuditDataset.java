package org.openehealth.ipf.commons.ihe.xacml20.chppq;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
//import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmytro Rud
 */
public class ChPpqAuditDataset extends WsAuditDataset {

    //@Getter @Setter private RFC3881EventCodes.RFC3881EventActionCodes action;
    @Getter @Setter private String queryId;
    @Getter @Setter private String patientId;
    @Getter private final List<String> policyAndPolicySetIds = new ArrayList<>();


    public ChPpqAuditDataset(boolean serverSide) {
        super(serverSide);
    }
}
