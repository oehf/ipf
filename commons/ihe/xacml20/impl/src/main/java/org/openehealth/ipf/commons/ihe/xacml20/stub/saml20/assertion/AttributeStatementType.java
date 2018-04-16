
package org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AttributeStatementType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AttributeStatementType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn:oasis:names:tc:SAML:2.0:assertion}StatementAbstractType"&gt;
 *       &lt;choice maxOccurs="unbounded"&gt;
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}Attribute"/&gt;
 *         &lt;element ref="{urn:oasis:names:tc:SAML:2.0:assertion}EncryptedAttribute"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AttributeStatementType", propOrder = {
    "attributeOrEncryptedAttribute"
})
public class AttributeStatementType
    extends StatementAbstractType
{

    @XmlElements({
        @XmlElement(name = "Attribute", type = AttributeType.class),
        @XmlElement(name = "EncryptedAttribute", type = EncryptedElementType.class)
    })
    protected List<Object> attributeOrEncryptedAttribute;

    /**
     * Gets the value of the attributeOrEncryptedAttribute property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributeOrEncryptedAttribute property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributeOrEncryptedAttribute().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AttributeType }
     * {@link EncryptedElementType }
     * 
     * 
     */
    public List<Object> getAttributeOrEncryptedAttribute() {
        if (attributeOrEncryptedAttribute == null) {
            attributeOrEncryptedAttribute = new ArrayList<Object>();
        }
        return this.attributeOrEncryptedAttribute;
    }

}
