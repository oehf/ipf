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

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Identifiable;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.Query;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryList;
import org.openehealth.ipf.tutorials.xds.store.DataStore.Index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.openehealth.ipf.tutorials.xds.store.Comparators.any;
import static org.openehealth.ipf.tutorials.xds.store.Comparators.contains;
import static org.openehealth.ipf.tutorials.xds.store.Comparators.evalQueryList;
import static org.openehealth.ipf.tutorials.xds.store.Comparators.matchesCode;
import static org.openehealth.ipf.tutorials.xds.store.Comparators.matchesEntryType;
import static org.openehealth.ipf.tutorials.xds.store.MetadataEntries.statusOf;

/**
 * A search in the {@link DataStore}, used as a route step:
 * <pre>{@code
 * .process(search(QueryContext.class, DOC_ENTRY)
 *         .byQuery(context -> context.getReq().getQuery())
 *         .patientId(context -> ...)
 *         .into(context -> context.getResp().getDocumentEntries()))
 * }</pre>
 * The message body is a context object of type {@code C}. All parameters of the search are read from it
 * when the step runs, and the results are added to the collection that {@link #into} names, skipping those
 * already in it. That lets several searches collect into the same collection.
 * <p>
 * The index methods ({@link #uuid}, {@link #targets}, ...) look up candidates in an {@link Index} of the
 * store; a parameter that is {@code null} leaves its index unused, while an empty collection matches
 * nothing. The filter methods ({@link #hasStatus}, {@link #byQuery}, ...) then narrow the candidates
 * down. Filters that read attributes only some kinds of entries have are refused when the route is built
 * for a search returning another kind.
 *
 * @param <C> the type of the context in the message body
 * @param <E> the type of the stored entries searched for
 * @param <R> the type of the results collected
 * @author Jens Riemschneider
 * @author Christian Ohr
 * @since 6.2
 */
public final class Search<C, E, R> implements Processor {

    private final Class<C> contextType;
    private final SearchResult<E, R> result;
    private final Map<Index, Function<? super C, ? extends Collection<?>>> keys = new EnumMap<>(Index.class);
    private final List<BiPredicate<? super E, ? super C>> filters = new ArrayList<>();
    private Function<? super C, ? extends Collection<? super R>> into;

    private Search(Class<C> contextType, SearchResult<E, R> result) {
        this.contextType = Objects.requireNonNull(contextType, "contextType");
        this.result = Objects.requireNonNull(result, "result");
    }

    /**
     * @param contextType the type of the context in the message body
     * @param result      what to search for and what to collect
     * @return a new search, to be completed by {@link #into}
     */
    public static <C, E, R> Search<C, E, R> search(Class<C> contextType, SearchResult<E, R> result) {
        return new Search<>(contextType, result);
    }

    // ----- Index lookups

    /** Entries with one of the given UUIDs. */
    public Search<C, E, R> uuids(Function<? super C, ? extends Collection<String>> uuids) {
        return byIndex(Index.ENTRY_UUID, uuids);
    }

    /** The entry with the given UUID. */
    public Search<C, E, R> uuid(Function<? super C, String> uuid) {
        return byIndex(Index.ENTRY_UUID, single(uuid));
    }

    /** Entries with one of the given unique IDs. */
    public Search<C, E, R> uniqueIds(Function<? super C, ? extends Collection<String>> uniqueIds) {
        return byIndex(Index.UNIQUE_ID, uniqueIds);
    }

    /** Entries with the given unique ID. */
    public Search<C, E, R> uniqueId(Function<? super C, String> uniqueId) {
        return byIndex(Index.UNIQUE_ID, single(uniqueId));
    }

    /** Entries of the given patient. */
    public Search<C, E, R> patientId(Function<? super C, Identifiable> patientId) {
        return byIndex(Index.PATIENT_ID, single(patientId));
    }

    /** Entries that are the source or target of one of the given associations. */
    public Search<C, E, R> referenced(Function<? super C, ? extends Collection<Association>> associations) {
        return byIndex(Index.ENTRY_UUID, context -> {
            var found = associations.apply(context);
            return found == null ? null : found.stream()
                    .flatMap(association -> Stream.of(association.getSourceUuid(), association.getTargetUuid()))
                    .toList();
        });
    }

    /** Associations whose source is one of the given entries. */
    public Search<C, E, R> sources(Function<? super C, ? extends Collection<? extends XDSMetaClass>> entries) {
        requireType(Association.class, "sources");
        return byIndex(Index.SOURCE_UUID, entryUuids(entries));
    }

    /** Associations whose source has one of the given UUIDs. */
    public Search<C, E, R> sourceUuids(Function<? super C, ? extends Collection<String>> uuids) {
        requireType(Association.class, "sourceUuids");
        return byIndex(Index.SOURCE_UUID, uuids);
    }

    /** Associations whose target is one of the given entries. */
    public Search<C, E, R> targets(Function<? super C, ? extends Collection<?>> entries) {
        requireType(Association.class, "targets");
        return byIndex(Index.TARGET_UUID, context -> {
            var found = entries.apply(context);
            return found == null ? null : found.stream().map(MetadataEntries::entryUuidOf).toList();
        });
    }

    /** Associations whose target has one of the given UUIDs. */
    public Search<C, E, R> targetUuids(Function<? super C, ? extends Collection<String>> uuids) {
        requireType(Association.class, "targetUuids");
        return byIndex(Index.TARGET_UUID, uuids);
    }

    /** Associations whose target has the given UUID. */
    public Search<C, E, R> targetUuid(Function<? super C, String> uuid) {
        requireType(Association.class, "targetUuid");
        return byIndex(Index.TARGET_UUID, single(uuid));
    }

    // ----- Filters

    /** Entries with the given availability status. */
    public Search<C, E, R> hasStatus(AvailabilityStatus status) {
        return filter((entry, context) -> Comparators.matchesValue(status, statusOf(entry)));
    }

    /** HasMember associations. */
    public Search<C, E, R> hasMember() {
        requireType(Association.class, "hasMember");
        return filter((entry, context) -> ((Association) entry).getAssociationType() == AssociationType.HAS_MEMBER);
    }

    /** Associations of one of the given types. */
    public Search<C, E, R> isOfTypes(Collection<AssociationType> types) {
        requireType(Association.class, "isOfTypes");
        return filter((entry, context) -> contains(types, ((Association) entry).getAssociationType()));
    }

    /** Associations of one of the given types; all associations if there are none. */
    public Search<C, E, R> types(Function<? super C, ? extends Collection<AssociationType>> types) {
        requireType(Association.class, "types");
        return filter((entry, context) ->
                contains(types.apply(context), ((Association) entry).getAssociationType()));
    }

    /** Document entries matching the confidentiality code query list. */
    public Search<C, E, R> confCodes(Function<? super C, QueryList<Code>> codes) {
        requireType(DocumentEntry.class, "confCodes");
        return filter((entry, context) ->
                evalQueryList(codes.apply(context), ((DocumentEntry) entry).getConfidentialityCodes()));
    }

    /** Document entries with one of the given format codes. */
    public Search<C, E, R> formatCodes(Function<? super C, ? extends Collection<Code>> codes) {
        requireType(DocumentEntry.class, "formatCodes");
        return filter((entry, context) ->
                any(codes.apply(context), code -> matchesCode(code, ((DocumentEntry) entry).getFormatCode())));
    }

    /** Document entries of one of the given types; only stable ones if there are none. */
    public Search<C, E, R> entryTypes(Function<? super C, List<DocumentEntryType>> types) {
        requireType(DocumentEntry.class, "entryTypes");
        return filter((entry, context) -> matchesEntryType(types.apply(context), ((DocumentEntry) entry).getType()));
    }

    /** Entries matching a Find... stored query; see {@link QueryMatcher}. */
    public Search<C, E, R> byQuery(Function<? super C, ? extends Query> query) {
        return filter((entry, context) -> QueryMatcher.matches(entry, query.apply(context)));
    }

    /** Entries of any other patient than the given one. */
    public Search<C, E, R> withoutPatientId(Function<? super C, Identifiable> patientId) {
        requireType(XDSMetaClass.class, "withoutPatientId");
        return filter((entry, context) ->
                !Comparators.matchesValue(patientId.apply(context), ((XDSMetaClass) entry).getPatientId()));
    }

    /** Document entries with another hash than the given one. */
    public Search<C, E, R> withoutHash(Function<? super C, String> hash) {
        requireType(DocumentEntry.class, "withoutHash");
        return filter((entry, context) ->
                !Comparators.matchesValue(hash.apply(context), ((DocumentEntry) entry).getHash()));
    }

    /** Entries satisfying the given condition. */
    public Search<C, E, R> filter(BiPredicate<? super E, ? super C> filter) {
        filters.add(Objects.requireNonNull(filter, "filter"));
        return this;
    }

    // ----- Result

    /**
     * @param into the collection to add the results to
     * @return this search, ready to be used as route step
     */
    public Search<C, E, R> into(Function<? super C, ? extends Collection<? super R>> into) {
        this.into = Objects.requireNonNull(into, "into");
        return this;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        if (into == null) {
            throw new IllegalStateException("Search for " + result + " does not say where to put its results");
        }
        var context = exchange.getIn().getMandatoryBody(contextType);
        var store = DataStore.lookup(exchange);

        var lookups = new EnumMap<Index, Collection<?>>(Index.class);
        keys.forEach((index, key) -> {
            var value = key.apply(context);
            if (value != null) {
                lookups.put(index, value);
            }
        });

        var found = store.search(lookups, entry -> result.isOfType(entry) && matches(result.cast(entry), context));

        var collected = into.apply(context);
        for (var entry : found) {
            var item = result.extractFrom(result.cast(entry));
            if (!collected.contains(item)) {
                collected.add(item);
            }
        }
    }

    private boolean matches(E entry, C context) {
        return filters.stream().allMatch(filter -> filter.test(entry, context));
    }

    private Search<C, E, R> byIndex(Index index, Function<? super C, ? extends Collection<?>> key) {
        if (keys.putIfAbsent(index, key) != null) {
            throw new IllegalStateException("Search for " + result + " uses the index " + index + " twice");
        }
        return this;
    }

    /**
     * Fails building the route if this search does not return entries of the given type, so that a filter
     * cannot silently read an attribute the entries do not have.
     */
    private void requireType(Class<?> type, String method) {
        if (!type.isAssignableFrom(result.getType())) {
            throw new IllegalArgumentException(method + "() applies to " + type.getSimpleName()
                    + " only, but the search is for " + result);
        }
    }

    private static <C> Function<C, List<Object>> single(Function<? super C, ?> key) {
        return context -> {
            var value = key.apply(context);
            return value == null ? null : List.of(value);
        };
    }

    private static <C> Function<C, List<String>> entryUuids(
            Function<? super C, ? extends Collection<? extends XDSMetaClass>> entries) {
        return context -> {
            var found = entries.apply(context);
            return found == null ? null : found.stream().map(XDSMetaClass::getEntryUuid).toList();
        };
    }
}
