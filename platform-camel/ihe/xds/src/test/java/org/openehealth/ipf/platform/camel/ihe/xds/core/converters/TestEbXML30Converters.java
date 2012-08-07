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
package org.openehealth.ipf.platform.camel.ihe.xds.core.converters;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.ProvideAndRegisterDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30.RetrieveImagingDocumentSetRequestType;
import org.openehealth.ipf.commons.ihe.xds.core.requests.*;
import org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryRequest;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.query.AdhocQueryResponse;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rs.RegistryResponseType;

/**
 * Tests for {@link EbXML30Converters}.
 * @author Jens Riemschneider
 */
public class TestEbXML30Converters {
    @Test
    public void testConvertProvideAndRegisterDocumentSet() {
        ProvideAndRegisterDocumentSet org = SampleData.createProvideAndRegisterDocumentSet();
        ProvideAndRegisterDocumentSetRequestType converted = EbXML30Converters.convert(org);
        ProvideAndRegisterDocumentSet copy = EbXML30Converters.convert(converted);
        assertEquals(org, copy);
    }

    @Test
    public void testConvertRegisterDocumentSet() {
        RegisterDocumentSet org = SampleData.createRegisterDocumentSet();
        SubmitObjectsRequest converted = EbXML30Converters.convert(org);
        RegisterDocumentSet copy = EbXML30Converters.convert(converted);
        assertEquals(org, copy);
    }

    @Test
    public void testConvertResponse() {
        Response org = SampleData.createResponse();
        RegistryResponseType converted = EbXML30Converters.convert(org);
        Response copy = EbXML30Converters.convert(converted);
        assertEquals(org, copy);
    }

    @Test
    public void testConvertQueryRegistry() {
        QueryRegistry org = SampleData.createFindDocumentsQuery();
        AdhocQueryRequest converted = EbXML30Converters.convert(org);
        QueryRegistry copy = EbXML30Converters.convert(converted);
        assertEquals(org, copy);
    }

    @Test
    public void testConvertQueryResponse() {
        QueryResponse org = SampleData.createQueryResponseWithLeafClass();
        AdhocQueryResponse converted = EbXML30Converters.convert(org);
        QueryResponse copy = EbXML30Converters.convertToQueryResponse(converted);
        assertEquals(org, copy);
    }

    @Test
    public void testConvertRetrieveDocumentSet() {
        RetrieveDocumentSet org = SampleData.createRetrieveDocumentSet();
        RetrieveDocumentSetRequestType converted = EbXML30Converters.convert(org);
        RetrieveDocumentSet copy = EbXML30Converters.convert(converted);
        assertEquals(org, copy);
    }

    @Test
    public void testConvertRetrieveImagingDocumentSet() {
        RetrieveImagingDocumentSet org = SampleData.createRetrieveImagingDocumentSet();
        RetrieveImagingDocumentSetRequestType converted = EbXML30Converters.convert(org);
        RetrieveImagingDocumentSet copy = EbXML30Converters.convert(converted);
        assertEquals(org, copy);
    }
}
