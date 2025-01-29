
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0214._2;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for noticeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="noticeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="code" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}codeType"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="descriptionLanguage" type="{http://www.ech.ch/xmlns/eCH-0011/8}languageType"/>
 *           &lt;element name="codeDescription" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}codeDescriptionType"/>
 *         &lt;/sequence>
 *         &lt;element name="comment" type="{http://www.ech.ch/xmlns/eCH-0213-commons/1}commentType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "noticeType", propOrder = {
    "code",
    "descriptionLanguage",
    "codeDescription",
    "comment"
})
public class NoticeType {

    protected int code;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String descriptionLanguage;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String codeDescription;
    protected String comment;

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
     * Gets the value of the descriptionLanguage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescriptionLanguage() {
        return descriptionLanguage;
    }

    /**
     * Sets the value of the descriptionLanguage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescriptionLanguage(String value) {
        this.descriptionLanguage = value;
    }

    /**
     * Gets the value of the codeDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodeDescription() {
        return codeDescription;
    }

    /**
     * Sets the value of the codeDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodeDescription(String value) {
        this.codeDescription = value;
    }

    /**
     * Gets the value of the comment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * Sets the value of the comment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

}
