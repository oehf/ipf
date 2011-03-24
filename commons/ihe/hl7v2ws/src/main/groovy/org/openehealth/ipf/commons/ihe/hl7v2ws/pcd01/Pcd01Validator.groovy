/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01


import java.util.Collection
import java.util.Map

import org.openehealth.ipf.commons.core.modules.api.ValidationException
import org.openehealth.ipf.commons.ihe.pixpdq.AbstractMessageAdapterValidator
import org.openehealth.ipf.modules.hl7.HL7v2Exception
import org.openehealth.ipf.modules.hl7.validation.support.DefaultTypeRulesValidationContext

import ca.uhn.hl7v2.HL7Exception
import ca.uhn.hl7v2.validation.MessageValidator
/**
 * @author Mitko Kolev
 *
 */
class Pcd01Validator extends  AbstractMessageAdapterValidator {
    
   public static DefaultTypeRulesValidationContext VALIDATION_CONTEXT = new DefaultTypeRulesValidationContext();
   
   static {
       VALIDATION_CONTEXT.configure()
       .forVersion('2.6')
          .message('ORU', 'R01').abstractSyntax(
       'MSH',
       [  {  'SFT'  }  ],
       {PATIENT_RESULT(
          [PATIENT(
             'PID',
             [  'PD1'  ],
             [  {  'NTE'  }  ],
             [  {  'NK1'  }  ],
             [VISIT(
                'PV1',
                [  'PV2'  ]
            )]
          )],
          {ORDER_OBSERVATION(
             [  'ORC'  ],
              'OBR',
              [{  'NTE'  }],
              [{TIMING_QTY(
                 'TQ1',
                 [{  'TQ2'  }]
              )}],

              [  'CTD'  ],
              [{OBSERVATION(
                 'OBX',
                 [  {  'NTE'  }  ]
              )}],

              [{  'FT1'  }],
              [{  'CTI'  }],
              [{SPECIMEN(
                 'SPM',
                 [{  'OBX'  }]
              )}]
           )}
        )},
        [ 'DSC' ]
     )
   }
   
   public static  Map<String, Map<String, String>> RULES = 
               [ 'ORU' : ['R01' : 'MSH PATIENT_RESULT'],
                 'ACK' : ['*'   : 'MSH MSA']
               ] 
 
   Map<String, Map<String, String>> getRules(){
       return RULES
   } 
        
  /**
   * Validates segment PID in PATIENT_RESULT.PATIENT.
   */
    void checkPATIENT_RESULT(msg, Collection<Exception> violations) {
        checkStructure(msg, violations)
        for (prGroup in msg.PATIENT_RESULT()){
            if (!prGroup.PATIENT.PID.isEmpty()) {
                checkPID(prGroup.PATIENT, violations)
            }
            if (prGroup.ORDER_OBSERVATION().isEmpty()){
                violations.add(new Exception("Missing OBR segment"))
            }
            for (ooGroup in prGroup.ORDER_OBSERVATION()){
                checkSegmentStructure(ooGroup, 'OBR', [1, 3, 4], violations)
                if (ooGroup.OBSERVATION().isEmpty()){
                    violations.add(new Exception("Missing OBX segment"))
                }
                for (obsGroup in ooGroup.OBSERVATION()){
                    checkSegmentStructure(obsGroup, 'OBX', [1, 3, 4, 11], violations)
                }
            }
        }
    }
    
    private void checkStructure(Object msg, Collection<Exception> violations){
        try {
            new MessageValidator(VALIDATION_CONTEXT, true).validate(msg.target)
        }catch (Exception e) {
            //Note, that it if the structure is incorrect, no validation should be done
            violations.add(e)
        }
    }
}
