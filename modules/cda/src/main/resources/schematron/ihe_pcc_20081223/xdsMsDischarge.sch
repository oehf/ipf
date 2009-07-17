<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE schema [

<!-- XDS-MS Discharge Summary Specification Note -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.4 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.4.ent'>

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

<!-- Resolved Problems -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.8 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.8.ent'>

<!-- Discharge Diagnosis -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.7 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.7.ent'>

<!-- Hospital Admission Diagnosis -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.3.ent'>

<!-- Medications Administered -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.21 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.21.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.4.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.4.1.ent'>

<!-- Hospital Discharge Medications -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.22 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.22.ent'>

<!-- Admission Medication History -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.20 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.20.ent'>

<!-- Allergies and Other Adverse Reactions -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.13 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.13.ent'>

<!-- Hospital Course Section -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.5 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.5.ent'>

<!-- Advance Directives -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.34 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.34.ent'>

<!-- History of Present Illness -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.4 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.4.ent'>

<!-- Functional Status -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.17 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.17.ent'>

<!-- Review of Systems -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.18 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.18.ent'>

<!-- Physicial Examination -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.24 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.24.ent'>

<!-- Vital Signs -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.25 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.25.ent'>

<!-- Discharge Procedures Tests, Reports -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.29 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.29.ent'>

<!-- Plan of Care -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.31 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.31.ent'>

<!-- Discharge Diet -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.33 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.33.ent'>




]>


<!--
    

-->
<schema xmlns="http://www.ascc.net/xml/schematron" xmlns:cda="urn:hl7-org:v3"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <title>Schematron schema for validating XDS-MS Discharge Summary Specification Module</title>
    <ns prefix="cda" uri="urn:hl7-org:v3"/>
    <ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>
    <phase id="errors">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.4-errors"/>        
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
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.8-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.7-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.3-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.21-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.22-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.20-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.13-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.5-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.34-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.4-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.17-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.18-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.24-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.29-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.31-errors"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.33-errors"/>       
        
        
    </phase>

    <phase id="warning">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.4-warnings"/>
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
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.8-warnings"/> 
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.7-warnings"/> 
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.3-warnings"/> 
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.21-warnings"/> 
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-warnings"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-warnings"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-warnings"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-warnings"/>   
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.22-warnings"/>   
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.20-warnings"/>   
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.13-warnings"/>   
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.5-warnings"/>   
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.34-warnings"/>   
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.4-warnings"/>   
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.17-warnings"/>   
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.18-warnings"/>   
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.24-warnings"/>   
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-warnings"/>   
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.29-warnings"/>   
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.31-warnings"/>   
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.33-warnings"/>   
        
        
    </phase>

    <phase id="notes">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.4-notes"/>   
        
        
        
    </phase> 
    
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.4;
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
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.8;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.7;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.3;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.21;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7; 
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.4.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.22;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.20;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.13;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.5;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.34;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.4;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.17;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.18;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.24;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.25;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.29;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.31;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.33;
    
</schema>
