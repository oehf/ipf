<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!DOCTYPE schema [
<!-- Replace baseURI below with a reference to the published Implementation Guide HTML. -->
<!ENTITY baseURI "">
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.7 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.7.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.6 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.2.6.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.7.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.7.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.2.20 SYSTEM 'templates/1.3.6.1.4.1.19376.1.2.20.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.2.20.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.2.20.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.2.20.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.2.20.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.2.20.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.2.20.3.ent'>
]>
<schema xmlns="http://www.ascc.net/xml/schematron" xmlns:msg="urn:hl7-org:v3">
    <!-- 
        To use iso schematron instead of schematron 1.5, 
        change the xmlns attribute from
        "http://www.ascc.net/xml/schematron" 
        to 
        "http://purl.oclc.org/dsdl/schematron"
    -->
    
    <title>BPPC - Basic Patient Privacy Consents Module</title>
    <ns prefix="cda" uri="urn:hl7-org:v3"/>
    
    <phase id='errors'>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.7.1-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.7-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.1-errors'/>   
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.2.6-errors'/>        
        <active pattern='p-1.3.6.1.4.1.19376.1.2.20-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.2.20.1-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.2.20.2-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.2.20.3-errors'/>
    </phase>
    
    
    <phase id='warning'>
        
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.1-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.2.20-warning'/>
       
        
    </phase>
    <!--    
        <phase id='manual'>
        <active pattern=''/>
        </phase>
    -->
    
        <phase id='note'>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.2.6-note'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.2.20.2-note'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.2.20.1-note'/>
        </phase>
        
  
    
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.7;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.6;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.7.1;
    &ent-1.3.6.1.4.1.19376.1.2.20;
    &ent-1.3.6.1.4.1.19376.1.2.20.1;
    &ent-1.3.6.1.4.1.19376.1.2.20.2;
    &ent-1.3.6.1.4.1.19376.1.2.20.3;
    
</schema>
