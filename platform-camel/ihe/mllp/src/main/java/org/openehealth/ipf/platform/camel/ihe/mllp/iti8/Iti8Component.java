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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti8;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti8.Iti8AuditDataset;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionComponent;

import static org.openehealth.ipf.commons.ihe.hl7v2.PIX.Interactions.ITI_8_PIX;

/**
 * Camel component for ITI-8 (PIX Feed).
 * @author Dmytro Rud
 */
public class Iti8Component extends MllpTransactionComponent<Iti8AuditDataset> {

    
    public Iti8Component() {
        super(ITI_8_PIX);
    }

    public Iti8Component(CamelContext camelContext) {
        super(camelContext, ITI_8_PIX);
    }

}
