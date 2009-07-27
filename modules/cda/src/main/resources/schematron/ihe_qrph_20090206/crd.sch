<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!DOCTYPE schema [

<!-- IHE QRPH Technical Framework Supplement - Clinical Research Data Capture (CRD) - CCD Option -->
<!ENTITY ent-crd SYSTEM 'templates/crd.ent'>

<!-- Active Problems Section -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.6 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.6.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.3 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.5.3.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.1.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.1.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.6 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.6.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.1.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.1.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.2.ent'>

<!-- Past Medical History -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.8 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.8.ent'>

<!-- Procedures and Interventions -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.19 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.19.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.4.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.4.1.ent'>

<!-- Social History -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.16 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.16.ent'>

<!-- Current Medications -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.19 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.19.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.7.2.ent'>

<!-- Vital Signs -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.13.1.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.2 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.13.2.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.25 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.25.ent'>

<!-- Physical Exam -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.15 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.15.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.24 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.24.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.25  SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.25.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.16 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.16.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.48 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.48.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.17 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.17.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.18 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.18.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.19 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.19.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.20 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.20.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.21 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.21.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.22 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.22.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.23 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.23.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.24 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.24.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.25 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.25.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.26 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.26.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.27 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.27.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.28 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.28.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.29 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.29.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.30 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.30.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.31 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.31.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.32 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.32.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.33 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.33.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.34 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.34.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.35 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.35.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.36 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.36.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.37 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.9.37.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.1.16.2.1 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.1.16.2.1.ent'>

<!-- Allergies and Other Adverse Reactions -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.13 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.13.ent'>

<!-- Coded Results -->
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.3.28 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.3.28.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.4 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.4.ent'>
<!ENTITY ent-1.3.6.1.4.1.19376.1.5.3.1.4.13 SYSTEM 'templates/1.3.6.1.4.1.19376.1.5.3.1.4.13.ent'>

]>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:cda="urn:hl7-org:v3">
    <!-- 
        To use iso schematron instead of schematron 1.5, 
        change the xmlns attribute from
        "http://www.ascc.net/xml/schematron" 
        to 
        "http://purl.oclc.org/dsdl/schematron"
    -->
    <title>IHE QRPH Technical Framework Supplement -- Clinical Research Data Capture (CRD) -- CCD Option</title>
    <ns prefix="cda" uri="urn:hl7-org:v3"/>
    <ns prefix="sdtc" uri="urn:hl7-org:sdtc"/>
    <ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>
    
    <phase id='errors'>
        <active pattern='p-crd-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.3.6-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.5.2-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.5.1-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.5-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.5.3-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.1.2-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.6-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.1-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.1.1-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.2-errors'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.3.8-errors'/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.19-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.16-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.19-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.2-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.15-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.24-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.16-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.48-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.17-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.18-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.19-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.20-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.21-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.22-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.23-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.24-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.25-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.26-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.27-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.28-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.29-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.30-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.31-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.32-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.33-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.34-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.35-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.36-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.37-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.16.2.1-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.13-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.28-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4-errors"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13-errors"/>
    </phase>
    
    <phase id='warning'>        
        <active pattern='p-crd-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.3.6-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.5.2-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.5.1-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.5-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.5.3-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.1.2-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.6-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.1-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.1.1-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.2-warning'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.3.8-warning'/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11-warning"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.19-warning"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-warning"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.16-warning"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.19-warning"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-warning"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-warning"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-warning"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2-warning"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.1-warning"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.2-warning"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-warning"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.15-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.24-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.16-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.48-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.17-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.18-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.19-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.20-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.21-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.22-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.23-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.24-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.25-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.26-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.27-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.28-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.29-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.30-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.31-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.32-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.33-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.34-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.35-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.36-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.37-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.16.2.1-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.13-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.28-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4-warnings"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13-warnings"/>
    </phase>
       
    <phase id='note'>          
        <active pattern='p-crd-note'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.3.6-note'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.5.2-note'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.5.1-note'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.5-note'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.5.3-note'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.1.2-note'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.6-note'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.1-note'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.1.1-note'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.4.2-note'/>
        <active pattern='p-1.3.6.1.4.1.19376.1.5.3.1.3.8-note'/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11-note"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.19-note"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4.1-note"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.16-note"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.19-note"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7-note"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.1-note"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.7.2-note"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2-note"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.1-note"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13.2-note"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-note"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.15-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.24-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.25-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.16-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.48-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.17-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.18-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.19-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.20-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.21-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.22-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.23-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.24-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.25-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.26-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.27-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.28-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.29-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.30-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.31-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.32-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.33-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.34-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.35-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.36-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.9.37-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.16.2.1-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.13-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.28-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.4-notes"/>
        <active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.4.13-notes"/>
    </phase>
  
    &ent-crd;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.6;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.5.3;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.1.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.6;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.1.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.8;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.11;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.19;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.4.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.16;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.19;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.7.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.5.3.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.13.2;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.25;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.15;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.24;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.25;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.16;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.48;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.17;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.18;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.19;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.20;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.21;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.22;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.23;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.24;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.25;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.26;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.27;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.28;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.29;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.30;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.31;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.32;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.33;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.34;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.35;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.36;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.9.37;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.1.16.2.1;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.13;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.3.28;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.4;
    &ent-1.3.6.1.4.1.19376.1.5.3.1.4.13;

</schema>
