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

import static org.openehealth.ipf.tutorials.xds.Comparators.*
import static org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.AssociationType.*

import org.apache.camel.model.OutputDefinition
import org.apache.camel.spi.RouteContext
import org.apache.camel.Processor
import org.openehealth.ipf.tutorials.xds.DataStore


/**
 * Model class to add the Search processor to route implementations.
 * @author Jens Riemschneider
 */
class SearchDefinition extends OutputDefinition<SearchDefinition> {
	def containerClosure
	def resultTypes
	def filters = []
	def resultField
	def indexEvals = []
	
	SearchDefinition(List<?> types) {
	    resultTypes = types
	    by { entry, param -> types.any { it.isOfType(entry) } }
	}
	
    SearchDefinition(SearchResult type) { this([type]) }
    
	def into(field) {
		this.resultField = field
		this
	}
	
	def by(filter) {
		filters.add(filter)
		this
	}

	def byIndex(indexEval) {
		indexEvals.add(indexEval)
		this
	}
	
	def by(Object[] filters) {
	    filters.each { by(it) }
	}
	
	def targets(field) {
        byIndex { store, param -> evalIndex(get(param, field)*.entryUuid, store.targetUuidIndex) }
	}
	
	def targetUuids(field) {
	    byIndex { store, param -> evalIndex(get(param, field), store.targetUuidIndex) }
	}
	
    def targetUuid(field) {
        byIndex { store, param -> evalIndex(get(param, field), store.targetUuidIndex) }
    }
    
	def sources(field) {
        byIndex { store, param -> evalIndex(get(param, field)*.entryUuid, store.sourceUuidIndex) }
	}
	
	def sourceUuids(field) {
        byIndex { store, param -> evalIndex(get(param, field), store.sourceUuidIndex) }
	}
	
	def uniqueIds(field) {
		byIndex { store, param -> evalIndex(get(param, field), store.uniqueIdIndex) }
	} 
	
    def uniqueId(field) {
		byIndex { store, param -> evalIndex(get(param, field), store.uniqueIdIndex) }
    }

	def uuids(field) {
		byIndex { store, param -> evalIndex(get(param, field), store.uuidIndex) }
	}     
	
    def uuid(field) {
		byIndex { store, param -> evalIndex(get(param, field), store.uuidIndex) }
    }     
        
    def referenced(field) {
        byIndex { store, param -> 
            def assocs = get(param, field)
            def sourcesAndTargets = assocs*.sourceUuid + assocs*.targetUuid 
            evalIndex(sourcesAndTargets, store.uuidIndex) 
        }
    }
    
	def status(status) {
	    by { entry, param -> equals(status, entry.availabilityStatus) }
	}
	
	def hasMember() {
	    by { entry, param -> entry.associationType == HAS_MEMBER }
	}
	
	def isOfTypes(types) {
	    by { entry, param -> contains(types, entry.associationType) }
	}
	
	def types(field) {
	    by { entry, param -> contains(get(param, field), entry.associationType) }
	}
	
    def confCodes(field) {
        by { entry, param -> evalQueryList(get(param, field), entry.confidentialityCodes) }
    }

    def formatCodes(field) {
        by { entry, param -> any(get(param, field)) { matchesCode(it, entry.formatCode) } }
    }
    
    def byQuery(field) {
        by { entry, param ->
            new QueryMatcher().matches(entry, get(param, field))
        }
    }
    
    def patientId(field) {
        byIndex { store, param -> evalIndex(get(param, field), store.patientIdIndex) }
    }
    
	def withoutPatientId(field) {
	    by { entry, param -> !equals(get(param, field), entry.patientId) }
	}
	
	def withoutHash(field) {
	    by { entry, param -> !equals(get(param, field), entry.hash) }
	}
	
	Processor createProcessor(RouteContext routeContext) throws Exception {
		new SearchProcessor(
				indexEvals : indexEvals,
		        filters : filters,
		        resultTypes : resultTypes, 
		        resultField : resultField,
				store : routeContext.lookup('dataStore', DataStore.class),
				processor : routeContext.createProcessor(this))
	}    

    private static def get(body, resultField) {
        resultField.split('\\.').inject(body) { obj, field -> obj[field] } 
    }

    private static def evalIndex(List<?> expected, index) {
		expected != null ? 
				expected.inject([] as Set) { prev, it ->
					def result = index[it]
	                result != null ? prev + result : prev
		        }.findAll { it != null } : 
		        null
    }

    private static def evalIndex(expected, index) {
		expected != null ? evalIndex([expected], index) : null
    }
}
