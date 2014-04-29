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
package org.openehealth.ipf.iheclient;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
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

    @Ignore
    public void testIti47() throws Exception {
        InputStream in = getClass().getResourceAsStream("/example-messages/PDQv3.xml");
        String request = IOUtils.toString(in);
        String result = iheClient.iti47PatientDemographicsQuery(request, "localhost", 8080, "pix-xref-mgr/ws/iti47Service?secure=true", false);
        System.out.println(result);
    }

}
