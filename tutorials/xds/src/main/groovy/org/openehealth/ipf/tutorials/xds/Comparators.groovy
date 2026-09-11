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

import java.util.regex.Pattern
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based

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
      * Reference ids are carried by the query as the rendered HL7 v2 CX strings
      * (ITI TF-2: 3.18.4.1.2.3.7.7), so they are matched against the rendering of the entry's ones.
      */
     static def evalReferenceIdList(referenceIds, referenceIdList) {
         evalList(referenceIds, referenceIdList) { expected, actual ->
             expected == Hl7v2Based.render(actual)
         }
     }

     /**
      * The excluding counterpart of {@link #evalQueryList}: an entry survives unless it matches the
      * excluded query list. An entry carrying no such codes at all matches nothing and hence survives.
      */
     static def evalExcludedQueryList(codes, codeList) {
         isEmpty(codes) || !evalQueryList(codes, codeList)
     }

     /** The excluding counterpart of {@link #evalReferenceIdList}. */
     static def evalExcludedReferenceIdList(referenceIds, referenceIdList) {
         isEmpty(referenceIds) || !evalReferenceIdList(referenceIds, referenceIdList)
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

     static def matchesAuthors(authorPersons, authors) {
         if (authorPersons == null || authorPersons.isEmpty())
             return true
         
         def authorTexts = getAuthorTexts(authors)
         authorPersons.any {
             def pattern = Pattern.compile(it.replaceAll('\\%', '.*').replaceAll('_', '.'))
             authorTexts.any { pattern.matcher(it).matches() }
         }
     }

     private static def getAuthorTexts(authors) {
         def texts = []
         authors.each { author ->
             texts.add(Hl7v2Based.render(author.authorPerson))
             author.authorInstitution.each { texts.add(Hl7v2Based.render(it)) }
             author.authorRole.each        { texts.add(Hl7v2Based.render(it)) }
             author.authorSpecialty.each   { texts.add(Hl7v2Based.render(it)) }
             author.authorTelecom.each     { texts.add(Hl7v2Based.render(it)) }
         }
         texts
     }

     static def isInRange(range, time) {
         if (range == null) 
             return true

         (range.from == null || time.millis >= range.from.dateTime.millis) &&
                (range.to == null || time.millis <= range.to.dateTime.millis)
     }

}
