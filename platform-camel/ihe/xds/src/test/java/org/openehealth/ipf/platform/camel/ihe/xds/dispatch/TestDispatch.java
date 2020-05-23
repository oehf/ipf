/*
 * Copyright 2015 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.xds.dispatch;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xds.core.SampleData;
import org.openehealth.ipf.platform.camel.ihe.xds.XdsStandardTestContainer;

import static org.junit.Assert.assertEquals;

public class TestDispatch extends XdsStandardTestContainer {
    
    static final String CONTEXT_DESCRIPTOR = "dispatch/dispatch-test-context.xml";

    final String ITI_18_SERVICE_URI = "xds-iti18://localhost:%d/xdsRegistry";
    final String ITI_42_SERVICE_URI = "xds-iti42://localhost:%d/xdsRegistry";
    final String PHARM_1_SERVICE_URL = "cmpd-pharm1://localhost:%d/xdsRegistry";

    public static void main(String... args) {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR, false, 8888);
    }
    
    @BeforeClass
    public static void classSetUp() {
        startServer(new CXFServlet(), CONTEXT_DESCRIPTOR);
    }


    @Test
    public void testXdsDispatch() {
        send(String.format(ITI_42_SERVICE_URI, getPort()), SampleData.createRegisterDocumentSet());
        send(String.format(ITI_18_SERVICE_URI, getPort()), SampleData.createFindDocumentsQuery());
        send(String.format(PHARM_1_SERVICE_URL, getPort()), SampleData.createFindDispensesQuery());
        var queue = getAuditSender();
        var messages = queue.getMessages();
        assertEquals(6, messages.size());
    }
}
