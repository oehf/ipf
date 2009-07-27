<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE schema [

<!-- ED Triage Note -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.1.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.1.1.ent'>

<!-- Chief Complaint -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1.ent'>

<!-- Reason For Visit -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1.1.ent'>

<!-- Mode of Arrival -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.10.3.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.10.3.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1.ent'>

<!-- History of Present Illness -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.4 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.4.ent'>

<!-- Past Medical History -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.8 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.8.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.3.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.1.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.1.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.6 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.6.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.1.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.1.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.2.ent'>

<!-- List of Surgeries -->
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

<!-- History of Pregnancies -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.4 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.5.3.4.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.5 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.13.5.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.13 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.13.ent'>

<!-- Current Medications -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.19 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.19.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.4.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.4.1.ent'>

<!-- Allergies -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.13 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.13.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.3.ent'>

<!-- Acuity Assessment -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.2.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.3.1.ent'>

<!-- Vital Signs -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.13.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.13.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.25 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.25.ent'>

<!-- Assessments -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.4 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.2.4.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.4 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.3.4.ent'>

<!-- Procedures and Interventions -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.19 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.19.ent'>

<!-- Medications Administered -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.21 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.21.ent'>

<!-- Intravenous Fluids Administered -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.6 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.2.6.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.3.2.ent'>

]>

<!--

-->
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:cda="urn:hl7-org:v3"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <title>Schematron schema for validating IHE PCC v3.0 ED Triage Note Module</title>
    <ns prefix="cda" uri="urn:hl7-org:v3"/>
    <ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>
    <phase id="errors">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.1.1-errors"/>   
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.3.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.4-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.8-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.3-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.6-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.11-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.23-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.12-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.3-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.14-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.16-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.4-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.5-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.19-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.13-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.3-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.4-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.4-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.19-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.21-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.6-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.2-errors"/>
        
        
    </phase>
    
    <phase id="warning">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.1.1-warnings"/>    
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1.1-warnings"/>    
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.3.2-warnings"/>    
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1-warnings"/>    
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.4-warnings"/>    
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.8-warnings"/>    
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.3-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.6-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.11-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.23-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.12-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.3-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.14-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.16-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.4-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.5-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.19-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.13-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.3-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.4-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.4-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.19-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.21-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.6-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.2-warnings"/>
        
    </phase>
    <phase id="manual">

        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.1.1-manual"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1.1-manual"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.3.2-manual"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1-manual"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.4-manual"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.8-manual"/>       
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.2-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.1-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.3-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1.2-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.6-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1.1-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.2-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.11-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.23-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.12-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.3-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.14-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.16-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.4-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.5-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.19-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.13-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.3-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.2-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.1-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.1-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.2-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.4-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.4-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.19-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.21-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.6-manual"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.2-manual"/>
        

    </phase> 

    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.1.1; 
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.10.3.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.10.4.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.4;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.8;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.3;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.1.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.6;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.1.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.11;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.23;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.12;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.3;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.3.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.14;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.16;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.4;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.5;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.13;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.19;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.4.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.13;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.3;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.25;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.4;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.4;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.19;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.21;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.6;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.3.2;
    
</schema>
