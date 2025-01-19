/*
 * Copyright 2025 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.svs.core.converters;

import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.svs.core.requests.RetrieveValueSetRequest;
import org.openehealth.ipf.commons.ihe.svs.core.requests.ValueSetRequest;
import org.openehealth.ipf.commons.ihe.svs.core.responses.Concept;
import org.openehealth.ipf.commons.ihe.svs.core.responses.ConceptList;
import org.openehealth.ipf.commons.ihe.svs.core.responses.RetrieveValueSetResponse;
import org.openehealth.ipf.commons.ihe.svs.core.responses.ValueSetResponse;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Quentin Ligier
 **/
class SvsConvertersTest {

    @Test
    public void testMarshalUnmarshalRequest() throws JAXBException {
        var expectedRequest = new RetrieveValueSetRequest(new ValueSetRequest("1.2.3", "en", "3"));
        var xml = SvsConverters.svsQueryToXml(expectedRequest);
        var request = SvsConverters.xmlToSvsQuery(xml);

        assertEquals(expectedRequest.getValueSet().getId(), request.getValueSet().getId());
        assertEquals(expectedRequest.getValueSet().getLang(), request.getValueSet().getLang());
        assertEquals(expectedRequest.getValueSet().getVersion(), request.getValueSet().getVersion());
    }

    @Test
    public void testUnmarshalRequest() throws JAXBException {
        var xml = """
            <RetrieveValueSetRequest xmlns="urn:ihe:iti:svs:2008">
                <ValueSet id="1.2.840.10008.6.1.308" xml:lang="en-EN"/>
            </RetrieveValueSetRequest>
            """;
        var request = SvsConverters.xmlToSvsQuery(xml);

        assertEquals("1.2.840.10008.6.1.308", request.getValueSet().getId());
        assertEquals("en-EN", request.getValueSet().getLang());
        assertNull(request.getValueSet().getVersion());
    }

    @Test
    public void testMarshalUnmarshalResponse() throws JAXBException {
        var valueSet = new ValueSetResponse("1.2.3", "Value Set", "3");
        var conceptList = new ConceptList("en");
        conceptList.getConcept().add(new Concept("10.1", "1.2.3", "Code", "MyCodeSystem", "3"));
        valueSet.getConceptList().add(conceptList);
        var expectedResponse = new RetrieveValueSetResponse(valueSet, OffsetDateTime.now());

        var xml = SvsConverters.svsResponseToXml(expectedResponse);
        var response = SvsConverters.xmlToSvsResponse(xml);

        assertNotNull(response);
        assertEquals(expectedResponse.getValueSet().getId(), response.getValueSet().getId());
        assertEquals(expectedResponse.getValueSet().getDisplayName(), response.getValueSet().getDisplayName());
        assertEquals(expectedResponse.getValueSet().getVersion(), response.getValueSet().getVersion());
        assertEquals(expectedResponse.getValueSet().getConceptList().get(0).getLang(),
                     response.getValueSet().getConceptList().get(0).getLang());
        assertEquals(expectedResponse.getValueSet().getConceptList().get(0).getConcept().get(0).getCode(),
                     response.getValueSet().getConceptList().get(0).getConcept().get(0).getCode());
        assertEquals(expectedResponse.getValueSet().getConceptList().get(0).getConcept().get(0).getDisplayName(),
                     response.getValueSet().getConceptList().get(0).getConcept().get(0).getDisplayName());
        assertEquals(expectedResponse.getValueSet().getConceptList().get(0).getConcept().get(0).getCodeSystem(),
                     response.getValueSet().getConceptList().get(0).getConcept().get(0).getCodeSystem());
        assertEquals(expectedResponse.getValueSet().getConceptList().get(0).getConcept().get(0).getCodeSystemName(),
                     response.getValueSet().getConceptList().get(0).getConcept().get(0).getCodeSystemName());
        assertEquals(expectedResponse.getValueSet().getConceptList().get(0).getConcept().get(0).getCodeSystemVersion(),
                     response.getValueSet().getConceptList().get(0).getConcept().get(0).getCodeSystemVersion());
    }

    @Test
    public void testUnmarshalResponse() throws JAXBException {
        var xml = """
            <RetrieveValueSetResponse xmlns="urn:ihe:iti:svs:2008" cacheExpirationHint="2008-08-15T00:00:00-05:00">
                <ValueSet id="1.2.840.10008.6.1.308" displayName="Common Anatomic Regions Context ID 4031" version="20061023">
                    <ConceptList xml:lang="en-US">
                        <Concept code="T-D4000" displayName="Abdomen" codeSystem="2.16.840.1.113883.6.5"/>
                        <Concept code="R-FAB57" displayName="Abdomen and Pelvis" codeSystem="2.16.840.1.113883.6.5"/>
                    </ConceptList>
                </ValueSet>
            </RetrieveValueSetResponse>
            """;
        var response = SvsConverters.xmlToSvsResponse(xml);

        assertEquals(1218776400, response.getCacheExpirationHint().toEpochSecond());
        assertEquals("1.2.840.10008.6.1.308", response.getValueSet().getId());
        assertEquals("Common Anatomic Regions Context ID 4031", response.getValueSet().getDisplayName());
        assertEquals("20061023", response.getValueSet().getVersion());

        assertEquals(1, response.getValueSet().getConceptList().size());
        var conceptList = response.getValueSet().getConceptList().get(0);
        assertEquals("en-US", conceptList.getLang());

        assertEquals(2, conceptList.getConcept().size());

        assertEquals("T-D4000", conceptList.getConcept().get(0).getCode());
        assertEquals("Abdomen", conceptList.getConcept().get(0).getDisplayName());
        assertEquals("2.16.840.1.113883.6.5", conceptList.getConcept().get(0).getCodeSystem());
        assertNull(conceptList.getConcept().get(0).getCodeSystemName());
        assertNull(conceptList.getConcept().get(0).getCodeSystemVersion());

        assertEquals("R-FAB57", conceptList.getConcept().get(1).getCode());
        assertEquals("Abdomen and Pelvis", conceptList.getConcept().get(1).getDisplayName());
        assertEquals("2.16.840.1.113883.6.5", conceptList.getConcept().get(1).getCodeSystem());
        assertNull(conceptList.getConcept().get(1).getCodeSystemName());
        assertNull(conceptList.getConcept().get(1).getCodeSystemVersion());
    }
}