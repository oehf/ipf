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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ebxml30;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetAllQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryList;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.AbstractQueryTransformerTest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.GetAllQueryTransformer;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link GetAllQueryTransformer}.
 * @author Jens Riemschneider
 */
public class GetAllQueryTransformerTest extends AbstractQueryTransformerTest<GetAllQuery, GetAllQueryTransformer> {
    
    @BeforeEach
    public void setUp() {
        transformer = GetAllQueryTransformer.getInstance();
        query = emptyQuery();
        
        query.setPatientId(new Identifiable("id1", new AssigningAuthority("uni1", "uniType1")));
        var confidentialityCodes = new QueryList<Code>();
        confidentialityCodes.getOuterList().add(
                Arrays.asList(new Code("code10", null, "scheme10"), new Code("code11", null, "scheme11")));
        confidentialityCodes.getOuterList().add(
                Collections.singletonList(new Code("code12", null, "scheme12")));
        query.setConfidentialityCodes(confidentialityCodes);
        query.setFormatCodes(Arrays.asList(new Code("code13", null, "scheme13"), new Code("code14", null, "scheme14")));
        query.setStatusDocuments(Arrays.asList(AvailabilityStatus.APPROVED, AvailabilityStatus.DEPRECATED));
        query.setStatusFolders(Collections.singletonList(AvailabilityStatus.DEPRECATED));
        query.setStatusSubmissionSets(Collections.singletonList(AvailabilityStatus.DEPRECATED));
        query.setHomeCommunityId("12.21.41");
        query.setDocumentEntryTypes(Collections.singletonList(DocumentEntryType.STABLE));

        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }
    
    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);
        assertEquals(QueryType.GET_ALL.getId(), ebXML.getId());
        assertEquals("12.21.41", ebXML.getHome());

        assertEquals(Collections.singletonList("'id1^^^&uni1&uniType1'"),
                ebXML.getSlotValues(QueryParameter.PATIENT_ID.getSlotName()));

        var slots = ebXML.getSlots(QueryParameter.DOC_ENTRY_CONFIDENTIALITY_CODE.getSlotName());
        assertEquals(2, slots.size());
        assertEquals(Arrays.asList("('code10^^scheme10')", "('code11^^scheme11')"), slots.get(0).getValueList());
        assertEquals(Collections.singletonList("('code12^^scheme12')"), slots.get(1).getValueList());
        
        assertEquals(Arrays.asList("('code13^^scheme13')", "('code14^^scheme14')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_FORMAT_CODE.getSlotName()));

        assertEquals(Arrays.asList("('urn:oasis:names:tc:ebxml-regrep:StatusType:Approved')", "('urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_STATUS.getSlotName()));

        assertEquals(Collections.singletonList("('urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated')"),
                ebXML.getSlotValues(QueryParameter.FOLDER_STATUS.getSlotName()));

        assertEquals(Collections.singletonList("('urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated')"),
                ebXML.getSlotValues(QueryParameter.SUBMISSION_SET_STATUS.getSlotName()));

        assertEquals(Collections.singletonList("('urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1')"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_TYPE.getSlotName()));

        assertEquals(8, ebXML.getSlots().size());
    }

    @Override
    protected GetAllQuery emptyQuery() {
        return new GetAllQuery();
    }
}
