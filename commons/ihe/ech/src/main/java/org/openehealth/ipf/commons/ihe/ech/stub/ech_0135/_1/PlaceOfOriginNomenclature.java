
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0135._1;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


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
 *         &lt;element name="placeOfOrigins">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="placeOfOrigin" type="{http://www.ech.ch/xmlns/eCH-0135/1}placeOfOriginType" maxOccurs="unbounded"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "placeOfOrigins"
})
@XmlRootElement(name = "placeOfOriginNomenclature")
public class PlaceOfOriginNomenclature {

    @XmlElement(required = true)
    protected PlaceOfOriginNomenclature.PlaceOfOrigins placeOfOrigins;

    /**
     * Gets the value of the placeOfOrigins property.
     * 
     * @return
     *     possible object is
     *     {@link PlaceOfOriginNomenclature.PlaceOfOrigins }
     *     
     */
    public PlaceOfOriginNomenclature.PlaceOfOrigins getPlaceOfOrigins() {
        return placeOfOrigins;
    }

    /**
     * Sets the value of the placeOfOrigins property.
     * 
     * @param value
     *     allowed object is
     *     {@link PlaceOfOriginNomenclature.PlaceOfOrigins }
     *     
     */
    public void setPlaceOfOrigins(PlaceOfOriginNomenclature.PlaceOfOrigins value) {
        this.placeOfOrigins = value;
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
     *         &lt;element name="placeOfOrigin" type="{http://www.ech.ch/xmlns/eCH-0135/1}placeOfOriginType" maxOccurs="unbounded"/>
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
        "placeOfOrigin"
    })
    public static class PlaceOfOrigins {

        @XmlElement(required = true)
        protected List<PlaceOfOriginType> placeOfOrigin;

        /**
         * Gets the value of the placeOfOrigin property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the placeOfOrigin property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPlaceOfOrigin().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PlaceOfOriginType }
         * 
         * 
         */
        public List<PlaceOfOriginType> getPlaceOfOrigin() {
            if (placeOfOrigin == null) {
                placeOfOrigin = new ArrayList<PlaceOfOriginType>();
            }
            return this.placeOfOrigin;
        }

    }

}
