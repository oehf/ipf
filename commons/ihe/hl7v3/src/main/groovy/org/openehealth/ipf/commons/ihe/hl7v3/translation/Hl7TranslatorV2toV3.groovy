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
package org.openehealth.ipf.commons.ihe.hl7v3.translation;

import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;

/**
 * Interface for HL7v2 to HL7v3 translators.
 * @author Dmytro Rud
 */
interface Hl7TranslatorV2toV3 {
    
    /**
     * Translates the {@link MessageAdapter} containing an HL7v2 message
     * into an HL7v3 XML String, optionally using the initial HL7v3 message.  
     */
    String translateV2toV3(MessageAdapter messageV2, String initialV3, String charset)
}
