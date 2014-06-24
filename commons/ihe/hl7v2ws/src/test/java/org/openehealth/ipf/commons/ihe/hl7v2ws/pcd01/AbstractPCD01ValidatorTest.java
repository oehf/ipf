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

import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.load;
import static org.openehealth.ipf.modules.hl7dsl.MessageAdapters.make;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.validation.DefaultValidationExceptionHandler;
import ca.uhn.hl7v2.validation.ReportingValidationExceptionHandler;
import ca.uhn.hl7v2.validation.ValidationExceptionHandler;
import ca.uhn.hl7v2.validation.Validator;
import ca.uhn.hl7v2.validation.impl.SimpleValidationExceptionHandler;
import org.junit.Before;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.HapiContextFactory;
import org.openehealth.ipf.gazelle.validation.profile.PcdTransactions;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;

import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;

/**
 * @author Mitko Kolev
 */
public abstract class AbstractPCD01ValidatorTest {

    private HapiContext hapiContext = HapiContextFactory.createHapiContext(PcdTransactions.PCD1);
    private Parser p = hapiContext.getPipeParser();

    protected MessageAdapter<ORU_R01> maximumMessage;


    public Validator getValidator() {
        return hapiContext.getMessageValidator();
    }

    public Parser getParser() {
        return hapiContext.getPipeParser();
    }

    @Before
    public void setUp() {
        maximumMessage = load(getParser(), "pcd01/valid-pcd01-MaximumRequest2.hl7");
    }

    protected MessageAdapter<ORU_R01> maxMsgReplace(String whatToReplace, String replacement) {
        return make(getParser(), maximumMessage.toString().replace(whatToReplace, replacement));
    }

    protected <T extends AbstractMessage> void validate(MessageAdapter<T> message) throws HL7Exception {
        SimpleValidationExceptionHandler handler = new SimpleValidationExceptionHandler(hapiContext);
        getValidator().validate(message.getHapiMessage(), handler);
        if (handler.hasFailed()) throw new HL7Exception("Validation has failed", handler.getExceptions().get(0));
    }

}
