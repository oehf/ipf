<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!DOCTYPE schema [
<!-- Replace baseURI below with a reference to the published Implementation Guide HTML. -->
<!ENTITY baseURI "">
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.3.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.1.ent'>
]>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:cda="urn:hl7-org:v3">
    
    <title>IHE_LAB_2.0</title>
    <ns prefix="cda" uri="urn:hl7-org:v3"/>
    <ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>
    <ns prefix="lab" uri="urn:oid:1.3.6.1.4.1.19376.1.3.2"/> 
    
    <phase id='errors'>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3-Lev2-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.1-errors'/>
    </phase>    
  
  
    <phase id='warnings'>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3-warnings'/>
        <!--
        <active pattern='p-1.3.6.1.4.1.19376.1.3.1-warnings'/>
        -->
    </phase>
  
    <!--  
    <phase id='note'>
        <active pattern='p1.3.6.1.4.1.19376.1.3.3-note'/>
        <active pattern='p1.3.6.1.4.1.19376.1.3.1-note'/>
    </phase>
    -->
    
    &ent-1.3.6.1.4.1.19376.1.3.3;
    &ent-1.3.6.1.4.1.19376.1.3.1;
    
    
</schema>
