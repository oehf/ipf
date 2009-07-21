/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.modules.ccd.builder

import groovy.util.GroovyTestCase

/**
 * @author Christian Ohr
 */
public class CCDExtensionTest extends GroovyTestCase {
    
     void testCCDExtension() {
         ExpandoMetaClass.enableGlobally()
         def ccdExtensions = new CCDExtension()
         int c = ccdExtensions.extensions.call()
         assertEquals(4, c)
     }
}
