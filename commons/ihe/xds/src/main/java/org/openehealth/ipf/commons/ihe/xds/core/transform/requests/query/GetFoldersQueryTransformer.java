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
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetFoldersQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

/**
 * Transforms between a {@link GetFoldersQuery} and {@link EbXMLAdhocQueryRequest}.
 *
 * @author Jens Riemschneider
 */
public class GetFoldersQueryTransformer extends GetByIDQueryTransformer<GetFoldersQuery> {

    @Getter
    private static final GetFoldersQueryTransformer instance = new GetFoldersQueryTransformer();

    /**
     * Constructs the transformer.
     */
    private GetFoldersQueryTransformer() {
        super(FOLDER_UUID, FOLDER_UNIQUE_ID);
    }

    @Override
    protected void toEbXML(GetFoldersQuery query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromStringList(FOLDER_LOGICAL_ID, query.getLogicalUuid());
        slots.fromInteger(METADATA_LEVEL, query.getMetadataLevel());
    }

    @Override
    protected void fromEbXML(GetFoldersQuery query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        query.setLogicalUuid(slots.toStringList(FOLDER_LOGICAL_ID));
        query.setMetadataLevel(slots.toInteger(METADATA_LEVEL));
    }

}
