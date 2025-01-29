
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0058._5;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for infoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="infoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="positiveReport">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="notice" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                   &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="negativeReport">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="notice" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *                   &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "infoType", propOrder = {
    "positiveReport",
    "negativeReport"
})
public class InfoType {

    protected InfoType.PositiveReport positiveReport;
    protected InfoType.NegativeReport negativeReport;

    /**
     * Gets the value of the positiveReport property.
     * 
     * @return
     *     possible object is
     *     {@link InfoType.PositiveReport }
     *     
     */
    public InfoType.PositiveReport getPositiveReport() {
        return positiveReport;
    }

    /**
     * Sets the value of the positiveReport property.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoType.PositiveReport }
     *     
     */
    public void setPositiveReport(InfoType.PositiveReport value) {
        this.positiveReport = value;
    }

    /**
     * Gets the value of the negativeReport property.
     * 
     * @return
     *     possible object is
     *     {@link InfoType.NegativeReport }
     *     
     */
    public InfoType.NegativeReport getNegativeReport() {
        return negativeReport;
    }

    /**
     * Sets the value of the negativeReport property.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoType.NegativeReport }
     *     
     */
    public void setNegativeReport(InfoType.NegativeReport value) {
        this.negativeReport = value;
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
     *         &lt;element name="notice" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *         &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
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
        "notice",
        "data"
    })
    public static class NegativeReport {

        @XmlElement(required = true)
        protected Object notice;
        protected Object data;

        /**
         * Gets the value of the notice property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getNotice() {
            return notice;
        }

        /**
         * Sets the value of the notice property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setNotice(Object value) {
            this.notice = value;
        }

        /**
         * Gets the value of the data property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getData() {
            return data;
        }

        /**
         * Sets the value of the data property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setData(Object value) {
            this.data = value;
        }

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
     *         &lt;element name="notice" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
     *         &lt;element name="data" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
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
        "notice",
        "data"
    })
    public static class PositiveReport {

        @XmlElement(required = true)
        protected Object notice;
        protected Object data;

        /**
         * Gets the value of the notice property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getNotice() {
            return notice;
        }

        /**
         * Sets the value of the notice property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setNotice(Object value) {
            this.notice = value;
        }

        /**
         * Gets the value of the data property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getData() {
            return data;
        }

        /**
         * Sets the value of the data property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setData(Object value) {
            this.data = value;
        }

    }

}
