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

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.lcm.SubmitObjectsRequest;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.IdentifiableType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.RegistryObjectListType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.RegistryPackageType;

/**
 * Various ebXML version 3.0 helper routines used in tests.
 * 
 * @author Dmytro Rud
 */
public class Ebxml30TestUtils extends Ebxml30Utils {

    /**
     * Creates a {@link SubmitObjectRequest} for unit tests purposes.
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public static SubmitObjectsRequest createTestSubmitObjectsRequest(String comment) {
        RegistryPackageType registryPackageType = new RegistryPackageType();
        registryPackageType.getExternalIdentifier().add(
                Ebxml30Utils.createExternalIdentifierType(
                        Ebxml30Utils.XDS_PATIENT_ID_ATTRIBUTE, 
                        "patient-id&1.2.3.4.5@ISO"));
        registryPackageType.getExternalIdentifier().add(
                Ebxml30Utils.createExternalIdentifierType(
                        Ebxml30Utils.XDS_UNIQUE_ID_ATTRIBUTE, 
                        "229.6.58.29.24.1235"));

        JAXBElement<IdentifiableType> element = new JAXBElement<IdentifiableType>(
                new QName("urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0", "RegistryPackage", "rim"),
                (Class)RegistryPackageType.class,
                registryPackageType); 

        RegistryObjectListType registryObjectListType = new RegistryObjectListType();
        registryObjectListType.getIdentifiable().add(element);
        
        SubmitObjectsRequest submitObjectRequest = new SubmitObjectsRequest();
        submitObjectRequest.setRegistryObjectList(registryObjectListType);
        submitObjectRequest.setComment(comment);
        
        return submitObjectRequest;
    }
    
}
