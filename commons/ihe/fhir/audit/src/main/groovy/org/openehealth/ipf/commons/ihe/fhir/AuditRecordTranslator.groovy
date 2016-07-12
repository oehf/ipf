/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir

import org.hl7.fhir.instance.model.*
import org.hl7.fhir.instance.model.AuditEvent.AuditEventSourceComponent
import org.hl7.fhir.instance.model.valuesets.AuditSourceTypeEnumFactory
import org.hl7.fhir.instance.model.valuesets.ObjectRoleEnumFactory
import org.hl7.fhir.instance.model.valuesets.ObjectTypeEnumFactory
import org.joda.time.format.ISODateTimeFormat
import org.openhealthtools.ihe.atna.auditor.events.AuditEventMessage
import org.openhealthtools.ihe.atna.auditor.models.rfc3881.*

/**
 * Translates ATNA audit records into FHIR AuditEvent resources.
 *
 * @author Dmytro Rud
 * @since 3.1
 * @deprecated use {@link org.openehealth.ipf.commons.ihe.fhir.translation.AuditRecordTranslator}
 */
class AuditRecordTranslator extends org.openehealth.ipf.commons.ihe.fhir.translation.AuditRecordTranslator {
}
