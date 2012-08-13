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

import ca.uhn.hl7v2.model.Type
import org.codehaus.groovy.runtime.InvokerHelper
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.adapt
import static org.openehealth.ipf.modules.hl7dsl.AdapterHelper.isEmpty

/**
 * @author Martin Krasser
 */
class TypeAdapter implements AbstractAdapter {

    Type type
    String path
    
    TypeAdapter(Type type) {
        this.type = type
        this.path = ''
    }
 
    Type getTarget() {
        type
    }

    Object invokeMethod(String name, Object args) {
        adapt(InvokerHelper.invokeMethod(type, name, args))
    }
    
    Object get(String s) {
        adapt(InvokerHelper.getProperty(type, s))
    }
   
    void set(String s, Object v) {
        InvokerHelper.setProperty(type, s, v)
    }
    
    def call(object) {
        throw new AdapterException("The type ${type.class.simpleName} is not repeatable for this field")
    }
    
    boolean isEmpty(){
      return isEmpty(type)
    }
    
    String getPath(){
        return path
    }
  
}