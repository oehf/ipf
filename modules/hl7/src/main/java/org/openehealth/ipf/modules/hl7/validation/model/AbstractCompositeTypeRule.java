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

import java.util.Collection;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.Location;
import ca.uhn.hl7v2.model.Composite;
import ca.uhn.hl7v2.model.DataTypeException;
import ca.uhn.hl7v2.model.Primitive;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.validation.ValidationException;
import ca.uhn.hl7v2.validation.builder.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mitko Kolev
 * @author Christian Ohr
 */
public abstract class AbstractCompositeTypeRule<T extends Composite> extends CompositeTypeRule<T> {

    public static final String EUI_64_PATTERN = "[0-9a-zA-Z]+";
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCompositeTypeRule.class);

    private final String appliesFor;
    private final String description;

    public AbstractCompositeTypeRule(Class<T> compositeTypeClass) {
        this(compositeTypeClass, compositeTypeClass.getSimpleName() + " composite type rule");
    }

    public AbstractCompositeTypeRule(Class<T> compositeTypeClass, String description) {
        this.appliesFor = compositeTypeClass.getSimpleName();
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public boolean matches(Class<? extends Composite> clazz) {
        return clazz.getSimpleName().equals(appliesFor);
    }

    @Override
    public String getSectionReference() {
        return "";
    }

    @Override
    public ValidationException[] apply(Composite composite) {
        return apply((T) composite, Location.UNKNOWN);
    }

    @Override
    public ValidationException[] apply(T composite, Location location) {
        if (matches(composite.getClass())) {
            return validate(composite, location);
        } else {
            throw new IllegalArgumentException("The rule " + this.getClass().getSimpleName() + " does not apply for class " + composite.getClass().getName());
        }
    }

    public abstract  ValidationException[] validate(T struct, Location location);

    protected ValidationException[] violations(Object value, Location location) {
        String msg = String.format(getDescription(), String.valueOf(value));
        ValidationException ve = new ValidationException(msg);
        ve.setLocation(location);
        return new ValidationException[]{ve};
    }



    /**
     * Returns true if the element at component is empty, false otherwise;
     *
     * @param element   a {@link Composite} instance
     * @param component (starting at 1)
     * @return <code>true</code> if the <code>element</code> at <code>component</code> is empty,
     * false otherwise;
     */
    public boolean isEmpty(T element, int component) {
        try {
            return empty().evaluate(val(element, component));
        } catch (ValidationException e) {
            return false;
        }
    }

    public boolean isNonEmpty(T element, int component) {
        return !isEmpty(element, component);
    }

    public boolean isEqual(String expected, T element, int component) {
        try {
            return isEqual(expected).evaluate(val(element, component));
        } catch (ValidationException e) {
            return false;
        }
    }

    public ValidationException enforce(Predicate p, T element, int component) {
        String val = val(element, component);
        try {
            return p.evaluate(val) ?
                    null :
                    new ValidationException(String.format("requires to be %s", val, p.getDescription()));
        } catch (ValidationException e) {
            return e;
        }
    }

    protected AbstractCompositeTypeRule validate(ValidationException e, Location l, Collection<ValidationException> violations) {
        if (e != null ) {
            e.setLocation(l);
            violations.add(e);
        }
        return this;
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
            Type type = element.getComponent(component - 1);
            if (type instanceof Primitive) {
                return ((Primitive) type).getValue();
            } else if (type != null) {
                return type.encode();
            }
        } catch (DataTypeException dte) {
            LOG.warn("Unable to extract the value of the HL7 type.", dte);
        } catch (HL7Exception e) {
            LOG.error("Unable to get the fieldSeparatorValue.", e);
        }
        return null;
    }

    private String msgPrefix(String expected, String found) {
        String val = "".equals(found) ? " empty value " : found;
        return "Expected " + expected + ", found " + val;
    }

    protected String msg(String prefix, Composite element, int component, String path) {
        String reference = getSectionReference();
        String elementType = element.getClass().getSimpleName();
        return new StringBuilder()
                .append("Validation error: ")
                .append((reference == null) ? ("rule for type " + elementType) : reference)
                .append(" : ")
                .append(prefix)
                .append(" of type ")
                .append(elementType)
                .append(", component ")
                .append(component)
                .append(", path ")
                .append(path)
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
