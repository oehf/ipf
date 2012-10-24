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

import java.text.SimpleDateFormat
import org.apache.camel.model.ProcessorDefinition
import org.openehealth.ipf.commons.core.config.ContextFacade;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException
import javax.activation.DataHandler

/**
 * The DSL for the registry and repository route implementations.
 * @author Jens Riemschneider
 */
class RegRepModelExtension {
     
    static ProcessorDefinition store(ProcessorDefinition self) {
        self.process { dataStore().store(it.in.body.entry) }
    }
    
    static ProcessorDefinition retrieve(ProcessorDefinition self) {
        self.transform { 
            [ new RetrievedDocument(dataStore().get(
                it.in.body.documentUniqueId), 
                it.in.body, 
                null, 
                null, 
                'text/plain') 
            ]
        }
    }
    
    static ProcessorDefinition search(ProcessorDefinition self, resultTypes) {
       def answer = new SearchDefinition(resultTypes)
       self.addOutput(answer)
       answer    
    }
    
    static ProcessorDefinition fail(ProcessorDefinition self, message) {
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
    
    static ProcessorDefinition splitEntries(ProcessorDefinition self, entriesClosure) {
        self.ipf().split { exchange ->
            def body = exchange.in.body
            def entries = entriesClosure(body) 
            entries.collect { entry -> body.clone() + [entry: entry] }
        }    
    }

    static ProcessorDefinition assignUuid(ProcessorDefinition self) {
        self.process {
            def entry = it.in.body.entry
            if (!entry.entryUuid.startsWith('urn:uuid:')) {
                def newEntryUuid = 'urn:uuid:' + UUID.randomUUID()
                it.in.body.uuidMap[entry.entryUuid] = newEntryUuid
                entry.entryUuid = newEntryUuid
            }
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
        }
    }
    
    static ProcessorDefinition status(ProcessorDefinition self, status) {
        self.process {
            it.in.body.entry.availabilityStatus = status
        }
    }

    // Updates the last update time and ensures that the time is actually changed
    static ProcessorDefinition updateTimeStamp(ProcessorDefinition self) {
        self.process {
            def formatter = new SimpleDateFormat('yyyyMMddHHmmss')
            def newTime = formatter.format(new Date())
            it.in.body.entry.lastUpdateTime = newTime
        }
    }
    
        // Converts entries to ObjectReferences
    static ProcessorDefinition convertToObjectRefs(ProcessorDefinition self, closure) {
        self.process {
            def entries = closure.call(it.in.body)
            it.in.body.resp.references.addAll(entries.collect { 
                new ObjectReference(it.entryUuid)
            })
            entries.clear()
        }
    }
    
    static ProcessorDefinition processBody(ProcessorDefinition self, closure) {
        self.process {closure(it.in.body)}
    }

    static ProcessorDefinition log(ProcessorDefinition self, log, closure) {
        self.process {log.info(closure.call(it)) }
    }

    private static DataStore dataStore() {
        ContextFacade.getBean(DataStore)
    }
}
