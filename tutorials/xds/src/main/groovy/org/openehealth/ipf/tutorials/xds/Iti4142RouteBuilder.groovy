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
import org.apache.camel.Expression
import org.apache.camel.Processor
import org.apache.camel.builder.RouteBuilder
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.requests.RegisterDocumentSet
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.activation.DataHandler
import javax.mail.util.ByteArrayDataSource

import static org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType.*
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus.APPROVED
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus.DEPRECATED
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.*
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti41RequestValidator
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti42RequestValidator
import static org.openehealth.ipf.tutorials.xds.SearchResult.*

/**
 * Route builder for ITI-41 and -42.
 * @author Jens Riemschneider
 */
class Iti4142RouteBuilder extends RouteBuilder {
    private final static Logger log = LoggerFactory.getLogger(Iti4142RouteBuilder.class);
    
    @Override
    public void configure() throws Exception {
        errorHandler(noErrorHandler())
        
        // Entry point for Provide and Register Document Set
        from('xds-iti41:xds-iti41')
            .log(log) { 'received iti41: ' + it.in.getBody(ProvideAndRegisterDocumentSet.class) }
            // Validate and convert the request
            .process(iti41RequestValidator())
            .transform ({exchange, type ->
                [ 'req': exchange.in.getBody(ProvideAndRegisterDocumentSet.class), 'uuidMap': [:] ]} as Expression)
            // Make the dataHandlers re-readable
            .to('direct:makeDocsReReadable')
            // Further validation based on the registry content
            .to('direct:checkForAssociationToDeprecatedObject', 'direct:checkPatientIds', 'direct:checkHashAndSize')
            // Store the individual entries contained in the request            
            .multicast().to(
                'direct:storeDocs',
                'direct:storeDocEntriesFromProvide',
                'direct:storeFolders',
                'direct:storeSubmissionSet',
                'direct:storeAssociations')
            .end()
            // Create success response
            .transform ( constant(new Response(Status.SUCCESS)) )

        // Entry point for Register Document Set
        from('xds-iti42:xds-iti42')
            .log(log) { 'received iti42: ' + it.in.getBody(RegisterDocumentSet.class) }
            // Validate and convert the request
            .process(iti42RequestValidator())
            .transform ( {exchange, type ->
                [ 'req': exchange.in.getBody(RegisterDocumentSet.class), 'uuidMap': [:] ]} as Expression
            )
            // Further validation based on the registry content
            .to('direct:checkForAssociationToDeprecatedObject', 'direct:checkPatientIds', 'direct:checkHash')
            // Store the individual entries contained in the request 
            .multicast().to(
                'direct:storeDocEntriesFromRegister',
                'direct:storeFolders',
                'direct:storeSubmissionSet',
                'direct:storeAssociations')
            .end()
            // Create success response
            .transform ( constant(new Response(Status.SUCCESS)) )
            
        // Deprecated documents should not be transformed any further
        from('direct:checkForAssociationToDeprecatedObject')
            .splitEntries { 
                it.req.associations.findAll { assoc -> 
                    assoc.associationType == APPEND || assoc.associationType == TRANSFORM
                } 
            }
            .search(DOC_ENTRY).uuid('entry.targetUuid').status(DEPRECATED).into('deprecatedDocs')
            .splitEntries { it.deprecatedDocs }
            .fail(DEPRECATED_OBJ_CANNOT_BE_TRANSFORMED)

        // All entries in the request must have the same patient ID, no matter if
        // they are only referenced or contained in the request itself. Also check
        // for a patient ID that we shouldn't store documents for.
        from('direct:checkPatientIds')
            .choice().when { it.in.body.req.submissionSet.patientId.id == '1111111' }
                .fail(UNKNOWN_PATIENT_ID)
                .otherwise()
            .end()
            .search([DOC_ENTRY, FOLDER])
                .referenced('req.associations')
                .withoutPatientId('req.submissionSet.patientId')
                .into('otherPatientsEntries')
            .splitEntries { it.otherPatientsEntries }
            .fail(FOLDER_PATIENT_ID_WRONG)

        // Document submissions that specify a size and hash must have correct values
        from('direct:checkHashAndSize')
            .splitEntries { it.req.documents }
            .choice()
                .when {
                    def hash = it.in.body.entry.documentEntry.hash
                    hash != null && hash != ContentUtils.sha1(it.in.body.entry.getContent(DataHandler))
                }.fail(INCORRECT_HASH)
            .end()
            .choice()
                .when {
                    def size = it.in.body.entry.documentEntry.size
                    size != null && size != ContentUtils.size(it.in.body.entry.getContent(DataHandler))
                }.fail(INCORRECT_SIZE)
            .end()

        // Resubmitted documents must have the same hash code as the version already in the store
        from('direct:checkHash')
            .splitEntries { it.req.documentEntries }
            .search(DOC_ENTRY).uniqueId('entry.uniqueId').withoutHash('entry.hash').into('docsWithOtherHash')
            .splitEntries { it.docsWithOtherHash }
            .fail(DIFFERENT_HASH_CODE_IN_RESUBMISSION)

        // Make documents re-readable, otherwise we loose the content of the stream after the first read
        from('direct:makeDocsReReadable')
            .splitEntries { it.req.documents }
            .processBody {
                def dataHandler = it.entry.getContent(DataHandler)
                def content = ContentUtils.getContent(dataHandler)
                it.entry.setContent(DataHandler,
                        new DataHandler(new ByteArrayDataSource(content, dataHandler.contentType)))
            }

        // Put all documents in the store
        from('direct:storeDocs')
            .splitEntries { it.req.documents }
            .store()
        
        // Put all document entries in the store
        from('direct:storeDocEntriesFromProvide')
            .splitEntries { it.req.documents }
            // Calculate some additional meta data values
            .updateWithRepositoryData()
            .processBody { it.entry = it.entry.documentEntry }
            .to('direct:store')
                    
        // Put all document entries in the store
        from('direct:storeDocEntriesFromRegister')
            .splitEntries { it.req.documentEntries }
            .to('direct:store')
            
        // Put all folders in the store
        from('direct:storeFolders')
            .splitEntries { it.req.folders }
            .updateTimeStamp()
            .to('direct:store')
            
        // Put the submission set in the store
        from('direct:storeSubmissionSet')
            .processBody { it.entry = it.req.submissionSet }
            .to('direct:store')

        // Finalizes the new entry and stores it
        from('direct:store')
            .status(APPROVED)
            .assignUuid()
            .store()
                    
        // Put all associations in the store
        from('direct:storeAssociations')
            .splitEntries { it.req.associations }
            .assignUuid()
            .changeAssociationUuids()
            .store()
            .multicast().to('direct:checkReplace', 'direct:updateTime')
            
        // Replace associations must deprecate the replaced document and copy   
        // the new document into all folders of the original one
        from('direct:checkReplace')            
            .choice().when { it.in.body.entry.associationType.isReplace() }
                .multicast().to('direct:copyFolderMembership', 'direct:deprecateTargetDocs').end()
                .otherwise()
            .end()
            
        // Copy the new document into all folders of the original one
        from('direct:copyFolderMembership')
            .search(ASSOC_SOURCE).hasMember().targetUuid('entry.targetUuid').into('containers')
            .search(FOLDER).uuids('containers').into('foldersContainingTarget')
            .processBody { it.assoc = it.entry }
            .splitEntries { it.foldersContainingTarget }
            .updateTimeStamp()
            .processBody {
                it.entry = new Association(HAS_MEMBER, 
                        'urn:uuid:' + UUID.randomUUID(), 
                        it.entry.entryUuid, 
                        it.assoc.sourceUuid)
            }
            .store()
            
        // Deprecate all replaced documents
        from('direct:deprecateTargetDocs')
            .search(DOC_ENTRY).uuid('entry.targetUuid').into('targetDocs')
            .splitEntries { it.targetDocs }
            .to('direct:deprecateDocEntry')        
            
        // Deprecate a single replaced document
        from('direct:deprecateDocEntry')
            .log(log) { 'deprecating: ' + it.in.body.entry.entryUuid }
            .status(DEPRECATED)
            // Any other transformation or addendum to the deprecated document must 
            // be deprecated as well. Clear fields from previous usage.
            .processBody { it.targetUuidsOfDeprecated = [] }
            .processBody { it.targetsOfDeprecated = [] }
            .search(ASSOC_SOURCE)
                .targetUuid('entry.entryUuid')
                .isOfTypes([TRANSFORM, APPEND])    
                .into('targetUuidsOfDeprecated')
            .search(DOC_ENTRY).uuids('targetUuidsOfDeprecated').into('targetsOfDeprecated')
            .splitEntries { it.targetsOfDeprecated }
            .to('direct:deprecateDocEntry')

        // Any folders that are related to the association need an update of their time stamp
        from('direct:updateTime')
            .search(FOLDER).uuid('entry.sourceUuid').into('folders')
            .splitEntries { it.folders }
            .updateTimeStamp()
    }
}
