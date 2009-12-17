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
package org.openehealth.ipf.commons.ihe.transform.core.definitions.v25.pdq.message;

import org.openehealth.ipf.commons.ihe.transform.core.definitions.v25.pdq.group.RSP_ZV2_QUERY_RESPONSE;
import org.openehealth.ipf.commons.ihe.transform.core.definitions.v25.pdq.segment.QPD;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v25.segment.DSC;
import ca.uhn.hl7v2.model.v25.segment.ERR;
import ca.uhn.hl7v2.model.v25.segment.MSA;
import ca.uhn.hl7v2.model.v25.segment.MSH;
import ca.uhn.hl7v2.model.v25.segment.QAK;
import ca.uhn.hl7v2.parser.DefaultModelClassFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import ca.uhn.log.HapiLogFactory;

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
       init(factory);
    }

    /**
     * Creates a new RSP_ZV2 Group with DefaultModelClassFactory. 
     */ 
    public RSP_ZV2() { 
       super(new DefaultModelClassFactory());
       init(new DefaultModelClassFactory());
    }

    private void init(ModelClassFactory factory) {
       try {
          this.add(MSH.class, true,  false);
          this.add(MSA.class, true,  false);
          this.add(ERR.class, false, false);
          this.add(QAK.class, true,  false);
          this.add(QPD.class, true,  false);
          this.add(RSP_ZV2_QUERY_RESPONSE.class, false, true);
          this.add(DSC.class, false, false);
       } catch(HL7Exception e) {
          HapiLogFactory.getHapiLog(this.getClass()).error("Unexpected error creating RSP_K21 - this is probably a bug in the source code generator.", e);
       }
    }

    /**
     * Returns MSH (Message Header) - creates it if necessary
     */
    public MSH getMSH() { 
       MSH ret = null;
       try {
          ret = (MSH)this.get("MSH");
       } catch(HL7Exception e) {
          HapiLogFactory.getHapiLog(this.getClass()).error("Unexpected error accessing data - this is probably a bug in the source code generator.", e);
          throw new RuntimeException(e);
       }
       return ret;
    }

    /**
     * Returns MSA (Message Acknowledgment) - creates it if necessary
     */
    public MSA getMSA() { 
       MSA ret = null;
       try {
          ret = (MSA)this.get("MSA");
       } catch(HL7Exception e) {
          HapiLogFactory.getHapiLog(this.getClass()).error("Unexpected error accessing data - this is probably a bug in the source code generator.", e);
          throw new RuntimeException(e);
       }
       return ret;
    }

    /**
     * Returns ERR (Error) - creates it if necessary
     */
    public ERR getERR() { 
       ERR ret = null;
       try {
          ret = (ERR)this.get("ERR");
       } catch(HL7Exception e) {
          HapiLogFactory.getHapiLog(this.getClass()).error("Unexpected error accessing data - this is probably a bug in the source code generator.", e);
          throw new RuntimeException(e);
       }
       return ret;
    }

    /**
     * Returns QAK (Query Acknowledgment) - creates it if necessary
     */
    public QAK getQAK() { 
       QAK ret = null;
       try {
          ret = (QAK)this.get("QAK");
       } catch(HL7Exception e) {
          HapiLogFactory.getHapiLog(this.getClass()).error("Unexpected error accessing data - this is probably a bug in the source code generator.", e);
          throw new RuntimeException(e);
       }
       return ret;
    }

    /**
     * Returns QPD (Query Parameter Definition) - creates it if necessary
     */
    public QPD getQPD() { 
       QPD ret = null;
       try {
          ret = (QPD)this.get("QPD");
       } catch(HL7Exception e) {
          HapiLogFactory.getHapiLog(this.getClass()).error("Unexpected error accessing data - this is probably a bug in the source code generator.", e);
          throw new RuntimeException(e);
       }
       return ret;
    }

    /**
     * Returns first repetition of RSP_ZV2_QUERY_RESPONSE (a Group object) - creates it if necessary
     */
    public RSP_ZV2_QUERY_RESPONSE getQUERY_RESPONSE() { 
       RSP_ZV2_QUERY_RESPONSE ret = null;
       try {
          ret = (RSP_ZV2_QUERY_RESPONSE)this.get("QUERY_RESPONSE");
       } catch(HL7Exception e) {
          HapiLogFactory.getHapiLog(this.getClass()).error("Unexpected error accessing data - this is probably a bug in the source code generator.", e);
          throw new RuntimeException(e);
       }
       return ret;
    }

    /**
     * Returns a specific repetition of RSP_ZV2_QUERY_RESPONSE
     * (a Group object) - creates it if necessary
     * throws HL7Exception if the repetition requested is more than one 
     *     greater than the number of existing repetitions.
     */
    public RSP_ZV2_QUERY_RESPONSE getQUERY_RESPONSE(int rep) throws HL7Exception { 
       return (RSP_ZV2_QUERY_RESPONSE)this.get("QUERY_RESPONSE", rep);
    }

    /** 
     * Returns the number of existing repetitions of RSP_ZV2_QUERY_RESPONSE 
     */ 
    public int getQUERY_RESPONSEReps() { 
        int reps = -1; 
        try { 
            reps = this.getAll("QUERY_RESPONSE").length; 
        } catch (HL7Exception e) { 
            String message = "Unexpected error accessing data - this is probably a bug in the source code generator."; 
            HapiLogFactory.getHapiLog(this.getClass()).error(message, e); 
            throw new RuntimeException(message);
        } 
        return reps; 
    } 

    /**
     * Returns DSC (Continuation Pointer) - creates it if necessary
     */
    public DSC getDSC() { 
       DSC ret = null;
       try {
          ret = (DSC)this.get("DSC");
       } catch(HL7Exception e) {
          HapiLogFactory.getHapiLog(this.getClass()).error("Unexpected error accessing data - this is probably a bug in the source code generator.", e);
          throw new RuntimeException(e);
       }
       return ret;
    }

}
