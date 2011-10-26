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
package org.openehealth.ipf.commons.ihe.hl7v2.definitions.pdq.v25.segment;

import java.util.Collection;

import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Varies;
import ca.uhn.hl7v2.model.v25.datatype.*;

import ca.uhn.log.HapiLogFactory;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import ca.uhn.hl7v2.HL7Exception;
import org.openehealth.ipf.modules.hl7.model.AbstractSegment;


/**
 * <p>Represents an HL7 QPD message segment.
 * The fields contained in this segment:</p><p>
 * QPD-1: Message Query Name (CE)<br>
 * QPD-2: Query Tag (ST)<br>
 * QPD-3: Query Input Parameter List (QIP)<br>
 * QPD-4,5,6,7: User Parameters (in successive fields) (varies)<br>
 * QPD-8: Extended Composite ID with Check Digit (CX)<br>
 */
@SuppressWarnings("serial")
public class QPD extends AbstractSegment {

    /**
     * Creates a QPD (Query Parameter Definition) segment object
     * which belongs to the given message
     */
    public QPD(Group parentGroup, ModelClassFactory modelFactory) {
        super(parentGroup, modelFactory);
        Message msg = getMessage();
        try {
            add("CE", true, 1, 250, new Object[]{msg});
            add("ST", true, 1, 32, new Object[]{msg});
            add("QIP", true, 0, 256, new Object[]{msg});
            add(Varies.class, false, 0, 256, new Object[]{msg}, null);
            add(Varies.class, false, 0, 256, new Object[]{msg}, null);
            add(Varies.class, false, 0, 256, new Object[]{msg}, null);
            add(Varies.class, false, 0, 256, new Object[]{msg}, null);
            add("CX", false, 0, 256, new Object[]{msg}, null);
        } catch (HL7Exception e) {
            HapiLogFactory.getHapiLog(getClass()).error("Can't instantiate " + getClass().getName(), e);
        }
    }

    /**
     * Returns Message Query Name (QPD-1).
     */
    public CE getMessageQueryName() {
        return getTypedField(1, 0);
    }

    /**
     * Returns Query Tag (QPD-2).
     */
    public ST getQueryTag() {
        return getTypedField(2, 0);
    }

    /**
     * Returns Demographics Fields (QPD-3).
     */
    public QIP getDemographicsFields(int rep) {
        return getTypedField(3, rep);
    }

    /**
     * Returns Demographics Fields (QPD-3).
     */
    public QIP[] getDemographicsFields() {
        Collection<QIP> result = getTypedField(3);
        return result.toArray(new QIP[result.size()]);
    }

    /**
     * Returns What Domains to be returned (QPD-8).
     */
    public CX getWhatDomainsReturned(int rep) {
        return getTypedField(8, rep);
    }

    /**
     * Returns What Domains to be returned (QPD-8).
     *
     * @return movement IDs
     */
    public CX[] getWhatDomainsReturned() {
        Collection<CX> result = getTypedField(8);
        return result.toArray(new CX[result.size()]);
    }

}