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
import static org.openhealthtools.ihe.common.cdar2.CDAR2Package.Literals.*


role(schema:'infrastructureRoot') {
     collections {
  		addrs(collection:'addr') {
  			addr(schema:'ad')
  		}        
  		ids(collection:'id') {
  			id(schema:'ii')
  		}
  		telecoms(collection:'telecom') {
  			telecom(schema:'tel')
  		}        
      }       
}

assignedAuthor(schema:'role', factory:'POCDMT000040_ASSIGNED_AUTHOR') {
    properties {
        // TODO restrict to choice of the following two
        assignedAuthoringDevice(schema:'authoringDevice')
        assignedPerson(schema:'person')
        representedOrganization(schema:'organization')
        code(schema:'roleCode')
    }
}

assignedCustodian(schema:'infrastructureRoot', factory:'POCDMT000040_ASSIGNED_CUSTODIAN') {
    properties {
        representedCustodianOrganization(schema:'custodianOrganization')
    }
}

assignedEntity(schema:'role', factory:'POCDMT000040_ASSIGNED_ENTITY') {
    properties {
        assignedPerson(schema:'person')
        representedOrganization(schema:'organization')
        code(schema:'roleCode')
    }
}

associatedEntity(schema:'role', factory:'POCDMT000040_ASSOCIATED_ENTITY') {
    properties {
        scopingOrganization(schema:'organization')
        associatedPerson(schema:'person')
        code(schema:'roleCode')
        classCode(factory:'ROLE_CLASS_ASSOCIATIVE') // TODO
    }
}

birthplace(schema:'infrastructureRoot', factory:'POCDMT000040_BIRTHPLACE') {
	properties { 
		place(schema:'place') 
	}
}


//TODO check that either person or organization is set
guardian(schema:'role', factory:'POCDMT000040_GUARDIAN') {
	properties {
		code(schema:'roleCode')
		guardianPerson(schema:'person')
		guardianOrganization(schema:'organization')
	}
}
healthCareFacility(schema:'infrastructureRoot', factory:'POCDMT000040_HEALTH_CARE_FACILITY') {
    properties {
		code(schema:'roleCode')
		location(schema:'place')
		serviceProviderOrganization(schema:'organization')
    }
    collections {
		ids(collection:'id') {
			id(schema:'ii')
		}        
    }
}

maintainedEntity(schema:'infrastructureRoot', factory:'POCDMT000040_MAINTAINED_ENTITY') {
    properties {
      effectiveTime(schema:'ivlts')
		maintainingPerson(schema:'person')
    }
}

manufacturedProduct(schema:'infrastructureRoot', factory:'POCDMT000040_MANUFACTURED_PRODUCT') {
   properties {
      manufacturerOrganization(schema:'organization')
      // exactly one of:
      manufacturedLabeledDrug(schema:'labeledDrug')
      manufacturedMaterial(schema:'material')
   }
   collections {
     ids(collection:'id') {
        id(schema:'ii')
     }
   }

}

intendedRecipient(schema:'role', factory:'POCDMT000040_INTENDED_RECIPIENT') {
    properties {
        classCode(factory:'XINFORMATION_RECIPIENT_ROLE')
        receivedOrganization(schema:'organization')
        informationRecipient(schema:'person')
    }
}

organizationPartOf(schema:'infrastructureRoot', factory:'POCDMT000040_ORGANIZATION_PART_OF') {
    properties {
        code(schema:'roleCode')
        effectiveTime(schema:'ivlts')
        statusCode(schema:'cs')
        wholeOrganization(schema:'organization')
    }
    collections {
		ids(collection:'id') {
			id(schema:'ii')
		}        
    }
}

participantRole(schema:'role',  factory: 'POCDMT000040_PARTICIPANT_ROLE') {
	properties {
	    code(schema:'roleCode')
	    //TODO one of playingEntity|playingDevice
	    playingEntity(schema:'playingEntity')
	    playingDevice(schema:'device')
	    scopingEntity(schema:'entity')
	}
}

patientRole(schema:'role',  factory: 'POCDMT000040_PATIENT_ROLE') {
	properties {
		patient(schema:'patient')
		providerOrganization(schema:'organization')
	}
}

relatedEntity(schema:'infrastructureRoot', factory:'POCDMT000040_RELATED_ENTITY') {
    properties {
        relatedPerson(schema:'person')
        effectiveTime(schema:'ivlts')
        code(schema:'roleCode')
        classCode(factory:'ROLE_CLASS_MUTUAL_RELATIONSHIP_MEMBER1')
    }
    collections {
		addrs(collection:'addr') {
			addr(schema:'ad')
		}        
		telecoms(collection:'telecom') {
			telecom(schema:'tel')
		}
    }
}


relatedSubject(schema:'infrastructureRoot', factory:'POCDMT000040_RELATED_SUBJECT') {
    properties {
        code(schema:'roleCode')
        subject(schema:'subjectPerson')
        classCode(factory:'XDOCUMENT_SUBJECT')
    }
    collections {
		addrs(collection:'addr') {
			addr(schema:'ad')
		}        
		telecoms(collection:'telecom') {
			telecom(schema:'tel')
		}        
    }
}


specimenRole(schema:'infrastructureRoot', factory:'POCDMT000040_SPECIMEN_ROLE') {
    properties {
        specimenPlayingEntity(schema:'playingEntity')
    }
    collections {
  		ids(collection:'id') {
  			id(schema:'ii')
  		}        
    }
}