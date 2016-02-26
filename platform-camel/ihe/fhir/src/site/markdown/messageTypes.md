## Message types in IPF FHIR Components

IPF FHIR Components ....


Data types for the *request* message of the supported transactions on consumer side are listed in the table below:

| Transaction      | Request Message Type (`org.hl7.fhir.instance.model...`) | Request Message Parameters
|------------------|-------------------------------------------------------- | --------------------------
| ITI-78           | n/a                                                     | Query Parameters
| ITI-81 	       | n/a                                                     | Query Parameters
| ITI-83 	       | n/a                                                     | Query Parameters


Data types for the *response* message of the supported transactions are listed in the table below:

| Transaction      | Response Message Type (`org.hl7.fhir.instance.model...`) 
|------------------|---------------------------------------------------------
| ITI-78           | `Bundle` of Patients 
| ITI-81           | `Bundle` of AuditEvents 
| ITI-83           | `Parameters`                                            

