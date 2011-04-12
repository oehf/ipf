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

package org.openehealth.ipf.modules.metabuilder

import groovytools.builder.*

/**
 * Tests the Meta Builder Framework
 * 
 * @author Stefan Ivanov
 */
class CustomerTest extends GroovyTestCase {
	
	MetaBuilder mb = new MetaBuilder(getClass().getClassLoader())
	
	/**
	 * Tests default collections values and 
	 */
	public void testCollectionsDef() {
		
		/* define */
		mb.define{
			phone(factory:Phone){
				properties {
					type(check: ['home','cell','work'], def: 'home')
					number(req: true)
				}
			}
		}
		
		mb.define{
			cellPhone(schema:'phone'){
				properties {
					type(def: 'cell')
				}
			}
		}
		
		mb.define{
			customer(factory:Customer){
				properties{
					name()
					dateOfBirth(def:'1.1.2000')
					ssn()
					type()
					partner(schema:'customer')
				}
				collections{
					phones(collection:'phone', min:1, def:{
						getMetaBuilder().buildList {
							phone(number:'-DefCustomerPhone-')
						}
					}){
						phone(schema:'cellPhone')
					}
				}
			}
		}
		
		mb.define {
			company(schema:'customer') {
				properties {
					type(req:true, def:'company')
				}
				collections{
					phones(collection:'phone', def:{
						getMetaBuilder().buildList {
							phone(number:'-DefCompanyPhone-')
						}
					}){
						phone(schema:'cellPhone')
					}
				}
			}
		}
		
		/* start unsing definition */
		def iCustomer = mb.build{
			customer(name:'GOGO', type:'free'){
				phone(number:'0000-company')
			}//customer
		}
		
		def iCompany = mb.build {
			company(name: 'BSP', ssn: '555-55-5555') {
				phone(number:'+0000')
				partner(name:'GOGO Partner', type:'relative'){
					phone(type:'home', number:'+9999')
					phone(number:'+9999')
				}//partner
			}//company
		}
		 
		assert iCustomer != null
		assert iCompany != null
				
	}
	

	/**
	 * Tests collection constraints propagation.
	 */
    public void testCollectionConstraints() {

        /* define */
        mb.define{
            phone(factory:Phone){
                properties {
                    type(check: ['home','cell','work'], def: 'home')
                    number(req: true)
                }
            }
        }

        mb.define{
            cellPhone(schema:'phone'){
                properties {
                    type(def: 'cell')
                }
            }
        }

        mb.define{
            customer(factory:Customer){
                properties{
                    name()
                    dateOfBirth(def:'1.1.2000')
                    ssn()
                    type()
                    partner(schema:'customer')
                }
                collections{
                    phones(collection:'phone'){
                        phone(schema:'cellPhone')
                    }
                }
            }
        }

        mb.define {
            customer1(schema:'customer') {
                properties {
                    type(req:true, def:'company')
                    partner(schema:'customer2')
                }
            }
        }

        mb.define {
            customer2 (schema:'customer'){
                 collections{
                    phones(collection:'phone', min:0){
                        phone(schema:'cellPhone')
                    }
                }
            }
        }

        /* start use meta builder definitions */
        def iCustomer1WithPartnerCustomer2 = mb.build {
            customer1(name: 'BSP', ssn: '555-55-5555') {
                phone(number:'+0000')
                partner(name:'GOGO Partner', type:'relative'){
                    phone(number:'+9998')
                }//partner
            }//company
        }
        assert iCustomer1WithPartnerCustomer2 != null
        
    }
}