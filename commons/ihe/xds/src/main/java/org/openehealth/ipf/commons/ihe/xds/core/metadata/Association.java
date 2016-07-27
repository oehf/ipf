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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.ExtraMetadataHolder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.ExtraMetadata;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.ExtraMetadataAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an XDS association.
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Association", propOrder = {
        "entryUuid", "sourceUuid", "targetUuid", "associationType", "label", "docCode",
        "previousVersion", "originalStatus", "newStatus", "associationPropagation", "availabilityStatus",
        "extraMetadata"})
@XmlRootElement(name = "association")
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
public class Association implements Serializable, ExtraMetadataHolder {

    private static final long serialVersionUID = -4556980177483609469L;

    private String targetUuid;
    private String sourceUuid;
    private AssociationType associationType;
    private AssociationLabel label;
    private String entryUuid;
    private Code docCode;
    private String previousVersion;
    private AvailabilityStatus originalStatus;
    private AvailabilityStatus newStatus;
    private Boolean associationPropagation;
    private AvailabilityStatus availabilityStatus;

    @XmlJavaTypeAdapter(ExtraMetadataAdapter.class)
    @XmlElement(type = ExtraMetadata.class)
    @Getter @Setter private Map<String, List<String>> extraMetadata;

    /**
     * Constructs an association.
     */
    public Association() {}
    
    /**
     * Constructs an association.
     * @param associationType
     *          the type of the association.
     * @param entryUuid
     *          UUID of the association entry.
     * @param sourceUuid
     *          the UUID of the source object.
     * @param targetUuid
     *          the UUID of the target object.
     */
    public Association(AssociationType associationType, String entryUuid, String sourceUuid, String targetUuid) {
        this.associationType = associationType;
        this.entryUuid = entryUuid;
        this.sourceUuid = sourceUuid;
        this.targetUuid = targetUuid;
    }

    /**
     * @return the UUID of the target object.
     */
    public String getTargetUuid() {
        return targetUuid;
    }

    /**
     * @param targetUuid
     *          the UUID of the target object.
     */
    public void setTargetUuid(String targetUuid) {
        this.targetUuid = targetUuid;
    }

    /**
     * @return the UUID of the source object.
     */
    public String getSourceUuid() {
        return sourceUuid;
    }

    /**
     * @param sourceUuid
     *          the UUID of the source object.
     */
    public void setSourceUuid(String sourceUuid) {
        this.sourceUuid = sourceUuid;
    }

    /**
     * @return the type of this association.
     */
    public AssociationType getAssociationType() {
        return associationType;
    }

    /**
     * @param associationType
     *          the type of this association.
     */
    public void setAssociationType(AssociationType associationType) {
        this.associationType = associationType;
    }

    /**
     * @return the label of the association.
     */
    public AssociationLabel getLabel() {
        return label;
    }

    /**
     * @param label
     *          the label of the association.
     */
    public void setLabel(AssociationLabel label) {
        this.label = label;
    }

    /**
     * @return UUID of this association entry.
     */
    public String getEntryUuid() {
        return entryUuid;
    }

    /**
     * @param entryUuid
     *          UUID of this association entry.
     */
    public void setEntryUuid(String entryUuid) {
        this.entryUuid = entryUuid;
    }

    /**
     * @return code describing the association (e.g. the type of transformation,
     *          reason for replacement).
     */
    public Code getDocCode() {
        return docCode;
    }

    /**
     * @param docCode
     *          code describing the association (e.g. the type of transformation,
     *          reason for replacement).
     */
    public void setDocCode(Code docCode) {
        this.docCode = docCode;
    }

    /**
     *
     * @return previous version slot value
     */
    public String getPreviousVersion() {
        return previousVersion;
    }

    /**
     *
     * @param previousVersion
     *          value of previous version in XDS metadata update association
     */
    public void setPreviousVersion(String previousVersion) {
        this.previousVersion = previousVersion;
    }

    /**
     * @return original status slot value
     */
    public AvailabilityStatus getOriginalStatus() {
        return originalStatus;
    }

    /**
     * @param originalStatus
     *           value of originalStatus in update availabilityStatus
     */
    public void setOriginalStatus(AvailabilityStatus originalStatus) {
        this.originalStatus = originalStatus;
    }

    /**
     * @return new status slot value
     */
    public AvailabilityStatus getNewStatus() {
        return newStatus;
    }

    /**
     * @param newStatus
     *           value of newStatus in update availabilityStatus
     */
    public void setNewStatus(AvailabilityStatus newStatus) {
        this.newStatus = newStatus;
    }

    /**
     * @return associationPropagation annotation value
     */
    public Boolean getAssociationPropagation() {
        return associationPropagation;
    }

    /**
     * @param associationPropagation
     *           value of associationPropagation annotation
     */
    public void setAssociationPropagation(Boolean associationPropagation) {
        this.associationPropagation = associationPropagation;
    }

    /**
     *
     * @return availabilityStatus value in XDS metadata update association
     */
    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    /**
     *
     * @param availabilityStatus
     *           value of availabilityStatus in XDS metadata update association
     */
    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

}
