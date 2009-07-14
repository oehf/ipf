<?xml version="1.0" encoding="UTF-8"?>

<!-- This stylesheet expects an instance of XsltTestService to be passed in
	and calls its getValue method.
-->
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:param="java:org.openehealth.ipf.commons.xml.XsltTestService">

	<xsl:param name="service" />

	<xsl:output method="xml" version="1.0" encoding="UTF-8"
		indent="yes" />

	<!-- Add message header -->
	<xsl:template match="top">
		<xsl:variable name="arg" select="." />
		<xsl:element name="first">
			<xsl:value-of select="param:getValue($service, $arg)" />
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>
