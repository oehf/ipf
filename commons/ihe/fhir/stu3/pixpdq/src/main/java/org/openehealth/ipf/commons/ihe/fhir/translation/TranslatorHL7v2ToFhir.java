/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.fhir.translation;

import ca.uhn.hl7v2.model.Message;

import java.util.Map;

/**
 * @author Christian Ohr
 * @since 3.4
 */
public interface TranslatorHL7v2ToFhir {

    /**
     * Translates a HL7v2 message into a FHIR resource
     *
     * @param message HL7v2 message
     * @return FHIR resource
     */
    Object translateHL7v2ToFhir(Message message, Map<String, Object> parameters);

}
