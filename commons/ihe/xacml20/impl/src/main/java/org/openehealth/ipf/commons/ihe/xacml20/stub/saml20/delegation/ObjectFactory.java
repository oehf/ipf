
package org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.delegation;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.delegation package. 
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

    private final static QName _Delegate_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:conditions:delegation", "Delegate");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openehealth.ipf.commons.ihe.xacml20.stub.saml20.delegation
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DelegateType }
     * 
     */
    public DelegateType createDelegateType() {
        return new DelegateType();
    }

    /**
     * Create an instance of {@link DelegationRestrictionType }
     * 
     */
    public DelegationRestrictionType createDelegationRestrictionType() {
        return new DelegationRestrictionType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DelegateType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:conditions:delegation", name = "Delegate")
    public JAXBElement<DelegateType> createDelegate(DelegateType value) {
        return new JAXBElement<DelegateType>(_Delegate_QNAME, DelegateType.class, null, value);
    }

}
