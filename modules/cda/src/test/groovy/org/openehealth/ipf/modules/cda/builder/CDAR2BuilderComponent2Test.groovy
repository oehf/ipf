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
 * @author Christian Ohr
 */
public class CDAR2BuilderComponent2Test extends AbstractCDAR2BuilderTest {
  /**
   * Test component nonXMLBody defaults
   */
  public void testNonXMLBodyDefaultValues() {
    def body = builder.build {
      nonXMLBody() {
        text()
      }//non xml body
    }

    assert body.getClassCode().name == 'DOCBODY'
    assert body.getMoodCode().name == 'EVN'
    assert body.isSetMoodCode() == false
    assert body.getText().mediaType == 'text/plain'
    assert body.getText().isSetMediaType() == false
    assert body.getText().getRepresentation().toString() == 'TXT'
    assert body.getText().getIntegrityCheckAlgorithm().getName() == 'SHA1'
    assert body.getText().getText() == null
  }

  /**
   * Test component with nonXMLBody
   */
  public void testNonXMLBody() {
    def body = builder.build {
      nonXMLBody(moodCode: 'EVN', classCode: 'DOCBODY') {
        typeId(
                root: '2.16.840.1.113883.1.3',
                extension: 'type'
        )
        text(
                mediaType: 'application/pdf',
                representation: 'B64',
                'this is the binary value as TXT/B64'
        )
      }
    }
    assert body.getClassCode().toString() == 'DOCBODY'
    assert body.getMoodCode().toString() == 'EVN'
    assert body.getText().getText() == 'this is the binary value as TXT/B64'
  }

  /**
   * Test one of the choice (XOR check)
   */
  public void testChoiceValidation() {
    def componentValid = builder.build {
      component {
        nonXMLBody()
      }
    }
    assert !componentValid.nonXMLBody ^ !componentValid.structuredBody
    assert componentValid instanceof POCDMT000040Component2
    shouldFail {
      builder.build {
        component {nonXMLBody() structuredBody()}
      }
    }
  }

  /**
   * Test the complete clinical document
   */
  public void testBuildClinicalDocument() {
    def document = builder.build {
      clinicalDocument {
        typeId(root: '2.16.840.1.113883.1.3', extension: 'POCD_HD000040')
        templateId(root: '2.16.840.1.113883.3.27.1776')
        id(root: '2.16.840.1.113883.19.4', extension: 'c266')
        code(
                code: '11488-4',
                codeSystem: '2.16.840.1.113883.6.1',
                codeSystemName: 'LOINC',
                displayName: 'Consultation note'
        )
        title('Good Health Clinic Consultation Note')
        effectiveTime('20000407')
        versionNumber(2)
        confidentialityCode(
                code: 'N',
                codeSystem: '2.16.840.1.113883.5.25')
        languageCode(code: 'en_US')
        setId(extension: "BB35", root: "2.16.840.1.113883.19.7")
        recordTarget {
          patientRole {
            id(extension: '12345', root: '2.16.840.1.113883.19.5')
          }
        }
        author()
        /* component with structured content*/
        component {
          nonXMLBody {
            text(
                    mediaType: 'application/pdf',
                    representation: 'B64',
                    'encoded binary value'
            )
          }
          structuredBody {
            component {
              section() {
                code(
                        code: '10164-2',
                        codeSystem: '2.16.840.1.113883.6.1',
                        codeSystemName: 'LOINC'
                )
                title('History of Illness')
                text('a narrative content.')
                entry {
                  act {// (classCode: 'ACT', moodCode: 'INT')
                    id()
                    code(
                            code: '23426006',
                            codeSystem: '2.16.840.1.113883.6.96',
                            codeSystemName: 'SNOMED CT',
                            displayName: 'Pulmonary function test'
                    )//code
                  }//act
                }//entry
              }//section
            }//component
          }//structured body
        }
      }
    }

    def renderer = new CDAR2Renderer()
    def opts = [:]
    opts[XMLResource.OPTION_DECLARE_XML] = true
    opts[XMLResource.OPTION_ENCODING] = 'utf-8'
    // System.out.println(renderer.render(document, opts))
    /* document is invalid actually */
    assertFalse(document.component.nonXMLBody == null ^ document.component.structuredBody == null)
  }


}