/**
 * PRPAIN201309UV02MCCIMT000100UV01Message.java
 *
 * File generated from the prpain201309UV02::PRPAIN201309UV02MCCIMT000100UV01Message uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.prpain201309UV02;

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
 * Description of the class PRPAIN201309UV02MCCIMT000100UV01Message.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PRPA_IN201309UV02.MCCI_MT000100UV01.Message", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"id",
	"creationTime",
	"securityText",
	"versionCode",
	"interactionId",
	"profileId",
	"processingCode",
	"processingModeCode",
	"acceptAckCode",
	"sequenceNumber",
	"attachmentText",
	"receiver",
	"respondTo",
	"sender",
	"attentionLine",
	"controlActProcess",
	"nullFlavor"
})
@XmlRootElement(name = "PRPA_IN201309UV02.MCCI_MT000100UV01.Message")
public class PRPAIN201309UV02MCCIMT000100UV01Message implements java.io.Serializable {
	
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
	@XmlElement(name = "id", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.II id;
	@XmlElement(name = "creationTime", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.TS creationTime;
	@XmlElement(name = "securityText", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.ST securityText;
	@XmlElement(name = "versionCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CS versionCode;
	@XmlElement(name = "interactionId", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.II interactionId;
	@XmlElement(name = "profileId", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.II> profileId;
	@XmlElement(name = "processingCode", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CS processingCode;
	@XmlElement(name = "processingModeCode", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CS processingModeCode;
	@XmlElement(name = "acceptAckCode", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CS acceptAckCode;
	@XmlElement(name = "sequenceNumber", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.INT sequenceNumber;
	@XmlElement(name = "attachmentText", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.ED> attachmentText;
	@XmlElement(name = "receiver", required = true, namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Receiver> receiver;
	@XmlElement(name = "respondTo", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01RespondTo> respondTo;
	@XmlElement(name = "sender", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Sender sender;
	@XmlElement(name = "attentionLine", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01AttentionLine> attentionLine;
	@XmlElement(name = "controlActProcess", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.prpain201309UV02.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess;
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
	public net.ihe.gazelle.hl7v3.datatypes.II getId() {
	    return id;
	}
	
	/**
	 * Set a value to attribute id.
	 * @param id.
	 */
	public void setId(net.ihe.gazelle.hl7v3.datatypes.II id) {
	    this.id = id;
	}
	
	
	
	
	/**
	 * Return creationTime.
	 * @return creationTime
	 */
	public net.ihe.gazelle.hl7v3.datatypes.TS getCreationTime() {
	    return creationTime;
	}
	
	/**
	 * Set a value to attribute creationTime.
	 * @param creationTime.
	 */
	public void setCreationTime(net.ihe.gazelle.hl7v3.datatypes.TS creationTime) {
	    this.creationTime = creationTime;
	}
	
	
	
	
	/**
	 * Return securityText.
	 * @return securityText
	 */
	public net.ihe.gazelle.hl7v3.datatypes.ST getSecurityText() {
	    return securityText;
	}
	
	/**
	 * Set a value to attribute securityText.
	 * @param securityText.
	 */
	public void setSecurityText(net.ihe.gazelle.hl7v3.datatypes.ST securityText) {
	    this.securityText = securityText;
	}
	
	
	
	
	/**
	 * Return versionCode.
	 * @return versionCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CS getVersionCode() {
	    return versionCode;
	}
	
	/**
	 * Set a value to attribute versionCode.
	 * @param versionCode.
	 */
	public void setVersionCode(net.ihe.gazelle.hl7v3.datatypes.CS versionCode) {
	    this.versionCode = versionCode;
	}
	
	
	
	
	/**
	 * Return interactionId.
	 * @return interactionId
	 */
	public net.ihe.gazelle.hl7v3.datatypes.II getInteractionId() {
	    return interactionId;
	}
	
	/**
	 * Set a value to attribute interactionId.
	 * @param interactionId.
	 */
	public void setInteractionId(net.ihe.gazelle.hl7v3.datatypes.II interactionId) {
	    this.interactionId = interactionId;
	}
	
	
	
	
	/**
	 * Return profileId.
	 * @return profileId
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.II> getProfileId() {
		if (profileId == null) {
	        profileId = new ArrayList<net.ihe.gazelle.hl7v3.datatypes.II>();
	    }
	    return profileId;
	}
	
	/**
	 * Set a value to attribute profileId.
	 * @param profileId.
	 */
	public void setProfileId(List<net.ihe.gazelle.hl7v3.datatypes.II> profileId) {
	    this.profileId = profileId;
	}
	
	
	
	/**
	 * Add a profileId to the profileId collection.
	 * @param profileId_elt Element to add.
	 */
	public void addProfileId(net.ihe.gazelle.hl7v3.datatypes.II profileId_elt) {
	    this.profileId.add(profileId_elt);
	}
	
	/**
	 * Remove a profileId to the profileId collection.
	 * @param profileId_elt Element to remove
	 */
	public void removeProfileId(net.ihe.gazelle.hl7v3.datatypes.II profileId_elt) {
	    this.profileId.remove(profileId_elt);
	}
	
	/**
	 * Return processingCode.
	 * @return processingCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CS getProcessingCode() {
	    return processingCode;
	}
	
	/**
	 * Set a value to attribute processingCode.
	 * @param processingCode.
	 */
	public void setProcessingCode(net.ihe.gazelle.hl7v3.datatypes.CS processingCode) {
	    this.processingCode = processingCode;
	}
	
	
	
	
	/**
	 * Return processingModeCode.
	 * @return processingModeCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CS getProcessingModeCode() {
	    return processingModeCode;
	}
	
	/**
	 * Set a value to attribute processingModeCode.
	 * @param processingModeCode.
	 */
	public void setProcessingModeCode(net.ihe.gazelle.hl7v3.datatypes.CS processingModeCode) {
	    this.processingModeCode = processingModeCode;
	}
	
	
	
	
	/**
	 * Return acceptAckCode.
	 * @return acceptAckCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CS getAcceptAckCode() {
	    return acceptAckCode;
	}
	
	/**
	 * Set a value to attribute acceptAckCode.
	 * @param acceptAckCode.
	 */
	public void setAcceptAckCode(net.ihe.gazelle.hl7v3.datatypes.CS acceptAckCode) {
	    this.acceptAckCode = acceptAckCode;
	}
	
	
	
	
	/**
	 * Return sequenceNumber.
	 * @return sequenceNumber
	 */
	public net.ihe.gazelle.hl7v3.datatypes.INT getSequenceNumber() {
	    return sequenceNumber;
	}
	
	/**
	 * Set a value to attribute sequenceNumber.
	 * @param sequenceNumber.
	 */
	public void setSequenceNumber(net.ihe.gazelle.hl7v3.datatypes.INT sequenceNumber) {
	    this.sequenceNumber = sequenceNumber;
	}
	
	
	
	
	/**
	 * Return attachmentText.
	 * @return attachmentText
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.ED> getAttachmentText() {
		if (attachmentText == null) {
	        attachmentText = new ArrayList<net.ihe.gazelle.hl7v3.datatypes.ED>();
	    }
	    return attachmentText;
	}
	
	/**
	 * Set a value to attribute attachmentText.
	 * @param attachmentText.
	 */
	public void setAttachmentText(List<net.ihe.gazelle.hl7v3.datatypes.ED> attachmentText) {
	    this.attachmentText = attachmentText;
	}
	
	
	
	/**
	 * Add a attachmentText to the attachmentText collection.
	 * @param attachmentText_elt Element to add.
	 */
	public void addAttachmentText(net.ihe.gazelle.hl7v3.datatypes.ED attachmentText_elt) {
	    this.attachmentText.add(attachmentText_elt);
	}
	
	/**
	 * Remove a attachmentText to the attachmentText collection.
	 * @param attachmentText_elt Element to remove
	 */
	public void removeAttachmentText(net.ihe.gazelle.hl7v3.datatypes.ED attachmentText_elt) {
	    this.attachmentText.remove(attachmentText_elt);
	}
	
	/**
	 * Return receiver.
	 * @return receiver
	 */
	public List<net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Receiver> getReceiver() {
		if (receiver == null) {
	        receiver = new ArrayList<net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Receiver>();
	    }
	    return receiver;
	}
	
	/**
	 * Set a value to attribute receiver.
	 * @param receiver.
	 */
	public void setReceiver(List<net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Receiver> receiver) {
	    this.receiver = receiver;
	}
	
	
	
	/**
	 * Add a receiver to the receiver collection.
	 * @param receiver_elt Element to add.
	 */
	public void addReceiver(net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Receiver receiver_elt) {
	    this.getReceiver().add(receiver_elt);
	}
	
	/**
	 * Remove a receiver to the receiver collection.
	 * @param receiver_elt Element to remove
	 */
	public void removeReceiver(net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Receiver receiver_elt) {
	    this.receiver.remove(receiver_elt);
	}
	
	/**
	 * Return respondTo.
	 * @return respondTo
	 */
	public List<net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01RespondTo> getRespondTo() {
		if (respondTo == null) {
	        respondTo = new ArrayList<net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01RespondTo>();
	    }
	    return respondTo;
	}
	
	/**
	 * Set a value to attribute respondTo.
	 * @param respondTo.
	 */
	public void setRespondTo(List<net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01RespondTo> respondTo) {
	    this.respondTo = respondTo;
	}
	
	
	
	/**
	 * Add a respondTo to the respondTo collection.
	 * @param respondTo_elt Element to add.
	 */
	public void addRespondTo(net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01RespondTo respondTo_elt) {
	    this.respondTo.add(respondTo_elt);
	}
	
	/**
	 * Remove a respondTo to the respondTo collection.
	 * @param respondTo_elt Element to remove
	 */
	public void removeRespondTo(net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01RespondTo respondTo_elt) {
	    this.respondTo.remove(respondTo_elt);
	}
	
	/**
	 * Return sender.
	 * @return sender
	 */
	public net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Sender getSender() {
	    return sender;
	}
	
	/**
	 * Set a value to attribute sender.
	 * @param sender.
	 */
	public void setSender(net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Sender sender) {
	    this.sender = sender;
	}
	
	
	
	
	/**
	 * Return attentionLine.
	 * @return attentionLine
	 */
	public List<net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01AttentionLine> getAttentionLine() {
		if (attentionLine == null) {
	        attentionLine = new ArrayList<net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01AttentionLine>();
	    }
	    return attentionLine;
	}
	
	/**
	 * Set a value to attribute attentionLine.
	 * @param attentionLine.
	 */
	public void setAttentionLine(List<net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01AttentionLine> attentionLine) {
	    this.attentionLine = attentionLine;
	}
	
	
	
	/**
	 * Add a attentionLine to the attentionLine collection.
	 * @param attentionLine_elt Element to add.
	 */
	public void addAttentionLine(net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01AttentionLine attentionLine_elt) {
	    this.attentionLine.add(attentionLine_elt);
	}
	
	/**
	 * Remove a attentionLine to the attentionLine collection.
	 * @param attentionLine_elt Element to remove
	 */
	public void removeAttentionLine(net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01AttentionLine attentionLine_elt) {
	    this.attentionLine.remove(attentionLine_elt);
	}
	
	/**
	 * Return controlActProcess.
	 * @return controlActProcess
	 */
	public net.ihe.gazelle.hl7v3.prpain201309UV02.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess getControlActProcess() {
	    return controlActProcess;
	}
	
	/**
	 * Set a value to attribute controlActProcess.
	 * @param controlActProcess.
	 */
	public void setControlActProcess(net.ihe.gazelle.hl7v3.prpain201309UV02.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess controlActProcess) {
	    this.controlActProcess = controlActProcess;
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.prpain201309UV02");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "PRPA_IN201309UV02.MCCI_MT000100UV01.Message").item(0);
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
   public static void validateByModule(PRPAIN201309UV02MCCIMT000100UV01Message pRPAIN201309UV02MCCIMT000100UV01Message, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (pRPAIN201309UV02MCCIMT000100UV01Message != null){
   			cvm.validate(pRPAIN201309UV02MCCIMT000100UV01Message, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: pRPAIN201309UV02MCCIMT000100UV01Message.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(pRPAIN201309UV02MCCIMT000100UV01Message.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: pRPAIN201309UV02MCCIMT000100UV01Message.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(pRPAIN201309UV02MCCIMT000100UV01Message.getId(), _location + "/id", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.TS.validateByModule(pRPAIN201309UV02MCCIMT000100UV01Message.getCreationTime(), _location + "/creationTime", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.ST.validateByModule(pRPAIN201309UV02MCCIMT000100UV01Message.getSecurityText(), _location + "/securityText", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(pRPAIN201309UV02MCCIMT000100UV01Message.getVersionCode(), _location + "/versionCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(pRPAIN201309UV02MCCIMT000100UV01Message.getInteractionId(), _location + "/interactionId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II profileId: pRPAIN201309UV02MCCIMT000100UV01Message.getProfileId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(profileId, _location + "/profileId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(pRPAIN201309UV02MCCIMT000100UV01Message.getProcessingCode(), _location + "/processingCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(pRPAIN201309UV02MCCIMT000100UV01Message.getProcessingModeCode(), _location + "/processingModeCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(pRPAIN201309UV02MCCIMT000100UV01Message.getAcceptAckCode(), _location + "/acceptAckCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.INT.validateByModule(pRPAIN201309UV02MCCIMT000100UV01Message.getSequenceNumber(), _location + "/sequenceNumber", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.ED attachmentText: pRPAIN201309UV02MCCIMT000100UV01Message.getAttachmentText()){
					net.ihe.gazelle.hl7v3.datatypes.ED.validateByModule(attachmentText, _location + "/attachmentText[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Receiver receiver: pRPAIN201309UV02MCCIMT000100UV01Message.getReceiver()){
					net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Receiver.validateByModule(receiver, _location + "/receiver[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01RespondTo respondTo: pRPAIN201309UV02MCCIMT000100UV01Message.getRespondTo()){
					net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01RespondTo.validateByModule(respondTo, _location + "/respondTo[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01Sender.validateByModule(pRPAIN201309UV02MCCIMT000100UV01Message.getSender(), _location + "/sender", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01AttentionLine attentionLine: pRPAIN201309UV02MCCIMT000100UV01Message.getAttentionLine()){
					net.ihe.gazelle.hl7v3.mccimt000100UV01.MCCIMT000100UV01AttentionLine.validateByModule(attentionLine, _location + "/attentionLine[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.prpain201309UV02.PRPAIN201309UV02QUQIMT021001UV01ControlActProcess.validateByModule(pRPAIN201309UV02MCCIMT000100UV01Message.getControlActProcess(), _location + "/controlActProcess", cvm, diagnostic);
    	}
    }

}