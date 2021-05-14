package org.openehealth.ipf.commons.ihe.svs.core.audit;

import lombok.*;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

@Getter
@Setter
@ToString
public class SvsAuditDataset extends WsAuditDataset {

    private static final long serialVersionUID = -8950614248765744542L;

    private String valueSetId;

    private String valueSetName;

    private String valueSetVersion;

    public SvsAuditDataset(boolean serverSide) {
        super(serverSide);
    }
}
