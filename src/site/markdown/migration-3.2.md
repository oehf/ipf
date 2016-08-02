## IPF 3.2 Migration Guide

IPF 3.2 comes with some changes that must be considered when upgrading from IPF 3.1 to IPF 3.2.

### Dependency POM

IPF declares a dependency POM that manages the versions of the major required 3rd party libraries as well as of all IPF modules.
In order to align with these versions and minimize conflicts, import the following dependency: 

  
```xml
    <dependencyManagement>
        <dependencies>
        ...
            <dependency>
                <groupId>org.openehealth.ipf</groupId>
                <artifactId>ipf-dependencies</artifactId>
                <version>${ipf.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        ...
        </dependencies>
    </dependencyManagement>
```    


### EhCache
 
EhCache is now an optional dependency. The EhCache implementations `org.openehealth.ipf.platform.camel.ihe.hl7v3.EhcacheHl7v3ContinuationStorage`,
`org.openehealth.ipf.platform.camel.ihe.mllp.core.EhcacheInteractiveContinuationStorage`,
`org.openehealth.ipf.platform.camel.ihe.mllp.core.EhcacheUnsolicitedFragmentationStorage` and 
`org.openehealth.ipf.commons.ihe.ws.correlation.EhcacheAsynchonyCorrelator` can only be used if you add a Maven dependency to EhCache to your project.


### XUA Token Parsing

The dependencies for parsing the IHE XUA SAML Token from web service requests (for the sake of ATNA auditing) have been isolated in the new
new ```ipf-commons-ihe-xua``` module. When you expect XUA tokens, you need to add the following Maven dependency:

```xml
    <dependency>
        <groupId>org.openehealth.ipf.commons</groupId>
        <artifactId>ipf-commons-ihe-xua</artifactId>
    </dependency>
```


### IHE Profile Updates

IPF 3.2 is compatible with IHE ITI Revision 12 (published Apr 22, 2016). 
In addition, the following Change Proposals have been implemented:

* [Ballot 34](http://wiki.ihe.net/index.php/ITI_Change_Proposals_2016#Ballot_34): Implemented CP-ITI-767 and CP-ITI-914
    * [CP-ITI-767](ftp://ftp.ihe.net/IT_Infrastructure/TF_Maintenance-2016/CPs/Ballots/ballot_34/CP-ITI-767-02.doc): Fix errors in CXi datatype and referenceIdList attribute examples 
    * [CP-ITI-914](ftp://ftp.ihe.net/IT_Infrastructure/TF_Maintenance-2016/CPs/Ballots/ballot_34/CP-ITI-914-04.doc): Value of Content-Type HTTP header action parameter 

* [Ballot 33](http://wiki.ihe.net/index.php/ITI_Change_Proposals_2016#Ballot_34): Implemented CP-ITI-582, CP-ITI-880, CP-ITI-889, CP-ITI-906 and CP-ITI-918
    * [CP-ITI-582](ftp://ftp.ihe.net/IT_Infrastructure/TF_Maintenance-2016/CPs/Ballots/ballot_33/CP-ITI-582-06.doc): Metadata Update correction for associations in GetAllQuery 
    * [CP-ITI-880](ftp://ftp.ihe.net/IT_Infrastructure/TF_Maintenance-2016/CPs/Ballots/ballot_33/CP-ITI-880-03.doc): Add new value for Study Instance UID in CXi.5 (Identifier Type Codes) 
    * [CP-ITI-889](ftp://ftp.ihe.net/IT_Infrastructure/TF_Maintenance-2016/CPs/Ballots/ballot_33/CP-ITI-889-08.doc): Clarify Error Code used for ‘RPLC’ or ‘XFRM_RPLC’ Association Validation 
    * [CP-ITI-582](ftp://ftp.ihe.net/IT_Infrastructure/TF_Maintenance-2016/CPs/Ballots/ballot_33/CP-ITI-906-00.doc): ITI-8 MSH message structure - Revert changes introduced in CP-ITI-786 
    * [CP-ITI-582](ftp://ftp.ihe.net/IT_Infrastructure/TF_Maintenance-2016/CPs/Ballots/ballot_33/CP-ITI-918-01.doc): Fix workflowInstanceId specification in Vol 3 CXi datatype definition 
    
* [Ballot 32](http://wiki.ihe.net/index.php/ITI_Change_Proposals_2016#Ballot_32): Implemented CP-ITI-884 and CP-ITI-885
    * [CP-ITI-582](ftp://ftp.ihe.net/IT_Infrastructure/TF_Maintenance-2016/CPs/Ballots/ballot_32/CP-ITI-884-00.doc): PIXm changes for DSTU2 
    * [CP-ITI-880](ftp://ftp.ihe.net/IT_Infrastructure/TF_Maintenance-2016/CPs/Ballots/ballot_32/CP-ITI-885-00.doc): PDQm changes for DSTU2

