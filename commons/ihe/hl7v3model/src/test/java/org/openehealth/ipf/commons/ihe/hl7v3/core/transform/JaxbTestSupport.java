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
package org.openehealth.ipf.commons.ihe.hl7v3.core.transform;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import net.ihe.gazelle.hl7v3.mcciin000002UV01.MCCIIN000002UV01Type;
import net.ihe.gazelle.hl7v3.prpain201301UV02.PRPAIN201301UV02Type;
import net.ihe.gazelle.hl7v3.prpain201302UV02.PRPAIN201302UV02Type;
import net.ihe.gazelle.hl7v3.prpain201304UV02.PRPAIN201304UV02Type;
import net.ihe.gazelle.hl7v3.prpain201305UV02.PRPAIN201305UV02Type;
import net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02Type;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * JAXB marshalling helpers for the transformer tests.
 */
public final class JaxbTestSupport {

    private static final JAXBContext JAXB_CONTEXT;

    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(
                    PRPAIN201301UV02Type.class, PRPAIN201302UV02Type.class, PRPAIN201304UV02Type.class,
                    PRPAIN201305UV02Type.class, PRPAIN201306UV02Type.class, MCCIIN000002UV01Type.class);
        } catch (final JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private JaxbTestSupport() {
    }

    public static String marshal(final Object object) throws JAXBException {
        final var marshaller = JAXB_CONTEXT.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        final var writer = new StringWriter();
        marshaller.marshal(object, writer);
        return writer.toString();
    }

    @SuppressWarnings("unchecked")
    public static <T> T unmarshal(final String xml) throws JAXBException {
        return (T) JAXB_CONTEXT.createUnmarshaller().unmarshal(new StringReader(xml));
    }

    @SuppressWarnings("unchecked")
    public static <T> T unmarshalResource(final String resource) throws JAXBException, IOException {
        try (InputStream inputStream = JaxbTestSupport.class.getClassLoader().getResourceAsStream(resource)) {
            if (inputStream == null) {
                throw new IOException("Resource not found: " + resource);
            }
            return (T) JAXB_CONTEXT.createUnmarshaller().unmarshal(inputStream);
        }
    }

    /**
     * Marshals and unmarshals again, simulating a message sent over the wire.
     */
    public static <T> T roundTrip(final T object) throws JAXBException {
        return unmarshal(marshal(object));
    }
}
