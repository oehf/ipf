<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!DOCTYPE schema [

<!-- CDA General Header Constraints -->
<!ENTITY ent-2.16.840.1.113883.10.20.3      SYSTEM 'templates/2.16.840.1.113883.10.20.3.ent'>

]>
<schema xmlns="http://purl.oclc.org/dsdl/schematron"
        xmlns:cda="urn:hl7-org:v3"
        queryBinding="xslt2">
<!--
To use iso schematron instead of schematron 1.5,
change the xmlns attribute from
"http://www.ascc.net/xml/schematron"
to
"http://purl.oclc.org/dsdl/schematron"
-->

<title>Schematron schema for validating conformance to History and Physical documents</title>
<ns prefix="cda" uri="urn:hl7-org:v3"/>

<phase id='errors'>
	<active pattern='p-2.16.840.1.113883.10.20.3-errors'/>
</phase>

<phase id='warning'>
	<active pattern='p-2.16.840.1.113883.10.20.3-warning'/>
</phase>
<!--
<phase id='manual'>
	<active pattern='p-2.16.840.1.113883.10.20.3-manual'/>
</phase>
-->

<!--  CDA for common document type (General Header Constraints): 2.16.840.1.113883.10.20.3 -->
&ent-2.16.840.1.113883.10.20.3;

</schema>
