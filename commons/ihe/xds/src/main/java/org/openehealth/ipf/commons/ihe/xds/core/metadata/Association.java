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
import org.openehealth.ipf.commons.core.URN;
import org.openehealth.ipf.commons.ihe.xds.core.ExtraMetadataHolder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.StringMap;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.StringMapAdapter;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Getter @Setter private String targetUuid;
    @Getter @Setter private String sourceUuid;
    @Getter @Setter private AssociationType associationType;
    @Getter @Setter private AssociationLabel label;
    @Getter @Setter private String entryUuid;
    @Getter @Setter private Code docCode;
    @Getter @Setter private String previousVersion;
    /**
     * @return original status slot value
     * @param originalStatus
     *           value of originalStatus in update availabilityStatus
     */
    @Getter @Setter private AvailabilityStatus originalStatus;
    /**
     * @return new status slot value
     * @param newStatus value of newStatus in update availabilityStatus
     */
    @Getter @Setter private AvailabilityStatus newStatus;
    @Getter @Setter private Boolean associationPropagation;
    /**
    *
    * @return availabilityStatus value in XDS metadata update association
    * @param availabilityStatus
    *           value of availabilityStatus in XDS metadata update association
    */
    @Getter @Setter private AvailabilityStatus availabilityStatus;

    @XmlJavaTypeAdapter(StringMapAdapter.class)
    @XmlElement(name = "extraMetadata", type = StringMap.class)
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
    
    public void assignEntryUuid() {
        this.entryUuid = new URN(UUID.randomUUID()).toString();
    }

}
