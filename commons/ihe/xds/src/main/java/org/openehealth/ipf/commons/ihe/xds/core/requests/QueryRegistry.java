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
package org.openehealth.ipf.commons.ihe.xds.core.requests;

import static org.apache.commons.lang3.Validate.notNull;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.*;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Request object for the Query Registry and Registry Stored Query transactions.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QueryRegistry")
@XmlRootElement(name = "queryRegistry")
@EqualsAndHashCode(callSuper = false, doNotUseGetters = true)
public class QueryRegistry implements Serializable {
    private static final long serialVersionUID = -7089029668323133489L;

    @XmlElementRefs({
            @XmlElementRef(type = FindDocumentsQuery.class),
            @XmlElementRef(type = FindDocumentsForMultiplePatientsQuery.class),
            @XmlElementRef(type = FetchQuery.class),
            @XmlElementRef(type = FindFoldersQuery.class),
            @XmlElementRef(type = FindFoldersForMultiplePatientsQuery.class),
            @XmlElementRef(type = FindSubmissionSetsQuery.class),
            @XmlElementRef(type = GetAllQuery.class),
            @XmlElementRef(type = GetAssociationsQuery.class),
            @XmlElementRef(type = GetDocumentsAndAssociationsQuery.class),
            @XmlElementRef(type = GetDocumentsQuery.class),
            @XmlElementRef(type = GetFolderAndContentsQuery.class),
            @XmlElementRef(type = GetFoldersForDocumentQuery.class),
            @XmlElementRef(type = GetFoldersQuery.class),
            @XmlElementRef(type = GetFromDocumentQuery.class),
            @XmlElementRef(type = GetRelatedDocumentsQuery.class),
            @XmlElementRef(type = GetSubmissionSetAndContentsQuery.class),
            @XmlElementRef(type = GetSubmissionSetsQuery.class)})

    @Getter private Query query;
    @XmlAttribute
    @Getter @Setter private QueryReturnType returnType = QueryReturnType.OBJECT_REF;

    /**
     * For JAXB serialization only.
     */
    public QueryRegistry() {
    }

    /**
     * Constructs the request.
     * @param query
     *          the query to use. Cannot be <code>null</code>.
     */
    public QueryRegistry(Query query) {
        notNull(query, "query cannot be null");
        this.query = query;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
