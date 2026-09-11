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
package org.openehealth.ipf.commons.ihe.xds.core.requests.query;

import org.openehealth.ipf.commons.ihe.xds.core.metadata.Author;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.LocalizedString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Turns a {@link SortOrder} into a {@link Comparator}, for the side that has to apply it.
 * <p>
 * A Document Registry built on IPF receives the requested order in {@link StoredQuery#getSortOrder()}
 * and has to order its result set by it. Doing that here rather than in every registry keeps the two
 * ends of the extension consistent: the same attribute names mean the same thing whether they are
 * written by an IPF consumer or applied by an IPF registry.
 * <p>
 * There is one comparator per kind of result -- {@link #documentEntryComparator(SortOrder)},
 * {@link #folderComparator(SortOrder)} and {@link #submissionSetComparator(SortOrder)} -- because the
 * attribute names differ by object type: the same concept is {@code $XDSDocumentEntryUniqueId} for a
 * document entry and {@code $XDSFolderUniqueId} for a folder. A query returning several kinds orders
 * each of them with its own comparator.
 * <p>
 * Two conventions worth knowing before relying on the result:
 * <ul>
 *   <li><b>Missing values sort last</b>, in both directions, as {@link SortKey.Direction#comparator()}
 *       defines.</li>
 *   <li><b>Multivalued attributes compare by their first value</b> in metadata order. That is
 *       well-defined and stable, but arbitrary when an entry has more than one -- an author person, for
 *       instance. A registry that needs different semantics registers its own attribute.</li>
 * </ul>
 * An attribute this does not know is ignored rather than rejected, matching how a registry treats a
 * query parameter it does not understand. Pass extra ones built with {@link #sortableBy(Function)} to
 * the two-argument overloads to teach it about extension attributes -- and report what was actually
 * applied in
 * {@link org.openehealth.ipf.commons.ihe.xds.core.responses.QueryResponse#setHonoredSortOrder}, so the
 * requester can tell.
 *
 * @author Christian Ohr
 * @since 6.0
 * @see SortOrder
 */
public final class SortOrderComparators {

    /**
     * One attribute a result set can be ordered by, able to produce the comparator for either direction.
     * <p>
     * This exists instead of a bare {@code Function} to the extracted value because the attributes of an
     * object type do not share a value type -- a size is a {@code Long}, a creation time a
     * {@code ZonedDateTime}, a status an enum -- while each of them is comparable to <em>itself</em>.
     * Java cannot say that about the values of one map: the nearest expressible type is the raw
     * {@code Comparable}, which then has to be cast back into a bounded form before it can be compared.
     * Capturing the value type here, at {@link #sortableBy(Function)}, where it is still known, keeps
     * the whole class free of casts.
     *
     * @param <T> the object type being ordered
     */
    @FunctionalInterface
    public interface SortableAttribute<T> {

        /**
         * @param direction the direction to order in
         * @return a comparator ordering by this attribute in that direction
         */
        Comparator<T> comparator(SortKey.Direction direction);
    }

    /**
     * @param extractor reads the attribute out of the object, e.g. {@code DocumentEntry::getSize}
     * @param <T>       the object type being ordered
     * @param <U>       the attribute's value type, which has to be comparable to itself
     * @return the attribute, ready to be put into the map of a comparator overload
     */
    public static <T, U extends Comparable<? super U>> SortableAttribute<T> sortableBy(
            Function<T, U> extractor) {
        return direction -> Comparator.comparing(extractor, direction.comparator());
    }

    /**
     * The slot names of an XDS object type all start with the same prefix, and the prefix is the only
     * thing that distinguishes {@code $XDSFolderUniqueId} from {@code $XDSDocumentEntryUniqueId}.
     */
    private static final String DOCUMENT_ENTRY_PREFIX = "$XDSDocumentEntry";
    private static final String FOLDER_PREFIX = "$XDSFolder";
    private static final String SUBMISSION_SET_PREFIX = "$XDSSubmissionSet";

    private static final Map<String, SortableAttribute<DocumentEntry>> DOCUMENT_ENTRY_ATTRIBUTES =
        attributes(DOCUMENT_ENTRY_PREFIX, Map.ofEntries(
            Map.entry(DOCUMENT_ENTRY_PREFIX + "CreationTime", sortableBy(entry -> dateTime(entry.getCreationTime()))),
            Map.entry(DOCUMENT_ENTRY_PREFIX + "ServiceStartTime", sortableBy(entry -> dateTime(entry.getServiceStartTime()))),
            Map.entry(DOCUMENT_ENTRY_PREFIX + "ServiceStopTime", sortableBy(entry -> dateTime(entry.getServiceStopTime()))),
            Map.entry(DOCUMENT_ENTRY_PREFIX + "Size", sortableBy(DocumentEntry::getSize)),
            Map.entry(DOCUMENT_ENTRY_PREFIX + "Hash", sortableBy(DocumentEntry::getHash)),
            Map.entry(DOCUMENT_ENTRY_PREFIX + "LanguageCode", sortableBy(DocumentEntry::getLanguageCode)),
            Map.entry(DOCUMENT_ENTRY_PREFIX + "MimeType", sortableBy(DocumentEntry::getMimeType)),
            Map.entry(DOCUMENT_ENTRY_PREFIX + "RepositoryUniqueId", sortableBy(DocumentEntry::getRepositoryUniqueId)),
            Map.entry(DOCUMENT_ENTRY_PREFIX + "SourcePatientId", sortableBy(entry -> Hl7v2Based.render(entry.getSourcePatientId()))),
            Map.entry(DOCUMENT_ENTRY_PREFIX + "ClassCode", sortableBy(entry -> code(entry.getClassCode()))),
            Map.entry(DOCUMENT_ENTRY_PREFIX + "TypeCode", sortableBy(entry -> code(entry.getTypeCode()))),
            Map.entry(DOCUMENT_ENTRY_PREFIX + "FormatCode", sortableBy(entry -> code(entry.getFormatCode()))),
            Map.entry(DOCUMENT_ENTRY_PREFIX + "PracticeSettingCode", sortableBy(entry -> code(entry.getPracticeSettingCode()))),
            Map.entry(DOCUMENT_ENTRY_PREFIX + "HealthcareFacilityTypeCode", sortableBy(entry -> code(entry.getHealthcareFacilityTypeCode()))),
            Map.entry(DOCUMENT_ENTRY_PREFIX + "AuthorPerson", sortableBy(entry -> firstAuthorPerson(entry.getAuthors()))),
            Map.entry(DOCUMENT_ENTRY_PREFIX + "ConfidentialityCode", sortableBy(entry -> firstCode(entry.getConfidentialityCodes()))),
            Map.entry(DOCUMENT_ENTRY_PREFIX + "EventCodeList", sortableBy(entry -> firstCode(entry.getEventCodeList())))));

    private static final Map<String, SortableAttribute<Folder>> FOLDER_ATTRIBUTES =
        attributes(FOLDER_PREFIX, Map.of(
            FOLDER_PREFIX + "LastUpdateTime", sortableBy(folder -> dateTime(folder.getLastUpdateTime())),
            FOLDER_PREFIX + "CodeList", sortableBy(folder -> firstCode(folder.getCodeList()))));

    /**
     * Intended recipients are deliberately absent: they are multivalued and, unlike an author person,
     * have no rendering that would give a meaningful order. A registry that wants to sort by them says
     * what that should mean by registering its own attribute.
     */
    private static final Map<String, SortableAttribute<SubmissionSet>> SUBMISSION_SET_ATTRIBUTES =
        attributes(SUBMISSION_SET_PREFIX, Map.of(
            SUBMISSION_SET_PREFIX + "SubmissionTime", sortableBy(set -> dateTime(set.getSubmissionTime())),
            SUBMISSION_SET_PREFIX + "SourceId", sortableBy(SubmissionSet::getSourceId),
            SUBMISSION_SET_PREFIX + "ContentType", sortableBy(set -> code(set.getContentTypeCode())),
            SUBMISSION_SET_PREFIX + "AuthorPerson", sortableBy(set -> firstAuthorPerson(set.getAuthors()))));

    private SortOrderComparators() {
    }

    // ------------------------------------------------------------------------------ document entries

    /**
     * @return the document entry attributes {@link #documentEntryComparator(SortOrder)} can order by
     */
    public static Set<String> supportedDocumentEntryAttributes() {
        return DOCUMENT_ENTRY_ATTRIBUTES.keySet();
    }

    /**
     * @param sortOrder the requested order, may be null
     * @return a comparator applying it, or null if there is nothing to apply -- which the caller should
     *      read as "leave the result set as it is" rather than as an error
     */
    public static Comparator<DocumentEntry> documentEntryComparator(SortOrder sortOrder) {
        return documentEntryComparator(sortOrder, Map.of());
    }

    /**
     * @param sortOrder            the requested order, may be null
     * @param additionalAttributes attributes beyond {@link #supportedDocumentEntryAttributes()}, keyed
     *                             by slot name and built with {@link #sortableBy(Function)}. An entry
     *                             here overrides a built-in one of the same name.
     * @return a comparator applying the order, or null if none of its keys is known
     */
    public static Comparator<DocumentEntry> documentEntryComparator(
            SortOrder sortOrder, Map<String, SortableAttribute<DocumentEntry>> additionalAttributes) {
        return comparator(sortOrder, DOCUMENT_ENTRY_ATTRIBUTES, additionalAttributes);
    }

    // ---------------------------------------------------------------------------------------- folders

    /**
     * @return the folder attributes {@link #folderComparator(SortOrder)} can order by
     */
    public static Set<String> supportedFolderAttributes() {
        return FOLDER_ATTRIBUTES.keySet();
    }

    /**
     * @param sortOrder the requested order, may be null
     * @return a comparator applying it, or null if there is nothing to apply
     */
    public static Comparator<Folder> folderComparator(SortOrder sortOrder) {
        return folderComparator(sortOrder, Map.of());
    }

    /**
     * @param sortOrder            the requested order, may be null
     * @param additionalAttributes attributes beyond {@link #supportedFolderAttributes()}, keyed by slot
     *                             name and built with {@link #sortableBy(Function)}
     * @return a comparator applying the order, or null if none of its keys is known
     */
    public static Comparator<Folder> folderComparator(
            SortOrder sortOrder, Map<String, SortableAttribute<Folder>> additionalAttributes) {
        return comparator(sortOrder, FOLDER_ATTRIBUTES, additionalAttributes);
    }

    // ------------------------------------------------------------------------------- submission sets

    /**
     * @return the submission set attributes {@link #submissionSetComparator(SortOrder)} can order by
     */
    public static Set<String> supportedSubmissionSetAttributes() {
        return SUBMISSION_SET_ATTRIBUTES.keySet();
    }

    /**
     * @param sortOrder the requested order, may be null
     * @return a comparator applying it, or null if there is nothing to apply
     */
    public static Comparator<SubmissionSet> submissionSetComparator(SortOrder sortOrder) {
        return submissionSetComparator(sortOrder, Map.of());
    }

    /**
     * @param sortOrder            the requested order, may be null
     * @param additionalAttributes attributes beyond {@link #supportedSubmissionSetAttributes()}, keyed
     *                             by slot name and built with {@link #sortableBy(Function)}
     * @return a comparator applying the order, or null if none of its keys is known
     */
    public static Comparator<SubmissionSet> submissionSetComparator(
            SortOrder sortOrder, Map<String, SortableAttribute<SubmissionSet>> additionalAttributes) {
        return comparator(sortOrder, SUBMISSION_SET_ATTRIBUTES, additionalAttributes);
    }

    // ------------------------------------------------------------------------------------- internals

    private static <T> Comparator<T> comparator(
            SortOrder sortOrder,
            Map<String, SortableAttribute<T>> knownAttributes,
            Map<String, SortableAttribute<T>> additionalAttributes) {

        if (sortOrder == null || sortOrder.isEmpty()) {
            return null;
        }
        var allAttributes = new HashMap<>(knownAttributes);
        allAttributes.putAll(additionalAttributes);

        // Keys are in order of precedence, so chaining them with thenComparing is the order itself: the
        // second key only decides where the first ties. Keys naming an attribute this does not know drop
        // out, and if that leaves nothing there is no order to apply.
        return sortOrder.getKeys().stream()
            .filter(key -> allAttributes.containsKey(key.getSlotName()))
            .map(key -> allAttributes.get(key.getSlotName()).comparator(key.getDirection()))
            .reduce(Comparator::thenComparing)
            .orElse(null);
    }

    /**
     * Builds the attribute map of one object type: the attributes every XDS object has, named with that
     * type's slot prefix, plus the ones specific to it. Carrying the prefix per type is what keeps
     * {@code $XDSFolderUniqueId} distinct from {@code $XDSDocumentEntryUniqueId}.
     *
     * @param slotPrefix         e.g. {@code $XDSFolder}
     * @param specificAttributes the attributes only this object type has
     */
    private static <T extends XDSMetaClass> Map<String, SortableAttribute<T>> attributes(
            String slotPrefix, Map<String, SortableAttribute<T>> specificAttributes) {

        Map<String, SortableAttribute<T>> attributes = new HashMap<>();
        attributes.put(slotPrefix + "EntryUUID", sortableBy(XDSMetaClass::getEntryUuid));
        attributes.put(slotPrefix + "UniqueId", sortableBy(XDSMetaClass::getUniqueId));
        attributes.put(slotPrefix + "LogicalID", sortableBy(XDSMetaClass::getLogicalUuid));
        attributes.put(slotPrefix + "PatientId", sortableBy(entry -> Hl7v2Based.render(entry.getPatientId())));
        attributes.put(slotPrefix + "Title", sortableBy(entry -> text(entry.getTitle())));
        attributes.put(slotPrefix + "Comments", sortableBy(entry -> text(entry.getComments())));
        attributes.put(slotPrefix + "Status", sortableBy(XDSMetaClass::getAvailabilityStatus));
        attributes.putAll(specificAttributes);
        return Map.copyOf(attributes);
    }

    // ---------------------------------------------------------------------------------- extractors

    private static ZonedDateTime dateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.getDateTime() : null;
    }

    private static String text(LocalizedString localizedString) {
        return localizedString != null ? localizedString.getValue() : null;
    }

    private static String code(Code code) {
        return code != null ? code.getCode() : null;
    }

    private static String firstCode(List<Code> codes) {
        return first(codes)
            .map(SortOrderComparators::code)
            .orElse(null);
    }

    private static String firstAuthorPerson(List<Author> authors) {
        return first(authors)
            .map(author -> Hl7v2Based.render(author.getAuthorPerson()))
            .orElse(null);
    }

    private static <T> Optional<T> first(List<T> values) {
        return (values == null || values.isEmpty()) ?
            Optional.empty() :
            Optional.ofNullable(values.get(0));
    }

}
