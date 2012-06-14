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

import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.group.RSP_ZV2_QUERY_RESPONSE;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.segment.QPD;
import org.openehealth.ipf.modules.hl7.model.AbstractMessage;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.v25.segment.*;
import ca.uhn.hl7v2.parser.ModelClassFactory;

/**
 * Represents a RSP_ZV2 message structure as described
 * in ITI TF Vol. 2a, pp. 219-220.
 * <p>
 * This structure contains the following elements: </p>
 * 0: MSH (Message Header) <br>
 * 1: MSA (Message Acknowledgment) <br>
 * 2: ERR (Error) <b>optional</b><br>
 * 3: QAK (Query Acknowledgment) <br>
 * 4: QPD (Query Parameter Definition) <br>
 * 5: RSP_ZV2_QUERY_RESPONSE (a Group object) <b>optional repeating</b><br>
 * 6: DSC (Continuation Pointer) <b>optional</b><br>
 */
@SuppressWarnings("serial")
public class RSP_ZV2 extends AbstractMessage {

    /**
     * Creates a new RSP_ZV2 Group with custom ModelClassFactory.
     */
    public RSP_ZV2(ModelClassFactory factory) {
        super(factory);
    }

    /**
     * Creates a new RSP_ZV2 Group with DefaultModelClassFactory.
     */
    public RSP_ZV2() {
        super();
    }

    @Override
    protected Map<Class<? extends Structure>, Cardinality> structures(
             Map<Class<? extends Structure>, Cardinality> s) {
        s.put(MSH.class, Cardinality.REQUIRED);
        s.put(MSA.class, Cardinality.REQUIRED);
        s.put(ERR.class, Cardinality.OPTIONAL);
        s.put(QAK.class, Cardinality.REQUIRED);
        s.put(QPD.class, Cardinality.REQUIRED);
        s.put(RSP_ZV2_QUERY_RESPONSE.class, Cardinality.OPTIONAL_REPEATING);
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
      * Returns  first repetition of RSP_ZV2_QUERY_RESPONSE (a Group object) - creates it if necessary
      */
    public RSP_ZV2_QUERY_RESPONSE getQUERY_RESPONSE() {
        return get("QUERY_RESPONSE", RSP_ZV2_QUERY_RESPONSE.class);
    }

    /**
      * Returns a specific repetition of RSP_ZV2_QUERY_RESPONSE
      * (a Group object) - creates it if necessary
      * throws HL7Exception if the repetition requested is more than one
      * greater than the number of existing repetitions.
      */
    public RSP_ZV2_QUERY_RESPONSE getQUERY_RESPONSE(int rep) throws HL7Exception {
        return get("QUERY_RESPONSE", RSP_ZV2_QUERY_RESPONSE.class, rep);
    }

    /**
      * Returns the number of existing repetitions of RSP_ZV2_QUERY_RESPONSE
      */
    public int getQUERY_RESPONSEReps() {
        return getReps("QUERY_RESPONSE", RSP_ZV2_QUERY_RESPONSE.class);
    }

    /**
      * Returns DSC (Continuation Pointer) - creates it if necessary
      */
    public DSC getDSC() {
        return get(DSC.class);
    }
}
