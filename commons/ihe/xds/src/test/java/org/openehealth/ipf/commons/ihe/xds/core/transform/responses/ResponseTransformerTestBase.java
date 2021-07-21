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
package org.openehealth.ipf.commons.ihe.xds.core.transform.responses;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.responses.ErrorCode;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Severity;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml.FactoryCreator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Tests for {@link ResponseTransformer}.
 * @author Jens Riemschneider
 */
public abstract class ResponseTransformerTestBase implements FactoryCreator {
    private ResponseTransformer transformer;
    private Response response;

    @BeforeEach
    public void baseSetUp() {
        var factory = createFactory();
        transformer = new ResponseTransformer(factory);
        
        response = SampleData.createResponse();
    }

    @Test
    public void testToEbXMLRegistryResponse() {
        var ebXML = transformer.toEbXML(response);

        assertEquals(Status.FAILURE, ebXML.getStatus());
        var errors = ebXML.getErrors();
        assertEquals(3, errors.size());

        var error = errors.get(0);
        assertEquals("context1", error.getCodeContext());
        assertEquals(ErrorCode.PATIENT_ID_DOES_NOT_MATCH.getOpcode(), error.getErrorCode());
        assertEquals(Severity.ERROR, error.getSeverity());
        assertEquals("location1", error.getLocation());

        error = errors.get(1);
        assertEquals("context2", error.getCodeContext());
        assertEquals(ErrorCode.SQL_ERROR.getOpcode(), error.getErrorCode());
        assertEquals(Severity.WARNING, error.getSeverity());
        assertEquals(null, error.getLocation());

        error = errors.get(2);
        assertEquals("context3", error.getCodeContext());
        assertEquals("MyCustomErrorCode", error.getErrorCode());
        assertEquals(Severity.ERROR, error.getSeverity());
        assertEquals("location3", error.getLocation());
    }
    
    @Test
    public void testToEbXMLRegistryResponseEmpty() {
        var ebXML = transformer.toEbXML(new Response());
        assertNull(ebXML.getStatus());
        assertEquals(0, ebXML.getErrors().size());
    }
    
    
    @Test
    public void testFromEbXML() {
        var ebXML = transformer.toEbXML(response);
        var result = transformer.fromEbXML(ebXML);
        assertEquals(response, result);
    }
    
    @Test
    public void testFromEbXMLEmpty() {
        var ebXML = transformer.toEbXML(new Response());
        var result = transformer.fromEbXML(ebXML);
        assertEquals(new Response(), result);
    }
}
