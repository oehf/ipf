
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0058._5;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ch.ech.xmlns.ech_0058._5 package. 
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

    private final static QName _Header_QNAME = new QName("http://www.ech.ch/xmlns/eCH-0058/5", "header");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.ech.xmlns.ech_0058._5
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link InfoType }
     * 
     */
    public InfoType createInfoType() {
        return new InfoType();
    }

    /**
     * Create an instance of {@link HeaderType }
     * 
     */
    public HeaderType createHeaderType() {
        return new HeaderType();
    }

    /**
     * Create an instance of {@link EventReport }
     * 
     */
    public EventReport createEventReport() {
        return new EventReport();
    }

    /**
     * Create an instance of {@link PartialDeliveryType }
     * 
     */
    public PartialDeliveryType createPartialDeliveryType() {
        return new PartialDeliveryType();
    }

    /**
     * Create an instance of {@link SendingApplicationType }
     * 
     */
    public SendingApplicationType createSendingApplicationType() {
        return new SendingApplicationType();
    }

    /**
     * Create an instance of {@link NamedMetaDataType }
     * 
     */
    public NamedMetaDataType createNamedMetaDataType() {
        return new NamedMetaDataType();
    }

    /**
     * Create an instance of {@link InfoType.PositiveReport }
     * 
     */
    public InfoType.PositiveReport createInfoTypePositiveReport() {
        return new InfoType.PositiveReport();
    }

    /**
     * Create an instance of {@link InfoType.NegativeReport }
     * 
     */
    public InfoType.NegativeReport createInfoTypeNegativeReport() {
        return new InfoType.NegativeReport();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link HeaderType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ech.ch/xmlns/eCH-0058/5", name = "header")
    public JAXBElement<HeaderType> createHeader(HeaderType value) {
        return new JAXBElement<HeaderType>(_Header_QNAME, HeaderType.class, null, value);
    }

}
