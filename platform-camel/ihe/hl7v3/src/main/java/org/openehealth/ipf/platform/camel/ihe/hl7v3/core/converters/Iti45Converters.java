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

import net.ihe.gazelle.hl7v3.prpain201309UV02.PRPAIN201309UV02Type;
import net.ihe.gazelle.hl7v3.prpain201310UV02.PRPAIN201310UV02Type;
import org.apache.camel.Converter;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PixV3QueryQuery;
import org.openehealth.ipf.commons.ihe.hl7v3.core.responses.PixV3QueryResponse;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.reponses.PixV3QueryResponseTransformer;
import org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests.PixV3QueryQueryTransformer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * Camel type converters for HL7 PRPA models in ITI-45 transaction.
 *
 * @author Quentin Ligier
 */
@Converter(generateLoader = true)
public class Iti45Converters {

    private static final PixV3QueryQueryTransformer SIMPLE_QUERY_TRANSFORMER = new PixV3QueryQueryTransformer();
    private static final PixV3QueryResponseTransformer SIMPLE_RESPONSE_TRANSFORMER = new PixV3QueryResponseTransformer();
    private static final JAXBContext JAXB_CONTEXT_PRPA_QUERY;
    private static final JAXBContext JAXB_CONTEXT_PRPA_RESPONSE;
    static {
        try {
            JAXB_CONTEXT_PRPA_QUERY = JAXBContext.newInstance(PRPAIN201309UV02Type.class);
            JAXB_CONTEXT_PRPA_RESPONSE = JAXBContext.newInstance(PRPAIN201310UV02Type.class);
        } catch (final JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Converter
    public static PRPAIN201309UV02Type xmlToPrpaQuery(final String xml) throws JAXBException {
        final Unmarshaller unmarshaller = JAXB_CONTEXT_PRPA_QUERY.createUnmarshaller();
        return (PRPAIN201309UV02Type) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    @Converter
    public static String prpaQueryToXml(final PRPAIN201309UV02Type query) throws JAXBException {
        final Marshaller marshaller = JAXB_CONTEXT_PRPA_QUERY.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
        final StringWriter stringWriter = new StringWriter();
        marshaller.marshal(query, stringWriter);
        return stringWriter.toString();
    }

    @Converter
    public static String xmlToSimpleQuery(final PixV3QueryQuery simpleQuery) throws JAXBException {
        return prpaQueryToXml(SIMPLE_QUERY_TRANSFORMER.toPrpa(simpleQuery));
    }

    @Converter
    public static PixV3QueryQuery simpleQueryToXml(final String xml) throws JAXBException {
        return SIMPLE_QUERY_TRANSFORMER.fromPrpa(xmlToPrpaQuery(xml));
    }

    @Converter
    public static PRPAIN201310UV02Type xmlToPrpaResponse(final String xml) throws JAXBException {
        final Unmarshaller unmarshaller = JAXB_CONTEXT_PRPA_RESPONSE.createUnmarshaller();
        return (PRPAIN201310UV02Type) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    @Converter
    public static String prpaResponseToXml(final PRPAIN201310UV02Type response) throws JAXBException {
        final Marshaller marshaller = JAXB_CONTEXT_PRPA_RESPONSE.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
        final StringWriter stringWriter = new StringWriter();
        marshaller.marshal(response, stringWriter);
        return stringWriter.toString();
    }

    @Converter
    public static String xmlToSimpleResponse(final PixV3QueryResponse simpleResponse) throws JAXBException {
        return prpaResponseToXml(SIMPLE_RESPONSE_TRANSFORMER.toPrpa(simpleResponse));
    }

    @Converter
    public static PixV3QueryResponse simpleResponseToXml(final String xml) throws JAXBException {
        return SIMPLE_RESPONSE_TRANSFORMER.fromPrpa(xmlToPrpaResponse(xml));
    }
}
