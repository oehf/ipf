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
package org.openehealth.ipf.platform.camel.cda.extend;

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import java.io.InputStream;

/**
 * @author Christian Ohr
 */
@ContextConfiguration(locations = { "/config/context-extend.xml" })
public class CDAModelExtensionTest extends AbstractExtensionTest {

    private final String cdaExample = "/message/SampleCDADocument.xml";
    private final String ccdExample = "/message/SampleCCDDocument.xml";

    @EndpointInject(value="mock:error")
    protected MockEndpoint mockError;
    
    @Test
    public void testXsdValidateSuccess() throws Exception {
        testValidateCDA("direct:input3", cdaExample);
        testValidateCDA("direct:input3", ccdExample);
    }
    
    @Test
    public void testSchematronValidateSuccess() throws Exception {
        testValidateCDA("direct:input4", ccdExample);
    }    

    
    private void testValidateCDA(String endpoint, String file) throws Exception {
        mockOutput.reset();
        mockError.reset();
        var stream = inputStream(file);
        mockOutput.expectedMessageCount(1);
        mockError.expectedMessageCount(0);
        producerTemplate.sendBody(endpoint, stream);
        mockOutput.assertIsSatisfied();
        mockError.assertIsSatisfied();
    }
    
    private static InputStream inputStream(String resource) {
        return CDAModelExtensionTest.class.getResourceAsStream(resource);
    }
    
}
