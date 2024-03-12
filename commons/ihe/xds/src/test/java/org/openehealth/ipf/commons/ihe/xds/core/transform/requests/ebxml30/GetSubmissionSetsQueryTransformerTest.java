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
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.GetSubmissionSetsQuery;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryType;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.AbstractQueryTransformerTest;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.GetSubmissionSetsQueryTransformer;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link GetSubmissionSetsQueryTransformer}.
 * @author Jens Riemschneider
 */
public class GetSubmissionSetsQueryTransformerTest extends AbstractQueryTransformerTest<GetSubmissionSetsQuery, GetSubmissionSetsQueryTransformer> {
    
    @BeforeEach
    public void setUp() {
        transformer = GetSubmissionSetsQueryTransformer.getInstance();
        query = emptyQuery();

        query.setUuids(Arrays.asList("uuid1", "uuid2"));
        query.setHomeCommunityId("home");

        ebXML = new EbXMLFactory30().createAdhocQueryRequest();
    }
    
    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);
        assertEquals(QueryType.GET_SUBMISSION_SETS.getId(), ebXML.getId());
        
        assertEquals(Arrays.asList("('uuid1')", "('uuid2')"),
                ebXML.getSlotValues(QueryParameter.UUID.getSlotName()));
        
        assertEquals("home", ebXML.getHome());
        assertEquals(1, ebXML.getSlots().size());
    }

    @Override
    protected GetSubmissionSetsQuery emptyQuery() {
        return new GetSubmissionSetsQuery();
    }
}
