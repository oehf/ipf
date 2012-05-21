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

import org.codehaus.groovy.runtime.InvokerHelper

import ca.uhn.hl7v2.model.DataTypeException

/**
 * Special closure that helps in the cases of HL7 DSL to allow for a default
 * repetition (0) and (sub-)component [1]. This is necessary for addressing
 * respective fields that have different structures in different HL7 versions. 
 * 
 * @author Christian Ohr
 */
class SelectorClosure extends Closure implements AbstractAdapter {
	
	def elements	
	AbstractAdapter adapter
	def index
	
	SelectorClosure(owner, elements, adapter, index) {
		super(owner)
		this.elements = elements
		this.adapter = adapter
		this.index = index
	}

	// Defining the value property with getValue() doesn't work
	public Object getProperty(String property) {
	    call(0)?.getProperty(property)
    }		

	// Forward other method calls to the first object
	def methodMissing(String name, args) {
	    InvokerHelper.invokeMethod(call(0), name, args)
	}

	// Forward property access to the first object
	def propertyMissing(String name) {
	    InvokerHelper.getProperty(call(0), name)
	}	
	
	protected Object doCall(Object argument) {
		if (argument != null){
            return elementAt(argument)
		}else {	
            return elements
		}
	}
	
    def elementAt(argument){
        AbstractAdapter element
        if (elements.size() <= argument){
            element = adapter.nrp(index)
        } else {
            element = elements[argument]
        }
        switch (element){
            case SegmentAdapter :
            case GroupAdapter   : element.setPath(element.path + "(${argument})")
        }
        element
    }
    
    boolean isEmpty(){
        boolean result = true;
        for (int index = 0; index < elements.size(); index ++) {
            AbstractAdapter el = elements[index];
            if (el != null && !el.isEmpty()){
                result = false;
                break;
            }
        }
        return result;
    }
    
    Object getTarget(){
        return adapter.target
    }
    
    String getPath(){
        elementAt(index).path
    }
    
    void setPath(String path){
        throw new DataTypeException("Cannot set the path of a selector closure")
    }
}
