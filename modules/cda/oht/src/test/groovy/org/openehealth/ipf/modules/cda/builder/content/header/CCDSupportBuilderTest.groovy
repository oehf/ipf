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
package org.openehealth.ipf.modules.cda.builder.content.header

import org.openhealthtools.ihe.common.cdar2.POCDMT000040RecordTarget
import org.openhealthtools.ihe.common.cdar2.POCDMT000040AssociatedEntity
import org.junit.Before
import org.junit.Test
import org.openehealth.ipf.modules.cda.builder.content.document.CCDDefinitionLoader
import org.openehealth.ipf.modules.cda.builder.AbstractCDAR2BuilderTest

/**
 * @author Stefan Ivanov
 */
public class CCDSupportBuilderTest extends AbstractCDAR2BuilderTest {
	
	@Before
	void initialize() throws Exception {
        new CCDDefinitionLoader(builder).loadSupport(loaded)
		new CCDSupportExtension(builder).register(registered)
	}
	
	@Test
	public void testCCDSupportGuardian() {
	    def POCDMT000040RecordTarget recordTarget = builder.build {
               recordTarget {
	                    patientRole {
	                        id('996-756-495@2.16.840.1.113883.19.5') 
	                        patient {
	                            name {
	                                given('Henry') 
	                                family('Levin')
	                                suffix('the 7th')
	                            }
	                            administrativeGenderCode('M')
	                            birthTime('19320924')
	                            guardian{//support gardian
	                                code(code:'guardian code', displayName:'Guardian Entry')
	                                guardianPerson{
	                                    name {
	                                        given('Guardian') 
	                                        family('Person')
	                                    }
	                                }
	                            }//guardian
	                        }//patient
	                        providerOrganization {
	                            id('2.16.840.1.113883.19.5')
	                            name('Good Health Clinic')
	                        }
	                    }//patient role
	                }//record target               
	        }
	    assert recordTarget.patientRole.patient.guardian != null
	}
	
	
	@Test
    public void testCCDSupportNextOfKin() {
        def POCDMT000040AssociatedEntity entity = builder.build {
            ccd_nextOfKin{
              id(root:'4ac71514-6a10-4164-9715-f8d96af48e6d')
              code(code:'65656005', codeSystem:'2.16.840.1.113883.6.96', displayName:'Biiological mother')
              telecom(value:'tel:(999)555-1212')
              associatedPerson{
                  name{
                      given('Henrietta')
                      family('Levin')
                  }
              }
          }//next of kin             
        }
        new CCDSupportValidator().doValidateNextOfKin(entity)
    }
	
	@Test
    public void testCCDSupportEmergencyContact() {
        def POCDMT000040AssociatedEntity entity = builder.build {
            ccd_emergencyContact{
                  id(root:'4ac71514-6a10-4164-9715-f8d96af48e6f')
                  associatedPerson{
                      name{
                          given('Baba')
                          family('John')
                      }
                  } 
            }             
        }
        new CCDSupportValidator().doValidateEmergencyContact(entity)
    }
	
	@Test
    public void testCCDSupportCaregiver() {
        def POCDMT000040AssociatedEntity entity = builder.build {
            ccd_caregiver{
                scopingOrganization{
                    name('Very Good Health Clinic')
                }
            }       
        }
        new CCDSupportValidator().doValidateCaregiver(entity)
    }
	
}
