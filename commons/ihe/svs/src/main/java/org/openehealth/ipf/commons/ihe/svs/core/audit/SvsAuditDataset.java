package org.openehealth.ipf.commons.ihe.svs.core.audit;

import lombok.*;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

/**
 * The audit dataset for SVS transactions.
 *
 * @author Quentin Ligier
 */
@Getter
@Setter
@ToString(callSuper = true)
public class SvsAuditDataset extends WsAuditDataset {

    private static final long serialVersionUID = -8950614248765744542L;

    /**
     * The retrieved value set ID (OID).
     */
    private String valueSetId;

    /**
     * The retrieved value set display name, if any.
     */
    private String valueSetName;

    /**
     * The retrieved value set version, if any.
     */
    private String valueSetVersion;

    public SvsAuditDataset(boolean serverSide) {
        super(serverSide);
        this.setSourceUserIsRequestor(false);
    }
}
