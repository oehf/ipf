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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents a stored query for GetFolderAndContents.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetFolderAndContentsQuery")
@XmlRootElement(name = "getFolderAndContentsQuery")
public class GetFolderAndContentsQuery extends GetByIdAndCodesQuery {
    private static final long serialVersionUID = -5400326849236563094L;

    /**
     * Constructs the query.
     */
    public GetFolderAndContentsQuery() {
        super(QueryType.GET_FOLDER_AND_CONTENTS);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
