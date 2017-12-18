/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.commons.ihe.hpd.iti59;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;
import org.openhealthtools.ihe.atna.auditor.codes.rfc3881.RFC3881EventCodes;

import java.util.Set;

/**
 * Audit dataset for the ITI-59 transaction.
 * @author Dmytro Rud
 */
public class Iti59AuditDataset extends WsAuditDataset {

    @RequiredArgsConstructor
    public static class RequestItem {
        @Getter private final String requestId;
        @Getter private final RFC3881EventCodes.RFC3881EventActionCodes actionCode;
        @Getter private final Set<String> providerIds;

        // proprietary extensions for Delete and ModifyDN
        @Getter private final String dn;
        @Getter private final String newRdn;

        @Getter @Setter private RFC3881EventCodes.RFC3881EventOutcomeCodes outcomeCode;
    }

    @Getter @Setter private RequestItem[] requestItems;

    public Iti59AuditDataset(boolean serverSide) {
        super(serverSide);
    }

}
