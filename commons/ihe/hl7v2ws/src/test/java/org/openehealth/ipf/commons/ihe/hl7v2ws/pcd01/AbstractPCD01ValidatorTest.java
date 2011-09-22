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

import org.junit.Before;
import org.openehealth.ipf.modules.hl7dsl.MessageAdapter;

import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;

/**
 * @author Mitko Kolev
 *
 */
public abstract class AbstractPCD01ValidatorTest {

    protected MessageAdapter<ORU_R01> maximumMessage;
    
    private Pcd01Validator validator = new Pcd01Validator();

    public Pcd01Validator getValiadtor(){
        return validator;
    }
    
    @Before
    public void setUp(){
        maximumMessage = load("pcd01/valid-pcd01-MaximumRequest2.hl7");
    }
    
    protected MessageAdapter<ORU_R01> maxMsgReplace(String whatToReplace, String replacement){
        return make(maximumMessage.toString().replace(whatToReplace, replacement));
     }
    protected <T extends AbstractMessage> void validate (MessageAdapter<T> message){
        getValiadtor().validate(message);
     }   
    
}
