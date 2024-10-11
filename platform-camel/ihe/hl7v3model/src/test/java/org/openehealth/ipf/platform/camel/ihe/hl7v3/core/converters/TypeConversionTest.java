/*
 * Copyright 2024 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.hl7v3.core.converters;

import net.ihe.gazelle.hl7v3.datatypes.II;
import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openehealth.ipf.commons.ihe.hl7v3.core.metadata.Device;
import org.openehealth.ipf.commons.ihe.hl7v3.core.requests.PixV3QueryRequest;
import org.openehealth.ipf.platform.camel.ihe.ws.StandardTestContainer;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TypeConversionTest extends StandardTestContainer {

    @BeforeAll
    public static void beforeAll() {
        startServer(new CXFServlet(), "context.xml");
    }

    @Test
    public void testPixV3QueryRequestTranslation() throws Exception {
        PixV3QueryRequest request = new PixV3QueryRequest();
        request.setReceiver(new Device());
        request.setSender(new Device());
        request.setQueryPatientId(new II("patient1", "3.14.15.926"));

        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getMessage().setBody(request);

        String s = exchange.getMessage().getMandatoryBody(String.class);
        assertTrue(s.startsWith("<"));
    }

}
