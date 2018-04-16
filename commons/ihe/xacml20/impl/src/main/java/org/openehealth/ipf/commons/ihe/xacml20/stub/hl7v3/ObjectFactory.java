
package org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3 package. 
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

    private final static QName _CodedValue_QNAME = new QName("urn:hl7-org:v3", "CodedValue");
    private final static QName _InstanceIdentifier_QNAME = new QName("urn:hl7-org:v3", "InstanceIdentifier");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openehealth.ipf.commons.ihe.xacml20.stub.hl7v3
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CV }
     * 
     */
    public CV createCV() {
        return new CV();
    }

    /**
     * Create an instance of {@link II }
     * 
     */
    public II createII() {
        return new II();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CV }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "CodedValue")
    public JAXBElement<CV> createCodedValue(CV value) {
        return new JAXBElement<CV>(_CodedValue_QNAME, CV.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link II }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "InstanceIdentifier")
    public JAXBElement<II> createInstanceIdentifier(II value) {
        return new JAXBElement<II>(_InstanceIdentifier_QNAME, II.class, null, value);
    }

}
