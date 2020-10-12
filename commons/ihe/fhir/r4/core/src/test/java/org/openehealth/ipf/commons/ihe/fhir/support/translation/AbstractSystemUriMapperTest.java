/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.fhir.support.translation;

import ca.uhn.fhir.context.FhirContext;
import org.junit.Before;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.fhir.support.DefaultNamingSystemServiceImpl;
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 *
 */
abstract class AbstractSystemUriMapperTest {

    private UriMapper uriMapper;

    @Before
    public void setup() {
        this.uriMapper = initMapper(new DefaultNamingSystemServiceImpl(FhirContext.forR4()));
    }

    protected abstract UriMapper initMapper(DefaultNamingSystemServiceImpl namingSystemService);

    @Test
    public void testTranslateOidUrn() {
        var oid = "1.2.3.4.5.6.7.8.9";
        assertEquals(oid, uriMapper.uriToOid("urn:oid:" + oid).orElse(null));
    }

    @Test
    public void testTranslateUriToOid() {
        var uri = "http://org.openehealth/ipf/commons/ihe/fhir/1";
        assertEquals("1.2.3.4", uriMapper.uriToOid(uri).orElse(null));
    }

    @Test
    public void testTranslateUriToOidFails() {
        var uri = "http://org.openehealth/ipf/commons/ihe/fhir/9";
        assertFalse(uriMapper.uriToOid(uri).isPresent());
    }

    @Test
    public void testTranslatePinUrn() {
        var namespace = "namespace";
        assertEquals(namespace, uriMapper.uriToNamespace("urn:pin:" + namespace).orElse(null));
    }

    @Test
    public void testTranslateEncodedPinUrn() {
        var namespace = "name%20space";
        assertEquals("name space", uriMapper.uriToNamespace("urn:pin:" + namespace).orElse(null));
    }

    @Test
    public void testTranslateUriToNamespace() {
        var uri = "http://org.openehealth/ipf/commons/ihe/fhir/1";
        assertEquals("fhir1", uriMapper.uriToNamespace(uri).get());
        uri = "http://org.openehealth/ipf/commons/ihe/fhir/9";
        assertFalse(uriMapper.uriToNamespace(uri).isPresent());
    }

    @Test
    public void testTranslateNamespaceToUri() {
        var namespace = "fhir1";
        assertEquals("http://org.openehealth/ipf/commons/ihe/fhir/1", uriMapper.namespaceToUri(namespace));
    }

    @Test
    public void testTranslateNamespaceToUrn() {
        var namespace = "fhir1 fhir2";
        assertEquals("urn:pin:fhir1%20fhir2", uriMapper.namespaceToUri(namespace));
    }
}
