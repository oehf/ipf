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
 
import static org.openehealth.ipf.tutorials.xds.SearchResult.*

import org.apache.camel.model.ProcessorDefinition
import org.openehealth.ipf.platform.camel.ihe.xds.commons.metadata.ObjectReference
import org.openehealth.ipf.tutorials.xds.SearchDefinition
import org.openehealth.ipf.platform.camel.ihe.xds.commons.validate.XDSMetaDataException
import org.openehealth.ipf.platform.camel.ihe.xds.commons.responses.*
import org.openehealth.ipf.tutorials.xds.ContentUtils

import java.text.SimpleDateFormat

/**
 * The DSL for the registry and repository route implementations.
 * @author Jens Riemschneider
 */
class RegRepModelExtension {
    def dataStore
     
    def extensions = {
        ProcessorDefinition.metaClass.log = { log, closure -> delegate.process { log.info(closure.call(it)) } }

        ProcessorDefinition.metaClass.store = {
            delegate.process {
                dataStore.store(it.in.body.entry)
            }
        }
        
        ProcessorDefinition.metaClass.retrieve = {
            delegate.transform {
                [ new RetrievedDocument(dataStore.get(it.in.body.documentUniqueId), it.in.body) ]            
            }
        }
        
        ProcessorDefinition.metaClass.search = { resultTypes ->
            def answer = new SearchDefinition(resultTypes)
            delegate.addOutput(answer)
            answer
        }
        
        ProcessorDefinition.metaClass.fail = { message ->
            delegate.process { 
                throw new XDSMetaDataException(message)
            }
        }

        ProcessorDefinition.metaClass.updateWithRepositoryData = {
            delegate.process {
                def documentEntry = it.in.body.entry.documentEntry
                documentEntry.hash = ContentUtils.sha1(it.in.body.entry.dataHandler)
                documentEntry.size = ContentUtils.size(it.in.body.entry.dataHandler)
                documentEntry.repositoryUniqueId = '1.19.6.24.109.42.1'
            }
        }
        
        ProcessorDefinition.metaClass.processBody = { closure ->  
            delegate.process { closure(it.in.body) }
        }
        
        ProcessorDefinition.metaClass.splitEntries = { entriesClosure ->
            delegate.ipf().split { exchange ->
                def body = exchange.in.body
                def entries = entriesClosure(body) 
                entries.collect { entry -> body.clone() + [entry: entry] }
            }
        }
        
        // Assigns a new UUID. New entries might define this UUID already
        ProcessorDefinition.metaClass.assignUuid = {
            delegate.process {
                def entry = it.in.body.entry
                if (!entry.entryUuid.startsWith('urn:uuid:')) {
                    def newEntryUuid = 'urn:uuid:' + UUID.randomUUID()
                    it.in.body.uuidMap[entry.entryUuid] = newEntryUuid
                    entry.entryUuid = newEntryUuid
                }
            }
        }
        
        // Changes the source and target UUIDs based on the UUID map
        ProcessorDefinition.metaClass.changeAssociationUuids = {
            delegate.process {
                def assoc = it.in.body.entry
                def uuidMap = it.in.body.uuidMap
                def sourceUuid = uuidMap[assoc.sourceUuid]
                if (sourceUuid != null) assoc.sourceUuid = sourceUuid
                def targetUuid = uuidMap[assoc.targetUuid]
                if (targetUuid != null) assoc.targetUuid = targetUuid
            }
        }
        
        // Sets the availability status of an entry
        ProcessorDefinition.metaClass.status = { status ->
            delegate.process {
                it.in.body.entry.availabilityStatus = status
            }
        }
        
        // Updates the last update time and ensures that the time is actually changed
        ProcessorDefinition.metaClass.updateTimeStamp = {
            delegate.process {
                def formatter = new SimpleDateFormat('yyyyMMddHHmmss')
                def newTime = formatter.format(new Date())
                while (newTime == it.in.body.entry.lastUpdateTime) {
                    Thread.sleep(50)
                    newTime = formatter.format(new Date())
                }
                it.in.body.entry.lastUpdateTime = newTime
            }
        }
        
        // Converts entries to ObjectReferences
        ProcessorDefinition.metaClass.convertToObjectRefs = { closure ->
            delegate.process { 
                def entries = closure.call(it.in.body)
                it.in.body.resp.references.addAll(entries.collect { 
                    new ObjectReference(it.entryUuid)
                })
                entries.clear()
            }
        }
    }
}
