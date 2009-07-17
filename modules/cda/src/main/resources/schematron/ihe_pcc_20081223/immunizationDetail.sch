<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE schema [

<!-- Immunization Detail Specification -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.18.1.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.18.1.2.ent'>

<!-- History of Immunizations -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.23 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.23.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.12 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.12.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.3.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.3.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.3.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.3.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.1.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.1.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.6 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.6.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.1.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.1.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.2.ent'>

<!-- Problem List -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.6 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.6.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.1.ent'>

<!-- History of Past Illness -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.8 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.8.ent'>

<!-- Allergies and Intolerances -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.13 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.13.ent'>

<!-- Medications -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.19 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.19.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.3.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.4.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.4.1.ent'>

<!-- Lab Results -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.13 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.13.ent'>

<!-- Coded Vital Signs -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.13.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.25 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.25.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.13.2.ent'>

<!-- Pregnancy History -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.4 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.5.3.4.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.5 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.13.5.ent'>

<!-- Coded Advance Directives -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.35 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.35.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.34 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.34.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.7 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.13.7.ent'>

]>


<!--
    

-->
<schema xmlns="http://www.ascc.net/xml/schematron" xmlns:cda="urn:hl7-org:v3"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <title>Schematron schema for validating Immunization Detail Specification Module</title>
    <ns prefix="cda" uri="urn:hl7-org:v3"/>
    <ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>
    <phase id="errors">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.18.1.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.23-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.12-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.3-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.3-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.6-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.6-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.8-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.13-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.19-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.4-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.5-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.35-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.34-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.7-errors"/>
    </phase>

    <phase id="warning">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.18.1.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.23-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.12-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.3-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.3-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.6-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.6-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.8-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.13-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.19-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.2-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.4-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.5-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.35-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.34-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.7-warnings"/>
    </phase>

    <phase id="notes">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.18.1.2-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.23-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.12-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.3-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3.1-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.3-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1.2-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.6-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.1.1-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.2-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.6-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.2-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.5.1-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.8-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.13-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.19-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.3-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.1-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.2-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.4-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.5-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.35-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.34-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.7-notes"/>
    </phase> 
    
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.18.1.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.23;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.12;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.3;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.3.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.3;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.1.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.6;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.1.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.6;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.8;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.13;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.19;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.3;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.4.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.13;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.25;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.4;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.5;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.35;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.34;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.7;

</schema>
