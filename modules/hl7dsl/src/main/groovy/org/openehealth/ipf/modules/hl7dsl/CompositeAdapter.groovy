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

import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.adaptType
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.adaptTypes
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.componentIndex
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.componentValue

import ca.uhn.hl7v2.model.Composite
import ca.uhn.hl7v2.model.DataTypeException
import ca.uhn.hl7v2.model.Primitive
import ca.uhn.hl7v2.model.Type
import ca.uhn.hl7v2.util.DeepCopy

/**
 * @author Martin Krasser
 */
class CompositeAdapter extends TypeAdapter {

    CompositeAdapter(Composite type) {
        super(type)
    }

    void from(Object value) {
        if (value instanceof CompositeAdapter) {
            DeepCopy.copy((Type)value.target, (Type)this.target)
        } else {
            throw new AdapterException("cannot assign from ${value.class.name} to ${CompositeAdapter.class.name}")
        }
    }
    
    def getAt(int idx) {
        adaptType(target.getComponent(componentIndex(idx)))
    }

    void putAt(int idx, Object value) {
        getAt(idx).from(value)
    }
    
    def getValue() {
        componentValue(this)
    }

}