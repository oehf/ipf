/**
 * PRPAMT201302UV02Guardian.java
 * <p>
 * File generated from the prpamt201302UV02::PRPAMT201302UV02Guardian uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.prpamt201302UV02;

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
 * Description of the class PRPAMT201302UV02Guardian.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PRPA_MT201302UV02.Guardian", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"id",
	"addr",
	"telecom",
	"statusCode",
	"effectiveTime",
	"certificateText",
	"guardianPerson",
	"guardianOrganization",
	"classCode",
	"negationInd",
	"nullFlavor"
})
@XmlRootElement(name = "PRPA_MT201302UV02.Guardian")
public class PRPAMT201302UV02Guardian implements java.io.Serializable {
	
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
	public List<net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02GuardianId> id;
	@XmlElement(name = "addr", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.AD> addr;
	@XmlElement(name = "telecom", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.TEL> telecom;
	@XmlElement(name = "statusCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CS statusCode;
	@XmlElement(name = "effectiveTime", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS effectiveTime;
	@XmlElement(name = "certificateText", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.ED certificateText;
	@XmlElement(name = "guardianPerson", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt030207UV.COCTMT030207UVPerson guardianPerson;
	@XmlElement(name = "guardianOrganization", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt150007UV.COCTMT150007UVOrganization guardianOrganization;
	@XmlAttribute(name = "classCode")
	public java.lang.String classCode;
	@XmlAttribute(name = "negationInd")
	public Boolean negationInd;
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
	public List<net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02GuardianId> getId() {
		if (id == null) {
	        id = new ArrayList<>();
	    }
	    return id;
	}
	
	/**
	 * Set a value to attribute id.
     */
	public void setId(List<net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02GuardianId> id) {
	    this.id = id;
	}
	
	
	
	/**
	 * Add a id to the id collection.
	 * @param id_elt Element to add.
	 */
	public void addId(net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02GuardianId id_elt) {
	    this.id.add(id_elt);
	}
	
	/**
	 * Remove a id to the id collection.
	 * @param id_elt Element to remove
	 */
	public void removeId(net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02GuardianId id_elt) {
	    this.id.remove(id_elt);
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
	 * Return statusCode.
	 * @return statusCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CS getStatusCode() {
	    return statusCode;
	}
	
	/**
	 * Set a value to attribute statusCode.
     */
	public void setStatusCode(net.ihe.gazelle.hl7v3.datatypes.CS statusCode) {
	    this.statusCode = statusCode;
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
	 * Return guardianPerson.
	 * @return guardianPerson
	 */
	public net.ihe.gazelle.hl7v3.coctmt030207UV.COCTMT030207UVPerson getGuardianPerson() {
	    return guardianPerson;
	}
	
	/**
	 * Set a value to attribute guardianPerson.
     */
	public void setGuardianPerson(net.ihe.gazelle.hl7v3.coctmt030207UV.COCTMT030207UVPerson guardianPerson) {
	    this.guardianPerson = guardianPerson;
	}
	
	
	
	
	/**
	 * Return guardianOrganization.
	 * @return guardianOrganization
	 */
	public net.ihe.gazelle.hl7v3.coctmt150007UV.COCTMT150007UVOrganization getGuardianOrganization() {
	    return guardianOrganization;
	}
	
	/**
	 * Set a value to attribute guardianOrganization.
     */
	public void setGuardianOrganization(net.ihe.gazelle.hl7v3.coctmt150007UV.COCTMT150007UVOrganization guardianOrganization) {
	    this.guardianOrganization = guardianOrganization;
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
	 * Return negationInd.
	 * @return negationInd
	 */
	public Boolean getNegationInd() {
	    return negationInd;
	}
	
	/**
	 * Set a value to attribute negationInd.
     */
	public void setNegationInd(Boolean negationInd) {
	    this.negationInd = negationInd;
	}
	
	
	/**
	 * Return negationInd.
	 * @return negationInd
	 * Generated for the use on jsf pages
	 */
	 @Deprecated
	public Boolean isNegationInd() {
	    return negationInd;
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.prpamt201302UV02");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "PRPA_MT201302UV02.Guardian").item(0);
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
   public static void validateByModule(PRPAMT201302UV02Guardian pRPAMT201302UV02Guardian, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (pRPAMT201302UV02Guardian != null){
   			cvm.validate(pRPAMT201302UV02Guardian, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: pRPAMT201302UV02Guardian.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(pRPAMT201302UV02Guardian.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: pRPAMT201302UV02Guardian.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02GuardianId id: pRPAMT201302UV02Guardian.getId()){
					net.ihe.gazelle.hl7v3.prpamt201302UV02.PRPAMT201302UV02GuardianId.validateByModule(id, _location + "/id[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.AD addr: pRPAMT201302UV02Guardian.getAddr()){
					net.ihe.gazelle.hl7v3.datatypes.AD.validateByModule(addr, _location + "/addr[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.TEL telecom: pRPAMT201302UV02Guardian.getTelecom()){
					net.ihe.gazelle.hl7v3.datatypes.TEL.validateByModule(telecom, _location + "/telecom[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(pRPAMT201302UV02Guardian.getStatusCode(), _location + "/statusCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.IVLTS.validateByModule(pRPAMT201302UV02Guardian.getEffectiveTime(), _location + "/effectiveTime", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.ED.validateByModule(pRPAMT201302UV02Guardian.getCertificateText(), _location + "/certificateText", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt030207UV.COCTMT030207UVPerson.validateByModule(pRPAMT201302UV02Guardian.getGuardianPerson(), _location + "/guardianPerson", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt150007UV.COCTMT150007UVOrganization.validateByModule(pRPAMT201302UV02Guardian.getGuardianOrganization(), _location + "/guardianOrganization", cvm, diagnostic);
    	}
    }

}