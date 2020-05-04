/*
 * Copyright 2018 the original author or authors.
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

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_SERVICE_START_TIME;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_SERVICE_STOP_TIME;

import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryObject;

/**
 * Tests for {@link ServiceTimeChronologyValidation}.
 * @author Thomas Papke
 */
public class ServiceTimeChronologyValidationTest {
    private static final ServiceTimeChronologyValidation validator = new ServiceTimeChronologyValidation();

    @Test
    public void validEqualDates() throws XDSMetaDataException {
        validator.validate(anyRegistryObjectWith("1980", "1980"));
        validator.validate(anyRegistryObjectWith("19800101010159", "19800101010159"));
    }

    @Test
    public void noValidationErrorOnValueNotPresent() throws XDSMetaDataException {
        validator.validate(anyRegistryObjectWith("1980", null));
        validator.validate(anyRegistryObjectWith(null, "19800101010159"));
        validator.validate(anyRegistryObjectWith(null, null));
    }

    @Test
    public void validInCorrectChronology() throws XDSMetaDataException {
        validator.validate(anyRegistryObjectWith("1980", "1981"));
        validator.validate(anyRegistryObjectWith("19800101010158", "19800101010159"));
        validator.validate(anyRegistryObjectWith("1980", "19800101010159"));
        validator.validate(anyRegistryObjectWith("19900101010159", "1990"));
    }

    @Test
    public void invalidChronology() throws XDSMetaDataException {
        assertFails("1981", "1980");
        assertFails("19800101010159", "19800101010158");
        assertFails("1981", "19800101010159");
        assertFails("19900101010159", "1989");
    }

    private static void assertFails(String serviceStartTime, String serviceStopTime) {
        try {
            validator.validate(anyRegistryObjectWith(serviceStartTime, serviceStopTime));
            fail("Expected exception: " + XDSMetaDataException.class + "serviceStartTime " + serviceStartTime
                    + " and serviceStopTime" + serviceStopTime);
        } catch (XDSMetaDataException e) {
            // Expected
        }
    }

    private static EbXMLRegistryObject anyRegistryObjectWith(String serviceStartTime, String serviceStopTime) {
        var mock = mock(EbXMLRegistryObject.class);
        when(mock.getSingleSlotValue(SLOT_NAME_SERVICE_START_TIME)).thenReturn(serviceStartTime);
        when(mock.getSingleSlotValue(SLOT_NAME_SERVICE_STOP_TIME)).thenReturn(serviceStopTime);
        return mock;
    }
}
