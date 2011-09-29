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

import static org.apache.commons.lang3.Validate.notNull;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLExternalIdentifier;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLInternationalString;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.ExternalIdentifierType;

/**
 * Encapsulation of {@link ExternalIdentifierType}.
 * @author Jens Riemschneider
 */
public class EbXMLExternalIdentifier30 implements EbXMLExternalIdentifier {
    private final ExternalIdentifierType externalIdentifier;
    
    /**
     * Constructs an external identifier by wrapping the given ebXML 3.0 object.
     * @param externalIdentifierType
     *          the object to wrap.
     */
    public EbXMLExternalIdentifier30(ExternalIdentifierType externalIdentifierType) {
        notNull(externalIdentifierType, "externalIdentifierType cannot be null");
        externalIdentifier = externalIdentifierType;
    }
    
    @Override
    public String getIdentificationScheme() {
        return externalIdentifier.getIdentificationScheme();
    }

    @Override
    public EbXMLInternationalString getName() {
        return new EbXMLInternationalString30(externalIdentifier.getName());
    }

    @Override
    public String getValue() {
        return externalIdentifier.getValue();
    }

    @Override
    public void setIdentificationScheme(String identificationScheme) {
        externalIdentifier.setIdentificationScheme(identificationScheme);        
    }

    @Override
    public void setName(EbXMLInternationalString name) {
        externalIdentifier.setName(((EbXMLInternationalString30)name).getInternal());
    }

    @Override
    public void setValue(String value) {
        externalIdentifier.setValue(value);
    }

    @Override
    public String getRegistryObject() {
        return externalIdentifier.getRegistryObject();
    }

    @Override
    public void setRegistryObject(String registryObject) {
        externalIdentifier.setRegistryObject(registryObject);
    }

    @Override
    public String getId() {
        return externalIdentifier.getId();
    }

    @Override
    public void setId(String id) {
        externalIdentifier.setId(id);
    }
}
