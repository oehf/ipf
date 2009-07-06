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
package org.openehealth.ipf.platform.camel.ihe.xds.iti17;

import static junit.framework.Assert.assertEquals;

import java.io.InputStream;


import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.StandardTestContainer;
import org.openehealth.ipf.platform.camel.ihe.xds.iti17.servlet.Iti17Servlet;

/**
 * Tests the ITI-17 transaction with a webservice and client adapter defined via URIs.
 * @author Jens Riemschneider
 */
public class TestIti17 extends StandardTestContainer {

    private static final String SERVICE1 = "xds-iti17://localhost:9091/xds-iti17-service1";
    private static final String SERVICE2 = "xds-iti17://localhost:9091/xds-iti17-service2";
    
    @BeforeClass
    public static void setUp() throws Exception {
        startServer(new Iti17Servlet(), "iti-17.xml");
    }
    
    /** Calls the route attached to the ITI-17 endpoint. 
     * @throws Exception  any exception thrown */
    @Test
    public void testIti17() throws Exception {
        InputStream response1 = (InputStream)getProducerTemplate().requestBody(SERVICE1, "?ok");
        String content1 = IOUtils.toString(response1);
        response1.close();

        assertEquals("service 1: ok", content1);

        InputStream response2 = (InputStream)getProducerTemplate().requestBody(SERVICE2, "?ok");
        String content2 = IOUtils.toString(response2);
        response2.close();

        assertEquals("service 2: ok", content2);
    }
}