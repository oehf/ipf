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

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.v25.group.RSP_K21_QUERY_RESPONSE;
import ca.uhn.hl7v2.model.v25.segment.*;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.segment.QPD;
import org.openehealth.ipf.modules.hl7.model.AbstractMessage;

import java.util.Map;

/**
 * Adds a custom QPD segment instead of the standard one
 * coming from v25 hapi package
 *
 * @see ca.uhn.hl7v2.model.v25.message.RSP_K21
 * @see org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.segment.QPD
 */
@SuppressWarnings("serial")
public class RSP_K21 extends AbstractMessage {

    /**
     * Creates a new RSP_K21 Group with custom ModelClassFactory.
     */
    public RSP_K21(ModelClassFactory factory) {
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

    @Override
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
     * Returns a specific repetition of SFT (Software Segment) - creates it if
     * necessary throws HL7Exception if the repetition requested is more than
     * one greater than the number of existing repetitions.
     */
    public SFT getSFT(int rep) {
        return getTyped("STF", rep, SFT.class);
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
     * Returns first repetition of RSP_K21_QUERY_RESPONSE (a Group object) -
     * creates it if necessary
     */
    public RSP_K21_QUERY_RESPONSE getQUERY_RESPONSE() {
        return getTyped("QUERY_RESPONSE", 0, RSP_K21_QUERY_RESPONSE.class);
    }

    /**
     * Returns a specific repetition of RSP_K21_QUERY_RESPONSE (a Group object)
     * - creates it if necessary throws HL7Exception if the repetition requested
     * is more than one greater than the number of existing repetitions.
     */
    public RSP_K21_QUERY_RESPONSE getQUERY_RESPONSE(int rep) {
        return getTyped("QUERY_RESPONSE", rep, RSP_K21_QUERY_RESPONSE.class);
    }

    /**
     * Returns the number of existing repetitions of RSP_K21_QUERY_RESPONSE
     */
    public int getQUERY_RESPONSEReps() {
        return getReps("QUERY_RESPONSE");
    }

    /**
     * Returns all repetitions of RSP_K21_QUERY_RESPONSE
     */
    public java.util.List<RSP_K21_QUERY_RESPONSE> getQUERY_RESPONSEAll() throws HL7Exception {
        return getAllAsList("QUERY_RESPONSE", RSP_K21_QUERY_RESPONSE.class);
    }

    /**
     * Returns DSC (Continuation Pointer) - creates it if necessary
     */
    public DSC getDSC() {
        return getTyped("DSC", DSC.class);
    }

}
