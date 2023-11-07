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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query;

import lombok.Getter;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindSubmissionSetsQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

/**
 * Transforms between {@link FindSubmissionSetsQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Jens Riemschneider
 */
public class FindSubmissionSetsQueryTransformer extends AbstractStoredQueryTransformer<FindSubmissionSetsQuery> {

    @Getter
    private static final FindSubmissionSetsQueryTransformer instance = new FindSubmissionSetsQueryTransformer();

    private FindSubmissionSetsQueryTransformer() {
    }

    @Override
    protected void toEbXML(FindSubmissionSetsQuery query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromString(SUBMISSION_SET_PATIENT_ID, Hl7v2Based.render(query.getPatientId()));
        slots.fromStringList(SUBMISSION_SET_SOURCE_ID, query.getSourceIds());
        slots.fromTimestamp(SUBMISSION_SET_SUBMISSION_TIME_FROM, query.getSubmissionTime().getFrom());
        slots.fromTimestamp(SUBMISSION_SET_SUBMISSION_TIME_TO, query.getSubmissionTime().getTo());
        slots.fromString(SUBMISSION_SET_AUTHOR_PERSON, query.getAuthorPerson());
        slots.fromCode(SUBMISSION_SET_CONTENT_TYPE_CODE, query.getContentTypeCodes());
        slots.fromStatus(SUBMISSION_SET_STATUS, query.getStatus());
    }

    @Override
    protected void fromEbXML(FindSubmissionSetsQuery query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        var patientId = slots.toString(SUBMISSION_SET_PATIENT_ID);
        query.setPatientId(Hl7v2Based.parse(patientId, Identifiable.class));
        query.setSourceIds(slots.toStringList(SUBMISSION_SET_SOURCE_ID));
        query.getSubmissionTime().setFrom(slots.toTimestamp(SUBMISSION_SET_SUBMISSION_TIME_FROM));
        query.getSubmissionTime().setTo(slots.toTimestamp(SUBMISSION_SET_SUBMISSION_TIME_TO));
        query.setAuthorPerson(slots.toString(SUBMISSION_SET_AUTHOR_PERSON));
        query.setContentTypeCodes(slots.toCodeList(SUBMISSION_SET_CONTENT_TYPE_CODE));
        query.setStatus(slots.toStatus(SUBMISSION_SET_STATUS));
    }
}
