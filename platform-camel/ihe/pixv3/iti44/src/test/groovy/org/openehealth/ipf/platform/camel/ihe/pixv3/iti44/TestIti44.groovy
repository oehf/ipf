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
package org.openehealth.ipf.platform.camel.ihe.pixv3.iti44

import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.BeforeClass
import org.apache.cxf.transport.servlet.CXFServlet

import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

/**
 * Tests for ITI-44.
 * @author Dmytro Rud
 */
class TestIti44 extends StandardTestContainer {

     def SERVICE1_PIX = "pixv3-iti44://localhost:${port}/pixv3-iti44-service1";
     def SERVICE1_XDS = "xds-iti44://localhost:${port}/xds-iti44-service1";

     @BeforeClass
     static void setUpClass() {
         startServer(new CXFServlet(), 'iti-44.xml')
     }
    
     @Test
     void testIti44Pix() {
         def response = send(SERVICE1_PIX, '<request/>', String.class)
         def slurper = new XmlSlurper().parseText(response)
         assert slurper.@from == 'PIX Manager'
     }

     @Test
     void testIti44Xds() {
         def response = send(SERVICE1_XDS, '<request/>', String.class)
         def slurper = new XmlSlurper().parseText(response)
         assert slurper.@from == 'Document Registry'
     }
}
