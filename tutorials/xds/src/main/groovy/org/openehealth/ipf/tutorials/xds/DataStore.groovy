/*

import com.sun.java.util.jar.pack.ConstantPool.Index;
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

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import javax.activation.DataHandler
import javax.mail.util.ByteArrayDataSource
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document
import org.openehealth.ipf.tutorials.xds.ContentUtils

/**
 * A simple store for meta data and documents.
 * @author Jens Riemschneider
 */
public class DataStore {
     private final static Log log = LogFactory.getLog(DataStore.class)
     
     def entries = new CopyOnWriteArrayList()
     def documents = new ConcurrentHashMap()
     
     def indexes = [uniqueId: new ConcurrentHashMap(), 
                    entryUuid: new ConcurrentHashMap(), 
                    sourceUuid: new ConcurrentHashMap(), 
                    targetUuid: new ConcurrentHashMap(), 
                    patientId: new ConcurrentHashMap()]

     /**
      * Stores a document in memory for later retrieval
      * @param document
      *          the document.
      */
     def store(Document document) {
         def uniqueId = document.documentEntry.uniqueId
         def contents = ContentUtils.getContent(document.dataHandler)     
         documents.put(uniqueId, contents)
         log.info("Stored document: " + uniqueId) 
     }

     /**
      * Stores the given entry in memory.
      * @param entry
      *          the new entry.
      */
     def store(entry) {
         entries.add(entry)
         indexes.findAll { entry.metaClass.hasProperty(entry, it.key) }
             .each { getFromIndex(it.value, entry."$it.key").add(entry) }
         log.info('Stored: ' + entry)
     }
     
     /**
      * Retrieves a document from the store.
      * @param uniqueId
      *          the unique ID of the document.
      * @return a {@link DataHandler} to access the document.
      */
     def get(uniqueId) {
         def contents = documents[uniqueId]
         if (contents == null) {
             throw new IllegalArgumentException('Unique ID not found in store: ' + uniqueId)
         }

         new DataHandler(new ByteArrayDataSource(contents, 'text/plain'))
     }

     /**
      * Performs a query on the stored entries.
      * @param indexEvals
      *         the list of index evaluations that are applied before filtering.
      * @param filters
      *         the list of filters to apply for the search.
      * @param param
      *         an additional parameter passed to the filter for each check.
      * @return the matching entries.
      */
     def search(indexEvals, filters, param) {
         def matches = indexEvals.collect { evalIndex(it.value(param), it.key) }
             .findAll { it != null }
             .inject(entries) { allMatches, indexMatches ->
                 allMatches != null ? allMatches.intersect(indexMatches) : indexMatches
             }

         if (matches == null) 
              matches = entries
              
         matches.findAll { entry -> 
             filters.every { filter -> filter(entry, param) } 
         }
     }

     private def evalIndex(List<?> keys, index) {
         keys != null ? 
                 keys.inject([] as Set) { prev, key ->
                     def result = indexes[index][key]
                     result != null ? prev + result : prev
                 }.findAll { it != null } : 
                 null
     }

     private def evalIndex(key, index) {
         key != null ? evalIndex([key], index) : null
     }
     
     private def getFromIndex(index, key) {
          index.putIfAbsent(key, new CopyOnWriteArrayList())
          index[key]
     }
}
