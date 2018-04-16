package org.openehealth.ipf.commons.ihe.xacml20.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Swiss EPR access level.
 *
 * @author Dmytro Rud
 */
@AllArgsConstructor
public enum AccessLevel {
    NORMAL    ("urn:e-health-suisse:2015:policies:access-level:normal"),
    RESTRICTED("urn:e-health-suisse:2015:policies:access-level:restricted");

    @Getter private final String urn;
}
