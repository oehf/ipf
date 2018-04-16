/*
 * Copyright 2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xacml20.chppq;

import lombok.extern.slf4j.Slf4j;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20MessageValidator;
import org.openehealth.ipf.commons.ihe.xacml20.Xacml20Utils;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

/**
 * @author Dmytro Rud
 */
@Slf4j
public class MessageValidationTest {

    private final Xacml20MessageValidator validator = new Xacml20MessageValidator();

    @BeforeClass
    public static void beforeClass() {
        Xacml20Utils.initializeHerasaf();
    }

    private static <T> T loadFile(String fn) throws Exception {
        InputStream stream = MessageValidationTest.class.getClassLoader().getResourceAsStream("messages/chppq/" + fn);
        Unmarshaller unmarshaller = Xacml20Utils.JAXB_CONTEXT.createUnmarshaller();
        Object object = unmarshaller.unmarshal(stream);
        if (object instanceof JAXBElement) {
            object = ((JAXBElement) object).getValue();
        }
        log.debug("File {} --> object {}", fn, object.getClass().getSimpleName());
        return (T) object;
    }

    @Test
    public void testValidation() throws Exception {
        validator.validateRequest(loadFile("query-per-patient-id.xml"));
        validator.validateRequest(loadFile("query-per-policy-id.xml"));
        validator.validateRequest(loadFile("add-request.xml"));
        validator.validateRequest(loadFile("update-request.xml"));
        validator.validateRequest(loadFile("delete-request.xml"));

        validator.validateResponse(loadFile("query-response.xml"));
        validator.validateResponse(loadFile("ack.xml"));
    }
}

