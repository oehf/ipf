
package org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.protocol;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IDPListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IDPListType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:protocol}IDPEntry" maxOccurs="unbounded"/&gt;
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:protocol}GetComplete" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IDPListType", propOrder = {
    "idpEntry",
    "getComplete"
})
public class IDPListType {

    @XmlElement(name = "IDPEntry", required = true)
    protected List<IDPEntryType> idpEntry;
    @XmlElement(name = "GetComplete")
    @XmlSchemaType(name = "anyURI")
    protected String getComplete;

    /**
     * Gets the value of the idpEntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the idpEntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIDPEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IDPEntryType }
     * 
     * 
     */
    public List<IDPEntryType> getIDPEntry() {
        if (idpEntry == null) {
            idpEntry = new ArrayList<IDPEntryType>();
        }
        return this.idpEntry;
    }

    /**
     * Gets the value of the getComplete property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetComplete() {
        return getComplete;
    }

    /**
     * Sets the value of the getComplete property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetComplete(String value) {
        this.getComplete = value;
    }

}
