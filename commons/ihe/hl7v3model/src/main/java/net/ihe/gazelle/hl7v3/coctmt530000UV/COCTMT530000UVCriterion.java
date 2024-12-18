/**
 * COCTMT530000UVCriterion.java
 * <p>
 * File generated from the coctmt530000UV::COCTMT530000UVCriterion uml Class
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
 * Description of the class COCTMT530000UVCriterion.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT530000UV.Criterion", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"code",
	"text",
	"value",
	"interpretationCode",
	"precondition",
	"classCode",
	"moodCode",
	"negationInd",
	"nullFlavor"
})
@XmlRootElement(name = "COCT_MT530000UV.Criterion")
public class COCTMT530000UVCriterion implements java.io.Serializable {
	
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
	@XmlElement(name = "code", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CD code;
	@XmlElement(name = "text", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.ED text;
	@XmlElement(name = "value", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.ANY value;
	@XmlElement(name = "interpretationCode", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE interpretationCode;
	@XmlElement(name = "precondition", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVPrecondition2> precondition;
	@XmlAttribute(name = "classCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.ActClassObservation classCode;
	@XmlAttribute(name = "moodCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.ActMood moodCode;
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
	 * Return text.
	 * @return text
	 */
	public net.ihe.gazelle.hl7v3.datatypes.ED getText() {
	    return text;
	}
	
	/**
	 * Set a value to attribute text.
     */
	public void setText(net.ihe.gazelle.hl7v3.datatypes.ED text) {
	    this.text = text;
	}
	
	
	
	
	/**
	 * Return value.
	 * @return value
	 */
	public net.ihe.gazelle.hl7v3.datatypes.ANY getValue() {
	    return value;
	}
	
	/**
	 * Set a value to attribute value.
     */
	public void setValue(net.ihe.gazelle.hl7v3.datatypes.ANY value) {
	    this.value = value;
	}
	
	
	
	
	/**
	 * Return interpretationCode.
	 * @return interpretationCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getInterpretationCode() {
	    return interpretationCode;
	}
	
	/**
	 * Set a value to attribute interpretationCode.
     */
	public void setInterpretationCode(net.ihe.gazelle.hl7v3.datatypes.CE interpretationCode) {
	    this.interpretationCode = interpretationCode;
	}
	
	
	
	
	/**
	 * Return precondition.
	 * @return precondition
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVPrecondition2> getPrecondition() {
		if (precondition == null) {
	        precondition = new ArrayList<>();
	    }
	    return precondition;
	}
	
	/**
	 * Set a value to attribute precondition.
     */
	public void setPrecondition(List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVPrecondition2> precondition) {
	    this.precondition = precondition;
	}
	
	
	
	/**
	 * Add a precondition to the precondition collection.
	 * @param precondition_elt Element to add.
	 */
	public void addPrecondition(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVPrecondition2 precondition_elt) {
	    this.precondition.add(precondition_elt);
	}
	
	/**
	 * Remove a precondition to the precondition collection.
	 * @param precondition_elt Element to remove
	 */
	public void removePrecondition(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVPrecondition2 precondition_elt) {
	    this.precondition.remove(precondition_elt);
	}
	
	/**
	 * Return classCode.
	 * @return classCode
	 */
	public net.ihe.gazelle.hl7v3.voc.ActClassObservation getClassCode() {
	    return classCode;
	}
	
	/**
	 * Set a value to attribute classCode.
     */
	public void setClassCode(net.ihe.gazelle.hl7v3.voc.ActClassObservation classCode) {
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.coctmt530000UV");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT530000UV.Criterion").item(0);
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
   public static void validateByModule(COCTMT530000UVCriterion cOCTMT530000UVCriterion, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT530000UVCriterion != null){
   			cvm.validate(cOCTMT530000UVCriterion, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT530000UVCriterion.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT530000UVCriterion.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT530000UVCriterion.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.CD.validateByModule(cOCTMT530000UVCriterion.getCode(), _location + "/code", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.ED.validateByModule(cOCTMT530000UVCriterion.getText(), _location + "/text", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.ANY.validateByModule(cOCTMT530000UVCriterion.getValue(), _location + "/value", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT530000UVCriterion.getInterpretationCode(), _location + "/interpretationCode", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVPrecondition2 precondition: cOCTMT530000UVCriterion.getPrecondition()){
					net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVPrecondition2.validateByModule(precondition, _location + "/precondition[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
    	}
    }

}