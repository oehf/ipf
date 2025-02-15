/**
 * COCTMT510000UV06Precondition.java
 * <p>
 * File generated from the coctmt510000UV06::COCTMT510000UV06Precondition uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.coctmt510000UV06;

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
 * Description of the class COCTMT510000UV06Precondition.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT510000UV06.Precondition", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"observation",
	"substanceAdministration",
	"supply",
	"procedure",
	"encounter",
	"act",
	"organizer",
	"actReference",
	"nullFlavor",
	"typeCode"
})
@XmlRootElement(name = "COCT_MT510000UV06.Precondition")
public class COCTMT510000UV06Precondition implements java.io.Serializable {
	
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
	@XmlElement(name = "observation", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVObservation observation;
	@XmlElement(name = "substanceAdministration", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSubstanceAdministration substanceAdministration;
	@XmlElement(name = "supply", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSupply supply;
	@XmlElement(name = "procedure", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVProcedure procedure;
	@XmlElement(name = "encounter", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVEncounter encounter;
	@XmlElement(name = "act", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVAct act;
	@XmlElement(name = "organizer", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVOrganizer organizer;
	@XmlElement(name = "actReference", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVActReference actReference;
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
	 * Return observation.
	 * @return observation
	 */
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVObservation getObservation() {
	    return observation;
	}
	
	/**
	 * Set a value to attribute observation.
     */
	public void setObservation(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVObservation observation) {
	    this.observation = observation;
	}
	
	
	
	
	/**
	 * Return substanceAdministration.
	 * @return substanceAdministration
	 */
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSubstanceAdministration getSubstanceAdministration() {
	    return substanceAdministration;
	}
	
	/**
	 * Set a value to attribute substanceAdministration.
     */
	public void setSubstanceAdministration(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSubstanceAdministration substanceAdministration) {
	    this.substanceAdministration = substanceAdministration;
	}
	
	
	
	
	/**
	 * Return supply.
	 * @return supply
	 */
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSupply getSupply() {
	    return supply;
	}
	
	/**
	 * Set a value to attribute supply.
     */
	public void setSupply(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSupply supply) {
	    this.supply = supply;
	}
	
	
	
	
	/**
	 * Return procedure.
	 * @return procedure
	 */
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVProcedure getProcedure() {
	    return procedure;
	}
	
	/**
	 * Set a value to attribute procedure.
     */
	public void setProcedure(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVProcedure procedure) {
	    this.procedure = procedure;
	}
	
	
	
	
	/**
	 * Return encounter.
	 * @return encounter
	 */
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVEncounter getEncounter() {
	    return encounter;
	}
	
	/**
	 * Set a value to attribute encounter.
     */
	public void setEncounter(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVEncounter encounter) {
	    this.encounter = encounter;
	}
	
	
	
	
	/**
	 * Return act.
	 * @return act
	 */
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVAct getAct() {
	    return act;
	}
	
	/**
	 * Set a value to attribute act.
     */
	public void setAct(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVAct act) {
	    this.act = act;
	}
	
	
	
	
	/**
	 * Return organizer.
	 * @return organizer
	 */
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVOrganizer getOrganizer() {
	    return organizer;
	}
	
	/**
	 * Set a value to attribute organizer.
     */
	public void setOrganizer(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVOrganizer organizer) {
	    this.organizer = organizer;
	}
	
	
	
	
	/**
	 * Return actReference.
	 * @return actReference
	 */
	public net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVActReference getActReference() {
	    return actReference;
	}
	
	/**
	 * Set a value to attribute actReference.
     */
	public void setActReference(net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVActReference actReference) {
	    this.actReference = actReference;
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.coctmt510000UV06");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT510000UV06.Precondition").item(0);
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
   public static void validateByModule(COCTMT510000UV06Precondition cOCTMT510000UV06Precondition, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT510000UV06Precondition != null){
   			cvm.validate(cOCTMT510000UV06Precondition, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT510000UV06Precondition.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT510000UV06Precondition.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT510000UV06Precondition.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVObservation.validateByModule(cOCTMT510000UV06Precondition.getObservation(), _location + "/observation", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSubstanceAdministration.validateByModule(cOCTMT510000UV06Precondition.getSubstanceAdministration(), _location + "/substanceAdministration", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVSupply.validateByModule(cOCTMT510000UV06Precondition.getSupply(), _location + "/supply", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVProcedure.validateByModule(cOCTMT510000UV06Precondition.getProcedure(), _location + "/procedure", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVEncounter.validateByModule(cOCTMT510000UV06Precondition.getEncounter(), _location + "/encounter", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVAct.validateByModule(cOCTMT510000UV06Precondition.getAct(), _location + "/act", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVOrganizer.validateByModule(cOCTMT510000UV06Precondition.getOrganizer(), _location + "/organizer", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.coctmt530000UV.COCTMT530000UVActReference.validateByModule(cOCTMT510000UV06Precondition.getActReference(), _location + "/actReference", cvm, diagnostic);
    	}
    }

}