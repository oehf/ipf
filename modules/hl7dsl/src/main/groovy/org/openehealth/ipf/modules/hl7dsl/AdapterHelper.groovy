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

import ca.uhn.hl7v2.model.Composite
import ca.uhn.hl7v2.model.Group
import ca.uhn.hl7v2.model.Message
import ca.uhn.hl7v2.model.Primitive
import ca.uhn.hl7v2.model.Varies
import ca.uhn.hl7v2.model.Segment
import ca.uhn.hl7v2.model.Structure
import ca.uhn.hl7v2.model.ExtraComponents
import ca.uhn.hl7v2.model.Type

/**
 * @author Martin Krasser
 */
class AdapterHelper {

    static componentIndex(int index) {
        if (index < 1) {
            throw new AdapterException('component index must be in range 1..n')
        }
        index - 1
    }
    
    static selector(elements) {
        { idx -> idx != null ? 
        		(elements.size <= idx ? 
        				new NullAdapter() : 
        				elements[idx]) : 
        		elements 
        }
    }
    
    static List adaptStructures(Structure[] structures) {
        structures.collect { adaptStructure(it) }
    }

    static List<TypeAdapter> adaptTypes(Type[] types) {
        types.collect { adaptType(it) }
    }
    
    static StructureAdapter adaptStructure(Structure structure) {
        switch (structure) {
            case Message : return new MessageAdapter(structure)
            case Group   : return new GroupAdapter(structure)
            case Segment : return new SegmentAdapter(structure)
        }
    }

    static TypeAdapter adaptType(Type type) {
        switch (type) {
            case Primitive : return new PrimitiveAdapter(type)
            case Composite : return new CompositeAdapter(type)
            case Varies    : return new VariesAdapter(type)
            case Type      : return new TypeAdapter(type)
        }
    }
    
    static Object adapt(Object object) {
        switch(object) {
            case ExtraComponents : return new ExtraComponentsAdapter(object)
            case Structure[]     : return adaptStructures(object)
            case Structure       : return adaptStructure(object)
            case Type[]          : return adaptTypes(object)
            case Type            : return adaptType(object)
            default              : return object
        }    
    }
    
    static String stringValue(def object) {
        switch (object) {
            case PrimitiveAdapter : return object.target.value
            case VariesAdapter    : return stringValue(adapt(object.target.data))
            default               : return object.toString()
        }
    }

}