/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query;

import lombok.Getter;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsExcludeQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_AUTHOR_PERSON_EXCLUDE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_CLASS_CODE_EXCLUDE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_CONFIDENTIALITY_CODE_EXCLUDE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_CONFIDENTIALITY_CODE_EXCLUDE_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_EVENT_CODE_EXCLUDE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_EVENT_CODE_EXCLUDE_SCHEME;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_FORMAT_CODE_EXCLUDE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_EXCLUDE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_PRACTICE_SETTING_CODE_EXCLUDE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_REFERENCE_IDS;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_REFERENCE_IDS_EXCLUDE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_TYPE_CODE_EXCLUDE;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_TYPE_EXCLUDE;

/**
 * Transforms between a {@link FindDocumentsExcludeQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Christian Ohr
 * @since 5.3
 */
public class FindDocumentsExcludeQueryTransformer extends AbstractFindDocumentsQueryTransformer<FindDocumentsExcludeQuery> {

    @Getter
    private static final FindDocumentsExcludeQueryTransformer instance = new FindDocumentsExcludeQueryTransformer();

    private FindDocumentsExcludeQueryTransformer() {
        super();
    }

    @Override
    protected void toEbXML(FindDocumentsExcludeQuery query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromStringList(DOC_ENTRY_REFERENCE_IDS, query.getReferenceIds());
        slots.fromStringList(DOC_ENTRY_REFERENCE_IDS_EXCLUDE, query.getExcludedReferenceIds());
        slots.fromCode(DOC_ENTRY_CLASS_CODE_EXCLUDE, query.getExcludedClassCodes());
        slots.fromCode(DOC_ENTRY_TYPE_CODE_EXCLUDE, query.getExcludedTypeCodes());
        slots.fromCode(DOC_ENTRY_PRACTICE_SETTING_CODE_EXCLUDE, query.getExcludedPracticeSettingCodes());
        slots.fromCode(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_EXCLUDE, query.getExcludedHealthcareFacilityTypeCodes());
        slots.fromCode(DOC_ENTRY_EVENT_CODE_EXCLUDE, query.getExcludedEventCodes());
        slots.fromCode(DOC_ENTRY_CONFIDENTIALITY_CODE_EXCLUDE, query.getExcludedConfidentialityCodes());
        slots.fromStringList(DOC_ENTRY_AUTHOR_PERSON_EXCLUDE, query.getExcludedAuthorPersons());
        slots.fromCode(DOC_ENTRY_FORMAT_CODE_EXCLUDE, query.getExcludedFormatCodes());
        slots.fromDocumentEntryType(DOC_ENTRY_TYPE_EXCLUDE, query.getExcludedDocumentEntryTypes());
    }

    @Override
    protected void fromEbXML(FindDocumentsExcludeQuery query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        query.setReferenceIds(slots.toStringQueryList(DOC_ENTRY_REFERENCE_IDS));
        query.setExcludedReferenceIds(slots.toStringQueryList(DOC_ENTRY_REFERENCE_IDS_EXCLUDE));
        query.setExcludedClassCodes(slots.toCodeList(DOC_ENTRY_CLASS_CODE_EXCLUDE));
        query.setExcludedTypeCodes(slots.toCodeList(DOC_ENTRY_TYPE_CODE_EXCLUDE));
        query.setExcludedPracticeSettingCodes(slots.toCodeList(DOC_ENTRY_PRACTICE_SETTING_CODE_EXCLUDE));
        query.setExcludedHealthcareFacilityTypeCodes(slots.toCodeList(DOC_ENTRY_HEALTHCARE_FACILITY_TYPE_CODE_EXCLUDE));
        query.setExcludedEventCodes(slots.toCodeQueryList(DOC_ENTRY_EVENT_CODE_EXCLUDE, DOC_ENTRY_EVENT_CODE_EXCLUDE_SCHEME));
        query.setExcludedConfidentialityCodes(slots.toCodeQueryList(DOC_ENTRY_CONFIDENTIALITY_CODE_EXCLUDE, DOC_ENTRY_CONFIDENTIALITY_CODE_EXCLUDE_SCHEME));
        query.setExcludedAuthorPersons(slots.toStringList(DOC_ENTRY_AUTHOR_PERSON_EXCLUDE));
        query.setExcludedFormatCodes(slots.toCodeList(DOC_ENTRY_FORMAT_CODE_EXCLUDE));
        query.setExcludedDocumentEntryTypes(slots.toDocumentEntryType(DOC_ENTRY_TYPE_EXCLUDE));
    }
}
