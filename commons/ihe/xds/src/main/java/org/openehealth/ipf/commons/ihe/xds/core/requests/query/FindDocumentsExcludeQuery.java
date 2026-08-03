/*
 * Copyright 2026 the original author or authors.
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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;

import java.io.Serial;
import java.util.List;

/**
 * Represents a stored query for the FindDocumentsExclude query.
 * <p>
 * The parameters prefixed with "excluded" carry the values that shall <em>not</em> be present in
 * the returned metadata. Per ITI TF-2: 3.18.4.1.2.3.7.15, each of them is mutually exclusive with
 * its non-excluding counterpart inherited from {@link FindDocumentsQuery}, i.e. a query may specify
 * either the class codes to look for or the class codes to exclude, but not both.
 * <p>
 * This query is only defined for actors claiming the FindDocumentsExclude Option
 * (ITI TF-1: 10.2.12).
 *
 * @author Christian Ohr
 * @since 5.3
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindDocumentsExcludeQuery", propOrder = {
    "excludedTypeCodes", "excludedClassCodes", "excludedPracticeSettingCodes", "excludedHealthcareFacilityTypeCodes",
    "excludedEventCodes", "excludedConfidentialityCodes", "excludedFormatCodes", "excludedAuthorPersons",
    "excludedDocumentEntryTypes", "referenceIds", "excludedReferenceIds"
})
@XmlRootElement(name = "findDocumentsExcludeQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class FindDocumentsExcludeQuery extends FindDocumentsQuery
    implements DocumentEntryTypeAwareStoredQuery, DocumentReferenceIdAwareStoredQuery {

    @Serial
    private static final long serialVersionUID = -3743133045134201787L;

    @XmlElement(name = "excludedTypeCode")
    @Getter @Setter private List<Code> excludedTypeCodes;
    @XmlElement(name = "excludedClassCode")
    @Getter @Setter private List<Code> excludedClassCodes;
    @XmlElement(name = "excludedPracticeSettingCode")
    @Getter @Setter private List<Code> excludedPracticeSettingCodes;
    @XmlElement(name = "excludedHealthcareFacilityTypeCode")
    @Getter @Setter private List<Code> excludedHealthcareFacilityTypeCodes;
    @XmlElement(name = "excludedEventCode")
    @Getter @Setter private QueryList<Code> excludedEventCodes;
    @XmlElement(name = "excludedConfidentialityCode")
    @Getter @Setter private QueryList<Code> excludedConfidentialityCodes;
    @XmlElement(name = "excludedFormatCode")
    @Getter @Setter private List<Code> excludedFormatCodes;
    @XmlElement(name = "excludedAuthorPerson")
    @Getter @Setter private List<String> excludedAuthorPersons;
    @XmlElement(name = "excludedDocumentEntryType")
    @Getter @Setter private List<DocumentEntryType> excludedDocumentEntryTypes;
    @XmlElement(name = "referenceId")
    @Getter @Setter private QueryList<String> referenceIds;
    @XmlElement(name = "excludedReferenceId")
    @Getter @Setter private QueryList<String> excludedReferenceIds;

    /**
     * Constructs the query.
     */
    public FindDocumentsExcludeQuery() {
        super(QueryType.FIND_DOCUMENTS_EXCLUDE);
    }

    /**
     * Constructs the query.
     * @param type
     *          the type of the query.
     */
    protected FindDocumentsExcludeQuery(QueryType type) {
        super(type);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
