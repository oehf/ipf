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

import java.io.Serializable;

/**
 * Base class for stored queries.
 * @author Jens Riemschneider
 */
public abstract class StoredQuery extends Query implements Serializable {
    private static final long serialVersionUID = -8296981156625412818L;

    /**
     * Constructs the query.
     * @param type
     *          the type of the query.
     */
    protected StoredQuery(QueryType type) {
        super(type);
    }
}
