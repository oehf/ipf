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

import javax.xml.bind.annotation.*;

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
@XmlType(name = "Telecom", propOrder = {"use", "type", "email", "countryCode", 
        "areaCityCode", "localNumber", "extension"})
public class Telecom extends Hl7v2Based<XTN> {
    private static final long serialVersionUID = 526836203236101658L;

    public Telecom() {
        super(new XTN(MESSAGE));
    }

    public Telecom(XTN xtn) {
        super(xtn);
    }

    @Deprecated
    public Telecom(String address, String addressType) {
        this();
        setAddress(address);
        setAddressType(addressType);
    }

    /**
     * Creates a telecom object containing an E-mail address.
     * @param email
     *      E-mail address (XTN-4).
     */
    public Telecom(String email) {
        this();
        setUse("NET");
        setType("Internet");
        setEmail(email);
    }

    /**
     * Creates a telecom object containing a phone number.
     * @param countryCode
     *      country code of phone number (XTN-5), can be <code>null</code>.
     * @param areaCityCode
     *      area/city code of phone number (XTN-6), can be <code>null</code>.
     * @param localNumber
     *      local part of the phone number (XTN-7).
     * @param extension
     *      extension of the phone number (XTN-8), can be <code>null</code>.
     */
    public Telecom(Integer countryCode, Integer areaCityCode, Integer localNumber, Integer extension) {
        this();
        setUse("PRN");
        setType("PH");
        setCountryCode(countryCode);
        setAreaCityCode(areaCityCode);
        setLocalNumber(localNumber);
        setExtension(extension);
    }

    /**
     * @return telecom use code (XTN-2) according to HL7 v.2.5 Table 0201.
     */
    @XmlElement(name = "use")
    public String getUse() {
        return getHapiObject().getXtn2_TelecommunicationUseCode().getValue();
    }

    /**
     * @param use
     *      telecom use code (XTN-2) according to HL7 v.2.5 Table 0201.
     */
    public void setUse(String use) {
        setValue(getHapiObject().getXtn2_TelecommunicationUseCode(), use);
    }

    /**
     * @return telecom type (XTN-3) according to HL7 v.2.5 Table 0202.
     */
    @XmlElement(name = "type")
    public String getType() {
        return getHapiObject().getXtn3_TelecommunicationEquipmentType().getValue();
    }

    /**
     * @param type
     *      telecom type (XTN-3) according to HL7 v.2.5 Table 0202.
     */
    public void setType(String type) {
        setValue(getHapiObject().getXtn3_TelecommunicationEquipmentType(), type);
    }

    /**
     * @return telecommunication equipment type (XTN-3) according to HL7 v.2.5 Table 0202.
     * @deprecated use {@link #getType()} instead.
     */
    @Deprecated
    @XmlTransient
    public String getAddressType() {
        return getType();
    }

    /**
     * @param addressType
     *      telecommunication equipment type (XTN-3) according to HL7 v.2.5 Table 0202.
     * @deprecated use {@link #setType(String)} instead.
     */
    @Deprecated
    public void setAddressType(String addressType) {
        setType(addressType);
    }

    /**
     * @return E-mail address (XTN-4).
     */
    @XmlElement(name = "email")
    public String getEmail() {
        return getHapiObject().getXtn4_EmailAddress().getValue();
    }

    /**
     * @param email
     *      E-mail address (XTN-4).
     */
    public void setEmail(String email) {
        setValue(getHapiObject().getXtn4_EmailAddress(), email);
    }

    /**
     * @return E-mail address (XTN-4).
     * @deprecated use {@link #getEmail()} instead.
     */
    @Deprecated
    @XmlTransient
    public String getAddress() {
        return getEmail();
    }

    /**
     * @param address
     *      E-mail address (XTN-4).
     * @deprecated use {@link #setEmail(String)} instead.
     */
    @Deprecated
    public void setAddress(String address) {
        setEmail(address);
    }

    /**
     * @return country code of phone number (XTN-5).
     */
    @XmlElement(name = "countryCode")
    public Integer getCountryCode() {
        return getIntegerValue(getHapiObject().getXtn5_CountryCode());
    }

    /**
     * @param countryCode
     *      country code of phone number (XTN-5).
     */
    public void setCountryCode(Integer countryCode) {
        setValue(getHapiObject().getXtn5_CountryCode(), countryCode);
    }

    /**
     * @return area/city code of phone number (XTN-6).
     */
    @XmlElement(name = "areaCityCode")
    public Integer getAreaCityCode() {
        return getIntegerValue(getHapiObject().getXtn6_AreaCityCode());
    }

    /**
     * @param areaCityCode
     *      area/city code of phone number (XTN-6).
     */
    public void setAreaCityCode(Integer areaCityCode) {
        setValue(getHapiObject().getXtn6_AreaCityCode(), areaCityCode);
    }

    /**
     * @return local part of the phone number (XTN-7).
     */
    @XmlElement(name = "localNumber")
    public Integer getLocalNumber() {
        return getIntegerValue(getHapiObject().getXtn7_LocalNumber());
    }

    /**
     * @param localNumber
     *      local part of the phone number (XTN-7).
     */
    public void setLocalNumber(Integer localNumber) {
        setValue(getHapiObject().getXtn7_LocalNumber(), localNumber);
    }

    /**
     * @return extension of the phone number (XTN-8).
     */
    @XmlElement(name = "extension")
    public Integer getExtension() {
        return getIntegerValue(getHapiObject().getXtn8_Extension());
    }

    /**
     * @param extension
     *      extension of the phone number (XTN-8).
     */
    public void setExtension(Integer extension) {
        setValue(getHapiObject().getXtn8_Extension(), extension);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Telecom telecom = (Telecom) o;

        if (getAreaCityCode() != null ? !getAreaCityCode().equals(telecom.getAreaCityCode()) : telecom.getAreaCityCode() != null)
            return false;
        if (getCountryCode() != null ? !getCountryCode().equals(telecom.getCountryCode()) : telecom.getCountryCode() != null)
            return false;
        if (getEmail() != null ? !getEmail().equals(telecom.getEmail()) : telecom.getEmail() != null) return false;
        if (getExtension() != null ? !getExtension().equals(telecom.getExtension()) : telecom.getExtension() != null)
            return false;
        if (getLocalNumber() != null ? !getLocalNumber().equals(telecom.getLocalNumber()) : telecom.getLocalNumber() != null)
            return false;
        if (getType() != null ? !getType().equals(telecom.getType()) : telecom.getType() != null) return false;
        if (getUse() != null ? !getUse().equals(telecom.getUse()) : telecom.getUse() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getUse() != null ? getUse().hashCode() : 0;
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getCountryCode() != null ? getCountryCode().hashCode() : 0);
        result = 31 * result + (getAreaCityCode() != null ? getAreaCityCode().hashCode() : 0);
        result = 31 * result + (getLocalNumber() != null ? getLocalNumber().hashCode() : 0);
        result = 31 * result + (getExtension() != null ? getExtension().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("use", getUse())
                .append("type", getType())
                .append("email", getEmail())
                .append("countryCode", getCountryCode())
                .append("areaCityCode", getAreaCityCode())
                .append("localNumber", getLocalNumber())
                .append("extension", getExtension())
                .toString();
    }

}
