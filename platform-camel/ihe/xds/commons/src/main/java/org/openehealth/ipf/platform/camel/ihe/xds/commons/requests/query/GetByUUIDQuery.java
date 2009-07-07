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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.XDSMetaClass;

/**
 * Base class for queries that are defined by a list of UUIDs. 
 * <p>
 * All non-list members of this class are allowed to be <code>null</code>.
 * The lists are pre-created and can therefore never be <code>null</code>.
 * @author Jens Riemschneider
 */
public abstract class GetByUUIDQuery extends StoredQuery {
    private final List<String> uuids = new ArrayList<String>();
    private String homeCommunityID;

    /**
     * Constructs the query.
     * @param type
     *          the type of the query.
     */
    protected GetByUUIDQuery(QueryType type) {
        super(type);
    }

    /**
     * @return the home community ID.
     */
    public String getHomeCommunityID() {
        return homeCommunityID;
    }

    /**
     * @param homeCommunityID   
     *          the home community ID.
     */
    public void setHomeCommunityID(String homeCommunityID) {
        this.homeCommunityID = homeCommunityID;
    }

    /**
     * @return the UUIDs used for filtering of {@link XDSMetaClass#getUniqueID()}.
     */
    public List<String> getUUIDs() {
        return uuids;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((homeCommunityID == null) ? 0 : homeCommunityID.hashCode());
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
        GetByUUIDQuery other = (GetByUUIDQuery) obj;
        if (homeCommunityID == null) {
            if (other.homeCommunityID != null)
                return false;
        } else if (!homeCommunityID.equals(other.homeCommunityID))
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