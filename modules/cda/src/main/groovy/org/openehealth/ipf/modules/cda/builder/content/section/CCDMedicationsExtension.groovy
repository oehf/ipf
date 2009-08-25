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


/**
 * Chapter 3.9 "Medications".
 *
 * Templates included:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.8 Medications
 * </ul>
 * Dependent templates:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.24 Medication Activity
 * <li>2.16.840.1.113883.10.20.1.34 Supply Activity
 * <li>2.16.840.1.113883.10.20.1.40 Comment 
 * </ul>
 *
 *
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDMedicationsExtension extends CompositeModelExtension {
	
	CCDMedicationsExtension() {
		super()
	}
	
	CCDMedicationsExtension(builder) {
		super(builder)
	}
	
	def register(Collection registered) {
		
	    super.register(registered)

        POCDMT000040ClinicalDocument.metaClass {
            setMedications { POCDMT000040Section section ->
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
            getMedications { ->
                delegate.component?.structuredBody?.component.find {
                    it.section?.code?.code == '10160-0'
                } ?.section
            }
        }
				
		POCDMT000040StructuredBody.metaClass {

			setMedications  {POCDMT000040Section section ->
				POCDMT000040Component3 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component3()
				component.section = section
				delegate.component.add(component)
			}
			getMedications  { ->
				delegate.component.find { 
					it.section.code.code == '10160-0'
				} ?.section
			}
		}
				
	}//ccd extensions 
	
	
	String extensionName() {
		'CCD Medications'
	}
	
	String templateId() {
		'2.16.840.1.113883.10.20.1.8'
	}
	
	Collection modelExtensions() {
	    [ new CCDMedicationActivityExtension(),
	      new CCDSupplyActivityExtension(),
          new CCDCommentsExtension()]
	}


}
