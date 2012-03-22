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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;

/**
 * Represents a stored query for GetAll.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAllQuery", propOrder = {
        "statusDocuments", "statusSubmissionSets", "statusFolders", "confidentialityCodes", "formatCodes",
        "documentEntryTypes", "patientId"})
@XmlRootElement(name = "getAllQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
public class GetAllQuery extends StoredQuery
        implements PatientIdBasedStoredQuery, DocumentEntryTypeAwareStoredQuery
{
    private static final long serialVersionUID = -4161172318244319631L;

    @XmlElement(name = "documentStatus")
    @Getter @Setter private List<AvailabilityStatus> statusDocuments;
    @XmlElement(name = "submissionSetStatus")
    @Getter @Setter private List<AvailabilityStatus> statusSubmissionSets;
    @XmlElement(name = "folderStatus")
    @Getter @Setter private List<AvailabilityStatus> statusFolders;
    @XmlElement(name = "confidentialityCode")
    @Getter @Setter private QueryList<Code> confidentialityCodes;
    @XmlElement(name = "formatCode")
    @Getter @Setter private List<Code> formatCodes;
    @XmlElement(name = "documentEntryType")
    @Getter @Setter private List<DocumentEntryType> documentEntryTypes;
    @Getter @Setter private Identifiable patientId;

    /**
     * Constructs the query.
     */
    public GetAllQuery() {
        super(QueryType.GET_ALL);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
