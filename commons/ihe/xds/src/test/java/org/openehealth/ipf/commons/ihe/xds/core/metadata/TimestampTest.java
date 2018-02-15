/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.requests.QueryRegistry;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FetchQuery;
import org.openehealth.ipf.commons.xml.XmlUtils;
import org.w3c.dom.Element;

import javax.xml.bind.JAXBContext;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

import static org.junit.Assert.*;

/**
 * @author Dmytro Rud
 */
public class TimestampTest {

    private static void check(Timestamp ts, Timestamp.Precision precision, String s) {
        ts.setPrecision(precision);
        assertEquals(s, Timestamp.toHL7(ts));
        assertEquals(precision, Timestamp.fromHL7(s).getPrecision());
    }

    @Test
    public void testRendering1() {
        Timestamp ts = Timestamp.fromHL7("20150102030405.777+0200");
        assertEquals(Timestamp.Precision.SECOND, ts.getPrecision());

        check(ts, Timestamp.Precision.SECOND, "20150102010405");
        check(ts, Timestamp.Precision.MINUTE, "201501020104");
        check(ts, Timestamp.Precision.HOUR, "2015010201");
        check(ts, Timestamp.Precision.DAY, "20150102");
        check(ts, Timestamp.Precision.MONTH, "201501");
        check(ts, Timestamp.Precision.YEAR, "2015");
    }

    @Test
    public void testRendering2() {
        Timestamp ts = Timestamp.fromHL7("2015");
        assertEquals(Timestamp.Precision.YEAR, ts.getPrecision());

        check(ts, Timestamp.Precision.SECOND, "20150101000000");
        check(ts, Timestamp.Precision.MINUTE, "201501010000");
        check(ts, Timestamp.Precision.HOUR, "2015010100");
        check(ts, Timestamp.Precision.DAY, "20150101");
        check(ts, Timestamp.Precision.MONTH, "201501");
        check(ts, Timestamp.Precision.YEAR, "2015");
    }

    @Test
    public void testNullValues() {
        assertNull(Timestamp.fromHL7(null));
        assertNull(Timestamp.fromHL7(""));
        assertNull(Timestamp.toHL7(null));
    }

    private static void expectFailure(String s) {
        try {
            Timestamp ts = Timestamp.fromHL7(s);
            fail("This line must be not reachable");
        } catch (Exception e) {
            // ok
        }
    }

    @Test
    public void testBadValue1() {
        expectFailure("201501020304056789"); // toooo loooong

        expectFailure("2015010203040"); // incomplete value for second
        expectFailure("20150102030");   // incomplete value for minute
        expectFailure("201501020");     // incomplete value for hour
        expectFailure("2015010");       // incomplete value for day
        expectFailure("20150");         // incomplete value for month
        expectFailure("015");           // incomplete value for year
        expectFailure("15");            // incomplete value for yea
        expectFailure("1");             // incomplete value for ye

        expectFailure(" ");             // philosophy can convert space into time, IPF cannot (yet)
        expectFailure("nine o'clock");  // philology can handle natural languages, IPF cannot (yet)
    }

    @Test
    public void testEquivalence() {
        Timestamp ts1 = Timestamp.fromHL7("20150102100405.777+0800");

        ts1.setPrecision(Timestamp.Precision.YEAR);
        assertEquals(ts1, Timestamp.fromHL7("2015"));
        assertNotEquals(ts1, Timestamp.fromHL7("2017"));

        ts1.setPrecision(Timestamp.Precision.MONTH);
        assertEquals(ts1, Timestamp.fromHL7("201501"));
        assertNotEquals(ts1, Timestamp.fromHL7("201507"));

        ts1.setPrecision(Timestamp.Precision.DAY);
        assertEquals(ts1, Timestamp.fromHL7("20150102"));
        assertNotEquals(ts1, Timestamp.fromHL7("20150107"));

        ts1.setPrecision(Timestamp.Precision.HOUR);
        assertEquals(ts1, Timestamp.fromHL7("2015010204+0200"));
        assertNotEquals(ts1, Timestamp.fromHL7("2015010207+0200"));

        ts1.setPrecision(Timestamp.Precision.MINUTE);
        assertEquals(ts1, Timestamp.fromHL7("201501020404+0200"));
        assertNotEquals(ts1, Timestamp.fromHL7("201501020407+0200"));

        ts1.setPrecision(Timestamp.Precision.SECOND);
        assertEquals(ts1, Timestamp.fromHL7("20150102040405.123+0200"));
        assertNotEquals(ts1, Timestamp.fromHL7("20150102040407.777+0200"));
    }

    @Test
    public void testNullValues2() {
        Timestamp ts1 = Timestamp.fromHL7("20151201");
        assertEquals(Timestamp.Precision.DAY, ts1.getPrecision());

        ts1.setPrecision(null);
        assertEquals(Timestamp.Precision.SECOND, ts1.getPrecision());
        assertEquals("20151201000000", Timestamp.toHL7(ts1));

        Timestamp ts2 = Timestamp.fromHL7("20151201000000");
        assertEquals(ts1, ts2);

        assertNotEquals(ts1.hashCode(), ts2.hashCode());
    }

    @Test
    public void testXml() throws Exception {
        QueryRegistry queryRegistry = SampleData.createFetchQuery();
        FetchQuery query = (FetchQuery) queryRegistry.getQuery();
        assertEquals(Timestamp.Precision.YEAR, query.getCreationTime().getFrom().getPrecision());
        query.getCreationTime().getFrom().setPrecision(null);

        Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("dummy");
        JAXBContext jaxbContext = JAXBContext.newInstance(QueryRegistry.class);
        jaxbContext.createMarshaller().marshal(query, element);

        String s = new String(XmlUtils.serialize(element.getFirstChild()));
        assertTrue(s.contains("<xds:from dateTime=\"1980-01-01T00:00:00Z\"/>"));
        assertTrue(s.contains("<xds:to dateTime=\"1981-01-01T00:00:00Z\" precision=\"YEAR\"/>"));

        s = s.replace("precision=\"YEAR\"", "precision=\"some garbage\"");

        ByteArrayInputStream stream = new ByteArrayInputStream(s.getBytes());

        FetchQuery query1 = (FetchQuery) jaxbContext.createUnmarshaller().unmarshal(stream);
        assertEquals(Timestamp.Precision.SECOND, query1.getCreationTime().getFrom().getPrecision());
        assertEquals(Timestamp.Precision.SECOND, query1.getCreationTime().getTo().getPrecision());
    }

}
