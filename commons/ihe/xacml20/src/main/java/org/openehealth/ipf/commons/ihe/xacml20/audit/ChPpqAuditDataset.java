/*
 * Copyright 2018 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xacml20.audit;

import lombok.Getter;
import lombok.Setter;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.ihe.ws.cxf.audit.WsAuditDataset;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmytro Rud
 * @since 3.5.1
 */
public class ChPpqAuditDataset extends WsAuditDataset {

    @Getter @Setter private EventActionCode action;
    @Getter @Setter private String queryId;
    @Getter @Setter private String patientId;
    @Getter private final List<String> policyAndPolicySetIds = new ArrayList<>();


    public ChPpqAuditDataset(boolean serverSide) {
        super(serverSide);
    }
}
