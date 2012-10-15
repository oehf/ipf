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
package org.openehealth.ipf.modules.hl7.validation.builder

import org.junit.Test;
import org.openehealth.ipf.modules.hl7.validation.DefaultValidationContext
import groovy.util.GroovyTestCase


/**
 * @author Christian Ohr
 */
public class RuleBuilderTest  {
    
    @Test
     void testForVersion() {
         def context = new DefaultValidationContext()
         def builder = context.configure().forVersion('2.4')
         assert context == builder.context
         assert builder.version == '2.4'
     }
     
     @Test
     void testForAllVersions() {
         def context = new DefaultValidationContext()
         def builder = context.configure().forAllVersions()
         assert context == builder.context
         assert builder.version == '2.1 2.2 2.3 2.3.1 2.4 2.5 2.5.1 2.6'
     }
     
     @Test
     void testForVersionAsOf() {
         def context = new DefaultValidationContext()
         def builder = context.configure().forVersion().asOf('2.3')
         assert context == builder.context
         assert builder.version == '2.3 2.3.1 2.4 2.5 2.5.1 2.6'
     }
     
     @Test
     void testForVersionBefore() {
         def context = new DefaultValidationContext()
         def builder = context.configure().forVersion().before('2.3')
         assert context == builder.context
         assert builder.version == '2.1 2.2'
     }
     
     @Test
     void testForVersionExcept() {
         def context = new DefaultValidationContext()
         def builder = context.configure().forVersion().except('2.3.1')
         assert context == builder.context
         assert builder.version == '2.1 2.2 2.3 2.4 2.5 2.5.1 2.6'
     }
     
     @Test
     void testForVersionAndType() {
         def context = new DefaultValidationContext()
         def builder = context.configure().forVersionAndPrimitiveType('2.4', 'ST')
         assert context == builder.context
         assert builder.version == '2.4'
         assert builder.typeName == 'ST'
        
     }
}
