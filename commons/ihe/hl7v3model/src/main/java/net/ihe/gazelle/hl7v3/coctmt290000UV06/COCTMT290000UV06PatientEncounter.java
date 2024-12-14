/**
 * COCTMT290000UV06PatientEncounter.java
 * <p>
 * File generated from the coctmt290000UV06::COCTMT290000UV06PatientEncounter uml Class
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
 * Description of the class COCTMT290000UV06PatientEncounter.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT290000UV06.PatientEncounter", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"id",
	"code",
	"effectiveTime",
	"activityTime",
	"priorityCode",
	"admissionReferralSourceCode",
	"dischargeDispositionCode",
	"reason",
	"classCode",
	"moodCode",
	"nullFlavor"
})
@XmlRootElement(name = "COCT_MT290000UV06.PatientEncounter")
public class COCTMT290000UV06PatientEncounter implements java.io.Serializable {
	
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
	public net.ihe.gazelle.hl7v3.datatypes.CD code;
	@XmlElement(name = "effectiveTime", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS effectiveTime;
	@XmlElement(name = "activityTime", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS activityTime;
	@XmlElement(name = "priorityCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE priorityCode;
	@XmlElement(name = "admissionReferralSourceCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE admissionReferralSourceCode;
	@XmlElement(name = "dischargeDispositionCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE dischargeDispositionCode;
	@XmlElement(name = "reason", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt290000UV06.COCTMT290000UV06Reason5> reason;
	@XmlAttribute(name = "classCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.ActClass classCode;
	@XmlAttribute(name = "moodCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.ActMood moodCode;
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
	public net.ihe.gazelle.hl7v3.datatypes.CD getCode() {
	    return code;
	}
	
	/**
	 * Set a value to attribute code.
     */
	public void setCode(net.ihe.gazelle.hl7v3.datatypes.CD code) {
	    this.code = code;
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
	 * Return activityTime.
	 * @return activityTime
	 */
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS getActivityTime() {
	    return activityTime;
	}
	
	/**
	 * Set a value to attribute activityTime.
     */
	public void setActivityTime(net.ihe.gazelle.hl7v3.datatypes.IVLTS activityTime) {
	    this.activityTime = activityTime;
	}
	
	
	
	
	/**
	 * Return priorityCode.
	 * @return priorityCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getPriorityCode() {
	    return priorityCode;
	}
	
	/**
	 * Set a value to attribute priorityCode.
     */
	public void setPriorityCode(net.ihe.gazelle.hl7v3.datatypes.CE priorityCode) {
	    this.priorityCode = priorityCode;
	}
	
	
	
	
	/**
	 * Return admissionReferralSourceCode.
	 * @return admissionReferralSourceCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getAdmissionReferralSourceCode() {
	    return admissionReferralSourceCode;
	}
	
	/**
	 * Set a value to attribute admissionReferralSourceCode.
     */
	public void setAdmissionReferralSourceCode(net.ihe.gazelle.hl7v3.datatypes.CE admissionReferralSourceCode) {
	    this.admissionReferralSourceCode = admissionReferralSourceCode;
	}
	
	
	
	
	/**
	 * Return dischargeDispositionCode.
	 * @return dischargeDispositionCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getDischargeDispositionCode() {
	    return dischargeDispositionCode;
	}
	
	/**
	 * Set a value to attribute dischargeDispositionCode.
     */
	public void setDischargeDispositionCode(net.ihe.gazelle.hl7v3.datatypes.CE dischargeDispositionCode) {
	    this.dischargeDispositionCode = dischargeDispositionCode;
	}
	
	
	
	
	/**
	 * Return reason.
	 * @return reason
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt290000UV06.COCTMT290000UV06Reason5> getReason() {
		if (reason == null) {
	        reason = new ArrayList<>();
	    }
	    return reason;
	}
	
	/**
	 * Set a value to attribute reason.
     */
	public void setReason(List<net.ihe.gazelle.hl7v3.coctmt290000UV06.COCTMT290000UV06Reason5> reason) {
	    this.reason = reason;
	}
	
	
	
	/**
	 * Add a reason to the reason collection.
	 * @param reason_elt Element to add.
	 */
	public void addReason(net.ihe.gazelle.hl7v3.coctmt290000UV06.COCTMT290000UV06Reason5 reason_elt) {
	    this.reason.add(reason_elt);
	}
	
	/**
	 * Remove a reason to the reason collection.
	 * @param reason_elt Element to remove
	 */
	public void removeReason(net.ihe.gazelle.hl7v3.coctmt290000UV06.COCTMT290000UV06Reason5 reason_elt) {
	    this.reason.remove(reason_elt);
	}
	
	/**
	 * Return classCode.
	 * @return classCode
	 */
	public net.ihe.gazelle.hl7v3.voc.ActClass getClassCode() {
	    return classCode;
	}
	
	/**
	 * Set a value to attribute classCode.
     */
	public void setClassCode(net.ihe.gazelle.hl7v3.voc.ActClass classCode) {
	    this.classCode = classCode;
	}
	
	
	
	
	/**
	 * Return moodCode.
	 * @return moodCode
	 */
	public net.ihe.gazelle.hl7v3.voc.ActMood getMoodCode() {
	    return moodCode;
	}
	
	/**
	 * Set a value to attribute moodCode.
     */
	public void setMoodCode(net.ihe.gazelle.hl7v3.voc.ActMood moodCode) {
	    this.moodCode = moodCode;
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
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT290000UV06.PatientEncounter").item(0);
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
   public static void validateByModule(COCTMT290000UV06PatientEncounter cOCTMT290000UV06PatientEncounter, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT290000UV06PatientEncounter != null){
   			cvm.validate(cOCTMT290000UV06PatientEncounter, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT290000UV06PatientEncounter.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT290000UV06PatientEncounter.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT290000UV06PatientEncounter.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II id: cOCTMT290000UV06PatientEncounter.getId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(id, _location + "/id[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.CD.validateByModule(cOCTMT290000UV06PatientEncounter.getCode(), _location + "/code", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.IVLTS.validateByModule(cOCTMT290000UV06PatientEncounter.getEffectiveTime(), _location + "/effectiveTime", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.IVLTS.validateByModule(cOCTMT290000UV06PatientEncounter.getActivityTime(), _location + "/activityTime", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT290000UV06PatientEncounter.getPriorityCode(), _location + "/priorityCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT290000UV06PatientEncounter.getAdmissionReferralSourceCode(), _location + "/admissionReferralSourceCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT290000UV06PatientEncounter.getDischargeDispositionCode(), _location + "/dischargeDispositionCode", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt290000UV06.COCTMT290000UV06Reason5 reason: cOCTMT290000UV06PatientEncounter.getReason()){
					net.ihe.gazelle.hl7v3.coctmt290000UV06.COCTMT290000UV06Reason5.validateByModule(reason, _location + "/reason[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
    	}
    }

}