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

import org.openhealthtools.ihe.common.cdar2.POCDMT000040ClinicalDocument
import org.openhealthtools.ihe.common.cdar2.POCDMT000040Section
import org.junit.BeforeClass
import org.junit.Test

import org.openehealth.ipf.modules.cda.builder.content.AbstractContentBuilderTest

/**
 * @author Christian Ohr
 * @author Stefan Ivanov
 */
public class CCDPayersBuilderTest extends AbstractContentBuilderTest {


    @BeforeClass
 	static void initialize() throws Exception {
 		builder().define(getClass().getResource('/builders/content/section/CCDPayersBuilder.groovy'))
 		def extension = new CCDPayersExtension(builder())
 	    extension.extensions.call()
 	}
     
     
     @Test
     void testCCDPayers() {
         POCDMT000040Section payers = builder().build {
             ccd_payers {
                 text {
                     paragraph('Payer information')
                     table(border:'1',width:'100%') {
                        thead {
                           tr {
                              th('Payer name')
                              th('Policy type')
                              th('Covered Party ID')
                              th('Authorizations')
                           }
                        }
                        tbody {
                           tr {
                              td('Good Health Insurance')
                              td('Extended healthcare / Self')
                              td('14d4a520-7aae-11db-9fe1-0800200c9a66')
                              td {
                                 linkHtml(href:'Colonoscopy.pdf', 'Colonoscopy')
                              }
                           }
                        }

                     }                              
                 }
                 coverageActivity {
                     id('1fe2cdd0-7aad-11db-9fe1-0800200c9a66')
                     policyActivity {
                         id('3e676a50-7aac-11db-9fe1-0800200c9a66')
                         code('EHCPOL')
                         payer {
                             id('329fcdf0-7ab3-11db-9fe1-0800200c9a66')
                             representedOrganization {
                                 name('Good Health Insurance')
                             }
                         }
                         coveredParty {
                             id('14d4a520-7aae-11db-9fe1-0800200c9a66')
                             code('SELF')
                         }
                         authorizationActivity {
                             id('f4dce790-8328-11db-9fe1-0800200c9a66')
                             code(nullFlavor:'NA')
                             promise {
                                 procedure(moodCode:'PRMS') {
                                     code(
                                       code:'73761001',
                                       codeSystem:'2.16.840.1.113883.6.96',
                                       displayName:'Colonoscopy'
                                     )
                                 }
                             }
                         }
                     }
                 }
              }
          }
         new CCDPayersValidator().validate(payers, null)
     }


    
}
