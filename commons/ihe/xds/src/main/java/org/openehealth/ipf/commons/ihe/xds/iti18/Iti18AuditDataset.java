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
package org.openehealth.ipf.commons.ihe.xds.iti18;

import org.openehealth.ipf.commons.ihe.xds.core.audit.XdsAuditDataset;

/**
 * ITI-18 specific Audit Dataset.
 * @author Dmytro Rud
 */
public class Iti18AuditDataset extends XdsAuditDataset {
    private static final long serialVersionUID = -972765429868799105L;

    private String queryUuid;
    private String homeCommunityId;

    /**
     * Constructs the audit dataset.
     * @param serverSide
     *            specifies whether this audit dataset will be used on the
     *            server side (<code>true</code>) or on the client side (
     *            <code>false</code>)
     */
    public Iti18AuditDataset(boolean serverSide) {
        super(serverSide);
    }

    /**
     * Sets the UUID of the query.
     * @param queryUuid
     *          the UUID of the query being audited.
     */
    public void setQueryUuid(String queryUuid) {
        this.queryUuid = queryUuid;
    }

    /**
     * @return the UUID of the query being audited.
     */
    public String getQueryUuid() {
        return queryUuid;
    }

    /**
     * Returns home community ID.
     */
    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    /**
     * Sets home community ID.
     */
    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
    }

}
