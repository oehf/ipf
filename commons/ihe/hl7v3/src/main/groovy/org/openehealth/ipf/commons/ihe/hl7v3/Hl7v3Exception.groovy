/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3;

/**
 * HL7v3-specific exception class.
 * @author Dmytro Rud
 */
class Hl7v3Exception extends RuntimeException {
    // overwrite fields of the base class due to syntax concerns
    String message
    Throwable cause


    /*
     * Where to find value set definitions fo the parameters:
     * HL7v3 Normative Edition documentation -> Foundation -> Vocabulary -> HL7 Value Sets
     * Then select the corresponding value set from the list.
     */

    // value set: AcknowledgementType
    String typeCode = 'AE'

    // value set: AcknowledgementDetailCode
    String acknowledgementDetailCode = 'INTERR'

    // value set: QueryResponse
    String queryResponseCode = 'AE'


    /*
     * Parameters below matter only when the element <controlActProcess>
     * is present in the error message structure.
     */

    // value set: QueryStatusCode
    String statusCode = 'aborted'

    String detectedIssueEventCode = 'ActAdministrativeDetectedIssueCode'
    String detectedIssueEventCodeSystem = '2.16.840.1.113883.5.4'
    String detectedIssueManagementCode
    String detectedIssueManagementCodeSystem = "2.16.840.1.113883.5.4"
}
