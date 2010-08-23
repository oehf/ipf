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

import org.apache.camel.Exchange
import org.apache.camel.processor.DelegateProcessor
import org.openehealth.ipf.tutorials.xds.DataStore

/**
 * Performs searches in a {@link DataStore}.
 * @author Jens Riemschneider
 */
class SearchProcessor extends DelegateProcessor {
     def filters
     def store
     def resultField
     def resultTypes
     def indexEvals
     
     void processNext(Exchange exchange) throws Exception {
         def results = store.search(indexEvals, filters, exchange.in.body)
         results = results.collect { result ->
             resultTypes.find { it.isOfType(result) }?.extractFrom(result)
         }

         get(exchange.in.body, resultField).addAll(results)

         super.processNext(exchange)
     }
     
     private def get(body, resultField) {
         def fields = resultField.split('\\.')
         fields.inject(body) { obj, field -> 
             if (obj[field] == null) {
                 obj[field] = []
             }
             obj[field]
         } 
     }
}
