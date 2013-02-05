Title: HITSP C32 v2.5 -- HITSP C83 v2.0 Schematron Package

Description: This package contains the schematron rules for validating XML documents 
against the guidelines specified by HITSP/C32 - HITSP Summary Documents Using HL7 
Continuity of Care Document (CCD) Component v2.5.  In addition, the HITSP/C32 document 
imports rules and value set definitions from HITSP/C83 CDA Content Modules Component 
v2.0 and HITSP/C80 Clinical Document and Message Terminology Component v2.0.  Please 
note that these schematron files check for conformance against HITSP and IHE PCC rules
only.  Running XML documents through these schematron files will not check them against 
any Clinical Document Architecture Release 2 (CDA R2), or Continuity of Care (CCD) rules 
which the HITSP/IHE specifications may require.  Schematron for CCD and CDA4CDT can be 
downloaded from the HL7 Wiki (http://wiki.hl7.org/).

Release Date: October 10, 2010


Package Contents:

HITSP_C32.sch -- Schematron file for HITSP/C32 - HITSP Summary Documents Using HL7
Continuity of Care Document (CCD) Component.

templates -- Directory containing all entity files which are imported by the main .sch 
schematron file.

cdar2c32 -- The CDA R2 schema, originally from HL7, but modified to allow for additions
to the schema as specified in HITSP/C83.


Outside Documentation

Documentation for HITSP can be found at the following URL:

http://hitsp.org/ConstructSet_Details.aspx?&PrefixAlpha=4&PrefixNumeric=32

http://hitsp.org/ConstructSet_Details.aspx?&PrefixAlpha=4&PrefixNumeric=83

http://hitsp.org/ConstructSet_Details.aspx?&PrefixAlpha=4&PrefixNumeric=80


Further Information

Additional information about this project can be read on-line at our website:
http://xreg2.nist.gov/cda-validation/


Disclaimers

Favorable outcome in the use of the test materials on this site does not imply
conformance recognition by NIST, CCHIT, or IHE.

Links to non-Federal Government Web sites do not imply NIST endorsement of any
particular product, service, organization, company, information provider, or
content.

This software was developed at the National Institute of Standards and Technology
by employees of the Federal Government in the course of their official duties.
Pursuant to title 17 Section 105 of the United States Code this software is not
subject to copyright protection and is in the public domain.

The CDA Guideline Validator is an experimental system. NIST assumes no responsibility
whatsoever for its use by other parties, and makes no guarantees, expressed or implied,
about its quality, reliability, or any other characteristic. We would appreciate
acknowledgment if the software is used. This software can be redistributed and/or
modified freely provided that any derivative works bear some notice that they are
derived from it, and any modified versions bear some notice that they have been
modified.
