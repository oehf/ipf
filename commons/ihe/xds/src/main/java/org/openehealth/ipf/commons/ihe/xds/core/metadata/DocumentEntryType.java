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

/**
 * Type of a document entry.
 * @author Dmytro Rud
 */
public enum DocumentEntryType {
    STABLE("urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1"),
    ON_DEMAND("urn:uuid:34268e47-fdf5-41a6-ba33-82133c465248");

    public static final String[] STABLE_OR_ON_DEMAND = new String[] {
            STABLE.getUuid(),
            ON_DEMAND.getUuid(),
    };

    private final String uuid;

    private DocumentEntryType(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public static String toUuid(DocumentEntryType type) {
        return (type != null) ? type.uuid : null;
    }

    public static DocumentEntryType valueOfUuid(String uuid) {
        for (DocumentEntryType type : DocumentEntryType.values()) {
            if (type.uuid.equals(uuid)) {
                return type;
            }
        }
        return null;
    }
}
