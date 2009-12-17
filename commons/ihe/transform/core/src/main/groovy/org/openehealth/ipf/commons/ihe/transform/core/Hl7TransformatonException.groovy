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
package org.openehealth.ipf.commons.ihe.transform.core

import org.openehealth.ipf.modules.hl7.AckTypeCode
import org.apache.commons.lang.builder.ToStringBuilderimport org.apache.commons.lang.builder.ToStringStyle
/**
 * Exception class containing all fields necessary 
 * to generate an HL7 v3 error message.
 * 
 * @author Dmytro Rud
 */
class Hl7TransformatonException extends RuntimeException {

    InteractionId      interactionId            = InteractionId.MCCI_IN000002UV01
    AckTypeCode        ackTypeCode              = AckTypeCode.AE
    QueryResponseCode  queryResponseCode        = QueryResponseCode.AE
    AckDetailTypeCode  ackDetailTypeCode        = AckDetailTypeCode.E
    String             errorConditionCodeSystem = '2.16.840.1.113883.12.357'
    int                errorConditionCode       = 207
    Collection<String> locations                = ['/']
    String             text                     = ''

    
    String getMessage() {
        return text
    }
    
    String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE)
    }
}


