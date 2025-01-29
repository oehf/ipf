
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0010._5;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ch.ech.xmlns.ech_0010._5 package. 
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

    private final static QName _PersonMailAddress_QNAME = new QName("http://www.ech.ch/xmlns/eCH-0010/5", "personMailAddress");
    private final static QName _MailAddress_QNAME = new QName("http://www.ech.ch/xmlns/eCH-0010/5", "mailAddress");
    private final static QName _OrganisationMailAdress_QNAME = new QName("http://www.ech.ch/xmlns/eCH-0010/5", "organisationMailAdress");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.ech.xmlns.ech_0010._5
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link OrganisationMailAddressType }
     * 
     */
    public OrganisationMailAddressType createOrganisationMailAddressType() {
        return new OrganisationMailAddressType();
    }

    /**
     * Create an instance of {@link MailAddressType }
     * 
     */
    public MailAddressType createMailAddressType() {
        return new MailAddressType();
    }

    /**
     * Create an instance of {@link PersonMailAddressType }
     * 
     */
    public PersonMailAddressType createPersonMailAddressType() {
        return new PersonMailAddressType();
    }

    /**
     * Create an instance of {@link OrganisationMailAddressInfoType }
     * 
     */
    public OrganisationMailAddressInfoType createOrganisationMailAddressInfoType() {
        return new OrganisationMailAddressInfoType();
    }

    /**
     * Create an instance of {@link AddressInformationType }
     * 
     */
    public AddressInformationType createAddressInformationType() {
        return new AddressInformationType();
    }

    /**
     * Create an instance of {@link SwissAddressInformationType }
     * 
     */
    public SwissAddressInformationType createSwissAddressInformationType() {
        return new SwissAddressInformationType();
    }

    /**
     * Create an instance of {@link PersonMailAddressInfoType }
     * 
     */
    public PersonMailAddressInfoType createPersonMailAddressInfoType() {
        return new PersonMailAddressInfoType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonMailAddressType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ech.ch/xmlns/eCH-0010/5", name = "personMailAddress")
    public JAXBElement<PersonMailAddressType> createPersonMailAddress(PersonMailAddressType value) {
        return new JAXBElement<PersonMailAddressType>(_PersonMailAddress_QNAME, PersonMailAddressType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MailAddressType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ech.ch/xmlns/eCH-0010/5", name = "mailAddress")
    public JAXBElement<MailAddressType> createMailAddress(MailAddressType value) {
        return new JAXBElement<MailAddressType>(_MailAddress_QNAME, MailAddressType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OrganisationMailAddressType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ech.ch/xmlns/eCH-0010/5", name = "organisationMailAdress")
    public JAXBElement<OrganisationMailAddressType> createOrganisationMailAdress(OrganisationMailAddressType value) {
        return new JAXBElement<OrganisationMailAddressType>(_OrganisationMailAdress_QNAME, OrganisationMailAddressType.class, null, value);
    }

}
