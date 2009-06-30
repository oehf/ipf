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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAssociation;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.ObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.AssociationType1;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.ObjectFactory;

/**
 * Encapsulation of {@link AssociationType1}.
 * @author Jens Riemschneider
 */
public class EbXMLAssociation21 implements EbXMLAssociation {
    private final static ObjectFactory rimFactory = new ObjectFactory();

    private final ObjectLibrary objectLibrary;    
    private final AssociationType1 association;

    private EbXMLAssociation21(AssociationType1 association, ObjectLibrary objectLibrary) {
        notNull(objectLibrary, "objectLibrary cannot be null");
        notNull(association, "association cannot be null");
        
        this.association = association;
        this.objectLibrary = objectLibrary;
    }

    static EbXMLAssociation create(ObjectLibrary objectLibrary) {        
        return new EbXMLAssociation21(rimFactory.createAssociationType1(), objectLibrary);
    }

    static EbXMLAssociation create(AssociationType1 association, ObjectLibrary objectLibrary) {        
        return new EbXMLAssociation21(association, objectLibrary);
    }

    @Override
    public String getSource() {
        return objectLibrary.getByObj(association.getSourceObject());
    }

    @Override
    public void setSource(String source) {
        association.setSourceObject(objectLibrary.getById(source));
    }

    @Override
    public String getTarget() {
        return objectLibrary.getByObj(association.getTargetObject());
    }

    @Override
    public void setTarget(String target) {
        association.setTargetObject(objectLibrary.getById(target));
    }

    @Override
    public AssociationType getAssociationType() {
        return AssociationType.valueOfOpcode(association.getAssociationType());
    }

    @Override
    public void setAssociationType(AssociationType associationType) {
        association.setAssociationType(AssociationType.getOpcode21(associationType));
    }

    AssociationType1 getInternal() {
        return association;
    }
}
