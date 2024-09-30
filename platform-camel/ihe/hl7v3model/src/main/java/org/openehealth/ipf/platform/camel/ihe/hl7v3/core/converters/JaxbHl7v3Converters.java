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

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import net.ihe.gazelle.hl7v3.mcciin000002UV01.MCCIIN000002UV01Type;
import net.ihe.gazelle.hl7v3.prpain201301UV02.PRPAIN201301UV02Type;
import net.ihe.gazelle.hl7v3.prpain201302UV02.PRPAIN201302UV02Type;
import net.ihe.gazelle.hl7v3.prpain201304UV02.PRPAIN201304UV02Type;
import net.ihe.gazelle.hl7v3.prpain201305UV02.PRPAIN201305UV02Type;
import net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02Type;
import net.ihe.gazelle.hl7v3.prpain201309UV02.PRPAIN201309UV02Type;
import net.ihe.gazelle.hl7v3.prpain201310UV02.PRPAIN201310UV02Type;
import net.ihe.gazelle.hl7v3.quqiin000003UV01.QUQIIN000003UV01CancelType;
import net.ihe.gazelle.hl7v3.quqiin000003UV01.QUQIIN000003UV01Type;
import org.apache.camel.Converter;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * Camel type converters between XML and Gazelle JAXB model classes.
 *
 * @author Quentin Ligier
 * @since 4.1
 */
public class JaxbHl7v3Converters {

    private static final JAXBContext JAXB_CONTEXT;

    static {
        try {
            JAXB_CONTEXT = JAXBContext.newInstance(
                PRPAIN201301UV02Type.class,
                PRPAIN201302UV02Type.class,
                PRPAIN201304UV02Type.class,
                PRPAIN201305UV02Type.class,
                PRPAIN201306UV02Type.class,
                PRPAIN201309UV02Type.class,
                PRPAIN201310UV02Type.class,
                MCCIIN000002UV01Type.class,
                QUQIIN000003UV01Type.class,
                QUQIIN000003UV01CancelType.class);
        } catch (final JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Converter
    public static PRPAIN201301UV02Type xmlToPRPAIN201301UV02(final String xml) throws JAXBException {
        return fromXml(xml);
    }

    @Converter
    public static String PRPAIN201301UV02toXml(final PRPAIN201301UV02Type request) throws JAXBException {
        return toXml(request);
    }

    @Converter
    public static PRPAIN201302UV02Type xmlToPRPAIN201302UV02(final String xml) throws JAXBException {
        return fromXml(xml);
    }

    @Converter
    public static String PRPAIN201302UV02toXml(final PRPAIN201302UV02Type request) throws JAXBException {
        return toXml(request);
    }

    @Converter
    public static PRPAIN201304UV02Type xmlToPRPAIN201304UV02(final String xml) throws JAXBException {
        return fromXml(xml);
    }

    @Converter
    public static String PRPAIN201304UV02toXml(final PRPAIN201304UV02Type request) throws JAXBException {
        return toXml(request);
    }

    @Converter
    public static PRPAIN201305UV02Type xmlToPRPAIN201305UV02(final String xml) throws JAXBException {
        return fromXml(xml);
    }

    @Converter
    public static String PRPAIN201305UV02toXml(final PRPAIN201305UV02Type request) throws JAXBException {
        return toXml(request);
    }

    @Converter
    public static PRPAIN201306UV02Type xmlToPRPAIN201306UV02(final String xml) throws JAXBException {
        return fromXml(xml);
    }

    @Converter
    public static String PRPAIN201306UV02toXml(final PRPAIN201306UV02Type request) throws JAXBException {
        return toXml(request);
    }

    @Converter
    public static PRPAIN201309UV02Type xmlToPRPAIN201309UV02(final String xml) throws JAXBException {
        return fromXml(xml);
    }

    @Converter
    public static String PRPAIN201309UV02toXml(final PRPAIN201309UV02Type request) throws JAXBException {
        return toXml(request);
    }

    @Converter
    public static PRPAIN201310UV02Type xmlToPRPAIN201310UV02(final String xml) throws JAXBException {
        return fromXml(xml);
    }

    @Converter
    public static String PRPAIN201310UV02toXml(final PRPAIN201310UV02Type request) throws JAXBException {
        return toXml(request);
    }

    @Converter
    public static MCCIIN000002UV01Type xmlToMCCIIN000002UV01(final String xml) throws JAXBException {
        return fromXml(xml);
    }

    @Converter
    public static String MCCIIN000002UV01toXml(final MCCIIN000002UV01Type request) throws JAXBException {
        return toXml(request);
    }

    @Converter
    public static QUQIIN000003UV01Type xmlToQUQIIN000003UV01(final String xml) throws JAXBException {
        return fromXml(xml);
    }

    @Converter
    public static String QUQIIN000003UV01toXml(final QUQIIN000003UV01Type request) throws JAXBException {
        return toXml(request);
    }

    @Converter
    public static QUQIIN000003UV01CancelType xmlToQUQIIN000003UV01Cancel(final String xml) throws JAXBException {
        return fromXml(xml);
    }

    @Converter
    public static String QUQIIN000003UV01CancelToXml(final QUQIIN000003UV01CancelType request) throws JAXBException {
        return toXml(request);
    }

    private static String toXml(Object pojo) throws JAXBException {
        final var marshaller = JAXB_CONTEXT.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
        final var stringWriter = new StringWriter();
        marshaller.marshal(pojo, stringWriter);
        return stringWriter.toString();
    }

    private static <T> T fromXml(String xml) throws JAXBException {
        final var unmarshaller = JAXB_CONTEXT.createUnmarshaller();
        return (T) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    }

}
