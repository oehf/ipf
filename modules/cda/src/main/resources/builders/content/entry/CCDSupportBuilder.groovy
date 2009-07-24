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
package builders.content.entry

import org.openhealthtools.ihe.common.cdar2.*

// Chapter 3.3: Support

// CONF-108: CCD MAY contain one or more patient guardians.
// CONF-109: A patient guardian SHALL be represented with 
//           ClinicalDocument / recordTarget / patientRole / patient / guardian.


// CONF-110: CCD MAY contain one or more next of kin.
// CONF-111: A next of kin SHALL be represented with 
//           ClinicalDocument / participant / associatedEntity.
// CONF-113: The value for “ClinicalDocument / participant / associatedEntity / @classCode” 
//           in a next of kin participant SHALL be “NOK” “Next of kin” 2.16.840.1.113883.5.41 EntityClass STATIC.
ccd_nextOfKin(schema:'associatedEntity'){
    properties{
        classCode(factory:'ROLE_CLASS_CONTACT',
                def: RoleClassContact.NOK_LITERAL)
    }
}

// CONF-115: CCD MAY contain one or more emergency contact.
// CONF-116: An emergency contact SHALL be represented with 
//           ClinicalDocument / participant / associatedEntity.
// CONF-117: The value for “ClinicalDocument / participant / @typeCode” in an 
//           emergency contact participant SHALL be “IND” “Indirect participant” 2.16.840.1.113883.5.90 ParticipationType STATIC.
// CONF-118: The value for “ClinicalDocument / participant / associatedEntity / @classCode”
//           in an emergency contact participant SHALL be “ECON” “Emergency contact” 2.16.840.1.113883.5.41 EntityClass STATIC.
ccd_emergencyContact(schema:'associatedEntity'){
    properties{
        classCode(factory:'ROLE_CLASS_CONTACT',
                def: RoleClassContact.ECON_LITERAL)
    }
    
}

// CONF-119: CCD MAY contain one or more patient caregivers.
// CONF-120: A patient caregiver SHALL be represented with 
//           ClinicalDocument / participant / associatedEntity.
// CONF-121: The value for “ClinicalDocument / participant / @typeCode” in a 
//           patient caregiver participant SHALL be “IND” “Indirect participant” 2.16.840.1.113883.5.90 ParticipationType STATIC.
// CONF-122: The value for “ClinicalDocument / participant / associatedEntity / @classCode” 
// in a patient caregiver participant SHALL be “CAREGIVER” “Caregiver” 2.16.840.1.113883.5.41 EntityClass STATIC.
ccd_caregiver(schema:'associatedEntity'){
    properties{
        classCode(factory:'ROLE_CLASS_MUTUAL_RELATIONSHIP_MEMBER1',
                def: RoleClassMutualRelationshipMember1.CAREGIVER_LITERAL)
    }
}
