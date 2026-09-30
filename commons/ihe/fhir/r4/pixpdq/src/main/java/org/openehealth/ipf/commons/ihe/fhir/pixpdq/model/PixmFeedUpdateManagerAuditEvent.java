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

package org.openehealth.ipf.commons.ihe.fhir.pixpdq.model;

import ca.uhn.fhir.model.api.annotation.ResourceDef;
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PixmProfile;
import org.openehealth.ipf.commons.ihe.fhir.support.audit.model.PatientUpdateAuditEvent;

import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.FhirEventTypeCode.MobilePatientIdentityFeed;

/**
 * The AuditEvent an ITI-104 audit record takes when it is written by the Patient Identifier Cross-
 * reference Manager, i.e. the server of the transaction, for a patient that has been updated or
 * merged. PIXm profiles it on the BALP PatientUpdate pattern, adding the IHE transaction subtype.
 *
 * @author Christian Ohr
 * @since 6.0
 */
@ResourceDef(
    name = "AuditEvent",
    id = "PixmFeedUpdateManagerAuditEvent",
    profile = PixmProfile.PIXM_FEED_UPDATE_MANAGER_AUDIT_PROFILE)
public class PixmFeedUpdateManagerAuditEvent extends PatientUpdateAuditEvent {

    public PixmFeedUpdateManagerAuditEvent() {
        super();
        addTransactionSubtype(MobilePatientIdentityFeed);
    }
}
