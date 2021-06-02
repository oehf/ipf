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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * Camel type converters for HL7 PRPA models for ITI-47 transaction.
 *
 * @author Quentin Ligier
 */
@Converter(generateLoader = true)
public class Iti47Converters {

    private static final JAXBContext JAXB_CONTEXT_PRPA_QUERY;
    private static final JAXBContext JAXB_CONTEXT_PRPA_RESPONSE;
    static {
        try {
            JAXB_CONTEXT_PRPA_QUERY = JAXBContext.newInstance(PRPAIN201305UV02Type.class);
            JAXB_CONTEXT_PRPA_RESPONSE = JAXBContext.newInstance(PRPAIN201306UV02Type.class);
        } catch (final JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Converter
    public static PRPAIN201305UV02Type xmlToPrpaQuery(final String xml) throws JAXBException {
        final Unmarshaller unmarshaller = JAXB_CONTEXT_PRPA_QUERY.createUnmarshaller();
        return (PRPAIN201305UV02Type) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    @Converter
    public static String prpaQueryToXml(final PRPAIN201305UV02Type query) throws JAXBException {
        final Marshaller marshaller = JAXB_CONTEXT_PRPA_QUERY.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
        final StringWriter stringWriter = new StringWriter();
        marshaller.marshal(query, stringWriter);
        return stringWriter.toString();
    }

    @Converter
    public static PRPAIN201306UV02Type xmlToPrpaResponse(final String xml) throws JAXBException {
        final Unmarshaller unmarshaller = JAXB_CONTEXT_PRPA_RESPONSE.createUnmarshaller();
        return (PRPAIN201306UV02Type) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    @Converter
    public static String prpaResponseToXml(final PRPAIN201306UV02Type response) throws JAXBException {
        final Marshaller marshaller = JAXB_CONTEXT_PRPA_RESPONSE.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
        final StringWriter stringWriter = new StringWriter();
        marshaller.marshal(response, stringWriter);
        return stringWriter.toString();
    }
}
