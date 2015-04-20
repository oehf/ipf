/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.hl7v2.definitions.pam.v25.message;

import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.v25.group.ADT_A01_INSURANCE;
import ca.uhn.hl7v2.model.v25.group.ADT_A01_PROCEDURE;
import ca.uhn.hl7v2.model.v25.segment.*;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pam.v25.segment.ZBE;
import org.openehealth.ipf.modules.hl7.model.AbstractMessage;

import java.util.Map;

/**
 *
 */
public class ADT_A16 extends AbstractMessage {

    public ADT_A16() {
        this(new DefaultModelClassFactory());
    }

    public ADT_A16(ModelClassFactory factory) {
        super(factory);
    }

    @Override
    protected Map<Class<? extends Structure>, Cardinality> structures(Map<Class<? extends Structure>, Cardinality> s) {
        s.put(MSH.class, Cardinality.REQUIRED);
        s.put(SFT.class, Cardinality.OPTIONAL_REPEATING);
        s.put(EVN.class, Cardinality.REQUIRED);
        s.put(PID.class, Cardinality.REQUIRED);
        s.put(PD1.class, Cardinality.OPTIONAL);
        s.put(ROL.class, Cardinality.OPTIONAL_REPEATING);
        s.put(NK1.class, Cardinality.OPTIONAL_REPEATING);
        s.put(PV1.class, Cardinality.REQUIRED);
        s.put(PV2.class, Cardinality.OPTIONAL);
        s.put(ZBE.class, Cardinality.OPTIONAL);
        s.put(ROL.class, Cardinality.OPTIONAL_REPEATING);
        s.put(DB1.class, Cardinality.OPTIONAL_REPEATING);
        s.put(OBX.class, Cardinality.OPTIONAL_REPEATING);
        s.put(AL1.class, Cardinality.OPTIONAL_REPEATING);
        s.put(DG1.class, Cardinality.OPTIONAL_REPEATING);
        s.put(DRG.class, Cardinality.OPTIONAL);
        s.put(ADT_A01_PROCEDURE.class, Cardinality.OPTIONAL_REPEATING);
        s.put(GT1.class, Cardinality.OPTIONAL_REPEATING);
        s.put(ADT_A01_INSURANCE.class, Cardinality.OPTIONAL_REPEATING);
        s.put(ACC.class, Cardinality.OPTIONAL);
        return s;
    }

}
