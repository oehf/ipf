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
package org.openehealth.ipf.commons.ihe.hl7v2.definitions.pix.v25.segment;

import ca.uhn.hl7v2.model.AbstractSegment;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.model.v25.datatype.CE;
import ca.uhn.hl7v2.model.v25.datatype.CX;
import ca.uhn.hl7v2.model.v25.datatype.ST;
import ca.uhn.hl7v2.parser.ModelClassFactory;


/**
 * <p>Represents an HL7 QPD message segment.
 * The fields contained in this segment:</p><p>
 * QPD-1: Message Query Name (CE)<br>
 * QPD-2: Query Tag (ST)<br>
 * QPD-3: PIX Query parameter<br>
 * QPD-4: What domains returned
  */

public class QPD extends AbstractSegment {

    /**
     * Creates a QPD (Query Parameter Definition) segment object
     * which belongs to the given message
     */
    public QPD(Group parentGroup, ModelClassFactory modelFactory) {
        super(parentGroup, modelFactory);
        var msg = getMessage();
        add(CE.class, true, 1, 250, new Object[]{msg}, "Message Query Name");
        add(ST.class, true, 1, 32, new Object[]{msg}, "Query Tag");
        add(CX.class, true, 1, 256, new Object[]{msg}, "Person Identifier");
        add(CX.class, false, 0, 256, new Object[]{msg}, "What domains returned");
    }

    /**
     * Returns Message Query Name (QPD-1).
     */
    public CE getMessageQueryName() {
        return getTypedField(1, 0);
    }

    /**
     * Returns Message Query Name (QPD-1).
     */
    public CE getQpd1_MessageQueryName() {
        return getTypedField(1, 0);
    }

    /**
     * Returns Query Tag (QPD-2).
     */
    public ST getQueryTag() {
        return getTypedField(2, 0);
    }

    /**
     * Returns Query Tag (QPD-2).
     */
    public ST getQpd2_QueryTag() {
        return getTypedField(2, 0);
    }

    /**
     * Returns Person identifier (QPD-3).
     */
    public CX getPersonIdentifier() {
        return getTypedField(3, 0);
    }

    /**
     * Returns Person identifier (QPD-3).
     */
    public CX getQpd3_PersonIdentifier() {
        return getTypedField(3, 0);
    }

    /**
     * Returns What Domains to be returned (QPD-4).
     */
    public CX getWhatDomainsReturned(int rep) {
        return getTypedField(4, rep);
    }

    /**
     * Returns What Domains to be returned (QPD-4).
     */
    public CX getQpd4_WhatDomainsReturned(int rep) {
        return getTypedField(4, rep);
    }

    /**
     * Returns What Domains to be returned (QPD-4).
     */
    public CX[] getWhatDomainsReturned() {
        return getTypedField(4, new CX[0]);
    }

    /** {@inheritDoc} */
    @Override
    protected Type createNewTypeWithoutReflection(int field) {
        switch (field) {
            case 0: return new CE(getMessage());
            case 1: return new ST(getMessage());
            case 2:
            case 3:
                return new CX(getMessage());
            default: return null;
        }
    }

}