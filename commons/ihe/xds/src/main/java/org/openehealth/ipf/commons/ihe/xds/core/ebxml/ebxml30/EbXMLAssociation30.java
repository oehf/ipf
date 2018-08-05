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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAssociation;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.AssociationType1;
import org.openehealth.ipf.commons.ihe.xds.core.transform.ebxml.StringToBoolTransformer;

import java.util.List;
import java.util.Map;

import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.*;

/**
 * Encapsulation of {@link AssociationType1}.
 * @author Jens Riemschneider
 */
public class EbXMLAssociation30 extends EbXMLRegistryObject30<AssociationType1> implements EbXMLAssociation {

    private final StringToBoolTransformer stringToBoolTransformer = new StringToBoolTransformer();

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
    public AvailabilityStatus getStatus() {
        return AvailabilityStatus.valueOfOpcode(getInternal().getStatus());
    }

    @Override
    public void setStatus(AvailabilityStatus availabilityStatus) {
        getInternal().setStatus(AvailabilityStatus.toQueryOpcode(availabilityStatus));
    }

    @Override
    public AvailabilityStatus getOriginalStatus() {
        return AvailabilityStatus.valueOfOpcode(getSingleSlotValue(SLOT_NAME_ORIGINAL_STATUS));
    }

    @Override
    public void setOriginalStatus(AvailabilityStatus status) {
        if (status != null) {
            addSlot(SLOT_NAME_ORIGINAL_STATUS, status.getOpcode());
        }
    }

    @Override
    public AvailabilityStatus getNewStatus() {
        return AvailabilityStatus.valueOfOpcode(getSingleSlotValue(SLOT_NAME_NEW_STATUS));
    }

    @Override
    public void setNewStatus(AvailabilityStatus status) {
        if (status != null) {
            addSlot(SLOT_NAME_NEW_STATUS, status.getOpcode());
        }
    }

    @Override
    public String getPreviousVersion() {
        return getSingleSlotValue(SLOT_NAME_PREVIOUS_VERSION);
    }

    @Override
    public void setPreviousVersion(String version) {
        addSlot(SLOT_NAME_PREVIOUS_VERSION, version);
    }

    @Override
    public Boolean getAssociationPropagation() {
        return stringToBoolTransformer.fromEbXML(getSingleSlotValue(SLOT_NAME_ASSOCIATION_PROPAGATION));
    }

    @Override
    public void setAssociationPropagation(Boolean value) {
        if (value != null) {
            addSlot(SLOT_NAME_ASSOCIATION_PROPAGATION, stringToBoolTransformer.toEbXML(value));
        }
    }

    @Override
    public Map<String, List<String>> getExtraMetadata() {
        return getInternal().getExtraMetadata();
    }

    @Override
    public void setExtraMetadata(Map<String, List<String>> map) {
        getInternal().setExtraMetadata(map);
    }
}
