package org.openehealth.ipf.tutorials.firstapp;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "first-app.mllp-port=12575")
class PatientFeedRouteTest {

    // ITI-8 uses HL7 v2.3.1. Segments are separated by carriage returns.
    private static final String ADT_A01 = String.join("\r",
            "MSH|^~\\&|SOURCE_APP|SOURCE_FAC|PIX_MGR|PIX_FAC|20261128120000||ADT^A01|MSG0001|P|2.3.1",
            "EVN|A01|20261128120000",
            "PID|||4711^^^&1.2.3.4&ISO||Doe^John||19700101|M",
            "PV1||O");

    @Autowired
    ProducerTemplate producerTemplate;

    @Value("${first-app.mllp-port}")
    int port;

    @Test
    void acceptsPatientFeed() throws Exception {
        // The producer side of pix-iti8 is the Patient Identity Source (the client actor)
        Message ack = producerTemplate.requestBody("pix-iti8://localhost:" + port, ADT_A01, Message.class);

        Terser terser = new Terser(ack);
        assertEquals("AA", terser.get("MSA-1"));
        assertEquals("MSG0001", terser.get("MSA-2"));
    }
}
