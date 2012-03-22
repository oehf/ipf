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
 * Represents a stored query for GetFolders.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetFoldersQuery")
@XmlRootElement(name = "getFoldersQuery")
public class GetFoldersQuery extends GetByIdQuery {
    private static final long serialVersionUID = 854601731250203237L;

    /**
     * Constructs the query.
     */
    public GetFoldersQuery() {
        super(QueryType.GET_FOLDERS);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
