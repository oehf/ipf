/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.platform.camel.ihe.continua.hrn;

import org.junit.Test;
import org.openehealth.ipf.modules.cda.CDAR2Validator;
import org.openhealthtools.mdht.uml.cda.ClinicalDocument;
import org.openhealthtools.mdht.uml.cda.util.CDAUtil;

import java.io.InputStream;

/**
 * Test whether sample Continua HRN documents can be successfully
 * validated with the MDHT framework.
 * @author Dmytro Rud
 */
public class DocumentsValidationTest {

    @Test
    public void testDocumentsValidation() throws Exception {
        doTest("12AB78.xml");
        // doTest("generatedPHM.xml");
        doTest("HL7_CDA_PHMR_Rich_Rodgers.xml");
    }

    private void doTest(String fileName) throws Exception {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("continua-hrn/" + fileName);
        ClinicalDocument document = CDAUtil.load(stream);
        new CDAR2Validator().validate(document, null);
    }
}
