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
package org.openehealth.ipf.commons.map.extend

import static org.easymock.EasyMock.*

import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.core.config.Registry
import org.openehealth.ipf.commons.map.BidiMappingService
import org.openehealth.ipf.commons.map.MappingService
import org.springframework.core.io.ClassPathResource

/**
 * @author Christian Ohr
 * @author Martin Krasser
 */
public class MappingExtensionTest {
    
    @BeforeClass
    static void setupClass() {
        BidiMappingService mappingService = new BidiMappingService()
        mappingService.setMappingScript(MappingExtensionTest.class.getResource("/example2.map"))
        Registry registry = createMock(Registry)
        ContextFacade.setRegistry(registry)
        expect(registry.bean(MappingService)).andReturn(mappingService).anyTimes()
        replay(registry)
    }
	
    @Test
    void testMap() {
        assert 'E'.map('encounterType') == 'EMER'
        assert 'X'.map('encounterType') == null
        assert 'X'.map('encounterType', 'WRONG') == 'WRONG'
    }
    
    @Test(expected=IllegalArgumentException)
    void testUnknownKey() {
        'Y'.map('BLABLA')
    }
    
    @Test
    void testMapReverse() {
        assert 'EMER'.mapReverse('encounterType') == 'E'
        assert 'X'.mapReverse('encounterType') == null
        assert 'X'.mapReverse('encounterType', 'WRONG') == 'WRONG'
    }
    
    @Test(expected=IllegalArgumentException)
    void testMapReverseUnknownKey() {
        'Y'.mapReverse('BLABLA')
    }
    
    @Test
    void testKeyAndValueSystems() {
        assert 'encounterType'.keySystem() == '2.16.840.1.113883.12.4'
        assert 'encounterType'.valueSystem() == '2.16.840.1.113883.5.4'
    }
    
    @Test(expected=IllegalArgumentException)
    void testUnknownKeySystem() {
        'Y'.keySystem()
    }

    @Test
    void testKeys() {
        assert 'encounterType'.keys().sort() == ['E','I','O']
        assert 'vip'.keys() == ['Y'] as Set
    }
    
    @Test
    void testValues() {
        assert 'encounterType'.values().sort() == ['AMB','EMER','IMP']
    }
    
    @Test
    void testHasKey() {
        assert 'encounterType'.hasKey('E')
        assert 'encounterType'.hasKey('Y') == false
    }
    
    @Test
    void testHasValue() {
        assert 'encounterType'.hasValue('EMER')
        assert 'encounterType'.hasValue('Y') == false
    }
    
}

