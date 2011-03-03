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
 * Base class for queries that are defined by a list of UUIDs or unique IDs. 
 * @author Jens Riemschneider
 */
public abstract class GetByIdQuery extends GetByUuidQuery implements Serializable {
    private static final long serialVersionUID = -3955280836816390271L;
    
    private List<String> uniqueIds;

    /**
     * Constructs the query.
     * @param type
     *          the type of the query.
     */
    protected GetByIdQuery(QueryType type) {
        super(type);
    }
    
    /**
     * @return the IDs for filtering {@link XDSMetaClass#getUniqueId()}.
     */
    public List<String> getUniqueIds() {
        return uniqueIds;
    }

    /**
     * @param uniqueIds
     *          the IDs for filtering {@link XDSMetaClass#getUniqueId()}.
     */
    public void setUniqueIds(List<String> uniqueIds) {
        this.uniqueIds = uniqueIds;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((uniqueIds == null) ? 0 : uniqueIds.hashCode());
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
        GetByIdQuery other = (GetByIdQuery) obj;
        if (uniqueIds == null) {
            if (other.uniqueIds != null)
                return false;
        } else if (!uniqueIds.equals(other.uniqueIds))
            return false;
        return true;
    }
}