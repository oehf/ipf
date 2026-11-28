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
package org.openehealth.ipf.tutorials.xds.routes;

import jakarta.activation.DataHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Response;
import org.openehealth.ipf.commons.ihe.xds.core.responses.Status;
import org.openehealth.ipf.tutorials.xds.store.ContentUtils;
import org.openehealth.ipf.tutorials.xds.store.Search;
import org.openehealth.ipf.tutorials.xds.store.SearchResult;
import org.openehealth.ipf.tutorials.xds.model.SubmissionContext;

import java.util.List;
import java.util.UUID;

import static org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType.APPEND;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType.HAS_MEMBER;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.AssociationType.TRANSFORM;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus.APPROVED;
import static org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus.DEPRECATED;
import static org.openehealth.ipf.commons.ihe.xds.core.requests.RegisterDocumentSet.supportiveBuilderWith;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.DEPRECATED_OBJ_CANNOT_BE_TRANSFORMED;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.DIFFERENT_HASH_CODE_IN_RESUBMISSION;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.FOLDER_PATIENT_ID_WRONG;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INCORRECT_HASH;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.INCORRECT_SIZE;
import static org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage.UNKNOWN_PATIENT_ID;
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti41RequestValidator;
import static org.openehealth.ipf.platform.camel.ihe.xds.XdsCamelValidators.iti42RequestValidator;
import static org.openehealth.ipf.tutorials.xds.routes.RegRepProcessors.assignUuid;
import static org.openehealth.ipf.tutorials.xds.routes.RegRepProcessors.changeAssociationUuids;
import static org.openehealth.ipf.tutorials.xds.routes.RegRepProcessors.entries;
import static org.openehealth.ipf.tutorials.xds.routes.RegRepProcessors.fail;
import static org.openehealth.ipf.tutorials.xds.routes.RegRepProcessors.logExchange;
import static org.openehealth.ipf.tutorials.xds.routes.RegRepProcessors.makeContentReReadable;
import static org.openehealth.ipf.tutorials.xds.routes.RegRepProcessors.onBody;
import static org.openehealth.ipf.tutorials.xds.routes.RegRepProcessors.status;
import static org.openehealth.ipf.tutorials.xds.routes.RegRepProcessors.store;
import static org.openehealth.ipf.tutorials.xds.routes.RegRepProcessors.updateTimeStamp;
import static org.openehealth.ipf.tutorials.xds.routes.RegRepProcessors.updateWithRepositoryData;
import static org.openehealth.ipf.tutorials.xds.store.SearchResult.ASSOC_SOURCE;
import static org.openehealth.ipf.tutorials.xds.store.SearchResult.DOC_ENTRY;
import static org.openehealth.ipf.tutorials.xds.store.SearchResult.FOLDER;

/**
 * Route builder for ITI-41 and -42.
 * <p>
 * The message body is a {@link SubmissionContext} from the moment the request is validated. Most routes
 * split it into one context per entry and work on that entry.
 *
 * @author Jens Riemschneider
 */
@Slf4j
public class Iti4142RouteBuilder extends RouteBuilder {

    /**
     * The registry the repository forwards ITI-41 submissions to as ITI-42 requests. This tutorial is
     * repository and registry in one, so by default it is the ITI-42 endpoint of this very server; a
     * repository that serves a separate registry points it there.
     */
    @Getter @Setter
    private String registryEndpoint = "xds-iti42://localhost:9091/xds-iti42";

    private static <E, R> Search<SubmissionContext, E, R> search(SearchResult<E, R> result) {
        return Search.search(SubmissionContext.class, result);
    }

    @Override
    public void configure() {
        errorHandler(noErrorHandler());

        // Entry point for Provide and Register Document Set
        from("xds-iti41:xds-iti41")
            .process(logExchange(log, exchange ->
                    "received iti41: " + exchange.getIn().getBody(ProvideAndRegisterDocumentSet.class)))
            // Validate and convert the request
            .process(iti41RequestValidator())
            .transform().body(ProvideAndRegisterDocumentSet.class, SubmissionContext::of)
            // Make the dataHandlers re-readable
            .to("direct:makeDocsReReadable")
            // Further validation based on the registry content
            .to("direct:checkForAssociationToDeprecatedObject", "direct:checkPatientIds", "direct:checkHashAndSize")
            // Store the individual entries contained in the request
            .to("direct:storeDocs")
            .to("direct:updateDocEntriesFromProvide")
            .log("Transform to RegisterDocumentSetRequest")
            // Transform to ITI-42 RegisterDocumentSet Request
            .transform().body(SubmissionContext.class, context -> supportiveBuilderWith(context.getSubmissionSet())
                    .withDocuments(context.getDocumentEntries())
                    .withFolders(context.getFolders())
                    .withAssociations(context.getAssociations())
                    .build())
            .log("Send to ITI-42 endpoint: " + registryEndpoint)
            .to(registryEndpoint);

        // Entry point for Register Document Set
        from("xds-iti42:xds-iti42")
            .process(logExchange(log, exchange ->
                    "received iti42: " + exchange.getIn().getBody(RegisterDocumentSet.class)))
            // Validate and convert the request
            .process(iti42RequestValidator())
            .transform().body(RegisterDocumentSet.class, SubmissionContext::of)
            // Further validation based on the registry content
            .to("direct:checkForAssociationToDeprecatedObject", "direct:checkPatientIds", "direct:checkHash")
            // Store the individual entries contained in the request
            .multicast().to(
                "direct:storeDocEntriesFromRegister",
                "direct:storeFolders",
                "direct:storeSubmissionSet",
                "direct:storeAssociations")
            .end()
            // Create success response, a new one per exchange
            .transform().body(body -> new Response(Status.SUCCESS));

        // Deprecated documents should not be transformed any further
        from("direct:checkForAssociationToDeprecatedObject")
            .split(entries(context -> context.getAssociations().stream()
                    .filter(association -> association.getAssociationType() == APPEND
                            || association.getAssociationType() == TRANSFORM)
                    .toList()))
            .process(search(DOC_ENTRY)
                    .uuid(context -> context.entry(Association.class).getTargetUuid())
                    .hasStatus(DEPRECATED)
                    .into(SubmissionContext::getDeprecatedDocs))
            .split(entries(SubmissionContext::getDeprecatedDocs))
            .process(fail(DEPRECATED_OBJ_CANNOT_BE_TRANSFORMED));

        // All entries in the request must have the same patient ID, no matter if
        // they are only referenced or contained in the request itself. Also check
        // for a patient ID that we shouldn't store documents for.
        from("direct:checkPatientIds")
            .choice()
                .when().body(SubmissionContext.class,
                        context -> "1111111".equals(context.getSubmissionSet().getPatientId().getId()))
                    .process(fail(UNKNOWN_PATIENT_ID))
            .end()
            .process(search(DOC_ENTRY)
                    .referenced(SubmissionContext::getAssociations)
                    .withoutPatientId(context -> context.getSubmissionSet().getPatientId())
                    .into(SubmissionContext::getOtherPatientsEntries))
            .process(search(FOLDER)
                    .referenced(SubmissionContext::getAssociations)
                    .withoutPatientId(context -> context.getSubmissionSet().getPatientId())
                    .into(SubmissionContext::getOtherPatientsEntries))
            .split(entries(SubmissionContext::getOtherPatientsEntries))
            .process(fail(FOLDER_PATIENT_ID_WRONG));

        // Document submissions that specify a size and hash must have correct values
        from("direct:checkHashAndSize")
            .split(entries(SubmissionContext::getDocuments))
            .choice()
                .when().body(SubmissionContext.class, context -> {
                    var document = context.entry(Document.class);
                    var hash = document.getDocumentEntry().getHash();
                    return hash != null && !hash.equals(ContentUtils.sha1(document.getContent(DataHandler.class)));
                })
                    .process(fail(INCORRECT_HASH))
            .end()
            .choice()
                .when().body(SubmissionContext.class, context -> {
                    var document = context.entry(Document.class);
                    var size = document.getDocumentEntry().getSize();
                    return size != null && size != ContentUtils.size(document.getContent(DataHandler.class));
                })
                    .process(fail(INCORRECT_SIZE))
            .end();

        // Resubmitted documents must have the same hash code as the version already in the store
        from("direct:checkHash")
            .split(entries(SubmissionContext::getDocumentEntries))
            .process(search(DOC_ENTRY)
                    .uniqueId(context -> context.entry(DocumentEntry.class).getUniqueId())
                    .withoutHash(context -> context.entry(DocumentEntry.class).getHash())
                    .into(SubmissionContext::getDocsWithOtherHash))
            .split(entries(SubmissionContext::getDocsWithOtherHash))
            .process(fail(DIFFERENT_HASH_CODE_IN_RESUBMISSION));

        // Make documents re-readable, otherwise we lose the content of the stream after the first read
        from("direct:makeDocsReReadable")
            .split(entries(SubmissionContext::getDocuments))
            .process(makeContentReReadable());

        // Put all documents in the store
        from("direct:storeDocs")
            .split(entries(SubmissionContext::getDocuments))
            .process(store());

        // calculate hash + size
        from("direct:updateDocEntriesFromProvide")
            .split(entries(SubmissionContext::getDocuments))
            // Calculate some additional meta data values
            .process(updateWithRepositoryData())
            .process(onBody(SubmissionContext.class,
                    context -> context.setEntry(context.entry(Document.class).getDocumentEntry())));

        // Put all document entries in the store
        from("direct:storeDocEntriesFromRegister")
            .split(entries(SubmissionContext::getDocumentEntries))
            .to("direct:store");

        // Put all folders in the store
        from("direct:storeFolders")
            .split(entries(SubmissionContext::getFolders))
            .process(updateTimeStamp())
            .to("direct:store");

        // Put the submission set in the store
        from("direct:storeSubmissionSet")
            .process(onBody(SubmissionContext.class, context -> context.setEntry(context.getSubmissionSet())))
            .to("direct:store");

        // Finalizes the new entry and stores it
        from("direct:store")
            .process(status(APPROVED))
            .process(assignUuid())
            .process(store());

        // Put all associations in the store
        from("direct:storeAssociations")
            .split(entries(SubmissionContext::getAssociations))
            .process(assignUuid())
            .process(changeAssociationUuids())
            .process(store())
            .multicast().to("direct:checkReplace", "direct:updateTime");

        // Replace associations must deprecate the replaced document and copy
        // the new document into all folders of the original one
        from("direct:checkReplace")
            .choice()
                .when().body(SubmissionContext.class,
                        context -> context.entry(Association.class).getAssociationType().isReplace())
                    .multicast().to("direct:copyFolderMembership", "direct:deprecateTargetDocs").end()
            .end();

        // Copy the new document into all folders of the original one
        from("direct:copyFolderMembership")
            .process(search(ASSOC_SOURCE)
                    .hasMember()
                    .targetUuid(context -> context.entry(Association.class).getTargetUuid())
                    .into(SubmissionContext::getContainers))
            .process(search(FOLDER)
                    .uuids(SubmissionContext::getContainers)
                    .into(SubmissionContext::getFoldersContainingTarget))
            .process(onBody(SubmissionContext.class,
                    context -> context.setReplacingAssociation(context.entry(Association.class))))
            .split(entries(SubmissionContext::getFoldersContainingTarget))
            .process(updateTimeStamp())
            .process(onBody(SubmissionContext.class, context -> context.setEntry(new Association(
                    HAS_MEMBER,
                    "urn:uuid:" + UUID.randomUUID(),
                    context.entry(Folder.class).getEntryUuid(),
                    context.getReplacingAssociation().getSourceUuid()))))
            .process(store());

        // Deprecate all replaced documents
        from("direct:deprecateTargetDocs")
            .process(search(DOC_ENTRY)
                    .uuid(context -> context.entry(Association.class).getTargetUuid())
                    .into(SubmissionContext::getTargetDocs))
            .split(entries(SubmissionContext::getTargetDocs))
            .to("direct:deprecateDocEntry");

        // Deprecate a single replaced document
        from("direct:deprecateDocEntry")
            .process(logExchange(log, exchange -> "deprecating: "
                    + exchange.getIn().getBody(SubmissionContext.class).entry(DocumentEntry.class).getEntryUuid()))
            .process(status(DEPRECATED))
            // Any other transformation or addendum to the deprecated document must
            // be deprecated as well
            .process(search(ASSOC_SOURCE)
                    .targetUuid(context -> context.entry(DocumentEntry.class).getEntryUuid())
                    .isOfTypes(List.of(TRANSFORM, APPEND))
                    .into(SubmissionContext::getTargetUuidsOfDeprecated))
            .process(search(DOC_ENTRY)
                    .uuids(SubmissionContext::getTargetUuidsOfDeprecated)
                    .into(SubmissionContext::getTargetsOfDeprecated))
            .split(entries(SubmissionContext::getTargetsOfDeprecated))
            .to("direct:deprecateDocEntry");

        // Any folders that are related to the association need an update of their time stamp
        from("direct:updateTime")
            .process(search(FOLDER)
                    .uuid(context -> context.entry(Association.class).getSourceUuid())
                    .into(SubmissionContext::getRelatedFolders))
            .split(entries(SubmissionContext::getRelatedFolders))
            .process(updateTimeStamp());
    }
}
