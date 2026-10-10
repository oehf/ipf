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

/**
 * Special closure that helps in the cases of HL7 DSL to allow for a default
 * repetition (0) and (sub-)component [1]. This is necessary for addressing
 * respective fields that have different structures in different HL7 versions. 
 *
 * @author Christian Ohr
 */
class Repeatable extends Closure implements Iterable<Visitable> {

    Structure structure
    def index

    /**
     * @param owner closure owner
     * @param structure segment containing the repeatable field or group containing the repeatable structure
     * @param index field index (if structure is a segment) or structure name (if structure is a group)
     */
    Repeatable(owner, Structure structure, index) {
        super(owner)
        this.structure = structure
        this.index = index
    }

    /**
     * @deprecated the repetitions are now always read from the structure, use
     * {@link #Repeatable(java.lang.Object, ca.uhn.hl7v2.model.Structure, java.lang.Object)}
     */
    @Deprecated
    Repeatable(owner, elements, Structure structure, index) {
        this(owner, structure, index)
    }

    /**
     * @return the current repetitions. These are read from the structure on each access, so that
     * repetitions added in the meantime are taken into account.
     */
    Visitable[] currentElements() {
        structure instanceof Segment ?
                ((Segment) structure).getField((int) index) :
                ((Group) structure).getAll((String) index)
    }

    @Override
    Iterator<Visitable> iterator() {
        return currentElements().iterator()
    }

    /**
     * Returns the named property of the first repetition
     *
     * @param property
     * @return the named property of the first repetition
     */
    def getProperty(String property) {
        InvokerHelper.getProperty(elementAt(0), property)
    }

    /**
     * Apply the method call to the first repetition and return the result
     *
     * @param name method name
     * @param args method args
     * @return result of the method call
     */
    def methodMissing(String name, args) {
        InvokerHelper.invokeMethod(elementAt(0), name, args)
    }

    /**
     * Returns the index'th element of the first repetition
     * @param index index
     * @return index'th element of the first repetition
     */
    def getAt(int index) {
        elementAt(0)[index]
    }

    /**
     * Forwards property access to the first repetition
     * @param name property name
     * @return result value
     */
    def propertyMissing(String name) {
        InvokerHelper.getProperty(elementAt(0), name)
    }

    /**
     * Puts the value into the first repetition
     * @param value
     */
    void from(Object value) {
        elementAt(0).from(value)
    }

    /**
     * Encode the repetition in a way how it would look like in a pipe-encoded message
     * @return encoded repetition
     */
    String encodeRepetitions() {
        currentElements().collect { it.encode() }.join(getSeparator(structure))
    }

    protected Object doCall(Object argument) {
        if (argument != null) {
            return elementAt((int) argument)
        } else {
            return currentElements()
        }
    }

    /**
     * Returns the repetition with the given index, adding repetitions up to this index
     * if they do not exist yet
     *
     * @param argument repetition index, starting with 0
     * @return the repetition
     */
    def elementAt(int argument) {
        while (size() <= argument) {
            structure.nrp(index)
        }
        currentElements()[argument]
    }

    int size() {
        currentElements().length
    }

    boolean isEmpty() {
        boolean result = true
        def elements = currentElements()
        for (int index = 0; index < elements.length; index++) {
            def element = elements[index]
            if (element != null && !element.isEmpty()) {
                result = false
                break
            }
        }
        return result
    }

    boolean asBoolean() {
        !isEmpty()
    }

    String getPath() {
        elementAt(0).path
    }

    private static String getSeparator(Structure s) {
        return s instanceof Segment ? s.message.encodingCharactersValue[1] : "\r"
    }
}
