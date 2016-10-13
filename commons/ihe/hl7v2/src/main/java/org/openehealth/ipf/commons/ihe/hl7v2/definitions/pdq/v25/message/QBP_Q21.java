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
package org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.message;

import java.util.Map;

import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.segment.ZZI;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.segment.QPD;
import org.openehealth.ipf.modules.hl7.model.AbstractMessage;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.v25.segment.DSC;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.model.v25.segment.RCP;
import ca.uhn.hl7v2.model.v25.segment.SFT;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * <p>
 * Represents a QBP_Q21 message structure (see chapter 3.3.56). This structure
 * contains the following elements:
 * </p>
 * 0: MSH (Message Header) <b></b><br>
 * 1: SFT (Software Segment) <b>optional repeating</b><br>
 * 2: QPD (Query Parameter Definition) <b></b><br>
 * 3: RCP (Response Control Parameter) <b></b><br>
 * 4: DSC (Continuation Pointer) <b>optional </b><br>
 */
@SuppressWarnings("serial")
public class QBP_Q21 extends AbstractMessage {

    /**
     * Creates a new QBP_Q21 Group with custom ModelClassFactory.
     */
    public QBP_Q21(ModelClassFactory factory) {
        super(factory);
    }

    /**
     * Creates a new QBP_Q21 Group with DefaultModelClassFactory.
     */
    public QBP_Q21() {
        super();
    }

    @Override
    protected Map<Class<? extends Structure>, Cardinality> structures(
            Map<Class<? extends Structure>, Cardinality> s) {
        s.put(MSH.class, Cardinality.REQUIRED);
        s.put(SFT.class, Cardinality.OPTIONAL_REPEATING);
        s.put(QPD.class, Cardinality.REQUIRED);
        s.put(RCP.class, Cardinality.REQUIRED);
        s.put(DSC.class, Cardinality.OPTIONAL);
        s.put(ZZI.class, Cardinality.OPTIONAL);
        return s;
    }

    public String getVersion() {
        return "2.5";
    }

    /**
     * Returns MSH (Message Header) - creates it if necessary
     */
    public MSH getMSH() {
        return getTyped("MSH", MSH.class);
    }

    /**
     * Returns first repetition of SFT (Software Segment) - creates it if
     * necessary
     */
    public SFT getSFT() {
        return getTyped("SFT", SFT.class);
    }

    /**
     * Returns all repetitions of SFT
     */
    public java.util.List<SFT> getSFTAll() throws HL7Exception {
        return getAllAsList("SFT", SFT.class);
    }

    /**
     * Returns a specific repetition of SFT (Software Segment) - creates it if
     * necessary throws HL7Exception if the repetition requested is more than
     * one greater than the number of existing repetitions.
     */
    public SFT getSFT(int rep) throws HL7Exception {
        return getTyped("SFT", rep, SFT.class);
    }

    /**
     * Returns the number of existing repetitions of SFT
     */
    public int getSFTReps() {
        return getReps("SFT");
    }

    /**
     * Returns QPD (Query Parameter Definition) - creates it if necessary
     */
    public QPD getQPD() {
        return getTyped("QPD", QPD.class);
    }

    /**
     * Returns RCP (Response Control Parameter) - creates it if necessary
     */
    public RCP getRCP() {
        return getTyped("RCP", RCP.class);
    }

    /**
     * Returns DSC (Continuation Pointer) - creates it if necessary
     */
    public DSC getDSC() {
        return getTyped("DSC", DSC.class);

    }

}