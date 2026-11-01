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
import org.apache.camel.Converter;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PdqV3QueryRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.PdqV3QueryResponse;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PdqV3QueryRequestTransformer;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.responses.PdqV3QueryResponseTransformer;

/**
 * Camel type converters for the simplified ITI-47 (PDQv3) data model.
 *
 * @author Christian Ohr
 * @since 6.1
 */
@Converter(generateLoader = true)
public class Iti47Converters {

    private static final PdqV3QueryRequestTransformer SIMPLE_REQUEST_TRANSFORMER = new PdqV3QueryRequestTransformer();
    private static final PdqV3QueryResponseTransformer SIMPLE_RESPONSE_TRANSFORMER = new PdqV3QueryResponseTransformer();

    /* --------------------- Requests --------------------- */

    @Converter
    public static String simpleRequestToXml(final PdqV3QueryRequest simpleRequest) throws JAXBException {
        return JaxbHl7v3Converters.PRPAIN201305UV02toXml(SIMPLE_REQUEST_TRANSFORMER.toPrpa(simpleRequest));
    }

    @Converter
    public static PdqV3QueryRequest xmlToSimpleRequest(final String xml) throws JAXBException {
        return SIMPLE_REQUEST_TRANSFORMER.fromPrpa(JaxbHl7v3Converters.xmlToPRPAIN201305UV02(xml));
    }

    /* --------------------- Responses --------------------- */

    @Converter
    public static String simpleResponseToXml(final PdqV3QueryResponse simpleResponse) throws JAXBException {
        return JaxbHl7v3Converters.PRPAIN201306UV02toXml(SIMPLE_RESPONSE_TRANSFORMER.toPrpa(simpleResponse));
    }

    @Converter
    public static PdqV3QueryResponse xmlToSimpleResponse(final String xml) throws JAXBException {
        return SIMPLE_RESPONSE_TRANSFORMER.fromPrpa(JaxbHl7v3Converters.xmlToPRPAIN201306UV02(xml));
    }

}
