/**
 * PRPAIN201304UV02Type.java
 * <p>
 * File generated from the prpain201304UV02::PRPAIN201304UV02Type uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.prpain201304UV02;

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
 * Description of the class PRPAIN201304UV02Type.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PRPA_IN201304UV02_._type", propOrder = {
	"iTSVersion"
})
@XmlRootElement(name = "PRPA_IN201304UV02")
public class PRPAIN201304UV02Type extends net.ihe.gazelle.hl7v3.prpain201304UV02.PRPAIN201304UV02MCCIMT000100UV01Message implements java.io.Serializable {
	
	/**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;

	
	@XmlAttribute(name = "ITSVersion", required = true)
	public String iTSVersion;
	
	/**
	 * An attribute containing marshalled element node
	 */
	@XmlTransient
	private org.w3c.dom.Node _xmlNodePresentation;
	
	
	/**
	 * Return iTSVersion.
	 * @return iTSVersion
	 */
	public String getITSVersion() {
	    return iTSVersion;
	}
	
	/**
	 * Set a value to attribute iTSVersion.
     */
	public void setITSVersion(String iTSVersion) {
	    this.iTSVersion = iTSVersion;
	}
	
	/**
	 * Return iTSVersion.
	 * @return iTSVersion
	 * Generated for the use on jsf pages
	 */
	 @Deprecated
	public String getiTSVersion() {
	    return iTSVersion;
	}
	
	/**
	 * Set a value to attribute iTSVersion.
     */
	 @Deprecated
	public void setiTSVersion(String iTSVersion) {
	    this.iTSVersion = iTSVersion;
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.prpain201304UV02");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "PRPA_IN201304UV02").item(0);
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
   public static void validateByModule(PRPAIN201304UV02Type pRPAIN201304UV02Type, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (pRPAIN201304UV02Type != null){
   			net.ihe.gazelle.hl7v3.prpain201304UV02.PRPAIN201304UV02MCCIMT000100UV01Message.validateByModule(pRPAIN201304UV02Type, _location, cvm, diagnostic);
    	}
    }

}