//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.01 at 10:44:03 PM CEST 
//


package org.openehealth.ipf.commons.xml.svrl;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openehealth.ipf.commons.xml.svrl package. 
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

    private final static QName _Text_QNAME = new QName("http://purl.oclc.org/dsdl/svrl", "text");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openehealth.ipf.commons.xml.svrl
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SuccessfulReport }
     * 
     */
    public SuccessfulReport createSuccessfulReport() {
        return new SuccessfulReport();
    }

    /**
     * Create an instance of {@link DiagnosticReference }
     * 
     */
    public DiagnosticReference createDiagnosticReference() {
        return new DiagnosticReference();
    }

    /**
     * Create an instance of {@link FailedAssert }
     * 
     */
    public FailedAssert createFailedAssert() {
        return new FailedAssert();
    }

    /**
     * Create an instance of {@link ActivePattern }
     * 
     */
    public ActivePattern createActivePattern() {
        return new ActivePattern();
    }

    /**
     * Create an instance of {@link SchematronOutput }
     * 
     */
    public SchematronOutput createSchematronOutput() {
        return new SchematronOutput();
    }

    /**
     * Create an instance of {@link NsPrefixInAttributeValues }
     * 
     */
    public NsPrefixInAttributeValues createNsPrefixInAttributeValues() {
        return new NsPrefixInAttributeValues();
    }

    /**
     * Create an instance of {@link FiredRule }
     * 
     */
    public FiredRule createFiredRule() {
        return new FiredRule();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://purl.oclc.org/dsdl/svrl", name = "text")
    public JAXBElement<String> createText(String value) {
        return new JAXBElement<>(_Text_QNAME, String.class, null, value);
    }

}
