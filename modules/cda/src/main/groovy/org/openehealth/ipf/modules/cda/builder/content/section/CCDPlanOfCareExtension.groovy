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
package org.openehealth.ipf.modules.cda.builder.content.section

import org.openhealthtools.ihe.common.cdar2.*
import org.openehealth.ipf.modules.cda.builder.CompositeModelExtension
import org.openehealth.ipf.modules.cda.builder.content.entry.*
import org.openehealth.ipf.modules.cda.builder.content.entry.CCDMedicationActivityExtension
import org.openehealth.ipf.modules.cda.builder.content.entry.CCDProcedureActivityExtension


/**
 * Chapter 3.16 "Plan Of Care"
 *  
 * Templates included:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.25 Plan Of Care Activity
 * </ul>
 * Dependent templates:
 * <ul>
 * <li>                             Information Source
 * <li>2.16.840.1.113883.10.20.1.40 Comments  
 * </ul>
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDPlanOfCareExtension extends CompositeModelExtension {
	
     CCDPlanOfCareExtension() {
		super()
	}
	
     CCDPlanOfCareExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
		
	    super.register(registered)

        POCDMT000040ClinicalDocument.metaClass {
            setPlanOfCare { POCDMT000040Section section ->
                if (delegate.component?.structuredBody){
                    delegate.component.structuredBody.component.add(builder.build {
                        sections(section:section)
                    })
                } else {
                    delegate.component = builder.build {
                        ccd_component{
                            structuredBody {
                                component(section:section)
                            }
                        }
                    }
                }
            }
            getPlanOfCare { ->
                delegate.component?.structuredBody?.component.find {
                    it.section?.code?.code == '18776-5'
                } ?.section
            }
        }
        	
		POCDMT000040StructuredBody.metaClass {
			setPlanOfCare  {POCDMT000040Section section ->
				POCDMT000040Component3 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component3()
				component.section = section
				delegate.component.add(component)
			}
			getPlanOfCare  { ->
				delegate.component.find {
					it.section.code.code == '18776-5'
				} ?.section
			}
		}
	    
		POCDMT000040Section.metaClass {			
			setPlanOfCareActivity {POCDMT000040Entry entry ->
				entry.typeCode = XActRelationshipEntry.DRIV_LITERAL
				delegate.entry.add(entry)
			}			
			getPlanOfCareActivity { ->
				delegate.entry
			}
		}
		
	}
		
	String extensionName() {
		'CCD Plan Of Care'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.10'
	}
	
	Collection modelExtensions() {
		[ new CCDSourceExtension(),
		  new CCDCommentsExtension(),
		  new CCDMedicationActivityExtension(),
		  new CCDSupplyActivityExtension(),
		  new CCDEncounterActivityExtension()
		]
	}
}
