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
package org.openehealth.ipf.platform.camel.ihe.pixpdqv3.pcc1

import org.apache.cxf.transport.servlet.CXFServlet
import org.junit.BeforeClass
import org.junit.Test
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer

/**
 * Tests for PCC-1.
 * @author Dmytro Rud
 */
class TestPcc1 extends StandardTestContainer {

     def SERVICE1 = "qed-pcc1://localhost:${org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer.port}/qed-pcc1-service1";

     @BeforeClass
     static void setUpClass() {
         startServer(new CXFServlet(), 'pcc-1.xml')
     }
    
     @Test
     void testPcc1() {
         def response = send(SERVICE1, '<QUPC_IN043100UV01 />', String.class)
         def slurper = new XmlSlurper().parseText(response)
         assert slurper.@from == 'Clinical Data Source'
     }
}
