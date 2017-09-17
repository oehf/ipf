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

import ca.uhn.hl7v2.model.v25.datatype.XAD;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Objects;

/**
 * Represents the address of a patient.
 * <p>
 * This class contains a subset of the fields from the HL7v2.5 XAD data type.
 * <p>
 * All members of this class are allowed to be <code>null</code>. When transforming
 * to HL7 this indicates that the values are empty. Trailing empty values are 
 * removed from the HL7 string.
 * @author Jens Riemschneider
 * @author Dmytro Rud
 */
@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlType(name = "Address", propOrder = {
        "streetAddress", "otherDesignation", "city", "countyParishCode", "stateOrProvince", "country", 
        "zipOrPostalCode"})
public class Address extends Hl7v2Based<XAD> {
    private static final long serialVersionUID = -5050715144917393181L;

    public Address() {
        super(new XAD(MESSAGE));
    }

    public Address(XAD xad) {
        super(xad);
    }


    /**
     * @return the street address (XAD.1).
     */
    public String getStreetAddress() {
        return getHapiObject().getXad1_StreetAddress().getSad1_StreetOrMailingAddress().getValue();
    }

    /**
     * @param streetAddress
     *          the street address (XAD.1).
     */
    public void setStreetAddress(String streetAddress) {
        setValue(getHapiObject().getXad1_StreetAddress().getSad1_StreetOrMailingAddress(), streetAddress);
    }

    /**
     * @return the other designation (XAD.2).
     */
    @XmlElement(name = "additionalLocator")
    public String getOtherDesignation() {
        return getHapiObject().getXad2_OtherDesignation().getValue();
    }

    /**
     * @param otherDesignation
     *          the other designation (XAD.2).
     */
    public void setOtherDesignation(String otherDesignation) {
        setValue(getHapiObject().getXad2_OtherDesignation(), otherDesignation);
    }

    /**
     * @return the city (XAD.3).
     */
    public String getCity() {
        return getHapiObject().getXad3_City().getValue();
    }

    /**
     * @param city
     *          the city (XAD.3).
     */
    public void setCity(String city) {
        setValue(getHapiObject().getXad3_City(), city);
    }

    /**
     * @return the state or province (XAD.4).
     */
    @XmlElement(name = "state")
    public String getStateOrProvince() {
        return getHapiObject().getXad4_StateOrProvince().getValue();
    }

    /**
     * @param stateOrProvince
     *          the state or province (XAD.4).
     */
    public void setStateOrProvince(String stateOrProvince) {
        setValue(getHapiObject().getXad4_StateOrProvince(), stateOrProvince);
    }

    /**
     * @return the zip or postal code (XAD.5).
     */
    @XmlElement(name = "postalCode")
    public String getZipOrPostalCode() {
        return getHapiObject().getXad5_ZipOrPostalCode().getValue();
    }

    /**
     * @param zipOrPostalCode
     *          the zip or postal code (XAD.5).
     */
    public void setZipOrPostalCode(String zipOrPostalCode) {
        setValue(getHapiObject().getXad5_ZipOrPostalCode(), zipOrPostalCode);
    }

    /**
     * @return the country (XAD.6).
     */
    public String getCountry() {
        return getHapiObject().getXad6_Country().getValue();
    }

    /**
     * @param country
     *          the country (XAD.6).
     */
    public void setCountry(String country) {
        setValue(getHapiObject().getXad6_Country(), country);
    }

    /**
     * @return the county parish code (XAD.9).
     */
    @XmlElement(name = "county")
    public String getCountyParishCode() {
        return getHapiObject().getXad9_CountyParishCode().getValue();
    }

    /**
     * @param countyParishCode
     *          the county parish code (XAD.9).
     */
    public void setCountyParishCode(String countyParishCode) {
        setValue(getHapiObject().getXad9_CountyParishCode(), countyParishCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address that = (Address) o;
        return Objects.equals(getCity(), that.getCity()) &&
                Objects.equals(getCountry(), that.getCountry()) &&
                Objects.equals(getCountyParishCode(), that.getCountyParishCode()) &&
                Objects.equals(getOtherDesignation(), that.getOtherDesignation()) &&
                Objects.equals(getStateOrProvince(), that.getStateOrProvince()) &&
                Objects.equals(getStreetAddress(), that.getStreetAddress()) &&
                Objects.equals(getZipOrPostalCode(), that.getZipOrPostalCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getCity(), getCountry(), getCountyParishCode(), getOtherDesignation(),
                getStateOrProvince(), getStreetAddress(), getZipOrPostalCode());
    }

    @Override
    public String toString() {
        return "Address(" +
                "streetAddress=" + getStreetAddress() +
                ", otherDesignation=" + getOtherDesignation() +
                ", city=" + getCity() +
                ", stateOrProvince=" + getStateOrProvince() +
                ", zipOrPostalCode=" + getZipOrPostalCode() +
                ", country=" + getCountry() +
                ", countyParishCode=" + getCountyParishCode() +
                ')';
    }
}
