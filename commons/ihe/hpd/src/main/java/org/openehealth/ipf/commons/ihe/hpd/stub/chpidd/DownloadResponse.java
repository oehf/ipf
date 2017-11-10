
package org.openehealth.ipf.commons.ihe.hpd.stub.chpidd;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

import org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2.BatchRequest;


/**
 * <p>Java class for DownloadResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DownloadResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn:oasis:names:tc:DSML:2:0:core}batchRequest" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="requestID" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DownloadResponse", propOrder = {
    "batchRequest"
})
@XmlRootElement(name = "downloadResponse")
public class DownloadResponse {

    @XmlElement(namespace = "urn:oasis:names:tc:DSML:2:0:core")
    protected List<BatchRequest> batchRequest;
    @XmlAttribute(name = "requestID")
    protected String requestID;

    /**
     * Gets the value of the batchRequest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the batchRequest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBatchRequest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BatchRequest }
     * 
     * 
     */
    public List<BatchRequest> getBatchRequest() {
        if (batchRequest == null) {
            batchRequest = new ArrayList<>();
        }
        return this.batchRequest;
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

}
