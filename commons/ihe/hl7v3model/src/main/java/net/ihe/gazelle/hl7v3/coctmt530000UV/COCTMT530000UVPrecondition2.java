/**
 * COCTMT530000UVPrecondition2.java
 * <p>
 * File generated from the coctmt530000UV::COCTMT530000UVPrecondition2 uml Class
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
 * Description of the class COCTMT530000UVPrecondition2.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT530000UV.Precondition2", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"conjunctionCode",
	"seperatableInd",
	"criterion",
	"contextConductionInd",
	"contextControlCode",
	"nullFlavor",
	"typeCode"
})
@XmlRootElement(name = "COCT_MT530000UV.Precondition2")
public class COCTMT530000UVPrecondition2 implements java.io.Serializable {
	
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
	@XmlElement(name = "conjunctionCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CS conjunctionCode;
	@XmlElement(name = "seperatableInd", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.BL seperatableInd;
	@XmlElement(name = "criterion", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVCriterion criterion;
	@XmlAttribute(name = "contextConductionInd")
	public Boolean contextConductionInd;
	@XmlAttribute(name = "contextControlCode")
	public net.ihe.gazelle.hl7v3.voc.ContextControl contextControlCode;
	@XmlAttribute(name = "nullFlavor")
	public net.ihe.gazelle.hl7v3.voc.NullFlavor nullFlavor;
	@XmlAttribute(name = "typeCode", required = true)
	public java.lang.String typeCode;
	
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
	 * Return conjunctionCode.
	 * @return conjunctionCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CS getConjunctionCode() {
	    return conjunctionCode;
	}
	
	/**
	 * Set a value to attribute conjunctionCode.
     */
	public void setConjunctionCode(net.ihe.gazelle.hl7v3.datatypes.CS conjunctionCode) {
	    this.conjunctionCode = conjunctionCode;
	}
	
	
	
	
	/**
	 * Return seperatableInd.
	 * @return seperatableInd
	 */
	public net.ihe.gazelle.hl7v3.datatypes.BL getSeperatableInd() {
	    return seperatableInd;
	}
	
	/**
	 * Set a value to attribute seperatableInd.
     */
	public void setSeperatableInd(net.ihe.gazelle.hl7v3.datatypes.BL seperatableInd) {
	    this.seperatableInd = seperatableInd;
	}
	
	
	
	
	/**
	 * Return criterion.
	 * @return criterion
	 */
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVCriterion getCriterion() {
	    return criterion;
	}
	
	/**
	 * Set a value to attribute criterion.
     */
	public void setCriterion(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVCriterion criterion) {
	    this.criterion = criterion;
	}
	
	
	
	
	/**
	 * Return contextConductionInd.
	 * @return contextConductionInd
	 */
	public Boolean getContextConductionInd() {
	    return contextConductionInd;
	}
	
	/**
	 * Set a value to attribute contextConductionInd.
     */
	public void setContextConductionInd(Boolean contextConductionInd) {
	    this.contextConductionInd = contextConductionInd;
	}
	
	
	/**
	 * Return contextConductionInd.
	 * @return contextConductionInd
	 * Generated for the use on jsf pages
	 */
	 @Deprecated
	public Boolean isContextConductionInd() {
	    return contextConductionInd;
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
	public java.lang.String getTypeCode() {
	    return typeCode;
	}
	
	/**
	 * Set a value to attribute typeCode.
     */
	public void setTypeCode(java.lang.String typeCode) {
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
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT530000UV.Precondition2").item(0);
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
   public static void validateByModule(COCTMT530000UVPrecondition2 cOCTMT530000UVPrecondition2, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT530000UVPrecondition2 != null){
   			cvm.validate(cOCTMT530000UVPrecondition2, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT530000UVPrecondition2.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT530000UVPrecondition2.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT530000UVPrecondition2.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(cOCTMT530000UVPrecondition2.getConjunctionCode(), _location + "/conjunctionCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.BL.validateByModule(cOCTMT530000UVPrecondition2.getSeperatableInd(), _location + "/seperatableInd", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVCriterion.validateByModule(cOCTMT530000UVPrecondition2.getCriterion(), _location + "/criterion", cvm, diagnostic);
    	}
    }

}