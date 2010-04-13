/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v3

/**
 * Validation profiles for messages of HL7 v3 based IHE transactions.
 * @author Dmytro Rud
 */
class Hl7v3ValidationProfiles {

    /**
     * Validation profiles for request messages.   
     */
    static final Map<Integer, Collection<List<String>>> REQUEST_TYPES = [
        44 : [['PRPA_IN201301UV02', null],
              ['PRPA_IN201302UV02', null],
              ['PRPA_IN201304UV02', null]],
        45 : [['PRPA_IN201309UV02', null]],
        46 : [['PRPA_IN201302UV02', null]],
        47 : [['PRPA_IN201305UV02', 'iti47/PRPA_IN201305UV02'],
              ['QUQI_IN000003UV01', null],
              ['QUQI_IN000003UV01_Cancel', null]],
        55 : [['PRPA_IN201305UV02', 'iti55/PRPA_IN201305UV02']],
        56 : [['PatientLocationQueryRequest', null, 'IHE/XCPD_PLQ']],
              ]
     
    /**
     * Validation profiles for response messages.   
     */
    static final Map<Integer, Collection<List<String>>> RESPONSE_TYPES = [
        44 : [['MCCI_IN000002UV01', null]],
        45 : [['PRPA_IN201310UV02', null]],
        46 : [['MCCI_IN000002UV01', null]],
        47 : [['PRPA_IN201306UV02', 'iti47/PRPA_IN201306UV02'],
              ['MCCI_IN000002UV01', null]],
        55 : [['PRPA_IN201306UV02', 'iti55/PRPA_IN201306UV02']],
        56 : [['PatientLocationQueryResponse', null, 'IHE/XCPD_PLQ']],
              ]

}
