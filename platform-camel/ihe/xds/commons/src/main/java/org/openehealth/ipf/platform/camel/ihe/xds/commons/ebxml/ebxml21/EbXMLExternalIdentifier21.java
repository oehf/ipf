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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ebxml21;

import static org.apache.commons.lang.Validate.notNull;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLExternalIdentifier;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLInternationalString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ExternalIdentifierType;

/**
 * Encapsulation of {@link ExternalIdentifierType}.
 * @author Jens Riemschneider
 */
public class EbXMLExternalIdentifier21 implements EbXMLExternalIdentifier {
    private final ExternalIdentifierType externalIdentifier;
    private final EbXMLObjectLibrary objectLibrary;
    
    /**
     * Constructs an identifier by wrapping an ebXML 2.1 object.
     * @param externalIdentifierType
     *          the identifier to wrap.
     * @param objectLibrary
     *          the object library to use.
     */
    public EbXMLExternalIdentifier21(ExternalIdentifierType externalIdentifierType, EbXMLObjectLibrary objectLibrary) {
        notNull(objectLibrary, "objectLibrary cannot be null");
        notNull(externalIdentifierType, "externalIdentifierType cannot be null");
        
        this.externalIdentifier = externalIdentifierType;
        this.objectLibrary = objectLibrary;
    }
    
    @Override
    public String getIdentificationScheme() {
        return objectLibrary.getByObj(externalIdentifier.getIdentificationScheme());
    }

    @Override
    public EbXMLInternationalString getName() {
        return new EbXMLInternationalString21(externalIdentifier.getName());
    }

    @Override
    public String getValue() {
        return externalIdentifier.getValue();
    }

    @Override
    public void setIdentificationScheme(String identificationScheme) {
        externalIdentifier.setIdentificationScheme(objectLibrary.getById(identificationScheme));        
    }

    @Override
    public void setName(EbXMLInternationalString name) {
        externalIdentifier.setName(((EbXMLInternationalString21)name).getInternal());
    }

    @Override
    public void setValue(String value) {
        externalIdentifier.setValue(value);
    }
}
