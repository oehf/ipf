/*
 * Copyright 2021 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.support.audit.marshal;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

/**
 * Serializes ATNA audit messages as XML-encoded FHIR R4 {@code AuditEvent} resources. Configure it as
 * the {@code serializationStrategy} of the audit context to have audit records written in the shape
 * that <a href="https://profiles.ihe.net/ITI/BALP/index.html">IHE BALP</a> and the AuditEvent profiles
 * of the IHE transactions define.
 *
 * @author Christian Ohr
 * @since 4.1
 * @see BalpJsonSerializationStrategy
 */
public class BalpXmlSerializationStrategy extends AbstractFhirAuditSerializationStrategy {

    /**
     * Uses a newly created R4 {@link FhirContext}. Prefer {@link #BalpXmlSerializationStrategy(FhirContext)}
     * whenever the application has one already: a FhirContext is expensive to create and meant to be shared.
     */
    public BalpXmlSerializationStrategy() {
        super();
    }

    /**
     * @param fhirContext the FhirContext whose XML parser serializes the AuditEvents. Must be an R4 context.
     */
    public BalpXmlSerializationStrategy(FhirContext fhirContext) {
        super(fhirContext);
    }

    /**
     * @param fhirContext the FhirContext to obtain the parser from
     * @return an XML parser
     */
    @Override
    protected IParser getParser(FhirContext fhirContext) {
        return fhirContext.newXmlParser();
    }
}
