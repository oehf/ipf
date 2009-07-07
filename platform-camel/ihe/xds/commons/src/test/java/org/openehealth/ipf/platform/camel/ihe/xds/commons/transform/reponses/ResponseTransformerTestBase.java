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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.reponses;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.SampleData;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLFactory;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLRegistryResponse;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.ErrorCode;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.ErrorInfo;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Response;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Severity;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.Status;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.ebxml.FactoryCreator;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.transform.responses.ResponseTransformer;

/**
 * Tests for {@link ResponseTransformer}.
 * @author Jens Riemschneider
 */
public abstract class ResponseTransformerTestBase implements FactoryCreator {
    private ResponseTransformer transformer;
    private Response response;
    private EbXMLFactory factory;
    
    @Before
    public void baseSetUp() {
        factory = createFactory();
        transformer = new ResponseTransformer(factory);
        
        response = SampleData.createResponse();
    }

    @Test
    public void testToEbXMLRegistryResponse() {
        EbXMLRegistryResponse ebXML = transformer.toEbXML(response);

        assertEquals(Status.FAILURE, ebXML.getStatus());
        List<ErrorInfo> errors = ebXML.getErrors();
        assertEquals(2, errors.size());
        
        ErrorInfo error = errors.get(0);
        assertEquals("context1", error.getCodeContext());
        assertEquals(ErrorCode.PATIENT_ID_DOES_NOT_MATCH, error.getErrorCode());
        assertEquals(Severity.ERROR, error.getSeverity());
        assertEquals("location1", error.getLocation());

        error = errors.get(1);
        assertEquals("context2", error.getCodeContext());
        assertEquals(ErrorCode.SQL_ERROR, error.getErrorCode());
        assertEquals(Severity.WARNING, error.getSeverity());
        assertEquals(null, error.getLocation());
    }
    
    @Test
    public void testToEbXMLRegistryResponseEmpty() {
        EbXMLRegistryResponse ebXML = transformer.toEbXML(new Response());
        assertNull(ebXML.getStatus());
        assertEquals(0, ebXML.getErrors().size());
    }
    
    
    @Test
    public void testFromEbXML() {
        EbXMLRegistryResponse ebXML = transformer.toEbXML(response);
        Response result = transformer.fromEbXML(ebXML);
        assertEquals(response, result);
    }
    
    @Test
    public void testFromEbXMLEmpty() {
        EbXMLRegistryResponse ebXML = transformer.toEbXML(new Response());
        Response result = transformer.fromEbXML(ebXML);
        assertEquals(new Response(), result);
    }
}
