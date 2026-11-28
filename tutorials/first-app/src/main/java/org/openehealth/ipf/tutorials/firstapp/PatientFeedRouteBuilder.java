package org.openehealth.ipf.tutorials.firstapp;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.openehealth.ipf.platform.camel.hl7.HL7v2.ack;
import static org.openehealth.ipf.platform.camel.hl7.HL7v2.get;

/**
 * Server side (Patient Identifier Cross-reference Manager) of the IHE PIX Patient Identity Feed (ITI-8).
 */
@Component
public class PatientFeedRouteBuilder extends RouteBuilder {

    @Override
    public void configure() {
        from("pix-iti8://0.0.0.0:{{first-app.mllp-port}}")
                .routeId("patient-feed")
                .setHeader("event", get("MSH-9-2"))
                .setHeader("patientId", get("PID-3-1"))
                .setHeader("patientName", get("PID-5-1"))
                .log("Received ${header.event} for patient ${header.patientId} (${header.patientName})")
                .transform(ack());
    }
}
