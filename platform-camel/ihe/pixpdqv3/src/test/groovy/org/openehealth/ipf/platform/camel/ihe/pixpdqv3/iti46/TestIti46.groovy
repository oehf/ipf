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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.iti46

import static org.junit.Assert.*
import org.openehealth.ipf.platform.camel.core.util.Exchanges

import org.junit.Before
import org.junit.Test
import org.junit.BeforeClass
import org.apache.cxf.transport.servlet.CXFServlet

import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

/**
 * Tests for ITI-46.
 * @author Dmytro Rud
 */
class TestIti46 extends StandardTestContainer {

     def SERVICE1 = "pixv3-iti46://localhost:${port}/pixv3-iti46-service1";

     @BeforeClass
     static void setUpClass() {
         startServer(new CXFServlet(), 'iti-46.xml')
     }
    
     @Test
     void testIti46() {
         def response = send(SERVICE1, '<request/>', String.class)
         def slurper = new XmlSlurper().parseText(response)
         assert slurper.@from == 'PIX Consumer'
     }
}
