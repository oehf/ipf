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
import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.TimeRange;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Abstract stored query for documents.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractDocumentsQuery", propOrder = {
        "status", "authorPersons", "creationTime", "serviceStartTime", "serviceStopTime",
        "classCodes", "confidentialityCodes", "eventCodes", "formatCodes",
        "healthcareFacilityTypeCodes", "practiceSettingCodes", "typeCodes", "patientId"})
@XmlRootElement(name = "abstractDocumentsQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
abstract public class AbstractDocumentsQuery extends StoredQuery implements PatientIdBasedStoredQuery {
    private static final long serialVersionUID = 1162423827844317922L;

    @Getter @Setter private List<AvailabilityStatus> status;
    @XmlElement(name = "typeCode")
    @Getter @Setter private List<Code> typeCodes;
    @XmlElement(name = "classCode")
    @Getter @Setter private List<Code> classCodes;
    @XmlElement(name = "practiceSettingCode")
    @Getter @Setter private List<Code> practiceSettingCodes;
    @XmlElement(name = "healthcareFacilityTypeCode")
    @Getter @Setter private List<Code> healthcareFacilityTypeCodes;
    @XmlElement(name = "eventCode")
    @Getter @Setter private QueryList<Code> eventCodes;
    @XmlElement(name = "confidentialityCode")
    @Getter @Setter private QueryList<Code> confidentialityCodes;
    @XmlElement(name = "formatCode")
    @Getter @Setter private List<Code> formatCodes;
    @XmlElement(name = "authorPerson")
    @Getter @Setter private List<String> authorPersons;
    @Getter @Setter private Identifiable patientId;

    @Getter private final TimeRange creationTime = new TimeRange();
    @Getter private final TimeRange serviceStartTime = new TimeRange();
    @Getter private final TimeRange serviceStopTime = new TimeRange();

    /**
     * For JAXB only.
     */
    protected AbstractDocumentsQuery() {
    }


    protected AbstractDocumentsQuery(QueryType type) {
        super(type);
    }
}
