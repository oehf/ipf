/**
 * PRPAMT201306UV02MatchCriterionList.java
 * <p>
 * File generated from the prpamt201306UV02::PRPAMT201306UV02MatchCriterionList uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.prpamt201306UV02;

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
 * Description of the class PRPAMT201306UV02MatchCriterionList.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PRPA_MT201306UV02.MatchCriterionList", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"id",
	"matchAlgorithm",
	"matchWeight",
	"minimumDegreeMatch",
	"nullFlavor"
})
@XmlRootElement(name = "PRPA_MT201306UV02.MatchCriterionList")
public class PRPAMT201306UV02MatchCriterionList implements java.io.Serializable {
	
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
	public net.ihe.gazelle.hl7v3.datatypes.II id;
	@XmlElement(name = "matchAlgorithm", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02MatchAlgorithm matchAlgorithm;
	@XmlElement(name = "matchWeight", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02MatchWeight matchWeight;
	@XmlElement(name = "minimumDegreeMatch", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02MinimumDegreeMatch minimumDegreeMatch;
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
	public net.ihe.gazelle.hl7v3.datatypes.II getId() {
	    return id;
	}
	
	/**
	 * Set a value to attribute id.
     */
	public void setId(net.ihe.gazelle.hl7v3.datatypes.II id) {
	    this.id = id;
	}
	
	
	
	
	/**
	 * Return matchAlgorithm.
	 * @return matchAlgorithm
	 */
	public net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02MatchAlgorithm getMatchAlgorithm() {
	    return matchAlgorithm;
	}
	
	/**
	 * Set a value to attribute matchAlgorithm.
     */
	public void setMatchAlgorithm(net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02MatchAlgorithm matchAlgorithm) {
	    this.matchAlgorithm = matchAlgorithm;
	}
	
	
	
	
	/**
	 * Return matchWeight.
	 * @return matchWeight
	 */
	public net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02MatchWeight getMatchWeight() {
	    return matchWeight;
	}
	
	/**
	 * Set a value to attribute matchWeight.
     */
	public void setMatchWeight(net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02MatchWeight matchWeight) {
	    this.matchWeight = matchWeight;
	}
	
	
	
	
	/**
	 * Return minimumDegreeMatch.
	 * @return minimumDegreeMatch
	 */
	public net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02MinimumDegreeMatch getMinimumDegreeMatch() {
	    return minimumDegreeMatch;
	}
	
	/**
	 * Set a value to attribute minimumDegreeMatch.
     */
	public void setMinimumDegreeMatch(net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02MinimumDegreeMatch minimumDegreeMatch) {
	    this.minimumDegreeMatch = minimumDegreeMatch;
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.prpamt201306UV02");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "PRPA_MT201306UV02.MatchCriterionList").item(0);
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
   public static void validateByModule(PRPAMT201306UV02MatchCriterionList pRPAMT201306UV02MatchCriterionList, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (pRPAMT201306UV02MatchCriterionList != null){
   			cvm.validate(pRPAMT201306UV02MatchCriterionList, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: pRPAMT201306UV02MatchCriterionList.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(pRPAMT201306UV02MatchCriterionList.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: pRPAMT201306UV02MatchCriterionList.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(pRPAMT201306UV02MatchCriterionList.getId(), _location + "/id", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02MatchAlgorithm.validateByModule(pRPAMT201306UV02MatchCriterionList.getMatchAlgorithm(), _location + "/matchAlgorithm", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02MatchWeight.validateByModule(pRPAMT201306UV02MatchCriterionList.getMatchWeight(), _location + "/matchWeight", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.prpamt201306UV02.PRPAMT201306UV02MinimumDegreeMatch.validateByModule(pRPAMT201306UV02MatchCriterionList.getMinimumDegreeMatch(), _location + "/minimumDegreeMatch", cvm, diagnostic);
    	}
    }

}