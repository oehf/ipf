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
package org.openehealth.ipf.modules.cda.builder.content.entry

import org.openhealthtools.ihe.common.cdar2.*
import org.openehealth.ipf.modules.cda.builder.CompositeModelExtension


/**
 * Chapter 3.14.2 "Procedure Activity" (2.16.840.1.113883.10.20.1.29)
 * 
 * Templates included:
 * <ul>
 * </ul>
 * Dependent templates:
 * <ul>
 * <li>                             Information Source
 * <li>2.16.840.1.113883.10.20.1.40 Comment 
 * <li>2.16.840.1.113883.10.20.1.49 Patient Instruction
 * <li>2.16.840.1.113883.10.20.1.48 Patient Awareness
 * <li>2.16.840.1.113883.10.20.1.52 Product Instance
 * <li>2.16.840.1.113883.10.20.1.24 Medication Activity
 * <li>2.16.840.1.113883.10.20.1.27 Problem Act
 * <li>2.16.840.1.113883.10.20.1.28 Problem Observation
 * <li>2.16.840.1.113883.10.20.1.45 Encounter Location
 * </ul>
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDProcedureActivityExtension extends CompositeModelExtension {
	
	CCDProcedureActivityExtension() {
		super()
	}
	
	CCDProcedureActivityExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
	    
	    super.register(registered)
	    
		POCDMT000040Section.metaClass {
			
			setProcedureActivity {POCDMT000040Entry entry ->
				entry.typeCode = XActRelationshipEntry.DRIV_LITERAL
				delegate.entry.add(entry)
			}
			
			getProcedureActivity { ->
				delegate.entry
			}
			
		}//procedure activity extensions
		
		POCDMT000040Entry.metaClass {
		    setProcedureActivityAct {POCDMT000040Act act ->
                delegate.act = act
		    }
        
		    getProcedureActivityAct { ->
                delegate.act
		    }
		    
		    setProcedureActivityObservation {POCDMT000040Observation obs ->
                delegate.observation = obs
		    }
    
		    getProcedureActivityObservation { ->
		        delegate.observation
		    }
        
		    setProcedureActivityProcedure {POCDMT000040Procedure proc ->
		        delegate.procedure = proc
		    }

		    getProcedureActivityProcedure { ->
		        delegate.procedure
		    }
		}
		
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Procedure Activity'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.29'
	}	
	
	Collection modelExtensions() {
        [ new CCDEncounterLocationExtension(),
          new CCDProblemActExtension(),
          new CCDProblemObservationExtension(),
          new CCDProductInstanceExtension(),
          new CCDPatientAwarenessExtension(),
          new CCDPatientInstructionExtension(),
          new CCDAgeObservationExtension(),
          new CCDMedicationActivityExtension(),
          new CCDSourceExtension(),
          new CCDCommentsExtension()
        ]
	}
}
