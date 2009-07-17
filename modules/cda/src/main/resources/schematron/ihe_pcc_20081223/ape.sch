<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE schema [

<!-- Antepartum Education Specification -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.16.1.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.16.1.3.ent'>

<!-- Coded Patient Education and Consents -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.39 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.39.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.19 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.19.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.4.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.4.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.13 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.13.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.4 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.4.ent'>

]>


<!--
    

-->
<schema xmlns="http://www.ascc.net/xml/schematron" xmlns:cda="urn:hl7-org:v3"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <title>Schematron schema for validating Antepartum Education Specification Module</title>
    <ns prefix="cda" uri="urn:hl7-org:v3"/>
    <ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>
    <phase id="errors">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.16.1.3-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.39-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.19-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4-errors"/>
    </phase>

    <phase id="warning">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.16.1.3-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.39-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.19-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4-warnings"/>
    </phase>

    <phase id="notes">
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.16.1.3-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.39-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.19-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4-notes"/>
    </phase> 
    
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.16.1.3;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.39;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.19;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.4.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.13;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.4;

</schema>
