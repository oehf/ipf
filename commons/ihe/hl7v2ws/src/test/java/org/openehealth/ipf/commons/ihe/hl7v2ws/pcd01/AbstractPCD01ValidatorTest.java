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
package org.openehealth.ipf.commons.ihe.hl7v2ws.pcd01;

import java.util.Scanner;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.validation.Validator;
import ca.uhn.hl7v2.validation.impl.SimpleValidationExceptionHandler;
import org.junit.Before;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory;
import org.openehealth.ipf.gazelle.validation.profile.PcdTransactions;

public abstract class AbstractPCD01ValidatorTest {

    private HapiContext hapiContext = HapiContextFactory.createHapiContext(PcdTransactions.PCD1);

    protected ORU_R01 maximumMessage;


    public Validator getValidator() {
        return hapiContext.getMessageValidator();
    }

    public Parser getParser() {
        return hapiContext.getPipeParser();
    }

    @Before
    public void setUp() throws HL7Exception {
        maximumMessage = load(getParser(), "pcd01/valid-pcd01-MaximumRequest2.hl7");
    }

    protected <T extends AbstractMessage> T maxMsgReplace(String whatToReplace, String replacement) throws HL7Exception {
        return (T)getParser().parse(maximumMessage.toString().replace(whatToReplace, replacement));
    }

    protected <T extends AbstractMessage> void validate(Message message) throws HL7Exception {
        SimpleValidationExceptionHandler handler = new SimpleValidationExceptionHandler(hapiContext);
        getValidator().validate(message, handler);
        if (handler.hasFailed()) throw new HL7Exception("Validation has failed", handler.getExceptions().get(0));
    }

    protected static <T extends Message> T load(Parser parser, String fileName) throws HL7Exception {
        return (T)parser.parse(
                new Scanner(AbstractPCD01ValidatorTest.class.getResourceAsStream("/" + fileName)).useDelimiter("\\A").next());
    }
}
