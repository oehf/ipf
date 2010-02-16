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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.ebxml21;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21.EbXMLFactory21;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.SqlQuery;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.SqlQueryTransformer;

/**
 * Tests for {@link SqlQueryTransformer}.
 * @author Jens Riemschneider
 */
public class SqlQueryTransformerTest {
    private SqlQueryTransformer transformer;
    private SqlQuery query;
    private EbXMLAdhocQueryRequest ebXML;

    @Before
    public void setUp() {
        transformer = new SqlQueryTransformer();
        query = (SqlQuery) SampleData.createSqlQuery().getQuery();
        
        ebXML = new EbXMLFactory21().createAdhocQueryRequest();
    }

    @Test
    public void testToEbXML() {
        transformer.toEbXML(query, ebXML);
        assertEquals("SELECT * FROM INTERNET", ebXML.getSql());
    }
    
    @Test
    public void testToEbXMLNull() {
        transformer.toEbXML(null, ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }
    
    @Test
    public void testToEbXMLEmpty() {
        transformer.toEbXML(new SqlQuery(), ebXML);
        assertEquals(0, ebXML.getSlots().size());
    }

    
    
    @Test
    public void testFromEbXML() {
        transformer.toEbXML(query, ebXML);
        SqlQuery result = new SqlQuery();
        transformer.fromEbXML(result, ebXML);
        
        assertEquals(query, result);
    }
    
    @Test
    public void testFromEbXMLNull() {
        SqlQuery result = new SqlQuery();
        transformer.fromEbXML(result, null);        
        assertEquals(new SqlQuery(), result);
    }
        
    @Test
    public void testFromEbXMLEmpty() {
        SqlQuery result = new SqlQuery();
        transformer.fromEbXML(result, ebXML);        
        assertEquals(new SqlQuery(), result);
    }
}
