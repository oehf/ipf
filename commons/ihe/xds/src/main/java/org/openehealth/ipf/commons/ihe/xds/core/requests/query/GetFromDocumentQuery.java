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

import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;

import java.io.Serializable;

/**
 * Base class for queries that retrieve results via a document entry.
 * @author Jens Riemschneider
 */
public abstract class GetFromDocumentQuery extends StoredQuery implements Serializable {
    private static final long serialVersionUID = 627720659958894242L;
    
    private String uuid;
    private String uniqueId;

    /**
     * Constructs the query.
     * @param type
     *          the type of the query.
     */
    protected GetFromDocumentQuery(QueryType type) {
        super(type);
    }

    /**
     * @return the uuid to filter {@link DocumentEntry#getEntryUuid()}.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid
     *          the uuid to filter {@link DocumentEntry#getEntryUuid()}.
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * @return the unique ID to filter {@link DocumentEntry#getUniqueId()}.
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * @param uniqueId
     *          the unique ID to filter {@link DocumentEntry#getUniqueId()}.
     */
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((uniqueId == null) ? 0 : uniqueId.hashCode());
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
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
        GetFromDocumentQuery other = (GetFromDocumentQuery) obj;
        if (uniqueId == null) {
            if (other.uniqueId != null)
                return false;
        } else if (!uniqueId.equals(other.uniqueId))
            return false;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }
}