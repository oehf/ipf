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
package org.openehealth.ipf.platform.camel.ihe.xds.commons.requests.query;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.XDSMetaClass;

/**
 * Base class for queries that are defined by a list of UUIDs. 
 * @author Jens Riemschneider
 */
public abstract class GetByUuidQuery extends StoredQuery {
    private List<String> uuids;
    private String homeCommunityId;

    /**
     * Constructs the query.
     * @param type
     *          the type of the query.
     */
    protected GetByUuidQuery(QueryType type) {
        super(type);
    }

    /**
     * @return the home community ID.
     */
    public String getHomeCommunityId() {
        return homeCommunityId;
    }

    /**
     * @param homeCommunityId   
     *          the home community ID.
     */
    public void setHomeCommunityId(String homeCommunityId) {
        this.homeCommunityId = homeCommunityId;
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
        int result = 1;
        result = prime * result + ((homeCommunityId == null) ? 0 : homeCommunityId.hashCode());
        result = prime * result + ((uuids == null) ? 0 : uuids.hashCode());
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
        GetByUuidQuery other = (GetByUuidQuery) obj;
        if (homeCommunityId == null) {
            if (other.homeCommunityId != null)
                return false;
        } else if (!homeCommunityId.equals(other.homeCommunityId))
            return false;
        if (uuids == null) {
            if (other.uuids != null)
                return false;
        } else if (!uuids.equals(other.uuids))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}