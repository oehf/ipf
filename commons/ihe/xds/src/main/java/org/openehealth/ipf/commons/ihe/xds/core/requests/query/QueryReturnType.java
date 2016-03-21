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

import lombok.EqualsAndHashCode;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XdsEnum;

import javax.xml.bind.annotation.XmlType;

/**
 * Return types for XDS queries (ITI-18, ITI-38, ITI-51, ITI-63).
 * @author Dmytro Rud
 */
@XmlType(name = "QueryReturnType")
@EqualsAndHashCode(callSuper = true)
public class QueryReturnType extends XdsEnum {
    private static final long serialVersionUID = 2726087546056654799L;

    // for ITI-18, ITI-38 and ITI-51
    public static final QueryReturnType LEAF_CLASS = new QueryReturnType(Type.OFFICIAL, "LeafClass");
    public static final QueryReturnType OBJECT_REF = new QueryReturnType(Type.OFFICIAL, "ObjectRef");

    // for ITI-63
    public static final QueryReturnType LEAF_CLASS_WITH_REPOSITORY_ITEM = new QueryReturnType(Type.OFFICIAL, "LeafClassWithRepositoryItem");

    public static final QueryReturnType[] OFFICIAL_VALUES =
            {LEAF_CLASS, OBJECT_REF, LEAF_CLASS_WITH_REPOSITORY_ITEM};

    public QueryReturnType(Type type, String ebXML) {
        super(type, ebXML);
    }

    @Override
    public String getJaxbValue() {
        return getEbXML30();
    }
}
