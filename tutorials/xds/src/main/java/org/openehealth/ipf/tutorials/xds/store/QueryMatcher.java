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
package org.openehealth.ipf.tutorials.xds.store;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsExcludeQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindFoldersQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindSubmissionSetsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.Query;

import static org.openehealth.ipf.tutorials.xds.store.Comparators.any;
import static org.openehealth.ipf.tutorials.xds.store.Comparators.contains;
import static org.openehealth.ipf.tutorials.xds.store.Comparators.containsNot;
import static org.openehealth.ipf.tutorials.xds.store.Comparators.matchesValue;
import static org.openehealth.ipf.tutorials.xds.store.Comparators.evalExcludedQueryList;
import static org.openehealth.ipf.tutorials.xds.store.Comparators.evalQueryList;
import static org.openehealth.ipf.tutorials.xds.store.Comparators.isInRange;
import static org.openehealth.ipf.tutorials.xds.store.Comparators.matchesAuthor;
import static org.openehealth.ipf.tutorials.xds.store.Comparators.matchesAuthors;
import static org.openehealth.ipf.tutorials.xds.store.Comparators.matchesCode;
import static org.openehealth.ipf.tutorials.xds.store.Comparators.matchesEntryType;
import static org.openehealth.ipf.tutorials.xds.store.Comparators.matchesNoAuthor;
import static org.openehealth.ipf.tutorials.xds.store.Comparators.none;

/**
 * Matches stored entries against the parameters of the Find... stored queries.
 *
 * @author Jens Riemschneider
 */
final class QueryMatcher {

    private QueryMatcher() {
    }

    /**
     * @param entry a stored entry of the kind the query searches for
     * @param query a FindDocuments, FindDocumentsExclude, FindFolders or FindSubmissionSets query
     * @return whether the entry matches the query
     * @throws IllegalArgumentException if the query does not search for this kind of entry
     */
    static boolean matches(Object entry, Query query) {
        // FindDocumentsExclude extends FindDocuments, so it must be tested first
        if (entry instanceof DocumentEntry documentEntry && query instanceof FindDocumentsExcludeQuery exclude) {
            return matchesFindDocumentsExclude(documentEntry, exclude);
        }
        if (entry instanceof DocumentEntry documentEntry && query instanceof FindDocumentsQuery findDocuments) {
            return matchesFindDocuments(documentEntry, findDocuments);
        }
        if (entry instanceof Folder folder && query instanceof FindFoldersQuery findFolders) {
            return matchesFindFolders(folder, findFolders);
        }
        if (entry instanceof SubmissionSet submissionSet && query instanceof FindSubmissionSetsQuery findSets) {
            return matchesFindSubmissionSets(submissionSet, findSets);
        }
        throw new IllegalArgumentException("A " + query.getClass().getSimpleName()
                + " cannot be matched against a " + entry.getClass().getSimpleName());
    }

    private static boolean matchesFindSubmissionSets(SubmissionSet entry, FindSubmissionSetsQuery query) {
        return matchesValue(query.getPatientId(), entry.getPatientId())
            && contains(query.getStatus(), entry.getAvailabilityStatus())
            && contains(query.getSourceIds(), entry.getSourceId())
            && any(query.getContentTypeCodes(), code -> matchesCode(code, entry.getContentTypeCode()))
            && isInRange(query.getSubmissionTime(), entry.getSubmissionTime())
            && matchesAuthor(query.getAuthorPerson(), entry.getAuthors());
    }

    private static boolean matchesFindFolders(Folder entry, FindFoldersQuery query) {
        return matchesValue(query.getPatientId(), entry.getPatientId())
            && contains(query.getStatus(), entry.getAvailabilityStatus())
            && isInRange(query.getLastUpdateTime(), entry.getLastUpdateTime())
            && evalQueryList(query.getCodes(), entry.getCodeList());
    }

    /**
     * FindDocumentsExclude is a FindDocuments query with a second set of parameters naming the values
     * that must <em>not</em> occur in the returned metadata (ITI TF-2: 3.18.4.1.2.3.7.15).
     * <p>
     * Each excluding parameter is mutually exclusive with its non-excluding counterpart, which the
     * request validator enforces, so the two sets never contradict each other here.
     */
    private static boolean matchesFindDocumentsExclude(DocumentEntry entry, FindDocumentsExcludeQuery query) {
        return matchesFindDocuments(entry, query)
            && none(query.getExcludedClassCodes(), code -> matchesCode(code, entry.getClassCode()))
            && none(query.getExcludedTypeCodes(), code -> matchesCode(code, entry.getTypeCode()))
            && none(query.getExcludedFormatCodes(), code -> matchesCode(code, entry.getFormatCode()))
            && none(query.getExcludedHealthcareFacilityTypeCodes(),
                    code -> matchesCode(code, entry.getHealthcareFacilityTypeCode()))
            && none(query.getExcludedPracticeSettingCodes(), code -> matchesCode(code, entry.getPracticeSettingCode()))
            && evalExcludedQueryList(query.getExcludedEventCodes(), entry.getEventCodeList())
            && evalExcludedQueryList(query.getExcludedConfidentialityCodes(), entry.getConfidentialityCodes())
            && matchesNoAuthor(query.getExcludedAuthorPersons(), entry.getAuthors())
            && containsNot(query.getExcludedDocumentEntryTypes(), entry.getType());
    }

    private static boolean matchesFindDocuments(DocumentEntry entry, FindDocumentsQuery query) {
        return matchesValue(query.getPatientId(), entry.getPatientId())
            && contains(query.getStatus(), entry.getAvailabilityStatus())
            && isInRange(query.getCreationTime(), entry.getCreationTime())
            && isInRange(query.getServiceStartTime(), entry.getServiceStartTime(), true)
            && isInRange(query.getServiceStopTime(), entry.getServiceStopTime(), true)
            && any(query.getClassCodes(), code -> matchesCode(code, entry.getClassCode()))
            && any(query.getTypeCodes(), code -> matchesCode(code, entry.getTypeCode()))
            && any(query.getFormatCodes(), code -> matchesCode(code, entry.getFormatCode()))
            && any(query.getHealthcareFacilityTypeCodes(), code -> matchesCode(code, entry.getHealthcareFacilityTypeCode()))
            && any(query.getPracticeSettingCodes(), code -> matchesCode(code, entry.getPracticeSettingCode()))
            && evalQueryList(query.getEventCodes(), entry.getEventCodeList())
            && evalQueryList(query.getConfidentialityCodes(), entry.getConfidentialityCodes())
            && matchesAuthors(query.getAuthorPersons(), entry.getAuthors())
            && matchesEntryType(query.getDocumentEntryTypes(), entry.getType());
    }
}
