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
package org.openehealth.ipf.commons.audit.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.openehealth.ipf.commons.audit.codes.EventActionCode;
import org.openehealth.ipf.commons.audit.codes.EventOutcomeIndicator;
import org.openehealth.ipf.commons.audit.types.EventId;
import org.openehealth.ipf.commons.audit.types.EventType;
import org.openehealth.ipf.commons.audit.types.PurposeOfUse;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode
public class EventIdentificationType implements Serializable, Validateable {

    @Getter
    private final EventId eventID;

    /**
     * The EventDateTime is the date and time that the event being reported took place. Some events have a significant duration.
     * In these cases, a date and time shall be chosen by a method that is consistent and appropriate for the event being reported.
     * The EventDateTime shall include the time zone information.
     * Creators of audit messages may support leap-seconds, but are not required to. Recipients of audit messages shall be able
     * to process messages with leap-second information.
     */
    @Getter
    private final Instant eventDateTime;

    @Getter
    private final EventOutcomeIndicator eventOutcomeIndicator;

    @Getter
    private EventActionCode eventActionCode;

    private List<PurposeOfUse> purposesOfUse;
    private List<EventType> eventTypeCodes;

    public EventIdentificationType(EventId eventID,
                                   Instant eventDateTime,
                                   EventOutcomeIndicator eventOutcomeIndicator) {
        this.eventID = requireNonNull(eventID, "eventID must be not null");
        this.eventDateTime = requireNonNull(eventDateTime, "eventDateTime must be not null");
        this.eventOutcomeIndicator = requireNonNull(eventOutcomeIndicator, "eventOutcomeIndicator must be not null");
    }

    /**
     * @return Identifier for the category of event
     */
    public List<EventType> getEventTypeCode() {
        if (eventTypeCodes == null) {
            eventTypeCodes = new ArrayList<>();
        }
        return this.eventTypeCodes;
    }

    public void setEventActionCode(EventActionCode eventActionCode) {
        this.eventActionCode = eventActionCode;
    }


    /**
     * <p>
     * The Purpose of Use value indicates the expected ultimate use of the data, rather than a likely near term use
     * such as "send to X". As explained in the IHE Access Control White Paper, there are Access Control decisions
     * that are based on the ultimate use of the data. For example a Patient may have provided a BPPC Consent/Authorization
     * for treatment purposes, but explicitly disallowed any use for research regardless of de-identification methods used.
     * </p>
     * <p>
     * The Purpose Of Use is also included in the Audit Event message to enable some forms of
     * reporting of Accounting of Disclosures and Breach Notification. One specific PurposeOfUse would be a
     * BreakGlass/Emergency-Mode-Access.
     * </p>
     * <p>
     * The PurposeOfUse value will come from a Value Set. This Value Set should be derived from the
     * codes found in ISO 14265, or XSPA (Cross-Enterprise Security and Privacy Authorization).
     * Implementations should expect that the Value Set used may be using locally defined values
     * </p>
     *
     * @return Purposes of Use for this event
     */
    public List<PurposeOfUse> getPurposesOfUse() {
        if (purposesOfUse == null) {
            purposesOfUse = new ArrayList<>();
        }
        return this.purposesOfUse;
    }

    @Override
    public void validate() {
        // no special rules
    }
}
