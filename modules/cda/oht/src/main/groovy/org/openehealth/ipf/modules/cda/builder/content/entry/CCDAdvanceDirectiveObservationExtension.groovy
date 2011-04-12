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
package org.openehealth.ipf.modules.cda.builder.content.entry

import org.openhealthtools.ihe.common.cdar2.*
import org.openehealth.ipf.modules.cda.builder.CompositeModelExtension
import org.openehealth.ipf.modules.cda.builder.content.entry.*

/**
 * Chapter 3.2.2.1 "Advance directive observations".
 *
 * Templates included:
 * <ul>
 * <li>2.16.840.1.113883.10.20.1.17 Advance Directive Observation
 * <li>2.16.840.1.113883.10.20.1.58 Advance Directive Observation Verification
 * <li>2.16.840.1.113883.10.20.1.36 Advance Directive Reference
 * <li>2.16.840.1.113883.10.20.1.37 Advance Directive Status Observation
 * <li>
 * </ul>
 * Dependent templates:
 * <ul>2.16.840.1.113883.10.20.1.57 Status Observation
 * <li>                             Information Source
 * <li>2.16.840.1.113883.10.20.1.40 Comment 
 * </ul>
 * 
 * @author Stefan Ivanov
 * @author Christian Ohr
 */
public class CCDAdvanceDirectiveObservationExtension extends CompositeModelExtension {
    
    CCDAdvanceDirectiveObservationExtension() {
        super()
    }
    
    CCDAdvanceDirectiveObservationExtension(builder) {
        super(builder)
    }
    
    def register(Collection registered) {
         
        super.register(registered)

        POCDMT000040Section.metaClass {
            setAdvanceDirectiveObservation { POCDMT000040Observation observation ->
                delegate.entry.add(builder.build {
                    ccd_entry(typeCode:'DRIV', observation:observation)
                })
            }
            
            getAdvanceDirectiveObservation { ->
                delegate.entry.findAll{ 
                    templateId() in it.observation?.templateId?.root
                }?.observation
            }
        }// advance directives section extensions

        POCDMT000040Observation.metaClass {
            
            // required by Advance Directive Observation Verification (2.16.840.1.113883.10.20.1.58)
            setVerifier  { POCDMT000040Participant2 participant ->
                delegate.participant.add(participant)
            }
            getVerifier { ->
                delegate.participant?.findAll{ 
                    '2.16.840.1.113883.10.20.1.58' in it.templateId?.root
                }
            }
            
            // CONF-509: A status observation SHALL be the target of an entryRelationship whose value for
            //           “entryRelationship / @typeCode” SHALL be “REFR” 2.16.840.1.113883.5.1002
            //           ActRelationshipType STATIC.
            setAdvanceDirectiveStatus { POCDMT000040Observation observationStatus ->
                delegate.entryRelationship.add(builder.build {
                    entryRelationship (typeCode:'REFR', observation:observationStatus)
                })
            }
            
            getAdvanceDirectiveStatus { ->
                delegate.entryRelationship?.find {
                    '2.16.840.1.113883.10.20.1.37' in it.observation?.templateId?.root
                }?.observation
            }
            
            // CONF-101: An advance directive reference (templateId 2.16.840.1.113883.10.20.1.36) SHALL be
            //           represented with Observation / reference / ExternalDocument.
            // CONF-103: The value for “Observation / reference / @typeCode” in an advance directive reference SHALL be
            //           “REFR” 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.
            setAdvanceDirectiveReference{ POCDMT000040ExternalDocument document ->
                delegate.reference.add(builder.build {
                    reference (typeCode:'REFR', externalDocument:document)
                })
            }
            
            getAdvanceDirectiveReference { ->
                delegate.reference.find{
                    '2.16.840.1.113883.10.20.1.36' in it.externalDocument?.templateId?.root 
                }?.externalDocument
            }
        }// advance directives observations extensions
    }
    
    String extensionName() {
        'CCD Advance Directive Observation'
    }
    
    String templateId() {
        '2.16.840.1.113883.10.20.1.17'
    }
    
    Collection modelExtensions() {
        [ new CCDSourceExtension(),
          new CCDCommentsExtension()]
    }
     
}