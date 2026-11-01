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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.core.iti47;

import net.ihe.gazelle.hl7v3.coctmt150003UV03.COCTMT150003UV03ContactParty;
import net.ihe.gazelle.hl7v3.coctmt150003UV03.COCTMT150003UV03Organization;
import net.ihe.gazelle.hl7v3.datatypes.II;
import net.ihe.gazelle.hl7v3.voc.RoleClassContact;
import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.hl7v3.core.Hl7v3MessageUtils;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.MatchedPatient;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PdqV3QueryRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.PdqV3QueryResponse;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.Hl7v3TransformerUtils;

import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti47RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.hl7v3.PixPdqV3CamelValidators.iti47ResponseValidator;

/**
 * PDQv3 Supplier route working on the simplified model.
 * <p>
 * The HL7v3 web service consumer delivers the request as XML string and expects an XML string as response.
 * The route validates both against the ITI-47 XML schema and schematron rules, and uses the type converters of this
 * module to work with {@link PdqV3QueryRequest} and {@link PdqV3QueryResponse} in between.
 */
public class Iti47ModelRouteBuilder extends RouteBuilder {

    public static final String PROVIDER_ORGANIZATION_OID = "1.2.840.114350.1.13.99998.8734";

    @Override
    public void configure() throws Exception {
        from("pdqv3-iti47:pdqv3-iti47-model-service")
                .process(iti47RequestValidator())
                .convertBodyTo(PdqV3QueryRequest.class)
                .process(exchange -> {
                    final var request = exchange.getMessage().getMandatoryBody(PdqV3QueryRequest.class);
                    exchange.getMessage().setBody(lookup(request));
                })
                .convertBodyTo(String.class)
                .process(iti47ResponseValidator());
    }

    /**
     * Pretends to look up the patient: every queried patient ID is found, and the patient's demographics are taken
     * from the query parameters.
     */
    private static PdqV3QueryResponse lookup(final PdqV3QueryRequest request) {
        final var response = Hl7v3MessageUtils.respond(request);
        if (request.getLivingSubjectIds().isEmpty()) {
            response.setNoDataFound();
            return response;
        }
        for (final var id : request.getLivingSubjectIds()) {
            final var patient = new MatchedPatient();
            patient.getPatientIds().add(id);
            request.getLivingSubjectNames().forEach(name -> patient.getNames().add(Hl7v3TransformerUtils.toPn(name)));
            patient.setAdministrativeGenderCode(request.getLivingSubjectAdministrativeGenderCode());
            patient.setBirthTime(request.getLivingSubjectBirthTime());
            patient.setProviderOrganization(providerOrganization());
            patient.setCustodianOid(PROVIDER_ORGANIZATION_OID);
            patient.setMatchDegree(100);
            response.getPatients().add(patient);
        }
        response.setDataFound();
        return response;
    }

    private static COCTMT150003UV03Organization providerOrganization() {
        final var organization = new COCTMT150003UV03Organization();
        organization.getId().add(new II(PROVIDER_ORGANIZATION_OID, null));
        final var contactParty = new COCTMT150003UV03ContactParty();
        contactParty.setClassCode(RoleClassContact.CON);
        organization.getContactParty().add(contactParty);
        return organization;
    }
}
