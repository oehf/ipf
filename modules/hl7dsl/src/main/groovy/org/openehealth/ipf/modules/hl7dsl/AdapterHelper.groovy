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

import ca.uhn.hl7v2.model.*

/**
 * @author Martin Krasser
 * @author Christian Ohr
 * @author Mitko Kolev
 */
class AdapterHelper {

    
    static String typePath(AbstractAdapter target, Integer repetition){
        String targetPath = target.path;
        return targetPath == '' ?  "${target.name}-${repetition}" : "${targetPath}-${repetition}"
    }
    
    static componentIndex(int index) {
        if (index < 1) {
            throw new AdapterException('component index must be in range 1..n')
        }
        index - 1
    }
    
    static selector(elements, adapter, index) {
        new SelectorClosure(AdapterHelper.class, elements, adapter, index)
    }
    
    static List adaptStructures(Structure[] structures) {
        structures.collect { adaptStructure(it) }
    }
    
    static List adaptStructures(Structure[] structures, String path) {
        structures.collect { adaptStructure(it, path) }
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

    static StructureAdapter adaptStructure(Structure structure, String path) {
        StructureAdapter result = adaptStructure(structure)
        result.setPath(path)
        return result
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
    
    static Object adapt(Object object, String path) {
        switch(object) {
            case Structure[]     : return adaptStructures(object, path)
            case Structure       : return adaptStructure(object, path)
            default : return adapt(object)
        }
    }
    
    static String stringValue(def object) {
        switch (object) {
            case PrimitiveAdapter : return object.value
            case VariesAdapter    : return stringValue(adapt(object.target.data))
            default               : return object.toString()
        }
    }
    
    static String originalStringValue(def object) {
        switch (object) {
            case PrimitiveAdapter : return object.originalValue
            case VariesAdapter    : return originalStringValue(adapt(object.target.data))
            default               : return object.toString()
        }
    }
    
    static def componentValue(def c) {
        def firstElement = c[1]
        firstElement instanceof SelectorClosure ? 
                firstElement(0).value : 
                firstElement.value
    }
    
    static def componentOriginalValue(def c) {
        def firstElement = c[1]
        firstElement instanceof SelectorClosure ? 
                firstElement(0).originalValue : 
                firstElement.originalValue
    }

    static boolean isEmpty(Type t){
        return !t.encode()
    }
 
    static boolean isEmpty(Varies varies){
        isEmpty(varies.data)
    }
}