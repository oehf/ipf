Title: HITSP C32 v2.4 Schematron Package


Description: This package contains the schematron rules for validating XML documents against the rules and 
guidelines specified by HITSP C32 HITSP Summary Documents Using HL7 Continuity of Care Document (CCD) Component
v2.4 (as well as rules and guidelines referenced to and incorporated into that document).  Please note that these 
schematron files check for conformance only against HITSP/C32, HITSP/C80, IHE PCC (Patient Care Coordination) and 
CDA4CDT rules only.  Running XML documents through these schematron files will not check them against any Clinical
Document Architecture Release 2 (CDA R2) or Continuity of Care (CCD) which the HITSP specifications require.  
Schematron for CCD can be downloaded from the HL7 Wiki (http://wiki.hl7.org/).  HITSP documentation is available 
from the HITSP website (http://hitsp.org/).  IHE documentation is available from the IHE website (http://www.ihe.net/).


Release Date: April 14, 2009


Package Contents:

HITSP_C32.sch  -- Schematron file for HITSP/C32 (2.16.840.1.113883.3.88.11.32.1).

templates -- Directory containing entity (.ent) files imported by the main schematron file.

cdar2c32 -- Directory containing the HL7 CDA R2 schema as modified to include HITSP extensions.

example -- Directory containing sample HITSP/C32 document(s).


Schematron Entity Files

The HITSP/C32 v2.4, HITSP/C83 v1.0 and IHE PCC specifications define multiple templateIds.  NIST has defined one 
schematron entity file for each of these templateIds. If a CCD instance document claims conformance to HITSP/C32 
then the existence of one templateId (2.16.840.1.113883.3.88.11.32.1) is required in the root ClinicalDocument 
element. 

For rules which do not fall under a specific templateId, the entity file is named for the Document and Section within
that document which contains those rules.


HITSP/C32 Validation at NIST web site

NIST has an experimental web site to check validation of various CDA profiles and guidelines, especially those 
produced by IHE and HITSP. The URL is:

          http://xreg2.nist.gov/cda-validation 

This site explains the CDA Guideline Validation project at NIST and includes pointers to the NIST validation page 
referenced above. The Schematron entity files included with this package are implemented at this site.


Disclaimers

Favorable outcome in the use of the test materials on this site does not imply
conformance recognition by NIST, HITSP, CCHIT, or IHE.

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
