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
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ExternalIdentifierType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.InternationalStringType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.LocalizedStringType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.RegistryPackageType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.SubmitObjectsRequest;

/**
 * ebXML version 2.1 specific constants and subroutines. 
 * 
 * @author Dmytro Rud
 */
public class Ebxml21Utils extends EbxmlUtils {


    /**
     * Creates and fills an {@link ExternalIdentifierType}.
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
     * Used for ITI-14, ITI-15
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
        List<Object> list = request.getLeafRegistryObjectList().getObjectRefOrAssociationOrAuditableEvent();
        for(Object o : list) {
            if(o instanceof RegistryPackageType) {
                RegistryPackageType registryPackage = (RegistryPackageType) o;
                List<ExternalIdentifierType> externalIdentifiers = registryPackage.getExternalIdentifier();
                for(ExternalIdentifierType id : externalIdentifiers) {
                    try {
                        String name = id.getName().getLocalizedString().get(0).getValue();  
                        if(XDS_UNIQUE_ID_ATTRIBUTE.equals(name)) {
                            auditDataset.setSubmissionSetUuid(id.getValue());
                        } else if(XDS_PATIENT_ID_ATTRIBUTE.equals(name)) {
                            auditDataset.setPatientId(id.getValue());
                        }
                    } catch(NullPointerException npe) {
                        // nop
                    }
                }
                break;
            }
        }
    }
    
    
}
