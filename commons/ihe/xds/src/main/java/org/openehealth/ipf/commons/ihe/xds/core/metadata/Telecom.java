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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

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
@XmlAccessorType
@XmlType(name = "Telecom", propOrder = {"use", "type", "email", "countryCode",
        "areaCityCode", "localNumber", "extension", "unformattedPhoneNumber"})
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
     *
     * @param email E-mail address (XTN-4).
     */
    public Telecom(String email) {
        this();
        setUse("NET");
        setType("Internet");
        setEmail(email);
    }

    /**
     * Creates a telecom object containing a phone number.
     *
     * @param countryCode  country code of phone number (XTN-5), can be <code>null</code>.
     * @param areaCityCode area/city code of phone number (XTN-6), can be <code>null</code>.
     * @param localNumber  local part of the phone number (XTN-7).
     * @param extension    extension of the phone number (XTN-8), can be <code>null</code>.
     */
    public Telecom(Long countryCode, Long areaCityCode, Long localNumber, Long extension) {
        this();
        setUse("PRN");
        setType("PH");
        setCountryCode(countryCode);
        setAreaCityCode(areaCityCode);
        setLocalNumber(localNumber);
        setExtension(extension);
    }

    /**
     * @param countryCode
     * @param areaCityCode
     * @param localNumber
     * @param extension
     * @deprecated
     */
    @Deprecated
    public Telecom(Integer countryCode, Integer areaCityCode, Integer localNumber, Integer extension) {
        this(
                countryCode == null ? null : countryCode.longValue(),
                areaCityCode == null ? null : areaCityCode.longValue(),
                localNumber == null ? null : localNumber.longValue(),
                extension == null ? null : extension.longValue());
    }

    /**
     * @return telecom use code (XTN-2) according to HL7 v.2.5 Table 0201.
     */
    @XmlElement(name = "use")
    public String getUse() {
        return getHapiObject().getXtn2_TelecommunicationUseCode().getValue();
    }

    /**
     * @param use telecom use code (XTN-2) according to HL7 v.2.5 Table 0201.
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
     * @param type telecom type (XTN-3) according to HL7 v.2.5 Table 0202.
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
     * @param addressType telecommunication equipment type (XTN-3) according to HL7 v.2.5 Table 0202.
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
     * @param email E-mail address (XTN-4).
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
     * @param address E-mail address (XTN-4).
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
    public Long getCountryCode() {
        return getLongValue(getHapiObject().getXtn5_CountryCode());
    }

    /**
     * @param countryCode country code of phone number (XTN-5).
     */
    public void setCountryCode(Long countryCode) {
        setValue(getHapiObject().getXtn5_CountryCode(), countryCode == null ? null : countryCode.toString());
    }

    /**
     * @return area/city code of phone number (XTN-6).
     */
    @XmlElement(name = "areaCityCode")
    public Long getAreaCityCode() {
        return getLongValue(getHapiObject().getXtn6_AreaCityCode());
    }

    /**
     * @param areaCityCode area/city code of phone number (XTN-6).
     */
    public void setAreaCityCode(Long areaCityCode) {
        setValue(getHapiObject().getXtn6_AreaCityCode(), areaCityCode == null ? null : areaCityCode.toString());
    }

    /**
     * @return local part of the phone number (XTN-7).
     */
    @XmlElement(name = "localNumber")
    public Long getLocalNumber() {
        return getLongValue(getHapiObject().getXtn7_LocalNumber());
    }

    /**
     * @param localNumber local part of the phone number (XTN-7).
     */
    public void setLocalNumber(Long localNumber) {
        setValue(getHapiObject().getXtn7_LocalNumber(), localNumber == null ? null : localNumber.toString() );
    }

    /**
     * @return extension of the phone number (XTN-8).
     */
    @XmlElement(name = "extension")
    public Long getExtension() {
        return getLongValue(getHapiObject().getXtn8_Extension());
    }

    /**
     * @param extension extension of the phone number (XTN-8).
     */
    public void setExtension(Long extension) {
        setValue(getHapiObject().getXtn8_Extension(), extension == null ? null : extension.toString());
    }

    /**
     * @return unformatted phone number (XTN-12).
     */
    @XmlElement(name = "unformattedPhoneNumber")
    public String getUnformattedPhoneNumber() {
        return getHapiObject().getXtn12_UnformattedTelephoneNumber().getValue();
    }

    /**
     * @param unformattedPhoneNumber unformatted the phone number (XTN-12).
     */
    public void setUnformattedPhoneNumber(String unformattedPhoneNumber) {
        setValue(getHapiObject().getXtn12_UnformattedTelephoneNumber(), unformattedPhoneNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Telecom that = (Telecom) o;
        return Objects.equals(getUse(), that.getUse()) &&
                Objects.equals(getType(), that.getType()) &&
                Objects.equals(getEmail(), that.getEmail()) &&
                Objects.equals(getCountryCode(), that.getCountryCode()) &&
                Objects.equals(getAreaCityCode(), that.getAreaCityCode()) &&
                Objects.equals(getLocalNumber(), that.getLocalNumber()) &&
                Objects.equals(getExtension(), that.getExtension()) &&
                Objects.equals(getUnformattedPhoneNumber(), that.getUnformattedPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUse(), getType(), getEmail(), getCountryCode(),
                getAreaCityCode(), getLocalNumber(), getExtension(), getUnformattedPhoneNumber());
    }

    @Override
    public String toString() {
        return "Telecom(" +
                "use=" + getUse() +
                ", type=" + getType() +
                ", email=" + getEmail() +
                ", countryCode=" + getCountryCode() +
                ", areaCityCode=" + getAreaCityCode() +
                ", localNumber=" + getLocalNumber() +
                ", extension=" + getExtension() +
                ", unformattedPhoneNumber=" + getUnformattedPhoneNumber() +
                ')';
    }
}
