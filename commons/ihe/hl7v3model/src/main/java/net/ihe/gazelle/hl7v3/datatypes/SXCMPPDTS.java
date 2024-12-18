/**
 * SXCMPPDTS.java
 * <p>
 * File generated from the datatypes::SXCMPPDTS uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.datatypes;

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
 * Description of the class SXCMPPDTS.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SXCM_PPD_TS", propOrder = {
	"operator"
})
@XmlRootElement(name = "SXCM_PPD_TS")
public class SXCMPPDTS extends net.ihe.gazelle.hl7v3.datatypes.PPDTS implements java.io.Serializable {
	
	/**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;

	
	/**
	 * 
	                     A code specifying whether the set component is included
	                     (union) or excluded (set-difference) from the set, or
	                     other set operations with the current set component and
	                     the set as constructed from the representation stream
	                     up to the current point.
	                  .
	 */
	@XmlAttribute(name = "operator")
	public net.ihe.gazelle.hl7v3.voc.SetOperator operator;
	
	/**
	 * An attribute containing marshalled element node
	 */
	@XmlTransient
	private org.w3c.dom.Node _xmlNodePresentation;
	
	
	/**
	 * Return operator.
	 * @return operator : 
	                     A code specifying whether the set component is included
	                     (union) or excluded (set-difference) from the set, or
	                     other set operations with the current set component and
	                     the set as constructed from the representation stream
	                     up to the current point.
	 */
	public net.ihe.gazelle.hl7v3.voc.SetOperator getOperator() {
	    return operator;
	}
	
	/**
	 * Set a value to attribute operator.
	 * @param operator : 
	                     A code specifying whether the set component is included
	                     (union) or excluded (set-difference) from the set, or
	                     other set operations with the current set component and
	                     the set as constructed from the representation stream
	                     up to the current point.
	 */
	public void setOperator(net.ihe.gazelle.hl7v3.voc.SetOperator operator) {
	    this.operator = operator;
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.datatypes");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "SXCM_PPD_TS").item(0);
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
   public static void validateByModule(SXCMPPDTS sXCMPPDTS, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (sXCMPPDTS != null){
   			net.ihe.gazelle.hl7v3.datatypes.PPDTS.validateByModule(sXCMPPDTS, _location, cvm, diagnostic);
    	}
    }

}