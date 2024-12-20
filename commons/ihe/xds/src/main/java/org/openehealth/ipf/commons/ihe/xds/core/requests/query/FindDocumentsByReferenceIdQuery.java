/*
 * Copyright 2013 the original author or authors.
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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ReferenceId;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.QuerySlotHelper;

import jakarta.xml.bind.annotation.*;

import java.io.Serial;

/**
 * Represents a stored query for FindDocumentsByReferenceIdQuery.
 * @author Dmytro Rud
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindDocumentsByReferenceIdQuery", propOrder = {"referenceIds"})
@XmlRootElement(name = "findDocumentsByReferenceIdQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class FindDocumentsByReferenceIdQuery extends FindDocumentsQuery {
    @Serial
    private static final long serialVersionUID = 8898792914033157098L;

    @XmlElement(name = "referenceId")
    @Getter @Setter private QueryList<String> referenceIds;


    /**
     * Constructs the query.
     */
    public FindDocumentsByReferenceIdQuery() {
        super(QueryType.FIND_DOCUMENTS_BY_REFERENCE_ID);
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
