/*
 * Copyright 2013 the original author or authors.
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
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.EbXMLFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsByTitleQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.AbstractQueryTransformerTest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.FindDocumentsByTitleQueryTransformer;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link FindDocumentsByTitleQueryTransformer}.
 * @author Christian Ohr
 */
public class FindDocumentsByTitleQueryTransformerTest extends AbstractQueryTransformerTest<FindDocumentsByTitleQuery, FindDocumentsByTitleQueryTransformer> {
    
    @BeforeEach
    public void setUp() {
        transformer = FindDocumentsByTitleQueryTransformer.getInstance();
        query = (FindDocumentsByTitleQuery) SampleData.createFindDocumentsByTitleQuery().getQuery();
        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }

    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);

        assertEquals(QueryType.FIND_DOCUMENTS_BY_TITLE.getId(), ebXML.getId());
        assertEquals("urn:oid:1.21.41", ebXML.getHome());
        assertEquals(Collections.singletonList("'id3^^^&1.3&ISO'"),
                ebXML.getSlotValues(QueryParameter.DOC_ENTRY_PATIENT_ID.getSlotName()));

        var titleSlots = ebXML.getSlots(QueryParameter.DOC_ENTRY_TITLE.getSlotName());
        assertEquals(1, titleSlots.size());
        assertEquals("('myTitle')", titleSlots.get(0).getValueList().get(0));
    }

    @Override
    protected FindDocumentsByTitleQuery emptyQuery() {
        return new FindDocumentsByTitleQuery();
    }
}
