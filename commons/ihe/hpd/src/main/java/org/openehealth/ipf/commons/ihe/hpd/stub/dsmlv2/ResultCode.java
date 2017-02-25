
package org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ResultCode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ResultCode"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;attribute name="code" use="required" type="{http://www.w3.org/2001/XMLSchema}int" /&gt;
 *       &lt;attribute name="descr" type="{urn:oasis:names:tc:DSML:2:0:core}LDAPResultCode" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ResultCode")
public class ResultCode {

    @XmlAttribute(name = "code", required = true)
    protected int code;
    @XmlAttribute(name = "descr")
    protected LDAPResultCode descr;

    /**
     * Gets the value of the code property.
     * 
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     */
    public void setCode(int value) {
        this.code = value;
    }

    /**
     * Gets the value of the descr property.
     * 
     * @return
     *     possible object is
     *     {@link LDAPResultCode }
     *     
     */
    public LDAPResultCode getDescr() {
        return descr;
    }

    /**
     * Sets the value of the descr property.
     * 
     * @param value
     *     allowed object is
     *     {@link LDAPResultCode }
     *     
     */
    public void setDescr(LDAPResultCode value) {
        this.descr = value;
    }

}
