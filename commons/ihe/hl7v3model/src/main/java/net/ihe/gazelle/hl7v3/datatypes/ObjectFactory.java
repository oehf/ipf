package net.ihe.gazelle.hl7v3.datatypes;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

	private final static QName _ADGroup_QNAME = new QName("", "group:2");
	private final static QName _ADDelimiter_QNAME = new QName("urn:hl7-org:v3", "delimiter");
	private final static QName _ADCountry_QNAME = new QName("urn:hl7-org:v3", "country");
	private final static QName _ADState_QNAME = new QName("urn:hl7-org:v3", "state");
	private final static QName _ADCounty_QNAME = new QName("urn:hl7-org:v3", "county");
	private final static QName _ADCity_QNAME = new QName("urn:hl7-org:v3", "city");
	private final static QName _ADPostalCode_QNAME = new QName("urn:hl7-org:v3", "postalCode");
	private final static QName _ADStreetAddressLine_QNAME = new QName("urn:hl7-org:v3", "streetAddressLine");
	private final static QName _ADHouseNumber_QNAME = new QName("urn:hl7-org:v3", "houseNumber");
	private final static QName _ADHouseNumberNumeric_QNAME = new QName("urn:hl7-org:v3", "houseNumberNumeric");
	private final static QName _ADDirection_QNAME = new QName("urn:hl7-org:v3", "direction");
	private final static QName _ADStreetName_QNAME = new QName("urn:hl7-org:v3", "streetName");
	private final static QName _ADStreetNameBase_QNAME = new QName("urn:hl7-org:v3", "streetNameBase");
	private final static QName _ADStreetNameType_QNAME = new QName("urn:hl7-org:v3", "streetNameType");
	private final static QName _ADAdditionalLocator_QNAME = new QName("urn:hl7-org:v3", "additionalLocator");
	private final static QName _ADUnitID_QNAME = new QName("urn:hl7-org:v3", "unitID");
	private final static QName _ADUnitType_QNAME = new QName("urn:hl7-org:v3", "unitType");
	private final static QName _ADCareOf_QNAME = new QName("urn:hl7-org:v3", "careOf");
	private final static QName _ADCensusTract_QNAME = new QName("urn:hl7-org:v3", "censusTract");
	private final static QName _ADDeliveryAddressLine_QNAME = new QName("urn:hl7-org:v3", "deliveryAddressLine");
	private final static QName _ADDeliveryInstallationType_QNAME = new QName("urn:hl7-org:v3", "deliveryInstallationType");
	private final static QName _ADDeliveryInstallationArea_QNAME = new QName("urn:hl7-org:v3", "deliveryInstallationArea");
	private final static QName _ADDeliveryInstallationQualifier_QNAME = new QName("urn:hl7-org:v3", "deliveryInstallationQualifier");
	private final static QName _ADDeliveryMode_QNAME = new QName("urn:hl7-org:v3", "deliveryMode");
	private final static QName _ADDeliveryModeIdentifier_QNAME = new QName("urn:hl7-org:v3", "deliveryModeIdentifier");
	private final static QName _ADBuildingNumberSuffix_QNAME = new QName("urn:hl7-org:v3", "buildingNumberSuffix");
	private final static QName _ADPostBox_QNAME = new QName("urn:hl7-org:v3", "postBox");
	private final static QName _ADPrecinct_QNAME = new QName("urn:hl7-org:v3", "precinct");
	private final static QName _ADUseablePeriod_QNAME = new QName("urn:hl7-org:v3", "useablePeriod");
	private final static QName _EDReference_QNAME = new QName("urn:hl7-org:v3", "reference");
	private final static QName _EDThumbnail_QNAME = new QName("urn:hl7-org:v3", "thumbnail");
	private final static QName _ENGroup_QNAME = new QName("", "group:2");
	private final static QName _ENDelimiter_QNAME = new QName("urn:hl7-org:v3", "delimiter");
	private final static QName _ENFamily_QNAME = new QName("urn:hl7-org:v3", "family");
	private final static QName _ENGiven_QNAME = new QName("urn:hl7-org:v3", "given");
	private final static QName _ENPrefix_QNAME = new QName("urn:hl7-org:v3", "prefix");
	private final static QName _ENSuffix_QNAME = new QName("urn:hl7-org:v3", "suffix");
	private final static QName _ENValidTime_QNAME = new QName("urn:hl7-org:v3", "validTime");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: generated
     * 
     */
    public ObjectFactory() {}
    
    /**
     * Create an instance of {@link  AD}
     * 
     */
    public AD createAD() {
        return new AD();
    }

    /**
     * Create an instance of {@link  AdxpDelimiter}
     * 
     */
    public AdxpDelimiter createAdxpDelimiter() {
        return new AdxpDelimiter();
    }

    /**
     * Create an instance of {@link  ADXP}
     * 
     */
    public ADXP createADXP() {
        return new ADXP();
    }

    /**
     * Create an instance of {@link  ST}
     * 
     */
    public ST createST() {
        return new ST();
    }

    /**
     * Create an instance of {@link  ED}
     * 
     */
    public ED createED() {
        return new ED();
    }

    /**
     * Create an instance of {@link  TEL}
     * 
     */
    public TEL createTEL() {
        return new TEL();
    }

    /**
     * Create an instance of {@link  SXCMTS}
     * 
     */
    public SXCMTS createSXCMTS() {
        return new SXCMTS();
    }

    /**
     * Create an instance of {@link  TS}
     * 
     */
    public TS createTS() {
        return new TS();
    }

    /**
     * Create an instance of {@link  Thumbnail}
     * 
     */
    public Thumbnail createThumbnail() {
        return new Thumbnail();
    }

    /**
     * Create an instance of {@link  AdxpCountry}
     * 
     */
    public AdxpCountry createAdxpCountry() {
        return new AdxpCountry();
    }

    /**
     * Create an instance of {@link  AdxpState}
     * 
     */
    public AdxpState createAdxpState() {
        return new AdxpState();
    }

    /**
     * Create an instance of {@link  AdxpCounty}
     * 
     */
    public AdxpCounty createAdxpCounty() {
        return new AdxpCounty();
    }

    /**
     * Create an instance of {@link  AdxpCity}
     * 
     */
    public AdxpCity createAdxpCity() {
        return new AdxpCity();
    }

    /**
     * Create an instance of {@link  AdxpPostalCode}
     * 
     */
    public AdxpPostalCode createAdxpPostalCode() {
        return new AdxpPostalCode();
    }

    /**
     * Create an instance of {@link  AdxpStreetAddressLine}
     * 
     */
    public AdxpStreetAddressLine createAdxpStreetAddressLine() {
        return new AdxpStreetAddressLine();
    }

    /**
     * Create an instance of {@link  AdxpHouseNumber}
     * 
     */
    public AdxpHouseNumber createAdxpHouseNumber() {
        return new AdxpHouseNumber();
    }

    /**
     * Create an instance of {@link  AdxpHouseNumberNumeric}
     * 
     */
    public AdxpHouseNumberNumeric createAdxpHouseNumberNumeric() {
        return new AdxpHouseNumberNumeric();
    }

    /**
     * Create an instance of {@link  AdxpDirection}
     * 
     */
    public AdxpDirection createAdxpDirection() {
        return new AdxpDirection();
    }

    /**
     * Create an instance of {@link  AdxpStreetName}
     * 
     */
    public AdxpStreetName createAdxpStreetName() {
        return new AdxpStreetName();
    }

    /**
     * Create an instance of {@link  AdxpStreetNameBase}
     * 
     */
    public AdxpStreetNameBase createAdxpStreetNameBase() {
        return new AdxpStreetNameBase();
    }

    /**
     * Create an instance of {@link  AdxpStreetNameType}
     * 
     */
    public AdxpStreetNameType createAdxpStreetNameType() {
        return new AdxpStreetNameType();
    }

    /**
     * Create an instance of {@link  AdxpAdditionalLocator}
     * 
     */
    public AdxpAdditionalLocator createAdxpAdditionalLocator() {
        return new AdxpAdditionalLocator();
    }

    /**
     * Create an instance of {@link  AdxpUnitID}
     * 
     */
    public AdxpUnitID createAdxpUnitID() {
        return new AdxpUnitID();
    }

    /**
     * Create an instance of {@link  AdxpUnitType}
     * 
     */
    public AdxpUnitType createAdxpUnitType() {
        return new AdxpUnitType();
    }

    /**
     * Create an instance of {@link  AdxpCareOf}
     * 
     */
    public AdxpCareOf createAdxpCareOf() {
        return new AdxpCareOf();
    }

    /**
     * Create an instance of {@link  AdxpCensusTract}
     * 
     */
    public AdxpCensusTract createAdxpCensusTract() {
        return new AdxpCensusTract();
    }

    /**
     * Create an instance of {@link  AdxpDeliveryAddressLine}
     * 
     */
    public AdxpDeliveryAddressLine createAdxpDeliveryAddressLine() {
        return new AdxpDeliveryAddressLine();
    }

    /**
     * Create an instance of {@link  AdxpDeliveryInstallationType}
     * 
     */
    public AdxpDeliveryInstallationType createAdxpDeliveryInstallationType() {
        return new AdxpDeliveryInstallationType();
    }

    /**
     * Create an instance of {@link  AdxpDeliveryInstallationArea}
     * 
     */
    public AdxpDeliveryInstallationArea createAdxpDeliveryInstallationArea() {
        return new AdxpDeliveryInstallationArea();
    }

    /**
     * Create an instance of {@link  AdxpDeliveryInstallationQualifier}
     * 
     */
    public AdxpDeliveryInstallationQualifier createAdxpDeliveryInstallationQualifier() {
        return new AdxpDeliveryInstallationQualifier();
    }

    /**
     * Create an instance of {@link  AdxpDeliveryMode}
     * 
     */
    public AdxpDeliveryMode createAdxpDeliveryMode() {
        return new AdxpDeliveryMode();
    }

    /**
     * Create an instance of {@link  AdxpDeliveryModeIdentifier}
     * 
     */
    public AdxpDeliveryModeIdentifier createAdxpDeliveryModeIdentifier() {
        return new AdxpDeliveryModeIdentifier();
    }

    /**
     * Create an instance of {@link  AdxpBuildingNumberSuffix}
     * 
     */
    public AdxpBuildingNumberSuffix createAdxpBuildingNumberSuffix() {
        return new AdxpBuildingNumberSuffix();
    }

    /**
     * Create an instance of {@link  AdxpPostBox}
     * 
     */
    public AdxpPostBox createAdxpPostBox() {
        return new AdxpPostBox();
    }

    /**
     * Create an instance of {@link  AdxpPrecinct}
     * 
     */
    public AdxpPrecinct createAdxpPrecinct() {
        return new AdxpPrecinct();
    }

    /**
     * Create an instance of {@link  ANYNonNull}
     * 
     */
    public ANYNonNull createANYNonNull() {
        return new ANYNonNull();
    }

    /**
     * Create an instance of {@link  BL}
     * 
     */
    public BL createBL() {
        return new BL();
    }

    /**
     * Create an instance of {@link  BN}
     * 
     */
    public BN createBN() {
        return new BN();
    }

    /**
     * Create an instance of {@link  BXITCD}
     * 
     */
    public BXITCD createBXITCD() {
        return new BXITCD();
    }

    /**
     * Create an instance of {@link  CD}
     * 
     */
    public CD createCD() {
        return new CD();
    }

    /**
     * Create an instance of {@link  CR}
     * 
     */
    public CR createCR() {
        return new CR();
    }

    /**
     * Create an instance of {@link  CV}
     * 
     */
    public CV createCV() {
        return new CV();
    }

    /**
     * Create an instance of {@link  CE}
     * 
     */
    public CE createCE() {
        return new CE();
    }

    /**
     * Create an instance of {@link  BXITIVLPQ}
     * 
     */
    public BXITIVLPQ createBXITIVLPQ() {
        return new BXITIVLPQ();
    }

    /**
     * Create an instance of {@link  IVLPQ}
     * 
     */
    public IVLPQ createIVLPQ() {
        return new IVLPQ();
    }

    /**
     * Create an instance of {@link  SXCMPQ}
     * 
     */
    public SXCMPQ createSXCMPQ() {
        return new SXCMPQ();
    }

    /**
     * Create an instance of {@link  PQ}
     * 
     */
    public PQ createPQ() {
        return new PQ();
    }

    /**
     * Create an instance of {@link  PQR}
     * 
     */
    public PQR createPQR() {
        return new PQR();
    }

    /**
     * Create an instance of {@link  IVXBPQ}
     * 
     */
    public IVXBPQ createIVXBPQ() {
        return new IVXBPQ();
    }

    /**
     * Create an instance of {@link  CO}
     * 
     */
    public CO createCO() {
        return new CO();
    }

    /**
     * Create an instance of {@link  EIVLEvent}
     * 
     */
    public EIVLEvent createEIVLEvent() {
        return new EIVLEvent();
    }

    /**
     * Create an instance of {@link  EIVLPPDTS}
     * 
     */
    public EIVLPPDTS createEIVLPPDTS() {
        return new EIVLPPDTS();
    }

    /**
     * Create an instance of {@link  SXCMPPDTS}
     * 
     */
    public SXCMPPDTS createSXCMPPDTS() {
        return new SXCMPPDTS();
    }

    /**
     * Create an instance of {@link  PPDTS}
     * 
     */
    public PPDTS createPPDTS() {
        return new PPDTS();
    }

    /**
     * Create an instance of {@link  IVLPPDPQ}
     * 
     */
    public IVLPPDPQ createIVLPPDPQ() {
        return new IVLPPDPQ();
    }

    /**
     * Create an instance of {@link  SXCMPPDPQ}
     * 
     */
    public SXCMPPDPQ createSXCMPPDPQ() {
        return new SXCMPPDPQ();
    }

    /**
     * Create an instance of {@link  PPDPQ}
     * 
     */
    public PPDPQ createPPDPQ() {
        return new PPDPQ();
    }

    /**
     * Create an instance of {@link  IVXBPPDPQ}
     * 
     */
    public IVXBPPDPQ createIVXBPPDPQ() {
        return new IVXBPPDPQ();
    }

    /**
     * Create an instance of {@link  EIVLTS}
     * 
     */
    public EIVLTS createEIVLTS() {
        return new EIVLTS();
    }

    /**
     * Create an instance of {@link  EN}
     * 
     */
    public EN createEN() {
        return new EN();
    }

    /**
     * Create an instance of {@link  EnDelimiter}
     * 
     */
    public EnDelimiter createEnDelimiter() {
        return new EnDelimiter();
    }

    /**
     * Create an instance of {@link  ENXP}
     * 
     */
    public ENXP createENXP() {
        return new ENXP();
    }

    /**
     * Create an instance of {@link  EnFamily}
     * 
     */
    public EnFamily createEnFamily() {
        return new EnFamily();
    }

    /**
     * Create an instance of {@link  EnGiven}
     * 
     */
    public EnGiven createEnGiven() {
        return new EnGiven();
    }

    /**
     * Create an instance of {@link  EnPrefix}
     * 
     */
    public EnPrefix createEnPrefix() {
        return new EnPrefix();
    }

    /**
     * Create an instance of {@link  EnSuffix}
     * 
     */
    public EnSuffix createEnSuffix() {
        return new EnSuffix();
    }

    /**
     * Create an instance of {@link  IVLTS}
     * 
     */
    public IVLTS createIVLTS() {
        return new IVLTS();
    }

    /**
     * Create an instance of {@link  IVXBTS}
     * 
     */
    public IVXBTS createIVXBTS() {
        return new IVXBTS();
    }

    /**
     * Create an instance of {@link  GLISTPQ}
     * 
     */
    public GLISTPQ createGLISTPQ() {
        return new GLISTPQ();
    }

    /**
     * Create an instance of {@link  GLISTTS}
     * 
     */
    public GLISTTS createGLISTTS() {
        return new GLISTTS();
    }

    /**
     * Create an instance of {@link  HXITCE}
     * 
     */
    public HXITCE createHXITCE() {
        return new HXITCE();
    }

    /**
     * Create an instance of {@link  HXITPQ}
     * 
     */
    public HXITPQ createHXITPQ() {
        return new HXITPQ();
    }

    /**
     * Create an instance of {@link  II}
     * 
     */
    public II createII() {
        return new II();
    }

    /**
     * Create an instance of {@link  INT}
     * 
     */
    public INT createINT() {
        return new INT();
    }

    /**
     * Create an instance of {@link  IVLINT}
     * 
     */
    public IVLINT createIVLINT() {
        return new IVLINT();
    }

    /**
     * Create an instance of {@link  SXCMINT}
     * 
     */
    public SXCMINT createSXCMINT() {
        return new SXCMINT();
    }

    /**
     * Create an instance of {@link  IVXBINT}
     * 
     */
    public IVXBINT createIVXBINT() {
        return new IVXBINT();
    }

    /**
     * Create an instance of {@link  IVLMO}
     * 
     */
    public IVLMO createIVLMO() {
        return new IVLMO();
    }

    /**
     * Create an instance of {@link  SXCMMO}
     * 
     */
    public SXCMMO createSXCMMO() {
        return new SXCMMO();
    }

    /**
     * Create an instance of {@link  MO}
     * 
     */
    public MO createMO() {
        return new MO();
    }

    /**
     * Create an instance of {@link  IVXBMO}
     * 
     */
    public IVXBMO createIVXBMO() {
        return new IVXBMO();
    }

    /**
     * Create an instance of {@link  IVLPPDTS}
     * 
     */
    public IVLPPDTS createIVLPPDTS() {
        return new IVLPPDTS();
    }

    /**
     * Create an instance of {@link  IVXBPPDTS}
     * 
     */
    public IVXBPPDTS createIVXBPPDTS() {
        return new IVXBPPDTS();
    }

    /**
     * Create an instance of {@link  IVLREAL}
     * 
     */
    public IVLREAL createIVLREAL() {
        return new IVLREAL();
    }

    /**
     * Create an instance of {@link  SXCMREAL}
     * 
     */
    public SXCMREAL createSXCMREAL() {
        return new SXCMREAL();
    }

    /**
     * Create an instance of {@link  REAL}
     * 
     */
    public REAL createREAL() {
        return new REAL();
    }

    /**
     * Create an instance of {@link  IVXBREAL}
     * 
     */
    public IVXBREAL createIVXBREAL() {
        return new IVXBREAL();
    }

    /**
     * Create an instance of {@link  ON}
     * 
     */
    public ON createON() {
        return new ON();
    }

    /**
     * Create an instance of {@link  PIVLPPDTS}
     * 
     */
    public PIVLPPDTS createPIVLPPDTS() {
        return new PIVLPPDTS();
    }

    /**
     * Create an instance of {@link  PIVLTS}
     * 
     */
    public PIVLTS createPIVLTS() {
        return new PIVLTS();
    }

    /**
     * Create an instance of {@link  PN}
     * 
     */
    public PN createPN() {
        return new PN();
    }

    /**
     * Create an instance of {@link  RTO}
     * 
     */
    public RTO createRTO() {
        return new RTO();
    }

    /**
     * Create an instance of {@link  RTOQTYQTY}
     * 
     */
    public RTOQTYQTY createRTOQTYQTY() {
        return new RTOQTYQTY();
    }

    /**
     * Create an instance of {@link  RTOMOPQ}
     * 
     */
    public RTOMOPQ createRTOMOPQ() {
        return new RTOMOPQ();
    }

    /**
     * Create an instance of {@link  RTOPQPQ}
     * 
     */
    public RTOPQPQ createRTOPQPQ() {
        return new RTOPQPQ();
    }

    /**
     * Create an instance of {@link  SC}
     * 
     */
    public SC createSC() {
        return new SC();
    }

    /**
     * Create an instance of {@link  SLISTPQ}
     * 
     */
    public SLISTPQ createSLISTPQ() {
        return new SLISTPQ();
    }

    /**
     * Create an instance of {@link  SLISTTS}
     * 
     */
    public SLISTTS createSLISTTS() {
        return new SLISTTS();
    }

    /**
     * Create an instance of {@link  SXCMCD}
     * 
     */
    public SXCMCD createSXCMCD() {
        return new SXCMCD();
    }

    /**
     * Create an instance of {@link  SXPRTS}
     * 
     */
    public SXPRTS createSXPRTS() {
        return new SXPRTS();
    }

    /**
     * Create an instance of {@link  TN}
     * 
     */
    public TN createTN() {
        return new TN();
    }

    /**
     * Create an instance of {@link  UVPTS}
     * 
     */
    public UVPTS createUVPTS() {
        return new UVPTS();
    }

    /**
     * Create an instance of {@link  CS}
     * 
     */
    public CS createCS() {
        return new CS();
    }



	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpDelimiter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "delimiter", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpDelimiter> createADDelimiter(net.ihe.gazelle.hl7v3.datatypes.AdxpDelimiter value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpDelimiter>(_ADDelimiter_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpDelimiter.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpCountry }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "country", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpCountry> createADCountry(net.ihe.gazelle.hl7v3.datatypes.AdxpCountry value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpCountry>(_ADCountry_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpCountry.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "state", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpState> createADState(net.ihe.gazelle.hl7v3.datatypes.AdxpState value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpState>(_ADState_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpState.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpCounty }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "county", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpCounty> createADCounty(net.ihe.gazelle.hl7v3.datatypes.AdxpCounty value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpCounty>(_ADCounty_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpCounty.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpCity }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "city", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpCity> createADCity(net.ihe.gazelle.hl7v3.datatypes.AdxpCity value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpCity>(_ADCity_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpCity.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpPostalCode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "postalCode", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpPostalCode> createADPostalCode(net.ihe.gazelle.hl7v3.datatypes.AdxpPostalCode value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpPostalCode>(_ADPostalCode_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpPostalCode.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpStreetAddressLine }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "streetAddressLine", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpStreetAddressLine> createADStreetAddressLine(net.ihe.gazelle.hl7v3.datatypes.AdxpStreetAddressLine value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpStreetAddressLine>(_ADStreetAddressLine_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpStreetAddressLine.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpHouseNumber }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "houseNumber", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpHouseNumber> createADHouseNumber(net.ihe.gazelle.hl7v3.datatypes.AdxpHouseNumber value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpHouseNumber>(_ADHouseNumber_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpHouseNumber.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpHouseNumberNumeric }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "houseNumberNumeric", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpHouseNumberNumeric> createADHouseNumberNumeric(net.ihe.gazelle.hl7v3.datatypes.AdxpHouseNumberNumeric value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpHouseNumberNumeric>(_ADHouseNumberNumeric_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpHouseNumberNumeric.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpDirection }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "direction", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpDirection> createADDirection(net.ihe.gazelle.hl7v3.datatypes.AdxpDirection value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpDirection>(_ADDirection_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpDirection.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpStreetName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "streetName", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpStreetName> createADStreetName(net.ihe.gazelle.hl7v3.datatypes.AdxpStreetName value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpStreetName>(_ADStreetName_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpStreetName.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpStreetNameBase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "streetNameBase", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpStreetNameBase> createADStreetNameBase(net.ihe.gazelle.hl7v3.datatypes.AdxpStreetNameBase value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpStreetNameBase>(_ADStreetNameBase_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpStreetNameBase.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpStreetNameType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "streetNameType", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpStreetNameType> createADStreetNameType(net.ihe.gazelle.hl7v3.datatypes.AdxpStreetNameType value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpStreetNameType>(_ADStreetNameType_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpStreetNameType.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpAdditionalLocator }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "additionalLocator", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpAdditionalLocator> createADAdditionalLocator(net.ihe.gazelle.hl7v3.datatypes.AdxpAdditionalLocator value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpAdditionalLocator>(_ADAdditionalLocator_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpAdditionalLocator.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpUnitID }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "unitID", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpUnitID> createADUnitID(net.ihe.gazelle.hl7v3.datatypes.AdxpUnitID value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpUnitID>(_ADUnitID_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpUnitID.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpUnitType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "unitType", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpUnitType> createADUnitType(net.ihe.gazelle.hl7v3.datatypes.AdxpUnitType value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpUnitType>(_ADUnitType_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpUnitType.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpCareOf }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "careOf", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpCareOf> createADCareOf(net.ihe.gazelle.hl7v3.datatypes.AdxpCareOf value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpCareOf>(_ADCareOf_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpCareOf.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpCensusTract }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "censusTract", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpCensusTract> createADCensusTract(net.ihe.gazelle.hl7v3.datatypes.AdxpCensusTract value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpCensusTract>(_ADCensusTract_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpCensusTract.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpDeliveryAddressLine }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "deliveryAddressLine", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryAddressLine> createADDeliveryAddressLine(net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryAddressLine value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryAddressLine>(_ADDeliveryAddressLine_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryAddressLine.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpDeliveryInstallationType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "deliveryInstallationType", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryInstallationType> createADDeliveryInstallationType(net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryInstallationType value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryInstallationType>(_ADDeliveryInstallationType_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryInstallationType.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpDeliveryInstallationArea }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "deliveryInstallationArea", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryInstallationArea> createADDeliveryInstallationArea(net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryInstallationArea value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryInstallationArea>(_ADDeliveryInstallationArea_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryInstallationArea.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpDeliveryInstallationQualifier }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "deliveryInstallationQualifier", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryInstallationQualifier> createADDeliveryInstallationQualifier(net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryInstallationQualifier value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryInstallationQualifier>(_ADDeliveryInstallationQualifier_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryInstallationQualifier.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpDeliveryMode }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "deliveryMode", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryMode> createADDeliveryMode(net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryMode value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryMode>(_ADDeliveryMode_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryMode.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpDeliveryModeIdentifier }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "deliveryModeIdentifier", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryModeIdentifier> createADDeliveryModeIdentifier(net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryModeIdentifier value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryModeIdentifier>(_ADDeliveryModeIdentifier_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpDeliveryModeIdentifier.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpBuildingNumberSuffix }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "buildingNumberSuffix", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpBuildingNumberSuffix> createADBuildingNumberSuffix(net.ihe.gazelle.hl7v3.datatypes.AdxpBuildingNumberSuffix value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpBuildingNumberSuffix>(_ADBuildingNumberSuffix_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpBuildingNumberSuffix.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpPostBox }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "postBox", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpPostBox> createADPostBox(net.ihe.gazelle.hl7v3.datatypes.AdxpPostBox value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpPostBox>(_ADPostBox_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpPostBox.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link AdxpPrecinct }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "precinct", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpPrecinct> createADPrecinct(net.ihe.gazelle.hl7v3.datatypes.AdxpPrecinct value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.AdxpPrecinct>(_ADPrecinct_QNAME, net.ihe.gazelle.hl7v3.datatypes.AdxpPrecinct.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link SXCMTS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "useablePeriod", scope = AD.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.SXCMTS> createADUseablePeriod(net.ihe.gazelle.hl7v3.datatypes.SXCMTS value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.SXCMTS>(_ADUseablePeriod_QNAME, net.ihe.gazelle.hl7v3.datatypes.SXCMTS.class, AD.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link TEL }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "reference", scope = ED.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.TEL> createEDReference(net.ihe.gazelle.hl7v3.datatypes.TEL value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.TEL>(_EDReference_QNAME, net.ihe.gazelle.hl7v3.datatypes.TEL.class, ED.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link Thumbnail }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "thumbnail", scope = ED.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.Thumbnail> createEDThumbnail(net.ihe.gazelle.hl7v3.datatypes.Thumbnail value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.Thumbnail>(_EDThumbnail_QNAME, net.ihe.gazelle.hl7v3.datatypes.Thumbnail.class, ED.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnDelimiter }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "delimiter", scope = EN.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.EnDelimiter> createENDelimiter(net.ihe.gazelle.hl7v3.datatypes.EnDelimiter value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.EnDelimiter>(_ENDelimiter_QNAME, net.ihe.gazelle.hl7v3.datatypes.EnDelimiter.class, EN.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnFamily }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "family", scope = EN.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.EnFamily> createENFamily(net.ihe.gazelle.hl7v3.datatypes.EnFamily value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.EnFamily>(_ENFamily_QNAME, net.ihe.gazelle.hl7v3.datatypes.EnFamily.class, EN.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnGiven }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "given", scope = EN.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.EnGiven> createENGiven(net.ihe.gazelle.hl7v3.datatypes.EnGiven value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.EnGiven>(_ENGiven_QNAME, net.ihe.gazelle.hl7v3.datatypes.EnGiven.class, EN.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnPrefix }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "prefix", scope = EN.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.EnPrefix> createENPrefix(net.ihe.gazelle.hl7v3.datatypes.EnPrefix value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.EnPrefix>(_ENPrefix_QNAME, net.ihe.gazelle.hl7v3.datatypes.EnPrefix.class, EN.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link EnSuffix }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "suffix", scope = EN.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.EnSuffix> createENSuffix(net.ihe.gazelle.hl7v3.datatypes.EnSuffix value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.EnSuffix>(_ENSuffix_QNAME, net.ihe.gazelle.hl7v3.datatypes.EnSuffix.class, EN.class, value);
    }
	/**
     * Create an instance of {@link JAXBElement }{@code <}{@link IVLTS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:hl7-org:v3", name = "validTime", scope = EN.class)
    public JAXBElement<net.ihe.gazelle.hl7v3.datatypes.IVLTS> createENValidTime(net.ihe.gazelle.hl7v3.datatypes.IVLTS value) {
        return new JAXBElement<net.ihe.gazelle.hl7v3.datatypes.IVLTS>(_ENValidTime_QNAME, net.ihe.gazelle.hl7v3.datatypes.IVLTS.class, EN.class, value);
    }

}