package org.openehealth.ipf.platform.camel.ihe.svs.core.converters;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.svs.core.requests.RetrieveValueSetRequest;
import org.openehealth.ipf.commons.ihe.svs.core.requests.ValueSetRequest;
import org.openehealth.ipf.commons.ihe.svs.core.responses.Concept;
import org.openehealth.ipf.commons.ihe.svs.core.responses.ConceptList;
import org.openehealth.ipf.commons.ihe.svs.core.responses.RetrieveValueSetResponse;
import org.openehealth.ipf.commons.ihe.svs.core.responses.ValueSetResponse;

import javax.xml.bind.JAXBException;

import java.time.OffsetDateTime;

import static org.junit.Assert.*;

public class SvsConvertersTest {

    @Test
    public void testRequest() throws JAXBException {
        var expectedRequest = new RetrieveValueSetRequest(new ValueSetRequest("1.2.3", "en", "3"));
        var xml = SvsConverters.svsQueryToXml(expectedRequest);
        var request = SvsConverters.xmlToSvsQuery(xml);

        assertNotNull(request);
        assertEquals(expectedRequest.getValueSet().getId(), request.getValueSet().getId());
        assertEquals(expectedRequest.getValueSet().getLang(), request.getValueSet().getLang());
        assertEquals(expectedRequest.getValueSet().getVersion(), request.getValueSet().getVersion());
    }

    @Test
    public void testResponse() throws JAXBException {
        var valueSet = new ValueSetResponse("1.2.3", "Value Set", "3");
        var conceptList = new ConceptList("en");
        conceptList.getConcept().add(new Concept("10.1", "1.2.3", "Code", "3"));
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
        assertEquals(expectedResponse.getValueSet().getConceptList().get(0).getConcept().get(0).getCodeSystemVersion(),
                response.getValueSet().getConceptList().get(0).getConcept().get(0).getCodeSystemVersion());
    }
}