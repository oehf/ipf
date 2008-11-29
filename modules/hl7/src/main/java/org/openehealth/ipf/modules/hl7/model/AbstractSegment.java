/*
 * Copyright 2008 the original author or authors.
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
package org.openehealth.ipf.modules.hl7.model;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.parser.ModelClassFactory;
import ca.uhn.log.HapiLogFactory;

/**
 * Convenience subclass that uses Java 5 Generics to abbreviate tedious HL7v2
 * parsing.
 * 
 * @author Christian Ohr
 */
@SuppressWarnings("serial")
public abstract class AbstractSegment extends ca.uhn.hl7v2.model.AbstractSegment {

    public AbstractSegment(Group parent, ModelClassFactory factory) {
        super(parent, factory);
    }

    /**
     * Returns the field content casted to the type that is specified by the
     * caller. This boils down the typed field accessor methods of subclasses to
     * just 1 line of code.
     * 
     * @param <T>
     * @param c
     * @param rep
     * @return the field content casted to a specified type
     */
    @SuppressWarnings("unchecked")
    protected <T extends Type> T getTypedField(int c, int rep) {
        try {
            Type t = getField(c, rep);
            return (T) t;
        } catch (ClassCastException cce) {
            HapiLogFactory.getHapiLog(this.getClass()).error(
                    "Unexpected problem obtaining field value.  This is a bug.", cce);
            throw new RuntimeException(cce);
        } catch (HL7Exception he) {
            HapiLogFactory.getHapiLog(this.getClass()).error(
                    "Unexpected problem obtaining field value.  This is a bug.", he);
            throw new RuntimeException(he);
        }
    }

    /**
     * Returns a repeated field content casted to the type that is specified by
     * the caller.
     * 
     * @param <T>
     * @param c
     * @return the multivalued field content casted to a specified type
     */
    @SuppressWarnings("unchecked")
    protected <T extends Type> List<T> getTypedField(int c) {
        try {
            Type[] t = getField(c);
            List<T> result = new ArrayList<T>(t.length);
            for (int i = 0; i < t.length; i++) {
                result.add((T) t[i]);
            }
            return result;
        } catch (ClassCastException cce) {
            HapiLogFactory.getHapiLog(this.getClass()).error(
                    "Unexpected problem obtaining field value.  This is a bug.", cce);
            throw new RuntimeException(cce);
        } catch (HL7Exception he) {
            HapiLogFactory.getHapiLog(this.getClass()).error(
                    "Unexpected problem obtaining field value.  This is a bug.", he);
            throw new RuntimeException(he);
        }
    }

}
