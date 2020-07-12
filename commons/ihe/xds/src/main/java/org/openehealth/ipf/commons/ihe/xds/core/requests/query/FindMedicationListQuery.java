/*
 * Copyright 2020 the original author or authors.
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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.TimeRange;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Represents a stored query for FindMedicationListQuery (PHARM-1).
 * @author Quentin Ligier
 * @since 3.7
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindMedicationListQuery", propOrder = {
        "formatCodes", "documentEntryTypes", "serviceStart", "serviceEnd"})
@XmlRootElement(name = "findMedicationListQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public class FindMedicationListQuery extends PharmacyDocumentsQuery {
    private static final long serialVersionUID = 7810851265303915098L;

    @XmlElement(name = "formatCode")
    @Getter @Setter private List<Code> formatCodes;
    @XmlElement(name = "documentEntryType")
    @Getter @Setter private List<DocumentEntryType> documentEntryTypes;
    @Getter private final TimeRange serviceStart = new TimeRange();
    @Getter private final TimeRange serviceEnd = new TimeRange();

    /**
     * Constructs the query.
     */
    public FindMedicationListQuery() {
        super(QueryType.FIND_MEDICATION_LIST);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
