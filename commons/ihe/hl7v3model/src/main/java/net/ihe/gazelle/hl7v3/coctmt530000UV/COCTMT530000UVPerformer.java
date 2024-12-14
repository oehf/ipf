/**
 * COCTMT530000UVPerformer.java
 * <p>
 * File generated from the coctmt530000UV::COCTMT530000UVPerformer uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.coctmt530000UV;

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
 * Description of the class COCTMT530000UVPerformer.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT530000UV.Performer", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"functionCode",
	"time",
	"modeCode",
	"relatedEntity",
	"patient",
	"assignedEntity",
	"contextControlCode",
	"nullFlavor",
	"typeCode"
})
@XmlRootElement(name = "COCT_MT530000UV.Performer")
public class COCTMT530000UVPerformer implements java.io.Serializable {
	
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
	@XmlElement(name = "functionCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE functionCode;
	@XmlElement(name = "time", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS time;
	@XmlElement(name = "modeCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE modeCode;
	@XmlElement(name = "relatedEntity", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVRelatedEntity relatedEntity;
	@XmlElement(name = "patient", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt050000UV01.COCTMT050000UV01Patient patient;
	@XmlElement(name = "assignedEntity", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01AssignedEntity assignedEntity;
	@XmlAttribute(name = "contextControlCode")
	public net.ihe.gazelle.hl7v3.voc.ContextControl contextControlCode;
	@XmlAttribute(name = "nullFlavor")
	public net.ihe.gazelle.hl7v3.voc.NullFlavor nullFlavor;
	@XmlAttribute(name = "typeCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.ParticipationPhysicalPerformer typeCode;
	
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
	 * Return functionCode.
	 * @return functionCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getFunctionCode() {
	    return functionCode;
	}
	
	/**
	 * Set a value to attribute functionCode.
     */
	public void setFunctionCode(net.ihe.gazelle.hl7v3.datatypes.CE functionCode) {
	    this.functionCode = functionCode;
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
     */
	public void setModeCode(net.ihe.gazelle.hl7v3.datatypes.CE modeCode) {
	    this.modeCode = modeCode;
	}
	
	
	
	
	/**
	 * Return relatedEntity.
	 * @return relatedEntity
	 */
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVRelatedEntity getRelatedEntity() {
	    return relatedEntity;
	}
	
	/**
	 * Set a value to attribute relatedEntity.
     */
	public void setRelatedEntity(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVRelatedEntity relatedEntity) {
	    this.relatedEntity = relatedEntity;
	}
	
	
	
	
	/**
	 * Return patient.
	 * @return patient
	 */
	public net.ihe.gazelle.hl7v3.coctmt050000UV01.COCTMT050000UV01Patient getPatient() {
	    return patient;
	}
	
	/**
	 * Set a value to attribute patient.
     */
	public void setPatient(net.ihe.gazelle.hl7v3.coctmt050000UV01.COCTMT050000UV01Patient patient) {
	    this.patient = patient;
	}
	
	
	
	
	/**
	 * Return assignedEntity.
	 * @return assignedEntity
	 */
	public net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01AssignedEntity getAssignedEntity() {
	    return assignedEntity;
	}
	
	/**
	 * Set a value to attribute assignedEntity.
     */
	public void setAssignedEntity(net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01AssignedEntity assignedEntity) {
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
     */
	public void setNullFlavor(net.ihe.gazelle.hl7v3.voc.NullFlavor nullFlavor) {
	    this.nullFlavor = nullFlavor;
	}
	
	
	
	
	/**
	 * Return typeCode.
	 * @return typeCode
	 */
	public net.ihe.gazelle.hl7v3.voc.ParticipationPhysicalPerformer getTypeCode() {
	    return typeCode;
	}
	
	/**
	 * Set a value to attribute typeCode.
     */
	public void setTypeCode(net.ihe.gazelle.hl7v3.voc.ParticipationPhysicalPerformer typeCode) {
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
				} catch (ParserConfigurationException ignored) {}
				try {
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.coctmt530000UV");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT530000UV.Performer").item(0);
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
   public static void validateByModule(COCTMT530000UVPerformer cOCTMT530000UVPerformer, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT530000UVPerformer != null){
   			cvm.validate(cOCTMT530000UVPerformer, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT530000UVPerformer.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT530000UVPerformer.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT530000UVPerformer.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT530000UVPerformer.getFunctionCode(), _location + "/functionCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.IVLTS.validateByModule(cOCTMT530000UVPerformer.getTime(), _location + "/time", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT530000UVPerformer.getModeCode(), _location + "/modeCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVRelatedEntity.validateByModule(cOCTMT530000UVPerformer.getRelatedEntity(), _location + "/relatedEntity", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt050000UV01.COCTMT050000UV01Patient.validateByModule(cOCTMT530000UVPerformer.getPatient(), _location + "/patient", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt090000UV01.COCTMT090000UV01AssignedEntity.validateByModule(cOCTMT530000UVPerformer.getAssignedEntity(), _location + "/assignedEntity", cvm, diagnostic);
    	}
    }

}