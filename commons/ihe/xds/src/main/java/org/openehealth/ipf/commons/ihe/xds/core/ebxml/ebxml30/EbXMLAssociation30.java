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

import org.openehealth.ipf.commons.ihe.xds.core.ebxml.enumfactories.AssociationTypeFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.enumfactories.AvailabilityStatusFactory30;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLAssociation;
import org.openehealth.ipf.commons.ihe.xds.core.ebxml.EbXMLObjectLibrary;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.stub.ebrs30.rim.AssociationType1;

import java.util.List;
import java.util.Map;

import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_ORIGINAL_STATUS;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_NEW_STATUS;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.Vocabulary.SLOT_NAME_PREVIOUS_VERSION;

/**
 * Encapsulation of {@link AssociationType1}.
 * @author Jens Riemschneider
 */
public class EbXMLAssociation30 extends EbXMLRegistryObject30<AssociationType1> implements EbXMLAssociation {
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
        return new AssociationTypeFactory30().fromEbXML(getInternal().getAssociationType());
    }

    @Override
    public void setAssociationType(AssociationType associationType) {
        getInternal().setAssociationType(new AssociationTypeFactory30().toEbXML(associationType));
    }

    @Override
    public AvailabilityStatus getStatus() {
        return new AvailabilityStatusFactory30().fromEbXML(getInternal().getStatus());
    }

    @Override
    public void setStatus(AvailabilityStatus availabilityStatus) {
        getInternal().setStatus(new AvailabilityStatusFactory30().toEbXML(availabilityStatus));
    }

    @Override
    public AvailabilityStatus getOriginalStatus() {
        return new AvailabilityStatusFactory30().fromEbXML(getSingleSlotValue(SLOT_NAME_ORIGINAL_STATUS));
    }

    @Override
    public void setOriginalStatus(AvailabilityStatus status) {
        String s = new AvailabilityStatusFactory30().toEbXML(status);
        if (s != null) {
            addSlot(SLOT_NAME_ORIGINAL_STATUS, s);
        }
    }

    @Override
    public AvailabilityStatus getNewStatus() {
        return new AvailabilityStatusFactory30().fromEbXML(getSingleSlotValue(SLOT_NAME_NEW_STATUS));
    }

    @Override
    public void setNewStatus(AvailabilityStatus status) {
        String s = new AvailabilityStatusFactory30().toEbXML(status);
        if (s != null) {
            addSlot(SLOT_NAME_NEW_STATUS, s);
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
    public Map<String, List<String>> getExtraMetadata() {
        return getInternal().getExtraMetadata();
    }

    @Override
    public void setExtraMetadata(Map<String, List<String>> map) {
        getInternal().setExtraMetadata(map);
    }

}
