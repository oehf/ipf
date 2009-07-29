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
package builders.content.section

import org.openhealthtools.ihe.common.cdar2.*

//CONF-145: A problem act (templateId 2.16.840.1.113883.10.20.1.27) SHALL be represented with Act.
ccd_problemAct(schema:'act'){
    properties{
        // CONF-146: The value for “Act / @classCode” in a problem act SHALL be 
        //           “ACT” 2.16.840.1.113883.5.6 ActClass STATIC.
        classCode(factory:'XACT_CLASS_DOCUMENT_ENTRY_ACT', def:XActClassDocumentEntryAct.ACT_LITERAL)
        // CONF-147: The value for “Act / @moodCode” in a problem act SHALL be 
        //           “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC.
        moodCode(factory:'XDOCUMENT_ACT_MOOD', def: XDocumentActMood.EVN_LITERAL)
        // CONF-149: The value for “Act / code / @NullFlavor” in a problem act SHALL be
        //           “NA” “Not applicable” 2.16.840.1.113883.5.1008 NullFlavor STATIC.
        code(def:{
            getMetaBuilder().build{
                cd(nullFlavor:'NA')
            }
        })
        //CONF-153: The target of a problem act with Act / entryRelationship / @typeCode=”SUBJ” 
        //          SHOULD be a problem observation (in the Problem section) or 
        //          alert observation (in the Alert section, see section 3.8 Alerts), 
        //          but MAY be some other clinical statement.
        problemObservation(schema:'ccd_problemObservation')
        alertObservation(schema:'ccd_alertObservation')
        // CONF-168: A problem act MAY contain exactly one episode observation.
        episodeObservation(schema:'ccd_episodeObservation')
        //CONF-179: A problem act MAY contain exactly one patient awareness.
        patientAwareness(schema:'ccd_patientAwareness')
    }
    collections{
        templateIds(collection:'templateId', def: {
            getMetaBuilder().buildList {
                ii(root:'2.16.840.1.113883.10.20.1.27')
            }
        })
        entryRelationships(collection:'entryRelationship', min:1)
    }
}