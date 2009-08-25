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
                         phase="manual">
         <ns uri="urn:hl7-org:v3" prefix="cda"/>
         <ns uri="http://www.w3.org/2001/XMLSchema-instance" prefix="xsi"/>
         <active-pattern name="CCD v1.0 Templates Root - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M8"/>
         <active-pattern name="Advance directives section - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M11"/>
         <active-pattern name="Alerts section - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M14"/>
         <active-pattern name="Encounters section - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M17"/>
         <active-pattern name="Family history section - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M20"/>
         <active-pattern name="Functional status section - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M23"/>
         <active-pattern name="Immunizations section - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M26"/>
         <active-pattern name="Medical equipment section - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M29"/>
         <active-pattern name="Medications section - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M32"/>
         <active-pattern name="Payers section - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M35"/>
         <active-pattern name="Plan of care section - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M38"/>
         <active-pattern name="Problem section - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M41"/>
         <active-pattern name="Procedures section - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M44"/>
         <active-pattern name="Purpose section - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M47"/>
         <active-pattern name="Results section - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M50"/>
         <active-pattern name="Social history section - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M53"/>
         <active-pattern name="Vital signs section - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M56"/>
         <active-pattern name="Advance directive observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M59"/>
         <active-pattern name="Alert observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M62"/>
         <active-pattern name="Authorization activity - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M65"/>
         <active-pattern name="Coverage activity - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M68"/>
         <active-pattern name="Encounter activity - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M71"/>
         <active-pattern name="Family history observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M74"/>
         <active-pattern name="Family history organizer - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M77"/>
         <active-pattern name="Medication activity - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M80"/>
         <active-pattern name="Plan of care activity - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M83"/>
         <active-pattern name="Policy activity - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M86"/>
         <active-pattern name="Problem act - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M89"/>
         <active-pattern name="Problem observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M92"/>
         <active-pattern name="Procedure activity - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M95"/>
         <active-pattern name="Purpose activity - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M98"/>
         <active-pattern name="Result observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M101"/>
         <active-pattern name="Result organizer - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M104"/>
         <active-pattern name="Social history observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M107"/>
         <active-pattern name="Supply activity - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M110"/>
         <active-pattern name="Vital signs organizer - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M113"/>
         <active-pattern name="Advance directive reference - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M116"/>
         <active-pattern name="Advance directive status observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M119"/>
         <active-pattern name="Age observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M122"/>
         <active-pattern name="Alert status observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M125"/>
         <active-pattern name="Comment - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M128"/>
         <active-pattern name="Episode observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M131"/>
         <active-pattern name="Family history cause of death observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M134"/>
         <active-pattern name="Fulfillment instruction - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M137"/>
         <active-pattern name="Location participation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M140"/>
         <active-pattern name="Medication series number observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M143"/>
         <active-pattern name="Medication status observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M146"/>
         <active-pattern name="Patient awareness - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M149"/>
         <active-pattern name="Patient instruction - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M152"/>
         <active-pattern name="Problem healthstatus observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M155"/>
         <active-pattern name="Problem status observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M158"/>
         <active-pattern name="Product - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M161"/>
         <active-pattern name="Product instance - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M164"/>
         <active-pattern name="Reaction observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M167"/>
         <active-pattern name="Severity observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M170"/>
         <active-pattern name="Social history status observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M173"/>
         <active-pattern name="Status observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M176"/>
         <active-pattern name="Status of functional status observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M179"/>
         <active-pattern name="Verification of an advance directive observation - manual validation phase">
            <xsl:apply-templates/>
         </active-pattern>
         <xsl:apply-templates select="/" mode="M182"/>
      </schematron-output>
   </xsl:template>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1&#34;]" priority="3999"
                 mode="M8">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M8"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M8"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.1&#34;]" priority="3999"
                 mode="M11">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.1&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M11"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M11"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.2&#34;]" priority="3999"
                 mode="M14">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.2&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M14"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M14"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.3&#34;]" priority="3999"
                 mode="M17">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.3&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M17"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M17"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.4&#34;]" priority="3999"
                 mode="M20">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.4&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M20"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M20"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.5&#34;]" priority="3999"
                 mode="M23">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.5&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M23"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M23"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.6&#34;]" priority="3999"
                 mode="M26">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.6&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M26"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M26"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.7&#34;]" priority="3999"
                 mode="M29">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.7&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M29"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M29"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.8&#34;]" priority="3999"
                 mode="M32">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.8&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M32"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M32"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.9&#34;]" priority="3999"
                 mode="M35">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.9&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M35"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M35"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.10&#34;]" priority="3999"
                 mode="M38">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.10&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M38"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M38"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.11&#34;]" priority="3999"
                 mode="M41">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.11&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M41"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M41"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.12&#34;]" priority="3999"
                 mode="M44">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.12&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M44"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M44"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.13&#34;]" priority="3999"
                 mode="M47">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.13&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M47"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M47"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.14&#34;]" priority="3999"
                 mode="M50">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.14&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M50"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M50"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.15&#34;]" priority="3999"
                 mode="M53">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.15&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M53"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M53"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.16&#34;]" priority="3999"
                 mode="M56">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.16&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M56"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M56"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.17&#34;]" priority="3999"
                 mode="M59">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.17&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M59"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M59"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.18&#34;]" priority="3999"
                 mode="M62">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.18&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M62"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M62"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.19&#34;]" priority="3999"
                 mode="M65">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.19&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M65"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M65"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.20&#34;]" priority="3999"
                 mode="M68">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.20&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M68"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M68"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.21&#34;]" priority="3999"
                 mode="M71">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.21&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M71"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M71"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.22&#34;]" priority="3999"
                 mode="M74">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.22&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M74"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M74"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.23&#34;]" priority="3999"
                 mode="M77">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.23&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M77"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M77"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.24&#34;]" priority="3999"
                 mode="M80">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.24&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M80"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M80"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.25&#34;]" priority="3999"
                 mode="M83">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.25&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M83"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M83"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.26&#34;]" priority="3999"
                 mode="M86">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.26&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M86"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M86"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.27&#34;]" priority="3999"
                 mode="M89">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.27&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M89"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M89"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.28&#34;]" priority="3999"
                 mode="M92">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.28&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M92"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M92"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.29&#34;]" priority="3999"
                 mode="M95">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.29&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M95"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M95"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.30&#34;]" priority="3999"
                 mode="M98">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.30&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M98"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M98"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.31&#34;]" priority="3999"
                 mode="M101">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.31&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M101"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M101"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.32&#34;]" priority="3999"
                 mode="M104">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.32&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M104"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M104"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.33&#34;]" priority="3999"
                 mode="M107">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.33&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M107"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M107"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.34&#34;]" priority="3999"
                 mode="M110">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.34&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M110"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M110"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.35&#34;]" priority="3999"
                 mode="M113">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.35&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M113"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M113"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.36&#34;]" priority="3999"
                 mode="M116">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.36&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M116"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M116"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.37&#34;]" priority="3999"
                 mode="M119">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.37&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M119"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M119"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.38&#34;]" priority="3999"
                 mode="M122">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.38&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M122"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M122"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.39&#34;]" priority="3999"
                 mode="M125">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.39&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M125"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M125"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.40&#34;]" priority="3999"
                 mode="M128">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.40&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M128"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M128"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.41&#34;]" priority="3999"
                 mode="M131">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.41&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M131"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M131"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.42&#34;]" priority="3999"
                 mode="M134">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.42&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M134"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M134"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.43&#34;]" priority="3999"
                 mode="M137">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.43&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M137"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M137"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.45&#34;]" priority="3999"
                 mode="M140">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.45&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M140"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M140"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.46&#34;]" priority="3999"
                 mode="M143">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.46&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M143"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M143"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.47&#34;]" priority="3999"
                 mode="M146">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.47&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M146"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M146"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.48&#34;]" priority="3999"
                 mode="M149">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.48&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M149"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M149"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.49&#34;]" priority="3999"
                 mode="M152">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.49&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M152"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M152"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.51&#34;]" priority="3999"
                 mode="M155">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.51&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M155"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M155"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.50&#34;]" priority="3999"
                 mode="M158">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.50&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M158"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M158"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.53&#34;]" priority="3999"
                 mode="M161">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.53&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M161"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M161"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.52&#34;]" priority="3999"
                 mode="M164">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.52&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M164"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M164"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.54&#34;]" priority="3999"
                 mode="M167">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.54&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M167"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M167"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.55&#34;]" priority="3999"
                 mode="M170">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.55&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M170"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M170"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.56&#34;]" priority="3999"
                 mode="M173">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.56&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M173"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M173"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.57&#34;]" priority="3999"
                 mode="M176">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.57&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M176"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M176"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.44&#34;]" priority="3999"
                 mode="M179">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.44&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M179"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M179"/>
   <xsl:template match="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.58&#34;]" priority="3999"
                 mode="M182">
      <fired-rule id="" context="*[cda:templateId/@root=&#34;2.16.840.1.113883.10.20.1.58&#34;]" role=""/>
      <xsl:if test=".">
         <successful-report id="" test="." role="">
            <xsl:attribute name="location">
               <xsl:apply-templates select="." mode="schematron-get-full-path"/>
            </xsl:attribute>
            <text/>
         </successful-report>
      </xsl:if>
      <xsl:apply-templates mode="M182"/>
   </xsl:template>
   <xsl:template match="text()" priority="-1" mode="M182"/>
   <xsl:template match="text()" priority="-1"/>
</xsl:stylesheet>