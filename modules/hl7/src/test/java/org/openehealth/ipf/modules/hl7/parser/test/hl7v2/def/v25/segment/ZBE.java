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
package org.openehealth.ipf.modules.hl7.parser.test.hl7v2.def.v25.segment;

import org.openehealth.ipf.modules.hl7.HL7v2Exception;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.datatype.EI;
import ca.uhn.hl7v2.model.v25.datatype.ST;
import ca.uhn.hl7v2.model.v25.datatype.TS;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * The ZBE segment is intended to be used for information that details ADT
 * movement information. Each ADT event (i.e. admission, discharge, transfer,
 * visit) has a unique identifier to allow for updates at a later point in time.
 * Furthermore, other medical information like diagnoses or documents can refer
 * to this movement using the identifier as reference.
 * <p>
 * The segment is defined by the German Patient Management Profile and extended
 * by the "Historic Movement" option of the IHE ITI Supplement Patient
 * Administration Framework (PAM) Integration Profile.
 * 
 * @author Christian Ohr
 */
@SuppressWarnings("serial")
public class ZBE extends AbstractSegment {

    /**
     * @param parent
     * @param factory
     */
    public ZBE(Group parent, ModelClassFactory factory) {
        super(parent, factory);
        Message message = getMessage();
        try {
            add(EI.class, true, 0, 999, new Object[] { message }, null);
            add(TS.class, true, 1, 26, new Object[] { message }, null);
            add(TS.class, false, 1, 26, new Object[] { message }, null);
            add(ST.class, true, 1, 10, new Object[] { message }, null);
        } catch (HL7Exception he) {
            throw new HL7v2Exception(he);
        }
    }

    /**
     * Returns movement ID (ZBE-1).
     * 
     * @param rep index of repeating field
     * @return movement ID
     */
    public EI getMovementID(int rep) {
        return getTypedField(1, rep);
    }

    /**
     * Returns movement IDs (ZBE-1).
     * 
     * @return movement IDs
     */
    public EI[] getMovementID() {
        return getTypedField(1, new EI[0]);
    }

    /**
     * Returns movement start date (ZBE-2).
     * 
     * @return movement start date (required)
     */
    public TS getStartMovementDateTime() {
        return getTypedField(2, 0);
    }

    /**
     * Returns movement end date (ZBE-3).
     * 
     * @return movement end date (optional)
     */
    public TS getStartMovementEndTime() {
        return getTypedField(3, 0);
    }

    /**
     * Returns movement action (ZBE-4).
     * 
     * @return movement action (required, one of INSERT, DELETE, UPDATE, REFERENCE)
     */
    public ST getAction() {
        return getTypedField(4, 0);
    }

}
