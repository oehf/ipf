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
package org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.message;

import java.util.Map;

import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.segment.QPD;
import org.openehealth.ipf.modules.hl7.model.AbstractMessage;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.v25.group.RSP_K21_QUERY_RESPONSE;
import ca.uhn.hl7v2.model.v25.segment.DSC;
import ca.uhn.hl7v2.model.v25.segment.ERR;
import ca.uhn.hl7v2.model.v25.segment.MSA;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.model.v25.segment.QAK;
import ca.uhn.hl7v2.model.v25.segment.SFT;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * <p>Represents a RSP_K21 message structure (see chapter 3.3.56). This structure contains the
 * following elements: </p>
 * 0: MSH (Message Header) <b></b><br>
 * 1: SFT (Software Segment) <b>optional repeating</b><br>
 * 2: MSA (Message Acknowledgment) <b></b><br>
 * 3: ERR (Error) <b>optional </b><br>
 * 4: QAK (Query Acknowledgment) <b></b><br>
 * 5: QPD (Query Parameter Definition) <b></b><br>
 * 6: RSP_K21_QUERY_RESPONSE (a Group object) <b>optional repeating</b><br>
 * 7: DSC (Continuation Pointer) <b>optional </b><br>
 */
@SuppressWarnings("serial")
public class RSP_K21 extends AbstractMessage {

    /**
     * Creates a new RSP_K21 Group with custom ModelClassFactory.
     */
    public RSP_K21(ModelClassFactory factory) {
        super(factory);
    }

    /**
     * Creates a new RSP_K21 Group with DefaultModelClassFactory.
     */
    public RSP_K21() {
        super();
    }


    @Override
    protected Map<Class<? extends Structure>, Cardinality> structures(
             Map<Class<? extends Structure>, Cardinality> s) {
        s.put(MSH.class, Cardinality.REQUIRED);
        s.put(SFT.class, Cardinality.OPTIONAL_REPEATING);
        s.put(MSA.class, Cardinality.REQUIRED);
        s.put(ERR.class, Cardinality.OPTIONAL);
        s.put(QAK.class, Cardinality.REQUIRED);
        s.put(QPD.class, Cardinality.REQUIRED);
        s.put(RSP_K21_QUERY_RESPONSE.class, Cardinality.OPTIONAL_REPEATING);
        s.put(DSC.class, Cardinality.OPTIONAL);
        return s;
    }


    /**
      * Returns MSH (Message Header) - creates it if necessary
      */
    public MSH getMSH() {
        return get(MSH.class);
    }

    /**
      * Returns  first repetition of SFT (Software Segment) - creates it if necessary
      */
    public SFT getSFT() {
        return get(SFT.class);
    }

    /**
      * Returns a specific repetition of SFT
      * (Software Segment) - creates it if necessary
      * throws HL7Exception if the repetition requested is more than one
      * greater than the number of existing repetitions.
      */
    public SFT getSFT(int rep) throws HL7Exception {
        return get(SFT.class, rep);
    }

    /**
      * Returns the number of existing repetitions of SFT
      */
    public int getSFTReps() {
        return getReps(SFT.class);
    }

    /**
      * Returns MSA (Message Acknowledgment) - creates it if necessary
      */
    public MSA getMSA() {
        return get(MSA.class);
    }

    /**
      * Returns ERR (Error) - creates it if necessary
      */
    public ERR getERR() {
        return get(ERR.class);
    }

    /**
      * Returns QAK (Query Acknowledgment) - creates it if necessary
      */
    public QAK getQAK() {
        return get(QAK.class);
    }

    /**
      * Returns QPD (Query Parameter Definition) - creates it if necessary
      */
    public QPD getQPD() {
        return get(QPD.class);
    }

    /**
      * Returns  first repetition of RSP_K21_QUERY_RESPONSE (a Group object) - creates it if necessary
      */
    public RSP_K21_QUERY_RESPONSE getQUERY_RESPONSE() {
    	return get("QUERY_RESPONSE", RSP_K21_QUERY_RESPONSE.class, 0);
    }

    /**
      * Returns a specific repetition of RSP_K21_QUERY_RESPONSE
      * (a Group object) - creates it if necessary
      * throws HL7Exception if the repetition requested is more than one
      * greater than the number of existing repetitions.
      */
    public RSP_K21_QUERY_RESPONSE getQUERY_RESPONSE(int rep) throws HL7Exception {
        return get("QUERY_RESPONSE", RSP_K21_QUERY_RESPONSE.class, rep);
    }

    /**
      * Returns the number of existing repetitions of RSP_K21_QUERY_RESPONSE
      */
    public int getQUERY_RESPONSEReps() {
        return getReps("QUERY_RESPONSE", RSP_K21_QUERY_RESPONSE.class);
    }

    /**
      * Returns DSC (Continuation Pointer) - creates it if necessary
      */
    public DSC getDSC() {
        return get(DSC.class);
    }

}
