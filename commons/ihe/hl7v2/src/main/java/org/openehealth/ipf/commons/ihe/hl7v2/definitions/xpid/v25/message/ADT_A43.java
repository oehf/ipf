/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.hl7v2.definitions.xpid.v25.message;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.v25.group.ADT_A43_PATIENT;
import ca.uhn.hl7v2.model.v25.segment.EVN;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.model.v25.segment.SFT;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import org.openehealth.ipf.commons.ihe.hl7v2.definitions.xpid.v25.segment.ZZI;
import org.openehealth.ipf.modules.hl7.model.AbstractMessage;

import java.util.List;
import java.util.Map;

/**
 * Custom ADT_A39 structure that renames the PATIENT group
 */
public class ADT_A43 extends AbstractMessage {

    public ADT_A43() {
        super();
    }

    public ADT_A43(ModelClassFactory factory) {
        super(factory);
    }

    @Override
    protected Map<Class<? extends Structure>, Cardinality> structures(Map<Class<? extends Structure>, Cardinality> s) {
        s.put(MSH.class, Cardinality.REQUIRED);
        s.put(SFT.class, Cardinality.OPTIONAL_REPEATING);
        s.put(EVN.class, Cardinality.REQUIRED);
        s.put(ADT_A43_PATIENT.class, Cardinality.REQUIRED_REPEATING);
        s.put(ZZI.class, Cardinality.OPTIONAL);
        return s;
    }

    public String getVersion() {
        return "2.5";
    }

    public MSH getMSH() {
        return getTyped("MSH", MSH.class);
    }

    public EVN getEVN() {
        return getTyped("EVN", EVN.class);
    }

    /**
     * Returns a specific repetition of SFT
     * (Software Segment) - creates it if necessary
     * throws HL7Exception if the repetition requested is more than one
     * greater than the number of existing repetitions.
     */
    public SFT getSFT(int rep) {
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

    public ADT_A43_PATIENT getPATIENT() {
        return this.getTyped("PATIENT", ADT_A43_PATIENT.class);
    }

    public ADT_A43_PATIENT getPATIENT(int rep) {
        return this.getTyped("PATIENT", rep, ADT_A43_PATIENT.class);
    }

    public int getPATIENTReps() {
        return this.getReps("PATIENT");
    }

    public List<ADT_A43_PATIENT> getPATIENTAll() throws HL7Exception {
        return this.getAllAsList("PATIENT", ADT_A43_PATIENT.class);
    }

    public void insertPATIENT(ADT_A43_PATIENT structure, int rep) throws HL7Exception {
        super.insertRepetition("PATIENT", structure, rep);
    }

    public ADT_A43_PATIENT insertPATIENT(int rep) throws HL7Exception {
        return (ADT_A43_PATIENT)super.insertRepetition("PATIENT", rep);
    }

    public ADT_A43_PATIENT removePATIENT(int rep) throws HL7Exception {
        return (ADT_A43_PATIENT)super.removeRepetition("PATIENT", rep);
    }
}
