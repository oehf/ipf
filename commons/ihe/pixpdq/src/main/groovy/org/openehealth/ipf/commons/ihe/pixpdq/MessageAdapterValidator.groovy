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
package org.openehealth.ipf.commons.ihe.pixpdq

import ca.uhn.hl7v2.model.GenericSegment
import ca.uhn.hl7v2.model.Group
import org.openehealth.ipf.commons.core.modules.api.ValidationException
import org.openehealth.ipf.commons.core.modules.api.Validator
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter
import org.openehealth.ipf.modules.hl7dsl.SelectorClosure

/**
 * HL7 message validator for PIX/PDQ transactions. 
 * @author Dmytro Rud
 */
class MessageAdapterValidator implements Validator<Object, Object> {
     
     /**
      * List of relevant segments for particular message types.
      */
     def static final RULES =
         [
          'ADT' : ['A01 A04 A05 A08' : 'MSH EVN PIDx PV1',
                   'A31'             : 'MSH EVN PID PV1',
                   'A40'             : 'MSH EVN PIDPD1MRGPV1',
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

     
     /**
      * Performs validation of an HL7 message
      * @param msg
      *     {@link MessageAdapter} with the mesasge to be validated
      * @param dummy
      *     unused fictive parameter  
      */
     void validate(Object msg, Object dummy) throws ValidationException {
         def msh91 = msg.MSH[9][1].value
         def msh92 = msg.MSH[9][2].value

         // find rules that correspond the type of the given message
         def segmentNames = null
         for(triggerEvents in RULES[msh91]?.keySet()) {
             if(triggerEvents.contains(msh92) || (triggerEvents == '*')) {
                 segmentNames = RULES[msh91][triggerEvents]
                 break
             }
         }
         if( ! segmentNames) {
             throw new ValidationException("No validation rules defined for ${msh91}^${msh92}")
         }
         
         // perform validation
         def exceptions = checkMessage(msg, segmentNames)
         if(exceptions) {
             throw new ValidationException('Message validation failed', exceptions)
         }
     }

     
     // --------------- Highest-level validation objects ---------------
      
     /**
      * Validates a message.
      */
     static def checkMessage(msg, segmentNames) {
         def exceptions = []
         exceptions += checkUnrecognizedSegments(msg.group)
         for(segmentName in segmentNames.tokenize()) {
             exceptions += "check${segmentName}"(msg)
         }
         exceptions
     }
     
     /**
      * Validates structure of a message segment.
      */
     static def checkSegmentStructure(msg, segmentName, fieldNumbers) {
         def exceptions = []
         def segment = msg."${segmentName}"
         for(i in fieldNumbers) {
             if( ! segment[i].value) {
                 exceptions += new Exception("Missing ${segmentName}-${i}")
             }
         }
         exceptions
     }

     /**
      * Searches for unrecognized segments in a Group.
      */
     static def checkUnrecognizedSegments(Group group) {
         def exceptions = []
         for(name in group.names) {
             def c = group.getClass(name)
             if(c == GenericSegment.class) {
                 exceptions += new Exception("Unknown segment ${name}")
             } else if(Group.class.isAssignableFrom(c) && group.getAll(name)) {
                 exceptions += checkUnrecognizedSegments(group.get(name))
             }
         }
         exceptions
     }
     

     // --------------- Groups, ordered alphabetically ---------------

     /**
      * Valdates group QUERY_RESPONSE from RSP^K22, RSP^K23.
      */
     static def checkQUERY_RESPONSE(msg) {
         def exceptions = []
         def queryResponse = msg.QUERY_RESPONSE
         if(queryResponse instanceof SelectorClosure) {
             // PDQ (ITI-21)
             for(repetition in queryResponse()) {
                 exceptions += checkPID(repetition)
             }
         } else {
             // PIX Query (ITI-9)
             exceptions += checkPatientIdList(queryResponse.PID[3])
         }
         exceptions
     }
     
     /**
      * Valdates group PIDPD1MRGPV1 from ADT^A40.
      */
     static def checkPIDPD1MRGPV1(msg) {
         def exceptions = []
         def group = msg.PIDPD1MRGPV1
         exceptions += checkShortPatientId(group.PID[3])
         exceptions += checkShortPatientId(group.MRG[1])
         exceptions
     }
     
     
     // --------------- Segments, ordered alphabetically ---------------

     /**
      * Validates segment EVN.
      */
     static def checkEVN(msg) {
         checkSegmentStructure(msg, 'EVN', [2])
     }

     /**
      * Validates segment MSA.
      */
     static def checkMSA(msg) {
         checkSegmentStructure(msg, 'MSA', [1, 2])
     }

     /**
      * Validates segment MSH.
      */
     static def checkMSH(msg) {
         checkSegmentStructure(msg, 'MSH', [1, 2, 7, 9, 10, 11, 12])
     }

     /**
      * Validates segment PID.
      */
     static def checkPID(msg) {
         def exceptions = []
         if (msg.MSH[9][2].value != 'A31') {
             exceptions += checkPatientName(msg.PID[5])
         }
         exceptions += checkPatientIdList(msg.PID[3])
         exceptions
     }

     /**
      * Validates segment PID (special case for PIX Feed).
      */
     static def checkPIDx(msg) {
         checkShortPatientId(msg.PID[3])
     }

     /**
      * Validates segment PV1.
      */
     static def checkPV1(msg) {
         checkSegmentStructure(msg, 'PV1', [2])
     }

     /**
      * Validates segment QAK.
      */
     static def checkQAK(msg) {
         checkSegmentStructure(msg, 'QAK', [1, 2])
     }
         
     /**
      * Validates segment QID.
      */
     static def checkQID(msg) {
         checkSegmentStructure(msg, 'QID', [1, 2])
     }
         
     /**
      * Validates segment QPD.
      */
     static def checkQPD(msg) {
         def exceptions = []
         exceptions += checkSegmentStructure(msg, 'QPD', [1, 2])
         def qpd3 = msg.QPD[3]
         if(qpd3 instanceof SelectorClosure){
             // for ITI-21, ITI-22 (PDQ Queries)
             if(qpd3().size() == 0) {
                 exceptions += new Exception('Empty query in QPD-3')
             }
         } else { 
             // for ITI-9 (PIX Query)
             exceptions += checkPatientId(msg.QPD[3])
         }
         exceptions
     }
     
     /**
      * Validates segment RCP.
      */
     static def checkRCP(msg) {
         msg.RCP?.value ? [] : [new Exception('Missing segment RCP')]
     }

     
     
     // --------------- Fine grained validation of particular fields ---------------

     /**
      * Validates patient name (datatype XPN).
      */
     static def checkPatientName(xpn) {
         def exceptions = []
         if( ! (xpn[1].value || xpn[2].value)) {
             exceptions += new Exception('Missing patient name')
         }
         exceptions
     }
     
     /**
      * Validates a single patient ID (datatype CX).
      */
     static def checkPatientId(cx) {
         def exceptions = []
         if( ! (cx[1].value && (cx[4][1].value || (cx[4][2].value && (cx[4][3].value == 'ISO'))))) {
             exceptions += new Exception('Incomplete patient ID')
         }
         exceptions
     }
     
     /**
      * Validates patient ID list (datatype repeatable CX).
      */
     static def checkPatientIdList(repeatableCX) {
         def exceptions = []
         repeatableCX().each { cx -> 
             exceptions += checkPatientId(cx) 
         }
         exceptions
     }
     
     /**
      * Validates short patient ID (i.e. without assigning authority, as in PIX Feed).
      */
     static def checkShortPatientId(pid3) {
         def exceptions = []
         if(( ! pid3?.value) || ( ! pid3[1]?.value)) {
             exceptions += new Exception('Missing patient ID')
         }
         exceptions
     }
}

