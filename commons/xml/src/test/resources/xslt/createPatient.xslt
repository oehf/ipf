<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/02/xpath-functions" xmlns:xdt="http://www.w3.org/2005/02/xpath-datatypes" xmlns:ns1="http://org.openehealth.test/ns1" xmlns:ns2="http://org.openehealth.test/ns2" xmlns:xm="http://www.w3.org/2005/05/xmlmime" xmlns="urn:hl7-org:v3">
	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>
	
	<!-- Add message header -->
	<xsl:template match="/ns1:patientRegistration">
		<PRPA_IN201101 ITSVersion="XML_1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
			<realmCode code="DE"/>
			<!-- TODO create/get message ID -->
			<id root="2.16.840.1.113883.3.37.2.411.4" extension="1995592957"/>
			<!-- Set the current time. Maybe we need to reformat the date string -->
			<xsl:element name="creationTime">
				<xsl:attribute name="value"><xsl:value-of select="current-dateTime()"/></xsl:attribute>
			</xsl:element>
			<versionCode code="V3-2006-05"/>
			<interactionId extension="PRPA_IN201101" root="2.16.840.1.113883.1.6"/>
			<profileId root="1.2.276.0.76.3.1.13.8" extension="10"/>
			<processingCode code="P"/>
			<processingModeCode code="T"/>
			<acceptAckCode code="ER"/>
			<!-- TODO should this really be hardcoded? -->
			<receiver>
				<device>
					<id root="2.16.840.1.113883.3.37.2" extension="111"/>
				</device>
			</receiver>
			<!-- TODO get sender information -->
			<sender>
				<device>					
					<id root="2.16.840.1.113883.3.37.2" extension="411"/>
				</device>
			</sender>
			<controlActProcess moodCode="EVN">
				<subject>
					<registrationEvent>
						<xsl:apply-templates/>
					</registrationEvent>
				</subject>
			</controlActProcess>
		</PRPA_IN201101>
	</xsl:template>

	<!-- Restructure effectiveTime -->
	<xsl:template name="urrgs" match="//ns1:patientRegistration/ns2:effectiveTime">
		<xsl:element name="effectiveTime">
			<xsl:attribute name="value" select="./ns2:low"/>
		</xsl:element>
	</xsl:template>
		
	<!-- Rename <subject> to <subject1> -->
	<xsl:template match="//ns1:patientRegistration/ns2:subject">
		<subject1>
			<xsl:apply-templates/>
		</subject1>
	</xsl:template>
	
	<!-- Rename <patient> to patientRole-->
	<xsl:template match="//ns1:patientRegistration/ns2:subject/ns2:patient">
		<patientRole>
			<xsl:apply-templates/>
		</patientRole>
	</xsl:template>

	<!-- Rename <person> to <patientPerson>-->
	<xsl:template match="//ns2:person">
		<patientPerson>
			<xsl:apply-templates/>
		</patientPerson>
	</xsl:template>
	
	<!-- Restructure <asCitizen> -->
	<xsl:template match="//ns2:asCitizen">
		<asCitizen>
			<politicalNation>
				<xsl:element name="code">
					<xsl:attribute name="code" select="@code"/>
				</xsl:element>
			</politicalNation>
		</asCitizen>
	</xsl:template>
	
	<!-- Restructure providerOrganization -->
	<xsl:template match="//ns2:providerOrganization">
		<providerOrganization>
			<xsl:apply-templates/>
			<contactParty/>
		</providerOrganization>
	</xsl:template>
	
	<!-- Default rules. They copy all elements and attributes while removing the namespaces -->
	<!-- These rules should apply if there is no more specific rule defined above. -->
	
	<xsl:template match="/|comment()|processing-instruction()">
    <xsl:copy>
      <!-- go process children (applies to root node only) -->
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="*">
    <xsl:element name="{local-name()}">
      <!-- go process attributes and children -->
      <xsl:apply-templates select="@*|node()"/>
    </xsl:element>
  </xsl:template>

  <xsl:template match="@*">
    <xsl:attribute name="{local-name()}">
      <xsl:value-of select="."/>
    </xsl:attribute>
  </xsl:template>
	
</xsl:stylesheet>
