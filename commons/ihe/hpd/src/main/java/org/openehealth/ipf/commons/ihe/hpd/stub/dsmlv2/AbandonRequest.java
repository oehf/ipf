
package org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AbandonRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AbandonRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn:oasis:names:tc:DSML:2:0:core}DsmlMessage"&gt;
 *       &lt;attribute name="abandonID" use="required" type="{urn:oasis:names:tc:DSML:2:0:core}RequestID" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbandonRequest")
public class AbandonRequest
    extends DsmlMessage
{

    @XmlAttribute(name = "abandonID", required = true)
    protected String abandonID;

    /**
     * Gets the value of the abandonID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAbandonID() {
        return abandonID;
    }

    /**
     * Sets the value of the abandonID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAbandonID(String value) {
        this.abandonID = value;
    }

}
