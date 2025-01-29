
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0058._5;

import java.util.ArrayList;
import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for headerType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="headerType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="senderId" type="{http://www.ech.ch/xmlns/eCH-0058/5}participantIdType"/>
 *         &lt;element name="originalSenderId" type="{http://www.ech.ch/xmlns/eCH-0058/5}participantIdType" minOccurs="0"/>
 *         &lt;element name="declarationLocalReference" type="{http://www.ech.ch/xmlns/eCH-0058/5}declarationLocalReferenceType" minOccurs="0"/>
 *         &lt;element name="recipientId" type="{http://www.ech.ch/xmlns/eCH-0058/5}participantIdType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="messageId" type="{http://www.ech.ch/xmlns/eCH-0058/5}messageIdType"/>
 *         &lt;element name="referenceMessageId" type="{http://www.ech.ch/xmlns/eCH-0058/5}messageIdType" minOccurs="0"/>
 *         &lt;element name="businessProcessId" type="{http://www.ech.ch/xmlns/eCH-0058/5}businessProcessIdType" minOccurs="0"/>
 *         &lt;element name="ourBusinessReferenceId" type="{http://www.ech.ch/xmlns/eCH-0058/5}businessReferenceIdType" minOccurs="0"/>
 *         &lt;element name="yourBusinessReferenceId" type="{http://www.ech.ch/xmlns/eCH-0058/5}businessReferenceIdType" minOccurs="0"/>
 *         &lt;element name="uniqueIdBusinessTransaction" type="{http://www.ech.ch/xmlns/eCH-0058/5}uniqueIdBusinessTransactionType" minOccurs="0"/>
 *         &lt;element name="messageType" type="{http://www.ech.ch/xmlns/eCH-0058/5}messageTypeType"/>
 *         &lt;element name="subMessageType" type="{http://www.ech.ch/xmlns/eCH-0058/5}subMessageTypeType" minOccurs="0"/>
 *         &lt;element name="sendingApplication" type="{http://www.ech.ch/xmlns/eCH-0058/5}sendingApplicationType"/>
 *         &lt;element name="partialDelivery" type="{http://www.ech.ch/xmlns/eCH-0058/5}partialDeliveryType" minOccurs="0"/>
 *         &lt;element name="subject" type="{http://www.ech.ch/xmlns/eCH-0058/5}subjectType" minOccurs="0"/>
 *         &lt;element name="comment" type="{http://www.ech.ch/xmlns/eCH-0058/5}commentType" minOccurs="0"/>
 *         &lt;element name="messageDate" type="{http://www.ech.ch/xmlns/eCH-0058/5}messageDateType"/>
 *         &lt;element name="initialMessageDate" type="{http://www.ech.ch/xmlns/eCH-0058/5}messageDateType" minOccurs="0"/>
 *         &lt;element name="eventDate" type="{http://www.ech.ch/xmlns/eCH-0058/5}eventDateType" minOccurs="0"/>
 *         &lt;element name="modificationDate" type="{http://www.ech.ch/xmlns/eCH-0058/5}eventDateType" minOccurs="0"/>
 *         &lt;element name="action" type="{http://www.ech.ch/xmlns/eCH-0058/5}actionType"/>
 *         &lt;element name="attachment" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="testDeliveryFlag" type="{http://www.ech.ch/xmlns/eCH-0058/5}testDeliveryFlagType"/>
 *         &lt;element name="responseExpected" type="{http://www.ech.ch/xmlns/eCH-0058/5}responseExpectedType" minOccurs="0"/>
 *         &lt;element name="businessCaseClosed" type="{http://www.ech.ch/xmlns/eCH-0058/5}businessCaseClosedType" minOccurs="0"/>
 *         &lt;element name="namedMetaData" type="{http://www.ech.ch/xmlns/eCH-0058/5}namedMetaDataType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="extension" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "headerType", propOrder = {
    "senderId",
    "originalSenderId",
    "declarationLocalReference",
    "recipientId",
    "messageId",
    "referenceMessageId",
    "businessProcessId",
    "ourBusinessReferenceId",
    "yourBusinessReferenceId",
    "uniqueIdBusinessTransaction",
    "messageType",
    "subMessageType",
    "sendingApplication",
    "partialDelivery",
    "subject",
    "comment",
    "messageDate",
    "initialMessageDate",
    "eventDate",
    "modificationDate",
    "action",
    "attachment",
    "testDeliveryFlag",
    "responseExpected",
    "businessCaseClosed",
    "namedMetaData",
    "extension"
})
public class HeaderType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String senderId;
    @XmlSchemaType(name = "anyURI")
    protected String originalSenderId;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String declarationLocalReference;
    @XmlSchemaType(name = "anyURI")
    protected List<String> recipientId;
    @XmlElement(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String messageId;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String referenceMessageId;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String businessProcessId;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String ourBusinessReferenceId;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String yourBusinessReferenceId;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String uniqueIdBusinessTransaction;
    @XmlElement(required = true)
    @XmlSchemaType(name = "anyURI")
    protected String messageType;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String subMessageType;
    @XmlElement(required = true)
    protected SendingApplicationType sendingApplication;
    protected PartialDeliveryType partialDelivery;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String subject;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "token")
    protected String comment;
    @XmlElement(required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar messageDate;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar initialMessageDate;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar eventDate;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar modificationDate;
    @XmlElement(required = true)
    protected String action;
    protected List<Object> attachment;
    protected boolean testDeliveryFlag;
    protected Boolean responseExpected;
    protected Boolean businessCaseClosed;
    protected List<NamedMetaDataType> namedMetaData;
    protected Object extension;

    /**
     * Gets the value of the senderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * Sets the value of the senderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSenderId(String value) {
        this.senderId = value;
    }

    /**
     * Gets the value of the originalSenderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOriginalSenderId() {
        return originalSenderId;
    }

    /**
     * Sets the value of the originalSenderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOriginalSenderId(String value) {
        this.originalSenderId = value;
    }

    /**
     * Gets the value of the declarationLocalReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeclarationLocalReference() {
        return declarationLocalReference;
    }

    /**
     * Sets the value of the declarationLocalReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeclarationLocalReference(String value) {
        this.declarationLocalReference = value;
    }

    /**
     * Gets the value of the recipientId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the recipientId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRecipientId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getRecipientId() {
        if (recipientId == null) {
            recipientId = new ArrayList<String>();
        }
        return this.recipientId;
    }

    /**
     * Gets the value of the messageId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the value of the messageId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageId(String value) {
        this.messageId = value;
    }

    /**
     * Gets the value of the referenceMessageId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReferenceMessageId() {
        return referenceMessageId;
    }

    /**
     * Sets the value of the referenceMessageId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReferenceMessageId(String value) {
        this.referenceMessageId = value;
    }

    /**
     * Gets the value of the businessProcessId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessProcessId() {
        return businessProcessId;
    }

    /**
     * Sets the value of the businessProcessId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessProcessId(String value) {
        this.businessProcessId = value;
    }

    /**
     * Gets the value of the ourBusinessReferenceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOurBusinessReferenceId() {
        return ourBusinessReferenceId;
    }

    /**
     * Sets the value of the ourBusinessReferenceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOurBusinessReferenceId(String value) {
        this.ourBusinessReferenceId = value;
    }

    /**
     * Gets the value of the yourBusinessReferenceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getYourBusinessReferenceId() {
        return yourBusinessReferenceId;
    }

    /**
     * Sets the value of the yourBusinessReferenceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYourBusinessReferenceId(String value) {
        this.yourBusinessReferenceId = value;
    }

    /**
     * Gets the value of the uniqueIdBusinessTransaction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUniqueIdBusinessTransaction() {
        return uniqueIdBusinessTransaction;
    }

    /**
     * Sets the value of the uniqueIdBusinessTransaction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUniqueIdBusinessTransaction(String value) {
        this.uniqueIdBusinessTransaction = value;
    }

    /**
     * Gets the value of the messageType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageType() {
        return messageType;
    }

    /**
     * Sets the value of the messageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageType(String value) {
        this.messageType = value;
    }

    /**
     * Gets the value of the subMessageType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubMessageType() {
        return subMessageType;
    }

    /**
     * Sets the value of the subMessageType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubMessageType(String value) {
        this.subMessageType = value;
    }

    /**
     * Gets the value of the sendingApplication property.
     * 
     * @return
     *     possible object is
     *     {@link SendingApplicationType }
     *     
     */
    public SendingApplicationType getSendingApplication() {
        return sendingApplication;
    }

    /**
     * Sets the value of the sendingApplication property.
     * 
     * @param value
     *     allowed object is
     *     {@link SendingApplicationType }
     *     
     */
    public void setSendingApplication(SendingApplicationType value) {
        this.sendingApplication = value;
    }

    /**
     * Gets the value of the partialDelivery property.
     * 
     * @return
     *     possible object is
     *     {@link PartialDeliveryType }
     *     
     */
    public PartialDeliveryType getPartialDelivery() {
        return partialDelivery;
    }

    /**
     * Sets the value of the partialDelivery property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartialDeliveryType }
     *     
     */
    public void setPartialDelivery(PartialDeliveryType value) {
        this.partialDelivery = value;
    }

    /**
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubject(String value) {
        this.subject = value;
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

    /**
     * Gets the value of the messageDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMessageDate() {
        return messageDate;
    }

    /**
     * Sets the value of the messageDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMessageDate(XMLGregorianCalendar value) {
        this.messageDate = value;
    }

    /**
     * Gets the value of the initialMessageDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getInitialMessageDate() {
        return initialMessageDate;
    }

    /**
     * Sets the value of the initialMessageDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setInitialMessageDate(XMLGregorianCalendar value) {
        this.initialMessageDate = value;
    }

    /**
     * Gets the value of the eventDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEventDate() {
        return eventDate;
    }

    /**
     * Sets the value of the eventDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEventDate(XMLGregorianCalendar value) {
        this.eventDate = value;
    }

    /**
     * Gets the value of the modificationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getModificationDate() {
        return modificationDate;
    }

    /**
     * Sets the value of the modificationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setModificationDate(XMLGregorianCalendar value) {
        this.modificationDate = value;
    }

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
    }

    /**
     * Gets the value of the attachment property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attachment property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttachment().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getAttachment() {
        if (attachment == null) {
            attachment = new ArrayList<Object>();
        }
        return this.attachment;
    }

    /**
     * Gets the value of the testDeliveryFlag property.
     * 
     */
    public boolean isTestDeliveryFlag() {
        return testDeliveryFlag;
    }

    /**
     * Sets the value of the testDeliveryFlag property.
     * 
     */
    public void setTestDeliveryFlag(boolean value) {
        this.testDeliveryFlag = value;
    }

    /**
     * Gets the value of the responseExpected property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isResponseExpected() {
        return responseExpected;
    }

    /**
     * Sets the value of the responseExpected property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setResponseExpected(Boolean value) {
        this.responseExpected = value;
    }

    /**
     * Gets the value of the businessCaseClosed property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isBusinessCaseClosed() {
        return businessCaseClosed;
    }

    /**
     * Sets the value of the businessCaseClosed property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setBusinessCaseClosed(Boolean value) {
        this.businessCaseClosed = value;
    }

    /**
     * Gets the value of the namedMetaData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the namedMetaData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNamedMetaData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NamedMetaDataType }
     * 
     * 
     */
    public List<NamedMetaDataType> getNamedMetaData() {
        if (namedMetaData == null) {
            namedMetaData = new ArrayList<NamedMetaDataType>();
        }
        return this.namedMetaData;
    }

    /**
     * Gets the value of the extension property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getExtension() {
        return extension;
    }

    /**
     * Sets the value of the extension property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setExtension(Object value) {
        this.extension = value;
    }

}
