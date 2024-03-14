/**
 * MFMIMT700701UV01Author2.java
 *
 * File generated from the mfmimt700701UV01::MFMIMT700701UV01Author2 uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.mfmimt700701UV01;

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
 * Description of the class MFMIMT700701UV01Author2.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MFMI_MT700701UV01.Author2", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"noteText",
	"time",
	"modeCode",
	"assignedEntity",
	"contextControlCode",
	"nullFlavor",
	"typeCode"
})
@XmlRootElement(name = "MFMI_MT700701UV01.Author2")
public class MFMIMT700701UV01Author2 implements java.io.Serializable {
	
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
	@XmlElement(name = "noteText", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.ED noteText;
	@XmlElement(name = "time", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS time;
	@XmlElement(name = "modeCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE modeCode;
	@XmlElement(name = "assignedEntity", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt090003UV01.COCTMT090003UV01AssignedEntity assignedEntity;
	@XmlAttribute(name = "contextControlCode")
	public net.ihe.gazelle.hl7v3.voc.ContextControl contextControlCode;
	@XmlAttribute(name = "nullFlavor")
	public net.ihe.gazelle.hl7v3.voc.NullFlavor nullFlavor;
	@XmlAttribute(name = "typeCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.ParticipationType typeCode;
	
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
	 * Return noteText.
	 * @return noteText
	 */
	public net.ihe.gazelle.hl7v3.datatypes.ED getNoteText() {
	    return noteText;
	}
	
	/**
	 * Set a value to attribute noteText.
	 * @param noteText.
	 */
	public void setNoteText(net.ihe.gazelle.hl7v3.datatypes.ED noteText) {
	    this.noteText = noteText;
	}
	
	
	
	
	/**
	 * Return time.
	 * @return time
	 */
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS getTime() {
	    return time;
	}
	
	/**
	 * Set a value to attribute time.
	 * @param time.
	 */
	public void setTime(net.ihe.gazelle.hl7v3.datatypes.IVLTS time) {
	    this.time = time;
	}
	
	
	
	
	/**
	 * Return modeCode.
	 * @return modeCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getModeCode() {
	    return modeCode;
	}
	
	/**
	 * Set a value to attribute modeCode.
	 * @param modeCode.
	 */
	public void setModeCode(net.ihe.gazelle.hl7v3.datatypes.CE modeCode) {
	    this.modeCode = modeCode;
	}
	
	
	
	
	/**
	 * Return assignedEntity.
	 * @return assignedEntity
	 */
	public net.ihe.gazelle.hl7v3.coctmt090003UV01.COCTMT090003UV01AssignedEntity getAssignedEntity() {
	    return assignedEntity;
	}
	
	/**
	 * Set a value to attribute assignedEntity.
	 * @param assignedEntity.
	 */
	public void setAssignedEntity(net.ihe.gazelle.hl7v3.coctmt090003UV01.COCTMT090003UV01AssignedEntity assignedEntity) {
	    this.assignedEntity = assignedEntity;
	}
	
	
	
	
	/**
	 * Return contextControlCode.
	 * @return contextControlCode
	 */
	public net.ihe.gazelle.hl7v3.voc.ContextControl getContextControlCode() {
	    return contextControlCode;
	}
	
	/**
	 * Set a value to attribute contextControlCode.
	 * @param contextControlCode.
	 */
	public void setContextControlCode(net.ihe.gazelle.hl7v3.voc.ContextControl contextControlCode) {
	    this.contextControlCode = contextControlCode;
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
	
	
	
	
	/**
	 * Return typeCode.
	 * @return typeCode
	 */
	public net.ihe.gazelle.hl7v3.voc.ParticipationType getTypeCode() {
	    return typeCode;
	}
	
	/**
	 * Set a value to attribute typeCode.
	 * @param typeCode.
	 */
	public void setTypeCode(net.ihe.gazelle.hl7v3.voc.ParticipationType typeCode) {
	    this.typeCode = typeCode;
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.mfmimt700701UV01");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "MFMI_MT700701UV01.Author2").item(0);
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
   public static void validateByModule(MFMIMT700701UV01Author2 mFMIMT700701UV01Author2, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (mFMIMT700701UV01Author2 != null){
   			cvm.validate(mFMIMT700701UV01Author2, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: mFMIMT700701UV01Author2.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(mFMIMT700701UV01Author2.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: mFMIMT700701UV01Author2.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.ED.validateByModule(mFMIMT700701UV01Author2.getNoteText(), _location + "/noteText", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.IVLTS.validateByModule(mFMIMT700701UV01Author2.getTime(), _location + "/time", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(mFMIMT700701UV01Author2.getModeCode(), _location + "/modeCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt090003UV01.COCTMT090003UV01AssignedEntity.validateByModule(mFMIMT700701UV01Author2.getAssignedEntity(), _location + "/assignedEntity", cvm, diagnostic);
    	}
    }

}