package org.openehealth.ipf.commons.ihe.svs.iti48;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.svs.core.audit.SvsAuditDataset;
import org.openehealth.ipf.commons.ihe.svs.core.audit.SvsEventTypeCode;
import org.openehealth.ipf.commons.ihe.svs.core.audit.SvsQueryInformationBuilder;
import org.openehealth.ipf.commons.ihe.svs.core.requests.RetrieveValueSetRequest;

import java.util.Map;

public class Iti48AuditStrategy extends AuditStrategySupport<SvsAuditDataset> {

    /**
     * @param serverSide <code>true</code> when this strategy is a server-side one;
     *                   <code>false</code> otherwise.
     */
    public Iti48AuditStrategy(final boolean serverSide) {
        super(serverSide);
    }

    /**
     * Creates a new audit dataset instance.
     */
    @Override
    public SvsAuditDataset createAuditDataset() {
        return new SvsAuditDataset(this.isServerSide());
    }

    /**
     * Enriches the given audit dataset with transaction-specific
     * contents of the request message and Camel exchange.
     *
     * @param auditDataset audit dataset to be enriched.
     * @param request      {@link Object} representing the request.
     * @param parameters   additional parameters
     */
    @Override
    public SvsAuditDataset enrichAuditDatasetFromRequest(final SvsAuditDataset auditDataset,
                                                         final Object request,
                                                         final Map<String, Object> parameters) {
        if (request instanceof RetrieveValueSetRequest) {
            final RetrieveValueSetRequest rvsRequest = (RetrieveValueSetRequest) request;
            auditDataset.setValueSetId(rvsRequest.getValueSet().getId());
            //auditDataset.setValueSetName(rvsRequest.getValueSet().ge);
        }
        return auditDataset;
    }

    /**
     * Enriches the given audit dataset with transaction-specific contents of the response message.
     *
     * @param auditDataset audit dataset to be enriched.
     * @param response     {@link Object} representing the responded resource.
     * @return true if response indicates success, false otherwise
     */
    @Override
    public boolean enrichAuditDatasetFromResponse(final SvsAuditDataset auditDataset,
                                                  final Object response) {
        System.out.println("enrichAuditDatasetFromResponse");
        return super.enrichAuditDatasetFromResponse(auditDataset, response);
    }

    /**
     * Constructs an {@link AuditMessage} from a provided {@link SvsAuditDataset}
     *
     * @param auditContext audit context
     * @param auditDataset audit dataset
     * @return audit messages
     */
    @Override
    public AuditMessage[] makeAuditMessage(final AuditContext auditContext,
                                           final SvsAuditDataset auditDataset) {
        return new SvsQueryInformationBuilder(auditContext, auditDataset, SvsEventTypeCode.RetrieveValueSet,
                auditDataset.getPurposesOfUse())
                .getMessages();
    }
}
