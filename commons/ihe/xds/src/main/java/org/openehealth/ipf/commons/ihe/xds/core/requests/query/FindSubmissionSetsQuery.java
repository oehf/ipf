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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.TimeRange;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Represents a stored query for FindSubmissionSets.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindSubmissionSetsQuery", propOrder = {
        "status", "sourceIds", "authorPerson", "submissionTime", "contentTypeCodes", "patientId"})
@XmlRootElement(name = "findSubmissionSetsQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
public class FindSubmissionSetsQuery extends StoredQuery implements PatientIdBasedStoredQuery {
    private static final long serialVersionUID = 1712346604151312305L;

    @Getter @Setter private List<AvailabilityStatus> status;
    @XmlElement(name = "sourceId")
    @Getter @Setter private List<String> sourceIds;
    @XmlElement(name = "contentTypeCode")
    @Getter @Setter private List<Code> contentTypeCodes;
    @Getter @Setter private String authorPerson;
    @Getter @Setter private Identifiable patientId;

    @Getter private final TimeRange submissionTime = new TimeRange();

    /**
     * Constructs the query.
     */
    public FindSubmissionSetsQuery() {
        super(QueryType.FIND_SUBMISSION_SETS);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
