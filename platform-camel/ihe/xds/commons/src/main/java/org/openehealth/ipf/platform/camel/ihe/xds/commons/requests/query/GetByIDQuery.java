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

/**
 * Base class for queries that are defined by a list of UUIDs or unique IDs. 
 * @author Jens Riemschneider
 */
public abstract class GetByIDQuery extends GetByUUIDQuery {
    private final List<String> uniqueIDs = new ArrayList<String>();

    protected GetByIDQuery(QueryType type) {
        super(type);
    }
    
    public List<String> getUniqueIDs() {
        return uniqueIDs;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((uniqueIDs == null) ? 0 : uniqueIDs.hashCode());
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
        GetByIDQuery other = (GetByIDQuery) obj;
        if (uniqueIDs == null) {
            if (other.uniqueIDs != null)
                return false;
        } else if (!uniqueIDs.equals(other.uniqueIDs))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}