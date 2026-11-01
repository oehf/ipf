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
package org.openehealth.ipf.commons.ihe.hl7v3.core.metadata;

import lombok.Data;
import net.ihe.gazelle.hl7v3.datatypes.II;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplified model of other identifiers of a person ({@code patientPerson/asOtherIDs}), i.e. identifiers assigned
 * by other organizations (e.g. a national patient ID).
 *
 * @author Christian Ohr
 * @since 6.1
 */
@Data
public class OtherIds {

    /**
     * The identifiers ({@code asOtherIDs/id}). The list is never {@code null}.
     */
    private final List<II> ids = new ArrayList<>();

    /**
     * The OID of the organization that assigned the identifiers ({@code asOtherIDs/scopingOrganization/id/@root}).
     */
    private String scopingOrganizationOid;

    /**
     * Creates other IDs consisting of one identifier, scoped by the organization given by the identifier root.
     *
     * @param id the identifier.
     * @return the other IDs.
     */
    public static OtherIds of(final II id) {
        final var otherIds = new OtherIds();
        otherIds.getIds().add(id);
        otherIds.setScopingOrganizationOid(id.getRoot());
        return otherIds;
    }
}
