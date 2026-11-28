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
package org.openehealth.ipf.tutorials.xds

import java.time.temporal.ChronoUnit
import java.util.regex.Pattern
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntryType
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp

/**
 * Comparators for datatypes used with XDS.
 * @author Jens Riemschneider
 */
class Comparators {

     static def equals(expected, actual) { expected == null || expected == actual }
     static def contains(expected, actual) { expected == null || expected.contains(actual) }
     static def any(expected, closure) { expected == null || expected.any(closure) } 

     /** The excluding counterparts: nothing to exclude means nothing is excluded. */
     static def containsNot(excluded, actual) { excluded == null || !excluded.contains(actual) }
     static def none(excluded, closure) { excluded == null || !excluded.any(closure) }

     static def matchesCode(expected, actual) {
         (expected.code == null || expected.code == actual.code) && 
         (expected.schemeName == null || expected.schemeName == actual.schemeName)
     }    

     static def evalQueryList(codes, codeList) {
         evalList(codes, codeList) { expected, actual -> matchesCode(expected, actual) }
     }

     /**
      * The excluding counterpart of {@link #evalQueryList}: an entry survives unless it matches the
      * excluded query list. An entry carrying no such codes at all matches nothing and hence survives.
      */
     static def evalExcludedQueryList(codes, codeList) {
         isEmpty(codes) || !evalQueryList(codes, codeList)
     }

     /**
      * Evaluates a query list with its AND/OR semantics: every element of the outer list must be
      * satisfied, and an element is satisfied by any value of its inner list matching any value the
      * entry carries.
      */
     private static def evalList(queryList, values, Closure matcher) {
         if (isEmpty(queryList))
             return true
         
         if (values.isEmpty())
             return false
         
         queryList.outerList.every { innerList ->
             innerList.any { inner ->
                 values.any { matcher(inner, it) }
             }
         }
     }

     private static def isEmpty(queryList) {
         queryList == null || queryList.outerList.isEmpty()
     }

     static def matchesAuthor(authorPerson, authors) {
         authorPerson == null || matchesAuthors([authorPerson], authors)
     }

     /**
      * The excluding counterpart of {@link #matchesAuthors}: an entry survives unless one of its
      * authors matches an excluded author person.
      */
     static def matchesNoAuthor(authorPersons, authors) {
         authorPersons == null || authorPersons.isEmpty() || !matchesAuthors(authorPersons, authors)
     }

     /**
      * Matches author person query values against the authors of an entry. Per ITI TF-2: 3.18.4.1.2.3.7.1,
      * a value is a pattern compatible with SQL {@code LIKE} ({@code %} for any characters, {@code _} for
      * a single one), applied to the authorPerson (XCN) value only -- not to institution, role, specialty
      * or telecom.
      */
     static def matchesAuthors(authorPersons, authors) {
         if (authorPersons == null || authorPersons.isEmpty())
             return true

         def persons = authors.findAll { it.authorPerson != null }.collect { Hl7v2Based.render(it.authorPerson) }
         authorPersons.any { authorPerson ->
             def pattern = likePattern(authorPerson)
             persons.any { pattern.matcher(it).matches() }
         }
     }

     /**
      * Translates an SQL LIKE pattern into a regular expression. Everything except the two wildcards is
      * literal: XCN values contain {@code ^} and {@code &}, which must not act as regex syntax.
      */
     static Pattern likePattern(String like) {
         def regex = new StringBuilder()
         like.each { ch ->
             regex.append(ch == '%' ? '.*' : ch == '_' ? '.' : Pattern.quote(ch))
         }
         Pattern.compile(regex.toString(), Pattern.DOTALL)
     }

     /**
      * Evaluates a time range parameter (ITI TF-2: 3.18.4.1.2.3.3): the From value is inclusive, the To value
      * exclusive. A time value covers the whole period its precision denotes -- creationTime "1981" is the
      * year 1981 -- and matches if that period overlaps the requested range.
      *
      * @param ignoreIfAbsent whether an entry without the attribute matches nevertheless. ITI-18 demands
      *        this for serviceStartTime and serviceStopTime only (ITI TF-2: 3.18.4.1.2.3.3.1).
      */
     static def isInRange(range, Timestamp time, boolean ignoreIfAbsent = false) {
         if (range == null || (range.from == null && range.to == null))
             return true
         if (time == null)
             return ignoreIfAbsent

         def start = time.dateTime.toInstant()
         def end = time.dateTime.plus(1, unitOf(time.precision)).toInstant()
         (range.from == null || end.isAfter(range.from.dateTime.toInstant())) &&
                (range.to == null || start.isBefore(range.to.dateTime.toInstant()))
     }

     private static ChronoUnit unitOf(Timestamp.Precision precision) {
         switch (precision) {
             case Timestamp.Precision.YEAR:   return ChronoUnit.YEARS
             case Timestamp.Precision.MONTH:  return ChronoUnit.MONTHS
             case Timestamp.Precision.DAY:    return ChronoUnit.DAYS
             case Timestamp.Precision.HOUR:   return ChronoUnit.HOURS
             case Timestamp.Precision.MINUTE: return ChronoUnit.MINUTES
             default:                         return ChronoUnit.SECONDS
         }
     }

     /**
      * Evaluates $XDSDocumentEntryType: if the query names no type, only stable entries match
      * (ITI TF-2: 3.18.4.1.2.3.6.2).
      */
     static def matchesEntryType(List<DocumentEntryType> queried, DocumentEntryType actual) {
         (queried ?: [DocumentEntryType.STABLE]).contains(actual ?: DocumentEntryType.STABLE)
     }

}
