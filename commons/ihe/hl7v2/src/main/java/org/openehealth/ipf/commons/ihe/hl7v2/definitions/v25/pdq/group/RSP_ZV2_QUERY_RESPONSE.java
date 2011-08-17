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
package org.openehealth.ipf.commons.ihe.hl7v2.definitions.v25.pdq.group;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractGroup;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.v25.segment.PD1;
import ca.uhn.hl7v2.model.v25.segment.PID;
import ca.uhn.hl7v2.model.v25.segment.PV1;
import ca.uhn.hl7v2.model.v25.segment.PV2;
import ca.uhn.hl7v2.model.v25.segment.QRI;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import ca.uhn.log.HapiLogFactory;


/**
 * Represents the RSP_ZV2_QUERY_RESPONSE group
 * as described in ITI TF Vol. 2a, pages 219-220.
 * <p/>
 * This Group contains the following elements:<p>
 * 0: PID <br>
 * 1: PD1 <b>optional</b><br>
 * 2: PV1 <br>
 * 3: PV2 <b>optional</b><br>
 * 4: QRI <b>optional</b><br>
 */
@SuppressWarnings("serial")
public class RSP_ZV2_QUERY_RESPONSE extends AbstractGroup {

    /**
     * Creates a new RSP_ZV2_QUERY_RESPONSE Group.
     */
    public RSP_ZV2_QUERY_RESPONSE(Group parent, ModelClassFactory factory) {
        super(parent, factory);
        try {
            add(PID.class, true, false);
            add(PD1.class, false, false);
            add(PV1.class, true, true);
            add(PV2.class, false, false);
            add(QRI.class, false, false);
        } catch (HL7Exception e) {
            HapiLogFactory.getHapiLog(getClass()).error("Unexpected error creating " + getClass().getName(), e);
        }
    }

    /**
     * Returns PID (Patient Identification) - creates it if necessary
     */
    public PID getPID() {
        try {
            return (PID) get("PID");
        } catch (HL7Exception e) {
            HapiLogFactory.getHapiLog(getClass()).error("Unexpected error accessing data", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns PD1 (Patient Additional Demographic) - creates it if necessary
     */
    public PD1 getPD1() {
        try {
            return (PD1) get("PD1");
        } catch (HL7Exception e) {
            HapiLogFactory.getHapiLog(getClass()).error("Unexpected error accessing data", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * Returns PV1 - creates it if necessary
     */
    public PV1 getPV1() {
        try {
            return (PV1) get("PV1");
        } catch (HL7Exception e) {
            HapiLogFactory.getHapiLog(getClass()).error("Unexpected error accessing data", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns PV2 - creates it if necessary
     */
    public PV2 getPV2() {
        try {
            return (PV2) get("PV2");
        } catch (HL7Exception e) {
            HapiLogFactory.getHapiLog(getClass()).error("Unexpected error accessing data", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns QRI (Query Response Instance) - creates it if necessary
     */
    public QRI getQRI() {
        try {
            return (QRI) get("QRI");
        } catch (HL7Exception e) {
            HapiLogFactory.getHapiLog(getClass()).error("Unexpected error accessing data", e);
            throw new RuntimeException(e);
        }
    }
}
