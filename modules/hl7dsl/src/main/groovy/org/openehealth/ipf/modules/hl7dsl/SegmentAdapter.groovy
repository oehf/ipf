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
package org.openehealth.ipf.modules.hl7dsl

import ca.uhn.hl7v2.model.Segment
import ca.uhn.hl7v2.model.Varies
import ca.uhn.hl7v2.util.DeepCopy
import org.codehaus.groovy.runtime.InvokerHelper
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.*

/**
 * @author Martin Krasser
 * @author Christian Ohr
 * @deprecated the ipd-modules-hl7dsl module is deprecated
 */
class SegmentAdapter<T extends Segment> extends StructureAdapter<T> {
    
    SegmentAdapter(T segment) {
        super(segment)
		this.path = ''
    }
    
    int count(int idx) {
        target.getField(idx).length
    }
    
    TypeAdapter nrp(int idx) {
        adaptType(target.getField(idx, count(idx)))
    }
    
    Object invokeMethod(String name, Object args) {
        adapt(InvokerHelper.invokeMethod(target, name, args))
    }
    
    Object get(String s) {
        adapt(InvokerHelper.getProperty(target, s))
    }

    def getAt(int idx) {
        def result;
        def adapters = adaptTypes(target.getField(idx))
        if (target.getMaxCardinality(idx) == 1) {
            // non-repeating field
            if (adapters.empty) {
                //HAPI expects 0 as index for the first element
                result = adaptType(target.getField(idx, 0))
            } else {
                result = adapters[0]
            }
            result.setPath(typePath(this, idx));
        } else { 
            // repeating field
            for(AbstractAdapter adapter : adapters){
                adapter.setPath(typePath(this, idx));
            }
            result = selector(adapters, this, idx)
        }
        result
    }
    
    void putAt(int idx, def value) {
        def type = getAt(idx)
        if (type instanceof SelectorClosure) {
            type(0).from(value)
        } else {
            type.from(value)
        }
    }

    void from(def value) {
        if (value instanceof SegmentAdapter) {
            DeepCopy.copy((Segment)value.target, target)
        } else {
            throw new AdapterException("cannot assign from ${value.class.name} to ${SegmentAdapter.class.name}")
        }
    }
    
    def call(object) {
        throw new AdapterException("The segment ${target.class.simpleName} is not repeatable in this group or message")
    }
    
    def getValue() {
        componentValue(this)
    }
    
    /**
     * Only when this segment adapter represents an OBX segment: sets the
     * data type of OBX-5 repetitions to the given one and ensures that
     * the count of existing OBX-5 repetitions is not less than the given
     * number.  This method will throw an exception when the segment
     * adapter does not represent an OBX segment.
     *
     * @param type
     *      HL7v2 type name, e.g. 'CE'.
     * @param desiredRepetitionsCount
     *      minimal count of available OBX-5 repetitions.
     */
    void setObx5Type(String type, int desiredRepetitionsCount = 1) {
        if (! target.getClass().name.endsWith('.OBX')) {
            throw new AdapterException('only OBX segments can be served by this method')
        }

        for (int i = 0; i < desiredRepetitionsCount - count(5); ++i) {
            nrp(5)
        }

        putAt(2, type)
        Varies.fixOBX5(target, target.message.parser.factory)
    }
}
