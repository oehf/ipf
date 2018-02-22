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
package org.openehealth.ipf.commons.ihe.fhir.pixpdq.translation;

import ca.uhn.hl7v2.model.Message;
import org.openehealth.ipf.commons.ihe.fhir.translation.FhirTranslator;

import java.util.Map;

/**
 * @author Christian Ohr
 * @since 3.1
 *
 * @deprecated
 */
public interface TranslatorFhirToHL7v2 extends FhirTranslator<Message> {

    default Message translateFhirToHL7v2(Object request, Map<String, Object> parameters) {
        return translateFhir(request, parameters);
    }
}
