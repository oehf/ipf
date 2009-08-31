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

import org.openehealth.ipf.commons.ihe.xds.metadata.*

/**
 * Type of search results used in queries to the {@link DataStore}.
 * @author Jens Riemschneider
 */
enum SearchResult {
     DOC_ENTRY(DocumentEntry.class, { it }),
     FOLDER(Folder.class, { it }),
     SUB_SET(SubmissionSet.class, { it }),
     ASSOC(Association.class, { it }),
     ASSOC_SOURCE(Association.class, { it.sourceUuid }),
     ASSOC_TARGET(Association.class, { it.targetUuid })
     
     def final type
     def final extractFrom
     
     private SearchResult(type, extractFrom) {
         this.type = type
         this.extractFrom = extractFrom
     }
     
     def isOfType(Object entry) {
         return entry.getClass().equals(type)
     }
}
