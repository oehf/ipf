/*
 * Copyright 2021 the original author or authors.
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

import org.apache.camel.Converter;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PixV3QueryRequest;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.PixV3QueryResponse;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryRequestTransformer;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.responses.PixV3QueryResponseTransformer;

import jakarta.xml.bind.JAXBException;

/**
 * Camel type converters for simplified ITI-45 data model.
 *
 * @author Quentin Ligier
 * @since 4.1
 */
@Converter(generateLoader = true)
public class Iti45Converters {

    private static final PixV3QueryRequestTransformer SIMPLE_REQUEST_TRANSFORMER = new PixV3QueryRequestTransformer();
    private static final PixV3QueryResponseTransformer SIMPLE_RESPONSE_TRANSFORMER = new PixV3QueryResponseTransformer();

    /* --------------------- Requests --------------------- */

    @Converter
    public static String simpleRequestToXml(final PixV3QueryRequest simpleRequest) throws JAXBException {
        return JaxbHl7v3Converters.PRPAIN201309UV02toXml(SIMPLE_REQUEST_TRANSFORMER.toPrpa(simpleRequest));
    }

    @Converter
    public static PixV3QueryRequest xmlToSimpleRequest(final String xml) throws JAXBException {
        return SIMPLE_REQUEST_TRANSFORMER.fromPrpa(JaxbHl7v3Converters.xmlToPRPAIN201309UV02(xml));
    }


    /* --------------------- Responses --------------------- */

    @Converter
    public static String simpleResponseToXml(final PixV3QueryResponse simpleResponse) throws JAXBException {
        return JaxbHl7v3Converters.PRPAIN201310UV02toXml(SIMPLE_RESPONSE_TRANSFORMER.toPrpa(simpleResponse));
    }

    @Converter
    public static PixV3QueryResponse xmlToSimpleResponse(final String xml) throws JAXBException {
        return SIMPLE_RESPONSE_TRANSFORMER.fromPrpa(JaxbHl7v3Converters.xmlToPRPAIN201310UV02(xml));
    }

}
