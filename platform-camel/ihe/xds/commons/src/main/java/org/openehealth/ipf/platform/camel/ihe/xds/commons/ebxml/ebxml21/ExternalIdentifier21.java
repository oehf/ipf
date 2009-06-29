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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ExternalIdentifier;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.InternationalString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ExternalIdentifierType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectFactory;

/**
 * Encapsulation of {@link ExternalIdentifierType}.
 * @author Jens Riemschneider
 */
public class ExternalIdentifier21 implements ExternalIdentifier {
    private final static ObjectFactory rimFactory = new ObjectFactory();

    private final ExternalIdentifierType externalIdentifier;
    private final ObjectLibrary objectLibrary;
    
    private ExternalIdentifier21(ExternalIdentifierType externalIdentifierType, ObjectLibrary objectLibrary) {
        notNull(objectLibrary, "objectLibrary cannot be null");
        notNull(externalIdentifierType, "externalIdentifierType cannot be null");
        
        this.externalIdentifier = externalIdentifierType;
        this.objectLibrary = objectLibrary;
    }
    
    public static ExternalIdentifier create(ObjectLibrary objectLibrary) {
        ExternalIdentifierType externalIdentifier = rimFactory.createExternalIdentifierType();
        return new ExternalIdentifier21(externalIdentifier, objectLibrary);
    }
    
    public static ExternalIdentifier create(ExternalIdentifierType externalIdentifier, ObjectLibrary objectLibrary) {
        return new ExternalIdentifier21(externalIdentifier, objectLibrary);
    }
    
    @Override
    public String getIdentificationScheme() {
        return objectLibrary.getByObj(externalIdentifier.getIdentificationScheme());
    }

    @Override
    public InternationalString getName() {
        return InternationalString21.create(externalIdentifier.getName());
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
    public void setName(InternationalString name) {
        externalIdentifier.setName(((InternationalString21)name).getInternal());
    }

    @Override
    public void setValue(String value) {
        externalIdentifier.setValue(value);
    }

    ExternalIdentifierType getInternal() {
        return externalIdentifier;
    }
}
