<!--
  ~ Copyright 2010 the original author or authors.
  ~
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~      http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  -->

<!--
Creates interactive continuation pieces of PDQv3 and QED responses.
@author Dmytro Rud
  -->

<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:hl7="urn:hl7-org:v3"
    xmlns:ipf="urn:org.openehealth.ipf">

    <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" omit-xml-declaration="yes" />

    <!-- all parameters are supposed to be validated beforehand -->
    <xsl:param name="startResultNumber" required="yes" as="xs:integer" />
    <xsl:param name="continuationCount" required="yes" as="xs:integer" />
    
    <xsl:param name="targetMessageIdRoot" required="yes" as="xs:string" />
    <xsl:param name="targetMessageIdExtension" required="yes" as="xs:string" />

    <!-- Identity copy of original contents -->
    <xsl:template match="@* | node()">
        <xsl:copy>
            <xsl:apply-templates select="@* | node()" />
        </xsl:copy>
    </xsl:template>

    <!-- check whether the response is positive -->
    <xsl:template match="/hl7:*/hl7:controlActProcess/hl7:queryAck/hl7:queryResponseCode[@code ne 'OK']">
        <xsl:copy-of select="error(QName('dummy', 'dummy'), '[ipf] negative response')" />
    </xsl:template>

    <!-- handle subjects -->
    <xsl:variable name="subjectsCount" as="xs:integer" select="count(/hl7:*/hl7:controlActProcess/hl7:subject)" />
    <xsl:variable name="endIndex" as="xs:integer" select="min(($subjectsCount, $startResultNumber + $continuationCount - 1))" />
    <xsl:template match="/*[$subjectsCount lt $startResultNumber]">
        <xsl:copy-of select="error(QName('dummy', 'dummy'), '[ipf] wrong startResultNumber')" />
    </xsl:template>
    <xsl:template match="/hl7:*/hl7:controlActProcess/hl7:subject[position() lt $startResultNumber]" />
    <xsl:template match="/hl7:*/hl7:controlActProcess/hl7:subject[position() gt $endIndex]" />

    <!-- actualize messaege id -->
    <xsl:template match="/hl7:*/hl7:id">
        <xsl:copy>
            <xsl:apply-templates select="@*[local-name() ne 'nullFlavor']" />
            <xsl:if test="not(@root)">
                <xsl:attribute name="root" select="'1.2.3'" />
            </xsl:if>
            <xsl:attribute name="extension" 
                   select="uuid:toString(uuid:randomUUID())"
                   xmlns:uuid="java:java.util.UUID" />
        </xsl:copy>
    </xsl:template>
    
    <!-- actualize message creation time -->
       <xsl:template match="/hl7:*/hl7:creationTime">
        <xsl:element name="creationTime" namespace="urn:hl7-org:v3">
            <xsl:attribute name="value" select="format-dateTime(current-dateTime(), '[Y][M01][D01][H01][m][s]')" />
        </xsl:element>
    </xsl:template>

    <!-- actualize target message id -->
    <xsl:template match="/hl7:*/hl7:acknowledgement/hl7:targetMessage/hl7:id">
        <xsl:element name="id" namespace="urn:hl7-org:v3">
            <xsl:if test="$targetMessageIdRoot">
                <xsl:attribute name="root" select="$targetMessageIdRoot" />
            </xsl:if>
            <xsl:if test="$targetMessageIdExtension">
                <xsl:attribute name="extension" select="$targetMessageIdExtension" />
            </xsl:if>
        </xsl:element>
    </xsl:template>

    <!-- actualize counters -->
    <xsl:template match="/hl7:*/hl7:controlActProcess/hl7:queryAck">    
        <xsl:copy>
            <xsl:apply-templates select="@* | *[not(ends-with(local-name(), 'Quantity'))]" />
            
            <xsl:element name="resultTotalQuantity" namespace="urn:hl7-org:v3">
                <xsl:attribute name="value" select="$subjectsCount" />
            </xsl:element>
            <xsl:element name="resultCurrentQuantity" namespace="urn:hl7-org:v3">
                <xsl:attribute name="value" select="$endIndex - $startResultNumber + 1" />
            </xsl:element>
            <xsl:element name="resultRemainingQuantity" namespace="urn:hl7-org:v3">
                <xsl:attribute name="value" select="$subjectsCount - $endIndex" />
            </xsl:element>
        </xsl:copy>
    </xsl:template>

    <!-- add human-readable message for the user -->
    <xsl:template match="/hl7:*/hl7:acknowledgement">
        <xsl:copy>
            <xsl:apply-templates select="@* | *" />
            <xsl:element name="acknowledgementDetail" namespace="urn:hl7-org:v3">
                <xsl:attribute name="typeCode" select="'I'" />
                <xsl:element name="text" namespace="urn:hl7-org:v3">
                    <xsl:value-of select="concat('This interactive continuation response for fragments ',
                                $startResultNumber, ' to ', $endIndex, ' has been generated by the IPF')" />
                </xsl:element>
            </xsl:element>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
