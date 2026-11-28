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
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.support.ExpressionAdapter;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.AvailabilityStatus;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.ObjectReference;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Timestamp;
import org.openehealth.ipf.commons.ihe.xds.core.requests.DocumentReference;
import org.openehealth.ipf.commons.ihe.xds.core.responses.RetrievedDocument;
import org.openehealth.ipf.commons.ihe.xds.core.validate.ValidationMessage;
import org.openehealth.ipf.commons.ihe.xds.core.validate.XDSMetaDataException;
import org.slf4j.Logger;
import org.openehealth.ipf.tutorials.xds.store.ByteArrayDataSource;
import org.openehealth.ipf.tutorials.xds.store.ContentUtils;
import org.openehealth.ipf.tutorials.xds.store.DataStore;
import org.openehealth.ipf.tutorials.xds.model.QueryContext;
import org.openehealth.ipf.tutorials.xds.model.SubmissionContext;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.openehealth.ipf.tutorials.xds.store.MetadataEntries.entryUuidOf;
import static org.openehealth.ipf.tutorials.xds.store.MetadataEntries.setEntryUuid;
import static org.openehealth.ipf.tutorials.xds.store.MetadataEntries.setStatus;

/**
 * The route steps of the registry and repository, as processors and expressions to use with
 * {@code .process(...)} and {@code .split(...)}.
 * <p>
 * Most steps work on the {@link SubmissionContext#getEntry() entry} of a {@link SubmissionContext}, after
 * the submission was split with {@link #entries}. Each says which kind of entry it expects, and fails with
 * a {@link ClassCastException} on any other.
 *
 * @author Jens Riemschneider
 * @author Christian Ohr
 * @since 6.2
 */
public final class RegRepProcessors {

    /** The unique ID of the repository of this tutorial. */
    public static final String REPOSITORY_UNIQUE_ID = "1.19.6.24.109.42.1";

    private RegRepProcessors() {
    }

    // ----- Splitting

    /**
     * Splits a submission into one {@link SubmissionContext} per entry.
     *
     * @param entries the entries to split into
     * @return an expression for {@code .split(...)}
     */
    public static Expression entries(Function<? super SubmissionContext, ? extends Collection<?>> entries) {
        return new ExpressionAdapter() {
            @Override
            public Object evaluate(Exchange exchange) {
                var context = exchange.getIn().getBody(SubmissionContext.class);
                return entries.apply(context).stream().map(context::withEntry).toList();
            }
        };
    }

    // ----- Steps on the entry of a submission context

    /** Stores the entry, a metadata entry or a {@link Document}, in the {@link DataStore}. */
    public static Processor store() {
        return exchange -> DataStore.lookup(exchange).store(submission(exchange).getEntry());
    }

    /** Sets the availability status of the entry. */
    public static Processor status(AvailabilityStatus status) {
        return exchange -> setStatus(submission(exchange).getEntry(), status);
    }

    /**
     * Replaces a symbolic entry UUID by a {@code urn:uuid:} UUID, remembering the replacement in the
     * {@link SubmissionContext#getUuidMap() UUID map}, so that {@link #changeAssociationUuids()} can redirect
     * associations to it.
     */
    public static Processor assignUuid() {
        return exchange -> {
            var context = submission(exchange);
            var entryUuid = entryUuidOf(context.getEntry());
            if (!entryUuid.startsWith("urn:uuid:")) {
                var newEntryUuid = "urn:uuid:" + UUID.randomUUID();
                context.getUuidMap().put(entryUuid, newEntryUuid);
                setEntryUuid(context.getEntry(), newEntryUuid);
            }
        };
    }

    /** Redirects the source and target of the {@link Association} to the UUIDs {@link #assignUuid()} assigned. */
    public static Processor changeAssociationUuids() {
        return exchange -> {
            var context = submission(exchange);
            var association = context.entry(Association.class);
            var sourceUuid = context.getUuidMap().get(association.getSourceUuid());
            if (sourceUuid != null) {
                association.setSourceUuid(sourceUuid);
            }
            var targetUuid = context.getUuidMap().get(association.getTargetUuid());
            if (targetUuid != null) {
                association.setTargetUuid(targetUuid);
            }
        };
    }

    /** Sets the last update time of the {@link Folder} to now. */
    public static Processor updateTimeStamp() {
        return exchange -> submission(exchange).entry(Folder.class)
                .setLastUpdateTime(new Timestamp(ZonedDateTime.now(), Timestamp.Precision.SECOND));
    }

    /** Sets the metadata the repository is responsible for on the entry of the {@link Document}. */
    public static Processor updateWithRepositoryData() {
        return exchange -> {
            var document = submission(exchange).entry(Document.class);
            var documentEntry = document.getDocumentEntry();
            var dataHandler = document.getContent(DataHandler.class);
            documentEntry.setHash(ContentUtils.sha1(dataHandler));
            documentEntry.setSize(ContentUtils.size(dataHandler));
            documentEntry.setRepositoryUniqueId(REPOSITORY_UNIQUE_ID);
        };
    }

    /**
     * Reads the content of the {@link Document} into memory, so that it can be read more than once. The
     * content arrives as a stream, which the first reader would otherwise consume.
     */
    public static Processor makeContentReReadable() {
        return exchange -> {
            var document = submission(exchange).entry(Document.class);
            var dataHandler = document.getContent(DataHandler.class);
            var content = ContentUtils.getContent(dataHandler);
            document.setContent(DataHandler.class,
                    new DataHandler(new ByteArrayDataSource(content, dataHandler.getContentType())));
        };
    }

    // ----- Other steps

    /** Replaces a {@link DocumentReference} in the message body by the document it refers to. */
    public static Processor retrieve() {
        return exchange -> {
            var reference = exchange.getIn().getMandatoryBody(DocumentReference.class);
            var dataHandler = DataStore.lookup(exchange).get(reference.getDocumentUniqueId());
            exchange.getIn().setBody(new RetrievedDocument(dataHandler, reference, null, null, "text/plain"));
        };
    }

    /**
     * Moves the given results of a query to the object references of the response.
     *
     * @param results the documents, folders, submission sets or associations of the response
     */
    public static Processor toObjectRefs(Function<? super QueryContext, ? extends Collection<?>> results) {
        return exchange -> {
            var context = exchange.getIn().getMandatoryBody(QueryContext.class);
            var entries = results.apply(context);
            entries.forEach(entry -> context.getResp().getReferences().add(new ObjectReference(entryUuidOf(entry))));
            entries.clear();
        };
    }

    /** Fails the transaction with the given error. */
    public static Processor fail(ValidationMessage message) {
        return exchange -> {
            throw new XDSMetaDataException(message);
        };
    }

    /**
     * Runs an action on the message body.
     *
     * @param type   the type of the body
     * @param action what to do with it
     */
    public static <C> Processor onBody(Class<C> type, Consumer<? super C> action) {
        return exchange -> action.accept(exchange.getIn().getMandatoryBody(type));
    }

    /** Logs a message derived from the exchange at INFO level. */
    public static Processor logExchange(Logger log, Function<? super Exchange, ?> message) {
        return exchange -> log.info(String.valueOf(message.apply(exchange)));
    }

    private static SubmissionContext submission(Exchange exchange) throws Exception {
        return exchange.getIn().getMandatoryBody(SubmissionContext.class);
    }
}
