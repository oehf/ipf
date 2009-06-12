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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.LeafRegistryObjectListType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.OrganizationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.RegistryPackageType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rs.SubmitObjectsRequest;

/**
 * Various ebXML version 3.0 helper routines used in tests.
 *   
 * @author Dmytro Rud
 */
public class Ebxml21TestUtils extends Ebxml21Utils {

    /**
     * Creates a {@link SubmitObjectRequest} for unit tests purposes.
     * 
     * @return
     */
    public static SubmitObjectsRequest createTestSubmitObjectRequest() { 
        OrganizationType orgType = new OrganizationType();
        orgType.setObjectType("ok");
        
        RegistryPackageType registryPackageType = new RegistryPackageType();
        registryPackageType.getExternalIdentifier().add(
                Ebxml21Utils.createExternalIdentifierType(
                        Ebxml21Utils.XDS_PATIENT_ID_ATTRIBUTE,
                        "patient-id&1.2.3.4.5@ISO"));
        registryPackageType.getExternalIdentifier().add(
                Ebxml21Utils.createExternalIdentifierType(
                        Ebxml21Utils.XDS_UNIQUE_ID_ATTRIBUTE,
                        "229.6.58.29.24.1235"));
        
        LeafRegistryObjectListType leafRegistryObjectListType = new LeafRegistryObjectListType();
        List<Object> objectRefOrAssociationOrAuditableEvent = leafRegistryObjectListType.getObjectRefOrAssociationOrAuditableEvent();
        objectRefOrAssociationOrAuditableEvent.add(orgType);
        objectRefOrAssociationOrAuditableEvent.add(registryPackageType);
        
        SubmitObjectsRequest request = new SubmitObjectsRequest();
        request.setLeafRegistryObjectList(leafRegistryObjectListType);
        
        return request; 
    }

}
