/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.translation;

import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.map.BidiMappingService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 *
 */
public class DefaultUriMapperTest {

    private BidiMappingService mappingService;
    private UriMapper uriMapper;

    @Before
    public void setup() {
        mappingService = new BidiMappingService();
        mappingService.setMappingScript(getClass().getResource("/mapping.map"));
        uriMapper = new DefaultUriMapper(mappingService, "uriToOid", "uriToNamespace");
    }

    @Test
    public void testTranslateOidUrn() throws Exception {
        String oid = "1.2.3.4.5.6.7.8.9";
        assertEquals(oid, uriMapper.uriToOid("urn:oid:" + oid));
    }

    @Test
    public void testTranslateUriToOid() throws Exception {
        String uri = "http://org.openehealth/ipf/commons/ihe/fhir/1";
        assertEquals("1.2.3.4", uriMapper.uriToOid(uri));
    }

    @Test(expected = FhirTranslationException.class)
    public void testTranslateUriToOidFails() throws Exception {
        String uri = "http://org.openehealth/ipf/commons/ihe/fhir/9";
        uriMapper.uriToOid(uri);
    }

    @Test
    public void testTranslatePinUrn() throws Exception {
        String namespace = "namespace";
        assertEquals(namespace, uriMapper.uriToNamespace("urn:pin:" + namespace));
    }

    @Test
    public void testTranslateUriToNamespace() throws Exception {
        String namespace = "http://org.openehealth/ipf/commons/ihe/fhir/1";
        assertEquals("fhir1", uriMapper.uriToNamespace(namespace));
        namespace = "http://org.openehealth/ipf/commons/ihe/fhir/9";
        assertNull(uriMapper.uriToNamespace(namespace));
    }
}
