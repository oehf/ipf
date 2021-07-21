/*
 * Copyright 2014 the original author or authors.
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
package org.openehealth.ipf.tutorials.iheclient;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:context.xml")
public class IHEWebServiceClientTest {

    @Autowired
    private IHEWebServiceClient iheClient;

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testSmoke() throws Exception {
        assertNotNull(iheClient.getCamelContext());
    }

    @Disabled
    public void testIti47() throws Exception {
        var in = getClass().getResourceAsStream("/example-messages/PDQv3.xml");
        var request = IOUtils.toString(in, Charset.defaultCharset());
        var result = iheClient.iti47PatientDemographicsQuery(request, "localhost", 8080, "pix-xref-mgr/ws/iti47Service?secure=true", false);
        System.out.println(result);
    }

}
