/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.continua.hrn.converters;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ParseException;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;

/**
 * Tests for {@link ProvideAndRegisterDocumentSetContentConverters}.
 * 
 * @author Stefan Ivanov
 */
public class ProvideAndRegisterDocumentSetContentConvertersTest {

    @Test(expected = ParseException.class)
    public void testConvertProvideAndRegisterDocumentSet() {
        ProvideAndRegisterDocumentSet set = SampleData.createProvideAndRegisterDocumentSet();
        ClinicalDocument doc = ProvideAndRegisterDocumentSetContentConverters.convert(set);
        assertNotNull(doc);
    }

}
