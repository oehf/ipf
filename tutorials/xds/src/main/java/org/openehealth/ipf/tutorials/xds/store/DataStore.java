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
package org.openehealth.ipf.tutorials.xds.store;

import jakarta.activation.DataHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A simple in-memory store for metadata and documents.
 * <p>
 * Metadata entries are {@link XDSMetaClass} instances (document entries, folders and submission sets)
 * and {@link Association}s, which share no common type. Each entry is indexed by the attributes it has
 * of those named by {@link Index}, so that searches need not scan the whole store.
 *
 * @author Jens Riemschneider
 */
@Slf4j
public class DataStore {

    /**
     * The attributes entries are indexed by. An entry without the attribute, or with no value for it, is
     * not in that index.
     */
    public enum Index {
        ENTRY_UUID(entry -> entry instanceof XDSMetaClass meta ? meta.getEntryUuid()
                : entry instanceof Association association ? association.getEntryUuid() : null),
        UNIQUE_ID(entry -> entry instanceof XDSMetaClass meta ? meta.getUniqueId() : null),
        PATIENT_ID(entry -> entry instanceof XDSMetaClass meta ? meta.getPatientId() : null),
        SOURCE_UUID(entry -> entry instanceof Association association ? association.getSourceUuid() : null),
        TARGET_UUID(entry -> entry instanceof Association association ? association.getTargetUuid() : null);

        private final Function<Object, Object> keyOf;

        Index(Function<Object, Object> keyOf) {
            this.keyOf = keyOf;
        }
    }

    private final List<Object> entries = new CopyOnWriteArrayList<>();
    private final Map<String, byte[]> documents = new ConcurrentHashMap<>();
    private final Map<Index, Map<Object, List<Object>>> indexes = new EnumMap<>(Index.class);

    /**
     * @return the store registered as bean {@code dataStore} in the Camel registry
     * @throws IllegalStateException if there is none
     */
    public static DataStore lookup(Exchange exchange) {
        var store = exchange.getContext().getRegistry().lookupByNameAndType("dataStore", DataStore.class);
        if (store == null) {
            throw new IllegalStateException("No DataStore bean named 'dataStore' in the registry");
        }
        return store;
    }

    public DataStore() {
        for (var index : Index.values()) {
            indexes.put(index, new ConcurrentHashMap<>());
        }
    }

    /**
     * Stores the content of a document for later retrieval by its unique ID.
     */
    public void store(Document document) {
        var uniqueId = document.getDocumentEntry().getUniqueId();
        documents.put(uniqueId, ContentUtils.getContent(document.getContent(DataHandler.class)));
        log.info("Stored document: {}", uniqueId);
    }

    /**
     * Stores a metadata entry. A {@link Document} passed here is stored as {@link #store(Document) document}.
     *
     * @param entry a document entry, folder, submission set or association
     */
    public void store(Object entry) {
        if (entry instanceof Document document) {
            store(document);
            return;
        }
        if (!(entry instanceof XDSMetaClass || entry instanceof Association)) {
            throw new IllegalArgumentException("Not a metadata entry: " + entry);
        }
        entries.add(entry);
        indexes.forEach((index, map) -> {
            var key = index.keyOf.apply(entry);
            if (key != null) {
                map.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>()).add(entry);
            }
        });
        log.info("Stored: {}", entry);
    }

    /**
     * @param uniqueId the unique ID of the document
     * @return a {@link DataHandler} to access the document
     * @throws IllegalArgumentException if no document with that unique ID is stored
     */
    public DataHandler get(String uniqueId) {
        var contents = documents.get(uniqueId);
        if (contents == null) {
            throw new IllegalArgumentException("Unique ID not found in store: " + uniqueId);
        }
        return new DataHandler(new ByteArrayDataSource(contents, "text/plain"));
    }

    /**
     * @return all metadata entries, in the order they were stored
     */
    public List<Object> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * Searches the stored metadata entries.
     *
     * @param keys   for each index to look up, the keys an entry may have. An entry must be found in each of
     *               these indexes, under any of the keys given for it. An empty key collection therefore
     *               matches nothing.
     * @param filter the condition the entries found must satisfy
     * @return the matching entries, in the order they were stored
     */
    public List<Object> search(Map<Index, ? extends Collection<?>> keys, Predicate<Object> filter) {
        Set<Object> indexed = null;
        for (var lookup : keys.entrySet()) {
            var found = identitySet();
            for (var key : lookup.getValue()) {
                if (key != null) {
                    found.addAll(indexes.get(lookup.getKey()).getOrDefault(key, List.of()));
                }
            }
            if (indexed == null) {
                indexed = found;
            } else {
                indexed.retainAll(found);
            }
        }
        var candidates = indexed;
        return entries.stream()
                .filter(entry -> candidates == null || candidates.contains(entry))
                .filter(filter)
                .toList();
    }

    private static Set<Object> identitySet() {
        return Collections.newSetFromMap(new IdentityHashMap<>());
    }
}
