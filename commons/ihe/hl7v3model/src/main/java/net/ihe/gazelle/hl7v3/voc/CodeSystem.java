/**
 * CodeSystem.java
 *
 * File generated from the voc::CodeSystem uml Enumeration
 * Generated by IHE - europe, gazelle team
 */
package net.ihe.gazelle.hl7v3.voc;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
/**
 * Description of the enumeration CodeSystem.
 *
 */

@XmlType(name = "CodeSystem")
@XmlEnum
@XmlRootElement(name = "CodeSystem")
public enum CodeSystem {
	@XmlEnumValue("ABCcodes")
	ABCCODES("ABCcodes"),
	@XmlEnumValue("ACR")
	ACR("ACR"),
	@XmlEnumValue("ART")
	ART("ART"),
	@XmlEnumValue("AS4")
	AS4("AS4"),
	@XmlEnumValue("AS4E")
	AS4E("AS4E"),
	@XmlEnumValue("ATC")
	ATC("ATC"),
	@XmlEnumValue("AcknowledgementCondition")
	ACKNOWLEDGEMENTCONDITION("AcknowledgementCondition"),
	@XmlEnumValue("AcknowledgementDetailCode")
	ACKNOWLEDGEMENTDETAILCODE("AcknowledgementDetailCode"),
	@XmlEnumValue("AcknowledgementDetailType")
	ACKNOWLEDGEMENTDETAILTYPE("AcknowledgementDetailType"),
	@XmlEnumValue("AcknowledgementType")
	ACKNOWLEDGEMENTTYPE("AcknowledgementType"),
	@XmlEnumValue("ActClass")
	ACTCLASS("ActClass"),
	@XmlEnumValue("ActCode")
	ACTCODE("ActCode"),
	@XmlEnumValue("ActExposureLevelCode")
	ACTEXPOSURELEVELCODE("ActExposureLevelCode"),
	@XmlEnumValue("ActInvoiceElementModifier")
	ACTINVOICEELEMENTMODIFIER("ActInvoiceElementModifier"),
	@XmlEnumValue("ActMood")
	ACTMOOD("ActMood"),
	@XmlEnumValue("ActPriority")
	ACTPRIORITY("ActPriority"),
	@XmlEnumValue("ActReason")
	ACTREASON("ActReason"),
	@XmlEnumValue("ActRelationshipCheckpoint")
	ACTRELATIONSHIPCHECKPOINT("ActRelationshipCheckpoint"),
	@XmlEnumValue("ActRelationshipJoin")
	ACTRELATIONSHIPJOIN("ActRelationshipJoin"),
	@XmlEnumValue("ActRelationshipSplit")
	ACTRELATIONSHIPSPLIT("ActRelationshipSplit"),
	@XmlEnumValue("ActRelationshipSubset")
	ACTRELATIONSHIPSUBSET("ActRelationshipSubset"),
	@XmlEnumValue("ActRelationshipType")
	ACTRELATIONSHIPTYPE("ActRelationshipType"),
	@XmlEnumValue("ActSite")
	ACTSITE("ActSite"),
	@XmlEnumValue("ActStatus")
	ACTSTATUS("ActStatus"),
	@XmlEnumValue("ActUncertainty")
	ACTUNCERTAINTY("ActUncertainty"),
	@XmlEnumValue("AddressPartType")
	ADDRESSPARTTYPE("AddressPartType"),
	@XmlEnumValue("AdministrativeGender")
	ADMINISTRATIVEGENDER("AdministrativeGender"),
	@XmlEnumValue("AmericanIndianAlaskaNativeLanguages")
	AMERICANINDIANALASKANATIVELANGUAGES("AmericanIndianAlaskaNativeLanguages"),
	@XmlEnumValue("C4")
	C4("C4"),
	@XmlEnumValue("C5")
	C5("C5"),
	@XmlEnumValue("CAMNCVS")
	CAMNCVS("CAMNCVS"),
	@XmlEnumValue("CAS")
	CAS("CAS"),
	@XmlEnumValue("CCI")
	CCI("CCI"),
	@XmlEnumValue("CD2")
	CD2("CD2"),
	@XmlEnumValue("CDCA")
	CDCA("CDCA"),
	@XmlEnumValue("CDCM")
	CDCM("CDCM"),
	@XmlEnumValue("CDS")
	CDS("CDS"),
	@XmlEnumValue("CE")
	CE("CE"),
	@XmlEnumValue("CLP")
	CLP("CLP"),
	@XmlEnumValue("CSAID")
	CSAID("CSAID"),
	@XmlEnumValue("CST")
	CST("CST"),
	@XmlEnumValue("CVX")
	CVX("CVX"),
	@XmlEnumValue("Calendar")
	CALENDAR("Calendar"),
	@XmlEnumValue("CalendarCycle")
	CALENDARCYCLE("CalendarCycle"),
	@XmlEnumValue("CalendarType")
	CALENDARTYPE("CalendarType"),
	@XmlEnumValue("Charset")
	CHARSET("Charset"),
	@XmlEnumValue("CodeSystem")
	CODESYSTEM("CodeSystem"),
	@XmlEnumValue("CodeSystemType")
	CODESYSTEMTYPE("CodeSystemType"),
	@XmlEnumValue("CodingRationale")
	CODINGRATIONALE("CodingRationale"),
	@XmlEnumValue("CommunicationFunctionType")
	COMMUNICATIONFUNCTIONTYPE("CommunicationFunctionType"),
	@XmlEnumValue("CompressionAlgorithm")
	COMPRESSIONALGORITHM("CompressionAlgorithm"),
	@XmlEnumValue("ConceptCodeRelationship")
	CONCEPTCODERELATIONSHIP("ConceptCodeRelationship"),
	@XmlEnumValue("ConceptGenerality")
	CONCEPTGENERALITY("ConceptGenerality"),
	@XmlEnumValue("ConceptProperty")
	CONCEPTPROPERTY("ConceptProperty"),
	@XmlEnumValue("ConceptStatus")
	CONCEPTSTATUS("ConceptStatus"),
	@XmlEnumValue("Confidentiality")
	CONFIDENTIALITY("Confidentiality"),
	@XmlEnumValue("ContainerCap")
	CONTAINERCAP("ContainerCap"),
	@XmlEnumValue("ContainerSeparator")
	CONTAINERSEPARATOR("ContainerSeparator"),
	@XmlEnumValue("ContentProcessingMode")
	CONTENTPROCESSINGMODE("ContentProcessingMode"),
	@XmlEnumValue("ContextControl")
	CONTEXTCONTROL("ContextControl"),
	@XmlEnumValue("Currency")
	CURRENCY("Currency"),
	@XmlEnumValue("DCL")
	DCL("DCL"),
	@XmlEnumValue("DCM")
	DCM("DCM"),
	@XmlEnumValue("DQL")
	DQL("DQL"),
	@XmlEnumValue("DataType")
	DATATYPE("DataType"),
	@XmlEnumValue("Dentition")
	DENTITION("Dentition"),
	@XmlEnumValue("DeviceAlertLevel")
	DEVICEALERTLEVEL("DeviceAlertLevel"),
	@XmlEnumValue("DocumentCompletion")
	DOCUMENTCOMPLETION("DocumentCompletion"),
	@XmlEnumValue("DocumentStorage")
	DOCUMENTSTORAGE("DocumentStorage"),
	@XmlEnumValue("E")
	E("E"),
	@XmlEnumValue("E5")
	E5("E5"),
	@XmlEnumValue("E6")
	E6("E6"),
	@XmlEnumValue("E7")
	E7("E7"),
	@XmlEnumValue("ENZC")
	ENZC("ENZC"),
	@XmlEnumValue("EPSG-GeodeticParameterDataset")
	EPSGGEODETICPARAMETERDATASET("EPSG-GeodeticParameterDataset"),
	@XmlEnumValue("EditStatus")
	EDITSTATUS("EditStatus"),
	@XmlEnumValue("EducationLevel")
	EDUCATIONLEVEL("EducationLevel"),
	@XmlEnumValue("EmployeeJobClass")
	EMPLOYEEJOBCLASS("EmployeeJobClass"),
	@XmlEnumValue("EncounterAccident")
	ENCOUNTERACCIDENT("EncounterAccident"),
	@XmlEnumValue("EncounterAcuity")
	ENCOUNTERACUITY("EncounterAcuity"),
	@XmlEnumValue("EncounterAdmissionSource")
	ENCOUNTERADMISSIONSOURCE("EncounterAdmissionSource"),
	@XmlEnumValue("EncounterReferralSource")
	ENCOUNTERREFERRALSOURCE("EncounterReferralSource"),
	@XmlEnumValue("EncounterSpecialCourtesy")
	ENCOUNTERSPECIALCOURTESY("EncounterSpecialCourtesy"),
	@XmlEnumValue("EntityClass")
	ENTITYCLASS("EntityClass"),
	@XmlEnumValue("EntityCode")
	ENTITYCODE("EntityCode"),
	@XmlEnumValue("EntityDeterminer")
	ENTITYDETERMINER("EntityDeterminer"),
	@XmlEnumValue("EntityHandling")
	ENTITYHANDLING("EntityHandling"),
	@XmlEnumValue("EntityNamePartQualifier")
	ENTITYNAMEPARTQUALIFIER("EntityNamePartQualifier"),
	@XmlEnumValue("EntityNamePartType")
	ENTITYNAMEPARTTYPE("EntityNamePartType"),
	@XmlEnumValue("EntityNameUse")
	ENTITYNAMEUSE("EntityNameUse"),
	@XmlEnumValue("EntityRisk")
	ENTITYRISK("EntityRisk"),
	@XmlEnumValue("EntityStatus")
	ENTITYSTATUS("EntityStatus"),
	@XmlEnumValue("EquipmentAlertLevel")
	EQUIPMENTALERTLEVEL("EquipmentAlertLevel"),
	@XmlEnumValue("Ethnicity")
	ETHNICITY("Ethnicity"),
	@XmlEnumValue("ExposureMode")
	EXPOSUREMODE("ExposureMode"),
	@XmlEnumValue("FDDC")
	FDDC("FDDC"),
	@XmlEnumValue("FDDX")
	FDDX("FDDX"),
	@XmlEnumValue("FDK")
	FDK("FDK"),
	@XmlEnumValue("GTSAbbreviation")
	GTSABBREVIATION("GTSAbbreviation"),
	@XmlEnumValue("GenderStatus")
	GENDERSTATUS("GenderStatus"),
	@XmlEnumValue("HB")
	HB("HB"),
	@XmlEnumValue("HHC")
	HHC("HHC"),
	@XmlEnumValue("HI")
	HI("HI"),
	@XmlEnumValue("HL7CommitteeIDInRIM")
	HL7COMMITTEEIDINRIM("HL7CommitteeIDInRIM"),
	@XmlEnumValue("HL7ConformanceInclusion")
	HL7CONFORMANCEINCLUSION("HL7ConformanceInclusion"),
	@XmlEnumValue("HL7DefinedRoseProperty")
	HL7DEFINEDROSEPROPERTY("HL7DefinedRoseProperty"),
	@XmlEnumValue("HL7ITSVersionCode")
	HL7ITSVERSIONCODE("HL7ITSVersionCode"),
	@XmlEnumValue("HL7StandardVersionCode")
	HL7STANDARDVERSIONCODE("HL7StandardVersionCode"),
	@XmlEnumValue("HL7UpdateMode")
	HL7UPDATEMODE("HL7UpdateMode"),
	@XmlEnumValue("HPC")
	HPC("HPC"),
	@XmlEnumValue("HealthcareProviderTaxonomyHIPAA")
	HEALTHCAREPROVIDERTAXONOMYHIPAA("HealthcareProviderTaxonomyHIPAA"),
	@XmlEnumValue("HtmlLinkType")
	HTMLLINKTYPE("HtmlLinkType"),
	@XmlEnumValue("I10")
	I10("I10"),
	@XmlEnumValue("I10P")
	I10P("I10P"),
	@XmlEnumValue("I9")
	I9("I9"),
	@XmlEnumValue("I9C")
	I9C("I9C"),
	@XmlEnumValue("IBT")
	IBT("IBT"),
	@XmlEnumValue("IC2")
	IC2("IC2"),
	@XmlEnumValue("ICD-10-CA")
	ICD10CA("ICD-10-CA"),
	@XmlEnumValue("ICDO")
	ICDO("ICDO"),
	@XmlEnumValue("ICS")
	ICS("ICS"),
	@XmlEnumValue("ICSD")
	ICSD("ICSD"),
	@XmlEnumValue("IETF1766")
	IETF1766("IETF1766"),
	@XmlEnumValue("IETF3066")
	IETF3066("IETF3066"),
	@XmlEnumValue("ISO3166-1")
	ISO31661("ISO3166-1"),
	@XmlEnumValue("ISO3166-2")
	ISO31662("ISO3166-2"),
	@XmlEnumValue("ISO3166-3")
	ISO31663("ISO3166-3"),
	@XmlEnumValue("ISO4217")
	ISO4217("ISO4217"),
	@XmlEnumValue("IUPC")
	IUPC("IUPC"),
	@XmlEnumValue("IUPP")
	IUPP("IUPP"),
	@XmlEnumValue("IntegrityCheckAlgorithm")
	INTEGRITYCHECKALGORITHM("IntegrityCheckAlgorithm"),
	@XmlEnumValue("JC8")
	JC8("JC8"),
	@XmlEnumValue("LN")
	LN("LN"),
	@XmlEnumValue("LanguageAbilityMode")
	LANGUAGEABILITYMODE("LanguageAbilityMode"),
	@XmlEnumValue("LanguageAbilityProficiency")
	LANGUAGEABILITYPROFICIENCY("LanguageAbilityProficiency"),
	@XmlEnumValue("LivingArrangement")
	LIVINGARRANGEMENT("LivingArrangement"),
	@XmlEnumValue("LocalMarkupIgnore")
	LOCALMARKUPIGNORE("LocalMarkupIgnore"),
	@XmlEnumValue("LocalRemoteControlState")
	LOCALREMOTECONTROLSTATE("LocalRemoteControlState"),
	@XmlEnumValue("MDC")
	MDC("MDC"),
	@XmlEnumValue("MDDX")
	MDDX("MDDX"),
	@XmlEnumValue("MDFAttributeType")
	MDFATTRIBUTETYPE("MDFAttributeType"),
	@XmlEnumValue("MDFSubjectAreaPrefix")
	MDFSUBJECTAREAPREFIX("MDFSubjectAreaPrefix"),
	@XmlEnumValue("MEDC")
	MEDC("MEDC"),
	@XmlEnumValue("MEDCIN")
	MEDCIN("MEDCIN"),
	@XmlEnumValue("MEDR")
	MEDR("MEDR"),
	@XmlEnumValue("MEDX")
	MEDX("MEDX"),
	@XmlEnumValue("MGPI")
	MGPI("MGPI"),
	@XmlEnumValue("MIME")
	MIME("MIME"),
	@XmlEnumValue("MULTUM")
	MULTUM("MULTUM"),
	@XmlEnumValue("MVX")
	MVX("MVX"),
	@XmlEnumValue("ManagedParticipationStatus")
	MANAGEDPARTICIPATIONSTATUS("ManagedParticipationStatus"),
	@XmlEnumValue("MapRelationship")
	MAPRELATIONSHIP("MapRelationship"),
	@XmlEnumValue("MaritalStatus")
	MARITALSTATUS("MaritalStatus"),
	@XmlEnumValue("MaterialType")
	MATERIALTYPE("MaterialType"),
	@XmlEnumValue("MdfHmdMetSourceType")
	MDFHMDMETSOURCETYPE("MdfHmdMetSourceType"),
	@XmlEnumValue("MdfHmdRowType")
	MDFHMDROWTYPE("MdfHmdRowType"),
	@XmlEnumValue("MdfRmimRowType")
	MDFRMIMROWTYPE("MdfRmimRowType"),
	@XmlEnumValue("MediaType")
	MEDIATYPE("MediaType"),
	@XmlEnumValue("MessageCondition")
	MESSAGECONDITION("MessageCondition"),
	@XmlEnumValue("MessageWaitingPriority")
	MESSAGEWAITINGPRIORITY("MessageWaitingPriority"),
	@XmlEnumValue("ModifyIndicator")
	MODIFYINDICATOR("ModifyIndicator"),
	@XmlEnumValue("NAACCR")
	NAACCR("NAACCR"),
	@XmlEnumValue("NAICS")
	NAICS("NAICS"),
	@XmlEnumValue("NDA")
	NDA("NDA"),
	@XmlEnumValue("NDC")
	NDC("NDC"),
	@XmlEnumValue("NIC")
	NIC("NIC"),
	@XmlEnumValue("NMMDS")
	NMMDS("NMMDS"),
	@XmlEnumValue("NOC")
	NOC("NOC"),
	@XmlEnumValue("NUBC-UB92")
	NUBCUB92("NUBC-UB92"),
	@XmlEnumValue("NUCCProviderCodes")
	NUCCPROVIDERCODES("NUCCProviderCodes"),
	@XmlEnumValue("NullFlavor")
	NULLFLAVOR("NullFlavor"),
	@XmlEnumValue("OHA")
	OHA("OHA"),
	@XmlEnumValue("ObservationInterpretation")
	OBSERVATIONINTERPRETATION("ObservationInterpretation"),
	@XmlEnumValue("ObservationMethod")
	OBSERVATIONMETHOD("ObservationMethod"),
	@XmlEnumValue("ObservationValue")
	OBSERVATIONVALUE("ObservationValue"),
	@XmlEnumValue("OrderableDrugForm")
	ORDERABLEDRUGFORM("OrderableDrugForm"),
	@XmlEnumValue("OrganizationNameType")
	ORGANIZATIONNAMETYPE("OrganizationNameType"),
	@XmlEnumValue("PNDS")
	PNDS("PNDS"),
	@XmlEnumValue("POS")
	POS("POS"),
	@XmlEnumValue("ParameterizedDataType")
	PARAMETERIZEDDATATYPE("ParameterizedDataType"),
	@XmlEnumValue("ParticipationFunction")
	PARTICIPATIONFUNCTION("ParticipationFunction"),
	@XmlEnumValue("ParticipationMode")
	PARTICIPATIONMODE("ParticipationMode"),
	@XmlEnumValue("ParticipationSignature")
	PARTICIPATIONSIGNATURE("ParticipationSignature"),
	@XmlEnumValue("ParticipationType")
	PARTICIPATIONTYPE("ParticipationType"),
	@XmlEnumValue("PatientImportance")
	PATIENTIMPORTANCE("PatientImportance"),
	@XmlEnumValue("PaymentTerms")
	PAYMENTTERMS("PaymentTerms"),
	@XmlEnumValue("PeriodicIntervalOfTimeAbbreviation")
	PERIODICINTERVALOFTIMEABBREVIATION("PeriodicIntervalOfTimeAbbreviation"),
	@XmlEnumValue("PersonDisabilityType")
	PERSONDISABILITYTYPE("PersonDisabilityType"),
	@XmlEnumValue("PostalAddressUse")
	POSTALADDRESSUSE("PostalAddressUse"),
	@XmlEnumValue("ProbabilityDistributionType")
	PROBABILITYDISTRIBUTIONTYPE("ProbabilityDistributionType"),
	@XmlEnumValue("ProcedureMethod")
	PROCEDUREMETHOD("ProcedureMethod"),
	@XmlEnumValue("ProcessingID")
	PROCESSINGID("ProcessingID"),
	@XmlEnumValue("ProcessingMode")
	PROCESSINGMODE("ProcessingMode"),
	@XmlEnumValue("QueryParameterValue")
	QUERYPARAMETERVALUE("QueryParameterValue"),
	@XmlEnumValue("QueryPriority")
	QUERYPRIORITY("QueryPriority"),
	@XmlEnumValue("QueryQuantityUnit")
	QUERYQUANTITYUNIT("QueryQuantityUnit"),
	@XmlEnumValue("QueryRequestLimit")
	QUERYREQUESTLIMIT("QueryRequestLimit"),
	@XmlEnumValue("QueryResponse")
	QUERYRESPONSE("QueryResponse"),
	@XmlEnumValue("QueryStatusCode")
	QUERYSTATUSCODE("QueryStatusCode"),
	@XmlEnumValue("RC")
	RC("RC"),
	@XmlEnumValue("RCFB")
	RCFB("RCFB"),
	@XmlEnumValue("RCV2")
	RCV2("RCV2"),
	@XmlEnumValue("Race")
	RACE("Race"),
	@XmlEnumValue("RelationalOperator")
	RELATIONALOPERATOR("RelationalOperator"),
	@XmlEnumValue("RelationshipConjunction")
	RELATIONSHIPCONJUNCTION("RelationshipConjunction"),
	@XmlEnumValue("ReligiousAffiliation")
	RELIGIOUSAFFILIATION("ReligiousAffiliation"),
	@XmlEnumValue("ResponseLevel")
	RESPONSELEVEL("ResponseLevel"),
	@XmlEnumValue("ResponseModality")
	RESPONSEMODALITY("ResponseModality"),
	@XmlEnumValue("ResponseMode")
	RESPONSEMODE("ResponseMode"),
	@XmlEnumValue("RoleClass")
	ROLECLASS("RoleClass"),
	@XmlEnumValue("RoleCode")
	ROLECODE("RoleCode"),
	@XmlEnumValue("RoleLinkType")
	ROLELINKTYPE("RoleLinkType"),
	@XmlEnumValue("RoleStatus")
	ROLESTATUS("RoleStatus"),
	@XmlEnumValue("RouteOfAdministration")
	ROUTEOFADMINISTRATION("RouteOfAdministration"),
	@XmlEnumValue("SCDHEC-GISSpatialAccuracyTiers")
	SCDHECGISSPATIALACCURACYTIERS("SCDHEC-GISSpatialAccuracyTiers"),
	@XmlEnumValue("SDM")
	SDM("SDM"),
	@XmlEnumValue("SNM")
	SNM("SNM"),
	@XmlEnumValue("SNM3")
	SNM3("SNM3"),
	@XmlEnumValue("SNT")
	SNT("SNT"),
	@XmlEnumValue("Sequencing")
	SEQUENCING("Sequencing"),
	@XmlEnumValue("SetOperator")
	SETOPERATOR("SetOperator"),
	@XmlEnumValue("SpecialArrangement")
	SPECIALARRANGEMENT("SpecialArrangement"),
	@XmlEnumValue("SpecimenType")
	SPECIMENTYPE("SpecimenType"),
	@XmlEnumValue("StyleType")
	STYLETYPE("StyleType"),
	@XmlEnumValue("SubstanceAdminSubstitution")
	SUBSTANCEADMINSUBSTITUTION("SubstanceAdminSubstitution"),
	@XmlEnumValue("SubstitutionCondition")
	SUBSTITUTIONCONDITION("SubstitutionCondition"),
	@XmlEnumValue("TableCellHorizontalAlign")
	TABLECELLHORIZONTALALIGN("TableCellHorizontalAlign"),
	@XmlEnumValue("TableCellScope")
	TABLECELLSCOPE("TableCellScope"),
	@XmlEnumValue("TableCellVerticalAlign")
	TABLECELLVERTICALALIGN("TableCellVerticalAlign"),
	@XmlEnumValue("TableFrame")
	TABLEFRAME("TableFrame"),
	@XmlEnumValue("TableRules")
	TABLERULES("TableRules"),
	@XmlEnumValue("TargetAwareness")
	TARGETAWARENESS("TargetAwareness"),
	@XmlEnumValue("TelecommunicationAddressUse")
	TELECOMMUNICATIONADDRESSUSE("TelecommunicationAddressUse"),
	@XmlEnumValue("TimingEvent")
	TIMINGEVENT("TimingEvent"),
	@XmlEnumValue("TransmissionRelationshipTypeCode")
	TRANSMISSIONRELATIONSHIPTYPECODE("TransmissionRelationshipTypeCode"),
	@XmlEnumValue("TribalEntityUS")
	TRIBALENTITYUS("TribalEntityUS"),
	@XmlEnumValue("UC")
	UC("UC"),
	@XmlEnumValue("UCUM")
	UCUM("UCUM"),
	@XmlEnumValue("UMD")
	UMD("UMD"),
	@XmlEnumValue("UML")
	UML("UML"),
	@XmlEnumValue("UPC")
	UPC("UPC"),
	@XmlEnumValue("URLScheme")
	URLSCHEME("URLScheme"),
	@XmlEnumValue("UnitsOfMeasure")
	UNITSOFMEASURE("UnitsOfMeasure"),
	@XmlEnumValue("VaccineManufacturer")
	VACCINEMANUFACTURER("VaccineManufacturer"),
	@XmlEnumValue("VaccineType")
	VACCINETYPE("VaccineType"),
	@XmlEnumValue("VocabularyDomainQualifier")
	VOCABULARYDOMAINQUALIFIER("VocabularyDomainQualifier"),
	@XmlEnumValue("W1-W2")
	W1W2("W1-W2"),
	@XmlEnumValue("W4")
	W4("W4"),
	@XmlEnumValue("WC")
	WC("WC");
	
	private final String value;

    CodeSystem(String v) {
        value = v;
    }
    
     public String value() {
        return value;
    }

    public static CodeSystem fromValue(String v) {
        for (CodeSystem c: CodeSystem.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
	
}