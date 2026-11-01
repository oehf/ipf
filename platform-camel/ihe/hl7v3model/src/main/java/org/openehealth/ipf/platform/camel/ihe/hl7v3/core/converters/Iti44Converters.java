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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.core.converters;

import jakarta.xml.bind.JAXBException;
import net.ihe.gazelle.hl7v3.prpain201301UV02.PRPAIN201301UV02Type;
import net.ihe.gazelle.hl7v3.prpain201302UV02.PRPAIN201302UV02Type;
import net.ihe.gazelle.hl7v3.prpain201304UV02.PRPAIN201304UV02Type;
import org.apache.camel.Converter;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientDuplicatesResolvedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordAddedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PatientRecordRevisedRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.AcceptAcknowledgement;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PatientDuplicatesResolvedTransformer;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PatientRecordAddedTransformer;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PatientRecordRevisedTransformer;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.responses.AcceptAcknowledgementTransformer;

/**
 * Camel type converters for the simplified ITI-44 (Patient Identity Feed HL7v3) data model.
 * <p>
 * ITI-46 (PIX Update Notification HL7v3) uses a subset of the same messages (record revised and
 * acknowledgement), so these converters cover ITI-46 as well.
 *
 * @author Christian Ohr
 * @since 6.1
 */
@Converter(generateLoader = true)
public class Iti44Converters {

    private static final PatientRecordAddedTransformer ADDED_TRANSFORMER = new PatientRecordAddedTransformer();
    private static final PatientRecordRevisedTransformer REVISED_TRANSFORMER = new PatientRecordRevisedTransformer();
    private static final PatientDuplicatesResolvedTransformer DUPLICATES_RESOLVED_TRANSFORMER = new PatientDuplicatesResolvedTransformer();
    private static final AcceptAcknowledgementTransformer ACKNOWLEDGEMENT_TRANSFORMER = new AcceptAcknowledgementTransformer();

    /* --------------------- Requests --------------------- */

    @Converter
    public static String recordAddedToXml(final PatientRecordAddedRequest request) throws JAXBException {
        return JaxbHl7v3Converters.PRPAIN201301UV02toXml(ADDED_TRANSFORMER.toPrpa(request));
    }

    @Converter
    public static PatientRecordAddedRequest xmlToRecordAdded(final String xml) throws JAXBException {
        return ADDED_TRANSFORMER.fromPrpa(JaxbHl7v3Converters.xmlToPRPAIN201301UV02(xml));
    }

    @Converter
    public static String recordRevisedToXml(final PatientRecordRevisedRequest request) throws JAXBException {
        return JaxbHl7v3Converters.PRPAIN201302UV02toXml(REVISED_TRANSFORMER.toPrpa(request));
    }

    @Converter
    public static PatientRecordRevisedRequest xmlToRecordRevised(final String xml) throws JAXBException {
        return REVISED_TRANSFORMER.fromPrpa(JaxbHl7v3Converters.xmlToPRPAIN201302UV02(xml));
    }

    @Converter
    public static String duplicatesResolvedToXml(final PatientDuplicatesResolvedRequest request) throws JAXBException {
        return JaxbHl7v3Converters.PRPAIN201304UV02toXml(DUPLICATES_RESOLVED_TRANSFORMER.toPrpa(request));
    }

    @Converter
    public static PatientDuplicatesResolvedRequest xmlToDuplicatesResolved(final String xml) throws JAXBException {
        return DUPLICATES_RESOLVED_TRANSFORMER.fromPrpa(JaxbHl7v3Converters.xmlToPRPAIN201304UV02(xml));
    }

    /**
     * Converts any of the ITI-44 request messages into the respective simplified model, so that a route can use
     * {@code convertBodyTo(PatientRecordRequest.class)} regardless of which message type was received.
     */
    @Converter
    public static PatientRecordRequest xmlToPatientRecord(final String xml) throws JAXBException {
        final var message = JaxbHl7v3Converters.xmlToMessage(xml);
        if (message instanceof PRPAIN201301UV02Type recordAdded) {
            return ADDED_TRANSFORMER.fromPrpa(recordAdded);
        }
        if (message instanceof PRPAIN201302UV02Type recordRevised) {
            return REVISED_TRANSFORMER.fromPrpa(recordRevised);
        }
        if (message instanceof PRPAIN201304UV02Type duplicatesResolved) {
            return DUPLICATES_RESOLVED_TRANSFORMER.fromPrpa(duplicatesResolved);
        }
        throw new IllegalArgumentException("Not a patient registry message: " + message.getClass().getSimpleName());
    }

    /* --------------------- Responses --------------------- */

    @Converter
    public static String acknowledgementToXml(final AcceptAcknowledgement acknowledgement) throws JAXBException {
        return JaxbHl7v3Converters.MCCIIN000002UV01toXml(ACKNOWLEDGEMENT_TRANSFORMER.toMcci(acknowledgement));
    }

    @Converter
    public static AcceptAcknowledgement xmlToAcknowledgement(final String xml) throws JAXBException {
        return ACKNOWLEDGEMENT_TRANSFORMER.fromMcci(JaxbHl7v3Converters.xmlToMCCIIN000002UV01(xml));
    }

}
