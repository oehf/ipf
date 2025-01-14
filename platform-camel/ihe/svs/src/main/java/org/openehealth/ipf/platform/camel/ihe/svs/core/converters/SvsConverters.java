/*
 * Copyright 2025 the original author or authors.
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

package org.openehealth.ipf.platform.camel.ihe.svs.core.converters;

import org.apache.camel.Converter;
import org.openehealth.ipf.commons.ihe.svs.core.requests.RetrieveValueSetRequest;
import org.openehealth.ipf.commons.ihe.svs.core.responses.RetrieveValueSetResponse;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * Camel type converters for SVS models.
 *
 * @author Quentin Ligier
 */
@Converter(generateLoader = true)
public class SvsConverters {

    private static final JAXBContext JAXB_CONTEXT_SVS_REQUEST;
    private static final JAXBContext JAXB_CONTEXT_SVS_RESPONSE;
    static {
        try {
            JAXB_CONTEXT_SVS_REQUEST = JAXBContext.newInstance(RetrieveValueSetRequest.class);
            JAXB_CONTEXT_SVS_RESPONSE = JAXBContext.newInstance(RetrieveValueSetResponse.class);
        } catch (final JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Converter
    public static RetrieveValueSetRequest xmlToSvsQuery(final String xml) throws JAXBException {
        var unmarshaller = JAXB_CONTEXT_SVS_REQUEST.createUnmarshaller();
        return (RetrieveValueSetRequest) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    @Converter
    public static String svsQueryToXml(final RetrieveValueSetRequest query) throws JAXBException {
        var marshaller = JAXB_CONTEXT_SVS_REQUEST.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
        var stringWriter = new StringWriter();
        marshaller.marshal(query, stringWriter);
        return stringWriter.toString();
    }

    @Converter
    public static InputStream svsQueryToInputStream(final RetrieveValueSetRequest query) throws JAXBException {
        return new ByteArrayInputStream(svsQueryToXml(query).getBytes(StandardCharsets.UTF_8));
    }

    @Converter
    public static RetrieveValueSetResponse xmlToSvsResponse(final String xml) throws JAXBException {
        var unmarshaller = JAXB_CONTEXT_SVS_RESPONSE.createUnmarshaller();
        return (RetrieveValueSetResponse) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

    @Converter
    public static String svsResponseToXml(final RetrieveValueSetResponse response) throws JAXBException {
        var marshaller = JAXB_CONTEXT_SVS_RESPONSE.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
        var stringWriter = new StringWriter();
        marshaller.marshal(response, stringWriter);
        return stringWriter.toString();
    }
}