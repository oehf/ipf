/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.modules.hl7.extend

import ca.uhn.hl7v2.model.*
import ca.uhn.hl7v2.parser.ModelClassFactory
import ca.uhn.hl7v2.util.DeepCopy
import ca.uhn.hl7v2.util.ReadOnlyMessageIterator
import org.codehaus.groovy.runtime.InvokerHelper
import org.openehealth.ipf.modules.hl7.dsl.HL7DslException
import org.openehealth.ipf.modules.hl7.dsl.Null
import org.openehealth.ipf.modules.hl7.dsl.Repeatable
import org.openehealth.ipf.modules.hl7.message.MessageUtils
import org.openehealth.ipf.modules.hl7.message.Visitors

import java.lang.reflect.Constructor

import static org.openehealth.ipf.modules.hl7.dsl.Messages.copyMessage

/**
 * Adds HL7 DSL extensions for Groovy. This is a replacement for the previous solution
 * that wrapped the HAPI classes into Adapters that provided the methods required for
 * the DSL.
 *
 * @DSL
 */
public class Hl7Dsl2ExtensionModule {


    // ==========================================================================
    // Visitable metaclass extensions
    // ==========================================================================

    /**
     * Indexed write access on any type. Note that indexes start with 1.
     *
     * @param delegate
     * @param idx index
     * @param value any source value
     */
    public static void putAt(Visitable delegate, idx, value) {
        delegate[idx].from(value)
    }

    // ==========================================================================
    // Type metaclass extensions
    // ==========================================================================

    /**
     * Explicitly disallow calls on Type instances
     */
    public static def call(Type delegate, args) {
        throw new HL7DslException("The type ${delegate.class.simpleName} is not repeatable for this field")
    }

    // ==========================================================================
    // Primitive metaclass extensions
    // ==========================================================================

    /**
     * @return true if the value of the primitive is a so-called "active null"
     */
    public static boolean isNullValue(Primitive delegate) {
        delegate.value == '""'
    }

    /**
     * @return the primitive value, or the empty string if {@link #isNullValue(ca.uhn.hl7v2.model.Primitive)}
     * was true
     */
    public static String getValue2(Primitive delegate) {
        isNullValue(delegate) ? '' : delegate.value
    }

    /**
     * In later HL7v2 versions, often primitive types are replaced by composites with the previous type
     * as the first component. Primitive types have no components, but it makes sense to return the
     * primitive when the provided index is 1, so that T[1] returns the same.
     *
     * @return the primitive value if idx is 1, otherwise a {@link Null} object
     */
    public static String getAt(Primitive delegate, int index) {
        return (index == 1) ?
                delegate :
                new Null(delegate.message)
    }

    /**
     * Returns the the non-empty value or the provided defaultValue. Equivalent with
     * T.value ?: default, but much more readable.
     *
     * @return the non-empty value or the provided defaultValue
     */
    public static String getValueOr(Primitive delegate, String defaultValue) {
        delegate.value ?: defaultValue
    }

    /**
     * Use the new value as the delegate's value
     */
    public static void from(Primitive delegate, Object newValue) {
        if (newValue instanceof Primitive) {
            delegate.value = newValue.value
        } else {
            delegate.value = stringValue(newValue)
        }
    }

    // ==========================================================================
    // ExtraComponents metaclass extensions
    // ==========================================================================

    def getAt(ExtraComponents delegate, int idx) {
        delegate.getComponent(idx)
    }

    // ==========================================================================
    // Composite metaclass extensions
    // ==========================================================================

    /**
     * Deep-Copy of composites
     *
     * @param delegate target composite
     * @param source composite
     */
    public static void from(Composite delegate, Composite value) {
        DeepCopy.copy(value, delegate)
    }

    /**
     * Copies primitive on first component of target composite
     *
     * @param delegate target composite
     * @param source primitive
     */
    public static void from(Composite delegate, Primitive value) {
        delegate[1].from(value)
    }

    /**
     * Copies first repetition on target composite
     *
     * @param delegate target composite
     * @param source type
     */
    public static void from(Composite delegate, Repeatable value) {
        from(delegate, value(0))
    }

    /**
     * Fallback method: copies the value on the first component of the target composite
     *
     * @param delegate target composite
     * @param source value
     */
    public static void from(Composite delegate, Object value) {
        delegate[1].from(value.toString())
    }

    /**
     * Indexed read access on composite. Note that indexes start with 1.
     *
     * @param delegate composite
     * @param idx index
     * @return the idx-th component of the composite
     */
    public static Object getAt(Composite delegate, int idx) {
        delegate.getComponent(componentIndex(idx))
    }

    /**
     * Returns the value of the first component of the composite
     * @param delegate composite
     *
     * @return value of the first component
     */
    public static Object getValue(Composite delegate) {
        componentValue(delegate)
    }

    /**
     * Returns the value of the first component of the composite or the empty string
     * if {@link #isNullValue(ca.uhn.hl7v2.model.Primitive)} was true
     *
     * @param delegate composite
     * @return value of the first component
     */
    public static Object getValue2(Composite delegate) {
        componentValue2(delegate)
    }


    /**
     * @return true if the value first component of the composite is a so-called "active null"
     */
    public static boolean isNullValue(Composite delegate) {
        componentValue(delegate) == '""'
    }

    //==========================================================================
    // Varies metaclass extensions
    // =========================================================================

    /**
     * Use the new value as the delegate's value
     * @param delegate
     * @param newValue
     */
    public static void from(Varies delegate, Type newValue) {
        delegate.data.from(newValue)
    }

    public static def getAt(Varies delegate, int idx) {
        if (delegate.data instanceof Composite) {
            return delegate.data[idx]
        }
        //First element of the sublevel is identical with this primitive element
        if (delegate.data instanceof Primitive) {
            return idx == 1 ? delegate.data : new Null()
        }
        return new Null(delegate.message)
    }

    public static String getValue(Varies delegate) {
        delegate.data.value
    }

    public static String getValue2(Varies delegate) {
        delegate.data.value2
    }

    public static boolean isNullValue(Varies delegate) {
        delegate.data.isNullValue()
    }

    public static String toString(Varies delegate) {
        delegate.data.value
    }

    // ==========================================================================
    // Structure metaclass extensions
    // ==========================================================================

    /**
     * @return a string representation of the location of the structure in the message
     */
    public static String getPath(Structure delegate) {
        findIndexOf(delegate.message) { it == delegate }
    }

    // ==========================================================================
    // Segment metaclass extensions
    // ==========================================================================

    /**
     * Returns the number of repetitions of the idx-th field in the segment. Note that the
     * index starts with 1.
     *
     * @param delegate segment
     * @param idx field index
     * @return number of repetitions
     */
    public static int count(Segment delegate, int idx) {
        delegate.getField(idx).length
    }

    /**
     * Returns a new repetition for the idx-th field in the segment. E.g., msg.PID.nrp(5)
     * would add a new person name to PID-5.
     *
     * @param delegate segment
     * @param idx field index
     * @return new type
     */
    public static Type nrp(Segment delegate, int idx) {
        delegate.getField(idx, count(delegate, idx))
    }

    /**
     * Returns the idx-th field of the segment.
     *
     * @param delegate segment
     * @param idx index
     * @return the idx-th field of the segment
     */
    public static def getAt(Segment delegate, int idx) {
        def result;
        Type[] field = delegate.getField(idx)
        if (delegate.getMaxCardinality(idx) == 1) {
            // non-repeating field
            if (field.length == 0) {
                //HAPI expects 0 as index for the first element
                result = delegate.getField(idx, 0)
            } else {
                result = field[0]
            }
        } else {
            result = selector(field, delegate, idx)
        }
        return result
    }

    /**
     * Deep-Copies a segment
     *
     * @param delegate target segment
     * @param value source segment
     */
    public static void from(Segment delegate, Segment value) {
        DeepCopy.copy(value, delegate)
    }


    public static void from(Segment delegate, Repeatable value) {
        from(delegate, value(0))
    }

    public static String getValue(Segment delegate) {
        throw new HL7DslException("Cannot obtain the value of a segment")
    }

    /**
     * Non-repeatable segments are not suitable for obtaining repetitions except if
     * @param delegate
     * @param args
     */
    public static Object call(Segment delegate, args) {
        throw new HL7DslException("The segment ${delegate.class.simpleName} is not repeatable in this group or message")
    }

    /**
     * Only when this segment represents an OBX segment: sets the
     * data type of OBX-5 repetitions to the given one and ensures that
     * the count of existing OBX-5 repetitions is not less than the given
     * number.  This method will throw an exception when the segment
     * does not represent an OBX segment.
     *
     * @param type
     *      HL7v2 type name, e.g. 'CE'.
     * @param desiredRepetitionsCount
     *      minimal count of available OBX-5 repetitions.
     */
    public static void setObx5Type(Segment delegate, String type, int desiredRepetitionsCount = 1) {
        if (!delegate.getClass().name.endsWith('.OBX')) {
            throw new HL7DslException('only OBX segments can be served by this method')
        }

        for (int i = 0; i < desiredRepetitionsCount - count(delegate, 5); ++i) {
            nrp(delegate, 5)
        }

        delegate[2] = type
        Varies.fixOBX5(delegate, delegate.message.parser.factory)
    }

    // ==========================================================================
    // Group metaclass extensions
    // ==========================================================================

    /**
     * Returns the number of repetitions if the substructre with name
     *
     * @param delegate group
     * @param name substructure name
     * @return the number of repetitions
     */
    public static int count(Group delegate, String name) {
        delegate.getAll(name).length
    }

    /**
     * Returns a new repetition for substructure in this group. E.g., msg.PATIENT_RESULT.nrp('ORDER_OBSERVATION')
     *
     * @param delegate segment
     * @param name substructure name
     * @return new structure
     */
    public static Structure nrp(Group delegate, String name) {
        delegate.get(name, count(delegate, name))
    }

    /**
     * Locations of all structures for which the closure returns true (Groovy truth)
     *
     * @param delegate delegate
     * @param c closure, taking a {@link Structure} as parameter
     * @return locations of matching structures
     */
    public static List<String> findIndexValues(Group delegate, Closure<?> c) {
        Visitors.findIndexValues(delegate, c)
    }

    /**
     * Location of the first structure for which the closure returns true (Groovy truth)
     *
     * @param delegate delegate
     * @param c closure, taking a {@link Structure} as parameter
     * @return location of first matching structure
     */
    public static String findIndexOf(Group delegate, Closure<?> c) {
        Visitors.findIndexOf(delegate, c)
    }

    /**
     * Location of the last structure for which the closure returns true (Groovy truth)
     *
     * @param delegate delegate
     * @param c closure, taking a {@link Structure} as parameter
     * @return location of last matching structure
     */
    public static String findLastIndexOf(Group delegate, Closure<?> c) {
        Visitors.findLastIndexOf(delegate, c)
    }

    /**
     * For each structure inside the group, the provided closure is executed. The closure takes two
     * parameters:
     * <ul>
     *     <li>the structure</li>
     *     <li>the location of the structure</li>
     * </ul>
     *
     * @param delegate group
     * @param c closure
     * @return the group
     */
    public static Group eachWithIndex(Group delegate, Closure<?> c) {
        Visitors.eachWithIndex(delegate, c)
    }

    // Support finders and allow to use X.Y(int) instead of X.getY(int) by replacing it with X.getYAll()[int]
    public static def methodMissing(Group delegate, String name, args) {
        if (name.startsWith('findLastIndexOf')) {
            return findLastIndexOf(delegate) { it.name == name.substring(15) }
        } else if (name.startsWith('findIndexOf')) {
            return findIndexOf(delegate) { it.name == name.substring(11) }
        } else if (name.startsWith('findAll')) {
            return delegate.findAll { it.name == name.substring(7) }
        } else if (name.startsWith('find')) {
            return delegate.find { it.name == name.substring(4) }
        } else {
            return getStructure(delegate, name, args)
        }
    }

    private static def getStructure(Group delegate, String name, args) {
        MetaProperty metaProperty = delegate.metaClass.getMetaProperty("${name}All")
        if (metaProperty) {
            Repeatable r = selector(metaProperty.getProperty(delegate), delegate, name)
            if (args?.size() == 1 && args[0] instanceof Integer) {
                return r.elementAt(args[0])
            } else {
                return r
            }
        } else {
            throw new HL7DslException("Unknown method ${name} with parameters ${args} on class ${delegate.class}")
        }
    }

    /**
     * Copies a structure into a group
     *
     * @param delegate group
     * @param name name of the substructure that takes the copy
     * @param value source structure
     */
    public static void set(Group delegate, String name, Object value) {
        Structure structure = delegate.get(name)
        structure.from(value)
    }

    /**
     * Substructure access by name. In general, group.XYZ == group['XYZ'] is true. However, for repeatable
     * structures, group.XYZ(i) is allowed while group['XYZ'](i) is not allowed
     *
     * @param delegate group
     * @param name name of the substructure
     * @return substructure
     */
    public static def getAt(Group delegate, String name) {
        if (delegate.isRepeating(name)) {
            return getStructure(delegate, name, [0])
        } else {
            return delegate.get(name)
        }
    }

    public static void from(Group delegate, value) {
        throw new UnsupportedOperationException('group copying not implemented yet')
    }

    /**
     * @return iterator over populated structures
     */
    public static Iterator iterator(Group delegate) {
        ReadOnlyMessageIterator.createPopulatedStructureIterator(delegate, Structure.class)
    }

    public static def call(Group delegate, args) {
        throw new HL7DslException("The group ${delegate.class.simpleName} is not repeatable in this group or message")
    }

    //==========================================================================
    // Message metaclass extensions
    // =========================================================================

    public static Message empty(Message delegate) {
        MessageUtils.empty(delegate)
    }

    public static Message copy(Message delegate) {
        MessageUtils.copy(delegate)
    }

    public static Writer writeTo(Message delegate, Writer writer) {
        String s = delegate.encode()
        writer.write(s)
        writer.flush()
        writer
    }

    // Helpers

    private static selector(elements, adapter, index) {
        new Repeatable(getClass(), elements, adapter, index)
    }

    private static componentIndex(int index) {
        if (index < 1) {
            throw new HL7DslException('component index must be in range 1..n')
        }
        index - 1
    }

    private static Object componentValue(Composite c) {
        def firstElement = c[1]
        firstElement instanceof Repeatable ?
                firstElement(0).value :
                firstElement.value
    }

    private static Object componentValue2(Composite c) {
        def firstElement = c[1]
        firstElement instanceof Repeatable ?
                firstElement(0).value2 :
                firstElement.value2
    }

    private static String stringValue(def object) {
        switch (object) {
            case Primitive: return object.value
            case Varies: return object.value
            default: return object.toString()
        }
    }

}