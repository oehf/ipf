<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!DOCTYPE schema [
<!-- Replace baseURI below with a reference to the published Implementation Guide HTML. -->
<!ENTITY baseURI "">
<!ENTITY ent-2.16.840.1.113883.10.20.9 SYSTEM 'templates/2.16.840.1.113883.10.20.9.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.0 SYSTEM 'templates/2.16.840.1.113883.10.20.9.0.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.1 SYSTEM 'templates/2.16.840.1.113883.10.20.9.1.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.2 SYSTEM 'templates/2.16.840.1.113883.10.20.9.2.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.4 SYSTEM 'templates/2.16.840.1.113883.10.20.9.4.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.8 SYSTEM 'templates/2.16.840.1.113883.10.20.9.8.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.9 SYSTEM 'templates/2.16.840.1.113883.10.20.9.9.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.14 SYSTEM 'templates/2.16.840.1.113883.10.20.9.14.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.1.7 SYSTEM '../ccd/templates/2.16.840.1.113883.10.20.1.7.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.1.16 SYSTEM '../ccd/templates/2.16.840.1.113883.10.20.1.16.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.1.35 SYSTEM '../ccd/templates/2.16.840.1.113883.10.20.1.35.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.1.14 SYSTEM '../ccd/templates/2.16.840.1.113883.10.20.1.14.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.1.13 SYSTEM '../ccd/templates/2.16.840.1.113883.10.20.1.13.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.1.8 SYSTEM '../ccd/templates/2.16.840.1.113883.10.20.1.8.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.1.5 SYSTEM '../ccd/templates/2.16.840.1.113883.10.20.1.5.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.1.52 SYSTEM '../ccd/templates/2.16.840.1.113883.10.20.1.52.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.1.31 SYSTEM '../ccd/templates/2.16.840.1.113883.10.20.1.31.ent'>
]>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:cda="urn:hl7-org:v3">

    <title>CDA_PHMR</title>
    <ns prefix="cda" uri="urn:hl7-org:v3"/>
    <ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>

    <phase id='errors'>
        <active pattern='p-2.16.840.1.113883.10.20.9-errors'/>
        <active pattern='p-2.16.840.1.113883.10.20.9.0-errors'/>
        <active pattern='p-2.16.840.1.113883.10.20.9.1-errors'/>
        <active pattern='p-2.16.840.1.113883.10.20.9.2-errors'/>
        <active pattern='p-2.16.840.1.113883.10.20.9.4-errors'/>
        <active pattern='p-2.16.840.1.113883.10.20.9.8-errors'/>
        <active pattern='p-2.16.840.1.113883.10.20.9.9-errors'/>
        <active pattern='p-2.16.840.1.113883.10.20.9.14-errors'/>
        <active pattern="p-2.16.840.1.113883.10.20.1.7-errors"/>
        <active pattern="p-2.16.840.1.113883.10.20.1.16-errors"/>
        <active pattern="p-2.16.840.1.113883.10.20.1.35-errors"/>
        <active pattern="p-2.16.840.1.113883.10.20.1.14-errors"/>
        <active pattern="p-2.16.840.1.113883.10.20.1.13-errors"/>
        <active pattern="p-2.16.840.1.113883.10.20.1.8-errors"/>
        <active pattern="p-2.16.840.1.113883.10.20.1.5-errors"/>
        <active pattern="p-2.16.840.1.113883.10.20.1.52-errors"/>
        <active pattern="p-2.16.840.1.113883.10.20.1.31-errors"/>
    </phase>

    <phase id='warnings'>
        <active pattern='p-2.16.840.1.113883.10.20.9-warning'/>
        <active pattern='p-2.16.840.1.113883.10.20.9.0-warning'/>
        <active pattern='p-2.16.840.1.113883.10.20.9.1-warning'/>
        <active pattern='p-2.16.840.1.113883.10.20.9.2-warning'/>
        <active pattern='p-2.16.840.1.113883.10.20.9.4-warning'/>
        <active pattern='p-2.16.840.1.113883.10.20.9.8-warning'/>
        <active pattern='p-2.16.840.1.113883.10.20.9.9-warning'/>
        <active pattern='p-2.16.840.1.113883.10.20.9.14-warning'/>
        <active pattern='p-2.16.840.1.113883.10.20.1.7-warning'/>
        <active pattern='p-2.16.840.1.113883.10.20.1.16-warning'/>
        <active pattern='p-2.16.840.1.113883.10.20.1.35-warning'/>
        <active pattern='p-2.16.840.1.113883.10.20.1.14-warning'/>
        <active pattern='p-2.16.840.1.113883.10.20.1.13-warning'/>
        <active pattern='p-2.16.840.1.113883.10.20.1.8-warning'/>
        <active pattern='p-2.16.840.1.113883.10.20.1.5-warning'/>
        <active pattern="p-2.16.840.1.113883.10.20.1.52-warning"/>
        <active pattern="p-2.16.840.1.113883.10.20.1.31-warning"/>
    </phase>

    <phase id='note'>
    </phase>

    &ent-2.16.840.1.113883.10.20.9;
    &ent-2.16.840.1.113883.10.20.9.0;
    &ent-2.16.840.1.113883.10.20.9.1;
    &ent-2.16.840.1.113883.10.20.9.2;
    &ent-2.16.840.1.113883.10.20.9.4;
    &ent-2.16.840.1.113883.10.20.9.8;
    &ent-2.16.840.1.113883.10.20.9.9;
    &ent-2.16.840.1.113883.10.20.9.14;
    &ent-2.16.840.1.113883.10.20.1.7;
    &ent-2.16.840.1.113883.10.20.1.16;
    &ent-2.16.840.1.113883.10.20.1.35;
    &ent-2.16.840.1.113883.10.20.1.14;
    &ent-2.16.840.1.113883.10.20.1.13;
    &ent-2.16.840.1.113883.10.20.1.8;
    &ent-2.16.840.1.113883.10.20.1.5;
    &ent-2.16.840.1.113883.10.20.1.52;
    &ent-2.16.840.1.113883.10.20.1.31;

</schema>