/**
 * PRPAMT201302UV02EmployeeId.java
 * <p>
 * File generated from the prpamt201302UV02::PRPAMT201302UV02EmployeeId uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.prpamt201302UV02;

// End of user code
import java.io.Serial;
import java.util.List;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.ihe.gazelle.gen.common.ConstraintValidatorModule;

import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * Description of the class PRPAMT201302UV02EmployeeId.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PRPA_MT201302UV02.Employee.id", propOrder = {
	"updateMode"
})
@XmlRootElement(name = "PRPA_MT201302UV02.Employee.id")
public class PRPAMT201302UV02EmployeeId extends net.ihe.gazelle.hl7v3.datatypes.II implements java.io.Serializable {
	
	/**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;

	
	@XmlAttribute(name = "updateMode")
	public net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02EmployeeIdUpdateMode updateMode;
	
	/**
	 * An attribute containing marshalled element node
	 */
	@XmlTransient
	private org.w3c.dom.Node _xmlNodePresentation;
	
	
	/**
	 * Return updateMode.
	 * @return updateMode
	 */
	public net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02EmployeeIdUpdateMode getUpdateMode() {
	    return updateMode;
	}
	
	/**
	 * Set a value to attribute updateMode.
     */
	public void setUpdateMode(net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02EmployeeIdUpdateMode updateMode) {
	    this.updateMode = updateMode;
	}
	
	
	
	
	
	public Node get_xmlNodePresentation() {
		if (_xmlNodePresentation == null){
				JAXBContext jc;
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setNamespaceAware(true);
				DocumentBuilder db = null;
				Document doc = null;
				try {
					db = dbf.newDocumentBuilder();
					doc = db.newDocument();
				} catch (ParserConfigurationException ignored) {}
				try {
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.prpamt201302UV02");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "PRPA_MT201302UV02.Employee.id").item(0);
				} catch (JAXBException e) {
					try{
						db = dbf.newDocumentBuilder();
						_xmlNodePresentation = db.newDocument();
					}
					catch(Exception ignored){}
				}
			}
			return _xmlNodePresentation;
	}
	
	public void set_xmlNodePresentation(Node _xmlNodePresentation) {
		this._xmlNodePresentation = _xmlNodePresentation;
	}
	
	
	

	
	/**
     * validate by a module of validation
     * 
     */
   public static void validateByModule(PRPAMT201302UV02EmployeeId pRPAMT201302UV02EmployeeId, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (pRPAMT201302UV02EmployeeId != null){
   			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(pRPAMT201302UV02EmployeeId, _location, cvm, diagnostic);
    	}
    }

}