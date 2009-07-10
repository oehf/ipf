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
package org.openehealth.ipf.modules.cda.builder

import org.openehealth.ipf.modules.cda.builder.CDAR2ModelExtension
import org.openehealth.ipf.modules.cda.CDAR2Renderer
import org.eclipse.emf.ecore.xmi.XMLResource
import org.openhealthtools.ihe.common.cdar2.*
import org.openhealthtools.ihe.common.cdar2.impl.*

/**
 * @author Stefan Ivanov
 */
public class CDAR2BuilderStructureSubstanceAdministrationTest extends AbstractCDAR2BuilderTest {
	
	
	/**
	 * Test simple SubstanceAdministration
	 */
	public void testSubstanceAdministration() {
		def entry = builder.build {
			entry{
			    substanceAdministration(classCode:'SBADM', moodCode:'EVN'){
                    id(root:'2.16.840.1.113883.19.8.1')
                    text('Prednisone 20mg qd')
                    routeCode( 
                        code:'PO',
                        codeSystem:'2.16.840.1.113883.5.112',
                        codeSystemName:'RouteOfAdministration'
                    )//route code
                    doseQuantity(value:'20',unit:'mg')
                    consumable{
                        manufacturedProduct{
                            manufacturedLabeledDrug{
                                code(
                                    code:'10312003',
                                    codeSystem:'2.16.840.1.113883.6.96' ,
                                    codeSystemName:'SNOMED CT',
                                    displayName:'Prednisone preparation')
                            }
                        }
			        }//consumable
			    }
			}//entry
		}
		// TODO add some assertions
	}
	
	/**
	 * Test substanceAdministration defaults
	 */
	public void testSubstanceAdministrationDefaultValues() {
		def entry = builder.build {
			entry{
			    substanceAdministration(classCode:'PROC', moodCode:'EVN'){
			        consumable{
			            manufacturedProduct{
			                manufacturedLabeledDrug{
			                    code(
	                                code:'10312003',
	                                codeSystem:'2.16.840.1.113883.6.96' ,
	                                codeSystemName:'SNOMED CT',
	                                displayName:'Prednisone preparation')
			                }
			            }
			        }//consumable
			    }//substance administration
			}//entry
		}
		//TODO add some assertions
	}	

}