/**
 * COCTMT080000UVPerson.java
 * <p>
 * File generated from the coctmt080000UV::COCTMT080000UVPerson uml Class
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.coctmt080000UV;

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
 * Description of the class COCTMT080000UVPerson.
 *
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "COCT_MT080000UV.Person", propOrder = {
	"realmCode",
	"typeId",
	"templateId",
	"code",
	"quantity",
	"name",
	"desc",
	"statusCode",
	"existenceTime",
	"telecom",
	"riskCode",
	"handlingCode",
	"administrativeGenderCode",
	"birthTime",
	"deceasedInd",
	"deceasedTime",
	"multipleBirthInd",
	"multipleBirthOrderNumber",
	"organDonorInd",
	"addr",
	"disabilityCode",
	"raceCode",
	"ethnicGroupCode",
	"asSpecimenAlternateIdentifier",
	"asSpecimenStub",
	"asContent",
	"additive",
	"classCode",
	"determinerCode",
	"nullFlavor"
})
@XmlRootElement(name = "COCT_MT080000UV.Person")
public class COCTMT080000UVPerson implements java.io.Serializable {
	
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
	@XmlElement(name = "code", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CE code;
	@XmlElement(name = "quantity", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.PQ> quantity;
	@XmlElement(name = "name", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.EN> name;
	@XmlElement(name = "desc", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.ED desc;
	@XmlElement(name = "statusCode", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.CS statusCode;
	@XmlElement(name = "existenceTime", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.IVLTS existenceTime;
	@XmlElement(name = "telecom", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.TEL> telecom;
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
	@XmlElement(name = "deceasedTime", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.TS deceasedTime;
	@XmlElement(name = "multipleBirthInd", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.BL multipleBirthInd;
	@XmlElement(name = "multipleBirthOrderNumber", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.INT multipleBirthOrderNumber;
	@XmlElement(name = "organDonorInd", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.datatypes.BL organDonorInd;
	@XmlElement(name = "addr", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.AD> addr;
	@XmlElement(name = "disabilityCode", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.CE> disabilityCode;
	@XmlElement(name = "raceCode", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.CE> raceCode;
	@XmlElement(name = "ethnicGroupCode", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.datatypes.CE> ethnicGroupCode;
	@XmlElement(name = "asSpecimenAlternateIdentifier", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVSpecimenAlternateIdentifier> asSpecimenAlternateIdentifier;
	@XmlElement(name = "asSpecimenStub", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVSpecimenStub> asSpecimenStub;
	@XmlElement(name = "asContent", namespace = "urn:hl7-org:v3")
	public net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVContent1 asContent;
	@XmlElement(name = "additive", namespace = "urn:hl7-org:v3")
	public List<net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVAdditive> additive;
	@XmlAttribute(name = "classCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.EntityClass classCode;
	@XmlAttribute(name = "determinerCode", required = true)
	public net.ihe.gazelle.hl7v3.voc.EntityDeterminer determinerCode;
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
	 * Return code.
	 * @return code
	 */
	public net.ihe.gazelle.hl7v3.datatypes.CE getCode() {
	    return code;
	}
	
	/**
	 * Set a value to attribute code.
     */
	public void setCode(net.ihe.gazelle.hl7v3.datatypes.CE code) {
	    this.code = code;
	}
	
	
	
	
	/**
	 * Return quantity.
	 * @return quantity
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.PQ> getQuantity() {
		if (quantity == null) {
	        quantity = new ArrayList<>();
	    }
	    return quantity;
	}
	
	/**
	 * Set a value to attribute quantity.
     */
	public void setQuantity(List<net.ihe.gazelle.hl7v3.datatypes.PQ> quantity) {
	    this.quantity = quantity;
	}
	
	
	
	/**
	 * Add a quantity to the quantity collection.
	 * @param quantity_elt Element to add.
	 */
	public void addQuantity(net.ihe.gazelle.hl7v3.datatypes.PQ quantity_elt) {
	    this.quantity.add(quantity_elt);
	}
	
	/**
	 * Remove a quantity to the quantity collection.
	 * @param quantity_elt Element to remove
	 */
	public void removeQuantity(net.ihe.gazelle.hl7v3.datatypes.PQ quantity_elt) {
	    this.quantity.remove(quantity_elt);
	}
	
	/**
	 * Return name.
	 * @return name
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.EN> getName() {
		if (name == null) {
	        name = new ArrayList<>();
	    }
	    return name;
	}
	
	/**
	 * Set a value to attribute name.
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
     */
	public void setExistenceTime(net.ihe.gazelle.hl7v3.datatypes.IVLTS existenceTime) {
	    this.existenceTime = existenceTime;
	}
	
	
	
	
	/**
	 * Return telecom.
	 * @return telecom
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.TEL> getTelecom() {
		if (telecom == null) {
	        telecom = new ArrayList<>();
	    }
	    return telecom;
	}
	
	/**
	 * Set a value to attribute telecom.
     */
	public void setTelecom(List<net.ihe.gazelle.hl7v3.datatypes.TEL> telecom) {
	    this.telecom = telecom;
	}
	
	
	
	/**
	 * Add a telecom to the telecom collection.
	 * @param telecom_elt Element to add.
	 */
	public void addTelecom(net.ihe.gazelle.hl7v3.datatypes.TEL telecom_elt) {
	    this.telecom.add(telecom_elt);
	}
	
	/**
	 * Remove a telecom to the telecom collection.
	 * @param telecom_elt Element to remove
	 */
	public void removeTelecom(net.ihe.gazelle.hl7v3.datatypes.TEL telecom_elt) {
	    this.telecom.remove(telecom_elt);
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
     */
	public void setDeceasedInd(net.ihe.gazelle.hl7v3.datatypes.BL deceasedInd) {
	    this.deceasedInd = deceasedInd;
	}
	
	
	
	
	/**
	 * Return deceasedTime.
	 * @return deceasedTime
	 */
	public net.ihe.gazelle.hl7v3.datatypes.TS getDeceasedTime() {
	    return deceasedTime;
	}
	
	/**
	 * Set a value to attribute deceasedTime.
     */
	public void setDeceasedTime(net.ihe.gazelle.hl7v3.datatypes.TS deceasedTime) {
	    this.deceasedTime = deceasedTime;
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
     */
	public void setOrganDonorInd(net.ihe.gazelle.hl7v3.datatypes.BL organDonorInd) {
	    this.organDonorInd = organDonorInd;
	}
	
	
	
	
	/**
	 * Return addr.
	 * @return addr
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.AD> getAddr() {
		if (addr == null) {
	        addr = new ArrayList<>();
	    }
	    return addr;
	}
	
	/**
	 * Set a value to attribute addr.
     */
	public void setAddr(List<net.ihe.gazelle.hl7v3.datatypes.AD> addr) {
	    this.addr = addr;
	}
	
	
	
	/**
	 * Add a addr to the addr collection.
	 * @param addr_elt Element to add.
	 */
	public void addAddr(net.ihe.gazelle.hl7v3.datatypes.AD addr_elt) {
	    this.addr.add(addr_elt);
	}
	
	/**
	 * Remove a addr to the addr collection.
	 * @param addr_elt Element to remove
	 */
	public void removeAddr(net.ihe.gazelle.hl7v3.datatypes.AD addr_elt) {
	    this.addr.remove(addr_elt);
	}
	
	/**
	 * Return disabilityCode.
	 * @return disabilityCode
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.CE> getDisabilityCode() {
		if (disabilityCode == null) {
	        disabilityCode = new ArrayList<>();
	    }
	    return disabilityCode;
	}
	
	/**
	 * Set a value to attribute disabilityCode.
     */
	public void setDisabilityCode(List<net.ihe.gazelle.hl7v3.datatypes.CE> disabilityCode) {
	    this.disabilityCode = disabilityCode;
	}
	
	
	
	/**
	 * Add a disabilityCode to the disabilityCode collection.
	 * @param disabilityCode_elt Element to add.
	 */
	public void addDisabilityCode(net.ihe.gazelle.hl7v3.datatypes.CE disabilityCode_elt) {
	    this.disabilityCode.add(disabilityCode_elt);
	}
	
	/**
	 * Remove a disabilityCode to the disabilityCode collection.
	 * @param disabilityCode_elt Element to remove
	 */
	public void removeDisabilityCode(net.ihe.gazelle.hl7v3.datatypes.CE disabilityCode_elt) {
	    this.disabilityCode.remove(disabilityCode_elt);
	}
	
	/**
	 * Return raceCode.
	 * @return raceCode
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.CE> getRaceCode() {
		if (raceCode == null) {
	        raceCode = new ArrayList<>();
	    }
	    return raceCode;
	}
	
	/**
	 * Set a value to attribute raceCode.
     */
	public void setRaceCode(List<net.ihe.gazelle.hl7v3.datatypes.CE> raceCode) {
	    this.raceCode = raceCode;
	}
	
	
	
	/**
	 * Add a raceCode to the raceCode collection.
	 * @param raceCode_elt Element to add.
	 */
	public void addRaceCode(net.ihe.gazelle.hl7v3.datatypes.CE raceCode_elt) {
	    this.raceCode.add(raceCode_elt);
	}
	
	/**
	 * Remove a raceCode to the raceCode collection.
	 * @param raceCode_elt Element to remove
	 */
	public void removeRaceCode(net.ihe.gazelle.hl7v3.datatypes.CE raceCode_elt) {
	    this.raceCode.remove(raceCode_elt);
	}
	
	/**
	 * Return ethnicGroupCode.
	 * @return ethnicGroupCode
	 */
	public List<net.ihe.gazelle.hl7v3.datatypes.CE> getEthnicGroupCode() {
		if (ethnicGroupCode == null) {
	        ethnicGroupCode = new ArrayList<>();
	    }
	    return ethnicGroupCode;
	}
	
	/**
	 * Set a value to attribute ethnicGroupCode.
     */
	public void setEthnicGroupCode(List<net.ihe.gazelle.hl7v3.datatypes.CE> ethnicGroupCode) {
	    this.ethnicGroupCode = ethnicGroupCode;
	}
	
	
	
	/**
	 * Add a ethnicGroupCode to the ethnicGroupCode collection.
	 * @param ethnicGroupCode_elt Element to add.
	 */
	public void addEthnicGroupCode(net.ihe.gazelle.hl7v3.datatypes.CE ethnicGroupCode_elt) {
	    this.ethnicGroupCode.add(ethnicGroupCode_elt);
	}
	
	/**
	 * Remove a ethnicGroupCode to the ethnicGroupCode collection.
	 * @param ethnicGroupCode_elt Element to remove
	 */
	public void removeEthnicGroupCode(net.ihe.gazelle.hl7v3.datatypes.CE ethnicGroupCode_elt) {
	    this.ethnicGroupCode.remove(ethnicGroupCode_elt);
	}
	
	/**
	 * Return asSpecimenAlternateIdentifier.
	 * @return asSpecimenAlternateIdentifier
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVSpecimenAlternateIdentifier> getAsSpecimenAlternateIdentifier() {
		if (asSpecimenAlternateIdentifier == null) {
	        asSpecimenAlternateIdentifier = new ArrayList<>();
	    }
	    return asSpecimenAlternateIdentifier;
	}
	
	/**
	 * Set a value to attribute asSpecimenAlternateIdentifier.
     */
	public void setAsSpecimenAlternateIdentifier(List<net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVSpecimenAlternateIdentifier> asSpecimenAlternateIdentifier) {
	    this.asSpecimenAlternateIdentifier = asSpecimenAlternateIdentifier;
	}
	
	
	
	/**
	 * Add a asSpecimenAlternateIdentifier to the asSpecimenAlternateIdentifier collection.
	 * @param asSpecimenAlternateIdentifier_elt Element to add.
	 */
	public void addAsSpecimenAlternateIdentifier(net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVSpecimenAlternateIdentifier asSpecimenAlternateIdentifier_elt) {
	    this.asSpecimenAlternateIdentifier.add(asSpecimenAlternateIdentifier_elt);
	}
	
	/**
	 * Remove a asSpecimenAlternateIdentifier to the asSpecimenAlternateIdentifier collection.
	 * @param asSpecimenAlternateIdentifier_elt Element to remove
	 */
	public void removeAsSpecimenAlternateIdentifier(net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVSpecimenAlternateIdentifier asSpecimenAlternateIdentifier_elt) {
	    this.asSpecimenAlternateIdentifier.remove(asSpecimenAlternateIdentifier_elt);
	}
	
	/**
	 * Return asSpecimenStub.
	 * @return asSpecimenStub
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVSpecimenStub> getAsSpecimenStub() {
		if (asSpecimenStub == null) {
	        asSpecimenStub = new ArrayList<>();
	    }
	    return asSpecimenStub;
	}
	
	/**
	 * Set a value to attribute asSpecimenStub.
     */
	public void setAsSpecimenStub(List<net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVSpecimenStub> asSpecimenStub) {
	    this.asSpecimenStub = asSpecimenStub;
	}
	
	
	
	/**
	 * Add a asSpecimenStub to the asSpecimenStub collection.
	 * @param asSpecimenStub_elt Element to add.
	 */
	public void addAsSpecimenStub(net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVSpecimenStub asSpecimenStub_elt) {
	    this.asSpecimenStub.add(asSpecimenStub_elt);
	}
	
	/**
	 * Remove a asSpecimenStub to the asSpecimenStub collection.
	 * @param asSpecimenStub_elt Element to remove
	 */
	public void removeAsSpecimenStub(net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVSpecimenStub asSpecimenStub_elt) {
	    this.asSpecimenStub.remove(asSpecimenStub_elt);
	}
	
	/**
	 * Return asContent.
	 * @return asContent
	 */
	public net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVContent1 getAsContent() {
	    return asContent;
	}
	
	/**
	 * Set a value to attribute asContent.
     */
	public void setAsContent(net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVContent1 asContent) {
	    this.asContent = asContent;
	}
	
	
	
	
	/**
	 * Return additive.
	 * @return additive
	 */
	public List<net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVAdditive> getAdditive() {
		if (additive == null) {
	        additive = new ArrayList<>();
	    }
	    return additive;
	}
	
	/**
	 * Set a value to attribute additive.
     */
	public void setAdditive(List<net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVAdditive> additive) {
	    this.additive = additive;
	}
	
	
	
	/**
	 * Add a additive to the additive collection.
	 * @param additive_elt Element to add.
	 */
	public void addAdditive(net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVAdditive additive_elt) {
	    this.additive.add(additive_elt);
	}
	
	/**
	 * Remove a additive to the additive collection.
	 * @param additive_elt Element to remove
	 */
	public void removeAdditive(net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVAdditive additive_elt) {
	    this.additive.remove(additive_elt);
	}
	
	/**
	 * Return classCode.
	 * @return classCode
	 */
	public net.ihe.gazelle.hl7v3.voc.EntityClass getClassCode() {
	    return classCode;
	}
	
	/**
	 * Set a value to attribute classCode.
     */
	public void setClassCode(net.ihe.gazelle.hl7v3.voc.EntityClass classCode) {
	    this.classCode = classCode;
	}
	
	
	
	
	/**
	 * Return determinerCode.
	 * @return determinerCode
	 */
	public net.ihe.gazelle.hl7v3.voc.EntityDeterminer getDeterminerCode() {
	    return determinerCode;
	}
	
	/**
	 * Set a value to attribute determinerCode.
     */
	public void setDeterminerCode(net.ihe.gazelle.hl7v3.voc.EntityDeterminer determinerCode) {
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
					jc = JAXBContext.newInstance("net.ihe.gazelle.hl7v3.coctmt080000UV");
					Marshaller m = jc.createMarshaller();
					m.marshal(this, doc);
					_xmlNodePresentation = doc.getElementsByTagNameNS("urn:hl7-org:v3", "COCT_MT080000UV.Person").item(0);
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
   public static void validateByModule(COCTMT080000UVPerson cOCTMT080000UVPerson, String _location, ConstraintValidatorModule cvm, List<net.ihe.gazelle.validation.Notification> diagnostic){
   		if (cOCTMT080000UVPerson != null){
   			cvm.validate(cOCTMT080000UVPerson, _location, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CS realmCode: cOCTMT080000UVPerson.getRealmCode()){
					net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(realmCode, _location + "/realmCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(cOCTMT080000UVPerson.getTypeId(), _location + "/typeId", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.II templateId: cOCTMT080000UVPerson.getTemplateId()){
					net.ihe.gazelle.hl7v3.datatypes.II.validateByModule(templateId, _location + "/templateId[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT080000UVPerson.getCode(), _location + "/code", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.PQ quantity: cOCTMT080000UVPerson.getQuantity()){
					net.ihe.gazelle.hl7v3.datatypes.PQ.validateByModule(quantity, _location + "/quantity[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.EN name: cOCTMT080000UVPerson.getName()){
					net.ihe.gazelle.hl7v3.datatypes.EN.validateByModule(name, _location + "/name[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.ED.validateByModule(cOCTMT080000UVPerson.getDesc(), _location + "/desc", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CS.validateByModule(cOCTMT080000UVPerson.getStatusCode(), _location + "/statusCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.IVLTS.validateByModule(cOCTMT080000UVPerson.getExistenceTime(), _location + "/existenceTime", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.TEL telecom: cOCTMT080000UVPerson.getTelecom()){
					net.ihe.gazelle.hl7v3.datatypes.TEL.validateByModule(telecom, _location + "/telecom[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT080000UVPerson.getRiskCode(), _location + "/riskCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT080000UVPerson.getHandlingCode(), _location + "/handlingCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(cOCTMT080000UVPerson.getAdministrativeGenderCode(), _location + "/administrativeGenderCode", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.TS.validateByModule(cOCTMT080000UVPerson.getBirthTime(), _location + "/birthTime", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.BL.validateByModule(cOCTMT080000UVPerson.getDeceasedInd(), _location + "/deceasedInd", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.TS.validateByModule(cOCTMT080000UVPerson.getDeceasedTime(), _location + "/deceasedTime", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.BL.validateByModule(cOCTMT080000UVPerson.getMultipleBirthInd(), _location + "/multipleBirthInd", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.INT.validateByModule(cOCTMT080000UVPerson.getMultipleBirthOrderNumber(), _location + "/multipleBirthOrderNumber", cvm, diagnostic);
			net.ihe.gazelle.hl7v3.datatypes.BL.validateByModule(cOCTMT080000UVPerson.getOrganDonorInd(), _location + "/organDonorInd", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.AD addr: cOCTMT080000UVPerson.getAddr()){
					net.ihe.gazelle.hl7v3.datatypes.AD.validateByModule(addr, _location + "/addr[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CE disabilityCode: cOCTMT080000UVPerson.getDisabilityCode()){
					net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(disabilityCode, _location + "/disabilityCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CE raceCode: cOCTMT080000UVPerson.getRaceCode()){
					net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(raceCode, _location + "/raceCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.datatypes.CE ethnicGroupCode: cOCTMT080000UVPerson.getEthnicGroupCode()){
					net.ihe.gazelle.hl7v3.datatypes.CE.validateByModule(ethnicGroupCode, _location + "/ethnicGroupCode[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVSpecimenAlternateIdentifier asSpecimenAlternateIdentifier: cOCTMT080000UVPerson.getAsSpecimenAlternateIdentifier()){
					net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVSpecimenAlternateIdentifier.validateByModule(asSpecimenAlternateIdentifier, _location + "/asSpecimenAlternateIdentifier[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVSpecimenStub asSpecimenStub: cOCTMT080000UVPerson.getAsSpecimenStub()){
					net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVSpecimenStub.validateByModule(asSpecimenStub, _location + "/asSpecimenStub[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
			net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVContent1.validateByModule(cOCTMT080000UVPerson.getAsContent(), _location + "/asContent", cvm, diagnostic);
			{
				int i = 0;
				for (net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVAdditive additive: cOCTMT080000UVPerson.getAdditive()){
					net.ihe.gazelle.hl7v3.coctmt080000UV.COCTMT080000UVAdditive.validateByModule(additive, _location + "/additive[" + i + "]", cvm, diagnostic);
					i++;
				}
			}
			
    	}
    }

}