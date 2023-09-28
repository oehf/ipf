/**
 * SXPRTS.java
 *
 * File generated from the datatypes::SXPRTS uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.datatypes;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
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
 * Description of the class SXPRTS.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SXPR_TS", propOrder = {
	"comp"
})
@XmlRootElement(name = "SXPR_TS")
public class SXPRTS extends net.ihe.gazelle.hl7v3.datatypes.SXCMTS implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@XmlElement(name = "comp", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.SXCMTS> comp;
	
	/**
	 * An attribute containing marshalled element node
	 */
	@XmlTransient
	private org.w3c.dom.Node _xmlNodePresentation;
	
	
	/**
	 * Return comp.
	 * @return comp
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.SXCMTS> getComp() {
		if (comp == null) {
	        comp = new ArrayList<net.ihe.gazelle.hl7v3.datatypes.SXCMTS>();
	    }
	    return comp;
	}
	
	/**
	 * Set a value to attribute comp.
	 * @param comp.
	 */
	public void setComp(List<net.ihe.gazelle.hl7v3.datatypes.SXCMTS> comp) {
	    this.comp = comp;
	}
	
	
	
	/**
	 * Add a comp to the comp collection.
	 * @param comp_elt Element to add.
	 */
	public void addComp(net.ihe.gazelle.hl7v3.datatypes.SXCMTS comp_elt) {
	    this.comp.add(comp_elt);
	}
	
	/**
	 * Remove a comp to the comp collection.
	 * @param comp_elt Element to remove
	 */
	public void removeComp(net.ihe.gazelle.hl7v3.datatypes.SXCMTS comp_elt) {
	    this.comp.remove(comp_elt);
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
				} catch (ParserConfigurationException e1) {}
				try {
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.datatypes");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "SXPR_TS").item(0);
				} catch (JAXBException e) {
					try{
						db = dbf.newDocumentBuilder();
						_xmlNodePresentation = db.newDocument();
					}
					catch(Exception ee){}
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
   public static void validateByModule(SXPRTS sXPRTS, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (sXPRTS != null){
   			net.ihe.gazelle.hl7v3.datatypes.SXCMTS.validateByModule(sXPRTS, _location, cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.SXCMTS comp: sXPRTS.getComp()){
					net.ihe.gazelle.hl7v3.datatypes.SXCMTS.validateByModule(comp, _location + "/comp[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
    	}
    }

}