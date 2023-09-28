/**
 * COCTMT530000UVAct.java
 *
 * File generated from the coctmt530000UV::COCTMT530000UVAct uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.coctmt530000UV;

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
 * Description of the class COCTMT530000UVAct.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT530000UV.Act", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"id",
	"code",
	"text",
	"statusCode",
	"effectiveTime",
	"availabilityTime",
	"priorityCode",
	"confidentialityCode",
	"uncertaintyCode",
	"languageCode",
	"subject",
	"recordTarget",
	"responsibleParty",
	"performer",
	"author",
	"dataEnterer",
	"informant",
	"verifier",
	"location",
	"definition",
	"conditions",
	"sourceOf1",
	"sourceOf2",
	"subjectOf",
	"targetOf",
	"classCode",
	"moodCode",
	"negationInd",
	"nullFlavor"
})
@XmlRootElement(name = "COCT_MT530000UV.Act")
public class COCTMT530000UVAct implements java.io.Serializable {
	
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
	@XmlElement(name = "code", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CD code;
	@XmlElement(name = "text", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.ED text;
	@XmlElement(name = "statusCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CS statusCode;
	@XmlElement(name = "effectiveTime", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS effectiveTime;
	@XmlElement(name = "availabilityTime", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.TS availabilityTime;
	@XmlElement(name = "priorityCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE priorityCode;
	@XmlElement(name = "confidentialityCode", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.CE> confidentialityCode;
	@XmlElement(name = "uncertaintyCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE uncertaintyCode;
	@XmlElement(name = "languageCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE languageCode;
	@XmlElement(name = "subject", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSubject2> subject;
	@XmlElement(name = "recordTarget", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVRecordTarget recordTarget;
	@XmlElement(name = "responsibleParty", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVResponsibleParty2> responsibleParty;
	@XmlElement(name = "performer", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVPerformer> performer;
	@XmlElement(name = "author", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVAuthor> author;
	@XmlElement(name = "dataEnterer", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVDataEnterer dataEnterer;
	@XmlElement(name = "informant", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVInformant> informant;
	@XmlElement(name = "verifier", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVVerifier> verifier;
	@XmlElement(name = "location", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVLocation> location;
	@XmlElement(name = "definition", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVDefinition definition;
	@XmlElement(name = "conditions", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVConditions> conditions;
	@XmlElement(name = "sourceOf1", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf1> sourceOf1;
	@XmlElement(name = "sourceOf2", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf3> sourceOf2;
	@XmlElement(name = "subjectOf", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSubject1 subjectOf;
	@XmlElement(name = "targetOf", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf2> targetOf;
	@XmlAttribute(name = "classCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.ActClassRoot classCode;
	@XmlAttribute(name = "moodCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.XClinicalStatementActMood moodCode;
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
	 * Return text.
	 * @return text
	 */
	public net.ihe.gazelle.hl7v3.datatypes.ED getText() {
	    return text;
	}
	
	/**
	 * Set a value to attribute text.
	 * @param text.
	 */
	public void setText(net.ihe.gazelle.hl7v3.datatypes.ED text) {
	    this.text = text;
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
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS getEffectiveTime() {
	    return effectiveTime;
	}
	
	/**
	 * Set a value to attribute effectiveTime.
	 * @param effectiveTime.
	 */
	public void setEffectiveTime(net.ihe.gazelle.hl7v3.datatypes.IVLTS effectiveTime) {
	    this.effectiveTime = effectiveTime;
	}
	
	
	
	
	/**
	 * Return availabilityTime.
	 * @return availabilityTime
	 */
	public net.ihe.gazelle.hl7v3.datatypes.TS getAvailabilityTime() {
	    return availabilityTime;
	}
	
	/**
	 * Set a value to attribute availabilityTime.
	 * @param availabilityTime.
	 */
	public void setAvailabilityTime(net.ihe.gazelle.hl7v3.datatypes.TS availabilityTime) {
	    this.availabilityTime = availabilityTime;
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
	 * @param priorityCode.
	 */
	public void setPriorityCode(net.ihe.gazelle.hl7v3.datatypes.CE priorityCode) {
	    this.priorityCode = priorityCode;
	}
	
	
	
	
	/**
	 * Return confidentialityCode.
	 * @return confidentialityCode
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.CE> getConfidentialityCode() {
		if (confidentialityCode == null) {
	        confidentialityCode = new ArrayList<net.ihe.gazelle.hl7v3.datatypes.CE>();
	    }
	    return confidentialityCode;
	}
	
	/**
	 * Set a value to attribute confidentialityCode.
	 * @param confidentialityCode.
	 */
	public void setConfidentialityCode(List<net.ihe.gazelle.hl7v3.datatypes.CE> confidentialityCode) {
	    this.confidentialityCode = confidentialityCode;
	}
	
	
	
	/**
	 * Add a confidentialityCode to the confidentialityCode collection.
	 * @param confidentialityCode_elt Element to add.
	 */
	public void addConfidentialityCode(net.ihe.gazelle.hl7v3.datatypes.CE confidentialityCode_elt) {
	    this.confidentialityCode.add(confidentialityCode_elt);
	}
	
	/**
	 * Remove a confidentialityCode to the confidentialityCode collection.
	 * @param confidentialityCode_elt Element to remove
	 */
	public void removeConfidentialityCode(net.ihe.gazelle.hl7v3.datatypes.CE confidentialityCode_elt) {
	    this.confidentialityCode.remove(confidentialityCode_elt);
	}
	
	/**
	 * Return uncertaintyCode.
	 * @return uncertaintyCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getUncertaintyCode() {
	    return uncertaintyCode;
	}
	
	/**
	 * Set a value to attribute uncertaintyCode.
	 * @param uncertaintyCode.
	 */
	public void setUncertaintyCode(net.ihe.gazelle.hl7v3.datatypes.CE uncertaintyCode) {
	    this.uncertaintyCode = uncertaintyCode;
	}
	
	
	
	
	/**
	 * Return languageCode.
	 * @return languageCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getLanguageCode() {
	    return languageCode;
	}
	
	/**
	 * Set a value to attribute languageCode.
	 * @param languageCode.
	 */
	public void setLanguageCode(net.ihe.gazelle.hl7v3.datatypes.CE languageCode) {
	    this.languageCode = languageCode;
	}
	
	
	
	
	/**
	 * Return subject.
	 * @return subject
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSubject2> getSubject() {
		if (subject == null) {
	        subject = new ArrayList<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSubject2>();
	    }
	    return subject;
	}
	
	/**
	 * Set a value to attribute subject.
	 * @param subject.
	 */
	public void setSubject(List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSubject2> subject) {
	    this.subject = subject;
	}
	
	
	
	/**
	 * Add a subject to the subject collection.
	 * @param subject_elt Element to add.
	 */
	public void addSubject(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSubject2 subject_elt) {
	    this.subject.add(subject_elt);
	}
	
	/**
	 * Remove a subject to the subject collection.
	 * @param subject_elt Element to remove
	 */
	public void removeSubject(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSubject2 subject_elt) {
	    this.subject.remove(subject_elt);
	}
	
	/**
	 * Return recordTarget.
	 * @return recordTarget
	 */
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVRecordTarget getRecordTarget() {
	    return recordTarget;
	}
	
	/**
	 * Set a value to attribute recordTarget.
	 * @param recordTarget.
	 */
	public void setRecordTarget(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVRecordTarget recordTarget) {
	    this.recordTarget = recordTarget;
	}
	
	
	
	
	/**
	 * Return responsibleParty.
	 * @return responsibleParty
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVResponsibleParty2> getResponsibleParty() {
		if (responsibleParty == null) {
	        responsibleParty = new ArrayList<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVResponsibleParty2>();
	    }
	    return responsibleParty;
	}
	
	/**
	 * Set a value to attribute responsibleParty.
	 * @param responsibleParty.
	 */
	public void setResponsibleParty(List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVResponsibleParty2> responsibleParty) {
	    this.responsibleParty = responsibleParty;
	}
	
	
	
	/**
	 * Add a responsibleParty to the responsibleParty collection.
	 * @param responsibleParty_elt Element to add.
	 */
	public void addResponsibleParty(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVResponsibleParty2 responsibleParty_elt) {
	    this.responsibleParty.add(responsibleParty_elt);
	}
	
	/**
	 * Remove a responsibleParty to the responsibleParty collection.
	 * @param responsibleParty_elt Element to remove
	 */
	public void removeResponsibleParty(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVResponsibleParty2 responsibleParty_elt) {
	    this.responsibleParty.remove(responsibleParty_elt);
	}
	
	/**
	 * Return performer.
	 * @return performer
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVPerformer> getPerformer() {
		if (performer == null) {
	        performer = new ArrayList<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVPerformer>();
	    }
	    return performer;
	}
	
	/**
	 * Set a value to attribute performer.
	 * @param performer.
	 */
	public void setPerformer(List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVPerformer> performer) {
	    this.performer = performer;
	}
	
	
	
	/**
	 * Add a performer to the performer collection.
	 * @param performer_elt Element to add.
	 */
	public void addPerformer(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVPerformer performer_elt) {
	    this.performer.add(performer_elt);
	}
	
	/**
	 * Remove a performer to the performer collection.
	 * @param performer_elt Element to remove
	 */
	public void removePerformer(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVPerformer performer_elt) {
	    this.performer.remove(performer_elt);
	}
	
	/**
	 * Return author.
	 * @return author
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVAuthor> getAuthor() {
		if (author == null) {
	        author = new ArrayList<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVAuthor>();
	    }
	    return author;
	}
	
	/**
	 * Set a value to attribute author.
	 * @param author.
	 */
	public void setAuthor(List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVAuthor> author) {
	    this.author = author;
	}
	
	
	
	/**
	 * Add a author to the author collection.
	 * @param author_elt Element to add.
	 */
	public void addAuthor(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVAuthor author_elt) {
	    this.author.add(author_elt);
	}
	
	/**
	 * Remove a author to the author collection.
	 * @param author_elt Element to remove
	 */
	public void removeAuthor(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVAuthor author_elt) {
	    this.author.remove(author_elt);
	}
	
	/**
	 * Return dataEnterer.
	 * @return dataEnterer
	 */
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVDataEnterer getDataEnterer() {
	    return dataEnterer;
	}
	
	/**
	 * Set a value to attribute dataEnterer.
	 * @param dataEnterer.
	 */
	public void setDataEnterer(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVDataEnterer dataEnterer) {
	    this.dataEnterer = dataEnterer;
	}
	
	
	
	
	/**
	 * Return informant.
	 * @return informant
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVInformant> getInformant() {
		if (informant == null) {
	        informant = new ArrayList<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVInformant>();
	    }
	    return informant;
	}
	
	/**
	 * Set a value to attribute informant.
	 * @param informant.
	 */
	public void setInformant(List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVInformant> informant) {
	    this.informant = informant;
	}
	
	
	
	/**
	 * Add a informant to the informant collection.
	 * @param informant_elt Element to add.
	 */
	public void addInformant(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVInformant informant_elt) {
	    this.informant.add(informant_elt);
	}
	
	/**
	 * Remove a informant to the informant collection.
	 * @param informant_elt Element to remove
	 */
	public void removeInformant(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVInformant informant_elt) {
	    this.informant.remove(informant_elt);
	}
	
	/**
	 * Return verifier.
	 * @return verifier
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVVerifier> getVerifier() {
		if (verifier == null) {
	        verifier = new ArrayList<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVVerifier>();
	    }
	    return verifier;
	}
	
	/**
	 * Set a value to attribute verifier.
	 * @param verifier.
	 */
	public void setVerifier(List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVVerifier> verifier) {
	    this.verifier = verifier;
	}
	
	
	
	/**
	 * Add a verifier to the verifier collection.
	 * @param verifier_elt Element to add.
	 */
	public void addVerifier(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVVerifier verifier_elt) {
	    this.verifier.add(verifier_elt);
	}
	
	/**
	 * Remove a verifier to the verifier collection.
	 * @param verifier_elt Element to remove
	 */
	public void removeVerifier(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVVerifier verifier_elt) {
	    this.verifier.remove(verifier_elt);
	}
	
	/**
	 * Return location.
	 * @return location
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVLocation> getLocation() {
		if (location == null) {
	        location = new ArrayList<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVLocation>();
	    }
	    return location;
	}
	
	/**
	 * Set a value to attribute location.
	 * @param location.
	 */
	public void setLocation(List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVLocation> location) {
	    this.location = location;
	}
	
	
	
	/**
	 * Add a location to the location collection.
	 * @param location_elt Element to add.
	 */
	public void addLocation(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVLocation location_elt) {
	    this.location.add(location_elt);
	}
	
	/**
	 * Remove a location to the location collection.
	 * @param location_elt Element to remove
	 */
	public void removeLocation(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVLocation location_elt) {
	    this.location.remove(location_elt);
	}
	
	/**
	 * Return definition.
	 * @return definition
	 */
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVDefinition getDefinition() {
	    return definition;
	}
	
	/**
	 * Set a value to attribute definition.
	 * @param definition.
	 */
	public void setDefinition(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVDefinition definition) {
	    this.definition = definition;
	}
	
	
	
	
	/**
	 * Return conditions.
	 * @return conditions
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVConditions> getConditions() {
		if (conditions == null) {
	        conditions = new ArrayList<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVConditions>();
	    }
	    return conditions;
	}
	
	/**
	 * Set a value to attribute conditions.
	 * @param conditions.
	 */
	public void setConditions(List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVConditions> conditions) {
	    this.conditions = conditions;
	}
	
	
	
	/**
	 * Add a conditions to the conditions collection.
	 * @param conditions_elt Element to add.
	 */
	public void addConditions(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVConditions conditions_elt) {
	    this.conditions.add(conditions_elt);
	}
	
	/**
	 * Remove a conditions to the conditions collection.
	 * @param conditions_elt Element to remove
	 */
	public void removeConditions(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVConditions conditions_elt) {
	    this.conditions.remove(conditions_elt);
	}
	
	/**
	 * Return sourceOf1.
	 * @return sourceOf1
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf1> getSourceOf1() {
		if (sourceOf1 == null) {
	        sourceOf1 = new ArrayList<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf1>();
	    }
	    return sourceOf1;
	}
	
	/**
	 * Set a value to attribute sourceOf1.
	 * @param sourceOf1.
	 */
	public void setSourceOf1(List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf1> sourceOf1) {
	    this.sourceOf1 = sourceOf1;
	}
	
	
	
	/**
	 * Add a sourceOf1 to the sourceOf1 collection.
	 * @param sourceOf1_elt Element to add.
	 */
	public void addSourceOf1(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf1 sourceOf1_elt) {
	    this.sourceOf1.add(sourceOf1_elt);
	}
	
	/**
	 * Remove a sourceOf1 to the sourceOf1 collection.
	 * @param sourceOf1_elt Element to remove
	 */
	public void removeSourceOf1(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf1 sourceOf1_elt) {
	    this.sourceOf1.remove(sourceOf1_elt);
	}
	
	/**
	 * Return sourceOf2.
	 * @return sourceOf2
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf3> getSourceOf2() {
		if (sourceOf2 == null) {
	        sourceOf2 = new ArrayList<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf3>();
	    }
	    return sourceOf2;
	}
	
	/**
	 * Set a value to attribute sourceOf2.
	 * @param sourceOf2.
	 */
	public void setSourceOf2(List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf3> sourceOf2) {
	    this.sourceOf2 = sourceOf2;
	}
	
	
	
	/**
	 * Add a sourceOf2 to the sourceOf2 collection.
	 * @param sourceOf2_elt Element to add.
	 */
	public void addSourceOf2(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf3 sourceOf2_elt) {
	    this.sourceOf2.add(sourceOf2_elt);
	}
	
	/**
	 * Remove a sourceOf2 to the sourceOf2 collection.
	 * @param sourceOf2_elt Element to remove
	 */
	public void removeSourceOf2(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf3 sourceOf2_elt) {
	    this.sourceOf2.remove(sourceOf2_elt);
	}
	
	/**
	 * Return subjectOf.
	 * @return subjectOf
	 */
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSubject1 getSubjectOf() {
	    return subjectOf;
	}
	
	/**
	 * Set a value to attribute subjectOf.
	 * @param subjectOf.
	 */
	public void setSubjectOf(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSubject1 subjectOf) {
	    this.subjectOf = subjectOf;
	}
	
	
	
	
	/**
	 * Return targetOf.
	 * @return targetOf
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf2> getTargetOf() {
		if (targetOf == null) {
	        targetOf = new ArrayList<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf2>();
	    }
	    return targetOf;
	}
	
	/**
	 * Set a value to attribute targetOf.
	 * @param targetOf.
	 */
	public void setTargetOf(List<net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf2> targetOf) {
	    this.targetOf = targetOf;
	}
	
	
	
	/**
	 * Add a targetOf to the targetOf collection.
	 * @param targetOf_elt Element to add.
	 */
	public void addTargetOf(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf2 targetOf_elt) {
	    this.targetOf.add(targetOf_elt);
	}
	
	/**
	 * Remove a targetOf to the targetOf collection.
	 * @param targetOf_elt Element to remove
	 */
	public void removeTargetOf(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf2 targetOf_elt) {
	    this.targetOf.remove(targetOf_elt);
	}
	
	/**
	 * Return classCode.
	 * @return classCode
	 */
	public net.ihe.gazelle.hl7v3.voc.ActClassRoot getClassCode() {
	    return classCode;
	}
	
	/**
	 * Set a value to attribute classCode.
	 * @param classCode.
	 */
	public void setClassCode(net.ihe.gazelle.hl7v3.voc.ActClassRoot classCode) {
	    this.classCode = classCode;
	}
	
	
	
	
	/**
	 * Return moodCode.
	 * @return moodCode
	 */
	public net.ihe.gazelle.hl7v3.voc.XClinicalStatementActMood getMoodCode() {
	    return moodCode;
	}
	
	/**
	 * Set a value to attribute moodCode.
	 * @param moodCode.
	 */
	public void setMoodCode(net.ihe.gazelle.hl7v3.voc.XClinicalStatementActMood moodCode) {
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
	 * @param negationInd.
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.coctmt530000UV");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT530000UV.Act").item(0);
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
   public static void validateByModule(COCTMT530000UVAct cOCTMT530000UVAct, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT530000UVAct != null){
   			cvm.validate(cOCTMT530000UVAct, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT530000UVAct.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT530000UVAct.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT530000UVAct.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II id: cOCTMT530000UVAct.getId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(id, _location + "/id[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.CD.validateByModule(cOCTMT530000UVAct.getCode(), _location + "/code", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.ED.validateByModule(cOCTMT530000UVAct.getText(), _location + "/text", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(cOCTMT530000UVAct.getStatusCode(), _location + "/statusCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.IVLTS.validateByModule(cOCTMT530000UVAct.getEffectiveTime(), _location + "/effectiveTime", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.TS.validateByModule(cOCTMT530000UVAct.getAvailabilityTime(), _location + "/availabilityTime", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT530000UVAct.getPriorityCode(), _location + "/priorityCode", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CE confidentialityCode: cOCTMT530000UVAct.getConfidentialityCode()){
					net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(confidentialityCode, _location + "/confidentialityCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT530000UVAct.getUncertaintyCode(), _location + "/uncertaintyCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT530000UVAct.getLanguageCode(), _location + "/languageCode", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSubject2 subject: cOCTMT530000UVAct.getSubject()){
					net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSubject2.validateByModule(subject, _location + "/subject[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVRecordTarget.validateByModule(cOCTMT530000UVAct.getRecordTarget(), _location + "/recordTarget", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVResponsibleParty2 responsibleParty: cOCTMT530000UVAct.getResponsibleParty()){
					net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVResponsibleParty2.validateByModule(responsibleParty, _location + "/responsibleParty[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVPerformer performer: cOCTMT530000UVAct.getPerformer()){
					net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVPerformer.validateByModule(performer, _location + "/performer[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVAuthor author: cOCTMT530000UVAct.getAuthor()){
					net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVAuthor.validateByModule(author, _location + "/author[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVDataEnterer.validateByModule(cOCTMT530000UVAct.getDataEnterer(), _location + "/dataEnterer", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVInformant informant: cOCTMT530000UVAct.getInformant()){
					net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVInformant.validateByModule(informant, _location + "/informant[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVVerifier verifier: cOCTMT530000UVAct.getVerifier()){
					net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVVerifier.validateByModule(verifier, _location + "/verifier[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVLocation location: cOCTMT530000UVAct.getLocation()){
					net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVLocation.validateByModule(location, _location + "/location[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVDefinition.validateByModule(cOCTMT530000UVAct.getDefinition(), _location + "/definition", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVConditions conditions: cOCTMT530000UVAct.getConditions()){
					net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVConditions.validateByModule(conditions, _location + "/conditions[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf1 sourceOf1: cOCTMT530000UVAct.getSourceOf1()){
					net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf1.validateByModule(sourceOf1, _location + "/sourceOf1[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf3 sourceOf2: cOCTMT530000UVAct.getSourceOf2()){
					net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf3.validateByModule(sourceOf2, _location + "/sourceOf2[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSubject1.validateByModule(cOCTMT530000UVAct.getSubjectOf(), _location + "/subjectOf", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf2 targetOf: cOCTMT530000UVAct.getTargetOf()){
					net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSourceOf2.validateByModule(targetOf, _location + "/targetOf[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
    	}
    }

}