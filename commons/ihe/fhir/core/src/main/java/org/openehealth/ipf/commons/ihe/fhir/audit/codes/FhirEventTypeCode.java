/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.audit.codes;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import lombok.Getter;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.types.EnumeratedCodedValue;
import org.openehealth.ipf.commons.audit.types.EventType;

import java.util.Map;

import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.EHS_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.IHE_SYSTEM_NAME;
import static org.openehealth.ipf.commons.ihe.fhir.audit.codes.Constants.RESTFUL_INTERACTION_SYSTEM_NAME;

/**
 * @author Christian Ohr
 */
public enum FhirEventTypeCode implements EventType, EnumeratedCodedValue<EventType> {

    ProvideDocumentBundle("ITI-65", IHE_SYSTEM_NAME, "Provide Document Bundle",
        "org.openehealth.ipf.commons.ihe.fhir.mhd.model.ProvideBundleRecipientAuditEvent",
        "org.openehealth.ipf.commons.ihe.fhir.mhd.model.ProvideBundleSourceAuditEvent"),
    MobileDocumentManifestQuery("ITI-66", IHE_SYSTEM_NAME, "Find Document Lists",
        "org.openehealth.ipf.commons.ihe.fhir.mhd.model.FindDocumentListsResponderAuditEvent",
        "org.openehealth.ipf.commons.ihe.fhir.mhd.model.FindDocumentListsConsumerAuditEvent"),
    MobileDocumentReferenceQuery("ITI-67", IHE_SYSTEM_NAME, "Find Document References",
        "org.openehealth.ipf.commons.ihe.fhir.mhd.model.FindDocumentReferencesResponderAuditEvent",
        "org.openehealth.ipf.commons.ihe.fhir.mhd.model.FindDocumentReferencesConsumerAuditEvent"),
    MobileDocumentRetrieval("ITI-68", IHE_SYSTEM_NAME, "Retrieve Document",
        "org.openehealth.ipf.commons.ihe.fhir.mhd.model.RetrieveDocumentResponderAuditEvent",
        "org.openehealth.ipf.commons.ihe.fhir.mhd.model.RetrieveDocumentConsumerAuditEvent"),
    MobilePatientDemographicsQuery("ITI-78", IHE_SYSTEM_NAME, "Mobile Patient Demographics Query",
        "org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PdqmSupplierAuditEvent",
        "org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PdqmConsumerAuditEvent"),
    RetrieveATNAAuditEvent("ITI-81", IHE_SYSTEM_NAME, "Retrieve ATNA AuditEvent"),
    MobilePatientIdentifierCrossReferenceQuery("ITI-83", IHE_SYSTEM_NAME, "Mobile Patient Identifier Cross-reference Query",
        "org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmManagerAuditEvent",
        "org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmConsumerAuditEvent"),
    // PIXm defines no Create profile for the Source, which cannot tell a create from an update
    MobilePatientIdentityFeed("ITI-104", IHE_SYSTEM_NAME, "Patient Identity Feed FHIR",
        Map.of(
            EventActionCode.Create, "org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmFeedCreateManagerAuditEvent",
            EventActionCode.Update, "org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmFeedUpdateManagerAuditEvent",
            EventActionCode.Delete, "org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmFeedDeleteManagerAuditEvent"),
        Map.of(
            EventActionCode.Update, "org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmFeedUpdateSourceAuditEvent",
            EventActionCode.Delete, "org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PixmFeedDeleteSourceAuditEvent")),
    SimplifiedPublish("ITI-105", IHE_SYSTEM_NAME, "Simplified Publish",
        "org.openehealth.ipf.commons.ihe.fhir.mhd.model.SimplifiedPublishRecipientAuditEvent",
        "org.openehealth.ipf.commons.ihe.fhir.mhd.model.SimplifiedPublishSourceAuditEvent"),
    PatientDemographicsMatch("ITI-119", IHE_SYSTEM_NAME, "Patient Demographics Match",
        "org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PdqmMatchSupplierAuditEvent",
        "org.openehealth.ipf.commons.ihe.fhir.pixpdq.model.PdqmMatchConsumerAuditEvent"),
    MobileQueryExistingData("PCC-44", IHE_SYSTEM_NAME, "Mobile Query Existing Data"),
    QueryPharmacyDocumentsOverMhd("PHARM-5", IHE_SYSTEM_NAME, "Query Pharmacy Documents over MHD"),
    MobilePrivacyPolicyFeed("PPQ-3", EHS_SYSTEM_NAME, "Mobile Privacy Policy Feed"),
    MobilePrivacyPolicyBundleFeed("PPQ-4", EHS_SYSTEM_NAME, "Mobile Privacy Policy Bundle Feed"),
    MobilePrivacyPolicyRetrieve("PPQ-5", EHS_SYSTEM_NAME, "Mobile Privacy Policy Retrieve");

    @Getter
    private final EventType value;

    private final String serverEventClassName;
    private final String clientEventClassName;

    // for transactions whose profile defines a different AuditEvent per action
    private final Map<EventActionCode, String> serverEventClassNames;
    private final Map<EventActionCode, String> clientEventClassNames;

    FhirEventTypeCode(String code, String codeSystemName, String displayName) {
        this(code, codeSystemName, displayName, (String) null, (String) null);
    }

    FhirEventTypeCode(String code, String codeSystemName, String displayName,
                      String serverEventClassName, String clientEventClassName) {
        this(code, codeSystemName, displayName, serverEventClassName, clientEventClassName, Map.of(), Map.of());
    }

    FhirEventTypeCode(String code, String codeSystemName, String displayName,
                      Map<EventActionCode, String> serverEventClassNames,
                      Map<EventActionCode, String> clientEventClassNames) {
        this(code, codeSystemName, displayName, null, null, serverEventClassNames, clientEventClassNames);
    }

    FhirEventTypeCode(String code, String codeSystemName, String displayName,
                      String serverEventClassName, String clientEventClassName,
                      Map<EventActionCode, String> serverEventClassNames,
                      Map<EventActionCode, String> clientEventClassNames) {
        this.value = EventType.of(code, codeSystemName, displayName);
        this.serverEventClassName = serverEventClassName;
        this.clientEventClassName = clientEventClassName;
        this.serverEventClassNames = serverEventClassNames;
        this.clientEventClassNames = clientEventClassNames;
    }

    /**
     * @param action action of the audited event
     * @return name of the AuditEvent class the server side records the event with, or null if the
     * profile of this transaction defines none for it
     * @since 6.0
     */
    public String getServerEventClassName(EventActionCode action) {
        return serverEventClassNames.getOrDefault(action, serverEventClassName);
    }

    /**
     * @param action action of the audited event
     * @return name of the AuditEvent class the client side records the event with, or null if the
     * profile of this transaction defines none for it
     * @since 6.0
     */
    public String getClientEventClassName(EventActionCode action) {
        return clientEventClassNames.getOrDefault(action, clientEventClassName);
    }

    /**
     * @return server event class name
     * @deprecated use @{link {@link #getServerEventClassName(EventActionCode)}}
     */
    @Deprecated(since = "6.0", forRemoval = true)
    public String getServerEventClassName() {
        return serverEventClassName;
    }

    /**
     * @return server event class name
     * @deprecated use @{link {@link #getClientEventClassName(EventActionCode)} (EventActionCode)}}
     */
    @Deprecated(since = "6.0", forRemoval = true)
    public String getClientEventClassName() {
        return clientEventClassName;
    }

    public static EventType fromRestOperationType(RestOperationTypeEnum operation) {
        return fromRestOperationType(operation, null);
    }

    public static EventType fromRestOperationType(RestOperationTypeEnum operation, String originalText) {
        return EventType.of(
                operation.getCode(),
                RESTFUL_INTERACTION_SYSTEM_NAME,
                originalText != null ? originalText : operation.getCode());
    }

}
