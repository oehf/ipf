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
package org.openehealth.ipf.modules.ccd.builder

import org.openhealthtools.ihe.common.cdar2.*
import org.openhealthtools.ihe.common.cdar2.impl.*
import junit.framework.Assert

/**
 * Implements a subset of validation rules for CCD instance documents.
 * May be used for test purposes only
 *
 * @author Stefan Ivanov
 */
public static class CCDConformanceValidatorHelper{

    /**
     * Implements a set of CCD Header conformance rules
     */
    public static boolean checkCCDHeaderConformance(POCDMT000040ClinicalDocument document){
        /* CONF-1: ClinicalDocument / code is '34133-9' */
        Assert.assertEquals('CONF-1 Failed', '34133-9', document.code.code)
        /* CONF-2: ClinicalDocument / documentationOf / serviceEvent = 1 */
        Assert.assertEquals('CONF-2 Failed', 1, document.documentationOf.size)
        def serviceEvent = document.documentationOf.get(0).serviceEvent
        /* CONF-3: ClinicalDocument / documentationOf / serviceEvent / @classCode is 'PCPR' */
        Assert.assertEquals('CONF-3 Failed', 'PCPR', serviceEvent.classCode.name)
        /* CONF-4: ClinicalDocument / documentationOf / serviceEvent SHALL contain
           * exactly one serviceEvent / effectiveTime / low and exactly one serviveEvent / effectiveTime / high.
           */
        Assert.assertNotNull('CONF-4 Failed effectiveTime/low', serviceEvent.effectiveTime.low)
        Assert.assertNotNull('CONF-4 Failed effectiveTime/high', serviceEvent.effectiveTime.high)

        /* 2.2 Language*/
        /* CONF-5: ClinicalDocument / languageCode != null */
        Assert.assertNotNull('CONF-5 Failed', document.languageCode.code)
        /* CONF-6: ClinicalDocument / languageCode language and country values */
        def locale = document.languageCode.code.split(/-/)
        Assert.assertTrue('CONF-6 Failed language code/language', locale[0] in Locale.getISOLanguages())
        if (locale.length == 2)
            Assert.assertTrue('CONF-6 Failed language code/country', locale[1] in Locale.getISOCountries())

        /* 2.3 Version CCD v1.0 */
        /* CONF-7: ClinicalDocument / templateId != null */
        /* CONF-8: ClinicalDocument / templateId / @root is '2.16.840.1.113883.10.20.1' and
           *        ClinicalDocument / templateId / @extension is null */
        def ccdVersions = document.templateId.findAll{  it.root == '2.16.840.1.113883.10.20.1'}
        Assert.assertTrue('CONF-7 Failed', ccdVersions.size == 1)
        Assert.assertTrue('CONF-8 Failed', ccdVersions[0].extension == null)

        /* 2.5 Patient */
        /* CONF-11: ClinicalDocument / recordTarget size is 1 || 2 */
        Assert.assertTrue('CONF-11', document.recordTarget.size == 1
                || document.recordTarget.size == 2)

        /* 2.6 From */
        /* CONF-12: ClinicalDocument / author / assignedAuthor / assignedPerson >= 1 and/or
           *          ClinicalDocument / author / assignedAuthor / representedOrganization >= 1
           */
        document.author.eachWithIndex{ item, index ->
            /* CONF-12: ClinicalDocument / author / assignedAuthor / assignedPerson >= 1 and/or
                *          ClinicalDocument / author / assignedAuthor / representedOrganization >= 1
                */
            Assert.assertTrue('CONF-12 Failed', item.assignedAuthor.assignedPerson != null ||
                    item.assignedAuthor.representedOrganization != null)

            /* CONF-13: If author has an associated representedOrganization
                 * with no assignedPerson or assignedAuthoringDevice,
                 * then the value for “ClinicalDocument / author / assignedAuthor / id / @NullFlavor”
                 * SHALL be “NA” “Not applicable” 2.16.840.1.113883.5.1008 NullFlavor STATIC.
                 */
            if (item.assignedAuthor.assignedPerson == null &&
                    item.assignedAuthor.assignedAuthoringDevice == null &&
                    item.assignedAuthor.representedOrganization != null){
                Assert.assertTrue('CONF-13 Failed', item.assignedAuthor.id.nullFlavor[0].name == 'NA')
            }
        }
        /* 2.7 To */
        /* CONF-14: ClinicalDocument / informationRecipient >= 0 */
        //TODO
        return true
    }



    /**
     * Implements a set of CCD Purpose conformance rules
     */
    public static boolean checkCCDPurposeConformance(POCDMT000040Component2 ccdPurposeComponent){
        Assert.assertTrue ccdPurposeComponent instanceof POCDMT000040Component2
        Assert.assertTrue ccdPurposeComponent.structuredBody.component.get(0) instanceof POCDMT000040Component3
        def section = ccdPurposeComponent.structuredBody.component.get(0).section
        Assert.assertTrue section instanceof POCDMT000040Section
        /* CONF-15: Purpose section(templateId) */
        Assert.assertEquals('CONF-15 Failed', '2.16.840.1.113883.10.20.1.13', ccdPurposeComponent.structuredBody.component.get(0).section.templateId.get(0).root)
        /* CONF-16,CONF-17: Purpose section/code */
        Assert.assertEquals('CONF-16 Failed', '48764-5',section.code.code)
        Assert.assertEquals('CONF-17 Failed', '2.16.840.1.113883.6.1', section.code.codeSystem)
        /* CONF-18, CONF-19: Purpose seciton/title */
        Assert.assertNotNull('CONF-18 Failed:', section.title)
        Assert.assertTrue('CONF-19 Failed', section.title.mixed[0].value.matches('(?i).*purpose*.'))
        /* clinical statement conformance: purpose activity */
        def act = ccdPurposeComponent.structuredBody.component.get(0).section.entry.get(0).act
        /* CONF-20: entry is act */
        Assert.assertTrue('CONF-20 Failed', act instanceof POCDMT000040Act)
        /* CONF-21: Act / @slassCode is 'ACT' */
        Assert.assertEquals('CONF-21 Failed', 'ACT', act.classCode.name)
        /* CONF-22: Act / @moodCode is 'EVN' */
        Assert.assertEquals('CONF-22 Failed', 'EVN', act.moodCode.name)
        /* CONF-23,CONF-24: Act / statusCode is 'completed' */
        Assert.assertNotNull('CONF-23 Failed', act.statusCode)
        Assert.assertEquals('CONF-24', 'completed', act.statusCode.code)
        /* CONF-25: Act / code value is 2.16.840.1.113883.6.96:23745001 */
        Assert.assertEquals('CONF-25 Failed codeSystem', '2.16.840.1.113883.6.96', act.code.codeSystem)
        Assert.assertEquals('CONF-25 Failed code', '23745001', act.code.code)
        /* CONF-26: Act / entryRelationship / @typeCode is 'RSON' */
        Assert.assertEquals('CONF-26 Failed', 'RSON', act.entryRelationship.get(0).typeCode.name)
        /* CONF-27: Act / entryrelationship /@typeCode target is
         *             in [Act, Observation, Procedure, Substance Administration, Supply]
         */
        //assertNotNull act.entryRelationship.get(0).act
        return true
    }

    /**
     * Implements set of CCD Payers conformance rules : coverage activity
     */
    public static boolean checkCCDPayersConformance(POCDMT000040Section section){
        Assert.assertTrue section instanceof POCDMT000040Section
        /* CONF-31: Section / code != null*/
        Assert.assertNotNull('CONF-31 Failed', section.code)
        /* CONF-32: Section / code value is  LOINC:2.16.840.1.113883.6.1:48768-6 */
        Assert.assertEquals('CONF-32 Failed code', '48768-6',section.code.code)
        Assert.assertEquals('CONF-32 Failed code system', '2.16.840.1.113883.6.1', section.code.codeSystem)
        /* CONF-33: Section / title != null */
        Assert.assertNotNull('CONF-33 Failed:', section.title)
        /* CONF-34: Section / title value contains 'insurance' or 'payers' */
        Assert.assertTrue('CONF-34 Failed', section.title.mixed[0].value.matches('(?i).*(payers|insurance)*.'))
        /* clinical statement conformance: payers information */
        def act = section.entry.get(0).act
        Assert.assertTrue(checkCCDPayersCoverageActivity(act))
        return true
    }


    /**
     * Implements set of CCD Payers conformance rules : coverage activity
     */
    public static boolean checkCCDPayersCoverageActivity(POCDMT000040Act act){
        /* CONF-35: coverage activity (templateId 2.16.840.1.113883.10.20.1.20) is Act */
        Assert.assertTrue('CONF-35 Failed', act instanceof POCDMT000040Act)
        /* CONF-36: Act / @classCode is 'ACT' */
        Assert.assertEquals('CONF-36 Failed', 'ACT', act.classCode.name)
        /* CONF-37: Act / @moodCode is 'DEF' */
        Assert.assertEquals('CONF-37 Failed', 'DEF', act.moodCode.name)
        /* CONF-38:  coverage activity has Act / id */
        Assert.assertNotNull('CONF-38 Failed', act.id)
        /* CONF-39:  coverage activity has exactly one Act / statusCode */
        Assert.assertNotNull('CONF-39 Failed', act.statusCode)
        /* CONF-40: Act / statusCode is 'completed' */
        Assert.assertEquals('CONF-40', 'completed', act.statusCode.code)
        /* CONF-41: Act / code is exactly one */
        Assert.assertNotNull('CONF-41 Failed', act.code)
        /* CONF-42: Act / code value is LOINC:2.16.840.1.113883.6.1:48768-6 */
        Assert.assertEquals('CONF-42 Failed codeSystem', '2.16.840.1.113883.6.1', act.code.codeSystem)
        Assert.assertEquals('CONF-42 Failed code', '48768-6', act.code.code)
        /* CONF-43: Act has one/more entryRelationship/s */
        Assert.assertTrue('CONF-43 Failed', act.entryRelationship.size >= 1)
        /* CONF-45: Act / entryRelationship / @typeCode value is 'COMP' */
        Assert.assertEquals('CONF-45 Failed', 'COMP', act.entryRelationship.get(0).typeCode.name)
        /* CONF-46: policy activity (templateId 2.16.840.1.113883.10.20.1.26) */
        act.entryRelationship.eachWithIndex{ item, index ->
            def policyActivity = item.act
            Assert.assertEquals('CONF-46 Failed templateId check on ${index}', '2.16.840.1.113883.10.20.1.26', policyActivity.templateId.get(0).root)
            Assert.assertTrue(checkCCDPayersPolicyActivity(item))
        }
        /* CONF-47: A coverage activity SHALL contain one or more sources of information, as defined in section 5.2 Source */
        //TODO check source

        return true
    }

    /**
     * Implements a set of CCD Payers conformance rules : policy activity
     */
    public static boolean checkCCDPayersPolicyActivity(POCDMT000040EntryRelationship coverageActivity){
        def policyActivity = coverageActivity.act
        /* CONF-48: A policy activity (templateId 2.16.840.1.113883.10.20.1.26) is Act */
        Assert.assertTrue('CONF-48 Failed', policyActivity instanceof POCDMT000040Act)
        /* CONF-49: Act / @classCode is 'ACT' */
        Assert.assertEquals('CONF-49 Failed', 'ACT', policyActivity.classCode.name)
        /* CONF-50: Act / @moodCode is 'EVN' */
        Assert.assertEquals('CONF-50 Failed', 'EVN', policyActivity.moodCode.name)
        /* CONF-51: policy activity has Act / id */
        Assert.assertNotNull('CONF-51 Failed', policyActivity.id)
        /* CONF-52: policy activity has exactly one Act / statusCode */
        Assert.assertNotNull('CONF-52 Failed', policyActivity.statusCode)
        /* CONF-53: Act / statusCode is 'completed' */
        Assert.assertEquals('CONF-40', 'completed', policyActivity.statusCode.code)
        /* CONF-56: policy activity Act / performer / @typeCode is 'PRF' */
        Assert.assertEquals('CONF-56 Failed', 'PRF', policyActivity.performer.get(0).typeCode.name)
        /* CONF-57: A payer in a policy activity SHALL contain one or more performer / assignedEntity / id, to represent the payer identification number. For pharamacy benefit programs this can be valued using the RxBIN and RxPCN numbers assigned by ANSI and NCPDP respectively. When a nationally recognized payer identification number is available, it would be placed here */
        Assert.assertTrue('CONF-57 Failed performer count', policyActivity.performer.get(0).assignedEntity.id.size >= 1)
        /* CONF-58: policy activity contain exactly one Act / participant / @typeCode = 'COV */
        Assert.assertEquals('CONF-58 Failed', 'COV', policyActivity.participant.get(0).typeCode.name)
        /* CONF-66: Act / entryRelationship / @typeCode is 'REFR' */
        policyActivity.entryRelationship.eachWithIndex { item, index->
            Assert.assertEquals('CONF-66 Failed ${index}', 'REFR', item.typeCode.name)
            Assert.assertTrue(checkCCDPayersAuthorizationAcitivity(item))
        }
        /* CONF-67: Act / entryRelationship / @typeCode is 'REFR' SHALL be 
         * an authorization activity (templateId 2.16.840.1.11CONF-67: 
         * The target of a policy activity with 
         * Act / entryRelationship / @typeCode=”REFR” SHALL be 
         * an authorization activity (templateId 2.16.840.1.113883.10.20.1.19) 
         * or an Act, with Act [@classCode = “ACT”] and Act [@moodCode = “DEF”], 
         * representing a description of the coverage plan */
        //TODO
        /* CONF-68: A description of the coverage plan SHALL contain one 
         * or more Act / Id, to represent the plan identifier */
        //TODO
        return true
    }

    /**
     * Implements a set of CCD Payers conformance rules : authorization activity
     */
    public static boolean checkCCDPayersAuthorizationAcitivity(POCDMT000040EntryRelationship authActivity){
        /* CONF-69: authorization activity (templateId 2.16.840.1.113883.10.20.1.19) */
        Assert.assertEquals('CONF-69', '2.16.840.1.113883.10.20.1.19', authActivity.act.templateId.get(0).root)
        /* CONF-70: Act / @classCode is 'ACT' */
        Assert.assertEquals('CONF-70 Failed', 'ACT', authActivity.act.classCode.name)
        /* CONF-71: authorization activity has at least one Act / id */
        Assert.assertNotNull('CONF-71 Failed', authActivity.act.id)
        /* CONF-72: Act / @moodCode is 'EVN' */
        Assert.assertEquals('CONF-72 Failed', 'EVN', authActivity.act.moodCode.name)
        /* CONF-73: authorization activity Act / entryRelationship / size >= 1 */
        Assert.assertTrue('CONF-73', authActivity.act.entryRelationship.size >= 1)
        /* CONF-74: Act / entryRelationship / @typeCode is 'SUBJ' */
        authActivity.act.entryRelationship.eachWithIndex{ item, index ->
            Assert.assertEquals('CONF-74 Failed typeCode on ${index}',
                    'SUBJ',
                    item.typeCode.name)
        }
        /* CONF-75: The target of an authorization activity with 
         * Act / entryRelationship / @typeCode=”SUBJ” SHALL be 
         * a clinical statement with moodCode = “PRMS” (Promise).
         */
        //TODO

        return true
    }

    /**
     * Implements set of CCD Advance Directives conformance rules
     */
    public static boolean checkCCDAdvanceDirectivesConformance(POCDMT000040Section section){
        Assert.assertTrue section instanceof POCDMT000040Section
        /* CONF-78: Section / code != null */
        Assert.assertNotNull('CONF-78 Failed', section.code)
        /* CONF-79: Section / code value is LOINC:2.16.840.1.113883.6.1:42348-3 */
        Assert.assertEquals('CONF-79 Failed code', '42348-3',section.code.code)
        Assert.assertEquals('CONF-79 Failed code system', '2.16.840.1.113883.6.1', section.code.codeSystem)
        /* CONF-80: Section / title != null */
        Assert.assertNotNull('CONF-80 Failed:', section.title)
        /* CONF-81: Section / title contains 'advance directives' */
        Assert.assertTrue('CONF-81 Failed', section.title.mixed[0].value.matches('(?i).*advance directives*.'))
        /* check advance directive observations */
        section.entry.eachWithIndex{ item, index ->
            Assert.assertTrue('CONF-82 Failed item ${index} is not an observation', item.observation != null)
            checkCCDAdvanceDirectiveObservationConformance(item.observation)
        }       
        return true
    }

    public static boolean checkCCDAdvanceDirectiveObservationConformance(POCDMT000040Observation observation){
        /* CONF-82: advance directive observation (templateId 2.16.840.1.113883.10.20.1.17) */
        Assert.assertTrue('CONF-82 Failed templateId', '2.16.840.1.113883.10.20.1.17' in observation.templateId.root)
        /* CONF-83: Observation / @classCode value is 'OBS' */
        Assert.assertEquals('CONF-83 Failed', 'OBS', observation.classCode.name)
        /* CONF-84: Observation / @moodCode value is 'EVN' */
        Assert.assertEquals('CONF-83 Failed', 'EVN', observation.moodCode.name)
        /* CONF-85: Observation / id. != null */
        Assert.assertTrue('CONF-83 Failed', observation.id.size >= 1)
        /* CONF-86: Observation / statusCode != null */
        Assert.assertNotNull('CONF-86 Failed', observation.statusCode)
        /* CONF-87: Observation / statusCode value is 'completed' */
        Assert.assertEquals('CONF-87 Failed', 'completed', observation.statusCode.code)
        /* CONF-88: An advance directive observation SHOULD contain exactly one Observation / effectiveTime,
         *          to indicate the effective time of the directive. */
        /* CONF-89: Observation / code == 1 */
        Assert.assertTrue('CONF-89 Failed', observation.code != null)
        /* CONF-90: The value for “Observation / code” in an advance directive observation MAY be
         *          selected from ValueSet 2.16.840.1.113883.1.11.20.2 AdvanceDirectiveTypeCode STATIC 20061017. */
        /* CONF-91: There SHOULD be an advance directive observation whose value for “Observation / code”
        *           is “304251008” “Resuscitation status” 2.16.840.1.113883.6.96 SNOMED CT STATIC. */
        /* Advance directive observation verifier */
        def verifiers = observation.participant.findAll{it.typeCode == ParticipationVerifier.VRF_LITERAL}
        verifiers.eachWithIndex{  item, index ->
            /* CONF-92: Observation / participant / templateId 2.16.840.1.113883.10.20.1.58) SHALL be represented with . */
            Assert.assertTrue('CONF-92 Failed verifier ${index}', '2.16.840.1.113883.10.20.1.58' in item.templateId.root)
            /* CONF-94: Observation / participant / @typeCode is 'VRF' */
            Assert.assertTrue('CONF-94 Failed verifier index ${index}', item.typeCode == ParticipationVerifier.VRF_LITERAL)
        }
        /* CONF-97: An advance directive observation SHALL contain one or more sources of information,
         *          as defined in section 5.2 Source. */
        //TODO
        /* Advance directive status */
        def statusObservation = observation.entryRelationship.findAll{'2.16.840.1.113883.10.20.1.37' in it.observation.templateId.root}?.observation
        /* CONF-98: advance directive status observation count is 1 */
        Assert.assertEquals('CONF-98 Failed', 1, statusObservation.size)
        /* CONF-99: directive status observation (templateId 2.16.840.1.113883.10.20.1.37) is
         *          conformant status observation (templateId 2.16.840.1.113883.10.20.1.57) */
        //checkObservationStatusConformance(observation)
        /* CONF-100: Observation / value” in an advance directive status observation SHALL be selected from
         *           ValueSet 2.16.840.1.113883.1.11.20.1 AdvanceDirectiveStatusCode STATIC 20061017. */
        /* advance directive references  */

        def reference = observation.reference.findAll{it.typeCode == XActRelationshipExternalReference.REFR_LITERAL}
        /* CONF-101: advance directive reference (templateId 2.16.840.1.113883.10.20.1.36)
        *            is Observation / reference / ExternalDocument. */
        Assert.assertNotNull('CONF-101 Failed', reference[0].externalDocument != null)
        Assert.assertTrue('CONF-101 Failed templateID', '2.16.840.1.113883.10.20.1.36' in reference[0].externalDocument.templateId.root)
        /* CONF-103: Observation / reference / @typeCode value is REFR */
        Assert.assertTrue('CONF-103 Failed', reference[0].typeCode == XActRelationshipExternalReference.REFR_LITERAL)
        /* CONF-104: ExternalDocument / id count >= 1 */
        Assert.assertTrue('CONF-104 Failed', reference[0].externalDocument.id.size >= 1 )
        /* CONF-105: The URL of a referenced advance directive document MAY be present, and SHALL be represented in
         *           Observation / reference / ExternalDocument / text / reference. A <linkHTML> element containing
         *           the same URL SHOULD be present in the associated CDA Narrative Block. */
        /* CONF-106: The MIME type of a referenced advance directive document MAY be present, and SHALL be represented
        *            in Observation / reference / ExternalDocument / text / @mediaType. */
        /* CONF-107: Where the value of Observation / reference / seperatableInd is “false”,
         *           the referenced advance directive document SHOULD be included in the CCD exchange package.
         *           The exchange mechanism SHOULD be based on Internet standard RFC 2557 “MIME Encapsulation of
         *           Aggregate Documents, such as HTML (MHTML)” (http://www.ietf.org/rfc/rfc2557.txt).
         *           (See CDA Release 2, section 3 “CDA Document Exchange in HL7 Messages” for examples and additional details).
         */

        return true
    }

    public static def retrievePayersPolicyActivity(POCDMT000040EntryRelationship entryRelationship){
        if (entryRelationship.eIsSet(CDAR2Package.POCDMT000040_ENTRY_RELATIONSHIP__ACT))
            return entryRelationship.act
        else if (entryRelationship.eIsSet(CDAR2Package.POCDMT000040_ENTRY_RELATIONSHIP__ENCOUNTER))
            return entryRelationship.encounter
        else if (entryRelationship.eIsSet(CDAR2Package.POCDMT000040_ENTRY_RELATIONSHIP__OBSERVATION))
                return entryRelationship.observation
            else if (entryRelationship.eIsSet(CDAR2Package.POCDMT000040_ENTRY_RELATIONSHIP__ORGANIZER))
                    return entryRelationship.organizer
                else if (entryRelationship.eIsSet(CDAR2Package.POCDMT000040_ENTRY_RELATIONSHIP__SUBSTANCE_ADMINISTRATION))
                        return entryRelationship.substanceAdministration
                    else if (entryRelationship.eIsSet(CDAR2Package.POCDMT000040_ENTRY_RELATIONSHIP__SUPPLY))
                            return entryRelationship.supply
                        else
                            return null
    }

}
