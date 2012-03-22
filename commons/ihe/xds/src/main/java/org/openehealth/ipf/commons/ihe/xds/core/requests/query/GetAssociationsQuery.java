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

import javax.xml.bind.annotation.*;

/**
 * Represents a stored query for GetAssociations.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetAssociationsQuery")
@XmlRootElement(name = "getAssociationsQuery")
public class GetAssociationsQuery extends GetByUuidQuery {
    private static final long serialVersionUID = 5623733746377113397L;

    /**
     * Constructs the query.
     */
    public GetAssociationsQuery() {
        super(QueryType.GET_ASSOCIATIONS);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
