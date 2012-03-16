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
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException
import org.openehealth.ipf.tutorials.xds.ContentUtils
import org.openehealth.ipf.tutorials.xds.SearchDefinition
import javax.activation.DataHandler

/**
 * The DSL for the registry and repository route implementations.
 * @author Jens Riemschneider
 */
class RegRepModelExtension {
    def dataStore
     
    def extensions = {
        ProcessorDefinition.metaClass.store = {
            delegate.process {
                dataStore.store(it.in.body.entry)
            }
        }
        
        ProcessorDefinition.metaClass.retrieve = {
            delegate.transform {
                [ new RetrievedDocument(dataStore.get(it.in.body.documentUniqueId), it.in.body, null, null, 'text/plain') ]
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
                def dataHandler = it.in.body.entry.getContent(DataHandler)
                documentEntry.hash = ContentUtils.sha1(dataHandler)
                documentEntry.size = ContentUtils.size(dataHandler)
                documentEntry.repositoryUniqueId = '1.19.6.24.109.42.1'
            }
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

        ProcessorDefinition.metaClass.processBody = { closure ->  
            delegate.process { closure(it.in.body) }
        }
    
        ProcessorDefinition.metaClass.log = { log, closure -> delegate.process { log.info(closure.call(it)) } }
    }
}
