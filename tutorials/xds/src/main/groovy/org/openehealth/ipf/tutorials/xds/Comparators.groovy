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

import java.text.SimpleDateFormat
import java.util.regex.Pattern
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Hl7v2Based

/**
 * Comparators for datatypes used with XDS.
 * @author Jens Riemschneider
 */
public class Comparators {
     static def equals(expected, actual) { expected == null || expected.equals(actual) }
     static def contains(expected, actual) { expected == null || expected.contains(actual) }
     static def any(expected, closure) { expected == null || expected.any(closure) } 

     static def matchesCode(expected, actual) {
         (expected.code == null || expected.code == actual.code) && 
         (expected.schemeName == null || expected.schemeName == actual.schemeName)
     }    

     static def evalQueryList(eventCodes, eventCodeList) {
         if (eventCodes == null || eventCodes.outerList.isEmpty())
             return true
         
         if (eventCodeList.isEmpty())
             return false
         
         eventCodes.outerList.every { innerList ->
             innerList.any { inner ->
                 eventCodeList.any { matchesCode(inner, it) }
             }
         }
     }

     static def matchesAuthor(authorPerson, authors) {
         authorPerson == null || matchesAuthors([authorPerson], authors)
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
             author.authorInstitution.each { texts.add(Hl7v2Based.render(it))  }
             texts.addAll(author.authorRole)
             texts.addAll(author.authorSpecialty)
         }
         texts
     }

     static def isInRange(range, timeStr) {
         if (range == null) 
             return true

         def time = parseDate(timeStr)
         
         (range.from == null || time >= parseDate(range.from)) && 
                (range.to == null || time <= parseDate(range.to))
     }

     private static def parseDate(dateStr) {
         def format = new SimpleDateFormat("yyyyMMddHHmmss")
         dateStr = dateStr + "00000101000000".substring(dateStr.length())
         format.parse(dateStr).time
     }
}
