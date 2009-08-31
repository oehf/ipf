/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.metadata;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Represents an identifiable person.
 * <p>
 * This class contains members from the HL7v2 XCN data type. The XDS profile
 * imposes some limitations on the XCN type. Most notably the XCN.9
 * component has the same restrictions as the CX.4 component (as described
 * in {@link Identifiable}. 
 * All of this class are allowed to be <code>null</code>. When transforming  
 * to HL7 this indicates that the values are empty. Trailing empty  
 * values are removed from the HL7 string.
 * @author Jens Riemschneider
 */
public class Person implements Serializable {    
    private static final long serialVersionUID = 1775227207521668959L;
    
    private Identifiable id;        // XCN.1 and XCN.9
    private Name name;              // XCN.2.1, XCN.3, XCN.4, XCN.5, XCN.6

    /**
     * Constructs a person.
     */
    public Person() {}
    
    /**
     * Constructs a person.
     * @param id
     *          the id of the person (XCN.1 and XCN.9).
     * @param name
     *          the name of the person (XCN.2.1, XCN.3, XCN.4, XCN.5, XCN.6).
     */
    public Person(Identifiable id, Name name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return the id of the person (XCN.1 and XCN.9).
     */
    public Identifiable getId() {
        return id;
    }

    /**
     * @param id
     *          the id of the person (XCN.1 and XCN.9).
     */
    public void setId(Identifiable id) {
        this.id = id;
    }

    /**
     * @return the name of the person (XCN.2.1, XCN.3, XCN.4, XCN.5, XCN.6).
     */
    public Name getName() {
        return name;
    }

    /**
     * @param name
     *          the name of the person (XCN.2.1, XCN.3, XCN.4, XCN.5, XCN.6).
     */
    public void setName(Name name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Person other = (Person) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
