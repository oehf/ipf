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
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetAssociationsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetSubmissionSetsQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.METADATA_LEVEL;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.UUID;

/**
 * Transforms between {@link GetAssociationsQuery} and {@link EbXMLAdhocQueryRequest}.
 *
 * @author Jens Riemschneider
 */
public class GetSubmissionSetsQueryTransformer extends GetByUUIDQueryTransformer<GetSubmissionSetsQuery> {

    @Getter
    private static final GetSubmissionSetsQueryTransformer instance = new GetSubmissionSetsQueryTransformer();


    /**
     * Constructs the transformer.
     */
    private GetSubmissionSetsQueryTransformer() {
        super(UUID);
    }

    @Override
    protected void toEbXML(GetSubmissionSetsQuery query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromInteger(METADATA_LEVEL, query.getMetadataLevel());
    }

    @Override
    protected void fromEbXML(GetSubmissionSetsQuery query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        query.setMetadataLevel(slots.toInteger(METADATA_LEVEL));
    }

}