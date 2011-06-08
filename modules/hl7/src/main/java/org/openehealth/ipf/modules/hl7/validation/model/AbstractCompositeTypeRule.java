/*
 * Copyright 2011 the original author or authors.
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
package org.openehealth.ipf.modules.hl7.validation.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Composite;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Primitive;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.validation.ValidationException;

/**
 * @author Mitko Kolev
 * 
 */
public abstract class AbstractCompositeTypeRule<T extends Composite> extends CompositeTypeRule<T> {

    private static final long serialVersionUID = 1861386949211093806L;

    private final String appliesFor;

    public static final String ISO_OID_PATTERN = "[1-9][0-9]*(\\.(0|([1-9][0-9]*)))+";

    public static final String EUI_64_PATTERN = "[0-9a-zA-Z]+";
    
    private static final Log LOG = LogFactory.getLog(AbstractCompositeTypeRule.class);

    public AbstractCompositeTypeRule(Class<T> compositeTypeClass) {
        this.appliesFor = compositeTypeClass.getSimpleName();
    }
    
    @Override
    public boolean appliesFor(Class<? extends Composite> clazz){
        return clazz.getSimpleName().equals(appliesFor);
    }

    public String getDescription() {
        return "Type rule for " + appliesFor;
    }

    @Override
    public ValidationException[] test(T struct, int fieldIndex, int repetition, String path) {
        ArrayList<ValidationException> violations = new ArrayList<ValidationException>();
        if(appliesFor(struct.getClass())){
            //Use the HL7 DSL convention for the repetition
            String rep = repetition == 0? "" :("(" + repetition + ")");
            validate(struct, path + rep + "[" + fieldIndex + "]", violations);
        }else {
            throw new IllegalArgumentException("The rule " + this.getClass().getSimpleName() + " does not apply for class " + struct.getClass().getName());
        }
        return (ValidationException[]) violations.toArray(new ValidationException[violations.size()]);
        
    }
    
    public abstract void validate(T struct, String path, Collection<ValidationException> violations);
   
    /**
     * Returns true if the element at component is empty, false otherwise;
     * 
     * @param element  a {@link Composite} instance
     * @param component (starting at 1)
     * @return <code>true</code> if the <code>element</code> at <code>component</code> is empty,
     * false otherwise; 
     */
    public boolean isEmpty(T element, int component) {
        String val = val(element, component);
        return (val == null) || (val.length() == 0);
    }

    /**
     * Returns true if the element at component is <b>not</b> empty, false otherwise;
     * 
     * @param element  a {@link Composite} instance
     * @param component (starting at 1)
     * @return <code>true</code> if the <code>element</code> at <code>component</code> is <b>not</b> empty,
     * false otherwise; 
     */
    public boolean isNonEmpty(T element, int component) {
        return !isEmpty(element, component);
    }
    
    public boolean isEqual(String expected, T element, int component) {
        boolean result = false;
        String val = val(element, component);
        if (expected.equals(val)) {
            result = true;
        }
        return result;
    }


    /**
     * If the component at <code>component</code> is empty, a new {@link ValidationException} 
     * will be added to the Collection of <code>violations</code>
     * 
     * @param element  a {@link Composite} instance
     * @param component (starting at 1)
     * @param path a path to the element
     * @param violations where to store the validation violations
     */
    public void mustBeNonEmpty(T element, int component, String path, Collection<ValidationException> violations) {
        if (isEmpty(element, component)) {
            String prefix = msgPrefix("non-empty value", "");
            violations.add(violation(msg(prefix, element, component, path)));
        }
    }
    
    /**
     * If the component at <code>component</code> is <b>non-empty</b>, a new {@link ValidationException} 
     * will be added to the Collection of <code>violations</code>
     * 
     * @param element  a {@link Composite} instance
     * @param component (starting at 1)
     * @param path a path to the element
     * @param violations where to store the validation violations
     */
    public void mustBeEmpty(T element, int component, String path, Collection<ValidationException> violations) {
        if (!isEmpty(element, component)) {
            String prefix = msgPrefix("empty value", val(element, component));
            violations.add(violation(msg(prefix, element, component, path)));
        }
    }

    /**
     * If the component at <code>component</code> is one of the 
     * <code>expected</code> values, a new {@link ValidationException} 
     * will be added to the Collection of <code>violations</code>
     * 
     * @param expected Array of expected values
     * @param element  a {@link Composite} instance
     * @param component (starting at 1)
     * @param path a path to the element
     * @param violations where to store the validation violations
     */
    public void mustBeOneOf(String[] expected, T element, int component, String path, Collection<ValidationException> violations) {
        String found = val(element, component);
        boolean isAllowed = false;
        for (String allowed : expected) {
            if (allowed.equals(found)) {
                isAllowed = true;
            }
        }
        if (!isAllowed) {
            String prefix = msgPrefix("one of " + Arrays.toString(expected), found);
            violations.add(violation(msg(prefix, element, component, path)));
        }
    }

    /**
     * If the component at <code>component</code> is <b>not equal</b> to 
     * <code>expected</code>, a new {@link ValidationException} 
     * will be added to the Collection of <code>violations</code>
     * 
     * @param expected the expected value
     * @param element  a {@link Composite} instance
     * @param component (starting at 1)
     * @param path a path to the element
     * @param violations where to store the validation violations
     */
    public void mustBeEqualTo(String expected, T element, int component, String path, Collection<ValidationException> violations) {
        String found = val(element, component);
        if (!expected.equals(found)){
            String prefix = msgPrefix(expected, found);
            violations.add(violation(msg(prefix, element, component, path)));
        }
    }
    
    /**
     * If the component at <code>component</code> is not an ISO OID, a new
     * {@link ValidationException} will be added to the Collection of
     * <code>violations</code>
     * 
     * @param element a {@link Composite} instance
     * @param component (starting at 1)
     * @param path a path to the element
     * @param violations where to store the validation violations
     */
    public void mustMatchIsoOid(T element, int component, String path, Collection<ValidationException> violations) {
        String found = val(element, component);
        boolean patternMatches = Pattern.compile(ISO_OID_PATTERN).matcher(found).matches();
        
        if (isEmpty(element, component) || !patternMatches) {
            String prefix = msgPrefix("ISO_OID pattern " + ISO_OID_PATTERN, found);
            violations.add(violation(msg(prefix, element, component, path)));
        }
    }

   

    /**
     * If the component at <code>component</code> is not an EUI-64 identifier, a new
     * {@link ValidationException} will be added to the Collection of
     * <code>violations</code>
     *
     * @param element a {@link Composite} instance
     * @param component (starting at 1)
     * @param path a path to the element
     * @param violations where to store the validation violations
     */
    public void mustMatchEui64(T element, int component, String path, Collection<ValidationException> violations) {
        String found = val(element, component);
        boolean patternMatches = Pattern.compile(EUI_64_PATTERN).matcher(found).matches();

        if (isEmpty(element, component) || !patternMatches) {
            String prefix = msgPrefix("EUI_64 pattern " +  EUI_64_PATTERN, found);
            violations.add(violation(msg(prefix, element, component, path)));
        }
    }

    protected ValidationException violation(String msg) {
        return new ValidationException(msg);
    }

    /**
     * @return a not-null String
     */
    protected String val(T element, int component) {
        String result = "";
        try {
            // components are starting at 0 in Composite
            Type res =  element.getComponent(component - 1);
            if (res instanceof Primitive){
                result = ((Primitive) res).getValue();
            } else if (res != null){
                result = res.encode();
            }
        } catch (DataTypeException dte) {
            LOG.warn("Unable to extract the value of the HL7 type.", dte);
        } catch (HL7Exception e) {
            LOG.error("Unable to get the fieldSeparatorValue.", e);
        }
        return result == null ? "" : result;
    }
    
    private String msgPrefix(String expected, String found) {
        String val = "".equals(found) ? "empty value" : found;
        return "Expected " + expected + ", found " + val;
    }
    
    protected String msg(String prefix, Composite element, int component, String path) {
        String reference = getSectionReference();
        String elementType = element.getClass().getSimpleName();
        return new StringBuilder()
                .append("Validation error - ")
                .append((reference == null) ? ("rule for type " + elementType) : reference)
                .append(" : ")
                .append(prefix)
                .append(" in type ")
                .append(elementType)
                .append(", path ")
                .append(path)
                .append(", component ")
                .append(component)
                .toString();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((appliesFor == null) ? 0 : appliesFor.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractCompositeTypeRule<?> other = (AbstractCompositeTypeRule<?>) obj;
        if (appliesFor == null) {
            if (other.appliesFor != null)
                return false;
        } else if (!appliesFor.equals(other.appliesFor))
            return false;
        return true;
    }
}
