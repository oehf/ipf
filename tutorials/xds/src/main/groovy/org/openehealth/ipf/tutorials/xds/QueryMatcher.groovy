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
package org.openehealth.ipf.tutorials.xds

import static org.openehealth.ipf.tutorials.xds.Comparators.*

import org.openehealth.ipf.commons.ihe.xds.core.metadata.*
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.*


/**
 * Matcher used by the {@link SearchDefinition} to match queries. Note that this
 * uses dynamic method dispatching in Groovy to find the method that fits
 * the entry and query type. 
 * @author Jens Riemschneider
 */
class QueryMatcher {
    def matches(SubmissionSet entry, FindSubmissionSetsQuery query) {
        equals(query.patientId, entry.patientId) &&
        contains(query.status, entry.availabilityStatus) &&
        contains(query.sourceIds, entry.sourceId) &&
        any(query.contentTypeCodes) { matchesCode(it, entry.contentTypeCode) } &&
        isInRange(query.submissionTime, entry.submissionTime) &&
        matchesAuthor(query.authorPerson, entry.authors)
    }
    
    def matches(Folder entry, FindFoldersQuery query) {        
        equals(query.patientId, entry.patientId) &&
        contains(query.status, entry.availabilityStatus) &&
        isInRange(query.lastUpdateTime, entry.lastUpdateTime) &&
        evalQueryList(query.codes, entry.codeList)
    }
    
    def matches(DocumentEntry entry, FindDocumentsQuery query) {
        equals(query.patientId, entry.patientId) &&
        contains(query.status, entry.availabilityStatus) &&
        isInRange(query.creationTime, entry.creationTime) &&
        isInRange(query.serviceStartTime, entry.serviceStartTime) &&
        isInRange(query.serviceStopTime, entry.serviceStopTime) &&
        any(query.classCodes) { matchesCode(it, entry.classCode) } &&
        any(query.typeCodes) { matchesCode(it, entry.typeCode) } &&
        any(query.formatCodes) { matchesCode(it, entry.formatCode) } &&
        any(query.healthcareFacilityTypeCodes) { matchesCode(it, entry.healthcareFacilityTypeCode) } &&
        any(query.practiceSettingCodes) { matchesCode(it, entry.practiceSettingCode) } &&
        evalQueryList(query.eventCodes, entry.eventCodeList) &&
        evalQueryList(query.confidentialityCodes, entry.confidentialityCodes) &&
        matchesAuthors(query.authorPersons, entry.authors) &&
        contains(query.documentEntryTypes, entry.type)
    }
}
