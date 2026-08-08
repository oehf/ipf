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

package org.openehealth.ipf.commons.ihe.fhir.support.audit.model;

import org.openehealth.ipf.commons.ihe.fhir.support.IheFhirProfile;

/**
 * Canonical URLs of the AuditEvent profiles defined by
 * <a href="https://profiles.ihe.net/ITI/BALP/index.html">IHE BALP</a>. Each of them is claimed by the
 * AuditEvent class of the same name in this package, and by the transaction-specific AuditEvents
 * deriving from it.
 * <p>
 * All values are compile-time constants, as they are referenced from {@code @ResourceDef} annotations.
 *
 * @author Christian Ohr
 */
public abstract class BalpConstants {

    /** Canonical URL prefix all BALP AuditEvent profiles share. */
    private static final String PROFILE_PREFIX = IheFhirProfile.IHE_ITI_PREFIX + "BALP/StructureDefinition/IHE.BasicAudit.";

    public static final String BALP_QUERY_AUDIT_PROFILE = PROFILE_PREFIX + "Query";
    public static final String BALP_PATIENT_QUERY_AUDIT_PROFILE = PROFILE_PREFIX + "PatientQuery";
    public static final String BALP_READ_AUDIT_PROFILE = PROFILE_PREFIX + "Read";
    public static final String BALP_PATIENT_READ_AUDIT_PROFILE = PROFILE_PREFIX + "PatientRead";
    public static final String BALP_UPDATE_AUDIT_PROFILE = PROFILE_PREFIX + "Update";
    public static final String BALP_PATIENT_UPDATE_AUDIT_PROFILE = PROFILE_PREFIX + "PatientUpdate";
    public static final String BALP_CREATE_AUDIT_PROFILE = PROFILE_PREFIX + "Create";
    public static final String BALP_PATIENT_CREATE_AUDIT_PROFILE = PROFILE_PREFIX + "PatientCreate";
    public static final String BALP_DELETE_AUDIT_PROFILE = PROFILE_PREFIX + "Delete";
    public static final String BALP_PATIENT_DELETE_AUDIT_PROFILE = PROFILE_PREFIX + "PatientDelete";
    public static final String BALP_EXPORT_AUDIT_PROFILE = PROFILE_PREFIX + "PrivacyDisclosure.Source";
    public static final String BALP_IMPORT_AUDIT_PROFILE = PROFILE_PREFIX + "PrivacyDisclosure.Recipient";
}
