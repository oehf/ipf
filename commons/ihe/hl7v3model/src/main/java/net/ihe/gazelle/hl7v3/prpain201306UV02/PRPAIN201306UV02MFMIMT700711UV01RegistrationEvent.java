/**
 * PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent.java
 * <p>
 * File generated from the prpain201306UV02::PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.prpain201306UV02;

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
 * Description of the class PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PRPA_IN201306UV02.MFMI_MT700711UV01.RegistrationEvent", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"id",
	"statusCode",
	"effectiveTime",
	"subject1",
	"author",
	"custodian",
	"inFulfillmentOf",
	"definition",
	"replacementOf",
	"classCode",
	"moodCode",
	"nullFlavor"
})
@XmlRootElement(name = "PRPA_IN201306UV02.MFMI_MT700711UV01.RegistrationEvent")
public class PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent implements java.io.Serializable {
	
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
	@XmlElement(name = "statusCode", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CS statusCode;
	@XmlElement(name = "effectiveTime", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS effectiveTime;
	@XmlElement(name = "subject1", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02MFMIMT700711UV01Subject2 subject1;
	@XmlElement(name = "author", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Author2 author;
	@XmlElement(name = "custodian", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Custodian custodian;
	@XmlElement(name = "inFulfillmentOf", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01InFulfillmentOf> inFulfillmentOf;
	@XmlElement(name = "definition", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Definition> definition;
	@XmlElement(name = "replacementOf", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01ReplacementOf> replacementOf;
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
	 * Return subject1.
	 * @return subject1
	 */
	public net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02MFMIMT700711UV01Subject2 getSubject1() {
	    return subject1;
	}
	
	/**
	 * Set a value to attribute subject1.
     */
	public void setSubject1(net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02MFMIMT700711UV01Subject2 subject1) {
	    this.subject1 = subject1;
	}
	
	
	
	
	/**
	 * Return author.
	 * @return author
	 */
	public net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Author2 getAuthor() {
	    return author;
	}
	
	/**
	 * Set a value to attribute author.
     */
	public void setAuthor(net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Author2 author) {
	    this.author = author;
	}
	
	
	
	
	/**
	 * Return custodian.
	 * @return custodian
	 */
	public net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Custodian getCustodian() {
	    return custodian;
	}
	
	/**
	 * Set a value to attribute custodian.
     */
	public void setCustodian(net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Custodian custodian) {
	    this.custodian = custodian;
	}
	
	
	
	
	/**
	 * Return inFulfillmentOf.
	 * @return inFulfillmentOf
	 */
	public List<net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01InFulfillmentOf> getInFulfillmentOf() {
		if (inFulfillmentOf == null) {
	        inFulfillmentOf = new ArrayList<>();
	    }
	    return inFulfillmentOf;
	}
	
	/**
	 * Set a value to attribute inFulfillmentOf.
     */
	public void setInFulfillmentOf(List<net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01InFulfillmentOf> inFulfillmentOf) {
	    this.inFulfillmentOf = inFulfillmentOf;
	}
	
	
	
	/**
	 * Add a inFulfillmentOf to the inFulfillmentOf collection.
	 * @param inFulfillmentOf_elt Element to add.
	 */
	public void addInFulfillmentOf(net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01InFulfillmentOf inFulfillmentOf_elt) {
	    this.inFulfillmentOf.add(inFulfillmentOf_elt);
	}
	
	/**
	 * Remove a inFulfillmentOf to the inFulfillmentOf collection.
	 * @param inFulfillmentOf_elt Element to remove
	 */
	public void removeInFulfillmentOf(net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01InFulfillmentOf inFulfillmentOf_elt) {
	    this.inFulfillmentOf.remove(inFulfillmentOf_elt);
	}
	
	/**
	 * Return definition.
	 * @return definition
	 */
	public List<net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Definition> getDefinition() {
		if (definition == null) {
	        definition = new ArrayList<>();
	    }
	    return definition;
	}
	
	/**
	 * Set a value to attribute definition.
     */
	public void setDefinition(List<net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Definition> definition) {
	    this.definition = definition;
	}
	
	
	
	/**
	 * Add a definition to the definition collection.
	 * @param definition_elt Element to add.
	 */
	public void addDefinition(net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Definition definition_elt) {
	    this.definition.add(definition_elt);
	}
	
	/**
	 * Remove a definition to the definition collection.
	 * @param definition_elt Element to remove
	 */
	public void removeDefinition(net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Definition definition_elt) {
	    this.definition.remove(definition_elt);
	}
	
	/**
	 * Return replacementOf.
	 * @return replacementOf
	 */
	public List<net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01ReplacementOf> getReplacementOf() {
		if (replacementOf == null) {
	        replacementOf = new ArrayList<>();
	    }
	    return replacementOf;
	}
	
	/**
	 * Set a value to attribute replacementOf.
     */
	public void setReplacementOf(List<net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01ReplacementOf> replacementOf) {
	    this.replacementOf = replacementOf;
	}
	
	
	
	/**
	 * Add a replacementOf to the replacementOf collection.
	 * @param replacementOf_elt Element to add.
	 */
	public void addReplacementOf(net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01ReplacementOf replacementOf_elt) {
	    this.replacementOf.add(replacementOf_elt);
	}
	
	/**
	 * Remove a replacementOf to the replacementOf collection.
	 * @param replacementOf_elt Element to remove
	 */
	public void removeReplacementOf(net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01ReplacementOf replacementOf_elt) {
	    this.replacementOf.remove(replacementOf_elt);
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.prpain201306UV02");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "PRPA_IN201306UV02.MFMI_MT700711UV01.RegistrationEvent").item(0);
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
   public static void validateByModule(PRPAIN201306UV02MFMIMT700711UV01RegistrationEvent pRPAIN201306UV02MFMIMT700711UV01RegistrationEvent, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (pRPAIN201306UV02MFMIMT700711UV01RegistrationEvent != null){
   			cvm.validate(pRPAIN201306UV02MFMIMT700711UV01RegistrationEvent, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: pRPAIN201306UV02MFMIMT700711UV01RegistrationEvent.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(pRPAIN201306UV02MFMIMT700711UV01RegistrationEvent.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: pRPAIN201306UV02MFMIMT700711UV01RegistrationEvent.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II id: pRPAIN201306UV02MFMIMT700711UV01RegistrationEvent.getId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(id, _location + "/id[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(pRPAIN201306UV02MFMIMT700711UV01RegistrationEvent.getStatusCode(), _location + "/statusCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.IVLTS.validateByModule(pRPAIN201306UV02MFMIMT700711UV01RegistrationEvent.getEffectiveTime(), _location + "/effectiveTime", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.prpain201306UV02.PRPAIN201306UV02MFMIMT700711UV01Subject2.validateByModule(pRPAIN201306UV02MFMIMT700711UV01RegistrationEvent.getSubject1(), _location + "/subject1", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Author2.validateByModule(pRPAIN201306UV02MFMIMT700711UV01RegistrationEvent.getAuthor(), _location + "/author", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Custodian.validateByModule(pRPAIN201306UV02MFMIMT700711UV01RegistrationEvent.getCustodian(), _location + "/custodian", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01InFulfillmentOf inFulfillmentOf: pRPAIN201306UV02MFMIMT700711UV01RegistrationEvent.getInFulfillmentOf()){
					net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01InFulfillmentOf.validateByModule(inFulfillmentOf, _location + "/inFulfillmentOf[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Definition definition: pRPAIN201306UV02MFMIMT700711UV01RegistrationEvent.getDefinition()){
					net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01Definition.validateByModule(definition, _location + "/definition[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01ReplacementOf replacementOf: pRPAIN201306UV02MFMIMT700711UV01RegistrationEvent.getReplacementOf()){
					net.ihe.gazelle.hl7v3.mfmimt700711UV01.MFMIMT700711UV01ReplacementOf.validateByModule(replacementOf, _location + "/replacementOf[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
    	}
    }

}