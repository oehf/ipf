<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!DOCTYPE schema [
<!-- Replace baseURI below with a reference to the published Implementation Guide HTML. -->
<!ENTITY baseURI "">
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.3.ent'>

<!ENTITY ent-1.3.6.1.4.1.19376.1.3.1.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.1.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.1.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.1.3.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.1.6 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.1.6.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.1.8 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.1.8.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.3.1.2.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.3.1.2.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.3.1.3.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.3.1.3.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.3.1.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.3.1.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.3.1.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.3.1.3.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.3.1.4 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.3.1.4.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.3.1.5 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.3.1.5.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.3.1.6 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.3.1.6.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.3.1.7 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.3.1.7.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.3.2.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.3.2.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.3.2.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.3.2.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.1.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.1.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.1.1.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.1.1.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.1.1.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.1.1.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.1.1.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.1.1.3.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.1.5 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.1.5.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.3.1.4 SYSTEM 'templates/1.3.6.1.4.1.19376.1.3.1.4.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.2.ent'>

]>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:cda="urn:hl7-org:v3">
    
    <title>IHE_LAB</title>
    <ns prefix="cda" uri="urn:hl7-org:v3"/>
    <ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>
    <ns prefix="lab" uri="urn:oid:1.3.6.1.4.1.19376.1.3.2"/> 
    
    <phase id='errors'>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3.1.2-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3.1.3-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3.1.4-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3.1.5-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3.1.6-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3.1.7-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3.2.1-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3.2.2-errors'/>   
        <!--active pattern='p1.3.6.1.4.1.19376.1.3.3-Lev2-errors'/ -->
        <active pattern='p-1.3.6.1.4.1.19376.1.3.1-errors'/>       
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3.1.2.1-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3.1.3.1-errors'/>  
        <active pattern='p-1.3.6.1.4.1.19376.1.3.1.2-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.1.3-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.1.6-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.1.8-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.1.1-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.1.1.1-errors'/>     
        <active pattern='p-1.3.6.1.4.1.19376.1.3.1.1.2-errors'/>       
        <active pattern='p-1.3.6.1.4.1.19376.1.3.1.1.3-errors'/>       
        <active pattern='p-1.3.6.1.4.1.19376.1.3.1.5-errors'/>       
        <active pattern='p-1.3.6.1.4.1.19376.1.3.1.4-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.2-errors'/>
    </phase>    
  
  
    <phase id='warnings'>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3-warnings'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3.1.4-warnings'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3.1.5-warnings'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3.1.6-warnings'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.3.1.7-warnings'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.3.1-warnings'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.2-warnings'/>
    </phase>
  
    <!--  
    <phase id='note'>
        <active pattern='p1.3.6.1.4.1.19376.1.3.3-note'/>
        <active pattern='p1.3.6.1.4.1.19376.1.3.1-note'/>
    </phase>
    -->
    
    &ent-1.3.6.1.4.1.19376.1.3.3;
    &ent-1.3.6.1.4.1.19376.1.3.3.1.2;  
    &ent-1.3.6.1.4.1.19376.1.3.3.1.3;
    &ent-1.3.6.1.4.1.19376.1.3.3.1.4;
    &ent-1.3.6.1.4.1.19376.1.3.3.1.5;
    &ent-1.3.6.1.4.1.19376.1.3.3.1.6;
    &ent-1.3.6.1.4.1.19376.1.3.3.1.7;
    &ent-1.3.6.1.4.1.19376.1.3.3.2.1;
    &ent-1.3.6.1.4.1.19376.1.3.3.2.2;
    
    &ent-1.3.6.1.4.1.19376.1.3.1;   
    &ent-1.3.6.1.4.1.19376.1.3.3.1.2.1;
    &ent-1.3.6.1.4.1.19376.1.3.3.1.3.1;
    &ent-1.3.6.1.4.1.19376.1.3.1.2;   
    &ent-1.3.6.1.4.1.19376.1.3.1.3;
    &ent-1.3.6.1.4.1.19376.1.3.1.6;
    &ent-1.3.6.1.4.1.19376.1.3.1.8;
    &ent-1.3.6.1.4.1.19376.1.3.1.1;
    &ent-1.3.6.1.4.1.19376.1.3.1.1.1;
    &ent-1.3.6.1.4.1.19376.1.3.1.1.2;
    &ent-1.3.6.1.4.1.19376.1.3.1.1.3;
    &ent-1.3.6.1.4.1.19376.1.3.1.5;
    &ent-1.3.6.1.4.1.19376.1.3.1.4;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.2;
</schema>
