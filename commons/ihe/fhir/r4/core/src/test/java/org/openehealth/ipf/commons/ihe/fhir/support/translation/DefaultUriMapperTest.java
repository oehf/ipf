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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.fhir.translation.DefaultUriMapper;
import org.openehealth.ipf.commons.map.Mappings;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 *
 */
public class DefaultUriMapperTest {

    private DefaultUriMapper uriMapper;

    @BeforeEach
    public void setup() {
        var mappings = Mappings.builder().load("classpath:/mapping.map").build();
        uriMapper = new DefaultUriMapper(mappings, "uriToOid", "uriToNamespace");
    }

    @Test
    public void testTranslateOidUrn() {
        var oid = "1.2.3.4.5.6.7.8.9";
        assertThat(uriMapper.uriToOid("urn:oid:" + oid).orElse(null), is(oid));
    }

    @Test
    public void testTranslateUriToOid() {
        var uri = "http://org.openehealth/ipf/commons/ihe/fhir/1";
        assertThat(uriMapper.uriToOid(uri).orElse(null), is("1.2.3.4"));
    }

    @Test
    public void testTranslateUriToOidFails() {
        var uri = "http://org.openehealth/ipf/commons/ihe/fhir/9";
        assertThat(uriMapper.uriToOid(uri).isPresent(), is(false));
    }

    @Test
    public void testTranslatePinUrn() {
        var namespace = "namespace";
        assertThat(uriMapper.uriToNamespace("urn:pin:" + namespace).orElse(null), is(namespace));
    }

    @Test
    public void testTranslateUriToNamespace() {
        var uri = "http://org.openehealth/ipf/commons/ihe/fhir/1";
        assertThat(uriMapper.uriToNamespace(uri).get(), is("fhir1"));
        uri = "http://org.openehealth/ipf/commons/ihe/fhir/9";
        assertThat(uriMapper.uriToNamespace(uri).isPresent(), is(false));
    }

    @Test
    public void testTranslateNamespaceToUri() {
        var namespace = "fhir1";
        assertThat(uriMapper.namespaceToUri(namespace),
                is("http://org.openehealth/ipf/commons/ihe/fhir/1"));
    }
}
