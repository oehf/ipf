/*
 * Copyright 2012 the original author or authors.
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
 * Type of a document entry.
 * @author Dmytro Rud
 */
@EqualsAndHashCode(callSuper = true)
public class DocumentEntryType extends XdsEnum {
    private static final long serialVersionUID = -5941669064647018977L;

    public static final DocumentEntryType STABLE = new DocumentEntryType(Type.OFFICIAL, "urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1");
    public static final DocumentEntryType ON_DEMAND = new DocumentEntryType(Type.OFFICIAL, "urn:uuid:34268e47-fdf5-41a6-ba33-82133c465248");

    public static final DocumentEntryType[] OFFICIAL_VALUES = {STABLE, ON_DEMAND};

    public static final String[] STABLE_OR_ON_DEMAND = {STABLE.getEbXML30(), ON_DEMAND.getEbXML30()};

    public DocumentEntryType(Type type, String ebXML) {
        super(type, ebXML);
    }

    @Override
    public String getJaxbValue() {
        if (this == STABLE) {
            return "stable";
        }
        if (this == ON_DEMAND) {
            return "on-demand";
        }
        return getEbXML30();
    }
}
