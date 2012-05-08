/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v2

import org.openehealth.ipf.modules.hl7.validation.DefaultValidationContext
import org.openehealth.ipf.modules.hl7dsl.SelectorClosure

/**
 * HL7 message validator for PIX/PDQ transactions. 
 * @author Dmytro Rud
 */
class MessageAdapterValidator extends AbstractMessageAdapterValidator {
     
     static final Map<String, Map<String, String>>RULES =
         [
          'ADT' : ['A01 A04 A05 A08' : 'MSH EVN PIDx PV1',
                   'A31'             : 'MSH EVN PIDy PV1',
                   'A40'             : 'MSH EVN PIDPD1MRGPV1',
                   'A43'             : 'MSH EVN PATIENT',
                  ],
          'QBP' : ['Q22 Q23 ZV1'     : 'MSH QPD RCP',
                  ],
          'RSP' : ['K22 K23'         : 'MSH MSA QAK QPD QUERY_RESPONSE',
                  ],
          'QCN' : ['J01'             : 'MSH QID',
                  ],              
          'ACK' : ['*'               : 'MSH MSA',
                  ],
         ]

      public Map<String, Map<String, String>> getRules(){
          return RULES;    
      }
      
      public DefaultValidationContext getValidationContext(){
          //adds default primitive checks
          return new DefaultValidationContext();
      }
      
     // --------------- Groups, ordered alphabetically ---------------

     /**
      * Valdates group QUERY_RESPONSE from RSP^K22, RSP^K23.
      */
     void checkQUERY_RESPONSE(msg, Collection<Exception> violations) {
         Collection<Exception> exceptions = []
         def queryResponse = msg.QUERY_RESPONSE
         if(queryResponse instanceof SelectorClosure) {
             // PDQ (ITI-21)
             for(repetition in queryResponse()) {
                 checkPID(repetition, violations)
             }
         } else {
             // PIX Query (ITI-9)
             checkPatientIdList(queryResponse.PID[3], violations)
         }
         exceptions
     }


    /**
     * Valdates group PATIENT from ADT^A43.
     */
    void checkPATIENT(msg,  Collection<Exception> violations) {
        def group = msg.PATIENT
        checkPatientIdList(group.PID[3], violations)
        checkShortPatientId(group.MRG[1], violations)
    }
     
     /**
      * Valdates group PIDPD1MRGPV1 from ADT^A40.
      */
     void checkPIDPD1MRGPV1(msg,  Collection<Exception> violations) {
         def group = msg.PIDPD1MRGPV1
         checkShortPatientId(group.PID[3], violations)
         checkShortPatientId(group.MRG[1], violations)
     }
     
     
     // --------------- Segments, ordered alphabetically ---------------

     /**
      * Validates segment EVN.
      */
     void checkEVN(msg, Collection<Exception> violations) {
         checkSegmentStructure(msg, 'EVN', [2], violations)
     }

     /**
      * Validates segment PID (special case for PIX Feed).
      */
     void checkPIDx(msg, Collection<Exception> violations) {
         checkShortPatientId(msg.PID[3], violations)
     }

     /**
      * Validates segment PID (special case for PIX Update Notification).
      */
     void checkPIDy(msg,Collection<Exception> violations) {
         checkPatientIdList(msg.PID[3], violations)
     }

     /**
      * Validates segment PV1.
      */
     void checkPV1(msg, Collection<Exception> violations) {
         checkSegmentStructure(msg, 'PV1', [2], violations)
     }

     /**
      * Validates segment QAK.
      */
     void checkQAK(msg, Collection<Exception> violations) {
         checkSegmentStructure(msg, 'QAK', [1, 2], violations)
     }
         
     /**
      * Validates segment QID.
      */
     void checkQID(msg, Collection<Exception> violations) {
         checkSegmentStructure(msg, 'QID', [1, 2], violations)
     }
         
     /**
      * Validates segment QPD.
      */
     void checkQPD(msg, Collection<Exception> violations) {
         checkSegmentStructure(msg, 'QPD', [1, 2], violations)
         def qpd3 = msg.QPD[3]
         if(qpd3 instanceof SelectorClosure){
             // for ITI-21, ITI-22 (PDQ Queries)
             if(qpd3().size() == 0) {
                 violations.add(new Exception('Empty query in QPD-3'))
             }
         } else { 
             // for ITI-9 (PIX Query)
             checkPatientId(msg.QPD[3], violations)
         }
     }
     
     /**
      * Validates segment RCP.
      */
     void checkRCP(msg, Collection<Exception> violations) {
         if (!msg.RCP?.value){
             violations.add(new Exception('Missing segment RCP'))
         }
     }
}

