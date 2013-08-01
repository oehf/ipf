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
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAssociation;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.AssociationType1;

import java.util.ArrayList;
import java.util.Map;

/**
 * Encapsulation of {@link AssociationType1}.
 * @author Jens Riemschneider
 */
public class EbXMLAssociation30 extends EbXMLRegistryObject30<AssociationType1> implements EbXMLAssociation, ExtraMetadataHolder {
    /**
     * Constructs an association by wrapping the given ebXML 3.0 object.
     * @param association
     *          the object to wrap.
     * @param objectLibrary
     *          the object library to use.
     */
    public EbXMLAssociation30(AssociationType1 association, EbXMLObjectLibrary objectLibrary) {
        super(association, objectLibrary);
    }

    @Override
    public String getSource() {
        return getInternal().getSourceObject();
    }

    @Override
    public String getTarget() {
        return getInternal().getTargetObject();
    }

    @Override
    public void setSource(String source) {
        getInternal().setSourceObject(source);
    }

    @Override
    public void setTarget(String target) {
        getInternal().setTargetObject(target);
    }

    @Override
    public AssociationType getAssociationType() {
        return AssociationType.valueOfOpcode30(getInternal().getAssociationType());
    }

    @Override
    public void setAssociationType(AssociationType associationType) {
        getInternal().setAssociationType(AssociationType.getOpcode30(associationType));
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
