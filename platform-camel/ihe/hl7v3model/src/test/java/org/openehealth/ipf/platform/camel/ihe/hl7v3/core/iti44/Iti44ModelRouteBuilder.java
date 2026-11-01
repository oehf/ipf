/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.core.iti44;

import net.ihe.gazelle.hl7v3.datatypes.CE;
import net.ihe.gazelle.hl7v3.datatypes.ED;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.mccimt000200UV01.MCCIMT000200UV01AcknowledgementDetail;
import net.ihe.gazelle.hl7v3.voc.AcknowledgementDetailType;
import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.hl7v3.core.Hl7v3MessageUtils;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.Patient;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientDuplicatesResolvedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordAddedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordRevisedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.AcceptAcknowledgement;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti44RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti44ResponseValidator;

/**
 * Patient Identity Cross-reference Manager route for ITI-44 working on the simplified model.
 * <p>
 * The HL7v3 web service consumer delivers the request as XML string and expects an XML string as response.
 * The route validates both against the ITI-44 XML schema and schematron rules. In between, it converts the request
 * into a {@link PatientRecordRequest}, which yields the subclass matching the received message (record added,
 * record revised or duplicates resolved), and answers with an {@link AcceptAcknowledgement}.
 * <p>
 * The patients are kept in a simple in-memory registry, so that tests can check what the server received.
 */
public class Iti44ModelRouteBuilder extends RouteBuilder {

    /**
     * HL7 table 0357 (message error condition codes)
     */
    private static final String ERROR_CODE_SYSTEM = "2.16.840.1.113883.12.357";

    private final Map<String, Patient> registry = new ConcurrentHashMap<>();

    @Override
    public void configure() throws Exception {
        from("pixv3-iti44:pixv3-iti44-model-service")
                .process(iti44RequestValidator())
                .convertBodyTo(PatientRecordRequest.class)
                .process(exchange -> {
                    final var request = exchange.getMessage().getMandatoryBody(PatientRecordRequest.class);
                    exchange.getMessage().setBody(register(request));
                })
                .convertBodyTo(String.class)
                .process(iti44ResponseValidator());
    }

    private AcceptAcknowledgement register(final PatientRecordRequest request) {
        final var patient = request.getPatient();
        final var ack = Hl7v3MessageUtils.ack(request);
        if (request instanceof PatientRecordAddedRequest) {
            put(patient);
        } else if (request instanceof PatientRecordRevisedRequest) {
            if (patient.getPatientIds().stream().map(Iti44ModelRouteBuilder::key).noneMatch(registry::containsKey)) {
                ack.setApplicationError();
                ack.getAcknowledgementDetails().add(unknownPatient(patient.getPatientIds()));
                return ack;
            }
            put(patient);
        } else if (request instanceof PatientDuplicatesResolvedRequest duplicatesResolved) {
            put(patient);
            duplicatesResolved.getObsoletePatientIds().forEach(id -> registry.remove(key(id)));
        } else {
            throw new IllegalArgumentException("Unexpected request " + request.getClass());
        }
        return ack;
    }

    private void put(final Patient patient) {
        patient.getPatientIds().forEach(id -> registry.put(key(id), patient));
    }

    private static MCCIMT000200UV01AcknowledgementDetail unknownPatient(final List<II> ids) {
        final var detail = new MCCIMT000200UV01AcknowledgementDetail();
        detail.setTypeCode(AcknowledgementDetailType.E);
        detail.setCode(new CE("204", "Unknown Key Identifier", ERROR_CODE_SYSTEM));
        final var text = new ED();
        text.mixed = List.of("Unknown patient " + key(ids.get(0)));
        detail.setText(text);
        return detail;
    }

    /**
     * @return the registered patient with the given ID, or {@code null}
     */
    public Patient getPatient(final II id) {
        return registry.get(key(id));
    }

    public void clear() {
        registry.clear();
    }

    private static String key(final II id) {
        return id.getRoot() + "^" + id.getExtension();
    }
}
