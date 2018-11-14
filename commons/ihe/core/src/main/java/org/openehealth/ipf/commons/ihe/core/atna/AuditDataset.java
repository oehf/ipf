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
import org.apache.commons.lang3.StringUtils;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.types.ActiveParticipantRoleId;
import org.openehealth.ipf.commons.audit.utils.AuditUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
            return StringUtils.isAllBlank(id, name);
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
     * Source User Name, e.g. extracted from a client certificate
     */
    @Getter
    @Setter
    String sourceUserName;

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
