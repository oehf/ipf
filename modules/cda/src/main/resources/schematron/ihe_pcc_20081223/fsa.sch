<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!DOCTYPE schema [
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.1  SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.12.2.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.2  SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.12.2.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.3  SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.12.2.3.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.4  SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.12.2.4.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.5  SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.12.2.5.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.1  SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.12.3.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.2  SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.12.3.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.3  SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.12.3.3.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.4  SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.12.3.4.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.5  SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.12.3.5.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.6  SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.12.3.6.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.7  SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.12.3.7.ent'>
]>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:cda="urn:hl7-org:v3">
    <!-- 
        To use iso schematron instead of schematron 1.5, 
        change the xmlns attribute from
        "http://www.ascc.net/xml/schematron" 
        to 
        "http://purl.oclc.org/dsdl/schematron"
    -->
    <title>IHE_FSA</title>
    <ns prefix="cda" uri="urn:hl7-org:v3"/>
    <ns prefix="sdtc" uri="urn:hl7-org:sdtc"/>
    <ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>
    
    <phase id='errors'>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.1-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.2-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.3-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.4-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.5-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.1-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.2-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.3-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.4-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.5-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.6-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.7-errors'/>
    </phase>
    
    
    <phase id='warning'>        
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.1-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.2-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.3-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.4-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.5-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.1-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.2-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.3-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.4-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.5-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.6-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.7-warning'/>
    </phase>
       
        <phase id='note'>          
            <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.1-note'/>
            <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.2-note'/>
            <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.3-note'/>
            <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.4-note'/>
            <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.5-note'/>
            <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.1-note'/>
            <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.2-note'/>
            <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.3-note'/>
            <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.4-note'/>
            <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.5-note'/>
            <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.6-note'/>
            <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.7-note'/>
        </phase>
  
    
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.3;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.4;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.2.5;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.3;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.4;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.5;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.6;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.12.3.7;
    
</schema>
