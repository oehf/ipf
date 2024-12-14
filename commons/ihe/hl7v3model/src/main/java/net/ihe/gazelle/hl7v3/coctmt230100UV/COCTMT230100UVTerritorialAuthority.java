/**
 * COCTMT230100UVTerritorialAuthority.java
 * <p>
 * File generated from the coctmt230100UV::COCTMT230100UVTerritorialAuthority uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.coctmt230100UV;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
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
 * Description of the class COCTMT230100UVTerritorialAuthority.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT230100UV.TerritorialAuthority", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"territory",
	"governingCountry",
	"classCode",
	"nullFlavor"
})
@XmlRootElement(name = "COCT_MT230100UV.TerritorialAuthority")
public class COCTMT230100UVTerritorialAuthority implements java.io.Serializable {
	
	/**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;

	
	@XmlElement(name = "realmCode", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.CS> realmCode;
	@XmlElement(name = "typeId", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.II typeId;
	@XmlElement(name = "templateId", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.II> templateId;
	@XmlElement(name = "territory", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt230100UV.COCTMT230100UVAgency territory;
	@XmlElement(name = "governingCountry", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt230100UV.COCTMT230100UVCountry governingCountry;
	@XmlAttribute(name = "classCode", required = true)
	public java.lang.String classCode;
	@XmlAttribute(name = "nullFlavor")
	public net.ihe.gazelle.hl7v3.voc.NullFlavor nullFlavor;
	
	/**
	 * An attribute containing marshalled element node
	 */
	@XmlTransient
	private org.w3c.dom.Node _xmlNodePresentation;
	
	
	/**
	 * Return realmCode.
	 * @return realmCode
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.CS> getRealmCode() {
		if (realmCode == null) {
	        realmCode = new ArrayList<>();
	    }
	    return realmCode;
	}
	
	/**
	 * Set a value to attribute realmCode.
     */
	public void setRealmCode(List<net.ihe.gazelle.hl7v3.datatypes.CS> realmCode) {
	    this.realmCode = realmCode;
	}
	
	
	
	/**
	 * Add a realmCode to the realmCode collection.
	 * @param realmCode_elt Element to add.
	 */
	public void addRealmCode(net.ihe.gazelle.hl7v3.datatypes.CS realmCode_elt) {
	    this.realmCode.add(realmCode_elt);
	}
	
	/**
	 * Remove a realmCode to the realmCode collection.
	 * @param realmCode_elt Element to remove
	 */
	public void removeRealmCode(net.ihe.gazelle.hl7v3.datatypes.CS realmCode_elt) {
	    this.realmCode.remove(realmCode_elt);
	}
	
	/**
	 * Return typeId.
	 * @return typeId
	 */
	public net.ihe.gazelle.hl7v3.datatypes.II getTypeId() {
	    return typeId;
	}
	
	/**
	 * Set a value to attribute typeId.
     */
	public void setTypeId(net.ihe.gazelle.hl7v3.datatypes.II typeId) {
	    this.typeId = typeId;
	}
	
	
	
	
	/**
	 * Return templateId.
	 * @return templateId
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.II> getTemplateId() {
		if (templateId == null) {
	        templateId = new ArrayList<>();
	    }
	    return templateId;
	}
	
	/**
	 * Set a value to attribute templateId.
     */
	public void setTemplateId(List<net.ihe.gazelle.hl7v3.datatypes.II> templateId) {
	    this.templateId = templateId;
	}
	
	
	
	/**
	 * Add a templateId to the templateId collection.
	 * @param templateId_elt Element to add.
	 */
	public void addTemplateId(net.ihe.gazelle.hl7v3.datatypes.II templateId_elt) {
	    this.templateId.add(templateId_elt);
	}
	
	/**
	 * Remove a templateId to the templateId collection.
	 * @param templateId_elt Element to remove
	 */
	public void removeTemplateId(net.ihe.gazelle.hl7v3.datatypes.II templateId_elt) {
	    this.templateId.remove(templateId_elt);
	}
	
	/**
	 * Return territory.
	 * @return territory
	 */
	public net.ihe.gazelle.hl7v3.coctmt230100UV.COCTMT230100UVAgency getTerritory() {
	    return territory;
	}
	
	/**
	 * Set a value to attribute territory.
     */
	public void setTerritory(net.ihe.gazelle.hl7v3.coctmt230100UV.COCTMT230100UVAgency territory) {
	    this.territory = territory;
	}
	
	
	
	
	/**
	 * Return governingCountry.
	 * @return governingCountry
	 */
	public net.ihe.gazelle.hl7v3.coctmt230100UV.COCTMT230100UVCountry getGoverningCountry() {
	    return governingCountry;
	}
	
	/**
	 * Set a value to attribute governingCountry.
     */
	public void setGoverningCountry(net.ihe.gazelle.hl7v3.coctmt230100UV.COCTMT230100UVCountry governingCountry) {
	    this.governingCountry = governingCountry;
	}
	
	
	
	
	/**
	 * Return classCode.
	 * @return classCode
	 */
	public java.lang.String getClassCode() {
	    return classCode;
	}
	
	/**
	 * Set a value to attribute classCode.
     */
	public void setClassCode(java.lang.String classCode) {
	    this.classCode = classCode;
	}
	
	
	
	
	/**
	 * Return nullFlavor.
	 * @return nullFlavor
	 */
	public net.ihe.gazelle.hl7v3.voc.NullFlavor getNullFlavor() {
	    return nullFlavor;
	}
	
	/**
	 * Set a value to attribute nullFlavor.
     */
	public void setNullFlavor(net.ihe.gazelle.hl7v3.voc.NullFlavor nullFlavor) {
	    this.nullFlavor = nullFlavor;
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.coctmt230100UV");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT230100UV.TerritorialAuthority").item(0);
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
   public static void validateByModule(COCTMT230100UVTerritorialAuthority cOCTMT230100UVTerritorialAuthority, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT230100UVTerritorialAuthority != null){
   			cvm.validate(cOCTMT230100UVTerritorialAuthority, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT230100UVTerritorialAuthority.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT230100UVTerritorialAuthority.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT230100UVTerritorialAuthority.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.coctmt230100UV.COCTMT230100UVAgency.validateByModule(cOCTMT230100UVTerritorialAuthority.getTerritory(), _location + "/territory", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt230100UV.COCTMT230100UVCountry.validateByModule(cOCTMT230100UVTerritorialAuthority.getGoverningCountry(), _location + "/governingCountry", cvm, diagnostic);
    	}
    }

}