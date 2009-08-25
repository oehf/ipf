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

/**
 * Chapter 3.6 : Family History
 *
 *  Template Definitions:
 *      Alert Section (2.16.840.1.113883.10.20.1.2)
 *
 *  Dependecies:
 *      Age Observation (ccd_ageObservation)
 *      Spurce
 *      Comments
 */


// CONF-184: CCD SHOULD contain exactly one and SHALL NOT contain more 
//           than one Family history section (templateId 2.16.840.1.113883.10.20.1.4). 
//           The Family history section SHALL contain a narrative block, and SHOULD 
//           contain clinical statements. Clinical statements SHOULD include one or 
//           more family history observations (templateId 2.16.840.1.113883.10.20.1.22), 
//           which MAY be contained within family history organizers 
//          (templateId 2.16.840.1.113883.10.20.1.23). 
//
// TODO: this sections may use CDA extensions, which are not yet included
// Note: Some constraints can not be automatically enforced at this point:
//       CONF-210, 211, 225

ccd_familyHistory(schema:'ccd_section') {
	properties {
		// CONF-185: The advance directive section SHALL contain Section / code.
		// CONF-186 : The value for “Section / code” SHALL be “10157-6”
		//          “History of family member diseases” 2.16.840.1.113883.6.1 LOINC STATIC.
		code(schema:'loincCode', def: {
			getMetaBuilder().build {
				loincCode(code:'10157-6',
				displayName:'History of family member diseases')
			}
		})
		
		// CONF-187: The advance directive section SHALL contain Section / title.
		// CONF-188: Section / title SHOULD be valued with a case-insensitive 
		//           language-insensitive text string containing “family history”.
		title(schema:'st', check: { it.text =~ /(?i)family history/ }, req:true, def: {
			getMetaBuilder().build { st('Family History') }
		})
		// CONF-189: The family history section SHALL NOT contain Section / subject.
		// TODO subject(check:{})
		text(schema:'strucDocText', req:true)
		familyHistoryObservation(schema:'ccd_familyHistoryObservationNotWithinAnOrganizer')
		familyMember(schema:'ccd_familyHistoryOrganizer')
		causeOfDeath(schema:'ccd_familyHistoryCauseOfDeathObservation')
	}
	collections {
		templateIds(collection:'templateId', def: {
			getMetaBuilder().buildList { ii(root:'2.16.840.1.113883.10.20.1.4') }
		})
		
	}
}


// CONF-190: A family history observation (templateId 2.16.840.1.113883.10.20.1.22) 
//           SHALL be represented with Observation
ccd_familyHistoryObservation(schema:'observation') {
	properties{
		// CONF-191: The value for “Observation / @moodCode” in a family history 
		//           observation SHALL be “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC
		moodCode(factory:'XACT_MOOD_DOCUMENT_OBSERVATION',
		            def:XActMoodDocumentObservation.EVN_LITERAL)
	    // CONF-193: A family history observation SHALL include exactly 
	    //           one Observation / statusCode
	    // CONF-194: The value for “Observation / statusCode” in a family history observation 
	    //           SHALL be “completed”  2.16.840.1.113883.5.14 ActStatus STATIC.
	    statusCode(schema:'cs', req:true, def: {
            getMetaBuilder().build {
                cs('completed')
            }
        })
        age(schema:'ccd_ageObservation')
        familyMember(schema:'ccd_familyMemberRelatedSubject')        
	}
	collections{
		// CONF-192: A family history observation SHALL contain at least one Observation / id.
		ids(collection:'id', min:1) { 
	        id(schema:'ii') 
	    }
		templateIds(collection:'templateId', def: {
			getMetaBuilder().buildList { ii(root:'2.16.840.1.113883.10.20.1.22') }
		})
	}
}

ccd_familyHistoryObservationNotWithinAnOrganizer(schema:'ccd_familyHistoryObservation') {
    properties {
        // CONF-209: A family history observation not contained within a family history 
        // organizer SHALL contain exactly one subject participant, representing the family 
        // member who is the subject of the observation .        
        // TODO: familyMember(req:true, schema:'ccd_familyMemberRelatedSubject')        
    }
}

// CONF-196: A family history cause of death observation 
//           (templateId 2.16.840.1.113883.10.20.1.42) SHALL conform to the 
//           constraints and is a kind of family history observation 
//           (templateId 2.16.840.1.113883.10.20.1.22).
ccd_familyHistoryCauseOfDeathObservation(schema:'ccd_familyHistoryObservation') {
    properties {
        // TODO: it's not enforced that the cause shall be an observation!
        cause(schema:'ccd_causeObservation', req:true)
    }
    collections {
		templateIds(collection:'templateId', def: {
			getMetaBuilder().buildList { ii(root:'2.16.840.1.113883.10.20.1.42') }
		})
    }
}

ccd_causeObservation(schema:'observation') {
    properties {
        age(schema:'ccd_ageObservation')
    }
}

// CONF-200: A family history organizer (templateId 2.16.840.1.113883.10.20.1.23) 
//           SHALL be represented with Organizer.
ccd_familyHistoryOrganizer(schema:'organizer') {
    properties {
        // CONF-201: The value for “Organizer / @classCode” in a family history 
        //           organizer SHALL be “CLUSTER” 2.16.840.1.113883.5.6 ActClass STATIC. 
        classCode(factory:'XACT_CLASS_DOCUMENT_ENTRY_ORGANIZER',
                def: XActClassDocumentEntryOrganizer.CLUSTER_LITERAL)
        // CONF-202: The value for “Organizer / @moodCode” in a family history organizer 
        //           SHALL be “EVN” 2.16.840.1.113883.5.1001 ActMood STATIC
        moodCode(factory:'XDOCUMENT_ACT_MOOD',
                def: XDocumentActMood.EVN_LITERAL) 
        // CONF-203: A family history organizer SHALL contain exactly one Organizer / statusCode.
        // CONF-204: The value for “Organizer / statusCode” in a family history organizer 
        //           SHALL be “completed”  2.16.840.1.113883.5.14 ActStatus STATIC.
        statusCode(schema:'cs', req:true, def: {
            getMetaBuilder().build {
                cs('completed')
            }
        })
        // CONF-208: A family history organizer SHALL contain exactly one subject 
        // participant, representing the family member who is the subject of the 
        // family history observations.
        familyPerson(req:true, schema:'ccd_familyMemberRelatedSubject')        
		familyHistoryObservation(schema:'ccd_familyHistoryObservationNotWithinAnOrganizer')
		causeOfDeath(schema:'ccd_familyHistoryCauseOfDeathObservation')
    }
    
    collections {
		templateIds(collection:'templateId', def: {
			getMetaBuilder().buildList { ii(root:'2.16.840.1.113883.10.20.1.23') }
		})
		// CONF-205: A family history organizer SHALL contain one or more Organizer / component.

        components(collection:'component'){
            component(schema:'ccd_familyHistoryOrganizerComponents')
        }
    }    
}

// CONF-206: The target of a family history organizer Organizer / component 
// relationship SHOULD be a family history observation, but MAY be some other 
// clinical statement.
ccd_familyHistoryOrganizerComponents(schema:'organizerComponents') {
    properties{
        observation(schema:'ccd_familyHistoryObservation')
    }
}

ccd_familyMemberRelatedSubject(schema:'relatedSubject') {
    properties {
        classCode(factory:'XDOCUMENT_SUBJECT', def: XDocumentSubject.PRS_LITERAL)
        // CONF-214: RelatedSubject SHALL contain exactly one RelatedSubject / code.        
        code(schema:'roleCode', req:true)
        subject(schema:'subjectPerson') // ccd_familyHistorySubjectPerson
    }
}

/* CCD Extensions. TODO; extend model
ccd_familyHistorySubjectPerson(schema:'subjectPerson') {
    properties {
        deceaseInd()
        deceasedTime(schema:'ts')
    }
    collections {
		ids(collection:'id', min:1) { 
	        id(schema:'ii') 
	    }        
    }
}
*/

