/*
 * Copyright 2010 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xcpd;

import org.apache.cxf.helpers.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.openehealth.ipf.commons.core.modules.api.ValidationException;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3ValidationProfiles;
import org.openehealth.ipf.commons.ihe.hl7v3.Hl7v3Validator;

/**
 * 
 * @author Boris Stanoojevic
 */
public class ValidatorTest {
    
    @Test
    public void testValidateSchematronOk() throws Exception {
        String message = IOUtils.readStringFromStream(getClass()
                .getResourceAsStream("/schematron/prpa-valid.xml"));
        Hl7v3Validator validator = new Hl7v3Validator();
        validator.validate(message, Hl7v3ValidationProfiles.getREQUEST_TYPES().get(55));
    }
    
    @Test
    public void testValidateSchematronError() throws Exception {
        String message = IOUtils.readStringFromStream(getClass()
                .getResourceAsStream("/schematron/prpa-invalid.xml"));
        Hl7v3Validator validator = new Hl7v3Validator();
        boolean failed = false;
        try {
            validator.validate(message, Hl7v3ValidationProfiles.getREQUEST_TYPES().get(55));
        } catch (ValidationException e) {
            failed = true;
        }
        Assert.assertTrue(failed);
    }
}
