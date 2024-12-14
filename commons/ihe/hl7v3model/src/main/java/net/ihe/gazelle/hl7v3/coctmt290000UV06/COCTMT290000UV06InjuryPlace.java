/**
 * COCTMT290000UV06InjuryPlace.java
 * <p>
 * File generated from the coctmt290000UV06::COCTMT290000UV06InjuryPlace uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.coctmt290000UV06;

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
 * Description of the class COCTMT290000UV06InjuryPlace.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT290000UV06.InjuryPlace", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"id",
	"code",
	"name",
	"desc",
	"positionText",
	"injuryLocation",
	"classCode",
	"determinerCode",
	"nullFlavor"
})
@XmlRootElement(name = "COCT_MT290000UV06.InjuryPlace")
public class COCTMT290000UV06InjuryPlace implements java.io.Serializable {
	
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
	@XmlElement(name = "id", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.II> id;
	@XmlElement(name = "code", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE code;
	@XmlElement(name = "name", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.EN> name;
	@XmlElement(name = "desc", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.ED desc;
	@XmlElement(name = "positionText", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.ED positionText;
	@XmlElement(name = "injuryLocation", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt290000UV06.COCTMT290000UV06InjuryLocation> injuryLocation;
	@XmlAttribute(name = "classCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.EntityClassPlace classCode;
	@XmlAttribute(name = "determinerCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.EntityDeterminer determinerCode;
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
	 * Return id.
	 * @return id
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.II> getId() {
		if (id == null) {
	        id = new ArrayList<>();
	    }
	    return id;
	}
	
	/**
	 * Set a value to attribute id.
     */
	public void setId(List<net.ihe.gazelle.hl7v3.datatypes.II> id) {
	    this.id = id;
	}
	
	
	
	/**
	 * Add a id to the id collection.
	 * @param id_elt Element to add.
	 */
	public void addId(net.ihe.gazelle.hl7v3.datatypes.II id_elt) {
	    this.id.add(id_elt);
	}
	
	/**
	 * Remove a id to the id collection.
	 * @param id_elt Element to remove
	 */
	public void removeId(net.ihe.gazelle.hl7v3.datatypes.II id_elt) {
	    this.id.remove(id_elt);
	}
	
	/**
	 * Return code.
	 * @return code
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getCode() {
	    return code;
	}
	
	/**
	 * Set a value to attribute code.
     */
	public void setCode(net.ihe.gazelle.hl7v3.datatypes.CE code) {
	    this.code = code;
	}
	
	
	
	
	/**
	 * Return name.
	 * @return name
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.EN> getName() {
		if (name == null) {
	        name = new ArrayList<>();
	    }
	    return name;
	}
	
	/**
	 * Set a value to attribute name.
     */
	public void setName(List<net.ihe.gazelle.hl7v3.datatypes.EN> name) {
	    this.name = name;
	}
	
	
	
	/**
	 * Add a name to the name collection.
	 * @param name_elt Element to add.
	 */
	public void addName(net.ihe.gazelle.hl7v3.datatypes.EN name_elt) {
	    this.name.add(name_elt);
	}
	
	/**
	 * Remove a name to the name collection.
	 * @param name_elt Element to remove
	 */
	public void removeName(net.ihe.gazelle.hl7v3.datatypes.EN name_elt) {
	    this.name.remove(name_elt);
	}
	
	/**
	 * Return desc.
	 * @return desc
	 */
	public net.ihe.gazelle.hl7v3.datatypes.ED getDesc() {
	    return desc;
	}
	
	/**
	 * Set a value to attribute desc.
     */
	public void setDesc(net.ihe.gazelle.hl7v3.datatypes.ED desc) {
	    this.desc = desc;
	}
	
	
	
	
	/**
	 * Return positionText.
	 * @return positionText
	 */
	public net.ihe.gazelle.hl7v3.datatypes.ED getPositionText() {
	    return positionText;
	}
	
	/**
	 * Set a value to attribute positionText.
     */
	public void setPositionText(net.ihe.gazelle.hl7v3.datatypes.ED positionText) {
	    this.positionText = positionText;
	}
	
	
	
	
	/**
	 * Return injuryLocation.
	 * @return injuryLocation
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt290000UV06.COCTMT290000UV06InjuryLocation> getInjuryLocation() {
		if (injuryLocation == null) {
	        injuryLocation = new ArrayList<>();
	    }
	    return injuryLocation;
	}
	
	/**
	 * Set a value to attribute injuryLocation.
     */
	public void setInjuryLocation(List<net.ihe.gazelle.hl7v3.coctmt290000UV06.COCTMT290000UV06InjuryLocation> injuryLocation) {
	    this.injuryLocation = injuryLocation;
	}
	
	
	
	/**
	 * Add a injuryLocation to the injuryLocation collection.
	 * @param injuryLocation_elt Element to add.
	 */
	public void addInjuryLocation(net.ihe.gazelle.hl7v3.coctmt290000UV06.COCTMT290000UV06InjuryLocation injuryLocation_elt) {
	    this.injuryLocation.add(injuryLocation_elt);
	}
	
	/**
	 * Remove a injuryLocation to the injuryLocation collection.
	 * @param injuryLocation_elt Element to remove
	 */
	public void removeInjuryLocation(net.ihe.gazelle.hl7v3.coctmt290000UV06.COCTMT290000UV06InjuryLocation injuryLocation_elt) {
	    this.injuryLocation.remove(injuryLocation_elt);
	}
	
	/**
	 * Return classCode.
	 * @return classCode
	 */
	public net.ihe.gazelle.hl7v3.voc.EntityClassPlace getClassCode() {
	    return classCode;
	}
	
	/**
	 * Set a value to attribute classCode.
     */
	public void setClassCode(net.ihe.gazelle.hl7v3.voc.EntityClassPlace classCode) {
	    this.classCode = classCode;
	}
	
	
	
	
	/**
	 * Return determinerCode.
	 * @return determinerCode
	 */
	public net.ihe.gazelle.hl7v3.voc.EntityDeterminer getDeterminerCode() {
	    return determinerCode;
	}
	
	/**
	 * Set a value to attribute determinerCode.
     */
	public void setDeterminerCode(net.ihe.gazelle.hl7v3.voc.EntityDeterminer determinerCode) {
	    this.determinerCode = determinerCode;
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.coctmt290000UV06");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT290000UV06.InjuryPlace").item(0);
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
   public static void validateByModule(COCTMT290000UV06InjuryPlace cOCTMT290000UV06InjuryPlace, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT290000UV06InjuryPlace != null){
   			cvm.validate(cOCTMT290000UV06InjuryPlace, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT290000UV06InjuryPlace.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT290000UV06InjuryPlace.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT290000UV06InjuryPlace.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II id: cOCTMT290000UV06InjuryPlace.getId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(id, _location + "/id[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT290000UV06InjuryPlace.getCode(), _location + "/code", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.EN name: cOCTMT290000UV06InjuryPlace.getName()){
					net.ihe.gazelle.hl7v3.datatypes.EN.validateByModule(name, _location + "/name[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.ED.validateByModule(cOCTMT290000UV06InjuryPlace.getDesc(), _location + "/desc", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.ED.validateByModule(cOCTMT290000UV06InjuryPlace.getPositionText(), _location + "/positionText", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt290000UV06.COCTMT290000UV06InjuryLocation injuryLocation: cOCTMT290000UV06InjuryPlace.getInjuryLocation()){
					net.ihe.gazelle.hl7v3.coctmt290000UV06.COCTMT290000UV06InjuryLocation.validateByModule(injuryLocation, _location + "/injuryLocation[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
    	}
    }

}