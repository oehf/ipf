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

import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.Code;
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.EntryID;

/**
 * Represents a stored query for GetFolderAndContents.
 * @author Jens Riemschneider
 */
public class GetFolderAndContentsQuery<T extends EntryID> extends GetByIDQuery<T> {
    private final QueryList<Code> confidentialityCodes = new QueryList<Code>();
    private final QueryList<Code> formatCodes = new QueryList<Code>();

    public GetFolderAndContentsQuery(T id) {
        super(new QueryList<T>(id));
    }

    public QueryList<Code> getConfidentialityCodes() {
        return confidentialityCodes;
    }

    public QueryList<Code> getFormatCodes() {
        return formatCodes;
    }
}
