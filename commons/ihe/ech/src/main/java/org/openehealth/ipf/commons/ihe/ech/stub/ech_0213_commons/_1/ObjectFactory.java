
package org.openehealth.ipf.commons.ihe.ech.stub.ech_0213_commons._1;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ch.ech.xmlns.ech_0213_commons._1 package. 
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

    private final static QName _PidsToUPITypeSPID_QNAME = new QName("http://www.ech.ch/xmlns/eCH-0213-commons/1", "SPID");
    private final static QName _PidsToUPITypeVn_QNAME = new QName("http://www.ech.ch/xmlns/eCH-0213-commons/1", "vn");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.ech.xmlns.ech_0213_commons._1
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link NegativeReportType }
     * 
     */
    public NegativeReportType createNegativeReportType() {
        return new NegativeReportType();
    }

    /**
     * Create an instance of {@link WarningType }
     * 
     */
    public WarningType createWarningType() {
        return new WarningType();
    }

    /**
     * Create an instance of {@link PidsToUPIType }
     * 
     */
    public PidsToUPIType createPidsToUPIType() {
        return new PidsToUPIType();
    }

    /**
     * Create an instance of {@link PidsFromUPIType }
     * 
     */
    public PidsFromUPIType createPidsFromUPIType() {
        return new PidsFromUPIType();
    }

    /**
     * Create an instance of {@link PersonFromUPIType }
     * 
     */
    public PersonFromUPIType createPersonFromUPIType() {
        return new PersonFromUPIType();
    }

    /**
     * Create an instance of {@link PersonToUPIType }
     * 
     */
    public PersonToUPIType createPersonToUPIType() {
        return new PersonToUPIType();
    }

    /**
     * Create an instance of {@link NegativeReportType.Notice }
     * 
     */
    public NegativeReportType.Notice createNegativeReportTypeNotice() {
        return new NegativeReportType.Notice();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ech.ch/xmlns/eCH-0213-commons/1", name = "SPID", scope = PidsToUPIType.class)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPidsToUPITypeSPID(String value) {
        return new JAXBElement<String>(_PidsToUPITypeSPID_QNAME, String.class, PidsToUPIType.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ech.ch/xmlns/eCH-0213-commons/1", name = "vn", scope = PidsToUPIType.class)
    public JAXBElement<Long> createPidsToUPITypeVn(Long value) {
        return new JAXBElement<Long>(_PidsToUPITypeVn_QNAME, Long.class, PidsToUPIType.class, value);
    }

}
