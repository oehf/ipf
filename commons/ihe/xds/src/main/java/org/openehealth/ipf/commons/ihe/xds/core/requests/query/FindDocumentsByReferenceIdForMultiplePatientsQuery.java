/*
 * Copyright 2024 the original author or authors.
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

import jakarta.xml.bind.annotation.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ReferenceId;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.QuerySlotHelper;

import java.io.Serial;
import java.util.List;

/**
 * Represents a Multi-Patient stored query for FindDocuments by Reference ID.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindDocumentsByReferenceIdForMultiplePatientsQuery",
        propOrder = {"patientIds","status", "documentEntryTypes", "referenceIds"})
@XmlRootElement(name = "findDocumentsByReferenceIdForMultiplePatientsQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class FindDocumentsByReferenceIdForMultiplePatientsQuery extends DocumentsQuery implements DocumentEntryTypeAwareStoredQuery {
    @Serial
    private static final long serialVersionUID = -8981126710746958226L;

    @Getter @Setter private List<Identifiable> patientIds;
    @Getter @Setter private List<AvailabilityStatus> status;
    @XmlElement(name = "documentEntryType")
    @Getter @Setter private List<DocumentEntryType> documentEntryTypes;
    @XmlElement(name = "referenceId")
    @Getter @Setter private QueryList<String> referenceIds;

    /**
     * Constructs the query.
     */
    public FindDocumentsByReferenceIdForMultiplePatientsQuery() {
        super(QueryType.FIND_DOCUMENTS_BY_REFERENCE_ID_MPQ);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    /**
     * Allows to use a collection of {@link ReferenceId} instead of a collection of {@link String}
     * for specifying the query parameter "$XDSDocumentEntryReferenceIdList".
     *
     * @param referenceIds a collection of {@link ReferenceId} objects with AND/OR semantics.
     */
    public void setTypedReferenceIds(QueryList<ReferenceId> referenceIds) {
        this.referenceIds = QuerySlotHelper.render(referenceIds);
    }

    /**
     * Tries to return the query parameter "$XDSDocumentEntryReferenceIdList"
     * as a collection of {@link ReferenceId} instead of a collection of {@link String}.
     * This may fail if SQL LIKE wildcards ("%", "_", etc.) are used in one or more elements.
     *
     * @return a collection of {@link ReferenceId} objects with AND/OR semantics.
     */
    public QueryList<ReferenceId> getTypedReferenceIds() {
        return QuerySlotHelper.parse(this.referenceIds, ReferenceId.class);
    }

}
