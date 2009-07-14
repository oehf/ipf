/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.platform.camel.ihe.xds.commons.utils;

import java.util.List;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.cxf.audit.AuditDataset;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLRegistryPackage;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30.EbXMLSubmitObjectsRequest30;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Vocabulary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ExternalIdentifierType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.InternationalStringType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.LocalizedStringType;

/**
 * ebXML version 3.0 specific constants and subroutines.  
 * 
 * @author Dmytro Rud
 */
public class Ebxml30Utils extends EbxmlUtils {


    /**
     * Creates and fills an {@link ExternalIdentifierType} (for test purposes).
     * <p>
     * In XML the created POJO would correspond to
     * <code>
     * &lt;ExternalIdentifier value="${value}">
     *     &lt;Name>
     *         &lt;LocalizedString value="${name}" />
     *     &lt;/Name>
     * &lt;/ExternalIdentifier>
     * </code>
     */ 
    public static ExternalIdentifierType createExternalIdentifierType(String name, String value) {
        LocalizedStringType localizedString = new LocalizedStringType();
        localizedString.setValue(name);
        
        InternationalStringType internationalString = new InternationalStringType();
        internationalString.getLocalizedString().add(localizedString);
        
        ExternalIdentifierType externalIdentifier = new ExternalIdentifierType();
        externalIdentifier.setName(internationalString);
        externalIdentifier.setValue(value);
        
        return externalIdentifier;
    }
    

    /**
     * Enriches a dataset with fields extracted from a submit objects request POJO.
     * <p>
     * Used for ITI-41, ITI-42
     * 
     * @param request
     *      a {@link SubmitObjectsRequest} as POJO 
     * @param auditDataset
     *      an audit dataset
     */
    public static void enrichDatasetFromSubmitObjectsRequest(
            SubmitObjectsRequest request, 
            AuditDataset auditDataset) 
    {
        EbXMLSubmitObjectsRequest30 ebXML = new EbXMLSubmitObjectsRequest30(request);
        List<EbXMLRegistryPackage> submissionSets = 
            ebXML.getRegistryPackages(Vocabulary.SUBMISSION_SET_CLASS_NODE);
        
        for (EbXMLRegistryPackage submissionSet : submissionSets) {
            String patientID = submissionSet.getExternalIdentifierValue(
                    Vocabulary.SUBMISSION_SET_PATIENT_ID_EXTERNAL_ID);            
            auditDataset.setPatientId(patientID);
            
            String uniqueID = submissionSet.getExternalIdentifierValue(
                    Vocabulary.SUBMISSION_SET_UNIQUE_ID_EXTERNAL_ID);
            auditDataset.setSubmissionSetUniqueID(uniqueID);
        }
    }
    
    
}
