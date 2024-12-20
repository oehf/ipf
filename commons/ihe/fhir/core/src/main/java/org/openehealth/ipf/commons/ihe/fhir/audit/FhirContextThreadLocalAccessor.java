/*
 * Copyright 2024 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.fhir.audit;

import ca.uhn.fhir.context.FhirContext;
import io.micrometer.context.ThreadLocalAccessor;

/**
 * ThreadLocalAccessor for a {@link FhirContextHolder}. Register an instance
 * to the {@link org.openehealth.ipf.commons.audit.AuditContext} using
 * {@link io.micrometer.context.ContextRegistry#registerThreadLocalAccessor(ThreadLocalAccessor)}
 * to be able to reuse a predefined {@link FhirContext} for audit record serialization via
 * {@link org.openehealth.ipf.commons.ihe.fhir.audit.protocol.AbstractFhirRestTLSAuditRecordSender} subclasses.
 */
public class FhirContextThreadLocalAccessor implements ThreadLocalAccessor<FhirContext> {

    public static final String KEY = "fhir.context";

    @Override
    public Object key() {
        return KEY;
    }

    @Override
    public FhirContext getValue() {
        return FhirContextHolder.get();
    }

    @Override
    public void setValue(FhirContext value) {
        FhirContextHolder.setCurrentContext(value);
    }

    @Override
    public void setValue() {
        FhirContextHolder.remove();
    }
}
