<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE schema [

<!-- ED Referral Specification Note -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.10 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.10.ent'>

<!-- XDS-MS Referral Specification Note -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.3.ent'>

<!-- Reason for Referral -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.1.ent'>

<!-- History of Present Illness -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.4 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.4.ent'>

<!-- Active Problems -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.6 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.6.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.3.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.6 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.6.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.1.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.1.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.1.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.1.2.ent'>

<!-- Current Meds -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.19 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.19.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.4.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.4.1.ent'>

<!-- Allergies -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.13 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.13.ent'>

<!-- Resolved Problems -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.8 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.8.ent'>

<!-- List of surgeries -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.11 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.11.ent'>

<!-- Immunizations -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.23 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.23.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.12 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.12.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.3.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.3.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.3.1.ent'>

<!-- Family History -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.14 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.14.ent'>

<!-- Social History -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.16 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.16.ent'>

<!-- Pertinent Review of Systems -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.18 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.18.ent'>

<!-- Vital Signs -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.25 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.25.ent'>

<!-- Physical Exam -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.24 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.24.ent'>

<!-- Relevant Diagnostic Surgical Procedures / Clinical Reports and Relevant Diagnostic Test and Reports -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.27 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.27.ent'>

<!-- Plan of Care -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.31 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.31.ent'>

<!-- Mode of Transport to the ED -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.10.3.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.10.3.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1.ent'>

<!-- Proposed ED Disposition -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.10 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.2.10.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.10.4.2.ent'>


<!-- Advance Directives -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.34 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.34.ent'>



]>


<!--
    

-->
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:cda="urn:hl7-org:v3"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <title>Schematron schema for validating ED Referral Specification Module</title>
    <ns prefix="cda" uri="urn:hl7-org:v3"/>
    <ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>
    <phase id="errors">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.3-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.1-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.4-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.6-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.2-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.1-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.3-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.6-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1.1-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.2-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1.2-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.19-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-errors"/>  
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.13-errors"/>  
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.8-errors"/>  
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.11-errors"/>  
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.23-errors"/>  
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.12-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.3-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.14-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.16-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.18-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.24-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.27-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.31-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.3.2-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.10-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.2-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.34-errors"/>
        
        
    </phase>

    <phase id="warning">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.3-warnings"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.4-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.6-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.3-warnings"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.6-warnings"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1-warnings"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1.1-warnings"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.2-warnings"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5-warnings"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1.2-warnings"/> 
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.19-warnings"/> 
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-warnings"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-warnings"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-warnings"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.13-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.8-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.11-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.23-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.12-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.3-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.14-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.16-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.18-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.24-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.27-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.31-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.3.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.10-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.34-warnings"/>
        
        
        
    </phase>

    <phase id="notes">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.3-notes"/>   
        
        
        
    </phase> 
    
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.10;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.3;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.4;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.6;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.3;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.6;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.1.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.1.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.19;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7; 
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.4.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.13;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.8;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.11;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.23;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.12;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.3;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.3.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.14;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.16;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.18;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.25;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.24;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.27;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.31;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.10.3.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.10;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.34;
    
    
</schema>
