/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.tutorials.xds.store;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass;

/**
 * Access to the attributes that all metadata entries have. Document entries, folders and submission sets
 * are {@link XDSMetaClass}es, but {@link Association}s are not, so there is no common type to call them
 * on.
 *
 * @author Christian Ohr
 * @since 6.2
 */
public final class MetadataEntries {

    private MetadataEntries() {
    }

    public static String entryUuidOf(Object entry) {
        if (entry instanceof XDSMetaClass meta) {
            return meta.getEntryUuid();
        }
        if (entry instanceof Association association) {
            return association.getEntryUuid();
        }
        throw notAnEntry(entry);
    }

    public static void setEntryUuid(Object entry, String entryUuid) {
        if (entry instanceof XDSMetaClass meta) {
            meta.setEntryUuid(entryUuid);
        } else if (entry instanceof Association association) {
            association.setEntryUuid(entryUuid);
        } else {
            throw notAnEntry(entry);
        }
    }

    public static AvailabilityStatus statusOf(Object entry) {
        if (entry instanceof XDSMetaClass meta) {
            return meta.getAvailabilityStatus();
        }
        if (entry instanceof Association association) {
            return association.getAvailabilityStatus();
        }
        throw notAnEntry(entry);
    }

    public static void setStatus(Object entry, AvailabilityStatus status) {
        if (entry instanceof XDSMetaClass meta) {
            meta.setAvailabilityStatus(status);
        } else if (entry instanceof Association association) {
            association.setAvailabilityStatus(status);
        } else {
            throw notAnEntry(entry);
        }
    }

    private static IllegalArgumentException notAnEntry(Object entry) {
        return new IllegalArgumentException("Not a metadata entry: " + entry);
    }
}
