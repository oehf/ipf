<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!DOCTYPE schema [
<!-- Replace baseURI below with a reference to the published Implementation Guide HTML. -->
<!ENTITY baseURI "">
<!ENTITY ent-2.16.840.1.113883.10.20.9.2 SYSTEM '2.16.840.1.113883.10.20.9.2.ent'>
]>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:cda="urn:hl7-org:v3">

    <title>CDA PHMR Vital Signs Constraints</title>
    <ns prefix="cda" uri="urn:hl7-org:v3"/>

    <phase id='errors'>
        <active pattern="p-2.16.840.1.113883.10.20.9.2-errors"/>
    </phase>

    <phase id='warnings'>
        <active pattern='p-2.16.840.1.113883.10.20.9.2-warning'/>        
    </phase>

    <phase id='note'>
        <!--<active pattern='p-2.16.840.1.113883.10.20.9.2-note'/> -->
    </phase>

    &ent-2.16.840.1.113883.10.20.9.2;
</schema>