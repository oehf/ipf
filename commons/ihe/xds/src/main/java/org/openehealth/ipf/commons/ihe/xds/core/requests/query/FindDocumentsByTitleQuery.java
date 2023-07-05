/*
 * Copyright 2021 the original author or authors.
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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Organization;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.QuerySlotHelper;

import jakarta.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindDocumentsByTitleQuery", propOrder = {"title", "authorInstitution"})
@XmlRootElement(name = "findDocumentsByTitleQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class FindDocumentsByTitleQuery  extends FindDocumentsQuery {

    @XmlElement(name = "title")
    @Getter @Setter
    private List<String> title;

    @XmlElement(name = "authorInstitution")
    @Getter @Setter
    private List<String> authorInstitution;

    public FindDocumentsByTitleQuery() {
        super(QueryType.FIND_DOCUMENTS_BY_TITLE);
    }

    protected FindDocumentsByTitleQuery(QueryType type) {
        super(type);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    /**
     * Allows to use a collection of {@link Organization} instead of a collection of {@link String}
     * for specifying the query parameter "$XDSDocumentEntryAuthorInstitution".
     *
     * @param authorInstitution a collection of {@link Organization} objects.
     */
    public void setTypedAuthorInstitution(List<Organization> authorInstitution) {
        this.authorInstitution = QuerySlotHelper.render(authorInstitution);
    }

    /**
     * Tries to return the query parameter "$XDSDocumentEntryAuthorInstitution"
     * as a collection of {@link Organization} instead of a collection of {@link String}.
     * This may fail if SQL LIKE wildcards ("%", "_", etc.) are used in one or more elements.
     *
     * @return a collection of {@link Organization} objects.
     */
    public List<Organization> getTypedAuthorInstitution() {
        return QuerySlotHelper.parse(this.authorInstitution, Organization.class);
    }
}

