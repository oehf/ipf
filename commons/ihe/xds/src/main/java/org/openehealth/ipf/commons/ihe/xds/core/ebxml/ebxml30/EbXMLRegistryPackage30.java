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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml30;

import org.openehealth.ipf.commons.ihe.xds.core.ExtraMetadataHolder;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLRegistryPackage;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.RegistryPackageType;

import java.util.ArrayList;
import java.util.Map;

/**
 * Encapsulation for {@link RegistryPackageType}.
 * @author Jens Riemschneider
 */
public class EbXMLRegistryPackage30 extends EbXMLRegistryObject30<RegistryPackageType> implements EbXMLRegistryPackage, ExtraMetadataHolder {
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
        getInternal().setStatus(AvailabilityStatus.toQueryOpcode(status));
    }

    @Override
    public Map<String, ArrayList<String>> getExtraMetadata() {
        return getInternal().getExtraMetadata();
    }

    @Override
    public void setExtraMetadata(Map<String, ArrayList<String>> map) {
        getInternal().setExtraMetadata(map);
    }
}
