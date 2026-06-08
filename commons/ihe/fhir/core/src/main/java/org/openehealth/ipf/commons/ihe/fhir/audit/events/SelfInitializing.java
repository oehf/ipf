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
package org.openehealth.ipf.commons.ihe.fhir.audit.events;

import org.openehealth.ipf.commons.audit.model.AuditMessage;

/**
 * Common interface for AuditEvents that initialize themselves given
 * a populated AuditMessage.
 */
public interface SelfInitializing {

    default void initialize(AuditMessage auditMessage) {
        setLocalAgent(auditMessage);
        setRemoteAgent(auditMessage);
        setUserAgent(auditMessage);
    }

    void setLocalAgent(AuditMessage auditMessage);
    void setRemoteAgent(AuditMessage auditMessage);
    void setUserAgent(AuditMessage auditMessage);

}
