<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!DOCTYPE schema [
<!-- Replace baseURI below with a reference to the published Implementation Guide HTML. -->
<!ENTITY baseURI "">
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.3.1.5 SYSTEM '1.3.6.1.4.1.19376.1.3.3.1.5.ent'>
]>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:cda="urn:hl7-org:v3">
    <!-- 
        To use iso schematron instead of schematron 1.5, 
        change the xmlns attribute from
        "http://www.ascc.net/xml/schematron" 
        to 
        "http://purl.oclc.org/dsdl/schematron"
    -->
    
    <title>Template_1.3.6.1.4.1.19376.1.3.3.1.5</title>
    
    <ns prefix="cda" uri="urn:hl7-org:v3"/>
    <ns prefix="crs" uri="urn:hl7-org:crs"/>
    <ns prefix="lab" uri="urn:oid:1.3.6.1.4.1.19376.1.3.2"/> 
    
    <phase id='errors'>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3.1.5-errors'/>
    </phase>
    
    
        <phase id='warnings'>
            <active pattern='p-1.3.6.1.4.1.19376.1.3.3.1.5-warnings'/>
        </phase>
    <!--
    <phase id='note'>
        <active pattern='p1.3.6.1.4.1.19376.1.3.3.1.5-note'/>
    </phase>
    
   --> 
    
    
    &ent-1.3.6.1.4.1.19376.1.3.3.1.5;
    
</schema>
