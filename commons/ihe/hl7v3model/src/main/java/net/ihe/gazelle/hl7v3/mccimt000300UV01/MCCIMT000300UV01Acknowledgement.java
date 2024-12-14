/**
 * MCCIMT000300UV01Acknowledgement.java
 * <p>
 * File generated from the mccimt000300UV01::MCCIMT000300UV01Acknowledgement uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.mccimt000300UV01;

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
 * Description of the class MCCIMT000300UV01Acknowledgement.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MCCI_MT000300UV01.Acknowledgement", propOrder = { "realmCode", "typeId", "templateId", "typeCode",
		"messageWaitingNumber", "messageWaitingPriorityCode", "targetMessage", "acknowledgementDetail", "nullFlavor" })
@XmlRootElement(name = "MCCI_MT000300UV01.Acknowledgement")
public class MCCIMT000300UV01Acknowledgement implements java.io.Serializable {

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
	@XmlElement(name = "typeCode", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CS typeCode;
	@XmlElement(name = "messageWaitingNumber", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.INT messageWaitingNumber;
	@XmlElement(name = "messageWaitingPriorityCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE messageWaitingPriorityCode;
	@XmlElement(name = "targetMessage", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01TargetMessage targetMessage;
	@XmlElement(name = "acknowledgementDetail", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01AcknowledgementDetail> acknowledgementDetail;
	@XmlAttribute(name = "nullFlavor")
	public net.ihe.gazelle.hl7v3.voc.NullFlavor nullFlavor;

	/**
	 * An attribute containing marshalled element node
	 */
	@XmlTransient
	private org.w3c.dom.Node _xmlNodePresentation;

	/**
	 * Return realmCode.
	 * 
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
	 * 
	 * @param realmCode
	 *            .
	 */
	public void setRealmCode(List<net.ihe.gazelle.hl7v3.datatypes.CS> realmCode) {
		this.realmCode = realmCode;
	}

	/**
	 * Add a realmCode to the realmCode collection.
	 * 
	 * @param realmCode_elt
	 *            Element to add.
	 */
	public void addRealmCode(net.ihe.gazelle.hl7v3.datatypes.CS realmCode_elt) {
		this.realmCode.add(realmCode_elt);
	}

	/**
	 * Remove a realmCode to the realmCode collection.
	 * 
	 * @param realmCode_elt
	 *            Element to remove
	 */
	public void removeRealmCode(net.ihe.gazelle.hl7v3.datatypes.CS realmCode_elt) {
		this.realmCode.remove(realmCode_elt);
	}

	/**
	 * Return typeId.
	 * 
	 * @return typeId
	 */
	public net.ihe.gazelle.hl7v3.datatypes.II getTypeId() {
		return typeId;
	}

	/**
	 * Set a value to attribute typeId.
	 * 
	 * @param typeId
	 *            .
	 */
	public void setTypeId(net.ihe.gazelle.hl7v3.datatypes.II typeId) {
		this.typeId = typeId;
	}

	/**
	 * Return templateId.
	 * 
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
	 * 
	 * @param templateId
	 *            .
	 */
	public void setTemplateId(List<net.ihe.gazelle.hl7v3.datatypes.II> templateId) {
		this.templateId = templateId;
	}

	/**
	 * Add a templateId to the templateId collection.
	 * 
	 * @param templateId_elt
	 *            Element to add.
	 */
	public void addTemplateId(net.ihe.gazelle.hl7v3.datatypes.II templateId_elt) {
		this.templateId.add(templateId_elt);
	}

	/**
	 * Remove a templateId to the templateId collection.
	 * 
	 * @param templateId_elt
	 *            Element to remove
	 */
	public void removeTemplateId(net.ihe.gazelle.hl7v3.datatypes.II templateId_elt) {
		this.templateId.remove(templateId_elt);
	}

	/**
	 * Return typeCode.
	 * 
	 * @return typeCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CS getTypeCode() {
		return typeCode;
	}

	/**
	 * Set a value to attribute typeCode.
	 * 
	 * @param typeCode
	 *            .
	 */
	public void setTypeCode(net.ihe.gazelle.hl7v3.datatypes.CS typeCode) {
		this.typeCode = typeCode;
	}

	/**
	 * Return messageWaitingNumber.
	 * 
	 * @return messageWaitingNumber
	 */
	public net.ihe.gazelle.hl7v3.datatypes.INT getMessageWaitingNumber() {
		return messageWaitingNumber;
	}

	/**
	 * Set a value to attribute messageWaitingNumber.
	 * 
	 * @param messageWaitingNumber
	 *            .
	 */
	public void setMessageWaitingNumber(net.ihe.gazelle.hl7v3.datatypes.INT messageWaitingNumber) {
		this.messageWaitingNumber = messageWaitingNumber;
	}

	/**
	 * Return messageWaitingPriorityCode.
	 * 
	 * @return messageWaitingPriorityCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getMessageWaitingPriorityCode() {
		return messageWaitingPriorityCode;
	}

	/**
	 * Set a value to attribute messageWaitingPriorityCode.
	 * 
	 * @param messageWaitingPriorityCode
	 *            .
	 */
	public void setMessageWaitingPriorityCode(net.ihe.gazelle.hl7v3.datatypes.CE messageWaitingPriorityCode) {
		this.messageWaitingPriorityCode = messageWaitingPriorityCode;
	}

	/**
	 * Return targetMessage.
	 * 
	 * @return targetMessage
	 */
	public net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01TargetMessage getTargetMessage() {
		return targetMessage;
	}

	/**
	 * Set a value to attribute targetMessage.
	 * 
	 * @param targetMessage
	 *            .
	 */
	public void setTargetMessage(net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01TargetMessage targetMessage) {
		this.targetMessage = targetMessage;
	}

	/**
	 * Return acknowledgementDetail.
	 * 
	 * @return acknowledgementDetail
	 */
	public List<net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01AcknowledgementDetail> getAcknowledgementDetail() {
		if (acknowledgementDetail == null) {
			acknowledgementDetail = new ArrayList<>();
		}
		return acknowledgementDetail;
	}

	/**
	 * Set a value to attribute acknowledgementDetail.
	 * 
	 * @param acknowledgementDetail
	 *            .
	 */
	public void setAcknowledgementDetail(
			List<net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01AcknowledgementDetail> acknowledgementDetail) {
		this.acknowledgementDetail = acknowledgementDetail;
	}

	/**
	 * Add a acknowledgementDetail to the acknowledgementDetail collection.
	 * 
	 * @param acknowledgementDetail_elt
	 *            Element to add.
	 */
	public void addAcknowledgementDetail(
			net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01AcknowledgementDetail acknowledgementDetail_elt) {
		if (this.acknowledgementDetail == null) {
			this.acknowledgementDetail = new ArrayList<>();
		}
		this.acknowledgementDetail.add(acknowledgementDetail_elt);
	}

	/**
	 * Remove a acknowledgementDetail to the acknowledgementDetail collection.
	 * 
	 * @param acknowledgementDetail_elt
	 *            Element to remove
	 */
	public void removeAcknowledgementDetail(
			net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01AcknowledgementDetail acknowledgementDetail_elt) {
		if (this.acknowledgementDetail != null) {
			this.acknowledgementDetail.remove(acknowledgementDetail_elt);
		}
	}

	/**
	 * Return nullFlavor.
	 * 
	 * @return nullFlavor
	 */
	public net.ihe.gazelle.hl7v3.voc.NullFlavor getNullFlavor() {
		return nullFlavor;
	}

	/**
	 * Set a value to attribute nullFlavor.
	 * 
	 * @param nullFlavor
	 *            .
	 */
	public void setNullFlavor(net.ihe.gazelle.hl7v3.voc.NullFlavor nullFlavor) {
		this.nullFlavor = nullFlavor;
	}

	public Node get_xmlNodePresentation() {
		if (_xmlNodePresentation == null) {
			JAXBContext jc;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db = null;
			Document doc = null;
			try {
				db = dbf.newDocumentBuilder();
				doc = db.newDocument();
			} catch (ParserConfigurationException ignored) {
			}
			try {
				jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.mccimt000300UV01");
				Marshaller m = jc.createMarshaller();
				m.marshal(this, doc);
				_xmlNodePresentation = doc
						.getElementsByTagNameNS("urn:hl7-org:v3", "MCCI_MT000300UV01.Acknowledgement").item(0);
			} catch (JAXBException e) {
				try {
					db = dbf.newDocumentBuilder();
					_xmlNodePresentation = db.newDocument();
				} catch (Exception ignored) {
				}
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
	public static void validateByModule(MCCIMT000300UV01Acknowledgement mCCIMT000300UV01Acknowledgement,
			String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic) {
		if (mCCIMT000300UV01Acknowledgement != null) {
			cvm.validate(mCCIMT000300UV01Acknowledgement, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode : mCCIMT000300UV01Acknowledgement.getRealmCode()) {
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]",
							cvm, diagnostic);
					i++;
				}
			}

			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(mCCIMT000300UV01Acknowledgement.getTypeId(), _location
					+ "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId : mCCIMT000300UV01Acknowledgement.getTemplateId()) {
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i
							+ "]", cvm, diagnostic);
					i++;
				}
			}

			net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(mCCIMT000300UV01Acknowledgement.getTypeCode(),
					_location + "/typeCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.INT.validateByModule(
					mCCIMT000300UV01Acknowledgement.getMessageWaitingNumber(), _location + "/messageWaitingNumber",
					cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(
					mCCIMT000300UV01Acknowledgement.getMessageWaitingPriorityCode(), _location
							+ "/messageWaitingPriorityCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01TargetMessage.validateByModule(
					mCCIMT000300UV01Acknowledgement.getTargetMessage(), _location + "/targetMessage", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01AcknowledgementDetail acknowledgementDetail : mCCIMT000300UV01Acknowledgement
						.getAcknowledgementDetail()) {
					net.ihe.gazelle.hl7v3.mccimt000300UV01.MCCIMT000300UV01AcknowledgementDetail.validateByModule(
							acknowledgementDetail, _location + "/acknowledgementDetail[" + i + "]", cvm, diagnostic);
					i++;
				}
			}

		}
	}

}