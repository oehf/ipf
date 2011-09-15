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

import org.openehealth.ipf.commons.ihe.hl7v2.AbstractMessageAdapterValidator
import org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01.rules.CNERule
import org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01.rules.CWERule
import org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01.rules.CXRule
import org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01.rules.EIRule
import org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01.rules.HDRule
import org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01.rules.XPNRule
import org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01.rules.XTNRule
import org.openehealth.ipf.modules.hl7.validation.DefaultValidationContext
import org.openehealth.ipf.modules.hl7.validation.builder.MessageRuleBuilder
import org.openehealth.ipf.modules.hl7dsl.GroupAdapter
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter

/**
 * Implements validaton for PCD, PCD-01 as defined in 
 * "IHE Patient Care Device (PCD) Technical Framework 5  Volume 2  (PCD TF-2)  Revision 1.2"
 * 
 * @author Mitko Kolev
 *
 */
class Pcd01Validator extends  AbstractMessageAdapterValidator {
 
   static DefaultValidationContext PCD01_CONTEXT = new DefaultValidationContext()
   static {
       MessageRuleBuilder.metaClass.applyPCD01TypeValidation = {
           delegate.checkCompositesWith(new CXRule())
           delegate.checkCompositesWith(new CNERule())
           delegate.checkCompositesWith(new CWERule())
           delegate.checkCompositesWith(new EIRule())
           delegate.checkCompositesWith(new HDRule())
           delegate.checkCompositesWith(new XPNRule())
           delegate.checkCompositesWith(new XTNRule())
           delegate
       }
       
       PCD01_CONTEXT.configure()
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
        [ 'DSC' ]).applyPCD01TypeValidation()
   }
 
   public static  Map<String, Map<String, String>> RULES = 
               [ 'ORU' : ['R01' : 'MSH PATIENT_RESULT'],
                 'ACK' : ['*'   : 'MSH MSA']
               ]

    //TODO integrate within the validation context with closure rules
    //based on groups or segments
    Map<String, Map<String, String>> getRules(){
        return RULES
    }
   
    DefaultValidationContext getValidationContext(){
       PCD01_CONTEXT
    }

   
    
    /**
     * Validates segment PID in PATIENT_RESULT.PATIENT.
     */
    void checkPATIENT_RESULT(msg, Collection<Exception> violations) {
        
        msg.PATIENT_RESULT().eachWithIndex(){  prGroup, i ->
           
            //allow empty patient groups
            if (!prGroup.PATIENT.isEmpty()){
                checkPID(prGroup.PATIENT, violations)
                checkVISIT(prGroup.PATIENT, violations)
            }
            
            if (prGroup.ORDER_OBSERVATION().isEmpty()){
                violations.add(new Exception("No ORDER_OBSERVATIONs found."))
            }
            
            prGroup.ORDER_OBSERVATION().eachWithIndex(){  ooGroup, t ->
                ooGroup.withPath(prGroup, t) //add explicitly, because the path tracking works only with the field like access
                checkORDER_OBSERVATION(ooGroup, t + 1, violations)
            }
        }
    }
    
    void checkORDER_OBSERVATION(GroupAdapter ooGroup, int obrIndex, Collection<Exception> violations) {
        checkOBR(ooGroup, obrIndex, violations)
        boolean checkTime = shouldValidateOBXTime(ooGroup, violations)
        
        ooGroup.OBSERVATION().eachWithIndex() { obs, int i ->
            obs.withPath(ooGroup, i) //add explicitly, because the path tracking works only with the field like access
            checkOBSERVATION(obs, i + 1, checkTime, violations)
        }
    }
    
    /*
    * Check patient name only for structure.
    */
    void checkPID(GroupAdapter patientGroup, Collection<Exception> violations) {
        checkSegmentStructure( patientGroup, 'PID', [3, 5], violations)
    }
    
    void checkVISIT(GroupAdapter patient, Collection<Exception> violations) {
        if (!patient.VISIT.isEmpty()){
            if (!patient.VISIT.PV1.isEmpty()){
                checkSegmentStructure( patient.VISIT, 'PV1', [2], violations)
            }
        }
    }
    
    void checkOBR(GroupAdapter ooGroup, int expectedObrIndex, Collection<Exception> violations) {
        checkSegmentValues(ooGroup, 'OBR', [1, 3, 4], [expectedObrIndex, ANY, ANY], violations)
        checkOBR7NotAfterOBR8(ooGroup, violations)
       
    }
    void checkOBR7NotAfterOBR8(GroupAdapter ooGroup, Collection<Exception> violations){
        //check if OBR-7 is befor OBR-8
        Date obr7Date = null
        Date obr8Date = null
        if (ooGroup.OBR[7].getValue()){
            obr7Date = ooGroup.OBR[7]?.getValueAsDate()
        }
        if (ooGroup.OBR[8].getValue()){
            obr8Date = ooGroup.OBR[8]?.getValueAsDate()
        }
        if(obr7Date && obr8Date && obr7Date.after(obr8Date)){
            violations.add(new Exception("${ooGroup.OBR}.path OBR-7 must be before OBR-8"))
        }
    }
    

    /*
     * The <code>checkTime</code> tells if the OBX-14 will be checked. It is required when OBX-7 is not given
     */
    void checkOBSERVATION(GroupAdapter obs, int expectedObservationIndex, boolean checkTime, Collection<Exception> violations) {
        checkSegmentStructure(obs, 'OBX', getOBXRequiredFields(checkTime), violations);
        //make sure that the observations are in the correct order
        checkSegmentValues(obs, 'OBX', [1], [expectedObservationIndex], violations)
        
        // FIXME although stated in the spec
        //checkOBX2WhenOBX11NotX(obs, violations)
        checkOBX6WhenOBX5HasValue(obs, violations)
        checkOBX2InAllowedDomain(obs,violations)
        checkOBX11InAllowedDomain(obs,violations)
    }
    
    void checkOBX2InAllowedDomain(obs,Collection<Exception> violations){
        if (obs.OBX[2].value) {
            //OBX [2] must have the same name as OBX[5]. This is guaranteed by the parser.
            checkFieldInAllowedDomain(obs, 'OBX', 2, ['CD', 'CF', 'DT', 'ED', 'FT', 'NA', 'NM', 'PN', 'SN', 'ST', 'TM', 'DTM', 'XCN'], violations);
        }
    }
    void checkOBX11InAllowedDomain(obs,Collection<Exception> violations){
        checkFieldInAllowedDomain(obs, 'OBX', 11, ['C', 'D', 'F', 'P', 'R', 'S', 'U', 'W', 'X'], violations);
    }
    
    void checkOBX2WhenOBX11NotX(obs, Collection<Exception> violations){
        def obx11 = obs.OBX[11]
        if (obx11 != 'X'){
            def obx2 = obs.OBX[2]
            if(!obx2){
                violations.add(new Exception("${obs.path} OBX-2 must be valued if the value of OBX-11 is not X"))
            }
        }
    }
    void checkOBX6WhenOBX5HasValue(obs, Collection<Exception> violations){
        def obx5 = obs.OBX[5]
        if (!obx5.isEmpty()){
            def obx6 = obs.OBX[6]
            if(obx6.isEmpty()){
                violations.add(new Exception("${obs.path} if OBX-5 is populated then OBX-6 must contain an appropriate value."))
            }
        }
    }
   
    
    Collection getOBXRequiredFields(boolean checkTime){
        Collection fields = [1, 3, 4]
        if (checkTime) {
            fields.add(14)
        }
        fields
    }
    
 
    boolean shouldValidateOBXTime(GroupAdapter ooGroup, Collection<Exception> violations) {
        def val = ooGroup?.OBR[7]?.value
        return (! val) || '0000'.equals(val)
    }
    
     /////////////////////// Response //////////////////////////
     
    void checkMSA(MessageAdapter msg, Collection<Exception> violations) {
        super.checkMSA(msg, violations)
        checkFieldInAllowedDomain(msg, 'MSA', 1, ['CA', 'CE', 'CR', 'AA', 'AR'], violations)

        String errorCode = msg.MSA[1].toString();
        if (!('AA'.equals(errorCode) || 'CA'.equals(errorCode))){
            checkERR(msg,violations)
        }
    }
 
    void checkMSH(MessageAdapter msg, Collection<Exception> violations) {
        checkSegmentStructure(msg, 'MSH', [1, 2, 3, 7, 9, 10, 11, 12, 15, 16, 21], violations)
    }
   
}
