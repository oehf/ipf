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

/**
 * @author Christian Ohr
 */
public class CDAR2NarrativeTest extends AbstractCDAR2BuilderTest {


     public void testListWithItems() {
         StrucDocText text = builder.build {
            strucDocText {
               list {
                  item('Theodur 200mg BID')
                  item('Proventil inhaler 2puffs QID PRN')
                  item('Prednisone 20mg qd')
                  item('HCTZ 25mg qd')
               }
            }
         }
         assert text != null
         assert text.mixed[0].value.item[0].text == 'Theodur 200mg BID'
         assert text.mixed[0].value.item[1].text == 'Proventil inhaler 2puffs QID PRN'
     }

     public void testListWithContentItems() {
         StrucDocText text = builder.build {
             strucDocText {
                list {
                   item {
                      content(ID:'a1', 'Asthma')
                   }
                   item {
                      content(ID:'a2', 'Hypertension (see HTN.cda for details)')
                   }
                   item {
                      content(ID:'a3', 'Osteoarthritis, ') {
                         content(ID:'a4', 'right knee')
                      }
                   }
                }
             }
         }

        assert text != null
        assert text.mixed[0].value.item[0].mixed[0].value.iD == 'a1'
        assert text.mixed[0].value.item[0].mixed[0].value.text == 'Asthma'
        assert text.mixed[0].value.item[1].mixed[0].value.iD == 'a2'
        assert text.mixed[0].value.item[1].mixed[0].value.text == 'Hypertension (see HTN.cda for details)'
     }

   public void testTable1() {
      StrucDocText text = builder.build {
       strucDocText {
         table {
            tbody {
               tr {
                  th('Date / Time')
                  th('April 7, 2000 14:30')
                  th('April 7, 2000 15:30')
               }
               tr {
                  th('Height')
                  td('177 cm (69.7 in)')
               }
               tr {
                  th('Weight')
                  td('194.0 lbs (88.0 kg)')
               }
            }

         }
      }
     }
     assert text != null
     assert text.mixed[0].value.tbody[0].group[0].value.group[0].value.text == 'Date / Time'
   }

   public void testTable2() {
      StrucDocText text = builder.build {
       strucDocText {
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
     }
     assert text != null
     assert text.mixed[0].value.text == 'Payer information'
     assert text.mixed[1].value.width == '100%'
     assert text.mixed[1].value.thead.group[0].value.group[0].value.text == 'Payer name'
     assert text.mixed[1].value.tbody[0].group[0].value.group[3].value.mixed[0].value.href == 'Colonoscopy.pdf'
     assert text.mixed[1].value.tbody[0].group[0].value.group[3].value.mixed[0].value.text == 'Colonoscopy'      
   }



   public void testRenderMultimedia() {
     StrucDocText text = builder.build {
       strucDocText('Erythematous rash, palmar surface, left index finger.') {
         renderMultiMedia(referencedObject:['MM1'])  // multi-valued primitive attribute
      }
     }
     assert text != null
     assert text.text == 'Erythematous rash, palmar surface, left index finger.'
     assert text.mixed[1].value.referencedObject[0] == 'MM1'
   }
    
}
