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
package org.openehealth.ipf.platform.camel.ihe.pdq.core.def.v25.segment;

import org.openehealth.ipf.modules.hl7.model.AbstractSegment;

import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v25.datatype.*;

import ca.uhn.log.HapiLogFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import ca.uhn.hl7v2.HL7Exception;


/**
 * <p>Represents an HL7 QPD message segment. 
 * This segment has the following fields:</p><p>
 * QPD-1: Message Query Name (CE)<br> 
 * QPD-2: Query Tag (ST)<br> 
 * QPD-3: User Parameters (in successive fields) (varies)<br> 
 * </p><p>The get...() methods return data from individual fields.  These methods 
 * do not throw exceptions and may therefore have to handle exceptions internally.  
 * If an exception is handled internally, it is logged and null is returned.  
 * This is not expected to happen - if it does happen this indicates not so much 
 * an exceptional circumstance as a bug in the code for this class.</p>    
 */
@SuppressWarnings("serial")
public class QPD extends AbstractSegment {

  /**
   * Creates a QPD (Query Parameter Definition) segment object that belongs to the given 
   * message.  
   */
  public QPD(Group parent, ModelClassFactory factory) {
    super(parent, factory);
    Message message = getMessage();
    try {
       this.add(CE.class, true, 1, 250, new Object[]{message});
       this.add(ST.class, false, 1, 32, new Object[]{message});
       this.add(Varies.class, true, 0, 256, new Object[]{message});
       this.add(Varies.class, false, 0, 256, new Object[]{message});
       this.add(Varies.class, false, 0, 256, new Object[]{message});
       this.add(Varies.class, false, 0, 256, new Object[]{message});
       this.add(Varies.class, false, 0, 256, new Object[]{message});       
       this.add(Varies.class, true, 0, 256, new Object[]{message});
    } catch (HL7Exception he) {
        HapiLogFactory.getHapiLog(this.getClass()).error("Can't instantiate " + this.getClass().getName(), he);
    }
  }

  /**
   * Returns Message Query Name (QPD-1).
   */
  public CE getMessageQueryName()  {
	  return getTypedField(1, 0);
  }

  /**
   * Returns Query Tag (QPD-2).
   */
  public ST getQueryTag()  {
	  return getTypedField(2, 0);
  }

  /**
   * Returns User Parameters (in successive fields) (QPD-3).
   */
  public Varies getUserParametersInsuccessivefields()  {
	  return getTypedField(3, 0);

  }

}