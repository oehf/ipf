<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" 
	xmlns:tut="http://www.openehealth.org/tutorial"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" />
	
	<xsl:template match="/tut:order">
		<xsl:element name="{local-name()}" namespace="{namespace-uri()}">
			<xsl:attribute name="category">
				<xsl:value-of select="./tut:category"/>
			</xsl:attribute>
		<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
	
	<xsl:template match="/tut:order/tut:category"/>


	<!-- Default rules. They copy all elements and attributes ignoring any namespaces -->
	<!-- These rules should apply if there is no more specific rule defined above. -->


	<xsl:template match="*">
		<xsl:element name="{local-name()}" namespace="{namespace-uri()}">
			<xsl:apply-templates select="@*|node()" />
		</xsl:element>
	</xsl:template>

	<xsl:template match="@*">
		<xsl:attribute name="{local-name()}">
    		<xsl:value-of select="." />
    	</xsl:attribute>
	</xsl:template>
		
</xsl:stylesheet>
