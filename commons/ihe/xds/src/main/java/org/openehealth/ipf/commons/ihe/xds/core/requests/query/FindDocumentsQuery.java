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

import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * Represents a stored query for FindDocuments.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindDocumentsQuery", propOrder = {
        "status", "authorPersons", "creationTime", "serviceStartTime", "serviceStopTime", "classCodes",
        "confidentialityCodes", "eventCodes", "formatCodes", "healthcareFacilityTypeCodes", "practiceSettingCodes",
        "typeCodes"})
@XmlRootElement(name = "findDocumentsQuery")
public class FindDocumentsQuery extends PatientIdBasedStoredQuery implements Serializable {
    private static final long serialVersionUID = -5765363916663583605L;

    private List<AvailabilityStatus> status;
    @XmlElement(name = "typeCode")
    private List<Code> typeCodes;
    @XmlElement(name = "classCode")
    private List<Code> classCodes;
    @XmlElement(name = "practiceSettingCode")
    private List<Code> practiceSettingCodes;
    @XmlElement(name = "healthcareFacilityTypeCode")
    private List<Code> healthcareFacilityTypeCodes;
    @XmlElement(name = "eventCode")
    private QueryList<Code> eventCodes;
    @XmlElement(name = "confidentialityCode")
    private QueryList<Code> confidentialityCodes;
    @XmlElement(name = "formatCode")
    private List<Code> formatCodes;
    @XmlElement(name = "authorPerson")
    private List<String> authorPersons;

    private final TimeRange creationTime = new TimeRange();
    private final TimeRange serviceStartTime = new TimeRange();
    private final TimeRange serviceStopTime = new TimeRange();

    /**
     * Constructs the query.
     */
    public FindDocumentsQuery() {
        super(QueryType.FIND_DOCUMENTS);
    }

    /**
     * @return the states for filtering {@link DocumentEntry#getAvailabilityStatus()}.
     */
    public List<AvailabilityStatus> getStatus() {
        return status;
    }
    
    /**
     * @param status    
     *          the states for filtering {@link DocumentEntry#getAvailabilityStatus()}.
     */
    public void setStatus(List<AvailabilityStatus> status) {
        this.status = status;
    }

    /**
     * @return the time range for filtering {@link DocumentEntry#getCreationTime()}.
     */
    public TimeRange getCreationTime() {
        return creationTime;
    }

    /**
     * @return the time range for filtering {@link DocumentEntry#getServiceStartTime()}.
     */
    public TimeRange getServiceStartTime() {
        return serviceStartTime;
    }

    /**
     * @return the time range for filtering {@link DocumentEntry#getServiceStopTime()}.
     */
    public TimeRange getServiceStopTime() {
        return serviceStopTime;
    }

    /**
     * @return the codes for filtering {@link DocumentEntry#getClassCode()}.
     */
    public List<Code> getClassCodes() {
        return classCodes;
    }

    /**
     * @param classCodes
     *          the codes for filtering {@link DocumentEntry#getClassCode()}.
     */
    public void setClassCodes(List<Code> classCodes) {
        this.classCodes = classCodes;
    }

    /**
     * @return the codes for filtering {@link DocumentEntry#getTypeCode()}.
     */
    public List<Code> getTypeCodes() {
        return typeCodes;
    }

    /**
     * @param typeCodes
     *          the codes for filtering {@link DocumentEntry#getTypeCode()}.
     */
    public void setTypeCodes(List<Code> typeCodes) {
        this.typeCodes = typeCodes;
    }

    /**
     * @return the codes for filtering {@link DocumentEntry#getPracticeSettingCode()}.
     */
    public List<Code> getPracticeSettingCodes() {
        return practiceSettingCodes;
    }
    
    /**
     * @param practiceSettingCodes
     *          the codes for filtering {@link DocumentEntry#getPracticeSettingCode()}.
     */
    public void setPracticeSettingCodes(List<Code> practiceSettingCodes) {
        this.practiceSettingCodes = practiceSettingCodes;
    }

    /**
     * @return the codes for filtering {@link DocumentEntry#getHealthcareFacilityTypeCode()}.
     */
    public List<Code> getHealthcareFacilityTypeCodes() {
        return healthcareFacilityTypeCodes;
    }
    
    /**
     * @param healthcareFacilityTypeCodes
     *          the codes for filtering {@link DocumentEntry#getHealthcareFacilityTypeCode()}.
     */
    public void setHealthcareFacilityTypeCodes(List<Code> healthcareFacilityTypeCodes) {
        this.healthcareFacilityTypeCodes = healthcareFacilityTypeCodes;
    }

    /**
     * @return the codes for filtering {@link DocumentEntry#getEventCodeList()}.
     */
    public QueryList<Code> getEventCodes() {
        return eventCodes;
    }

    /**
     * @param eventCodes
     *          the codes for filtering {@link DocumentEntry#getEventCodeList()}.
     */
    public void setEventCodes(QueryList<Code> eventCodes) {
        this.eventCodes = eventCodes;
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

    /**
     * @return the author persons texts for filtering {@link DocumentEntry#getAuthors()}.
     */
    public List<String> getAuthorPersons() {
        return authorPersons;
    }

    /**
     * @param authorPersons
     *          the author persons texts for filtering {@link DocumentEntry#getAuthors()}.
     */
    public void setAuthorPersons(List<String> authorPersons) {
        this.authorPersons = authorPersons;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((authorPersons == null) ? 0 : authorPersons.hashCode());
        result = prime * result + ((classCodes == null) ? 0 : classCodes.hashCode());
        result = prime * result + ((typeCodes == null) ? 0 : typeCodes.hashCode());
        result = prime * result
                + ((confidentialityCodes == null) ? 0 : confidentialityCodes.hashCode());
        result = prime * result + ((creationTime == null) ? 0 : creationTime.hashCode());
        result = prime * result + ((eventCodes == null) ? 0 : eventCodes.hashCode());
        result = prime * result + ((formatCodes == null) ? 0 : formatCodes.hashCode());
        result = prime
                * result
                + ((healthcareFacilityTypeCodes == null) ? 0 : healthcareFacilityTypeCodes
                        .hashCode());
        result = prime * result
                + ((practiceSettingCodes == null) ? 0 : practiceSettingCodes.hashCode());
        result = prime * result + ((serviceStartTime == null) ? 0 : serviceStartTime.hashCode());
        result = prime * result + ((serviceStopTime == null) ? 0 : serviceStopTime.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
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
        FindDocumentsQuery other = (FindDocumentsQuery) obj;
        if (authorPersons == null) {
            if (other.authorPersons != null)
                return false;
        } else if (!authorPersons.equals(other.authorPersons))
            return false;
        if (classCodes == null) {
            if (other.classCodes != null)
                return false;
        } else if (!classCodes.equals(other.classCodes))
            return false;
        if (typeCodes == null) {
            if (other.typeCodes != null)
                return false;
        } else if (!typeCodes.equals(other.typeCodes))
            return false;
        if (confidentialityCodes == null) {
            if (other.confidentialityCodes != null)
                return false;
        } else if (!confidentialityCodes.equals(other.confidentialityCodes))
            return false;
        if (creationTime == null) {
            if (other.creationTime != null)
                return false;
        } else if (!creationTime.equals(other.creationTime))
            return false;
        if (eventCodes == null) {
            if (other.eventCodes != null)
                return false;
        } else if (!eventCodes.equals(other.eventCodes))
            return false;
        if (formatCodes == null) {
            if (other.formatCodes != null)
                return false;
        } else if (!formatCodes.equals(other.formatCodes))
            return false;
        if (healthcareFacilityTypeCodes == null) {
            if (other.healthcareFacilityTypeCodes != null)
                return false;
        } else if (!healthcareFacilityTypeCodes.equals(other.healthcareFacilityTypeCodes))
            return false;
        if (practiceSettingCodes == null) {
            if (other.practiceSettingCodes != null)
                return false;
        } else if (!practiceSettingCodes.equals(other.practiceSettingCodes))
            return false;
        if (serviceStartTime == null) {
            if (other.serviceStartTime != null)
                return false;
        } else if (!serviceStartTime.equals(other.serviceStartTime))
            return false;
        if (serviceStopTime == null) {
            if (other.serviceStopTime != null)
                return false;
        } else if (!serviceStopTime.equals(other.serviceStopTime))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        return true;
    }
}
