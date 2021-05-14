package org.openehealth.ipf.commons.ihe.svs.core.audit;

import lombok.Getter;
import org.openehealth.ipf.commons.audit.types.EnumeratedCodedValue;
import org.openehealth.ipf.commons.audit.types.EventType;

/**
 * EventTypes for the SVS module
 *
 * @author Quentin Ligier
 */
public enum SvsEventTypeCode implements EventType, EnumeratedCodedValue<EventType> {

    RetrieveValueSet("ITI-48", "Retrieve Value Set");

    @Getter
    private final EventType value;

    SvsEventTypeCode(String code, String displayName) {
        this.value = EventType.of(code, "IHE Transactions", displayName);
    }
}
