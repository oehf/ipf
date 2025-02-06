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
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindFoldersQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.*;

/**
 * Transforms between a {@link FindFoldersQuery} and {@link EbXMLAdhocQueryRequest}.
 * @author Jens Riemschneider
 */
public class FindFoldersQueryTransformer extends AbstractStoredQueryTransformer<FindFoldersQuery>{

    @Getter
    private static final FindFoldersQueryTransformer instance = new FindFoldersQueryTransformer();

    private FindFoldersQueryTransformer() {
    }

    @Override
    protected void toEbXML(FindFoldersQuery query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromString(FOLDER_PATIENT_ID, Hl7v2Based.render(query.getPatientId()));
        slots.fromTimestamp(FOLDER_LAST_UPDATE_TIME_FROM, query.getLastUpdateTime().getFrom());
        slots.fromTimestamp(FOLDER_LAST_UPDATE_TIME_TO, query.getLastUpdateTime().getTo());
        slots.fromCode(FOLDER_CODES, query.getCodes());
        slots.fromStatus(FOLDER_STATUS, query.getStatus());
        slots.fromInteger(METADATA_LEVEL, query.getMetadataLevel());
    }

    @Override
    protected void fromEbXML(FindFoldersQuery query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        var patientId = slots.toString(FOLDER_PATIENT_ID);
        query.setPatientId(Identifiable.parse(patientId));
        query.setCodes(slots.toCodeQueryList(FOLDER_CODES, FOLDER_CODES_SCHEME));
        query.getLastUpdateTime().setFrom(slots.toTimestamp(FOLDER_LAST_UPDATE_TIME_FROM));
        query.getLastUpdateTime().setTo(slots.toTimestamp(FOLDER_LAST_UPDATE_TIME_TO));
        query.setStatus(slots.toStatus(FOLDER_STATUS));
        query.setMetadataLevel(slots.toInteger(METADATA_LEVEL));
    }
}
