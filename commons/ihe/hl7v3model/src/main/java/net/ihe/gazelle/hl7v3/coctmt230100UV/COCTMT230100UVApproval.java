/**
 * COCTMT230100UVApproval.java
 *
 * File generated from the coctmt230100UV::COCTMT230100UVApproval uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.coctmt230100UV;

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
 * Description of the class COCTMT230100UVApproval.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT230100UV.Approval", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"id",
	"code",
	"statusCode",
	"holder",
	"author",
	"classCode",
	"moodCode",
	"nullFlavor"
})
@XmlRootElement(name = "COCT_MT230100UV.Approval")
public class COCTMT230100UVApproval implements java.io.Serializable {
	
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
	@XmlElement(name = "code", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CD code;
	@XmlElement(name = "statusCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CS statusCode;
	@XmlElement(name = "holder", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt230100UV.COCTMT230100UVHolder holder;
	@XmlElement(name = "author", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt230100UV.COCTMT230100UVAuthor author;
	@XmlAttribute(name = "classCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.ActClassContract classCode;
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
	 * Return holder.
	 * @return holder
	 */
	public net.ihe.gazelle.hl7v3.coctmt230100UV.COCTMT230100UVHolder getHolder() {
	    return holder;
	}
	
	/**
	 * Set a value to attribute holder.
	 * @param holder.
	 */
	public void setHolder(net.ihe.gazelle.hl7v3.coctmt230100UV.COCTMT230100UVHolder holder) {
	    this.holder = holder;
	}
	
	
	
	
	/**
	 * Return author.
	 * @return author
	 */
	public net.ihe.gazelle.hl7v3.coctmt230100UV.COCTMT230100UVAuthor getAuthor() {
	    return author;
	}
	
	/**
	 * Set a value to attribute author.
	 * @param author.
	 */
	public void setAuthor(net.ihe.gazelle.hl7v3.coctmt230100UV.COCTMT230100UVAuthor author) {
	    this.author = author;
	}
	
	
	
	
	/**
	 * Return classCode.
	 * @return classCode
	 */
	public net.ihe.gazelle.hl7v3.voc.ActClassContract getClassCode() {
	    return classCode;
	}
	
	/**
	 * Set a value to attribute classCode.
	 * @param classCode.
	 */
	public void setClassCode(net.ihe.gazelle.hl7v3.voc.ActClassContract classCode) {
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.coctmt230100UV");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT230100UV.Approval").item(0);
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
   public static void validateByModule(COCTMT230100UVApproval cOCTMT230100UVApproval, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT230100UVApproval != null){
   			cvm.validate(cOCTMT230100UVApproval, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT230100UVApproval.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT230100UVApproval.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT230100UVApproval.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT230100UVApproval.getId(), _location + "/id", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CD.validateByModule(cOCTMT230100UVApproval.getCode(), _location + "/code", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(cOCTMT230100UVApproval.getStatusCode(), _location + "/statusCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt230100UV.COCTMT230100UVHolder.validateByModule(cOCTMT230100UVApproval.getHolder(), _location + "/holder", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt230100UV.COCTMT230100UVAuthor.validateByModule(cOCTMT230100UVApproval.getAuthor(), _location + "/author", cvm, diagnostic);
    	}
    }

}