/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.audit.codes;

public class Constants {

    public static final String IHE_SYSTEM_NAME = "urn:ihe:event-type-code";
    public static final String EHS_SYSTEM_NAME = "urn:e-health-suisse:event-type-code";

    public static final String DCM_SYSTEM_NAME = "http://dicom.nema.org/resources/ontology/DCM";
    public static final String DCM_OCLIENT_CODE = "110150";
    public static final String SECURITY_SOURCE_SYSTEM_NAME = "http://terminology.hl7.org/CodeSystem/security-source-type";
    public static final String AUDIT_ENTITY_SYSTEM_NAME = "http://terminology.hl7.org/CodeSystem/audit-entity-type";
    public static final String OBJECT_ROLE_SYSTEM_NAME = "http://terminology.hl7.org/CodeSystem/object-role";
    public static final String AUDIT_LIFECYCLE_SYSTEM_NAME = "http://terminology.hl7.org/CodeSystem/dicom-audit-lifecycle";
    public static final String OUSER_AGENT_TYPE_SYSTEM_NAME = "http://terminology.hl7.org/CodeSystem/v3-ParticipationType";
    public static final String OUSER_AGENT_TYPE_CODE = "IRCP";
    public static final String OUSER_AGENT_PURPOSE_OF_USE_SYSTEM_NAME = "http://terminology.hl7.org/CodeSystem/v3-ActReason";
    public static final String OUSER_AGENT_ROLE_SYSTEM_NAME = "http://terminology.hl7.org/CodeSystem/v3-RoleClass";
    public static final String OUSER_AGENT_TYPE_OPAQUE_SYSTEM_NAME = "https://profiles.ihe.net/ITI/BALP/CodeSystem/UserAgentTypes";
    public static final String OUSER_AGENT_TYPE_OPAQUE_CODE = "UserOauthAgent";
}
