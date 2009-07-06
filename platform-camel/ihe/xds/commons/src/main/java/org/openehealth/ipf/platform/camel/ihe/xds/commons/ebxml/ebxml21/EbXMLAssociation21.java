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
import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs21.rim.AssociationType1;

/**
 * Encapsulation of {@link AssociationType1}.
 * @author Jens Riemschneider
 */
public class EbXMLAssociation21 implements EbXMLAssociation {
    private final EbXMLObjectLibrary objectLibrary;    
    private final AssociationType1 association;

    /**
     * Constructs an association by wrapping this given object.
     * @param association
     *          the association to wrap.
     * @param objectLibrary
     *          the object library to use.
     */
    public EbXMLAssociation21(AssociationType1 association, EbXMLObjectLibrary objectLibrary) {
        notNull(objectLibrary, "objectLibrary cannot be null");
        notNull(association, "association cannot be null");
        
        this.association = association;
        this.objectLibrary = objectLibrary;
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
