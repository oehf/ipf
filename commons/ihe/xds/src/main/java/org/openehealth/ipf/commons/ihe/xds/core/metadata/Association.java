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

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.xml.bind.annotation.*;

/**
 * Represents an XDS association.
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Association", propOrder = {
        "entryUuid", "sourceUuid", "targetUuid", "associationType", "label", "docCode"})
@XmlRootElement(name = "association")
public class Association implements Serializable {
    private static final long serialVersionUID = -4556980177483609469L;
    
    private String targetUuid;
    private String sourceUuid;
    private AssociationType associationType;
    private AssociationLabel label;
    private String entryUuid;
    private Code docCode;
    
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((associationType == null) ? 0 : associationType.hashCode());
        result = prime * result + ((docCode == null) ? 0 : docCode.hashCode());
        result = prime * result + ((entryUuid == null) ? 0 : entryUuid.hashCode());
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((sourceUuid == null) ? 0 : sourceUuid.hashCode());
        result = prime * result + ((targetUuid == null) ? 0 : targetUuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Association other = (Association) obj;
        if (associationType == null) {
            if (other.associationType != null)
                return false;
        } else if (!associationType.equals(other.associationType))
            return false;
        if (docCode == null) {
            if (other.docCode != null)
                return false;
        } else if (!docCode.equals(other.docCode))
            return false;
        if (entryUuid == null) {
            if (other.entryUuid != null)
                return false;
        } else if (!entryUuid.equals(other.entryUuid))
            return false;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        if (sourceUuid == null) {
            if (other.sourceUuid != null)
                return false;
        } else if (!sourceUuid.equals(other.sourceUuid))
            return false;
        if (targetUuid == null) {
            if (other.targetUuid != null)
                return false;
        } else if (!targetUuid.equals(other.targetUuid))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
