package org.openehealth.ipf.commons.ihe.hl7v3.core.transform.requests;

import net.ihe.gazelle.hl7v3.datatypes.*;
import net.ihe.gazelle.hl7v3.prpain201309UV02.PRPAIN201309UV02Type;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.core.HL7DTM;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.Device;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PixV3QueryQuery;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PixV3QueryQueryTransformerTest {

    @Test
    public void testTransform() throws Exception {
        final PixV3QueryQueryTransformer transformer = new PixV3QueryQueryTransformer();
        final JAXBContext jaxbContext = JAXBContext.newInstance(PRPAIN201309UV02Type.class);
        final PixV3QueryQuery query = getSampleQuery();

        // Transform simple query to JAXB model
        final PRPAIN201309UV02Type prpain201309UV02Type = transformer.toPrpa(query);

        // Marshall JAXB model
        final Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF8");
        final StringWriter stringWriter = new StringWriter();
        marshaller.marshal(prpain201309UV02Type, stringWriter);
        final String xml = stringWriter.toString();

        // Unmarshall JAXB model
        final Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        final PRPAIN201309UV02Type parsedPrpain201309UV02Type =
                (PRPAIN201309UV02Type) unmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        // Transform JAXB model to simple query
        final PixV3QueryQuery parsedQuery = transformer.fromPrpa(parsedPrpain201309UV02Type);

        // The two simple queries should be equal
        assertIiEquals(query.getQueryPatientId(), parsedQuery.getQueryPatientId());
        assertEquals(query.getDataSourceOids(), parsedQuery.getDataSourceOids());
        assertIiEquals(query.getMessageId(), parsedQuery.getMessageId());
        assertIiEquals(query.getQueryId(), parsedQuery.getQueryId());
        assertDeviceEquals(query.getReceiver(), parsedQuery.getReceiver());
        assertDeviceEquals(query.getSender(), parsedQuery.getSender());
        assertEquals(HL7DTM.toSimpleString(query.getCreationTime()), HL7DTM.toSimpleString(parsedQuery.getCreationTime()));
    }

    public static void assertDeviceEquals(final Device expectedDevice, final Device actualDevice) {
        assertEquals(expectedDevice.getTelecom().getValue(), actualDevice.getTelecom().getValue());
        assertEquals(expectedDevice.getDesc().mixed, actualDevice.getDesc().mixed);
        assertEquals(expectedDevice.getSoftwareName().mixed, actualDevice.getSoftwareName().mixed);
        assertEquals(expectedDevice.getManufacturerModelName().mixed, actualDevice.getManufacturerModelName().mixed);
        assertEquals(expectedDevice.getNames().get(0).mixed, actualDevice.getNames().get(0).mixed);
        assertEquals(expectedDevice.getIds().get(0), actualDevice.getIds().get(0));
        assertEquals(expectedDevice.getDeviceTelecom().get(0).getValue(), actualDevice.getDeviceTelecom().get(0).getValue());
    }

    public static void assertIiEquals(final II expectedIi, final II actualIi) {
        assertEquals(expectedIi.getRoot(), actualIi.getRoot());
        assertEquals(expectedIi.getExtension(), actualIi.getExtension());
    }

    public static PixV3QueryQuery getSampleQuery() {
        final PixV3QueryQuery query = new PixV3QueryQuery();
        query.setCreationTime(ZonedDateTime.now());
        query.getDataSourceOids().add("1.2.3");
        query.getDataSourceOids().add("7.8.9");
        query.setQueryPatientId(new II("123", "1.2.3"));
        query.setQueryId(new II("queryId", "4.5.6"));
        query.setMessageId(new II("m1", "1.3.5"));

        query.setReceiver(new Device());
        query.getReceiver().setDesc(new ED());
        query.getReceiver().getDesc().mixed = List.of("Description 1");
        query.getReceiver().setTelecom(new TEL());
        query.getReceiver().getTelecom().setValue("123456789");
        query.getReceiver().getIds().add(new II("1.2.840.114350.1.13.99999.4567", null));
        query.getReceiver().setSoftwareName(new SC());
        query.getReceiver().getSoftwareName().mixed = List.of("Software name 1");
        query.getReceiver().setManufacturerModelName(new SC());
        query.getReceiver().getManufacturerModelName().mixed = List.of("Manufacturer model name 1");
        query.getReceiver().getDeviceTelecom().add(new TEL());
        query.getReceiver().getDeviceTelecom().get(0).setValue("https://example.org/PIXQuery");
        query.getReceiver().getNames().add(new EN());
        query.getReceiver().getNames().get(0).mixed = List.of("Receiver");

        query.setSender(new Device());
        query.getSender().setDesc(new ED());
        query.getSender().getDesc().mixed = List.of("Description 2");
        query.getSender().setTelecom(new TEL());
        query.getSender().getTelecom().setValue("987654321");
        query.getSender().getIds().add(new II("1.2.840.114350.1.13.99997.2.7788", null));
        query.getSender().setSoftwareName(new SC());
        query.getSender().getSoftwareName().mixed = List.of("Software name 2");
        query.getSender().setManufacturerModelName(new SC());
        query.getSender().getManufacturerModelName().mixed = List.of("Manufacturer model name 2");
        query.getSender().getDeviceTelecom().add(new TEL());
        query.getSender().getDeviceTelecom().get(0).setValue("https://example.org/");
        query.getSender().getNames().add(new EN());
        query.getSender().getNames().get(0).mixed = List.of("Sender");
        return query;
    }
}