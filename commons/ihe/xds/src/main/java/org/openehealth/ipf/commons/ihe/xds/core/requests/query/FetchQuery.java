/*
 * Copyright 2012 the original author or authors.
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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents a stored query Cross-Community Fetch.
 * @author Dmytro Rud
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FetchQuery")
@XmlRootElement(name = "fetchQuery")
public class FetchQuery extends AbstractDocumentsQuery {
    private static final long serialVersionUID = 7171404956090539393L;

    /**
     * Constructs the query.
     */
    public FetchQuery() {
        super(QueryType.FETCH);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
