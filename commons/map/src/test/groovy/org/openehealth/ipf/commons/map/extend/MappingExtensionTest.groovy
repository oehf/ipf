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

import org.openehealth.ipf.commons.map.BidiMappingService

import org.springframework.core.io.ClassPathResource

/**
 * @author Christian Ohr
 * @author Martin Krasser
 */
public class MappingExtensionTest extends GroovyTestCase {
	
     def mappingService
     def extension
	 
    static {
        ExpandoMetaClass.enableGlobally()
    }
	
    void setUp() {
        mappingService = new BidiMappingService()
        mappingService.addMappingScript(new ClassPathResource("example2.map"))
        extension = new MappingExtension()
        extension.mappingService = mappingService
        extension.extensions()
    }
	
    void testMap() {
        assert 'E'.map('encounterType') == 'EMER'
        assert 'X'.map('encounterType') == null
        assert 'X'.map('encounterType', 'WRONG') == 'WRONG'
        
        assert 'E'.mapEncounterType() == 'EMER'
        assert 'X'.mapEncounterType() == null
        assert 'X'.mapEncounterType('WRONG') == 'WRONG'
        assert 'Y'.mapVip() == 'VIP'
        
        try {
        	assert 'Y'.map('BLABLA')
        	fail()
        } catch (IllegalArgumentException e) {
        	// o.k.
        }
    }
    
    void testMapReverse() {
        assert 'EMER'.mapReverse('encounterType') == 'E'
        assert 'X'.mapReverse('encounterType') == null
        assert 'X'.mapReverse('encounterType', 'WRONG') == 'WRONG'
        
        assert 'EMER'.mapReverseEncounterType() == 'E'
        assert 'X'.mapReverseEncounterType() == null
        assert 'X'.mapReverseEncounterType('WRONG') == 'WRONG'
        assert 'VIP'.mapReverseVip() == 'Y'
        try {
        	assert 'Y'.mapReverse('BLABLA')
        	fail()
        } catch (IllegalArgumentException e) {
        	// o.k.
        }
    }
    
    void testKeyAndValueSystems() {
        assert 'encounterType'.keySystem() == '2.16.840.1.113883.12.4'
        assert 'encounterType'.valueSystem() == '2.16.840.1.113883.5.4'
            try {
            	assert 'Y'.keySystem()
            	fail()
            } catch (IllegalArgumentException e) {
            	// o.k.
            }
    }

    void testKeys() {
        assert 'encounterType'.keys().sort() == ['E','I','O']
        assert 'vip'.keys() == ['Y'] as Set
    }
    
    void testValues() {
        assert 'encounterType'.values().sort() == ['AMB','EMER','IMP']
    }
    
    void testHasKey() {
        assert 'encounterType'.hasKey('E')
        assert 'encounterType'.hasKey('Y') == false
    }
    
    void testHasValue() {
        assert 'encounterType'.hasValue('EMER')
        assert 'encounterType'.hasValue('Y') == false
    }
    
}

