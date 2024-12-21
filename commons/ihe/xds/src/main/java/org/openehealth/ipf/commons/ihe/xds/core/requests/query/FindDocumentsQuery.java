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
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentAvailability;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;

import jakarta.xml.bind.annotation.*;

import java.io.Serial;
import java.util.List;

/**
 * Represents a stored query for FindDocuments.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindDocumentsQuery", propOrder = {
        "patientId", "status", "documentEntryTypes", "documentAvailability", "metadataLevel", "targetCommunityIds"})
@XmlRootElement(name = "findDocumentsQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class FindDocumentsQuery extends DocumentsQuery implements PatientIdBasedStoredQuery, DocumentEntryTypeAwareStoredQuery, TargetCommunityIdListBasedStoredQuery {
    @Serial
    private static final long serialVersionUID = -5765363916663583605L;

    @Getter @Setter private Identifiable patientId;
    @Getter @Setter private List<AvailabilityStatus> status;
    @XmlElement(name = "documentEntryType")
    @Getter @Setter private List<DocumentEntryType> documentEntryTypes;
    @Getter @Setter private List<DocumentAvailability> documentAvailability;
    @Getter @Setter private Integer metadataLevel;
    @Getter @Setter private List<String> targetCommunityIds;

    /**
     * Constructs the query.
     */
    public FindDocumentsQuery() {
        super(QueryType.FIND_DOCUMENTS);
    }

    protected FindDocumentsQuery(QueryType type) {
        super(type);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
