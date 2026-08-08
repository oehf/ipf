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
package org.openehealth.ipf.commons.ihe.core.atna;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectIdTypeCode;
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * A generic data structure used to store information pieces needed for auditing.
 *
 * @author Dmytro Rud
 */
public abstract class AuditDataset implements Serializable {

    @NoArgsConstructor
    public static class HumanUser {
        /** ID, preferably in the format defined in the IHE XUA profile */
        @Getter @Setter private String id;

        /** Real-world name */
        @Getter @Setter private String name;

        /** Role codes */
        @Getter private final List<ActiveParticipantRoleId> roles = new ArrayList<>();

        public HumanUser(String id, String name, Collection<ActiveParticipantRoleId> roles) {
            this.id = id;
            this.name = name;
            this.roles.addAll(roles);
        }

        public boolean isEmpty() {
            return (id == null || id.trim().isEmpty()) && (name == null || name.trim().isEmpty());
        }
    }


    /**
     * whether we audit on server (true) or on client (false)
     */
    @Getter
    private final boolean serverSide;

    /**
     * Overall outcome of the transaction that causes this audit event
     */
    @Getter
    @Setter
    private EventOutcomeIndicator eventOutcomeIndicator;

    /**
     * Description of the overall outcome of the transaction that causes this audit event
     */
    @Getter
    @Setter
    private String eventOutcomeDescription;

    /**
     * Source UserName, e.g. extracted from a client certificate
     */
    @Getter
    @Setter
    private String sourceUserName;

    /**
     * Id correlating the audit records that the two ends of a transaction write about it, taken from
     * whichever HTTP header {@link org.openehealth.ipf.commons.audit.AuditContext#getRequestIdHeaderNames()}
     * names.
     */
    @Getter
    private String requestId;

    /**
     * Participant object ID type the correlation id is recorded under. The value is the same whichever
     * it is; what differs is which profile asked for the correlation, and the profiles disagree on the
     * code -- see {@link ParticipantObjectIdTypeCode#XRequestId} and
     * {@link ParticipantObjectIdTypeCode#W3cTraceContext}.
     */
    @Getter
    private ParticipantObjectIdType requestIdType = ParticipantObjectIdTypeCode.XRequestId;

    /**
     * Sets the correlation id along with the participant object ID type it is to be recorded under.
     *
     * @param requestId     the correlation id
     * @param requestIdType how to record it, e.g. {@link ParticipantObjectIdTypeCode#W3cTraceContext}
     */
    public void setRequestId(String requestId, ParticipantObjectIdType requestIdType) {
        this.requestId = requestId;
        this.requestIdType = requireNonNull(requestIdType, "request ID type must not be null");
    }

    /**
     * @return the correlation id if it is a W3C Trace Context one, null otherwise
     * @deprecated the trace context id was never anything but a correlation id read from an HTTP header,
     *      which is what {@link #getRequestId()} is. Only the participant object it ends up in differs,
     *      and that is {@link #getRequestIdType()} now.
     */
    @Deprecated(since = "5.3", forRemoval = true)
    public String getW3cTraceContextId() {
        return isW3cTraceContext(requestIdType) ? requestId : null;
    }

    /**
     * @param w3cTraceContextId contents of a {@code traceparent} header
     * @deprecated use {@link #setRequestId(String, ParticipantObjectIdType)}, with
     *      {@link ParticipantObjectIdTypeCode#W3cTraceContext} or, for the Swiss EPR,
     *      {@link ParticipantObjectIdTypeCode#SwissW3cTraceContext}. This one keeps setting the Swiss
     *      code, which is what it always did.
     */
    @Deprecated(since = "5.3", forRemoval = true)
    public void setW3cTraceContextId(String w3cTraceContextId) {
        setRequestId(w3cTraceContextId, ParticipantObjectIdTypeCode.SwissW3cTraceContext);
    }

    private static boolean isW3cTraceContext(ParticipantObjectIdType requestIdType) {
        // matched on the code alone: the same traceparent is recorded under more than one code system
        // name, and a caller may have built an equivalent type of its own instead of using a constant
        return ParticipantObjectIdTypeCode.W3cTraceContext.getCode().equals(requestIdType.getCode());
    }

    /**
     * @param serverSide   specifies whether this audit dataset will be used on the
     *                     server side (<code>true</code>) or on the client side
     *                     (<code>false</code>)
     */
    public AuditDataset(boolean serverSide) {
        this.serverSide = serverSide;
    }

    @Override
    public String toString() {
        return "AuditDataset{" +
                "serverSide=" + serverSide +
                ", eventOutcomeIndicator=" + eventOutcomeIndicator +
                ", eventOutcomeDescription='" + eventOutcomeDescription + '\'' +
                ", sourceUserName='" + sourceUserName + '\'' +
                '}';
    }

    /**
     * @return the user ID of the transaction source
     */
    public abstract String getSourceUserId();

    /**
     * @return the user ID of the transaction destination
     */
    public abstract String getDestinationUserId();

    /**
     * Returns the local address. May fall back to {@link AuditUtils#getLocalIPAddress()} if not explicitly set
     *
     * @return the local address
     */
    public abstract String getLocalAddress();

    /**
     * @return the remote address of the transaction
     */
    public abstract String getRemoteAddress();

    /**
     * @return information about human user(s) participating in the transaction
     */
    public abstract List<HumanUser> getHumanUsers();

    /**
     * @return true if the source user is the requestor of the event
     */
    public boolean isSourceUserIsRequestor() {
        return getHumanUsers().isEmpty();
    }

    /**
     * @return true if the destination user is the requestor of the event
     */
    public boolean isDestinationUserIsRequestor() {
        return false;
    }
}
