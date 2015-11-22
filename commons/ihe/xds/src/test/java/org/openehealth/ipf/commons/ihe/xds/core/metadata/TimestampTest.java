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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

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
        check(ts, Timestamp.Precision.HOUR,   "2015010201");
        check(ts, Timestamp.Precision.DAY,    "20150102");
        check(ts, Timestamp.Precision.MONTH,  "201501");
        check(ts, Timestamp.Precision.YEAR,   "2015");
    }

    @Test
    public void testRendering2() {
        Timestamp ts = Timestamp.fromHL7("2015");
        assertEquals(Timestamp.Precision.YEAR, ts.getPrecision());

        check(ts, Timestamp.Precision.SECOND, "20150101000000");
        check(ts, Timestamp.Precision.MINUTE, "201501010000");
        check(ts, Timestamp.Precision.HOUR,   "2015010100");
        check(ts, Timestamp.Precision.DAY,    "20150101");
        check(ts, Timestamp.Precision.MONTH,  "201501");
        check(ts, Timestamp.Precision.YEAR,   "2015");
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
        // Somewhere in Japan, with 777 milliseconds...
        Timestamp ts1 = Timestamp.fromHL7("20150102100405.777+0800");

        // ...and in Berlin, with only 123 milliseconds, but...
        Timestamp ts2 = new Timestamp(
                new DateTime(2015, 1, 2, 3, 4, 5, 123, DateTimeZone.forID("CET")),
                Timestamp.Precision.SECOND);

        // ...the both timestamps can be considered equal.
        assertEquals(ts1, ts2);

        ts2.setPrecision(Timestamp.Precision.MINUTE);
        assertNotEquals(ts1, ts2);

        ts1.setPrecision(Timestamp.Precision.MINUTE);
        assertEquals(ts1, ts2);

        // introduce a difference of 1 second
        ts1.setDateTime(ts1.getDateTime().minus(1000L));
        assertNotEquals(ts1, ts2);
    }

}
