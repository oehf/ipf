/**
 * COCTMT820000UVAssignedProvider.java
 * <p>
 * File generated from the coctmt820000UV::COCTMT820000UVAssignedProvider uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.coctmt820000UV;

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
 * Description of the class COCTMT820000UVAssignedProvider.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT820000UV.AssignedProvider", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"id",
	"code",
	"addr",
	"telecom",
	"effectiveTime",
	"certificateText",
	"assignedPerson",
	"representedOrganization",
	"classCode",
	"nullFlavor"
})
@XmlRootElement(name = "COCT_MT820000UV.AssignedProvider")
public class COCTMT820000UVAssignedProvider implements java.io.Serializable {
	
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
	@XmlElement(name = "addr", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.AD> addr;
	@XmlElement(name = "telecom", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.TEL> telecom;
	@XmlElement(name = "effectiveTime", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS effectiveTime;
	@XmlElement(name = "certificateText", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.ED certificateText;
	@XmlElement(name = "assignedPerson", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt820000UV.COCTMT820000UVPerson assignedPerson;
	@XmlElement(name = "representedOrganization", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt150000UV02.COCTMT150000UV02Organization representedOrganization;
	@XmlAttribute(name = "classCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.RoleClassAssignedEntity classCode;
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
	 * Return addr.
	 * @return addr
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.AD> getAddr() {
		if (addr == null) {
	        addr = new ArrayList<>();
	    }
	    return addr;
	}
	
	/**
	 * Set a value to attribute addr.
     */
	public void setAddr(List<net.ihe.gazelle.hl7v3.datatypes.AD> addr) {
	    this.addr = addr;
	}
	
	
	
	/**
	 * Add a addr to the addr collection.
	 * @param addr_elt Element to add.
	 */
	public void addAddr(net.ihe.gazelle.hl7v3.datatypes.AD addr_elt) {
	    this.addr.add(addr_elt);
	}
	
	/**
	 * Remove a addr to the addr collection.
	 * @param addr_elt Element to remove
	 */
	public void removeAddr(net.ihe.gazelle.hl7v3.datatypes.AD addr_elt) {
	    this.addr.remove(addr_elt);
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
	 * Return effectiveTime.
	 * @return effectiveTime
	 */
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS getEffectiveTime() {
	    return effectiveTime;
	}
	
	/**
	 * Set a value to attribute effectiveTime.
     */
	public void setEffectiveTime(net.ihe.gazelle.hl7v3.datatypes.IVLTS effectiveTime) {
	    this.effectiveTime = effectiveTime;
	}
	
	
	
	
	/**
	 * Return certificateText.
	 * @return certificateText
	 */
	public net.ihe.gazelle.hl7v3.datatypes.ED getCertificateText() {
	    return certificateText;
	}
	
	/**
	 * Set a value to attribute certificateText.
     */
	public void setCertificateText(net.ihe.gazelle.hl7v3.datatypes.ED certificateText) {
	    this.certificateText = certificateText;
	}
	
	
	
	
	/**
	 * Return assignedPerson.
	 * @return assignedPerson
	 */
	public net.ihe.gazelle.hl7v3.coctmt820000UV.COCTMT820000UVPerson getAssignedPerson() {
	    return assignedPerson;
	}
	
	/**
	 * Set a value to attribute assignedPerson.
     */
	public void setAssignedPerson(net.ihe.gazelle.hl7v3.coctmt820000UV.COCTMT820000UVPerson assignedPerson) {
	    this.assignedPerson = assignedPerson;
	}
	
	
	
	
	/**
	 * Return representedOrganization.
	 * @return representedOrganization
	 */
	public net.ihe.gazelle.hl7v3.coctmt150000UV02.COCTMT150000UV02Organization getRepresentedOrganization() {
	    return representedOrganization;
	}
	
	/**
	 * Set a value to attribute representedOrganization.
     */
	public void setRepresentedOrganization(net.ihe.gazelle.hl7v3.coctmt150000UV02.COCTMT150000UV02Organization representedOrganization) {
	    this.representedOrganization = representedOrganization;
	}
	
	
	
	
	/**
	 * Return classCode.
	 * @return classCode
	 */
	public net.ihe.gazelle.hl7v3.voc.RoleClassAssignedEntity getClassCode() {
	    return classCode;
	}
	
	/**
	 * Set a value to attribute classCode.
     */
	public void setClassCode(net.ihe.gazelle.hl7v3.voc.RoleClassAssignedEntity classCode) {
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.coctmt820000UV");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT820000UV.AssignedProvider").item(0);
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
   public static void validateByModule(COCTMT820000UVAssignedProvider cOCTMT820000UVAssignedProvider, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT820000UVAssignedProvider != null){
   			cvm.validate(cOCTMT820000UVAssignedProvider, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT820000UVAssignedProvider.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT820000UVAssignedProvider.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT820000UVAssignedProvider.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II id: cOCTMT820000UVAssignedProvider.getId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(id, _location + "/id[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT820000UVAssignedProvider.getCode(), _location + "/code", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.AD addr: cOCTMT820000UVAssignedProvider.getAddr()){
					net.ihe.gazelle.hl7v3.datatypes.AD.validateByModule(addr, _location + "/addr[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.TEL telecom: cOCTMT820000UVAssignedProvider.getTelecom()){
					net.ihe.gazelle.hl7v3.datatypes.TEL.validateByModule(telecom, _location + "/telecom[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.IVLTS.validateByModule(cOCTMT820000UVAssignedProvider.getEffectiveTime(), _location + "/effectiveTime", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.ED.validateByModule(cOCTMT820000UVAssignedProvider.getCertificateText(), _location + "/certificateText", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt820000UV.COCTMT820000UVPerson.validateByModule(cOCTMT820000UVAssignedProvider.getAssignedPerson(), _location + "/assignedPerson", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt150000UV02.COCTMT150000UV02Organization.validateByModule(cOCTMT820000UVAssignedProvider.getRepresentedOrganization(), _location + "/representedOrganization", cvm, diagnostic);
    	}
    }

}