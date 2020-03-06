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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * Base class for Pharmacy Documents Queries (PHARM-1).
 * @author Quentin Ligier
 * @since 3.7
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PharmacyDocumentsQuery", propOrder = {"patientId", "status", "metadataLevel"})
@XmlRootElement(name = "pharmacyDocumentsQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public abstract class PharmacyDocumentsQuery extends StoredQuery implements PatientIdBasedStoredQuery {
    private static final long serialVersionUID = -4878731956719028791L;

    @Getter @Setter private Identifiable patientId;
    @Getter @Setter private List<AvailabilityStatus> status;
    @Getter @Setter private Integer metadataLevel;

    /**
     * For JAXB serialization only.
     */
    public PharmacyDocumentsQuery() {
    }

    /**
     * Constructs the query.
     * @param queryType
     *          the type of the query.
     */
    protected PharmacyDocumentsQuery(final QueryType queryType) {
        super(queryType);
    }
}
