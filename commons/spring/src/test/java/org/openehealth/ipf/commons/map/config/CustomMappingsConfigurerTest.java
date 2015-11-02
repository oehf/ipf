/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.map.config;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context-custom-configurer.xml",
		"/context-custom-mappings.xml" })
public class CustomMappingsConfigurerTest {

    @Autowired
    private CustomMappingsConfigurer<?> configurer;
    
    @Test
    public void testMappings() {
        assertEquals("b1", configurer.getMappingService().get("m1", "a1"));
        assertEquals("b2", configurer.getMappingService().get("m2", "a2"));
        assertEquals("b3", configurer.getMappingService().get("m3", "a3"));
        
        assertEquals("c1", configurer.getMappingService().get("m4", "d1"));
        assertEquals("c2", configurer.getMappingService().get("m5", "d2"));
        assertEquals("c3", configurer.getMappingService().get("m6", "d3"));
    }
	
}
