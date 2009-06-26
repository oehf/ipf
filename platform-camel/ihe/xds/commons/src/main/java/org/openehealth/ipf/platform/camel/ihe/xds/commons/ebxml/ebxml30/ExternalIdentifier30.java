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

import static org.apache.commons.lang.Validate.notNull;

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ExternalIdentifier;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.InternationalString;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ExternalIdentifierType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.ObjectFactory;

/**
 * Encapsulation of {@link ExternalIdentifierType}.
 * @author Jens Riemschneider
 */
public class ExternalIdentifier30 implements ExternalIdentifier {
    private final static ObjectFactory rimFactory = new ObjectFactory();

    private final ExternalIdentifierType externalIdentifier;
    
    private ExternalIdentifier30(ExternalIdentifierType externalIdentifierType) {
        notNull(externalIdentifierType, "externalIdentifierType cannot be null");
        
        this.externalIdentifier = externalIdentifierType;
    }
    
    public static ExternalIdentifier create() {
        ExternalIdentifierType externalIdentifier = rimFactory.createExternalIdentifierType();
        return new ExternalIdentifier30(externalIdentifier);
    }
    
    public static ExternalIdentifier create(ExternalIdentifierType externalIdentifier) {
        return new ExternalIdentifier30(externalIdentifier);
    }
    
    @Override
    public String getIdentificationScheme() {
        return externalIdentifier.getIdentificationScheme();
    }

    @Override
    public InternationalString getName() {
        return InternationalString30.create(externalIdentifier.getName());
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
    public void setName(InternationalString name) {
        externalIdentifier.setName(((InternationalString30)name).getInternal());
    }

    @Override
    public void setValue(String value) {
        externalIdentifier.setValue(value);
    }

    ExternalIdentifierType getInternal() {
        return externalIdentifier;
    }
}
