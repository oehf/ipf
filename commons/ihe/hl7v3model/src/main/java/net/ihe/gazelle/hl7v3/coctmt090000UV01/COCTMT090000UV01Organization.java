/**
 * COCTMT090000UV01Organization.java
 *
 * File generated from the coctmt090000UV01::COCTMT090000UV01Organization uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.coctmt090000UV01;

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
 * Description of the class COCTMT090000UV01Organization.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT090000UV01.Organization", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"name",
	"asLicensedEntity",
	"asMember",
	"asRoleOther",
	"asLocatedEntity",
	"languageCommunication",
	"classCode",
	"determinerCode",
	"nullFlavor"
})
@XmlRootElement(name = "COCT_MT090000UV01.Organization")
public class COCTMT090000UV01Organization implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@XmlElement(name = "realmCode", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.CS> realmCode;
	@XmlElement(name = "typeId", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.II typeId;
	@XmlElement(name = "templateId", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.II> templateId;
	@XmlElement(name = "name", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.EN> name;
	@XmlElement(name = "asLicensedEntity", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01LicensedEntity> asLicensedEntity;
	@XmlElement(name = "asMember", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01Member> asMember;
	@XmlElement(name = "asRoleOther", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01RoleOther> asRoleOther;
	@XmlElement(name = "asLocatedEntity", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt070000UV01.COCTMT070000UV01LocatedEntity asLocatedEntity;
	@XmlElement(name = "languageCommunication", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01LanguageCommunication> languageCommunication;
	@XmlAttribute(name = "classCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.EntityClassOrganization classCode;
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
	        realmCode = new ArrayList<net.ihe.gazelle.hl7v3.datatypes.CS>();
	    }
	    return realmCode;
	}
	
	/**
	 * Set a value to attribute realmCode.
	 * @param realmCode.
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
	 * @param typeId.
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
	        templateId = new ArrayList<net.ihe.gazelle.hl7v3.datatypes.II>();
	    }
	    return templateId;
	}
	
	/**
	 * Set a value to attribute templateId.
	 * @param templateId.
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
	 * Return name.
	 * @return name
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.EN> getName() {
		if (name == null) {
	        name = new ArrayList<net.ihe.gazelle.hl7v3.datatypes.EN>();
	    }
	    return name;
	}
	
	/**
	 * Set a value to attribute name.
	 * @param name.
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
	 * Return asLicensedEntity.
	 * @return asLicensedEntity
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01LicensedEntity> getAsLicensedEntity() {
		if (asLicensedEntity == null) {
	        asLicensedEntity = new ArrayList<net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01LicensedEntity>();
	    }
	    return asLicensedEntity;
	}
	
	/**
	 * Set a value to attribute asLicensedEntity.
	 * @param asLicensedEntity.
	 */
	public void setAsLicensedEntity(List<net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01LicensedEntity> asLicensedEntity) {
	    this.asLicensedEntity = asLicensedEntity;
	}
	
	
	
	/**
	 * Add a asLicensedEntity to the asLicensedEntity collection.
	 * @param asLicensedEntity_elt Element to add.
	 */
	public void addAsLicensedEntity(net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01LicensedEntity asLicensedEntity_elt) {
	    this.asLicensedEntity.add(asLicensedEntity_elt);
	}
	
	/**
	 * Remove a asLicensedEntity to the asLicensedEntity collection.
	 * @param asLicensedEntity_elt Element to remove
	 */
	public void removeAsLicensedEntity(net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01LicensedEntity asLicensedEntity_elt) {
	    this.asLicensedEntity.remove(asLicensedEntity_elt);
	}
	
	/**
	 * Return asMember.
	 * @return asMember
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01Member> getAsMember() {
		if (asMember == null) {
	        asMember = new ArrayList<net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01Member>();
	    }
	    return asMember;
	}
	
	/**
	 * Set a value to attribute asMember.
	 * @param asMember.
	 */
	public void setAsMember(List<net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01Member> asMember) {
	    this.asMember = asMember;
	}
	
	
	
	/**
	 * Add a asMember to the asMember collection.
	 * @param asMember_elt Element to add.
	 */
	public void addAsMember(net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01Member asMember_elt) {
	    this.asMember.add(asMember_elt);
	}
	
	/**
	 * Remove a asMember to the asMember collection.
	 * @param asMember_elt Element to remove
	 */
	public void removeAsMember(net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01Member asMember_elt) {
	    this.asMember.remove(asMember_elt);
	}
	
	/**
	 * Return asRoleOther.
	 * @return asRoleOther
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01RoleOther> getAsRoleOther() {
		if (asRoleOther == null) {
	        asRoleOther = new ArrayList<net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01RoleOther>();
	    }
	    return asRoleOther;
	}
	
	/**
	 * Set a value to attribute asRoleOther.
	 * @param asRoleOther.
	 */
	public void setAsRoleOther(List<net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01RoleOther> asRoleOther) {
	    this.asRoleOther = asRoleOther;
	}
	
	
	
	/**
	 * Add a asRoleOther to the asRoleOther collection.
	 * @param asRoleOther_elt Element to add.
	 */
	public void addAsRoleOther(net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01RoleOther asRoleOther_elt) {
	    this.asRoleOther.add(asRoleOther_elt);
	}
	
	/**
	 * Remove a asRoleOther to the asRoleOther collection.
	 * @param asRoleOther_elt Element to remove
	 */
	public void removeAsRoleOther(net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01RoleOther asRoleOther_elt) {
	    this.asRoleOther.remove(asRoleOther_elt);
	}
	
	/**
	 * Return asLocatedEntity.
	 * @return asLocatedEntity
	 */
	public net.ihe.gazelle.hl7v3.coctmt070000UV01.COCTMT070000UV01LocatedEntity getAsLocatedEntity() {
	    return asLocatedEntity;
	}
	
	/**
	 * Set a value to attribute asLocatedEntity.
	 * @param asLocatedEntity.
	 */
	public void setAsLocatedEntity(net.ihe.gazelle.hl7v3.coctmt070000UV01.COCTMT070000UV01LocatedEntity asLocatedEntity) {
	    this.asLocatedEntity = asLocatedEntity;
	}
	
	
	
	
	/**
	 * Return languageCommunication.
	 * @return languageCommunication
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01LanguageCommunication> getLanguageCommunication() {
		if (languageCommunication == null) {
	        languageCommunication = new ArrayList<net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01LanguageCommunication>();
	    }
	    return languageCommunication;
	}
	
	/**
	 * Set a value to attribute languageCommunication.
	 * @param languageCommunication.
	 */
	public void setLanguageCommunication(List<net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01LanguageCommunication> languageCommunication) {
	    this.languageCommunication = languageCommunication;
	}
	
	
	
	/**
	 * Add a languageCommunication to the languageCommunication collection.
	 * @param languageCommunication_elt Element to add.
	 */
	public void addLanguageCommunication(net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01LanguageCommunication languageCommunication_elt) {
	    this.languageCommunication.add(languageCommunication_elt);
	}
	
	/**
	 * Remove a languageCommunication to the languageCommunication collection.
	 * @param languageCommunication_elt Element to remove
	 */
	public void removeLanguageCommunication(net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01LanguageCommunication languageCommunication_elt) {
	    this.languageCommunication.remove(languageCommunication_elt);
	}
	
	/**
	 * Return classCode.
	 * @return classCode
	 */
	public net.ihe.gazelle.hl7v3.voc.EntityClassOrganization getClassCode() {
	    return classCode;
	}
	
	/**
	 * Set a value to attribute classCode.
	 * @param classCode.
	 */
	public void setClassCode(net.ihe.gazelle.hl7v3.voc.EntityClassOrganization classCode) {
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
	 * @param determinerCode.
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
	 * @param nullFlavor.
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
				} catch (ParserConfigurationException e1) {}
				try {
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.coctmt090000UV01");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT090000UV01.Organization").item(0);
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
   public static void validateByModule(COCTMT090000UV01Organization cOCTMT090000UV01Organization, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT090000UV01Organization != null){
   			cvm.validate(cOCTMT090000UV01Organization, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT090000UV01Organization.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT090000UV01Organization.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT090000UV01Organization.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.EN name: cOCTMT090000UV01Organization.getName()){
					net.ihe.gazelle.hl7v3.datatypes.EN.validateByModule(name, _location + "/name[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01LicensedEntity asLicensedEntity: cOCTMT090000UV01Organization.getAsLicensedEntity()){
					net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01LicensedEntity.validateByModule(asLicensedEntity, _location + "/asLicensedEntity[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01Member asMember: cOCTMT090000UV01Organization.getAsMember()){
					net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01Member.validateByModule(asMember, _location + "/asMember[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01RoleOther asRoleOther: cOCTMT090000UV01Organization.getAsRoleOther()){
					net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01RoleOther.validateByModule(asRoleOther, _location + "/asRoleOther[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.coctmt070000UV01.COCTMT070000UV01LocatedEntity.validateByModule(cOCTMT090000UV01Organization.getAsLocatedEntity(), _location + "/asLocatedEntity", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01LanguageCommunication languageCommunication: cOCTMT090000UV01Organization.getLanguageCommunication()){
					net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01LanguageCommunication.validateByModule(languageCommunication, _location + "/languageCommunication[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
    	}
    }

}