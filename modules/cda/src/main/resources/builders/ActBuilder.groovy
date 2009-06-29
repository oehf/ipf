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
package builders

import org.openhealthtools.ihe.common.cdar2.*

// The Clinical Document
clinicalDocument(schema:'infrastructureRoot', factory:'POCDMT000040_CLINICAL_DOCUMENT') {
	properties {
	    // Represents the unique instance identifier of a clinical document. 
		id(schema:'ii', req:true)
		// The code specifying the particular kind of document
		code(schema:'ce', req:true)
	    // Represents the title of the document
		title(schema:'st')
	    // Signifies the document creation time, when the document first came into being
		effectiveTime(schema:'ts', req:true)
	    // The code specifying the confidentiality of the document
		confidentialityCode(schema:'confidentialityCode', req:true)
		// Specifies the human language of character data
		languageCode(schema:'cs')
	    // Represents an identifier that is common across all document revisions
		setId(schema:'ii')
		// An integer value used to version successive replacement documents
		versionNumber(schema:'_int')
	    // Represents the time a document is released (i.e. copied or sent to a display 
	    // device) from a document management system that maintains revision control over the document
		copyTime(schema:'ts')  
	    // Represents the participant who has transformed a dictated note into text
		dataEnterer(schema:'dataEnterer')
		// Represents the organization that is in charge of maintaining the document
		custodian(schema:'custodian')
		// Represents a participant who has legally authenticated the document
		legalAuthenticator(schema:'legalAuthenticator')		
		// This optional class represents the setting of the clinical encounter during 
		// which the documented act(s) or ServiceEvent occurred
		componentOf(schema:'componentOf')
		// Link to the CDA body
		component(schema:'component')

        // Try a shortcut
        patient(schema:'patientRole')
	    
	}
	collections {    
	    // The recordTarget represents the medical record that this document belongs to
	    recordTargets(collection:'recordTarget', min:1) {
	        recordTarget(schema:'recordTarget')
	    }	    
	    // Represents the humans and/or machines that authored the document
	    authors(collection:'author', min:1) {
	        author(schema:'author')
	    }
	    // Represents a recipient who should receive a copy of the document
	    informationRecipients(collection:'informationRecipient') {
	        informationRecipient(schema:'informationRecipient')
	    }
	    // An informant (or source of information) is a person that provides relevant information
	    informants(collection:'informant') {
	        informant(schema:'informant')
	    }
	    // Represents a participants who has attested to the accuracy of the document, 
	    // but who does not have privileges to legally authenticate the document
	    authenticators(collection:'authenticator') {
	        authenticator(schema:'authenticator')
	    }
	    // Used to represent other participants not explicitly mentioned by other classes
	    participants(collection:'participant') {
	        participant(schema:'participant')
	    }
	    // This class references the consents associated with this document
	    authorizations(collection:'authorization') {
	        authorization(schema:'authorization')
	    }
	    // This class represents those orders that are fulfilled by this document
	    inFullfillmentOfs(collection:'inFullfillmentOf') {
	        inFullfillmentOf(schema:'inFullfillmentOf')
	    }
	    // This class represents the main Act, such as a colonoscopy or an appendectomy, being documented
	    documentationOfs(collection:'documentationOf') {
	        documentationOf(schema:'documentationOf')
	    }
	    // The ParentDocument represents the source of a document revision, addenda, or transformation
	    relatedDocuments(collection:'relatedDocument') {
	        relatedDocument(schema:'relatedDocument')
	    }
	}
}

// Authorization consent for a ClinicalDocument
consent(schema:'infrastructureRoot', factory:'POCDMT000040_CONSENT') {
     properties {
        code(schema:'ce')
        statusCode(schema:'cs', req:true)
     }
     collections {
 		ids(collection:'id') {
			id(schema:'ii')
		}
     }
}

encompassingEncounter(schema:'infrastructureRoot', factory:'POCDMT000040_ENCOMPASSING_ENCOUNTER') {
     properties {
        code(schema:'ce')
        effectiveTime(schema:'ivlts', req:true)
        dischargeDispositionCode(schema:'ce')
        location(schema:'location')
        responsibleParty(schema:'responsibleParty')
     }
     collections {
 		ids(collection:'id') {
			id(schema:'ii')
		}
 		encounterParticipants(collection:'encounterParticipant') {
 		   encounterParticipant(schema:'encounterParticipant')
 		}
     }
}

order(schema:'infrastructureRoot', factory:'POCDMT000040_ORDER') {
    properties {
       code(schema:'ce')
       priorityCode(schema:'ce')
    }
    collections {
		ids(collection:'id', min:1) {
			id(schema:'ii')
		}
    }
}

parentDocument(schema:'infrastructureRoot', factory:'POCDMT000040_PARENT_DOCUMENT') {
    properties {
		setId(schema:'ii')
		code(schema:'ce')
		text(schema:'ed')
		versionNumber(schema:'_int')
    }
    collections {
 		ids(collection:'id', min:1) {
 			id(schema:'ii')
 		}        
    }
}

section(schema:'infrastructureRoot', factory:'POCDMT000040_SECTION') {
    properties {
        moodCode(factory:'ACT_MOOD')
        classCode(factory:'ACT_CLASS_ORGANIZER')
        code(schema:'ce') // DocumentSectionType code 
        id(schema:'ii')
        title(schema:'st')
        text(schema:'strucDocText') // section-text mimeType='text/x-hl7-text+xml', NARRATIVE PART
        confidentialityCode(schema:'confidentialityCode')
        languageCode(schema:'cs')
        subject(schema:'subject')
    }
    collections {
	    authors(collection:'author') {
	        author(schema:'author')
	    }
	    informants(collection:'informant') {
	        informant(schema:'informant')
	    }
	    entries(collection:'entry') {
	        entry(schema:'entry')
	    }
	    components(collection:'component') {
	        component(schema:'sectionComponents')
	    }

	    
    }
}

serviceEvent(schema:'infrastructureRoot', factory:'POCDMT000040_SERVICE_EVENT') {
    properties {
       code(schema:'ce')
       effectiveTime(schema:'ivlts')
    }
    collections {
		ids(collection:'id', min:1) {
			id(schema:'ii')
		}
		performers(collection:'performer') {
		    performer(schema:'performer')
		}
    }
}

nonXMLBody(schema:'infrastructureRoot', factory:'POCDMT000040_NON_XML_BODY') {
    properties {
        moodCode(factory:'ACT_MOOD')
        classCode(factory:'ACT_CLASS')
        text(schema:'ed')
        confidentialityCode(schema:'confidentialityCode')
        languageCode(schema:'cs')
    }
}


structuredBody(schema:'infrastructureRoot', factory:'POCDMT000040_STRUCTURED_BODY') {
    properties {
        confidentialityCode(schema:'confidentialityCode')
        languageCode(schema:'cs')
        moodCode(factory:'ACT_MOOD')
    }
    collections {
        components(collection:'component', min:1) {
            component(schema:'sections')            
        }
    }
}


clinicalStatement(schema:'infrastructureRoot') {
    properties {
        subject(schema:'subject')        
    }
    collections {
	    authors(collection:'author') {
	        author(schema:'author')
	    }
	    informants(collection:'informant') {
	        informant(schema:'informant')
	    }
		performers(collection:'performer') {
		    performer(schema:'clinicalStatementPerformer')
		}
		specimens(collection:'specimen') {
		    specimen(schema:'specimen')
		}
		participants(collection:'participant') {
		    participant(schema:'clinicalStatementParticipant')
		}
      entryRelationships(collection:'entryRelationship') {
          entryRelationship(schema:'entryRelationship')
      }
		preconditions(collection:'precondition') {
		    precondition(schema:'precondition')
		}
		references(collection:'reference') {
		    reference(schema:'reference')
		}
    }
}

act(schema:'clinicalStatement', factory:'POCDMT000040_ACT') {
    properties {
        classCode(factory:'XACT_CLASS_DOCUMENT_ENTRY_ACT')
        moodCode(factory:'XDOCUMENT_ACT_MOOD')
        negationInd()
        code(schema:'cd', req:true)
        text(schema:'ed')
        statusCode(schema:'cs')
        effectiveTime(schema:'ivlts')
        priorityCode(schema:'ce')
        languageCode(schema:'cs')
    }
    collections {
        ids(collection:'id') {
            id(schema:'ii')
        }
    }
}

encounter(schema:'clinicalStatement',factory:'POCDMT000040_ENCOUNTER'){
    properties{
        classCode(factory:'ACT_CLASS_ROOT_MEMBER7',
                def: ActClassRootMember7.ENC_LITERAL)
        moodCode(factory:'XDOCUMENT_ENCOUNTER_MOOD')
        code(schema:'cd')
        text(schema:'ed')
        statusCode(schema:'cs')
        effectiveTime(schema:'ivlts')
        priorityCode(schema:'ce')
    }
    collections {
        ids(collection:'id') {
            id(schema:'ii')
        }
    }
}

observation(schema:'clinicalStatement',factory:'POCDMT000040_OBSERVATION'){
    properties {
        classCode(factory:'ACT_CLASS_OBSERVATION',
                req:true,
                def:ActClassObservationMember3.OBS_LITERAL)
        moodCode(factory:'XACT_MOOD_DOCUMENT_OBSERVATION',
                req:true,
                def:XActMoodDocumentObservation.INT_LITERAL)
        negationInd()
        code(schema:'ce', req:true)
        derivationExpr(schema:'ST')
        text(schema:'ed')
        statusCode(schema:'cs')
        effectiveTime(schema:'ivlts')
        priorityCode(schema:'ce')
        repeatNumber(schema:'ivl_int')
        languageCode(schema:'cs')
    }
    collections {
        ids(collection:'id') {
            id(schema:'ii')
        }
        values(collection:'value'){
            value(schema:'cd') // TODO: ANY!
        }
        interpretationCodes(collection:'interpretationCode'){
            interpretationCode(schema:'ce')
        }
        methodCodes(collection:'methodCode'){
            methodCode(schema:'ce')
        }
        targetSiteCodes(collection:'targetSiteCode'){
            targetSiteCode(schema:'cd')    
        }
        referenceRanges(collection:'referenceRange'){
            referenceRange(schema:'referenceRange')
        }
    }
}

observationMedia(schema:'clinicalStatement',factory:'POCDMT000040_OBSERVATION_MEDIA'){
    properties{
        classCode(factory:'ACT_CLASS_OBSERVATION')
        moodCode(factory:'XDOCUMENT_ACT_MOOD',
                def:XDocumentActMood.EVN_LITERAL)
        languageCode(schema:'cs')
        value(schema:'ed')
    }
    collections {
        ids(collection:'id') {
            id(schema:'ii')
        }
    }
}

organizer(schema:'clinicalStatement',factory:'POCDMT000040_ORGANIZER'){
    properties {
        classCode(factory:'XACT_CLASS_DOCUMENT_ENTRY_ORGANIZER')
        moodCode(factory:'XDOCUMENT_ACT_MOOD')
        code(schema:'cd')
        statusCode(schema:'cs', req:true)
        effectiveTime(schema:'ivlts')
    }
    collections {
        ids(collection:'id') {
            id(schema:'ii')
        }
        components(collection:'component'){
            component(schema:'organizerComponents')
        }
    }
}

procedure(schema:'clinicalStatement',factory:'POCDMT000040_PROCEDURE'){
    properties {
        classCode(factory:'ACT_CLASS_ROOT',
                  def: ActClassRootMember7.PROC_LITERAL)
        moodCode(factory:'XDOCUMENT_PROCEDURE_MOOD')
        negationInd()
        code(schema:'cd')
        text(schema:'ed')
        statusCode(schema:'cs')
        effectiveTime(schema:'ivlts')
        priorityCode(schema:'cd')
        languageCode(schema:'cs')
    }
    collections {
        ids(collection:'id') {
            id(schema:'ii')
        }
        methodCodes(collection:'methodCode'){
            methodCode(schema:'ce')
        }
        approachSiteCodes(collection:'approachSiteCode'){
            approachSiteCode(schema:'cd')
        }
        targetSiteCodes(collection:'targetSiteCode'){
            targetSiteCode(schema:'cd')
        }
    }
}

regionOfInterest(schema:'clinicalStatement',factory:'POCDMT000040_REGION_OF_INTEREST'){
    properties {
        classCode(factory:'ACT_CLASS_ROOT', def: ActClassROI.ROIOVL_LITERAL)
        moodCode(factory:'XDOCUMENT_ACT_MOOD', def: XDocumentActMood.EVN_LITERAL)
        ID()
        code(schema:'cs', req:true)
    }
    collections {
        ids(collection:'id', min: 1) {
            id(schema:'ii')
        }
        values(collection:'value', min: 1){
            value(factory: 'POCDMT000040_REGION_OF_INTEREST_VALUE'){
              properties{
                value(factory: java.math.BigInteger)
                unsorted(def:false)
              }
            }
        }
    }
}

substanceAdministration(schema:'clinicalStatement',factory:'POCDMT000040_SUBSTANCE_ADMINISTRATION'){
    properties {
        classCode(factory:'ACT_CLASS_ROOT', def: ActClassRootMember7.SBADM_LITERAL)
        moodCode(factory:'XDOCUMENT_SUBSTANCE_MOOD')
        negationInd()
        code(schema:'cs')
        text(schema:'ed')
        statusCode(schema:'cs')
        priorityeCode(schema:'ce')
        repeatNumber(schema:'ivlint')
        routeCode(schema:'ce')
        doseQuantity(schema:'ivlpq')
        rateQuantity(schema:'ivlpq')
        maxDoseQuantity(schema:'rtopqpq')
        administrationUnitCode(schema:'ce', max:1)
        consumable(schema:'consumable', req:true)
    }
    collections {
        ids(collection:'id') {
            id(schema:'ii')
        }
        effectiveTimes(collection:'effectiveTime'){
            effectiveTime(schema:'sxcmts')
        }
        approachSiteCodes(collection:'approachSiteCode'){
            approachSiteCode(schema:'cd')
        }
    }
}

supply(schema:'clinicalStatement',factory:'POCDMT000040_SUPPLY'){
    properties{
        classCode(factory:'ACT_CLASS_SUPPLY', def: ActClassSupply.PLY_LITERAL)
        moodCode(factory:'XDOCUMENT_SUBSTANCE_MOOD')
        code(schema:'cd')
        text(schema:'ed')
        statusCode(schema:'cs')
        independentInd(schema:'bl')
        product(schema:'product')
    }
    collections {
        ids(collection:'id') {
            id(schema:'ii')
        }
        effectiveTimes(collection:'effectiveTime'){
            effectiveTime(schema:'sxcmts')    
        }
        priorityCodes(collection:'priorityCode'){
                priorityCode(schema:'ce')
        }
        repeatNumbers(collection:'repeatNumber'){
            repeatNumber(schema:'ivlint')
        }
        quantitys(collection:'quantity'){
            quantity(schema:'pq')
        }
        expectedUseTime(collection:'expectedUseTime'){
            expectedUseTime(schema:'ivlts')
        }
    }
}

/*reference definitions */

criterion(schema:'infrastructureRoot', factory:'POCDMT000040_CRITERION') {
   properties {
      code(schema:'cd')
      text(schema:'ed')
      value()      
   }
}


observationRange(schema:'infrastructureRoot', factory:'POCDMT000040_OBSERVATION_RANGE') {
   properties {
      code(schema:'cd')
      text(schema:'ed')
      value()
      interpretationCode(schema:'ce')
   }
}


externalAct(schema:'infrastructureRoot', factory:'POCDMT000040_EXTERNAL_ACT'){
    properties{
        classCode(factory:'ACT_CLASS_ROOT', def: ActClassRootMember7.ACT_LITERAL)
        moodCode(factory:'XDOCUMENT_ACT_MOOD', def: XDocumentActMood.EVN_LITERAL)
        code(schema:'cd')
        text('ed')
    }
    collections {
        ids(collection:'id') {
            id(schema:'ii')
        }
    }
}

externalObservation(schema:'infrastructureRoot', factory:'POCDMT000040_EXTERNAL_OBSERVATION'){
    properties{
        classCode(factory:'ACT_CLASS_OBSERVATION_MEMBER3', def: ActClassObservationMember3.OBS_LITERAL)
        moodCode(factory:'XDOCUMENT_ACT_MOOD', def: XDocumentActMood.EVN_LITERAL)
        code(schema:'cd')
        text('ed')
    }
    collections {
        ids(collection:'id') {
            id(schema:'ii')
        }
    }
}

externalProcedure(schema:'infrastructureRoot', factory:'POCDMT000040_EXTERNAL_PROCEDURE'){
    properties{
        classCode(factory:'ACT_CLASS_ROOT', def: ActClassRootMember7.PROC_LITERAL)
        moodCode(factory:'XDOCUMENT_ACT_MOOD', def: XDocumentActMood.EVN_LITERAL)
        code(schema:'cd')
        text('ed')
    }
    collections {
        ids(collection:'id') {
            id(schema:'ii')
        }
    }
}

externalDocument(schema:'infrastructureRoot', factory:'POCDMT000040_EXTERNAL_DOCUMENT'){
    properties{
        classCode(factory:'ACT_CLASS_DOCUMENT_MEMBER1', def: ActClassDocumentMember1.DOC_LITERAL)
        moodCode(factory:'XDOCUMENT_ACT_MOOD', def: XDocumentActMood.EVN_LITERAL)
        code(schema:'cd')
        text(schema:'ed')
        setId(schema:'ii')
        versionNumber(schema:'_int')
    }
    collections {
        ids(collection:'id') {
            id(schema:'ii')
        }
    }
}

/* some other extended types */

