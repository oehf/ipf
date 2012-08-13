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
import ca.uhn.hl7v2.model.Type
import ca.uhn.hl7v2.util.DeepCopy
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.*

/**
 * @author Martin Krasser
 */
class CompositeAdapter extends TypeAdapter {

    CompositeAdapter(Composite type) {
        super(type)
    }

    // Copy composite on composite
    void from(CompositeAdapter value) {
        DeepCopy.copy((Type)value.target, (Type)this.target)
    }
    
    // Copy Primitive on first component
    void from(PrimitiveAdapter value) {
        getAt(1).from(value)
    }
    
    // Try copying first repetition
    void from(SelectorClosure value) {
        from(value(0))
    }
    
    // Everything else: copy String on first component
    void from(Object value) {
        getAt(1).from(value.toString())
    }
    
    def getAt(int idx) {
        TypeAdapter result = adaptType(target.getComponent(componentIndex(idx)))
        result.setPath(typePath(this, idx))
        result
    }

    void putAt(int idx, Object value) {
        getAt(idx).from(value)
    }
    
    def getValue() {
        componentValue(this)
    }
    
    def getOriginalValue() {
        componentOriginalValue(this)
    }
    
    boolean isNullValue() {
        componentOriginalValue(this) == '""'
    }
  

}