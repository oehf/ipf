/**
 * IVLREAL.java
 *
 * File generated from the datatypes::IVLREAL uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.datatypes;

// End of user code
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
 * Description of the class IVLREAL.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IVL_REAL", propOrder = {
	"low",
	"width",
	"high",
	"center",
})
@XmlRootElement(name = "IVL_REAL")
public class IVLREAL extends net.ihe.gazelle.hl7v3.datatypes.SXCMREAL implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	/**
	 * 
	                           The low limit of the interval.
	                        .
	 */
	@XmlElement(name = "low", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.IVXBREAL low;
	/**
	 * 
	                           The difference between high and low boundary. The
	                           purpose of distinguishing a width property is to
	                           handle all cases of incomplete information
	                           symmetrically. In any interval representation only
	                           two of the three properties high, low, and width need
	                           to be stated and the third can be derived.
	                        .
	 */
	@XmlElement(name = "width", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.REAL width;
	/**
	 * 
	                           The high limit of the interval.
	                        .
	 */
	@XmlElement(name = "high", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.IVXBREAL high;
	/**
	 * 
	                           The difference between high and low boundary. The
	                           purpose of distinguishing a width property is to
	                           handle all cases of incomplete information
	                           symmetrically. In any interval representation only
	                           two of the three properties high, low, and width need
	                           to be stated and the third can be derived.
	                        .
	 */
	/**
	 * 
	                           The high limit of the interval.
	                        .
	 */
	/**
	 * 
	                           The arithmetic mean of the interval (low plus high
	                           divided by 2). The purpose of distinguishing the center
	                           as a semantic property is for conversions of intervals
	                           from and to point values.
	                        .
	 */
	@XmlElement(name = "center", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.REAL center;
	/**
	 * 
	                           The difference between high and low boundary. The
	                           purpose of distinguishing a width property is to
	                           handle all cases of incomplete information
	                           symmetrically. In any interval representation only
	                           two of the three properties high, low, and width need
	                           to be stated and the third can be derived.
	                        .
	 */
	
	/**
	 * An attribute containing marshalled element node
	 */
	@XmlTransient
	private org.w3c.dom.Node _xmlNodePresentation;
	
	
	
	
	public net.ihe.gazelle.hl7v3.datatypes.IVXBREAL getLow() {
		return low;
	}

	public void setLow(net.ihe.gazelle.hl7v3.datatypes.IVXBREAL low) {
		this.low = low;
	}

	public net.ihe.gazelle.hl7v3.datatypes.REAL getWidth() {
		return width;
	}

	public void setWidth(net.ihe.gazelle.hl7v3.datatypes.REAL width) {
		this.width = width;
	}

	public net.ihe.gazelle.hl7v3.datatypes.IVXBREAL getHigh() {
		return high;
	}

	public void setHigh(net.ihe.gazelle.hl7v3.datatypes.IVXBREAL high) {
		this.high = high;
	}

	public net.ihe.gazelle.hl7v3.datatypes.REAL getCenter() {
		return center;
	}

	public void setCenter(net.ihe.gazelle.hl7v3.datatypes.REAL center) {
		this.center = center;
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
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "IVL_REAL").item(0);
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
   public static void validateByModule(IVLREAL iVLREAL, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (iVLREAL != null){
   			net.ihe.gazelle.hl7v3.datatypes.SXCMREAL.validateByModule(iVLREAL, _location, cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.IVXBREAL.validateByModule(iVLREAL.getLow(), _location + "/low", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.REAL.validateByModule(iVLREAL.getWidth(), _location + "/width", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.IVXBREAL.validateByModule(iVLREAL.getHigh(), _location + "/high", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.REAL.validateByModule(iVLREAL.getCenter(), _location + "/center", cvm, diagnostic);
    	}
    }

}