/**
 * COCTMT030000UV04NonPersonLivingSubject.java
 *
 * File generated from the coctmt030000UV04::COCTMT030000UV04NonPersonLivingSubject uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.coctmt030000UV04;

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
 * Description of the class COCTMT030000UV04NonPersonLivingSubject.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT030000UV04.NonPersonLivingSubject", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"id",
	"quantity",
	"name",
	"desc",
	"statusCode",
	"existenceTime",
	"riskCode",
	"handlingCode",
	"administrativeGenderCode",
	"birthTime",
	"deceasedInd",
	"multipleBirthInd",
	"multipleBirthOrderNumber",
	"organDonorInd",
	"strainText",
	"genderStatusCode",
	"asMember",
	"asOtherIDs",
	"asCoveredParty",
	"contactParty",
	"guardian",
	"guarantor",
	"birthPlace",
	"classCode",
	"determinerCode",
	"nullFlavor"
})
@XmlRootElement(name = "COCT_MT030000UV04.NonPersonLivingSubject")
public class COCTMT030000UV04NonPersonLivingSubject implements java.io.Serializable {
	
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
	@XmlElement(name = "quantity", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.INT quantity;
	@XmlElement(name = "name", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.EN> name;
	@XmlElement(name = "desc", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.ED desc;
	@XmlElement(name = "statusCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CS statusCode;
	@XmlElement(name = "existenceTime", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS existenceTime;
	@XmlElement(name = "riskCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE riskCode;
	@XmlElement(name = "handlingCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE handlingCode;
	@XmlElement(name = "administrativeGenderCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE administrativeGenderCode;
	@XmlElement(name = "birthTime", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.TS birthTime;
	@XmlElement(name = "deceasedInd", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.BL deceasedInd;
	@XmlElement(name = "multipleBirthInd", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.BL multipleBirthInd;
	@XmlElement(name = "multipleBirthOrderNumber", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.INT multipleBirthOrderNumber;
	@XmlElement(name = "organDonorInd", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.BL organDonorInd;
	@XmlElement(name = "strainText", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.ED strainText;
	@XmlElement(name = "genderStatusCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE genderStatusCode;
	@XmlElement(name = "asMember", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Member> asMember;
	@XmlElement(name = "asOtherIDs", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04OtherIDs> asOtherIDs;
	@XmlElement(name = "asCoveredParty", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt500000UV04.COCTMT500000UV04CoveredParty asCoveredParty;
	@XmlElement(name = "contactParty", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04ContactParty> contactParty;
	@XmlElement(name = "guardian", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Guardian> guardian;
	@XmlElement(name = "guarantor", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Guarantor> guarantor;
	@XmlElement(name = "birthPlace", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04BirthPlace birthPlace;
	@XmlAttribute(name = "classCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.EntityClassNonPersonLivingSubject classCode;
	@XmlAttribute(name = "determinerCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.XDeterminerInstanceKind determinerCode;
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
	 * Return quantity.
	 * @return quantity
	 */
	public net.ihe.gazelle.hl7v3.datatypes.INT getQuantity() {
	    return quantity;
	}
	
	/**
	 * Set a value to attribute quantity.
	 * @param quantity.
	 */
	public void setQuantity(net.ihe.gazelle.hl7v3.datatypes.INT quantity) {
	    this.quantity = quantity;
	}
	
	
	
	
	/**
	 * Return name.
	 * @return name
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.EN> getName() {
		if (name == null) {
	        name = new ArrayList<net.ihe.gazelle.hl7v3.datatypes.EN>();
	    }
	    return name;
	}
	
	/**
	 * Set a value to attribute name.
	 * @param name.
	 */
	public void setName(List<net.ihe.gazelle.hl7v3.datatypes.EN> name) {
	    this.name = name;
	}
	
	
	
	/**
	 * Add a name to the name collection.
	 * @param name_elt Element to add.
	 */
	public void addName(net.ihe.gazelle.hl7v3.datatypes.EN name_elt) {
	    this.name.add(name_elt);
	}
	
	/**
	 * Remove a name to the name collection.
	 * @param name_elt Element to remove
	 */
	public void removeName(net.ihe.gazelle.hl7v3.datatypes.EN name_elt) {
	    this.name.remove(name_elt);
	}
	
	/**
	 * Return desc.
	 * @return desc
	 */
	public net.ihe.gazelle.hl7v3.datatypes.ED getDesc() {
	    return desc;
	}
	
	/**
	 * Set a value to attribute desc.
	 * @param desc.
	 */
	public void setDesc(net.ihe.gazelle.hl7v3.datatypes.ED desc) {
	    this.desc = desc;
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
	 * Return existenceTime.
	 * @return existenceTime
	 */
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS getExistenceTime() {
	    return existenceTime;
	}
	
	/**
	 * Set a value to attribute existenceTime.
	 * @param existenceTime.
	 */
	public void setExistenceTime(net.ihe.gazelle.hl7v3.datatypes.IVLTS existenceTime) {
	    this.existenceTime = existenceTime;
	}
	
	
	
	
	/**
	 * Return riskCode.
	 * @return riskCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getRiskCode() {
	    return riskCode;
	}
	
	/**
	 * Set a value to attribute riskCode.
	 * @param riskCode.
	 */
	public void setRiskCode(net.ihe.gazelle.hl7v3.datatypes.CE riskCode) {
	    this.riskCode = riskCode;
	}
	
	
	
	
	/**
	 * Return handlingCode.
	 * @return handlingCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getHandlingCode() {
	    return handlingCode;
	}
	
	/**
	 * Set a value to attribute handlingCode.
	 * @param handlingCode.
	 */
	public void setHandlingCode(net.ihe.gazelle.hl7v3.datatypes.CE handlingCode) {
	    this.handlingCode = handlingCode;
	}
	
	
	
	
	/**
	 * Return administrativeGenderCode.
	 * @return administrativeGenderCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getAdministrativeGenderCode() {
	    return administrativeGenderCode;
	}
	
	/**
	 * Set a value to attribute administrativeGenderCode.
	 * @param administrativeGenderCode.
	 */
	public void setAdministrativeGenderCode(net.ihe.gazelle.hl7v3.datatypes.CE administrativeGenderCode) {
	    this.administrativeGenderCode = administrativeGenderCode;
	}
	
	
	
	
	/**
	 * Return birthTime.
	 * @return birthTime
	 */
	public net.ihe.gazelle.hl7v3.datatypes.TS getBirthTime() {
	    return birthTime;
	}
	
	/**
	 * Set a value to attribute birthTime.
	 * @param birthTime.
	 */
	public void setBirthTime(net.ihe.gazelle.hl7v3.datatypes.TS birthTime) {
	    this.birthTime = birthTime;
	}
	
	
	
	
	/**
	 * Return deceasedInd.
	 * @return deceasedInd
	 */
	public net.ihe.gazelle.hl7v3.datatypes.BL getDeceasedInd() {
	    return deceasedInd;
	}
	
	/**
	 * Set a value to attribute deceasedInd.
	 * @param deceasedInd.
	 */
	public void setDeceasedInd(net.ihe.gazelle.hl7v3.datatypes.BL deceasedInd) {
	    this.deceasedInd = deceasedInd;
	}
	
	
	
	
	/**
	 * Return multipleBirthInd.
	 * @return multipleBirthInd
	 */
	public net.ihe.gazelle.hl7v3.datatypes.BL getMultipleBirthInd() {
	    return multipleBirthInd;
	}
	
	/**
	 * Set a value to attribute multipleBirthInd.
	 * @param multipleBirthInd.
	 */
	public void setMultipleBirthInd(net.ihe.gazelle.hl7v3.datatypes.BL multipleBirthInd) {
	    this.multipleBirthInd = multipleBirthInd;
	}
	
	
	
	
	/**
	 * Return multipleBirthOrderNumber.
	 * @return multipleBirthOrderNumber
	 */
	public net.ihe.gazelle.hl7v3.datatypes.INT getMultipleBirthOrderNumber() {
	    return multipleBirthOrderNumber;
	}
	
	/**
	 * Set a value to attribute multipleBirthOrderNumber.
	 * @param multipleBirthOrderNumber.
	 */
	public void setMultipleBirthOrderNumber(net.ihe.gazelle.hl7v3.datatypes.INT multipleBirthOrderNumber) {
	    this.multipleBirthOrderNumber = multipleBirthOrderNumber;
	}
	
	
	
	
	/**
	 * Return organDonorInd.
	 * @return organDonorInd
	 */
	public net.ihe.gazelle.hl7v3.datatypes.BL getOrganDonorInd() {
	    return organDonorInd;
	}
	
	/**
	 * Set a value to attribute organDonorInd.
	 * @param organDonorInd.
	 */
	public void setOrganDonorInd(net.ihe.gazelle.hl7v3.datatypes.BL organDonorInd) {
	    this.organDonorInd = organDonorInd;
	}
	
	
	
	
	/**
	 * Return strainText.
	 * @return strainText
	 */
	public net.ihe.gazelle.hl7v3.datatypes.ED getStrainText() {
	    return strainText;
	}
	
	/**
	 * Set a value to attribute strainText.
	 * @param strainText.
	 */
	public void setStrainText(net.ihe.gazelle.hl7v3.datatypes.ED strainText) {
	    this.strainText = strainText;
	}
	
	
	
	
	/**
	 * Return genderStatusCode.
	 * @return genderStatusCode
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getGenderStatusCode() {
	    return genderStatusCode;
	}
	
	/**
	 * Set a value to attribute genderStatusCode.
	 * @param genderStatusCode.
	 */
	public void setGenderStatusCode(net.ihe.gazelle.hl7v3.datatypes.CE genderStatusCode) {
	    this.genderStatusCode = genderStatusCode;
	}
	
	
	
	
	/**
	 * Return asMember.
	 * @return asMember
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Member> getAsMember() {
		if (asMember == null) {
	        asMember = new ArrayList<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Member>();
	    }
	    return asMember;
	}
	
	/**
	 * Set a value to attribute asMember.
	 * @param asMember.
	 */
	public void setAsMember(List<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Member> asMember) {
	    this.asMember = asMember;
	}
	
	
	
	/**
	 * Add a asMember to the asMember collection.
	 * @param asMember_elt Element to add.
	 */
	public void addAsMember(net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Member asMember_elt) {
	    this.asMember.add(asMember_elt);
	}
	
	/**
	 * Remove a asMember to the asMember collection.
	 * @param asMember_elt Element to remove
	 */
	public void removeAsMember(net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Member asMember_elt) {
	    this.asMember.remove(asMember_elt);
	}
	
	/**
	 * Return asOtherIDs.
	 * @return asOtherIDs
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04OtherIDs> getAsOtherIDs() {
		if (asOtherIDs == null) {
	        asOtherIDs = new ArrayList<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04OtherIDs>();
	    }
	    return asOtherIDs;
	}
	
	/**
	 * Set a value to attribute asOtherIDs.
	 * @param asOtherIDs.
	 */
	public void setAsOtherIDs(List<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04OtherIDs> asOtherIDs) {
	    this.asOtherIDs = asOtherIDs;
	}
	
	
	
	/**
	 * Add a asOtherIDs to the asOtherIDs collection.
	 * @param asOtherIDs_elt Element to add.
	 */
	public void addAsOtherIDs(net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04OtherIDs asOtherIDs_elt) {
	    this.asOtherIDs.add(asOtherIDs_elt);
	}
	
	/**
	 * Remove a asOtherIDs to the asOtherIDs collection.
	 * @param asOtherIDs_elt Element to remove
	 */
	public void removeAsOtherIDs(net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04OtherIDs asOtherIDs_elt) {
	    this.asOtherIDs.remove(asOtherIDs_elt);
	}
	
	/**
	 * Return asCoveredParty.
	 * @return asCoveredParty
	 */
	public net.ihe.gazelle.hl7v3.coctmt500000UV04.COCTMT500000UV04CoveredParty getAsCoveredParty() {
	    return asCoveredParty;
	}
	
	/**
	 * Set a value to attribute asCoveredParty.
	 * @param asCoveredParty.
	 */
	public void setAsCoveredParty(net.ihe.gazelle.hl7v3.coctmt500000UV04.COCTMT500000UV04CoveredParty asCoveredParty) {
	    this.asCoveredParty = asCoveredParty;
	}
	
	
	
	
	/**
	 * Return contactParty.
	 * @return contactParty
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04ContactParty> getContactParty() {
		if (contactParty == null) {
	        contactParty = new ArrayList<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04ContactParty>();
	    }
	    return contactParty;
	}
	
	/**
	 * Set a value to attribute contactParty.
	 * @param contactParty.
	 */
	public void setContactParty(List<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04ContactParty> contactParty) {
	    this.contactParty = contactParty;
	}
	
	
	
	/**
	 * Add a contactParty to the contactParty collection.
	 * @param contactParty_elt Element to add.
	 */
	public void addContactParty(net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04ContactParty contactParty_elt) {
	    this.contactParty.add(contactParty_elt);
	}
	
	/**
	 * Remove a contactParty to the contactParty collection.
	 * @param contactParty_elt Element to remove
	 */
	public void removeContactParty(net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04ContactParty contactParty_elt) {
	    this.contactParty.remove(contactParty_elt);
	}
	
	/**
	 * Return guardian.
	 * @return guardian
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Guardian> getGuardian() {
		if (guardian == null) {
	        guardian = new ArrayList<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Guardian>();
	    }
	    return guardian;
	}
	
	/**
	 * Set a value to attribute guardian.
	 * @param guardian.
	 */
	public void setGuardian(List<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Guardian> guardian) {
	    this.guardian = guardian;
	}
	
	
	
	/**
	 * Add a guardian to the guardian collection.
	 * @param guardian_elt Element to add.
	 */
	public void addGuardian(net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Guardian guardian_elt) {
	    this.guardian.add(guardian_elt);
	}
	
	/**
	 * Remove a guardian to the guardian collection.
	 * @param guardian_elt Element to remove
	 */
	public void removeGuardian(net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Guardian guardian_elt) {
	    this.guardian.remove(guardian_elt);
	}
	
	/**
	 * Return guarantor.
	 * @return guarantor
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Guarantor> getGuarantor() {
		if (guarantor == null) {
	        guarantor = new ArrayList<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Guarantor>();
	    }
	    return guarantor;
	}
	
	/**
	 * Set a value to attribute guarantor.
	 * @param guarantor.
	 */
	public void setGuarantor(List<net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Guarantor> guarantor) {
	    this.guarantor = guarantor;
	}
	
	
	
	/**
	 * Add a guarantor to the guarantor collection.
	 * @param guarantor_elt Element to add.
	 */
	public void addGuarantor(net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Guarantor guarantor_elt) {
	    this.guarantor.add(guarantor_elt);
	}
	
	/**
	 * Remove a guarantor to the guarantor collection.
	 * @param guarantor_elt Element to remove
	 */
	public void removeGuarantor(net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Guarantor guarantor_elt) {
	    this.guarantor.remove(guarantor_elt);
	}
	
	/**
	 * Return birthPlace.
	 * @return birthPlace
	 */
	public net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04BirthPlace getBirthPlace() {
	    return birthPlace;
	}
	
	/**
	 * Set a value to attribute birthPlace.
	 * @param birthPlace.
	 */
	public void setBirthPlace(net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04BirthPlace birthPlace) {
	    this.birthPlace = birthPlace;
	}
	
	
	
	
	/**
	 * Return classCode.
	 * @return classCode
	 */
	public net.ihe.gazelle.hl7v3.voc.EntityClassNonPersonLivingSubject getClassCode() {
	    return classCode;
	}
	
	/**
	 * Set a value to attribute classCode.
	 * @param classCode.
	 */
	public void setClassCode(net.ihe.gazelle.hl7v3.voc.EntityClassNonPersonLivingSubject classCode) {
	    this.classCode = classCode;
	}
	
	
	
	
	/**
	 * Return determinerCode.
	 * @return determinerCode
	 */
	public net.ihe.gazelle.hl7v3.voc.XDeterminerInstanceKind getDeterminerCode() {
	    return determinerCode;
	}
	
	/**
	 * Set a value to attribute determinerCode.
	 * @param determinerCode.
	 */
	public void setDeterminerCode(net.ihe.gazelle.hl7v3.voc.XDeterminerInstanceKind determinerCode) {
	    this.determinerCode = determinerCode;
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.coctmt030000UV04");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT030000UV04.NonPersonLivingSubject").item(0);
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
   public static void validateByModule(COCTMT030000UV04NonPersonLivingSubject cOCTMT030000UV04NonPersonLivingSubject, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT030000UV04NonPersonLivingSubject != null){
   			cvm.validate(cOCTMT030000UV04NonPersonLivingSubject, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT030000UV04NonPersonLivingSubject.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT030000UV04NonPersonLivingSubject.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II id: cOCTMT030000UV04NonPersonLivingSubject.getId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(id, _location + "/id[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.INT.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getQuantity(), _location + "/quantity", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.EN name: cOCTMT030000UV04NonPersonLivingSubject.getName()){
					net.ihe.gazelle.hl7v3.datatypes.EN.validateByModule(name, _location + "/name[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.ED.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getDesc(), _location + "/desc", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getStatusCode(), _location + "/statusCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.IVLTS.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getExistenceTime(), _location + "/existenceTime", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getRiskCode(), _location + "/riskCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getHandlingCode(), _location + "/handlingCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getAdministrativeGenderCode(), _location + "/administrativeGenderCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.TS.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getBirthTime(), _location + "/birthTime", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.BL.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getDeceasedInd(), _location + "/deceasedInd", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.BL.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getMultipleBirthInd(), _location + "/multipleBirthInd", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.INT.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getMultipleBirthOrderNumber(), _location + "/multipleBirthOrderNumber", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.BL.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getOrganDonorInd(), _location + "/organDonorInd", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.ED.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getStrainText(), _location + "/strainText", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getGenderStatusCode(), _location + "/genderStatusCode", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Member asMember: cOCTMT030000UV04NonPersonLivingSubject.getAsMember()){
					net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Member.validateByModule(asMember, _location + "/asMember[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04OtherIDs asOtherIDs: cOCTMT030000UV04NonPersonLivingSubject.getAsOtherIDs()){
					net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04OtherIDs.validateByModule(asOtherIDs, _location + "/asOtherIDs[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.coctmt500000UV04.COCTMT500000UV04CoveredParty.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getAsCoveredParty(), _location + "/asCoveredParty", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04ContactParty contactParty: cOCTMT030000UV04NonPersonLivingSubject.getContactParty()){
					net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04ContactParty.validateByModule(contactParty, _location + "/contactParty[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Guardian guardian: cOCTMT030000UV04NonPersonLivingSubject.getGuardian()){
					net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Guardian.validateByModule(guardian, _location + "/guardian[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Guarantor guarantor: cOCTMT030000UV04NonPersonLivingSubject.getGuarantor()){
					net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04Guarantor.validateByModule(guarantor, _location + "/guarantor[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.coctmt030000UV04.COCTMT030000UV04BirthPlace.validateByModule(cOCTMT030000UV04NonPersonLivingSubject.getBirthPlace(), _location + "/birthPlace", cvm, diagnostic);
    	}
    }

}