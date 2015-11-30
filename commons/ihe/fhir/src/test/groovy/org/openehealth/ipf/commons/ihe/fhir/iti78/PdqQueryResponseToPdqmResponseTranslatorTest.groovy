/*
 * Copyright 2015 the original author or authors.
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

package org.openehealth.ipf.commons.ihe.fhir.iti78

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException
import ca.uhn.hl7v2.HapiContext
import org.apache.commons.io.IOUtils
import org.easymock.EasyMock
import org.hl7.fhir.instance.model.Identifier
import org.hl7.fhir.instance.model.Parameters
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.core.config.Registry
import org.openehealth.ipf.commons.ihe.fhir.iti83.PixQueryResponseToPixmResponseTranslator
import org.openehealth.ipf.commons.ihe.fhir.translation.DefaultUriMapper
import org.openehealth.ipf.commons.ihe.fhir.translation.UriMapper
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.CustomModelClassUtils
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory
import org.openehealth.ipf.commons.map.BidiMappingService
import org.openehealth.ipf.commons.map.MappingService
import org.openehealth.ipf.gazelle.validation.profile.pixpdq.PixPdqTransactions

/**
 *
 */
class PdqQueryResponseToPdqmResponseTranslatorTest extends Assert {

    private static final HapiContext PIX_QUERY_CONTEXT = HapiContextFactory.createHapiContext(
            CustomModelClassUtils.createFactory("pdq", "2.5"),
            PixPdqTransactions.ITI21)

    private PixQueryResponseToPixmResponseTranslator translator
    MappingService mappingService

    @Before
    public void setup() {
        mappingService = new BidiMappingService()
        mappingService.addMappingScript(getClass().getClassLoader().getResource('mapping.map'))
        UriMapper mapper = new DefaultUriMapper(mappingService, 'uriToOid', 'uriToNamespace')
        translator = new PdqResponseToPdqmResponseTranslator(mapper)

        Registry registry = EasyMock.createMock(Registry)
        ContextFacade.setRegistry(registry)
        EasyMock.expect(registry.bean(MappingService)).andReturn(mappingService).anyTimes()
        EasyMock.replay(registry)
    }

    @Test
    public void test() {
        // TODO
    }
}
