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
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openehealth.ipf.commons.ihe.xds.core.ExtraMetadataHolder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.ExtraMetadata;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.ExtraMetadataAdapter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.XdsEnumAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
public class Association implements Serializable, ExtraMetadataHolder {
    private static final long serialVersionUID = -4556980177483609469L;

    @Getter @Setter private String targetUuid;
    @Getter @Setter private String sourceUuid;

    @XmlJavaTypeAdapter(XdsEnumAdapter.AssociationTypeAdapter.class)
    @Getter @Setter private AssociationType associationType;

    @XmlJavaTypeAdapter(XdsEnumAdapter.AssociationLabelAdapter.class)
    @Getter @Setter private AssociationLabel label;

    @Getter @Setter private String entryUuid;
    @Getter @Setter private Code docCode;
    @Getter @Setter private String previousVersion;

    @XmlJavaTypeAdapter(XdsEnumAdapter.AvailabilityStatusAdapter.class)
    @Getter @Setter private AvailabilityStatus originalStatus;

    @XmlJavaTypeAdapter(XdsEnumAdapter.AvailabilityStatusAdapter.class)
    @Getter @Setter private AvailabilityStatus newStatus;

    @Getter @Setter private Boolean associationPropagation;

    @XmlJavaTypeAdapter(XdsEnumAdapter.AvailabilityStatusAdapter.class)
    @Getter @Setter private AvailabilityStatus availabilityStatus;

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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
