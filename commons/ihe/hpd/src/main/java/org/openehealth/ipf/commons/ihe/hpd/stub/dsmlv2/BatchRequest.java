
package org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;


/**
 * <p>Java class for BatchRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BatchRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="authRequest" type="{urn:oasis:names:tc:DSML:2:0:core}AuthRequest" minOccurs="0"/&gt;
 *         &lt;group ref="{urn:oasis:names:tc:DSML:2:0:core}BatchRequests" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="requestID" type="{urn:oasis:names:tc:DSML:2:0:core}RequestID" /&gt;
 *       &lt;attribute name="processing" default="sequential"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;enumeration value="sequential"/&gt;
 *             &lt;enumeration value="parallel"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="responseOrder" default="sequential"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;enumeration value="sequential"/&gt;
 *             &lt;enumeration value="unordered"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *       &lt;attribute name="onError" default="exit"&gt;
 *         &lt;simpleType&gt;
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *             &lt;enumeration value="resume"/&gt;
 *             &lt;enumeration value="exit"/&gt;
 *           &lt;/restriction&gt;
 *         &lt;/simpleType&gt;
 *       &lt;/attribute&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BatchRequest", propOrder = {
    "authRequest",
    "batchRequests"
})
@XmlRootElement
public class BatchRequest {

    protected AuthRequest authRequest;
    @XmlElements({
        @XmlElement(name = "searchRequest", type = SearchRequest.class),
        @XmlElement(name = "modifyRequest", type = ModifyRequest.class),
        @XmlElement(name = "addRequest", type = AddRequest.class),
        @XmlElement(name = "delRequest", type = DelRequest.class),
        @XmlElement(name = "modDNRequest", type = ModifyDNRequest.class),
        @XmlElement(name = "compareRequest", type = CompareRequest.class),
        @XmlElement(name = "abandonRequest", type = AbandonRequest.class),
        @XmlElement(name = "extendedRequest", type = ExtendedRequest.class)
    })
    protected List<DsmlMessage> batchRequests;
    @XmlAttribute(name = "requestID")
    protected String requestID;
    @XmlAttribute(name = "processing")
    protected String processing;
    @XmlAttribute(name = "responseOrder")
    protected String responseOrder;
    @XmlAttribute(name = "onError")
    protected String onError;

    /**
     * Gets the value of the authRequest property.
     * 
     * @return
     *     possible object is
     *     {@link AuthRequest }
     *     
     */
    public AuthRequest getAuthRequest() {
        return authRequest;
    }

    /**
     * Sets the value of the authRequest property.
     * 
     * @param value
     *     allowed object is
     *     {@link AuthRequest }
     *     
     */
    public void setAuthRequest(AuthRequest value) {
        this.authRequest = value;
    }

    /**
     * Gets the value of the batchRequests property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the batchRequests property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBatchRequests().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SearchRequest }
     * {@link ModifyRequest }
     * {@link AddRequest }
     * {@link DelRequest }
     * {@link ModifyDNRequest }
     * {@link CompareRequest }
     * {@link AbandonRequest }
     * {@link ExtendedRequest }
     * 
     * 
     */
    public List<DsmlMessage> getBatchRequests() {
        if (batchRequests == null) {
            batchRequests = new ArrayList<DsmlMessage>();
        }
        return this.batchRequests;
    }

    /**
     * Gets the value of the requestID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRequestID() {
        return requestID;
    }

    /**
     * Sets the value of the requestID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRequestID(String value) {
        this.requestID = value;
    }

    /**
     * Gets the value of the processing property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessing() {
        if (processing == null) {
            return "sequential";
        } else {
            return processing;
        }
    }

    /**
     * Sets the value of the processing property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessing(String value) {
        this.processing = value;
    }

    /**
     * Gets the value of the responseOrder property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponseOrder() {
        if (responseOrder == null) {
            return "sequential";
        } else {
            return responseOrder;
        }
    }

    /**
     * Sets the value of the responseOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponseOrder(String value) {
        this.responseOrder = value;
    }

    /**
     * Gets the value of the onError property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOnError() {
        if (onError == null) {
            return "exit";
        } else {
            return onError;
        }
    }

    /**
     * Sets the value of the onError property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOnError(String value) {
        this.onError = value;
    }

}
