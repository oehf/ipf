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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * Represents an association between two documents.
 * <p>
 * All members of this class are allowed to be <code>null</code>.
 * @author Jens Riemschneider
 */
public class Association {
    private String targetUUID;
    private String sourceUUID;
    private AssociationType associationType;
    private AssociationLabel label;
    private String entryUUID;
    private Code docCode;

    /**
     * @return the UUID of the target object.
     */
    public String getTargetUUID() {
        return targetUUID;
    }

    /**
     * @param targetUUID
     *          the UUID of the target object.
     */
    public void setTargetUUID(String targetUUID) {
        this.targetUUID = targetUUID;
    }

    /**
     * @return the UUID of the source object.
     */
    public String getSourceUUID() {
        return sourceUUID;
    }
    
    /**
     * @param sourceUUID
     *          the UUID of the source object.
     */
    public void setSourceUUID(String sourceUUID) {
        this.sourceUUID = sourceUUID;
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
    public String getEntryUUID() {
        return entryUUID;
    }

    /**
     * @param entryUUID
     *          UUID of this association entry.
     */
    public void setEntryUUID(String entryUUID) {
        this.entryUUID = entryUUID;
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
        result = prime * result + ((entryUUID == null) ? 0 : entryUUID.hashCode());
        result = prime * result + ((label == null) ? 0 : label.hashCode());
        result = prime * result + ((sourceUUID == null) ? 0 : sourceUUID.hashCode());
        result = prime * result + ((targetUUID == null) ? 0 : targetUUID.hashCode());
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
        if (entryUUID == null) {
            if (other.entryUUID != null)
                return false;
        } else if (!entryUUID.equals(other.entryUUID))
            return false;
        if (label == null) {
            if (other.label != null)
                return false;
        } else if (!label.equals(other.label))
            return false;
        if (sourceUUID == null) {
            if (other.sourceUUID != null)
                return false;
        } else if (!sourceUUID.equals(other.sourceUUID))
            return false;
        if (targetUUID == null) {
            if (other.targetUUID != null)
                return false;
        } else if (!targetUUID.equals(other.targetUUID))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
