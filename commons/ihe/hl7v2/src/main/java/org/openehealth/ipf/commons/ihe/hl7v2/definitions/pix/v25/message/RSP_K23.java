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
package org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.message;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.v25.group.RSP_K23_QUERY_RESPONSE;
import ca.uhn.hl7v2.model.v25.segment.*;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.segment.QPD;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.segment.ZZI;
import org.openehealth.ipf.modules.hl7.model.AbstractMessage;

import java.util.Map;

/**
 * Adds a custom QPD segment instead of the standard one
 * coming from v25 hapi package
 *
 * @see ca.uhn.hl7v2.model.v25.message.RSP_K23
 * @see org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.segment.QPD
 */
@SuppressWarnings("serial")
public class RSP_K23 extends AbstractMessage {
    /**
      * Creates a new RSP_K23 Group with custom ModelClassFactory.
      */
    public RSP_K23(ModelClassFactory factory) {
        super(factory);
    }

    /**
      * Creates a new RSP_K23 Group with DefaultModelClassFactory.
      */
    public RSP_K23() {
        super();
    }

    @Override
    protected Map<Class<? extends Structure>, Cardinality> structures(
              Map<Class<? extends Structure>, Cardinality> s)
    {
        s.put(MSH.class, Cardinality.REQUIRED);
        s.put(SFT.class, Cardinality.OPTIONAL_REPEATING);
        s.put(MSA.class, Cardinality.REQUIRED);
        s.put(ERR.class, Cardinality.OPTIONAL);
        s.put(QAK.class, Cardinality.REQUIRED);
        s.put(QPD.class, Cardinality.REQUIRED);
        s.put(RSP_K23_QUERY_RESPONSE.class, Cardinality.OPTIONAL);
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
      * Returns  first repetition of SFT (Software Segment) - creates it if necessary
      */
    public SFT getSFT() {
        return getTyped("SFT", SFT.class);
    }

    /**
      * Returns a specific repetition of SFT
      * (Software Segment) - creates it if necessary
      * throws HL7Exception if the repetition requested is more than one
      * greater than the number of existing repetitions.
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
     * Returns all repetitions of SFT
     */
    public java.util.List<SFT> getSFTAll() throws HL7Exception {
        return getAllAsList("SFT", SFT.class);
    }

    /**
      * Returns MSA (Message Acknowledgment) - creates it if necessary
      */
    public MSA getMSA() {
        return getTyped("MSA", MSA.class);
    }

    /**
      * Returns ERR (Error) - creates it if necessary
      */
    public ERR getERR() {
        return getTyped("ERR", ERR.class);
    }

    /**
      * Returns QAK (Query Acknowledgment) - creates it if necessary
      */
    public QAK getQAK() {
        return getTyped("QAK", QAK.class);
    }

    /**
      * Returns QPD (Query Parameter Definition) - creates it if necessary
      */
    public QPD getQPD() {
        return getTyped("QPD", QPD.class);
    }

    /**
      * Returns RSP_K23_QUERY_RESPONSE (a Group object) - creates it if necessary
      */
    public RSP_K23_QUERY_RESPONSE getQUERY_RESPONSE() {
        return getTyped("QUERY_RESPONSE", RSP_K23_QUERY_RESPONSE.class);
    }

    /**
      * Returns DSC (Continuation Pointer) - creates it if necessary
      */
    public DSC getDSC() {
        return getTyped("DSC", DSC.class);
    }

}
