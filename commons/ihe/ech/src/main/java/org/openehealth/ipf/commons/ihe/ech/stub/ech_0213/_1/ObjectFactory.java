
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0213._1;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ch.ech.xmlns.ech_0213._1 package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RequestContentAdditionalInputParameterKey_QNAME = new QName("http://www.ech.ch/xmlns/eCH-0213/1", "additionalInputParameterKey");
    private final static QName _RequestContentAdditionalInputParameterValue_QNAME = new QName("http://www.ech.ch/xmlns/eCH-0213/1", "additionalInputParameterValue");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.ech.xmlns.ech_0213._1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Request }
     * 
     */
    public Request createRequest() {
        return new Request();
    }

    /**
     * Create an instance of {@link Response }
     * 
     */
    public Response createResponse() {
        return new Response();
    }

    /**
     * Create an instance of {@link Request.Content }
     * 
     */
    public Request.Content createRequestContent() {
        return new Request.Content();
    }

    /**
     * Create an instance of {@link Response.PositiveResponse }
     * 
     */
    public Response.PositiveResponse createResponsePositiveResponse() {
        return new Response.PositiveResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ech.ch/xmlns/eCH-0213/1", name = "additionalInputParameterKey", scope = Request.Content.class)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createRequestContentAdditionalInputParameterKey(String value) {
        return new JAXBElement<String>(_RequestContentAdditionalInputParameterKey_QNAME, String.class, Request.Content.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ech.ch/xmlns/eCH-0213/1", name = "additionalInputParameterValue", scope = Request.Content.class)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createRequestContentAdditionalInputParameterValue(String value) {
        return new JAXBElement<String>(_RequestContentAdditionalInputParameterValue_QNAME, String.class, Request.Content.class, value);
    }

}
