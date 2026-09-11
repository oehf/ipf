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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlType;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

/**
 * Base class for stored queries.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StoredQuery", propOrder = {"homeCommunityId", "extraParameters", "sortOrder"})
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public abstract class StoredQuery extends Query {
    @Serial
    private static final long serialVersionUID = -8296981156625412818L;

    @Getter @Setter private String homeCommunityId;
    @Getter private final Map<String, QueryList<String>> extraParameters = new HashMap<>();

    /**
     * The order the result set is requested in, or null to leave it to the registry.
     * <p>
     * ITI-18 defines no ordering, so this is carried as an extension slot and a registry that does not
     * implement the extension will ignore it. Sits here rather than on the individual query types
     * because it applies to all of them, the way {@link #homeCommunityId} does.
     * <p>
     * Typed rather than left to {@link #getExtraParameters()}, which could carry the very same slot: this
     * way the encoding, the slot name and the tiebreaker rule exist once instead of in every consumer and
     * registry that speaks the extension, and the responding side gets the parsed form it needs to build
     * a comparator from. Because of that, the slot is read back into this field rather than into the extra
     * parameters -- setting it through those works on the wire, but it returns here.
     */
    @Getter @Setter private SortOrder sortOrder;

    /**
     * For JAXB serialization only.
     */
    public StoredQuery() {
    }

    /**
     * Constructs the query.
     * @param type
     *          the type of the query.
     */
    protected StoredQuery(QueryType type) {
        super(type);
    }

}
