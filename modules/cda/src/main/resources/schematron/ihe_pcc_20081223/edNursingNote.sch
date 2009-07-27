<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE schema [
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.1.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.1.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.13.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.13.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.25 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.25.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.4 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.2.4.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.4 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.3.4.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.13 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.13.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.19 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.19.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.4.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.4.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.21 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.21.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.3.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.3.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.3.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.6 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.2.6.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.3.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.10 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.2.10.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.10.4.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1.ent'>

]>

<!--
    

-->
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:cda="urn:hl7-org:v3"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <title>Schematron schema for validating IHE PCC v3.0 ED Nursing Note Module</title>
    <ns prefix="cda" uri="urn:hl7-org:v3"/>
    <ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>
    <phase id="errors">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.1.2-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.4-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.4-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.19-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.21-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.3-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.6-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.2-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.10-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.2-errors"/>        
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1-errors"/>        
        
    </phase>

    <phase id="warning">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.1.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.4-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.4-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.19-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.21-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.3-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.6-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.10-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1-warnings"/>
        
    </phase>

    <phase id="manual">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.1.2-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.1-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.2-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.4-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.4-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.19-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.21-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.3-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.6-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.2-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.10-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.2-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1-manual"/>
        
    </phase> 
   
    <phase id="notes">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.1.2-notes" />
    </phase>
 
    <!-- Top level ED Nurses Note -->
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.1.2;
    
    <!-- Vital Signs -->
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.25;
    
    <!-- Assessments -->
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.4;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.4;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.13;
    
    <!-- Procedures and Interventions -->
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.19;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.4.1;
    
    <!-- Medications Administered -->
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.21;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.3;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.3.1;
    
    <!-- Intravenous Fluids Administered -->
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.6;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.2;
    
    <!-- ED Disposition -->
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.10;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.2; 
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1;
    
</schema>
