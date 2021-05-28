/*
 * Copyright 2021 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.svs.iti48;

import org.openehealth.ipf.commons.audit.AuditContext;
import org.openehealth.ipf.commons.audit.codes.*;
import org.openehealth.ipf.commons.audit.event.BaseAuditMessageBuilder;
import org.openehealth.ipf.commons.audit.model.AuditMessage;
import org.openehealth.ipf.commons.ihe.core.atna.AuditStrategySupport;
import org.openehealth.ipf.commons.ihe.svs.core.audit.SvsAuditDataset;
import org.openehealth.ipf.commons.ihe.svs.core.audit.SvsEventTypeCode;
import org.openehealth.ipf.commons.ihe.svs.core.requests.RetrieveValueSetRequest;
import org.openehealth.ipf.commons.ihe.svs.core.responses.RetrieveValueSetResponse;

import java.util.Map;

import static org.openehealth.ipf.commons.audit.utils.AuditUtils.getHostFromUrl;
import static org.openehealth.ipf.commons.audit.utils.AuditUtils.getProcessId;

/**
 * Audit strategy for ITI-48.
 *
 * @author Quentin Ligier
 */
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
            var rvsRequest = (RetrieveValueSetRequest) request;
            auditDataset.setValueSetId(rvsRequest.getValueSet().getId());
            auditDataset.setValueSetVersion(rvsRequest.getValueSet().getVersion());
        }
        return auditDataset;
    }

    /**
     * Enriches the given audit dataset with transaction-specific contents of the response message.
     *
     * @param auditDataset audit dataset to be enriched.
     * @param response     {@link Object} representing the responded resource.
     * @param auditContext audit context, if relevant.
     * @return true if response indicates success, false otherwise
     */
    @Override
    public boolean enrichAuditDatasetFromResponse(final SvsAuditDataset auditDataset,
                                                  final Object response,
                                                  final AuditContext auditContext) {
        if (response instanceof RetrieveValueSetResponse) {
            var rvsResponse = (RetrieveValueSetResponse) response;
            auditDataset.setValueSetId(rvsResponse.getValueSet().getId());
            auditDataset.setValueSetVersion(rvsResponse.getValueSet().getVersion());
            auditDataset.setValueSetName(rvsResponse.getValueSet().getDisplayName());
            auditDataset.setEventOutcomeIndicator(EventOutcomeIndicator.Success);
        }
        return true;
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
        var builder = new SvsAuditMessageBuilder();
        builder.setEventIdentification(
                auditDataset.getEventOutcomeIndicator(),
                auditDataset.getEventOutcomeDescription(),
                auditDataset.isServerSide() ? EventActionCode.Read : EventActionCode.Create,
                auditDataset.isServerSide() ? EventIdCode.Export : EventIdCode.Import,
                SvsEventTypeCode.RetrieveValueSet,
                auditDataset.getPurposesOfUse()
        );

        if (auditDataset.isServerSide()) {
            // Because the source user is not the requester, we have to invert the source and destination
            builder.addSourceActiveParticipant(
                    auditDataset.getDestinationUserId(),
                    getProcessId(),
                    null,
                    auditDataset.getLocalAddress(),
                    auditDataset.isSourceUserIsRequestor()
            );
            builder.addDestinationActiveParticipant(
                    auditDataset.getSourceUserId(),
                    auditDataset.getSourceUserName(),
                    null,
                    auditDataset.getRemoteAddress(),
                    !auditDataset.isSourceUserIsRequestor()
            );
        } else {
            builder.addSourceActiveParticipant(
                    auditDataset.getSourceUserId(),
                    getProcessId(),
                    null,
                    auditDataset.getLocalAddress(),
                    auditDataset.isSourceUserIsRequestor()
            );
            builder.addDestinationActiveParticipant(
                    auditDataset.getDestinationUserId(),
                    null,
                    null,
                    auditDataset.getRemoteAddress(),
                    !auditDataset.isSourceUserIsRequestor()
            );
        }
        for (var humanUser : auditDataset.getHumanUsers()) {
            if (!humanUser.isEmpty()) {
                builder.addActiveParticipant(
                        humanUser.getId() != null ?
                                humanUser.getId() :
                                auditContext.getAuditValueIfMissing(),
                        null,
                        humanUser.getName(),
                        false,
                        humanUser.getRoles(),
                        null);
            }
        }
        if (auditContext != null) {
            builder.setAuditSource(auditContext);
        }
        if (auditDataset.getValueSetId() != null) {
            builder.addParticipantObjectIdentification(
                    ParticipantObjectIdTypeCode.SearchCriteria,
                    auditDataset.getValueSetName(),
                    null,
                    null, // Spec issue: this should contain the value set version
                    auditDataset.getValueSetId(),
                    ParticipantObjectTypeCode.System,
                    ParticipantObjectTypeCodeRole.Report,
                    null,
                    null
            );
        }

        return builder.getMessages();
    }

    private static class SvsAuditMessageBuilder extends BaseAuditMessageBuilder<SvsAuditMessageBuilder> {}
}
