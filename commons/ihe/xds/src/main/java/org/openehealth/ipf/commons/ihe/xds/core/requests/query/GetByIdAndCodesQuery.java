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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Base class for queries that are defined by:
 * <li> a UUID or unique ID 
 * <li> a list of format codes
 * <li> a list of confidentiality codes
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetByIdAndCodesQuery", propOrder = {
        "confidentialityCodes", "formatCodes", "documentEntryTypes"})
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
public abstract class GetByIdAndCodesQuery extends GetFromDocumentQuery
        implements DocumentEntryTypeAwareStoredQuery
{
    private static final long serialVersionUID = -8311996966550912396L;

    @XmlElement(name = "confidentialityCode")
    @Getter @Setter private QueryList<Code> confidentialityCodes;
    @XmlElement(name = "formatCode")
    @Getter @Setter private List<Code> formatCodes;
    @XmlElement(name = "documentEntryType")
    @Getter @Setter private List<DocumentEntryType> documentEntryTypes;


    /**
     * For JAXB serialization only.
     */
    public GetByIdAndCodesQuery() {
    }

    /**
     * Constructs the query.
     * @param type
     *          the type of query.
     */
    protected GetByIdAndCodesQuery(QueryType type) {
        super(type);
    }
}
