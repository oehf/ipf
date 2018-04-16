
package org.openehealth.ipf.commons.ihe.xacml20.stub.xacml20.saml.assertion;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.herasaf.xacml.core.context.impl.RequestType;
import org.herasaf.xacml.core.context.impl.ResponseType;
import org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.assertion.StatementAbstractType;


/**
 * <p>Java class for XACMLAuthzDecisionStatementType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="XACMLAuthzDecisionStatementType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn:oasis:names:tc:SAML:2.0:assertion}StatementAbstractType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:context:schema:os}Response"/&gt;
 *         &lt;element ref="{urn:oasis:names:tc:xacml:2.0:context:schema:os}Request" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "XACMLAuthzDecisionStatementType", propOrder = {
    "response",
    "request"
})
public class XACMLAuthzDecisionStatementType
    extends StatementAbstractType
{

    @XmlElement(name = "Response", namespace = "urn:oasis:names:tc:xacml:2.0:context:schema:os", required = true)
    protected ResponseType response;
    @XmlElement(name = "Request", namespace = "urn:oasis:names:tc:xacml:2.0:context:schema:os")
    protected RequestType request;

    /**
     * Gets the value of the response property.
     * 
     * @return
     *     possible object is
     *     {@link ResponseType }
     *     
     */
    public ResponseType getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseType }
     *     
     */
    public void setResponse(ResponseType value) {
        this.response = value;
    }

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link RequestType }
     *     
     */
    public RequestType getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestType }
     *     
     */
    public void setRequest(RequestType value) {
        this.request = value;
    }

}
