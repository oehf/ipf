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
package org.openehealth.ipf.platform.camel.ihe.mllp.iti64;

import org.apache.camel.CamelContext;
import org.openehealth.ipf.commons.ihe.hl7v2.atna.iti64.Iti64AuditDataset;
import org.openehealth.ipf.platform.camel.ihe.mllp.core.MllpTransactionComponent;

import static org.openehealth.ipf.commons.ihe.hl7v2.XPID.Interactions.ITI_64;

/**
 * Camel component for ITI-64 (XAD-PID Change Management - XPID).
 *
 * @author Boris Stanojevic
 */
public class Iti64Component extends MllpTransactionComponent<Iti64AuditDataset> {

    public Iti64Component() {
        super(ITI_64);
    }

    public Iti64Component(CamelContext camelContext) {
        super(camelContext, ITI_64);
    }

}
