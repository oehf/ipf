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

import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a stored query for GetRelatedDocuments.
 * @author Jens Riemschneider
 */
public class GetRelatedDocumentsQuery extends GetFromDocumentQuery implements Serializable {
    private static final long serialVersionUID = -8768793068458839362L;
    
    private List<AssociationType> associationTypes;

    /**
     * Constructs the query.
     */
    public GetRelatedDocumentsQuery() {
        super(QueryType.GET_RELATED_DOCUMENTS);
    }

    /**
     * @return the types used for filtering {@link Association#getAssociationType()}.
     */
    public List<AssociationType> getAssociationTypes() {
        return associationTypes;
    }

    /**
     * @param associationTypes
     *          the types used for filtering {@link Association#getAssociationType()}.
     */
    public void setAssociationTypes(List<AssociationType> associationTypes) {
        this.associationTypes = associationTypes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((associationTypes == null) ? 0 : associationTypes.hashCode());
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
        GetRelatedDocumentsQuery other = (GetRelatedDocumentsQuery) obj;
        if (associationTypes == null) {
            if (other.associationTypes != null)
                return false;
        } else if (!associationTypes.equals(other.associationTypes))
            return false;
        return true;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
