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
 * Implements more strict validaton for PCD-01
 * 
 * There must be at least one ORDER_OBSERVATION in the message
 * There must be at least one PATIENT_RESULT
 * 
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
        
        msg.PATIENT_RESULT().eachWithIndex(){  prGroup, i ->
			prGroup.withPath(msg, i)
			
			//allow empty patient groups
			if (!prGroup.PATIENT.isEmpty()){
				checkPID(prGroup.PATIENT, violations)
			}
			
			if (prGroup.ORDER_OBSERVATION().isEmpty()){
				violations.add(new Exception("No ORDER_OBSERVATIONs found."))
			}
			
			prGroup.ORDER_OBSERVATION().eachWithIndex(){  ooGroup, t ->
				ooGroup.withPath(prGroup, t)
				checkORDER_OBSERVATION(ooGroup, t + 1, violations)
			}
		}
    }
	
	void checkORDER_OBSERVATION(ooGroup, int obrIndex, Collection<Exception> violations) {
		checkOBR(ooGroup, obrIndex, violations)
		boolean checkTime = shouldValidateOBXTime(ooGroup, obrIndex, violations)
		
		ooGroup.OBSERVATION().eachWithIndex() { obs, i ->
			obs.withPath(ooGroup, i)
			checkOBSERVATION(obs, checkTime, violations)
		}
		
		if (ooGroup.SPECIMEN().isEmpty()){
			return;
		}
		
		ooGroup.SPECIMEN().eachWithIndex() { specimen, i ->
			specimen.withPath(ooGroup, i)
			checkSPECIMEN(specimen, i + 1, checkTime, violations)
		}
	}
    
	/*
	* Check patient name only for structure.
	*/
	void checkPID(patientGroup, Collection<Exception> violations) {
		checkSegmentStructure( patientGroup, 'PID', [3, 5], violations)
	}
	
   
	void checkOBR(ooGroup, int obrIndex, Collection<Exception> violations) {
		checkSegmentValues(ooGroup, 'OBR', [1, 3, 4], [obrIndex, ANY, ANY, ANY, ANY, ANY], violations)
	}
	/*
	 * The <code>checkTime</code> tells if the OBX-14 will be checked. It is requred when OBS-7 is not given
	 */
	void checkOBSERVATION(obs, boolean checkTime, Collection<Exception> violations) {
		checkSegmentStructure(obs, 'OBX', getOBXRequiredFields(obs.OBX, checkTime), violations);
		if (obs.OBX[2].value){
			//OBX [2] must have the same name as OBX[5]. This is guaranteed by the parser.
			checkFieldInAllowedDomain(obs,  'OBX', 2, ['CD', 'CF', 'DT', 'ED', 'FT', 'NA', 'NM', 'PN', 'SN', 'ST', 'TM', 'DTM', 'XCN'], violations);
		}
		
	}
	
	void checkSPECIMEN(specimen, int specimenIndex, boolean checkTime, Collection<Exception> violations) {
		checkSegmentValues(specimen, 'SPM', [1, 4], [specimenIndex, ANY], violations)
		Collection obs = specimen.OBX();
		if(!obs.isEmpty()){
			obs.eachWithIndex() { obx, i ->
				checkSegmentStructureAtRepetition(specimen, 'OBX', i + 1, getOBXRequiredFields(obx,checkTime), violations)
			}
		}
	}
	
	Collection getOBXRequiredFields(obx, boolean checkTime){
		boolean shouldValidateOBX6 = obx[5].value == null ? false : true;
		
		if (shouldValidateOBX6){
			if (checkTime){
				return  [1, 3, 4, 6, 11, 14]
			 } else {
				   return [1, 3, 4, 6, 11]
			 }
		}else {
			if (checkTime){
			   return  [1, 3, 4, 11, 14]
			} else {
				  return [1, 3, 4, 11]
			}
		}
	}
	
 
	boolean shouldValidateOBXTime(ooGroup, int obrIndex, Collection<Exception> violations) {
		def val = ooGroup.OBR[7].value;
		return val == null || '0000'.equals(val)
	}
	
	 /////////////////////// Response //////////////////////////
	 
	 void checkMSA(msg, Collection<Exception> violations) {
		 super.checkMSA(msg, violations)
		 checkFieldInAllowedDomain(msg, 'MSA', 1, ['CA', 'CE', 'CR', 'AA', 'AR'], violations)
		 
		 String errorCode = msg.MSA[1].toString();
		 if (!('AA'.equals(errorCode) || 'CA'.equals(errorCode))){
			 checkERR(msg,violations)
		 }
	 }
	 
}
