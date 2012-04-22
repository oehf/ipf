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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.BeanUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

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
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
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
        XcnName name = new XcnName(getHapiObject());
        return name.isEmpty() ? null : name;
    }

    /**
     * @param name
     *          the name of the person (XCN.2.1, XCN.3, XCN.4, XCN.5, XCN.6, XCN.7).
     */
    public void setName(Name name) {
        if (name != null) {
            BeanUtils.copyProperties(name, new XcnName(getHapiObject()));
        }
        else {
            XCN xcn = getHapiObject();
            xcn.getXcn2_FamilyName().clear();
            xcn.getXcn3_GivenName().clear();
            xcn.getXcn4_SecondAndFurtherGivenNamesOrInitialsThereof().clear();
            xcn.getXcn5_SuffixEgJRorIII().clear();
            xcn.getXcn6_PrefixEgDR().clear();
            xcn.getXcn7_DegreeEgMD().clear();
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
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
        if (getId() == null) {
            if (other.getId() != null)
                return false;
        } else if (!getId().equals(other.getId()))
            return false;
        if (getName() == null) {
            if (other.getName() != null)
                return false;
        } else if (!getName().equals(other.getName()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", getId())
                .append("name", getName())
                .toString();
    }
}
