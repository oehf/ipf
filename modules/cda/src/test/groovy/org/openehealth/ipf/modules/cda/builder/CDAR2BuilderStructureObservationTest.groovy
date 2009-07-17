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
public class CDAR2BuilderStructureObservationTest extends AbstractCDAR2BuilderTest {


  /**
   * Test observation defaults
   */
  public void testObservationDefaultValues() {
//    def entry = builder.build {
//      entry {
//        observation() {
//          code(
//                  code: '23426006',
//                  codeSystem: '2.16.840.1.113883.6.96'
//          )//code
//        }//observation
//      }//entry
//    }
//
//    assertTrue(entry.observation.isSetMoodCode())
//    assert entry.observation.moodCode.name == 'INT'
//    assert entry.observation.classCode.name == 'OBS'
//    assertFalse(entry.observation.isSetNegationInd())
//    assertEquals 0, entry.observation.entryRelationship.size
//    assertNull entry.observation.text
  }

  /**
   * Test observation
   */
  public void testComplexObservation() {
    def entry = builder.build {
      entry {
        observation(classCode: 'CNOD', moodCode: 'EVN', negationInd: true) {
          id()
          code(
                  code: '14657009',
                  codeSystem: '2.16.840.1.113883.6.96',
                  codeSystemName: 'SNOMED CT',
                  displayName: 'Established diagnosis'
          )//code
          statusCode(code: 'completed')
          effectiveTime(value: '200004071530')
          value(builder.build {
              ce(nullFlavor: 'NP')
          })
          value(builder.build {
              ce(
                  code: '40275004',
                  codeSystem: '2.16.840.1.113883.6.96',
                  codeSystemName: 'SNOMED CT',
                  displayName: 'Contact dermatitis') {
                      translation(
                              code: '692.9',
                              codeSystem: '2.16.840.1.113883.6.2', codeSystemName: 'ICD9CM',
                              displayName: 'Contact Dermatitis, NOS'
                      )}//translation
              })//value
          methodCode(
                  code: '37931006',
                  codeSystem: '2.16.840.1.113883.6.96',
                  codeSystemName: 'SNOMED CT',
                  displayName: 'Auscultation'
          )//method code
          targetSiteCode(
                  code: '48856004',
                  codeSystem: '2.16.840.1.113883.6.96',
                  codeSystemName: 'SNOMED CT',
                  displayName: 'Skin of palmer surface of index finger'
          ) {
            qualifier {
              name(
                      code: '78615007',
                      codeSystem: '2.16.840.1.113883.6.96',
                      codeSystemName: 'SNOMED CT',
                      displayName: 'with laterality'
              )//name
              value(builder.build {
                  ce(
                      code: '7771000',
                      codeSystem: '2.16.840.1.113883.6.96',
                      codeSystemName: 'SNOMED CT',
                      displayName: 'left'
                    )
              })//value
            }//qualifier
          }//targetSiteCode
        }//observation
      }//entry
    }

    assertEquals 'CNOD', entry.observation.classCode.name
    assertEquals 2, entry.observation.value.size
    assertEquals 1, entry.observation.targetSiteCode.size
  }

  /**
   *
   */
  /*
  public void testObservationReference() {
    def entry = builder.build {
      entry {
        observation(classCode: 'COND', moodCode: 'EVN') {
          code(
                  code: '14657009',
                  codeSystem: '2.16.840.1.113883.6.96',
                  codeSystemName: 'SNOMED CT',
                  displayName: 'Established diagnosis'
          )
          statusCode(code: 'completed')
          effectiveTime(value: '200004071530')
          value(
                  code: '59621000',
                  codeSystem: '2.16.840.1.113883.6.96',
                  codeSystemName: 'SNOMED CT',
                  displayName: 'Essential hypertension'
          ) {
            translation(
                    code: '4019',
                    codeSystem: '2.16.840.1.113883.6.2',
                    codeSystemName: 'ICD9CM',
                    displayName: 'HYPERTENSION NOS'
            )
          }//value
          reference(typeCode: 'ELNK') {
            externalObservation(classCode: 'CNOD') {
              id(root: '2.16.840.1.113883.19.1.37')
            }//externalObservation
          }//reference one
          reference(typeCode: 'SPRT') {
            externalObservation(classCode: 'DGIMG') {
              id(root: '2.16.840.1.113883.19.1.14')
              code(
                      code: '56350004',
                      codeSystem: '2.16.840.1.113883.6.96',
                      codeSystemName: 'SNOMED CT',
                      displayName: 'Chest-X-ray'
              )
            }
          }//reference two
          reference(typeCode: 'SPRT') {
            seperatableInd(value: 'false')
            externalDocument {
              id(root: '2.16.840.1.113883.19.4.789')
              text(mediaType: 'multipart/related') {
                reference(value: 'HTN.cda')
              }
              setId(root: '2.16.840.1.113883.19.7.2465')
              versionNumber(value: '1')
            }//external document
          }//reference three
        }//observation
      }//entry
    }

    //TODO fix this: should be SPRT
    assertEquals 'XCRPT', entry.observation.reference.typeCode[1].name
  }
  */

}