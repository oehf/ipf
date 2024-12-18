/**
 * UVPTS.java
 * <p>
 * File generated from the datatypes::UVPTS uml Class
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
 * Description of the class UVPTS.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UVP_TS", propOrder = {
	"probability"
})
@XmlRootElement(name = "UVP_TS")
public class UVPTS extends net.ihe.gazelle.hl7v3.datatypes.TS implements java.io.Serializable {
	
	/**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;

	
	/**
	 * 
	               The probability assigned to the value, a decimal number
	               between 0 (very uncertain) and 1 (certain).
	            .
	 */
	@XmlAttribute(name = "probability")
	@jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter(net.ihe.gazelle.adapters.DoubleAdapter.class)
	public Double probability;
	
	/**
	 * An attribute containing marshalled element node
	 */
	@XmlTransient
	private org.w3c.dom.Node _xmlNodePresentation;
	
	
	/**
	 * Return probability.
	 * @return probability : 
	               The probability assigned to the value, a decimal number
	               between 0 (very uncertain) and 1 (certain).
	 */
	public Double getProbability() {
	    return probability;
	}
	
	/**
	 * Set a value to attribute probability.
	 * @param probability : 
	               The probability assigned to the value, a decimal number
	               between 0 (very uncertain) and 1 (certain).

	 */
	public void setProbability(Double probability) {
	    this.probability = probability;
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
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "UVP_TS").item(0);
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
   public static void validateByModule(UVPTS uVPTS, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (uVPTS != null){
   			net.ihe.gazelle.hl7v3.datatypes.TS.validateByModule(uVPTS, _location, cvm, diagnostic);
    	}
    }

}