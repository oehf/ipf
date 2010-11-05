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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/context-custom-configurer.xml",
		"/context-custom-classes.xml" })
public class CustomModelClassFactoryConfigurerTest {

    @Autowired
    private CustomModelClassFactoryConfigurer configurer;
    
    @Test
    public void testMappings() {
    	Map<String, String[]> map = configurer.getCustomModelClassFactory().getCustomModelClasses();
    	assertTrue(map.containsKey("2.5"));
    	assertEquals("org.openehealth.ipf.modules.hl7.parser.test.hl7v2.def.v25", map.get("2.5")[0]);
    }	
}
