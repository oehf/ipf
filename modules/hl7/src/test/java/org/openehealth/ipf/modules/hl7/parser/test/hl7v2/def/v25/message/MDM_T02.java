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
package org.openehealth.ipf.modules.hl7.parser.test.hl7v2.def.v25.message;

import org.openehealth.ipf.modules.hl7.parser.test.hl7v2.def.v25.segment.ZBE;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import ca.uhn.log.HapiLogFactory;

@SuppressWarnings("serial")
public class MDM_T02 extends ca.uhn.hl7v2.model.v25.message.MDM_T02 {

    public MDM_T02() {
        super();
    }

    public MDM_T02(ModelClassFactory factory) {
        super(factory);
        init(factory);
    }

    /**
     * Add the ZBE segment at the end of the structure
     * 
     * @param factory
     */
    private void init(ModelClassFactory factory) {
        try {
            add(ZBE.class, false, false);
        } catch (HL7Exception e) {
            HapiLogFactory.getHapiLog(this.getClass()).error(
                    "Unexpected error creating message structure", e);
        }
    }

    public ZBE getZBE() {
        try {
            return (ZBE) get("ZBE");
        } catch (HL7Exception e) {
            throw new RuntimeException(e);
        }
    }

}
