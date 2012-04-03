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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Represents a stored query for FindDocuments.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindDocumentsQuery", propOrder = {"documentEntryTypes"})
@XmlRootElement(name = "findDocumentsQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
public class FindDocumentsQuery extends AbstractDocumentsQuery implements DocumentEntryTypeAwareStoredQuery {
    private static final long serialVersionUID = -5765363916663583605L;

    @XmlElement(name = "documentEntryType")
    @Getter @Setter private List<DocumentEntryType> documentEntryTypes;

    /**
     * Constructs the query.
     */
    public FindDocumentsQuery() {
        super(QueryType.FIND_DOCUMENTS);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
