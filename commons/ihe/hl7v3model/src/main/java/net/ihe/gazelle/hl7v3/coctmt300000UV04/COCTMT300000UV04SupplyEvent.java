/**
 * COCTMT300000UV04SupplyEvent.java
 *
 * File generated from the coctmt300000UV04::COCTMT300000UV04SupplyEvent uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.coctmt300000UV04;

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
 * Description of the class COCTMT300000UV04SupplyEvent.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT300000UV04.SupplyEvent", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"id",
	"code",
	"effectiveTime",
	"quantity",
	"expectedUseTime",
	"product",
	"performer",
	"origin",
	"destination",
	"location",
	"reasonOf",
	"classCode",
	"moodCode",
	"nullFlavor"
})
@XmlRootElement(name = "COCT_MT300000UV04.SupplyEvent")
public class COCTMT300000UV04SupplyEvent implements java.io.Serializable {
	
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
	public net.ihe.gazelle.hl7v3.datatypes.II id;
	@XmlElement(name = "code", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE code;
	@XmlElement(name = "effectiveTime", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.TS effectiveTime;
	@XmlElement(name = "quantity", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.PQ quantity;
	@XmlElement(name = "expectedUseTime", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS expectedUseTime;
	@XmlElement(name = "product", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Product product;
	@XmlElement(name = "performer", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Performer1 performer;
	@XmlElement(name = "origin", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Origin origin;
	@XmlElement(name = "destination", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Destination destination;
	@XmlElement(name = "location", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Location location;
	@XmlElement(name = "reasonOf", required = true, namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Reason2 reasonOf;
	@XmlAttribute(name = "classCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.ActClassSupply classCode;
	@XmlAttribute(name = "moodCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.XActMoodIntentEvent moodCode;
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
	public net.ihe.gazelle.hl7v3.datatypes.CE getCode() {
	    return code;
	}
	
	/**
	 * Set a value to attribute code.
	 * @param code.
	 */
	public void setCode(net.ihe.gazelle.hl7v3.datatypes.CE code) {
	    this.code = code;
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
	 * Return quantity.
	 * @return quantity
	 */
	public net.ihe.gazelle.hl7v3.datatypes.PQ getQuantity() {
	    return quantity;
	}
	
	/**
	 * Set a value to attribute quantity.
	 * @param quantity.
	 */
	public void setQuantity(net.ihe.gazelle.hl7v3.datatypes.PQ quantity) {
	    this.quantity = quantity;
	}
	
	
	
	
	/**
	 * Return expectedUseTime.
	 * @return expectedUseTime
	 */
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS getExpectedUseTime() {
	    return expectedUseTime;
	}
	
	/**
	 * Set a value to attribute expectedUseTime.
	 * @param expectedUseTime.
	 */
	public void setExpectedUseTime(net.ihe.gazelle.hl7v3.datatypes.IVLTS expectedUseTime) {
	    this.expectedUseTime = expectedUseTime;
	}
	
	
	
	
	/**
	 * Return product.
	 * @return product
	 */
	public net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Product getProduct() {
	    return product;
	}
	
	/**
	 * Set a value to attribute product.
	 * @param product.
	 */
	public void setProduct(net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Product product) {
	    this.product = product;
	}
	
	
	
	
	/**
	 * Return performer.
	 * @return performer
	 */
	public net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Performer1 getPerformer() {
	    return performer;
	}
	
	/**
	 * Set a value to attribute performer.
	 * @param performer.
	 */
	public void setPerformer(net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Performer1 performer) {
	    this.performer = performer;
	}
	
	
	
	
	/**
	 * Return origin.
	 * @return origin
	 */
	public net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Origin getOrigin() {
	    return origin;
	}
	
	/**
	 * Set a value to attribute origin.
	 * @param origin.
	 */
	public void setOrigin(net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Origin origin) {
	    this.origin = origin;
	}
	
	
	
	
	/**
	 * Return destination.
	 * @return destination
	 */
	public net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Destination getDestination() {
	    return destination;
	}
	
	/**
	 * Set a value to attribute destination.
	 * @param destination.
	 */
	public void setDestination(net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Destination destination) {
	    this.destination = destination;
	}
	
	
	
	
	/**
	 * Return location.
	 * @return location
	 */
	public net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Location getLocation() {
	    return location;
	}
	
	/**
	 * Set a value to attribute location.
	 * @param location.
	 */
	public void setLocation(net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Location location) {
	    this.location = location;
	}
	
	
	
	
	/**
	 * Return reasonOf.
	 * @return reasonOf
	 */
	public net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Reason2 getReasonOf() {
	    return reasonOf;
	}
	
	/**
	 * Set a value to attribute reasonOf.
	 * @param reasonOf.
	 */
	public void setReasonOf(net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Reason2 reasonOf) {
	    this.reasonOf = reasonOf;
	}
	
	
	
	
	/**
	 * Return classCode.
	 * @return classCode
	 */
	public net.ihe.gazelle.hl7v3.voc.ActClassSupply getClassCode() {
	    return classCode;
	}
	
	/**
	 * Set a value to attribute classCode.
	 * @param classCode.
	 */
	public void setClassCode(net.ihe.gazelle.hl7v3.voc.ActClassSupply classCode) {
	    this.classCode = classCode;
	}
	
	
	
	
	/**
	 * Return moodCode.
	 * @return moodCode
	 */
	public net.ihe.gazelle.hl7v3.voc.XActMoodIntentEvent getMoodCode() {
	    return moodCode;
	}
	
	/**
	 * Set a value to attribute moodCode.
	 * @param moodCode.
	 */
	public void setMoodCode(net.ihe.gazelle.hl7v3.voc.XActMoodIntentEvent moodCode) {
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.coctmt300000UV04");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT300000UV04.SupplyEvent").item(0);
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
   public static void validateByModule(COCTMT300000UV04SupplyEvent cOCTMT300000UV04SupplyEvent, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT300000UV04SupplyEvent != null){
   			cvm.validate(cOCTMT300000UV04SupplyEvent, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT300000UV04SupplyEvent.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT300000UV04SupplyEvent.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT300000UV04SupplyEvent.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT300000UV04SupplyEvent.getId(), _location + "/id", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT300000UV04SupplyEvent.getCode(), _location + "/code", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.TS.validateByModule(cOCTMT300000UV04SupplyEvent.getEffectiveTime(), _location + "/effectiveTime", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.PQ.validateByModule(cOCTMT300000UV04SupplyEvent.getQuantity(), _location + "/quantity", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.IVLTS.validateByModule(cOCTMT300000UV04SupplyEvent.getExpectedUseTime(), _location + "/expectedUseTime", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Product.validateByModule(cOCTMT300000UV04SupplyEvent.getProduct(), _location + "/product", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Performer1.validateByModule(cOCTMT300000UV04SupplyEvent.getPerformer(), _location + "/performer", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Origin.validateByModule(cOCTMT300000UV04SupplyEvent.getOrigin(), _location + "/origin", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Destination.validateByModule(cOCTMT300000UV04SupplyEvent.getDestination(), _location + "/destination", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Location.validateByModule(cOCTMT300000UV04SupplyEvent.getLocation(), _location + "/location", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt300000UV04.COCTMT300000UV04Reason2.validateByModule(cOCTMT300000UV04SupplyEvent.getReasonOf(), _location + "/reasonOf", cvm, diagnostic);
    	}
    }

}