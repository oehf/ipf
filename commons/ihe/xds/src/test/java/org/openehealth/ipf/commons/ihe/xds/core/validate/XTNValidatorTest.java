/*
 * Copyright 2013 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.validate;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Tests for {@link org.openehealth.ipf.commons.ihe.xds.core.validate.XTNValidator}.
 * @author Dmytro Rud
 */
public class XTNValidatorTest {
    private static final XTNValidator validator = new XTNValidator();

    @Test
    public void testValidateGoodCases() throws XDSMetaDataException {
        validator.validate("^^Internet^a@x.ua");
        validator.validate("^^Internet^a@x.ua^^^^^");
        validator.validate("^NET^Internet^a@x.ua^^^^^");
        validator.validate("^^PH^^11^22^33^44^");
        validator.validate("^PRN^PH^^11^22^33^44^");
        validator.validate("^^PH^^^^33");
        validator.validate("^PRN^PH^^^^33^^");
        validator.validate("^^CP^^11^22^33^44^");
        validator.validate("^PRN^CP^^11^22^33^44^");
        validator.validate("^PRN^CP^^^^^^^^^8 (0622) 90-66-83^^^");
    }
    
    @Test 
    public void testValidateBadCases() throws XDSMetaDataException {
        assertFails("");
        assertFails("^^^");
        assertFails("^^Internet^");
        assertFails("^^Floppynet^blabla");
        assertFails("^^Internet^a@x.ua^12345");
        assertFails("^^Internet^^11^22^33^44");
        assertFails("^^Internet^^^^^^^^^8 (0622) 90-66-83^^^");
        assertFails("^NET^PH^^11^22^33^44");
        assertFails("^^PH^^^^^^55^^^");
        assertFails("^^CP^^11^22^^44");
        assertFails("^^PH^abc@de.com^11^22^33^44");
        assertFails("^PRN^CP^^^^111^^^^^222^^^");
    }

    private static void assertFails(String value) {
        try {
            validator.validate(value);
            fail("Expected exception: " + XDSMetaDataException.class + " for " + value);
        } catch (XDSMetaDataException e) {
            // Expected
        }
    }
}
