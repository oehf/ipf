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
package org.openehealth.ipf.commons.ihe.xds.core.ebxml.ebxml21;

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAssociation;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs21.rim.AssociationType1;

/**
 * Encapsulation of {@link AssociationType1}.
 * @author Jens Riemschneider
 */
public class EbXMLAssociation21 extends EbXMLRegistryObject21<AssociationType1> implements EbXMLAssociation {
    /**
     * Constructs an association by wrapping this given object.
     * @param association
     *          the association to wrap.
     * @param objectLibrary
     *          the object library to use.
     */
    public EbXMLAssociation21(AssociationType1 association, EbXMLObjectLibrary objectLibrary) {
        super(association, objectLibrary);
    }

    @Override
    public String getSource() {
        return getObjectLibrary().getByObj(getInternal().getSourceObject());
    }

    @Override
    public void setSource(String source) {
        getInternal().setSourceObject(getObjectLibrary().getById(source));
    }

    @Override
    public String getTarget() {
        return getObjectLibrary().getByObj(getInternal().getTargetObject());
    }

    @Override
    public void setTarget(String target) {
        getInternal().setTargetObject(getObjectLibrary().getById(target));
    }

    @Override
    public AssociationType getAssociationType() {
        return AssociationType.valueOfOpcode21(getInternal().getAssociationType());
    }

    @Override
    public void setAssociationType(AssociationType associationType) {
        getInternal().setAssociationType(AssociationType.getOpcode21(associationType));
    }
}
