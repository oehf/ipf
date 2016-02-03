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
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.jaxbadapters.XdsEnumAdapter;

/**
 * Represents a stored query for GetAll.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAllQuery", propOrder = {
        "statusDocuments", "statusSubmissionSets", "statusFolders", "confidentialityCodes", "formatCodes",
        "documentEntryTypes", "patientId", "associationStatuses", "metadataLevel"})
@XmlRootElement(name = "getAllQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
public class GetAllQuery extends StoredQuery
        implements PatientIdBasedStoredQuery, DocumentEntryTypeAwareStoredQuery
{
    private static final long serialVersionUID = -4161172318244319631L;

    @XmlJavaTypeAdapter(XdsEnumAdapter.AvailabilityStatusForQueryAdapter.class)
    @XmlElement(name = "documentStatus")
    @Getter @Setter private List<AvailabilityStatus> statusDocuments;

    @XmlJavaTypeAdapter(XdsEnumAdapter.AvailabilityStatusForQueryAdapter.class)
    @XmlElement(name = "submissionSetStatus")
    @Getter @Setter private List<AvailabilityStatus> statusSubmissionSets;

    @XmlJavaTypeAdapter(XdsEnumAdapter.AvailabilityStatusForQueryAdapter.class)
    @XmlElement(name = "folderStatus")
    @Getter @Setter private List<AvailabilityStatus> statusFolders;

    @XmlElement(name = "confidentialityCode")
    @Getter @Setter private QueryList<Code> confidentialityCodes;

    @XmlElement(name = "formatCode")
    @Getter @Setter private List<Code> formatCodes;

    @XmlJavaTypeAdapter(XdsEnumAdapter.DocumentEntryTypeAdapter.class)
    @XmlElement(name = "documentEntryType")
    @Getter @Setter private List<DocumentEntryType> documentEntryTypes;

    @Getter @Setter private Identifiable patientId;

    @XmlJavaTypeAdapter(XdsEnumAdapter.AvailabilityStatusForQueryAdapter.class)
    @XmlElement(name = "associationStatus")
    @Getter @Setter private List<AvailabilityStatus> associationStatuses;

    @Getter @Setter private Integer metadataLevel;

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
