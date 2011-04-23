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
package org.openehealth.ipf.commons.ihe.xds.core.requests.query;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;

/**
 * Represents a stored query for GetAll.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAllQuery", propOrder = {
        "statusDocuments", "statusSubmissionSets", "statusFolders", "confidentialityCodes", "formatCodes"})
@XmlRootElement(name = "getAllQuery")
public class GetAllQuery extends PatientIdBasedStoredQuery implements Serializable {
    private static final long serialVersionUID = -4161172318244319631L;

    @XmlElement(name = "documentStatus")
    private List<AvailabilityStatus> statusDocuments;
    @XmlElement(name = "submissionSetStatus")
    private List<AvailabilityStatus> statusSubmissionSets;
    @XmlElement(name = "folderStatus")
    private List<AvailabilityStatus> statusFolders;

    @XmlElement(name = "confidentialityCode")
    private QueryList<Code> confidentialityCodes;
    @XmlElement(name = "formatCode")
    private List<Code> formatCodes;

    /**
     * Constructs the query.
     */
    public GetAllQuery() {
        super(QueryType.GET_ALL);
    }

    /**
     * @return the states for filtering {@link DocumentEntry#getAvailabilityStatus()}.
     */
    public List<AvailabilityStatus> getStatusDocuments() {
        return statusDocuments;
    }

    /**
     * @param statusDocuments
     *          the states for filtering {@link DocumentEntry#getAvailabilityStatus()}.
     */
    public void setStatusDocuments(List<AvailabilityStatus> statusDocuments) {
        this.statusDocuments = statusDocuments;
    }

    /**
     * @return the states for filtering {@link SubmissionSet#getAvailabilityStatus()}.
     */
    public List<AvailabilityStatus> getStatusSubmissionSets() {
        return statusSubmissionSets;
    }

    /**
     * @param statusSubmissionSets
     *          the states for filtering {@link SubmissionSet#getAvailabilityStatus()}.
     */
    public void setStatusSubmissionSets(List<AvailabilityStatus> statusSubmissionSets) {
        this.statusSubmissionSets = statusSubmissionSets;
    }

    /**
     * @return the states for filtering {@link Folder#getAvailabilityStatus()}.
     */
    public List<AvailabilityStatus> getStatusFolders() {
        return statusFolders;
    }

    /**
     * @param statusFolders
     *          the states for filtering {@link Folder#getAvailabilityStatus()}.
     */
    public void setStatusFolders(List<AvailabilityStatus> statusFolders) {
        this.statusFolders = statusFolders;
    }

    /**
     * @return the codes for filtering {@link DocumentEntry#getConfidentialityCodes()}.
     */
    public QueryList<Code> getConfidentialityCodes() {
        return confidentialityCodes;
    }

    /**
     * @param confidentialityCodes
     *          the codes for filtering {@link DocumentEntry#getConfidentialityCodes()}.
     */
    public void setConfidentialityCodes(QueryList<Code> confidentialityCodes) {
        this.confidentialityCodes = confidentialityCodes;
    }

    /**
     * @return the codes for filtering {@link DocumentEntry#getFormatCode()}.
     */
    public List<Code> getFormatCodes() {
        return formatCodes;
    }

    /**
     * @param formatCodes
     *          the codes for filtering {@link DocumentEntry#getFormatCode()}.
     */
    public void setFormatCodes(List<Code> formatCodes) {
        this.formatCodes = formatCodes;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((confidentialityCodes == null) ? 0 : confidentialityCodes.hashCode());
        result = prime * result + ((formatCodes == null) ? 0 : formatCodes.hashCode());
        result = prime * result + ((statusDocuments == null) ? 0 : statusDocuments.hashCode());
        result = prime * result + ((statusFolders == null) ? 0 : statusFolders.hashCode());
        result = prime * result
                + ((statusSubmissionSets == null) ? 0 : statusSubmissionSets.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        GetAllQuery other = (GetAllQuery) obj;
        if (confidentialityCodes == null) {
            if (other.confidentialityCodes != null)
                return false;
        } else if (!confidentialityCodes.equals(other.confidentialityCodes))
            return false;
        if (formatCodes == null) {
            if (other.formatCodes != null)
                return false;
        } else if (!formatCodes.equals(other.formatCodes))
            return false;
        if (statusDocuments == null) {
            if (other.statusDocuments != null)
                return false;
        } else if (!statusDocuments.equals(other.statusDocuments))
            return false;
        if (statusFolders == null) {
            if (other.statusFolders != null)
                return false;
        } else if (!statusFolders.equals(other.statusFolders))
            return false;
        if (statusSubmissionSets == null) {
            if (other.statusSubmissionSets != null)
                return false;
        } else if (!statusSubmissionSets.equals(other.statusSubmissionSets))
            return false;
        return true;
    }
}
