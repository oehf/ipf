/**
 * COCTMT510000UV06Underwriter.java
 * <p>
 * File generated from the coctmt510000UV06::COCTMT510000UV06Underwriter uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.coctmt510000UV06;

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
 * Description of the class COCTMT510000UV06Underwriter.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT510000UV06.Underwriter", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"id",
	"code",
	"name",
	"addr",
	"telecom",
	"effectiveTime",
	"underwritingOrganization",
	"scoperOrganization",
	"directAuthorityOver1",
	"directAuthorityOver2",
	"classCode",
	"nullFlavor"
})
@XmlRootElement(name = "COCT_MT510000UV06.Underwriter")
public class COCTMT510000UV06Underwriter implements java.io.Serializable {
	
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
	public List<net.ihe.gazelle.hl7v3.datatypes.ON> name;
	@XmlElement(name = "addr", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.AD> addr;
	@XmlElement(name = "telecom", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.TEL> telecom;
	@XmlElement(name = "effectiveTime", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS effectiveTime;
	@XmlElement(name = "underwritingOrganization", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt150000UV02.COCTMT150000UV02Organization underwritingOrganization;
	@XmlElement(name = "scoperOrganization", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt150000UV02.COCTMT150000UV02Organization scoperOrganization;
	@XmlElement(name = "directAuthorityOver1", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt510000UV06.COCTMT510000UV06DirectAuthorityOver2> directAuthorityOver1;
	@XmlElement(name = "directAuthorityOver2", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt510000UV06.COCTMT510000UV06DirectAuthorityOver> directAuthorityOver2;
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
	public List<net.ihe.gazelle.hl7v3.datatypes.ON> getName() {
		if (name == null) {
	        name = new ArrayList<>();
	    }
	    return name;
	}
	
	/**
	 * Set a value to attribute name.
     */
	public void setName(List<net.ihe.gazelle.hl7v3.datatypes.ON> name) {
	    this.name = name;
	}
	
	
	
	/**
	 * Add a name to the name collection.
	 * @param name_elt Element to add.
	 */
	public void addName(net.ihe.gazelle.hl7v3.datatypes.ON name_elt) {
	    this.name.add(name_elt);
	}
	
	/**
	 * Remove a name to the name collection.
	 * @param name_elt Element to remove
	 */
	public void removeName(net.ihe.gazelle.hl7v3.datatypes.ON name_elt) {
	    this.name.remove(name_elt);
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
	 * Return underwritingOrganization.
	 * @return underwritingOrganization
	 */
	public net.ihe.gazelle.hl7v3.coctmt150000UV02.COCTMT150000UV02Organization getUnderwritingOrganization() {
	    return underwritingOrganization;
	}
	
	/**
	 * Set a value to attribute underwritingOrganization.
     */
	public void setUnderwritingOrganization(net.ihe.gazelle.hl7v3.coctmt150000UV02.COCTMT150000UV02Organization underwritingOrganization) {
	    this.underwritingOrganization = underwritingOrganization;
	}
	
	
	
	
	/**
	 * Return scoperOrganization.
	 * @return scoperOrganization
	 */
	public net.ihe.gazelle.hl7v3.coctmt150000UV02.COCTMT150000UV02Organization getScoperOrganization() {
	    return scoperOrganization;
	}
	
	/**
	 * Set a value to attribute scoperOrganization.
     */
	public void setScoperOrganization(net.ihe.gazelle.hl7v3.coctmt150000UV02.COCTMT150000UV02Organization scoperOrganization) {
	    this.scoperOrganization = scoperOrganization;
	}
	
	
	
	
	/**
	 * Return directAuthorityOver1.
	 * @return directAuthorityOver1
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt510000UV06.COCTMT510000UV06DirectAuthorityOver2> getDirectAuthorityOver1() {
		if (directAuthorityOver1 == null) {
	        directAuthorityOver1 = new ArrayList<>();
	    }
	    return directAuthorityOver1;
	}
	
	/**
	 * Set a value to attribute directAuthorityOver1.
     */
	public void setDirectAuthorityOver1(List<net.ihe.gazelle.hl7v3.coctmt510000UV06.COCTMT510000UV06DirectAuthorityOver2> directAuthorityOver1) {
	    this.directAuthorityOver1 = directAuthorityOver1;
	}
	
	
	
	/**
	 * Add a directAuthorityOver1 to the directAuthorityOver1 collection.
	 * @param directAuthorityOver1_elt Element to add.
	 */
	public void addDirectAuthorityOver1(net.ihe.gazelle.hl7v3.coctmt510000UV06.COCTMT510000UV06DirectAuthorityOver2 directAuthorityOver1_elt) {
	    this.directAuthorityOver1.add(directAuthorityOver1_elt);
	}
	
	/**
	 * Remove a directAuthorityOver1 to the directAuthorityOver1 collection.
	 * @param directAuthorityOver1_elt Element to remove
	 */
	public void removeDirectAuthorityOver1(net.ihe.gazelle.hl7v3.coctmt510000UV06.COCTMT510000UV06DirectAuthorityOver2 directAuthorityOver1_elt) {
	    this.directAuthorityOver1.remove(directAuthorityOver1_elt);
	}
	
	/**
	 * Return directAuthorityOver2.
	 * @return directAuthorityOver2
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt510000UV06.COCTMT510000UV06DirectAuthorityOver> getDirectAuthorityOver2() {
		if (directAuthorityOver2 == null) {
	        directAuthorityOver2 = new ArrayList<>();
	    }
	    return directAuthorityOver2;
	}
	
	/**
	 * Set a value to attribute directAuthorityOver2.
     */
	public void setDirectAuthorityOver2(List<net.ihe.gazelle.hl7v3.coctmt510000UV06.COCTMT510000UV06DirectAuthorityOver> directAuthorityOver2) {
	    this.directAuthorityOver2 = directAuthorityOver2;
	}
	
	
	
	/**
	 * Add a directAuthorityOver2 to the directAuthorityOver2 collection.
	 * @param directAuthorityOver2_elt Element to add.
	 */
	public void addDirectAuthorityOver2(net.ihe.gazelle.hl7v3.coctmt510000UV06.COCTMT510000UV06DirectAuthorityOver directAuthorityOver2_elt) {
	    this.directAuthorityOver2.add(directAuthorityOver2_elt);
	}
	
	/**
	 * Remove a directAuthorityOver2 to the directAuthorityOver2 collection.
	 * @param directAuthorityOver2_elt Element to remove
	 */
	public void removeDirectAuthorityOver2(net.ihe.gazelle.hl7v3.coctmt510000UV06.COCTMT510000UV06DirectAuthorityOver directAuthorityOver2_elt) {
	    this.directAuthorityOver2.remove(directAuthorityOver2_elt);
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.coctmt510000UV06");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT510000UV06.Underwriter").item(0);
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
   public static void validateByModule(COCTMT510000UV06Underwriter cOCTMT510000UV06Underwriter, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT510000UV06Underwriter != null){
   			cvm.validate(cOCTMT510000UV06Underwriter, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT510000UV06Underwriter.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT510000UV06Underwriter.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT510000UV06Underwriter.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II id: cOCTMT510000UV06Underwriter.getId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(id, _location + "/id[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT510000UV06Underwriter.getCode(), _location + "/code", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.ON name: cOCTMT510000UV06Underwriter.getName()){
					net.ihe.gazelle.hl7v3.datatypes.ON.validateByModule(name, _location + "/name[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.AD addr: cOCTMT510000UV06Underwriter.getAddr()){
					net.ihe.gazelle.hl7v3.datatypes.AD.validateByModule(addr, _location + "/addr[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.TEL telecom: cOCTMT510000UV06Underwriter.getTelecom()){
					net.ihe.gazelle.hl7v3.datatypes.TEL.validateByModule(telecom, _location + "/telecom[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.IVLTS.validateByModule(cOCTMT510000UV06Underwriter.getEffectiveTime(), _location + "/effectiveTime", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt150000UV02.COCTMT150000UV02Organization.validateByModule(cOCTMT510000UV06Underwriter.getUnderwritingOrganization(), _location + "/underwritingOrganization", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt150000UV02.COCTMT150000UV02Organization.validateByModule(cOCTMT510000UV06Underwriter.getScoperOrganization(), _location + "/scoperOrganization", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt510000UV06.COCTMT510000UV06DirectAuthorityOver2 directAuthorityOver1: cOCTMT510000UV06Underwriter.getDirectAuthorityOver1()){
					net.ihe.gazelle.hl7v3.coctmt510000UV06.COCTMT510000UV06DirectAuthorityOver2.validateByModule(directAuthorityOver1, _location + "/directAuthorityOver1[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt510000UV06.COCTMT510000UV06DirectAuthorityOver directAuthorityOver2: cOCTMT510000UV06Underwriter.getDirectAuthorityOver2()){
					net.ihe.gazelle.hl7v3.coctmt510000UV06.COCTMT510000UV06DirectAuthorityOver.validateByModule(directAuthorityOver2, _location + "/directAuthorityOver2[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
    	}
    }

}