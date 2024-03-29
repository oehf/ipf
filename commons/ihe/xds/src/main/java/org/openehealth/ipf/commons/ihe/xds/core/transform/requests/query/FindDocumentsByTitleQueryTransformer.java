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
package org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query;

import lombok.Getter;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.FindDocumentsByTitleQuery;

import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_AUTHOR_INSTITUTION;
import static org.openehealth.ipf.commons.ihe.xds.core.transform.requests.QueryParameter.DOC_ENTRY_TITLE;

public class FindDocumentsByTitleQueryTransformer extends AbstractFindDocumentsQueryTransformer<FindDocumentsByTitleQuery> {

    @Getter
    private static final FindDocumentsByTitleQueryTransformer instance = new FindDocumentsByTitleQueryTransformer();

    private FindDocumentsByTitleQueryTransformer() {
    }

    @Override
    protected void toEbXML(FindDocumentsByTitleQuery query, QuerySlotHelper slots) {
        super.toEbXML(query, slots);
        slots.fromStringList(DOC_ENTRY_TITLE, query.getTitle());
        slots.fromStringList(DOC_ENTRY_AUTHOR_INSTITUTION, query.getAuthorInstitution());
    }

    @Override
    protected void fromEbXML(FindDocumentsByTitleQuery query, QuerySlotHelper slots) {
        super.fromEbXML(query, slots);
        query.setTitle(slots.toStringList(DOC_ENTRY_TITLE));
        query.setAuthorInstitution(slots.toStringList(DOC_ENTRY_AUTHOR_INSTITUTION));
    }


}
