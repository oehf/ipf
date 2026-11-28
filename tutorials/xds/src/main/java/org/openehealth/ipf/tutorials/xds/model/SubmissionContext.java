/*
 * Copyright 2026 the original author or authors.
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
package org.openehealth.ipf.tutorials.xds.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Association;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Document;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.DocumentEntry;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.Folder;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.SubmissionSet;
import org.openehealth.ipf.commons.ihe.xds.core.metadata.XDSMetaClass;
import org.openehealth.ipf.commons.ihe.xds.core.requests.ProvideAndRegisterDocumentSet;
import org.openehealth.ipf.commons.ihe.xds.core.requests.RegisterDocumentSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Message body of the ITI-41 and ITI-42 routes while a submission is being validated and stored.
 * <p>
 * ITI-41 (Provide and Register) and ITI-42 (Register) submit the same metadata, except that ITI-41 also
 * carries the documents themselves. The two request classes share no common type, so the context holds
 * the parts of the submission instead of the request. That lets the validation and storage routes serve
 * both transactions. For ITI-41, {@link #getDocumentEntries()} holds the entries of the provided
 * documents, which are the same instances, so updating an entry also updates its document.
 * <p>
 * Most routes split the submission and handle one {@link #getEntry() entry} at a time. Each split part
 * gets its own context from {@link #withEntry(Object)}. It shares the submission and the UUID map with
 * its parent, but starts with empty intermediate results, so that the parts of a split cannot see what
 * their siblings found.
 *
 * @author Christian Ohr
 * @since 6.2
 */
@Getter
@ToString(of = {"submissionSet", "entry"})
public class SubmissionContext {

    // ----- The submission, shared by all contexts derived from it

    private final SubmissionSet submissionSet;
    private final List<Folder> folders;
    private final List<DocumentEntry> documentEntries;
    private final List<Association> associations;
    /** The provided documents; empty for ITI-42. */
    private final List<Document> documents;

    /**
     * Maps symbolic entry UUIDs of the submission to the {@code urn:uuid:} UUIDs they are stored under,
     * so that associations can be redirected to the stored entries. Shared by all split parts.
     */
    private final Map<String, String> uuidMap;

    // ----- Per split part

    /**
     * The object this split part handles: a {@link Document}, {@link DocumentEntry}, {@link Folder},
     * {@link SubmissionSet} or {@link Association}, depending on the route. Routes may replace it, for
     * example by the entry of a document.
     */
    @Setter
    private Object entry;

    /**
     * The "replace" association whose target's folder memberships are being copied. This is the only
     * intermediate result that {@link #withEntry(Object)} carries into the split parts, because each part
     * adds the replacing document to one of those folders.
     */
    @Setter
    private Association replacingAssociation;

    // ----- Intermediate search results, fresh for every split part

    /** Deprecated documents an APPEND or TRANSFORM association of the submission points to. */
    private final List<DocumentEntry> deprecatedDocs = new ArrayList<>();

    /** Stored documents and folders referenced by the submission that belong to another patient. */
    private final List<XDSMetaClass> otherPatientsEntries = new ArrayList<>();

    /** Stored documents with the unique ID of a resubmitted document, but with another hash. */
    private final List<DocumentEntry> docsWithOtherHash = new ArrayList<>();

    /** UUIDs of the folders the document replaced by {@link #getEntry()} is a member of. */
    private final List<String> containers = new ArrayList<>();

    /** The folders whose UUIDs are in {@link #getContainers()}. */
    private final List<Folder> foldersContainingTarget = new ArrayList<>();

    /** The documents replaced by the association in {@link #getEntry()}. */
    private final List<DocumentEntry> targetDocs = new ArrayList<>();

    /** UUIDs of documents that transform or append the deprecated document in {@link #getEntry()}. */
    private final List<String> targetUuidsOfDeprecated = new ArrayList<>();

    /** The documents whose UUIDs are in {@link #getTargetUuidsOfDeprecated()}. */
    private final List<DocumentEntry> targetsOfDeprecated = new ArrayList<>();

    /** Stored folders that are the source of the association in {@link #getEntry()}. */
    private final List<Folder> relatedFolders = new ArrayList<>();

    private SubmissionContext(SubmissionSet submissionSet,
                              List<Folder> folders,
                              List<DocumentEntry> documentEntries,
                              List<Association> associations,
                              List<Document> documents,
                              Map<String, String> uuidMap,
                              Object entry,
                              Association replacingAssociation) {
        this.submissionSet = submissionSet;
        this.folders = Objects.requireNonNull(folders, "folders");
        this.documentEntries = Objects.requireNonNull(documentEntries, "documentEntries");
        this.associations = Objects.requireNonNull(associations, "associations");
        this.documents = Objects.requireNonNull(documents, "documents");
        this.uuidMap = uuidMap;
        this.entry = entry;
        this.replacingAssociation = replacingAssociation;
    }

    /**
     * @param request an ITI-41 request
     * @return the context of the submission in the request
     */
    public static SubmissionContext of(ProvideAndRegisterDocumentSet request) {
        var documentEntries = request.getDocuments().stream()
                .map(Document::getDocumentEntry)
                .toList();
        return new SubmissionContext(
                request.getSubmissionSet(),
                request.getFolders(),
                new ArrayList<>(documentEntries),
                request.getAssociations(),
                request.getDocuments(),
                new HashMap<>(),
                null,
                null);
    }

    /**
     * @param request an ITI-42 request
     * @return the context of the submission in the request
     */
    public static SubmissionContext of(RegisterDocumentSet request) {
        return new SubmissionContext(
                request.getSubmissionSet(),
                request.getFolders(),
                request.getDocumentEntries(),
                request.getAssociations(),
                List.of(),
                new HashMap<>(),
                null,
                null);
    }

    /**
     * @param entry the object a split part handles
     * @return a context for that split part, sharing the submission and the UUID map with this one
     */
    public SubmissionContext withEntry(Object entry) {
        return new SubmissionContext(submissionSet, folders, documentEntries, associations, documents,
                uuidMap, entry, replacingAssociation);
    }

    /**
     * @param type the type the current route expects the entry to have
     * @return the entry of this split part
     * @throws ClassCastException if the entry has another type, which means a route was wired wrongly
     */
    public <T> T entry(Class<T> type) {
        return type.cast(entry);
    }
}
