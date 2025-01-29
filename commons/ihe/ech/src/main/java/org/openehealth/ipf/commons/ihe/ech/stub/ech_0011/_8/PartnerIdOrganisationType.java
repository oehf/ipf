
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0011._8;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;
import org.openehealth.ipf.commons.ihe.ech.stub.ech_0044._4.NamedPersonIdType;


/**
 * <p>Java class for partnerIdOrganisationType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="partnerIdOrganisationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="localPersonId" type="{http://www.ech.ch/xmlns/eCH-0044/4}namedPersonIdType"/>
 *         &lt;element name="otherPersonId" type="{http://www.ech.ch/xmlns/eCH-0044/4}namedPersonIdType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "partnerIdOrganisationType", propOrder = {
    "localPersonId",
    "otherPersonId"
})
public class PartnerIdOrganisationType {

    @XmlElement(required = true)
    protected NamedPersonIdType localPersonId;
    protected List<NamedPersonIdType> otherPersonId;

    /**
     * Gets the value of the localPersonId property.
     * 
     * @return
     *     possible object is
     *     {@link NamedPersonIdType }
     *     
     */
    public NamedPersonIdType getLocalPersonId() {
        return localPersonId;
    }

    /**
     * Sets the value of the localPersonId property.
     * 
     * @param value
     *     allowed object is
     *     {@link NamedPersonIdType }
     *     
     */
    public void setLocalPersonId(NamedPersonIdType value) {
        this.localPersonId = value;
    }

    /**
     * Gets the value of the otherPersonId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the otherPersonId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOtherPersonId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NamedPersonIdType }
     * 
     * 
     */
    public List<NamedPersonIdType> getOtherPersonId() {
        if (otherPersonId == null) {
            otherPersonId = new ArrayList<NamedPersonIdType>();
        }
        return this.otherPersonId;
    }

}
