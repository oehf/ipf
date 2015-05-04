<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<!DOCTYPE schema [
<!ENTITY ent-2.16.840.1.113883.10.20.9 SYSTEM 'templates/2.16.840.1.113883.10.20.9.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.1 SYSTEM 'templates/2.16.840.1.113883.10.20.9.1.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.2 SYSTEM 'templates/2.16.840.1.113883.10.20.9.2.ent'>


<!ENTITY ent-2.16.840.1.113883.10.20.9.3 SYSTEM 'templates/2.16.840.1.113883.10.20.9.3.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.4 SYSTEM 'templates/2.16.840.1.113883.10.20.9.4.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.5 SYSTEM 'templates/2.16.840.1.113883.10.20.9.5.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.6 SYSTEM 'templates/2.16.840.1.113883.10.20.9.6.ent'>

<!ENTITY ent-2.16.840.1.113883.10.20.9.7 SYSTEM 'templates/2.16.840.1.113883.10.20.9.7.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.8 SYSTEM 'templates/2.16.840.1.113883.10.20.9.8.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.9 SYSTEM 'templates/2.16.840.1.113883.10.20.9.9.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.10 SYSTEM 'templates/2.16.840.1.113883.10.20.9.10.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.11 SYSTEM 'templates/2.16.840.1.113883.10.20.9.11.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.12 SYSTEM 'templates/2.16.840.1.113883.10.20.9.12.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.13 SYSTEM 'templates/2.16.840.1.113883.10.20.9.13.ent'>
<!ENTITY ent-2.16.840.1.113883.10.20.9.14 SYSTEM 'templates/2.16.840.1.113883.10.20.9.14.ent'>


]>
   <schema xmlns="http://purl.oclc.org/dsdl/schematron" xmlns:cda="urn:hl7-org:v3" queryBinding="xslt2">
   <title>PHMR 1.1</title>
   <ns prefix="cda" uri="urn:hl7-org:v3"/>
   <ns prefix="sdtc" uri="urn:hl7-org:sdtc"/>
   <ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance"/>

   <phase id='errors'>
      
      <active pattern='p-2.16.840.1.113883.10.20.9-errors'/>
      
      <active pattern='p-2.16.840.1.113883.10.20.9.1-errors'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.2-errors'/>
      
      <active pattern='p-2.16.840.1.113883.10.20.9.3-errors'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.4-errors'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.5-errors'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.6-errors'/>
      
      <active pattern='p-2.16.840.1.113883.10.20.9.7-errors'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.8-errors'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.9-errors'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.10-errors'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.11-errors'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.12-errors'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.13-errors'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.14-errors'/>

   </phase>

   <phase id='warning'>
      <active pattern='p-2.16.840.1.113883.10.20.9-warnings'/>
      
      <active pattern='p-2.16.840.1.113883.10.20.9.1-warnings'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.2-warnings'/>
      
      <active pattern='p-2.16.840.1.113883.10.20.9.3-warnings'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.4-warnings'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.5-warnings'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.6-warnings'/>
      
      <active pattern='p-2.16.840.1.113883.10.20.9.7-warnings'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.8-warnings'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.9-warnings'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.10-warnings'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.11-warnings'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.12-warnings'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.13-warnings'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.14-warnings'/>

   </phase>

   <phase id='note'>
      <active pattern='p-2.16.840.1.113883.10.20.9-notes'/>
      
      <active pattern='p-2.16.840.1.113883.10.20.9.1-notes'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.2-notes'/>
      
      <active pattern='p-2.16.840.1.113883.10.20.9.3-notes'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.4-notes'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.5-notes'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.6-notes'/>
      
      <active pattern='p-2.16.840.1.113883.10.20.9.7-notes'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.8-notes'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.9-notes'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.10-notes'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.11-notes'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.12-notes'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.13-notes'/>
      <active pattern='p-2.16.840.1.113883.10.20.9.14-notes'/>


   </phase>


   &ent-2.16.840.1.113883.10.20.9;
   
   &ent-2.16.840.1.113883.10.20.9.1;
   &ent-2.16.840.1.113883.10.20.9.2;
   
   &ent-2.16.840.1.113883.10.20.9.3;
   &ent-2.16.840.1.113883.10.20.9.4;
   &ent-2.16.840.1.113883.10.20.9.5;
   &ent-2.16.840.1.113883.10.20.9.6;
   
   &ent-2.16.840.1.113883.10.20.9.7;
   &ent-2.16.840.1.113883.10.20.9.8;
   &ent-2.16.840.1.113883.10.20.9.9;
   &ent-2.16.840.1.113883.10.20.9.10;
   &ent-2.16.840.1.113883.10.20.9.11;
   &ent-2.16.840.1.113883.10.20.9.12;
   &ent-2.16.840.1.113883.10.20.9.13;
   &ent-2.16.840.1.113883.10.20.9.14;



</schema>
