/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.commons.lbs.store;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.UUID;

import org.junit.Test;
import org.openehealth.ipf.commons.lbs.store.FlatUriUuidConversion;


/**
 * @author Jens Riemschneider
 */
public class FlatUuidUriConversionTest {
    @Test
    public void testSimpleCorrectCase() {
        URI baseUri = URI.create("http://schnuffelhost/");
        FlatUriUuidConversion strat = new FlatUriUuidConversion(baseUri);
        UUID uuid = UUID.randomUUID();
        URI uri = strat.toUri(uuid);
        assertNotNull(uri);
        assertEquals(uuid, strat.toUuid(uri));
    }
    
    @Test
    public void testToUuidWithOtherScheme() {
        URI baseUri1 = URI.create("http://schnuffelhost/");
        FlatUriUuidConversion strat1 = new FlatUriUuidConversion(baseUri1);
        URI baseUri2 = URI.create("ftp://schnuffelhost/");
        FlatUriUuidConversion strat2 = new FlatUriUuidConversion(baseUri2);
        UUID uuid = UUID.randomUUID();
        URI uri = strat1.toUri(uuid);
        assertNull(strat2.toUuid(uri));
    }
    
    @Test
    public void testToUuidWithOtherHost() {
        URI baseUri1 = URI.create("http://schnuffelhost/");
        FlatUriUuidConversion strat1 = new FlatUriUuidConversion(baseUri1);
        URI baseUri2 = URI.create("http://otherhost/");
        FlatUriUuidConversion strat2 = new FlatUriUuidConversion(baseUri2);
        UUID uuid = UUID.randomUUID();
        URI uri = strat1.toUri(uuid);
        assertNull(strat2.toUuid(uri));
    }
    
    @Test
    public void testToUuidWithNonUuid() {
        URI baseUri = URI.create("http://schnuffelhost/");
        FlatUriUuidConversion strat = new FlatUriUuidConversion(baseUri);
        assertNull(strat.toUuid(URI.create("http://schnuffelhost/unsinn")));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNullSafetyToUriUuidNull() {
        URI baseUri = URI.create("http://schnuffelhost/");
        new FlatUriUuidConversion(baseUri).toUri(null);        
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSafetyConstructor() {
        new FlatUriUuidConversion(null);        
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSafetyToUuidUriNull() {
        URI baseUri = URI.create("http://schnuffelhost/");
        new FlatUriUuidConversion(baseUri).toUuid(null);        
    }
}
