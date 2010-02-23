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
import ca.uhn.hl7v2.model.Primitive
import ca.uhn.hl7v2.model.Type
import ca.uhn.hl7v2.model.Varies
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.adaptType
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.componentIndex

/**
 * @author Christian Ohr
 */
class VariesAdapter extends TypeAdapter {

    VariesAdapter(Varies type) {
        super(type)
    }

    void from(Object value) {
        if (value instanceof TypeAdapter) {
            this.target.data = value.target
        } else if (value instanceof Type) {
            this.target.data = value
        } else {
            throw new AdapterException("cannot assign from ${value.class.name} to ${VariesAdapter.class.name}")
        }
    }
    
    def getAt(int idx) {
        if (!target.data) {
            return new NullAdapter()
        }
        if (target.data instanceof Composite) {
          return adaptType(target.data.getComponent(componentIndex(idx)))
        }
        //First element of the sublevel is identical with this primitive element
        if (target.data instanceof Primitive) {
          return idx == 1 ? target.data : new NullAdapter()
        }

        return new NullAdapter();
    }
    
    void putAt(int idx, Object value) {
        getAt(idx).from(value)
    }
    
    String getValue() {
        target.data.value
    }
    
    String getOriginalValue() {
        target.data.originalValue
    }
    
    boolean isNullValue() {
        target.data.originalValue == '""'
    }
    
    String toString() {
        target.data.value
    }

}