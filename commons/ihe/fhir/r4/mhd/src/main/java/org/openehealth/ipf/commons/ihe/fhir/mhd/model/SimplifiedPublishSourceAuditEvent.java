/*
 * Copyright 2026 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.mhd.model;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.openehealth.ipf.commons.audit.codes.ActiveParticipantRoleIdCode;
import org.openehealth.ipf.commons.ihe.fhir.mhd.MhdProfile;

/**
 * The AuditEvent an ITI-105 audit record takes when it is written by the Document Source, i.e. the
 * client of the transaction.
 *
 * @author Christian Ohr
 * @since 5.3
 */
@ResourceDef(
    name = "AuditEvent",
    id = "SimplifiedPublishSourceAuditEvent",
    profile = MhdProfile.SIMPLIFIED_PUBLISH_SOURCE_AUDIT_PROFILE)
public class SimplifiedPublishSourceAuditEvent extends SimplifiedPublishAuditEvent {

    /**
     * @return {@link ActiveParticipantRoleIdCode#Source}, the audit source being the client here
     */
    @Override
    protected ActiveParticipantRoleIdCode localRole() {
        return ActiveParticipantRoleIdCode.Source;
    }
}
