
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0213._1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlElementRefs;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0058._5.HeaderType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1.PersonToUPIType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1.PidsToUPIType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="header" type="{http://www.ech.ch/xmlns/eCH-0058/5}headerType"/>
 *         &lt;element name="content">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="SPIDCategory" type="{http://www.ech.ch/xmlns/eCH-0044/4}personIdCategoryType"/>
 *                   &lt;element name="responseLanguage" type="{http://www.ech.ch/xmlns/eCH-0011/8}languageType"/>
 *                   &lt;element name="actionOnSPID" type="{http://www.ech.ch/xmlns/eCH-0213/1}actionOnSPIDType"/>
 *                   &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *                     &lt;element name="additionalInputParameterKey" type="{http://www.ech.ch/xmlns/eCH-0213/1}additionalInputParameterKeyType"/>
 *                     &lt;element name="additionalInputParameterValue" type="{http://www.ech.ch/xmlns/eCH-0213/1}additionalInputParameterValueType"/>
 *                   &lt;/sequence>
 *                   &lt;sequence maxOccurs="2">
 *                     &lt;element name="pidsToUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsToUPIType"/>
 *                   &lt;/sequence>
 *                   &lt;element name="personToUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personToUPIType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="minorVersion" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "header",
    "content"
})
@XmlRootElement(name = "request")
public class Request {

    @XmlElement(required = true)
    protected HeaderType header;
    @XmlElement(required = true)
    protected Request.Content content;
    @XmlAttribute(name = "minorVersion", required = true)
    protected BigInteger minorVersion;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link HeaderType }
     *     
     */
    public HeaderType getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeaderType }
     *     
     */
    public void setHeader(HeaderType value) {
        this.header = value;
    }

    /**
     * Gets the value of the content property.
     * 
     * @return
     *     possible object is
     *     {@link Request.Content }
     *     
     */
    public Request.Content getContent() {
        return content;
    }

    /**
     * Sets the value of the content property.
     * 
     * @param value
     *     allowed object is
     *     {@link Request.Content }
     *     
     */
    public void setContent(Request.Content value) {
        this.content = value;
    }

    /**
     * Gets the value of the minorVersion property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getMinorVersion() {
        return minorVersion;
    }

    /**
     * Sets the value of the minorVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setMinorVersion(BigInteger value) {
        this.minorVersion = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="SPIDCategory" type="{http://www.ech.ch/xmlns/eCH-0044/4}personIdCategoryType"/>
     *         &lt;element name="responseLanguage" type="{http://www.ech.ch/xmlns/eCH-0011/8}languageType"/>
     *         &lt;element name="actionOnSPID" type="{http://www.ech.ch/xmlns/eCH-0213/1}actionOnSPIDType"/>
     *         &lt;sequence maxOccurs="unbounded" minOccurs="0">
     *           &lt;element name="additionalInputParameterKey" type="{http://www.ech.ch/xmlns/eCH-0213/1}additionalInputParameterKeyType"/>
     *           &lt;element name="additionalInputParameterValue" type="{http://www.ech.ch/xmlns/eCH-0213/1}additionalInputParameterValueType"/>
     *         &lt;/sequence>
     *         &lt;sequence maxOccurs="2">
     *           &lt;element name="pidsToUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}pidsToUPIType"/>
     *         &lt;/sequence>
     *         &lt;element name="personToUPI" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}personToUPIType" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "spidCategory",
        "responseLanguage",
        "actionOnSPID",
        "additionalInputParameterKeyAndAdditionalInputParameterValue",
        "pidsToUPI",
        "personToUPI"
    })
    public static class Content {

        @XmlElement(name = "SPIDCategory", required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "token")
        protected String spidCategory;
        @XmlElement(required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "token")
        protected String responseLanguage;
        @XmlElement(required = true)
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlSchemaType(name = "token")
        protected String actionOnSPID;
        @XmlElementRefs({
            @XmlElementRef(name = "additionalInputParameterValue", namespace = "http://www.ech.ch/xmlns/eCH-0213/1", type = JAXBElement.class, required = false),
            @XmlElementRef(name = "additionalInputParameterKey", namespace = "http://www.ech.ch/xmlns/eCH-0213/1", type = JAXBElement.class, required = false)
        })
        protected List<JAXBElement<String>> additionalInputParameterKeyAndAdditionalInputParameterValue;
        @XmlElement(required = true)
        protected List<PidsToUPIType> pidsToUPI;
        protected PersonToUPIType personToUPI;

        /**
         * Gets the value of the spidCategory property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getSPIDCategory() {
            return spidCategory;
        }

        /**
         * Sets the value of the spidCategory property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setSPIDCategory(String value) {
            this.spidCategory = value;
        }

        /**
         * Gets the value of the responseLanguage property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getResponseLanguage() {
            return responseLanguage;
        }

        /**
         * Sets the value of the responseLanguage property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setResponseLanguage(String value) {
            this.responseLanguage = value;
        }

        /**
         * Gets the value of the actionOnSPID property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getActionOnSPID() {
            return actionOnSPID;
        }

        /**
         * Sets the value of the actionOnSPID property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setActionOnSPID(String value) {
            this.actionOnSPID = value;
        }

        /**
         * Gets the value of the additionalInputParameterKeyAndAdditionalInputParameterValue property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the additionalInputParameterKeyAndAdditionalInputParameterValue property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getAdditionalInputParameterKeyAndAdditionalInputParameterValue().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link JAXBElement }{@code <}{@link String }{@code >}
         * {@link JAXBElement }{@code <}{@link String }{@code >}
         * 
         * 
         */
        public List<JAXBElement<String>> getAdditionalInputParameterKeyAndAdditionalInputParameterValue() {
            if (additionalInputParameterKeyAndAdditionalInputParameterValue == null) {
                additionalInputParameterKeyAndAdditionalInputParameterValue = new ArrayList<JAXBElement<String>>();
            }
            return this.additionalInputParameterKeyAndAdditionalInputParameterValue;
        }

        /**
         * Gets the value of the pidsToUPI property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the pidsToUPI property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPidsToUPI().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PidsToUPIType }
         * 
         * 
         */
        public List<PidsToUPIType> getPidsToUPI() {
            if (pidsToUPI == null) {
                pidsToUPI = new ArrayList<PidsToUPIType>();
            }
            return this.pidsToUPI;
        }

        /**
         * Gets the value of the personToUPI property.
         * 
         * @return
         *     possible object is
         *     {@link PersonToUPIType }
         *     
         */
        public PersonToUPIType getPersonToUPI() {
            return personToUPI;
        }

        /**
         * Sets the value of the personToUPI property.
         * 
         * @param value
         *     allowed object is
         *     {@link PersonToUPIType }
         *     
         */
        public void setPersonToUPI(PersonToUPIType value) {
            this.personToUPI = value;
        }

    }

}
