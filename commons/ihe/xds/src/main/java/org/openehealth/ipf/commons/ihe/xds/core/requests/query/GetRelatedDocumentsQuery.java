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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;

import jakarta.xml.bind.annotation.*;

import java.io.Serial;
import java.util.List;

/**
 * Represents a stored query for GetRelatedDocuments.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetRelatedDocumentsQuery", propOrder = {
        "associationTypes", "documentEntryTypes", "associationStatuses", "metadataLevel"})
@XmlRootElement(name = "getRelatedDocumentsQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class GetRelatedDocumentsQuery extends GetFromDocumentQuery
        implements DocumentEntryTypeAwareStoredQuery
{
    @Serial
    private static final long serialVersionUID = -8768793068458839362L;

    @XmlElement(name = "associationType")
    @Getter @Setter private List<AssociationType> associationTypes;
    @XmlElement(name = "documentEntryType")
    @Getter @Setter private List<DocumentEntryType> documentEntryTypes;
    @XmlElement(name = "associationStatus")
    @Getter @Setter private List<AvailabilityStatus> associationStatuses;
    @Getter @Setter private Integer metadataLevel;

    /**
     * Constructs the query.
     */
    public GetRelatedDocumentsQuery() {
        super(QueryType.GET_RELATED_DOCUMENTS);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
