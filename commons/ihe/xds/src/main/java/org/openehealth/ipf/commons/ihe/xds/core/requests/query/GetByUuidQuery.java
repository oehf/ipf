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

import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass;

import java.io.Serializable;
import java.util.List;

/**
 * Base class for queries that are defined by a list of UUIDs. 
 * @author Jens Riemschneider
 */
public abstract class GetByUuidQuery extends StoredQuery implements Serializable {
    private static final long serialVersionUID = -7962722576557371093L;
    
    private List<String> uuids;

    /**
     * Constructs the query.
     * @param type
     *          the type of the query.
     */
    protected GetByUuidQuery(QueryType type) {
        super(type);
    }

    /**
     * @return the UUIDs used for filtering of {@link XDSMetaClass#getEntryUuid()}.
     */
    public List<String> getUuids() {
        return uuids;
    }

    /**
     * @param uuids
     *          the UUIDs used for filtering of {@link XDSMetaClass#getEntryUuid()}.
     */
    public void setUuids(List<String> uuids) {
        this.uuids = uuids;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((uuids == null) ? 0 : uuids.hashCode());
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
        GetByUuidQuery other = (GetByUuidQuery) obj;
        if (uuids == null) {
            if (other.uuids != null)
                return false;
        } else if (!uuids.equals(other.uuids))
            return false;
        return true;
    }
}