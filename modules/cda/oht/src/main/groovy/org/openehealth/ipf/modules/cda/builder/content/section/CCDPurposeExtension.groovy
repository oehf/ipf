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
import org.openehealth.ipf.modules.cda.builder.content.entry.*
import org.openehealth.ipf.modules.cda.builder.CompositeModelExtension

/**
 * Chapter 2.8 "Purpose"
 * 
 * Templates included:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.30 Purpose Activity
 * </ul>
 * Dependent templates:
 * <ul>
 * <li>                             Information Source
 * <li>2.16.840.1.113883.10.20.1.40 Comment 
 * </ul>
 * 
 *
 * @author Christian Ohr
 */
public class CCDPurposeExtension extends CompositeModelExtension{

    CCDPurposeExtension() {
        super()
    }
    
    CCDPurposeExtension(builder) {
        super(builder)
    }
    
	def register(Collection registered) {
	    
	    super.register(registered)
	    	    
	    POCDMT000040ClinicalDocument.metaClass {
            setPurpose { POCDMT000040Section section ->
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
            
            getPurpose{ ->
                delegate.component?.structuredBody?.component?.find {
                    it.section?.code?.code == '48764-5'
                } ?.section
            }
            
        }

        POCDMT000040StructuredBody.metaClass {

	        setPurpose  {POCDMT000040Section section ->
                POCDMT000040Component3 component = CDAR2Factory.eINSTANCE.createPOCDMT000040Component3()
                component.section = section
                delegate.component.add(component)
            }
            getPurpose  { ->
                delegate.component.find { 
                    it.section?.code?.code == '48764-5' 
                }?.section
            }
        } //purpose structured body extensions

        POCDMT000040Section.metaClass {
            setPurposeActivity  {POCDMT000040EntryRelationship relationship ->

                POCDMT000040Entry entry = builder.build {
                    // CONF-20: A purpose activity (templateId 2.16.840.1.113883.10.20.1.30)
                    //          SHALL be represented with Act.
                    // CONF-21: The value for “Act / @classCode” in a purpose activity SHALL
                    //          be “ACT” 2.16.840.1.113883.5.6 ActClass STATIC.
                    // CONF-22: The value for “Act / @moodCode” in a purpose activity SHALL
                    //          be “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
                    // CONF-23: A purpose activity SHALL contain exactly one Act / statusCode.
                    // CONF-24: The value for “Act / statusCode” in a purpose activity SHALL
                    //          be “completed” 2.16.840.1.113883.5.14 ActStatus STATIC.
                    // CONF-25: A purpose activity SHALL contain exactly one Act / code,
                    //          with a value of “23745001” “Documentation procedure” 2.16.840.1.113883.6.96
                    //          SNOMED CT STATIC.
                    ccd_entry(typeCode:'DRIV') {
                        act(classCode:'ACT', moodCode:'EVN')  {
                            templateId('2.16.840.1.113883.10.20.1.30')
                            statusCode('completed')
                            code(code:'23745001', codeSystem:'2.16.840.1.113883.6.96',
                                    codeSystemName:'SNOMED CT', displayName:'Documentation procedure')
                        }
                    }
                }

                delegate.entry.add(entry)
                entry.act.entryRelationship.add(relationship)
            }

            getPurposeActivity { ->
                delegate.entry?.act?.findAll { 
                    '2.16.840.1.113883.10.20.1.30' in it.templateId.root
                }?.entryRelationship[0]
            }
        }// purpose section extensions
        
        
    }//ccd purpose extensions 
    
    
    String extensionName() {
        'CCD Purpose'
    }
    
    String templateId() {
        '2.16.840.1.113883.10.20.1.13'
    }
    
	Collection modelExtensions() {
		[ new CCDSourceExtension(),
		  new CCDCommentsExtension()
		]
	}    
    
}
