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
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.*;
import org.openehealth.ipf.commons.ihe.xds.core.transform.requests.query.QuerySlotHelper;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Abstract stored query for PHARM-1 stable documents queries.
 * @author Quentin Ligier
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Pharm1StableDocumentsQuery", propOrder = {
        "authorPersons", "creationTime", "serviceStartTime", "serviceStopTime",
        "confidentialityCodes", "eventCodes", "uuids", "healthcareFacilityTypeCodes",
        "practiceSettingCodes", "uniqueIds"})
@XmlRootElement(name = "abstractPharm1StableDocumentsQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true, doNotUseGetters = true)
public abstract class Pharm1StableDocumentsQuery extends PharmacyDocumentsQuery {
    private static final long serialVersionUID = 7497052735222205532L;

    @XmlElement(name = "uuid")
    @Getter @Setter private List<String> uuids;
    @XmlElement(name = "uniqueId")
    @Getter @Setter private List<String> uniqueIds;
    @XmlElement(name = "practiceSettingCode")
    @Getter @Setter private List<Code> practiceSettingCodes;
    @Getter private final TimeRange creationTime = new TimeRange();
    @Getter private final TimeRange serviceStartTime = new TimeRange();
    @Getter private final TimeRange serviceStopTime = new TimeRange();
    @XmlElement(name = "healthcareFacilityTypeCode")
    @Getter @Setter private List<Code> healthcareFacilityTypeCodes;
    @XmlElement(name = "eventCode")
    @Getter @Setter private QueryList<Code> eventCodes;
    @XmlElement(name = "confidentialityCode")
    @Getter @Setter private QueryList<Code> confidentialityCodes;
    @XmlElement(name = "authorPerson")
    @Getter @Setter private List<String> authorPersons;

    /**
     * For JAXB serialization only.
     */
    public Pharm1StableDocumentsQuery() {
    }

    /**
     * Constructs the query.
     * @param queryType
     *          the type of the query.
     */
    protected Pharm1StableDocumentsQuery(final QueryType queryType) {
        super(queryType);
    }

    /**
     * Allows to use a collection of {@link Person} instead of a collection of {@link String}
     * for specifying the query parameter "$XDSDocumentEntryAuthorPerson".
     *
     * @param authorPersons a collection of {@link Person} objects.
     */
    public void setTypedAuthorPersons(List<Person> authorPersons) {
        this.authorPersons = QuerySlotHelper.render(authorPersons);
    }

    /**
     * Tries to return the query parameter "$XDSDocumentEntryAuthorPerson"
     * as a collection of {@link Person} instead of a collection of {@link String}.
     * This may fail if SQL LIKE wildcards ("%", "_", etc.) are used in one or more elements.
     *
     * @return a collection of {@link Person} objects.
     */
    public List<Person> getTypedAuthorPersons() {
        return QuerySlotHelper.parse(this.authorPersons, Person.class);
    }

    // TODO:QLIG
    /**
     * FMTP  FindMedicationTreatmentPlans
     * FP    FindPrescriptions
     * FD    FindDispenses
     * FMA   FindMedicationAdministrations
     * FPV   FindPrescriptionsForValidation
     * FPD   FindPrescriptionsForDispense
     * FML   FindMedicationList
     */
    // PatientId                  x FMTP FP FD FMA FPV FPD FML
    // EntryUUID                  x FMTP FP FD FMA FPV FPD
    // UniqueId                   x FMTP FP FD FMA FPV FPD
    // PracticeSettingCode        x FMTP FP FD FMA FPV FPD
    // CreationTime               x FMTP FP FD FMA FPV FPD
    // ServiceStartTime           x FMTP FP FD FMA FPV FPD FML
    // ServiceStopTime            x FMTP FP FD FMA FPV FPD FML
    // HealthcareFacilityTypeCode x FMTP FP FD FMA FPV FPD
    // EventCodeList              x FMTP FP FD FMA FPV FPD
    // ConfidentialityCode        x FMTP FP FD FMA FPV FPD
    // AuthorPerson               x FMTP FP FD FMA FPV FPD
    // Status                     x FMTP FP FD FMA FPV FPD FML
    // MetadataLevel              x FMTP FP FD FMA FPV FPD FML
    // FormatCode                   FML
    // Type                         FML

}
