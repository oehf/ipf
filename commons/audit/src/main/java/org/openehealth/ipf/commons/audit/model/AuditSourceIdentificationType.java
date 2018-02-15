/*
 * Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.audit.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.audit.types.AuditSource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * @author Christian Ohr
 * @since 3.5
 */
@EqualsAndHashCode
public class AuditSourceIdentificationType implements Serializable, Validateable {

    /**
     * Identifier of the source that detected the auditable event and created this audit message.
     * Although often the audit source is one of the participants, it could also be an external system
     * that is monitoring the activities of the participants (e.g., an add-on audit-generating device).
     */
    @Getter
    private final String auditSourceID;

    /**
     * <p>
     * Logical source location within the healthcare enterprise network,
     * e.g., a hospital or other provider location within a multi-entity provider group.
     * </p>
     * <p>
     * Serves to further qualify the Audit Source ID, since Audit Source ID is not required to be globally unique.
     * </p>
     */
    @Getter @Setter
    private String auditEnterpriseSiteID;

    private List<AuditSource> auditSourceTypeCode;

    /**
     * @param auditSourceID identifier of the source that detected the auditable event and created this audit message
     */
    public AuditSourceIdentificationType(String auditSourceID) {
        this.auditSourceID = requireNonNull(auditSourceID, "auditSourceID must be not null");
    }

    /**
     * Code specifying the type of source. The Audit Source Type Code values specify the type of source where an event originated.
     * Codes from coded terminologies and implementation defined codes can also be used for the AuditSourceTypeCode.
     *
     * @return codes specifying the type of source
     */
    public List<AuditSource> getAuditSourceType() {
        if (auditSourceTypeCode == null) {
            auditSourceTypeCode = new ArrayList<>();
        }
        return this.auditSourceTypeCode;
    }

    @Override
    public void validate() {
        // no special rules
    }
}
