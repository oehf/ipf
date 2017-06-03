
package org.openehealth.ipf.commons.ihe.hpd.stub.chpidd;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openehealth.ipf.commons.ihe.hpd.stub.chpidd package. 
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

    private final static QName _DownloadRequest_QNAME = new QName("urn:ehealth-suisse:names:tc:CS:1", "downloadRequest");
    private final static QName _DownloadResponse_QNAME = new QName("urn:ehealth-suisse:names:tc:CS:1", "downloadResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openehealth.ipf.commons.ihe.hpd.stub.chpidd
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DownloadRequest }
     * 
     */
    public DownloadRequest createDownloadRequest() {
        return new DownloadRequest();
    }

    /**
     * Create an instance of {@link DownloadResponse }
     * 
     */
    public DownloadResponse createDownloadResponse() {
        return new DownloadResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DownloadRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ehealth-suisse:names:tc:CS:1", name = "downloadRequest")
    public JAXBElement<DownloadRequest> createDownloadRequest(DownloadRequest value) {
        return new JAXBElement<DownloadRequest>(_DownloadRequest_QNAME, DownloadRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DownloadResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:ehealth-suisse:names:tc:CS:1", name = "downloadResponse")
    public JAXBElement<DownloadResponse> createDownloadResponse(DownloadResponse value) {
        return new JAXBElement<DownloadResponse>(_DownloadResponse_QNAME, DownloadResponse.class, null, value);
    }

}
