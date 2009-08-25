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
                         phase="warning">
         <ns uri="urn:hl7-org:v3" prefix="cda"/>
         <ns uri="http://www.w3.org/2001/XMLSchema-instance" prefix="xsi"/>
         <active-pattern name="CCD v1.0 Templates Root - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M7"/>
         <active-pattern name="Advance directives section - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M10"/>
         <active-pattern name="Alerts section - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M13"/>
         <active-pattern name="Encounters section - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M16"/>
         <active-pattern name="Family history section - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M19"/>
         <active-pattern name="Functional status section - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M22"/>
         <active-pattern name="Immunizations section - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M25"/>
         <active-pattern name="Medical equipment section - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M28"/>
         <active-pattern name="Medications section - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M31"/>
         <active-pattern name="Payers section - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M34"/>
         <active-pattern name="Plan of care section - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M37"/>
         <active-pattern name="Problem section - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M40"/>
         <active-pattern name="Procedures section - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M43"/>
         <active-pattern name="Purpose section - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M46"/>
         <active-pattern name="Results section - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M49"/>
         <active-pattern name="Social history section - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M52"/>
         <active-pattern name="Vital signs section - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M55"/>
         <active-pattern name="Advance directive observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M58"/>
         <active-pattern name="Alert observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M61"/>
         <active-pattern name="Authorization activity - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M64"/>
         <active-pattern name="Coverage activity - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M67"/>
         <active-pattern name="Encounter activity - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M70"/>
         <active-pattern name="Family history observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M73"/>
         <active-pattern name="Family history organizer - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M76"/>
         <active-pattern name="Medication activity - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M79"/>
         <active-pattern name="Plan of care activity - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M82"/>
         <active-pattern name="Policy activity - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M85"/>
         <active-pattern name="Problem act - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M88"/>
         <active-pattern name="Problem observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M91"/>
         <active-pattern name="Procedure activity - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M94"/>
         <active-pattern name="Purpose activity - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M97"/>
         <active-pattern name="Result observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M100"/>
         <active-pattern name="Result organizer - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M103"/>
         <active-pattern name="Social history observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M106"/>
         <active-pattern name="Supply activity - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M109"/>
         <active-pattern name="Vital signs organizer - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M112"/>
         <active-pattern name="Advance directive reference - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M115"/>
         <active-pattern name="Advance directive status observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M118"/>
         <active-pattern name="Age observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M121"/>
         <active-pattern name="Alert status observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M124"/>
         <active-pattern name="Comment - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M127"/>
         <active-pattern name="Episode observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M130"/>
         <active-pattern name="Family history cause of death observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M133"/>
         <active-pattern name="Fulfillment instruction - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M136"/>
         <active-pattern name="Location participation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M139"/>
         <active-pattern name="Medication series number observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M142"/>
         <active-pattern name="Medication status observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M145"/>
         <active-pattern name="Patient awareness - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M148"/>
         <active-pattern name="Patient instruction - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M151"/>
         <active-pattern name="Problem healthstatus observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M154"/>
         <active-pattern name="Problem status observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M157"/>
         <active-pattern name="Product - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M160"/>
         <active-pattern name="Product instance - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M163"/>
         <active-pattern name="Reaction observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M166"/>
         <active-pattern name="Severity observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M169"/>
         <active-pattern name="Social history status observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M172"/>
         <active-pattern name="Status observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M175"/>
         <active-pattern name="Status of functional status observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M178"/>
         <active-pattern name="Verification of an advance directive observation - warning validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M181"/>
      </schematron-output>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1&#34;]" priority="3999"
                 mode="M7">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M7"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M7"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.1&#34;]" priority="3999"
                 mode="M10">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.1&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.17&#34;]"/>
         <xsl:otherwise>
            <failed-assert id="" test="descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.17&#34;]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Purpose section SHOULD include one or more advance directive observations (templateId 2.16.840.1.113883.10.20.1.17)</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.17&#34;]/cda:code[@code=&#34;304251008&#34;][@codeSystem=&#34;2.16.840.1.113883.6.96&#34;]"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.17&#34;]/cda:code[@code=&#34;304251008&#34;][@codeSystem=&#34;2.16.840.1.113883.6.96&#34;]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>There SHOULD be an advance directive observation whos value for Observation / code is '304251008' 'Resuscitation status' 2.16.840.1.113883.6.96 SNOMED CT STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'advance directives')"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'advance directives')"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Section / title SHOULD be valued with a case-insensitive language-insensitive text string containing "advance directives".</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M10"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M10"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.2&#34;]" priority="3999"
                 mode="M13">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.2&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="descendant::*[cda:templateId/@root='2.16.840.1.113883.10.20.1.27']"/>
         <xsl:otherwise>
            <failed-assert id="" test="descendant::*[cda:templateId/@root='2.16.840.1.113883.10.20.1.27']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Alerts section SHOULD include one or more problem acts (templateId 2.16.840.1.113883.10.20.1.27).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'alert') or contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'allergies and adverse reactions')"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'alert') or contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'allergies and adverse reactions')"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Section / title SHOULD be valued with a case-insensitive language-insensitive text string containing "alert" and/or "allergies and adverse reactions".</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M13"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root='2.16.840.1.113883.10.20.1.27'][ancestor::*[cda:templateId/@root='2.16.840.1.113883.10.20.1.2']]"
                 priority="3998"
                 mode="M13">
      <fired-rule id=""
                  context="*[cda:templateId/@root='2.16.840.1.113883.10.20.1.27'][ancestor::*[cda:templateId/@root='2.16.840.1.113883.10.20.1.2']]"
                  role=""/>
      <xsl:choose>
         <xsl:when test="descendant::*[cda:templateId/@root='2.16.840.1.113883.10.20.1.18']"/>
         <xsl:otherwise>
            <failed-assert id="" test="descendant::*[cda:templateId/@root='2.16.840.1.113883.10.20.1.18']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A problem act SHOULD include one or more alert observations (templateId 2.16.840.1.113883.10.20.1.18).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M13"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M13"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.3&#34;]" priority="3999"
                 mode="M16">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.3&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test=".//cda:templateId[@root='2.16.840.1.113883.10.20.1.21']"/>
         <xsl:otherwise>
            <failed-assert id="" test=".//cda:templateId[@root='2.16.840.1.113883.10.20.1.21']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Encounters SHOULD include one or more encounter activities (templateId 2.16.840.1.113883.10.20.1.21).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'encounters')"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'encounters')"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Section / title SHOULD be valued with a case-insensitive language-insensitive text string containing "encounters".</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M16"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M16"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.4&#34;]" priority="3999"
                 mode="M19">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.4&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'family history')"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'family history')"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Section / title SHOULD be valued with a case-insensitive language-insensitive text string containing "family history".</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M19"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M19"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.5&#34;]" priority="3999"
                 mode="M22">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.5&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.27&#34;] | descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.32&#34;]"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.27&#34;] | descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.32&#34;]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Functional Status section SHOULD include one or more problem acts (templateId 2.16.840.1.113883.10.20.1.27) and/or result organizers (templateId 2.16.840.1.113883.10.20.1.32).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'functional status')"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'functional status')"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Section / title SHOULD be valued with a case-insensitive language-insensitive text string containing "functional status".</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M22"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M22"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.6&#34;]" priority="3999"
                 mode="M25">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.6&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.24&#34;]|descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.34&#34;]"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.24&#34;]|descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.34&#34;]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Immunizations section SHOULD include one or more medication activities (templateId 2.16.840.1.113883.10.20.1.24) and/or supply activities (templateId 2.16.840.1.113883.10.20.1.34).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'immunization')"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'immunization')"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Section / title SHOULD be valued with a case-insensitive language-insensitive text string containing "immunization".</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M25"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M25"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.7&#34;]" priority="3999"
                 mode="M28">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.7&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.34&#34;]"/>
         <xsl:otherwise>
            <failed-assert id="" test="descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.34&#34;]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The medical equipment section SHOULD include one or more supply activities (templateId 2.16.840.1.113883.10.20.1.34)</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'equipment')"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'equipment')"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Section / title SHOULD be valued with a case-insensitive language-insensitive text string containing "equipment".</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M28"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M28"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.8&#34;]" priority="3999"
                 mode="M31">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.8&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="descendant::*[cda:templateId/@root='2.16.840.1.113883.10.20.1.24' or cda:templateId/@root='2.16.840.1.113883.10.20.1.34' ]"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="descendant::*[cda:templateId/@root='2.16.840.1.113883.10.20.1.24' or cda:templateId/@root='2.16.840.1.113883.10.20.1.34' ]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Medications section SHOULD include one or more medication activities (templateId 2.16.840.1.113883.10.20.1.24) and/or supply activities (templateId 2.16.840.1.113883.10.20.1.34)</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'medication')"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'medication')"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Section / title SHOULD be valued with a case-insensitive language-insensitive text string containing "medication".</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M31"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M31"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.9&#34;]" priority="3999"
                 mode="M34">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.9&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.20&#34;]"/>
         <xsl:otherwise>
            <failed-assert id="" test="descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.20&#34;]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Payers section SHOULD include one or more coverage activities (templateId 2.16.840.1.113883.10.20.1.20).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'insurance') or contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'payers')"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'insurance') or contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'payers')"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Section / title SHOULD be valued with a case-insensitive language-insensitive text string containing "insurance" or "payers".</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M34"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M34"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.10&#34;]" priority="3999"
                 mode="M37">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.10&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="descendant::*[cda:templateId/@root='2.16.840.1.113883.10.20.1.25']"/>
         <xsl:otherwise>
            <failed-assert id="" test="descendant::*[cda:templateId/@root='2.16.840.1.113883.10.20.1.25']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Plan of Care SHOULD contain clinical statements. Clinical statements SHALL include one or more plan of care activities (templateId 2.16.840.1.113883.10.20.1.25)</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'plan')"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'plan')"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Section / title SHOULD be valued with a case-insensitive language-insensitive text string containing "plan".</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M37"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M37"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.11&#34;]" priority="3999"
                 mode="M40">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.11&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test=".//cda:templateId[@root='2.16.840.1.113883.10.20.1.27']"/>
         <xsl:otherwise>
            <failed-assert id="" test=".//cda:templateId[@root='2.16.840.1.113883.10.20.1.27']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Clinical statements SHOULD include one or more problem acts (templateId 2.16.840.1.113883.10.20.1.27).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'problem')"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'problem')"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Section / title SHOULD be valued with a case-insensitive language-insensitive text string containing "problem".</text>
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
      <xsl:apply-templates mode="M40"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M40"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.12&#34;]" priority="3999"
                 mode="M43">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.12&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test=".//cda:templateId[@root='2.16.840.1.113883.10.20.1.29']"/>
         <xsl:otherwise>
            <failed-assert id="" test=".//cda:templateId[@root='2.16.840.1.113883.10.20.1.29']" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The procedure section SHOULD contain clinical statements, which SHOULD include one or more procedure activities (templateId 2.16.840.1.113883.10.20.1.29).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'procedures')"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'procedures')"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Section / title SHOULD be valued with a case-insensitive language-insensitive text string containing "procedures".</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M43"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M43"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.13&#34;]" priority="3999"
                 mode="M46">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.13&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.30&#34;]"/>
         <xsl:otherwise>
            <failed-assert id="" test="descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.30&#34;]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Purpose section SHOULD include one or more purpose activities (templateId 2.16.840.1.113883.10.20.1.30).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'purpose')"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'purpose')"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Section / title SHOULD be valued with a case-insensitive language-insensitive text string containing "purpose".</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M46"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M46"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.14&#34;]" priority="3999"
                 mode="M49">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.14&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test=".//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.32&#34;]"/>
         <xsl:otherwise>
            <failed-assert id="" test=".//cda:templateId[@root=&#34;2.16.840.1.113883.10.20.1.32&#34;]" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Results section SHOULD include one or more result organizers (templateId 2.16.840.1.113883.10.20.1.32).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'results')"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'results')"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Section / title SHOULD be valued with a case-insensitive language-insensitive text string containing "results".</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M49"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M49"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.15&#34;]" priority="3999"
                 mode="M52">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.15&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="descendant::*[cda:templateId/@root='2.16.840.1.113883.10.20.1.33']"/>
         <xsl:otherwise>
            <failed-assert id="" test="descendant::*[cda:templateId/@root='2.16.840.1.113883.10.20.1.33']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Social history section SHOULD include one or more social history observations (templateId 2.16.840.1.113883.10.20.1.33)</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'social history')"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'social history')"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Section / title SHOULD be valued with a case-insensitive language-insensitive text string containing "social history".</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M52"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M52"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.16&#34;]" priority="3999"
                 mode="M55">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.16&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.35&#34;]"/>
         <xsl:otherwise>
            <failed-assert id="" test="descendant::*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.35&#34;]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The Vital Signs section SHOULD include one or more vital signs organizers (templateId 2.16.840.1.113883.10.20.1.35)</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'vital signs')"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="contains(translate(cda:title,'QWERTYUIOPASDFGHJKLZXCVBNM','qwertyuiopasdfghjklzxcvbnm'),'vital signs')"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>Section / title SHOULD be valued with a case-insensitive language-insensitive text string containing "vital signs".</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M55"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M55"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.17&#34;]" priority="3999"
                 mode="M58">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.17&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M58"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M58"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.18&#34;]" priority="3999"
                 mode="M61">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.18&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:participant"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:participant" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An alert observation SHOULD contain at least one Observation / participant, representing the agent that is the cause of the allergy or adverse reaction.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:participant/cda:participantRole/cda:playingEntity/cda:code[@codeSystem='2.16.840.1.113883.6.88' or @codeSystem='2.16.840.1.113883.6.59' ]"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="cda:participant/cda:participantRole/cda:playingEntity/cda:code[@codeSystem='2.16.840.1.113883.6.88' or @codeSystem='2.16.840.1.113883.6.59' ]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "participant / participantRole / playingEntity / code" in an agent participation SHOULD be selected from the RxNorm (2.16.840.1.113883.6.88) code system for medications, and from the CDC Vaccine Code (2.16.840.1.113883.6.59) code system for immunizations.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M61"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M61"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.19&#34;]" priority="3999"
                 mode="M64">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.19&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M64"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M64"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.20&#34;]" priority="3999"
                 mode="M67">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.20&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M67"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M67"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.21&#34;]" priority="3999"
                 mode="M70">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.21&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="count(cda:code) = 1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:code) = 1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>An encounter activity SHOULD contain exactly one Encounter / code.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M70"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M70"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.22&#34;]" priority="3999"
                 mode="M73">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.22&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:effectiveTime"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:effectiveTime" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A family history observation SHOULD include Observation / effectiveTime.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M73"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M73"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.23&#34;]" priority="3999"
                 mode="M76">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.23&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:component[cda:observation/cda:templateId/@root='2.16.840.1.113883.10.20.1.22']"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="cda:component[cda:observation/cda:templateId/@root='2.16.840.1.113883.10.20.1.22']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The target of a family history organizer Organizer / component relationship SHOULD be a family history observation, but MAY be some other clinical statement.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:subject/cda:relatedSubject/cda:subject)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:subject/cda:relatedSubject/cda:subject)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>RelatedSubject SHOULD contain exactly one RelatedSubject / subject.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:subject/cda:relatedSubject/cda:subject/cda:administrativeGenderCode)=1"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="count(cda:subject/cda:relatedSubject/cda:subject/cda:administrativeGenderCode)=1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>RelatedSubject / subject SHOULD contain exactly one RelatedSubject / subject / administrativeGenderCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:subject/cda:relatedSubject/cda:subject/cda:birthTime)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:subject/cda:relatedSubject/cda:subject/cda:birthTime)=1"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>RelatedSubject / subject SHOULD contain exactly one RelatedSubject / subject / birthTime.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M76"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M76"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.24&#34;]" priority="3999"
                 mode="M79">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.24&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A medication activity SHOULD contain exactly one SubstanceAdministration / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:effectiveTime"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:effectiveTime" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A medication activity SHOULD contain one or more SubstanceAdministration / effectiveTime elements, used to indicate the actual or intended start and stop date of a medication, and the frequency of administration.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:routeCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:routeCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A medication activity SHOULD contain exactly one SubstanceAdministration / routeCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:doseQuantity)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:doseQuantity)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A medication activity SHOULD contain exactly one SubstanceAdministration / doseQuantity.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M79"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M79"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.25&#34;]" priority="3999"
                 mode="M82">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.25&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M82"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M82"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.26&#34;]" priority="3999"
                 mode="M85">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.26&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:code"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A policy activity SHOULD contain zero to one Act / code, which represents the type of coverage.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M85"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.26&#34;]/cda:participant[@typeCode=&#34;COV&#34;]"
                 priority="3998"
                 mode="M85">
      <fired-rule id=""
                  context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.26&#34;]/cda:participant[@typeCode=&#34;COV&#34;]"
                  role=""/>
      <xsl:choose>
         <xsl:when test="cda:participantRole/cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:participantRole/cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A covered party in a policy activity SHOULD contain one or more participant / participantRole / id, to represent the patient's member or subscriber identifier with respect to the payer.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:participantRole/cda:code"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:participantRole/cda:code" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A covered party in a policy activity SHOULD contain exactly one participant / participantRole / code, to represent the reason for coverage (e.g. Self, Family dependent, student).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M85"/>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.26&#34;]/cda:participant[@typeCode=&#34;HLD&#34;]"
                 priority="3997"
                 mode="M85">
      <fired-rule id=""
                  context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.26&#34;]/cda:participant[@typeCode=&#34;HLD&#34;]"
                  role=""/>
      <xsl:choose>
         <xsl:when test="cda:participantRole/cda:id"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:participantRole/cda:id" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A subscriber in a policy activity SHOULD contain one or more participant / participantRole / id, to represent the subscriber's identifier with respect to the payer.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M85"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M85"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.27&#34;]" priority="3999"
                 mode="M88">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.27&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:entryRelationship[@typeCode='SUBJ']/cda:observation/cda:templateId[@root='2.16.840.1.113883.10.20.1.28']                 or cda:entryRelationship[@typeCode='SUBJ']/cda:observation/cda:templateId[@root='2.16.840.1.113883.10.20.1.18']"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="cda:entryRelationship[@typeCode='SUBJ']/cda:observation/cda:templateId[@root='2.16.840.1.113883.10.20.1.28'] or cda:entryRelationship[@typeCode='SUBJ']/cda:observation/cda:templateId[@root='2.16.840.1.113883.10.20.1.18']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The target of a problem act with Act / entryRelationship / @typeCode="SUBJ" SHOULD be a problem observation (in the Problem section) or alert observation (in the Alert section, see section 3.9 Alerts), but MAY be some other clinical statement.</text>
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
      <xsl:apply-templates mode="M88"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M88"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.28&#34;]" priority="3999"
                 mode="M91">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.28&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="count(cda:effectiveTime)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:effectiveTime)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A problem observation SHOULD contain exactly one Observation / effectiveTime, to indicate the timing of condition (e.g. the time the condition started, the onset of the illness or symptom).</text>
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
      <xsl:apply-templates mode="M91"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M91"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.29&#34;]" priority="3999"
                 mode="M94">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.29&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="count(cda:effectiveTime)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:effectiveTime)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A procedure activity SHOULD contain exactly one [Act | Observation | Procedure] / effectiveTime.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:code[@codeSystem='2.16.840.1.113883.6.1' or @codeSystem='2.16.840.1.113883.6.96']"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="cda:code[@codeSystem='2.16.840.1.113883.6.1' or @codeSystem='2.16.840.1.113883.6.96']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "[Act | Observation | Procedure] / code" in a procedure activity SHOULD be selected from LOINC (codeSystem 2.16.840.1.113883.6.1) or SNOMED CT (codeSystem 2.16.840.1.113883.6.96).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M94"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M94"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.30&#34;]" priority="3999"
                 mode="M97">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.30&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M97"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M97"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.31&#34;]" priority="3999"
                 mode="M100">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.31&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:code[@codeSystem='2.16.840.1.113883.6.1' or @codeSystem='2.16.840.1.113883.6.96' ]"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="cda:code[@codeSystem='2.16.840.1.113883.6.1' or @codeSystem='2.16.840.1.113883.6.96' ]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / code" in a result observation SHOULD be selected from LOINC (codeSystem 2.16.840.1.113883.6.1) or SNOMED CT (codeSystem 2.16.840.1.113883.6.96).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:effectiveTime)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:effectiveTime)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A result observation SHOULD contain exactly one Observation / effectiveTime, which represents the biologically relevant time (e.g. time the specimen was obtained from the patient).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:interpretationCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:interpretationCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A result observation SHOULD contain exactly one Observation / interpretationCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="cda:referenceRange"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:referenceRange" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A result observation SHOULD contain one or more Observation / referenceRange to show the normal range of values for the observation result.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M100"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M100"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.32&#34;]" priority="3999"
                 mode="M103">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.32&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:code[@codeSystem='2.16.840.1.113883.6.1' or @codeSystem='2.16.840.1.113883.6.96']"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="cda:code[@codeSystem='2.16.840.1.113883.6.1' or @codeSystem='2.16.840.1.113883.6.96']"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for Organizer / code in a result organizer SHOULD be selected from LOINC (codeSystem 2.16.840.1.113883.6.1) or SNOMED CT (codeSystem 2.16.840.1.113883.6.96)</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M103"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M103"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.33&#34;]" priority="3999"
                 mode="M106">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.33&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:code[@codeSystem='2.16.840.1.113883.6.1' or @codeSystem='2.16.840.1.113883.6.96' ]"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="cda:code[@codeSystem='2.16.840.1.113883.6.1' or @codeSystem='2.16.840.1.113883.6.96' ]"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / code" in a social history observation SHOULD be selected from LOINC (codeSystem 2.16.840.1.113883.6.1) or SNOMED CT (codeSystem 2.16.840.1.113883.6.96).</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M106"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M106"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.34&#34;]" priority="3999"
                 mode="M109">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.34&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="count(cda:statusCode)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:statusCode)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A supply activity SHOULD contain exactly one Supply / statusCode.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:choose>
         <xsl:when test="count(cda:effectiveTime)=1"/>
         <xsl:otherwise>
            <failed-assert id="" test="count(cda:effectiveTime)=1" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A supply activity SHOULD contain exactly one Supply / effectiveTime, to indicate the actual or intended time of dispensing.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M109"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M109"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.35&#34;]" priority="3999"
                 mode="M112">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.35&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M112"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M112"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.36&#34;]" priority="3999"
                 mode="M115">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.36&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M115"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M115"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.37&#34;]" priority="3999"
                 mode="M118">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.37&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M118"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M118"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.38&#34;]" priority="3999"
                 mode="M121">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.38&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M121"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M121"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.39&#34;]" priority="3999"
                 mode="M124">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.39&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M124"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M124"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.40&#34;]" priority="3999"
                 mode="M127">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.40&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M127"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M127"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.41&#34;]" priority="3999"
                 mode="M130">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.41&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:code/@code='ASSERTION'"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:code/@code='ASSERTION'" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for "Observation / Code" in an episode observation SHOULD be "ASSERTION" 2.16.840.1.113883.5.4 ActCode STATIC.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M130"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M130"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.42&#34;]" priority="3999"
                 mode="M133">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.42&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M133"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M133"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.43&#34;]" priority="3999"
                 mode="M136">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.43&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M136"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M136"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.45&#34;]" priority="3999"
                 mode="M139">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.45&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M139"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M139"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.46&#34;]" priority="3999"
                 mode="M142">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.46&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M142"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M142"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.47&#34;]" priority="3999"
                 mode="M145">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.47&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M145"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M145"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.48&#34;]" priority="3999"
                 mode="M148">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.48&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M148"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M148"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.49&#34;]" priority="3999"
                 mode="M151">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.49&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M151"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M151"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.51&#34;]" priority="3999"
                 mode="M154">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.51&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M154"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M154"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.50&#34;]" priority="3999"
                 mode="M157">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.50&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M157"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M157"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.53&#34;]" priority="3999"
                 mode="M160">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.53&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="   cda:manufacturedMaterial/cda:code/@codeSystem = '2.16.840.1.113883.6.88'    or cda:manufacturedMaterial/cda:code/@codeSystem = '2.16.840.1.113883.6.59'   or cda:manufacturedMaterial/cda:code/@codeSystem = '2.16.840.1.113883.6.96'"/>
         <xsl:otherwise>
            <failed-assert id=""
                           test="cda:manufacturedMaterial/cda:code/@codeSystem = '2.16.840.1.113883.6.88' or cda:manufacturedMaterial/cda:code/@codeSystem = '2.16.840.1.113883.6.59' or cda:manufacturedMaterial/cda:code/@codeSystem = '2.16.840.1.113883.6.96'"
                           role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>The value for manufacturedMaterial / code in a product template SHOULD be selected from the RxNorm (2.16.840.1.113883.6.88) code system for medications, and from the CDC Vaccine Code (2.16.840.1.113883.6.59) code system for immunizations, or MAY be selected from ValueSet 2.16.840.1.113883.1.11.20.8 MedicationTypeCode STATIC 20061017.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M160"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M160"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.52&#34;]/cda:id"
                 priority="3999"
                 mode="M163">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.52&#34;]/cda:id"
                  role=""/>
      <xsl:choose>
         <xsl:when test="../cda:scopingEntity"/>
         <xsl:otherwise>
            <failed-assert id="" test="../cda:scopingEntity" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>If participantRole in a product instance contains participantRole / id, then participantRole SHOULD also contain participantRole / scopingEntity.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M163"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M163"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.54&#34;]" priority="3999"
                 mode="M166">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.54&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M166"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M166"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.55&#34;]" priority="3999"
                 mode="M169">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.55&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M169"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M169"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.56&#34;]" priority="3999"
                 mode="M172">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.56&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M172"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M172"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.57&#34;]" priority="3999"
                 mode="M175">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.57&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M175"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M175"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.44&#34;]" priority="3999"
                 mode="M178">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.44&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M178"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M178"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.58&#34;]" priority="3999"
                 mode="M181">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.58&#34;]" role=""/>
      <xsl:choose>
         <xsl:when test="cda:time"/>
         <xsl:otherwise>
            <failed-assert id="" test="cda:time" role="">
               <xsl:attribute name="location">
                  <xsl:apply-templates select="." mode="schematron-get-full-path"/>
               </xsl:attribute>
               <text>A verification of an advance directive observation SHOULD contain Observation / participant / time.</text>
            </failed-assert>
         </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates mode="M181"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M181"/>
   <xsl:template match="text()" priority="-1"/>
</xsl:stylesheet>