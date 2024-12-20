/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hl7v3.core.transform.responses;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import net.ihe.gazelle.hl7v3.coctmt150003UV03.COCTMT150003UV03Organization;
import net.ihe.gazelle.hl7v3.datatypes.CE;
import net.ihe.gazelle.hl7v3.datatypes.ED;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.datatypes.ON;
import net.ihe.gazelle.hl7v3.datatypes.PN;
import net.ihe.gazelle.hl7v3.datatypes.ST;
import net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01AcknowledgementDetail;
import net.ihe.gazelle.hl7v3.prpain201310UV02.PRPAIN201310UV02Type;
import net.ihe.gazelle.hl7v3.prpamt201304UV02.PRPAMT201304UV02OtherIDs;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.core.HL7DTM;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.PixV3QueryResponse;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformerTest;

public class PixV3QueryResponseTransformerTest {

    @Test
    public void testTransform() throws Exception {
        final var transformer = new PixV3QueryResponseTransformer();
        final var jaxbContext = JAXBContext.newInstance(PRPAIN201310UV02Type.class);
        final var response = getSampleResponse();

        // Transform simple response to JAXB model
        final var prpain201310UV02Type = transformer.toPrpa(response);

        // Marshall JAXB model
        final var marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
        final var stringWriter = new StringWriter();
        marshaller.marshal(prpain201310UV02Type, stringWriter);
        final var xml = stringWriter.toString();

        // Unmarshall JAXB model
        final var unmarshaller = jaxbContext.createUnmarshaller();
        final var parsedPrpain201310UV02Type =
                (PRPAIN201310UV02Type) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        // Transform JAXB model to simple response
        final var parsedResponse = transformer.fromPrpa(parsedPrpain201310UV02Type);

        // The two simple responses should be equal
        PixV3QueryRequestTransformerTest.assertIiEquals(response.getQueryPatientId(), parsedResponse.getQueryPatientId());
        assertEquals(response.getDataSourceOids(), parsedResponse.getDataSourceOids());
        PixV3QueryRequestTransformerTest.assertIiEquals(response.getMessageId(), parsedResponse.getMessageId());
        PixV3QueryRequestTransformerTest.assertIiEquals(response.getQueryId(), parsedResponse.getQueryId());
        PixV3QueryRequestTransformerTest.assertDeviceEquals(response.getReceiver(), parsedResponse.getReceiver());
        PixV3QueryRequestTransformerTest.assertDeviceEquals(response.getSender(), parsedResponse.getSender());
        assertEquals(HL7DTM.toSimpleString(response.getCreationTime()), HL7DTM.toSimpleString(parsedResponse.getCreationTime()));
        PixV3QueryRequestTransformerTest.assertIiEquals(response.getTargetMessageId(), parsedResponse.getTargetMessageId());
        assertEquals(response.getAcknowledgementTypeCode(), parsedResponse.getAcknowledgementTypeCode());
        assertEquals(response.getQueryResponseCode(), parsedResponse.getQueryResponseCode());
        PixV3QueryRequestTransformerTest.assertIiEquals(response.getPatientIds().get(0), parsedResponse.getPatientIds().get(0));
        assertEquals(response.getCustodianOid(), parsedResponse.getCustodianOid());
        assertEquals(response.getAcknowledgementDetails().get(0).getCode().getCode(),
                parsedResponse.getAcknowledgementDetails().get(0).getCode().getCode());
        assertEquals(response.getAcknowledgementDetails().get(0).getLocation().get(0).mixed,
                parsedResponse.getAcknowledgementDetails().get(0).getLocation().get(0).mixed);
        assertEquals(response.getAcknowledgementDetails().get(0).getText().mixed,
                parsedResponse.getAcknowledgementDetails().get(0).getText().mixed);
        assertEquals(response.getProviderOrganization().getName().get(0).mixed,
                parsedResponse.getProviderOrganization().getName().get(0).mixed);
        assertEquals(response.getPersonName().mixed, parsedResponse.getPersonName().mixed);
        PixV3QueryRequestTransformerTest.assertIiEquals(response.getPersonIds().get(0), parsedResponse.getPersonIds().get(0));
        PixV3QueryRequestTransformerTest.assertIiEquals(response.getAsOtherIDs().get(0).getId().get(0),
                parsedResponse.getAsOtherIDs().get(0).getId().get(0));
    }

    public static PixV3QueryResponse getSampleResponse() {
        final var response = PixV3QueryResponse.fromQuery(PixV3QueryRequestTransformerTest.getSampleQuery());
        response.setDataFound();
        response.setTargetMessageId(new II("m1", "1.3.5"));
        response.setMessageId(new II("m2", "1.3.5"));
        response.getPatientIds().add(new II("9810", "2.16.756.5.30.1.127"));
        response.setCustodianOid("1.4.2");
        response.setProviderOrganization(new COCTMT150003UV03Organization());
        response.getProviderOrganization().getName().add(new ON());
        response.getProviderOrganization().getName().get(0).mixed = List.of("Provider Organization");
        response.setPersonName(new PN());
        response.getPersonName().mixed = List.of("Person Name");
        response.getPersonIds().add(new II("76133761", "1.3.6.1.4.1.12559.11.25.1.19"));
        response.getAsOtherIDs().add(new PRPAMT201304UV02OtherIDs());
        response.getAsOtherIDs().get(0).setId(List.of(new II("1.2.840.114350.1.13", "38273N237")));

        final var ad = new MCCIMT000300UV01AcknowledgementDetail();
        ad.setCode(new CE("204", null, null));
        ad.getLocation().add(new ST());
        ad.getLocation().get(0).mixed = List.of("/hl7:PRPA_IN201309UV02/hl7:controlActProcess");
        ad.setText(new ED());
        ad.getText().mixed = List.of("Requested record not found");
        response.getAcknowledgementDetails().add(ad);
        return response;
    }
}