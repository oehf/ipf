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

import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.adapt
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.adaptType
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.adaptTypes
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.selector
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.stringValue
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.componentValue

import org.codehaus.groovy.runtime.InvokerHelper

import ca.uhn.hl7v2.model.DataTypeException
import ca.uhn.hl7v2.model.Primitive
import ca.uhn.hl7v2.model.Segment
import ca.uhn.hl7v2.model.Type
import ca.uhn.hl7v2.util.DeepCopy

/**
 * @author Martin Krasser
 * @author Christian Ohr
 */
class SegmentAdapter extends StructureAdapter {

    Segment segment
    
    SegmentAdapter(Segment segment) {
        this.segment = segment
    }

    def getTarget() {
        segment
    }
    
    int count(int idx) {
        segment.getField(idx).length
    }
    
    TypeAdapter nrp(int idx) {
        adaptType(segment.getField(idx, count(idx)))
    }
    
    Object invokeMethod(String name, Object args) {
        adapt(InvokerHelper.invokeMethod(segment, name, args))
    }
    
    Object get(String s) {
        adapt(InvokerHelper.getProperty(segment, s))
    }

    def getAt(int idx) {
        def adapters = adaptTypes(segment.getField(idx))
        if (segment.getMaxCardinality(idx) == 1) { 
            // non-repeating field
            if (adapters.empty) {
                return adaptType(segment.getField(idx, 0))
            } else {
                return adapters[0]
            }            
        } else { 
            // repeating field
            return selector(adapters, this, idx)
        }
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
            DeepCopy.copy((Segment)value.target, (Segment)this.target)
        } else {
            throw new AdapterException("cannot assign from ${value.class.name} to ${SegmentAdapter.class.name}")
        }
    }
    
    def call(object) {
        throw new AdapterException("The segment ${segment.class.simpleName} is not repeatable in this group or message")
    }
    
    def getValue() {
        componentValue(this)
    }
	
	/**
	 * @return true if the segment is empty
	 */
	boolean isEmpty() {
		int i = 1
		boolean found = false
		while (i <= segment.numFields() && !found) {
			found = (count(i++) > 0)
		}
		!found
	}
}
