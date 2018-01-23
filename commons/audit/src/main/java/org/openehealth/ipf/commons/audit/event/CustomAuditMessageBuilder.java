/*
 * Copyright 2018 the original author or authors.
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

package org.openehealth.ipf.commons.audit.event;

import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.types.EventId;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;

/**
 *
 * Audit Message builder used to build custom audit messages
 *
 * @author Christian Ohr
 * @since 3.5
 */
public class CustomAuditMessageBuilder extends BaseAuditMessageBuilder<CustomAuditMessageBuilder> {

    public CustomAuditMessageBuilder(EventOutcomeIndicator outcome,
                                     String eventOutcomeDescription,
                                     EventActionCode eventActionCode,
                                     EventId eventId,
                                     EventType eventType,
                                     PurposeOfUse... purposesOfUse) {
        super();
        setEventIdentification(outcome,
                eventOutcomeDescription,
                eventActionCode,
                eventId,
                eventType,
                purposesOfUse
        );
    }


}
