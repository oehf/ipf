/*
 * Copyright 2008 the original author or authors.
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
import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.support.ExpressionAdapter
import org.openehealth.ipf.commons.core.config.ContextFacade
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException
import org.slf4j.Logger

import jakarta.activation.DataHandler

import java.time.ZonedDateTime

/**
 * The DSL for the registry and repository route implementations.
 *
 * @author Jens Riemschneider
 */
class RegRepModelExtension {
     
    static ProcessorDefinition store(ProcessorDefinition self) {
        self.process {
            dataStore().store(it.in.body.entry)
        }
    }
    
    static ProcessorDefinition retrieve(ProcessorDefinition self) {
        self.transform(new ExpressionAdapter() {
            @Override
            Object evaluate(Exchange exchange) {
                new RetrievedDocument(dataStore().get(
                        exchange.in.body.documentUniqueId),
                        exchange.in.body,
                        null,
                        null,
                        'text/plain')
            }
        })
    }
    
    static SearchDefinition search(ProcessorDefinition self, resultTypes) {
       def answer = new SearchDefinition(resultTypes)
       self.addOutput(answer)
       answer    
    }
    
    static ProcessorDefinition fail(ProcessorDefinition self, ValidationMessage message) {
        self.process { throw new XDSMetaDataException(message) }
    }
    
    static ProcessorDefinition updateWithRepositoryData(ProcessorDefinition self) {
        self.process {
            def documentEntry = it.in.body.entry.documentEntry
            def dataHandler = it.in.body.entry.getContent(DataHandler)
            documentEntry.hash = ContentUtils.sha1(dataHandler)
            documentEntry.size = ContentUtils.size(dataHandler)
            documentEntry.repositoryUniqueId = '1.19.6.24.109.42.1'
        }
    }
    
    static ProcessorDefinition splitEntries(ProcessorDefinition self, Closure<?> entriesClosure) {
        self.split(new ExpressionAdapter() {
            @Override
            Object evaluate(Exchange exchange) {
                def body = exchange.in.body
                def entries = entriesClosure.call(body)
                entries.collect { entry -> body.clone() + [entry: entry] }
            }
        })
    }

    static ProcessorDefinition assignUuid(ProcessorDefinition self) {
        self.process {
            def entry = it.in.body.entry
            if (!entry.entryUuid.startsWith('urn:uuid:')) {
                def newEntryUuid = 'urn:uuid:' + UUID.randomUUID()
                it.in.body.uuidMap[entry.entryUuid] = newEntryUuid
                entry.entryUuid = newEntryUuid
            }
            null
        }
    }
    
    static ProcessorDefinition changeAssociationUuids(ProcessorDefinition self) {
        self.process {
            def assoc = it.in.body.entry
            def uuidMap = it.in.body.uuidMap
            def sourceUuid = uuidMap[assoc.sourceUuid]
            if (sourceUuid != null) assoc.sourceUuid = sourceUuid
            def targetUuid = uuidMap[assoc.targetUuid]
            if (targetUuid != null) assoc.targetUuid = targetUuid
            null
        }
    }
    
    static ProcessorDefinition status(ProcessorDefinition self, AvailabilityStatus status) {
        self.process {
            it.in.body.entry.availabilityStatus = status
        }
    }

    // Updates the last update time and ensures that the time is actually changed
    static ProcessorDefinition updateTimeStamp(ProcessorDefinition self) {
        self.process {
            it.in.body.entry.lastUpdateTime = new Timestamp(ZonedDateTime.now(), Timestamp.Precision.SECOND)
        }
    }
    
    // Converts entries to ObjectReferences
    static ProcessorDefinition convertToObjectRefs(ProcessorDefinition self, Closure closure) {
        self.process {
            def entries = closure.call(it.in.body)
            it.in.body.resp.references.addAll(entries.collect { 
                new ObjectReference(it.entryUuid)
            })
            entries.clear()
        }
    }
    
    static ProcessorDefinition processBody(ProcessorDefinition self, Closure closure) {
        self.process {
            closure(it.in.body)
        }
    }

    static ProcessorDefinition logExchange(ProcessorDefinition self, Logger log, Closure closure) {
        self.process {
            log.info(closure.call(it).toString())
        }
    }

    private static DataStore dataStore() {
        ContextFacade.getBean(DataStore)
    }

}
