/**
 * MFMIMT700711UV01QueryAck.java
 *
 * File generated from the mfmimt700711UV01::MFMIMT700711UV01QueryAck uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.mfmimt700711UV01;

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
 * Description of the class MFMIMT700711UV01QueryAck.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MFMI_MT700711UV01.QueryAck", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"queryId",
	"statusCode",
	"queryResponseCode",
	"resultTotalQuantity",
	"resultCurrentQuantity",
	"resultRemainingQuantity",
	"nullFlavor"
})
@XmlRootElement(name = "MFMI_MT700711UV01.QueryAck")
public class MFMIMT700711UV01QueryAck implements java.io.Serializable {
	
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
	@XmlElement(name = "queryId", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.II queryId;
	@XmlElement(name = "statusCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CS statusCode;
	@XmlElement(name = "queryResponseCode", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CS queryResponseCode;
	@XmlElement(name = "resultTotalQuantity", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.INT resultTotalQuantity;
	@XmlElement(name = "resultCurrentQuantity", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.INT resultCurrentQuantity;
	@XmlElement(name = "resultRemainingQuantity", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.INT resultRemainingQuantity;
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
	 * Return queryId.
	 * @return queryId
	 */
	public net.ihe.gazelle.hl7v3.datatypes.II getQueryId() {
	    return queryId;
	}
	
	/**
	 * Set a value to attribute queryId.
	 * @param queryId.
	 */
	public void setQueryId(net.ihe.gazelle.hl7v3.datatypes.II queryId) {
	    this.queryId = queryId;
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
	 * Return queryResponseCode.
	 * @return queryResponseCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CS getQueryResponseCode() {
	    return queryResponseCode;
	}
	
	/**
	 * Set a value to attribute queryResponseCode.
	 * @param queryResponseCode.
	 */
	public void setQueryResponseCode(net.ihe.gazelle.hl7v3.datatypes.CS queryResponseCode) {
	    this.queryResponseCode = queryResponseCode;
	}
	
	
	
	
	/**
	 * Return resultTotalQuantity.
	 * @return resultTotalQuantity
	 */
	public net.ihe.gazelle.hl7v3.datatypes.INT getResultTotalQuantity() {
	    return resultTotalQuantity;
	}
	
	/**
	 * Set a value to attribute resultTotalQuantity.
	 * @param resultTotalQuantity.
	 */
	public void setResultTotalQuantity(net.ihe.gazelle.hl7v3.datatypes.INT resultTotalQuantity) {
	    this.resultTotalQuantity = resultTotalQuantity;
	}
	
	
	
	
	/**
	 * Return resultCurrentQuantity.
	 * @return resultCurrentQuantity
	 */
	public net.ihe.gazelle.hl7v3.datatypes.INT getResultCurrentQuantity() {
	    return resultCurrentQuantity;
	}
	
	/**
	 * Set a value to attribute resultCurrentQuantity.
	 * @param resultCurrentQuantity.
	 */
	public void setResultCurrentQuantity(net.ihe.gazelle.hl7v3.datatypes.INT resultCurrentQuantity) {
	    this.resultCurrentQuantity = resultCurrentQuantity;
	}
	
	
	
	
	/**
	 * Return resultRemainingQuantity.
	 * @return resultRemainingQuantity
	 */
	public net.ihe.gazelle.hl7v3.datatypes.INT getResultRemainingQuantity() {
	    return resultRemainingQuantity;
	}
	
	/**
	 * Set a value to attribute resultRemainingQuantity.
	 * @param resultRemainingQuantity.
	 */
	public void setResultRemainingQuantity(net.ihe.gazelle.hl7v3.datatypes.INT resultRemainingQuantity) {
	    this.resultRemainingQuantity = resultRemainingQuantity;
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.mfmimt700711UV01");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "MFMI_MT700711UV01.QueryAck").item(0);
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
   public static void validateByModule(MFMIMT700711UV01QueryAck mFMIMT700711UV01QueryAck, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (mFMIMT700711UV01QueryAck != null){
   			cvm.validate(mFMIMT700711UV01QueryAck, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: mFMIMT700711UV01QueryAck.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(mFMIMT700711UV01QueryAck.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: mFMIMT700711UV01QueryAck.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(mFMIMT700711UV01QueryAck.getQueryId(), _location + "/queryId", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(mFMIMT700711UV01QueryAck.getStatusCode(), _location + "/statusCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(mFMIMT700711UV01QueryAck.getQueryResponseCode(), _location + "/queryResponseCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.INT.validateByModule(mFMIMT700711UV01QueryAck.getResultTotalQuantity(), _location + "/resultTotalQuantity", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.INT.validateByModule(mFMIMT700711UV01QueryAck.getResultCurrentQuantity(), _location + "/resultCurrentQuantity", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.INT.validateByModule(mFMIMT700711UV01QueryAck.getResultRemainingQuantity(), _location + "/resultRemainingQuantity", cvm, diagnostic);
    	}
    }

}