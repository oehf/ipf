/*
 * Copyright 2012 the original author or authors.
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

import ca.uhn.hl7v2.model.v25.datatype.XTN;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents a telecommunication address.
 * <p>
 * This class contains members from the HL7 v.2.5 XTN data type.
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * When transforming to HL7 this indicates that the values are empty.
 * Trailing empty values are removed from the HL7 string.
 *
 * @author Dmytro Rud
 */
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "Telecom", propOrder = {"address", "addressType"})
public class Telecom extends Hl7v2Based<XTN> {
    private static final long serialVersionUID = 526836203236101658L;

    public Telecom() {
        super(new XTN(MESSAGE));
    }

    public Telecom(XTN xtn) {
        super(xtn);
    }

    public Telecom(String address, String addressType) {
        this();
        setAddress(address);
        setAddressType(addressType);
    }

    /**
     * @return telecommunication address (XTN-4).
     */
    @XmlElement(name = "address")
    public String getAddress() {
        return getHapiObject().getXtn4_EmailAddress().getValue();
    }

    /**
     * @param address
     *      telecommunication address (XTN-4).
     */
    public void setAddress(String address) {
        setValue(getHapiObject().getXtn4_EmailAddress(), address);
    }

    /**
     * @return telecommunication address type (XTN-3) according to HL7 v.2.5 Table 0202.
     */
    @XmlElement(name = "addressType")
    public String getAddressType() {
        return getHapiObject().getXtn3_TelecommunicationEquipmentType().getValue();
    }

    /**
     * @param addressType
     *      telecommunication address type (XTN-3) according to HL7 v.2.5 Table 0202.
     */
    public void setAddressType(String addressType) {
        setValue(getHapiObject().getXtn3_TelecommunicationEquipmentType(), addressType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Telecom)) return false;
        Telecom telecom = (Telecom) o;
        if (getAddress() != null ? !getAddress().equals(telecom.getAddress()) : telecom.getAddress() != null)
            return false;
        if (getAddressType() != null ? !getAddressType().equals(telecom.getAddressType()) : telecom.getAddressType() != null)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = getAddress() != null ? getAddress().hashCode() : 0;
        result = 31 * result + (getAddressType() != null ? getAddressType().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("address", getAddress())
                .append("addressType", getAddressType())
                .toString();
    }

}
