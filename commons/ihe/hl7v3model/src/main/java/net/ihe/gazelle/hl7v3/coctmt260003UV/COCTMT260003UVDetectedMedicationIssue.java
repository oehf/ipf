/**
 * COCTMT260003UVDetectedMedicationIssue.java
 *
 * File generated from the coctmt260003UV::COCTMT260003UVDetectedMedicationIssue uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.coctmt260003UV;

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
 * Description of the class COCTMT260003UVDetectedMedicationIssue.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT260003UV.DetectedMedicationIssue", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"code",
	"value",
	"definition",
	"subject",
	"mitigatedBy",
	"subjectOf",
	"classCode",
	"moodCode",
	"nullFlavor"
})
@XmlRootElement(name = "COCT_MT260003UV.DetectedMedicationIssue")
public class COCTMT260003UVDetectedMedicationIssue implements java.io.Serializable {
	
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
	@XmlElement(name = "code", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CS code;
	@XmlElement(name = "value", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE value;
	@XmlElement(name = "definition", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVDefinition> definition;
	@XmlElement(name = "subject", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVSubject2> subject;
	@XmlElement(name = "mitigatedBy", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVMitigates> mitigatedBy;
	@XmlElement(name = "subjectOf", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVSubject> subjectOf;
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
	 * Return code.
	 * @return code
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CS getCode() {
	    return code;
	}
	
	/**
	 * Set a value to attribute code.
	 * @param code.
	 */
	public void setCode(net.ihe.gazelle.hl7v3.datatypes.CS code) {
	    this.code = code;
	}
	
	
	
	
	/**
	 * Return value.
	 * @return value
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getValue() {
	    return value;
	}
	
	/**
	 * Set a value to attribute value.
	 * @param value.
	 */
	public void setValue(net.ihe.gazelle.hl7v3.datatypes.CE value) {
	    this.value = value;
	}
	
	
	
	
	/**
	 * Return definition.
	 * @return definition
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVDefinition> getDefinition() {
		if (definition == null) {
	        definition = new ArrayList<net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVDefinition>();
	    }
	    return definition;
	}
	
	/**
	 * Set a value to attribute definition.
	 * @param definition.
	 */
	public void setDefinition(List<net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVDefinition> definition) {
	    this.definition = definition;
	}
	
	
	
	/**
	 * Add a definition to the definition collection.
	 * @param definition_elt Element to add.
	 */
	public void addDefinition(net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVDefinition definition_elt) {
	    this.definition.add(definition_elt);
	}
	
	/**
	 * Remove a definition to the definition collection.
	 * @param definition_elt Element to remove
	 */
	public void removeDefinition(net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVDefinition definition_elt) {
	    this.definition.remove(definition_elt);
	}
	
	/**
	 * Return subject.
	 * @return subject
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVSubject2> getSubject() {
		if (subject == null) {
	        subject = new ArrayList<net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVSubject2>();
	    }
	    return subject;
	}
	
	/**
	 * Set a value to attribute subject.
	 * @param subject.
	 */
	public void setSubject(List<net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVSubject2> subject) {
	    this.subject = subject;
	}
	
	
	
	/**
	 * Add a subject to the subject collection.
	 * @param subject_elt Element to add.
	 */
	public void addSubject(net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVSubject2 subject_elt) {
	    this.subject.add(subject_elt);
	}
	
	/**
	 * Remove a subject to the subject collection.
	 * @param subject_elt Element to remove
	 */
	public void removeSubject(net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVSubject2 subject_elt) {
	    this.subject.remove(subject_elt);
	}
	
	/**
	 * Return mitigatedBy.
	 * @return mitigatedBy
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVMitigates> getMitigatedBy() {
		if (mitigatedBy == null) {
	        mitigatedBy = new ArrayList<net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVMitigates>();
	    }
	    return mitigatedBy;
	}
	
	/**
	 * Set a value to attribute mitigatedBy.
	 * @param mitigatedBy.
	 */
	public void setMitigatedBy(List<net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVMitigates> mitigatedBy) {
	    this.mitigatedBy = mitigatedBy;
	}
	
	
	
	/**
	 * Add a mitigatedBy to the mitigatedBy collection.
	 * @param mitigatedBy_elt Element to add.
	 */
	public void addMitigatedBy(net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVMitigates mitigatedBy_elt) {
	    this.mitigatedBy.add(mitigatedBy_elt);
	}
	
	/**
	 * Remove a mitigatedBy to the mitigatedBy collection.
	 * @param mitigatedBy_elt Element to remove
	 */
	public void removeMitigatedBy(net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVMitigates mitigatedBy_elt) {
	    this.mitigatedBy.remove(mitigatedBy_elt);
	}
	
	/**
	 * Return subjectOf.
	 * @return subjectOf
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVSubject> getSubjectOf() {
		if (subjectOf == null) {
	        subjectOf = new ArrayList<net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVSubject>();
	    }
	    return subjectOf;
	}
	
	/**
	 * Set a value to attribute subjectOf.
	 * @param subjectOf.
	 */
	public void setSubjectOf(List<net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVSubject> subjectOf) {
	    this.subjectOf = subjectOf;
	}
	
	
	
	/**
	 * Add a subjectOf to the subjectOf collection.
	 * @param subjectOf_elt Element to add.
	 */
	public void addSubjectOf(net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVSubject subjectOf_elt) {
	    this.subjectOf.add(subjectOf_elt);
	}
	
	/**
	 * Remove a subjectOf to the subjectOf collection.
	 * @param subjectOf_elt Element to remove
	 */
	public void removeSubjectOf(net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVSubject subjectOf_elt) {
	    this.subjectOf.remove(subjectOf_elt);
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
	 * @param classCode.
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
	 * @param moodCode.
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.coctmt260003UV");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT260003UV.DetectedMedicationIssue").item(0);
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
   public static void validateByModule(COCTMT260003UVDetectedMedicationIssue cOCTMT260003UVDetectedMedicationIssue, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT260003UVDetectedMedicationIssue != null){
   			cvm.validate(cOCTMT260003UVDetectedMedicationIssue, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT260003UVDetectedMedicationIssue.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT260003UVDetectedMedicationIssue.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT260003UVDetectedMedicationIssue.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(cOCTMT260003UVDetectedMedicationIssue.getCode(), _location + "/code", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT260003UVDetectedMedicationIssue.getValue(), _location + "/value", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVDefinition definition: cOCTMT260003UVDetectedMedicationIssue.getDefinition()){
					net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVDefinition.validateByModule(definition, _location + "/definition[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVSubject2 subject: cOCTMT260003UVDetectedMedicationIssue.getSubject()){
					net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVSubject2.validateByModule(subject, _location + "/subject[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVMitigates mitigatedBy: cOCTMT260003UVDetectedMedicationIssue.getMitigatedBy()){
					net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVMitigates.validateByModule(mitigatedBy, _location + "/mitigatedBy[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVSubject subjectOf: cOCTMT260003UVDetectedMedicationIssue.getSubjectOf()){
					net.ihe.gazelle.hl7v3.coctmt260003UV.COCTMT260003UVSubject.validateByModule(subjectOf, _location + "/subjectOf[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
    	}
    }

}