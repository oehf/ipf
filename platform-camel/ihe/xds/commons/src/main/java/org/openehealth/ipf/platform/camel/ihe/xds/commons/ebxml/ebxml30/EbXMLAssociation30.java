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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.ebxml.EbXMLAssociation;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationType;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.stub.ebrs30.rim.AssociationType1;

/**
 * Encapsulation of {@link AssociationType1}.
 * @author Jens Riemschneider
 */
public class EbXMLAssociation30 implements EbXMLAssociation {
    private final AssociationType1 association;

    /**
     * Constructs an association by wrapping the given ebXML 3.0 object.
     * @param association
     *          the object to wrap.
     */
    public EbXMLAssociation30(AssociationType1 association) {
        notNull(association, "association cannot be null");        
        this.association = association;
    }

    @Override
    public String getSource() {
        return association.getSourceObject();
    }

    @Override
    public String getTarget() {
        return association.getTargetObject();
    }

    @Override
    public void setSource(String source) {
        association.setSourceObject(source);
    }

    @Override
    public void setTarget(String target) {
        association.setTargetObject(target);
    }

    @Override
    public AssociationType getAssociationType() {
        return AssociationType.valueOfOpcode(association.getAssociationType());
    }

    @Override
    public void setAssociationType(AssociationType associationType) {
        association.setAssociationType(AssociationType.getOpcode30(associationType));
    }

    /**
     * @return the ebXML 3.0 representation of this object.
     */
    AssociationType1 getInternal() {
        return association;
    }
}
