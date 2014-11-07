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
package org.openehealth.ipf.modules.hl7.dsl

import ca.uhn.hl7v2.model.Group
import ca.uhn.hl7v2.model.Segment
import ca.uhn.hl7v2.model.Structure
import ca.uhn.hl7v2.model.Visitable
import org.codehaus.groovy.runtime.InvokerHelper

import ca.uhn.hl7v2.model.DataTypeException

/**
 * Special closure that helps in the cases of HL7 DSL to allow for a default
 * repetition (0) and (sub-)component [1]. This is necessary for addressing
 * respective fields that have different structures in different HL7 versions. 
 *
 * @author Christian Ohr
 */
class Repeatable extends Closure implements Iterable<Visitable> {

    Structure structure
    def elements
    def index

    Repeatable(owner, elements, structure, index) {
        super(owner)
        this.elements = elements
        this.structure = structure
        this.index = index
    }

    @Override
    Iterator<Visitable> iterator() {
        return elements.iterator()
    }

    // Defining the value property with getValue() doesn't work
    public Object getProperty(String property) {
        InvokerHelper.getProperty(elementAt(0), property)
    }

    // Forward other method calls to the first object
    def methodMissing(String name, args) {
        InvokerHelper.invokeMethod(elementAt(0), name, args)
    }

    // Forward index access to the first object
    def getAt(int index) {
        elementAt(0)[index]
    }

    // Forward property access to the first object
    def propertyMissing(String name) {
        InvokerHelper.getProperty(elementAt(0), name)
    }

    // Forward property access to the first object
    void from(Object value) {
        elementAt(0).from(value)
    }

    protected Object doCall(Object argument) {
        if (argument != null) {
            return elementAt((int)argument)
        } else {
            return elements
        }
    }

    def elementAt(int argument) {
        def element
        if (elements.size() <= argument) {
            element = structure.nrp(index)
        } else {
            element = elements[argument]
        }
        element
    }

    int size() {
        elements.size()
    }

    boolean isEmpty() {
        boolean result = true;
        for (int index = 0; index < elements.size(); index++) {
            def element = elements[index];
            if (element != null && !element.isEmpty()) {
                result = false;
                break;
            }
        }
        return result;
    }

    String getPath() {
        elementAt(index).path
    }

}
