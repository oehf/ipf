/*
 * Copyright 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.transform.core;

import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;

/**
 * Interface for HL7 v3 to v2 translators. 
 * @author Dmytro Rud
 */
public interface Hl73to2RequestTranslator {
    
    /**
     * Translates a HL7 v3 request message to HL7 v2.
     * @param xmlText
     *          HL7 v3 request message as String.
     * @return
     *          HL7 v2 translated request message.
     */
    public MessageAdapter translateRequest(String xmlText);
}
