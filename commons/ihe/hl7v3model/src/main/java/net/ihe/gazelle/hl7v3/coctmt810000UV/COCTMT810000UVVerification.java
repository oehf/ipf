/**
 * COCTMT810000UVVerification.java
 *
 * File generated from the coctmt810000UV::COCTMT810000UVVerification uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.coctmt810000UV;

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
 * Description of the class COCTMT810000UVVerification.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT810000UV.Verification", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"id",
	"code",
	"statusCode",
	"effectiveTime",
	"reasonCode",
	"value",
	"methodCode",
	"primaryPerformer",
	"inFulfillmentOf",
	"support",
	"classCode",
	"moodCode",
	"nullFlavor"
})
@XmlRootElement(name = "COCT_MT810000UV.Verification")
public class COCTMT810000UVVerification implements java.io.Serializable {
	
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
	@XmlElement(name = "id", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.II> id;
	@XmlElement(name = "code", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CD code;
	@XmlElement(name = "statusCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CS statusCode;
	@XmlElement(name = "effectiveTime", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.TS effectiveTime;
	@XmlElement(name = "reasonCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE reasonCode;
	@XmlElement(name = "value", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE value;
	@XmlElement(name = "methodCode", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.CE> methodCode;
	@XmlElement(name = "primaryPerformer", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVPrimaryPerformer> primaryPerformer;
	@XmlElement(name = "inFulfillmentOf", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVInFulfillmentOf> inFulfillmentOf;
	@XmlElement(name = "support", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVSupport> support;
	@XmlAttribute(name = "classCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.ActClassObservation classCode;
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
	 * Return id.
	 * @return id
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.II> getId() {
		if (id == null) {
	        id = new ArrayList<net.ihe.gazelle.hl7v3.datatypes.II>();
	    }
	    return id;
	}
	
	/**
	 * Set a value to attribute id.
	 * @param id.
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
	 * @param code.
	 */
	public void setCode(net.ihe.gazelle.hl7v3.datatypes.CD code) {
	    this.code = code;
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
	 * @param statusCode.
	 */
	public void setStatusCode(net.ihe.gazelle.hl7v3.datatypes.CS statusCode) {
	    this.statusCode = statusCode;
	}
	
	
	
	
	/**
	 * Return effectiveTime.
	 * @return effectiveTime
	 */
	public net.ihe.gazelle.hl7v3.datatypes.TS getEffectiveTime() {
	    return effectiveTime;
	}
	
	/**
	 * Set a value to attribute effectiveTime.
	 * @param effectiveTime.
	 */
	public void setEffectiveTime(net.ihe.gazelle.hl7v3.datatypes.TS effectiveTime) {
	    this.effectiveTime = effectiveTime;
	}
	
	
	
	
	/**
	 * Return reasonCode.
	 * @return reasonCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getReasonCode() {
	    return reasonCode;
	}
	
	/**
	 * Set a value to attribute reasonCode.
	 * @param reasonCode.
	 */
	public void setReasonCode(net.ihe.gazelle.hl7v3.datatypes.CE reasonCode) {
	    this.reasonCode = reasonCode;
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
	 * Return methodCode.
	 * @return methodCode
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.CE> getMethodCode() {
		if (methodCode == null) {
	        methodCode = new ArrayList<net.ihe.gazelle.hl7v3.datatypes.CE>();
	    }
	    return methodCode;
	}
	
	/**
	 * Set a value to attribute methodCode.
	 * @param methodCode.
	 */
	public void setMethodCode(List<net.ihe.gazelle.hl7v3.datatypes.CE> methodCode) {
	    this.methodCode = methodCode;
	}
	
	
	
	/**
	 * Add a methodCode to the methodCode collection.
	 * @param methodCode_elt Element to add.
	 */
	public void addMethodCode(net.ihe.gazelle.hl7v3.datatypes.CE methodCode_elt) {
	    this.methodCode.add(methodCode_elt);
	}
	
	/**
	 * Remove a methodCode to the methodCode collection.
	 * @param methodCode_elt Element to remove
	 */
	public void removeMethodCode(net.ihe.gazelle.hl7v3.datatypes.CE methodCode_elt) {
	    this.methodCode.remove(methodCode_elt);
	}
	
	/**
	 * Return primaryPerformer.
	 * @return primaryPerformer
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVPrimaryPerformer> getPrimaryPerformer() {
		if (primaryPerformer == null) {
	        primaryPerformer = new ArrayList<net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVPrimaryPerformer>();
	    }
	    return primaryPerformer;
	}
	
	/**
	 * Set a value to attribute primaryPerformer.
	 * @param primaryPerformer.
	 */
	public void setPrimaryPerformer(List<net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVPrimaryPerformer> primaryPerformer) {
	    this.primaryPerformer = primaryPerformer;
	}
	
	
	
	/**
	 * Add a primaryPerformer to the primaryPerformer collection.
	 * @param primaryPerformer_elt Element to add.
	 */
	public void addPrimaryPerformer(net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVPrimaryPerformer primaryPerformer_elt) {
	    this.primaryPerformer.add(primaryPerformer_elt);
	}
	
	/**
	 * Remove a primaryPerformer to the primaryPerformer collection.
	 * @param primaryPerformer_elt Element to remove
	 */
	public void removePrimaryPerformer(net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVPrimaryPerformer primaryPerformer_elt) {
	    this.primaryPerformer.remove(primaryPerformer_elt);
	}
	
	/**
	 * Return inFulfillmentOf.
	 * @return inFulfillmentOf
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVInFulfillmentOf> getInFulfillmentOf() {
		if (inFulfillmentOf == null) {
	        inFulfillmentOf = new ArrayList<net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVInFulfillmentOf>();
	    }
	    return inFulfillmentOf;
	}
	
	/**
	 * Set a value to attribute inFulfillmentOf.
	 * @param inFulfillmentOf.
	 */
	public void setInFulfillmentOf(List<net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVInFulfillmentOf> inFulfillmentOf) {
	    this.inFulfillmentOf = inFulfillmentOf;
	}
	
	
	
	/**
	 * Add a inFulfillmentOf to the inFulfillmentOf collection.
	 * @param inFulfillmentOf_elt Element to add.
	 */
	public void addInFulfillmentOf(net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVInFulfillmentOf inFulfillmentOf_elt) {
	    this.inFulfillmentOf.add(inFulfillmentOf_elt);
	}
	
	/**
	 * Remove a inFulfillmentOf to the inFulfillmentOf collection.
	 * @param inFulfillmentOf_elt Element to remove
	 */
	public void removeInFulfillmentOf(net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVInFulfillmentOf inFulfillmentOf_elt) {
	    this.inFulfillmentOf.remove(inFulfillmentOf_elt);
	}
	
	/**
	 * Return support.
	 * @return support
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVSupport> getSupport() {
		if (support == null) {
	        support = new ArrayList<net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVSupport>();
	    }
	    return support;
	}
	
	/**
	 * Set a value to attribute support.
	 * @param support.
	 */
	public void setSupport(List<net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVSupport> support) {
	    this.support = support;
	}
	
	
	
	/**
	 * Add a support to the support collection.
	 * @param support_elt Element to add.
	 */
	public void addSupport(net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVSupport support_elt) {
	    this.support.add(support_elt);
	}
	
	/**
	 * Remove a support to the support collection.
	 * @param support_elt Element to remove
	 */
	public void removeSupport(net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVSupport support_elt) {
	    this.support.remove(support_elt);
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
	 * @param classCode.
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.coctmt810000UV");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT810000UV.Verification").item(0);
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
   public static void validateByModule(COCTMT810000UVVerification cOCTMT810000UVVerification, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT810000UVVerification != null){
   			cvm.validate(cOCTMT810000UVVerification, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT810000UVVerification.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT810000UVVerification.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT810000UVVerification.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II id: cOCTMT810000UVVerification.getId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(id, _location + "/id[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.CD.validateByModule(cOCTMT810000UVVerification.getCode(), _location + "/code", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(cOCTMT810000UVVerification.getStatusCode(), _location + "/statusCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.TS.validateByModule(cOCTMT810000UVVerification.getEffectiveTime(), _location + "/effectiveTime", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT810000UVVerification.getReasonCode(), _location + "/reasonCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT810000UVVerification.getValue(), _location + "/value", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CE methodCode: cOCTMT810000UVVerification.getMethodCode()){
					net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(methodCode, _location + "/methodCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVPrimaryPerformer primaryPerformer: cOCTMT810000UVVerification.getPrimaryPerformer()){
					net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVPrimaryPerformer.validateByModule(primaryPerformer, _location + "/primaryPerformer[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVInFulfillmentOf inFulfillmentOf: cOCTMT810000UVVerification.getInFulfillmentOf()){
					net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVInFulfillmentOf.validateByModule(inFulfillmentOf, _location + "/inFulfillmentOf[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVSupport support: cOCTMT810000UVVerification.getSupport()){
					net.ihe.gazelle.hl7v3.coctmt810000UV.COCTMT810000UVSupport.validateByModule(support, _location + "/support[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
    	}
    }

}