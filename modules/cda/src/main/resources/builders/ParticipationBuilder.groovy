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

import groovytools.builder.*
import org.openhealthtools.ihe.common.cdar2.*

authenticator(schema:'dataEnterer', factory:'POCDMT000040_AUTHENTICATOR') {
    properties {
        signatureCode(schema:'cs')
    }
}

author(schema:'infrastructureRoot', factory:'POCDMT000040_AUTHOR') {
    properties {
        time(schema:'ts')
        assignedAuthor(schema:'assignedAuthor')
        functionCode(schema:'ce')
    }
}

clinicalStatementParticipant(schema:'infrastructureRoot', factory:'POCDMT000040_PARTICIPANT2') {
    properties {
        awarenessCode(schema:'ce')
        time(schema:'ivlts')
        participantRole(schema:'participantRole')
        typeCode(factory:'PARTICIPATION_TYPE', req:true)
    }
}

clinicalStatementPerformer(schema:'infrastructureRoot', factory:'POCDMT000040_PERFORMER2') {
    properties {
        modeCode(schema:'ce')
        assignedEntity(schema:'assignedEntity')
        time(schema:'ivlts')
        typeCode(factory:'PARTICIPATION_PHYSICAL_PERFORMER')
    }
}

custodian(schema:'infrastructureRoot', factory:'POCDMT000040_CUSTODIAN') {
    properties {
        assignedCustodian(schema:'assignedCustodian')
    }
}

dataEnterer(schema:'infrastructureRoot', factory:'POCDMT000040_DATA_ENTERER') {
	properties {
		assignedEntity(schema:'assignedEntity')
		time(schema:'ts')
	}
}

encounterParticipant(schema:'infrastructureRoot', factory:'POCDMT000040_ENCOUNTER_PARTICIPANT') {
    properties {
        typeCode(factory:'ACT_RELATIONSHIP_HAS_COMPONENT')
        assignedEntity(schema:'assignedEntity')
        time(schema:'ivlts')
    }
}

legalAuthenticator(schema:'authenticator', factory:'POCDMT000040_LEGAL_AUTHENTICATOR') {
}

informationRecipient(schema:'infrastructureRoot', factory:'POCDMT000040_INFORMATION_RECIPIENT') {
    properties {
        intendedRecipient(schema:'intendedRecipient')
        typeCode(factory:'XINFORMATION_RECIPIENT')
    }
}

informant(schema:'infrastructureRoot', factory:'POCDMT000040_INFORMANT12') {
    properties {
        // TODO choice of one of the two properties
        assignedEntity(schema:'assignedEntity')
        relatedEntity(schema:'relatedEntity')
    }
}

location(schema:'infrastructureRoot', factory:'POCDMT000040_LOCATION') {
    properties {
        healthCareFacility(schema:'healthCareFacility')
    }
}

participant(schema:'infrastructureRoot', factory:'POCDMT000040_PARTICIPANT1') {
    properties {
        associatedEntity(schema:'associatedEntity')
        functionCode(schema:'ce')
        time(schema:'ivlts')
        typeCode(factory:'PARTICIPATION_TYPE')
    }
}

product(schema:'infrastructureRoot', factory:'POCDMT000040_PRODUCT') {
   properties {
      manufacturedProduct(schema:'manufacturedProduct', req:true)
   }
}

consumable(schema:'infrastructureRoot', factory:'POCDMT000040_CONSUMABLE') {
   properties {
      // typeCode(factory:'TODO') // Fixed value
      manufacturedProduct(schema:'manufacturedProduct', req:true)
   }   
}

performer(schema:'infrastructureRoot', factory:'POCDMT000040_PERFORMER1') {
    properties {
        assignedEntity(schema:'assignedEntity')
        functionCode(schema:'ce')
        time(schema:'ivlts')
        typeCode(factory:'XSERVICE_EVENT_PERFORMER')
    }
}

recordTarget(schema:'infrastructureRoot', factory:'POCDMT000040_RECORD_TARGET') {
	properties { 
	    patientRole(schema:'patientRole', req:true) 
	}
}

responsibleParty(schema:'infrastructureRoot', factory:'POCDMT000040_RESPONSIBLE_PARTY') {
    properties {
        assignedEntity(schema:'assignedEntity')
        typeCode(factory:'XENCOUNTER_PARTICIPANT')
    }
}

specimen(schema:'infrastructureRoot', factory:'POCDMT000040_SPECIMEN') {
    properties {
        specimenRole(schema:'specimenRole')
    }
}

subject(schema:'infrastructureRoot', factory:'POCDMT000040_SUBJECT') {
    properties {
        awarenessCode(schema:'ce')
        relatedSubject(schema:'relatedSubject')
        typeCode(factory:'PARTICIPATION_TARGET_SUBJECT')
        contextControlCode(factory:'CONTEXT_CONTROL')
    }
}


