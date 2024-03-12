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
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SubscriptionForPatientIndependentSubmissionSetQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SubscriptionForSubmissionSetQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

/**
 * Transforms between a {@link SubscriptionForSubmissionSetQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Christian Ohr
 */
public class SubscriptionForPatientIndependentSubmissionSetQueryTransformer extends AbstractStoredQueryTransformer<SubscriptionForPatientIndependentSubmissionSetQuery> {

    @Getter
    private static final SubscriptionForPatientIndependentSubmissionSetQueryTransformer instance = new SubscriptionForPatientIndependentSubmissionSetQueryTransformer();

    private SubscriptionForPatientIndependentSubmissionSetQueryTransformer() {
        super();
    }

    @Override
    protected void toEbXML(SubscriptionForPatientIndependentSubmissionSetQuery query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromStringList(SUBMISSION_SET_SOURCE_ID, query.getSourceIds());
        slots.fromStringList(SUBMISSION_SET_AUTHOR_PERSON, query.getAuthorPersons());
        slots.fromStringList(SUBMISSION_SET_INTENDED_RECIPIENT, query.getIntendedRecipients());
    }

    @Override
    protected void fromEbXML(SubscriptionForPatientIndependentSubmissionSetQuery query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        query.setSourceIds(slots.toStringList(SUBMISSION_SET_SOURCE_ID));
        query.setAuthorPersons(slots.toStringList(SUBMISSION_SET_AUTHOR_PERSON));
        query.setIntendedRecipients(slots.toStringList(SUBMISSION_SET_INTENDED_RECIPIENT));
    }

}
