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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import ca.uhn.hl7v2.model.v25.datatype.XCN;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import java.util.Objects;

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
 * @author Dmytro Rud
 */
@XmlAccessorType()
@XmlType(name = "Person", propOrder = {"id", "name"})
public class Person extends Hl7v2Based<XCN> {
    private static final long serialVersionUID = 1775227207521668959L;
    
    /**
     * Constructs a person.
     */
    public Person() {
        super(new XCN(MESSAGE));
    }


    /**
     * Constructs a person.
     */
    public Person(XCN xcn) {
        super(xcn);
    }


    /**
     * Constructs a person.
     * @param id
     *          the id of the person (XCN.1 and XCN.9).
     * @param name
     *          the name of the person (XCN.2.1, XCN.3, XCN.4, XCN.5, XCN.6, XCN.7).
     */
    public Person(Identifiable id, Name name) {
        this();
        setId(id);
        setName(name);
    }

    /**
     * @return the id of the person (XCN.1 and XCN.9).
     */
    public Identifiable getId() {
        return new Identifiable(
                getHapiObject().getXcn1_IDNumber().getValue(),
                new AssigningAuthority(getHapiObject().getXcn9_AssigningAuthority()));
    }

    /**
     * @param id
     *          the id of the person (XCN.1 and XCN.9).
     */
    public void setId(Identifiable id) {
        if (id != null) {
            setValue(getHapiObject().getXcn1_IDNumber(), id.getId());
            setAssigningAuthority(id.getAssigningAuthority(), getHapiObject().getXcn9_AssigningAuthority());
        } else {
            getHapiObject().getXcn1_IDNumber().clear();
            getHapiObject().getXcn9_AssigningAuthority().clear();
        }
    }

    /**
     * @return the name of the person (XCN.2.1, XCN.3, XCN.4, XCN.5, XCN.6, XCN.7).
     */
    public Name getName() {
        var name = new XcnName(getHapiObject());
        return name.isEmpty() ? null : name;
    }

    /**
     * @param name
     *          the name of the person (XCN.2.1, XCN.3, XCN.4, XCN.5, XCN.6, XCN.7).
     */
    public void setName(Name name) {
        if (name != null) {
            var thisName = new XcnName(getHapiObject());
            thisName.copyFrom(name);
        }
        else {
            var xcn = getHapiObject();
            xcn.getXcn2_FamilyName().clear();
            xcn.getXcn3_GivenName().clear();
            xcn.getXcn4_SecondAndFurtherGivenNamesOrInitialsThereof().clear();
            xcn.getXcn5_SuffixEgJRorIII().clear();
            xcn.getXcn6_PrefixEgDR().clear();
            xcn.getXcn7_DegreeEgMD().clear();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (Person) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return "Person(" +
                "id=" + getId() +
                ", name=" + getName() +
                ')';
    }
}
