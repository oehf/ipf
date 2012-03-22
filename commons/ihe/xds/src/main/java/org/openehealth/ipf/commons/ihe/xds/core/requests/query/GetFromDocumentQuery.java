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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Base class for queries that retrieve results via a document entry.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetFromDocumentQuery", propOrder = {"uuid", "uniqueId" })
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
public abstract class GetFromDocumentQuery extends StoredQuery {
    private static final long serialVersionUID = 627720659958894242L;
    
    @Getter @Setter private String uuid;
    @Getter @Setter private String uniqueId;

    /**
     * For JAXB serialization only.
     */
    public GetFromDocumentQuery() {
    }

    /**
     * Constructs the query.
     * @param type
     *          the type of the query.
     */
    protected GetFromDocumentQuery(QueryType type) {
        super(type);
    }
}