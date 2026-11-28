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

import lombok.Getter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;

import java.util.function.Function;

/**
 * What a {@link Search} looks for in the {@link DataStore}, and what it collects of each entry it finds.
 *
 * @param <E> the type of the stored entries searched for
 * @param <R> the type of the results collected
 * @author Jens Riemschneider
 * @since 6.2
 */
public final class SearchResult<E, R> {

    public static final SearchResult<DocumentEntry, DocumentEntry> DOC_ENTRY =
            new SearchResult<>("DOC_ENTRY", DocumentEntry.class, Function.identity());
    public static final SearchResult<Folder, Folder> FOLDER =
            new SearchResult<>("FOLDER", Folder.class, Function.identity());
    public static final SearchResult<SubmissionSet, SubmissionSet> SUB_SET =
            new SearchResult<>("SUB_SET", SubmissionSet.class, Function.identity());
    public static final SearchResult<Association, Association> ASSOC =
            new SearchResult<>("ASSOC", Association.class, Function.identity());
    /** Searches associations and collects the UUIDs of their sources. */
    public static final SearchResult<Association, String> ASSOC_SOURCE =
            new SearchResult<>("ASSOC_SOURCE", Association.class, Association::getSourceUuid);
    /** Searches associations and collects the UUIDs of their targets. */
    public static final SearchResult<Association, String> ASSOC_TARGET =
            new SearchResult<>("ASSOC_TARGET", Association.class, Association::getTargetUuid);

    private final String name;

    @Getter
    private final Class<E> type;
    private final Function<? super E, ? extends R> extractor;

    private SearchResult(String name, Class<E> type, Function<? super E, ? extends R> extractor) {
        this.name = name;
        this.type = type;
        this.extractor = extractor;
    }

    /**
     * @return whether the stored entry is of the type searched for. Subclasses do not count.
     */
    boolean isOfType(Object entry) {
        return entry.getClass() == type;
    }

    E cast(Object entry) {
        return type.cast(entry);
    }

    R extractFrom(E entry) {
        return extractor.apply(entry);
    }

    @Override
    public String toString() {
        return name;
    }
}
