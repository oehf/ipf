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

import net.ihe.gazelle.hl7v3.prpain201305UV02.PRPAIN201305UV02Type;
import net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02Type;
import org.apache.camel.Converter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

/**
 * Camel type converters for HL7 PRPA models for ITI-47 transaction.
 *
 * @author Quentin Ligier
 * @since 4.1
 */
@Converter(generateLoader = true)
public class Iti47Converters extends Hl7v3ConvertersBase {

    private static final JAXBContext JAXB_CONTEXT_PRPA_REQUEST;
    private static final JAXBContext JAXB_CONTEXT_PRPA_RESPONSE;
    static {
        try {
            JAXB_CONTEXT_PRPA_REQUEST = JAXBContext.newInstance(PRPAIN201305UV02Type.class);
            JAXB_CONTEXT_PRPA_RESPONSE = JAXBContext.newInstance(PRPAIN201306UV02Type.class);
        } catch (final JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }


    /* --------------------- Requests --------------------- */

    @Converter
    public static PRPAIN201305UV02Type xmlToPrpaRequest(final String xml) throws JAXBException {
        return fromXml(xml, JAXB_CONTEXT_PRPA_REQUEST);
    }

    @Converter
    public static String prpaRequestToXml(final PRPAIN201305UV02Type request) throws JAXBException {
        return toXml(request, JAXB_CONTEXT_PRPA_REQUEST);
    }


    /* --------------------- Responses --------------------- */

    @Converter
    public static PRPAIN201306UV02Type xmlToPrpaResponse(final String xml) throws JAXBException {
        return fromXml(xml, JAXB_CONTEXT_PRPA_RESPONSE);
    }

    @Converter
    public static String prpaResponseToXml(final PRPAIN201306UV02Type response) throws JAXBException {
        return toXml(response, JAXB_CONTEXT_PRPA_RESPONSE);
    }
}
