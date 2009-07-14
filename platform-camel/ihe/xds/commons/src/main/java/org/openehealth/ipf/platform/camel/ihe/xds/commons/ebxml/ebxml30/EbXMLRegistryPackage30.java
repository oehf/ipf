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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml30;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLRegistryPackage;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AvailabilityStatus;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.RegistryPackageType;

/**
 * Encapsulation for {@link RegistryPackageType}.
 * @author Jens Riemschneider
 */
public class EbXMLRegistryPackage30 extends EbXMLRegistryObject30<RegistryPackageType> implements EbXMLRegistryPackage {
    /**
     * Constructs a registry package by wrapping the given ebXML 3.0 object.
     * @param registryPackage
     *          the object to wrap.
     * @param objectLibrary
     *          the object library to use.
     */
    public EbXMLRegistryPackage30(RegistryPackageType registryPackage, EbXMLObjectLibrary objectLibrary) {
        super(registryPackage, objectLibrary);
    }

    @Override
    public AvailabilityStatus getStatus() {
        return AvailabilityStatus.valueOfOpcode(getInternal().getStatus());
    }

    @Override
    public void setStatus(AvailabilityStatus status) {
        getInternal().setStatus(AvailabilityStatus.toOpcode(status));
    }
}
