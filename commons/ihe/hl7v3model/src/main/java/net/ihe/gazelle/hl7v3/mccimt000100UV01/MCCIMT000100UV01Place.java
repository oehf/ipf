/**
 * MCCIMT000100UV01Place.java
 * <p>
 * File generated from the mccimt000100UV01::MCCIMT000100UV01Place uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.mccimt000100UV01;

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
 * Description of the class MCCIMT000100UV01Place.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MCCI_MT000100UV01.Place", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"id",
	"name",
	"telecom",
	"classCode",
	"determinerCode",
	"nullFlavor"
})
@XmlRootElement(name = "MCCI_MT000100UV01.Place")
public class MCCIMT000100UV01Place implements java.io.Serializable {
	
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
	@XmlElement(name = "id", required = true, namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.II> id;
	@XmlElement(name = "name", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.EN> name;
	@XmlElement(name = "telecom", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.TEL> telecom;
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
	 * Return telecom.
	 * @return telecom
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.TEL> getTelecom() {
		if (telecom == null) {
	        telecom = new ArrayList<>();
	    }
	    return telecom;
	}
	
	/**
	 * Set a value to attribute telecom.
     */
	public void setTelecom(List<net.ihe.gazelle.hl7v3.datatypes.TEL> telecom) {
	    this.telecom = telecom;
	}
	
	
	
	/**
	 * Add a telecom to the telecom collection.
	 * @param telecom_elt Element to add.
	 */
	public void addTelecom(net.ihe.gazelle.hl7v3.datatypes.TEL telecom_elt) {
	    this.telecom.add(telecom_elt);
	}
	
	/**
	 * Remove a telecom to the telecom collection.
	 * @param telecom_elt Element to remove
	 */
	public void removeTelecom(net.ihe.gazelle.hl7v3.datatypes.TEL telecom_elt) {
	    this.telecom.remove(telecom_elt);
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.mccimt000100UV01");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "MCCI_MT000100UV01.Place").item(0);
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
   public static void validateByModule(MCCIMT000100UV01Place mCCIMT000100UV01Place, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (mCCIMT000100UV01Place != null){
   			cvm.validate(mCCIMT000100UV01Place, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: mCCIMT000100UV01Place.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(mCCIMT000100UV01Place.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: mCCIMT000100UV01Place.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II id: mCCIMT000100UV01Place.getId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(id, _location + "/id[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.EN name: mCCIMT000100UV01Place.getName()){
					net.ihe.gazelle.hl7v3.datatypes.EN.validateByModule(name, _location + "/name[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.TEL telecom: mCCIMT000100UV01Place.getTelecom()){
					net.ihe.gazelle.hl7v3.datatypes.TEL.validateByModule(telecom, _location + "/telecom[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
    	}
    }

}