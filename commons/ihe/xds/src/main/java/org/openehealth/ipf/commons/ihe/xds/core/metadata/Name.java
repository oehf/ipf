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

import ca.uhn.hl7v2.model.Composite;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.NameAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Objects;

/**
 * This class represents a name.
 * <p>
 * It is derived from the HL7v2 data types XPN and XCN. It only contains
 * naming related fields of these data types.
 * <p>
 * All members of this class are allowed to be <code>null</code>. When transforming
 * to HL7 this indicates that the values are empty. Trailing empty values are 
 * removed from the HL7 string.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlJavaTypeAdapter(value = NameAdapter.class)
@XmlType(name = "Name", propOrder = {"prefix", "givenName", "secondAndFurtherGivenNames",
        "familyName", "suffix", "degree"})
public abstract class Name<T extends Composite> extends Hl7v2Based<T> {
    private static final long serialVersionUID = -3455779057944896901L;

    protected Name() {
    }

    protected Name(T hapiObject) {
        super(hapiObject);
    }


    @XmlElement(name = "family")
    abstract public String getFamilyName();                  // XCN.2.1, XPN.1.1
    @XmlElement(name = "given")
    abstract public String getGivenName();                   // XCN.3, XPN.2
    @XmlElement(name = "secondAndFurtherGiven")
    abstract public String getSecondAndFurtherGivenNames();  // XCN.4, XPN.3
    abstract public String getSuffix();                      // XCN.5, XPN.4
    abstract public String getPrefix();                      // XCN.6, XPN.5
    abstract public String getDegree();                      // XCN.7, XPN.6

    abstract public void setFamilyName(String value);                  // XCN.2.1, XPN.1.1
    abstract public void setGivenName(String value);                   // XCN.3, XPN.2
    abstract public void setSecondAndFurtherGivenNames(String value);  // XCN.4, XPN.3
    abstract public void setSuffix(String value);                      // XCN.5, XPN.4
    abstract public void setPrefix(String value);                      // XCN.6, XPN.5
    abstract public void setDegree(String value);                      // XCN.7, XPN.6


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Name)) return false;
        Name<?> that = (Name<?>) o;
        return Objects.equals(getFamilyName(), that.getFamilyName()) &&
                Objects.equals(getGivenName(), that.getGivenName()) &&
                Objects.equals(getSecondAndFurtherGivenNames(), that.getSecondAndFurtherGivenNames()) &&
                Objects.equals(getSuffix(), that.getSuffix()) &&
                Objects.equals(getPrefix(), that.getPrefix()) &&
                Objects.equals(getDegree(), that.getDegree());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFamilyName(), getGivenName(), getSecondAndFurtherGivenNames(),
                getSuffix(), getPrefix(), getDegree());
    }

    @Override
    public String toString() {
        return "Name(" +
                "familyName=" + getFamilyName() +
                ", givenName=" + getGivenName() +
                ", secondAndFurtherGivenNames=" + getSecondAndFurtherGivenNames() +
                ", suffix=" + getSuffix() +
                ", prefix=" + getPrefix() +
                ", degree=" + getDegree() +
                ')';
    }
}
