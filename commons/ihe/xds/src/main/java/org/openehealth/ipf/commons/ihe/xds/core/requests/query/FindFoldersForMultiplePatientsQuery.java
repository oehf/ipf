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
 * Represents a Multi-Patient stored query for FindFolders.
 * @author Jens Riemschneider
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FindFoldersForMultiplePatientsQuery",
        propOrder = {"status", "lastUpdateTime", "codes", "patientIds"})
@XmlRootElement(name = "findFoldersForMultiplePatientsQuery")
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
public class FindFoldersForMultiplePatientsQuery extends StoredQuery  {
    private static final long serialVersionUID = 4156643982985304259L;

    @Getter @Setter private List<AvailabilityStatus> status;
    @XmlElement(name = "code")
    @Getter @Setter private QueryList<Code> codes;
    @Getter @Setter private List<Identifiable> patientIds;

    @Getter private final TimeRange lastUpdateTime = new TimeRange();

    /**
     * Constructs the query.
     */
    public FindFoldersForMultiplePatientsQuery() {
        super(QueryType.FIND_FOLDERS_MPQ);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
