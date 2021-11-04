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

import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link EbXML30Converters}.
 * @author Jens Riemschneider
 */
public class TestEbXML30Converters {
    @Test
    public void testConvertProvideAndRegisterDocumentSet() {
        var org = SampleData.createProvideAndRegisterDocumentSet();
        var converted = EbXML30Converters.convert(org);
        var copy = EbXML30Converters.convert(converted);
        assertEquals(org, copy);
    }

    @Test
    public void testConvertRegisterDocumentSet() {
        var org = SampleData.createRegisterDocumentSet();
        var converted = EbXML30Converters.convert(org);
        var copy = EbXML30Converters.convert(converted);
        assertEquals(org, copy);
    }

    @Test
    public void testConvertResponse() {
        var org = SampleData.createResponse();
        var converted = EbXML30Converters.convert(org);
        var copy = EbXML30Converters.convert(converted);
        assertEquals(org, copy);
    }

    @Test
    public void testConvertQueryRegistry() {
        var org = SampleData.createFindDocumentsQuery();
        var converted = EbXML30Converters.convert(org);
        var copy = EbXML30Converters.convert(converted);
        assertEquals(org, copy);
    }

    @Test
    public void testConvertQueryResponse() {
        var org = SampleData.createQueryResponseWithLeafClass();
        var converted = EbXML30Converters.convert(org);
        var copy = EbXML30Converters.convertToQueryResponse(converted);
        assertEquals(org, copy);
    }

    @Test
    public void testConvertRetrieveDocumentSet() {
        var org = SampleData.createRetrieveDocumentSet();
        var converted = EbXML30Converters.convert(org);
        var copy = EbXML30Converters.convert(converted);
        assertEquals(org, copy);
    }

    @Test
    public void testConvertRetrieveImagingDocumentSet() {
        var org = SampleData.createRetrieveImagingDocumentSet();
        var converted = EbXML30Converters.convert(org);
        var copy = EbXML30Converters.convert(converted);
        assertEquals(org, copy);
    }
}
