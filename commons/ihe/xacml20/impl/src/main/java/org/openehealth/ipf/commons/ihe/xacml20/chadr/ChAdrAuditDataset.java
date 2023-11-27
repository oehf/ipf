/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.xacml20.chadr;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.audit.codes.ParticipantObjectTypeCodeRole;
import org.openehealth.ipf.commons.audit.types.ParticipantObjectIdType;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmytro Rud
 * @since 4.8.0
 */
public class ChAdrAuditDataset extends WsAuditDataset {

    private static final String UNKNOWN = "[unknown]";

    @Getter @Setter private String subjectId = UNKNOWN;
    @Getter @Setter private ParticipantObjectTypeCodeRole objectRole;
    @Getter @Setter private ParticipantObjectIdType subjectRole = ParticipantObjectIdType.of(UNKNOWN, UNKNOWN, UNKNOWN);

    // map from resource ID to decision
    @Getter @Setter private Map<String, String> decisionsByResourceIds = new HashMap<>();

    public ChAdrAuditDataset(boolean serverSide) {
        super(serverSide);
    }
}
