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
package org.openehealth.ipf.commons.ihe.hl7v2.definitions.pam.v25.segment;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v25.datatype.*;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import org.openehealth.ipf.modules.hl7.HL7v2Exception;

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
            add(EI.class, true, 0, 427, new Object[] { message }, "Movement ID");
            add(TS.class, true, 1, 26, new Object[] { message }, "Start Movement Date/Time");
            add(TS.class, false, 1, 26, new Object[] { message }, "End Movement Date/Time");
            add(ST.class, true, 1, 6, new Object[] { message }, "Movement Action");
            add(ID.class, true, 1, 1, new Object[] { message }, "Historical Movement Indicator");
            add(ID.class, false, 1, 3, new Object[] { message }, "Original trigger event code");
            add(XON.class, false, 1, 567, new Object[] { message }, "Responsible Ward");
            add(XON.class, false, 1, 567, new Object[] { message }, "Responsible Nursing Ward");
            add(CWE.class, false, 1, 3, new Object[] { message }, "Movement Scope");
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
    public ST getMovementAction() {
        return getTypedField(4, 0);
    }

    /**
     * Returns historical movement indicator (ZBE-5).
     *
     * @return historical movement indicator (required, one of Y, N)
     */
    public ID getHistoricalMovementIndicator() {
        return getTypedField(5, 0);
    }

    /**
     * Returns original trigger event code (ZBE-6). In the case of an UPDATE of the movement
     * (trigger A08), this field conveys the original trigger event that was sent with the INSERT
     *
     * @return original trigger event code
     */
    public ID getOriginalTriggerEventCode() {
        return getTypedField(6, 0);
    }

    /**
     * Returns responsible ward (ZBE-7).
     * This is Medical or Nursing Ward, depending of the trigger event of the message.
     * If ZBE-8 exists, then ZBE-7 shall be interpreted as the Responsible Medical Ward.
     *
     * @return responsible ward
     */
    public XON getResponsibleWard() {
        return getTypedField(7, 0);
    }

    /**
     * Returns responsible nursing ward (ZBE-8).
     * If ZBE-8 exists, then ZBE-7 shall be interpreted as the Responsible Medical Ward.
     *
     * @return responsible nursing ward
     */
    public XON getResponsibleNursingWard() {
        return getTypedField(8, 0);
    }

    /**
     * Returns movement scope (ZBE-9).
     *
     * @return movement scope
     */
    public CWE getMovementScope() {
        return getTypedField(9, 0);
    }
}
