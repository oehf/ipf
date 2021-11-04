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
package org.openehealth.ipf.commons.ihe.hl7v3.translation

import ca.uhn.hl7v2.model.Message
import groovy.xml.slurpersupport.GPathResult


/**
 * Interface for HL7v3 to HL7v2 translators.
 * @author Dmytro Rud
 */
interface Hl7TranslatorV3toV2 {
    
    /**
     * Translates the String containing an HL7v3 XML document
     * into a {@link Message} containing an HL7v2 message,
     * optionally using the initial HL7v2 message.  
     */
    Message translateV3toV2(String v3, Message initialV2)

    /**
     * Called after the translation is done
     * @param xml parsed HL7v3 message
     * @param result translated HL7v2 message
     */
    void postprocess(Message qry, GPathResult xml)
}
