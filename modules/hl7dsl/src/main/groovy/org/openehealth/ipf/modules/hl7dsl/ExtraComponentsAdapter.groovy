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

import ca.uhn.hl7v2.model.ExtraComponents
import ca.uhn.hl7v2.model.DataTypeException

/**
 * @author Christian Ohr
 */
class ExtraComponentsAdapter {

	ExtraComponents extraComponents
	
	ExtraComponentsAdapter(ExtraComponents extraComponents) {
		this.extraComponents = extraComponents
    }

    def getTarget() {
    	extraComponents
    }
    
    Object invokeMethod(String name, Object args) {
        adapt(InvokerHelper.invokeMethod(segment, name, args))
    }
    
    def getAt(int idx) {
        adaptType(target.getComponent(componentIndex(idx)))
    }
    
    def getValue() {
        componentValue(this)
    }
    
}