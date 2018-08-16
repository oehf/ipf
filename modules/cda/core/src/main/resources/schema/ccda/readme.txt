This zip file packages CDA Schema with SDTC extensions approved by HL7 SDWG.


This package supplements the published CDA R2 base standard with updated Schema definitions.

This package was prepared by HL7 SDWG.
 

========================
Contents of the update package:

readme.txt:	
  This file
  
CDA_SDTC.xsd:
   XML schema for message type POCD_MT000040.**No Change in this release.**

POCD_MT000040_SDTC.xsd:
   Schema defining the elements and attributes including SDTC extensions for message type POCD_MT000040

** UPDATE **
   2017-10-10 Added extension approved by SDWG on 2017-05-17
                      Participant2/functionCode
                      Performer2/functionCode

SDTC.xsd:
   Schema defining all extensions in the SDTC namespace. It contains elements, attributes and datatypes

** UPDATE **
   2017-10-10  Added extension approved by SDWG on 2017-05-17 functionCode

datatypes.xsd:
   Schema defining the CDA data types.**No Change in this release.**

datatypes-base_SDTC.xsd:
   Schema defining base data types with SDTC extensions.**No Change in this release.**
   
** UPDATE **   
    2017-10-10  Fixed ED datatype bug approved by SD on 2017-09-13 to allow other content in ED

infrastructureRoot.xsd:
   Schema defining the infrastructureRoot. **No Change in this release.**  

NarrativeBlock.xsd:
   Schema defining constructs allowed in the CDA narrative block. **No Change in this release.**

voc.xsd:
   Schema defining allowed vocabulary values. **No Change in this release.**

Oct 10, 2017