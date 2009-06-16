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

authoringDevice(schema:'infrastructureRoot', factory:'POCDMT000040_AUTHORING_DEVICE') {
     properties {
       code(schema:'ce')
       manufacturerModelName(schema:'sc')
       softwareName(schema:'sc')
     }
     collections {
 		asMaintainedEntities(collection:'asMaintainedEntity') {
 		   asMaintainedEntity(schema:'maintainedEntity')
 		}
     }
     
}

custodianOrganization(schema:'infrastructureRoot', factory:'POCDMT000040_CUSTODIAN_ORGANIZATION') {
    properties {
        addr(schema:'ad')
        name(schema:'on')
		telecom(schema:'tel')
    }
    collections {
		ids(collection:'id') {
			id(schema:'ii')
		}
    }
}

labeledDrug(schema:'infrastructureRoot', factory:'POCDMT000040_LABELED_DRUG') {
    properties {
       code(schema:'ce')
       name(schema:'en')
    }
}

material(schema:'labeledDrug', factory:'POCDMT000040_MATERIAL') {
    properties {
       lotNumberText(schema:'st')
    }
}

person(schema:'infrastructureRoot', factory:'POCDMT000040_PERSON') {
	collections { 
		names(collection:'name') {
			name(schema:'pn') 
		}
	}
}
patient(schema:'person', factory:'POCDMT000040_PATIENT') {
	properties {
		id(schema:'ii')
		administrativeGenderCode(schema:'administrativeGenderCode')
		birthTime(schema:'ts')
		maritalStatusCode(schema:'ce')
		religiousAffiliationCode(schema:'ce')
		raceCode(schema:'ce')
		ethnicGroupCode(schema:'ce')
		birthPlace(schema:'birthPlace')                    
	}
	collections {
		guardians(collection:'guardian') {
			guardian(schema:'guardian') 
		}
	}
}                          

organization(schema:'infrastructureRoot', factory:'POCDMT000040_ORGANIZATION') {
	properties { 
		standardIndustryClassCode(schema:'ce')
		asOrganizationPartOf(schema:'organizationPartOf')
	}
	collections {
		ids(collection:'id') {
			id(schema:'ii')
		}
		names(collection:'name') {
			name(schema:'on')
		}
		telecoms(collection:'telecom') {
			telecom(schema:'tel')
		}
		addrs(collection:'addr') {
			addr(schema:'ad')
		}
	}
}

place(schema:'infrastructureRoot', factory:'POCDMT000040_PLACE') {
	properties {
		name(schema:'en')
		addr(schema:'ad')
	}
}

playingEntity(schema:'infrastructureRoot', factory:'POCDMT000040_PLAYING_ENTITY') {
    properties {
        code(schema:'ce')
        desc(schema:'ed')
    }
    collections {
		names(collection:'name') {
			name(schema:'pn')
		}
		quantities(collection:'quantity') {
		    quantity(schema:'pq')
		}
        
    }
}

subjectPerson(schema:'person', factory:'POCDMT000040_SUBJECT_PERSON') {
    properties {
		administrativeGenderCode(schema:'administrativeGenderCode')
		birthTime(schema:'ts')
    }
}




