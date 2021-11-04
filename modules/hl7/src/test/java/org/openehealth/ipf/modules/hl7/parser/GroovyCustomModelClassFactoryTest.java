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
package org.openehealth.ipf.modules.hl7.parser;

import ca.uhn.hl7v2.HL7Exception;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openehealth.ipf.modules.hl7.config.CustomModelClassFactoryConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = { "/context-custom-configurer.xml",
		"/context-custom-classes.xml" })
public class GroovyCustomModelClassFactoryTest {

    @Autowired
    private CustomModelClassFactoryConfigurer configurer;
    
    @Test
    public void testMappings() throws HL7Exception {
        var map = configurer.getCustomModelClassFactory().getCustomModelClasses();
        assertTrue(map.containsKey("2.5"));
        assertTrue(map.containsKey("2.4"));
        Class<?> clazz = configurer.getCustomModelClassFactory()
                .getMessageClass("MDM_T01", "2.5", false);
        assertNotNull(clazz);
        assertEquals("org.openehealth.ipf.modules.hl7.parser.test.hl7v2.def.v25.message.MDM_T01",
                clazz.getCanonicalName());
        Class<?> clazz1 = configurer.getCustomModelClassFactory()
                .getMessageClass("MDM_T02", "2.4", false);
        assertNotNull(clazz1);
        assertEquals("org.openehealth.ipf.modules.hl7.parser.groovytest.hl7v2.def.v24.message.MDM_T02",
                clazz1.getCanonicalName());
    }	
}
