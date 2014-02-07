<?xml version="1.0" encoding="utf-8" standalone="yes"?>
<sch:schema xmlns:voc="http://www.lantanagroup.com/voc" xmlns:svs="urn:ihe:iti:svs:2008" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:cda="urn:hl7-org:v3" xmlns:sdtc="urn:hl7-org:sdtc" xmlns="urn:hl7-org:v3" xmlns:sch="http://purl.oclc.org/dsdl/schematron">
  <sch:ns prefix="voc" uri="http://www.lantanagroup.com/voc" />
  <sch:ns prefix="svs" uri="urn:ihe:iti:svs:2008" />
  <sch:ns prefix="xsi" uri="http://www.w3.org/2001/XMLSchema-instance" />
  <sch:ns prefix="cda" uri="urn:hl7-org:v3" />
  <sch:ns prefix="sdtc" uri="urn:hl7-org:sdtc" />
  <sch:let name="vocDocument" value="document('schematron/ccda/voc.xml')"/>
  <sch:phase id="errors">
    <sch:active pattern="p-consolidaton-document-types" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.15.3.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.15.3.8-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.7-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.7.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.3-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.3.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.4-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.4.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.6-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.6.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.16-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.5-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.5.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.2-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.5.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.26-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.5.2-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.27-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.28-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.9-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.8-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.6-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.25-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.24.3.90-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.7-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.20-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.23-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.17-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.18-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.30-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.19-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.24-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.14-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.29-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.1.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.9-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.31-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.10-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.32-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.3-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.4-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.33-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.34-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.35-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.36-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.8-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.9-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.10-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.37-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.2.10-errors" />
    <sch:active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.18-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.11-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.11.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.20-errors" />
    <sch:active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.12-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.13-errors" />
    <sch:active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.1-errors" />
    <sch:active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.4-errors" />
    <sch:active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.5-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.21.2.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.21.2.2-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.16-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.2-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.14-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.18-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.21-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.15-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.17-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.22-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.23-errors" />
    <sch:active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.26-errors" />
    <sch:active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.33-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.24-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.2.5-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.12-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.13-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.26-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.32-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.7.12-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.7.14-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.7.13-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.33-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.29-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.27-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.25-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.18.2.12-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.18.2.9-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.28-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.30-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.31-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.34-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.35-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.38-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.39-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.36-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.37-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.40-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.3-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.5-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.4-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.2-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.2-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.8-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.7-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.6-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.1.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.6-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.1.2-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.38-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.39-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.40-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.41-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.42-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.43-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.44-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.46-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.45-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.47-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.21.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.48-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.21.2.3-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.22.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.49-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.50-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.51-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.52-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.60-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.61-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.1.19-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.53-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.54-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.2.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.5-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.3-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.4-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.5-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.63-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.8-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.9-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.10-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.11-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.12-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.13-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.14-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.5.1.1-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.64-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.41-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.42-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.43-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.5.3-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.65-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.44-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.45-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.5.4-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.67-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.72-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.74-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.68-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.73-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.66-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.75-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.70-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.69-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.76-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.77-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.85-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.78-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.79-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.80-errors" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.86-errors" />
  </sch:phase>
  <sch:phase id="warnings">
    <sch:active pattern="p-2.16.840.1.113883.10.20.15.3.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.15.3.8-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.7-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.7.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.3-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.3.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.4-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.4.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.6-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.6.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.16-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.5-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.5.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.2-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.5.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.26-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.5.2-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.27-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.28-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.9-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.8-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.6-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.25-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.24.3.90-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.7-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.20-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.23-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.17-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.18-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.30-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.19-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.24-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.14-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.29-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.1.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.9-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.31-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.10-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.32-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.3-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.4-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.33-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.34-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.35-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.36-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.8-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.9-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.10-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.37-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.2.10-warnings" />
    <sch:active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.18-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.11-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.11.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.20-warnings" />
    <sch:active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.12-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.13-warnings" />
    <sch:active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.1-warnings" />
    <sch:active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.4-warnings" />
    <sch:active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.5-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.21.2.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.21.2.2-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.16-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.2-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.14-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.18-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.21-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.15-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.17-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.22-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.23-warnings" />
    <sch:active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.26-warnings" />
    <sch:active pattern="p-1.3.6.1.4.1.19376.1.5.3.1.3.33-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.24-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.2.5-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.12-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.13-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.26-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.32-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.7.12-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.7.14-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.7.13-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.33-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.29-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.27-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.25-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.18.2.12-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.18.2.9-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.28-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.30-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.31-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.34-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.35-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.38-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.39-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.36-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.37-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.40-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.3-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.5-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.4-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.2-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.2-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.8-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.7-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.1.6-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.1.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.6-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.1.2-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.38-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.39-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.40-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.41-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.42-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.43-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.44-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.46-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.45-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.47-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.21.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.48-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.21.2.3-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.22.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.49-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.50-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.51-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.52-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.60-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.61-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.1.19-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.53-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.54-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.2.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.5-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.3-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.4-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.5-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.63-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.8-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.9-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.10-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.11-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.12-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.13-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.6.2.14-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.5.1.1-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.64-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.41-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.42-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.43-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.5.3-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.65-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.44-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.2.45-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.5.4-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.67-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.72-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.74-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.68-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.73-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.66-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.75-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.70-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.69-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.76-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.77-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.85-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.78-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.79-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.80-warnings" />
    <sch:active pattern="p-2.16.840.1.113883.10.20.22.4.86-warnings" />
  </sch:phase>
  <sch:pattern id="p-consolidaton-document-types">
    <sch:rule id="p-consolidaton-document-types-errors-abstract" abstract="true">
      <sch:assert id="a-doc-type-check" test="cda:ClinicalDocument/cda:templateId[           @root='2.16.840.1.113883.10.20.22.1.2' or           @root='2.16.840.1.113883.10.20.22.1.4' or            @root='2.16.840.1.113883.10.20.22.1.5' or            @root='2.16.840.1.113883.10.20.22.1.8' or            @root='2.16.840.1.113883.10.20.22.1.3' or            @root='2.16.840.1.113883.10.20.22.1.7' or            @root='2.16.840.1.113883.10.20.22.1.6' or            @root='2.16.840.1.113883.10.20.22.1.9' or            @root='2.16.840.1.113883.10.20.22.1.10']">
        In order to validate this file against the Consolidated CDA templates, you must include one of the following in ClinicalDocument/templateId/@root: 
        2.16.840.1.113883.10.20.22.1.2,
        2.16.840.1.113883.10.20.22.1.4,
        2.16.840.1.113883.10.20.22.1.5,
        2.16.840.1.113883.10.20.22.1.8,
        2.16.840.1.113883.10.20.22.1.3,
        2.16.840.1.113883.10.20.22.1.7,
        2.16.840.1.113883.10.20.22.1.6,
        2.16.840.1.113883.10.20.22.1.9, or 
        2.16.840.1.113883.10.20.22.1.10
      </sch:assert>
    </sch:rule>
    <sch:rule id="p-consolidaton-document-types-errors" context="/">
      <sch:extends rule="p-consolidaton-document-types-errors-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.15.3.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.15.3.1-errors-abstract" abstract="true">
      <sch:assert id="a-444" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:444).</sch:assert>
      <sch:assert id="a-445" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:445).</sch:assert>
      <sch:assert id="a-448" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:448).</sch:assert>
      <sch:assert id="a-450" test="count(cda:value[@xsi:type='TS'])=1">SHALL contain exactly one [1..1] value with @xsi:type="TS" (CONF:450).</sch:assert>
      <sch:assert id="a-19096" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19096).</sch:assert>
      <sch:assert id="a-19139" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19139).</sch:assert>
      <sch:assert id="a-19140" test="cda:code[@code='11778-8' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="11778-8" Estimated date of delivery (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:19140).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.15.3.1-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.15.3.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.15.3.1-errors-abstract" />
      <sch:assert id="a-16762" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.15.3.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:16762) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.15.3.1" (CONF:16763).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.15.3.8-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.15.3.8-errors-abstract" abstract="true">
      <sch:assert id="a-451" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:451).</sch:assert>
      <sch:assert id="a-452" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:452).</sch:assert>
      <sch:assert id="a-455" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:455).</sch:assert>
      <sch:assert id="a-457" test="count(cda:value[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='77386006'][@codeSystem='2.16.840.1.113883.6.96'][@xsi:type='CD'])=1">SHALL contain exactly one [1..1] value="77386006" Pregnant with @xsi:type="CD" (CodeSystem: SNOMED-CT 2.16.840.1.113883.6.96 STATIC) (CONF:457).</sch:assert>
      <sch:assert id="a-19110" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19110).</sch:assert>
      <sch:assert id="a-19153" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19153).</sch:assert>
      <sch:assert id="a-19154" test="cda:code[@code='ASSERTION' and @codeSystem='2.16.840.1.113883.5.4']">This code SHALL contain exactly one [1..1] @code="ASSERTION" Assertion (CodeSystem: ActCode 2.16.840.1.113883.5.4 STATIC) (CONF:19154).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.15.3.8-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.15.3.8']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.15.3.8-errors-abstract" />
      <sch:assert id="a-16768" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.15.3.8'])=1">SHALL contain exactly one [1..1] templateId (CONF:16768) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.15.3.8" (CONF:16868).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.1-errors-abstract" abstract="true">
      <sch:assert id="a-5250" test="cda:typeId[@root='2.16.840.1.113883.1.3']">This typeId SHALL contain exactly one [1..1] @root="2.16.840.1.113883.1.3" (CONF:5250).</sch:assert>
      <sch:assert id="a-5251" test="cda:typeId[@extension='POCD_HD000040']">This typeId SHALL contain exactly one [1..1] @extension="POCD_HD000040" (CONF:5251).</sch:assert>
      <sch:assert id="a-5253" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:5253).</sch:assert>
      <sch:assert id="a-5254" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:5254).</sch:assert>
      <sch:assert id="a-5256" test="count(cda:effectiveTime)=1">SHALL contain exactly one [1..1] effectiveTime (CONF:5256).</sch:assert>
      <sch:assert id="a-5259" test="count(cda:confidentialityCode[@code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.1.11.16926']/voc:code/@value])=1">SHALL contain exactly one [1..1] confidentialityCode, which SHOULD be selected from ValueSet HL7 BasicConfidentialityKind 2.16.840.1.113883.1.11.16926 STATIC 2010-04-21 (CONF:5259).</sch:assert>
      <sch:assert id="a-5266" test="count(cda:recordTarget) &gt; 0">SHALL contain at least one [1..*] recordTarget (CONF:5266).</sch:assert>
      <sch:assert id="a-5267" test="cda:recordTarget[count(cda:patientRole)=1]">Such recordTargets SHALL contain exactly one [1..1] patientRole (CONF:5267).</sch:assert>
      <sch:assert id="a-5268" test="cda:recordTarget/cda:patientRole[count(cda:id) &gt; 0]">This patientRole SHALL contain at least one [1..*] id (CONF:5268).</sch:assert>
      <sch:assert id="a-5271" test="cda:recordTarget/cda:patientRole[count(cda:addr) &gt; 0]">This patientRole SHALL contain at least one [1..*] addr (CONF:5271).</sch:assert>
      <sch:assert id="a-5280" test="cda:recordTarget/cda:patientRole[count(cda:telecom) &gt; 0]">This patientRole SHALL contain at least one [1..*] telecom (CONF:5280).</sch:assert>
      <sch:assert id="a-5283" test="cda:recordTarget/cda:patientRole[count(cda:patient)=1]">This patientRole SHALL contain exactly one [1..1] patient (CONF:5283).</sch:assert>
      <sch:assert id="a-5284" test="cda:recordTarget/cda:patientRole/cda:patient[count(cda:name)=1]">This patient SHALL contain exactly one [1..1] name (CONF:5284).</sch:assert>
      <sch:assert id="a-5298" test="cda:recordTarget/cda:patientRole/cda:patient[count(cda:birthTime)=1]">This patient SHALL contain exactly one [1..1] birthTime (CONF:5298).</sch:assert>
      <sch:assert id="a-5299-c" test="string-length(cda:recordTarget/cda:patientRole/cda:patient/cda:birthTime/@value) &gt;= 4">SHALL be precise to year (CONF:5299).</sch:assert>
      <sch:assert id="a-5361" test="count(cda:typeId)=1">SHALL contain exactly one [1..1] typeId (CONF:5361).</sch:assert>
      <sch:assert id="a-5363" test="count(cda:id)=1">SHALL contain exactly one [1..1] id (CONF:5363).</sch:assert>
      <sch:assert id="a-5372" test="count(cda:languageCode)=1">SHALL contain exactly one [1..1] languageCode, which SHALL be selected from ValueSet Language 2.16.840.1.113883.1.11.11526 DYNAMIC (CONF:5372).</sch:assert>
      <sch:assert id="a-5428" test="cda:author/cda:assignedAuthor[count(cda:telecom) &gt; 0]">This assignedAuthor SHALL contain at least one [1..*] telecom (CONF:5428).</sch:assert>
      <sch:assert id="a-5444" test="count(cda:author) &gt; 0">SHALL contain at least one [1..*] author (CONF:5444).</sch:assert>
      <sch:assert id="a-5445" test="cda:author[count(cda:time)=1]">Such authors SHALL contain exactly one [1..1] time (CONF:5445).</sch:assert>
      <sch:assert id="a-5448" test="cda:author[count(cda:assignedAuthor)=1]">Such authors SHALL contain exactly one [1..1] assignedAuthor (CONF:5448).</sch:assert>
      <sch:assert id="a-5449" test="cda:author/cda:assignedAuthor[count(cda:id[@root])=1]">This assignedAuthor SHALL contain exactly one [1..1] id (CONF:5449) such that it SHALL contain exactly one [1..1] @root (CONF:16786).</sch:assert>
      <sch:assert id="a-5452" test="cda:author/cda:assignedAuthor[count(cda:addr) &gt; 0]">This assignedAuthor SHALL contain at least one [1..*] addr (CONF:5452).</sch:assert>
      <sch:assert id="a-5519" test="count(cda:custodian)=1">SHALL contain exactly one [1..1] custodian (CONF:5519).</sch:assert>
      <sch:assert id="a-5520" test="cda:custodian[count(cda:assignedCustodian)=1]">This custodian SHALL contain exactly one [1..1] assignedCustodian (CONF:5520).</sch:assert>
      <sch:assert id="a-5521" test="cda:custodian/cda:assignedCustodian[count(cda:representedCustodianOrganization)=1]">This assignedCustodian SHALL contain exactly one [1..1] representedCustodianOrganization (CONF:5521).</sch:assert>
      <sch:assert id="a-5522" test="cda:custodian/cda:assignedCustodian/cda:representedCustodianOrganization[count(cda:id) &gt; 0]">This representedCustodianOrganization SHALL contain at least one [1..*] id (CONF:5522).</sch:assert>
      <sch:assert id="a-5524" test="cda:custodian/cda:assignedCustodian/cda:representedCustodianOrganization[count(cda:name)=1]">This representedCustodianOrganization SHALL contain exactly one [1..1] name (CONF:5524).</sch:assert>
      <sch:assert id="a-5525" test="cda:custodian/cda:assignedCustodian/cda:representedCustodianOrganization[count(cda:telecom)=1]">This representedCustodianOrganization SHALL contain exactly one [1..1] telecom (CONF:5525).</sch:assert>
      <sch:assert id="a-5559" test="cda:custodian/cda:assignedCustodian/cda:representedCustodianOrganization[count(cda:addr)=1]">This representedCustodianOrganization SHALL contain exactly one [1..1] addr (CONF:5559).</sch:assert>
      <sch:assert id="a-6394" test="cda:recordTarget/cda:patientRole/cda:patient[count(cda:administrativeGenderCode)=1]">This patient SHALL contain exactly one [1..1] administrativeGenderCode, which SHALL be selected from ValueSet Administrative Gender (HL7 V3) 2.16.840.1.113883.1.11.1 DYNAMIC (CONF:6394).</sch:assert>
      <sch:assert id="a-10411-c" test="(count(cda:recordTarget/cda:patientRole/cda:patient/cda:name/cda:given) &gt; 0 and cda:recordTarget/cda:patientRole/cda:patient/cda:name/cda:family)">The content of name SHALL be a conformant US Realm Patient Name (PTN.US.FIELDED) (2.16.840.1.113883.10.20.22.5.1) (CONF:10411).</sch:assert>
      <sch:assert id="a-10412-c" test="cda:recordTarget/cda:patientRole/cda:addr[cda:streetAddressLine and cda:city and ((not(cda:country) or cda:country!='US') or (cda:country='US' and cda:state and cda:postalCode))]">The content of addr SHALL be a conformant US Realm Address (AD.US.FIELDED) (2.16.840.1.113883.10.20.22.5.2) (CONF:10412).</sch:assert>
      <sch:assert id="a-10421-c" test="cda:custodian/cda:assignedCustodian/cda:representedCustodianOrganization/cda:addr[cda:streetAddressLine and cda:city and ((not(cda:country) or cda:country!='US') or (cda:country='US' and cda:state and cda:postalCode))]">The content of addr SHALL be a conformant US Realm Address (AD.US.FIELDED) (2.16.840.1.113883.10.20.22.5.2) (CONF:10421).</sch:assert>
      <sch:assert id="a-16790-c" test="count(cda:author/cda:assignedAuthor/cda:assignedPerson)=1 or count(cda:author/cda:assignedAuthor/cda:assignedAuthoringDevice)=1">There SHALL be exactly one assignedAuthor/assignedPerson or exactly one assignedAuthor/assignedAuthoringDevice (CONF:16790).</sch:assert>
      <sch:assert id="a-16791" test="count(cda:realmCode[@code='US'])=1">SHALL contain exactly one [1..1] realmCode="US" (CONF:16791).</sch:assert>
      <sch:assert id="a-16865-c" test="string-length(cda:effectiveTime//@value)&gt;=8">The content SHALL be a conformant US Realm Date and Time (DTM.US.FIELDED) (2.16.840.1.113883.10.20.22.5.4) (CONF:16865).</sch:assert>
      <sch:assert id="a-16866-c" test="string-length(cda:author/cda:time//@value)&gt;=8">The content SHALL be a conformant US Realm Date and Time (DTM.US.FIELDED) (2.16.840.1.113883.10.20.22.5.4) (CONF:16866).</sch:assert>
      <sch:assert id="a-16871-c" test="cda:author/cda:assignedAuthor/cda:addr[cda:streetAddressLine and cda:city and ((not(cda:country) or cda:country!='US') or (cda:country='US' and cda:state and cda:postalCode))]">The content SHALL be a conformant US Realm Address (AD.US.FIELDED) (2.16.840.1.113883.10.20.22.5.2) (CONF:16871).</sch:assert>
      <sch:assert id="a-19521-c" test="not(cda:author/cda:assignedAuthor/cda:assignedPerson) or cda:author/cda:assignedAuthor[count(cda:id[@root='2.16.840.1.113883.4.6'])=1]">If this assignedAuthor is an assignedPerson the assignedAuthor id SHALL contain exactly one [1..1] @root="2.16.840.1.113883.4.6" National Provider Identifier (CONF:19521).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.1-errors" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-errors-abstract" />
      <sch:assert id="a-5252" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.1.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:5252) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.1.1" (CONF:10036).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.7-errors">
    <!--Pattern is used in an implied relationship.-->
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.7-errors-abstract" abstract="true">
      <sch:assert id="a-6273" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:6273).</sch:assert>
      <sch:assert id="a-15423" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15423).</sch:assert>
      <sch:assert id="a-15424" test="cda:code[@code='47519-4' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="47519-4" History of Procedures (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15424).</sch:assert>
      <sch:assert id="a-17184" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:17184).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.7-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.7']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.7-errors-abstract" />
      <sch:assert id="a-6270" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.7'])=1">SHALL contain exactly one [1..1] templateId (CONF:6270) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.7" (CONF:6271).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.7.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.7.1-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.7-errors-abstract" />
      <sch:assert id="a-7893" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7893).</sch:assert>
      <sch:assert id="a-7894" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7894).</sch:assert>
      <sch:assert id="a-15425" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15425).</sch:assert>
      <sch:assert id="a-15426" test="cda:code[@code='47519-4' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="47519-4" History of Procedures (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15426).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.7.1-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.7.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.7.1-errors-abstract" />
      <sch:assert id="a-7891" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.7.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:7891) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.7.1" (CONF:10447).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.3-errors">
    <!--Pattern is used in an implied relationship.-->
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.3-errors-abstract" abstract="true">
      <sch:assert id="a-7118" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7118).</sch:assert>
      <sch:assert id="a-8891" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8891).</sch:assert>
      <sch:assert id="a-15431" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15431).</sch:assert>
      <sch:assert id="a-15432" test="cda:code[@code='30954-2' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="30954-2" Relevant diagnostic tests and/or laboratory data (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15432).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.3-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.3']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.3-errors-abstract" />
      <sch:assert id="a-7116" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.3'])=1">SHALL contain exactly one [1..1] templateId (CONF:7116) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.3" (CONF:9136).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.3.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.3.1-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.3-errors-abstract" />
      <sch:assert id="a-7111" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7111).</sch:assert>
      <sch:assert id="a-7112" test="count(cda:entry[count(cda:organizer[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.1'])=1]) &gt; 0">SHALL contain at least one [1..*] entry (CONF:7112) such that it SHALL contain exactly one [1..1] Result Organizer (templateId:2.16.840.1.113883.10.20.22.4.1) (CONF:15516).</sch:assert>
      <sch:assert id="a-8892" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8892).</sch:assert>
      <sch:assert id="a-15433" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15433).</sch:assert>
      <sch:assert id="a-15434" test="cda:code[@code='30954-2' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="30954-2" Relevant diagnostic tests and/or laboratory data (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15434).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.3.1-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.3.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.3.1-errors-abstract" />
      <sch:assert id="a-7108" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.3.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:7108) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.3.1" (CONF:9137).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.4-errors">
    <!--Pattern is used in an implied relationship.-->
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.4-errors-abstract" abstract="true">
      <sch:assert id="a-7270" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7270).</sch:assert>
      <sch:assert id="a-9966" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:9966).</sch:assert>
      <sch:assert id="a-15242" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15242).</sch:assert>
      <sch:assert id="a-15243" test="cda:code[@code='8716-3' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="8716-3" Vital Signs (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15243).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.4-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.4']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.4-errors-abstract" />
      <sch:assert id="a-7268" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.4'])=1">SHALL contain exactly one [1..1] templateId (CONF:7268) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.4" (CONF:10451).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.4.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.4.1-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.4-errors-abstract" />
      <sch:assert id="a-7275" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7275).</sch:assert>
      <sch:assert id="a-7276" test="count(cda:entry[count(cda:organizer[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.26'])=1]) &gt; 0">SHALL contain at least one [1..*] entry (CONF:7276) such that it SHALL contain exactly one [1..1] Vital Signs Organizer (templateId:2.16.840.1.113883.10.20.22.4.26) (CONF:15964).</sch:assert>
      <sch:assert id="a-9967" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:9967).</sch:assert>
      <sch:assert id="a-15962" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15962).</sch:assert>
      <sch:assert id="a-15963" test="cda:code[@code='8716-3' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="8716-3" Vital Signs (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15963).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.4.1-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.4.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.4.1-errors-abstract" />
      <sch:assert id="a-7273" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.4.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:7273) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.4.1" (CONF:10452).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.6-errors">
    <!--Pattern is used in an implied relationship.-->
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.6-errors-abstract" abstract="true">
      <sch:assert id="a-7802" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7802).</sch:assert>
      <sch:assert id="a-7803" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7803).</sch:assert>
      <sch:assert id="a-15345" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15345).</sch:assert>
      <sch:assert id="a-15346" test="cda:code[@code='48765-2' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="48765-2" Allergies, adverse reactions, alerts (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15346).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.6-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.6']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.6-errors-abstract" />
      <sch:assert id="a-7800" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.6'])=1">SHALL contain exactly one [1..1] templateId (CONF:7800) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.6" (CONF:10378).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.6.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.6.1-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.6-errors-abstract" />
      <sch:assert id="a-7530" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7530).</sch:assert>
      <sch:assert id="a-7531" test="count(cda:entry[count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.30'])=1]) &gt; 0">SHALL contain at least one [1..*] entry (CONF:7531) such that it SHALL contain exactly one [1..1] Allergy Problem Act (templateId:2.16.840.1.113883.10.20.22.4.30) (CONF:15446).</sch:assert>
      <sch:assert id="a-7534" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7534).</sch:assert>
      <sch:assert id="a-15349" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15349).</sch:assert>
      <sch:assert id="a-15350" test="cda:code[@code='48765-2' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="48765-2" Allergies, adverse reactions, alerts (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15350).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.6.1-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.6.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.6.1-errors-abstract" />
      <sch:assert id="a-7527" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.6.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:7527) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.6.1" (CONF:10379).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.16-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.16-errors-abstract" abstract="true">
      <sch:assert id="a-7496" test="@classCode='SBADM'">SHALL contain exactly one [1..1] @classCode="SBADM" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7496).</sch:assert>
      <sch:assert id="a-7497" test="@moodCode and @moodCode=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.18']/voc:code/@value">SHALL contain exactly one [1..1] @moodCode, which SHALL be selected from ValueSet MoodCodeEvnInt 2.16.840.1.113883.11.20.9.18 STATIC 2011-04-03 (CONF:7497).</sch:assert>
      <sch:assert id="a-7500" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:7500).</sch:assert>
      <sch:assert id="a-7507" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:7507).</sch:assert>
      <sch:assert id="a-7508" test="count(cda:effectiveTime[count(cda:low)=1][count(cda:high)=1])=1">SHALL contain exactly one [1..1] effectiveTime (CONF:7508) such that it SHALL contain exactly one [1..1] low (CONF:7511). SHALL contain exactly one [1..1] high (CONF:7512).</sch:assert>
      <sch:assert id="a-7520" test="count(cda:consumable)=1">SHALL contain exactly one [1..1] consumable (CONF:7520).</sch:assert>
      <sch:assert id="a-16085" test="cda:consumable[count(cda:manufacturedProduct[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.23'])=1]">This consumable SHALL contain exactly one [1..1] Medication Information (templateId:2.16.840.1.113883.10.20.22.4.23) (CONF:16085).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.16-errors" context="cda:substanceAdministration[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.16']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.16-errors-abstract" />
      <sch:assert id="a-7499" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.16'])=1">SHALL contain exactly one [1..1] templateId (CONF:7499) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.16" (CONF:10504).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.5-errors">
    <!--Pattern is used in an implied relationship.-->
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.5-errors-abstract" abstract="true">
      <sch:assert id="a-7879" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7879).</sch:assert>
      <sch:assert id="a-7880" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7880).</sch:assert>
      <sch:assert id="a-15407" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15407).</sch:assert>
      <sch:assert id="a-15408" test="cda:code[@code='11450-4' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="11450-4" Problem List (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15408).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.5-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.5']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.5-errors-abstract" />
      <sch:assert id="a-7877" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.5'])=1">SHALL contain exactly one [1..1] templateId (CONF:7877) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.5" (CONF:10440).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.5.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.5.1-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.5-errors-abstract" />
      <sch:assert id="a-9181" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:9181).</sch:assert>
      <sch:assert id="a-9182" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:9182).</sch:assert>
      <sch:assert id="a-9183" test="count(cda:entry) &gt; 0">SHALL contain at least one [1..*] entry (CONF:9183).</sch:assert>
      <sch:assert id="a-15409" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15409).</sch:assert>
      <sch:assert id="a-15410" test="cda:code[@code='11450-4' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="11450-4" Problem List (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15410).</sch:assert>
      <sch:assert id="a-15506" test="cda:entry[count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.3'])=1]">Such entries SHALL contain exactly one [1..1] Problem Concern Act (Condition) (templateId:2.16.840.1.113883.10.20.22.4.3) (CONF:15506).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.5.1-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.5.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.5.1-errors-abstract" />
      <sch:assert id="a-9179" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.5.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:9179) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.5.1" (CONF:10441).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.1-errors-abstract" abstract="true">
      <sch:assert id="a-7121" test="@classCode">SHALL contain exactly one [1..1] @classCode (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7121).</sch:assert>
      <sch:assert id="a-7122" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7122).</sch:assert>
      <sch:assert id="a-7123" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:7123).</sch:assert>
      <sch:assert id="a-7124" test="count(cda:component[count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.2'])=1]) &gt; 0">SHALL contain at least one [1..*] component (CONF:7124) such that it SHALL contain exactly one [1..1] Result Observation (templateId:2.16.840.1.113883.10.20.22.4.2) (CONF:14850).</sch:assert>
      <sch:assert id="a-7127" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:7127).</sch:assert>
      <sch:assert id="a-7128" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:7128).</sch:assert>
      <sch:assert id="a-14848" test="cda:statusCode[@code and @code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.39']/voc:code/@value]">This statusCode SHALL contain exactly one [1..1] @code, which SHALL be selected from ValueSet Result Status 2.16.840.1.113883.11.20.9.39 STATIC (CONF:14848).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.1-errors" context="cda:organizer[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.1-errors-abstract" />
      <sch:assert id="a-7126" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:7126) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.1" (CONF:9134).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.2-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.2-errors-abstract" abstract="true">
      <sch:assert id="a-7130" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7130).</sch:assert>
      <sch:assert id="a-7131" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7131).</sch:assert>
      <sch:assert id="a-7133" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:7133).</sch:assert>
      <sch:assert id="a-7134" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:7134).</sch:assert>
      <sch:assert id="a-7137" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:7137).</sch:assert>
      <sch:assert id="a-7140" test="count(cda:effectiveTime)=1">SHALL contain exactly one [1..1] effectiveTime (CONF:7140).</sch:assert>
      <sch:assert id="a-7143" test="count(cda:value)=1">SHALL contain exactly one [1..1] value (CONF:7143).</sch:assert>
      <sch:assert id="a-14849" test="cda:statusCode[@code and @code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.39']/voc:code/@value]">This statusCode SHALL contain exactly one [1..1] @code, which SHALL be selected from ValueSet Result Status 2.16.840.1.113883.11.20.9.39 STATIC (CONF:14849).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.2-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.2']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.2-errors-abstract" />
      <sch:assert id="a-7136" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.2'])=1">SHALL contain exactly one [1..1] templateId (CONF:7136) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.2" (CONF:9138).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.5.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.1-errors-abstract" abstract="true">
      <sch:assert id="a-7157" test="count(cda:given[@xsi:type='ST']) &gt; 0">SHALL contain at least one [1..*] given (CONF:7157).</sch:assert>
      <sch:assert id="a-7159" test="count(cda:family[@xsi:type='ST'])=1">SHALL contain exactly one [1..1] family (CONF:7159).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.1-errors" context="cda:PN[cda:templateId/@root='2.16.840.1.113883.10.20.22.5.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.5.1-errors-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.26-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.26-errors-abstract" abstract="true">
      <sch:assert id="a-7279" test="@classCode='CLUSTER'">SHALL contain exactly one [1..1] @classCode="CLUSTER" CLUSTER (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7279).</sch:assert>
      <sch:assert id="a-7280" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7280).</sch:assert>
      <sch:assert id="a-7282" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:7282).</sch:assert>
      <sch:assert id="a-7284" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:7284).</sch:assert>
      <sch:assert id="a-7285" test="count(cda:component[count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.27'])=1]) &gt; 0">SHALL contain at least one [1..*] component (CONF:7285) such that it SHALL contain exactly one [1..1] Vital Sign Observation (templateId:2.16.840.1.113883.10.20.22.4.27) (CONF:15946).</sch:assert>
      <sch:assert id="a-7288" test="count(cda:effectiveTime)=1">The effectiveTime represents clinically effective time of the measurement, which is most likely when the measurement was performed (e.g., a BP measurement).
SHALL contain exactly one [1..1] effectiveTime (CONF:7288).</sch:assert>
      <sch:assert id="a-19120" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19120).</sch:assert>
      <sch:assert id="a-19176" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19176).</sch:assert>
      <sch:assert id="a-19177" test="cda:code[@code='46680005' and @codeSystem='2.16.840.1.113883.6.96']">This code SHALL contain exactly one [1..1] @code="46680005" Vital signs (CodeSystem: SNOMED-CT 2.16.840.1.113883.6.96 STATIC) (CONF:19177).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.26-errors" context="cda:organizer[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.26']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.26-errors-abstract" />
      <sch:assert id="a-7281" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.26'])=1">SHALL contain exactly one [1..1] templateId (CONF:7281) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.26" (CONF:10528).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.5.2-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.2-errors-abstract" abstract="true">
      <sch:assert id="a-7291-c" test="count(cda:streetAddressLine[@xsi:type='ST']) &gt; 0 and count(cda:streetAddressLine[@xsi:type='ST']) &lt; 5">SHALL contain at least one and not more than 4 streetAddressLine (CONF:7291).</sch:assert>
      <sch:assert id="a-7292" test="count(cda:city[@xsi:type='ST'])=1">SHALL contain exactly one [1..1] city (CONF:7292).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.2-errors" context="cda:AD[cda:templateId/@root='2.16.840.1.113883.10.20.22.5.2']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.5.2-errors-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.27-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.27-errors-abstract" abstract="true">
      <sch:assert id="a-7297" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7297).</sch:assert>
      <sch:assert id="a-7298" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7298).</sch:assert>
      <sch:assert id="a-7300" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:7300).</sch:assert>
      <sch:assert id="a-7301" test="count(cda:code)=1">SHALL contain exactly one [1..1] code, which SHOULD be selected from ValueSet Vital Sign Result Value Set 2.16.840.1.113883.3.88.12.80.62 DYNAMIC (CONF:7301).</sch:assert>
      <sch:assert id="a-7303" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:7303).</sch:assert>
      <sch:assert id="a-7304" test="count(cda:effectiveTime)=1">SHALL contain exactly one [1..1] effectiveTime (CONF:7304).</sch:assert>
      <sch:assert id="a-7305" test="count(cda:value[@xsi:type='PQ'])=1">SHALL contain exactly one [1..1] value with @xsi:type="PQ" (CONF:7305).</sch:assert>
      <sch:assert id="a-19119" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19119).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.27-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.27']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.27-errors-abstract" />
      <sch:assert id="a-7299" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.27'])=1">SHALL contain exactly one [1..1] templateId (CONF:7299) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.27" (CONF:10527).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.28-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.28-errors-abstract" abstract="true">
      <sch:assert id="a-7318" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7318).</sch:assert>
      <sch:assert id="a-7319" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7319).</sch:assert>
      <sch:assert id="a-7320" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:7320).</sch:assert>
      <sch:assert id="a-7321" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:7321).</sch:assert>
      <sch:assert id="a-7322" test="count(cda:value[@xsi:type='CE'])=1">SHALL contain exactly one [1..1] value with @xsi:type="CE", where the @code SHALL be selected from ValueSet Problem Status Value Set 2.16.840.1.113883.3.88.12.80.68 DYNAMIC (CONF:7322).</sch:assert>
      <sch:assert id="a-19087" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19087).</sch:assert>
      <sch:assert id="a-19131" test="cda:code[@code='33999-4' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="33999-4" Status (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:19131).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.28-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.28']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.28-errors-abstract" />
      <sch:assert id="a-7317" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.28'])=1">SHALL contain exactly one [1..1] templateId (CONF:7317) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.28" (CONF:10490).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.9-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.9-errors-abstract" abstract="true">
      <sch:assert id="a-7325" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7325).</sch:assert>
      <sch:assert id="a-7326" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7326).</sch:assert>
      <sch:assert id="a-7328" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:7328).</sch:assert>
      <sch:assert id="a-7329" test="count(cda:id)=1">SHALL contain exactly one [1..1] id (CONF:7329).</sch:assert>
      <sch:assert id="a-7335" test="count(cda:value[@xsi:type='CD'])=1">SHALL contain exactly one [1..1] value with @xsi:type="CD", where the @code SHALL be selected from ValueSet Problem Value Set 2.16.840.1.113883.3.88.12.3221.7.4 DYNAMIC (CONF:7335).</sch:assert>
      <sch:assert id="a-16851" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:16851).</sch:assert>
      <sch:assert id="a-19114" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19114).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.9-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.9']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.9-errors-abstract" />
      <sch:assert id="a-7323" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.9'])=1">SHALL contain exactly one [1..1] templateId (CONF:7323) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.9" (CONF:10523).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.8-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.8-errors-abstract" abstract="true">
      <sch:assert id="a-7345" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7345).</sch:assert>
      <sch:assert id="a-7346" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7346).</sch:assert>
      <sch:assert id="a-7352" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:7352).</sch:assert>
      <sch:assert id="a-7356" test="count(cda:value[@xsi:type='CD'])=1">SHALL contain exactly one [1..1] value with @xsi:type="CD", where the @code SHALL be selected from ValueSet Problem Severity 2.16.840.1.113883.3.88.12.3221.6.8 DYNAMIC (CONF:7356).</sch:assert>
      <sch:assert id="a-19115" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19115).</sch:assert>
      <sch:assert id="a-19168" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19168).</sch:assert>
      <sch:assert id="a-19169" test="cda:code[@code='SEV' and @codeSystem='2.16.840.1.113883.5.4']">This code SHALL contain exactly one [1..1] @code="SEV" (CodeSystem: ActCode 2.16.840.1.113883.5.4 STATIC) (CONF:19169).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.8-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.8']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.8-errors-abstract" />
      <sch:assert id="a-7347" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.8'])=1">SHALL contain exactly one [1..1] templateId (CONF:7347) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.8" (CONF:10525).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.6-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.6-errors-abstract" abstract="true">
      <sch:assert id="a-7357" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7357).</sch:assert>
      <sch:assert id="a-7358" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7358).</sch:assert>
      <sch:assert id="a-7364" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:7364).</sch:assert>
      <sch:assert id="a-7365" test="count(cda:value[@xsi:type='CD'])=1">SHALL contain exactly one [1..1] value with @xsi:type="CD", where the @code SHALL be selected from ValueSet Problem Status Value Set 2.16.840.1.113883.3.88.12.80.68 DYNAMIC (CONF:7365).</sch:assert>
      <sch:assert id="a-19113" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19113).</sch:assert>
      <sch:assert id="a-19162" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19162).</sch:assert>
      <sch:assert id="a-19163" test="cda:code[@code='33999-4' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="33999-4" Status (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:19163).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.6-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.6']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.6-errors-abstract" />
      <sch:assert id="a-7359" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.6'])=1">SHALL contain exactly one [1..1] templateId (CONF:7359) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.6" (CONF:10518).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.25-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.25-errors-abstract" abstract="true" />
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.25-errors" context="cda:criterion[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.25']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.25-errors-abstract" />
      <sch:assert id="a-7372" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.25'])=1">SHALL contain exactly one [1..1] templateId (CONF:7372) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.25" (CONF:10517).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.24.3.90-errors">
    <!--Pattern is used in an implied relationship.-->
    <sch:rule id="r-2.16.840.1.113883.10.20.24.3.90-errors-abstract" abstract="true">
      <sch:assert id="a-16303" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:16303).</sch:assert>
      <sch:assert id="a-16304" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:16304).</sch:assert>
      <sch:assert id="a-16307" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:16307).</sch:assert>
      <sch:assert id="a-16308" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:16308).</sch:assert>
      <sch:assert id="a-16309" test="count(cda:effectiveTime)=1">SHALL contain exactly one [1..1] effectiveTime (CONF:16309).</sch:assert>
      <sch:assert id="a-16312" test="count(cda:value[@xsi:type='CD'])=1">SHALL contain exactly one [1..1] value with @xsi:type="CD" (CONF:16312).</sch:assert>
      <sch:assert id="a-16317-c" test="cda:value[@code]">This value SHALL contain exactly one [1..1] @code, which SHALL be selected from ValueSet Allergy/Adverse Event Type Value Set 2.16.840.1.113883.3.88.12.3221.6.2 DYNAMIC (CONF:16317).</sch:assert>
      <sch:assert id="a-16341" test="count(cda:entryRelationship[@typeCode='SUBJ'][@inversionInd='true'][count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.8'])=1])=1">SHALL contain exactly one [1..1] entryRelationship (CONF:16341) such that it SHALL contain exactly one [1..1] @typeCode="SUBJ" Has Subject (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:16342). SHALL contain exactly one [1..1] @inversionInd="true" True (CONF:16343). SHALL contain exactly one [1..1] Severity Observation (templateId:2.16.840.1.113883.10.20.22.4.8) (CONF:16344).</sch:assert>
      <sch:assert id="a-16345" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:16345).</sch:assert>
      <sch:assert id="a-16346" test="cda:code[@code='ASSERTION' and @codeSystem='2.16.840.1.113883.5.4']">This code SHALL contain exactly one [1..1] @code="ASSERTION" Assertion (CodeSystem: ActCode 2.16.840.1.113883.5.4 STATIC) (CONF:16346).</sch:assert>
      <sch:assert id="a-26354" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14) (CONF:26354).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.24.3.90-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.24.3.90']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.24.3.90-errors-abstract" />
      <sch:assert id="a-16305" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.24.3.90'])=1">SHALL contain exactly one [1..1] templateId (CONF:16305) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.24.3.90" (CONF:16306).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.7-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.7-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.24.3.90-errors-abstract" />
      <sch:assert id="a-7379" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7379).</sch:assert>
      <sch:assert id="a-7380" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7380).</sch:assert>
      <sch:assert id="a-7382" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:7382).</sch:assert>
      <sch:assert id="a-7387" test="count(cda:effectiveTime)=1">SHALL contain exactly one [1..1] effectiveTime (CONF:7387).</sch:assert>
      <sch:assert id="a-7390" test="count(cda:value[@xsi:type='CD'])=1">SHALL contain exactly one [1..1] value with @xsi:type="CD" (CONF:7390).</sch:assert>
      <sch:assert id="a-9139" test="cda:value[@xsi:type='CD'][@code]">This value SHALL contain exactly one [1..1] @code, which SHALL be selected from ValueSet Allergy/Adverse Event Type Value Set 2.16.840.1.113883.3.88.12.3221.6.2 DYNAMIC (CONF:9139).</sch:assert>
      <sch:assert id="a-15947" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15947).</sch:assert>
      <sch:assert id="a-15948" test="cda:code[@code='ASSERTION' and @codeSystem='2.16.840.1.113883.5.4']">This code SHALL contain exactly one [1..1] @code="ASSERTION" Assertion (CodeSystem: ActCode 2.16.840.1.113883.5.4 STATIC) (CONF:15948).</sch:assert>
      <sch:assert id="a-19084-c" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:19084).</sch:assert>
      <sch:assert id="a-19085-c" test="cda:statusCode/@code='completed'">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19085).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.7-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.7']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.7-errors-abstract" />
      <sch:assert id="a-7381" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.7'])=1">SHALL contain exactly one [1..1] templateId (CONF:7381) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.7" (CONF:10488).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.20-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.20-errors-abstract" abstract="true">
      <sch:assert id="a-7391" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7391).</sch:assert>
      <sch:assert id="a-7392" test="@moodCode='INT'">SHALL contain exactly one [1..1] @moodCode="INT" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7392).</sch:assert>
      <sch:assert id="a-7396" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:7396).</sch:assert>
      <sch:assert id="a-16884" test="count(cda:code)=1">SHALL contain exactly one [1..1] code, which SHOULD be selected from ValueSet Patient Education 2.16.840.1.113883.11.20.9.34 DYNAMIC (CONF:16884).</sch:assert>
      <sch:assert id="a-19106" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19106).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.20-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.20']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.20-errors-abstract" />
      <sch:assert id="a-7393" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.20'])=1">SHALL contain exactly one [1..1] templateId (CONF:7393) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.20" (CONF:10503).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.23-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.23-errors-abstract" abstract="true">
      <sch:assert id="a-7408" test="@classCode='MANU'">SHALL contain exactly one [1..1] @classCode="MANU" (CodeSystem: RoleClass 2.16.840.1.113883.5.110 STATIC) (CONF:7408).</sch:assert>
      <sch:assert id="a-7411" test="count(cda:manufacturedMaterial)=1">SHALL contain exactly one [1..1] manufacturedMaterial (CONF:7411).</sch:assert>
      <sch:assert id="a-7412" test="cda:manufacturedMaterial[count(cda:code)=1]">This manufacturedMaterial SHALL contain exactly one [1..1] code, which SHALL be selected from ValueSet Medication Clinical Drug Name Value Set 2.16.840.1.113883.3.88.12.80.17 DYNAMIC (CONF:7412).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.23-errors" context="cda:manufacturedProduct[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.23']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.23-errors-abstract" />
      <sch:assert id="a-7409" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.23'])=1">SHALL contain exactly one [1..1] templateId (CONF:7409) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.23" (CONF:10506).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.17-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.17-errors-abstract" abstract="true">
      <sch:assert id="a-7427" test="@classCode='SPLY'">SHALL contain exactly one [1..1] @classCode="SPLY" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7427).</sch:assert>
      <sch:assert id="a-7428" test="@moodCode='INT'">SHALL contain exactly one [1..1] @moodCode="INT" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7428).</sch:assert>
      <sch:assert id="a-7430" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:7430).</sch:assert>
      <sch:assert id="a-7432" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:7432).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.17-errors" context="cda:supply[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.17']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.17-errors-abstract" />
      <sch:assert id="a-7429" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.17'])=1">SHALL contain exactly one [1..1] templateId (CONF:7429) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.17" (CONF:10507).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.18-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.18-errors-abstract" abstract="true">
      <sch:assert id="a-7451" test="@classCode">SHALL contain exactly one [1..1] @classCode (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7451).</sch:assert>
      <sch:assert id="a-7452" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7452).</sch:assert>
      <sch:assert id="a-7454" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:7454).</sch:assert>
      <sch:assert id="a-7455" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode, which SHALL be selected from ValueSet Medication Fill Status 2.16.840.1.113883.3.88.12.80.64 DYNAMIC (CONF:7455).</sch:assert>
      <sch:assert id="a-9333-c" test="cda:product/cda:manufacturedProduct[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.23' or cda:templateId/@root='2.16.840.1.113883.10.20.22.4.54']">A supply act SHALL contain one product/Medication Information or one product/Immunization Medication Information template (CONF:9333).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.18-errors" context="cda:supply[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.18']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.18-errors-abstract" />
      <sch:assert id="a-7453" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.18'])=1">SHALL contain exactly one [1..1] templateId (CONF:7453) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.18" (CONF:10505).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.30-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.30-errors-abstract" abstract="true">
      <sch:assert id="a-7469" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7469).</sch:assert>
      <sch:assert id="a-7470" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7470).</sch:assert>
      <sch:assert id="a-7472" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:7472).</sch:assert>
      <sch:assert id="a-7477" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:7477).</sch:assert>
      <sch:assert id="a-7485" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:7485).</sch:assert>
      <sch:assert id="a-7498" test="count(cda:effectiveTime)=1">SHALL contain exactly one [1..1] effectiveTime (CONF:7498).</sch:assert>
      <sch:assert id="a-7504-c" test="((../cda:statusCode[@code='active']) and (cda:low)) or not(../cda:statusCode[@code='active'])">If statusCode/@code="active" Active, then effectiveTime SHALL contain [1..1] low (CONF:7504).</sch:assert>
      <sch:assert id="a-7509" test="count(cda:entryRelationship[@typeCode='SUBJ'][count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.7'])=1]) &gt; 0">SHALL contain at least one [1..*] entryRelationship (CONF:7509) such that it SHALL contain exactly one [1..1] @typeCode="SUBJ" Has subject (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:7915). SHALL contain exactly one [1..1] Allergy - Intolerance Observation (templateId:2.16.840.1.113883.10.20.22.4.7) (CONF:14925).</sch:assert>
      <sch:assert id="a-10085-c" test="count(cda:statusCode[@code='completed'])=0 or (count(cda:statusCode[@code='completed'])=1 and count(cda:effective[cda:high])=1)">If statusCode/@code="completed" Completed, then effectiveTime SHALL contain [1..1] high (CONF:10085).</sch:assert>
      <sch:assert id="a-19086" test="cda:statusCode[@code and @code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.19']/voc:code/@value]">This statusCode SHALL contain exactly one [1..1] @code, which SHALL be selected from ValueSet ProblemAct statusCode 2.16.840.1.113883.11.20.9.19 STATIC 2011-09-09 (CONF:19086).</sch:assert>
      <sch:assert id="a-19158" test="cda:code[@code='48765-2' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="48765-2" Allergies, adverse reactions, alerts (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:19158).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.30-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.30']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.30-errors-abstract" />
      <sch:assert id="a-7471" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.30'])=1">SHALL contain exactly one [1..1] templateId (CONF:7471) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.30" (CONF:10489).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.19-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.19-errors-abstract" abstract="true">
      <sch:assert id="a-7480" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7480).</sch:assert>
      <sch:assert id="a-7481" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7481).</sch:assert>
      <sch:assert id="a-7483" test="count(cda:id)=1">SHALL contain exactly one [1..1] id (CONF:7483).</sch:assert>
      <sch:assert id="a-7487" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:7487).</sch:assert>
      <sch:assert id="a-16886" test="count(cda:code[@code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.3.88.12.3221.7.2']/voc:code/@value])=1">SHALL contain exactly one [1..1] code, which SHOULD be selected from ValueSet Problem Type 2.16.840.1.113883.3.88.12.3221.7.2 STATIC 2012-06-01 (CONF:16886).</sch:assert>
      <sch:assert id="a-19105" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19105).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.19-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.19']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.19-errors-abstract" />
      <sch:assert id="a-7482" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.19'])=1">SHALL contain exactly one [1..1] templateId (CONF:7482) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.19" (CONF:10502).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.24-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.24-errors-abstract" abstract="true">
      <sch:assert id="a-7490" test="@classCode='MANU'">SHALL contain exactly one [1..1] @classCode="MANU" (CodeSystem: RoleClass 2.16.840.1.113883.5.110 STATIC) (CONF:7490).</sch:assert>
      <sch:assert id="a-7492" test="count(cda:playingEntity)=1">SHALL contain exactly one [1..1] playingEntity (CONF:7492).</sch:assert>
      <sch:assert id="a-7493" test="cda:playingEntity[count(cda:code)=1]">This playingEntity/code is used to supply a coded term for the drug vehicle.
This playingEntity SHALL contain exactly one [1..1] code (CONF:7493).</sch:assert>
      <sch:assert id="a-19137" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19137).</sch:assert>
      <sch:assert id="a-19138" test="cda:code[@code='412307009' and @codeSystem='2.16.840.1.113883.6.96']">This code SHALL contain exactly one [1..1] @code="412307009" Drug Vehicle (CodeSystem: SNOMED-CT 2.16.840.1.113883.6.96 STATIC) (CONF:19138).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.24-errors" context="cda:participantRole[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.24']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.24-errors-abstract" />
      <sch:assert id="a-7495" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.24'])=1">SHALL contain exactly one [1..1] templateId (CONF:7495) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.24" (CONF:10493).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.14-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.14-errors-abstract" abstract="true">
      <sch:assert id="a-7652" test="@classCode='PROC'">SHALL contain exactly one [1..1] @classCode="PROC" Procedure (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7652).</sch:assert>
      <sch:assert id="a-7653" test="@moodCode and @moodCode=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.18']/voc:code/@value">SHALL contain exactly one [1..1] @moodCode, which SHALL be selected from ValueSet MoodCodeEvnInt 2.16.840.1.113883.11.20.9.18 STATIC 2011-04-03 (CONF:7653).</sch:assert>
      <sch:assert id="a-7655" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:7655).</sch:assert>
      <sch:assert id="a-7656" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:7656).</sch:assert>
      <sch:assert id="a-7661" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode, which SHALL be selected from ValueSet ProcedureAct statusCode 2.16.840.1.113883.11.20.9.22 DYNAMIC (CONF:7661).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.14-errors" context="cda:procedure[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.14']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.14-errors-abstract" />
      <sch:assert id="a-7654" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.14'])=1">SHALL contain exactly one [1..1] templateId (CONF:7654) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.14" (CONF:10521).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.29-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.29-errors-abstract" abstract="true">
      <sch:assert id="a-7557" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7557).</sch:assert>
      <sch:assert id="a-7558" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7558).</sch:assert>
      <sch:assert id="a-7560" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:7560).</sch:assert>
      <sch:assert id="a-7562" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:7562).</sch:assert>
      <sch:assert id="a-7564" test="count(cda:value[translate(text(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='182904002'][@codeSystem='2.16.840.1.113883.6.96'])=1">SHALL contain exactly one [1..1] value="182904002" Drug treatment unknown (CodeSystem: SNOMED-CT 2.16.840.1.113883.6.96 STATIC) (CONF:7564).</sch:assert>
      <sch:assert id="a-19107" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19107).</sch:assert>
      <sch:assert id="a-19149" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19149).</sch:assert>
      <sch:assert id="a-19150" test="cda:code[@code='ASSERTION' and @codeSystem='2.16.840.1.113883.5.4']">This code SHALL contain exactly one [1..1] @code="ASSERTION" Assertion (CodeSystem: ActCode 2.16.840.1.113883.5.4 STATIC) (CONF:19150).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.29-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.29']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.29-errors-abstract" />
      <sch:assert id="a-7559" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.29'])=1">SHALL contain exactly one [1..1] templateId (CONF:7559) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.29" (CONF:10508).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.1-errors">
    <!--Pattern is used in an implied relationship.-->
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.1-errors-abstract" abstract="true">
      <sch:assert id="a-7793-c" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7793).</sch:assert>
      <sch:assert id="a-7794" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7794).</sch:assert>
      <sch:assert id="a-15385" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15385).</sch:assert>
      <sch:assert id="a-15386" test="cda:code[@code='10160-0' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="10160-0" History of medication use (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15386).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.1-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.1-errors-abstract" />
      <sch:assert id="a-7791" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:7791) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.1" (CONF:10432).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.1.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.1.1-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.1-errors-abstract" />
      <sch:assert id="a-7570" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7570).</sch:assert>
      <sch:assert id="a-7571" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7571).</sch:assert>
      <sch:assert id="a-7572" test="count(cda:entry[count(cda:substanceAdministration[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.16'])=1]) &gt; 0">SHALL contain at least one [1..*] entry (CONF:7572) such that it SHALL contain exactly one [1..1] Medication Activity (templateId:2.16.840.1.113883.10.20.22.4.16) (CONF:15500).</sch:assert>
      <sch:assert id="a-15387" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15387).</sch:assert>
      <sch:assert id="a-15388" test="cda:code[@code='10160-0' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="10160-0" History of medication use (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15388).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.1.1-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.1.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.1.1-errors-abstract" />
      <sch:assert id="a-7568" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.1.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:7568) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.1.1" (CONF:10433).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.9-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.9-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-errors-abstract" />
      <sch:assert id="a-7589" test="cda:code[@code]">SHALL contain exactly one [1..1] code/@code, which SHALL be selected from ValueSet ProgressNoteDocumentTypeCode 2.16.840.1.113883.11.20.8.1 DYNAMIC (CONF:7589).</sch:assert>
      <sch:assert id="a-7595" test="count(cda:componentOf)=1">SHALL contain exactly one [1..1] componentOf (CONF:7595).</sch:assert>
      <sch:assert id="a-7596" test="cda:componentOf[count(cda:encompassingEncounter)=1]">This componentOf SHALL contain exactly one [1..1] encompassingEncounter (CONF:7596).</sch:assert>
      <sch:assert id="a-7597" test="cda:componentOf/cda:encompassingEncounter[count(cda:id) &gt; 0]">This encompassingEncounter SHALL contain at least one [1..*] id (CONF:7597).</sch:assert>
      <sch:assert id="a-7598" test="cda:componentOf/cda:encompassingEncounter[count(cda:effectiveTime)=1]">This encompassingEncounter SHALL contain exactly one [1..1] effectiveTime (CONF:7598).</sch:assert>
      <sch:assert id="a-7599" test="cda:componentOf/cda:encompassingEncounter/cda:effectiveTime[count(cda:low)=1]">This effectiveTime SHALL contain exactly one [1..1] low (CONF:7599).</sch:assert>
      <sch:assert id="a-7611" test="cda:componentOf/cda:encompassingEncounter[count(cda:id)=1]">This encompassingEncounter SHALL contain exactly one [1..1] location/healthCareFacility/id (CONF:7611).</sch:assert>
      <sch:assert id="a-8704-c" test="count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=2 or (count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.9'])=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=0)">SHALL include an Assessment and Plan Section, or an Assessment Section and a Plan Section (CONF:8704).</sch:assert>
      <sch:assert id="a-10069-c" test="count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=2 or (count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.9'])=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=0)">SHALL NOT include an Assessment/Plan Section when an Assessment Section and a Plan of Care Section are present (CONF:10069).</sch:assert>
      <sch:assert id="a-17189" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:17189).</sch:assert>
      <sch:assert id="a-17190" test="cda:code[@code]">This code SHALL contain exactly one [1..1] @code, which SHALL be selected from ValueSet ProgressNoteDocumentTypeCode 2.16.840.1.113883.11.20.8.1 DYNAMIC (CONF:17190).</sch:assert>
      <sch:assert id="a-10138-c" test="string-length(cda:componentOf/cda:encompassingEncounter/cda:effectiveTime//@value)&gt;=8">The content of effectiveTime SHALL be a conformant US Realm Date and Time (DT.US.FIELDED) (2.16.840.1.113883.10.20.22.5.4) (CONF:10138).</sch:assert>
      <sch:assert id="a-9594-c" test="not(testable)">If structuredBody, the component/structuredBody SHALL conform to the section constraints below (CONF:9594).</sch:assert>
      <sch:assert id="a-9592-c" test="count(//cda:structuredBody | //cda:nonXMLBody)=1">A Progress Note can have either a structuredBody or a nonXMLBody (CONF:9592).</sch:assert>
      <sch:assert id="a-9591" test="count(cda:component)=1">SHALL contain exactly one [1..1] component (CONF:9591).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.9-errors" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.9']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.9-errors-abstract" />
      <sch:assert id="a-7588" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.1.9'])=1">SHALL contain exactly one [1..1] templateId (CONF:7588) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.1.9" (CONF:10052).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.31-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.31-errors-abstract" abstract="true">
      <sch:assert id="a-7613" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7613).</sch:assert>
      <sch:assert id="a-7614" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7614).</sch:assert>
      <sch:assert id="a-7615" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:7615).</sch:assert>
      <sch:assert id="a-7617" test="count(cda:value[@xsi:type='PQ'])=1">SHALL contain exactly one [1..1] value with @xsi:type="PQ" (CONF:7617).</sch:assert>
      <sch:assert id="a-7618" test="cda:value[@xsi:type='PQ'][@unit]">This value SHALL contain exactly one [1..1] @unit, which SHALL be selected from ValueSet AgePQ_UCUM 2.16.840.1.113883.11.20.9.21 DYNAMIC (CONF:7618).</sch:assert>
      <sch:assert id="a-15965" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:15965).</sch:assert>
      <sch:assert id="a-15966" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:15966).</sch:assert>
      <sch:assert id="a-16776" test="cda:code[@code='445518008' and @codeSystem='2.16.840.1.113883.6.96']">This code SHALL contain exactly one [1..1] @code="445518008" Age At Onset (CodeSystem: SNOMED-CT 2.16.840.1.113883.6.96 STATIC) (CONF:16776).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.31-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.31']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.31-errors-abstract" />
      <sch:assert id="a-7899" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.31'])=1">SHALL contain exactly one [1..1] templateId (CONF:7899) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.31" (CONF:10487).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.10-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.10-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-errors-abstract" />
      <sch:assert id="a-7620" test="count(cda:component[cda:nonXMLBody])=1">SHALL contain exactly one [1..1] component/nonXMLBody (CONF:7620).</sch:assert>
      <sch:assert id="a-7622" test="cda:component/cda:nonXMLBody[count(cda:text)=1]">This component/nonXMLBody SHALL contain exactly one [1..1] text (CONF:7622).</sch:assert>
      <sch:assert id="a-7640" test="count(cda:author[cda:assignedAuthor])=1">SHALL contain exactly one [1..1] author/assignedAuthor (CONF:7640).</sch:assert>
      <sch:assert id="a-7641" test="cda:author/cda:assignedAuthor[count(cda:addr)=1]">This author/assignedAuthor SHALL contain exactly one [1..1] addr (CONF:7641).</sch:assert>
      <sch:assert id="a-7642" test="cda:author/cda:assignedAuthor[count(cda:telecom)=1]">This author/assignedAuthor SHALL contain exactly one [1..1] telecom (CONF:7642).</sch:assert>
      <sch:assert id="a-7643" test="count(cda:recordTarget/cda:patientRole[cda:id])=1">SHALL contain exactly one [1..1] recordTarget/patientRole/id (CONF:7643).</sch:assert>
      <sch:assert id="a-7645" test="count(cda:custodian/cda:assignedCustodian[cda:representedCustodianOrganization])=1">SHALL contain exactly one [1..1] custodian/assignedCustodian/representedCustodianOrganization (CONF:7645).</sch:assert>
      <sch:assert id="a-7648" test="cda:custodian/cda:assignedCustodian/cda:representedCustodianOrganization[count(cda:id)=1]">This custodian/assignedCustodian/representedCustodianOrganization SHALL contain exactly one [1..1] id (CONF:7648).</sch:assert>
      <sch:assert id="a-7649" test="cda:custodian/cda:assignedCustodian/cda:representedCustodianOrganization[count(cda:name)=1]">This custodian/assignedCustodian/representedCustodianOrganization SHALL contain exactly one [1..1] name (CONF:7649).</sch:assert>
      <sch:assert id="a-7650" test="cda:custodian/cda:assignedCustodian/cda:representedCustodianOrganization[count(cda:telecom)=1]">This custodian/assignedCustodian/representedCustodianOrganization SHALL contain exactly one [1..1] telecom (CONF:7650).</sch:assert>
      <sch:assert id="a-7651" test="cda:custodian/cda:assignedCustodian/cda:representedCustodianOrganization[count(cda:addr)=1]">This custodian/assignedCustodian/representedCustodianOrganization SHALL contain exactly one [1..1] addr (CONF:7651).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.10-errors" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.10']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.10-errors-abstract" />
      <sch:assert id="a-7710" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.1.10'])=1">SHALL contain exactly one [1..1] templateId (CONF:7710) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.1.10" (CONF:10054).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.32-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.32-errors-abstract" abstract="true">
      <sch:assert id="a-7758" test="@classCode='SDLOC'">SHALL contain exactly one [1..1] @classCode="SDLOC" (CodeSystem: RoleCode 2.16.840.1.113883.5.111 STATIC) (CONF:7758).</sch:assert>
      <sch:assert id="a-16850" test="count(cda:code[@code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.1.11.20275']/voc:code/@value])=1">SHALL contain exactly one [1..1] code, which SHALL be selected from ValueSet HealthcareServiceLocation 2.16.840.1.113883.1.11.20275 STATIC (CONF:16850).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.32-errors" context="cda:participantRole[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.32']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.32-errors-abstract" />
      <sch:assert id="a-7635" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.32'])=1">SHALL contain exactly one [1..1] templateId (CONF:7635) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.32" (CONF:10524).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.3-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.3-errors-abstract" abstract="true">
      <sch:assert id="a-9024" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" Act (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:9024).</sch:assert>
      <sch:assert id="a-9025" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:9025).</sch:assert>
      <sch:assert id="a-9026" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:9026).</sch:assert>
      <sch:assert id="a-9027" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:9027).</sch:assert>
      <sch:assert id="a-9029" test="count(cda:statusCode[@code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.19']/voc:code/@value])=1">SHALL contain exactly one [1..1] statusCode, which SHALL be selected from ValueSet ProblemAct statusCode 2.16.840.1.113883.11.20.9.19 STATIC 2011-09-09 (CONF:9029).</sch:assert>
      <sch:assert id="a-9030" test="count(cda:effectiveTime)=1">The effectiveTime element records the starting and ending times during which the concern was active on the Problem List.
SHALL contain exactly one [1..1] effectiveTime (CONF:9030).</sch:assert>
      <sch:assert id="a-9032" test="cda:effectiveTime[count(cda:low)=1]">This effectiveTime SHALL contain exactly one [1..1] low (CONF:9032).</sch:assert>
      <sch:assert id="a-9034" test="count(cda:entryRelationship[@typeCode='SUBJ'][count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.4'])=1]) &gt; 0">SHALL contain at least one [1..*] entryRelationship (CONF:9034) such that it SHALL contain exactly one [1..1] @typeCode="SUBJ" Has subject (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:9035). SHALL contain exactly one [1..1] Problem Observation (templateId:2.16.840.1.113883.10.20.22.4.4) (CONF:15980).</sch:assert>
      <sch:assert id="a-19184" test="cda:code[@code='CONC' and @codeSystem='2.16.840.1.113883.5.6']">This code SHALL contain exactly one [1..1] @code="CONC" Concern (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:19184).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.3-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.3']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.3-errors-abstract" />
      <sch:assert id="a-16772" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.3'])=1">SHALL contain exactly one [1..1] templateId (CONF:16772) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.3" (CONF:16773).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.4-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.4-errors-abstract" abstract="true">
      <sch:assert id="a-9041" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:9041).</sch:assert>
      <sch:assert id="a-9042" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:9042).</sch:assert>
      <sch:assert id="a-9043" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:9043).</sch:assert>
      <sch:assert id="a-9045" test="count(cda:code[@code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.3.88.12.3221.7.2']/voc:code/@value])=1">SHALL contain exactly one [1..1] code, which SHOULD be selected from ValueSet Problem Type 2.16.840.1.113883.3.88.12.3221.7.2 STATIC 2012-06-01 (CONF:9045).</sch:assert>
      <sch:assert id="a-9049" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:9049).</sch:assert>
      <sch:assert id="a-9058-c" test="count(cda:value[@xsi:type='CD'])=1">SHALL contain exactly one [1..1] value with @xsi:type="CD", where the @code SHOULD be selected from ValueSet Problem Value Set 2.16.840.1.113883.3.88.12.3221.7.4 DYNAMIC (CONF:9058).</sch:assert>
      <sch:assert id="a-19112" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19112).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.4-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.4']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.4-errors-abstract" />
      <sch:assert id="a-14926" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.4'])=1">SHALL contain exactly one [1..1] templateId (CONF:14926) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.4" (CONF:14927).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.33-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.33-errors-abstract" abstract="true">
      <sch:assert id="a-7663" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7663).</sch:assert>
      <sch:assert id="a-7664" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7664).</sch:assert>
      <sch:assert id="a-7666" test="count(cda:entryRelationship[@typeCode='SUBJ'][count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.4'])=1]) &gt; 0">SHALL contain at least one [1..*] entryRelationship (CONF:7666) such that it SHALL contain exactly one [1..1] @typeCode="SUBJ" Has Subject (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:7667). SHALL contain exactly one [1..1] Problem Observation (templateId:2.16.840.1.113883.10.20.22.4.4) (CONF:15536).</sch:assert>
      <sch:assert id="a-19147" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19147).</sch:assert>
      <sch:assert id="a-19148" test="cda:code[@code='11535-2' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="11535-2" Hospital discharge diagnosis (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:19148).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.33-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.33']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.33-errors-abstract" />
      <sch:assert id="a-16764" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.33'])=1">SHALL contain exactly one [1..1] templateId (CONF:16764) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.33" (CONF:16765).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.34-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.34-errors-abstract" abstract="true">
      <sch:assert id="a-7671" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7671).</sch:assert>
      <sch:assert id="a-7672" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7672).</sch:assert>
      <sch:assert id="a-7674" test="count(cda:entryRelationship[@typeCode='SUBJ'][count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.4'])=1]) &gt; 0">SHALL contain at least one [1..*] entryRelationship (CONF:7674) such that it SHALL contain exactly one [1..1] @typeCode="SUBJ" Has Subject (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:7675). SHALL contain exactly one [1..1] Problem Observation (templateId:2.16.840.1.113883.10.20.22.4.4) (CONF:15535).</sch:assert>
      <sch:assert id="a-19145" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19145).</sch:assert>
      <sch:assert id="a-19146" test="cda:code[@code='46241-6' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="46241-6" Admission diagnosis (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:19146).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.34-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.34']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.34-errors-abstract" />
      <sch:assert id="a-16747" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.34'])=1">SHALL contain exactly one [1..1] templateId (CONF:16747) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.34" (CONF:16748).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.35-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.35-errors-abstract" abstract="true">
      <sch:assert id="a-7689" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7689).</sch:assert>
      <sch:assert id="a-7690" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7690).</sch:assert>
      <sch:assert id="a-7691" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:7691).</sch:assert>
      <sch:assert id="a-7692" test="count(cda:entryRelationship[@typeCode='SUBJ'][count(cda:substanceAdministration[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.16'])=1]) &gt; 0">SHALL contain at least one [1..*] entryRelationship (CONF:7692) such that it SHALL contain exactly one [1..1] @typeCode="SUBJ" Has Subject (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:7693). SHALL contain exactly one [1..1] Medication Activity (templateId:2.16.840.1.113883.10.20.22.4.16) (CONF:15525).</sch:assert>
      <sch:assert id="a-19161" test="cda:code[@code='10183-2' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="10183-2" Discharge medication (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:19161).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.35-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.35']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.35-errors-abstract" />
      <sch:assert id="a-16760" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.35'])=1">SHALL contain exactly one [1..1] templateId (CONF:16760) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.35" (CONF:16761).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.36-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.36-errors-abstract" abstract="true">
      <sch:assert id="a-7698" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:7698).</sch:assert>
      <sch:assert id="a-7699" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:7699).</sch:assert>
      <sch:assert id="a-7701" test="count(cda:entryRelationship[@typeCode='SUBJ'][count(cda:substanceAdministration[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.16'])=1]) &gt; 0">SHALL contain at least one [1..*] entryRelationship (CONF:7701) such that it SHALL contain exactly one [1..1] @typeCode="SUBJ" (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:7702). SHALL contain exactly one [1..1] Medication Activity (templateId:2.16.840.1.113883.10.20.22.4.16) (CONF:15520).</sch:assert>
      <sch:assert id="a-15518" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15518).</sch:assert>
      <sch:assert id="a-15519" test="cda:code[@code='42346-7' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="42346-7" Medications on Admission (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15519).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.36-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.36']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.36-errors-abstract" />
      <sch:assert id="a-16758" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.36'])=1">SHALL contain exactly one [1..1] templateId (CONF:16758) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.36" (CONF:16759).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.8-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.8-errors-abstract" abstract="true">
      <sch:assert id="a-7713" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7713).</sch:assert>
      <sch:assert id="a-14757" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:14757).</sch:assert>
      <sch:assert id="a-14758" test="cda:code[@code='51848-0' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="51848-0" Assessments (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:14758).</sch:assert>
      <sch:assert id="a-16774" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:16774).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.8-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.8']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.8-errors-abstract" />
      <sch:assert id="a-7711" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'])=1">SHALL contain exactly one [1..1] templateId (CONF:7711) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.8" (CONF:10382).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.9-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.9-errors-abstract" abstract="true">
      <sch:assert id="a-7707" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7707).</sch:assert>
      <sch:assert id="a-15353" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15353).</sch:assert>
      <sch:assert id="a-15354" test="cda:code[@code='51847-2']">This code SHALL contain exactly one [1..1] @code="51847-2" Assessment and Plan (CONF:15354).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.9-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.9']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.9-errors-abstract" />
      <sch:assert id="a-7705" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.9'])=1">SHALL contain exactly one [1..1] templateId (CONF:7705) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.9" (CONF:10381).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.10-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.10-errors-abstract" abstract="true">
      <sch:assert id="a-7725" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7725).</sch:assert>
      <sch:assert id="a-14749" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:14749).</sch:assert>
      <sch:assert id="a-14750" test="cda:code[@code='18776-5' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="18776-5" Plan of Care (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:14750).</sch:assert>
      <sch:assert id="a-16986" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:16986).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.10-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.10']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.10-errors-abstract" />
      <sch:assert id="a-7723" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=1">SHALL contain exactly one [1..1] templateId (CONF:7723) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.10" (CONF:10435).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.37-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.37-errors-abstract" abstract="true">
      <sch:assert id="a-7900" test="@classCode='MANU'">SHALL contain exactly one [1..1] @classCode="MANU" Manufactured Product (CodeSystem: RoleClass 2.16.840.1.113883.5.110 STATIC) (CONF:7900).</sch:assert>
      <sch:assert id="a-7902" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:7902).</sch:assert>
      <sch:assert id="a-7903" test="count(cda:playingDevice)=1">SHALL contain exactly one [1..1] playingDevice (CONF:7903).</sch:assert>
      <sch:assert id="a-7905" test="count(cda:scopingEntity)=1">SHALL contain exactly one [1..1] scopingEntity (CONF:7905).</sch:assert>
      <sch:assert id="a-7908" test="cda:scopingEntity[count(cda:id) &gt; 0]">This scopingEntity SHALL contain at least one [1..*] id (CONF:7908).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.37-errors" context="cda:participantRole[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.37']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.37-errors-abstract" />
      <sch:assert id="a-7901" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.37'])=1">SHALL contain exactly one [1..1] templateId (CONF:7901) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.37" (CONF:10522).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.2.10-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.2.10-errors-abstract" abstract="true">
      <sch:assert id="a-7808" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7808).</sch:assert>
      <sch:assert id="a-7809" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7809).</sch:assert>
      <sch:assert id="a-15397" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15397).</sch:assert>
      <sch:assert id="a-15398" test="cda:code[@code='29545-1' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="29545-1" Physical Findings (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15398).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.2.10-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.2.10']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.2.10-errors-abstract" />
      <sch:assert id="a-7806" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.2.10'])=1">SHALL contain exactly one [1..1] templateId (CONF:7806) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.2.10" (CONF:10465).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.3.18-errors">
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.18-errors-abstract" abstract="true">
      <sch:assert id="a-7814" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7814).</sch:assert>
      <sch:assert id="a-7815" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7815).</sch:assert>
      <sch:assert id="a-15435" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15435).</sch:assert>
      <sch:assert id="a-15436" test="cda:code[@code='10187-3' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="10187-3" Review of Systems (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15436).</sch:assert>
    </sch:rule>
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.18-errors" context="cda:section[cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.3.18']">
      <sch:extends rule="r-1.3.6.1.4.1.19376.1.5.3.1.3.18-errors-abstract" />
      <sch:assert id="a-7812" test="count(cda:templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.3.18'])=1">SHALL contain exactly one [1..1] templateId (CONF:7812) such that it SHALL contain exactly one [1..1] @root="1.3.6.1.4.1.19376.1.5.3.1.3.18" (CONF:10469).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.11-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.11-errors-abstract" abstract="true">
      <sch:assert id="a-7818" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7818).</sch:assert>
      <sch:assert id="a-7819" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7819).</sch:assert>
      <sch:assert id="a-15359" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15359).</sch:assert>
      <sch:assert id="a-15360" test="cda:code[@code='10183-2' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="10183-2" Hospital Discharge Medications (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15360).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.11-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.11']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.11-errors-abstract" />
      <sch:assert id="a-7816" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.11'])=1">SHALL contain exactly one [1..1] templateId (CONF:7816) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.11" (CONF:10396).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.11.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.11.1-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.11-errors-abstract" />
      <sch:assert id="a-7824" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7824).</sch:assert>
      <sch:assert id="a-7825" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7825).</sch:assert>
      <sch:assert id="a-7826" test="count(cda:entry[count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.35'])=1]) &gt; 0">SHALL contain at least one [1..*] entry (CONF:7826) such that it SHALL contain exactly one [1..1] Discharge Medication (templateId:2.16.840.1.113883.10.20.22.4.35) (CONF:15491).</sch:assert>
      <sch:assert id="a-15361" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15361).</sch:assert>
      <sch:assert id="a-15362" test="cda:code[@code='10183-2' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="10183-2" Hospital Discharge Medications (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15362).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.11.1-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.11.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.11.1-errors-abstract" />
      <sch:assert id="a-7822" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.11.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:7822) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.11.1" (CONF:10397).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.20-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.20-errors-abstract" abstract="true">
      <sch:assert id="a-7830" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7830).</sch:assert>
      <sch:assert id="a-7831" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7831).</sch:assert>
      <sch:assert id="a-15474" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15474).</sch:assert>
      <sch:assert id="a-15475" test="cda:code[@code='11348-0' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="11348-0" History of Past Illness (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15475).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.20-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.20']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.20-errors-abstract" />
      <sch:assert id="a-7828" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.20'])=1">SHALL contain exactly one [1..1] templateId (CONF:7828) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.20" (CONF:10390).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1-errors">
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1-errors-abstract" abstract="true">
      <sch:assert id="a-7834" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7834).</sch:assert>
      <sch:assert id="a-7835" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7835).</sch:assert>
      <sch:assert id="a-15451" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15451).</sch:assert>
      <sch:assert id="a-15452" test="cda:code[@code='10154-3' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="10154-3" Chief Complaint (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15452).</sch:assert>
    </sch:rule>
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1-errors" context="cda:section[cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1']">
      <sch:extends rule="r-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1-errors-abstract" />
      <sch:assert id="a-7832" test="count(cda:templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:7832) such that it SHALL contain exactly one [1..1] @root="1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1" (CONF:10453).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.12-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.12-errors-abstract" abstract="true">
      <sch:assert id="a-7838" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7838).</sch:assert>
      <sch:assert id="a-7839" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7839).</sch:assert>
      <sch:assert id="a-15429" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15429).</sch:assert>
      <sch:assert id="a-15430" test="cda:code[@code='29299-5' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="29299-5" Reason for Visit (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15430).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.12-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.12']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.12-errors-abstract" />
      <sch:assert id="a-7836" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.12'])=1">SHALL contain exactly one [1..1] templateId (CONF:7836) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.12" (CONF:10448).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.13-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.13-errors-abstract" abstract="true">
      <sch:assert id="a-7842" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7842).</sch:assert>
      <sch:assert id="a-7843" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7843).</sch:assert>
      <sch:assert id="a-15449" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15449).</sch:assert>
      <sch:assert id="a-15450" test="cda:code[@code='46239-0' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="46239-0" Chief Complaint and Reason for Visit (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15450).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.13-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.13']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.13-errors-abstract" />
      <sch:assert id="a-7840" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.13'])=1">SHALL contain exactly one [1..1] templateId (CONF:7840) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.13" (CONF:10383).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.3.1-errors">
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.1-errors-abstract" abstract="true">
      <sch:assert id="a-7846" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7846).</sch:assert>
      <sch:assert id="a-7847" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7847).</sch:assert>
      <sch:assert id="a-15427" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15427).</sch:assert>
      <sch:assert id="a-15428" test="cda:code[@code='42349-1' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="42349-1" Reason for Referral (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15428).</sch:assert>
    </sch:rule>
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.1-errors" context="cda:section[cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.3.1']">
      <sch:extends rule="r-1.3.6.1.4.1.19376.1.5.3.1.3.1-errors-abstract" />
      <sch:assert id="a-7844" test="count(cda:templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.3.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:7844) such that it SHALL contain exactly one [1..1] @root="1.3.6.1.4.1.19376.1.5.3.1.3.1" (CONF:10468).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.3.4-errors">
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.4-errors-abstract" abstract="true">
      <sch:assert id="a-7850" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7850).</sch:assert>
      <sch:assert id="a-7851" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7851).</sch:assert>
      <sch:assert id="a-15477" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15477).</sch:assert>
      <sch:assert id="a-15478" test="cda:code[@code='10164-2' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="10164-2" (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15478).</sch:assert>
    </sch:rule>
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.4-errors" context="cda:section[cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.3.4']">
      <sch:extends rule="r-1.3.6.1.4.1.19376.1.5.3.1.3.4-errors-abstract" />
      <sch:assert id="a-7848" test="count(cda:templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.3.4'])=1">SHALL contain exactly one [1..1] templateId (CONF:7848) such that it SHALL contain exactly one [1..1] @root="1.3.6.1.4.1.19376.1.5.3.1.3.4" (CONF:10458).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.3.5-errors">
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.5-errors-abstract" abstract="true">
      <sch:assert id="a-7854" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7854).</sch:assert>
      <sch:assert id="a-7855" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7855).</sch:assert>
      <sch:assert id="a-15487" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15487).</sch:assert>
      <sch:assert id="a-15488" test="cda:code[@code='8648-8' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="8648-8" Hospital Course (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15488).</sch:assert>
    </sch:rule>
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.5-errors" context="cda:section[cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.3.5']">
      <sch:extends rule="r-1.3.6.1.4.1.19376.1.5.3.1.3.5-errors-abstract" />
      <sch:assert id="a-7852" test="count(cda:templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.3.5'])=1">SHALL contain exactly one [1..1] templateId (CONF:7852) such that it SHALL contain exactly one [1..1] @root="1.3.6.1.4.1.19376.1.5.3.1.3.5" (CONF:10459).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.21.2.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.21.2.1-errors-abstract" abstract="true">
      <sch:assert id="a-7871" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7871).</sch:assert>
      <sch:assert id="a-7872" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7872).</sch:assert>
      <sch:assert id="a-15389" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15389).</sch:assert>
      <sch:assert id="a-15390" test="cda:code[@code='61149-1' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="61149-1" Objective (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15390).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.21.2.1-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.21.2.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.21.2.1-errors-abstract" />
      <sch:assert id="a-7869" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.21.2.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:7869) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.21.2.1" (CONF:10462).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.21.2.2-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.21.2.2-errors-abstract" abstract="true">
      <sch:assert id="a-7875" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7875).</sch:assert>
      <sch:assert id="a-7876" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7876).</sch:assert>
      <sch:assert id="a-15437" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15437).</sch:assert>
      <sch:assert id="a-15438" test="cda:code[@code='61150-9' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="61150-9" Subjective (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15438).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.21.2.2-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.21.2.2']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.21.2.2-errors-abstract" />
      <sch:assert id="a-7873" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.21.2.2'])=1">SHALL contain exactly one [1..1] templateId (CONF:7873) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.21.2.2" (CONF:10470).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.16-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.16-errors-abstract" abstract="true">
      <sch:assert id="a-7912" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7912).</sch:assert>
      <sch:assert id="a-7913" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7913).</sch:assert>
      <sch:assert id="a-15365" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15365).</sch:assert>
      <sch:assert id="a-15366" test="cda:code[@code='11493-4' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="11493-4" Hospital Discharge Studies Summary (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15366).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.16-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.16']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.16-errors-abstract" />
      <sch:assert id="a-7910" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.16'])=1">SHALL contain exactly one [1..1] templateId (CONF:7910) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.16" (CONF:10398).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.2-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.2-errors-abstract" abstract="true">
      <sch:assert id="a-7967" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7967).</sch:assert>
      <sch:assert id="a-7968" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7968).</sch:assert>
      <sch:assert id="a-15367" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15367).</sch:assert>
      <sch:assert id="a-15368" test="cda:code[@code='11369-6' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="11369-6" Immunizations (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15368).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.2-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.2']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.2-errors-abstract" />
      <sch:assert id="a-7965" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.2'])=1">SHALL contain exactly one [1..1] templateId (CONF:7965) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.2" (CONF:10399).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.14-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.14-errors-abstract" abstract="true">
      <sch:assert id="a-7922" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7922).</sch:assert>
      <sch:assert id="a-7923" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7923).</sch:assert>
      <sch:assert id="a-14578" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:14578).</sch:assert>
      <sch:assert id="a-14579" test="cda:code[@code='47420-5' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="47420-5" Functional Status (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:14579).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.14-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.14']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.14-errors-abstract" />
      <sch:assert id="a-7920" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.14'])=1">SHALL contain exactly one [1..1] templateId (CONF:7920) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.14" (CONF:10389).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.18-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.18-errors-abstract" abstract="true">
      <sch:assert id="a-7926" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7926).</sch:assert>
      <sch:assert id="a-7927" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7927).</sch:assert>
      <sch:assert id="a-15395" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15395).</sch:assert>
      <sch:assert id="a-15396" test="cda:code[@code='48768-6' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="48768-6" Payers (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15396).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.18-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.18']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.18-errors-abstract" />
      <sch:assert id="a-7924" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.18'])=1">SHALL contain exactly one [1..1] templateId (CONF:7924) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.18" (CONF:10434).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.21-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.21-errors-abstract" abstract="true">
      <sch:assert id="a-7930" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7930).</sch:assert>
      <sch:assert id="a-7931" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7931).</sch:assert>
      <sch:assert id="a-15340" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15340).</sch:assert>
      <sch:assert id="a-15342" test="cda:code[@code='42348-3' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="42348-3" Advance Directives (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15342).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.21-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.21']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.21-errors-abstract" />
      <sch:assert id="a-7928" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.21'])=1">SHALL contain exactly one [1..1] templateId (CONF:7928) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.21" (CONF:10376).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.15-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.15-errors-abstract" abstract="true">
      <sch:assert id="a-7934" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7934).</sch:assert>
      <sch:assert id="a-7935" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7935).</sch:assert>
      <sch:assert id="a-15469" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15469).</sch:assert>
      <sch:assert id="a-15470" test="cda:code[@code='10157-6' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="10157-6" Family History (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15470).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.15-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.15']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.15-errors-abstract" />
      <sch:assert id="a-7932" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.15'])=1">SHALL contain exactly one [1..1] templateId (CONF:7932) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.15" (CONF:10388).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.17-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.17-errors-abstract" abstract="true">
      <sch:assert id="a-7938" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7938).</sch:assert>
      <sch:assert id="a-7939" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7939).</sch:assert>
      <sch:assert id="a-14819" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:14819).</sch:assert>
      <sch:assert id="a-14820" test="cda:code[@code='29762-2' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="29762-2" Social History (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:14820).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.17-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.17']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.17-errors-abstract" />
      <sch:assert id="a-7936" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.17'])=1">SHALL contain exactly one [1..1] templateId (CONF:7936) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.17" (CONF:10449).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.22-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.22-errors-abstract" abstract="true">
      <sch:assert id="a-7942" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7942).</sch:assert>
      <sch:assert id="a-7943" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7943).</sch:assert>
      <sch:assert id="a-14901-c" test=".">SHALL contain exactly one [1..1] @typeCode="COMP" has component (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:14901).</sch:assert>
      <sch:assert id="a-15461" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15461).</sch:assert>
      <sch:assert id="a-15462" test="cda:code[@code='46240-8' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="46240-8" Encounters (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15462).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.22-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.22']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.22-errors-abstract" />
      <sch:assert id="a-7940" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.22'])=1">SHALL contain exactly one [1..1] templateId (CONF:7940) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.22" (CONF:10386).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.23-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.23-errors-abstract" abstract="true">
      <sch:assert id="a-7946" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7946).</sch:assert>
      <sch:assert id="a-7947" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7947).</sch:assert>
      <sch:assert id="a-15381" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15381).</sch:assert>
      <sch:assert id="a-15382" test="cda:code[@code='46264-8' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="46264-8" Medical Equipment (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15382).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.23-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.23']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.23-errors-abstract" />
      <sch:assert id="a-7944" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.23'])=1">SHALL contain exactly one [1..1] templateId (CONF:7944) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.23" (CONF:10404).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.3.26-errors">
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.26-errors-abstract" abstract="true">
      <sch:assert id="a-7973" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7973).</sch:assert>
      <sch:assert id="a-7974" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7974).</sch:assert>
      <sch:assert id="a-15363" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15363).</sch:assert>
      <sch:assert id="a-15364" test="cda:code[@code='10184-0' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="10184-0" Hospital Discharge Physical (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15364).</sch:assert>
    </sch:rule>
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.26-errors" context="cda:section[cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.3.26']">
      <sch:extends rule="r-1.3.6.1.4.1.19376.1.5.3.1.3.26-errors-abstract" />
      <sch:assert id="a-7971" test="count(cda:templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.3.26'])=1">SHALL contain exactly one [1..1] templateId (CONF:7971) such that it SHALL contain exactly one [1..1] @root="1.3.6.1.4.1.19376.1.5.3.1.3.26" (CONF:10460).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.3.33-errors">
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.33-errors-abstract" abstract="true">
      <sch:assert id="a-7977" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7977).</sch:assert>
      <sch:assert id="a-7978" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7978).</sch:assert>
      <sch:assert id="a-15459" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15459).</sch:assert>
      <sch:assert id="a-15460" test="cda:code[@code='42344-2' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="42344-2" Discharge Diet (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15460).</sch:assert>
    </sch:rule>
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.33-errors" context="cda:section[cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.3.33']">
      <sch:extends rule="r-1.3.6.1.4.1.19376.1.5.3.1.3.33-errors-abstract" />
      <sch:assert id="a-7975" test="count(cda:templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.3.33'])=1">SHALL contain exactly one [1..1] templateId (CONF:7975) such that it SHALL contain exactly one [1..1] @root="1.3.6.1.4.1.19376.1.5.3.1.3.33" (CONF:10455).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.24-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.24-errors-abstract" abstract="true">
      <sch:assert id="a-7981" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7981).</sch:assert>
      <sch:assert id="a-7982" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7982).</sch:assert>
      <sch:assert id="a-15355" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15355).</sch:assert>
      <sch:assert id="a-15356" test="cda:code[@code='11535-2' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="11535-2" Hospital Discharge Diagnosis (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15356).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.24-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.24']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.24-errors-abstract" />
      <sch:assert id="a-7979" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.24'])=1">SHALL contain exactly one [1..1] templateId (CONF:7979) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.24" (CONF:10394).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.2.5-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.2.5-errors-abstract" abstract="true">
      <sch:assert id="a-7987" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:7987).</sch:assert>
      <sch:assert id="a-7988" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:7988).</sch:assert>
      <sch:assert id="a-15472" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15472).</sch:assert>
      <sch:assert id="a-15473" test="cda:code[@code='10210-3' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="10210-3" General Status (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15473).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.2.5-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.2.5']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.2.5-errors-abstract" />
      <sch:assert id="a-7985" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.2.5'])=1">SHALL contain exactly one [1..1] templateId (CONF:7985) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.2.5" (CONF:10457).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.12-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.12-errors-abstract" abstract="true">
      <sch:assert id="a-8298" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode, which SHALL be selected from ValueSet ProcedureAct statusCode 2.16.840.1.113883.11.20.9.22 DYNAMIC (CONF:8298).</sch:assert>
      <sch:assert id="a-8293" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:8293).</sch:assert>
      <sch:assert id="a-8292" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:8292).</sch:assert>
      <sch:assert id="a-8290" test="@moodCode and @moodCode=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.18']/voc:code/@value">SHALL contain exactly one [1..1] @moodCode, which SHALL be selected from ValueSet MoodCodeEvnInt 2.16.840.1.113883.11.20.9.18 STATIC 2011-04-03 (CONF:8290).</sch:assert>
      <sch:assert id="a-8289" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" Act (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8289).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.12-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.12']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.12-errors-abstract" />
      <sch:assert id="a-8291" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.12'])=1">SHALL contain exactly one [1..1] templateId (CONF:8291) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.12" (CONF:10519).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.13-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.13-errors-abstract" abstract="true">
      <sch:assert id="a-8237" test="@moodCode and @moodCode=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.18']/voc:code/@value">SHALL contain exactly one [1..1] @moodCode, which SHALL be selected from ValueSet MoodCodeEvnInt 2.16.840.1.113883.11.20.9.18 STATIC 2011-04-03 (CONF:8237).</sch:assert>
      <sch:assert id="a-8239" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:8239).</sch:assert>
      <sch:assert id="a-8245" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode, which SHALL be selected from ValueSet ProcedureAct statusCode 2.16.840.1.113883.11.20.9.22 DYNAMIC (CONF:8245).</sch:assert>
      <sch:assert id="a-8282" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8282).</sch:assert>
      <sch:assert id="a-16846" test="count(cda:value)=1">SHALL contain exactly one [1..1] value (CONF:16846).</sch:assert>
      <sch:assert id="a-19197" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19197).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.13-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.13']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.13-errors-abstract" />
      <sch:assert id="a-8238" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.13'])=1">SHALL contain exactly one [1..1] templateId (CONF:8238) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.13" (CONF:10520).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.26-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.26-errors-abstract" abstract="true">
      <sch:assert id="a-8024" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8024).</sch:assert>
      <sch:assert id="a-8025" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8025).</sch:assert>
      <sch:assert id="a-15439" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15439).</sch:assert>
      <sch:assert id="a-15440" test="cda:code[@code='29554-3' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="29554-3" Surgery Description (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15440).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.26-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.26']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.26-errors-abstract" />
      <sch:assert id="a-8022" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.26'])=1">SHALL contain exactly one [1..1] templateId (CONF:8022) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.26" (CONF:10450).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.32-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.32-errors-abstract" abstract="true">
      <sch:assert id="a-8027" test="cda:code[@code='10830-8']">SHALL contain exactly one [1..1] code/@code="10830-8" Complications (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:8027).</sch:assert>
      <sch:assert id="a-8028" test="count(cda:title[@xsi:type='ST'])=1">SHALL contain exactly one [1..1] title (CONF:8028).</sch:assert>
      <sch:assert id="a-8029" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8029).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.32-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.32']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.32-errors-abstract" />
      <sch:assert id="a-8026" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.32'])=1">SHALL contain exactly one [1..1] templateId (CONF:8026) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.32" (CONF:10385).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.7.12-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.7.12-errors-abstract" abstract="true">
      <sch:assert id="a-8032" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8032).</sch:assert>
      <sch:assert id="a-8033" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8033).</sch:assert>
      <sch:assert id="a-15391" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15391).</sch:assert>
      <sch:assert id="a-15392" test="cda:code[@code='10216-0' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="10216-0" Operative Note Fluids (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15392).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.7.12-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.7.12']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.7.12-errors-abstract" />
      <sch:assert id="a-8030" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.7.12'])=1">SHALL contain exactly one [1..1] templateId (CONF:8030) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.7.12" (CONF:10463).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.7.14-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.7.14-errors-abstract" abstract="true">
      <sch:assert id="a-8036" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8036).</sch:assert>
      <sch:assert id="a-8037" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8037).</sch:assert>
      <sch:assert id="a-15393" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15393).</sch:assert>
      <sch:assert id="a-15394" test="cda:code[@code='10223-6' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="10223-6" Operative Note Surgical Procedure (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15394).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.7.14-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.7.14']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.7.14-errors-abstract" />
      <sch:assert id="a-8034" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.7.14'])=1">SHALL contain exactly one [1..1] templateId (CONF:8034) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.7.14" (CONF:10464).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.7.13-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.7.13-errors-abstract" abstract="true">
      <sch:assert id="a-8040" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8040).</sch:assert>
      <sch:assert id="a-8041" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8041).</sch:assert>
      <sch:assert id="a-15441" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15441).</sch:assert>
      <sch:assert id="a-15442" test="cda:code[@code='11537-8' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="11537-8" Surgical Drains (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15442).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.7.13-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.7.13']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.7.13-errors-abstract" />
      <sch:assert id="a-8038" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.7.13'])=1">SHALL contain exactly one [1..1] templateId (CONF:8038) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.7.13" (CONF:10473).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.33-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.33-errors-abstract" abstract="true">
      <sch:assert id="a-8044" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8044).</sch:assert>
      <sch:assert id="a-8045" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8045).</sch:assert>
      <sch:assert id="a-15371" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15371).</sch:assert>
      <sch:assert id="a-15372" test="cda:code[@code='55122-6' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="55122-6" Implants (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15372).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.33-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.33']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.33-errors-abstract" />
      <sch:assert id="a-8042" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.33'])=1">SHALL contain exactly one [1..1] templateId (CONF:8042) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.33" (CONF:10401).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.29-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.29-errors-abstract" abstract="true">
      <sch:assert id="a-8060" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8060).</sch:assert>
      <sch:assert id="a-8061" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8061).</sch:assert>
      <sch:assert id="a-15419" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15419).</sch:assert>
      <sch:assert id="a-15420" test="cda:code[@code='59768-2' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="59768-2" Procedure Indications  (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15420).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.29-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.29']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.29-errors-abstract" />
      <sch:assert id="a-8058" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.29'])=1">SHALL contain exactly one [1..1] templateId (CONF:8058) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.29" (CONF:10445).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.27-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.27-errors-abstract" abstract="true">
      <sch:assert id="a-8064" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8064).</sch:assert>
      <sch:assert id="a-8065" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8065).</sch:assert>
      <sch:assert id="a-15411" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15411).</sch:assert>
      <sch:assert id="a-15412" test="cda:code[@code='29554-3' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="29554-3" Procedure Description (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15412).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.27-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.27']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.27-errors-abstract" />
      <sch:assert id="a-8062" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.27'])=1">SHALL contain exactly one [1..1] templateId (CONF:8062) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.27" (CONF:10442).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.25-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.25-errors-abstract" abstract="true">
      <sch:assert id="a-8068" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8068).</sch:assert>
      <sch:assert id="a-8069" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8069).</sch:assert>
      <sch:assert id="a-15351" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15351).</sch:assert>
      <sch:assert id="a-15352" test="cda:code[@code='59774-0']">This code SHALL contain exactly one [1..1] @code="59774-0" Anesthesia (CONF:15352).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.25-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.25']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.25-errors-abstract" />
      <sch:assert id="a-8066" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.25'])=1">SHALL contain exactly one [1..1] templateId (CONF:8066) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.25" (CONF:10380).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.18.2.12-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.18.2.12-errors-abstract" abstract="true">
      <sch:assert id="a-8072" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8072).</sch:assert>
      <sch:assert id="a-8073" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8073).</sch:assert>
      <sch:assert id="a-15413" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15413).</sch:assert>
      <sch:assert id="a-15414" test="cda:code[@code='59775-7' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="59775-7" Procedure Disposition (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15414).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.18.2.12-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.18.2.12']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.18.2.12-errors-abstract" />
      <sch:assert id="a-8070" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.18.2.12'])=1">SHALL contain exactly one [1..1] templateId (CONF:8070) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.18.2.12" (CONF:10466).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.18.2.9-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.18.2.9-errors-abstract" abstract="true">
      <sch:assert id="a-8076" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8076).</sch:assert>
      <sch:assert id="a-8077" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8077).</sch:assert>
      <sch:assert id="a-15415" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15415).</sch:assert>
      <sch:assert id="a-15416" test="cda:code[@code='59770-8' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="59770-8" Procedure Estimated Blood Loss (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15416).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.18.2.9-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.18.2.9']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.18.2.9-errors-abstract" />
      <sch:assert id="a-8074" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.18.2.9'])=1">SHALL contain exactly one [1..1] templateId (CONF:8074) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.18.2.9" (CONF:10467).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.28-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.28-errors-abstract" abstract="true">
      <sch:assert id="a-8080" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8080).</sch:assert>
      <sch:assert id="a-8081" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8081).</sch:assert>
      <sch:assert id="a-15417" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15417).</sch:assert>
      <sch:assert id="a-15418" test="cda:code[@code='59776-5' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="59776-5" Procedure Findings (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15418).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.28-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.28']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.28-errors-abstract" />
      <sch:assert id="a-8078" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.28'])=1">SHALL contain exactly one [1..1] templateId (CONF:8078) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.28" (CONF:10443).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.30-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.30-errors-abstract" abstract="true">
      <sch:assert id="a-8084" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8084).</sch:assert>
      <sch:assert id="a-8085" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8085).</sch:assert>
      <sch:assert id="a-15399" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15399).</sch:assert>
      <sch:assert id="a-15400" test="cda:code[@code='59772-4' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="59772-4" Planned Procedure (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15400).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.30-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.30']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.30-errors-abstract" />
      <sch:assert id="a-8082" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.30'])=1">SHALL contain exactly one [1..1] templateId (CONF:8082) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.30" (CONF:10436).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.31-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.31-errors-abstract" abstract="true">
      <sch:assert id="a-8088" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8088).</sch:assert>
      <sch:assert id="a-8089" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8089).</sch:assert>
      <sch:assert id="a-15421" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15421).</sch:assert>
      <sch:assert id="a-15422" test="cda:code[@code='59773-2' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="59773-2" Procedure Specimens Taken (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15422).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.31-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.31']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.31-errors-abstract" />
      <sch:assert id="a-8086" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.31'])=1">SHALL contain exactly one [1..1] templateId (CONF:8086) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.31" (CONF:10446).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.34-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.34-errors-abstract" abstract="true">
      <sch:assert id="a-8099" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8099).</sch:assert>
      <sch:assert id="a-8100" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8100).</sch:assert>
      <sch:assert id="a-15405" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15405).</sch:assert>
      <sch:assert id="a-15406" test="cda:code[@code='10219-4' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="10219-4" Preoperative Diagnosis (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15406).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.34-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.34']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.34-errors-abstract" />
      <sch:assert id="a-8097" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.34'])=1">SHALL contain exactly one [1..1] templateId (CONF:8097) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.34" (CONF:10439).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.35-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.35-errors-abstract" abstract="true">
      <sch:assert id="a-8103" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8103).</sch:assert>
      <sch:assert id="a-8104" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8104).</sch:assert>
      <sch:assert id="a-15401" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15401).</sch:assert>
      <sch:assert id="a-15402" test="cda:code[@code='10218-6' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="10218-6" Postoperative Diagnosis (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15402).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.35-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.35']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.35-errors-abstract" />
      <sch:assert id="a-8101" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.35'])=1">SHALL contain exactly one [1..1] templateId (CONF:8101) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.35" (CONF:10437).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.38-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.38-errors-abstract" abstract="true">
      <sch:assert id="a-8154" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8154).</sch:assert>
      <sch:assert id="a-8155" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8155).</sch:assert>
      <sch:assert id="a-15383" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15383).</sch:assert>
      <sch:assert id="a-15384" test="cda:code[@code='29549-3' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="29549-3" Medications Administered (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15384).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.38-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.38']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.38-errors-abstract" />
      <sch:assert id="a-8152" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.38'])=1">SHALL contain exactly one [1..1] templateId (CONF:8152) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.38" (CONF:10405).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.39-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.39-errors-abstract" abstract="true">
      <sch:assert id="a-8162" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8162).</sch:assert>
      <sch:assert id="a-8163" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8163).</sch:assert>
      <sch:assert id="a-15379" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15379).</sch:assert>
      <sch:assert id="a-15380" test="cda:code[@code='11329-0' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="11329-0" Medical (General) History (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15380).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.39-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.39']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.39-errors-abstract" />
      <sch:assert id="a-8160" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.39'])=1">SHALL contain exactly one [1..1] templateId (CONF:8160) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.39" (CONF:10403).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.36-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.36-errors-abstract" abstract="true">
      <sch:assert id="a-8170" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8170).</sch:assert>
      <sch:assert id="a-8171" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8171).</sch:assert>
      <sch:assert id="a-15403" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15403).</sch:assert>
      <sch:assert id="a-15404" test="cda:code[@code='59769-0' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="59769-0" Postprocedure Diagnosis (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15404).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.36-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.36']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.36-errors-abstract" />
      <sch:assert id="a-8167" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.36'])=1">SHALL contain exactly one [1..1] templateId (CONF:8167) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.36" (CONF:10438).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.37-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.37-errors-abstract" abstract="true">
      <sch:assert id="a-8176" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8176).</sch:assert>
      <sch:assert id="a-8177" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8177).</sch:assert>
      <sch:assert id="a-15453" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15453).</sch:assert>
      <sch:assert id="a-15454" test="cda:code[@code='55109-3' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="55109-3" Complications (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15454).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.37-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.37']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.37-errors-abstract" />
      <sch:assert id="a-8174" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.37'])=1">SHALL contain exactly one [1..1] templateId (CONF:8174) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.37" (CONF:10384).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.40-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.40-errors-abstract" abstract="true">
      <sch:assert id="a-8180" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8180).</sch:assert>
      <sch:assert id="a-8181" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8181).</sch:assert>
      <sch:assert id="a-15373" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15373).</sch:assert>
      <sch:assert id="a-15374" test="cda:code[@code='59771-6' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="59771-6" Procedure Implants (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15374).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.40-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.40']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.40-errors-abstract" />
      <sch:assert id="a-8178" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.40'])=1">SHALL contain exactly one [1..1] templateId (CONF:8178) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.40" (CONF:10444).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.3-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.3-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-errors-abstract" />
      <sch:assert id="a-8338" test="count(cda:componentOf)=1">SHALL contain exactly one [1..1] componentOf (CONF:8338).</sch:assert>
      <sch:assert id="a-8339" test="cda:componentOf[count(cda:encompassingEncounter)=1]">This componentOf SHALL contain exactly one [1..1] encompassingEncounter (CONF:8339).</sch:assert>
      <sch:assert id="a-8340" test="cda:componentOf/cda:encompassingEncounter[count(cda:id)=1]">This encompassingEncounter SHALL contain exactly one [1..1] id (CONF:8340).</sch:assert>
      <sch:assert id="a-8341" test="cda:componentOf/cda:encompassingEncounter[count(cda:effectiveTime)=1]">This encompassingEncounter SHALL contain exactly one [1..1] effectiveTime (CONF:8341).</sch:assert>
      <sch:assert id="a-8349" test="count(cda:component)=1">SHALL contain exactly one [1..1] component (CONF:8349).</sch:assert>
      <sch:assert id="a-8350-c" test="count(cda:component/cda:structuredBody | cda:component/cda:nonXMLBody)=1">A History and Physical document can have either a structuredBody or a nonXMLBody (CONF:8350).</sch:assert>
      <sch:assert id="a-10135-c" test="string-length(cda:componentOf/cda:encompassingEncounter/cda:effectiveTime//@value)&gt;=8">The content of effectiveTime SHALL be a conformant US Realm Date and Time (DT.US.FIELDED) (2.16.840.1.113883.10.20.22.5.4) (CONF:10135).</sch:assert>
      <sch:assert id="a-17185" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:17185).</sch:assert>
      <sch:assert id="a-17186" test="cda:code[@code]">This code SHALL contain exactly one [1..1] @code, which SHALL be selected from ValueSet HPDocumentType 2.16.840.1.113883.1.11.20.22 DYNAMIC (CONF:17186).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.3-errors" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.3']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.3-errors-abstract" />
      <sch:assert id="a-8283" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.1.3'])=1">SHALL contain exactly one [1..1] templateId (CONF:8283) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.1.3" (CONF:10046).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.5-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.5-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-errors-abstract" />
      <sch:assert id="a-9484-c" test="count(//cda:*[cda:templateId/@root='2.16.840.1.113883.10.20.6.1.2'])=1">SHALL contain exactly one [1..1] Findings Section (DIR) (templateId:2.16.840.1.113883.10.20.6.1.2) (CONF:9484).</sch:assert>
      <sch:assert id="a-14833" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:14833).</sch:assert>
      <sch:assert id="a-14907" test="count(cda:component)=1">SHALL contain exactly one [1..1] component (CONF:14907).</sch:assert>
      <sch:assert id="a-14908-c" test="count(cda:component/cda:structuredBody | cda:component/cda:nonXMLBody) =1">A Diagnostic Imaging Report can have either a structuredBody or a nonXMLBody (CONF:14908).</sch:assert>
      <sch:assert id="a-14910-c" test="not(testable)">If structuredBody, the component/structuredBody SHALL conform to the section constraints below (CONF:14910).</sch:assert>
      <sch:assert id="a-8416" test="count(cda:documentationOf[count(cda:serviceEvent[@classCode='ACT'][count(cda:code)=1])=1])=1">SHALL contain exactly one [1..1] documentationOf (CONF:8416) such that it SHALL contain exactly one [1..1] serviceEvent (CONF:8431). This serviceEvent SHALL contain exactly one [1..1] @classCode="ACT" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8430). This serviceEvent SHALL contain exactly one [1..1] code (CONF:8419).</sch:assert>
      <sch:assert id="a-8410-c" test="not(cda:informant)">SHALL NOT contain [0..0] informant (CONF:8410).</sch:assert>
      <sch:assert id="a-8408" test="cda:code[@code]">SHALL contain exactly one [1..1] code/@code, which SHALL be selected from ValueSet DIRDocumentTypeCodes 2.16.840.1.113883.11.20.9.32 DYNAMIC (CONF:8408).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.5-errors" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.5']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.5-errors-abstract" />
      <sch:assert id="a-8404" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.1.5'])=1">SHALL contain exactly one [1..1] templateId (CONF:8404) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.1.5" (CONF:10042).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.4-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.4-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-errors-abstract" />
      <sch:assert id="a-8375-c" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.1.4'])=1">SHALL contain exactly one [1..1] templateId (CONF:8375).</sch:assert>
      <sch:assert id="a-8382" test="count(cda:inFulfillmentOf) &gt; 0">SHALL contain at least one [1..*] inFulfillmentOf (CONF:8382).</sch:assert>
      <sch:assert id="a-8385" test="cda:inFulfillmentOf[count(cda:order)=1]">Such inFulfillmentOfs SHALL contain exactly one [1..1] order (CONF:8385).</sch:assert>
      <sch:assert id="a-8386" test="count(cda:componentOf)=1">SHALL contain exactly one [1..1] componentOf (CONF:8386).</sch:assert>
      <sch:assert id="a-8387" test="cda:componentOf[count(cda:encompassingEncounter)=1]">This componentOf SHALL contain exactly one [1..1] encompassingEncounter (CONF:8387).</sch:assert>
      <sch:assert id="a-8388" test="cda:componentOf/cda:encompassingEncounter[count(cda:id)=1]">This encompassingEncounter SHALL contain exactly one [1..1] id (CONF:8388).</sch:assert>
      <sch:assert id="a-8389" test="cda:componentOf/cda:encompassingEncounter[count(cda:effectiveTime)=1]">This encompassingEncounter SHALL contain exactly one [1..1] effectiveTime (CONF:8389).</sch:assert>
      <sch:assert id="a-8397" test="count(cda:component)=1">SHALL contain exactly one [1..1] component (CONF:8397).</sch:assert>
      <sch:assert id="a-8398-c" test="count(cda:component/cda:structuredBody | cda:component/cda:nonXMLBody)=1">A Consultation Note can have either a structuredBody or a nonXMLBody (CONF:8398).</sch:assert>
      <sch:assert id="a-9102" test="cda:inFulfillmentOf/cda:order[count(cda:id) &gt; 0]">This order SHALL contain at least one [1..*] id (CONF:9102).</sch:assert>
      <sch:assert id="a-9493-c" test="//cda:section[cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.3.4']">SHALL contain exactly one [1..1] History of Present Illness Section (templateId:1.3.6.1.4.1.19376.1.5.3.1.3.4) (CONF:9493).</sch:assert>
      <sch:assert id="a-9501-c" test="count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=2 or (count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.9'])=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=0)">SHALL include an Assessment and Plan Section, or an Assessment Section and a Plan of Care Section (CONF:9501).</sch:assert>
      <sch:assert id="a-9503-c" test="not(tested)">If structuredBody, the component/structuredBody SHALL conform to the section constraints below (CONF:9503).</sch:assert>
      <sch:assert id="a-9504-c" test="//cda:section/cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.3.1' or //cda:section/cda:templateId/@root='2.16.840.1.113883.10.20.22.2.12'">SHALL include a Reason for Referral or Reason for Visit section (CONF:9504).</sch:assert>
      <sch:assert id="a-10028-c" test="count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=2 or (count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.9'])=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=0)">SHALL NOT include an Assessment/Plan Section when an Assessment Section and a Plan of Care Section are present (CONF:10028).</sch:assert>
      <sch:assert id="a-10029-c" test="count(//cda:templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.12'])&lt;=1 and count(//cda:templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.12'])=2 or (count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.13'])=1 and count(//cda:templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.12'])=0)">SHALL NOT include a combined Chief Complaint and Reason for Visit Section with either a Chief Complaint Section or a Reason for Visit Section (CONF:10029).</sch:assert>
      <sch:assert id="a-10132-c" test="string-length(cda:componentOf/cda:encompassingEncounter/cda:effectiveTime//@value)&gt;=8">The content of effectiveTime SHALL be a conformant US Realm Date and Time (DT.US.FIELDED) (2.16.840.1.113883.10.20.22.5.4) (CONF:10132).</sch:assert>
      <sch:assert id="a-17176" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:17176).</sch:assert>
      <sch:assert id="a-17177" test="cda:code[@code]">This code SHALL contain exactly one [1..1] @code, which SHALL be selected from ValueSet ConsultDocumentType 2.16.840.1.113883.11.20.9.31 DYNAMIC (CONF:17177).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.4-errors" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.4']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.4-errors-abstract" />
      <sch:assert id="a-10040" test="cda:templateId[@root='2.16.840.1.113883.10.20.22.1.4']">This templateId SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.1.4" (CONF:10040).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.1-errors-abstract" abstract="true">
      <sch:assert id="a-8423" test="cda:templateId[@root='2.16.840.1.113883.10.20.6.2.1']">SHALL contain exactly one [1..1] templateId/@root="2.16.840.1.113883.10.20.6.2.1" (CONF:8423).</sch:assert>
      <sch:assert id="a-8424" test="@typeCode='PRF'">SHALL contain exactly one [1..1] @typeCode="PRF" Performer (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8424).</sch:assert>
      <sch:assert id="a-8426" test="count(cda:assignedEntity)=1">SHALL contain exactly one [1..1] assignedEntity (CONF:8426).</sch:assert>
      <sch:assert id="a-8427" test="cda:assignedEntity[count(cda:code)=1]">This assignedEntity SHALL contain exactly one [1..1] code (CONF:8427).</sch:assert>
      <sch:assert id="a-10033" test="cda:assignedEntity[count(cda:id) &gt; 0]">This assignedEntity SHALL contain at least one [1..*] id (CONF:10033).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.1-errors" context="cda:Performer1[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.1-errors-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.2-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.2-errors-abstract" abstract="true">
      <sch:assert id="a-8440" test="cda:templateId[@root='2.16.840.1.113883.10.20.6.2.2']">SHALL contain exactly one [1..1] templateId/@root="2.16.840.1.113883.10.20.6.2.2" (CONF:8440).</sch:assert>
      <sch:assert id="a-8881" test="@typeCode='ATND'">SHALL contain exactly one [1..1] @typeCode="ATND" Attender (CodeSystem: HL7ParticipationType 2.16.840.1.113883.5.90 STATIC) (CONF:8881).</sch:assert>
      <sch:assert id="a-8886" test="count(cda:assignedEntity)=1">SHALL contain exactly one [1..1] assignedEntity (CONF:8886).</sch:assert>
      <sch:assert id="a-8887" test="cda:assignedEntity[count(cda:id) &gt; 0]">This assignedEntity SHALL contain at least one [1..*] id (CONF:8887).</sch:assert>
      <sch:assert id="a-8888" test="cda:assignedEntity[count(cda:code)=1]">This assignedEntity SHALL contain exactly one [1..1] code (CONF:8888).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.2-errors" context="cda:encounterParticipant[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.2']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.2-errors-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.2-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.2-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-errors-abstract" />
      <sch:assert id="a-8452" test="count(cda:documentationOf)=1">SHALL contain exactly one [1..1] documentationOf (CONF:8452).</sch:assert>
      <sch:assert id="a-8453" test="cda:documentationOf/cda:serviceEvent[@classCode='PCPR']">This serviceEvent SHALL contain exactly one [1..1] @classCode="PCPR" Care Provision (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8453).</sch:assert>
      <sch:assert id="a-8454" test="cda:documentationOf/cda:serviceEvent/cda:effectiveTime[count(cda:low)=1]">This effectiveTime SHALL contain exactly one [1..1] low (CONF:8454).</sch:assert>
      <sch:assert id="a-8455" test="cda:documentationOf/cda:serviceEvent/cda:effectiveTime[count(cda:high)=1]">This effectiveTime SHALL contain exactly one [1..1] high (CONF:8455).</sch:assert>
      <sch:assert id="a-8456-c" test="count(cda:author/cda:assignedAuthor/cda:assignedPerson | cda:author/cda:assignedAuthor/cda:representedOrganization)=1">SHALL contain exactly one [1..1] assignedPerson or exactly one [1..1]  representedOrganization (CONF:8456).</sch:assert>
      <sch:assert id="a-8457-c" test=" (count(cda:author/cda:assignedAuthor/cda:representedOrganization[cda:assignedPerson | cda:assignedAuthoringDevice])=0 and (//ClinicalDocument/cda:author/cda:assignedAuthor/cda:id/@nullFlavor='NA')) or  (count(cda:representedOrganization[cda:assignedPerson | cda:assignedAuthoringDevice])&gt;0) or  (count(cda:representedOrganization)=0)">If assignedAuthor has an associated representedOrganization with no assignedPerson or assignedAuthoringDevice, then the value for ?ClinicalDocument/author/assignedAuthor/id/@NullFlavor? SHALL be ?NA? ?Not applicable? 2.16.840.1.113883.5.1008 NullFlavor STATIC (CONF:8457).</sch:assert>
      <sch:assert id="a-8480" test="cda:documentationOf[count(cda:serviceEvent)=1]">This documentationOf SHALL contain exactly one [1..1] serviceEvent (CONF:8480).</sch:assert>
      <sch:assert id="a-8481" test="cda:documentationOf/cda:serviceEvent[count(cda:effectiveTime)=1]">This serviceEvent SHALL contain exactly one [1..1] effectiveTime (CONF:8481).</sch:assert>
      <sch:assert id="a-9442" test="count(cda:author) &gt; 0">SHALL contain at least one [1..*] author (CONF:9442).</sch:assert>
      <sch:assert id="a-9443" test="cda:author[count(cda:assignedAuthor)=1]">Such authors SHALL contain exactly one [1..1] assignedAuthor (CONF:9443).</sch:assert>
      <sch:assert id="a-17180" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:17180).</sch:assert>
      <sch:assert id="a-17181" test="cda:code[@code='34133-9' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="34133-9" Summarization of Episode Note (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:17181).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.2-errors" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.2']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.2-errors-abstract" />
      <sch:assert id="a-8450" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.1.2'])=1">SHALL contain exactly one [1..1] templateId (CONF:8450) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.1.2" (CONF:10038).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.8-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.8-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-errors-abstract" />
      <sch:assert id="a-8471" test="count(cda:componentOf)=1">SHALL contain exactly one [1..1] componentOf (CONF:8471).</sch:assert>
      <sch:assert id="a-8472" test="cda:componentOf[count(cda:encompassingEncounter)=1]">This componentOf SHALL contain exactly one [1..1] encompassingEncounter (CONF:8472).</sch:assert>
      <sch:assert id="a-8473-c" test="cda:componentOf/cda:encompassingEncounter[count(cda:effectiveTime/cda:low)=1]">This encompassingEncounter SHALL contain exactly one [1..1] effectiveTime/low (CONF:8473).</sch:assert>
      <sch:assert id="a-8475-c" test="cda:componentOf/cda:encompassingEncounter[count(cda:effectiveTime/cda:high)=1]">This encompassingEncounter SHALL contain exactly one [1..1] effectiveTime/high (CONF:8475).</sch:assert>
      <sch:assert id="a-9537-c" test="count(//cda:structuredBody | //cda:nonXMLBody)=1">A Discharge Summary can have either a structuredBody or a nonXMLBody (CONF:9537).</sch:assert>
      <sch:assert id="a-9539" test="count(cda:component)=1">SHALL contain exactly one [1..1] component (CONF:9539).</sch:assert>
      <sch:assert id="a-17178" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:17178).</sch:assert>
      <sch:assert id="a-17179" test="cda:code[@code]">This code SHALL contain exactly one [1..1] @code, which SHALL be selected from ValueSet DischargeSummaryDocumentTypeCode 2.16.840.1.113883.11.20.4.1 DYNAMIC (CONF:17179).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.8-errors" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.8']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.8-errors-abstract" />
      <sch:assert id="a-8463" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.1.8'])=1">SHALL contain exactly one [1..1] templateId (CONF:8463) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.1.8" (CONF:10044).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.7-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.7-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-errors-abstract" />
      <sch:assert id="a-8486" test="count(cda:documentationOf) &gt; 0">SHALL contain at least one [1..*] documentationOf (CONF:8486).</sch:assert>
      <sch:assert id="a-8487-c" test="cda:documentationOf/cda:serviceEvent/cda:code[@codeSystem='2.16.840.1.113883.6.96' or @codeSystem='2.16.840.1.113883.6.12' or @codeSystem='2.16.840.1.113883.6.104']">I.	The value of Clinical Document /documentationOf/serviceEvent/code SHALL be from ICD9 CM Procedures (CodeSystem 2.16.840.1.113883.6.104), CPT-4 (CodeSystem 2.16.840.1.113883.6.12), or values descending from 71388002 (Procedure) from the SNOMED CT (CodeSystem 2.16.840.1.113883.6.96) ValueSet Procedure 2.16.840.1.113883.3.88.12.80.28 DYNAMIC (CONF:8487).</sch:assert>
      <sch:assert id="a-8488-c" test="cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:low">The serviceEvent/effectiveTime SHALL be present with effectiveTime/low (CONF:8488).</sch:assert>
      <sch:assert id="a-8489" test="cda:documentationOf/cda:serviceEvent[count(cda:performer[@typeCode='PPRF'][count(cda:assignedEntity[count(cda:code)=1])=1])=1]">This serviceEvent SHALL contain exactly one [1..1] performer (CONF:8489) such that it SHALL contain exactly one [1..1] @typeCode="PPRF" Primary performer (CodeSystem: HL7ParticipationType 2.16.840.1.113883.5.90 STATIC) (CONF:8495). SHALL contain exactly one [1..1] assignedEntity (CONF:10917). This assignedEntity SHALL contain exactly one [1..1] code (CONF:8490).</sch:assert>
      <sch:assert id="a-8493" test="cda:documentationOf[count(cda:serviceEvent)=1]">Such documentationOfs SHALL contain exactly one [1..1] serviceEvent (CONF:8493).</sch:assert>
      <sch:assert id="a-8494" test="cda:documentationOf/cda:serviceEvent[count(cda:effectiveTime)=1]">This serviceEvent SHALL contain exactly one [1..1] effectiveTime (CONF:8494).</sch:assert>
      <sch:assert id="a-8512-c" test="not(tested)">Any assistants SHALL be identified and SHALL be identified as secondary performers (SPRF) (CONF:8512).</sch:assert>
      <sch:assert id="a-9585" test="count(cda:component)=1">SHALL contain exactly one [1..1] component (CONF:9585).</sch:assert>
      <sch:assert id="a-9586-c" test="count(//cda:structuredBody | //cda:nonXMLBody)=1">An Operative Note can have either a structuredBody or a nonXMLBody (CONF:9586).</sch:assert>
      <sch:assert id="a-9596-c" test="not(tested)">If structuredBody, the component/structuredBody SHALL conform to the section constraints below (CONF:9596).</sch:assert>
      <sch:assert id="a-9883-c" test="count(//cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.25'])=1">SHALL contain exactly one [1..1] Anesthesia Section (templateId:2.16.840.1.113883.10.20.22.2.25) (CONF:9883).</sch:assert>
      <sch:assert id="a-9885-c" test="count(//cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.37'])=1">SHALL contain exactly one [1..1] Complications Section (templateId:2.16.840.1.113883.10.20.22.2.37) (CONF:9885).</sch:assert>
      <sch:assert id="a-9888-c" test="count(//cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.34'])=1">SHALL contain exactly one [1..1] Preoperative Diagnosis Section (templateId:2.16.840.1.113883.10.20.22.2.34) (CONF:9888).</sch:assert>
      <sch:assert id="a-9890-c" test="count(//cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.18.2.9'])=1">SHALL contain exactly one [1..1] Procedure Estimated Blood Loss Section (templateId:2.16.840.1.113883.10.20.18.2.9) (CONF:9890).</sch:assert>
      <sch:assert id="a-9892-c" test="count(//cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.28'])=1">SHALL contain exactly one [1..1] Procedure Findings Section (templateId:2.16.840.1.113883.10.20.22.2.28) (CONF:9892).</sch:assert>
      <sch:assert id="a-9894-c" test="count(//cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.31'])=1">SHALL contain exactly one [1..1] Procedure Specimens Taken Section (templateId:2.16.840.1.113883.10.20.22.2.31) (CONF:9894).</sch:assert>
      <sch:assert id="a-9896-c" test="count(//cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.27'])=1">SHALL contain exactly one [1..1] Procedure Description Section (templateId:2.16.840.1.113883.10.20.22.2.27) (CONF:9896).</sch:assert>
      <sch:assert id="a-9913-c" test="count(//cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.35'])=1">SHALL contain exactly one [1..1] Postoperative Diagnosis Section (templateId:2.16.840.1.113883.10.20.22.2.35) (CONF:9913).</sch:assert>
      <sch:assert id="a-10058-c" test="count(cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:high | cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:width) = 1">If a width is not present, the serviceEvent/effectiveTime SHALL include effectiveTime/high (CONF:10058).</sch:assert>
      <sch:assert id="a-10060-c" test="count(cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:high | cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:width) = 1">When only the date and the length of the procedure are known a width element SHALL be present and the serviceEvent/effectiveTime/high SHALL not be present (CONF:10060).</sch:assert>
      <sch:assert id="a-10136-c" test="string-length(cda:documentationOf/cda:serviceEvent/cda:effectiveTime//@value)&gt;=8">The content of effectiveTime SHALL be a conformant US Realm Date and Time (DT.US.FIELDED) (2.16.840.1.113883.10.20.22.5.4) (CONF:10136).</sch:assert>
      <sch:assert id="a-17187" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:17187).</sch:assert>
      <sch:assert id="a-17188" test="cda:code[@code]">This code SHALL contain exactly one [1..1] @code, which SHALL be selected from ValueSet SurgicalOperationNoteDocumentTypeCode 2.16.840.1.113883.11.20.1.1 DYNAMIC (CONF:17188).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.7-errors" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.7']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.7-errors-abstract" />
      <sch:assert id="a-8483" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.1.7'])=1">SHALL contain exactly one [1..1] templateId (CONF:8483) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.1.7" (CONF:10048).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.6-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.6-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-errors-abstract" />
      <sch:assert id="a-8510" test="count(cda:documentationOf[count(cda:serviceEvent[count(cda:performer[@typeCode='PPRF'][count(cda:assignedEntity)=1])=1][count(cda:effectiveTime[count(cda:low)=1])=1])=1]) &gt; 0">SHALL contain at least one [1..*] documentationOf (CONF:8510) such that it SHALL contain exactly one [1..1] serviceEvent (CONF:10061). This serviceEvent SHALL contain exactly one [1..1] performer (CONF:8520). This performer SHALL contain exactly one [1..1] @typeCode="PPRF" Primary Performer (CodeSystem: HL7ParticipationType 2.16.840.1.113883.5.90 STATIC) (CONF:8521). This performer SHALL contain exactly one [1..1] assignedEntity (CONF:14911). This serviceEvent SHALL contain exactly one [1..1] effectiveTime (CONF:10062). This effectiveTime SHALL contain exactly one [1..1] low (CONF:26449).</sch:assert>
      <sch:assert id="a-8511-c" test="cda:documentationOf/cda:serviceEvent/cda:code[@codeSystem='2.16.840.1.113883.6.96' or @codeSystem='2.16.840.1.113883.6.12' or @codeSystem='2.16.840.1.113883.6.104']">The value of Clinical Document /documentationOf/serviceEvent/code SHALL be from ICD9 CM Procedures (codeSystem 2.16.840.1.113883.6.104), CPT-4 (codeSystem 2.16.840.1.113883.6.12), or values descending from 71388002 (Procedure) from the SNOMED CT (codeSystem 2.16.840.1.113883.6.96) ValueSet 2.16.840.1.113883.3.88.12.80.28 Procedure DYNAMIC (CONF:8511).</sch:assert>
      <sch:assert id="a-8513-c" test="cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:low">The serviceEvent/effectiveTime SHALL be present with effectiveTime/low (CONF:8513).</sch:assert>
      <sch:assert id="a-8514-c" test="count(cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:high | cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:width)=1">If a width is not present, the serviceEvent/effectiveTime SHALL include effectiveTime/high (CONF:8514).</sch:assert>
      <sch:assert id="a-8515-c" test="count(cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:high | cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:width)=1">When only the date and the length of the procedure are known a width element SHALL be present and the serviceEvent/effectiveTime/high SHALL not be present (CONF:8515).</sch:assert>
      <sch:assert id="a-9588" test="count(cda:component)=1">SHALL contain exactly one [1..1] component (CONF:9588).</sch:assert>
      <sch:assert id="a-9589-c" test="count(cda:component/cda:structuredBody | cda:component/cda:nonXMLBody) = 1">A Procedure Note can have either a structuredBody or a nonXMLBody (CONF:9589).</sch:assert>
      <sch:assert id="a-9595-c" test="not(testable)">If structuredBody, the component/structuredBody SHALL conform to the section constraints below (CONF:9595).</sch:assert>
      <sch:assert id="a-9643-c" test="(count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=2) or (count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.9'])=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=0)">SHALL include an Assessment and Plan Section, or an Assessment Section and a Plan Section (CONF:9643).</sch:assert>
      <sch:assert id="a-9802-c" test="count(//cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.37'])">SHALL contain exactly one [1..1] Complications Section (templateId:2.16.840.1.113883.10.20.22.2.37) (CONF:9802).</sch:assert>
      <sch:assert id="a-9805-c" test="count(//cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.27'])">SHALL contain exactly one [1..1] Procedure Description Section (templateId:2.16.840.1.113883.10.20.22.2.27) (CONF:9805).</sch:assert>
      <sch:assert id="a-9807-c" test="count(//cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.29'])">SHALL contain exactly one [1..1] Procedure Indications Section (templateId:2.16.840.1.113883.10.20.22.2.29) (CONF:9807).</sch:assert>
      <sch:assert id="a-9850-c" test="count(//cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.36'])">SHALL contain exactly one [1..1] Postprocedure Diagnosis Section (templateId:2.16.840.1.113883.10.20.22.2.36) (CONF:9850).</sch:assert>
      <sch:assert id="a-10063-c" test="string-length(cda:documentationOf/cda:serviceEvent/cda:effectiveTime//@value)&gt;=8">The content of effectiveTime SHALL be a conformant US Realm Date and Time (DT.US.FIELDED) (2.16.840.1.113883.10.20.22.5.4) (CONF:10063).</sch:assert>
      <sch:assert id="a-10064-c" test="count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=2 or (count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.9'])=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=0)">SHALL NOT include an Assessment/Plan Section when an Assessment Section and a Plan of Care Section are present (CONF:10064).</sch:assert>
      <sch:assert id="a-10065-c" test="not(tested)">SHALL NOT include a Chief Complaint and Reason for Visit Section with either a Chief Complaint Section or a Reason for Visit Section (CONF:10065).</sch:assert>
      <sch:assert id="a-17182" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:17182).</sch:assert>
      <sch:assert id="a-17183" test="cda:code[@code]">This code SHALL contain exactly one [1..1] @code, which SHALL be selected from ValueSet ProcedureNoteDocumentTypeCodes 2.16.840.1.113883.11.20.6.1 DYNAMIC (CONF:17183).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.6-errors" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.6']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.6-errors-abstract" />
      <sch:assert id="a-8496" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.1.6'])=1">SHALL contain exactly one [1..1] templateId (CONF:8496) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.1.6" (CONF:10050).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.1.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.1.1-errors-abstract" abstract="true">
      <sch:assert id="a-8530" test="count(cda:entry) &gt; 0">SHALL contain at least one [1..*] entry (CONF:8530).</sch:assert>
      <sch:assert id="a-15456" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15456).</sch:assert>
      <sch:assert id="a-15457" test="cda:code[@code='121181' and @codeSystem='1.2.840.10008.2.16.4']">This code SHALL contain exactly one [1..1] @code="121181" Dicom Object Catalog (CodeSystem: DCM 1.2.840.10008.2.16.4 STATIC) (CONF:15457).</sch:assert>
      <sch:assert id="a-15458" test="cda:entry[count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.6'])=1]">Such entries SHALL contain exactly one [1..1] Study Act (templateId:2.16.840.1.113883.10.20.6.2.6) (CONF:15458).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.1.1-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.6.1.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.1.1-errors-abstract" />
      <sch:assert id="a-8525" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.6.1.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:8525) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.6.1.1" (CONF:10454).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.6-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.6-errors-abstract" abstract="true">
      <sch:assert id="a-9207" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:9207).</sch:assert>
      <sch:assert id="a-9208" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:9208).</sch:assert>
      <sch:assert id="a-9210" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:9210).</sch:assert>
      <sch:assert id="a-9211-c" test="not(cda:id[@extension])">Such ids SHALL NOT contain [0..0] @extension (CONF:9211).</sch:assert>
      <sch:assert id="a-9213" test="cda:id[@root]">The @root contains the OID of the study instance UID since DICOM study ids consist only of an OID
Such ids SHALL contain exactly one [1..1] @root (CONF:9213).</sch:assert>
      <sch:assert id="a-9219" test="count(cda:entryRelationship[@typeCode='COMP'][count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.63'])=1]) &gt; 0">SHALL contain at least one [1..*] entryRelationship (CONF:9219) such that it SHALL contain exactly one [1..1] @typeCode="COMP" Component (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:9220). SHALL contain exactly one [1..1] Series Act (templateId:2.16.840.1.113883.10.20.22.4.63) (CONF:15937).</sch:assert>
      <sch:assert id="a-19172" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19172).</sch:assert>
      <sch:assert id="a-19173" test="cda:code[@code='113014' and @codeSystem='1.2.840.10008.2.16.4']">This code SHALL contain exactly one [1..1] @code="113014" (CodeSystem: DCM 1.2.840.10008.2.16.4 STATIC) (CONF:19173).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.6-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.6']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.6-errors-abstract" />
      <sch:assert id="a-9209" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.6.2.6'])=1">SHALL contain exactly one [1..1] templateId (CONF:9209) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.6.2.6" (CONF:10533).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.1.2-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.1.2-errors-abstract" abstract="true" />
    <sch:rule id="r-2.16.840.1.113883.10.20.6.1.2-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.6.1.2']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.1.2-errors-abstract" />
      <sch:assert id="a-8531" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.6.1.2'])=1">SHALL contain exactly one [1..1] templateId (CONF:8531) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.6.1.2" (CONF:10456).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.38-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.38-errors-abstract" abstract="true">
      <sch:assert id="a-8548" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8548).</sch:assert>
      <sch:assert id="a-8549" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:8549).</sch:assert>
      <sch:assert id="a-8551" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:8551).</sch:assert>
      <sch:assert id="a-8553" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:8553).</sch:assert>
      <sch:assert id="a-8558" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:8558).</sch:assert>
      <sch:assert id="a-19117" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19117).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.38-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.38']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.38-errors-abstract" />
      <sch:assert id="a-8550" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.38'])=1">SHALL contain exactly one [1..1] templateId (CONF:8550) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.38" (CONF:10526).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.39-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.39-errors-abstract" abstract="true">
      <sch:assert id="a-8538" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8538).</sch:assert>
      <sch:assert id="a-8539" test="@moodCode and @moodCode=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.23']/voc:code/@value">SHALL contain exactly one [1..1] @moodCode, which SHALL be selected from ValueSet Plan of Care moodCode (Act/Encounter/Procedure) 2.16.840.1.113883.11.20.9.23 STATIC 2011-09-30 (CONF:8539).</sch:assert>
      <sch:assert id="a-8546" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:8546).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.39-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.39']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.39-errors-abstract" />
      <sch:assert id="a-8544" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.39'])=1">SHALL contain exactly one [1..1] templateId (CONF:8544) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.39" (CONF:10510).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.40-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.40-errors-abstract" abstract="true">
      <sch:assert id="a-8564" test="@classCode='ENC'">SHALL contain exactly one [1..1] @classCode="ENC" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8564).</sch:assert>
      <sch:assert id="a-8565" test="@moodCode and @moodCode=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.23']/voc:code/@value">SHALL contain exactly one [1..1] @moodCode, which SHALL be selected from ValueSet Plan of Care moodCode (Act/Encounter/Procedure) 2.16.840.1.113883.11.20.9.23 STATIC 2011-09-30 (CONF:8565).</sch:assert>
      <sch:assert id="a-8567" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:8567).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.40-errors" context="cda:encounter[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.40']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.40-errors-abstract" />
      <sch:assert id="a-8566" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.40'])=1">SHALL contain exactly one [1..1] templateId (CONF:8566) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.40" (CONF:10511).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.41-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.41-errors-abstract" abstract="true">
      <sch:assert id="a-8568" test="@classCode='PROC'">SHALL contain exactly one [1..1] @classCode="PROC" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8568).</sch:assert>
      <sch:assert id="a-8569" test="@moodCode and @moodCode=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.23']/voc:code/@value">SHALL contain exactly one [1..1] @moodCode, which SHALL be selected from ValueSet Plan of Care moodCode (Act/Encounter/Procedure) 2.16.840.1.113883.11.20.9.23 STATIC 2011-09-30 (CONF:8569).</sch:assert>
      <sch:assert id="a-8571" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:8571).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.41-errors" context="cda:procedure[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.41']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.41-errors-abstract" />
      <sch:assert id="a-8570" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.41'])=1">SHALL contain exactly one [1..1] templateId (CONF:8570) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.41" (CONF:10513).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.42-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.42-errors-abstract" abstract="true">
      <sch:assert id="a-8572" test="@classCode='SBADM'">SHALL contain exactly one [1..1] @classCode="SBADM" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8572).</sch:assert>
      <sch:assert id="a-8573" test="@moodCode and @moodCode=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.24']/voc:code/@value">SHALL contain exactly one [1..1] @moodCode, which SHALL be selected from ValueSet Plan of Care moodCode (SubstanceAdministration/Supply) 2.16.840.1.113883.11.20.9.24 STATIC 2011-09-30 (CONF:8573).</sch:assert>
      <sch:assert id="a-8575" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:8575).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.42-errors" context="cda:substanceAdministration[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.42']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.42-errors-abstract" />
      <sch:assert id="a-8574" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.42'])=1">SHALL contain exactly one [1..1] templateId (CONF:8574) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.42" (CONF:10514).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.43-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.43-errors-abstract" abstract="true">
      <sch:assert id="a-8577" test="@classCode='SPLY'">SHALL contain exactly one [1..1] @classCode="SPLY" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8577).</sch:assert>
      <sch:assert id="a-8578" test="@moodCode and @moodCode=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.24']/voc:code/@value">SHALL contain exactly one [1..1] @moodCode, which SHALL be selected from ValueSet Plan of Care moodCode (SubstanceAdministration/Supply) 2.16.840.1.113883.11.20.9.24 STATIC 2011-09-30 (CONF:8578).</sch:assert>
      <sch:assert id="a-8580" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:8580).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.43-errors" context="cda:supply[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.43']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.43-errors-abstract" />
      <sch:assert id="a-8579" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.43'])=1">SHALL contain exactly one [1..1] templateId (CONF:8579) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.43" (CONF:10515).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.44-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.44-errors-abstract" abstract="true">
      <sch:assert id="a-8581" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8581).</sch:assert>
      <sch:assert id="a-8582" test="@moodCode and @moodCode=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.25']/voc:code/@value">SHALL contain exactly one [1..1] @moodCode, which SHALL be selected from ValueSet Plan of Care moodCode (Observation) 2.16.840.1.113883.11.20.9.25 STATIC 2011-09-30 (CONF:8582).</sch:assert>
      <sch:assert id="a-8584" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:8584).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.44-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.44']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.44-errors-abstract" />
      <sch:assert id="a-8583" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.44'])=1">SHALL contain exactly one [1..1] templateId (CONF:8583) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.44" (CONF:10512).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.46-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.46-errors-abstract" abstract="true">
      <sch:assert id="a-8586" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8586).</sch:assert>
      <sch:assert id="a-8587" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:8587).</sch:assert>
      <sch:assert id="a-8589" test="count(cda:code[@code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.3.88.12.3221.7.2']/voc:code/@value])=1">SHALL contain exactly one [1..1] code, which SHOULD be selected from ValueSet Problem Type 2.16.840.1.113883.3.88.12.3221.7.2 STATIC 2012-06-01 (CONF:8589).</sch:assert>
      <sch:assert id="a-8590" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:8590).</sch:assert>
      <sch:assert id="a-8591" test="count(cda:value[@xsi:type='CD'])=1">SHALL contain exactly one [1..1] value with @xsi:type="CD", where the @code SHALL be selected from ValueSet Problem Value Set 2.16.840.1.113883.3.88.12.3221.7.4 DYNAMIC (CONF:8591).</sch:assert>
      <sch:assert id="a-8592" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:8592).</sch:assert>
      <sch:assert id="a-19098" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19098).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.46-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.46']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.46-errors-abstract" />
      <sch:assert id="a-8599" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.46'])=1">SHALL contain exactly one [1..1] templateId (CONF:8599) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.46" (CONF:10496).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.45-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.45-errors-abstract" abstract="true">
      <sch:assert id="a-8600" test="@classCode='CLUSTER'">SHALL contain exactly one [1..1] @classCode="CLUSTER" Cluster (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8600).</sch:assert>
      <sch:assert id="a-8601" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:8601).</sch:assert>
      <sch:assert id="a-8602" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:8602).</sch:assert>
      <sch:assert id="a-8607" test="count(cda:component) &gt; 0">SHALL contain at least one [1..*] component (CONF:8607).</sch:assert>
      <sch:assert id="a-8609" test="count(cda:subject)=1">SHALL contain exactly one [1..1] subject (CONF:8609).</sch:assert>
      <sch:assert id="a-15244" test="cda:subject[count(cda:relatedSubject)=1]">This subject SHALL contain exactly one [1..1] relatedSubject (CONF:15244).</sch:assert>
      <sch:assert id="a-15245" test="cda:subject/cda:relatedSubject[@classCode='PRS']">This relatedSubject SHALL contain exactly one [1..1] @classCode="PRS" Person (CodeSystem: EntityClass 2.16.840.1.113883.5.41 STATIC) (CONF:15245).</sch:assert>
      <sch:assert id="a-15246" test="cda:subject/cda:relatedSubject[count(cda:code)=1]">This relatedSubject SHALL contain exactly one [1..1] code (CONF:15246).</sch:assert>
      <sch:assert id="a-15247" test="cda:subject/cda:relatedSubject/cda:code[@code]">This code SHALL contain exactly one [1..1] @code, which SHOULD be selected from ValueSet Family Member Value Set 2.16.840.1.113883.1.11.19579 DYNAMIC (CONF:15247).</sch:assert>
      <sch:assert id="a-16888" test="cda:component[count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.46'])=1]">Such components SHALL contain exactly one [1..1] Family History Observation (templateId:2.16.840.1.113883.10.20.22.4.46) (CONF:16888).</sch:assert>
      <sch:assert id="a-19099" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19099).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.45-errors" context="cda:organizer[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.45']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.45-errors-abstract" />
      <sch:assert id="a-8604" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.45'])=1">SHALL contain exactly one [1..1] templateId (CONF:8604) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.45" (CONF:10497).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.47-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.47-errors-abstract" abstract="true">
      <sch:assert id="a-8621" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8621).</sch:assert>
      <sch:assert id="a-8622" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:8622).</sch:assert>
      <sch:assert id="a-8625" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:8625).</sch:assert>
      <sch:assert id="a-8626-c" test="count(cda:value[@code='419099009'][@xsi:type='CD'])=1">SHALL contain exactly one [1..1] value="419099009" Dead with @xsi:type="CD" (CodeSystem: SNOMED-CT 2.16.840.1.113883.6.96 STATIC) (CONF:8626).</sch:assert>
      <sch:assert id="a-19097" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19097).</sch:assert>
      <sch:assert id="a-19141" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19141).</sch:assert>
      <sch:assert id="a-19142" test="cda:code[@code='ASSERTION' and @codeSystem='2.16.840.1.113883.5.4']">This code SHALL contain exactly one [1..1] @code="ASSERTION" Assertion (CodeSystem: ActCode 2.16.840.1.113883.5.4 STATIC) (CONF:19142).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.47-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.47']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.47-errors-abstract" />
      <sch:assert id="a-8623" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.47'])=1">SHALL contain exactly one [1..1] templateId (CONF:8623) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.47" (CONF:10495).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.21.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.21.1-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.21-errors-abstract" />
      <sch:assert id="a-8645" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8645).</sch:assert>
      <sch:assert id="a-8646" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8646).</sch:assert>
      <sch:assert id="a-8647" test="count(cda:entry[count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.48'])=1]) &gt; 0">SHALL contain at least one [1..*] entry (CONF:8647) such that it SHALL contain exactly one [1..1] Advance Directive Observation (templateId:2.16.840.1.113883.10.20.22.4.48) (CONF:15445).</sch:assert>
      <sch:assert id="a-15343" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15343).</sch:assert>
      <sch:assert id="a-15344" test="cda:code[@code='42348-3' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="42348-3" Advance Directives (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15344).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.21.1-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.21.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.21.1-errors-abstract" />
      <sch:assert id="a-8643" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.21.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:8643) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.21.1" (CONF:10377).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.48-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.48-errors-abstract" abstract="true">
      <sch:assert id="a-8648" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8648).</sch:assert>
      <sch:assert id="a-8649" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:8649).</sch:assert>
      <sch:assert id="a-8651" test="count(cda:code[@code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.1.11.20.2']/voc:code/@value])=1">SHALL contain exactly one [1..1] code, which SHOULD be selected from ValueSet AdvanceDirectiveTypeCode 2.16.840.1.113883.1.11.20.2 STATIC 2006-10-17 (CONF:8651).</sch:assert>
      <sch:assert id="a-8652" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:8652).</sch:assert>
      <sch:assert id="a-8654" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:8654).</sch:assert>
      <sch:assert id="a-8656" test="count(cda:effectiveTime)=1">SHALL contain exactly one [1..1] effectiveTime (CONF:8656).</sch:assert>
      <sch:assert id="a-15521" test="cda:effectiveTime[count(cda:high)=1]">This effectiveTime SHALL contain exactly one [1..1] high (CONF:15521).</sch:assert>
      <sch:assert id="a-19082" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19082).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.48-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.48']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.48-errors-abstract" />
      <sch:assert id="a-8655" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.48'])=1">SHALL contain exactly one [1..1] templateId (CONF:8655) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.48" (CONF:10485).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.21.2.3-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.21.2.3-errors-abstract" abstract="true">
      <sch:assert id="a-8682" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8682).</sch:assert>
      <sch:assert id="a-8683" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8683).</sch:assert>
      <sch:assert id="a-15377" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15377).</sch:assert>
      <sch:assert id="a-15378" test="cda:code[@code='62387-6' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="62387-6" Interventions Provided (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15378).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.21.2.3-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.21.2.3']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.21.2.3-errors-abstract" />
      <sch:assert id="a-8680" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.21.2.3'])=1">SHALL contain exactly one [1..1] templateId (CONF:8680) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.21.2.3" (CONF:10461).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.22.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.22.1-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.22-errors-abstract" />
      <sch:assert id="a-8707" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:8707).</sch:assert>
      <sch:assert id="a-8708" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:8708).</sch:assert>
      <sch:assert id="a-8709" test="count(cda:entry[count(cda:encounter[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.49'])=1]) &gt; 0">SHALL contain at least one [1..*] entry (CONF:8709) such that it SHALL contain exactly one [1..1] Encounter Activities (templateId:2.16.840.1.113883.10.20.22.4.49) (CONF:15468).</sch:assert>
      <sch:assert id="a-15466" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15466).</sch:assert>
      <sch:assert id="a-15467" test="cda:code[@code='46240-8' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="46240-8" Encounters (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15467).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.22.1-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.22.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.22.1-errors-abstract" />
      <sch:assert id="a-8705" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.22.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:8705) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.22.1" (CONF:10387).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.49-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.49-errors-abstract" abstract="true">
      <sch:assert id="a-8712" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.49'][@root='2.16.840.1.113883.10.20.22.4.49'])=1">SHALL contain exactly one [1..1] templateId (CONF:8712) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.49" (CONF:10494). SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.49" (CONF:26353).</sch:assert>
      <sch:assert id="a-8710" test="@classCode='ENC'">SHALL contain exactly one [1..1] @classCode="ENC" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8710).</sch:assert>
      <sch:assert id="a-8711" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:8711).</sch:assert>
      <sch:assert id="a-8713" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:8713).</sch:assert>
      <sch:assert id="a-8715" test="count(cda:effectiveTime)=1">SHALL contain exactly one [1..1] effectiveTime (CONF:8715).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.49-errors" context="cda:encounter[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.49']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.49-errors-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.50-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.50-errors-abstract" abstract="true">
      <sch:assert id="a-8745" test="@classCode='SPLY'">SHALL contain exactly one [1..1] @classCode="SPLY" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8745).</sch:assert>
      <sch:assert id="a-8746" test="@moodCode and @moodCode=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.18']/voc:code/@value">SHALL contain exactly one [1..1] @moodCode, which SHALL be selected from ValueSet MoodCodeEvnInt 2.16.840.1.113883.11.20.9.18 STATIC 2011-04-03 (CONF:8746).</sch:assert>
      <sch:assert id="a-8748" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:8748).</sch:assert>
      <sch:assert id="a-8749" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:8749).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.50-errors" context="cda:supply[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.50']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.50-errors-abstract" />
      <sch:assert id="a-8747" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.50'])=1">SHALL contain exactly one [1..1] templateId (CONF:8747) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.50" (CONF:10509).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.51-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.51-errors-abstract" abstract="true">
      <sch:assert id="a-8756" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" (CONF:8756).</sch:assert>
      <sch:assert id="a-8757" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CONF:8757).</sch:assert>
      <sch:assert id="a-8759" test="count(cda:entryRelationship) &gt; 0">SHALL contain at least one [1..*] entryRelationship (CONF:8759).</sch:assert>
      <sch:assert id="a-8760" test="cda:entryRelationship[@typeCode='SUBJ']">Such entryRelationships SHALL contain exactly one [1..1] @typeCode="SUBJ" Has subject (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:8760).</sch:assert>
      <sch:assert id="a-15583" test="cda:entryRelationship[count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.4'])=1]">Such entryRelationships SHALL contain exactly one [1..1] Problem Observation (templateId:2.16.840.1.113883.10.20.22.4.4) (CONF:15583).</sch:assert>
      <sch:assert id="a-19151" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19151).</sch:assert>
      <sch:assert id="a-19152" test="cda:code[@code='59769-0' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="59769-0" Postprocedure diagnosis (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:19152).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.51-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.51']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.51-errors-abstract" />
      <sch:assert id="a-16766" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.51'])=1">SHALL contain exactly one [1..1] templateId (CONF:16766) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.51" (CONF:16767).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.52-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.52-errors-abstract" abstract="true">
      <sch:assert id="a-8826" test="@classCode='SBADM'">SHALL contain exactly one [1..1] @classCode="SBADM" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8826).</sch:assert>
      <sch:assert id="a-8827" test="@moodCode and @moodCode=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.18']/voc:code/@value">SHALL contain exactly one [1..1] @moodCode, which SHALL be selected from ValueSet MoodCodeEvnInt 2.16.840.1.113883.11.20.9.18 STATIC (CONF:8827).</sch:assert>
      <sch:assert id="a-8829" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:8829).</sch:assert>
      <sch:assert id="a-8833" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:8833).</sch:assert>
      <sch:assert id="a-8834" test="count(cda:effectiveTime)=1">SHALL contain exactly one [1..1] effectiveTime (CONF:8834).</sch:assert>
      <sch:assert id="a-8847" test="count(cda:consumable)=1">SHALL contain exactly one [1..1] consumable (CONF:8847).</sch:assert>
      <sch:assert id="a-15546" test="cda:consumable[count(cda:manufacturedProduct[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.54'])=1]">This consumable SHALL contain exactly one [1..1] Immunization Medication Information (templateId:2.16.840.1.113883.10.20.22.4.54) (CONF:15546).</sch:assert>
      <sch:assert id="a-8985" test="@negationInd">Use negationInd="true" to indicate that the immunization was not given.
SHALL contain exactly one [1..1] @negationInd (CONF:8985).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.52-errors" context="cda:substanceAdministration[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.52']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.52-errors-abstract" />
      <sch:assert id="a-8828" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.52'])=1">SHALL contain exactly one [1..1] templateId (CONF:8828) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.52" (CONF:10498).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.60-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.60-errors-abstract" abstract="true">
      <sch:assert id="a-8872" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" Act (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8872).</sch:assert>
      <sch:assert id="a-8873" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:8873).</sch:assert>
      <sch:assert id="a-8874" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:8874).</sch:assert>
      <sch:assert id="a-8875" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:8875).</sch:assert>
      <sch:assert id="a-8876" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:8876).</sch:assert>
      <sch:assert id="a-8878" test="count(cda:entryRelationship[@typeCode='COMP'][count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.61'])=1]) &gt; 0">SHALL contain at least one [1..*] entryRelationship (CONF:8878) such that it SHALL contain exactly one [1..1] @typeCode="COMP" has component (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:8879). SHALL contain exactly one [1..1] Policy Activity (templateId:2.16.840.1.113883.10.20.22.4.61) (CONF:15528).</sch:assert>
      <sch:assert id="a-19094" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19094).</sch:assert>
      <sch:assert id="a-19160" test="cda:code[@code='48768-6' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="48768-6" Payment sources (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:19160).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.60-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.60']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.60-errors-abstract" />
      <sch:assert id="a-8897" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.60'])=1">SHALL contain exactly one [1..1] templateId (CONF:8897) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.60" (CONF:10492).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.61-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.61-errors-abstract" abstract="true">
      <sch:assert id="a-8898" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" Act (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8898).</sch:assert>
      <sch:assert id="a-8899" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:8899).</sch:assert>
      <sch:assert id="a-8901" test="count(cda:id) &gt; 0">This id is a unique identifier for the policy or program providing the coverage
SHALL contain at least one [1..*] id (CONF:8901).</sch:assert>
      <sch:assert id="a-8902" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:8902).</sch:assert>
      <sch:assert id="a-8903" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:8903).</sch:assert>
      <sch:assert id="a-8906" test="count(cda:performer[@typeCode][count(cda:assignedEntity[count(cda:id) &gt; 0])=1][count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.87'])=1])=1">This performer represents the Payer.
SHALL contain exactly one [1..1] performer (CONF:8906) such that it SHALL contain exactly one [1..1] @typeCode (CodeSystem: HL7ParticipationType 2.16.840.1.113883.5.90 STATIC) (CONF:8907). SHALL contain exactly one [1..1] assignedEntity (CONF:8908). This assignedEntity SHALL contain at least one [1..*] id (CONF:8909). SHALL contain exactly one [1..1] templateId (CONF:16808). This templateId SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.87" Payer Performer (CONF:16809).</sch:assert>
      <sch:assert id="a-8916" test="count(cda:participant[@typeCode='COV'][count(cda:participantRole[count(cda:id) &gt; 0][count(cda:code)=1])=1][count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.89'])=1])=1">SHALL contain exactly one [1..1] participant (CONF:8916) such that it SHALL contain exactly one [1..1] @typeCode="COV" Coverage target (CodeSystem: HL7ParticipationType 2.16.840.1.113883.5.90 STATIC) (CONF:8917). SHALL contain exactly one [1..1] participantRole (CONF:8921). This participantRole SHALL contain at least one [1..*] id (CONF:8922). This participantRole SHALL contain exactly one [1..1] code (CONF:8923). SHALL contain exactly one [1..1] templateId (CONF:16812). This templateId SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.89" Covered Party Participant (CONF:16814).</sch:assert>
      <sch:assert id="a-8939" test="count(cda:entryRelationship[@typeCode='REFR']) &gt; 0">SHALL contain at least one [1..*] entryRelationship (CONF:8939) such that it SHALL contain exactly one [1..1] @typeCode="REFR" Refers to (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:8940).</sch:assert>
      <sch:assert id="a-19109" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19109).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.61-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.61']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.61-errors-abstract" />
      <sch:assert id="a-8900" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.61'])=1">SHALL contain exactly one [1..1] templateId (CONF:8900) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.61" (CONF:10516).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.1.19-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.1.19-errors-abstract" abstract="true">
      <sch:assert id="a-8944" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" Act (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8944).</sch:assert>
      <sch:assert id="a-8945" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8945).</sch:assert>
      <sch:assert id="a-8947" test="count(cda:id)=1">SHALL contain exactly one [1..1] id (CONF:8947).</sch:assert>
      <sch:assert id="a-8948" test="count(cda:entryRelationship[@typeCode='SUBJ']) &gt; 0">SHALL contain at least one [1..*] entryRelationship (CONF:8948) such that it SHALL contain exactly one [1..1] @typeCode="SUBJ" Has Subject (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:8949).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.1.19-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.1.19']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.1.19-errors-abstract" />
      <sch:assert id="a-8946" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.1.19'])=1">SHALL contain exactly one [1..1] templateId (CONF:8946) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.1.19" (CONF:10529).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.53-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.53-errors-abstract" abstract="true">
      <sch:assert id="a-8991" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8991).</sch:assert>
      <sch:assert id="a-8992" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:8992).</sch:assert>
      <sch:assert id="a-8994" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:8994).</sch:assert>
      <sch:assert id="a-8995" test="count(cda:code)=1">SHALL contain exactly one [1..1] code, which SHALL be selected from ValueSet No Immunization Reason Value Set 2.16.840.1.113883.1.11.19717 DYNAMIC (CONF:8995).</sch:assert>
      <sch:assert id="a-8996" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:8996).</sch:assert>
      <sch:assert id="a-19104" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19104).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.53-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.53']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.53-errors-abstract" />
      <sch:assert id="a-8993" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.53'])=1">SHALL contain exactly one [1..1] templateId (CONF:8993) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.53" (CONF:10500).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.54-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.54-errors-abstract" abstract="true">
      <sch:assert id="a-9002" test="@classCode='MANU'">SHALL contain exactly one [1..1] @classCode="MANU" (CodeSystem: RoleClass 2.16.840.1.113883.5.110 STATIC) (CONF:9002).</sch:assert>
      <sch:assert id="a-9006" test="count(cda:manufacturedMaterial)=1">SHALL contain exactly one [1..1] manufacturedMaterial (CONF:9006).</sch:assert>
      <sch:assert id="a-9007" test="cda:manufacturedMaterial[count(cda:code)=1]">This manufacturedMaterial SHALL contain exactly one [1..1] code, which SHALL be selected from ValueSet Vaccine Administered Value Set 2.16.840.1.113883.3.88.12.80.22 DYNAMIC (CONF:9007).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.54-errors" context="cda:manufacturedProduct[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.54']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.54-errors-abstract" />
      <sch:assert id="a-9004" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.54'])=1">SHALL contain exactly one [1..1] templateId (CONF:9004) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.54" (CONF:10499).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.2.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.2.1-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.2-errors-abstract" />
      <sch:assert id="a-9017" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:9017).</sch:assert>
      <sch:assert id="a-9018" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:9018).</sch:assert>
      <sch:assert id="a-9019" test="count(cda:entry[count(cda:substanceAdministration[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.52'])=1]) &gt; 0">SHALL contain at least one [1..*] entry (CONF:9019) such that it SHALL contain exactly one [1..1] Immunization Activity (templateId:2.16.840.1.113883.10.20.22.4.52) (CONF:15495).</sch:assert>
      <sch:assert id="a-15369" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15369).</sch:assert>
      <sch:assert id="a-15370" test="cda:code[@code='11369-6' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="11369-6" Immunizations (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15370).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.2.1-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.2.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.2.1-errors-abstract" />
      <sch:assert id="a-9015" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.2.1'])=1">SHALL contain exactly one [1..1] templateId (CONF:9015) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.2.1" (CONF:10400).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.5-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.5-errors-abstract" abstract="true">
      <sch:assert id="a-9057" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:9057).</sch:assert>
      <sch:assert id="a-9072" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:9072).</sch:assert>
      <sch:assert id="a-9074" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:9074).</sch:assert>
      <sch:assert id="a-9075" test="count(cda:value[@xsi:type='CD'])=1">SHALL contain exactly one [1..1] value with @xsi:type="CD", where the @code SHALL be selected from ValueSet HealthStatus 2.16.840.1.113883.1.11.20.12 DYNAMIC (CONF:9075).</sch:assert>
      <sch:assert id="a-19103" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19103).</sch:assert>
      <sch:assert id="a-19143" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19143).</sch:assert>
      <sch:assert id="a-19144" test="cda:code[@code='11323-3' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="11323-3" Health status (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:19144).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.5-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.5']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.5-errors-abstract" />
      <sch:assert id="a-16756" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.5'])=1">SHALL contain exactly one [1..1] templateId (CONF:16756) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.5" (CONF:16757).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.3-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.3-errors-abstract" abstract="true">
      <sch:assert id="a-9190-c" test="count(cda:code[@xsi:type='CD'])=1">SHALL contain exactly one [1..1] code with @xsi:type="CD" (CONF:9190).</sch:assert>
      <sch:assert id="a-9191" test="count(cda:subject)=1">SHALL contain exactly one [1..1] subject (CONF:9191).</sch:assert>
      <sch:assert id="a-15347" test="cda:subject[count(cda:name)=1]">The name element is used to store the DICOM fetus ID, typically a pseudonym such as fetus_1.
This subject SHALL contain exactly one [1..1] name (CONF:15347).</sch:assert>
      <sch:assert id="a-19129-c" test="@code='121026'">This code SHALL contain exactly one [1..1] @code="121026" (CodeSystem: DCM 1.2.840.10008.2.16.4 STATIC) (CONF:19129).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.3-errors" context="cda:relatedSubject[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.3']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.3-errors-abstract" />
      <sch:assert id="a-9189" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.6.2.3'])=1">SHALL contain exactly one [1..1] templateId (CONF:9189) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.6.2.3" (CONF:10535).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.4-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.4-errors-abstract" abstract="true">
      <sch:assert id="a-9196" test="count(cda:id) &gt; 0">The id element contains the author's id or the DICOM device observer UID
SHALL contain at least one [1..*] id (CONF:9196).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.4-errors" context="cda:assignedAuthor[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.4']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.4-errors-abstract" />
      <sch:assert id="a-9194" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.6.2.4'])=1">SHALL contain exactly one [1..1] templateId (CONF:9194) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.6.2.4" (CONF:10536).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.5-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.5-errors-abstract" abstract="true">
      <sch:assert id="a-9201" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:9201).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.5-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.5']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.5-errors-abstract" />
      <sch:assert id="a-9200" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.6.2.5'])=1">SHALL contain exactly one [1..1] templateId (CONF:9200) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.6.2.5" (CONF:10530).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.63-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.63-errors-abstract" abstract="true">
      <sch:assert id="a-9222" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" Act (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:9222).</sch:assert>
      <sch:assert id="a-9223" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:9223).</sch:assert>
      <sch:assert id="a-9224" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:9224).</sch:assert>
      <sch:assert id="a-9225" test="cda:id[@root]">The @root contains the OID of the study instance UID since DICOM study ids consist only of an OID
Such ids SHALL contain exactly one [1..1] @root (CONF:9225).</sch:assert>
      <sch:assert id="a-9226-c" test="not(cda:id[@extension])">Such ids SHALL NOT contain [0..0] @extension (CONF:9226).</sch:assert>
      <sch:assert id="a-9237" test="count(cda:entryRelationship[@typeCode='COMP'][count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.8'])=1]) &gt; 0">SHALL contain at least one [1..*] entryRelationship (CONF:9237) such that it SHALL contain exactly one [1..1] @typeCode="COMP" Component (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:9238). SHALL contain exactly one [1..1] SOP Instance Observation (templateId:2.16.840.1.113883.10.20.6.2.8) (CONF:15927).</sch:assert>
      <sch:assert id="a-19166" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19166).</sch:assert>
      <sch:assert id="a-19167" test="cda:code[@code='113015' and @codeSystem='1.2.840.10008.2.16.4']">This code SHALL contain exactly one [1..1] @code="113015" (CodeSystem: DCM 1.2.840.10008.2.16.4 STATIC) (CONF:19167).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.63-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.63']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.63-errors-abstract" />
      <sch:assert id="a-10918" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.63'])=1">SHALL contain exactly one [1..1] templateId (CONF:10918) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.63" (CONF:10919).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.8-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.8-errors-abstract" abstract="true">
      <sch:assert id="a-9240" test="@classCode='DGIMG'">SHALL contain exactly one [1..1] @classCode="DGIMG" Diagnostic Image (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:9240).</sch:assert>
      <sch:assert id="a-9241" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:9241).</sch:assert>
      <sch:assert id="a-9242" test="count(cda:id) &gt; 0">The @root contains an OID representing the DICOM SOP Instance UID
SHALL contain at least one [1..*] id (CONF:9242).</sch:assert>
      <sch:assert id="a-9244" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:9244).</sch:assert>
      <sch:assert id="a-19225" test="cda:code[@code]">This code SHALL contain exactly one [1..1] @code (CONF:19225).</sch:assert>
      <sch:assert id="a-19226-c" test=".">@code is an OID for a valid SOP class name UID (CONF:19226).</sch:assert>
      <sch:assert id="a-19227" test="cda:code[@codeSystem='1.2.840.10008.2.6.1']">This code SHALL contain exactly one [1..1] @codeSystem="1.2.840.10008.2.6.1" DCMUID (CONF:19227).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.8-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.8']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.8-errors-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.9-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.9-errors-abstract" abstract="true">
      <sch:assert id="a-9264" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:9264).</sch:assert>
      <sch:assert id="a-9265" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:9265).</sch:assert>
      <sch:assert id="a-9267" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:9267).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.9-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.9']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.9-errors-abstract" />
      <sch:assert id="a-9266" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.6.2.9'])=1">SHALL contain exactly one [1..1] templateId (CONF:9266) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.6.2.9" (CONF:10531).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.10-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.10-errors-abstract" abstract="true">
      <sch:assert id="a-9276" test="@classCode='ROIBND'">SHALL contain exactly one [1..1] @classCode="ROIBND" Bounded Region of Interest (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:9276).</sch:assert>
      <sch:assert id="a-9277" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:9277).</sch:assert>
      <sch:assert id="a-9279" test="count(cda:entryRelationship)=1">SHALL contain exactly one [1..1] entryRelationship (CONF:9279).</sch:assert>
      <sch:assert id="a-9280" test="cda:entryRelationship[@typeCode='COMP']">This entryRelationship SHALL contain exactly one [1..1] @typeCode="COMP" Component (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:9280).</sch:assert>
      <sch:assert id="a-15923" test="cda:entryRelationship[count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.11'])=1]">This entryRelationship SHALL contain exactly one [1..1] Boundary Observation (templateId:2.16.840.1.113883.10.20.6.2.11) (CONF:15923).</sch:assert>
      <sch:assert id="a-19164" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19164).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.10-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.10']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.10-errors-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.11-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.11-errors-abstract" abstract="true">
      <sch:assert id="a-9282" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:9282).</sch:assert>
      <sch:assert id="a-9283" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:9283).</sch:assert>
      <sch:assert id="a-9284" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:9284).</sch:assert>
      <sch:assert id="a-9285" test="count(cda:value[@xsi:type='INT']) &gt; 0">Each number represents a frame for display.
SHALL contain at least one [1..*] value with @xsi:type="INT" (CONF:9285).</sch:assert>
      <sch:assert id="a-19157" test="cda:code[@code='113036' and @codeSystem='1.2.840.10008.2.16.4']">This code SHALL contain exactly one [1..1] @code="113036" Frames for Display (CodeSystem: DCM 1.2.840.10008.2.16.4 STATIC) (CONF:19157).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.11-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.11']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.11-errors-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.12-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.12-errors-abstract" abstract="true">
      <sch:assert id="a-9288" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: ActCode 2.16.840.1.113883.5.4 STATIC) (CONF:9288).</sch:assert>
      <sch:assert id="a-9289" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:9289).</sch:assert>
      <sch:assert id="a-9291" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:9291).</sch:assert>
      <sch:assert id="a-9292" test="count(cda:value[@xsi:type='ED'])=1">SHALL contain exactly one [1..1] value with @xsi:type="ED" (CONF:9292).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.12-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.12']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.12-errors-abstract" />
      <sch:assert id="a-9290" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.6.2.12'])=1">SHALL contain exactly one [1..1] templateId (CONF:9290) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.6.2.12" (CONF:10534).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.13-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.13-errors-abstract" abstract="true">
      <sch:assert id="a-9304" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:9304).</sch:assert>
      <sch:assert id="a-9305" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:9305).</sch:assert>
      <sch:assert id="a-9308" test="count(cda:value)=1">SHALL contain exactly one [1..1] value (CONF:9308).</sch:assert>
      <sch:assert id="a-15523" test="count(cda:templateId)=1">SHALL contain exactly one [1..1] templateId (CONF:15523).</sch:assert>
      <sch:assert id="a-19181" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19181).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.13-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.13']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.13-errors-abstract" />
      <sch:assert id="a-15524" test="cda:templateId[@root='2.16.840.1.113883.10.20.6.2.13']">This templateId SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.6.2.13" (CONF:15524).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.14-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.14-errors-abstract" abstract="true">
      <sch:assert id="a-9317" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:9317).</sch:assert>
      <sch:assert id="a-9318" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:9318).</sch:assert>
      <sch:assert id="a-9320" test="count(cda:code)=1">The value set of the observation/code includes numeric measurement types for linear dimensions, areas, volumes, and other numeric measurements. This value set is extensible and comprises the union of SNOMED codes for observable entities as reproduced in DIRQuantityMeasurementTypeCodes (ValueSet: 2.16.840.1.113883.11.20.9.29) and DICOM Codes in DICOMQuantityMeasurementTypeCodes (ValueSet: 2.16.840.1.113883.11.20.9.30).
SHALL contain exactly one [1..1] code (CONF:9320).</sch:assert>
      <sch:assert id="a-9324" test="count(cda:value[@xsi:type='PQ'])=1">SHALL contain exactly one [1..1] value with @xsi:type="PQ" (CONF:9324).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.14-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.14']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.14-errors-abstract" />
      <sch:assert id="a-9319" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.6.2.14'])=1">SHALL contain exactly one [1..1] templateId (CONF:9319) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.6.2.14" (CONF:10532).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.5.1.1-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.1.1-errors-abstract" abstract="true">
      <sch:assert id="a-9368" test="count(cda:name)=1">SHALL contain exactly one [1..1] name (CONF:9368).</sch:assert>
      <sch:assert id="a-9371-c" test="cda:given|cda:family or (count(*)=0 and string-length(.)!=0)">The content of name SHALL be either a conformant Patient Name (PTN.US.FIELDED), or a string (CONF:9371).</sch:assert>
      <sch:assert id="a-9372-c" test="cda:given|cda:family or (count(*)=0 and string-length(.)!=0)">The string SHALL NOT contain name parts (CONF:9372).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.1.1-errors" context="cda:PN[cda:templateId/@root='2.16.840.1.113883.10.20.22.5.1.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.5.1.1-errors-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.64-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.64-errors-abstract" abstract="true">
      <sch:assert id="a-9425" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" Act (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:9425).</sch:assert>
      <sch:assert id="a-9426" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:9426).</sch:assert>
      <sch:assert id="a-9428" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:9428).</sch:assert>
      <sch:assert id="a-9430" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:9430).</sch:assert>
      <sch:assert id="a-9431-c" test="not(tested-here)">This text SHALL contain exactly one [1..1] reference/@value (CONF:9431).</sch:assert>
      <sch:assert id="a-15967" test="cda:text[count(cda:reference)=1]">This text SHALL contain exactly one [1..1] reference (CONF:15967).</sch:assert>
      <sch:assert id="a-15968" test="cda:text/cda:reference[@value]">This reference SHALL contain exactly one [1..1] @value (CONF:15968).</sch:assert>
      <sch:assert id="a-15969-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15969).</sch:assert>
      <sch:assert id="a-19159" test="cda:code[@code='48767-8' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="48767-8" Annotation Comment (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:19159).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.64-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.64']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.64-errors-abstract" />
      <sch:assert id="a-9427" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.64'])=1">SHALL contain exactly one [1..1] templateId (CONF:9427) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.64" (CONF:10491).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.41-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.41-errors-abstract" abstract="true">
      <sch:assert id="a-9921" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:9921).</sch:assert>
      <sch:assert id="a-9922" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:9922).</sch:assert>
      <sch:assert id="a-15357" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15357).</sch:assert>
      <sch:assert id="a-15358" test="cda:code[@code='8653-8' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="8653-8" Hospital Discharge Instructions (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15358).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.41-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.41']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.41-errors-abstract" />
      <sch:assert id="a-9919" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.41'])=1">SHALL contain exactly one [1..1] templateId (CONF:9919) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.41" (CONF:10395).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.42-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.42-errors-abstract" abstract="true">
      <sch:assert id="a-9917" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:9917).</sch:assert>
      <sch:assert id="a-9918" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:9918).</sch:assert>
      <sch:assert id="a-15485" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15485).</sch:assert>
      <sch:assert id="a-15486" test="cda:code[@code='18841-7' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="18841-7" Hospital Consultations Section (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15486).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.42-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.42']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.42-errors-abstract" />
      <sch:assert id="a-9915" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.42'])=1">SHALL contain exactly one [1..1] templateId (CONF:9915) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.42" (CONF:10393).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.43-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.43-errors-abstract" abstract="true">
      <sch:assert id="a-9932" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:9932).</sch:assert>
      <sch:assert id="a-9933" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:9933).</sch:assert>
      <sch:assert id="a-15479" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15479).</sch:assert>
      <sch:assert id="a-15480" test="cda:code[@code='46241-6' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="46241-6" Hospital Admission Diagnosis (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15480).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.43-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.43']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.43-errors-abstract" />
      <sch:assert id="a-9930" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.43'])=1">SHALL contain exactly one [1..1] templateId (CONF:9930) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.43" (CONF:10391).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.5.3-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.3-errors-abstract" abstract="true">
      <sch:assert id="a-10078-c" test="string-length(@value) &gt;= 8">SHALL be precise to the day (CONF:10078).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.3-errors" context="cda:IVL_TS[cda:templateId/@root='2.16.840.1.113883.10.20.22.5.3']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.5.3-errors-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.65-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.65-errors-abstract" abstract="true">
      <sch:assert id="a-10090" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" (CONF:10090).</sch:assert>
      <sch:assert id="a-10091" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CONF:10091).</sch:assert>
      <sch:assert id="a-10093" test="count(cda:entryRelationship) &gt; 0">SHALL contain at least one [1..*] entryRelationship (CONF:10093).</sch:assert>
      <sch:assert id="a-10094" test="cda:entryRelationship[@typeCode='SUBJ']">Such entryRelationships SHALL contain exactly one [1..1] @typeCode="SUBJ" Has subject (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:10094).</sch:assert>
      <sch:assert id="a-15605" test="cda:entryRelationship[count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.4'])=1]">Such entryRelationships SHALL contain exactly one [1..1] Problem Observation (templateId:2.16.840.1.113883.10.20.22.4.4) (CONF:15605).</sch:assert>
      <sch:assert id="a-19155" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19155).</sch:assert>
      <sch:assert id="a-19156" test="cda:code[@code='10219-4' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="10219-4" Preoperative Diagnosis (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:19156).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.65-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.65']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.65-errors-abstract" />
      <sch:assert id="a-16770" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.65'])=1">SHALL contain exactly one [1..1] templateId (CONF:16770) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.65" (CONF:16771).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.44-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.44-errors-abstract" abstract="true">
      <sch:assert id="a-10100" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:10100).</sch:assert>
      <sch:assert id="a-10101" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:10101).</sch:assert>
      <sch:assert id="a-15482" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15482).</sch:assert>
      <sch:assert id="a-15483" test="cda:code[@code='42346-7' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="42346-7" Medications on Admission (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15483).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.44-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.44']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.44-errors-abstract" />
      <sch:assert id="a-10098" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.44'])=1">SHALL contain exactly one [1..1] templateId (CONF:10098) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.44" (CONF:10392).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.45-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.45-errors-abstract" abstract="true">
      <sch:assert id="a-10114" test="count(cda:title)=1">SHALL contain exactly one [1..1] title (CONF:10114).</sch:assert>
      <sch:assert id="a-10115" test="count(cda:text)=1">SHALL contain exactly one [1..1] text (CONF:10115).</sch:assert>
      <sch:assert id="a-15375" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:15375).</sch:assert>
      <sch:assert id="a-15376" test="cda:code[@code='69730-0' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="69730-0" Instructions (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:15376).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.45-errors" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.45']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.45-errors-abstract" />
      <sch:assert id="a-10112" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.2.45'])=1">SHALL contain exactly one [1..1] templateId (CONF:10112) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.2.45" (CONF:10402).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.5.4-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.4-errors-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.4-errors" context="cda:TS[cda:templateId/@root='2.16.840.1.113883.10.20.22.5.4']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.5.4-errors-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.67-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.67-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.2-errors-abstract" />
      <sch:assert id="a-13905" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:13905).</sch:assert>
      <sch:assert id="a-13906" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:13906).</sch:assert>
      <sch:assert id="a-13907" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:13907).</sch:assert>
      <sch:assert id="a-13908-c" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:13908).</sch:assert>
      <sch:assert id="a-13929" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:13929).</sch:assert>
      <sch:assert id="a-13930" test="count(cda:effectiveTime)=1">Represents clinically effective time of the measurement, which may be when the measurement was performed (e.g., a BP measurement), or may be when sample was taken (and measured some time afterwards)
SHALL contain exactly one [1..1] effectiveTime (CONF:13930).</sch:assert>
      <sch:assert id="a-13932" test="count(cda:value)=1">SHALL contain exactly one [1..1] value (CONF:13932).</sch:assert>
      <sch:assert id="a-19101" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19101).</sch:assert>
      <sch:assert id="a-26448" test="cda:code[not(@code and @codeSystem='2.16.840.1.113883.6.1') or @code and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain zero or one [0..1] @code, which SHOULD be selected from CodeSystem LOINC (2.16.840.1.113883.6.1) STATIC (CONF:26448).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.67-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.67']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.67-errors-abstract" />
      <sch:assert id="a-13889" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.67'])=1">SHALL contain exactly one [1..1] templateId (CONF:13889) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.67" (CONF:13890).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.72-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.72-errors-abstract" abstract="true">
      <sch:assert id="a-14219" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:14219).</sch:assert>
      <sch:assert id="a-14220" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14220).</sch:assert>
      <sch:assert id="a-14223" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:14223).</sch:assert>
      <sch:assert id="a-14227" test="count(cda:participant) &gt; 0">SHALL contain at least one [1..*] participant (CONF:14227).</sch:assert>
      <sch:assert id="a-14228" test="cda:participant[count(cda:participantRole)=1]">Such participants SHALL contain exactly one [1..1] participantRole (CONF:14228).</sch:assert>
      <sch:assert id="a-14229" test="cda:participant/cda:participantRole[@classCode='CAREGIVER']">This participantRole SHALL contain exactly one [1..1] @classCode="CAREGIVER" (CONF:14229).</sch:assert>
      <sch:assert id="a-14230" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:14230).</sch:assert>
      <sch:assert id="a-14233" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:14233).</sch:assert>
      <sch:assert id="a-14599" test="count(cda:value)=1">SHALL contain exactly one [1..1] value (CONF:14599).</sch:assert>
      <sch:assert id="a-14600-c" test="cda:value[@codeSystem='2.16.840.1.113883.6.1' or @codeSystem='2.16.840.1.113883.6.96']">Where the @code SHALL be selected from LOINC (codeSystem: 2.16.840.1.113883.6.1) or SNOMED CT (CodeSystem: 2.16.840.1.113883.6.96) (CONF:14600).</sch:assert>
      <sch:assert id="a-19090" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19090).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.72-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.72']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.72-errors-abstract" />
      <sch:assert id="a-14221" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.72'])=1">SHALL contain exactly one [1..1] templateId (CONF:14221) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.72" (CONF:14222).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.74-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.74-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.2-errors-abstract" />
      <sch:assert id="a-14249" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:14249).</sch:assert>
      <sch:assert id="a-14250" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14250).</sch:assert>
      <sch:assert id="a-14254" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:14254).</sch:assert>
      <sch:assert id="a-14257" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:14257).</sch:assert>
      <sch:assert id="a-14261" test="count(cda:effectiveTime)=1">Represents clinically effective time of the measurement, which may be the time the measurement was performed (e.g., a BP measurement), or may be the time the sample was taken (and measured some time afterwards).
SHALL contain exactly one [1..1] effectiveTime (CONF:14261).</sch:assert>
      <sch:assert id="a-14263" test="count(cda:value)=1">SHALL contain exactly one [1..1] value (CONF:14263).</sch:assert>
      <sch:assert id="a-14591" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:14591).</sch:assert>
      <sch:assert id="a-19092" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19092).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.74-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.74']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.74-errors-abstract" />
      <sch:assert id="a-14255" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.74'])=1">SHALL contain exactly one [1..1] templateId (CONF:14255) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.74" (CONF:14256).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.68-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.68-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.4-errors-abstract" />
      <sch:assert id="a-14282" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:14282).</sch:assert>
      <sch:assert id="a-14283" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14283).</sch:assert>
      <sch:assert id="a-14284" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:14284).</sch:assert>
      <sch:assert id="a-14286" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:14286).</sch:assert>
      <sch:assert id="a-14291" test="count(cda:value[@xsi:type='CD'])=1">SHALL contain exactly one [1..1] value with @xsi:type="CD", where the @code SHOULD be selected from ValueSet Problem Value Set 2.16.840.1.113883.3.88.12.3221.7.4 DYNAMIC (CONF:14291).</sch:assert>
      <sch:assert id="a-14314" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:14314).</sch:assert>
      <sch:assert id="a-19100" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19100).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.68-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.68']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.68-errors-abstract" />
      <sch:assert id="a-14312" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.68'])=1">SHALL contain exactly one [1..1] templateId (CONF:14312) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.68" (CONF:14313).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.73-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.73-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.4-errors-abstract" />
      <sch:assert id="a-14319" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:14319).</sch:assert>
      <sch:assert id="a-14320" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14320).</sch:assert>
      <sch:assert id="a-14321" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:14321).</sch:assert>
      <sch:assert id="a-14323" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:14323).</sch:assert>
      <sch:assert id="a-14349" test="count(cda:value[@xsi:type='CD'])=1">SHALL contain exactly one [1..1] value with @xsi:type="CD", where the @code SHOULD be selected from ValueSet Problem Value Set 2.16.840.1.113883.3.88.12.3221.7.4 DYNAMIC (CONF:14349).</sch:assert>
      <sch:assert id="a-14804" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:14804).</sch:assert>
      <sch:assert id="a-19091" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19091).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.73-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.73']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.73-errors-abstract" />
      <sch:assert id="a-14346" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.73'])=1">SHALL contain exactly one [1..1] templateId (CONF:14346) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.73" (CONF:14347).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.66-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.66-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.1-errors-abstract" />
      <sch:assert id="a-14355" test="@classCode='CLUSTER'">SHALL contain exactly one [1..1] @classCode="CLUSTER", which SHALL be selected from CodeSystem HL7ActClass (2.16.840.1.113883.5.6) STATIC (CONF:14355).</sch:assert>
      <sch:assert id="a-14357" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14357).</sch:assert>
      <sch:assert id="a-14358" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:14358).</sch:assert>
      <sch:assert id="a-14359" test="count(cda:component[count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.67'])=1]) &gt; 0">SHALL contain at least one [1..*] component (CONF:14359) such that it SHALL contain exactly one [1..1] Functional Status Result Observation (templateId:2.16.840.1.113883.10.20.22.4.67) (CONF:14368).</sch:assert>
      <sch:assert id="a-14363" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:14363).</sch:assert>
      <sch:assert id="a-14364" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:14364).</sch:assert>
      <sch:assert id="a-19102" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19102).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.66-errors" context="cda:organizer[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.66']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.66-errors-abstract" />
      <sch:assert id="a-14361" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.66'])=1">SHALL contain exactly one [1..1] templateId (CONF:14361) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.66" (CONF:14362).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.75-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.75-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.1-errors-abstract" />
      <sch:assert id="a-14369" test="@classCode='CLUSTER'">SHALL contain exactly one [1..1] @classCode="CLUSTER", which SHALL be selected from CodeSystem HL7ActClass (2.16.840.1.113883.5.6) STATIC (CONF:14369).</sch:assert>
      <sch:assert id="a-14371" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14371).</sch:assert>
      <sch:assert id="a-14372" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:14372).</sch:assert>
      <sch:assert id="a-14373" test="count(cda:component[count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.74'])=1]) &gt; 0">SHALL contain at least one [1..*] component (CONF:14373) such that it SHALL contain exactly one [1..1] Cognitive Status Result Observation (templateId:2.16.840.1.113883.10.20.22.4.74) (CONF:14381).</sch:assert>
      <sch:assert id="a-14377" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:14377).</sch:assert>
      <sch:assert id="a-14378" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:14378).</sch:assert>
      <sch:assert id="a-19093" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19093).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.75-errors" context="cda:organizer[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.75']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.75-errors-abstract" />
      <sch:assert id="a-14375" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.75'])=1">SHALL contain exactly one [1..1] templateId (CONF:14375) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.75" (CONF:14376).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.70-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.70-errors-abstract" abstract="true">
      <sch:assert id="a-14383" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:14383).</sch:assert>
      <sch:assert id="a-14384" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14384).</sch:assert>
      <sch:assert id="a-14389" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:14389).</sch:assert>
      <sch:assert id="a-14394" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:14394).</sch:assert>
      <sch:assert id="a-14395" test="count(cda:effectiveTime)=1">SHALL contain exactly one [1..1] effectiveTime (CONF:14395).</sch:assert>
      <sch:assert id="a-14396" test="count(cda:value[@xsi:type='CD' and @code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.35']/voc:code/@value])=1">SHALL contain exactly one [1..1] value with @xsi:type="CD", where the @code SHOULD be selected from ValueSet Pressure Ulcer Stage 2.16.840.1.113883.11.20.9.35 STATIC (CONF:14396).</sch:assert>
      <sch:assert id="a-14759" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:14759).</sch:assert>
      <sch:assert id="a-14760" test="cda:code[@code='ASSERTION' and @codeSystem='2.16.840.1.113883.5.4']">This code SHALL contain exactly one [1..1] @code="ASSERTION" Assertion (CodeSystem: ActCode 2.16.840.1.113883.5.4 STATIC) (CONF:14760).</sch:assert>
      <sch:assert id="a-19111" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19111).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.70-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.70']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.70-errors-abstract" />
      <sch:assert id="a-14387" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.70'])=1">SHALL contain exactly one [1..1] templateId (CONF:14387) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.70" (CONF:14388).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.69-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.69-errors-abstract" abstract="true">
      <sch:assert id="a-14434" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:14434).</sch:assert>
      <sch:assert id="a-14435" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14435).</sch:assert>
      <sch:assert id="a-14438" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:14438).</sch:assert>
      <sch:assert id="a-14439" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:14439).</sch:assert>
      <sch:assert id="a-14444" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:14444).</sch:assert>
      <sch:assert id="a-14445" test="count(cda:effectiveTime)=1">Represents clinically effective time of the measurement, which may be when the measurement was performed (e.g., a BP measurement), or may be when sample was taken (and measured some time afterwards)
SHALL contain exactly one [1..1] effectiveTime (CONF:14445).</sch:assert>
      <sch:assert id="a-14450" test="count(cda:value)=1">SHALL contain exactly one [1..1] value (CONF:14450).</sch:assert>
      <sch:assert id="a-19088" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19088).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.69-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.69']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.69-errors-abstract" />
      <sch:assert id="a-14436" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.69'])=1">SHALL contain exactly one [1..1] templateId (CONF:14436) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.69" (CONF:14437).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.76-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.76-errors-abstract" abstract="true">
      <sch:assert id="a-14705" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:14705).</sch:assert>
      <sch:assert id="a-14706" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14706).</sch:assert>
      <sch:assert id="a-14709" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:14709).</sch:assert>
      <sch:assert id="a-14714" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:14714).</sch:assert>
      <sch:assert id="a-14715" test="count(cda:effectiveTime)=1">SHALL contain exactly one [1..1] effectiveTime (CONF:14715).</sch:assert>
      <sch:assert id="a-14718" test="count(cda:entryRelationship)=1">SHALL contain exactly one [1..1] entryRelationship (CONF:14718).</sch:assert>
      <sch:assert id="a-14719" test="cda:entryRelationship[@typeCode='SUBJ']">This entryRelationship SHALL contain exactly one [1..1] @typeCode="SUBJ" Has subject (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:14719).</sch:assert>
      <sch:assert id="a-14720" test="cda:entryRelationship[count(cda:observation)=1]">This entryRelationship SHALL contain exactly one [1..1] observation (CONF:14720).</sch:assert>
      <sch:assert id="a-14721" test="cda:entryRelationship/cda:observation[@classCode='OBS']">This observation SHALL contain exactly one [1..1] @classCode="OBS" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:14721).</sch:assert>
      <sch:assert id="a-14722" test="cda:entryRelationship/cda:observation[@moodCode='EVN']">This observation SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14722).</sch:assert>
      <sch:assert id="a-14725" test="cda:entryRelationship/cda:observation[count(cda:value[@xsi:type='CD' and @code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.35']/voc:code/@value])=1]">This observation SHALL contain exactly one [1..1] value with @xsi:type="CD", where the @code SHOULD be selected from ValueSet Pressure Ulcer Stage 2.16.840.1.113883.11.20.9.35 STATIC (CONF:14725).</sch:assert>
      <sch:assert id="a-14767" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:14767).</sch:assert>
      <sch:assert id="a-14768" test="cda:code[@code='2264892003']">This code SHALL contain exactly one [1..1] @code="2264892003" number of pressure ulcers (CONF:14768).</sch:assert>
      <sch:assert id="a-14771" test="count(cda:value[@xsi:type='INT'])=1">SHALL contain exactly one [1..1] value with @xsi:type="INT" (CONF:14771).</sch:assert>
      <sch:assert id="a-19108" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19108).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.76-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.76']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.76-errors-abstract" />
      <sch:assert id="a-14707" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.76'])=1">SHALL contain exactly one [1..1] templateId (CONF:14707) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.76" (CONF:14708).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.77-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.77-errors-abstract" abstract="true">
      <sch:assert id="a-14726" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:14726).</sch:assert>
      <sch:assert id="a-14727" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14727).</sch:assert>
      <sch:assert id="a-14730" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:14730).</sch:assert>
      <sch:assert id="a-14731" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:14731).</sch:assert>
      <sch:assert id="a-14732" test="cda:code[@code='420905001' and @codeSystem='2.16.840.1.113883.6.96']">This code SHALL contain exactly one [1..1] @code="420905001" Highest Pressure Ulcer Stage (CodeSystem: SNOMED-CT 2.16.840.1.113883.6.96 STATIC) (CONF:14732).</sch:assert>
      <sch:assert id="a-14733" test="count(cda:value)=1">SHALL contain exactly one [1..1] value (CONF:14733).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.77-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.77']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.77-errors-abstract" />
      <sch:assert id="a-14728" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.77'])=1">SHALL contain exactly one [1..1] templateId (CONF:14728) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.77" (CONF:14729).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.85-errors">
    <!--Pattern is used in an implied relationship.-->
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.85-errors-abstract" abstract="true">
      <sch:assert id="a-16558" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:16558).</sch:assert>
      <sch:assert id="a-16559" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:16559).</sch:assert>
      <sch:assert id="a-16561" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:16561).</sch:assert>
      <sch:assert id="a-16562" test="count(cda:value[@xsi:type='CD'])=1">SHALL contain exactly one [1..1] value with @xsi:type="CD" (CONF:16562).</sch:assert>
      <sch:assert id="a-16563" test="cda:value[@xsi:type='CD'][@code]">This value SHALL contain exactly one [1..1] @code, which SHALL be selected from ValueSet Tobacco Use 2.16.840.1.113883.11.20.9.41 DYNAMIC (CONF:16563).</sch:assert>
      <sch:assert id="a-16564" test="count(cda:effectiveTime)=1">SHALL contain exactly one [1..1] effectiveTime (CONF:16564).</sch:assert>
      <sch:assert id="a-16565" test="cda:effectiveTime[count(cda:low)=1]">This effectiveTime SHALL contain exactly one [1..1] low (CONF:16565).</sch:assert>
      <sch:assert id="a-19118" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19118).</sch:assert>
      <sch:assert id="a-19174" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19174).</sch:assert>
      <sch:assert id="a-19175" test="cda:code[@code='ASSERTION' and @codeSystem='2.16.840.1.113883.5.4']">This code SHALL contain exactly one [1..1] @code="ASSERTION" Assertion (CodeSystem: ActCode 2.16.840.1.113883.5.4 STATIC) (CONF:19175).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.85-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.85']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.85-errors-abstract" />
      <sch:assert id="a-16566" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.85'])=1">SHALL contain exactly one [1..1] templateId (CONF:16566) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.85" (CONF:16567).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.78-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.78-errors-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.85-errors-abstract" />
      <sch:assert id="a-14806" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:14806).</sch:assert>
      <sch:assert id="a-14807" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14807).</sch:assert>
      <sch:assert id="a-14809" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:14809).</sch:assert>
      <sch:assert id="a-14810" test="count(cda:value[@xsi:type='CD'])=1">SHALL contain exactly one [1..1] value with @xsi:type="CD" (CONF:14810).</sch:assert>
      <sch:assert id="a-14814" test="count(cda:effectiveTime)=1">SHALL contain exactly one [1..1] effectiveTime (CONF:14814).</sch:assert>
      <sch:assert id="a-14817" test="cda:value[@xsi:type='CD'][@code and @code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.38']/voc:code/@value]">This value SHALL contain exactly one [1..1] @code, which SHALL be selected from ValueSet Smoking Status 2.16.840.1.113883.11.20.9.38 STATIC (CONF:14817).</sch:assert>
      <sch:assert id="a-14818" test="cda:effectiveTime[count(cda:low)=1]">This effectiveTime SHALL contain exactly one [1..1] low (CONF:14818).</sch:assert>
      <sch:assert id="a-19116" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19116).</sch:assert>
      <sch:assert id="a-19170" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19170).</sch:assert>
      <sch:assert id="a-19171" test="cda:code[@code='ASSERTION' and @codeSystem='2.16.840.1.113883.5.4']">This code SHALL contain exactly one [1..1] @code="ASSERTION" Assertion (CodeSystem: ActCode 2.16.840.1.113883.5.4 STATIC) (CONF:19171).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.78-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.78']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.78-errors-abstract" />
      <sch:assert id="a-14815" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.78'])=1">SHALL contain exactly one [1..1] templateId (CONF:14815) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.78" (CONF:14816).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.79-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.79-errors-abstract" abstract="true">
      <sch:assert id="a-14851" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:14851).</sch:assert>
      <sch:assert id="a-14852" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14852).</sch:assert>
      <sch:assert id="a-14853" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:14853).</sch:assert>
      <sch:assert id="a-14854" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:14854).</sch:assert>
      <sch:assert id="a-14855" test="count(cda:effectiveTime)=1">SHALL contain exactly one [1..1] effectiveTime (CONF:14855).</sch:assert>
      <sch:assert id="a-14857" test="count(cda:value[@xsi:type='CD'])=1">SHALL contain exactly one [1..1] value with @xsi:type="CD" (CONF:14857).</sch:assert>
      <sch:assert id="a-14873" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:14873).</sch:assert>
      <sch:assert id="a-14874" test="cda:effectiveTime[count(cda:low)=1]">This effectiveTime SHALL contain exactly one [1..1] low (CONF:14874).</sch:assert>
      <sch:assert id="a-15142" test="cda:value[@xsi:type='CD'][@code='419099009']">This value SHALL contain exactly one [1..1] @code="419099009" Dead (CodeSystem: SNOMED-CT 2.16.840.1.113883.6.96 STATIC) (CONF:15142).</sch:assert>
      <sch:assert id="a-19095" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19095).</sch:assert>
      <sch:assert id="a-19135" test="cda:code[@code='ASSERTION' and @codeSystem='2.16.840.1.113883.5.4']">This code SHALL contain exactly one [1..1] @code="ASSERTION" Assertion (CodeSystem: ActCode 2.16.840.1.113883.5.4 STATIC) (CONF:19135).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.79-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.79']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.79-errors-abstract" />
      <sch:assert id="a-14871" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.79'])=1">SHALL contain exactly one [1..1] templateId (CONF:14871) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.79" (CONF:14872).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.80-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.80-errors-abstract" abstract="true">
      <sch:assert id="a-14889" test="@classCode='ACT'">SHALL contain exactly one [1..1] @classCode="ACT" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:14889).</sch:assert>
      <sch:assert id="a-14890" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14890).</sch:assert>
      <sch:assert id="a-14892" test="count(cda:entryRelationship[@typeCode='SUBJ'][count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.4'])=1]) &gt; 0">SHALL contain at least one [1..*] entryRelationship (CONF:14892) such that it SHALL contain exactly one [1..1] @typeCode="SUBJ" (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:14893). SHALL contain exactly one [1..1] Problem Observation (templateId:2.16.840.1.113883.10.20.22.4.4) (CONF:14898).</sch:assert>
      <sch:assert id="a-19182" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19182).</sch:assert>
      <sch:assert id="a-19183" test="cda:code[@code='29308-4' and @codeSystem='2.16.840.1.113883.6.1']">This code SHALL contain exactly one [1..1] @code="29308-4" Diagnosis (CodeSystem: LOINC 2.16.840.1.113883.6.1 STATIC) (CONF:19183).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.80-errors" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.80']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.80-errors-abstract" />
      <sch:assert id="a-14895" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.80'])=1">SHALL contain exactly one [1..1] templateId (CONF:14895) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.80" (CONF:14896).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.86-errors">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.86-errors-abstract" abstract="true">
      <sch:assert id="a-16715" test="@classCode='OBS'">SHALL contain exactly one [1..1] @classCode="OBS" Observation (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:16715).</sch:assert>
      <sch:assert id="a-16716" test="@moodCode='EVN'">SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:16716).</sch:assert>
      <sch:assert id="a-16720" test="count(cda:statusCode)=1">SHALL contain exactly one [1..1] statusCode (CONF:16720).</sch:assert>
      <sch:assert id="a-16724" test="count(cda:id) &gt; 0">SHALL contain at least one [1..*] id (CONF:16724).</sch:assert>
      <sch:assert id="a-16754" test="count(cda:value) &gt; 0">SHALL contain at least one [1..*] value (CONF:16754).</sch:assert>
      <sch:assert id="a-19089" test="cda:statusCode[@code='completed']">This statusCode SHALL contain exactly one [1..1] @code="completed" Completed (CodeSystem: ActStatus 2.16.840.1.113883.5.14 STATIC) (CONF:19089).</sch:assert>
      <sch:assert id="a-19178" test="count(cda:code)=1">SHALL contain exactly one [1..1] code (CONF:19178).</sch:assert>
      <sch:assert id="a-19179" test="cda:code[@code]">This code SHALL contain exactly one [1..1] @code (CONF:19179).</sch:assert>
      <sch:assert id="a-19180-c" test="count(cda:code[@codeSystem])=0 or cda:code[@codeSystem='2.16.840.1.113883.6.1'] or cda:code[@codeSystem='2.16.840.1.113883.6.96']">Such that the @code SHALL be from LOINC (CodeSystem: 2.16.840.1.113883.6.1) or SNOMED CT (CodeSystem: 2.16.840.1.113883.6.96) and represents components of the scale (CONF:19180).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.86-errors" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.86']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.86-errors-abstract" />
      <sch:assert id="a-16722" test="count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.86'])=1">SHALL contain exactly one [1..1] templateId (CONF:16722) such that it SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.86" (CONF:16723).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.15.3.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.15.3.1-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.15.3.1-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.15.3.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.15.3.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.15.3.8-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.15.3.8-warnings-abstract" abstract="true">
      <sch:assert id="a-2018" test="count(cda:effectiveTime)=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:2018).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.15.3.8-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.15.3.8']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.15.3.8-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.1-warnings-abstract" abstract="true">
      <sch:assert id="a-5255-c" test=".">Can either be a locally defined name or the display name corresponding to clinicalDocument/code (CONF:5255).</sch:assert>
      <sch:assert id="a-5300-c" test="string-length(cda:recordTarget/cda:patientRole/cda:patient/cda:birthTime/@value) &gt;= 8">SHOULD be precise to day (CONF:5300).</sch:assert>
      <sch:assert id="a-5303" test="cda:recordTarget/cda:patientRole/cda:patient[count(cda:maritalStatusCode)=1]">This patient SHOULD contain zero or one [0..1] maritalStatusCode, which SHALL be selected from ValueSet Marital Status Value Set 2.16.840.1.113883.1.11.12212 DYNAMIC (CONF:5303).</sch:assert>
      <sch:assert id="a-5326" test="not(cda:recordTarget/cda:patientRole/cda:patient/cda:guardian) or cda:recordTarget/cda:patientRole/cda:patient/cda:guardian[count(cda:code)=1]">The guardian, if present, SHOULD contain zero or one [0..1] code, which SHALL be selected from ValueSet PersonalRelationshipRoleType 2.16.840.1.113883.1.11.19563 DYNAMIC (CONF:5326).</sch:assert>
      <sch:assert id="a-5359" test="not(cda:recordTarget/cda:patientRole/cda:patient/cda:guardian) or cda:recordTarget/cda:patientRole/cda:patient/cda:guardian[count(cda:addr) &gt; 0]">The guardian, if present, SHOULD contain zero or more [0..*] addr (CONF:5359).</sch:assert>
      <sch:assert id="a-5375" test="cda:recordTarget/cda:patientRole/cda:telecom[@use]">Such telecoms SHOULD contain zero or one [0..1] @use, which SHALL be selected from ValueSet Telecom Use (US Realm Header) 2.16.840.1.113883.11.20.9.20 DYNAMIC (CONF:5375).</sch:assert>
      <sch:assert id="a-5385" test="not(cda:recordTarget/cda:patientRole/cda:patient/cda:guardian) or cda:recordTarget/cda:patientRole/cda:patient/cda:guardian[count(cda:guardianPerson)=1]">The guardian, if present, SHALL contain exactly one [1..1] guardianPerson (CONF:5385).</sch:assert>
      <sch:assert id="a-5386" test="not(cda:recordTarget/cda:patientRole/cda:patient/cda:guardian/cda:guardianPerson) or cda:recordTarget/cda:patientRole/cda:patient/cda:guardian/cda:guardianPerson[count(cda:name) &gt; 0]">This guardianPerson SHALL contain at least one [1..*] name (CONF:5386).</sch:assert>
      <sch:assert id="a-5396" test="not(cda:recordTarget/cda:patientRole/cda:patient/cda:birthplace) or cda:recordTarget/cda:patientRole/cda:patient/cda:birthplace[count(cda:place)=1]">The birthplace, if present, SHALL contain exactly one [1..1] place (CONF:5396).</sch:assert>
      <sch:assert id="a-5397" test="not(cda:recordTarget/cda:patientRole/cda:patient/cda:birthplace/cda:place) or cda:recordTarget/cda:patientRole/cda:patient/cda:birthplace/cda:place[count(cda:addr)=1]">This place SHALL contain exactly one [1..1] addr (CONF:5397).</sch:assert>
      <sch:assert id="a-5402-c" test="(cda:recordTarget/cda:patientRole/cda:addr[cda:country='US' or cda:country='USA']) and cda:recordTarget/cda:patientRole/cda:addr/cda:state">If country is US, this addr SHALL contain exactly one [1..1] state, which SHALL be selected from ValueSet 2.16.840.1.113883.3.88.12.80.1 StateValueSet DYNAMIC (CONF:5402).</sch:assert>
      <sch:assert id="a-5404" test="not(cda:recordTarget/cda:patientRole/cda:patient/cda:birthplace/cda:place/cda:addr) or cda:recordTarget/cda:patientRole/cda:patient/cda:birthplace/cda:place/cda:addr[count(cda:country)=1]">This addr SHOULD contain zero or one [0..1] country, which SHALL be selected from ValueSet CountryValueSet 2.16.840.1.113883.3.88.12.80.63 DYNAMIC (CONF:5404).</sch:assert>
      <sch:assert id="a-5406" test="cda:recordTarget/cda:patientRole/cda:patient[count(cda:languageCommunication) &gt; 0]">This patient SHOULD contain zero or more [0..*] languageCommunication (CONF:5406).</sch:assert>
      <sch:assert id="a-5407" test="not(cda:recordTarget/cda:patientRole/cda:patient/cda:languageCommunication) or cda:recordTarget/cda:patientRole/cda:patient/cda:languageCommunication[count(cda:languageCode)=1]">The languageCommunication, if present, SHALL contain exactly one [1..1] languageCode, which SHALL be selected from ValueSet Language 2.16.840.1.113883.1.11.11526 DYNAMIC (CONF:5407).</sch:assert>
      <sch:assert id="a-5417" test="not(cda:recordTarget/cda:patientRole/cda:providerOrganization) or cda:recordTarget/cda:patientRole/cda:providerOrganization[count(cda:id) &gt; 0]">The providerOrganization, if present, SHALL contain at least one [1..*] id (CONF:5417).</sch:assert>
      <sch:assert id="a-5419" test="not(cda:recordTarget/cda:patientRole/cda:providerOrganization) or cda:recordTarget/cda:patientRole/cda:providerOrganization[count(cda:name) &gt; 0]">The providerOrganization, if present, SHALL contain at least one [1..*] name (CONF:5419).</sch:assert>
      <sch:assert id="a-5420" test="not(cda:recordTarget/cda:patientRole/cda:providerOrganization) or cda:recordTarget/cda:patientRole/cda:providerOrganization[count(cda:telecom) &gt; 0]">The providerOrganization, if present, SHALL contain at least one [1..*] telecom (CONF:5420).</sch:assert>
      <sch:assert id="a-5422" test="not(cda:recordTarget/cda:patientRole/cda:providerOrganization) or cda:recordTarget/cda:patientRole/cda:providerOrganization[count(cda:addr) &gt; 0]">The providerOrganization, if present, SHALL contain at least one [1..*] addr (CONF:5422).</sch:assert>
      <sch:assert id="a-5430-c" test="not(tested-here)">This assignedAuthor SHOULD contain zero or one [0..1] assignedPerson (CONF:5430).</sch:assert>
      <sch:assert id="a-5442" test="not(cda:dataEnterer) or cda:dataEnterer[count(cda:assignedEntity)=1]">The dataEnterer, if present, SHALL contain exactly one [1..1] assignedEntity (CONF:5442).</sch:assert>
      <sch:assert id="a-5443" test="not(cda:dataEnterer/cda:assignedEntity) or cda:dataEnterer/cda:assignedEntity[count(cda:id) &gt; 0]">This assignedEntity SHALL contain at least one [1..*] id (CONF:5443).</sch:assert>
      <sch:assert id="a-5460" test="not(cda:dataEnterer/cda:assignedEntity) or cda:dataEnterer/cda:assignedEntity[count(cda:addr) &gt; 0]">This assignedEntity SHALL contain at least one [1..*] addr (CONF:5460).</sch:assert>
      <sch:assert id="a-5466" test="not(cda:dataEnterer/cda:assignedEntity) or cda:dataEnterer/cda:assignedEntity[count(cda:telecom) &gt; 0]">This assignedEntity SHALL contain at least one [1..*] telecom (CONF:5466).</sch:assert>
      <sch:assert id="a-5469" test="not(cda:dataEnterer/cda:assignedEntity) or cda:dataEnterer/cda:assignedEntity[count(cda:assignedPerson)=1]">This assignedEntity SHALL contain exactly one [1..1] assignedPerson (CONF:5469).</sch:assert>
      <sch:assert id="a-5470" test="not(cda:dataEnterer/cda:assignedEntity/cda:assignedPerson) or cda:dataEnterer/cda:assignedEntity/cda:assignedPerson[count(cda:name) &gt; 0]">This assignedPerson SHALL contain at least one [1..*] name (CONF:5470).</sch:assert>
      <sch:assert id="a-5566" test="not(cda:informationRecipient) or cda:informationRecipient[count(cda:intendedRecipient)=1]">The informationRecipient, if present, SHALL contain exactly one [1..1] intendedRecipient (CONF:5566).</sch:assert>
      <sch:assert id="a-5568" test="not(cda:informationRecipient/cda:intendedRecipient/cda:informationRecipient) or cda:informationRecipient/cda:intendedRecipient/cda:informationRecipient[count(cda:name) &gt; 0]">The informationRecipient, if present, SHALL contain at least one [1..*] name (CONF:5568).</sch:assert>
      <sch:assert id="a-5578" test="not(cda:informationRecipient/cda:intendedRecipient/cda:receivedOrganization) or cda:informationRecipient/cda:intendedRecipient/cda:receivedOrganization[count(cda:name)=1]">The receivedOrganization, if present, SHALL contain exactly one [1..1] name (CONF:5578).</sch:assert>
      <sch:assert id="a-5579" test="count(cda:legalAuthenticator)=1">SHOULD contain zero or one [0..1] legalAuthenticator (CONF:5579).</sch:assert>
      <sch:assert id="a-5580" test="not(cda:legalAuthenticator) or cda:legalAuthenticator[count(cda:time)=1]">The legalAuthenticator, if present, SHALL contain exactly one [1..1] time (CONF:5580).</sch:assert>
      <sch:assert id="a-5583" test="not(cda:legalAuthenticator) or cda:legalAuthenticator[count(cda:signatureCode)=1]">The legalAuthenticator, if present, SHALL contain exactly one [1..1] signatureCode (CONF:5583).</sch:assert>
      <sch:assert id="a-5584" test="not(cda:legalAuthenticator/cda:signatureCode) or cda:legalAuthenticator/cda:signatureCode[@code='S']">This signatureCode SHALL contain exactly one [1..1] @code="S" (CodeSystem: Participationsignature 2.16.840.1.113883.5.89 STATIC) (CONF:5584).</sch:assert>
      <sch:assert id="a-5585" test="not(cda:legalAuthenticator) or cda:legalAuthenticator[count(cda:assignedEntity)=1]">The legalAuthenticator, if present, SHALL contain exactly one [1..1] assignedEntity (CONF:5585).</sch:assert>
      <sch:assert id="a-5586" test="not(cda:legalAuthenticator/cda:assignedEntity) or cda:legalAuthenticator/cda:assignedEntity[count(cda:id) &gt; 0]">This assignedEntity SHALL contain at least one [1..*] id (CONF:5586).</sch:assert>
      <sch:assert id="a-5589" test="not(cda:legalAuthenticator/cda:assignedEntity) or cda:legalAuthenticator/cda:assignedEntity[count(cda:addr) &gt; 0]">This assignedEntity SHALL contain at least one [1..*] addr (CONF:5589).</sch:assert>
      <sch:assert id="a-5595" test="not(cda:legalAuthenticator/cda:assignedEntity) or cda:legalAuthenticator/cda:assignedEntity[count(cda:telecom) &gt; 0]">This assignedEntity SHALL contain at least one [1..*] telecom (CONF:5595).</sch:assert>
      <sch:assert id="a-5597" test="not(cda:legalAuthenticator/cda:assignedEntity) or cda:legalAuthenticator/cda:assignedEntity[count(cda:assignedPerson)=1]">This assignedEntity SHALL contain exactly one [1..1] assignedPerson (CONF:5597).</sch:assert>
      <sch:assert id="a-5598" test="not(cda:legalAuthenticator/cda:assignedEntity/cda:assignedPerson) or cda:legalAuthenticator/cda:assignedEntity/cda:assignedPerson[count(cda:name) &gt; 0]">This assignedPerson SHALL contain at least one [1..*] name (CONF:5598).</sch:assert>
      <sch:assert id="a-5608" test="not(cda:authenticator) or cda:authenticator[count(cda:time)=1]">The authenticator, if present, SHALL contain exactly one [1..1] time (CONF:5608).</sch:assert>
      <sch:assert id="a-5610" test="not(cda:authenticator) or cda:authenticator[count(cda:signatureCode)=1]">The authenticator, if present, SHALL contain exactly one [1..1] signatureCode (CONF:5610).</sch:assert>
      <sch:assert id="a-5611" test="not(cda:authenticator/cda:signatureCode) or cda:authenticator/cda:signatureCode[@code='S']">This signatureCode SHALL contain exactly one [1..1] @code="S" (CodeSystem: Participationsignature 2.16.840.1.113883.5.89 STATIC) (CONF:5611).</sch:assert>
      <sch:assert id="a-5612" test="not(cda:authenticator) or cda:authenticator[count(cda:assignedEntity)=1]">The authenticator, if present, SHALL contain exactly one [1..1] assignedEntity (CONF:5612).</sch:assert>
      <sch:assert id="a-5613" test="not(cda:authenticator/cda:assignedEntity) or cda:authenticator/cda:assignedEntity[count(cda:id) &gt; 0]">This assignedEntity SHALL contain at least one [1..*] id (CONF:5613).</sch:assert>
      <sch:assert id="a-5616" test="not(cda:authenticator/cda:assignedEntity) or cda:authenticator/cda:assignedEntity[count(cda:addr) &gt; 0]">This assignedEntity SHALL contain at least one [1..*] addr (CONF:5616).</sch:assert>
      <sch:assert id="a-5622" test="not(cda:authenticator/cda:assignedEntity) or cda:authenticator/cda:assignedEntity[count(cda:telecom) &gt; 0]">This assignedEntity SHALL contain at least one [1..*] telecom (CONF:5622).</sch:assert>
      <sch:assert id="a-5624" test="not(cda:authenticator/cda:assignedEntity) or cda:authenticator/cda:assignedEntity[count(cda:assignedPerson)=1]">This assignedEntity SHALL contain exactly one [1..1] assignedPerson (CONF:5624).</sch:assert>
      <sch:assert id="a-5625" test="not(cda:authenticator/cda:assignedEntity/cda:assignedPerson) or cda:authenticator/cda:assignedEntity/cda:assignedPerson[count(cda:name) &gt; 0]">This assignedPerson SHALL contain at least one [1..*] name (CONF:5625).</sch:assert>
      <sch:assert id="a-7993" test="not(cda:recordTarget/cda:patientRole/cda:patient/cda:guardian/cda:telecom) or cda:recordTarget/cda:patientRole/cda:patient/cda:guardian/cda:telecom[@use]">The telecom, if present, SHOULD contain zero or one [0..1] @use, which SHALL be selected from ValueSet Telecom Use (US Realm Header) 2.16.840.1.113883.11.20.9.20 DYNAMIC (CONF:7993).</sch:assert>
      <sch:assert id="a-7994" test="not(cda:recordTarget/cda:patientRole/cda:providerOrganization/cda:telecom) or cda:recordTarget/cda:patientRole/cda:providerOrganization/cda:telecom[@use]">Such telecoms SHOULD contain zero or one [0..1] @use, which SHALL be selected from ValueSet Telecom Use (US Realm Header) 2.16.840.1.113883.11.20.9.20 DYNAMIC (CONF:7994).</sch:assert>
      <sch:assert id="a-7995" test="cda:author/cda:assignedAuthor/cda:telecom[@use]">Such telecoms SHOULD contain zero or one [0..1] @use, which SHALL be selected from ValueSet Telecom Use (US Realm Header) 2.16.840.1.113883.11.20.9.20 DYNAMIC (CONF:7995).</sch:assert>
      <sch:assert id="a-7996" test="not(cda:dataEnterer/cda:assignedEntity/cda:telecom) or cda:dataEnterer/cda:assignedEntity/cda:telecom[@use]">Such telecoms SHOULD contain zero or one [0..1] @use, which SHALL be selected from ValueSet Telecom Use (US Realm Header) 2.16.840.1.113883.11.20.9.20 DYNAMIC (CONF:7996).</sch:assert>
      <sch:assert id="a-7998" test="cda:custodian/cda:assignedCustodian/cda:representedCustodianOrganization/cda:telecom[@use]">This telecom SHOULD contain zero or one [0..1] @use, which SHALL be selected from ValueSet Telecom Use (US Realm Header) 2.16.840.1.113883.11.20.9.20 DYNAMIC (CONF:7998).</sch:assert>
      <sch:assert id="a-7999" test="not(cda:legalAuthenticator/cda:assignedEntity/cda:telecom) or cda:legalAuthenticator/cda:assignedEntity/cda:telecom[@use]">Such telecoms SHOULD contain zero or one [0..1] @use, which SHALL be selected from ValueSet Telecom Use (US Realm Header) 2.16.840.1.113883.11.20.9.20 DYNAMIC (CONF:7999).</sch:assert>
      <sch:assert id="a-8000" test="not(cda:authenticator/cda:assignedEntity/cda:telecom) or cda:authenticator/cda:assignedEntity/cda:telecom[@use]">Such telecoms SHOULD contain zero or one [0..1] @use, which SHALL be selected from ValueSet Telecom Use (US Realm Header) 2.16.840.1.113883.11.20.9.20 DYNAMIC (CONF:8000).</sch:assert>
      <sch:assert id="a-8002-c" test="not(cda:informant) or cda:informant[cda:assignedEntity or cda:relatedEntity]">The informant, if present, SHALL contain exactly one [1..1] assignedEntity (CONF:8002).</sch:assert>
      <sch:assert id="a-8220-c" test="not(informant) or informant/cda:*/addr">This assignedEntity SHALL contain at least one [1..*] addr (CONF:8220).</sch:assert>
      <sch:assert id="a-8221-c" test="not(cda:informant) or cda:informant[cda:assignedEntity/cda:assignedPerson or cda:relatedEntity/cda:relatedPerson]">This assignedEntity SHALL contain exactly one [1..1] assignedPerson (CONF:8221).</sch:assert>
      <sch:assert id="a-8222-c" test="not(cda:informant) or cda:informant/cda:assignedEntity/cda:assignedPerson/cda:name or cda:informant/cda:relatedEntity/cda:relatedPerson/cda:name">This assignedPerson SHALL contain at least one [1..*] name (CONF:8222).</sch:assert>
      <sch:assert id="a-9945" test="not(cda:informant/cda:assignedEntity) or cda:informant/cda:assignedEntity[count(cda:id) &gt; 0]">This assignedEntity SHALL contain at least one [1..*] id (CONF:9945).</sch:assert>
      <sch:assert id="a-9946-c" test=".">If assignedEntity/id is a provider then this id, SHOULD include zero or one [0..1] id where id/@root ="2.16.840.1.113883.4.6" National Provider Identifier (CONF:9946).</sch:assert>
      <sch:assert id="a-9953" test="not(cda:inFulfillmentOf) or cda:inFulfillmentOf[count(cda:order)=1]">The inFulfillmentOf, if present, SHALL contain exactly one [1..1] order (CONF:9953).</sch:assert>
      <sch:assert id="a-9954" test="not(cda:inFulfillmentOf/cda:order) or cda:inFulfillmentOf/cda:order[count(cda:id) &gt; 0]">This order SHALL contain at least one [1..*] id (CONF:9954).</sch:assert>
      <sch:assert id="a-9956" test="not(cda:componentOf) or cda:componentOf[count(cda:encompassingEncounter)=1]">The componentOf, if present, SHALL contain exactly one [1..1] encompassingEncounter (CONF:9956).</sch:assert>
      <sch:assert id="a-9958" test="not(cda:componentOf/cda:encompassingEncounter) or cda:componentOf/cda:encompassingEncounter[count(cda:effectiveTime)=1]">This encompassingEncounter SHALL contain exactly one [1..1] effectiveTime (CONF:9958).</sch:assert>
      <sch:assert id="a-9959" test="not(cda:componentOf/cda:encompassingEncounter) or cda:componentOf/cda:encompassingEncounter[count(cda:id) &gt; 0]">This encompassingEncounter SHALL contain at least one [1..*] id (CONF:9959).</sch:assert>
      <sch:assert id="a-9965" test="not(cda:recordTarget/cda:patientRole/cda:patient/cda:languageCommunication) or cda:recordTarget/cda:patientRole/cda:patient/cda:languageCommunication[count(cda:proficiencyLevelCode)=1]">The languageCommunication, if present, SHOULD contain zero or one [0..1] proficiencyLevelCode, which SHALL be selected from ValueSet LanguageAbilityProficiency 2.16.840.1.113883.1.11.12199 DYNAMIC (CONF:9965).</sch:assert>
      <sch:assert id="a-9991-c" test=".">This id SHALL be a globally unique identifier for the document (CONF:9991).</sch:assert>
      <sch:assert id="a-9992-c" test=".">This code SHALL specify the particular kind of document (e.g. History and Physical, Discharge Summary, Progress Note) (CONF:9992).</sch:assert>
      <sch:assert id="a-10007-c" test=".">Unless otherwise specified by the document specific header constraints, when participant/@typeCode is IND, associatedEntity/@classCode SHALL be selected from ValueSet 2.16.840.1.113883.11.20.9.33 INDRoleclassCodes STATIC 2011-09-30 (CONF:10007).</sch:assert>
      <sch:assert id="a-10413-c" test="cda:recordTarget/cda:patientRole/cda:patient/cda:guardian/cda:addr[cda:streetAddressLine and cda:city and ((not(cda:country) or cda:country!='US') or (cda:country='US' and cda:state and cda:postalCode))]">The content of addr SHALL be a conformant US Realm Address (AD.US.FIELDED) (2.16.840.1.113883.10.20.22.5.2) (CONF:10413).</sch:assert>
      <sch:assert id="a-10414-c" test="(count(cda:recordTarget/cda:patientRole/cda:patient/cda:guardian/cda:guardianPerson/cda:name/cda:given) &gt; 0 and cda:recordTarget/cda:patientRole/cda:patient/cda:guardian/cda:guardianPerson/cda:name/cda:family) or (count(cda:recordTarget/cda:patientRole/cda:patient/cda:guardian/cda:guardianPerson/cda:name[*])=0 and string-length(cda:recordTarget/cda:patientRole/cda:patient/cda:guardian/cda:guardianPerson/cda:name)!=0)">The content of name SHALL be a conformant US Realm Person Name (PN.US.FIELDED) (2.16.840.1.113883.10.20.22.5.1.1) (CONF:10414).</sch:assert>
      <sch:assert id="a-10415-c" test="cda:recordTarget/cda:patientRole/cda:providerOrganization/cda:addr[cda:streetAddressLine and cda:city and ((not(cda:country) or cda:country!='US') or (cda:country='US' and cda:state and cda:postalCode))]">The content of addr SHALL be a conformant US Realm Address (AD.US.FIELDED) (2.16.840.1.113883.10.20.22.5.2) (CONF:10415).</sch:assert>
      <sch:assert id="a-10417-c" test="not(cda:dataEnterer) or cda:dataEnterer/cda:assignedEntity/cda:addr[cda:streetAddressLine and cda:city and ((not(cda:country) or cda:country!='US') or (cda:country='US' and cda:state and cda:postalCode))]">The content of addr SHALL be a conformant US Realm Address (AD.US.FIELDED) (2.16.840.1.113883.10.20.22.5.2) (CONF:10417).</sch:assert>
      <sch:assert id="a-10418-c" test="not(cda:dataEnterer) or (count(cda:dataEnterer/cda:assignedEntity/cda:assignedPerson/cda:name/cda:given) &gt; 0 and cda:dataEnterer/cda:assignedEntity/cda:assignedPerson/cda:name/cda:family) or (count(cda:dataEnterer/cda:assignedEntity/cda:assignedPerson/cda:name[*])=0 and string-length(cda:dataEnterer/cda:assignedEntity/cda:assignedPerson/cda:name)!=0)">The content of name SHALL be a conformant US Realm Person Name (PN.US.FIELDED) (2.16.840.1.113883.10.20.22.5.1.1) (CONF:10418).</sch:assert>
      <sch:assert id="a-10419-c" test="not(cda:informant) or cda:informant/cda:*/cda:addr[cda:streetAddressLine and cda:city and ((not(cda:country) or cda:country!='US') or (cda:country='US' and cda:state and cda:postalCode))]">The content of addr SHALL be a conformant US Realm Address (AD.US.FIELDED) (2.16.840.1.113883.10.20.22.5.2) (CONF:10419).</sch:assert>
      <sch:assert id="a-10420-c" test="not(cda:informant) or (count(cda:informant/cda:assignedEntity/cda:assignedPerson/cda:name/cda:given) &gt; 0 and cda:informant/cda:assignedEntity/cda:assignedPerson/cda:name/cda:family) or (count(cda:informant/cda:assignedEntity/cda:assignedPerson/cda:name[*])=0 and string-length(cda:informant/cda:assignedEntity/cda:assignedPerson/cda:name)!=0)">The content of name SHALL be a conformant US Realm Person Name (PN.US.FIELDED) (2.16.840.1.113883.10.20.22.5.1.1) (CONF:10420).</sch:assert>
      <sch:assert id="a-10424-c" test="not(cda:authenticator) or (count(cda:authenticator/cda:assignedEntity/cda:assignedPerson/cda:name/cda:given) &gt; 0 and cda:authenticator/cda:assignedEntity/cda:assignedPerson/cda:name/cda:family)&#xA;or&#xA;(count(cda:authenticator/cda:assignedEntity/cda:assignedPerson/cda:name/cda:given) &gt; 0 and cda:authenticator/cda:assignedEntity/cda:assignedPerson/cda:name/cda:family) or (count(cda:authenticator/cda:assignedEntity/cda:assignedPerson/cda:name[*])=0 and string-length(cda:authenticator/cda:assignedEntity/cda:assignedPerson/cda:name)!=0)">The content of name SHALL be a conformant US Realm Person Name (PN.US.FIELDED) (2.16.840.1.113883.10.20.22.5.1.1) (CONF:10424).</sch:assert>
      <sch:assert id="a-10425-c" test="not(cda:authenticator) or cda:authenticator/cda:assignedEntity/cda:addr[cda:streetAddressLine and cda:city and ((not(cda:country) or cda:country!='US') or (cda:country='US' and cda:state and cda:postalCode))]">The content of addr SHALL be a conformant US Realm Address (AD.US.FIELDED) (2.16.840.1.113883.10.20.22.5.2) (CONF:10425).</sch:assert>
      <sch:assert id="a-10427-c" test="not(cda:informationRecipient/cda:intendedRecipient/cda:informationRecipient) or (count(cda:informationRecipient/cda:intendedRecipient/cda:informationRecipient/cda:name/cda:given) &gt; 0 and cda:informationRecipient/cda:intendedRecipient/cda:informationRecipient/cda:name/cda:family)&#xA;or&#xA;(count(cda:informationRecipient/cda:intendedRecipient/cda:informationRecipient/cda:name/cda:given) &gt; 0 and cda:informationRecipient/cda:intendedRecipient/cda:informationRecipient/cda:name/cda:family) or (count(cda:informationRecipient/cda:intendedRecipient/cda:informationRecipient/cda:name[*])=0 and string-length(cda:informationRecipient/cda:intendedRecipient/cda:informationRecipient/cda:name)!=0)">The content of name SHALL be a conformant US Realm Person Name (PN.US.FIELDED) (2.16.840.1.113883.10.20.22.5.1.1) (CONF:10427).</sch:assert>
      <sch:assert id="a-10429-c" test="not(cda:legalAuthenticator) or cda:legalAuthenticator/cda:assignedEntity/cda:addr[cda:streetAddressLine and cda:city and ((not(cda:country) or cda:country!='US') or (cda:country='US' and cda:state and cda:postalCode))]">The content of addr SHALL be a conformant US Realm Address (AD.US.FIELDED) (2.16.840.1.113883.10.20.22.5.2) (CONF:10429).</sch:assert>
      <sch:assert id="a-10430-c" test="not(cda:legalAuthenticator) or (count(cda:legalAuthenticator/cda:assignedEntity/cda:assignedPerson/cda:name/cda:given) &gt; 0 and cda:legalAuthenticator/cda:assignedEntity/cda:assignedPerson/cda:name/cda:family) or&#xA;(count(cda:legalAuthenticator/cda:assignedEntity/cda:assignedPerson/cda:name/cda:given) &gt; 0 and cda:legalAuthenticator/cda:assignedEntity/cda:assignedPerson/cda:name/cda:family) or (count(cda:legalAuthenticator/cda:assignedEntity/cda:assignedPerson/cda:name[*])=0 and string-length(cda:legalAuthenticator/cda:assignedEntity/cda:assignedPerson/cda:name)!=0)">The content of name SHALL be a conformant US Realm Person Name (PN.US.FIELDED) (2.16.840.1.113883.10.20.22.5.1.1) (CONF:10430).</sch:assert>
      <sch:assert id="a-14836" test="not(cda:documentationOf) or cda:documentationOf[count(cda:serviceEvent)=1]">The documentationOf, if present, SHALL contain exactly one [1..1] serviceEvent (CONF:14836).</sch:assert>
      <sch:assert id="a-14837" test="not(cda:documentationOf/cda:serviceEvent) or cda:documentationOf/cda:serviceEvent[count(cda:effectiveTime)=1]">This serviceEvent SHALL contain exactly one [1..1] effectiveTime (CONF:14837).</sch:assert>
      <sch:assert id="a-14838" test="not(cda:documentationOf/cda:serviceEvent/cda:effectiveTime) or cda:documentationOf/cda:serviceEvent/cda:effectiveTime[count(cda:low)=1]">This effectiveTime SHALL contain exactly one [1..1] low (CONF:14838).</sch:assert>
      <sch:assert id="a-14839" test="not(cda:documentationOf/cda:serviceEvent) or cda:documentationOf/cda:serviceEvent[count(cda:performer) &gt; 0]">This serviceEvent SHOULD contain zero or more [0..*] performer (CONF:14839).</sch:assert>
      <sch:assert id="a-14840" test="not(cda:documentationOf/cda:serviceEvent/cda:performer) or cda:documentationOf/cda:serviceEvent/cda:performer[@typeCode]">The performer, if present, SHALL contain exactly one [1..1] @typeCode (CodeSystem: HL7ParticipationType 2.16.840.1.113883.5.90 STATIC) (CONF:14840).</sch:assert>
      <sch:assert id="a-14841" test="not(cda:documentationOf/cda:serviceEvent/cda:performer) or cda:documentationOf/cda:serviceEvent/cda:performer[count(cda:assignedEntity)=1]">The performer, if present, SHALL contain exactly one [1..1] assignedEntity (CONF:14841).</sch:assert>
      <sch:assert id="a-14842" test="not(cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity) or cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity[count(cda:code)=1]">This assignedEntity SHOULD contain zero or one [0..1] code (CONF:14842).</sch:assert>
      <sch:assert id="a-14843" test="not(cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity/cda:code) or cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity/cda:code[@code and @codeSystem='2.16.840.1.113883.6.101']">The code, if present, SHALL contain exactly one [1..1] @code, which SHOULD be selected from CodeSystem NUCCProviderTaxonomy (2.16.840.1.113883.6.101) STATIC (CONF:14843).</sch:assert>
      <sch:assert id="a-14846" test="not(cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity) or cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity[count(cda:id) &gt; 0]">This assignedEntity SHALL contain at least one [1..*] id (CONF:14846).</sch:assert>
      <sch:assert id="a-14847" test="not(cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity/cda:id) or cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity/cda:id[@root='2.16.840.1.113883.4.6']">Such ids SHOULD contain zero or one [0..1] @root="2.16.840.1.113883.4.6" National Provider Identifier (CONF:14847).</sch:assert>
      <sch:assert id="a-16783-c" test="not(tested-here)">This assignedAuthor SHOULD contain zero or one [0..1] assignedAuthoringDevice (CONF:16783).</sch:assert>
      <sch:assert id="a-16784" test="not(cda:author/cda:assignedAuthor/cda:assignedAuthoringDevice) or cda:author/cda:assignedAuthor/cda:assignedAuthoringDevice[count(cda:manufacturerModelName)=1]">The assignedAuthoringDevice, if present, SHALL contain exactly one [1..1] manufacturerModelName (CONF:16784).</sch:assert>
      <sch:assert id="a-16785" test="not(cda:author/cda:assignedAuthor/cda:assignedAuthoringDevice) or cda:author/cda:assignedAuthor/cda:assignedAuthoringDevice[count(cda:softwareName)=1]">The assignedAuthoringDevice, if present, SHALL contain exactly one [1..1] softwareName (CONF:16785).</sch:assert>
      <sch:assert id="a-16787" test="cda:author/cda:assignedAuthor[count(cda:code)=1]">This assignedAuthor SHOULD contain zero or one [0..1] code (CONF:16787).</sch:assert>
      <sch:assert id="a-16788" test="not(cda:author/cda:assignedAuthor/cda:code) or cda:author/cda:assignedAuthor/cda:code[@code]">The code, if present, SHALL contain exactly one [1..1] @code, which SHOULD be selected from ValueSet Healthcare Provider Taxonomy (HIPAA) 2.16.840.1.114222.4.11.1066 DYNAMIC (CONF:16788).</sch:assert>
      <sch:assert id="a-16789" test="not(cda:author/cda:assignedAuthor/cda:assignedPerson) or cda:author/cda:assignedAuthor/cda:assignedPerson[count(cda:name) &gt; 0]">The assignedPerson, if present, SHALL contain at least one [1..*] name (CONF:16789).</sch:assert>
      <sch:assert id="a-16819" test="not(cda:documentationOf/cda:serviceEvent/cda:performer/cda:functionCode) or cda:documentationOf/cda:serviceEvent/cda:performer/cda:functionCode[@codeSystem]">The functionCode, if present, SHOULD contain zero or one [0..1] @codeSystem, which SHOULD be selected from CodeSystem participationFunction (2.16.840.1.113883.5.88) STATIC (CONF:16819).</sch:assert>
      <sch:assert id="a-16820" test="not(cda:recordTarget/cda:patientRole/cda:providerOrganization/cda:id) or cda:recordTarget/cda:patientRole/cda:providerOrganization/cda:id[@root='2.16.840.1.113883.4.6']">Such ids SHOULD contain zero or one [0..1] @root="2.16.840.1.113883.4.6" National Provider Identifier (CONF:16820).</sch:assert>
      <sch:assert id="a-16821" test="not(cda:dataEnterer/cda:assignedEntity/cda:id) or cda:dataEnterer/cda:assignedEntity/cda:id[@root='2.16.840.1.113883.4.6']">Such ids SHOULD contain zero or one [0..1] @root="2.16.840.1.113883.4.6" National Provider Identifier (CONF:16821).</sch:assert>
      <sch:assert id="a-16822" test="cda:custodian/cda:assignedCustodian/cda:representedCustodianOrganization/cda:id[@root='2.16.840.1.113883.4.6']">Such ids SHOULD contain zero or one [0..1] @root="2.16.840.1.113883.4.6" National Provider Identifier (CONF:16822).</sch:assert>
      <sch:assert id="a-16824" test="not(cda:authenticator/cda:assignedEntity/cda:id) or cda:authenticator/cda:assignedEntity/cda:id[@root='2.16.840.1.113883.4.6']">Such ids SHOULD contain zero or one [0..1] @root="2.16.840.1.113883.4.6" National Provider Identifier  (CONF:16824).</sch:assert>
      <sch:assert id="a-16872-c" test="(count(cda:author/cda:assignedAuthor/cda:assignedPerson/cda:name/cda:given) &gt; 0 and cda:author/cda:assignedAuthor/cda:assignedPerson/cda:name/cda:family) or (count(cda:author/cda:assignedAuthor/cda:assignedPerson/cda:name[*])=0 and string-length(cda:author/cda:assignedAuthor/cda:assignedPerson/cda:name)!=0)">The content SHALL be a conformant US Realm Person Name (PN.US.FIELDED) (2.16.840.1.113883.10.20.22.5.1.1) (CONF:16872).</sch:assert>
      <sch:assert id="a-16873-c" test="string-length(cda:legalAuthenticator/cda:time//@value)&gt;=8">The content SHALL be a conformant US Realm Date and Time (DTM.US.FIELDED) (2.16.840.1.113883.10.20.22.5.4) (CONF:16873).</sch:assert>
      <sch:assert id="a-16874-c" test="string-length(cda:authenticator/cda:time//@value)&gt;=8">The content SHALL be a conformant US Realm Date and Time (DTM.US.FIELDED) (2.16.840.1.113883.10.20.22.5.4) (CONF:16874).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.1-warnings" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.7-warnings">
    <!--Pattern is used in an implied relationship.-->
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.7-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.7-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.7']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.7-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.7.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.7.1-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.7-warnings-abstract" />
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.7.1-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.7.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.7.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.3-warnings">
    <!--Pattern is used in an implied relationship.-->
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.3-warnings-abstract" abstract="true">
      <sch:assert id="a-7119" test="count(cda:entry[count(cda:organizer[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.1'])=1]) &gt; 0">SHOULD contain zero or more [0..*] entry (CONF:7119) such that it SHALL contain exactly one [1..1] Result Organizer (templateId:2.16.840.1.113883.10.20.22.4.1) (CONF:15515).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.3-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.3']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.3-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.3.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.3.1-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.3-warnings-abstract" />
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.3.1-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.3.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.3.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.4-warnings">
    <!--Pattern is used in an implied relationship.-->
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.4-warnings-abstract" abstract="true">
      <sch:assert id="a-7271" test="count(cda:entry[count(cda:organizer[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.26'])=1]) &gt; 0">SHOULD contain zero or more [0..*] entry (CONF:7271) such that it SHALL contain exactly one [1..1] Vital Signs Organizer (templateId:2.16.840.1.113883.10.20.22.4.26) (CONF:15517).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.4-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.4']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.4-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.4.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.4.1-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.4-warnings-abstract" />
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.4.1-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.4.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.4.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.6-warnings">
    <!--Pattern is used in an implied relationship.-->
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.6-warnings-abstract" abstract="true">
      <sch:assert id="a-7804" test="count(cda:entry[count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.30'])=1]) &gt; 0">SHOULD contain zero or more [0..*] entry (CONF:7804) such that it SHALL contain exactly one [1..1] Allergy Problem Act (templateId:2.16.840.1.113883.10.20.22.4.30) (CONF:15444).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.6-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.6']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.6-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.6.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.6.1-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.6-warnings-abstract" />
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.6.1-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.6.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.6.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.16-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.16-warnings-abstract" abstract="true">
      <sch:assert id="a-7501" test="count(cda:text)=1">SHOULD contain zero or one [0..1] text (CONF:7501).</sch:assert>
      <sch:assert id="a-7513" test="count(cda:effectiveTime[@operator='A'])=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:7513) such that it SHALL contain exactly one [1..1] @operator="A" (CONF:9106).</sch:assert>
      <sch:assert id="a-7516" test="count(cda:doseQuantity)=1">SHOULD contain zero or one [0..1] doseQuantity (CONF:7516).</sch:assert>
      <sch:assert id="a-7525" test="not(cda:rateQuantity) or cda:rateQuantity[@unit]">The rateQuantity, if present, SHALL contain exactly one [1..1] @unit, which SHALL be selected from ValueSet UnitsOfMeasureCaseSensitive 2.16.840.1.113883.1.11.12839 DYNAMIC (CONF:7525).</sch:assert>
      <sch:assert id="a-7526" test="not(cda:doseQuantity) or cda:doseQuantity[@unit]">The doseQuantity, if present, SHOULD contain zero or one [0..1] @unit, which SHALL be selected from ValueSet UnitsOfMeasureCaseSensitive 2.16.840.1.113883.1.11.12839 DYNAMIC (CONF:7526).</sch:assert>
      <sch:assert id="a-9105-c" test="count(cda:effectiveTime[@xsi:type=&quot;PIVL_TS&quot; or @xsi:type=&quot;EIVL_TS&quot;])=1">SHALL contain exactly one [1..1] @xsi:type=?PIVL_TS? or ?EIVL_TS? (CONF:9105).</sch:assert>
      <sch:assert id="a-15977" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:15977).</sch:assert>
      <sch:assert id="a-15978" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15978).</sch:assert>
      <sch:assert id="a-15979-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15979).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.16-warnings" context="cda:substanceAdministration[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.16']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.16-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.5-warnings">
    <!--Pattern is used in an implied relationship.-->
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.5-warnings-abstract" abstract="true">
      <sch:assert id="a-7881" test="count(cda:entry) &gt; 0">SHOULD contain zero or more [0..*] entry (CONF:7881).</sch:assert>
      <sch:assert id="a-15505" test="not(cda:entry) or cda:entry[count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.3'])=1]">The entry, if present, SHALL contain exactly one [1..1] Problem Concern Act (Condition) (templateId:2.16.840.1.113883.10.20.22.4.3) (CONF:15505).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.5-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.5']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.5-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.5.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.5.1-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.5-warnings-abstract" />
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.5.1-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.5.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.5.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.1-warnings-abstract" abstract="true">
      <sch:assert id="a-7165-c" test="../cda:organizer/@classCode='CLUSTER' or ../cda:organizer/@classCode='BATTERY'">SHOULD contain zero or one [0..1] @classCode="CLUSTER" Cluster (CodeSystem: 2.16.840.1.113883.5.6 HL7ActClass) OR SHOULD contain zero or one [0..1] @classCode="BATTERY" Battery (CodeSystem: 2.16.840.1.113883.5.6 HL7ActClass) (CONF:7165).</sch:assert>
      <sch:assert id="a-19218-c" test="cda:code[@codeSystem='2.16.840.1.113883.6.1'] or cda:code[@codeSystem='2.16.840.1.113883.6.96'] or cda:code[@codeSystem='2.16.840.1.113883.6.12']">SHOULD be selected from LOINC (codeSystem 2.16.840.1.113883.6.1) or SNOMED CT (codeSystem 2.16.840.1.113883.6.96), and MAY be selected from CPT-4 (codeSystem 2.16.840.1.113883.6.12) (CONF:19218).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.1-warnings" context="cda:organizer[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.2-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.2-warnings-abstract" abstract="true">
      <sch:assert id="a-7138" test="count(cda:text)=1">SHOULD contain zero or one [0..1] text (CONF:7138).</sch:assert>
      <sch:assert id="a-7147" test="count(cda:interpretationCode) &gt; 0">SHOULD contain zero or more [0..*] interpretationCode (CONF:7147).</sch:assert>
      <sch:assert id="a-7150" test="count(cda:referenceRange) &gt; 0">SHOULD contain zero or more [0..*] referenceRange (CONF:7150).</sch:assert>
      <sch:assert id="a-7151" test="not(cda:referenceRange) or cda:referenceRange[count(cda:observationRange)=1]">The referenceRange, if present, SHALL contain exactly one [1..1] observationRange (CONF:7151).</sch:assert>
      <sch:assert id="a-7152-c" test="not(cda:referenceRange/cda:observationRange/cda:code)">This observationRange SHALL NOT contain [0..0] code (CONF:7152).</sch:assert>
      <sch:assert id="a-15924" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:15924).</sch:assert>
      <sch:assert id="a-15925" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15925).</sch:assert>
      <sch:assert id="a-15926-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15926).</sch:assert>
      <sch:assert id="a-19211-c" test="count(cda:code[@codeSystem])=0 or cda:code[@codeSystem='2.16.840.1.113883.6.1'] or cda:code[@codeSystem='2.16.840.1.113883.6.96']">SHOULD be from LOINC (CodeSystem: 2.16.840.1.113883.6.1) or SNOMED CT (CodeSystem: 2.16.840.1.113883.6.96) (CONF:19211).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.2-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.2']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.2-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.5.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.1-warnings-abstract" abstract="true">
      <sch:assert id="a-7163-c" test=".">The second occurrence of given (given[2]) if provided, SHALL include middle name or middle initial (CONF:7163).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.1-warnings" context="cda:PN[cda:templateId/@root='2.16.840.1.113883.10.20.22.5.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.5.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.26-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.26-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.26-warnings" context="cda:organizer[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.26']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.26-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.5.2-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.2-warnings-abstract" abstract="true">
      <sch:assert id="a-7290" test="@use and @use=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.1.11.10637']/voc:code/@value">SHOULD contain zero or one [0..1] @use, which SHALL be selected from ValueSet PostalAddressUse 2.16.840.1.113883.1.11.10637 STATIC 2005-05-01 (CONF:7290).</sch:assert>
      <sch:assert id="a-7293" test="count(cda:state[@xsi:type='ST'])=1">SHOULD contain zero or one [0..1] state (ValueSet: StateValueSet 2.16.840.1.113883.3.88.12.80.1 DYNAMIC) (CONF:7293).</sch:assert>
      <sch:assert id="a-7294" test="count(cda:postalCode)=1">SHOULD contain zero or one [0..1] postalCode, which SHOULD be selected from ValueSet PostalCodeValueSet 2.16.840.1.113883.3.88.12.80.2 DYNAMIC (CONF:7294).</sch:assert>
      <sch:assert id="a-7295" test="count(cda:country)=1">SHOULD contain zero or one [0..1] country, which SHALL be selected from ValueSet CountryValueSet 2.16.840.1.113883.3.88.12.80.63 DYNAMIC (CONF:7295).</sch:assert>
      <sch:assert id="a-10024-c" test="cda:streetAddressLine and cda:city and (cda:country!='US' or (cda:country='US' and cda:state and cda:postalCode))">State is required if the country is US. If country is not specified, its assumed to be US. If country is something other than US, the state MAY be present but MAY be bound to different vocabularies (CONF:10024).</sch:assert>
      <sch:assert id="a-10025-c" test="cda:streetAddressLine and cda:city and (cda:country!='US' or (cda:country='US' and cda:state and cda:postalCode))">PostalCode is required if the country is US. If country is not specified, its assumed to be US. If country is something other than US, the postalCode MAY be present but MAY be bound to different vocabularies (CONF:10025).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.2-warnings" context="cda:AD[cda:templateId/@root='2.16.840.1.113883.10.20.22.5.2']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.5.2-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.27-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.27-warnings-abstract" abstract="true">
      <sch:assert id="a-7302" test="count(cda:text)=1">SHOULD contain zero or one [0..1] text (CONF:7302).</sch:assert>
      <sch:assert id="a-15943" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:15943).</sch:assert>
      <sch:assert id="a-15944" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15944).</sch:assert>
      <sch:assert id="a-15945-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15945).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.27-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.27']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.27-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.28-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.28-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.28-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.28']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.28-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.9-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.9-warnings-abstract" abstract="true">
      <sch:assert id="a-7330" test="count(cda:text)=1">SHOULD contain zero or one [0..1] text (CONF:7330).</sch:assert>
      <sch:assert id="a-7332" test="count(cda:effectiveTime)=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:7332).</sch:assert>
      <sch:assert id="a-7333" test="not(cda:effectiveTime) or cda:effectiveTime[count(cda:low)=1]">The effectiveTime, if present, SHOULD contain zero or one [0..1] low (CONF:7333).</sch:assert>
      <sch:assert id="a-7334" test="not(cda:effectiveTime) or cda:effectiveTime[count(cda:high)=1]">The effectiveTime, if present, SHOULD contain zero or one [0..1] high (CONF:7334).</sch:assert>
      <sch:assert id="a-7580" test="count(cda:entryRelationship[@typeCode='SUBJ'][@inversionInd='true'][count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.8'])=1])=1">SHOULD contain zero or one [0..1] entryRelationship (CONF:7580) such that it SHALL contain exactly one [1..1] @typeCode="SUBJ" Has subject (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:7581). SHALL contain exactly one [1..1] @inversionInd="true" TRUE (CONF:10375). SHALL contain exactly one [1..1] Severity Observation (templateId:2.16.840.1.113883.10.20.22.4.8) (CONF:15922).</sch:assert>
      <sch:assert id="a-15917" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:15917).</sch:assert>
      <sch:assert id="a-15918" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15918).</sch:assert>
      <sch:assert id="a-15919-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15919).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.9-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.9']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.9-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.8-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.8-warnings-abstract" abstract="true">
      <sch:assert id="a-7350" test="count(cda:text)=1">SHOULD contain zero or one [0..1] text (CONF:7350).</sch:assert>
      <sch:assert id="a-9117" test="count(cda:interpretationCode) &gt; 0">SHOULD contain zero or more [0..*] interpretationCode (CONF:9117).</sch:assert>
      <sch:assert id="a-15928" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:15928).</sch:assert>
      <sch:assert id="a-15929" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15929).</sch:assert>
      <sch:assert id="a-15930-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15930).</sch:assert>
      <sch:assert id="a-16038" test="not(cda:interpretationCode) or cda:interpretationCode[@code]">The interpretationCode, if present, SHOULD contain zero or one [0..1] @code, which SHOULD be selected from ValueSet Observation Interpretation (HL7) 2.16.840.1.113883.1.11.78 DYNAMIC (CONF:16038).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.8-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.8']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.8-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.6-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.6-warnings-abstract" abstract="true">
      <sch:assert id="a-7362" test="count(cda:text)=1">SHOULD contain zero or one [0..1] text (CONF:7362).</sch:assert>
      <sch:assert id="a-15593" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:15593).</sch:assert>
      <sch:assert id="a-15594" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHALL contain exactly one [1..1] @value (CONF:15594).</sch:assert>
      <sch:assert id="a-15595-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15595).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.6-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.6']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.6-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.25-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.25-warnings-abstract" abstract="true">
      <sch:assert id="a-7369" test="count(cda:value[@xsi:type='CD'])=1">SHOULD contain zero or one [0..1] value with @xsi:type="CD" (CONF:7369).</sch:assert>
      <sch:assert id="a-16854" test="count(cda:code)=1">SHOULD contain zero or one [0..1] code (CONF:16854).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.25-warnings" context="cda:criterion[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.25']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.25-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.24.3.90-warnings">
    <!--Pattern is used in an implied relationship.-->
    <sch:rule id="r-2.16.840.1.113883.10.20.24.3.90-warnings-abstract" abstract="true">
      <sch:assert id="a-16318" test="count(cda:participant) &gt; 0">SHOULD contain zero or more [0..*] participant (CONF:16318).</sch:assert>
      <sch:assert id="a-16319" test="not(cda:participant) or cda:participant[@typeCode='CSM']">The participant, if present, SHALL contain exactly one [1..1] @typeCode="CSM" Consumable (CodeSystem: HL7ParticipationType 2.16.840.1.113883.5.90 STATIC) (CONF:16319).</sch:assert>
      <sch:assert id="a-16320" test="not(cda:participant) or cda:participant[count(cda:participantRole)=1]">The participant, if present, SHALL contain exactly one [1..1] participantRole (CONF:16320).</sch:assert>
      <sch:assert id="a-16321" test="not(cda:participant/cda:participantRole) or cda:participant/cda:participantRole[@classCode='MANU']">This participantRole SHALL contain exactly one [1..1] @classCode="MANU" Manufactured Product (CodeSystem: RoleClass 2.16.840.1.113883.5.110 STATIC) (CONF:16321).</sch:assert>
      <sch:assert id="a-16322" test="not(cda:participant/cda:participantRole) or cda:participant/cda:participantRole[count(cda:playingEntity)=1]">This participantRole SHALL contain exactly one [1..1] playingEntity (CONF:16322).</sch:assert>
      <sch:assert id="a-16323" test="not(cda:participant/cda:participantRole/cda:playingEntity) or cda:participant/cda:participantRole/cda:playingEntity[@classCode='MMAT']">This playingEntity SHALL contain exactly one [1..1] @classCode="MMAT" Manufactured Material (CodeSystem: EntityClass 2.16.840.1.113883.5.41 STATIC) (CONF:16323).</sch:assert>
      <sch:assert id="a-16324" test="not(cda:participant/cda:participantRole/cda:playingEntity) or cda:participant/cda:participantRole/cda:playingEntity[count(cda:code)=1]">This playingEntity SHALL contain exactly one [1..1] code (CONF:16324).</sch:assert>
      <sch:assert id="a-16325-c" test=".">In an allergy to a specific medication the code SHALL be selected from the ValueSet 2.16.840.1.113883.3.88.12.80.16 Medication Brand Name DYNAMIC or the ValueSet 2.16.840.1.113883.3.88.12.80.17 Medication Clinical Drug DYNAMIC (CONF:16325).</sch:assert>
      <sch:assert id="a-16326" test="not(cda:participant/cda:participantRole/cda:playingEntity/cda:code) or cda:participant/cda:participantRole/cda:playingEntity/cda:code[count(cda:originalText)=1]">This code SHOULD contain zero or one [0..1] originalText (CONF:16326).</sch:assert>
      <sch:assert id="a-16327" test="not(cda:participant/cda:participantRole/cda:playingEntity/cda:code/cda:originalText) or cda:participant/cda:participantRole/cda:playingEntity/cda:code/cda:originalText[count(cda:reference)=1]">The originalText, if present, SHOULD contain zero or one [0..1] reference (CONF:16327).</sch:assert>
      <sch:assert id="a-16328" test="not(cda:participant/cda:participantRole/cda:playingEntity/cda:code/cda:originalText/cda:reference) or cda:participant/cda:participantRole/cda:playingEntity/cda:code/cda:originalText/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:16328).</sch:assert>
      <sch:assert id="a-16329-c" test="count(cda:participant/cda:participantRole/cda:playingEntity/cda:code/cda:originalText/cda:reference[@value])=0 or starts-with(cda:participant/cda:participantRole/cda:playingEntity/cda:code/cda:originalText/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:16329).</sch:assert>
      <sch:assert id="a-16331-c" test=".">In an allergy to a class of medications the code SHALL be selected from the ValueSet 2.16.840.1.113883.3.88.12.80.18 Medication Drug Class DYNAMIC (CONF:16331).</sch:assert>
      <sch:assert id="a-16332-c" test=".">In an allergy to a food or other substance the code SHALL be selected from the ValueSet 2.16.840.1.113883.3.88.12.80.20 Ingredient Name DYNAMIC (CONF:16332).</sch:assert>
      <sch:assert id="a-16337" test="count(cda:entryRelationship[@inversionInd='true'][@typeCode='MFST'][count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.9'])=1]) &gt; 0">SHOULD contain zero or more [0..*] entryRelationship (CONF:16337) such that it SHALL contain exactly one [1..1] @inversionInd="true" True (CONF:16338). SHALL contain exactly one [1..1] @typeCode="MFST" Is Manifestation of (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:16339). SHALL contain exactly one [1..1] Reaction Observation (templateId:2.16.840.1.113883.10.20.22.4.9) (CONF:16340).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.24.3.90-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.24.3.90']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.24.3.90-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.7-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.7-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.24.3.90-warnings-abstract" />
      <sch:assert id="a-7402" test="count(cda:participant[@typeCode='CSM'][count(cda:participantRole[@classCode='MANU'][count(cda:playingEntity[@classCode='MMAT'][count(cda:code)=1])=1])=1])=1">SHOULD contain zero or one [0..1] participant (CONF:7402) such that it SHALL contain exactly one [1..1] @typeCode="CSM" Consumable (CodeSystem: HL7ParticipationType 2.16.840.1.113883.5.90 STATIC) (CONF:7403). SHALL contain exactly one [1..1] participantRole (CONF:7404). This participantRole SHALL contain exactly one [1..1] @classCode="MANU" Manufactured Product (CodeSystem: RoleClass 2.16.840.1.113883.5.110 STATIC) (CONF:7405). This participantRole SHALL contain exactly one [1..1] playingEntity (CONF:7406). This playingEntity SHALL contain exactly one [1..1] @classCode="MMAT" Manufactured Material (CodeSystem: EntityClass 2.16.840.1.113883.5.41 STATIC) (CONF:7407). This playingEntity SHALL contain exactly one [1..1] code (CONF:7419).</sch:assert>
      <sch:assert id="a-7421-c" test=".">In an allergy to a specific medication the code SHALL be selected from the ValueSet 2.16.840.1.113883.3.88.12.80.16 Medication Brand Name DYNAMIC or the ValueSet 2.16.840.1.113883.3.88.12.80.17 Medication Clinical Drug DYNAMIC (CONF:7421).</sch:assert>
      <sch:assert id="a-7422" test="cda:value[@xsi:type='CD'][count(cda:originalText)=1]">This value SHOULD contain zero or one [0..1] originalText (CONF:7422).</sch:assert>
      <sch:assert id="a-7447" test="count(cda:entryRelationship[@inversionInd='true'][@typeCode='MFST'][count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.9'])=1]) &gt; 0">SHOULD contain zero or more [0..*] entryRelationship (CONF:7447) such that it SHALL contain exactly one [1..1] @inversionInd="true" True (CONF:7449). SHALL contain exactly one [1..1] @typeCode="MFST" Is Manifestation of (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:7907). SHALL contain exactly one [1..1] Reaction Observation (templateId:2.16.840.1.113883.10.20.22.4.9) (CONF:15955).</sch:assert>
      <sch:assert id="a-9103-c" test=".">If it is unknown when the allergy began, this effectiveTime SHALL contain low/@nullFLavor="UNK" (CONF:9103).</sch:assert>
      <sch:assert id="a-9961" test="count(cda:entryRelationship[@typeCode='SUBJ'][@inversionInd='true'][count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.8'])=1])=1">SHOULD contain zero or one [0..1] entryRelationship (CONF:9961) such that it SHALL contain exactly one [1..1] @typeCode="SUBJ" Has Subject (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:9962). SHALL contain exactly one [1..1] @inversionInd="true" True (CONF:9964). SHALL contain exactly one [1..1] Severity Observation (templateId:2.16.840.1.113883.10.20.22.4.8) (CONF:15956).</sch:assert>
      <sch:assert id="a-10082-c" test=".">If the allergy is no longer a concern, this effectiveTime MAY contain zero or one [0..1] high (CONF:10082).</sch:assert>
      <sch:assert id="a-10083-c" test=".">In an allergy to a class of medications the code SHALL be selected from the ValueSet 2.16.840.1.113883.3.88.12.80.18 Medication Drug Class DYNAMIC (CONF:10083).</sch:assert>
      <sch:assert id="a-10084-c" test=".">In an allergy to a food or other substance the code SHALL be selected from the ValueSet 2.16.840.1.113883.3.88.12.80.20 Ingredient Name DYNAMIC (CONF:10084).</sch:assert>
      <sch:assert id="a-15950" test="not(cda:value[@xsi:type='CD']/cda:originalText/cda:reference) or cda:value[@xsi:type='CD']/cda:originalText/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15950).</sch:assert>
      <sch:assert id="a-15951-c" test="count(cda:value/cda:originalText/cda:reference[@value])=0 or starts-with(cda:value/cda:originalText/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15951).</sch:assert>
      <sch:assert id="a-15953-c" test="count(cda:participant/cda:participantRole/cda:playingEntity/cda:code/cda:originalText/cda:reference[@value])=0 or starts-with(cda:participant/cda:participantRole/cda:playingEntity/cda:code/cda:originalText/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15953).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.7-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.7']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.7-warnings-abstract" />
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.7-7402-branch-7402-warnings-abstract" abstract="true">
      <sch:assert id="a-7424-branch-7402" test="not(cda:participantRole/cda:playingEntity/cda:code) or cda:participantRole/cda:playingEntity/cda:code[count(cda:originalText)=1]">This code SHOULD contain zero or one [0..1] originalText (CONF:7424).</sch:assert>
      <sch:assert id="a-7425-branch-7402" test="not(cda:participantRole/cda:playingEntity/cda:code/cda:originalText) or cda:participantRole/cda:playingEntity/cda:code/cda:originalText[count(cda:reference)=1]">The originalText, if present, SHOULD contain zero or one [0..1] reference (CONF:7425).</sch:assert>
      <sch:assert id="a-15952-branch-7402" test="not(cda:participantRole/cda:playingEntity/cda:code/cda:originalText/cda:reference) or cda:participantRole/cda:playingEntity/cda:code/cda:originalText/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15952).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.7-7402-branch-7402-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.7']/cda:participant[@typeCode='CSM'][cda:participantRole[@classCode='MANU'][cda:playingEntity[@classCode='MMAT'][cda:code]]]">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.7-7402-branch-7402-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.20-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.20-warnings-abstract" abstract="true">
      <sch:assert id="a-7395" test="count(cda:text)=1">SHOULD contain zero or one [0..1] text (CONF:7395).</sch:assert>
      <sch:assert id="a-15577" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:15577).</sch:assert>
      <sch:assert id="a-15578" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15578).</sch:assert>
      <sch:assert id="a-15579-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This @value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15579).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.20-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.20']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.20-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.23-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.23-warnings-abstract" abstract="true">
      <sch:assert id="a-7413" test="cda:manufacturedMaterial/cda:code[count(cda:originalText)=1]">This code SHOULD contain zero or one [0..1] originalText (CONF:7413).</sch:assert>
      <sch:assert id="a-15986" test="not(cda:manufacturedMaterial/cda:code/cda:originalText) or cda:manufacturedMaterial/cda:code/cda:originalText[count(cda:reference)=1]">The originalText, if present, SHOULD contain zero or one [0..1] reference (CONF:15986).</sch:assert>
      <sch:assert id="a-15987" test="not(cda:manufacturedMaterial/cda:code/cda:originalText/cda:reference) or cda:manufacturedMaterial/cda:code/cda:originalText/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15987).</sch:assert>
      <sch:assert id="a-15988-c" test="count(cda:manufacturedMaterial/cda:code/cda:originalText/cda:reference[@value])=0 or starts-with(cda:manufacturedMaterial/cda:code/cda:originalText/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15988).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.23-warnings" context="cda:manufacturedProduct[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.23']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.23-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.17-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.17-warnings-abstract" abstract="true">
      <sch:assert id="a-7434" test="count(cda:repeatNumber)=1">SHOULD contain zero or one [0..1] repeatNumber (CONF:7434).</sch:assert>
      <sch:assert id="a-7436" test="count(cda:quantity)=1">SHOULD contain zero or one [0..1] quantity (CONF:7436).</sch:assert>
      <sch:assert id="a-7444" test="not(cda:entryRelationship) or cda:entryRelationship[@typeCode='SUBJ']">The entryRelationship, if present, SHALL contain exactly one [1..1] @typeCode="SUBJ" (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:7444).</sch:assert>
      <sch:assert id="a-7445" test="not(cda:entryRelationship) or cda:entryRelationship[@inversionInd='true']">The entryRelationship, if present, SHALL contain exactly one [1..1] @inversionInd="true" True (CONF:7445).</sch:assert>
      <sch:assert id="a-15143" test="count(cda:effectiveTime[count(cda:high)=1])=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:15143) such that it SHALL contain exactly one [1..1] high (CONF:15144).</sch:assert>
      <sch:assert id="a-16095" test="not(cda:entryRelationship) or cda:entryRelationship[count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.20'])=1]">The entryRelationship, if present, SHALL contain exactly one [1..1] Instructions (templateId:2.16.840.1.113883.10.20.22.4.20) (CONF:16095).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.17-warnings" context="cda:supply[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.17']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.17-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.18-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.18-warnings-abstract" abstract="true">
      <sch:assert id="a-7456" test="count(cda:effectiveTime)=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:7456).</sch:assert>
      <sch:assert id="a-7457" test="count(cda:repeatNumber)=1">SHOULD contain zero or one [0..1] repeatNumber (CONF:7457).</sch:assert>
      <sch:assert id="a-7458" test="count(cda:quantity)=1">SHOULD contain zero or one [0..1] quantity (CONF:7458).</sch:assert>
      <sch:assert id="a-7467" test="not(cda:performer) or cda:performer[count(cda:assignedEntity)=1]">The performer, if present, SHALL contain exactly one [1..1] assignedEntity (CONF:7467).</sch:assert>
      <sch:assert id="a-7468" test="not(cda:performer/cda:assignedEntity) or cda:performer/cda:assignedEntity[count(cda:addr)=1]">This assignedEntity SHOULD contain zero or one [0..1] addr (CONF:7468).</sch:assert>
      <sch:assert id="a-10565-c" test="cda:performer/cda:assignedEntity/cda:addr[cda:streetAddressLine and cda:city and ((not(cda:country) or cda:country!='US') or (cda:country='US' and cda:state and cda:postalCode))]">The content of addr SHALL be a conformant US Realm Address (AD.US.FIELDED) (2.16.840.1.113883.10.20.22.5.2) (CONF:10565).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.18-warnings" context="cda:supply[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.18']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.18-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.30-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.30-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.30-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.30']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.30-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.19-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.19-warnings-abstract" abstract="true">
      <sch:assert id="a-7488" test="count(cda:effectiveTime)=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:7488).</sch:assert>
      <sch:assert id="a-7489" test="count(cda:value[@xsi:type='CD'])=1">SHOULD contain zero or one [0..1] value with @xsi:type="CD" (CONF:7489).</sch:assert>
      <sch:assert id="a-15985" test="not(cda:value[@xsi:type='CD']) or cda:value[@xsi:type='CD'][@code]">The value, if present, SHOULD contain zero or one [0..1] @code, which SHOULD be selected from ValueSet Problem Value Set 2.16.840.1.113883.3.88.12.3221.7.4 DYNAMIC (CONF:15985).</sch:assert>
      <sch:assert id="a-15991-c" test="not(tested)">If the diagnosis is unknown or the SNOMED code is unknown, @nullFlavor SHOULD be ?UNK?. If the code is something other than SNOMED, @nullFlavor SHOULD be ?OTH? and the other code SHOULD be placed in the translation element (CONF:15991).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.19-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.19']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.19-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.24-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.24-warnings-abstract" abstract="true">
      <sch:assert id="a-10087-c" test=".">This playingEntity/name MAY be used for the vehicle name in text, such as Normal Saline (CONF:10087).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.24-warnings" context="cda:participantRole[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.24']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.24-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.14-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.14-warnings-abstract" abstract="true">
      <sch:assert id="a-7662" test="count(cda:effectiveTime)=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:7662).</sch:assert>
      <sch:assert id="a-7683" test="count(cda:targetSiteCode) &gt; 0">SHOULD contain zero or more [0..*] targetSiteCode (CONF:7683).</sch:assert>
      <sch:assert id="a-7704" test="not(cda:specimen) or cda:specimen[count(cda:specimenRole)=1]">The specimen, if present, SHALL contain exactly one [1..1] specimenRole (CONF:7704).</sch:assert>
      <sch:assert id="a-7716" test="not(cda:specimen/cda:specimenRole) or cda:specimen/cda:specimenRole[count(cda:id) &gt; 0]">This specimenRole SHOULD contain zero or more [0..*] id (CONF:7716).</sch:assert>
      <sch:assert id="a-7718" test="count(cda:performer[count(cda:assignedEntity[count(cda:id) &gt; 0][count(cda:addr)=1][count(cda:telecom)=1])=1]) &gt; 0">SHOULD contain zero or more [0..*] performer (CONF:7718) such that it SHALL contain exactly one [1..1] assignedEntity (CONF:7720). This assignedEntity SHALL contain at least one [1..*] id (CONF:7722). This assignedEntity SHALL contain exactly one [1..1] addr (CONF:7731). This assignedEntity SHALL contain exactly one [1..1] telecom (CONF:7732).</sch:assert>
      <sch:assert id="a-16082" test="not(cda:targetSiteCode) or cda:targetSiteCode[@code]">The targetSiteCode, if present, SHALL contain exactly one [1..1] @code, which SHALL be selected from ValueSet Body Site Value Set 2.16.840.1.113883.3.88.12.3221.8.9 DYNAMIC (CONF:16082).</sch:assert>
      <sch:assert id="a-19203" test="cda:code[count(cda:originalText)=1]">This code SHOULD contain zero or one [0..1] originalText (CONF:19203).</sch:assert>
      <sch:assert id="a-19204" test="not(cda:code/cda:originalText) or cda:code/cda:originalText[count(cda:reference)=1]">The originalText, if present, SHOULD contain zero or one [0..1] reference (CONF:19204).</sch:assert>
      <sch:assert id="a-19205" test="not(cda:code/cda:originalText/cda:reference) or cda:code/cda:originalText/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:19205).</sch:assert>
      <sch:assert id="a-19206-c" test="count(cda:code/cda:originalText/cda:reference[@value])=0 or starts-with(cda:code/cda:originalText/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:19206).</sch:assert>
      <sch:assert id="a-19207-c" test="count(cda:code[@codeSystem])=0 or cda:code[@codeSystem='2.16.840.1.113883.6.1'] or cda:code[@codeSystem='2.16.840.1.113883.6.96'] or cda:code[@codeSystem='2.16.840.1.113883.6.12'] or cda:code[@codeSystem='2.16.840.1.113883.6.104'] or cda:code[@codeSystem='2.16.840.1.113883.6.4']">This code in a procedure activity SHOULD be selected from LOINC (codeSystem 2.16.840.1.113883.6.1) or SNOMED CT (CodeSystem: 2.16.840.1.113883.6.96), and MAY be selected from CPT-4 (CodeSystem: 2.16.840.1.113883.6.12) or ICD10 PCS (CodeSystem: 2.16.840.1.113883.6.4) (CONF:19207).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.14-warnings" context="cda:procedure[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.14']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.14-warnings-abstract" />
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.14-7718-branch-7718-warnings-abstract" abstract="true">
      <sch:assert id="a-7733-branch-7718" test="not(cda:assignedEntity) or cda:assignedEntity[count(cda:representedOrganization)=1]">This assignedEntity SHOULD contain zero or one [0..1] representedOrganization (CONF:7733).</sch:assert>
      <sch:assert id="a-7734-branch-7718" test="not(cda:assignedEntity/cda:representedOrganization) or cda:assignedEntity/cda:representedOrganization[count(cda:id) &gt; 0]">The representedOrganization, if present, SHOULD contain zero or more [0..*] id (CONF:7734).</sch:assert>
      <sch:assert id="a-7736-branch-7718" test="not(cda:assignedEntity/cda:representedOrganization) or cda:assignedEntity/cda:representedOrganization[count(cda:addr)=1]">The representedOrganization, if present, SHALL contain exactly one [1..1] addr (CONF:7736).</sch:assert>
      <sch:assert id="a-7737-branch-7718" test="not(cda:assignedEntity/cda:representedOrganization) or cda:assignedEntity/cda:representedOrganization[count(cda:telecom)=1]">The representedOrganization, if present, SHALL contain exactly one [1..1] telecom (CONF:7737).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.14-7718-branch-7718-warnings" context="cda:procedure[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.14']/cda:performer[cda:assignedEntity[cda:id][cda:addr][cda:telecom]]">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.14-7718-branch-7718-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.29-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.29-warnings-abstract" abstract="true">
      <sch:assert id="a-7563" test="count(cda:effectiveTime)=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:7563).</sch:assert>
      <sch:assert id="a-15580" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:15580).</sch:assert>
      <sch:assert id="a-15581" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15581).</sch:assert>
      <sch:assert id="a-15582-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15582).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.29-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.29']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.29-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.1-warnings">
    <!--Pattern is used in an implied relationship.-->
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.1-warnings-abstract" abstract="true">
      <sch:assert id="a-7795" test="count(cda:entry[count(cda:substanceAdministration[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.16'])=1]) &gt; 0">SHOULD contain zero or more [0..*] entry (CONF:7795) such that it SHALL contain exactly one [1..1] Medication Activity (templateId:2.16.840.1.113883.10.20.22.4.16) (CONF:15984).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.1-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.1.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.1.1-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.1-warnings-abstract" />
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.1.1-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.1.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.1.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.9-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.9-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-warnings-abstract" />
      <sch:assert id="a-7603" test="count(cda:documentationOf)=1">SHOULD contain zero or one [0..1] documentationOf (CONF:7603).</sch:assert>
      <sch:assert id="a-7604" test="not(cda:documentationOf) or cda:documentationOf[count(cda:serviceEvent)=1]">The documentationOf, if present, SHALL contain exactly one [1..1] serviceEvent (CONF:7604).</sch:assert>
      <sch:assert id="a-10066-c" test="count(cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:high | cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:width)=1">If a width element is not present, the serviceEvent SHALL include effectiveTime/high (CONF:10066).</sch:assert>
      <sch:assert id="a-26420" test="not(cda:documentationOf/cda:serviceEvent) or cda:documentationOf/cda:serviceEvent[@classCode='PCPR']">This serviceEvent SHALL contain exactly one [1..1] @classCode="PCPR" Care Provision (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:26420).</sch:assert>
      <sch:assert id="a-10137-c" test="not(cda:documentationOf/cda:serviceEvent/cda:effectiveTime) or string-length(cda:documentationOf/cda:serviceEvent/cda:effectiveTime//@value)&gt;=8">The content of effectiveTime SHALL be a conformant US Realm Date and Time (DT.US.FIELDED) (2.16.840.1.113883.10.20.22.5.4) (CONF:10137).</sch:assert>
      <sch:assert id="a-9593-c" test=".">A Progress Note can conform to CDA Level 1 (nonXMLBody), CDA Level 2 (structuredBody with sections that contain a narrative block), or CDA Level 3 (structuredBody containing sections that contain a narrative block and coded entries). In this template (templateId 2.16.840.1.113883.10.20.22.1.9), coded entries are optional (CONF:9593).</sch:assert>
      <sch:assert id="a-9482-c" test="cda:documentationOf/cda:serviceEvent/cda:effectiveTime/cda:low">The serviceEvent/effectiveTime element SHOULD be present with effectiveTime/low element (CONF:9482).</sch:assert>
      <sch:assert id="a-9481-c" test="not(cda:documentationOf/cda:serviceEvent) or cda:documentationOf/cda:serviceEvent[count(cda:effectiveTime)=1]">This serviceEvent SHOULD contain zero or one [0..1] effectiveTime (CONF:9481).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.9-warnings" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.9']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.9-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.31-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.31-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.31-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.31']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.31-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.10-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.10-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-warnings-abstract" />
      <sch:assert id="a-7623-c" test=".">The text element SHALL either contain a reference element with a value attribute, or have a representation attribute with the value of B64, a mediaType attribute, and contain the media content (CONF:7623).</sch:assert>
      <sch:assert id="a-7624-c" test=".">The value of @mediaType, if present, SHALL be drawn from the value set 2.16.840.1.113883.11.20.7.1 SupportedFileFormats STATIC 20100512 (CONF:7624).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.10-warnings" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.10']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.10-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.32-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.32-warnings-abstract" abstract="true">
      <sch:assert id="a-7760" test="count(cda:addr) &gt; 0">SHOULD contain zero or more [0..*] addr (CONF:7760).</sch:assert>
      <sch:assert id="a-7761" test="count(cda:telecom) &gt; 0">SHOULD contain zero or more [0..*] telecom (CONF:7761).</sch:assert>
      <sch:assert id="a-7763" test="not(cda:playingEntity) or cda:playingEntity[@classCode='PLC']">The playingEntity, if present, SHALL contain exactly one [1..1] @classCode="PLC" (CodeSystem: EntityClass 2.16.840.1.113883.5.41 STATIC) (CONF:7763).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.32-warnings" context="cda:participantRole[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.32']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.32-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.3-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.3-warnings-abstract" abstract="true">
      <sch:assert id="a-9033" test="cda:effectiveTime[count(cda:high)=1]">This effectiveTime SHOULD contain zero or one [0..1] high (CONF:9033).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.3-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.3']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.3-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.4-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.4-warnings-abstract" abstract="true">
      <sch:assert id="a-9050" test="count(cda:effectiveTime)=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:9050).</sch:assert>
      <sch:assert id="a-9185" test="count(cda:text)=1">SHOULD contain zero or one [0..1] text (CONF:9185).</sch:assert>
      <sch:assert id="a-10142-c" test="not(tested)">If the diagnosis is unknown or the SNOMED code is unknown, @nullFlavor SHOULD be ?UNK?.  If the code is something other than SNOMED, @nullFlavor SHOULD be ?OTH? and the other code SHOULD be placed in the translation element (CONF:10142).</sch:assert>
      <sch:assert id="a-15587" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:15587).</sch:assert>
      <sch:assert id="a-15588" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHALL contain exactly one [1..1] @value (CONF:15588).</sch:assert>
      <sch:assert id="a-15589-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15589).</sch:assert>
      <sch:assert id="a-15603" test="not(cda:effectiveTime) or cda:effectiveTime[count(cda:low)=1]">The effectiveTime, if present, SHALL contain exactly one [1..1] low (CONF:15603).</sch:assert>
      <sch:assert id="a-15604" test="not(cda:effectiveTime) or cda:effectiveTime[count(cda:high)=1]">The effectiveTime, if present, SHOULD contain zero or one [0..1] high (CONF:15604).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.4-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.4']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.4-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.33-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.33-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.33-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.33']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.33-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.34-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.34-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.34-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.34']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.34-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.35-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.35-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.35-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.35']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.35-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.36-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.36-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.36-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.36']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.36-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.8-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.8-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.8-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.8']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.8-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.9-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.9-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.9-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.9']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.9-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.10-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.10-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.10-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.10']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.10-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.37-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.37-warnings-abstract" abstract="true">
      <sch:assert id="a-16837" test="cda:playingDevice[count(cda:code)=1]">This playingDevice SHOULD contain zero or one [0..1] code (CONF:16837).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.37-warnings" context="cda:participantRole[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.37']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.37-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.2.10-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.2.10-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.2.10-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.2.10']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.2.10-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.3.18-warnings">
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.18-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.18-warnings" context="cda:section[cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.3.18']">
      <sch:extends rule="r-1.3.6.1.4.1.19376.1.5.3.1.3.18-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.11-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.11-warnings-abstract" abstract="true">
      <sch:assert id="a-7820" test="count(cda:entry[count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.35'])=1]) &gt; 0">SHOULD contain zero or more [0..*] entry (CONF:7820) such that it SHALL contain exactly one [1..1] Discharge Medication (templateId:2.16.840.1.113883.10.20.22.4.35) (CONF:15490).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.11-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.11']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.11-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.11.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.11.1-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.11-warnings-abstract" />
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.11.1-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.11.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.11.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.20-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.20-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.20-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.20']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.20-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1-warnings">
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1-warnings" context="cda:section[cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1']">
      <sch:extends rule="r-1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.12-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.12-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.12-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.12']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.12-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.13-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.13-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.13-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.13']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.13-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.3.1-warnings">
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.1-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.1-warnings" context="cda:section[cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.3.1']">
      <sch:extends rule="r-1.3.6.1.4.1.19376.1.5.3.1.3.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.3.4-warnings">
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.4-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.4-warnings" context="cda:section[cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.3.4']">
      <sch:extends rule="r-1.3.6.1.4.1.19376.1.5.3.1.3.4-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.3.5-warnings">
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.5-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.5-warnings" context="cda:section[cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.3.5']">
      <sch:extends rule="r-1.3.6.1.4.1.19376.1.5.3.1.3.5-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.21.2.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.21.2.1-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.21.2.1-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.21.2.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.21.2.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.21.2.2-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.21.2.2-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.21.2.2-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.21.2.2']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.21.2.2-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.16-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.16-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.16-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.16']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.16-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.2-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.2-warnings-abstract" abstract="true">
      <sch:assert id="a-7969" test="count(cda:entry[count(cda:substanceAdministration[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.52'])=1]) &gt; 0">SHOULD contain zero or more [0..*] entry (CONF:7969) such that it SHALL contain exactly one [1..1] Immunization Activity (templateId:2.16.840.1.113883.10.20.22.4.52) (CONF:15494).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.2-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.2']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.2-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.14-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.14-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.14-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.14']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.14-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.18-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.18-warnings-abstract" abstract="true">
      <sch:assert id="a-7959" test="count(cda:entry[count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.60'])=1]) &gt; 0">SHOULD contain zero or more [0..*] entry (CONF:7959) such that it SHALL contain exactly one [1..1] Coverage Activity (templateId:2.16.840.1.113883.10.20.22.4.60) (CONF:15501).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.18-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.18']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.18-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.21-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.21-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.21-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.21']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.21-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.15-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.15-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.15-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.15']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.15-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.17-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.17-warnings-abstract" abstract="true">
      <sch:assert id="a-14823" test="count(cda:entry[count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.78'])=1]) &gt; 0">SHOULD contain zero or more [0..*] entry (CONF:14823) such that it SHALL contain exactly one [1..1] Smoking Status Observation (templateId:2.16.840.1.113883.10.20.22.4.78) (CONF:14824).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.17-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.17']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.17-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.22-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.22-warnings-abstract" abstract="true">
      <sch:assert id="a-7951" test="count(cda:entry[count(cda:encounter[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.49'])=1]) &gt; 0">SHOULD contain zero or more [0..*] entry (CONF:7951) such that it SHALL contain exactly one [1..1] Encounter Activities (templateId:2.16.840.1.113883.10.20.22.4.49) (CONF:15465).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.22-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.22']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.22-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.23-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.23-warnings-abstract" abstract="true">
      <sch:assert id="a-7948" test="count(cda:entry[count(cda:supply[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.50'])=1]) &gt; 0">SHOULD contain zero or more [0..*] entry (CONF:7948) such that it SHALL contain exactly one [1..1] Non-Medicinal Supply Activity (templateId:2.16.840.1.113883.10.20.22.4.50) (CONF:15497).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.23-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.23']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.23-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.3.26-warnings">
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.26-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.26-warnings" context="cda:section[cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.3.26']">
      <sch:extends rule="r-1.3.6.1.4.1.19376.1.5.3.1.3.26-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-1.3.6.1.4.1.19376.1.5.3.1.3.33-warnings">
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.33-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-1.3.6.1.4.1.19376.1.5.3.1.3.33-warnings" context="cda:section[cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.3.33']">
      <sch:extends rule="r-1.3.6.1.4.1.19376.1.5.3.1.3.33-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.24-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.24-warnings-abstract" abstract="true">
      <sch:assert id="a-7983" test="count(cda:entry)=1">SHOULD contain zero or one [0..1] entry (CONF:7983).</sch:assert>
      <sch:assert id="a-15489" test="not(cda:entry) or cda:entry[count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.33'])=1]">The entry, if present, SHALL contain exactly one [1..1] Hospital Discharge Diagnosis (templateId:2.16.840.1.113883.10.20.22.4.33) (CONF:15489).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.24-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.24']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.24-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.2.5-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.2.5-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.2.5-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.2.5']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.2.5-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.12-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.12-warnings-abstract" abstract="true">
      <sch:assert id="a-15599" test="not(cda:participant) or cda:participant[count(cda:participantRole[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.32'])=1]">The participant, if present, SHALL contain exactly one [1..1] Service Delivery Location (templateId:2.16.840.1.113883.10.20.22.4.32) (CONF:15599).</sch:assert>
      <sch:assert id="a-15600" test="not(cda:entryRelationship) or cda:entryRelationship[count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.20'])=1]">The entryRelationship, if present, SHALL contain exactly one [1..1] Instructions (templateId:2.16.840.1.113883.10.20.22.4.20) (CONF:15600).</sch:assert>
      <sch:assert id="a-15601" test="not(cda:entryRelationship) or cda:entryRelationship[count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.19'])=1]">The entryRelationship, if present, SHALL contain exactly one [1..1] Indication (templateId:2.16.840.1.113883.10.20.22.4.19) (CONF:15601).</sch:assert>
      <sch:assert id="a-15602" test="not(cda:entryRelationship) or cda:entryRelationship[count(cda:substanceAdministration[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.16'])=1]">The entryRelationship, if present, SHALL contain exactly one [1..1] Medication Activity (templateId:2.16.840.1.113883.10.20.22.4.16) (CONF:15602).</sch:assert>
      <sch:assert id="a-19186" test="cda:code[count(cda:originalText)=1]">This code SHOULD contain zero or one [0..1] originalText (CONF:19186).</sch:assert>
      <sch:assert id="a-19189-c" test="count(cda:code/cda:originalText/cda:reference[@value])=0 or starts-with(cda:code/cda:originalText/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:19189).</sch:assert>
      <sch:assert id="a-19190-c" test="count(cda:code[@codeSystem])=0 or cda:code[@codeSystem='2.16.840.1.113883.6.1'] or cda:code[@codeSystem='2.16.840.1.113883.6.96'] or cda:code[@codeSystem='2.16.840.1.113883.6.104']">This code in a procedure activity act SHOULD be selected from LOINC (CodeSystem: 2.16.840.1.113883.6.1) or SNOMED CT (CodeSystem: 2.16.840.1.113883.6.96) (CONF:19190).</sch:assert>
      <sch:assert id="a-8330" test="not(cda:entryRelationship) or cda:entryRelationship[@typeCode='COMP']">The entryRelationship, if present, SHALL contain exactly one [1..1] @typeCode="COMP" Has Component (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:8330).</sch:assert>
      <sch:assert id="a-8327" test="not(cda:entryRelationship) or cda:entryRelationship[@typeCode='RSON']">The entryRelationship, if present, SHALL contain exactly one [1..1] @typeCode="RSON" Has Reason (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:8327).</sch:assert>
      <sch:assert id="a-8324" test="not(cda:entryRelationship) or cda:entryRelationship[@inversionInd='true']">The entryRelationship, if present, SHALL contain exactly one [1..1] @inversionInd="true" true (CONF:8324).</sch:assert>
      <sch:assert id="a-8323" test="not(cda:entryRelationship) or cda:entryRelationship[@typeCode='SUBJ']">The entryRelationship, if present, SHALL contain exactly one [1..1] @typeCode="SUBJ" Has Subject (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:8323).</sch:assert>
      <sch:assert id="a-8320" test="not(cda:entryRelationship/cda:encounter) or cda:entryRelationship/cda:encounter[count(cda:id)=1]">This encounter SHALL contain exactly one [1..1] id (CONF:8320).</sch:assert>
      <sch:assert id="a-8319" test="not(cda:entryRelationship/cda:encounter) or cda:entryRelationship/cda:encounter[@moodCode='EVN']">This encounter SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:8319).</sch:assert>
      <sch:assert id="a-8318" test="not(cda:entryRelationship/cda:encounter) or cda:entryRelationship/cda:encounter[@classCode='ENC']">This encounter SHALL contain exactly one [1..1] @classCode="ENC" Encounter (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8318).</sch:assert>
      <sch:assert id="a-8317" test="not(cda:entryRelationship) or cda:entryRelationship[count(cda:encounter)=1]">The entryRelationship, if present, SHALL contain exactly one [1..1] encounter (CONF:8317).</sch:assert>
      <sch:assert id="a-8316" test="not(cda:entryRelationship) or cda:entryRelationship[@inversionInd='true']">The entryRelationship, if present, SHALL contain exactly one [1..1] @inversionInd="true" true (CONF:8316).</sch:assert>
      <sch:assert id="a-8315" test="not(cda:entryRelationship) or cda:entryRelationship[@typeCode='COMP']">The entryRelationship, if present, SHALL contain exactly one [1..1] @typeCode="COMP" Has Component (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:8315).</sch:assert>
      <sch:assert id="a-8312" test="not(cda:participant) or cda:participant[@typeCode='LOC']">The participant, if present, SHALL contain exactly one [1..1] @typeCode="LOC" Location (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:8312).</sch:assert>
      <sch:assert id="a-8310" test="not(cda:performer/cda:assignedEntity/cda:representedOrganization) or cda:performer/cda:assignedEntity/cda:representedOrganization[count(cda:telecom)=1]">The representedOrganization, if present, SHALL contain exactly one [1..1] telecom (CONF:8310).</sch:assert>
      <sch:assert id="a-8309" test="not(cda:performer/cda:assignedEntity/cda:representedOrganization) or cda:performer/cda:assignedEntity/cda:representedOrganization[count(cda:addr)=1]">The representedOrganization, if present, SHALL contain exactly one [1..1] addr (CONF:8309).</sch:assert>
      <sch:assert id="a-8307" test="not(cda:performer/cda:assignedEntity/cda:representedOrganization) or cda:performer/cda:assignedEntity/cda:representedOrganization[count(cda:id) &gt; 0]">The representedOrganization, if present, SHOULD contain zero or more [0..*] id (CONF:8307).</sch:assert>
      <sch:assert id="a-8306" test="not(cda:performer/cda:assignedEntity) or cda:performer/cda:assignedEntity[count(cda:representedOrganization)=1]">This assignedEntity SHOULD contain zero or one [0..1] representedOrganization (CONF:8306).</sch:assert>
      <sch:assert id="a-8305" test="not(cda:performer/cda:assignedEntity) or cda:performer/cda:assignedEntity[count(cda:telecom)=1]">This assignedEntity SHALL contain exactly one [1..1] telecom (CONF:8305).</sch:assert>
      <sch:assert id="a-8304" test="not(cda:performer/cda:assignedEntity) or cda:performer/cda:assignedEntity[count(cda:addr)=1]">This assignedEntity SHALL contain exactly one [1..1] addr (CONF:8304).</sch:assert>
      <sch:assert id="a-8303" test="not(cda:performer/cda:assignedEntity) or cda:performer/cda:assignedEntity[count(cda:id) &gt; 0]">This assignedEntity SHALL contain at least one [1..*] id (CONF:8303).</sch:assert>
      <sch:assert id="a-8302" test="not(cda:performer) or cda:performer[count(cda:assignedEntity)=1]">The performer, if present, SHALL contain exactly one [1..1] assignedEntity (CONF:8302).</sch:assert>
      <sch:assert id="a-8301" test="count(cda:performer) &gt; 0">SHOULD contain zero or more [0..*] performer (CONF:8301).</sch:assert>
      <sch:assert id="a-8299" test="count(cda:effectiveTime)=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:8299).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.12-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.12']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.12-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.13-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.13-warnings-abstract" abstract="true">
      <sch:assert id="a-8246" test="count(cda:effectiveTime)=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:8246).</sch:assert>
      <sch:assert id="a-8250" test="count(cda:targetSiteCode) &gt; 0">SHOULD contain zero or more [0..*] targetSiteCode (CONF:8250).</sch:assert>
      <sch:assert id="a-8251" test="count(cda:performer) &gt; 0">SHOULD contain zero or more [0..*] performer (CONF:8251).</sch:assert>
      <sch:assert id="a-8252" test="not(cda:performer) or cda:performer[count(cda:assignedEntity)=1]">The performer, if present, SHALL contain exactly one [1..1] assignedEntity (CONF:8252).</sch:assert>
      <sch:assert id="a-8253" test="not(cda:performer/cda:assignedEntity) or cda:performer/cda:assignedEntity[count(cda:id) &gt; 0]">This assignedEntity SHALL contain at least one [1..*] id (CONF:8253).</sch:assert>
      <sch:assert id="a-8254" test="not(cda:performer/cda:assignedEntity) or cda:performer/cda:assignedEntity[count(cda:addr)=1]">This assignedEntity SHALL contain exactly one [1..1] addr (CONF:8254).</sch:assert>
      <sch:assert id="a-8255" test="not(cda:performer/cda:assignedEntity) or cda:performer/cda:assignedEntity[count(cda:telecom)=1]">This assignedEntity SHALL contain exactly one [1..1] telecom (CONF:8255).</sch:assert>
      <sch:assert id="a-8256" test="not(cda:performer/cda:assignedEntity) or cda:performer/cda:assignedEntity[count(cda:representedOrganization)=1]">This assignedEntity SHOULD contain zero or one [0..1] representedOrganization (CONF:8256).</sch:assert>
      <sch:assert id="a-8257" test="not(cda:performer/cda:assignedEntity/cda:representedOrganization) or cda:performer/cda:assignedEntity/cda:representedOrganization[count(cda:id) &gt; 0]">The representedOrganization, if present, SHOULD contain zero or more [0..*] id (CONF:8257).</sch:assert>
      <sch:assert id="a-8259" test="not(cda:performer/cda:assignedEntity/cda:representedOrganization) or cda:performer/cda:assignedEntity/cda:representedOrganization[count(cda:addr)=1]">The representedOrganization, if present, SHALL contain exactly one [1..1] addr (CONF:8259).</sch:assert>
      <sch:assert id="a-8260" test="not(cda:performer/cda:assignedEntity/cda:representedOrganization) or cda:performer/cda:assignedEntity/cda:representedOrganization[count(cda:telecom)=1]">The representedOrganization, if present, SHALL contain exactly one [1..1] telecom (CONF:8260).</sch:assert>
      <sch:assert id="a-8262" test="not(cda:participant) or cda:participant[@typeCode='LOC']">The participant, if present, SHALL contain exactly one [1..1] @typeCode="LOC" Location (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:8262).</sch:assert>
      <sch:assert id="a-8265" test="not(cda:entryRelationship) or cda:entryRelationship[@typeCode='COMP']">The entryRelationship, if present, SHALL contain exactly one [1..1] @typeCode="COMP" Component (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:8265).</sch:assert>
      <sch:assert id="a-8266" test="not(cda:entryRelationship) or cda:entryRelationship[@inversionInd='true']">The entryRelationship, if present, SHALL contain exactly one [1..1] @inversionInd="true" true (CONF:8266).</sch:assert>
      <sch:assert id="a-8267" test="not(cda:entryRelationship) or cda:entryRelationship[count(cda:encounter)=1]">The entryRelationship, if present, SHALL contain exactly one [1..1] encounter (CONF:8267).</sch:assert>
      <sch:assert id="a-8268" test="not(cda:entryRelationship/cda:encounter) or cda:entryRelationship/cda:encounter[@classCode='ENC']">This encounter SHALL contain exactly one [1..1] @classCode="ENC" Encounter (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:8268).</sch:assert>
      <sch:assert id="a-8269" test="not(cda:entryRelationship/cda:encounter) or cda:entryRelationship/cda:encounter[@moodCode='EVN']">This encounter SHALL contain exactly one [1..1] @moodCode="EVN" Event (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:8269).</sch:assert>
      <sch:assert id="a-8270" test="not(cda:entryRelationship/cda:encounter) or cda:entryRelationship/cda:encounter[count(cda:id)=1]">This encounter SHALL contain exactly one [1..1] id (CONF:8270).</sch:assert>
      <sch:assert id="a-15904" test="not(cda:participant) or cda:participant[count(cda:participantRole[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.32'])=1]">The participant, if present, SHALL contain exactly one [1..1] Service Delivery Location (templateId:2.16.840.1.113883.10.20.22.4.32) (CONF:15904).</sch:assert>
      <sch:assert id="a-16071" test="not(cda:targetSiteCode) or cda:targetSiteCode[@code]">The targetSiteCode, if present, SHALL contain exactly one [1..1] @code, which SHALL be selected from ValueSet Body Site Value Set 2.16.840.1.113883.3.88.12.3221.8.9 DYNAMIC (CONF:16071).</sch:assert>
      <sch:assert id="a-19198" test="cda:code[count(cda:originalText)=1]">This code SHOULD contain zero or one [0..1] originalText (CONF:19198).</sch:assert>
      <sch:assert id="a-19199" test="not(cda:code/cda:originalText) or cda:code/cda:originalText[count(cda:reference)=1]">The originalText, if present, SHOULD contain zero or one [0..1] reference (CONF:19199).</sch:assert>
      <sch:assert id="a-19200" test="not(cda:code/cda:originalText/cda:reference) or cda:code/cda:originalText/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:19200).</sch:assert>
      <sch:assert id="a-19201-c" test="count(cda:code/cda:originalText/cda:reference[@value])=0 or starts-with(cda:code/cda:originalText/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:19201).</sch:assert>
      <sch:assert id="a-19202-c" test="count(cda:code[@codeSystem])=0 or cda:code[@codeSystem='2.16.840.1.113883.6.1'] or cda:code[@codeSystem='2.16.840.1.113883.6.96']">This @code SHOULD be selected from LOINC (CodeSystem: 2.16.840.1.113883.6.1) or SNOMED CT (CodeSystem: 2.16.840.1.113883.6.96), and MAY be selected from CPT-4 (CodeSystem: 2.16.840.1.113883.6.12), ICD10 PCS (CodeSystem: 2.16.840.1.113883.6.4) (CONF:19202).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.13-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.13']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.13-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.26-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.26-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.26-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.26']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.26-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.32-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.32-warnings-abstract" abstract="true">
      <sch:assert id="a-8050" test="cda:entry[count(*[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.4'])=1]">Such entries SHALL contain exactly one [1..1] Problem Observation (templateId:2.16.840.1.113883.10.20.22.4.4) (CONF:8050).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.32-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.32']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.32-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.7.12-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.7.12-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.7.12-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.7.12']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.7.12-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.7.14-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.7.14-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.7.14-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.7.14']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.7.14-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.7.13-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.7.13-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.7.13-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.7.13']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.7.13-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.33-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.33-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.33-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.33']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.33-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.29-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.29-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.29-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.29']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.29-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.27-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.27-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.27-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.27']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.27-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.25-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.25-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.25-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.25']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.25-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.18.2.12-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.18.2.12-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.18.2.12-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.18.2.12']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.18.2.12-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.18.2.9-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.18.2.9-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.18.2.9-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.18.2.9']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.18.2.9-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.28-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.28-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.28-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.28']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.28-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.30-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.30-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.30-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.30']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.30-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.31-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.31-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.31-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.31']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.31-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.34-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.34-warnings-abstract" abstract="true">
      <sch:assert id="a-10096" test="count(cda:entry[count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.65'])=1])=1">SHOULD contain zero or one [0..1] entry (CONF:10096) such that it SHALL contain exactly one [1..1] Preoperative Diagnosis (templateId:2.16.840.1.113883.10.20.22.4.65) (CONF:15504).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.34-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.34']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.34-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.35-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.35-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.35-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.35']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.35-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.38-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.38-warnings-abstract" abstract="true">
      <sch:assert id="a-15499" test="not(cda:entry) or cda:entry[count(cda:substanceAdministration[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.16'])=1]">The entry, if present, SHALL contain exactly one [1..1] Medication Activity (templateId:2.16.840.1.113883.10.20.22.4.16) (CONF:15499).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.38-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.38']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.38-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.39-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.39-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.39-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.39']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.39-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.36-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.36-warnings-abstract" abstract="true">
      <sch:assert id="a-8762" test="count(cda:entry[count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.51'])=1])=1">SHOULD contain zero or one [0..1] entry (CONF:8762) such that it SHALL contain exactly one [1..1] Postprocedure Diagnosis (templateId:2.16.840.1.113883.10.20.22.4.51) (CONF:15503).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.36-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.36']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.36-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.37-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.37-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.37-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.37']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.37-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.40-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.40-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.40-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.40']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.40-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.3-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.3-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-warnings-abstract" />
      <sch:assert id="a-8343-c" test="not(cda:componentOf/cda:encompassingEncounter/cda:encounterParticipant) or cda:componentOf/cda:encompassingEncounter/cda:encounterParticipant/cda:assignedEntity[cda:assignedPerson | cda:representedOrganization]">An encounterParticipant element, if present, SHALL contain an assignedEntity element, which SHALL contain an assignedPerson element, a representedOrganization element, or both (CONF:8343).</sch:assert>
      <sch:assert id="a-8348-c" test="not(cda:componentOf/cda:encompassingEncounter/cda:responsibleParty) or cda:componentOf/cda:encompassingEncounter/cda:responsibleParty/cda:assignedEntity[cda:assignedPerson | cda:representedOrganization]">The responsibleParty element, if present, SHALL contain an assignedEntity element, which SHALL contain an assignedPerson element, a representedOrganization element, or both (CONF:8348).</sch:assert>
      <sch:assert id="a-8352-c" test=".">A History and Physical document can conform to CDA Level 1 (nonXMLBody), CDA Level 2 (structuredBody with sections that contain a narrative block), or CDA Level 3 (structuredBody containing sections that contain a narrative block and coded entries). In this template (templateId 2.16.840.1.113883.10.20.22.1.3), coded entries are optional (CONF:8352).</sch:assert>
      <sch:assert id="a-9597-c" test=".">If structuredBody, the component/structuredBody SHALL conform to the section constraints below (CONF:9597).</sch:assert>
      <sch:assert id="a-9621-c" test="//cda:section/cda:templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.3.4']">SHOULD contain zero or one [0..1] History of Present Illness Section (templateId:1.3.6.1.4.1.19376.1.5.3.1.3.4) (CONF:9621).</sch:assert>
      <sch:assert id="a-9986-c" test="count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=2 or (count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.9'])=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=0)">SHALL include an Assessment and Plan Section, or an Assessment Section and a Plan Section (CONF:9986).</sch:assert>
      <sch:assert id="a-10056-c" test="count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=2 or (count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.9'])=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.8'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.10'])=0)">SHALL NOT include an Assessment/Plan Section when an Assessment Section and a Plan of Care Section are present (CONF:10056).</sch:assert>
      <sch:assert id="a-10057-c" test="not(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.13'] and &#xA;          (//cda:templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1'] or&#xA;           //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.12']))">SHALL NOT contain a Chief Complaint and Reason for Visit Section when either a Chief Complaint Section or a Reason for Visit Section is present (CONF:10057).</sch:assert>
      <sch:assert id="a-9642-c" test="//cda:section/cda:templateId/@root='1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1' or //cda:section/cda:templateId/@root='2.16.840.1.113883.10.20.22.2.12' or //cda:section/cda:templateId/@root='2.16.840.1.113883.10.20.22.2.13'">SHALL include a Chief Complaint and Reason for Visit Section, Chief Complaint Section, or a Reason for Visit Section (CONF:9642).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.3-warnings" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.3']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.3-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.5-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.5-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-warnings-abstract" />
      <sch:assert id="a-10031-c" test="cda:id[contains(@root,'.') and (starts-with(@root,'0.') or starts-with(@root,'1.') or starts-with(@root,'2.'))]">OIDs SHALL be represented in dotted decimal notation, where each decimal number is either 0 or starts with a nonzero digit. More formally, an OID SHALL be in the form ([0-2])(.([1-9][0-9]*|0))+ (CONF:10031).</sch:assert>
      <sch:assert id="a-10032-c" test="string-length(cda:id/@root)&lt;65">OIDs SHALL be no more than 64 characters in length (CONF:10032).</sch:assert>
      <sch:assert id="a-10133-c" test="not(cda:relatedDocument/cda:id/cda:componentOf) or string-length(cda:relatedDocument/cda:id/cda:componentOf/cda:encompassingEncounter/cda:effectiveTime//@value)&gt;=8">The content of effectiveTime SHALL be a conformant US Realm Date and Time (DT.US.FIELDED) (2.16.840.1.113883.10.20.22.5.4) (CONF:10133).</sch:assert>
      <sch:assert id="a-10478-c" test="cda:given|cda:family or (count(*)=0 and string-length(.)!=0)">The content of name SHALL be a conformant US Realm Person Name (PN.US.FIELDED) (2.16.840.1.113883.10.20.22.5.1.1) (CONF:10478).</sch:assert>
      <sch:assert id="a-14834" test="cda:code[@code]">This code SHOULD contain zero or one [0..1] @code, which SHOULD be selected from ValueSet DIRDocumentTypeCodes 2.16.840.1.113883.11.20.9.32 DYNAMIC (CONF:14834).</sch:assert>
      <sch:assert id="a-15141-c" test="count(//cda:section/cda:templateId[@root='2.16.840.1.113883.10.20.6.1.1'])&lt;2">SHOULD contain zero or one [0..1] DICOM Object Catalog Section - DCM 121181 (templateId:2.16.840.1.113883.10.20.6.1.1) (CONF:15141).</sch:assert>
      <sch:assert id="a-8448-c" test="not(cda:componentOf) or cda:componentOf/cda:encompassingEncounter/cda:templateId['2.16.840.1.113883.10.20.6.2.2']">This encompassingEncounter SHOULD contain zero or one [0..1] Physician of Record Participant (templateId:2.16.840.1.113883.10.20.6.2.2) (CONF:8448).</sch:assert>
      <sch:assert id="a-8439-c" test="count(cda:assignedPerson | cda:representedOrganization)&lt;2">SHOULD contain zero or one [0..1] assignedPerson OR SHOULD contain zero or one [0..1] representedOrganization (CONF:8439).</sch:assert>
      <sch:assert id="a-8436-c" test=".">In the case of transformed DICOM SR documents, an appropriate null flavor MAY be used if the id is unavailable (CONF:8436).</sch:assert>
      <sch:assert id="a-8420-c" test=".">The value of serviceEvent/code SHALL NOT conflict with the ClininicalDocument/code. When transforming from DICOM SR documents that do not contain a procedure code, an appropriate nullFlavor SHALL be used on serviceEvent/code (CONF:8420).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.5-warnings" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.5']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.5-warnings-abstract" />
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.5-8432-branch-8432-warnings-abstract" abstract="true">
      <sch:assert id="a-9407-branch-8432" test="not(cda:id/cda:componentOf/cda:encompassingEncounter/cda:responsibleParty) or cda:id/cda:componentOf/cda:encompassingEncounter/cda:responsibleParty[count(cda:assignedEntity)=1]">The responsibleParty, if present, SHALL contain exactly one [1..1] assignedEntity (CONF:9407).</sch:assert>
      <sch:assert id="a-8449-branch-8432" test="not(cda:id/cda:componentOf) or cda:id/cda:componentOf[count(cda:encompassingEncounter)=1]">The componentOf, if present, SHALL contain exactly one [1..1] encompassingEncounter (CONF:8449).</sch:assert>
      <sch:assert id="a-8437-branch-8432" test="not(cda:id/cda:componentOf/cda:encompassingEncounter) or cda:id/cda:componentOf/cda:encompassingEncounter[count(cda:effectiveTime)=1]">This encompassingEncounter SHALL contain exactly one [1..1] effectiveTime (CONF:8437).</sch:assert>
      <sch:assert id="a-8435-branch-8432" test="not(cda:id/cda:componentOf/cda:encompassingEncounter) or cda:id/cda:componentOf/cda:encompassingEncounter[count(cda:id) &gt; 0]">This encompassingEncounter SHALL contain at least one [1..*] id (CONF:8435).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.5-8432-branch-8432-warnings" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.5']/cda:relatedDocument[cda:id]">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.5-8432-branch-8432-warnings-abstract" />
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.5-8416-branch-8416-warnings-abstract" abstract="true">
      <sch:assert id="a-8422-branch-8416" test="cda:serviceEvent[count(cda:performer[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.1']) &gt; 0]">This serviceEvent SHOULD contain zero or more [0..*] Physician Reading Study Performer (templateId:2.16.840.1.113883.10.20.6.2.1) (CONF:8422).</sch:assert>
      <sch:assert id="a-8418-branch-8416" test="cda:serviceEvent[count(cda:id) &gt; 0]">This serviceEvent SHOULD contain zero or more [0..*] id (CONF:8418).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.5-8416-branch-8416-warnings" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.5']/cda:documentationOf[cda:serviceEvent[@classCode='ACT'][cda:code]]">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.5-8416-branch-8416-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.4-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.4-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-warnings-abstract" />
      <sch:assert id="a-8394-c" test="not(cda:componentOf/cda:encompassingEncounter/cda:responsibleParty) or cda:componentOf/cda:encompassingEncounter/cda:responsibleParty/cda:assignedEntity[cda:assignedPerson or cda:representedOrganization]">The responsibleParty element, if present, SHALL contain an assignedEntity element which SHALL contain an assignedPerson element, a representedOrganization element, or both (CONF:8394).</sch:assert>
      <sch:assert id="a-8396-c" test="not(cda:componentOf/cda:encompassingEncounter/cda:encounterParticipant) or cda:componentOf/cda:encompassingEncounter/cda:encounterParticipant/cda:assignedEntity[cda:assignedPerson or cda:representedOrganization]">An encounterParticipant element, if present, SHALL contain an assignedEntity element which SHALL contain an assignedPerson element,  a representedOrganization element, or both (CONF:8396).</sch:assert>
      <sch:assert id="a-8399-c" test=".">A Consultation Note can conform to CDA Level 1 (nonXMLBody), CDA Level 2 (structuredBody with sections that contain a narrative block), or CDA Level 3 (structuredBody containing sections that contain a narrative block and coded entries). In this template (templateId 2.16.840.1.113883.10.20.22.1.4), coded entries are optional (CONF:8399).</sch:assert>
      <sch:assert id="a-9495-c" test="count(//cda:section/cda:templateId[@root='2.16.840.1.113883.10.20.2.10'])=1">SHOULD contain zero or one [0..1] Physical Exam Section (templateId:2.16.840.1.113883.10.20.2.10) (CONF:9495).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.4-warnings" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.4']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.4-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.1-warnings-abstract" abstract="true">
      <sch:assert id="a-8428-c" test=".">SHALL contain a valid DICOM personal identification code sequence (@codeSystem is 1.2.840.10008.2.16.4) or an appropriate national health care provider coding system (e.g., NUCC in the U.S., where @codeSystem is 2.16.840.1.113883.6.101) (CONF:8428).</sch:assert>
      <sch:assert id="a-8429-c" test="cda:assignedPerson | cda:representedOrganization">Every assignedEntity element SHALL have at least one assignedPerson or representedOrganization (CONF:8429).</sch:assert>
      <sch:assert id="a-10034-c" test="@root='2.16.840.1.113883.4.6'">The id SHOULD include zero or one [0..1] id where id/@root ="2.16.840.1.113883.4.6" National Provider Identifier (CONF:10034).</sch:assert>
      <sch:assert id="a-10134-c" test="not(cda:time) or string-length(cda:time//@value) &gt;= 8">The content of time SHALL be a conformant US Realm Date and Time (DTM.US.FIELDED) (2.16.840.1.113883.10.20.22.5.3) (CONF:10134).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.1-warnings" context="cda:Performer1[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.2-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.2-warnings-abstract" abstract="true">
      <sch:assert id="a-8889-c" test=".">SHALL contain a valid DICOM Organizational Role from DICOM CID 7452  (Value Set 1.2.840.10008.6.1.516)(@codeSystem is 1.2.840.10008.2.16.4) or an appropriate national health care provider coding system (e.g., NUCC in the U.S., where @codeSystem is 2.16.840.1.113883.6.101)Footnote: DICOM Part 16 (NEMA PS3.16), page 631 in the 2011 edition. See ftp://medical.nema.org/medical/dicom/2011/11_16pu.pdf (CONF:8889).</sch:assert>
      <sch:assert id="a-8890" test="cda:assignedEntity[count(cda:name)=1]">This assignedEntity SHOULD contain zero or one [0..1] name (CONF:8890).</sch:assert>
      <sch:assert id="a-10035-c" test="@root ='2.16.840.1.113883.4.6'">The id SHOULD include zero or one [0..1] id where id/@root ="2.16.840.1.113883.4.6" National Provider Identifier (CONF:10035).</sch:assert>
      <sch:assert id="a-16075" test="not(cda:assignedEntity/cda:representedOrganization) or cda:assignedEntity/cda:representedOrganization[count(cda:name)=1]">The representedOrganization, if present, SHOULD contain zero or one [0..1] name (CONF:16075).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.2-warnings" context="cda:encounterParticipant[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.2']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.2-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.2-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.2-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-warnings-abstract" />
      <sch:assert id="a-8458" test="not(cda:documentationOf/cda:serviceEvent/cda:performer) or cda:documentationOf/cda:serviceEvent/cda:performer[@typeCode='PRF']">The performer, if present, SHALL contain exactly one [1..1] @typeCode="PRF" Participation physical performer (CodeSystem: HL7ParticipationType 2.16.840.1.113883.5.90 STATIC) (CONF:8458).</sch:assert>
      <sch:assert id="a-8460" test="not(cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity) or cda:documentationOf/cda:serviceEvent/cda:performer/cda:assignedEntity[count(cda:id) &gt; 0]">The assignedEntity, if present, SHALL contain at least one [1..*] id (CONF:8460).</sch:assert>
      <sch:assert id="a-8482" test="cda:documentationOf/cda:serviceEvent[count(cda:performer) &gt; 0]">This serviceEvent SHOULD contain zero or more [0..*] performer (CONF:8482).</sch:assert>
      <sch:assert id="a-9451-c" test="count(//cda:section/cda:templateId[@root='2.16.840.1.113883.10.20.22.2.7.1'])=1">SHOULD contain zero or one [0..1] Procedures Section (entries required) (templateId:2.16.840.1.113883.10.20.22.2.7.1) (CONF:9451).</sch:assert>
      <sch:assert id="a-10026-c" test=".">ServiceEvent/performer represents the healthcare providers involved in the current or pertinent historical care of the patient. Preferably, the patient?s key healthcare providers would be listed, particularly their primary physician and any active consulting physicians, therapists, and counselors (CONF:10026).</sch:assert>
      <sch:assert id="a-10027-c" test="@root='2.16.840.1.113883.4.6'">SHOULD include zero or one [0..1] id where id/@root ="2.16.840.1.113883.4.6" National Provider Identifier (CONF:10027).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.2-warnings" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.2']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.2-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.8-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.8-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-warnings-abstract" />
      <sch:assert id="a-8476-c" test=".">The dischargeDispositionCode SHALL be present where the value of code SHOULD be selected from ValueSet 2.16.840.1.113883.3.88.12.80.33 NUBC UB-04 FL17-Patient Status DYNAMIC (www.nubc.org) (CONF:8476).</sch:assert>
      <sch:assert id="a-8477-c" test=".">The dischargeDispositionCode, @displayName, or NUBC UB-04 Print Name, SHALL be displayed when the document is rendered (CONF:8477).</sch:assert>
      <sch:assert id="a-8478-c" test=".">The encounterParticipant elements MAY be present. If present, the encounterParticipant/assignedEntity element SHALL have at least one assignedPerson or representedOrganization element present (CONF:8478).</sch:assert>
      <sch:assert id="a-8479-c" test=".">The responsibleParty element MAY be present. If present, the responsibleParty/assignedEntity element SHALL have at least one assignedPerson or representedOrganization element present (CONF:8479).</sch:assert>
      <sch:assert id="a-9538-c" test=".">A Discharge Summary can conform to CDA Level 1 (nonXMLBody), CDA Level 2 (structuredBody with sections that contain a narrative block), or CDA Level 3 (structuredBody containing sections that contain a narrative block and coded entries). In this template (templateId 2.16.840.1.113883.10.20.22.1.8), coded entries are optional (CONF:9538).</sch:assert>
      <sch:assert id="a-9540-c" test=".">If structuredBody, the component/structuredBody SHALL conform to the section constraints below (CONF:9540).</sch:assert>
      <sch:assert id="a-10055-c" test="count(//cda:templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1'])&lt;=1 and count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.12'])&lt;=1 and count(//cda:templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.12'])=2 or (count(//cda:templateId[@root='2.16.840.1.113883.10.20.22.2.13'])=1 and count(//cda:templateId[@root='1.3.6.1.4.1.19376.1.5.3.1.1.13.2.1'] | //cda:templateId[@root='2.16.840.1.113883.10.20.22.2.12'])=0)">SHALL NOT include a Chief Complaint and Reason for Visit Section with either a Chief Complaint Section or a Reason for Visit Section (CONF:10055).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.8-warnings" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.8']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.8-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.7-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.7-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-warnings-abstract" />
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.7-warnings" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.7']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.7-warnings-abstract" />
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.7-8489-branch-8489-warnings-abstract" abstract="true">
      <sch:assert id="a-8491-branch-8489" test="cda:assignedEntity/cda:code[@code]">This code SHOULD contain zero or one [0..1] @code, which SHOULD be selected from ValueSet Provider Role Value Set 2.16.840.1.113883.3.88.12.3221.4 DYNAMIC (CONF:8491).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.7-8489-branch-8489-warnings" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.7']/cda:documentationOf[cda:serviceEvent][cda:performer[@typeCode='PPRF'][cda:assignedEntity[cda:code]]]">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.7-8489-branch-8489-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.1.6-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.6-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.1-warnings-abstract" />
      <sch:assert id="a-8499" test="count(cda:componentOf[cda:encompassingEncounter])=1">SHOULD contain zero or one [0..1] componentOf/encompassingEncounter (CONF:8499).</sch:assert>
      <sch:assert id="a-8500" test="not(cda:componentOf/cda:encompassingEncounter) or cda:componentOf/cda:encompassingEncounter[count(cda:id) &gt; 0]">The componentOf/encompassingEncounter, if present, SHALL contain at least one [1..*] location/healthCareFacility/id (CONF:8500).</sch:assert>
      <sch:assert id="a-8501" test="not(cda:componentOf/cda:encompassingEncounter) or cda:componentOf/cda:encompassingEncounter[count(cda:code)=1]">The componentOf/encompassingEncounter, if present, SHALL contain exactly one [1..1] code (CONF:8501).</sch:assert>
      <sch:assert id="a-9937-c" test="count(//cda:section[not (cda:title)])=0 and not(//cda:section/cda:title[string-length()=0])">Each section SHALL have a title and the title SHALL NOT be empty (CONF:9937).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.6-warnings" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.6']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.6-warnings-abstract" />
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.6-8510-branch-8510-warnings-abstract" abstract="true">
      <sch:assert id="a-14912-branch-8510" test="cda:serviceEvent/cda:performer/cda:assignedEntity[count(cda:code)=1]">This assignedEntity SHOULD contain zero or one [0..1] code (CONF:14912).</sch:assert>
      <sch:assert id="a-14913-branch-8510" test="not(cda:serviceEvent/cda:performer/cda:assignedEntity/cda:code) or cda:serviceEvent/cda:performer/cda:assignedEntity/cda:code[@code]">The code, if present, SHOULD contain zero or one [0..1] @code, which SHALL be selected from ValueSet Healthcare Provider Taxonomy (HIPAA) 2.16.840.1.114222.4.11.1066 DYNAMIC (CONF:14913).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.1.6-8510-branch-8510-warnings" context="cda:ClinicalDocument[cda:templateId/@root='2.16.840.1.113883.10.20.22.1.6']/cda:documentationOf[cda:serviceEvent[cda:performer[@typeCode='PPRF'][cda:assignedEntity]][cda:effectiveTime[cda:low]]]">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.1.6-8510-branch-8510-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.1.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.1.1-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.1.1-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.6.1.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.1.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.6-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.6-warnings-abstract" abstract="true">
      <sch:assert id="a-9216" test="count(cda:effectiveTime)=1">If present, the effectiveTime contains the time the study was started
SHOULD contain zero or one [0..1] effectiveTime (CONF:9216).</sch:assert>
      <sch:assert id="a-15995" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:15995).</sch:assert>
      <sch:assert id="a-15996" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15996).</sch:assert>
      <sch:assert id="a-15997-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15997).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.6-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.6']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.6-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.1.2-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.1.2-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.1.2-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.6.1.2']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.1.2-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.38-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.38-warnings-abstract" abstract="true">
      <sch:assert id="a-8555-c" test=".">Observation/value can be any data type. Where Observation/value is a physical quantity, the unit of measure SHALL be expressed using a valid Unified Code for Units of Measure (UCUM) expression (CONF:8555).</sch:assert>
      <sch:assert id="a-8559" test="count(cda:value)=1">SHOULD contain zero or one [0..1] value (CONF:8559).</sch:assert>
      <sch:assert id="a-19220" test="cda:code[@code and @code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.3.88.12.80.60']/voc:code/@value]">This code SHOULD contain zero or one [0..1] @code, which SHOULD be selected from ValueSet Social History Type Value Set 2.16.840.1.113883.3.88.12.80.60 STATIC (CONF:19220).</sch:assert>
      <sch:assert id="a-19221" test="cda:code[count(cda:originalText)=1]">This code SHOULD contain zero or one [0..1] originalText (CONF:19221).</sch:assert>
      <sch:assert id="a-19222" test="not(cda:code/cda:originalText) or cda:code/cda:originalText[count(cda:reference)=1]">The originalText, if present, SHOULD contain zero or one [0..1] reference (CONF:19222).</sch:assert>
      <sch:assert id="a-19223" test="not(cda:code/cda:originalText/cda:reference) or cda:code/cda:originalText/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:19223).</sch:assert>
      <sch:assert id="a-19224-c" test="count(cda:code/cda:originalText/cda:reference[@value])=0 or starts-with(cda:code/cda:originalText/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:19224).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.38-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.38']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.38-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.39-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.39-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.39-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.39']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.39-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.40-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.40-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.40-warnings" context="cda:encounter[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.40']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.40-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.41-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.41-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.41-warnings" context="cda:procedure[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.41']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.41-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.42-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.42-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.42-warnings" context="cda:substanceAdministration[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.42']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.42-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.43-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.43-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.43-warnings" context="cda:supply[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.43']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.43-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.44-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.44-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.44-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.44']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.44-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.46-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.46-warnings-abstract" abstract="true">
      <sch:assert id="a-8593" test="count(cda:effectiveTime)=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:8593).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.46-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.46']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.46-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.45-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.45-warnings-abstract" abstract="true">
      <sch:assert id="a-15248" test="cda:subject/cda:relatedSubject[count(cda:subject)=1]">This relatedSubject SHOULD contain zero or one [0..1] subject (CONF:15248).</sch:assert>
      <sch:assert id="a-15249-c" test="count(cda:subject/cda:relatedSubject/cda:subject/sdtc:id)=1">The subject SHOULD contain zero or more [0..*] sdtc:id. The prefix sdtc: SHALL be bound to the namespace ?urn:hl7-org:sdtc?. The use of the namespace provides a necessary extension to CDA R2 for the use of the id element (CONF:15249).</sch:assert>
      <sch:assert id="a-15974" test="not(cda:subject/cda:relatedSubject/cda:subject) or cda:subject/cda:relatedSubject/cda:subject[count(cda:administrativeGenderCode)=1]">The subject, if present, SHALL contain exactly one [1..1] administrativeGenderCode (CONF:15974).</sch:assert>
      <sch:assert id="a-15975" test="not(cda:subject/cda:relatedSubject/cda:subject/cda:administrativeGenderCode) or cda:subject/cda:relatedSubject/cda:subject/cda:administrativeGenderCode[@code and @code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.1.11.1']/voc:code/@value]">This administrativeGenderCode SHALL contain exactly one [1..1] @code, which SHALL be selected from ValueSet Administrative Gender (HL7 V3) 2.16.840.1.113883.1.11.1 STATIC (CONF:15975).</sch:assert>
      <sch:assert id="a-15976" test="not(cda:subject/cda:relatedSubject/cda:subject) or cda:subject/cda:relatedSubject/cda:subject[count(cda:birthTime)=1]">The subject, if present, SHOULD contain zero or one [0..1] birthTime (CONF:15976).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.45-warnings" context="cda:organizer[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.45']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.45-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.47-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.47-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.47-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.47']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.47-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.21.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.21.1-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.21-warnings-abstract" />
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.21.1-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.21.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.21.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.48-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.48-warnings-abstract" abstract="true">
      <sch:assert id="a-8662" test="count(cda:participant[@typeCode='VRF'][count(cda:templateId[@root='2.16.840.1.113883.10.20.1.58'])=1][count(cda:participantRole)=1]) &gt; 0">SHOULD contain zero or more [0..*] participant (CONF:8662) such that it SHALL contain exactly one [1..1] @typeCode="VRF" Verifier (CodeSystem: HL7ParticipationType 2.16.840.1.113883.5.90 STATIC) (CONF:8663). SHALL contain exactly one [1..1] templateId (CONF:8664). This templateId SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.1.58" (CONF:10486). SHALL contain exactly one [1..1] participantRole (CONF:8825).</sch:assert>
      <sch:assert id="a-8666-c" test=".">The data type of Observation/participant/time in a verification SHALL be TS (time stamp) (CONF:8666).</sch:assert>
      <sch:assert id="a-8667" test="count(cda:participant[@typeCode='CST'][count(cda:participantRole[@classCode='AGNT'][count(cda:playingEntity[count(cda:name)=1])=1])=1])=1">SHOULD contain zero or one [0..1] participant (CONF:8667) such that it SHALL contain exactly one [1..1] @typeCode="CST" Custodian (CodeSystem: HL7ParticipationType 2.16.840.1.113883.5.90 STATIC) (CONF:8668). SHALL contain exactly one [1..1] participantRole (CONF:8669). This participantRole SHALL contain exactly one [1..1] @classCode="AGNT" Agent (CodeSystem: RoleClass 2.16.840.1.113883.5.110 STATIC) (CONF:8670). This participantRole SHALL contain exactly one [1..1] playingEntity (CONF:8824). This playingEntity SHALL contain exactly one [1..1] name (CONF:8673).</sch:assert>
      <sch:assert id="a-8674-c" test=".">The name of the agent who can provide a copy of the Advance Directive SHALL be recorded in the  name element inside the  playingEntity  element (CONF:8674).</sch:assert>
      <sch:assert id="a-8692" test="count(cda:reference[@typeCode='REFR'][count(cda:externalDocument[count(cda:id) &gt; 0])=1]) &gt; 0">SHOULD contain zero or more [0..*] reference (CONF:8692) such that it SHALL contain exactly one [1..1] externalDocument (CONF:8693). This externalDocument SHALL contain at least one [1..*] id (CONF:8695). SHALL contain exactly one [1..1] @typeCode="REFR" Refers to (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:8694).</sch:assert>
      <sch:assert id="a-8698-c" test=".">The URL of a referenced advance directive document MAY be present, and SHALL be represented in Observation/reference/ExternalDocument/text/reference (CONF:8698).</sch:assert>
      <sch:assert id="a-8699-c" test=".">If a URL is referenced, then it SHOULD have a corresponding linkHTML element in narrative block (CONF:8699).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.48-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.48']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.48-warnings-abstract" />
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.48-8662-branch-8662-warnings-abstract" abstract="true">
      <sch:assert id="a-8665-branch-8662" test="count(cda:time)=1">SHOULD contain zero or one [0..1] time (CONF:8665).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.48-8662-branch-8662-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.48']/cda:participant[@typeCode='VRF'][cda:templateId[@root='2.16.840.1.113883.10.20.1.58']][cda:participantRole]">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.48-8662-branch-8662-warnings-abstract" />
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.48-8667-branch-8667-warnings-abstract" abstract="true">
      <sch:assert id="a-8671-branch-8667" test="not(cda:participantRole) or cda:participantRole[count(cda:addr)=1]">This participantRole SHOULD contain zero or one [0..1] addr (CONF:8671).</sch:assert>
      <sch:assert id="a-8672-branch-8667" test="not(cda:participantRole) or cda:participantRole[count(cda:telecom)=1]">This participantRole SHOULD contain zero or one [0..1] telecom (CONF:8672).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.48-8667-branch-8667-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.48']/cda:participant[@typeCode='CST'][cda:participantRole[@classCode='AGNT'][cda:playingEntity[cda:name]]]">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.48-8667-branch-8667-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.21.2.3-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.21.2.3-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.21.2.3-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.21.2.3']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.21.2.3-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.22.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.22.1-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.22-warnings-abstract" />
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.22.1-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.22.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.22.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.49-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.49-warnings-abstract" abstract="true">
      <sch:assert id="a-8714" test="count(cda:code)=1">SHOULD contain zero or one [0..1] code, which SHOULD be selected from ValueSet EncounterTypeCode 2.16.840.1.113883.3.88.12.80.32 DYNAMIC (CONF:8714).</sch:assert>
      <sch:assert id="a-8719" test="not(cda:code) or cda:code[count(cda:originalText)=1]">The code, if present, SHOULD contain zero or one [0..1] originalText (CONF:8719).</sch:assert>
      <sch:assert id="a-8720-c" test="not(tested-here)">The originalText, if present, SHOULD contain zero or one [0..1] reference/@value (CONF:8720).</sch:assert>
      <sch:assert id="a-8726" test="not(cda:performer) or cda:performer[count(cda:assignedEntity)=1]">The performer, if present, SHALL contain exactly one [1..1] assignedEntity (CONF:8726).</sch:assert>
      <sch:assert id="a-15970" test="not(cda:code/cda:originalText) or cda:code/cda:originalText[count(cda:reference)=1]">The originalText, if present, SHOULD contain zero or one [0..1] reference (CONF:15970).</sch:assert>
      <sch:assert id="a-15971" test="not(cda:code/cda:originalText/cda:reference) or cda:code/cda:originalText/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15971).</sch:assert>
      <sch:assert id="a-15972-c" test="count(cda:code/cda:originalText/cda:reference[@value])=0 or starts-with(cda:code/cda:originalText/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15972).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.49-warnings" context="cda:encounter[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.49']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.49-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.50-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.50-warnings-abstract" abstract="true">
      <sch:assert id="a-8751" test="count(cda:quantity)=1">SHOULD contain zero or one [0..1] quantity (CONF:8751).</sch:assert>
      <sch:assert id="a-15498" test="count(cda:effectiveTime[@xsi:type='IVL_TS'])=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:15498).</sch:assert>
      <sch:assert id="a-16867-c" test="not(cda:effectiveTime) or cda:effectiveTime[count(cda:high)=1]">The effectiveTime, if present, SHOULD contain zero or one [0..1] high (CONF:16867).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.50-warnings" context="cda:supply[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.50']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.50-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.51-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.51-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.51-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.51']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.51-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.52-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.52-warnings-abstract" abstract="true">
      <sch:assert id="a-8831" test="count(cda:text)=1">SHOULD contain zero or one [0..1] text (CONF:8831).</sch:assert>
      <sch:assert id="a-8841" test="count(cda:doseQuantity)=1">SHOULD contain zero or one [0..1] doseQuantity (CONF:8841).</sch:assert>
      <sch:assert id="a-8842" test="not(cda:doseQuantity) or cda:doseQuantity[@unit]">The doseQuantity, if present, SHOULD contain zero or one [0..1] @unit, which SHALL be selected from ValueSet UnitsOfMeasureCaseSensitive 2.16.840.1.113883.1.11.12839 DYNAMIC (CONF:8842).</sch:assert>
      <sch:assert id="a-8849" test="count(cda:performer)=1">SHOULD contain zero or one [0..1] performer (CONF:8849).</sch:assert>
      <sch:assert id="a-8851" test="not(cda:participant) or cda:participant[@typeCode='CSM']">The participant, if present, SHALL contain exactly one [1..1] @typeCode="CSM" (CodeSystem: HL7ParticipationType 2.16.840.1.113883.5.90 STATIC) (CONF:8851).</sch:assert>
      <sch:assert id="a-15547" test="not(cda:participant) or cda:participant[count(cda:participantRole[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.24'])=1]">The participant, if present, SHALL contain exactly one [1..1] Drug Vehicle (templateId:2.16.840.1.113883.10.20.22.4.24) (CONF:15547).</sch:assert>
      <sch:assert id="a-15545-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1 (CONF:15545).</sch:assert>
      <sch:assert id="a-15544" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15544).</sch:assert>
      <sch:assert id="a-15543" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:15543).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.52-warnings" context="cda:substanceAdministration[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.52']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.52-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.60-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.60-warnings-abstract" abstract="true">
      <sch:assert id="a-17175-c" test="not(cda:entryRelationship[@typeCode='COMP'][cda:sequenceNumber]) or cda:entryRelationship[@typeCode='COMP'][cda:sequenceNumber][@value]">The sequenceNumber, if present, SHALL contain exactly one [1..1] @value (CONF:17175).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.60-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.60']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.60-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.61-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.61-warnings-abstract" abstract="true">
      <sch:assert id="a-8912-c" test="cda:performer[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.87']]/cda:assignedEntity[count(cda:representedOrganization) &lt; 2]">This assignedEntity SHOULD contain zero or one [0..1] representedOrganization (CONF:8912).</sch:assert>
      <sch:assert id="a-8913-c" test="not(cda:performer[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.87']]/cda:assignedEntity/cda:representedOrganization) or cda:performer[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.87']]/cda:assignedEntity/cda:representedOrganization[count(cda:name) &lt; 2]">The representedOrganization, if present, SHOULD contain zero or one [0..1] name (CONF:8913).</sch:assert>
      <sch:assert id="a-8914-c" test="cda:performer[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.87']]/cda:assignedEntity[count(cda:code) &lt; 2]">This assignedEntity SHOULD contain zero or one [0..1] code (CONF:8914).</sch:assert>
      <sch:assert id="a-8932-c" test="cda:participant[@typeCode='COV'][cda:templateId[@root='2.16.840.1.113883.10.20.22.4.89']]/cda:participantRole[count(cda:playingEntity) &lt; 2]">This participantRole SHOULD contain zero or one [0..1] playingEntity (CONF:8932).</sch:assert>
      <sch:assert id="a-8933-c" test=".">If the member date of birth as recorded by the health plan differs from the patient date of birth as recorded in the registration/medication summary, then the member date of birth SHALL be recorded in sdtc:birthTime. The prefix sdtc: SHALL be bound to the namespace ?urn:hl7-org:sdtc?. The use of the namespace provides a necessary extension to CDA R2 for the use of the birthTime element (CONF:8933).</sch:assert>
      <sch:assert id="a-8934" test="count(cda:participant[@typeCode='HLD'][count(cda:participantRole[count(cda:id) &gt; 0])=1][count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.90'])=1])=1">SHOULD contain zero or one [0..1] participant (CONF:8934) such that it SHALL contain exactly one [1..1] @typeCode="HLD" Holder (CodeSystem: HL7ParticipationType 2.16.840.1.113883.5.90 STATIC) (CONF:8935). SHALL contain exactly one [1..1] participantRole (CONF:8936). This participantRole SHALL contain at least one [1..*] id (CONF:8937). SHALL contain exactly one [1..1] templateId (CONF:16813). This templateId SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.90" Policy Holder Participant (CONF:16815).</sch:assert>
      <sch:assert id="a-8942-c" test=".">The target of a policy activity with act/entryRelationship/@typeCode="REFR" SHALL be an authorization activity (templateId 2.16.840.1.113883.10.20.1.19) OR an act, with act[@classCode="ACT"] and act[@moodCode="DEF"], representing a description of the coverage plan (CONF:8942).</sch:assert>
      <sch:assert id="a-8943-c" test=".">A description of the coverage plan SHALL contain one or more act/id, to represent the plan identifier, and an act/text with the name of the plan (CONF:8943).</sch:assert>
      <sch:assert id="a-8956-c" test="cda:participant[@typeCode='COV'][cda:templateId[@root='2.16.840.1.113883.10.20.22.4.89']]/cda:participantRole[count(cda:addr) &lt; 2]">This participantRole SHOULD contain zero or one [0..1] addr (CONF:8956).</sch:assert>
      <sch:assert id="a-8961" test="count(cda:performer[count(cda:assignedEntity[count(cda:code[@code='GUAR'])=1])=1][count(cda:templateId[@root='2.16.840.1.113883.10.20.22.4.88'])=1])=1">This performer represents the Guarantor.
SHOULD contain zero or one [0..1] performer="PRF" Performer (CodeSystem: HL7ParticipationType 2.16.840.1.113883.5.90 STATIC) (CONF:8961) such that it SHALL contain exactly one [1..1] assignedEntity (CONF:8962). This assignedEntity SHALL contain exactly one [1..1] code (CONF:8968). This code SHALL contain exactly one [1..1] @code="GUAR" Guarantor (CodeSystem: RoleCode 2.16.840.1.113883.5.111 STATIC) (CONF:16096). SHALL contain exactly one [1..1] templateId (CONF:16810). This templateId SHALL contain exactly one [1..1] @root="2.16.840.1.113883.10.20.22.4.88" Guarantor Performer (CONF:16811).</sch:assert>
      <sch:assert id="a-8963-c" test="count(cda:performer[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.88']][count(cda:time) &lt; 2][count(cda:assignedEntity)=1]) &lt; 2">SHOULD contain zero or one [0..1] time (CONF:8963).</sch:assert>
      <sch:assert id="a-8964-c" test="not(cda:performer[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.88']]/cda:assignedEntity) or cda:performer[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.88']]/cda:assignedEntity[count(cda:addr) &lt; 2]">This assignedEntity SHOULD contain zero or one [0..1] addr (CONF:8964).</sch:assert>
      <sch:assert id="a-8965-c" test="not(cda:performer[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.88']]/cda:assignedEntity) or cda:performer[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.88']]/cda:assignedEntity[count(cda:telecom) &lt; 2]">This assignedEntity SHOULD contain zero or one [0..1] telecom (CONF:8965).</sch:assert>
      <sch:assert id="a-8967-c" test="cda:performer[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.88']]/cda:assignedEntity/cda:assignedPerson/cda:name or cda:performer[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.88']]/cda:assignedEntity/cda:representedOrganization/cda:name">SHOULD include assignedEntity/assignedPerson/name AND/OR assignedEntity/representedOrganization/name (CONF:8967).</sch:assert>
      <sch:assert id="a-8984-c" test=".">This id is a unique identifier for  the covered party member. Implementers SHOULD use the same GUID for each instance of a member identifier from the same health plan (CONF:8984).</sch:assert>
      <sch:assert id="a-10120-c" test=".">This id is a unique identifier for the subscriber of the coverage (CONF:10120).</sch:assert>
      <sch:assert id="a-10481-c" test="cda:performer[@typeCode][cda:templateId[@root='2.16.840.1.113883.10.20.22.4.87']]/cda:assignedEntity/cda:addr[cda:streetAddressLine and cda:city and ((not(cda:country) or cda:country!='US') or (cda:country='US' and cda:state and cda:postalCode))]">The content of addr SHALL be a conformant US Realm Address (AD.US.FIELDED) (2.16.840.1.113883.10.20.22.5.2) (CONF:10481).</sch:assert>
      <sch:assert id="a-10482-c" test="not (cda:performer[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.88']]/cda:assignedEntity/cda:addr) or cda:performer[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.88']]/cda:assignedEntity/cda:addr[cda:streetAddressLine and cda:city and ((not(cda:country) or cda:country!='US') or (cda:country='US' and cda:state and cda:postalCode))]">The content of addr SHALL be a conformant US Realm Address (AD.US.FIELDED) (2.16.840.1.113883.10.20.22.5.2) (CONF:10482).</sch:assert>
      <sch:assert id="a-10483-c" test="not(cda:participant[@typeCode='HLD'][cda:templateId[@root='2.16.840.1.113883.10.20.22.4.90']]/cda:participantRole/cda:addr) or cda:participant[@typeCode='HLD'][cda:templateId[@root='2.16.840.1.113883.10.20.22.4.90']]/cda:participantRole/cda:addr[cda:streetAddressLine and cda:city and ((not(cda:country) or cda:country!='US') or (cda:country='US' and cda:state and cda:postalCode))]">The content of addr SHALL be a conformant US Realm Address (AD.US.FIELDED) (2.16.840.1.113883.10.20.22.5.2) (CONF:10483).</sch:assert>
      <sch:assert id="a-10484-c" test="cda:participant[@typeCode='COV']/cda:participantRole/cda:addr[cda:streetAddressLine and cda:city and ((not(cda:country) or cda:country!='US') or (cda:country='US' and cda:state and cda:postalCode))]">The content of addr SHALL be a conformant US Realm Address (AD.US.FIELDED) (2.16.840.1.113883.10.20.22.5.2) (CONF:10484).</sch:assert>
      <sch:assert id="a-15992-c" test="not(cda:performer/cda:assignedEntity[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.87']]/cda:code) or cda:performer/cda:assignedEntity[cda:templateId[@root='2.16.840.1.113883.10.20.22.4.87']]/cda:code[@code]">The code, if present, SHALL contain exactly one [1..1] @code, which SHOULD be selected from ValueSet HL7FinanciallyResponsiblePartyType 2.16.840.1.113883.1.11.10416 DYNAMIC (CONF:15992).</sch:assert>
      <sch:assert id="a-16078-c" test="cda:participant[@typeCode='COV'][cda:templateId[@root='2.16.840.1.113883.10.20.22.4.89']]/cda:participantRole/cda:code[not(@code) or @code]">This code SHOULD contain zero or one [0..1] @code, which SHOULD be selected from ValueSet Coverage Role Type Value Set 2.16.840.1.113883.1.11.18877 DYNAMIC (CONF:16078).</sch:assert>
      <sch:assert id="a-19185" test="cda:code[@code]">This code SHOULD contain zero or one [0..1] @code, which SHOULD be selected from ValueSet Health Insurance Type Value Set 2.16.840.1.113883.3.88.12.3221.5.2 DYNAMIC (CONF:19185).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.61-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.61']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.61-warnings-abstract" />
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.61-8916-branch-8916-warnings-abstract" abstract="true">
      <sch:assert id="a-8918-branch-8916" test="count(cda:time)=1">SHOULD contain zero or one [0..1] time (CONF:8918).</sch:assert>
      <sch:assert id="a-8919-branch-8916" test="not(cda:time) or cda:time[count(cda:low)=1]">The time, if present, SHOULD contain zero or one [0..1] low (CONF:8919).</sch:assert>
      <sch:assert id="a-8920-branch-8916" test="not(cda:time) or cda:time[count(cda:high)=1]">The time, if present, SHOULD contain zero or one [0..1] high (CONF:8920).</sch:assert>
      <sch:assert id="a-8930-branch-8916" test="not(cda:participantRole/cda:playingEntity) or cda:participantRole/cda:playingEntity[count(cda:name)=1]">If the covered party?s name is recorded differently in the health plan and in the registration/medication summary (due to marriage or for other reasons), use the name as it is recorded in the health plan.
The playingEntity, if present, SHALL contain exactly one [1..1] name (CONF:8930).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.61-8916-branch-8916-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.61']/cda:participant[@typeCode='COV'][cda:participantRole[cda:id][cda:code]][cda:templateId[@root='2.16.840.1.113883.10.20.22.4.89']]">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.61-8916-branch-8916-warnings-abstract" />
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.61-8934-branch-8934-warnings-abstract" abstract="true">
      <sch:assert id="a-8925-branch-8934" test="not(cda:participantRole) or cda:participantRole[count(cda:addr)=1]">This participantRole SHOULD contain zero or one [0..1] addr (CONF:8925).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.61-8934-branch-8934-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.61']/cda:participant[@typeCode='HLD'][cda:participantRole[cda:id]][cda:templateId[@root='2.16.840.1.113883.10.20.22.4.90']]">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.61-8934-branch-8934-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.1.19-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.1.19-warnings-abstract" abstract="true">
      <sch:assert id="a-8951-c" test=".">The target of an authorization activity with act/entryRelationship/@typeCode="SUBJ" SHALL be a clinical statement with moodCode="PRMS" Promise (CONF:8951).</sch:assert>
      <sch:assert id="a-8952-c" test=".">The target of an authorization activity MAY contain one or more performer, to indicate the providers that have been authorized to provide treatment (CONF:8952).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.1.19-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.1.19']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.1.19-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.53-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.53-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.53-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.53']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.53-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.54-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.54-warnings-abstract" abstract="true">
      <sch:assert id="a-9008" test="cda:manufacturedMaterial/cda:code[count(cda:originalText)=1]">This code SHOULD contain zero or one [0..1] originalText (CONF:9008).</sch:assert>
      <sch:assert id="a-9012" test="count(cda:manufacturerOrganization)=1">SHOULD contain zero or one [0..1] manufacturerOrganization (CONF:9012).</sch:assert>
      <sch:assert id="a-9014" test="cda:manufacturedMaterial[count(cda:lotNumberText)=1]">This manufacturedMaterial SHOULD contain zero or one [0..1] lotNumberText (CONF:9014).</sch:assert>
      <sch:assert id="a-15555" test="not(cda:manufacturedMaterial/cda:code/cda:originalText) or cda:manufacturedMaterial/cda:code/cda:originalText[count(cda:reference)=1]">The originalText, if present, SHOULD contain zero or one [0..1] reference (CONF:15555).</sch:assert>
      <sch:assert id="a-15556" test="not(cda:manufacturedMaterial/cda:code/cda:originalText/cda:reference) or cda:manufacturedMaterial/cda:code/cda:originalText/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15556).</sch:assert>
      <sch:assert id="a-15557-c" test="count(cda:manufacturedMaterial/cda:code/cda:originalText/cda:reference[@value])=0 or starts-with(cda:manufacturedMaterial/cda:code/cda:originalText/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15557).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.54-warnings" context="cda:manufacturedProduct[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.54']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.54-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.2.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.2.1-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.2-warnings-abstract" />
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.2.1-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.2.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.2.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.5-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.5-warnings-abstract" abstract="true">
      <sch:assert id="a-9270" test="count(cda:text)=1">SHOULD contain zero or one [0..1] text (CONF:9270).</sch:assert>
      <sch:assert id="a-15529" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:15529).</sch:assert>
      <sch:assert id="a-15530" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15530).</sch:assert>
      <sch:assert id="a-15531-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15531).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.5-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.5']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.5-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.3-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.3-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.3-warnings" context="cda:relatedSubject[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.3']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.3-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.4-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.4-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.4-warnings" context="cda:assignedAuthor[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.4']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.4-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.5-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.5-warnings-abstract" abstract="true">
      <sch:assert id="a-9203" test="count(cda:effectiveTime[@xsi:type='TS'])=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:9203).</sch:assert>
      <sch:assert id="a-17173" test="not(cda:effectiveTime[@xsi:type='TS']) or cda:effectiveTime[@xsi:type='TS'][@value]">The effectiveTime, if present, SHALL contain exactly one [1..1] @value (CONF:17173).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.5-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.5']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.5-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.63-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.63-warnings-abstract" abstract="true">
      <sch:assert id="a-9235" test="count(cda:effectiveTime)=1">If present, the effectiveTime contains the time the series was started
SHOULD contain zero or one [0..1] effectiveTime (CONF:9235).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.63-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.63']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.63-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.8-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.8-warnings-abstract" abstract="true">
      <sch:assert id="a-9246" test="count(cda:text)=1">SHOULD contain zero or one [0..1] text (CONF:9246).</sch:assert>
      <sch:assert id="a-9247" test="not(cda:text) or cda:text[@mediaType='application/dicom']">The text, if present, SHALL contain exactly one [1..1] @mediaType="application/dicom" (CONF:9247).</sch:assert>
      <sch:assert id="a-9248" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHALL contain exactly one [1..1] reference (CONF:9248).</sch:assert>
      <sch:assert id="a-9249-c" test="not(tested)">SHALL contain a @value that contains a WADO reference as a URI (CONF:9249).</sch:assert>
      <sch:assert id="a-9250" test="count(cda:effectiveTime)=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:9250).</sch:assert>
      <sch:assert id="a-9251" test="not(cda:effectiveTime) or cda:effectiveTime[@value]">The effectiveTime, if present, SHALL contain exactly one [1..1] @value (CONF:9251).</sch:assert>
      <sch:assert id="a-9252-c" test="not(cda:low)">The effectiveTime, if present, SHALL NOT contain [0..0] low (CONF:9252).</sch:assert>
      <sch:assert id="a-9253-c" test="not(cda:high)">The effectiveTime, if present, SHALL NOT contain [0..0] high (CONF:9253).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.8-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.8']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.8-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.9-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.9-warnings-abstract" abstract="true">
      <sch:assert id="a-9273" test="count(cda:value[@xsi:type='CD'])=1">The value element is a SHOULD to allow backwards compatibility with the DICOM CMET.  Note that the use of ASSERTION for the code differs from the DICOM CMET. This is intentional. The DICOM CMET was created before the Term Info guidelines describing the use of the assertion pattern were released. It was determined that this IG should follow the latest Term Info guidelines. Implementers using both this IG and the DICOM CMET should be aware of this difference and apply appropriate transformations.
SHOULD contain zero or one [0..1] value with @xsi:type="CD", where the @code SHOULD be selected from ValueSet DICOMPurposeOfReference 2.16.840.1.113883.11.20.9.28 DYNAMIC (CONF:9273).</sch:assert>
      <sch:assert id="a-19208" test="cda:code[@code='ASSERTION' and @codeSystem='2.16.840.1.113883.5.4']">This code SHOULD contain zero or one [0..1] @code="ASSERTION" Assertion (CodeSystem: ActCode 2.16.840.1.113883.5.4 STATIC) (CONF:19208).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.9-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.9']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.9-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.10-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.10-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.10-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.10']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.10-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.11-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.11-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.11-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.11']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.11-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.12-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.12-warnings-abstract" abstract="true">
      <sch:assert id="a-9294" test="count(cda:effectiveTime)=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:9294).</sch:assert>
      <sch:assert id="a-15938" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:15938).</sch:assert>
      <sch:assert id="a-15939" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15939).</sch:assert>
      <sch:assert id="a-15940-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15940).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.12-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.12']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.12-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.13-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.13-warnings-abstract" abstract="true">
      <sch:assert id="a-9309" test="count(cda:effectiveTime)=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:9309).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.13-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.13']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.13-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.6.2.14-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.14-warnings-abstract" abstract="true">
      <sch:assert id="a-9326" test="count(cda:effectiveTime)=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:9326).</sch:assert>
      <sch:assert id="a-19210" test="cda:code[@code]">This code SHOULD contain zero or one [0..1] @code, which SHOULD be selected from ValueSet DIRQuantityMeasurementTypeCodes 2.16.840.1.113883.11.20.9.29 DYNAMIC (CONF:19210).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.6.2.14-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.6.2.14']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.6.2.14-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.5.1.1-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.1.1-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.1.1-warnings" context="cda:PN[cda:templateId/@root='2.16.840.1.113883.10.20.22.5.1.1']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.5.1.1-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.64-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.64-warnings-abstract" abstract="true">
      <sch:assert id="a-9434" test="not(cda:author) or cda:author[count(cda:time)=1]">The author, if present, SHALL contain exactly one [1..1] time (CONF:9434).</sch:assert>
      <sch:assert id="a-9435" test="not(cda:author) or cda:author[count(cda:assignedAuthor)=1]">The author, if present, SHALL contain exactly one [1..1] assignedAuthor (CONF:9435).</sch:assert>
      <sch:assert id="a-9436" test="not(cda:author/cda:assignedAuthor) or cda:author/cda:assignedAuthor[count(cda:id)=1]">This assignedAuthor SHALL contain exactly one [1..1] id (CONF:9436).</sch:assert>
      <sch:assert id="a-9437" test="not(cda:author/cda:assignedAuthor) or cda:author/cda:assignedAuthor[count(cda:addr)=1]">This assignedAuthor SHALL contain exactly one [1..1] addr (CONF:9437).</sch:assert>
      <sch:assert id="a-9438-c" test="cda:assignedPerson/cda:name or cda:representedOrganization/cda:name">SHALL include assignedPerson/name or representedOrganization/name (CONF:9438).</sch:assert>
      <sch:assert id="a-9439-c" test="cda:assignedPerson/cda:name[cda:given and cda:family] or (count(cda:assignedPerson/cda:name[*])=0 and string-length(cda:assignedPerson/cda:name)!=0)">An  assignedPerson/name SHALL be a conformant US Realm Person Name (PN.US.FIELDED) (2.16.840.1.113883.10.20.22.5.1.1) (CONF:9439).</sch:assert>
      <sch:assert id="a-10480-c" test="cda:author/cda:assignedAuthor/cda:addr[cda:streetAddressLine and cda:city and (cda:country!='US' or ((not(cda:country) or cda:country!='US') and cda:state and cda:postalCode))]">The content of addr SHALL be a conformant US Realm Address (AD.US.FIELDED) (2.16.840.1.113883.10.20.22.5.2) (CONF:10480).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.64-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.64']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.64-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.41-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.41-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.41-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.41']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.41-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.42-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.42-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.42-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.42']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.42-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.43-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.43-warnings-abstract" abstract="true">
      <sch:assert id="a-9934" test="count(cda:entry)=1">SHOULD contain zero or one [0..1] entry (CONF:9934).</sch:assert>
      <sch:assert id="a-15481" test="not(cda:entry) or cda:entry[count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.34'])=1]">The entry, if present, SHALL contain exactly one [1..1] Hospital Admission Diagnosis (templateId:2.16.840.1.113883.10.20.22.4.34) (CONF:15481).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.43-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.43']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.43-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.5.3-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.3-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.3-warnings" context="cda:IVL_TS[cda:templateId/@root='2.16.840.1.113883.10.20.22.5.3']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.5.3-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.65-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.65-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.65-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.65']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.65-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.44-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.44-warnings-abstract" abstract="true">
      <sch:assert id="a-10102" test="count(cda:entry[count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.36'])=1]) &gt; 0">SHOULD contain zero or more [0..*] entry (CONF:10102) such that it SHALL contain exactly one [1..1] Admission Medication (templateId:2.16.840.1.113883.10.20.22.4.36) (CONF:15484).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.44-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.44']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.44-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.2.45-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.45-warnings-abstract" abstract="true">
      <sch:assert id="a-10116" test="count(cda:entry) &gt; 0">SHOULD contain zero or more [0..*] entry (CONF:10116).</sch:assert>
      <sch:assert id="a-15496" test="not(cda:entry) or cda:entry[count(cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.20'])=1]">The entry, if present, SHALL contain exactly one [1..1] Instructions (templateId:2.16.840.1.113883.10.20.22.4.20) (CONF:15496).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.2.45-warnings" context="cda:section[cda:templateId/@root='2.16.840.1.113883.10.20.22.2.45']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.2.45-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.5.4-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.4-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.5.4-warnings" context="cda:TS[cda:templateId/@root='2.16.840.1.113883.10.20.22.5.4']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.5.4-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.67-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.67-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.2-warnings-abstract" />
      <sch:assert id="a-13926" test="count(cda:text)=1">SHOULD contain zero or one [0..1] text (CONF:13926).</sch:assert>
      <sch:assert id="a-13927" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:13927).</sch:assert>
      <sch:assert id="a-13928-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:13928).</sch:assert>
      <sch:assert id="a-13933" test="count(cda:interpretationCode) &gt; 0">SHOULD contain zero or more [0..*] interpretationCode (CONF:13933).</sch:assert>
      <sch:assert id="a-13937" test="count(cda:referenceRange) &gt; 0">SHOULD contain zero or more [0..*] referenceRange (CONF:13937).</sch:assert>
      <sch:assert id="a-13938" test="not(cda:referenceRange) or cda:referenceRange[count(cda:observationRange)=1]">The referenceRange, if present, SHALL contain exactly one [1..1] observationRange (CONF:13938).</sch:assert>
      <sch:assert id="a-13939-c" test="not(cda:referenceRange/cda:observationRange/cda:code)">This observationRange SHALL NOT contain [0..0] code (CONF:13939).</sch:assert>
      <sch:assert id="a-14234-c" test="not(cda:value/@xsi:type='CD') or (cda:value/@xsi:type='CD' and count(cda:value[@codeSystem='2.16.840.1.113883.6.96'])=1)">If xsi:type=?CD?, SHOULD contain a code from SNOMED CT (CodeSystem: 2.16.840.1.113883.6.96) (CONF:14234).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.67-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.67']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.67-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.72-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.72-warnings-abstract" abstract="true">
      <sch:assert id="a-14831" test="not(cda:participant/cda:time) or cda:participant/cda:time[count(cda:low)=1]">The time, if present, SHALL contain exactly one [1..1] low (CONF:14831).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.72-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.72']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.72-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.74-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.74-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.2-warnings-abstract" />
      <sch:assert id="a-14258" test="count(cda:text)=1">SHOULD contain zero or one [0..1] text (CONF:14258).</sch:assert>
      <sch:assert id="a-14264" test="count(cda:interpretationCode) &gt; 0">SHOULD contain zero or more [0..*] interpretationCode (CONF:14264).</sch:assert>
      <sch:assert id="a-14267" test="count(cda:referenceRange) &gt; 0">SHOULD contain zero or more [0..*] referenceRange (CONF:14267).</sch:assert>
      <sch:assert id="a-14268" test="not(cda:referenceRange) or cda:referenceRange[count(cda:observationRange)=1]">The referenceRange, if present, SHALL contain exactly one [1..1] observationRange (CONF:14268).</sch:assert>
      <sch:assert id="a-14269-c" test="not(cda:referenceRange/cda:observationRange/cda:code)">This observationRange SHALL NOT contain [0..0] code (CONF:14269).</sch:assert>
      <sch:assert id="a-14271-c" test="not(cda:value/@xsi:type='CD') or (cda:value/@xsi:type='CD' and count(cda:value[@codeSystem='2.16.840.1.113883.6.96'])=1)">If xsi:type=?CD?, SHOULD contain a code from SNOMED CT (CodeSystem: 2.16.840.1.113883.6.96) (CONF:14271).</sch:assert>
      <sch:assert id="a-14592" test="cda:code[@code='373930000' and @codeSystem='2.16.840.1.113883.6.96']">This code SHOULD contain zero or one [0..1] @code="373930000" Cognitive function finding (CodeSystem: SNOMED-CT 2.16.840.1.113883.6.96 STATIC) (CONF:14592).</sch:assert>
      <sch:assert id="a-15549" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:15549).</sch:assert>
      <sch:assert id="a-15550" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15550).</sch:assert>
      <sch:assert id="a-15551-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15551).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.74-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.74']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.74-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.68-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.68-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.4-warnings-abstract" />
      <sch:assert id="a-14287" test="count(cda:effectiveTime)=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:14287).</sch:assert>
      <sch:assert id="a-14293-c" test="not(tested)">If the diagnosis is unknown or the SNOMED code is unknown, @nullFlavor SHOULD be ?UNK?.  If the code is something other than SNOMED, @nullFlavor SHOULD be ?OTH? and the other code SHOULD be placed in the translation element (CONF:14293).</sch:assert>
      <sch:assert id="a-14304" test="count(cda:text)=1">SHOULD contain zero or one [0..1] text (CONF:14304).</sch:assert>
      <sch:assert id="a-14315" test="cda:code[@code='248536006' and @codeSystem='2.16.840.1.113883.6.96']">This code SHOULD contain zero or one [0..1] @code="248536006" finding of functional performance and activity (CodeSystem: SNOMED-CT 2.16.840.1.113883.6.96 STATIC) (CONF:14315).</sch:assert>
      <sch:assert id="a-15552" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:15552).</sch:assert>
      <sch:assert id="a-15553" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15553).</sch:assert>
      <sch:assert id="a-15554-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15554).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.68-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.68']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.68-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.73-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.73-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.4-warnings-abstract" />
      <sch:assert id="a-14324" test="count(cda:effectiveTime)=1">SHOULD contain zero or one [0..1] effectiveTime (CONF:14324).</sch:assert>
      <sch:assert id="a-14341" test="count(cda:text)=1">SHOULD contain zero or one [0..1] text (CONF:14341).</sch:assert>
      <sch:assert id="a-14805" test="cda:code[@code='373930000' and @codeSystem='2.16.840.1.113883.6.96']">This code SHOULD contain zero or one [0..1] @code="373930000" Cognitive function finding   (CodeSystem: SNOMED-CT 2.16.840.1.113883.6.96 STATIC) (CONF:14805).</sch:assert>
      <sch:assert id="a-15532" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:15532).</sch:assert>
      <sch:assert id="a-15533" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHOULD contain zero or one [0..1] @value (CONF:15533).</sch:assert>
      <sch:assert id="a-15534-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15534).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.73-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.73']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.73-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.66-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.66-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.1-warnings-abstract" />
      <sch:assert id="a-14747" test="cda:code[@code]">This code SHOULD contain zero or one [0..1] @code (CONF:14747).</sch:assert>
      <sch:assert id="a-14748-c" test="count(cda:code[@codeSystem])=0 or cda:code[@codeSystem='2.16.840.1.113883.6.254'] or cda:code[@codeSystem='2.16.840.1.113883.6.96']">SHOULD be selected from ICF (codeSystem 2.16.840.1.113883.6.254) or SNOMED CT (codeSystem 2.16.840.1.113883.6.96) (CONF:14748).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.66-warnings" context="cda:organizer[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.66']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.66-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.75-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.75-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.1-warnings-abstract" />
      <sch:assert id="a-14697" test="cda:code[@code]">This code SHOULD contain zero or one [0..1] @code (CONF:14697).</sch:assert>
      <sch:assert id="a-14698-c" test="count(cda:code[@codeSystem])=0 or cda:code[@codeSystem='2.16.840.1.113883.6.254'] or cda:code[@codeSystem='2.16.840.1.113883.6.96']">Should be selected from ICF (codeSystem 2.16.840.1.113883.6.254) or SNOMED CT (codeSystem 2.16.840.1.113883.6.96) (CONF:14698).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.75-warnings" context="cda:organizer[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.75']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.75-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.70-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.70-warnings-abstract" abstract="true">
      <sch:assert id="a-14391" test="count(cda:text)=1">SHOULD contain zero or one [0..1] text (CONF:14391).</sch:assert>
      <sch:assert id="a-14392" test="not(cda:text) or cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:14392).</sch:assert>
      <sch:assert id="a-14398-c" test="not(tested)">If the stage unknown or the SNOMED code is unknown, @nullFlavor SHOULD be ?UNK?.  If the code is something other than SNOMED, @nullFlavor SHOULD be ?OTH? and the other code SHOULD be placed in the translation element (CONF:14398).</sch:assert>
      <sch:assert id="a-14410" test="count(cda:entryRelationship[@typeCode='COMP'][count(cda:observation[@classCode='OBS'][@moodCode='EVN'][count(cda:code[@code='401238003'])=1][count(cda:value)=1])=1])=1">SHOULD contain zero or one [0..1] entryRelationship (CONF:14410) such that it SHALL contain exactly one [1..1] @typeCode="COMP" (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:14411). SHALL contain exactly one [1..1] observation (CONF:14619). This observation SHALL contain exactly one [1..1] code (CONF:14620). This code SHALL contain exactly one [1..1] @code="401238003" Length of Wound (CodeSystem: SNOMED-CT 2.16.840.1.113883.6.96 STATIC) (CONF:14621). This observation SHALL contain exactly one [1..1] value with @xsi:type="PQ" (CONF:14622). This observation SHALL contain exactly one [1..1] @classCode="OBS" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:14685). This observation SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14686).</sch:assert>
      <sch:assert id="a-14601" test="count(cda:entryRelationship[@typeCode='COMP'][count(cda:observation[@classCode='OBS'][@moodCode='EVN'][count(cda:code[@code='401239006'])=1][count(cda:value)=1])=1])=1">SHOULD contain zero or one [0..1] entryRelationship (CONF:14601) such that it SHALL contain exactly one [1..1] @typeCode="COMP" (CONF:14602). SHALL contain exactly one [1..1] observation (CONF:14623). This observation SHALL contain exactly one [1..1] code (CONF:14624). This code SHALL contain exactly one [1..1] @code="401239006" Width of Wound (CodeSystem: SNOMED-CT 2.16.840.1.113883.6.96 STATIC) (CONF:14625). This observation SHALL contain exactly one [1..1] value with @xsi:type="PQ" (CONF:14626). This observation SHALL contain exactly one [1..1] @classCode="OBS" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:14687). This observation SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14688).</sch:assert>
      <sch:assert id="a-14605" test="count(cda:entryRelationship[@typeCode='COMP'][count(cda:observation[@classCode='OBS'][@moodCode='EVN'][count(cda:code[@code='425094009'])=1][count(cda:value)=1])=1])=1">SHOULD contain zero or one [0..1] entryRelationship (CONF:14605) such that it SHALL contain exactly one [1..1] @typeCode="COMP" (CONF:14606). SHALL contain exactly one [1..1] observation (CONF:14627). This observation SHALL contain exactly one [1..1] code (CONF:14628). This code SHALL contain exactly one [1..1] @code="425094009" Depth of Wound (CodeSystem: SNOMED-CT 2.16.840.1.113883.6.96 STATIC) (CONF:14629). This observation SHALL contain exactly one [1..1] value with @xsi:type="PQ" (CONF:14630). This observation SHALL contain exactly one [1..1] @classCode="OBS" (CodeSystem: HL7ActClass 2.16.840.1.113883.5.6 STATIC) (CONF:14689). This observation SHALL contain exactly one [1..1] @moodCode="EVN" (CodeSystem: ActMood 2.16.840.1.113883.5.1001 STATIC) (CONF:14690).</sch:assert>
      <sch:assert id="a-14797" test="count(cda:targetSiteCode) &gt; 0">SHOULD contain zero or more [0..*] targetSiteCode (CONF:14797).</sch:assert>
      <sch:assert id="a-14798-c" test="not(cda:targetSiteCode) or cda:targetSiteCode[@code and @code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.36']/voc:code/@value]">The targetSiteCode, if present, SHALL contain exactly one [1..1] @code, which SHOULD be selected from ValueSet Pressure Point  2.16.840.1.113883.11.20.9.36 STATIC (CONF:14798).</sch:assert>
      <sch:assert id="a-14799" test="not(cda:targetSiteCode) or cda:targetSiteCode[count(cda:qualifier)=1]">The targetSiteCode, if present, SHOULD contain zero or one [0..1] qualifier (CONF:14799).</sch:assert>
      <sch:assert id="a-14800-c" test="not(cda:targetSiteCode/cda:qualifier) or cda:targetSiteCode/cda:qualifier[count(cda:name)=1]">The qualifier, if present, SHALL contain exactly one [1..1] name (CONF:14800).</sch:assert>
      <sch:assert id="a-14801" test="not(cda:targetSiteCode/cda:qualifier/cda:name) or cda:targetSiteCode/cda:qualifier/cda:name[@code='272741003']">This name SHOULD contain zero or one [0..1] @code="272741003" laterality (CodeSystem: SNOMED-CT 2.16.840.1.113883.6.96 STATIC) (CONF:14801).</sch:assert>
      <sch:assert id="a-14802-c" test="not(cda:targetSiteCode/cda:qualifier) or cda:targetSiteCode/cda:qualifier[count(cda:value)=1]">The qualifier, if present, SHALL contain exactly one [1..1] value (CONF:14802).</sch:assert>
      <sch:assert id="a-14803" test="not(cda:targetSiteCode/cda:qualifier/cda:value) or cda:targetSiteCode/cda:qualifier/cda:value[@code and @code=$vocDocument/voc:systems/voc:system[@valueSetOid='2.16.840.1.113883.11.20.9.37']/voc:code/@value]">This value SHOULD contain zero or one [0..1] @code, which SHOULD be selected from ValueSet TargetSite Qualifiers  2.16.840.1.113883.11.20.9.37 STATIC (CONF:14803).</sch:assert>
      <sch:assert id="a-15585" test="not(cda:text/cda:reference) or cda:text/cda:reference[@value]">The reference, if present, SHALL contain exactly one [1..1] @value (CONF:15585).</sch:assert>
      <sch:assert id="a-15586-c" test="count(cda:text/cda:reference[@value])=0 or starts-with(cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:15586).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.70-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.70']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.70-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.69-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.69-warnings-abstract" abstract="true">
      <sch:assert id="a-14440-c" test="count(cda:code[@codeSystem])=0 or cda:code[@codeSystem='2.16.840.1.113883.6.1'] or cda:code[@codeSystem='2.16.840.1.113883.6.96']">SHOULD be from LOINC (CodeSystem: 2.16.840.1.113883.6.1) or SNOMED CT (CodeSystem: 2.16.840.1.113883.6.96) identifying the assessment scale (CONF:14440).</sch:assert>
      <sch:assert id="a-14451" test="count(cda:entryRelationship[@typeCode='COMP'][count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.86'])=1]) &gt; 0">SHOULD contain zero or more [0..*] entryRelationship (CONF:14451) such that it SHALL contain exactly one [1..1] @typeCode="COMP" has component (CONF:16741). SHALL contain exactly one [1..1] Assessment Scale Supporting Observation (templateId:2.16.840.1.113883.10.20.22.4.86) (CONF:16742).</sch:assert>
      <sch:assert id="a-16800" test="not(cda:referenceRange) or cda:referenceRange[count(cda:observationRange)=1]">The referenceRange, if present, SHALL contain exactly one [1..1] observationRange (CONF:16800).</sch:assert>
      <sch:assert id="a-16801" test="not(cda:referenceRange/cda:observationRange) or cda:referenceRange/cda:observationRange[count(cda:text)=1]">This observationRange SHOULD contain zero or one [0..1] text (CONF:16801).</sch:assert>
      <sch:assert id="a-16802" test="not(cda:referenceRange/cda:observationRange/cda:text) or cda:referenceRange/cda:observationRange/cda:text[count(cda:reference)=1]">The text, if present, SHOULD contain zero or one [0..1] reference (CONF:16802).</sch:assert>
      <sch:assert id="a-16804-c" test="count(cda:referenceRange/cda:observationRange/cda:text/cda:reference[@value])=0 or starts-with(cda:referenceRange/cda:observationRange/cda:text/cda:reference/@value, '#')">This reference/@value SHALL begin with a '#' and SHALL point to its corresponding narrative (using the approach defined in CDA Release 2, section 4.3.5.1) (CONF:16804).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.69-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.69']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.69-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.76-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.76-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.76-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.76']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.76-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.77-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.77-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.77-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.77']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.77-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.85-warnings">
    <!--Pattern is used in an implied relationship.-->
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.85-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.85-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.85']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.85-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.78-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.78-warnings-abstract" abstract="true">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.85-warnings-abstract" />
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.78-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.78']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.78-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.79-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.79-warnings-abstract" abstract="true">
      <sch:assert id="a-14868" test="count(cda:entryRelationship[@typeCode='CAUS'][count(cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.4'])=1])=1">SHOULD contain zero or one [0..1] entryRelationship (CONF:14868) such that it SHALL contain exactly one [1..1] Problem Observation (templateId:2.16.840.1.113883.10.20.22.4.4) (CONF:14870). SHALL contain exactly one [1..1] @typeCode="CAUS" Is etiology for (CodeSystem: HL7ActRelationshipType 2.16.840.1.113883.5.1002 STATIC) (CONF:14875).</sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.79-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.79']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.79-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.80-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.80-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.80-warnings" context="cda:act[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.80']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.80-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
  <sch:pattern id="p-2.16.840.1.113883.10.20.22.4.86-warnings">
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.86-warnings-abstract" abstract="true">
      <sch:assert test="."></sch:assert>
    </sch:rule>
    <sch:rule id="r-2.16.840.1.113883.10.20.22.4.86-warnings" context="cda:observation[cda:templateId/@root='2.16.840.1.113883.10.20.22.4.86']">
      <sch:extends rule="r-2.16.840.1.113883.10.20.22.4.86-warnings-abstract" />
    </sch:rule>
  </sch:pattern>
</sch:schema>