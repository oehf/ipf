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
package builders.content.document

import groovytools.builder.*
import org.openhealthtools.ihe.common.cdar2.*

// The Continuity of Care document

continuityOfCareDocument(schema:'clinicalDocument') {
	properties {
	    typeId(def:{
	        getMetaBuilder().build {
	            typeId(root:'2.16.840.1.113883.1.3', extension:'POCD_MT000040')
	        }
	    })
		code(schema:'loincCode', def: {
			getMetaBuilder().build {
				loincCode(code:'34133-9',
				displayName:'Summarization of episode note')
			}
		})
		mainActivity(schema:'ccd_serviceEvent', req: true)
		nextOfKin(schema:'ccd_nextOfKin')
		emergencyContact(schema:'ccd_emergencyContact')
		caregiver(schema:'ccd_caregiver')
		component(schema:'ccd_component')
	}
	collections {
		templateIds(collection:'templateId', def: {
			getMetaBuilder().buildList {
				ii(root:'2.16.840.1.113883.10.20.1')
			}
		})
		{
			templateId(schema:'ii')
		}
	}
}

// Add CCD component

ccd_component(schema:'component') {
	properties {
		structuredBody(schema:'ccd_structuredBody')
	}
}

//  Add CCD sections as properties

ccd_structuredBody(schema:'structuredBody') {
	properties {
	    purpose(schema:'ccd_purpose')
	    payers(schema:'ccd_payers')
		advanceDirectives(schema:'ccd_advanceDirectives')
		functionalStatus(schema:'ccd_functionalStatus')
        alerts(schema:'ccd_alerts')
        encounters(schema:'ccd_encounters')
        familyHistory(schema:'ccd_familyHistory')
        socialHistory(schema:'ccd_socialHistory')
        medications(schema:'ccd_medications')
		immunizations(schema:'ccd_immunizations')
		medicalEquipment(schema:'ccd_medicalEquipment')
		problems(schema:'ccd_problems')
		procedures(schema:'ccd_procedures')
		results(schema:'ccd_results')
		vitalSigns(schema:'ccd_vitalSigns')
		planOfCare(schema:'ccd_planOfCare')
	}
}



