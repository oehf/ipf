/*
 * Copyright 2009 the original author or authors.
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
package org.openehealth.ipf.commons.ihe.xds.core.metadata;

import lombok.EqualsAndHashCode;

/**
 * Describes the availability of an entry.
 * 
 * @author Jens Riemschneider
 */
@EqualsAndHashCode(callSuper = true)
public class AvailabilityStatus extends XdsVersionedEnum {
    private static final long serialVersionUID = -4550809703490656065L;

    /** The entry is approved. */
    public static final AvailabilityStatus APPROVED = new AvailabilityStatus(Type.OFFICIAL, "Approved", "urn:oasis:names:tc:ebxml-regrep:StatusType:Approved");
    /** The entry is deprecated. */
    public static final AvailabilityStatus DEPRECATED = new AvailabilityStatus(Type.OFFICIAL, "Deprecated", "urn:oasis:names:tc:ebxml-regrep:StatusType:Deprecated");
    /** The entry is submitted. */
    public static final AvailabilityStatus SUBMITTED = new AvailabilityStatus(Type.OFFICIAL, "Submitted", "urn:oasis:names:tc:ebxml-regrep:StatusType:Submitted");

    public static final AvailabilityStatus[] OFFICIAL_VALUES = {APPROVED, DEPRECATED, SUBMITTED};


    public AvailabilityStatus(Type type, String ebXML21, String ebXML30) {
        super(type, ebXML21, ebXML30);
    }

    /** @return ebXML representation of the availability code suitable for Queries */
    public String getQueryEbXML() {
        return getEbXML30();
    }

    @Override
    public String getJaxbValue() {
        return getEbXML21();
    }
}
