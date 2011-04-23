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

import static org.apache.commons.lang.Validate.notNull;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
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
public class QueryRegistry implements Serializable {
    private static final long serialVersionUID = -7089029668323133489L;

    @XmlElementRefs({
            @XmlElementRef(type = FindDocumentsQuery.class),
            @XmlElementRef(type = FindFoldersQuery.class),
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
    private Query query;
    @XmlAttribute
    private boolean returnLeafObjects;

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

    /**
     * @return the query to use (never <code>null</code>).
     */
    public Query getQuery() {
        return query;
    }

    /**
     * @return <code>true</code> if the objects data for any found object should be 
     *          returned in the response. <code>false</code> if only an object reference
     *          should be returned.  
     */
    public boolean isReturnLeafObjects() {
        return returnLeafObjects;
    }

    /**
     * @param returnLeafObjects
     *          <code>true</code> if the objects data for any found object should be 
     *          returned in the response. <code>false</code> if only an object reference
     *          should be returned.
     */
    public void setReturnLeafObjects(boolean returnLeafObjects) {
        this.returnLeafObjects = returnLeafObjects;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((query == null) ? 0 : query.hashCode());
        result = prime * result + (returnLeafObjects ? 1231 : 1237);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        QueryRegistry other = (QueryRegistry) obj;
        if (query == null) { 
            if (other.query != null)
                return false;
        } else if (!query.equals(other.query))
            return false;
        return returnLeafObjects == other.returnLeafObjects;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
