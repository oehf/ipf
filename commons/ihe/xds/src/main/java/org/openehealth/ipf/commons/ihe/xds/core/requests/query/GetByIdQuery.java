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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

/**
 * Base class for queries that are defined by a list of UUIDs or unique IDs. 
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetByIdQuery", propOrder = {"uniqueIds"})
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
public abstract class GetByIdQuery extends GetByUuidQuery implements Serializable {
    private static final long serialVersionUID = -3955280836816390271L;

    @XmlElement(name = "uniqueId")
    @Getter @Setter private List<String> uniqueIds;

    /**
     * For JAXB serialization only.
     */
    public GetByIdQuery() {
    }

    /**
     * Constructs the query.
     * @param type
     *          the type of the query.
     */
    protected GetByIdQuery(QueryType type) {
        super(type);
    }
}