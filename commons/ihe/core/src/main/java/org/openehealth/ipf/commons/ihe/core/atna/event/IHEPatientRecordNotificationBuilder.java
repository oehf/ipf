/*
 * Copyright 2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.commons.ihe.core.atna.event;

import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;
import org.openehealth.ipf.commons.ihe.core.atna.AuditDataset;

import java.util.Collections;
import java.util.List;

/**
 * @author Christian Ohr
 */
public class IHEPatientRecordNotificationBuilder extends IHEPatientRecordBuilder<IHEPatientRecordNotificationBuilder> {


    public IHEPatientRecordNotificationBuilder(AuditDataset auditDataset, EventActionCode action, EventType eventType) {
        this(auditDataset, action, eventType, null, Collections.emptyList(), Collections.emptyList());
    }
    public IHEPatientRecordNotificationBuilder(AuditDataset auditDataset, EventActionCode action, EventType eventType,
                                               String userName, List<PurposeOfUse> purposesOfUse, List<String> userRoles) {
        super(auditDataset, action, eventType, purposesOfUse);
        // TODO userName?
    }

    @Override
    public void validate() {
        // TODO
        delegate.validate();
    }
}
