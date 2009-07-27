/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.modules.cda.support;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.openehealth.ipf.modules.cda.support.DateTimeUtils.DATETIME_MINUTE_PRECISION;
import static org.openehealth.ipf.modules.cda.support.DateTimeUtils.DATETIME_SECOND_PRECISION;
import static org.openehealth.ipf.modules.cda.support.DateTimeUtils.DATE_PRECISION;
import static org.openehealth.ipf.modules.cda.support.DateTimeUtils.MONTH_PRECISION;
import static org.openehealth.ipf.modules.cda.support.DateTimeUtils.YEAR_PRECISION;
import static org.openehealth.ipf.modules.cda.support.DateTimeUtils.isValidDateTime;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Christian Ohr
 */
public class DateTimeUtilsTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testIsValidDateTimeString() {
        assertTrue(isValidDateTime("2009"));
        assertTrue(isValidDateTime("200902"));
        assertTrue(isValidDateTime("20090228"));
        assertTrue(isValidDateTime("200902281205"));
        assertTrue(isValidDateTime("200902281205+0001"));
        assertTrue(isValidDateTime("20090228120556"));
        assertTrue(isValidDateTime("20090228120556+0001"));
        assertTrue(isValidDateTime("20090228120556.123"));
        assertTrue(isValidDateTime("20090228120556.123+0200"));
        assertFalse(isValidDateTime("200913"));
        assertFalse(isValidDateTime("20090230"));
        assertFalse(isValidDateTime("200902282505"));
        assertFalse(isValidDateTime("200902281205+9999"));
        assertFalse(isValidDateTime("20090228120599"));
        assertFalse(isValidDateTime("20090228120599+9999"));
        assertFalse(isValidDateTime("20090228120556.12345"));
        assertFalse(isValidDateTime("20090228120556.123+9999"));
    }

    @Test
    public void testIsValidDateTimeMinimumPrecision() {
        assertTrue(isValidDateTime("2009", YEAR_PRECISION));
        assertTrue(isValidDateTime("200902", MONTH_PRECISION));
        assertTrue(isValidDateTime("20090228", DATE_PRECISION));
        assertTrue(isValidDateTime("200902281205", DATETIME_MINUTE_PRECISION));
        assertTrue(isValidDateTime("200902281205+0001",
                DATETIME_MINUTE_PRECISION));
        assertTrue(isValidDateTime("20090228120556", DATETIME_SECOND_PRECISION));
        assertTrue(isValidDateTime("20090228120556+0001",
                DATETIME_MINUTE_PRECISION));
        assertTrue(isValidDateTime("20090228120556.123",
                DATETIME_MINUTE_PRECISION));
        assertTrue(isValidDateTime("20090228120556.123+0200",
                DATETIME_MINUTE_PRECISION));
        assertFalse(isValidDateTime("2009", DATETIME_MINUTE_PRECISION));
        assertFalse(isValidDateTime("2009", DATE_PRECISION));
        assertFalse(isValidDateTime("20090230", DATETIME_MINUTE_PRECISION));
    }

    @Test
    public void testIsValidDateTimeExactPrecision() {
        assertTrue(isValidDateTime("2009", YEAR_PRECISION, YEAR_PRECISION));
        assertTrue(isValidDateTime("200902", YEAR_PRECISION, MONTH_PRECISION));
        assertTrue(isValidDateTime("20090228", YEAR_PRECISION, DATE_PRECISION));
        assertTrue(isValidDateTime("200902281205", DATE_PRECISION,
                DATETIME_MINUTE_PRECISION));
        assertTrue(isValidDateTime("200902281205+0001", DATE_PRECISION,
                DATETIME_MINUTE_PRECISION));
        assertTrue(isValidDateTime("20090228120556", DATETIME_SECOND_PRECISION,
                DATETIME_SECOND_PRECISION));
        assertFalse(isValidDateTime("2009", DATE_PRECISION, DATE_PRECISION));
        assertFalse(isValidDateTime("20090230", YEAR_PRECISION, YEAR_PRECISION));
    }

}
