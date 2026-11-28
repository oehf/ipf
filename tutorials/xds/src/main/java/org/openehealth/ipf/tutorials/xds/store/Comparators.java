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

import org.openehealth.ipf.commons.ihe.xds.core.metadata.Author;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Code;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.TimeRange;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp;
import org.openehealth.ipf.commons.ihe.xds.core.requests.query.QueryList;

import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Comparisons of query parameters with the metadata of stored entries.
 * <p>
 * Unless stated otherwise, a query parameter that is {@code null} constrains nothing, so the comparison
 * succeeds.
 *
 * @author Jens Riemschneider
 */
final class Comparators {

    private Comparators() {
    }

    static boolean matchesValue(Object expected, Object actual) {
        return expected == null || Objects.equals(expected, actual);
    }

    static boolean contains(Collection<?> expected, Object actual) {
        return expected == null || expected.contains(actual);
    }

    static <T> boolean any(Collection<T> expected, Predicate<? super T> predicate) {
        return expected == null || expected.stream().anyMatch(predicate);
    }

    /** The excluding counterpart of {@link #contains}: nothing to exclude means nothing is excluded. */
    static boolean containsNot(Collection<?> excluded, Object actual) {
        return excluded == null || !excluded.contains(actual);
    }

    /** The excluding counterpart of {@link #any}: nothing to exclude means nothing is excluded. */
    static <T> boolean none(Collection<T> excluded, Predicate<? super T> predicate) {
        return excluded == null || excluded.stream().noneMatch(predicate);
    }

    /**
     * @return whether the code carries the code value and scheme of the queried code. A part the query
     *         leaves out matches anything, and a missing code matches only a query that leaves out both.
     */
    static boolean matchesCode(Code expected, Code actual) {
        return (expected.getCode() == null
                    || (actual != null && expected.getCode().equals(actual.getCode())))
            && (expected.getSchemeName() == null
                    || (actual != null && expected.getSchemeName().equals(actual.getSchemeName())));
    }

    static boolean evalQueryList(QueryList<Code> codes, List<Code> codeList) {
        return evalList(codes, codeList, Comparators::matchesCode);
    }

    /**
     * The excluding counterpart of {@link #evalQueryList}: an entry survives unless it matches the
     * excluded query list. An entry carrying no such codes at all matches nothing and hence survives.
     */
    static boolean evalExcludedQueryList(QueryList<Code> codes, List<Code> codeList) {
        return isEmpty(codes) || !evalQueryList(codes, codeList);
    }

    /**
     * Evaluates a query list with its AND/OR semantics: every element of the outer list must be
     * satisfied, and an element is satisfied by any value of its inner list matching any value the
     * entry carries.
     */
    private static <T> boolean evalList(QueryList<T> queryList, List<T> values, BiPredicate<T, T> matcher) {
        if (isEmpty(queryList)) {
            return true;
        }
        if (values == null || values.isEmpty()) {
            return false;
        }
        return queryList.getOuterList().stream().allMatch(innerList ->
                innerList.stream().anyMatch(inner ->
                        values.stream().anyMatch(value -> matcher.test(inner, value))));
    }

    private static boolean isEmpty(QueryList<?> queryList) {
        return queryList == null || queryList.getOuterList().isEmpty();
    }

    static boolean matchesAuthor(String authorPerson, List<Author> authors) {
        return authorPerson == null || matchesAuthors(List.of(authorPerson), authors);
    }

    /**
     * The excluding counterpart of {@link #matchesAuthors}: an entry survives unless one of its
     * authors matches an excluded author person.
     */
    static boolean matchesNoAuthor(List<String> authorPersons, List<Author> authors) {
        return authorPersons == null || authorPersons.isEmpty() || !matchesAuthors(authorPersons, authors);
    }

    /**
     * Matches author person query values against the authors of an entry. Per ITI TF-2: 3.18.4.1.2.3.7.1,
     * a value is a pattern compatible with SQL {@code LIKE} ({@code %} for any characters, {@code _} for
     * a single one), applied to the authorPerson (XCN) value only -- not to institution, role, specialty
     * or telecom.
     */
    static boolean matchesAuthors(List<String> authorPersons, List<Author> authors) {
        if (authorPersons == null || authorPersons.isEmpty()) {
            return true;
        }
        var persons = authors.stream()
                .filter(author -> author.getAuthorPerson() != null)
                .map(author -> Hl7v2Based.render(author.getAuthorPerson()))
                // an author person without any populated field renders as null
                .filter(Objects::nonNull)
                .toList();
        return authorPersons.stream()
                .map(Comparators::likePattern)
                .anyMatch(pattern ->
                    persons.stream()
                        .anyMatch(person -> pattern.matcher(person).matches()));
    }

    /**
     * Translates an SQL LIKE pattern into a regular expression. Everything except the two wildcards is
     * literal: XCN values contain {@code ^} and {@code &}, which must not act as regex syntax.
     */
    static Pattern likePattern(String like) {
        // by code point, so that a character outside the BMP is quoted whole, not as two lone surrogates
        var regex = like.codePoints()
                .mapToObj(Comparators::toRegex)
                .collect(Collectors.joining());
        return Pattern.compile(regex, Pattern.DOTALL);
    }

    private static String toRegex(int likeCharacter) {
        return switch (likeCharacter) {
            case '%' -> ".*";   // any number of characters
            case '_' -> ".";    // exactly one character
            default -> Pattern.quote(Character.toString(likeCharacter));
        };
    }

    static boolean isInRange(TimeRange range, Timestamp time) {
        return isInRange(range, time, false);
    }

    /**
     * Evaluates a time range parameter (ITI TF-2: 3.18.4.1.2.3.3): the From value is inclusive, the To value
     * exclusive. A time value covers the whole period its precision denotes -- creationTime "1981" is the
     * year 1981 -- and matches if that period overlaps the requested range.
     *
     * @param ignoreIfAbsent whether an entry without the attribute matches nevertheless. ITI-18 demands
     *        this for serviceStartTime and serviceStopTime only (ITI TF-2: 3.18.4.1.2.3.3.1).
     */
    static boolean isInRange(TimeRange range, Timestamp time, boolean ignoreIfAbsent) {
        if (range == null || (range.getFrom() == null && range.getTo() == null)) {
            return true;
        }
        if (time == null) {
            return ignoreIfAbsent;
        }
        var start = time.getDateTime().toInstant();
        var end = time.getDateTime().plus(1, unitOf(time.getPrecision())).toInstant();
        return (range.getFrom() == null || end.isAfter(range.getFrom().getDateTime().toInstant()))
            && (range.getTo() == null || start.isBefore(range.getTo().getDateTime().toInstant()));
    }

    private static ChronoUnit unitOf(Timestamp.Precision precision) {
        return switch (precision) {
            case YEAR -> ChronoUnit.YEARS;
            case MONTH -> ChronoUnit.MONTHS;
            case DAY -> ChronoUnit.DAYS;
            case HOUR -> ChronoUnit.HOURS;
            case MINUTE -> ChronoUnit.MINUTES;
            default -> ChronoUnit.SECONDS;
        };
    }

    /**
     * Evaluates $XDSDocumentEntryType: if the query names no type, only stable entries match
     * (ITI TF-2: 3.18.4.1.2.3.6.2).
     */
    static boolean matchesEntryType(List<DocumentEntryType> queried, DocumentEntryType actual) {
        var types = (queried == null || queried.isEmpty()) ? List.of(DocumentEntryType.STABLE) : queried;
        return types.contains(actual != null ? actual : DocumentEntryType.STABLE);
    }
}
