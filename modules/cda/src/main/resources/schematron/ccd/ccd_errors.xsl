<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:sch="http://www.ascc.net/xml/schematron"
                xmlns:cda="urn:hl7-org:v3"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                version="1.0"
                cda:dummy-for-xmlns=""
                xsi:dummy-for-xmlns="">
   <xsl:output method="xml" omit-xml-declaration="no" standalone="yes" indent="yes"/>
   <xsl:template match="*|@*" mode="schematron-get-full-path">
      <xsl:apply-templates select="parent::*" mode="schematron-get-full-path"/>
      <xsl:text>/</xsl:text>
      <xsl:if test="count(. | ../@*) = count(../@*)">@</xsl:if>
      <xsl:value-of select="name()"/>
      <xsl:text>[</xsl:text>
      <xsl:value-of select="1+count(preceding-sibling::*[name()=name(current())])"/>
      <xsl:text>]</xsl:text>
   </xsl:template>
   <xsl:template match="/">
      <schematron-output title="Schematron schema for validating conformance to CCD documents"
                         schemaVersion=""
                         phase="errors">
         <ns uri="urn:hl7-org:v3" prefix="cda"/>
         <ns uri="http://www.w3.org/2001/XMLSchema-instance" prefix="xsi"/>
         <active-pattern name="CCD v1.0 Templates Root - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M6"/>
         <active-pattern name="Advance directives section - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M9"/>
         <active-pattern name="Alerts section - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M12"/>
         <active-pattern name="Encounters section - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M15"/>
         <active-pattern name="Family history section - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M18"/>
         <active-pattern name="Functional status section - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M21"/>
         <active-pattern name="Immunizations section - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M24"/>
         <active-pattern name="Medical equipment section - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M27"/>
         <active-pattern name="Medications section - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M30"/>
         <active-pattern name="Payers section - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M33"/>
         <active-pattern name="Plan of care section - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M36"/>
         <active-pattern name="Problem section - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M39"/>
         <active-pattern name="Procedures section - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M42"/>
         <active-pattern name="Purpose section - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M45"/>
         <active-pattern name="Results section - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M48"/>
         <active-pattern name="Social history section - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M51"/>
         <active-pattern name="Vital signs section - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M54"/>
         <active-pattern name="Advance directive observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M57"/>
         <active-pattern name="Alert observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M60"/>
         <active-pattern name="Authorization activity - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M63"/>
         <active-pattern name="Coverage activity - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M66"/>
         <active-pattern name="Encounter activity - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M69"/>
         <active-pattern name="Family history observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M72"/>
         <active-pattern name="Family history organizer - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M75"/>
         <active-pattern name="Medication activity - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M78"/>
         <active-pattern name="Plan of care activity - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M81"/>
         <active-pattern name="Policy activity - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M84"/>
         <active-pattern name="Problem act - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M87"/>
         <active-pattern name="Problem observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M90"/>
         <active-pattern name="Procedure activity - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M93"/>
         <active-pattern name="Purpose activity - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M96"/>
         <active-pattern name="Result observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M99"/>
         <active-pattern name="Result organizer - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M102"/>
         <active-pattern name="Social history observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M105"/>
         <active-pattern name="Supply activity - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M108"/>
         <active-pattern name="Vital signs organizer - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M111"/>
         <active-pattern name="Advance directive reference - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M114"/>
         <active-pattern name="Advance directive status observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M117"/>
         <active-pattern name="Age observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M120"/>
         <active-pattern name="Alert status observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M123"/>
         <active-pattern name="Comment - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M126"/>
         <active-pattern name="Episode observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M129"/>
         <active-pattern name="Family history cause of death observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M132"/>
         <active-pattern name="Fulfillment instruction - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M135"/>
         <active-pattern name="Location participation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M138"/>
         <active-pattern name="Medication series number observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M141"/>
         <active-pattern name="Medication status observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M144"/>
         <active-pattern name="Patient awareness - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M147"/>
         <active-pattern name="Patient instruction - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M150"/>
         <active-pattern name="Problem healthstatus observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M153"/>
         <active-pattern name="Problem status observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M156"/>
         <active-pattern name="Product - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M159"/>
         <active-pattern name="Product instance - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M162"/>
         <active-pattern name="Reaction observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M165"/>
         <active-pattern name="Severity observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M168"/>
         <active-pattern name="Social history status observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M171"/>
         <active-pattern name="Status observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M174"/>
         <active-pattern name="Status of functional status observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M177"/>
         <active-pattern name="Verification of an advance directive observation - errors validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M180"/>
      </schematron-output>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1&#34;]" priority="3999"
                 mode="M6">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:ClinicalDocument"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:ClinicalDocument" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The root of a CCD document must be ClinicalDocument in the HL7 namespace</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='34133-9'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='34133-9'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The ClinicalDocument/code element SHALL be present, and SHALL be valued with LOINC code 34133-9 (SUMMARIZATION OF EPISODE NOTE).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:documentationOf/cda:serviceEvent"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:documentationOf/cda:serviceEvent" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A CCD SHALL contain exactly one ClinicalDocument / documentationOf / serviceEvent.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:documentationOf/cda:serviceEvent/@classCode='PCPR'"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:documentationOf/cda:serviceEvent/@classCode='PCPR'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "ClinicalDocument / documentationOf / serviceEvent / classCode" SHALL be "PCPR" "Care provision" 2.16.840.1.113883.5.6 ActClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:low"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:low"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>ClinicalDocument / documentationOf / serviceEvent SHALL contain exactly one serviceEvent / effectiveTime / low and exactly one serviveEvent / effectiveTime / high.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:high"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:high"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>ClinicalDocument / documentationOf / serviceEvent SHALL contain exactly one serviceEvent / effectiveTime / low and exactly one serviveEvent / effectiveTime / high.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:languageCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:languageCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>CCD SHALL contain exactly one ClinicalDocument / languageCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:templateId)&gt;=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:templateId)&gt;=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>CCD SHALL contain one or more ClinicalDocument / templateId.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:templateId[@root='2.16.840.1.113883.10.20.1' and not(@extension)]"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="cda:templateId[@root='2.16.840.1.113883.10.20.1' and not(@extension)]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>At least one ClinicalDocument / templateId SHALL value ClinicalDocument / templateId / @root with "2.16.840.1.113883.10.20.1", and SHALL NOT contain ClinicalDocument / templateId / @extension.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="string-length(cda:effectiveTime/@value) &gt; 18"/>
         <xsl:otherwise>
            <failed-assert id="" test="string-length(cda:effectiveTime/@value) &gt; 18" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>ClinicalDocument / effectiveTime SHALL be expressed with precision to include seconds.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:effectiveTime/@value,&#34;+-&#34;,&#34;ZZ&#34;),&#34;Z&#34;)"/>
         <xsl:otherwise>
            <failed-assert id="" test="contains(translate(cda:effectiveTime/@value,&#34;+-&#34;,&#34;ZZ&#34;),&#34;Z&#34;)"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>ClinicalDocument / effectiveTime SHALL include an explicit time zone offset.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:recordTarget)&lt;=2"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:recordTarget)&lt;=2" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>CCD shall contain one to two ClinicalDocument / recordTarget.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:author/cda:assignedAuthor/cda:assignedPerson | cda:author/cda:assignedAuthor/cda:representedOrganization"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="cda:author/cda:assignedAuthor/cda:assignedPerson | cda:author/cda:assignedAuthor/cda:representedOrganization"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>CCD SHALL contain one or more ClinicalDocument / author / assignedAuthor / assignedPerson and/or ClinicalDocument / author / assignedAuthor / representedOrganization.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.13&#34;]) &lt;= 1"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.13&#34;]) &lt;= 1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>CCD MAY contain exactly one and SHALL NOT contain more than one Purpose section (templateId 2.16.840.1.113883.10.20.1.13).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.9&#34;])  &lt;= 1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.9&#34;]) &lt;= 1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>CCD SHOULD contain exactly one and SHALL NOT contain more than one Payers section (templateId 2.16.840.1.113883.10.20.1.9).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.1&#34;])  &lt;= 1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.1&#34;]) &lt;= 1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>CCD SHOULD contain exactly one and SHALL NOT contain more than one Advance directives section (templateId 2.16.840.1.113883.10.20.1.1).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.11&#34;]) &lt;= 1"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.11&#34;]) &lt;= 1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>CCD SHOULD contain exactly one and SHALL NOT contain more than one Problem section (templateId 2.16.840.1.113883.10.20.1.11).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.7&#34;])  &lt;= 1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.7&#34;]) &lt;= 1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>CCD SHOULD contain exactly one and SHALL NOT contain more than one Medical Equipment section (templateId 2.16.840.1.113883.10.20.1.7).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.6&#34;])  &lt;= 1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.6&#34;]) &lt;= 1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>CCD SHOULD contain exactly one and SHALL NOT contain more than one Immunizations section (templateId 2.16.840.1.113883.10.20.1.6).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.9&#34;])  &lt;= 1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.9&#34;]) &lt;= 1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>CCD SHOULD contain exactly one and SHALL NOT contain more than one Payers section (templateId 2.16.840.1.113883.10.20.1.9).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.1&#34;])  &lt;= 1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.1&#34;]) &lt;= 1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>CCD SHOULD contain exactly one and SHALL NOT contain more than one Advance directives section (templateId 2.16.840.1.113883.10.20.1.1).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.5&#34;])  &lt;= 1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(.//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.5&#34;]) &lt;= 1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>CCD SHOULD contain exactly one and SHALL NOT contain more than one Functional status section (templateId 2.16.840.1.113883.10.20.1.5).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(.//cda:templateId[@root='2.16.840.1.113883.10.20.1.14']) &lt;= 1"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="count(.//cda:templateId[@root='2.16.840.1.113883.10.20.1.14']) &lt;= 1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>CCD SHOULD contain exactly one and SHALL NOT contain more than one Results section (templateId 2.16.840.1.113883.10.20.1.14).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(.//cda:templateId[@root='2.16.840.1.113883.10.20.1.12']) &lt;=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(.//cda:templateId[@root='2.16.840.1.113883.10.20.1.12']) &lt;=1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>CCD SHOULD contain exactly one and SHALL NOT contain more than one Procedures section (templateId 2.16.840.1.113883.10.20.1.12).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(.//cda:templateId[@root='2.16.840.1.113883.10.20.1.3'])  &lt;= 1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(.//cda:templateId[@root='2.16.840.1.113883.10.20.1.3']) &lt;= 1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>CCD SHOULD contain exactly one and SHALL NOT contain more than one Encounters section (templateId 2.16.840.1.113883.10.20.1.3).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M6"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M6"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.1&#34;]" priority="3999"
                 mode="M9">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.1&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:section"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:section" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The root of an Advance Directives section shall be 'section' in the HL7 namespace.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:text"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:text" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Advance Directives section SHALL contain a narrative block.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='42348-3'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='42348-3'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Section / code SHALL be '42348-3' 'Advance directives' 2.16.840.1.113883.6.1 LOINC STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:title"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:title" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Advance Directives section SHALL contain Section / title.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M9"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M9"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.2&#34;]" priority="3999"
                 mode="M12">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.2&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:section"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:section" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The root of a Alerts section shall be 'section' in the HL7 namespace.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:text"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:text" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Alerts section SHALL contain a narrative block.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The alert section SHALL contain Section / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:title"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:title" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The alert section SHALL contain Section / title.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M12"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M12"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.3&#34;]" priority="3999"
                 mode="M15">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.3&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:text"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:text" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Encounters section SHALL contain a narrative block.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='46240-8'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='46240-8'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Section / code" SHALL be "46240-8" "History of encounters" 2.16.840.1.113883.6.1 LOINC STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:title"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:title" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>encounters section SHALL contain Section / title.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M15"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M15"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.4&#34;]" priority="3999"
                 mode="M18">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.4&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:code[@code='10157-6'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='10157-6'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The family history section SHALL contain Section / code. The value for "Section / code" SHALL be "10157-6" "History of family member diseases" 2.16.840.1.113883.6.1 LOINC STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:title"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:title" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The family history section SHALL contain Section / title.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="not(cda:subject)"/>
         <xsl:otherwise>
            <failed-assert id="" test="not(cda:subject)" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The family history section SHALL NOT contain Section / subject.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M18"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M18"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.5&#34;]" priority="3999"
                 mode="M21">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.5&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:section"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:section" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The root of a Functional Status section shall be 'section' in the HL7 namespace.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:text"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:text" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Functional Status section SHALL contain a narrative block.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='47420-5'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='47420-5'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Section / code SHALL be '47420-5' 'Functional status assessment' 2.16.840.1.113883.6.1 LOINC STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:title"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:title" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Functional Status section SHALL contain Section / title.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M21"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M21"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.6&#34;]" priority="3999"
                 mode="M24">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.6&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:section"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:section" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The root of an Immunizations section shall be 'section' in the HL7 namespace.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:text"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:text" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Immunizations section SHALL contain a narrative block.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The immunizations section SHALL contain Section / code</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='11369-6'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='11369-6'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Section / code" SHALL be "11369-6" "History of immunizations" 2.16.840.1.113883.6.1 LOINC STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:title"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:title" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The immunizations section SHALL contain Section / title.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M24"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M24"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.7&#34;]" priority="3999"
                 mode="M27">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.7&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:section"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:section" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The root of a medical equipment section shall be 'section' in the HL7 namespace.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:text"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:text" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The medical equipment section SHALL contain a narrative block.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The medical equipment section SHALL contain Section / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='46264-8'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='46264-8'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for 'Section / code' SHALL be '46264-8' 'History of medical device use' 2.16.840.1.113883.6.1 LOINC STATIC</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:title"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:title" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The medical equipment section SHALL contain Section / title.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M27"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M27"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.8&#34;]" priority="3999"
                 mode="M30">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.8&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:section"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:section" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The root of a Medications section shall be 'section' in the HL7 namespace.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:text"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:text" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Medications section SHALL contain a narrative block.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The medications section SHALL contain Section / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='10160-0'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='10160-0'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Section / code" SHALL be "10160-0" "History of medication use" 2.16.840.1.113883.6.1 LOINC STATIC</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:title"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:title" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The medications section SHALL contain Section / title.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M30"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M30"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.9&#34;]" priority="3999"
                 mode="M33">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.9&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:section"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:section" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The root of a Payers section shall be 'section' in the HL7 namespace.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:text"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:text" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Payers section SHALL contain a narrative block.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='48768-6'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='48768-6'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Section / code" SHALL be "48768-6" "Payment sources" 2.16.840.1.113883.6.1 LOINC STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:title"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:title" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Payers section SHALL contain Section / title.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M33"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M33"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.10&#34;]" priority="3999"
                 mode="M36">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.10&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:section"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:section" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The root of a Plan of care section shall be 'section' in the HL7 namespace.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:text"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:text" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Plan of Care section SHALL contain a narrative block.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='18776-5'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='18776-5'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The plan of care section SHALL contain Section / code. The value for "Section / code" SHALL be "18776-5" "Treatment plan" 2.16.840.1.113883.6.1 LOINC STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:title"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:title" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The plan of care section SHALL contain Section / title.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M36"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M36"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.11&#34;]" priority="3999"
                 mode="M39">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.11&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:text"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:text" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Problem section SHALL contain a narrative block</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='11450-4'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='11450-4'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The problem section SHALL contain Section / code. The value for "Section / code" SHALL be "11450-4" "Problem list" 2.16.840.1.113883.6.1 LOINC STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:title"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:title" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The problem section SHALL contain Section / title.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M39"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M39"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.12&#34;]" priority="3999"
                 mode="M42">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.12&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:text"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:text" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Procedures section SHALL contain a narrative block.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='47519-4'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='47519-4'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The procedure section SHALL contain Section / code. The value for "Section / code" SHALL be "47519-4" "History of procedures" 2.16.840.1.113883.6.1 LOINC STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:title"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:title" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The procedure section SHALL contain Section / title.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M42"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.12&#34;]" priority="3998"
                 mode="M42">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.12&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:section"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:section" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The root of a Procedures section shall be 'section' in the HL7 namespace.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M42"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M42"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.13&#34;]" priority="3999"
                 mode="M45">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.13&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:section"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:section" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The root of a purpose section shall be 'section' in the HL7 namespace.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:text"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:text" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Purpose section SHALL contain a narrative block.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='48764-5'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='48764-5'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Section / code SHALL be "48764-5" "Summary purpose" 2.16.840.1.113883.6.1 LOINC STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:title"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:title" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Purpose section SHALL contain Section / title.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M45"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M45"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.14&#34;]" priority="3999"
                 mode="M48">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.14&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:section"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:section" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The root of a Results section shall be section in the HL7 namespace.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:text"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:text" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Results section SHALL contain a narrative block.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The result section SHALL contain Section / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='30954-2'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='30954-2'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Section / code SHALL be 30954-2 Relevant diagnostic tests and/or laboratory data 2.16.840.1.113883.6.1 LOINC STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:title"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:title" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The results section SHALL contain Section / title.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M48"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M48"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.15&#34;]" priority="3999"
                 mode="M51">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.15&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:section"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:section" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The root of a Social history section shall be 'section' in the HL7 namespace.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:text"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:text" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Social history section SHALL contain a narrative block.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The social history section SHALL contain Section / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='29762-2'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='29762-2'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Section / code" SHALL be "29762-2" "Social history" 2.16.840.1.113883.6.1 LOINC STATIC</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:title"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:title" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The social history section SHALL contain Section / title.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M51"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M51"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.16&#34;]" priority="3999"
                 mode="M54">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.16&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:section"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:section" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The root of a Vital Signs section shall be 'section' in the HL7 namespace.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:text"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:text" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Vital Signs section SHALL contain a narrative block.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The vital signs section SHALL contain Section / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='8716-3'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='8716-3'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Section / code" SHALL be "8716-3" "Vital signs" 2.16.840.1.113883.6.1 LOINC STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:title"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:title" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The vital signs section SHALL contain Section / title.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M54"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M54"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.17&#34;]" priority="3999"
                 mode="M57">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.17&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:observation"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:observation" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An advance directive observation (templateId 2.16.840.1.113883.10.20.1.17) SHALL be represented with Observation.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@classCode='OBS'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@classCode='OBS'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Observation / classCode in an advance directive observation SHALL be 'OBS' 2.16.840.1.113883.5.6 ActClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Observation / moodCode in an advance directive observation SHALL be 'EVN' 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An advance directive observation SHALL contain at least one Observation / id.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:statusCode"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:statusCode" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An advance directive observation SHALL contain exactly one Observation / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:statusCode/@code='completed'"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:statusCode/@code='completed'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Observation / statusCode in an advance directive observation SHALL be 'completed' 2.16.840.1.113883.5.14 ActStatus STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.37&#34;])=1"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="count(descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.37&#34;])=1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An advance directive observation SHALL contain exactly one advance directive status observation.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.36&#34;])&lt;=1"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="count(descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.36&#34;])&lt;=1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An advance directive observation MAY contain exactly one advance directive reference.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M57"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M57"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.18&#34;]" priority="3999"
                 mode="M60">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.18&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:observation"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:observation" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An alert observation (templateId 2.16.840.1.113883.10.20.1.18) SHALL be represented with Observation.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / moodCode" in an alert observation SHALL be "EVN" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An alert observation SHALL include exactly one Observation / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:statusCode[@code='completed']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:statusCode[@code='completed']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / statusCode" in an alert observation SHALL be "completed" 2.16.840.1.113883.5.14 ActStatus STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="not(cda:participant) or (count(cda:participant//cda:playingEntity)=1)"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="not(cda:participant) or (count(cda:participant//cda:playingEntity)=1)"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An agent participation in an alert observation SHALL contain exactly one participant / participantRole / playingEntity.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="not(cda:participant) or (cda:participant[@typeCode='CSM'])"/>
         <xsl:otherwise>
            <failed-assert id="" test="not(cda:participant) or (cda:participant[@typeCode='CSM'])" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>value for Observation / participant / typeCode in an agent participation SHALL be "CSM" "Consumable" 2.16.840.1.113883.5.90 ParticipationType STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="not(cda:participant) or (cda:participant/cda:participantRole[@classCode='MANU'])"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="not(cda:participant) or (cda:participant/cda:participantRole[@classCode='MANU'])"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Observation / participant / participantRole / classCode in an agent participation SHALL be "MANU" "Manufactured" 2.16.840.1.113883.5.110 RoleClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="not(cda:participant) or (cda:participant/cda:participantRole/cda:playingEntity[@classCode='MMAT'])"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="not(cda:participant) or (cda:participant/cda:participantRole/cda:playingEntity[@classCode='MMAT'])"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Observation / participant / participantRole / playingEntity / classCode in an agent participation SHALL be "MMAT" "Manufactured material" 2.16.840.1.113883.5.41 EntityClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="not(cda:participant) or (cda:participant/cda:participantRole/cda:playingEntity/cda:code)"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="not(cda:participant) or (cda:participant/cda:participantRole/cda:playingEntity/cda:code)"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An agent participation in an alert observation SHALL contain exactly one participant / participantRole / playingEntity / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M60"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.34&#34;]//*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.54&#34;]"
                 priority="3998"
                 mode="M60">
      <fired-rule id=""
                  context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.34&#34;]//*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.54&#34;]"
                  role=""/>
      <xsl:choose>
         <xsl:when test="parent::cda:entryRelationship/@typeCode='MFST'"/>
         <xsl:otherwise>
            <failed-assert id="" test="parent::cda:entryRelationship/@typeCode='MFST'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "entryRelationship / typeCode" in a relationship between an alert observation and reaction observation SHALL be "MFST" "Is manifestation of" 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M60"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M60"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.19&#34;]" priority="3999"
                 mode="M63">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.19&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:act"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:act" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An authorization activity (templateId 2.16.840.1.113883.10.20.1.19) SHALL be represented with Act.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@classCode='ACT'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@classCode='ACT'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Act / classCode in an authorization activity SHALL be 'ACT' 2.16.840.1.113883.5.6 ActClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An authorization activity SHALL contain at least one Act / id.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Act / moodCode in an authorization activity SHALL be 'EVN' 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:entryRelationship"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:entryRelationship" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An authorization activity SHALL contain one or more Act / entryRelationship.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M63"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root='2.16.840.1.113883.10.20.1.19']/cda:entryRelationship"
                 priority="3998"
                 mode="M63">
      <fired-rule id=""
                  context="*[cda:templateId/@root='2.16.840.1.113883.10.20.1.19']/cda:entryRelationship"
                  role=""/>
      <xsl:choose>
         <xsl:when test="@typeCode='SUBJ'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@typeCode='SUBJ'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Act / entryRelationship / typeCode in an authorization activity SHALL be 'SUBJ' 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="*[@moodCode='PRMS']"/>
         <xsl:otherwise>
            <failed-assert id="" test="*[@moodCode='PRMS']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The target of an authorization activity with Act / entryRelationship / @typeCode='SUBJ' SHALL be a clinical statement with moodCode = 'PRMS' (Promise).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M63"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M63"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.20&#34;]" priority="3999"
                 mode="M66">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.20&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:act"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:act" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A coverage activity (templateId 2.16.840.1.113883.10.20.1.20) SHALL be represented with Act.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="self::cda:act[@classCode='ACT']"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:act[@classCode='ACT']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Act / classCode in a coverage activity SHALL be 'ACT' 2.16.840.1.113883.5.6 ActClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="self::cda:act[@moodCode='DEF']"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:act[@moodCode='DEF']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Act / moodCode in a coverage activity SHALL be "DEF" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A coverage activity SHALL contain at least one Act / id.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode) = 1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode) = 1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A coverage activity SHALL contain exactly one Act / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:statusCode[@code='completed']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:statusCode[@code='completed']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Act / statusCode in a coverage activity SHALL be 'completed' 2.16.840.1.113883.5.14 ActStatus STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='48768-6'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='48768-6'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for 'Act / code' in a coverage activity SHALL be '48768-6' 'Payment sources' 2.16.840.1.113883.6.1 LOINC STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:entryRelationship"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:entryRelationship" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A coverage activity SHALL contain one or more Act / entryRelationship.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="not(cda:entryRelationship[cda:sequenceNumber[position()=2]])"/>
         <xsl:otherwise>
            <failed-assert id="" test="not(cda:entryRelationship[cda:sequenceNumber[position()=2]])"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An entryRelationship in a coverage activity MAY contain exactly one entryRelationship / sequenceNumber, which serves to order the payment sources.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="not(cda:entryRelationship[not(@typeCode='COMP')])"/>
         <xsl:otherwise>
            <failed-assert id="" test="not(cda:entryRelationship[not(@typeCode='COMP')])" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Act / entryRelationship / typeCode in a coverage activity SHALL be "COMP" 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M66"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root='2.16.840.1.113883.10.20.1.20']/cda:entryRelationship[@typeCode='COMP']"
                 priority="3998"
                 mode="M66">
      <fired-rule id=""
                  context="*[cda:templateId/@root='2.16.840.1.113883.10.20.1.20']/cda:entryRelationship[@typeCode='COMP']"
                  role=""/>
      <xsl:choose>
         <xsl:when test="*/cda:templateId[@root='2.16.840.1.113883.10.20.1.26']"/>
         <xsl:otherwise>
            <failed-assert id="" test="*/cda:templateId[@root='2.16.840.1.113883.10.20.1.26']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The target of a coverage activity with Act / entryRelationship / @typeCode='COMP' SHALL be a policy activity (templateId 2.16.840.1.113883.10.20.1.26).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M66"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M66"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.21&#34;]" priority="3999"
                 mode="M69">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.21&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:encounter"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:encounter" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An encounter activity (templateId 2.16.840.1.113883.10.20.1.21) SHALL be represented with Encounter.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@classCode='ENC'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@classCode='ENC'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Encounter / classCode" in an encounter activity SHALL be "ENC" 2.16.840.1.113883.5.6 ActClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Encounter / moodCode" in an encounter activity SHALL be "EVN" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:id) &gt;= 1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:id) &gt;= 1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An encounter activity SHALL contain at least one Encounter / id.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:effectiveTime) &lt; 2"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:effectiveTime) &lt; 2" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An encounter activity MAY contain exactly one Encounter / effectiveTime, to indicate date, time, and/or duration of an encounter.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M69"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.21&#34;]/cda:entryRelationship"
                 priority="3998"
                 mode="M69">
      <fired-rule id=""
                  context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.21&#34;]/cda:entryRelationship"
                  role=""/>
      <xsl:choose>
         <xsl:when test="@typeCode='RSON'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@typeCode='RSON'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An encounter activity MAY contain one or more Encounter / entryRelationship, whose value for "entryRelationship / typeCode" SHALL be "RSON" "Has reason" 2.16.840.1.113883.5.1002 ActRelationshipType STATIC, where the target of the relationship represents the indication for the activity.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M69"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.21&#34;]/cda:performer"
                 priority="3997"
                 mode="M69">
      <fired-rule id=""
                  context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.21&#34;]/cda:performer"
                  role=""/>
      <xsl:choose>
         <xsl:when test="count(./cda:assignedEntity/cda:code) &lt; 2"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(./cda:assignedEntity/cda:code) &lt; 2" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Encounter / performer MAY contain exactly one Encounter / performer / assignedEntity / code, to define the role of the practioner.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M69"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M69"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.22&#34;]" priority="3999"
                 mode="M72">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.22&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:observation"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:observation" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A family history observation (templateId 2.16.840.1.113883.10.20.1.22) SHALL be represented with Observation.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / moodCode" in a family history observation SHALL be "EVN" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A family history observation SHALL contain at least one Observation / id.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A family history observation SHALL include exactly one Observation / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:statusCode/@code='completed'"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:statusCode/@code='completed'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / statusCode" in a family history observation SHALL be "completed" 2.16.840.1.113883.5.14 ActStatus STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M72"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M72"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.23&#34;]" priority="3999"
                 mode="M75">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.23&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:organizer"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:organizer" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A family history organizer (templateId 2.16.840.1.113883.10.20.1.23) SHALL be represented with Organizer.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@classCode='CLUSTER'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@classCode='CLUSTER'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Organizer / classCode" in a family history organizer SHALL be "CLUSTER" 2.16.840.1.113883.5.6 ActClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Organizer / moodCode" in a family history organizer SHALL be "EVN" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A family history organizer SHALL contain exactly one Organizer / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:statusCode/@code='completed'"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:statusCode/@code='completed'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Organizer / statusCode" in a family history organizer SHALL be "completed" 2.16.840.1.113883.5.14 ActStatus STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:component"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:component" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A family history organizer SHALL contain one or more Organizer / component.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:subject)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:subject)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A family history organizer SHALL contain exactly one subject participant, representing the family member who is the subject of the family history observations.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:subject/cda:relatedSubject)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:subject/cda:relatedSubject)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A subject participant SHALL contain exactly one RelatedSubject, representing the relationship of the subject to the patient.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:subject/cda:relatedSubject/@classCode='PRS'"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:subject/cda:relatedSubject/@classCode='PRS'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "RelatedSubject / classCode" SHALL be "PRS" "Personal relationship" 2.16.840.1.113883.5.110 RoleClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:subject/cda:relatedSubject/cda:code)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:subject/cda:relatedSubject/cda:code)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>RelatedSubject SHALL contain exactly one RelatedSubject / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M75"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M75"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.24&#34;]" priority="3999"
                 mode="M78">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.24&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:substanceAdministration"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:substanceAdministration" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A medication activity (templateId 2.16.840.1.113883.10.20.1.24) SHALL be represented with SubstanceAdministration</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN' or @moodCode='INT'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN' or @moodCode='INT'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "SubstanceAdministration / moodCode" in a medication activity SHALL be "EVN" or "INT" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A medication activity SHALL contain at least one SubstanceAdministration / id.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M78"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.24&#34;]/cda:entryRelationship[cda:observation/cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.54&#34;]"
                 priority="3998"
                 mode="M78">
      <fired-rule id=""
                  context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.24&#34;]/cda:entryRelationship[cda:observation/cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.54&#34;]"
                  role=""/>
      <xsl:choose>
         <xsl:when test="@typeCode='CAUS'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@typeCode='CAUS'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "entryRelationship / typeCode" in a relationship between a medication activity and reaction observation SHALL be "CAUS" "Is etiology for" 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M78"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M78"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.25&#34;]" priority="3999"
                 mode="M81">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.25&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:act or self::cda:encounter or self::cda:observation or self::cda:procedure or self::cda:substanceAdministration or self::cda:supply"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="self::cda:act or self::cda:encounter or self::cda:observation or self::cda:procedure or self::cda:substanceAdministration or self::cda:supply"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A plan of care activity (templateId 2.16.840.1.113883.10.20.1.25) SHALL be represented with Act, Encounter, Observation, Procedure, SubstanceAdministration, or Supply.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A plan of care activity SHALL contain at least one [Act | Encounter | Observation | Procedure | SubstanceAdministration | Supply] / id.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(@moodCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(@moodCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A plan of care activity SHALL contain exactly one [Act | Encounter | Observation | Procedure | SubstanceAdministration | Supply] / moodCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="self::cda:act or self::cda:encounter or self::cda:observation or self::cda:procedure or self::cda:substanceAdministration or self::cda:supply"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="self::cda:act or self::cda:encounter or self::cda:observation or self::cda:procedure or self::cda:substanceAdministration or self::cda:supply"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A plan of care activity (templateId 2.16.840.1.113883.10.20.1.25) SHALL be represented with Act, Encounter, Observation, Procedure, SubstanceAdministration, or Supply.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A plan of care activity SHALL contain at least one [Act | Encounter | Observation | Procedure | SubstanceAdministration | Supply] / id.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(@moodCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(@moodCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A plan of care activity SHALL contain exactly one [Act | Encounter | Observation | Procedure | SubstanceAdministration | Supply] / moodCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:if test="(self::cda:act or self::cda:encounter or self::cda:procedure) and not (@moodCode='INT' or @moodCode='ARQ' or @moodCode='PRMS' or @moodCode='PRP' or @moodCode='RQO')">
         <successful-report id=""
                            test="(self::cda:act or self::cda:encounter or self::cda:procedure) and not (@moodCode='INT' or @moodCode='ARQ' or @moodCode='PRMS' or @moodCode='PRP' or @moodCode='RQO')"
                            role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text>The value for "[Act | Encounter | Procedure] / moodCode" in a plan of care activity SHALL be ["INT" (intent) | "ARQ" (appointment request) | "PRMS" (promise) | "PRP" (proposal) | "RQO" (request)] 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
         </successful-report>
      </xsl:if>
      <xsl:if test="(self::cda:substanceAdministration or self::cda:supply) and not (@moodCode='INT' or @moodCode='PRMS' or @moodCode='PRP' or @moodCode='RQO')">
         <successful-report id=""
                            test="(self::cda:substanceAdministration or self::cda:supply) and not (@moodCode='INT' or @moodCode='PRMS' or @moodCode='PRP' or @moodCode='RQO')"
                            role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text>The value for "[SubstanceAdministration | Supply] / moodCode" in a plan of care activity SHALL be ["INT" (intent) | "PRMS" (promise) | "PRP" (proposal) | "RQO" (request)] 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
         </successful-report>
      </xsl:if>
      <xsl:if test="(self::cda:observation) and not (@moodCode='INT' or @moodCode='PRMS'or @moodCode='PRP' or @moodCode='RQO' or @moodCode='GOL')">
         <successful-report id=""
                            test="(self::cda:observation) and not (@moodCode='INT' or @moodCode='PRMS'or @moodCode='PRP' or @moodCode='RQO' or @moodCode='GOL')"
                            role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text>The value for "Observation / moodCode" in a plan of care activity SHALL be ["INT" (intent) | "PRMS" (promise) | "PRP" (proposal) | "RQO" (request) | "GOL" (goal)] 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M81"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M81"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.26&#34;]" priority="3999"
                 mode="M84">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.26&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:act"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:act" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A policy activity (templateId 2.16.840.1.113883.10.20.1.26) SHALL be represented with Act.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="self::cda:act[@classCode='ACT']"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:act[@classCode='ACT']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Act / classCode in a policy activity SHALL be 'ACT' 2.16.840.1.113883.5.6 ActClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="self::cda:act[@moodCode='EVN']"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:act[@moodCode='EVN']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Act / moodCode in a policy activity SHALL be "EVN" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A policy activity SHALL contain at least one Act / id, which represents the group or contract number related to the insurance policy or program.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode) = 1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode) = 1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A coverage policy SHALL contain exactly one Act / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:statusCode[@code='completed']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:statusCode[@code='completed']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Act / statusCode in a policy activity SHALL be 'completed' 2.16.840.1.113883.5.14 ActStatus STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:performer[@typeCode='PRF'])=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:performer[@typeCode='PRF'])=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A policy activity SHALL contain exactly one Act / performer [@typeCode="PRF"], representing the payer.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:participant[@typeCode='COV'])=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:participant[@typeCode='COV'])=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A policy activity SHALL contain exactly one Act / participant [@typeCode="COV"], representing the covered party.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:participant[@typeCode='COV']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:participant[@typeCode='COV']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A policy activity SHALL contain exactly one Act / participant [@typeCode="COV"], representing the covered party.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:participant[@typeCode='HLD'])&lt;=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:participant[@typeCode='HLD'])&lt;=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A policy activity MAY contain exactly one Act / participant [@typeCode='HLD'], representing the subscriber.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="not(cda:entryRelationship[not(@typeCode='REFR')])"/>
         <xsl:otherwise>
            <failed-assert id="" test="not(cda:entryRelationship[not(@typeCode='REFR')])" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Act / entryRelationship / typeCode in a policy activity SHALL be 'REFR' 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M84"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.26&#34;]/cda:participant[@typeCode=&#34;COV&#34;]"
                 priority="3998"
                 mode="M84">
      <fired-rule id=""
                  context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.26&#34;]/cda:participant[@typeCode=&#34;COV&#34;]"
                  role=""/>
      <xsl:choose>
         <xsl:when test="count(cda:time)&lt;=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:time)&lt;=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A covered party in a policy activity MAY contain exactly one participant / time, to represent the time period over which the patient is covered.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M84"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root='2.16.840.1.113883.10.20.1.26']/cda:entryRelationship[@typeCode='REFR']"
                 priority="3997"
                 mode="M84">
      <fired-rule id=""
                  context="*[cda:templateId/@root='2.16.840.1.113883.10.20.1.26']/cda:entryRelationship[@typeCode='REFR']"
                  role=""/>
      <xsl:choose>
         <xsl:when test="*[cda:templateId/@root='2.16.840.1.113883.10.20.1.19'] | cda:act[@classCode='ACT'][@moodCode='DEF']"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="*[cda:templateId/@root='2.16.840.1.113883.10.20.1.19'] | cda:act[@classCode='ACT'][@moodCode='DEF']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The target of a policy activity with Act / entryRelationship / @typeCode='REFR' SHALL be an authorization activity (templateId 2.16.840.1.113883.10.20.1.19) or an Act, with Act [@classCode = 'ACT'] and Act [@moodCode = 'DEF'], representing a description of the coverage plan.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M84"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root='2.16.840.1.113883.10.20.1.26']/cda:entryRelationship[@typeCode='REFR']/cda:act[@classCode='ACT'][@moodCode='DEF']"
                 priority="3996"
                 mode="M84">
      <fired-rule id=""
                  context="*[cda:templateId/@root='2.16.840.1.113883.10.20.1.26']/cda:entryRelationship[@typeCode='REFR']/cda:act[@classCode='ACT'][@moodCode='DEF']"
                  role=""/>
      <xsl:choose>
         <xsl:when test="cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A description of the coverage plan SHALL contain one or more Act / Id, to represent the plan identifier.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M84"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M84"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.27&#34;]" priority="3999"
                 mode="M87">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.27&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:act[@classCode='ACT'][@moodCode='EVN']"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:act[@classCode='ACT'][@moodCode='EVN']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A problem act (templateId 2.16.840.1.113883.10.20.1.27) SHALL be represented with Act. The value for "Act / classCode" in a problem act SHALL be "ACT" 2.16.840.1.113883.5.6 ActClass STATIC. The value for "Act / moodCode" in a problem act SHALL be "EVN" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A problem act SHALL contain at least one Act / id.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code/@nullFlavor='NA'"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code/@nullFlavor='NA'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Act / code / @NullFlavor" in a problem act SHALL be "NA" "Not applicable" 2.16.840.1.113883.5.1008 NullFlavor STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:effectiveTime) &lt; 2"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:effectiveTime) &lt; 2" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A problem act MAY contain exactly one Act / effectiveTime, to indicate the timing of the concern (e.g. the time the problem was noted).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:entryRelationship"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:entryRelationship" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A problem act SHALL contain one or more Act / entryRelationship.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:templateId[@root='2.16.840.1.113883.10.20.1.41']) &lt; 2"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.1.41']) &lt; 2"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A problem act MAY contain exactly one episode observation. The template identifier for an episode observation is 2.16.840.1.113883.10.20.1.41.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:templateId[@root='2.16.840.1.113883.10.20.1.48']) &lt; 2"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.1.48']) &lt; 2"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A problem act MAY contain exactly one patient awareness.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M87"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M87"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.28&#34;]" priority="3999"
                 mode="M90">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.28&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:observation[@moodCode='EVN']"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:observation[@moodCode='EVN']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A problem observation (templateId 2.16.840.1.113883.10.20.1.28) SHALL be represented with Observation. The value for "Observation / moodCode" in a problem observation SHALL be "EVN" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A problem observation SHALL include exactly one Observation / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:statusCode/@code='completed'"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:statusCode/@code='completed'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / statusCode" in a problem observation SHALL be "completed" 2.16.840.1.113883.5.14 ActStatus STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:templateId[@root='2.16.840.1.113883.10.20.1.50']) &lt; 2"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.1.50']) &lt; 2"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A problem observation MAY contain exactly one problem status observation. The template identifier for a problem status observation is 2.16.840.1.113883.10.20.1.50.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:templateId[@root='2.16.840.1.113883.10.20.1.51']) &lt; 2"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.1.51']) &lt; 2"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A problem observation MAY contain exactly one problem healthstatus observation. The template identifier for a problem healthstatus observation is 2.16.840.1.113883.10.20.1.51.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:templateId[@root='2.16.840.1.113883.10.20.1.48']) &lt; 2"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.1.48']) &lt; 2"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A problem observation MAY contain exactly one patient awareness.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M90"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M90"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.29&#34;]" priority="3999"
                 mode="M93">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.29&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:procedure or self::cda:act or self::cda:observation"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:procedure or self::cda:act or self::cda:observation"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A procedure activity (templateId 2.16.840.1.113883.10.20.1.29) SHALL be represented with Act, Observation, or Procedure.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "[Act | Observation | Procedure] / moodCode" in a procedure activity SHALL be "EVN" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A procedure activity SHALL contain at least one [Act | Observation | Procedure] / id.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A procedure activity SHALL contain exactly one [Act | Observation | Procedure] / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:code)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:code)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A procedure activity SHALL contain exactly one [Act | Observation | Procedure] / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M93"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M93"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.30&#34;]" priority="3999"
                 mode="M96">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.30&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:act"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:act" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A purpose activity (templateId 2.16.840.1.113883.10.20.1.30) SHALL be represented with Act.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="self::cda:act[@classCode='ACT']"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:act[@classCode='ACT']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Act / classCode in a purpose activity SHALL be 'ACT' 2.16.840.1.113883.5.6 ActClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="self::cda:act[@moodCode='EVN']"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:act[@moodCode='EVN']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Act / moodCode in a purpose activity SHALL be 'EVN' 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode) = 1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode) = 1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A purpose activity SHALL contain exactly one Act / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:statusCode[@code='completed']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:statusCode[@code='completed']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Act / statusCode in a purpose activity SHALL be 'completed' 2.16.840.1.113883.5.14 ActStatus STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='23745001'][@codeSystem='2.16.840.1.113883.6.96']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='23745001'][@codeSystem='2.16.840.1.113883.6.96']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A purpose activity SHALL contain exactly one Act / code, with a value of '23745001' 'Documentation procedure' 2.16.840.1.113883.6.96 SNOMED CT STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:entryRelationship/@typeCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:entryRelationship/@typeCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A purpose activity SHALL contain exactly one Act / entryRelationship / typeCode, with a value of 'RSON' 'Has reason' 2.16.840.1.113883.5.1002 ActRelationshipType STATIC, to indicate the reason or purpose for creating the CCD.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:entryRelationship[@typeCode='RSON']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:entryRelationship[@typeCode='RSON']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A purpose activity SHALL contain exactly one Act / entryRelationship / typeCode, with a value of 'RSON' 'Has reason' 2.16.840.1.113883.5.1002 ActRelationshipType STATIC, to indicate the reason or purpose for creating the CCD.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:entryRelationship[@typeCode='RSON']/cda:act | cda:entryRelationship[@typeCode='RSON']/cda:encounter | cda:entryRelationship[@typeCode='RSON']/cda:observation | cda:entryRelationship[@typeCode='RSON']/cda:procedure | cda:entryRelationship[@typeCode='RSON']/cda:substanceAdministration | cda:entryRelationship[@typeCode='RSON']/cda:supply"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="cda:entryRelationship[@typeCode='RSON']/cda:act | cda:entryRelationship[@typeCode='RSON']/cda:encounter | cda:entryRelationship[@typeCode='RSON']/cda:observation | cda:entryRelationship[@typeCode='RSON']/cda:procedure | cda:entryRelationship[@typeCode='RSON']/cda:substanceAdministration | cda:entryRelationship[@typeCode='RSON']/cda:supply"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The target of Act / entryRelationship / typeCode in a purpose activity SHALL be an Act, Encounter, Observation, Procedure, SubstanceAdministration, or Supply.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M96"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M96"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.31&#34;]" priority="3999"
                 mode="M99">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.31&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:observation"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:observation" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A result observation must be represented with the observation.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / moodCode" in a result observation SHALL be EVN.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A result observation SHALL contain at least one Observation / id.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A result observation SHALL contain exactly one Observation / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:code)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:code)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A result observation SHALL contain exactly one Observation / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:value)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:value)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A result observation SHALL contain exactly one Observation / value</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="not(cda:referenceRange/cda:observationRange/cda:code)"/>
         <xsl:otherwise>
            <failed-assert id="" test="not(cda:referenceRange/cda:observationRange/cda:code)" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A result observation SHALL NOT contain Observation / referenceRange / observationRange / code, as this attribute is not used by the HL7 Clinical Statement or Lab Committee models.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M99"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.31&#34;]/cda:value[@xsi:type=&#34;PQ&#34;]"
                 priority="3998"
                 mode="M99">
      <fired-rule id=""
                  context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.31&#34;]/cda:value[@xsi:type=&#34;PQ&#34;]"
                  role=""/>
      <xsl:choose>
         <xsl:when test="@unit"/>
         <xsl:otherwise>
            <failed-assert id="" test="@unit" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Where Observation / value is a physical quantity, the unit of measure SHALL be expressed using a valid UCUM expression.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M99"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M99"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.32&#34;]" priority="3999"
                 mode="M102">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.32&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:organizer"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:organizer" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A result organizer (templateId 2.16.840.1.113883.10.20.1.32) SHALL be represented with Organizer.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Organizer / moodCode in a result organizer SHALL be EVN.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A result organizer SHALL contain at least one Organizer / id.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A result organizer SHALL contain exactly one Organizer / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:code)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:code)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A result organizer SHALL contain exactly one Organizer / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:component"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:component" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A result organizer SHALL contain one or more Organizer / component.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test=".//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.31&#34;]"/>
         <xsl:otherwise>
            <failed-assert id="" test=".//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.31&#34;]" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Each result organizers SHALL contain one or more result observations (templateId 2.16.840.1.113883.10.20.1.31).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:component/cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.1.31']"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="cda:component/cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.1.31']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The target of one or more result organizer Organizer / component relationships SHALL be a result observation.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M102"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M102"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.33&#34;]" priority="3999"
                 mode="M105">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.33&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:observation"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:observation" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A social history observation (templateId 2.16.840.1.113883.10.20.1.33) SHALL be represented with Observation.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@classCode='OBS'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@classCode='OBS'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / classCode" in a social history observation SHALL be "OBS" 2.16.840.1.113883.5.6 ActClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / moodCode" in a social history observation SHALL be "EVN" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A social history observation SHALL contain at least one Observation / id.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A social history observation SHALL include exactly one Observation / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:statusCode[@code='completed']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:statusCode[@code='completed']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / statusCode" in a social history observation SHALL be "completed" 2.16.840.1.113883.5.14 ActStatus STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M105"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M105"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.34&#34;]" priority="3999"
                 mode="M108">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.34&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:supply"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:supply" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A supply activity (templateId 2.16.840.1.113883.10.20.1.34) SHALL be represented with Supply.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN' or @moodCode='INT'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN' or @moodCode='INT'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Supply / moodCode" in a medication activity SHALL be "EVN" or "INT" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A supply activity SHALL contain at least one Supply / id.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M108"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.34&#34;]//*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.43&#34;]"
                 priority="3998"
                 mode="M108">
      <fired-rule id=""
                  context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.34&#34;]//*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.43&#34;]"
                  role=""/>
      <xsl:choose>
         <xsl:when test="parent::cda:entryRelationship/@typeCode='SUBJ'"/>
         <xsl:otherwise>
            <failed-assert id="" test="parent::cda:entryRelationship/@typeCode='SUBJ'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "entryRelationship / typeCode" in a relationship between a supply activity and fulfillment instruction SHALL be "SUBJ" "Subject" 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M108"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M108"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.35&#34;]" priority="3999"
                 mode="M111">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.35&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:component/cda:observation[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.31&#34;]"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="cda:component/cda:observation[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.31&#34;]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Each vital signs organizers SHALL contain one or more result observations (templateId 2.16.840.1.113883.10.20.1.31)</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M111"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M111"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.36&#34;]" priority="3999"
                 mode="M114">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.36&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:externalDocument"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:externalDocument" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An advance directive reference (templateId 2.16.840.1.113883.10.20.1.36) SHALL be represented with Observation / reference / ExternalDocument.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="parent::cda:reference/@typeCode='REFR'"/>
         <xsl:otherwise>
            <failed-assert id="" test="parent::cda:reference/@typeCode='REFR'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Observation / reference / typeCode in an advance directive reference SHALL be 'REFR' 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>ExternalDocument SHALL contain at least one ExternalDocument / id.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M114"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M114"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.37&#34;]" priority="3999"
                 mode="M117">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.37&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M117"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M117"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.38&#34;]" priority="3999"
                 mode="M120">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.38&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:observation[@classCode='OBS'][@moodCode='EVN']"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:observation[@classCode='OBS'][@moodCode='EVN']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An age observation (templateId 2.16.840.1.113883.10.20.1.38) SHALL be represented with Observation. The value for "Observation / classCode" in an age observation SHALL be "OBS" 2.16.840.1.113883.5.6 ActClass STATIC. The value for "Observation / moodCode" in an age observation SHALL be "EVN" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code/@code='397659008'"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code/@code='397659008'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / code" in an age observation SHALL be "397659008" "Age" 2.16.840.1.113883.6.96 SNOMED CT STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode)=1 and cda:statusCode/@code='completed'"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode)=1 and cda:statusCode/@code='completed'"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An age observation SHALL include exactly one Observation / statusCode. The value for "Observation / statusCode" in an age observation SHALL be "completed" 2.16.840.1.113883.5.14 ActStatus STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:value)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:value)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An age observation SHALL include exactly one Observation / value.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M120"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M120"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.39&#34;]" priority="3999"
                 mode="M123">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.39&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="document('voc.xml')/systems/system[@codeSystemName='SNOMED CT'][@group='AlertStatusCode']/code[@value = current()/cda:value/@code]"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="document('voc.xml')/systems/system[@codeSystemName='SNOMED CT'][@group='AlertStatusCode']/code[@value = current()/cda:value/@code]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / value" in an alert status observation SHALL be selected from ValueSet 2.16.840.1.113883.1.11.20.3 AlertStatusCode STATIC 20061017.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M123"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M123"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.40&#34;]" priority="3999"
                 mode="M126">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.40&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:act"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:act" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A comment (templateId 2.16.840.1.113883.10.20.1.40) SHALL be represented with Act.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@classCode='ACT'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@classCode='ACT'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Act / classCode" in a comment SHALL be "ACT" 2.16.840.1.113883.5.6 ActClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Act / moodCode" in a comment SHALL be "EVN" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:code)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:code)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A comment SHALL contain exactly one Act / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M126"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M126"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.41&#34;]" priority="3999"
                 mode="M129">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.41&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:observation[@classCode='OBS' and @moodCode='EVN']"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:observation[@classCode='OBS' and @moodCode='EVN']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An episode observation (templateId 2.16.840.1.113883.10.20.1.41) SHALL be represented with Observation. The value for "Observation / classCode" in an episode observation SHALL be "OBS" 2.16.840.1.113883.5.6 ActClass STATIC. The value for "Observation / moodCode" in an episode observation SHALL be "EVN" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode)=1 and cda:statusCode/@code='completed'"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode)=1 and cda:statusCode/@code='completed'"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An episode observation SHALL include exactly one Observation / statusCode. The value for "Observation / statusCode" in an episode observation SHALL be "completed" 2.16.840.1.113883.5.14 ActStatus STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M129"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M129"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.42&#34;]" priority="3999"
                 mode="M132">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.42&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:entryRelationship[@typeCode='CAUS']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:entryRelationship[@typeCode='CAUS']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A family history cause of death observation SHALL contain one or more entryRelationship / typeCode. The value for at least one "entryRelationship / typeCode" in a family history cause of death observation SHALL be "CAUS" "is etiology for" 2.16.840.1.113883.5.1002 ActRelationshipType STATIC, with a target family history observation of death.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M132"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M132"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.43&#34;]" priority="3999"
                 mode="M135">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.43&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:act"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:act" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A fulfillment instruction (templateId 2.16.840.1.113883.10.20.1.43) SHALL be represented with Act.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='INT'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='INT'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Act / moodCode" in a fulfillment instruction SHALL be "INT" "Intent" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M135"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M135"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.45&#34;]" priority="3999"
                 mode="M138">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.45&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:participant[@typeCode='LOC']"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:participant[@typeCode='LOC']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A location participation (templateId 2.16.840.1.113883.10.20.1.45) SHALL be represented with the participant participation.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:participantRole) = 1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:participantRole) = 1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A location participation SHALL contain exactly one participant / participantRole.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:participantRole/@code) &lt; 2"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:participantRole/@code) &lt; 2" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Participant / participantRole in a location participation MAY contain exactly one participant / participantRole / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:participantRole/@classCode = 'SDLOC'"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:participantRole/@classCode = 'SDLOC'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "participant / participantRole / classCode" in a location participation SHALL be "SDLOC" "Service delivery location" 2.16.840.1.113883.5.110 RoleClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:participantRole/playingEntity) &lt; 2"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:participantRole/playingEntity) &lt; 2" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Participant / participantRole in a location participation MAY contain exactly one participant / participantRole / playingEntity.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:participantRole/cda:playingEntity/@classCode = 'PLC'"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:participantRole/cda:playingEntity/@classCode = 'PLC'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "participant / participantRole / playingEntity / classCode" in a location participation SHALL be "PLC" "Place" 2.16.840.1.113883.5.41 EntityClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M138"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M138"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.46&#34;]" priority="3999"
                 mode="M141">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.46&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:observation"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:observation" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A medication series number observation (templateId 2.16.840.1.113883.10.20.1.46) SHALL be represented with Observation.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@classCode='OBS'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@classCode='OBS'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / classCode" in a medication series number observation SHALL be "OBS" 2.16.840.1.113883.5.6 ActClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / moodCode" in a medication series number observation SHALL be "EVN" 2.16.840.1.113883.5.1001 ActMood STATIC</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A medication series number observation SHALL include exactly one Observation / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:code)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:code)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A medication series number observation SHALL contain exactly one Observation / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='30973-2'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='30973-2'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / code" in a medication series number observation SHALL be "30973-2" "Dose number" 2.16.840.1.113883.6.1 LOINC STATIC</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:value)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:value)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A medication series number observation SHALL contain exactly one Observation / value.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:value[@xsi:type='INT']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:value[@xsi:type='INT']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The data type for "Observation / value" in a medication series number observation SHALL be INT (integer).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M141"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root='2.16.840.1.113883.10.20.1.24']/cda:entryRelationship[cda:observation/cda:templateId/@root='2.16.840.1.113883.10.20.1.46']"
                 priority="3998"
                 mode="M141">
      <fired-rule id=""
                  context="*[cda:templateId/@root='2.16.840.1.113883.10.20.1.24']/cda:entryRelationship[cda:observation/cda:templateId/@root='2.16.840.1.113883.10.20.1.46']"
                  role=""/>
      <xsl:choose>
         <xsl:when test="@typeCode='SUBJ'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@typeCode='SUBJ'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "entryRelationship / typeCode" in a relationship between a medication activity and medication series number observation SHALL be "SUBJ" "Subject" 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M141"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M141"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.47&#34;]" priority="3999"
                 mode="M144">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.47&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M144"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M144"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.48&#34;]" priority="3999"
                 mode="M147">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.48&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:participant[@typeCode='SBJ']"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:participant[@typeCode='SBJ']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Patient awareness (templateId 2.16.840.1.113883.10.20.1.48) of a problem, observation, or other clinical statement SHALL be represented with participant. The value for "participant / typeCode" in a patient awareness SHALL be "SBJ" "Subject" 2.16.840.1.113883.5.90 ParticipationType STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:awarenessCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:awarenessCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Patient awareness SHALL contain exactly one participant / awarenessCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:participantRole/cda:id)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:participantRole/cda:id)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Patient awareness SHALL contain exactly one participant / participantRole / id</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole//cda:id[@root=cda:participantRole/cda:id/@root]"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="/cda:ClinicalDocument/cda:recordTarget/cda:patientRole//cda:id[@root=cda:participantRole/cda:id/@root]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>(The value of the participant/participantRole/id) SHALL also be present in ClinicalDocument / recordTarget / patientRole / id.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M147"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M147"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.49&#34;]" priority="3999"
                 mode="M150">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.49&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:act"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:act" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A patient instruction (templateId 2.16.840.1.113883.10.20.1.49) SHALL be represented with Act.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='INT'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='INT'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Act / moodCode" in a patient instruction SHALL be "INT" "Intent" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="parent::cda:entryRelationship/@typeCode='SUBJ'"/>
         <xsl:otherwise>
            <failed-assert id="" test="parent::cda:entryRelationship/@typeCode='SUBJ'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "entryRelationship / typeCode" in a relationship to a patient instruction SHALL be "SUBJ" "Subject" 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M150"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M150"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.51&#34;]" priority="3999"
                 mode="M153">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.51&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:code/@code='11323-3'"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code/@code='11323-3'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>... the value for "Observation / code" in a problem healthstatus observation SHALL be "11323-3" "Health status" 2.16.840.1.113883.6.1 LOINC STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="document('voc.xml')/systems/system[@codeSystemName='SNOMED CT'][@group='ProblemHealthStatusCode']/code[@value = current()/cda:value/@code]"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="document('voc.xml')/systems/system[@codeSystemName='SNOMED CT'][@group='ProblemHealthStatusCode']/code[@value = current()/cda:value/@code]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / value" in a problem healthstatus observation SHALL be selected from ValueSet 2.16.840.1.113883.1.11.20.12 ProblemHealthStatusCode STATIC 20061017.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M153"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M153"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.50&#34;]" priority="3999"
                 mode="M156">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.50&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="document('voc.xml')/systems/system[@codeSystemName='SNOMED CT'][@group='ProblemStatusCode']/code[@value = current()/cda:value/@code]"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="document('voc.xml')/systems/system[@codeSystemName='SNOMED CT'][@group='ProblemStatusCode']/code[@value = current()/cda:value/@code]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / value" in a problem status observation SHALL be selected from ValueSet 2.16.840.1.113883.1.11.20.13 ProblemStatusCode STATIC 20061017.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M156"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M156"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.53&#34;]" priority="3999"
                 mode="M159">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.53&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:manufacturedProduct"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:manufacturedProduct" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A product (templateId 2.16.840.1.113883.10.20.1.53) SHALL be represented with the ManufacturedProduct class.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:manufacturedMaterial/cda:code) = 1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:manufacturedMaterial/cda:code) = 1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A ManufacturedProduct in a product template SHALL contain exactly one manufacturedMaterial/code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:manufacturedMaterial/cda:code/cda:originalText) = 1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:manufacturedMaterial/cda:code/cda:originalText) = 1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A manufacturedMaterial in a product template SHALL contain exactly one Material / code / originalText, which represents the generic name of the product.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M159"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M159"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.52&#34;]" priority="3999"
                 mode="M162">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.52&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:participantRole"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:participantRole" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A product instance (templateId 2.16.840.1.113883.10.20.1.52) SHALL be represented with the ParticipantRole class.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@classCode='MANU'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@classCode='MANU'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "participantRole / classCode" in a product instance SHALL be "MANU" "Manufactured product" 2.16.840.1.113883.5.110 RoleClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M162"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M162"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.54&#34;]" priority="3999"
                 mode="M165">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.54&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:observation"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:observation" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A reaction observation (templateId 2.16.840.1.113883.10.20.1.54) SHALL be represented with Observation.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@classCode='OBS'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@classCode='OBS'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / classCode" in a reaction observation SHALL be "OBS" 2.16.840.1.113883.5.6 ActClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / moodCode" in a reaction observation SHALL be "EVN" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A reaction observation SHALL include exactly one Observation / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:statusCode[@code='completed']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:statusCode[@code='completed']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / statusCode" in a reaction observation SHALL be "completed" 2.16.840.1.113883.5.14 ActStatus STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M165"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M165"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.55&#34;]" priority="3999"
                 mode="M168">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.55&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:observation"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:observation" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A severity observation (templateId 2.16.840.1.113883.10.20.1.55) SHALL be represented with Observation.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@classCode='OBS'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@classCode='OBS'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / classCode" in a severity observation SHALL be "OBS" 2.16.840.1.113883.5.6 ActClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / moodCode" in a severity observation SHALL be "EVN" 2.16.840.1.113883.5.1001 ActMood STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A severity observation SHALL include exactly one Observation / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:statusCode[@code='completed']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:statusCode[@code='completed']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / statusCode" in a severity observation SHALL be "completed" 2.16.840.1.113883.5.14 ActStatus STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:code)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:code)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A severity observation SHALL contain exactly one Observation / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='SEV']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='SEV']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / code" in a severity observation SHALL be "SEV" "Severity observation" 2.16.840.1.113883.5.4 ActCode STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:value)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:value)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A severity observation SHALL contain exactly one Observation / value.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M168"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.54&#34;]/cda:entryRelationship[cda:observation/cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.55&#34;]"
                 priority="3998"
                 mode="M168">
      <fired-rule id=""
                  context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.54&#34;]/cda:entryRelationship[cda:observation/cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.55&#34;]"
                  role=""/>
      <xsl:choose>
         <xsl:when test="@typeCode='SUBJ'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@typeCode='SUBJ'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "entryRelationship / typeCode" in a relationship between a reaction observation and severity observation SHALL be "SUBJ" "Has subject" 2.16.840.1.113883.5.1002 ActRelationshipType STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M168"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M168"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.56&#34;]" priority="3999"
                 mode="M171">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.56&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="document('voc.xml')/systems/system[@codeSystemName='SNOMED CT'][@group='SocialHistoryStatusCode']/code[@value = current()/cda:value/@code]"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="document('voc.xml')/systems/system[@codeSystemName='SNOMED CT'][@group='SocialHistoryStatusCode']/code[@value = current()/cda:value/@code]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / value" in a social history status observation SHALL be selected from ValueSet 2.16.840.1.113883.1.11.20.17 SocialHistoryStatusCode STATIC 20061017.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M171"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M171"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.57&#34;]" priority="3999"
                 mode="M174">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.57&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:observation"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:observation" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A status observation (templateId 2.16.840.1.113883.10.20.1.57) SHALL be represented with Observation.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="parent::cda:entryRelationship/@typeCode='REFR'"/>
         <xsl:otherwise>
            <failed-assert id="" test="parent::cda:entryRelationship/@typeCode='REFR'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A status observation SHALL be the target of an entryRelationship whos value for "entryRelationship / typeCode" SHALL be "REFR" 2.16.840.1.113883.5.1002 ActRelationshipType STATIC</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@classCode='OBS'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@classCode='OBS'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / classCode" in a status observation SHALL be "OBS" 2.16.840.1.113883.5.6 ActClass STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@moodCode='EVN'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@moodCode='EVN'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / moodCode" in a status observation SHALL be "EVN" 2.16.840.1.113883.5.1001 ActMood STATIC</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:code)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:code)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A status observation SHALL contain exactly one Observation / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@code='33999-4'][@codeSystem='2.16.840.1.113883.6.1']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code[@code='33999-4'][@codeSystem='2.16.840.1.113883.6.1']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / code" in a status observation SHALL be "33999-4" "Status" 2.16.840.1.113883.6.1 LOINC STATIC</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A status observation SHALL contain exactly one Observation / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:statusCode[@code='completed']"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:statusCode[@code='completed']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / statusCode" in a status observation SHALL be "completed" 2.16.840.1.113883.5.14 ActStatus STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="(count(@*)=2) and @classCode and @moodCode"/>
         <xsl:otherwise>
            <failed-assert id="" test="(count(@*)=2) and @classCode and @moodCode" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A status observation SHALL NOT contain any additional Observation attributes.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:participant)=0"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:participant)=0" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A status observation SHALL NOT contain any Observation participants.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M174"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M174"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.44&#34;]" priority="3999"
                 mode="M177">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.44&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M177"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M177"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.58&#34;]" priority="3999"
                 mode="M180">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.58&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="self::cda:participant"/>
         <xsl:otherwise>
            <failed-assert id="" test="self::cda:participant" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A verification of an advance directive observation (templateId 2.16.840.1.113883.10.20.1.58) SHALL be represented with Observation / participant.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="@typeCode='VRF'"/>
         <xsl:otherwise>
            <failed-assert id="" test="@typeCode='VRF'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Observation / participant / typeCode in a verification SHALL be 'VRF' 'Verifier' 2.16.840.1.113883.5.90 ParticipationType STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M180"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M180"/>
   <xsl:template match="text()" priority="-1"/>
</xsl:stylesheet>