/*
 * Copyright 2024 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.audit.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ResourceType;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FhirAuditServer extends RestfulServer implements IResourceProvider {

    private final Map<String, AuditEvent> auditEvents = Collections.synchronizedMap(new HashMap<>());

    public FhirAuditServer() {
        setFhirContext(FhirContext.forR4());
        setResourceProviders(this);
    }

    public List<AuditEvent> getAuditEvents() {
        return new ArrayList<>(auditEvents.values());
    }

    public void clearAuditEvents() {
        auditEvents.clear();
    }

    @Read()
    public AuditEvent read(@IdParam IdType theId) {
        AuditEvent auditEvent = auditEvents.get(theId.getIdPart());
        if (auditEvent == null) {
            throw new ResourceNotFoundException(theId);
        }
        return auditEvent;
    }

    @Delete()
    public MethodOutcome delete(@IdParam IdType theId) {
        AuditEvent auditEvent = auditEvents.remove(theId.getIdPart());
        if (auditEvent == null) {
            throw new ResourceNotFoundException(theId);
        }
        return new MethodOutcome(theId);
    }

    @Create()
    public MethodOutcome create(@ResourceParam AuditEvent auditEvent) {
        String id = UUID.randomUUID().toString();
        IdType idType = new IdType(ResourceType.AuditEvent.name(), id);
        auditEvent.setId(idType);
        auditEvents.put(id, auditEvent);
        return new MethodOutcome(idType, true);
    }

    @Search
    public List<AuditEvent> list(@OptionalParam(name= AuditEvent.SP_TYPE) TokenParam type,
                                 @OptionalParam(name= AuditEvent.SP_SUBTYPE) TokenParam subtype) {
        Stream<AuditEvent> allAuditEvents = getAuditEvents().stream();
        if (type != null) {
            if (isNotBlank(type.getSystem()) && isNotBlank(type.getValue())) {
                allAuditEvents = allAuditEvents.filter(auditEvent ->
                    auditEvent.getType().hasSystem() && auditEvent.getType().getSystem().equals(type.getSystem()) &&
                    auditEvent.getType().hasCode() && auditEvent.getType().getCode().equals(type.getValue()));
            } else if (isNotBlank(type.getSystem()) && isBlank(type.getValue())){
                allAuditEvents = allAuditEvents.filter(auditEvent ->
                    auditEvent.getType().hasSystem() && auditEvent.getType().getSystem().equals(type.getSystem()));
            } else if (isBlank(type.getSystem()) && isNotBlank(type.getValue())){
                allAuditEvents = allAuditEvents.filter(auditEvent ->
                    auditEvent.getType().hasCode() && auditEvent.getType().getCode().equals(type.getValue()));
            }
        }
        if (subtype != null) {
            if (isNotBlank(subtype.getSystem()) && isNotBlank(subtype.getValue())) {
                allAuditEvents = allAuditEvents.filter(auditEvent ->
                    auditEvent.getSubtypeFirstRep().hasSystem() && auditEvent.getSubtypeFirstRep().getSystem().equals(subtype.getSystem()) &&
                        auditEvent.getSubtypeFirstRep().hasCode() && auditEvent.getSubtypeFirstRep().getCode().equals(subtype.getValue()));
            } else if (isNotBlank(subtype.getSystem()) && isBlank(subtype.getValue())){
                allAuditEvents = allAuditEvents.filter(auditEvent ->
                    auditEvent.getSubtypeFirstRep().hasSystem() && auditEvent.getSubtypeFirstRep().getSystem().equals(subtype.getSystem()));
            } else if (isBlank(subtype.getSystem()) && isNotBlank(subtype.getValue())){
                allAuditEvents = allAuditEvents.filter(auditEvent ->
                    auditEvent.getSubtypeFirstRep().hasCode() && auditEvent.getSubtypeFirstRep().getCode().equals(subtype.getValue()));
            }
        }
        return allAuditEvents.collect(Collectors.toList());
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return AuditEvent.class;
    }
}
