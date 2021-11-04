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
package org.openehealth.ipf.modules.hl7.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "/context-custom-configurer.xml",
		"/context-custom-classes.xml" })
public class CustomModelClassFactoryConfigurerTest {

    @Autowired
    private CustomModelClassFactoryConfigurer<?> configurer;
    
    @Test
    public void testMappings() {
        var map = configurer.getCustomModelClassFactory().getCustomModelClasses();
    	assertTrue(map.containsKey("2.5"));
    	assertEquals("org.openehealth.ipf.modules.hl7.parser.test.hl7v2.def.v25", map.get("2.5")[0]);
    }	
}
