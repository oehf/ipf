<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!DOCTYPE schema [
<!-- Replace baseURI below with a reference to the published Implementation Guide HTML. -->
<!ENTITY baseURI "">
<!ENTITY ent-1.3.6.1.4.1.19376.1.2.20 SYSTEM '1.3.6.1.4.1.19376.1.2.20.ent'>
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
        <active pattern='p-1.3.6.1.4.1.19376.1.2.20-errors'/>
    </phase>
    
    
    <phase id='warning'>
        <active pattern='p-1.3.6.1.4.1.19376.1.2.20-warning'/>
    </phase>
    <!--   
        <phase id='manual'>
        <active pattern=''/>
        </phase>
    
    
        <phase id='note'>
        <active pattern=''/>
        </phase>
        
    -->
    
    &ent-1.3.6.1.4.1.19376.1.2.20;
    
    
</schema>
