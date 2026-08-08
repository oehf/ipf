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
import org.openehealth.ipf.commons.ihe.fhir.pixpdq.PdqmProfile;

/**
 * The AuditEvent an ITI-78 audit record takes when it is written by the Patient Demographics Consumer,
 * i.e. the client of the transaction.
 *
 * @author Christian Ohr
 * @since 5.3
 */
@ResourceDef(
    name = "AuditEvent",
    id = "PdqmConsumerAuditEvent",
    profile = PdqmProfile.PDQM_CONSUMER_AUDIT_PROFILE)
public class PdqmConsumerAuditEvent extends PdqmAuditEvent {

}
