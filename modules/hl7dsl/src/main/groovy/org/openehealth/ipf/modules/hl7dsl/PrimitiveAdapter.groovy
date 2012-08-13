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

import ca.uhn.hl7v2.model.Primitive
import ca.uhn.hl7v2.model.Type
import ca.uhn.hl7v2.util.DeepCopy
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.originalStringValue

/**
 * @author Martin Krasser
 * @author Christian Ohr
 */
class PrimitiveAdapter extends TypeAdapter {

    PrimitiveAdapter(Primitive type) {
        super(type)
    }

    void from(Object value) {
        if (value instanceof PrimitiveAdapter) {
            DeepCopy.copy((Type)value.target, (Type)this.target)
        } else {
            target.value = originalStringValue(value) 
        }
    }
    
    String toString() {
        this.value
    }
    
    boolean isNullValue() {
        target.value == '""'
    }
    
    def getValue() {
        target.value == '""' ? '' : target.value
    }
    
    def getOriginalValue() {
        target.value
    }
    
    def getAt(int idx) {
        throw new AdapterException("Type ${type.class.simpleName} is a Primitive type and has no (sub-)components")
    }
    
    def call(object) {
        throw new AdapterException("Type ${type.class.simpleName} is a Primitive type and is not repeatable")
    } 
    
}