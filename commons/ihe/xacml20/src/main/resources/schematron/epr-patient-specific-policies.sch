<?xml version="1.0" encoding="UTF-8"?>

<!--
Schematron rules to check constraints on CH:PPQ-1 requests (addition, update and deletion of patient-specific policies).
These rules shall be applied after XSD-based validation.

History:
07-Jun-2021: Initial version (Dmytro Rud, Swiss Post)
07-Jul-2021: Fix of ResourceMatch validation (Dmytro Rud, Swiss Post)
20-Jul-2021: Check that policy set IDs are UUIDs in URN format (Dmytro Rud, Swiss Post)
01-Dec-2021: Do not trim whitespace in attribute values (Dmytro Rud, Swiss Post)
05-Jan-2022: Fix possible referenced policies in template 202 (Dmytro Rud, Swiss Post)
09-Jan-2022: Consider changes in template 203 due to EPDREL-21 (Dmytro Rud, Swiss Post)
23-May-2022: Revert previous change because the template change was reverted (Dmytro Rud, ahdis)
27-Mar-2023: Consider the split of the template 301 into 301+304 (Dmytro Rud, adesso)
29-Mar-2023: Do not allow exclusion list for groups in template 302 (Dmytro Rud, adesso)
-->
<sch:schema queryBinding="xslt2"
            xmlns:sch="http://purl.oclc.org/dsdl/schematron"
            xmlns:val="urn:e-health-suisse:2021:policy-validation"
            xmlns:xs="http://www.w3.org/2001/XMLSchema"
            xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <sch:ns prefix="fn"     uri="http://www.w3.org/2005/xpath-functions"/>
    <sch:ns prefix="hl7"    uri="urn:hl7-org:v3"/>
    <sch:ns prefix="saml"   uri="urn:oasis:names:tc:SAML:2.0:assertion" />
    <sch:ns prefix="val"    uri="urn:e-health-suisse:2021:policy-validation"/>
    <sch:ns prefix="xacml"  uri="urn:oasis:names:tc:xacml:2.0:policy:schema:os"/>
    <sch:ns prefix="xsi"    uri="http://www.w3.org/2001/XMLSchema-instance"/>

    <!-- if this parameter is set to true, then the validation will fail if the to-date in EnvironmentMatch is less than the current date -->
    <sch:let name="need-check-current-date" value="fn:false()"/>

    <sch:pattern id="pattern1">

        <!-- validate assertion version -->
        <sch:rule context="/*/saml:Assertion">
            <sch:assert test="@Version eq '2.0'">
                Attribute 'Version' must equal to '2.0'
            </sch:assert>
        </sch:rule>

        <!-- validate policy issuer identification -->
        <sch:rule context="/*/saml:Assertion/saml:Issuer">
            <sch:assert test="@NameQualifier eq 'urn:e-health-suisse:community-index'">
                Attribute 'NameQualifier' must equal to 'urn:e-health-suisse:community-index'
            </sch:assert>
            <sch:assert test="val:is-oid-urn(text())">
                Policy source shall be specified as OID in URN format
            </sch:assert>
        </sch:rule>


        <!-- validate each policy set in Add and Update requests -->
        <sch:rule context="/*[(local-name() eq 'AddPolicyRequest') or (local-name() eq 'UpdatePolicyRequest')]/saml:Assertion/saml:Statement/xacml:PolicySet">
            <sch:assert test="@PolicyCombiningAlgId eq 'urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:deny-overrides'">
                Attribute 'PolicyCombiningAlgId' must equal to 'urn:oasis:names:tc:xacml:1.0:policy-combining-algorithm:deny-overrides'
            </sch:assert>
            <sch:assert test="val:is-uuid-urn(@PolicySetId)">
                Attribute 'PolicySetId' must be a UUID in URN format
            </sch:assert>

            <!-- I. Validate element Environment -->
            <sch:assert test="fn:count(xacml:Target/xacml:Environments/xacml:Environment) le 1">
                At most one element 'Environment' is allowed
            </sch:assert>

            <sch:let name="envMatches" value="xacml:Target/xacml:Environments/xacml:Environment/xacml:EnvironmentMatch"/>
            <sch:let name="fromDate"   value="$envMatches[val:is-date-restriction-environment-match(., 'urn:oasis:names:tc:xacml:1.0:function:date-less-than-or-equal')]"/>
            <sch:let name="toDate"     value="$envMatches[val:is-date-restriction-environment-match(., 'urn:oasis:names:tc:xacml:1.0:function:date-greater-than-or-equal')]"/>

            <sch:assert test="fn:count($fromDate) le 1">
                At most one from-date is allowed in element 'EnvironmentMatch'
            </sch:assert>
            <sch:assert test="fn:count($toDate) le 1">
                At most one to-date is allowed in element 'EnvironmentMatch'
            </sch:assert>
            <sch:assert test="fn:count($fromDate) + fn:count($toDate) eq fn:count($envMatches)">
                Element 'EnvironmentMatch' can define only from-date and/or to-date
            </sch:assert>

            <sch:assert test="(not($need-check-current-date)) or (not($toDate)) or (xs:date(val:attribute-value-text($toDate)) ge fn:current-date())">
                To-date must be equal to or greater than the current date in element 'EnvironmentMatch'
            </sch:assert>
            <sch:assert test="(not($fromDate and $toDate)) or (xs:date(val:attribute-value-text($toDate)) ge xs:date(val:attribute-value-text($fromDate)))">
                To-date must be equal to or greater than the from-date in element 'EnvironmentMatch'
            </sch:assert>


            <!-- II. Validate combination of elements Subject, ResourceMatch, EnvironmentMatch and PolicySetIdReference -->
            <sch:assert test="fn:count(xacml:PolicySetIdReference) eq 1">
                Exactly one element 'PolicySetIdReference' must be present
            </sch:assert>
            <sch:assert test="fn:count(xacml:Target/xacml:Resources/xacml:Resource) eq 1">
                Exactly one element 'Resource' must be present
            </sch:assert>

            <sch:let name="policyRef" value="fn:normalize-space(xacml:PolicySetIdReference/text())"/>
            <sch:let name="subjects" value="xacml:Target/xacml:Subjects/xacml:Subject"/>
            <sch:let name="resourceMatches" value="xacml:Target/xacml:Resources/xacml:Resource/xacml:ResourceMatch"/>

            <sch:assert test="val:is-template-201-combination($subjects, $resourceMatches, $policyRef, $envMatches) or
                              val:is-template-202-combination($subjects, $resourceMatches, $policyRef, $envMatches) or
                              val:is-template-203-combination($subjects, $resourceMatches, $policyRef, $envMatches) or
                              val:is-template-301-combination($subjects, $resourceMatches, $policyRef) or
                              val:is-template-302-combination($subjects, $resourceMatches, $policyRef, $toDate) or
                              val:is-template-303-combination($subjects, $resourceMatches, $policyRef) or
                              val:is-template-304-combination($subjects, $resourceMatches, $policyRef, $fromDate, $toDate)">
                The provided combination of elements 'Subject', 'Resource', 'Environment' and 'PolicySetIdReference'
                does not correspond to any official policy template (201, 202, 203, 301, 302, 303, 304)
            </sch:assert>


            <!-- III. Validate that the elements Subject and Resource reference the same patient -->
            <sch:let name="eprSpidResourceMatch" value="$resourceMatches[val:is-pat-id-resource-match(.)]"/>
            <sch:assert test="fn:count($eprSpidResourceMatch) eq 1">
                Exactly one element 'ResourceMatch' must carry the EPR-SPID of the patient
            </sch:assert>

            <sch:let name="eprSpidSubjectMatch" value="$subjects/xacml:SubjectMatch[val:is-pat-id-subject-match(.)]"/>
            <sch:assert test="(not($eprSpidSubjectMatch)) or (val:attribute-value-text($eprSpidSubjectMatch) eq $eprSpidResourceMatch/xacml:AttributeValue/hl7:InstanceIdentifier/@extension)">
                EPR-SPIDs in the elements 'SubjectMatch' and 'ResourceMatch' must be equal
            </sch:assert>

        </sch:rule>

        <!-- check for prohibited elements in the Statement element of Add and Update requests -->
        <sch:rule context="/*[(local-name() eq 'AddPolicyRequest') or (local-name() eq 'UpdatePolicyRequest')]/saml:Assertion/saml:Statement/*">
            <sch:assert test="(namespace-uri() eq 'urn:oasis:names:tc:xacml:2.0:policy:schema:os') and (local-name() eq 'PolicySet')">
                Only elements 'PolicySet' are allowed
            </sch:assert>
        </sch:rule>

        <!-- check for prohibited elements in the Statement element of Delete requests -->
        <sch:rule context="/*[local-name() eq 'DeletePolicyRequest']/saml:Assertion/saml:Statement/*">
            <sch:assert test="(namespace-uri() eq 'urn:oasis:names:tc:xacml:2.0:policy:schema:os') and (local-name() eq 'PolicySetIdReference')">
                Only elements 'PolicySetIdReference' are allowed
            </sch:assert>
        </sch:rule>

        <!-- check for prohibited elements in the Assertion element -->
        <sch:rule context="/*/saml:Assertion/*">
            <sch:assert test="(local-name() eq 'Issuer') or (local-name() eq 'Statement')">
                Only elements 'Issuer' and 'Statement' are allowed
            </sch:assert>
        </sch:rule>

        <!-- check for prohibited elements in the PolicySet element -->
        <sch:rule context="/*/saml:Assertion/saml:Statement/xacml:PolicySet/*">
            <sch:assert test="(local-name() eq 'Description') or (local-name() eq 'Target') or (local-name() eq 'PolicySetIdReference')">
                Only elements 'Description', 'Target', and 'PolicySetIdReference' are allowed
            </sch:assert>
        </sch:rule>

        <!-- check for prohibited elements in the Target element -->
        <sch:rule context="/*/saml:Assertion/saml:Statement/xacml:PolicySet/xacml:Target/*">
            <sch:assert test="(local-name() eq 'Subjects') or (local-name() eq 'Resources') or (local-name() eq 'Environments')">
                Only elements 'Subjects', 'Resources', and 'Environments' are allowed
            </sch:assert>
        </sch:rule>

    </sch:pattern>



    <!-- ==================== Functions for handling primitives ==================== -->

    <!-- Returns true iff the given string represents an OID in URN format -->
    <xsl:function name="val:is-oid-urn" as="xs:boolean">
        <xsl:param name="value" as="xs:string"/>
        <xsl:sequence select="fn:matches($value, '^urn:oid:([0-2])((\.0)|(\.[1-9][0-9]*))*$', 'i')"/>
    </xsl:function>

    <!-- Returns true iff the given string represents a UUID in URN format -->
    <xsl:function name="val:is-uuid-urn" as="xs:boolean">
        <xsl:param name="value" as="xs:string"/>
        <xsl:sequence select="fn:matches($value, '^urn:uuid:[0-9a-f]{8}\-[0-9a-f]{4}\-[0-9a-f]{4}\-[0-9a-f]{4}\-[0-9a-f]{12}$', 'i')"/>
    </xsl:function>

    <!-- Returns true iff the given string represents an EPR-SPID -->
    <xsl:function name="val:is-epr-spid" as="xs:boolean">
        <xsl:param name="value" as="xs:string"/>
        <xsl:sequence select="fn:matches($value, '^\d{18}$')"/>
    </xsl:function>

    <!-- Returns true iff the given string represents a GLN -->
    <xsl:function name="val:is-gln" as="xs:boolean">
        <xsl:param name="value" as="xs:string"/>
        <xsl:sequence select="fn:matches($value, '^\d{13}$')"/>
    </xsl:function>

    <!-- Returns true iff the given string represents an ID of a REP -->
    <xsl:function name="val:is-rep-id" as="xs:boolean">
        <xsl:param name="value" as="xs:string"/>
        <xsl:sequence select="not(fn:empty(fn:normalize-space($value)))"/>
    </xsl:function>

    <!-- Returns the attribute value of the given SubjectMatch element, as string -->
    <xsl:function name="val:attribute-value-text" as="xs:string">
        <xsl:param name="element"/>
        <xsl:sequence select="$element/xacml:AttributeValue/text()"/>
    </xsl:function>

    <!-- Returns true iff the given element is a designator with the given attribute ID and data type -->
    <xsl:function name="val:is-given-designator" as="xs:boolean">
        <xsl:param name="element"/>
        <xsl:param name="attribute-id" as="xs:string"/>
        <xsl:param name="data-type" as="xs:string"/>
        <xsl:sequence select="($element/@AttributeId eq $attribute-id) and ($element/@DataType eq $data-type)"/>
    </xsl:function>


    <!-- ==================== Functions for validation of *-Match elements (e.g. SubjectMatch) ==================== -->

    <!-- Returns true iff the given *-Match element has the given comparator function, value data type, and attribute ID -->
    <xsl:function name="val:is-given-match" as="xs:boolean">
        <xsl:param name="element"/>
        <xsl:param name="designator-name" as="xs:string"/>
        <xsl:param name="comparator" as="xs:string"/>
        <xsl:param name="attribute-id" as="xs:string"/>
        <xsl:param name="data-type" as="xs:string"/>
        <xsl:param name="data-type-is-simple" as="xs:boolean"/>

        <xsl:variable name="designator" select="$element/*[local-name() eq $designator-name]"/>
        <xsl:variable name="expected-value-element-count" as="xs:integer" select="if ($data-type-is-simple) then 0 else 1"/>

        <xsl:sequence select="($element/@MatchId eq $comparator) and
                              ($element/xacml:AttributeValue/@DataType eq $data-type) and
                              (count($element/xacml:AttributeValue/*) eq $expected-value-element-count) and
                              val:is-given-designator($designator, $attribute-id, $data-type)"/>
    </xsl:function>

    <!-- Returns true iff the given SubjectMatch element has the given comparator function, value data type, and attribute ID -->
    <xsl:function name="val:is-given-subject-match" as="xs:boolean">
        <xsl:param name="element"/>
        <xsl:param name="comparator" as="xs:string"/>
        <xsl:param name="attribute-id" as="xs:string"/>
        <xsl:param name="data-type" as="xs:string"/>
        <xsl:param name="data-type-is-simple" as="xs:boolean"/>

        <xsl:sequence select="val:is-given-match($element, 'SubjectAttributeDesignator', $comparator, $attribute-id, $data-type, $data-type-is-simple)"/>
    </xsl:function>

    <!-- Returns true iff the given SubjectMatch element is related to a user ID (e.g. GLN for HCP) -->
    <xsl:function name="val:is-user-id-subject-match" as="xs:boolean">
        <xsl:param name="element"/>
        <xsl:sequence select="val:is-given-subject-match(
                $element,
                'urn:oasis:names:tc:xacml:1.0:function:string-equal',
                'urn:oasis:names:tc:xacml:1.0:subject:subject-id',
                'http://www.w3.org/2001/XMLSchema#string',
                fn:true())"/>
    </xsl:function>

    <!-- Returns true iff the given SubjectMatch element is related to an HCP ID (GLN) -->
    <xsl:function name="val:is-hcp-id-subject-match" as="xs:boolean">
        <xsl:param name="element"/>
        <xsl:sequence select="val:is-user-id-subject-match($element) and val:is-gln(val:attribute-value-text($element))"/>
    </xsl:function>

    <!-- Returns true iff the given SubjectMatch element is related to a patient ID (EPR-SPID) -->
    <xsl:function name="val:is-pat-id-subject-match" as="xs:boolean">
        <xsl:param name="element"/>
        <xsl:sequence select="val:is-user-id-subject-match($element) and val:is-epr-spid(val:attribute-value-text($element))"/>
    </xsl:function>

    <!-- Returns true iff the given SubjectMatch element is related to a REP ID (any non-empty string) -->
    <xsl:function name="val:is-rep-id-subject-match" as="xs:boolean">
        <xsl:param name="element"/>
        <xsl:sequence select="val:is-user-id-subject-match($element) and val:is-rep-id(val:attribute-value-text($element))"/>
    </xsl:function>

    <!-- Returns true iff the given SubjectMatch element is related to a Group ID (OID in URN format) -->
    <xsl:function name="val:is-group-id-subject-match" as="xs:boolean">
        <xsl:param name="element"/>
        <xsl:variable name="b1" select="val:is-given-subject-match(
                $element,
                'urn:oasis:names:tc:xacml:1.0:function:anyURI-equal',
                'urn:oasis:names:tc:xspa:1.0:subject:organization-id',
                'http://www.w3.org/2001/XMLSchema#anyURI',
                fn:true())"/>
        <xsl:variable name="b2" select="val:is-oid-urn(val:attribute-value-text($element))"/>
        <xsl:sequence select="$b1 and $b2"/>
    </xsl:function>

    <!-- Returns true iff the given SubjectMatch element is related to the expected Subject ID qualifier -->
    <xsl:function name="val:is-subject-id-qualifier-subject-match" as="xs:boolean">
        <xsl:param name="element"/>
        <xsl:param name="expectedValue" as="xs:string"/>
        <xsl:variable name="b1" select="val:is-given-subject-match(
                $element,
                'urn:oasis:names:tc:xacml:1.0:function:string-equal',
                'urn:oasis:names:tc:xacml:1.0:subject:subject-id-qualifier',
                'http://www.w3.org/2001/XMLSchema#string',
                fn:true())"/>
        <xsl:variable name="b2" select="val:attribute-value-text($element) eq $expectedValue"/>
        <xsl:sequence select="$b1 and $b2"/>
    </xsl:function>

    <!-- Returns true iff the given SubjectMatch element is related to the expected Subject Role code -->
    <xsl:function name="val:is-subject-role-subject-match" as="xs:boolean">
        <xsl:param name="element"/>
        <xsl:param name="expectedRoleCode" as="xs:string"/>
        <xsl:variable name="b1" select="val:is-given-subject-match(
                $element,
                'urn:hl7-org:v3:function:CV-equal',
                'urn:oasis:names:tc:xacml:2.0:subject:role',
                'urn:hl7-org:v3#CV',
                fn:false())"/>
        <xsl:variable name="value" select="$element/xacml:AttributeValue/hl7:CodedValue"/>
        <xsl:variable name="b2" select="($value/@codeSystem eq '2.16.756.5.30.1.127.3.10.6') and ($value/@code eq $expectedRoleCode)"/>
        <xsl:sequence select="$b1 and $b2"/>
    </xsl:function>

    <!-- Returns true iff the given SubjectMatch element is related to the expected Purpose of Use code -->
    <xsl:function name="val:is-purpose-of-use-subject-match" as="xs:boolean">
        <xsl:param name="element"/>
        <xsl:param name="expectedPouCode" as="xs:string"/>
        <xsl:variable name="b1" select="val:is-given-subject-match(
                $element,
                'urn:hl7-org:v3:function:CV-equal',
                'urn:oasis:names:tc:xspa:1.0:subject:purposeofuse',
                'urn:hl7-org:v3#CV',
                fn:false())"/>
        <xsl:variable name="value" select="$element/xacml:AttributeValue/hl7:CodedValue"/>
        <xsl:variable name="b2" select="($value/@codeSystem eq '2.16.756.5.30.1.127.3.10.5') and ($value/@code eq $expectedPouCode)"/>
        <xsl:sequence select="$b1 and $b2"/>
    </xsl:function>

    <!-- Returns true iff the given EnvironmentMatch element defines the given date restriction (to or from) -->
    <xsl:function name="val:is-date-restriction-environment-match" as="xs:boolean">
        <xsl:param name="element"/>
        <xsl:param name="comparator" as="xs:string"/>
        <xsl:sequence select="val:is-given-match(
                $element,
                'EnvironmentAttributeDesignator',
                $comparator,
                'urn:oasis:names:tc:xacml:1.0:environment:current-date',
                'http://www.w3.org/2001/XMLSchema#date',
                fn:true())"/>
    </xsl:function>

    <!-- Returns true iff the given ResourceMatch element is related to a patient's EPR-SPID -->
    <xsl:function name="val:is-pat-id-resource-match" as="xs:boolean">
        <xsl:param name="element"/>
        <xsl:variable name="b1" select="val:is-given-match(
                $element,
                'ResourceAttributeDesignator',
                'urn:hl7-org:v3:function:II-equal',
                'urn:e-health-suisse:2015:epr-spid',
                'urn:hl7-org:v3#II',
                fn:false())"/>
        <xsl:variable name="identifier" select="$element/xacml:AttributeValue/hl7:InstanceIdentifier"/>
        <xsl:variable name="b2" select="($identifier/@root eq '2.16.756.5.30.1.127.3.10.3') and val:is-epr-spid($identifier/@extension)"/>
        <xsl:sequence select="$b1 and $b2"/>
    </xsl:function>

    <!-- Returns true iff the given ResourceMatch element is related to a given date -->
    <xsl:function name="val:is-date-resource-match" as="xs:boolean">
        <xsl:param name="element"/>
        <xsl:param name="comparator"/>
        <xsl:param name="attributeId"/>
        <xsl:param name="evnMatch"/>
        <xsl:variable name="b1" select="val:is-given-match(
                $element,
                'ResourceAttributeDesignator',
                $comparator,
                $attributeId,
                'http://www.w3.org/2001/XMLSchema#date',
                fn:true())"/>
        <xsl:variable name="b2" select="val:attribute-value-text($element) eq val:attribute-value-text($evnMatch)"/>
        <xsl:sequence select="(not($evnMatch) and not($b1)) or
                              ($evnMatch and $b1 and $b2)"/>
    </xsl:function>


    <!-- ==================== Functions for validation of Subject and SubjectMatches elements ==================== -->

    <!-- Returns true iff the given Subject element suits for the policy template 201 -->
    <xsl:function name="val:is-template-201-subject" as="xs:boolean">
        <xsl:param name="subject"/>
        <xsl:variable name="subjectMatches" select="$subject/xacml:SubjectMatch"/>
        <xsl:sequence select="(fn:count($subjectMatches) eq 3) and
                              (fn:count($subjectMatches[val:is-pat-id-subject-match(.)]) eq 1) and
                              (fn:count($subjectMatches[val:is-subject-id-qualifier-subject-match(., 'urn:e-health-suisse:2015:epr-spid')]) eq 1) and
                              (fn:count($subjectMatches[val:is-subject-role-subject-match(., 'PAT')]) eq 1)"/>
    </xsl:function>

    <!-- Returns true iff the given Subject element suits for the policy template 202 -->
    <xsl:function name="val:is-template-202-subject" as="xs:boolean">
        <xsl:param name="subject"/>
        <xsl:variable name="subjectMatches" select="$subject/xacml:SubjectMatch"/>
        <xsl:sequence select="(fn:count($subjectMatches) eq 3) and
                              (fn:count($subjectMatches[val:is-purpose-of-use-subject-match(., 'EMER')]) eq 1) and
                              (fn:count($subjectMatches[val:is-subject-id-qualifier-subject-match(., 'urn:gs1:gln')]) eq 1) and
                              (fn:count($subjectMatches[val:is-subject-role-subject-match(., 'HCP')]) eq 1)"/>
    </xsl:function>

    <!-- Returns true iff the given Subject element with the given Purpose of Use code suits for the policy template 203 -->
    <xsl:function name="val:is-template-203-subject" as="xs:boolean">
        <xsl:param name="subject"/>
        <xsl:param name="purposeOfUseCode" as="xs:string"/>
        <xsl:variable name="subjectMatches" select="$subject/xacml:SubjectMatch"/>
        <xsl:sequence select="(fn:count($subjectMatches) eq 3) and
                              (fn:count($subjectMatches[val:is-purpose-of-use-subject-match(., $purposeOfUseCode)]) eq 1) and
                              (fn:count($subjectMatches[val:is-subject-id-qualifier-subject-match(., 'urn:gs1:gln')]) eq 1) and
                              (fn:count($subjectMatches[val:is-subject-role-subject-match(., 'HCP')]) eq 1)"/>
    </xsl:function>

    <!-- Returns true iff the given Subject element suits for the policy template 301 -->
    <xsl:function name="val:is-template-301-subject" as="xs:boolean">
        <xsl:param name="subject"/>
        <xsl:variable name="subjectMatches" select="$subject/xacml:SubjectMatch"/>
        <xsl:sequence select="(fn:count($subjectMatches) eq 3) and
                              (fn:count($subjectMatches[val:is-hcp-id-subject-match(.)]) eq 1) and
                              (fn:count($subjectMatches[val:is-subject-id-qualifier-subject-match(., 'urn:gs1:gln')]) eq 1) and
                              (fn:count($subjectMatches[val:is-subject-role-subject-match(., 'HCP')]) eq 1)"/>
    </xsl:function>

    <!-- Returns true iff the given Subject element suits for the policy template 302 -->
    <xsl:function name="val:is-template-302-subject" as="xs:boolean">
        <xsl:param name="subject"/>
        <xsl:variable name="subjectMatches" select="$subject/xacml:SubjectMatch"/>
        <xsl:sequence select="(fn:count($subjectMatches) eq 2) and
                              (fn:count($subjectMatches[val:is-group-id-subject-match(.)]) eq 1) and
                              (fn:count($subjectMatches[val:is-subject-role-subject-match(., 'HCP')]) eq 1)"/>
    </xsl:function>

    <!-- Returns true iff the given Subject element suits for the policy template 303 -->
    <xsl:function name="val:is-template-303-subject" as="xs:boolean">
        <xsl:param name="subject"/>
        <xsl:variable name="subjectMatches" select="$subject/xacml:SubjectMatch"/>
        <xsl:sequence select="(fn:count($subjectMatches) eq 3) and
                              (fn:count($subjectMatches[val:is-rep-id-subject-match(.)]) eq 1) and
                              (fn:count($subjectMatches[val:is-subject-id-qualifier-subject-match(., 'urn:e-health-suisse:representative-id')]) eq 1) and
                              (fn:count($subjectMatches[val:is-subject-role-subject-match(., 'REP')]) eq 1)"/>
    </xsl:function>

    <!-- Returns true iff the given Subject element suits for the policy template 304 -->
    <xsl:function name="val:is-template-304-subject" as="xs:boolean">
        <xsl:param name="subject"/>
        <xsl:variable name="subjectMatches" select="$subject/xacml:SubjectMatch"/>
        <xsl:sequence select="(fn:count($subjectMatches) eq 3) and
                              (fn:count($subjectMatches[val:is-hcp-id-subject-match(.)]) eq 1) and
                              (fn:count($subjectMatches[val:is-subject-id-qualifier-subject-match(., 'urn:gs1:gln')]) eq 1) and
                              (fn:count($subjectMatches[val:is-subject-role-subject-match(., 'HCP')]) eq 1)"/>
    </xsl:function>


    <!-- ==================== Functions for validation of Resource elements ==================== -->

    <!-- Returns true iff the given Resource element suits all policy templates excluding 304 -->
    <xsl:function name="val:is-common-resource-matches" as="xs:boolean">
        <xsl:param name="resourceMatches"/>
        <xsl:sequence select="fn:count($resourceMatches) eq 1"/>
    </xsl:function>

    <!-- Returns true iff the given Resource element suits the policy template 304 -->
    <xsl:function name="val:is-template-304-resource-matches" as="xs:boolean">
        <xsl:param name="resourceMatches"/>
        <xsl:param name="fromDate"/>
        <xsl:param name="toDate"/>
        <xsl:variable name="fromDateResourceMatches" select="$resourceMatches[val:is-date-resource-match(
                .,
                'urn:oasis:names:tc:xacml:1.0:function:date-less-than-or-equal',
                'urn:e-health-suisse:2023:policy-attributes:start-date',
                $fromDate)]"/>
        <xsl:variable name="toDateResourceMatches" select="$resourceMatches[val:is-date-resource-match(
                .,
                'urn:oasis:names:tc:xacml:1.0:function:date-greater-than-or-equal',
                'urn:e-health-suisse:2023:policy-attributes:end-date',
                $toDate)]"/>
        <xsl:sequence select="(fn:count($resourceMatches)         eq (if ($fromDate) then 3 else 2)) and
                              (fn:count($fromDateResourceMatches) eq (if ($fromDate) then 1 else 2)) and
                              (fn:count($toDateResourceMatches)   eq 1)"/>
    </xsl:function>


    <!-- ==================== Functions for validation of template-specific element combinations ==================== -->

    <!-- Returns true iff the given combination of Subject, EnvironmentMatch and PolicySetIdReference suits for the policy template 201 -->
    <xsl:function name="val:is-template-201-combination" as="xs:boolean">
        <xsl:param name="subjects"/>
        <xsl:param name="resourceMatches"/>
        <xsl:param name="policyRef"/>
        <xsl:param name="envMatches"/>
        <xsl:sequence select="(fn:count($subjects) eq 1) and
                              (fn:count($subjects[val:is-template-201-subject(.)]) eq 1) and
                              val:is-common-resource-matches($resourceMatches) and
                              ($policyRef eq 'urn:e-health-suisse:2015:policies:access-level:full') and
                              (not($envMatches))"/>
    </xsl:function>

    <!-- Returns true iff the given combination of Subject, EnvironmentMatch and PolicySetIdReference suits for the policy template 202 -->
    <xsl:function name="val:is-template-202-combination" as="xs:boolean">
        <xsl:param name="subjects"/>
        <xsl:param name="resourceMatches"/>
        <xsl:param name="policyRef"/>
        <xsl:param name="envMatches"/>
        <xsl:sequence select="(fn:count($subjects) eq 1) and
                              (fn:count($subjects[val:is-template-202-subject(.)]) eq 1) and
                              val:is-common-resource-matches($resourceMatches) and
                              (($policyRef eq 'urn:e-health-suisse:2015:policies:access-level:normal') or
                               ($policyRef eq 'urn:e-health-suisse:2015:policies:access-level:restricted')) and
                              (not($envMatches))"/>
    </xsl:function>

    <!-- Returns true iff the given combination of Subject, EnvironmentMatch and PolicySetIdReference suits for the policy template 203 -->
    <xsl:function name="val:is-template-203-combination" as="xs:boolean">
        <xsl:param name="subjects"/>
        <xsl:param name="resourceMatches"/>
        <xsl:param name="policyRef"/>
        <xsl:param name="envMatches"/>
        <xsl:sequence select="(fn:count($subjects) eq 3) and
                              (fn:count($subjects[val:is-template-203-subject(., 'NORM')]) eq 1) and
                              (fn:count($subjects[val:is-template-203-subject(., 'AUTO')]) eq 1) and
                              (fn:count($subjects[val:is-template-203-subject(., 'DICOM_AUTO')]) eq 1) and
                              val:is-common-resource-matches($resourceMatches) and
                              (($policyRef eq 'urn:e-health-suisse:2015:policies:provide-level:normal') or
                               ($policyRef eq 'urn:e-health-suisse:2015:policies:provide-level:restricted') or
                               ($policyRef eq 'urn:e-health-suisse:2015:policies:provide-level:secret')) and
                              (not($envMatches))"/>
    </xsl:function>

    <!-- Returns true iff the given combination of Subject, EnvironmentMatch and PolicySetIdReference suits for the policy template 301 -->
    <xsl:function name="val:is-template-301-combination" as="xs:boolean">
        <xsl:param name="subjects"/>
        <xsl:param name="resourceMatches"/>
        <xsl:param name="policyRef"/>
        <xsl:sequence select="(fn:count($subjects) eq 1) and
                              (fn:count($subjects[val:is-template-301-subject(.)]) eq 1) and
                              val:is-common-resource-matches($resourceMatches) and
                              (($policyRef eq 'urn:e-health-suisse:2015:policies:exclusion-list') or
                               ($policyRef eq 'urn:e-health-suisse:2015:policies:access-level:normal') or
                               ($policyRef eq 'urn:e-health-suisse:2015:policies:access-level:restricted'))"/>
    </xsl:function>

    <!-- Returns true iff the given combination of Subject, EnvironmentMatch and PolicySetIdReference suits for the policy template 302 -->
    <xsl:function name="val:is-template-302-combination" as="xs:boolean">
        <xsl:param name="subjects"/>
        <xsl:param name="resourceMatches"/>
        <xsl:param name="policyRef"/>
        <xsl:param name="toDate"/>
        <xsl:sequence select="(fn:count($subjects) eq 1) and
                              (fn:count($subjects[val:is-template-302-subject(.)]) eq 1) and
                              val:is-common-resource-matches($resourceMatches) and
                              (($policyRef eq 'urn:e-health-suisse:2015:policies:access-level:normal') or
                               ($policyRef eq 'urn:e-health-suisse:2015:policies:access-level:restricted')) and
                              $toDate"/>
    </xsl:function>

    <!-- Returns true iff the given combination of Subject, EnvironmentMatch and PolicySetIdReference suits for the policy template 303 -->
    <xsl:function name="val:is-template-303-combination" as="xs:boolean">
        <xsl:param name="subjects"/>
        <xsl:param name="resourceMatches"/>
        <xsl:param name="policyRef"/>
        <xsl:sequence select="(fn:count($subjects) eq 1) and
                              (fn:count($subjects[val:is-template-303-subject(.)]) eq 1) and
                              val:is-common-resource-matches($resourceMatches) and
                              ($policyRef eq 'urn:e-health-suisse:2015:policies:access-level:full')"/>
    </xsl:function>

    <!-- Returns true iff the given combination of Subject, EnvironmentMatch and PolicySetIdReference suits for the policy template 304 -->
    <xsl:function name="val:is-template-304-combination" as="xs:boolean">
        <xsl:param name="subjects"/>
        <xsl:param name="resourceMatches"/>
        <xsl:param name="policyRef"/>
        <xsl:param name="fromDate"/>
        <xsl:param name="toDate"/>
        <xsl:sequence select="(fn:count($subjects) eq 1) and
                              (fn:count($subjects[val:is-template-304-subject(.)]) eq 1) and
                              $toDate and
                              val:is-template-304-resource-matches($resourceMatches, $fromDate, $toDate) and
                              (($policyRef eq 'urn:e-health-suisse:2015:policies:access-level:delegation-and-normal') or
                               ($policyRef eq 'urn:e-health-suisse:2015:policies:access-level:delegation-and-restricted'))"/>
    </xsl:function>

</sch:schema>