/**
 * EN.java
 * <p>
 * File generated from the datatypes::EN uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.datatypes;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlElementRefs;
import jakarta.xml.bind.annotation.XmlMixed;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.ihe.gazelle.gen.common.ConstraintValidatorModule;

import net.ihe.gazelle.hl7v3.voc.NullFlavor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;


/**
 * Description of the class EN.
 *
 * 
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EN", propOrder = { "nullFlavor",
		"mixed",
	"use"
})
@XmlRootElement(name = "EN")
public class EN  implements java.io.Serializable {
	
	/**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;

	/**
	 *
	 An exceptional value expressing missing information
	 and possibly the reason why the information is missing.
	 Comes from ANY (cannot be extended due to a bug in Jaxb)
	 */
	@XmlAttribute(name = "nullFlavor")
	public net.ihe.gazelle.hl7v3.voc.NullFlavor nullFlavor;

	
	@XmlElementRefs({
			@XmlElementRef(name = "delimiter", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
			@XmlElementRef(name = "family", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
			@XmlElementRef(name = "given", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
			@XmlElementRef(name = "prefix", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
			@XmlElementRef(name = "suffix", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
			@XmlElementRef(name = "validTime", namespace = "urn:hl7-org:v3", type = JAXBElement.class)
		})
		@XmlMixed
	public List<java.io.Serializable> mixed;
	/**
	 * 
	                     A set of codes advising a system or user which name
	                     in a set of like names to select for a given purpose.
	                     A name without specific use code might be a default
	                     name useful for any purpose, but a name with a specific
	                     use code would be preferred for that respective purpose.
	                  .
	 */
	@XmlAttribute(name = "use")
	public java.lang.String use;
	
	/**
	 * An attribute containing marshalled element node
	 */
	@XmlTransient
	private org.w3c.dom.Node _xmlNodePresentation;

	public NullFlavor getNullFlavor() {
		return nullFlavor;
	}

	public void setNullFlavor(NullFlavor nullFlavor) {
		this.nullFlavor = nullFlavor;
	}

	/**
	 * Return mixed.
	 * @return mixed
	 */
	public List<java.io.Serializable> getMixed() {
		if (mixed == null) {
	        mixed = new ArrayList<>();
	    }
	    return mixed;
	}
	
	/**
	 * Set a value to attribute mixed.
     */
	public void setMixed(List<java.io.Serializable> mixed) {
	    this.mixed = mixed;
	}
	
	
	
	/**
	 * Add a mixed to the mixed collection.
	 * @param mixed_elt Element to add.
	 */
	public void addMixed(java.io.Serializable mixed_elt) {
	    this.mixed.add(mixed_elt);
	}
	
	/**
	 * Remove a mixed to the mixed collection.
	 * @param mixed_elt Element to remove
	 */
	public void removeMixed(java.io.Serializable mixed_elt) {
	    this.mixed.remove(mixed_elt);
	}
	
	
	/**
	 * Return delimiter.
	 * @return delimiter
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.EnDelimiter> getDelimiter() {
		List<net.ihe.gazelle.hl7v3.datatypes.EnDelimiter> delimiter_el = new ArrayList<>();
			for(java.io.Serializable obj : this.getMixed()){
			if (obj instanceof jakarta.xml.bind.JAXBElement){
				if (((jakarta.xml.bind.JAXBElement<?>)obj).getName().getLocalPart().equals("delimiter")){
					delimiter_el.add( (net.ihe.gazelle.hl7v3.datatypes.EnDelimiter) ((jakarta.xml.bind.JAXBElement<?>)obj).getValue() );
				}
			}
		} 

	    return delimiter_el;
	}
	
	public void addDelimiter(net.ihe.gazelle.hl7v3.datatypes.EnDelimiter obj){
		ObjectFactory of = new ObjectFactory();
		JAXBElement<net.ihe.gazelle.hl7v3.datatypes.EnDelimiter> jobj = of.createENDelimiter(obj);
		this.getMixed().add(jobj);
	}
	
	
	/**
	 * Return family.
	 * @return family
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.EnFamily> getFamily() {
		List<net.ihe.gazelle.hl7v3.datatypes.EnFamily> family_el = new ArrayList<>();
			for(java.io.Serializable obj : this.getMixed()){
			if (obj instanceof jakarta.xml.bind.JAXBElement){
				if (((jakarta.xml.bind.JAXBElement<?>)obj).getName().getLocalPart().equals("family")){
					family_el.add( (net.ihe.gazelle.hl7v3.datatypes.EnFamily) ((jakarta.xml.bind.JAXBElement<?>)obj).getValue() );
				}
			}
		} 

	    return family_el;
	}
	
	public void addFamily(net.ihe.gazelle.hl7v3.datatypes.EnFamily obj){
		ObjectFactory of = new ObjectFactory();
		JAXBElement<net.ihe.gazelle.hl7v3.datatypes.EnFamily> jobj = of.createENFamily(obj);
		this.getMixed().add(jobj);
	}
	
	
	/**
	 * Return given.
	 * @return given
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.EnGiven> getGiven() {
		List<net.ihe.gazelle.hl7v3.datatypes.EnGiven> given_el = new ArrayList<>();
			for(java.io.Serializable obj : this.getMixed()){
			if (obj instanceof jakarta.xml.bind.JAXBElement){
				if (((jakarta.xml.bind.JAXBElement<?>)obj).getName().getLocalPart().equals("given")){
					given_el.add( (net.ihe.gazelle.hl7v3.datatypes.EnGiven) ((jakarta.xml.bind.JAXBElement<?>)obj).getValue() );
				}
			}
		} 

	    return given_el;
	}
	
	public void addGiven(net.ihe.gazelle.hl7v3.datatypes.EnGiven obj){
		ObjectFactory of = new ObjectFactory();
		JAXBElement<net.ihe.gazelle.hl7v3.datatypes.EnGiven> jobj = of.createENGiven(obj);
		this.getMixed().add(jobj);
	}
	
	
	/**
	 * Return prefix.
	 * @return prefix
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.EnPrefix> getPrefix() {
		List<net.ihe.gazelle.hl7v3.datatypes.EnPrefix> prefix_el = new ArrayList<>();
			for(java.io.Serializable obj : this.getMixed()){
			if (obj instanceof jakarta.xml.bind.JAXBElement){
				if (((jakarta.xml.bind.JAXBElement<?>)obj).getName().getLocalPart().equals("prefix")){
					prefix_el.add( (net.ihe.gazelle.hl7v3.datatypes.EnPrefix) ((jakarta.xml.bind.JAXBElement<?>)obj).getValue() );
				}
			}
		} 

	    return prefix_el;
	}
	
	public void addPrefix(net.ihe.gazelle.hl7v3.datatypes.EnPrefix obj){
		ObjectFactory of = new ObjectFactory();
		JAXBElement<net.ihe.gazelle.hl7v3.datatypes.EnPrefix> jobj = of.createENPrefix(obj);
		this.getMixed().add(jobj);
	}
	
	
	/**
	 * Return suffix.
	 * @return suffix
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.EnSuffix> getSuffix() {
		List<net.ihe.gazelle.hl7v3.datatypes.EnSuffix> suffix_el = new ArrayList<>();
			for(java.io.Serializable obj : this.getMixed()){
			if (obj instanceof jakarta.xml.bind.JAXBElement){
				if (((jakarta.xml.bind.JAXBElement<?>)obj).getName().getLocalPart().equals("suffix")){
					suffix_el.add( (net.ihe.gazelle.hl7v3.datatypes.EnSuffix) ((jakarta.xml.bind.JAXBElement<?>)obj).getValue() );
				}
			}
		} 

	    return suffix_el;
	}
	
	public void addSuffix(net.ihe.gazelle.hl7v3.datatypes.EnSuffix obj){
		ObjectFactory of = new ObjectFactory();
		JAXBElement<net.ihe.gazelle.hl7v3.datatypes.EnSuffix> jobj = of.createENSuffix(obj);
		this.getMixed().add(jobj);
	}
	
	
	/**
	 * Return validTime.
	 * @return validTime
	 */
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS getValidTime() {
		net.ihe.gazelle.hl7v3.datatypes.IVLTS validTime_el = null;
			for(java.io.Serializable obj : this.getMixed()){
			if (obj instanceof jakarta.xml.bind.JAXBElement){
				if (((jakarta.xml.bind.JAXBElement<?>)obj).getName().getLocalPart().equals("validTime")){
					validTime_el = (net.ihe.gazelle.hl7v3.datatypes.IVLTS) ((jakarta.xml.bind.JAXBElement<?>)obj).getValue();
					return validTime_el;
				}
			}
		} 

	    return validTime_el;
	}
	
	public void addValidTime(net.ihe.gazelle.hl7v3.datatypes.IVLTS obj){
		ObjectFactory of = new ObjectFactory();
		JAXBElement<net.ihe.gazelle.hl7v3.datatypes.IVLTS> jobj = of.createENValidTime(obj);
		this.getMixed().add(jobj);
	}
	
	
	/**
	 * Return use.
	 * @return use : 
	                     A set of codes advising a system or user which name
	                     in a set of like names to select for a given purpose.
	                     A name without specific use code might be a default
	                     name useful for any purpose, but a name with a specific
	                     use code would be preferred for that respective purpose.
	                  
	                  
	 */
	public java.lang.String getUse() {
	    return use;
	}
	
	/**
	 * Set a value to attribute use.
                
	                     A set of codes advising a system or user which name
	                     in a set of like names to select for a given purpose.
	                     A name without specific use code might be a default
	                     name useful for any purpose, but a name with a specific
	                     use code would be preferred for that respective purpose.
	                  .
	 */
	public void setUse(java.lang.String use) {
	    this.use = use;
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
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "EN").item(0);
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
	
	
	public List<String> getListStringValues(){
		List<String> res = new ArrayList<>();
		for(java.io.Serializable obj : this.getMixed()){
			if (obj instanceof String){
				res.add((String)obj);
			}
		} 
		return res;
	}
	

	
	/**
     * validate by a module of validation
     * 
     */
   public static void validateByModule(EN eN, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
	   cvm.validate(eN, _location, diagnostic);
	   if (eN != null){
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.EnDelimiter delimiter: eN.getDelimiter()){
					net.ihe.gazelle.hl7v3.datatypes.EnDelimiter.validateByModule(delimiter, _location + "/delimiter[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.EnFamily family: eN.getFamily()){
					net.ihe.gazelle.hl7v3.datatypes.EnFamily.validateByModule(family, _location + "/family[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.EnGiven given: eN.getGiven()){
					net.ihe.gazelle.hl7v3.datatypes.EnGiven.validateByModule(given, _location + "/given[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.EnPrefix prefix: eN.getPrefix()){
					net.ihe.gazelle.hl7v3.datatypes.EnPrefix.validateByModule(prefix, _location + "/prefix[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.EnSuffix suffix: eN.getSuffix()){
					net.ihe.gazelle.hl7v3.datatypes.EnSuffix.validateByModule(suffix, _location + "/suffix[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.IVLTS.validateByModule(eN.getValidTime(), _location + "/validTime", cvm, diagnostic);
    	}
    }

}